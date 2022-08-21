package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.provider.cache.Filter;
import java.util.Comparator;

/* loaded from: classes2.dex */
public interface CacheItem {
    public static final Long TRUE = 1L;
    public static final Long FALSE = 0L;
    public static final Long DEFAULT_LONG = 0L;
    public static final Integer DEFAULT_INT = 0;

    /* loaded from: classes2.dex */
    public interface ColumnMapper {
        int getIndex(String str);
    }

    /* loaded from: classes2.dex */
    public interface Generator<T extends CacheItem> extends Filter.FilterFactory<T> {
        /* renamed from: from */
        T mo1225from(long j, ContentValues contentValues);

        /* renamed from: from */
        T mo1226from(Cursor cursor);

        String[] getProjection();

        void update(T t, ContentValues contentValues);
    }

    /* loaded from: classes2.dex */
    public interface Merger<T extends CacheItem> {
        T merge(T t, T t2, int i);
    }

    /* loaded from: classes2.dex */
    public interface QueryFactory<T extends CacheItem> extends Filter.FilterFactory<T> {
        Comparator<T> getComparator(int i, boolean z);

        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        Filter.CompareFilter<T> getFilter(int i, Filter.Comparator comparator, String str);

        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        ColumnMapper getMapper();

        Merger<T> getMerger(int i);
    }

    /* renamed from: copy */
    Object mo1224copy();

    Object get(int i, boolean z);

    int getType(int i);

    default String toShortString() {
        return toString();
    }
}
