package com.miui.gallery.model.datalayer.repository.album.ai;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource;
import com.miui.gallery.model.datalayer.repository.album.ai.datasource.IAIAlbumDBDataSource;
import com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource;
import com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.IAIAlbumDataCacheSource;
import com.miui.gallery.model.datalayer.utils.CacheBean;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import java.util.List;

/* loaded from: classes2.dex */
public class AIAlbumModelImpl implements IAIAlbumModel {
    public IAIAlbumDataCacheSource mCacheSource = new AIAlbumCacheDataSource();
    public IAIAlbumDBDataSource mDBSource;

    public AIAlbumModelImpl(Context context) {
        this.mDBSource = new AIAlbumDBDataSource(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<PageResults<CoverList>> queryAIAlbumCover(Integer num, Integer num2, Integer num3) {
        return Flowable.concatArrayDelayError(PageResults.wrapperDataToPageResult(4, this.mCacheSource.queryAIAlbumCover(num, num2, num3)), PageResults.wrapperDataToPageResult(2, this.mDBSource.queryAIAlbumCover(num, num2, num3).doAfterNext(new Consumer<CoverList>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.AIAlbumModelImpl.1
            @Override // io.reactivex.functions.Consumer
            public void accept(CoverList coverList) throws Exception {
                AIAlbumModelImpl.this.mCacheSource.saveCache(new CacheBean(System.currentTimeMillis(), coverList, "cache_ai_cover"));
            }
        })));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<List<PeopleItem>> queryPersons(int i, final boolean z) {
        return Flowable.mergeArrayDelayError(this.mCacheSource.queryPersons(i).mergeWith(this.mDBSource.queryPeopleFaceSnapList(i).subscribeOn(RxGalleryExecutors.getInstance().getUserThreadScheduler())).elementAtOrError(0L).subscribeOn(RxGalleryExecutors.getInstance().getUserThreadScheduler()).toFlowable(), this.mDBSource.queryPersons(i, z).doAfterNext(new Consumer<List<PeopleItem>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.AIAlbumModelImpl.2
            @Override // io.reactivex.functions.Consumer
            public void accept(List<PeopleItem> list) throws Exception {
                if (!z) {
                    AIAlbumModelImpl.this.mCacheSource.saveCache(new CacheBean(System.currentTimeMillis(), list, "cache_face"));
                }
            }
        }).subscribeOn(RxGalleryExecutors.getInstance().getUserThreadScheduler()));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<List<SuggestionData>> queryTagsAlbum(Integer num) {
        return Flowable.concatArrayDelayError(this.mCacheSource.queryTagsAlbum(num), this.mDBSource.queryTagsAlbum(num).doAfterNext(new Consumer<List<SuggestionData>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.AIAlbumModelImpl.3
            @Override // io.reactivex.functions.Consumer
            public void accept(List<SuggestionData> list) throws Exception {
                AIAlbumModelImpl.this.mCacheSource.saveCache(new CacheBean(System.currentTimeMillis(), list, "cache_tags"));
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<List<SuggestionData>> queryLocationsAlbum(Integer num) {
        return Flowable.concatArrayDelayError(this.mCacheSource.queryLocationsAlbum(num), this.mDBSource.queryLocationsAlbum(num).doAfterNext(new Consumer<List<SuggestionData>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.AIAlbumModelImpl.4
            @Override // io.reactivex.functions.Consumer
            public void accept(List<SuggestionData> list) throws Exception {
                AIAlbumModelImpl.this.mCacheSource.saveCache(new CacheBean(System.currentTimeMillis(), list, "cache_location"));
            }
        }));
    }
}
