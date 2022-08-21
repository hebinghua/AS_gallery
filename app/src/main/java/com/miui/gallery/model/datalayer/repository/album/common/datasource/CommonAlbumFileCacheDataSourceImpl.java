package com.miui.gallery.model.datalayer.repository.album.common.datasource;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import com.google.gson.reflect.TypeToken;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.utils.AlbumFileCache;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.cache.AlbumCacheItem;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.util.RxUtils;
import io.reactivex.Flowable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class CommonAlbumFileCacheDataSourceImpl implements ICommonAlbumDataSource {
    public final Object CACHE_LOCK = new Object();
    public List<AlbumCacheItem> mFileCacheList;

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 4;
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource
    public Flowable<List<Album>> queryAlbums(final long j, final QueryParam queryParam) {
        return Flowable.fromCallable(new Callable<List<Album>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumFileCacheDataSourceImpl.1
            @Override // java.util.concurrent.Callable
            public List<Album> call() throws Exception {
                List<AlbumCacheItem> cache = CommonAlbumFileCacheDataSourceImpl.this.getCache();
                String[] bindArgs = queryParam.getBindArgs();
                String selection = queryParam.getSelection();
                if (cache != null && !cache.isEmpty()) {
                    String concatenateWhere = DatabaseUtils.concatenateWhere("query_flags = ?", selection);
                    if (bindArgs != null) {
                        System.arraycopy(bindArgs, 0, bindArgs, 1, bindArgs.length + 1);
                        bindArgs[0] = String.valueOf(j);
                    } else {
                        bindArgs = new String[]{String.valueOf(j)};
                    }
                    QueryParam queryParam2 = new QueryParam(queryParam.isDistinct(), queryParam.getColumns(), concatenateWhere, bindArgs, queryParam.getGroupBy(), queryParam.getHaving(), queryParam.getOrderBy(), queryParam.getLimit(), queryParam.getExtra());
                    AlbumCacheItem.Generator generator = new AlbumCacheItem.Generator();
                    ContentValues contentValues = new ContentValues(1);
                    for (int i = 0; i < cache.size(); i++) {
                        AlbumCacheItem albumCacheItem = cache.get(i);
                        long attributes = AlbumCacheManager.getInstance().getAttributes(albumCacheItem.getId());
                        if (attributes != albumCacheItem.getAttributes()) {
                            contentValues.put("attributes", Long.valueOf(attributes));
                            generator.update(albumCacheItem, contentValues);
                        }
                    }
                    return (List) AlbumCacheManager.getInstance().filterFrom(cache, queryParam2).stream().map(new Function<AlbumCacheItem, Album>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumFileCacheDataSourceImpl.1.1
                        @Override // java.util.function.Function
                        public Album apply(AlbumCacheItem albumCacheItem2) {
                            return albumCacheItem2.transform();
                        }
                    }).collect(Collectors.toList());
                }
                return CollectionUtils.emptyList();
            }
        }).compose(RxUtils.emptyListCheck());
    }

    public List<AlbumCacheItem> getCache() {
        List<AlbumCacheItem> list = this.mFileCacheList;
        if (list == null || list.isEmpty()) {
            synchronized (this.CACHE_LOCK) {
                List<AlbumCacheItem> list2 = this.mFileCacheList;
                if (list2 == null || list2.isEmpty()) {
                    this.mFileCacheList = (List) AlbumFileCache.getInstance().getCache(AlbumFileCache.AlbumCacheType.COMMON, "com_cache_albums", new TypeToken<List<AlbumCacheItem>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumFileCacheDataSourceImpl.2
                    }.getType());
                }
            }
        }
        return this.mFileCacheList;
    }

    public void saveCache(List<AlbumCacheItem> list) {
        AlbumFileCache.getInstance().saveCache(AlbumFileCache.AlbumCacheType.COMMON, "com_cache_albums", list);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource
    public Flowable<String> queryAlbumNameById(long j) {
        throw new IllegalStateException("the data source not support this method");
    }
}
