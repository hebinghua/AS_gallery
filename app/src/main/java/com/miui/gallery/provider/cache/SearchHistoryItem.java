package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.Filter;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.Comparator;
import java.util.Map;

/* loaded from: classes2.dex */
public class SearchHistoryItem implements CacheItem {
    public static final Map<String, Integer> COLUMN_MAP;
    public static final CacheItem.ColumnMapper COLUMN_MAPPER;
    public static final String[] PROJECTION;
    @SerializedName(CallMethod.RESULT_ICON)
    public String icon;
    @SerializedName(MapBundleKey.MapObjKey.OBJ_URL)
    public String intentActionURI;
    @SerializedName("subtitle")
    public String subTitle;
    @SerializedName("timestamp")
    public Long timestamp;
    @SerializedName("title")
    public String title;

    @Override // com.miui.gallery.provider.cache.CacheItem
    public Object get(int i, boolean z) {
        if (i == 0) {
            return this.title;
        }
        if (i == 1) {
            return this.subTitle;
        }
        if (i == 2) {
            return this.intentActionURI;
        }
        if (i == 3) {
            return this.icon;
        }
        if (i == 4) {
            return this.timestamp;
        }
        throw new IllegalArgumentException(" not recognized column.");
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public int getType(int i) {
        if (i == 0 || i == 1 || i == 2 || i == 3) {
            return 3;
        }
        if (i != 4) {
            throw new IllegalArgumentException(" not recognized column.");
        }
        return 1;
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    /* renamed from: copy */
    public SearchHistoryItem mo1224copy() {
        SearchHistoryItem searchHistoryItem = new SearchHistoryItem();
        searchHistoryItem.title = this.title;
        searchHistoryItem.subTitle = this.subTitle;
        searchHistoryItem.icon = this.icon;
        searchHistoryItem.intentActionURI = this.intentActionURI;
        searchHistoryItem.timestamp = this.timestamp;
        return searchHistoryItem;
    }

    static {
        String[] strArr = {"title", "subTitle", "actionUri", CallMethod.RESULT_ICON, "timestamp"};
        PROJECTION = strArr;
        ArrayMap arrayMap = new ArrayMap(strArr.length);
        COLUMN_MAP = arrayMap;
        arrayMap.put("title", 0);
        arrayMap.put("subTitle", 1);
        arrayMap.put("actionUri", 2);
        arrayMap.put(CallMethod.RESULT_ICON, 3);
        arrayMap.put("timestamp", 4);
        COLUMN_MAPPER = new CacheItem.ColumnMapper() { // from class: com.miui.gallery.provider.cache.SearchHistoryItem.1
            @Override // com.miui.gallery.provider.cache.CacheItem.ColumnMapper
            public int getIndex(String str) {
                Integer num = (Integer) SearchHistoryItem.COLUMN_MAP.get(str);
                if (num == null) {
                    return -1;
                }
                return num.intValue();
            }
        };
    }

    /* loaded from: classes2.dex */
    public static class Generator implements CacheItem.Generator<SearchHistoryItem> {
        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        public Filter.CompareFilter<SearchHistoryItem> getFilter(int i, Filter.Comparator comparator, String str) {
            return null;
        }

        public SearchHistoryItem generate() {
            return new SearchHistoryItem();
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from  reason: collision with other method in class */
        public SearchHistoryItem mo1226from(Cursor cursor) {
            SearchHistoryItem generate = generate();
            generate.title = cursor.getString(0);
            generate.subTitle = cursor.getString(1);
            generate.intentActionURI = cursor.getString(2);
            generate.icon = cursor.getString(3);
            generate.timestamp = Long.valueOf(cursor.getLong(4));
            return generate;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from  reason: collision with other method in class */
        public SearchHistoryItem mo1225from(long j, ContentValues contentValues) {
            SearchHistoryItem generate = generate();
            update(generate, contentValues);
            generate.timestamp = contentValues.getAsLong(SearchHistoryItem.PROJECTION[4]);
            return generate;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        public void update(SearchHistoryItem searchHistoryItem, ContentValues contentValues) {
            String[] strArr = SearchHistoryItem.PROJECTION;
            if (contentValues.containsKey(strArr[0])) {
                searchHistoryItem.title = contentValues.getAsString(strArr[0]);
            }
            if (contentValues.containsKey(strArr[1])) {
                searchHistoryItem.subTitle = contentValues.getAsString(strArr[1]);
            }
            if (contentValues.containsKey(strArr[2])) {
                searchHistoryItem.intentActionURI = contentValues.getAsString(strArr[2]);
            }
            if (contentValues.containsKey(strArr[3])) {
                searchHistoryItem.icon = contentValues.getAsString(strArr[3]);
            }
            if (contentValues.containsKey(strArr[4])) {
                searchHistoryItem.timestamp = contentValues.getAsLong(strArr[4]);
            }
        }

        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        public CacheItem.ColumnMapper getMapper() {
            return SearchHistoryItem.COLUMN_MAPPER;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        public String[] getProjection() {
            return SearchHistoryItem.PROJECTION;
        }
    }

    /* loaded from: classes2.dex */
    public static class QueryFactory implements CacheItem.QueryFactory<SearchHistoryItem> {
        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public CacheItem.Merger<SearchHistoryItem> getMerger(int i) {
            return null;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public CacheItem.ColumnMapper getMapper() {
            return SearchHistoryItem.COLUMN_MAPPER;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public Comparator<SearchHistoryItem> getComparator(int i, boolean z) {
            if (i == 4) {
                return new TimestampComparator(z);
            }
            return null;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public Filter.CompareFilter<SearchHistoryItem> getFilter(int i, Filter.Comparator comparator, String str) {
            if (i == 0) {
                return new TitleFilter(comparator, str);
            }
            if (i != 1) {
                return null;
            }
            return new SubTitleFilter(comparator, str);
        }

        /* loaded from: classes2.dex */
        public static class TitleFilter extends Filter.CompareFilter<SearchHistoryItem> {
            public TitleFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public SearchHistoryItem filter(SearchHistoryItem searchHistoryItem) {
                if (this.mComparator != Filter.Comparator.LIKE || TextUtils.isEmpty(this.mArgument) || !QueryFactory.containsIgnoreCase(searchHistoryItem.title, this.mArgument)) {
                    return null;
                }
                return searchHistoryItem;
            }
        }

        /* loaded from: classes2.dex */
        public static class SubTitleFilter extends Filter.CompareFilter<SearchHistoryItem> {
            public SubTitleFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
            }

            @Override // com.miui.gallery.provider.cache.Filter
            public SearchHistoryItem filter(SearchHistoryItem searchHistoryItem) {
                if (this.mComparator != Filter.Comparator.LIKE || TextUtils.isEmpty(this.mArgument) || !QueryFactory.containsIgnoreCase(searchHistoryItem.subTitle, this.mArgument)) {
                    return null;
                }
                return searchHistoryItem;
            }
        }

        public static boolean containsIgnoreCase(String str, String str2) {
            return !TextUtils.isEmpty(str) && str.toLowerCase().contains(str2.toLowerCase());
        }

        /* loaded from: classes2.dex */
        public static class TimestampComparator implements Comparator<SearchHistoryItem> {
            public boolean mDescent;

            public TimestampComparator(boolean z) {
                this.mDescent = z;
            }

            @Override // java.util.Comparator
            public int compare(SearchHistoryItem searchHistoryItem, SearchHistoryItem searchHistoryItem2) {
                int compareTo = searchHistoryItem.timestamp.compareTo(searchHistoryItem2.timestamp);
                return this.mDescent ? -compareTo : compareTo;
            }
        }
    }
}
