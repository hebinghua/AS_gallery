package com.miui.gallery.provider.cache;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.Filter;
import java.util.Comparator;
import java.util.Map;

/* loaded from: classes2.dex */
public class SearchIconItem implements CacheItem, Parcelable {
    public static final Map<String, Integer> COLUMN_MAP;
    public static final CacheItem.ColumnMapper COLUMN_MAPPER;
    public static final Parcelable.Creator<SearchIconItem> CREATOR;
    public static final String[] PROJECTION;
    public Float decodeRegionH;
    public int decodeRegionOrientation;
    public Float decodeRegionW;
    public Float decodeRegionX;
    public Float decodeRegionY;
    public String downloadUri;
    public String filePath;
    public String iconUri;
    public Uri notifyUri;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public Object get(int i, boolean z) {
        if (i == 0) {
            return this.iconUri;
        }
        if (i == 1) {
            return this.filePath;
        }
        if (i == 2) {
            return this.downloadUri;
        }
        if (i == 3) {
            return Integer.valueOf(this.decodeRegionOrientation);
        }
        if (i == 4) {
            return this.decodeRegionX;
        }
        if (i == 5) {
            return this.decodeRegionY;
        }
        if (i == 6) {
            return this.decodeRegionW;
        }
        if (i == 7) {
            return this.decodeRegionH;
        }
        throw new IllegalArgumentException(" not recognized column.");
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public int getType(int i) {
        if (i == 0 || i == 1 || i == 2) {
            return 3;
        }
        if (i == 3) {
            return 1;
        }
        if (i != 4 && i != 5 && i != 6 && i != 7) {
            throw new IllegalArgumentException(" not recognized column.");
        }
        return 2;
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    /* renamed from: copy */
    public SearchIconItem mo1224copy() {
        SearchIconItem searchIconItem = new SearchIconItem();
        searchIconItem.iconUri = this.iconUri;
        searchIconItem.filePath = this.filePath;
        searchIconItem.downloadUri = this.downloadUri;
        searchIconItem.decodeRegionOrientation = this.decodeRegionOrientation;
        searchIconItem.decodeRegionX = this.decodeRegionX;
        searchIconItem.decodeRegionY = this.decodeRegionY;
        searchIconItem.decodeRegionH = this.decodeRegionH;
        searchIconItem.decodeRegionW = this.decodeRegionW;
        searchIconItem.notifyUri = this.notifyUri;
        return searchIconItem;
    }

    static {
        String[] strArr = {"icon_uri", "file_path", "download_uri", "decode_region_orientation", "decode_region_x", "decode_region_y", "decode_region_w", "decode_region_h"};
        PROJECTION = strArr;
        ArrayMap arrayMap = new ArrayMap(strArr.length);
        COLUMN_MAP = arrayMap;
        arrayMap.put("icon_uri", 0);
        arrayMap.put("file_path", 1);
        arrayMap.put("download_uri", 2);
        arrayMap.put("decode_region_orientation", 3);
        arrayMap.put("decode_region_x", 4);
        arrayMap.put("decode_region_y", 5);
        arrayMap.put("decode_region_w", 6);
        arrayMap.put("decode_region_h", 7);
        COLUMN_MAPPER = new CacheItem.ColumnMapper() { // from class: com.miui.gallery.provider.cache.SearchIconItem.1
            @Override // com.miui.gallery.provider.cache.CacheItem.ColumnMapper
            public int getIndex(String str) {
                Integer num = (Integer) SearchIconItem.COLUMN_MAP.get(str);
                if (num == null) {
                    return -1;
                }
                return num.intValue();
            }
        };
        CREATOR = new Parcelable.Creator<SearchIconItem>() { // from class: com.miui.gallery.provider.cache.SearchIconItem.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public SearchIconItem mo1221createFromParcel(Parcel parcel) {
                return new SearchIconItem(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public SearchIconItem[] mo1222newArray(int i) {
                return new SearchIconItem[i];
            }
        };
    }

    /* loaded from: classes2.dex */
    public static class QueryFactory implements CacheItem.QueryFactory<SearchIconItem> {
        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public Comparator<SearchIconItem> getComparator(int i, boolean z) {
            return null;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public Filter.CompareFilter<SearchIconItem> getFilter(int i, Filter.Comparator comparator, String str) {
            return null;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public CacheItem.Merger<SearchIconItem> getMerger(int i) {
            return null;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public CacheItem.ColumnMapper getMapper() {
            return SearchIconItem.COLUMN_MAPPER;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.iconUri);
        parcel.writeString(this.filePath);
        parcel.writeString(this.downloadUri);
        parcel.writeInt(this.decodeRegionOrientation);
        parcel.writeValue(this.decodeRegionX);
        parcel.writeValue(this.decodeRegionY);
        parcel.writeValue(this.decodeRegionW);
        parcel.writeValue(this.decodeRegionH);
        parcel.writeParcelable(this.notifyUri, i);
    }

    public SearchIconItem() {
    }

    public SearchIconItem(Parcel parcel) {
        this.iconUri = parcel.readString();
        this.filePath = parcel.readString();
        this.downloadUri = parcel.readString();
        this.decodeRegionOrientation = parcel.readInt();
        this.decodeRegionX = (Float) parcel.readValue(Float.class.getClassLoader());
        this.decodeRegionY = (Float) parcel.readValue(Float.class.getClassLoader());
        this.decodeRegionW = (Float) parcel.readValue(Float.class.getClassLoader());
        this.decodeRegionH = (Float) parcel.readValue(Float.class.getClassLoader());
        this.notifyUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
    }
}
