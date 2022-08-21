package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.provider.cache.MediaCacheItem;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class ShareMediaCacheItem extends MediaCacheItem {

    /* loaded from: classes2.dex */
    public static class QueryFactory extends MediaCacheItem.QueryFactory<ShareMediaCacheItem> {
    }

    @Override // com.miui.gallery.provider.cache.MediaCacheItem, com.miui.gallery.provider.cache.CacheItem
    /* renamed from: copy  reason: collision with other method in class */
    public ShareMediaCacheItem mo1224copy() {
        ShareMediaCacheItem shareMediaCacheItem = new ShareMediaCacheItem();
        internalCopy(shareMediaCacheItem);
        return shareMediaCacheItem;
    }

    /* loaded from: classes2.dex */
    public static class Generator extends MediaCacheItem.Generator<ShareMediaCacheItem> {
        public String[] mProjections;

        public Generator(FavoritesDelegate favoritesDelegate) {
            super(favoritesDelegate);
        }

        @Override // com.miui.gallery.provider.cache.MediaCacheItem.Generator
        public ShareMediaCacheItem genNewItem() {
            return new ShareMediaCacheItem();
        }

        @Override // com.miui.gallery.provider.cache.MediaCacheItem.Generator, com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from */
        public ShareMediaCacheItem mo1226from(Cursor cursor) {
            ShareMediaCacheItem shareMediaCacheItem = new ShareMediaCacheItem();
            initFromCursor(cursor, shareMediaCacheItem);
            shareMediaCacheItem.mId = ShareMediaManager.convertToMediaId(shareMediaCacheItem.mId);
            return shareMediaCacheItem;
        }

        @Override // com.miui.gallery.provider.cache.MediaCacheItem.Generator, com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from */
        public ShareMediaCacheItem mo1225from(long j, ContentValues contentValues) {
            ShareMediaCacheItem shareMediaCacheItem = new ShareMediaCacheItem();
            initFromContentValues(j, contentValues, shareMediaCacheItem);
            shareMediaCacheItem.mId = ShareMediaManager.convertToMediaId(shareMediaCacheItem.mId);
            return shareMediaCacheItem;
        }

        @Override // com.miui.gallery.provider.cache.MediaCacheItem.Generator, com.miui.gallery.provider.cache.CacheItem.Generator
        public String[] getProjection() {
            if (this.mProjections == null) {
                ArrayList arrayList = new ArrayList(Arrays.asList(super.getProjection()));
                arrayList.remove("source_pkg");
                this.mProjections = (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
            return this.mProjections;
        }
    }
}
