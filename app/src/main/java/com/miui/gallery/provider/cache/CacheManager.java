package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public abstract class CacheManager<T extends CacheItem> {
    public String TAG = "CacheManager";
    public List<T> mCache;
    public CacheItem.Generator<T> mGenerator;
    public final List<OnOperationListener<T>> mOperationListeners;
    public CacheItem.QueryFactory<T> mQueryFactory;
    public final ReentrantReadWriteLock.ReadLock mReadLock;
    public final ReentrantReadWriteLock mReadWriteLock;
    public final ReentrantReadWriteLock.WriteLock mWriteLock;

    /* loaded from: classes2.dex */
    public interface OnOperationListener<T extends CacheItem> {
        default void onBulkInsert(List<T> list) {
        }

        default void onInsert(T t) {
        }

        default void onRemove(List<T> list) {
        }

        default void onUpdate(List<T> list, ContentValues contentValues) {
        }
    }

    /* loaded from: classes2.dex */
    public interface ResultConverter<T, R> {
        R convert(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, List<T> list);
    }

    public ContentValues filterLogInfo(ContentValues contentValues) {
        return contentValues;
    }

    public boolean isInitialized() {
        return true;
    }

    public abstract Cursor onCreateCursor(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, List<T> list);

    public CacheManager() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mReadWriteLock = reentrantReadWriteLock;
        this.mReadLock = reentrantReadWriteLock.readLock();
        this.mWriteLock = reentrantReadWriteLock.writeLock();
        this.mOperationListeners = new LinkedList();
    }

    public <R> List<R> query(String str, String[] strArr, String str2, String str3, String str4, Bundle bundle, IMediaProcessor<? super T, R> iMediaProcessor) {
        return iMediaProcessor.processCache(doQuery(str, strArr, str2, str3, str4), bundle);
    }

    public Cursor query(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle) {
        return (Cursor) query(strArr, str, strArr2, str2, str3, str4, bundle, new ResultConverter() { // from class: com.miui.gallery.provider.cache.CacheManager$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.provider.cache.CacheManager.ResultConverter
            public final Object convert(String[] strArr3, String str5, String[] strArr4, String str6, String str7, String str8, Bundle bundle2, List list) {
                return CacheManager.this.onCreateCursor(strArr3, str5, strArr4, str6, str7, str8, bundle2, list);
            }
        });
    }

    public <R> R query(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, ResultConverter<T, R> resultConverter) {
        return resultConverter.convert(strArr, str, strArr2, str2, str3, str4, bundle, doQuery(str, strArr2, str2, str3, str4));
    }

    public List<T> doQuery(String str, String[] strArr, String str2, String str3, String str4) {
        TimingTracing.beginTracing(this.TAG, "doQuery");
        try {
            int parseInt = !TextUtils.isEmpty(str4) ? Integer.parseInt(str4) : 0;
            Filter<T> from = Filter.from(str, strArr, this.mQueryFactory);
            this.mReadLock.lock();
            List<T> filter = filter(from);
            TimingTracing.addSplit("filter");
            this.mReadLock.unlock();
            if (!TextUtils.isEmpty(str3)) {
                filter = group(filter, str3);
                TimingTracing.addSplit("group");
            }
            if (!TextUtils.isEmpty(str2) && !filter.isEmpty()) {
                filter = sort(filter, str2);
                TimingTracing.addSplit("sort");
            }
            if (parseInt > 0 && parseInt < filter.size()) {
                filter = filter.subList(0, parseInt);
            }
            return filter;
        } finally {
            TimingTracing.stopTracing(null);
        }
    }

    public long insert(long j, ContentValues contentValues) {
        this.mWriteLock.lock();
        try {
            T mo1225from = this.mGenerator.mo1225from(j, contentValues);
            if (this.mCache.contains(mo1225from)) {
                DefaultLogger.w(this.TAG, "insert: item already exists: %s", mo1225from);
                StatHelper.recordCountEvent("error_full", "cache_manager_insert_dup_id");
                return j;
            }
            this.mCache.add(mo1225from);
            for (OnOperationListener<T> onOperationListener : this.mOperationListeners) {
                onOperationListener.onInsert(mo1225from);
            }
            return j;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public int bulkInsert(List<Pair<Long, ContentValues>> list, boolean z) {
        ArrayList arrayList = new ArrayList(list.size());
        this.mWriteLock.lock();
        try {
            for (Pair<Long, ContentValues> pair : list) {
                T mo1225from = this.mGenerator.mo1225from(((Long) pair.first).longValue(), (ContentValues) pair.second);
                if (!z && this.mCache.contains(mo1225from)) {
                    DefaultLogger.w(this.TAG, "bulkInsert: item already exists: %s", mo1225from);
                    StatHelper.recordCountEvent("error_full", "cache_manager_bulk_insert_dup_id");
                } else {
                    arrayList.add(mo1225from);
                    this.mCache.add(mo1225from);
                }
            }
            this.mWriteLock.unlock();
            for (OnOperationListener<T> onOperationListener : this.mOperationListeners) {
                onOperationListener.onBulkInsert(arrayList);
            }
            return arrayList.size();
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    public int delete(String str, String[] strArr) {
        DefaultLogger.d(this.TAG, "deleting %s, %s", str, Arrays.toString(strArr));
        int internalDelete = internalDelete(str, strArr);
        DefaultLogger.d(this.TAG, "delete finished: %d", Integer.valueOf(internalDelete));
        return internalDelete;
    }

    public final int internalDelete(String str, String[] strArr) {
        Filter<T> from = Filter.from(str, strArr, this.mGenerator);
        this.mWriteLock.lock();
        try {
            List<T> filterAndDelete = filterAndDelete(from);
            int size = filterAndDelete.size();
            if (!filterAndDelete.isEmpty()) {
                for (OnOperationListener<T> onOperationListener : this.mOperationListeners) {
                    onOperationListener.onRemove(filterAndDelete);
                }
            }
            return size;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public int update(String str, String[] strArr, ContentValues contentValues) {
        DefaultLogger.d(this.TAG, "updating %s, args: %s with ContentValues[%s]", str, Arrays.toString(strArr), filterLogInfo(contentValues));
        int internalUpdate = internalUpdate(str, strArr, contentValues);
        DefaultLogger.d(this.TAG, "update finished");
        return internalUpdate;
    }

    public final int internalUpdate(String str, String[] strArr, ContentValues contentValues) {
        Filter<T> from = Filter.from(str, strArr, this.mGenerator);
        this.mWriteLock.lock();
        try {
            List<T> filterAndReplace = filterAndReplace(from);
            int i = 0;
            for (T t : filterAndReplace) {
                this.mGenerator.update(t, contentValues);
                DefaultLogger.d(this.TAG, "updated %s", t.toShortString());
                i++;
            }
            for (OnOperationListener<T> onOperationListener : this.mOperationListeners) {
                onOperationListener.onUpdate(filterAndReplace, contentValues);
            }
            this.mWriteLock.unlock();
            DefaultLogger.fd(this.TAG, "updated %d items", Integer.valueOf(i));
            return i;
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    public List<T> internalQueryByFilter(Filter<T> filter) {
        this.mReadLock.lock();
        try {
            return filter(filter);
        } finally {
            this.mReadLock.unlock();
        }
    }

    public List<T> filter(Filter<T> filter) {
        ArrayList arrayList = new ArrayList(this.mCache.size());
        for (T t : this.mCache) {
            T filter2 = filter.filter(t);
            if (filter2 != null) {
                arrayList.add(filter2);
            }
        }
        return arrayList;
    }

    public final List<T> filterAndDelete(Filter<T> filter) {
        LinkedList linkedList = new LinkedList();
        Iterator<T> it = this.mCache.iterator();
        while (it.hasNext()) {
            T next = it.next();
            if (filter.filter(next) != null) {
                it.remove();
                linkedList.add(next);
                DefaultLogger.d(this.TAG, "deleted %s", next);
            }
        }
        return linkedList;
    }

    public final List<T> filterAndReplace(Filter<T> filter) {
        LinkedList linkedList = new LinkedList();
        ListIterator<T> listIterator = this.mCache.listIterator(0);
        while (listIterator.hasNext()) {
            T next = listIterator.next();
            if (filter.filter(next) != null) {
                T t = (T) next.mo1224copy();
                listIterator.set(t);
                linkedList.add(t);
            }
        }
        return linkedList;
    }

    public List<T> group(List<T> list, String str) {
        int index = this.mQueryFactory.getMapper().getIndex(str);
        CacheItem.Merger<T> merger = this.mQueryFactory.getMerger(index);
        if (index == -1) {
            throw new IllegalArgumentException(str + " not found.");
        }
        HashMap hashMap = new HashMap((list.size() * 4) / 3);
        for (T t : list) {
            Object obj = t.get(index, false);
            T t2 = (T) hashMap.get(obj);
            if (t2 == null) {
                hashMap.put(obj, t);
            } else {
                hashMap.put(obj, merger.merge(t2, t, index));
            }
        }
        return new ArrayList(hashMap.values());
    }

    public List<T> sort(List<T> list, String str) {
        int indexOf = str.indexOf(32);
        boolean z = false;
        String substring = str.substring(0, indexOf > 0 ? indexOf : str.length());
        int index = this.mQueryFactory.getMapper().getIndex(substring);
        if (index < 0) {
            throw new IllegalArgumentException(substring + " not found");
        }
        if (indexOf > 0 && str.substring(indexOf).trim().equalsIgnoreCase("desc")) {
            z = true;
        }
        sort(list, index, z);
        return list;
    }

    public void sort(List<T> list, int i, boolean z) {
        Comparator<T> comparator = this.mQueryFactory.getComparator(i, z);
        if (comparator != null) {
            try {
                list.sort(comparator);
            } catch (Exception e) {
                DefaultLogger.e(this.TAG, "cacheItems sort error,current comparator:%s , %s", comparator.toString(), e);
            }
        }
    }

    public CacheItem.QueryFactory<T> getQueryFactory() {
        return this.mQueryFactory;
    }

    public void addOperationListener(OnOperationListener<T> onOperationListener) {
        this.mOperationListeners.add(onOperationListener);
    }

    public void removeOperationListener(OnOperationListener<T> onOperationListener) {
        this.mOperationListeners.remove(onOperationListener);
    }
}
