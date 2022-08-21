package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.provider.cache.SearchHistoryItem;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchHistoryManager extends CacheManager<SearchHistoryItem> {
    public static final List<SearchHistoryItem> EMPTY_CACHE = new ArrayList(0);
    public static volatile SearchHistoryManager sSearchHistoryManager;

    public SearchHistoryManager() {
        this.mQueryFactory = new SearchHistoryItem.QueryFactory();
        this.mCache = EMPTY_CACHE;
        this.mGenerator = new SearchHistoryItem.Generator();
    }

    public void releaseCache() {
        this.mCache = EMPTY_CACHE;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public Cursor query(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle) {
        ensureCache();
        if (bundle != null && !TextUtils.isEmpty(bundle.getString("query_text"))) {
            String[] strArr3 = {bundle.getString("query_text"), bundle.getString("query_text")};
            if (str == null) {
                str = "(title LIKE ? OR subTitle LIKE ?)";
            } else {
                str = "(" + str + ") AND (title LIKE ? OR subTitle LIKE ?)";
            }
            strArr2 = strArr2 == null ? strArr3 : StringUtils.mergeStringArray(strArr2, strArr3);
        }
        return super.query(strArr, str, strArr2, str2, str3, str4, bundle);
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public long insert(long j, ContentValues contentValues) {
        long j2 = -1;
        if (contentValues == null || contentValues.size() <= 0) {
            SearchLog.w(".searchProvider.SearchHistoryManager", "Try to insert history with empty content value!");
            return -1L;
        }
        SearchHistoryItem searchHistoryItem = (SearchHistoryItem) this.mGenerator.mo1225from(j, contentValues);
        if (searchHistoryItem == null || TextUtils.isEmpty(searchHistoryItem.intentActionURI)) {
            SearchLog.w(".searchProvider.SearchHistoryManager", "Try to insert history with no action uri!");
            return -1L;
        }
        if (searchHistoryItem.timestamp == null) {
            searchHistoryItem.timestamp = Long.valueOf(System.currentTimeMillis());
        }
        this.mWriteLock.lock();
        try {
            ArrayList arrayList = new ArrayList();
            if (SearchHistoryStorageHelper.addHistoryItem(searchHistoryItem, arrayList) > 0) {
                this.mCache = arrayList;
                j2 = 0;
            }
            return j2;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public int delete(String str) {
        this.mWriteLock.lock();
        try {
            ArrayList arrayList = new ArrayList();
            if (SearchHistoryStorageHelper.removeHistoryItem(str, arrayList) > 0) {
                this.mCache = arrayList;
            }
            this.mWriteLock.unlock();
            return 1;
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public int delete(String str, String[] strArr) {
        if (str == null || strArr == null) {
            return deleteAll();
        }
        return delete(strArr[0]);
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public int update(String str, String[] strArr, ContentValues contentValues) {
        SearchLog.w(".searchProvider.SearchHistoryManager", "[Update] operation not supported! ");
        return 0;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public Cursor onCreateCursor(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, List<SearchHistoryItem> list) {
        if (bundle != null) {
            int i = bundle.getInt("query_limit", -1);
            while (i >= 0 && i < list.size()) {
                list.remove(list.size() - 1);
            }
        }
        return new RawCursor(list, strArr, this.mQueryFactory.getMapper());
    }

    public int deleteAll() {
        List<T> list;
        this.mWriteLock.lock();
        try {
            int clearSavedHistories = SearchHistoryStorageHelper.clearSavedHistories();
            if (clearSavedHistories > 0 && (list = this.mCache) != EMPTY_CACHE) {
                list.clear();
            }
            return clearSavedHistories;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public final void ensureCache() {
        if (this.mCache == EMPTY_CACHE) {
            this.mWriteLock.lock();
            try {
                load();
            } finally {
                this.mWriteLock.unlock();
            }
        }
    }

    public final void load() {
        List<SearchHistoryItem> savedHistories;
        if (this.mCache != EMPTY_CACHE || (savedHistories = SearchHistoryStorageHelper.getSavedHistories()) == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        this.mCache = arrayList;
        arrayList.addAll(savedHistories);
    }

    public static SearchHistoryManager getInstance() {
        if (sSearchHistoryManager == null) {
            synchronized (SearchHistoryManager.class) {
                if (sSearchHistoryManager == null) {
                    sSearchHistoryManager = new SearchHistoryManager();
                }
            }
        }
        return sSearchHistoryManager;
    }
}
