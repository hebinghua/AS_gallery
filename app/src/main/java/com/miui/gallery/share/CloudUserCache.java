package com.miui.gallery.share;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.share.AlbumShareUIManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class CloudUserCache extends DBCache<String, List<CloudUserCacheEntry>> {
    public static final List<CloudUserCacheEntry> EMPTY_ENTRY_LIST = Lists.newArrayList();
    public static final CloudUserCache sOwnerUserCache = new CloudUserCache() { // from class: com.miui.gallery.share.CloudUserCache.1
        @Override // com.miui.gallery.share.CloudUserCache
        public String getSelection() {
            return "serverStatus = ?";
        }

        @Override // com.miui.gallery.share.CloudUserCache, com.miui.gallery.share.DBCache
        public /* bridge */ /* synthetic */ String newKey(Cursor cursor) {
            return super.newKey(cursor);
        }

        @Override // com.miui.gallery.share.CloudUserCache, com.miui.gallery.share.DBCache
        /* renamed from: newValue */
        public /* bridge */ /* synthetic */ List<CloudUserCacheEntry> mo1388newValue(Cursor cursor) {
            return super.mo1388newValue(cursor);
        }

        @Override // com.miui.gallery.share.DBCache
        public Uri getUri() {
            return GalleryContract.CloudUser.CLOUD_USER_URI;
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public void syncFromServer(String str, AlbumShareUIManager.OnCompletionListener<Void, Void> onCompletionListener) {
            AlbumShareUIManager.syncAllUserInfoFromNetworkAsync(onCompletionListener);
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public String[] getProjection() {
            return new String[]{"albumId", "userId", "createTime", "relation", "relationText", "serverStatus", "phone"};
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public CloudUserCacheEntry newValueElement(Cursor cursor) {
            return new CloudUserCacheEntry(cursor.getString(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public String[] getSelectionArgs() {
            return new String[]{"normal"};
        }
    };
    public static final CloudUserCache sSharerUserCache = new CloudUserCache() { // from class: com.miui.gallery.share.CloudUserCache.2
        @Override // com.miui.gallery.share.CloudUserCache
        public String getSelection() {
            return null;
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public String[] getSelectionArgs() {
            return new String[0];
        }

        @Override // com.miui.gallery.share.CloudUserCache, com.miui.gallery.share.DBCache
        public /* bridge */ /* synthetic */ String newKey(Cursor cursor) {
            return super.newKey(cursor);
        }

        @Override // com.miui.gallery.share.CloudUserCache, com.miui.gallery.share.DBCache
        /* renamed from: newValue */
        public /* bridge */ /* synthetic */ List<CloudUserCacheEntry> mo1388newValue(Cursor cursor) {
            return super.mo1388newValue(cursor);
        }

        @Override // com.miui.gallery.share.DBCache
        public Uri getUri() {
            return GalleryContract.ShareUser.SHARE_USER_URI;
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public void syncFromServer(String str, AlbumShareUIManager.OnCompletionListener<Void, Void> onCompletionListener) {
            AlbumShareUIManager.syncUserListForAlbumAsync(str, true, onCompletionListener);
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public String[] getProjection() {
            return new String[]{"albumId", "userId", "createTime", "relation", "relationText", "serverStatus"};
        }

        @Override // com.miui.gallery.share.CloudUserCache
        public CloudUserCacheEntry newValueElement(Cursor cursor) {
            return new CloudUserCacheEntry(cursor.getString(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), null);
        }
    };

    public abstract String[] getProjection();

    public abstract String getSelection();

    public abstract String[] getSelectionArgs();

    public abstract CloudUserCacheEntry newValueElement(Cursor cursor);

    public abstract void syncFromServer(String str, AlbumShareUIManager.OnCompletionListener<Void, Void> onCompletionListener);

    @Override // com.miui.gallery.share.DBCache
    public String newKey(Cursor cursor) {
        return cursor.getString(0);
    }

    @Override // com.miui.gallery.share.DBCache
    /* renamed from: newValue */
    public List<CloudUserCacheEntry> mo1388newValue(Cursor cursor) {
        return Lists.newArrayList();
    }

    @Override // com.miui.gallery.share.DBCache
    public Cursor queryInBackground() {
        return GalleryApp.sGetAndroidContext().getContentResolver().query(getUri(), getProjection(), getSelection(), getSelectionArgs(), "albumId");
    }

    @Override // com.miui.gallery.share.DBCache
    public Map<String, List<CloudUserCacheEntry>> loadInBackground() {
        HashMap newHashMap = Maps.newHashMap();
        Cursor queryInBackground = queryInBackground();
        if (queryInBackground != null) {
            String str = null;
            List<CloudUserCacheEntry> list = null;
            while (queryInBackground.moveToNext()) {
                try {
                    String newKey = newKey(queryInBackground);
                    if (newKey != null) {
                        if (!TextUtils.equals(str, newKey)) {
                            List<CloudUserCacheEntry> mo1388newValue = mo1388newValue(queryInBackground);
                            newHashMap.put(newKey, mo1388newValue);
                            list = mo1388newValue;
                            str = newKey;
                        }
                        if (list != null) {
                            list.add(newValueElement(queryInBackground));
                        }
                    }
                } finally {
                    queryInBackground.close();
                }
            }
        }
        return newHashMap;
    }

    public List<CloudUserCacheEntry> getCloudUserListByAlbumId(String str) {
        List<CloudUserCacheEntry> list = getCache().get(str);
        return list != null ? Collections.unmodifiableList(list) : EMPTY_ENTRY_LIST;
    }

    public static CloudUserCache getOwnerUserCache() {
        return sOwnerUserCache;
    }

    public static CloudUserCache getSharerUserCache() {
        return sSharerUserCache;
    }
}
