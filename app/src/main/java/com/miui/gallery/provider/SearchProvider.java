package com.miui.gallery.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.provider.cache.SearchHistoryManager;
import com.miui.gallery.provider.cache.SearchIconManager;
import com.miui.gallery.search.SearchContract;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes2.dex */
public class SearchProvider extends ContentProvider {
    public static final UriMatcher sUriMatcher;
    public ContentResolver mContentResolver;
    public SearchHistoryManager mSearchHistoryManager = null;
    public SearchIconManager mSearchIconManager = null;
    public final Object mInitLock = new Object();

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.search", "history", 1);
        uriMatcher.addURI("com.miui.gallery.search", CallMethod.RESULT_ICON, 2);
    }

    public final void init() {
        synchronized (this.mInitLock) {
            if (this.mSearchHistoryManager == null) {
                this.mSearchHistoryManager = new SearchHistoryManager();
                this.mSearchIconManager = new SearchIconManager();
                this.mContentResolver = new ContentResolver();
            }
        }
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        str.hashCode();
        if (str.equals("isSupportSearch") && TextUtils.equals(getCallingPackage(), "com.android.quicksearchbox")) {
            boolean z = true;
            if (!AIAlbumStatusHelper.isLocalSearchOpen(true) || !AIAlbumStatusHelper.isCloudControlSearchBarOpen()) {
                z = false;
            }
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("isSupport", z);
            return bundle2;
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        init();
        int match = sUriMatcher.match(uri);
        boolean z = true;
        if (match == 1) {
            Bundle bundle = new Bundle();
            if (uri.getQueryParameter("query_limit") != null) {
                bundle.putInt("query_limit", Integer.valueOf(uri.getQueryParameter("query_limit")).intValue());
            }
            if (!TextUtils.isEmpty(uri.getQueryParameter("query_text"))) {
                bundle.putString("query_text", uri.getQueryParameter("query_text"));
            }
            Cursor query = this.mSearchHistoryManager.query(strArr, str, strArr2, str2, (String) null, (String) null, bundle);
            if (query == null) {
                return query;
            }
            registerNotifyUri(query, match);
            return query;
        }
        if (match == 2) {
            String queryParameter = uri.getQueryParameter("icon_uri");
            boolean booleanQueryParameter = uri.getBooleanQueryParameter("use_disk_cache", false);
            if (!booleanQueryParameter && !uri.getBooleanQueryParameter("cache_to_disk", false)) {
                z = false;
            }
            boolean booleanQueryParameter2 = uri.getBooleanQueryParameter("high_accuracy", false);
            if (!TextUtils.isEmpty(queryParameter)) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("use_disk_cache", booleanQueryParameter);
                bundle2.putBoolean("cache_to_disk", z);
                bundle2.putBoolean("high_accuracy", booleanQueryParameter2);
                return this.mSearchIconManager.query(Uri.parse(queryParameter), strArr, bundle2);
            }
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        init();
        long insert = sUriMatcher.match(uri) != 1 ? -1L : this.mSearchHistoryManager.insert(-1L, contentValues);
        int i = (insert > (-1L) ? 1 : (insert == (-1L) ? 0 : -1));
        if (i != 0) {
            notifyChange(uri);
        }
        if (i != 0) {
            return ContentUris.withAppendedId(uri, insert);
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        init();
        int delete = sUriMatcher.match(uri) != 1 ? 0 : this.mSearchHistoryManager.delete(str, strArr);
        if (delete > 0) {
            notifyChange(uri);
        }
        return delete;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        init();
        int update = sUriMatcher.match(uri) != 1 ? 0 : this.mSearchHistoryManager.update(str, strArr, contentValues);
        if (update > 0) {
            notifyChange(uri);
        }
        return update;
    }

    public final void notifyChange(Uri uri) {
        this.mContentResolver.notifyInternal(uri, null, 0L, null);
    }

    public final void registerNotifyUri(Cursor cursor, int i) {
        if (i != 1) {
            return;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), SearchContract.History.URI);
    }

    @Override // android.content.ContentProvider, android.content.ComponentCallbacks
    public void onLowMemory() {
        init();
        this.mSearchHistoryManager.releaseCache();
        this.mSearchIconManager.releaseCache();
    }

    @Override // android.content.ContentProvider, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        init();
        if (i >= 5) {
            this.mSearchHistoryManager.releaseCache();
            this.mSearchIconManager.releaseCache();
        }
    }

    /* loaded from: classes2.dex */
    public class ContentResolver extends GalleryContentResolver {
        public ContentResolver() {
        }

        @Override // com.miui.gallery.provider.GalleryContentResolver
        public Object matchUri(Uri uri) {
            return Integer.valueOf(SearchProvider.sUriMatcher.match(uri));
        }

        @Override // com.miui.gallery.provider.GalleryContentResolver
        public void notifyInternal(Uri uri, ContentObserver contentObserver, long j, Bundle bundle) {
            if (SearchProvider.sUriMatcher.match(uri) != 1) {
                return;
            }
            SearchProvider.this.getContext().getContentResolver().notifyChange(SearchContract.History.URI, contentObserver, false);
        }
    }
}
