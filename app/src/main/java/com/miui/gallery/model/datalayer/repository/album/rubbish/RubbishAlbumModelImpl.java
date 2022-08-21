package com.miui.gallery.model.datalayer.repository.album.rubbish;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.GalleryDataRepositoryConfig$AlbumRepositoryConfig$RubbishAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import com.miui.gallery.util.MediaAndAlbumOperations;
import io.reactivex.Flowable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class RubbishAlbumModelImpl implements IRubbishAlbumModel {
    public final WeakReference<Context> mContext;
    public final IRubbishAlbumDataSource[] mDataSources;

    public RubbishAlbumModelImpl(Context context) {
        this.mContext = new WeakReference<>(context);
        this.mDataSources = GalleryDataRepositoryConfig$AlbumRepositoryConfig$RubbishAlbumModel.getDataSources(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<PageResults<List<Album>>> queryRubbishAlbum(final Integer num) {
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(this.mDataSources).map(new Function<IRubbishAlbumDataSource, Flowable<PageResults<List<Album>>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.2
            @Override // java.util.function.Function
            public Flowable<PageResults<List<Album>>> apply(IRubbishAlbumDataSource iRubbishAlbumDataSource) {
                return PageResults.wrapperDataToPageResult(iRubbishAlbumDataSource.getSourceType(), iRubbishAlbumDataSource.queryRubbishAlbum(num));
            }
        }).toArray(new IntFunction<Flowable<PageResults<List<Album>>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.1
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<List<Album>>>[] apply(int i) {
                return new Flowable[i];
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<PageResults<CoverList>> queryRubbishAlbumsAllPhoto(final Integer num) {
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(this.mDataSources).map(new Function<IRubbishAlbumDataSource, Flowable<PageResults<CoverList>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.4
            @Override // java.util.function.Function
            public Flowable<PageResults<CoverList>> apply(IRubbishAlbumDataSource iRubbishAlbumDataSource) {
                return PageResults.wrapperDataToPageResult(iRubbishAlbumDataSource.getSourceType(), iRubbishAlbumDataSource.queryRubbishAlbumsAllPhoto(num).map(new io.reactivex.functions.Function<List<BaseAlbumCover>, CoverList>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.4.1
                    @Override // io.reactivex.functions.Function
                    /* renamed from: apply  reason: avoid collision after fix types in other method */
                    public CoverList mo2564apply(List<BaseAlbumCover> list) throws Exception {
                        return new CoverList(list);
                    }
                }));
            }
        }).toArray(new IntFunction<Flowable<PageResults<CoverList>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.3
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<CoverList>>[] apply(int i) {
                return new Flowable[i];
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<Boolean> doChangeAlbumShowInRubbishPage(final boolean z, final long[] jArr) {
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1123call() throws Exception {
                MediaAndAlbumOperations.doChangeShowInRubbishAlbums((Context) RubbishAlbumModelImpl.this.mContext.get(), jArr, z, false);
                return Boolean.TRUE;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<RubbishAlbumManualHideResult> doAddNoMediaForRubbishAlbum(final List<String> list) {
        return Flowable.fromCallable(new Callable<RubbishAlbumManualHideResult>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public RubbishAlbumManualHideResult mo1124call() throws Exception {
                return MediaAndAlbumOperations.doAddNoMediaForRubbishAlbum(list);
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<RubbishAlbumManualHideResult> doRemoveNoMediaForRubbishAlbum(final List<String> list) {
        return Flowable.fromCallable(new Callable<RubbishAlbumManualHideResult>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public RubbishAlbumManualHideResult mo1125call() throws Exception {
                return MediaAndAlbumOperations.doRemoveNoMediaForRubbishAlbum(list);
            }
        });
    }
}
