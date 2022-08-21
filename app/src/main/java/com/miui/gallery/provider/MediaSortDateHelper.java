package com.miui.gallery.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* loaded from: classes2.dex */
public class MediaSortDateHelper {

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final SortDateProvider INSTANCE = MediaSortDateHelper.createSortDateProvider();
    }

    /* loaded from: classes2.dex */
    public enum SortDate {
        CREATE_TIME,
        MODIFY_TIME
    }

    /* loaded from: classes2.dex */
    public interface SortDateProvider {
        List<Long> getAlbumIdsBySortDate(SortDate sortDate);

        List<String> getAlbumPathsByCoverSortDate(SortDate sortDate);

        List<String> getAlbumPathsBySortDate(SortDate sortDate);

        SortDate getDefaultSortDate();

        SortDate getSortDateByAlbumPath(String str);
    }

    public static SortDateProvider getSortDateProvider() {
        return SingletonHolder.INSTANCE;
    }

    public static SortDateProvider createSortDateProvider() {
        return new DefaultSortDateProvider();
    }

    /* loaded from: classes2.dex */
    public static class DefaultSortDateProvider implements SortDateProvider {
        public static final SortDate DEFAULT_SORT_DATE = SortDate.MODIFY_TIME;
        public HashSet<Long> mAlbumIds;
        public final List<String> mAlbums;
        public final List<String> mFixedAlbums;
        public final Object mLock = new Object();

        public DefaultSortDateProvider() {
            List localPaths = MediaSortDateHelper.getLocalPaths(AlbumDataHelper.getUserCreateLocalPath().toLowerCase());
            int size = localPaths != null ? localPaths.size() + 3 : 3;
            ArrayList arrayList = new ArrayList(3);
            this.mFixedAlbums = arrayList;
            arrayList.add(AlbumDataHelper.getScreenshotsLocalPath().toLowerCase());
            arrayList.add(AlbumDataHelper.getCameraLocalPath().toLowerCase());
            arrayList.add(AlbumDataHelper.getScreenRecorderLocalPath().toLowerCase());
            ArrayList arrayList2 = new ArrayList(size);
            this.mAlbums = arrayList2;
            arrayList2.addAll(arrayList);
            if (localPaths == null || localPaths.isEmpty()) {
                return;
            }
            arrayList2.addAll(localPaths);
        }

        @Override // com.miui.gallery.provider.MediaSortDateHelper.SortDateProvider
        public SortDate getSortDateByAlbumPath(String str) {
            return (TextUtils.isEmpty(str) || !this.mAlbums.contains(str.toLowerCase())) ? DEFAULT_SORT_DATE : SortDate.CREATE_TIME;
        }

        @Override // com.miui.gallery.provider.MediaSortDateHelper.SortDateProvider
        public List<String> getAlbumPathsBySortDate(SortDate sortDate) {
            if (sortDate == SortDate.CREATE_TIME) {
                return new ArrayList(this.mAlbums);
            }
            return new ArrayList();
        }

        @Override // com.miui.gallery.provider.MediaSortDateHelper.SortDateProvider
        public List<String> getAlbumPathsByCoverSortDate(SortDate sortDate) {
            if (sortDate == SortDate.CREATE_TIME) {
                return new ArrayList(this.mFixedAlbums);
            }
            return new ArrayList();
        }

        @Override // com.miui.gallery.provider.MediaSortDateHelper.SortDateProvider
        public List<Long> getAlbumIdsBySortDate(SortDate sortDate) {
            if (this.mAlbumIds == null) {
                synchronized (this.mLock) {
                    Set<String> cachedSortByCreateTimeAlbumIds = GalleryPreferences.Album.getCachedSortByCreateTimeAlbumIds();
                    if (BaseMiscUtil.isValid(cachedSortByCreateTimeAlbumIds)) {
                        this.mAlbumIds = new HashSet<>(cachedSortByCreateTimeAlbumIds.size());
                        for (String str : cachedSortByCreateTimeAlbumIds) {
                            this.mAlbumIds.add(Long.valueOf(str));
                        }
                    }
                    updateCacheByFixedAndPathPrefix(AlbumDataHelper.getUserCreateLocalPath().toLowerCase());
                }
            }
            if (BaseMiscUtil.isValid(this.mAlbumIds) && sortDate == SortDate.CREATE_TIME) {
                return new ArrayList(this.mAlbumIds);
            }
            return new ArrayList();
        }

        @Override // com.miui.gallery.provider.MediaSortDateHelper.SortDateProvider
        public SortDate getDefaultSortDate() {
            return DEFAULT_SORT_DATE;
        }

        public final void updateCacheByFixedAndPathPrefix(String str) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            Uri uri = GalleryContract.Album.URI;
            String[] strArr = {j.c};
            String format = String.format(Locale.US, "(%s COLLATE NOCASE IN ('%s')) or %s COLLATE NOCASE like ?", "localPath", TextUtils.join("', '", this.mFixedAlbums), "localPath");
            HashSet hashSet = (HashSet) SafeDBUtil.safeQuery(sGetAndroidContext, uri, strArr, format, new String[]{str + "%"}, (String) null, new SafeDBUtil.QueryHandler<HashSet<String>>() { // from class: com.miui.gallery.provider.MediaSortDateHelper.DefaultSortDateProvider.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public HashSet<String> mo1808handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    HashSet<String> hashSet2 = new HashSet<>(cursor.getCount());
                    do {
                        hashSet2.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                    return hashSet2;
                }
            });
            if (hashSet != null) {
                GalleryPreferences.Album.setCachedSortByCreateTimeAlbumIds(hashSet);
            }
        }
    }

    public static List<String> getLocalPaths(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        String format = String.format(Locale.US, "%s COLLATE NOCASE like ?", "localPath");
        List<String> list = (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Album.URI, new String[]{"localPath"}, format, new String[]{str + "%"}, (String) null, new SafeDBUtil.QueryHandler<List<String>>() { // from class: com.miui.gallery.provider.MediaSortDateHelper.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<String> mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                ArrayList arrayList = new ArrayList(cursor.getCount());
                do {
                    arrayList.add(cursor.getString(0).toLowerCase());
                } while (cursor.moveToNext());
                return arrayList;
            }
        });
        DefaultLogger.d("MediaSortDateHelper", "query album localPath costs: %sms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        if (list != null) {
            DefaultLogger.d("MediaSortDateHelper", "query album localPath list size=%s", Integer.valueOf(list.size()));
        }
        return list;
    }
}
