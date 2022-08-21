package com.miui.gallery.model.datalayer.repository.album.common.datasource;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.provider.cache.CacheManager;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class AlbumDataSourceHelper {
    public static final String[] CLOUD_ONLY_ID_MODE_PROJECTION = {j.c, InternalContract$Album.ALIAS_COVER_PATH, "localGroupId", "serverStatus", "localFlag"};
    public final UriMatcher mUriMatcher;

    public AlbumDataSourceHelper() {
        UriMatcher uriMatcher = new UriMatcher(-1);
        this.mUriMatcher = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "media", 1);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "gallery_cloud", 2);
    }

    public static AlbumDataSourceHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumDataSourceHelper INSTANCE = new AlbumDataSourceHelper();
    }

    public List<BaseAlbumCover> queryRecentPhotosInAlbum(Context context, Uri uri, Long[] lArr, boolean z, int i, int i2) {
        if (lArr == null || lArr.length == 0) {
            return CollectionUtils.emptyList();
        }
        Cursor cursor = null;
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            try {
                acquire.append(String.format("(localGroupId IN (%s)  AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) ) ", TextUtils.join(",", lArr)));
                if (GalleryPreferences.LocalMode.isOnlyShowLocalPhoto()) {
                    acquire.append(" AND ");
                    acquire.append(InternalContract$Cloud.ALIAS_LOCAL_MEDIA);
                }
                ArrayList arrayList = new ArrayList(i);
                String str = i2 != 1 ? i2 != 2 ? i2 != 3 ? "dateModified DESC " : "dateModified ASC " : "dateTaken DESC " : "dateTaken ASC ";
                int match = this.mUriMatcher.match(uri);
                if (match != 1) {
                    if (match == 2 && (cursor = CloudUtils.queryAlbumPhotos(context, uri, CLOUD_ONLY_ID_MODE_PROJECTION, acquire.toString(), null, str, i)) != null && cursor.moveToFirst()) {
                        int i3 = 0;
                        do {
                            BaseAlbumCover baseAlbumCover = new BaseAlbumCover();
                            baseAlbumCover.id = cursor.getLong(cursor.getColumnIndex("localGroupId"));
                            baseAlbumCover.coverId = cursor.getLong(cursor.getColumnIndex(j.c));
                            baseAlbumCover.coverPath = cursor.getString(cursor.getColumnIndex("coverPath"));
                            baseAlbumCover.coverUri = Album.getCoverUri(cursor.getInt(cursor.getColumnIndex("serverStatus")), baseAlbumCover.coverId);
                            arrayList.add(baseAlbumCover);
                            i3++;
                            if (!cursor.moveToNext()) {
                                break;
                            }
                        } while (i3 < i);
                    }
                } else {
                    for (MediaCacheItem mediaCacheItem : (List) MediaManager.getInstance().query(CLOUD_ONLY_ID_MODE_PROJECTION, acquire.toString(), null, str, null, String.valueOf(i), null, new CacheManager.ResultConverter<MediaCacheItem, List<MediaCacheItem>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.datasource.AlbumDataSourceHelper.1
                        @Override // com.miui.gallery.provider.cache.CacheManager.ResultConverter
                        public List<MediaCacheItem> convert(String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, Bundle bundle, List<MediaCacheItem> list) {
                            return list;
                        }
                    })) {
                        if (mediaCacheItem.getAlbumId() != null && !TextUtils.isEmpty(mediaCacheItem.getAliasClearThumbnail())) {
                            BaseAlbumCover baseAlbumCover2 = new BaseAlbumCover();
                            baseAlbumCover2.id = mediaCacheItem.getAlbumId().longValue();
                            baseAlbumCover2.coverId = mediaCacheItem.getId();
                            baseAlbumCover2.coverPath = mediaCacheItem.getAliasClearThumbnail();
                            baseAlbumCover2.coverUri = Album.getCoverUri(mediaCacheItem.getAliasSyncState().intValue(), baseAlbumCover2.coverId);
                            arrayList.add(baseAlbumCover2);
                        }
                    }
                }
                if (arrayList.isEmpty() && !z) {
                    for (int i4 = 0; i4 < lArr.length && i4 < i; i4++) {
                        BaseAlbumCover baseAlbumCover3 = new BaseAlbumCover();
                        baseAlbumCover3.id = lArr[i4].longValue();
                        arrayList.add(baseAlbumCover3);
                    }
                }
                return arrayList;
            } catch (Exception e) {
                e.printStackTrace();
                BaseMiscUtil.closeSilently(cursor);
                Pools.getStringBuilderPool().release(acquire);
                return CollectionUtils.emptyList();
            }
        } finally {
            BaseMiscUtil.closeSilently(cursor);
            Pools.getStringBuilderPool().release(acquire);
        }
    }

    public List<BaseAlbumCover> queryRecentPhotosInAlbum(Context context, Uri uri, List<Album> list, boolean z, int i, int i2) {
        if (list == null || list.isEmpty()) {
            return CollectionUtils.emptyList();
        }
        int size = list.size();
        Long[] lArr = new Long[size];
        ArrayMap arrayMap = new ArrayMap(size);
        for (int i3 = 0; i3 < size; i3++) {
            Album album = list.get(i3);
            lArr[i3] = Long.valueOf(album.getAlbumId());
            arrayMap.put(Long.valueOf(album.getAlbumId()), album.getAlbumName());
        }
        List<BaseAlbumCover> queryRecentPhotosInAlbum = queryRecentPhotosInAlbum(context, uri, lArr, z, i, i2);
        for (BaseAlbumCover baseAlbumCover : queryRecentPhotosInAlbum) {
            baseAlbumCover.albumName = (String) arrayMap.get(Long.valueOf(baseAlbumCover.id));
        }
        return queryRecentPhotosInAlbum;
    }
}
