package com.miui.gallery.model.datalayer.repository.album.ai.datasource.local;

import com.google.gson.reflect.TypeToken;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.base_optimization.util.ExceptionUtils;
import com.miui.gallery.model.datalayer.utils.AlbumFileCache;
import com.miui.gallery.model.datalayer.utils.CacheBean;
import com.miui.gallery.model.dto.AIAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.FaceAlbumCover;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Flowable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class AIAlbumCacheDataSource implements IAIAlbumDataCacheSource {
    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.IAIAlbumDataCacheSource
    public Flowable<CoverList> queryAIAlbumCover(final Integer num, final Integer num2, final Integer num3) {
        return Flowable.fromCallable(new Callable<CoverList>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public CoverList mo1116call() throws Exception {
                Type type = new TypeToken<List<? extends AIAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.1.1
                }.getType();
                List list = (List) AIAlbumCacheDataSource.this.getCache("ai_cover_face", new TypeToken<List<FaceAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.1.2
                }.getType());
                List list2 = (List) AIAlbumCacheDataSource.this.getCache("ai_cover_location", type);
                List list3 = (List) AIAlbumCacheDataSource.this.getCache("ai_cover_tag", type);
                ArrayList arrayList = new ArrayList(num.intValue() + num2.intValue() + num3.intValue());
                if (list != null) {
                    arrayList.addAll(list);
                }
                if (list2 != null) {
                    arrayList.addAll(list2);
                }
                if (list3 != null) {
                    arrayList.addAll(list3);
                }
                return new CoverList(arrayList);
            }
        }).onErrorReturnItem(new CoverList());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.IAIAlbumDataCacheSource
    public Flowable<List<PeopleItem>> queryPersons(int i) {
        return Flowable.fromCallable(new Callable<List<PeopleItem>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.2
            @Override // java.util.concurrent.Callable
            public List<PeopleItem> call() throws Exception {
                DefaultLogger.d("AIAlbumLocalCacheDataSource", "start query cache queryPersons:" + System.currentTimeMillis());
                CacheBean cacheBean = (CacheBean) AIAlbumCacheDataSource.this.getCache("cache_face", new TypeToken<CacheBean<List<PeopleItem>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.2.1
                }.getType());
                if (cacheBean == null) {
                    return CollectionUtils.emptyList();
                }
                DefaultLogger.d("AIAlbumLocalCacheDataSource", "end query cache  queryPersons:" + System.currentTimeMillis());
                return (List) cacheBean.getCacheDatas();
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.IAIAlbumDataCacheSource
    public Flowable<List<SuggestionData>> queryTagsAlbum(final Integer num) {
        return Flowable.fromCallable(new Callable<List<SuggestionData>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.3
            @Override // java.util.concurrent.Callable
            public List<SuggestionData> call() throws Exception {
                DefaultLogger.v("AIAlbumLocalCacheDataSource", "start queryTagsAlbum cache,limit: %d", num);
                CacheBean cacheBean = (CacheBean) AIAlbumCacheDataSource.this.getCache("cache_tags", new TypeToken<CacheBean<List<SuggestionData>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.3.1
                }.getType());
                if (cacheBean == null) {
                    DefaultLogger.v("AIAlbumLocalCacheDataSource", "no SuggestionTagsPhoto cache");
                    return CollectionUtils.emptyList();
                }
                return (List) cacheBean.getCacheDatas();
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.IAIAlbumDataCacheSource
    public Flowable<List<SuggestionData>> queryLocationsAlbum(Integer num) {
        return Flowable.fromCallable(new Callable<List<SuggestionData>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.4
            @Override // java.util.concurrent.Callable
            public List<SuggestionData> call() throws Exception {
                CacheBean cacheBean = (CacheBean) AIAlbumCacheDataSource.this.getCache("cache_location", new TypeToken<CacheBean<List<SuggestionData>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.AIAlbumCacheDataSource.4.1
                }.getType());
                if (cacheBean == null) {
                    return CollectionUtils.emptyList();
                }
                return (List) cacheBean.getCacheDatas();
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.local.IAIAlbumDataCacheSource
    public void saveCache(CacheBean cacheBean) {
        try {
            if (cacheBean.getCacheDatas() instanceof CoverList) {
                internalSaveAIAlbumCoverList((CoverList) cacheBean.getCacheDatas());
            } else {
                AlbumFileCache.getInstance().saveCache(AlbumFileCache.AlbumCacheType.AI, cacheBean.getKey(), cacheBean);
            }
        } catch (Exception e) {
            DefaultLogger.e("AIAlbumLocalCacheDataSource", ExceptionUtils.getStackTraceString(e));
        }
    }

    public final void internalSaveAIAlbumCoverList(CoverList<AIAlbumCover> coverList) throws JSONException {
        List<AIAlbumCover> covers = coverList.getCovers();
        if (covers == null || covers.isEmpty()) {
            return;
        }
        AIAlbumCover aIAlbumCover = covers.get(0);
        AlbumFileCache albumFileCache = AlbumFileCache.getInstance();
        AlbumFileCache.AlbumCacheType albumCacheType = AlbumFileCache.AlbumCacheType.AI;
        String str = "ai_cover_location";
        if (!aIAlbumCover.type.equals(str)) {
            str = aIAlbumCover.type.equals("ai_cover_tag") ? "ai_cover_tag" : "ai_cover_face";
        }
        albumFileCache.saveCache(albumCacheType, str, covers);
    }

    public final <T> T getCache(String str, Type type) {
        return (T) AlbumFileCache.getInstance().getCache(AlbumFileCache.AlbumCacheType.AI, str, type);
    }
}
