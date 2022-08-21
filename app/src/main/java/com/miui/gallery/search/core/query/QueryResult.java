package com.miui.gallery.search.core.query;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryResult {
    public final List<Source> mExpectedSources;
    public boolean mHasResults;
    public final QueryInfo mQuery;
    public final SourceResult[] mSourceResults;
    public boolean mClosed = false;
    public final DataSetObservable mDataSetObservable = new DataSetObservable();
    public final Object mSourceResultLock = new Object();
    public boolean mDone = false;
    public final HashMap<String, Integer> mSourcePositions = new HashMap<>();

    public QueryResult(QueryInfo queryInfo, List<Source> list) {
        this.mHasResults = false;
        this.mQuery = queryInfo;
        this.mExpectedSources = list;
        this.mSourceResults = new SourceResult[list.size()];
        for (int i = 0; i < this.mExpectedSources.size(); i++) {
            this.mSourcePositions.put(this.mExpectedSources.get(i).getName(), Integer.valueOf(i));
        }
        SearchLog.d("QueryResult", "new QueryResult [" + hashCode() + "] query \"" + queryInfo + "\" expected sources: " + this.mExpectedSources);
        this.mHasResults = false;
    }

    public void done() {
        this.mDone = true;
        notifyDataSetChanged();
    }

    public boolean isDone() {
        return this.mDone || getSourceResultsCount() >= this.mExpectedSources.size();
    }

    public final int getSourceResultsCount() {
        int i;
        synchronized (this.mSourceResultLock) {
            int i2 = 0;
            i = 0;
            while (true) {
                SourceResult[] sourceResultArr = this.mSourceResults;
                if (i2 < sourceResultArr.length) {
                    if (sourceResultArr[i2] != null) {
                        i++;
                    }
                    i2++;
                }
            }
        }
        return i;
    }

    /* JADX WARN: Type inference failed for: r2v8, types: [com.miui.gallery.search.core.suggestion.SuggestionCursor, android.database.Cursor] */
    public boolean addSourceResults(List<SourceResult> list) {
        boolean z = false;
        if (isClosed()) {
            for (SourceResult sourceResult : list) {
                sourceResult.release();
            }
            return false;
        }
        for (SourceResult sourceResult2 : list) {
            if (sourceResult2.getData() != 0 && sourceResult2.getData().getCount() > 0) {
                this.mHasResults = true;
            }
            Integer num = this.mSourcePositions.get(sourceResult2.getSource().getName());
            if (num == null) {
                SearchLog.w("QueryResult", "Got unexpected SourceResult from corpus " + sourceResult2.getSource().getName());
                sourceResult2.release();
            } else {
                synchronized (this.mSourceResultLock) {
                    if (this.mSourceResults[num.intValue()] != null) {
                        if (SearchUtils.compareResultHashCode(this.mSourceResults[num.intValue()], sourceResult2)) {
                            SearchLog.d("QueryResult", "We ignored later result of query [%s] source %s, for they are identical", this.mQuery, sourceResult2.getSource());
                            sourceResult2.release();
                        } else {
                            this.mSourceResults[num.intValue()].release();
                            sourceResult2.acquire();
                            this.mSourceResults[num.intValue()] = sourceResult2;
                        }
                    } else {
                        sourceResult2.acquire();
                        this.mSourceResults[num.intValue()] = sourceResult2;
                    }
                    z = true;
                }
            }
        }
        if (z) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.search.core.query.QueryResult.1
                @Override // java.lang.Runnable
                public void run() {
                    QueryResult.this.notifyDataSetChanged();
                }
            });
        }
        return z;
    }

    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if (this.mClosed) {
            throw new IllegalStateException("registerDataSetObserver() when closed");
        }
        this.mDataSetObservable.registerObserver(dataSetObserver);
    }

    public void notifyDataSetChanged() {
        this.mDataSetObservable.notifyChanged();
    }

    public void close() {
        SourceResult[] sourceResultArr;
        if (this.mClosed) {
            throw new IllegalStateException("Double close()");
        }
        this.mClosed = true;
        this.mDataSetObservable.unregisterAll();
        synchronized (this.mSourceResultLock) {
            for (SourceResult sourceResult : this.mSourceResults) {
                if (sourceResult != null) {
                    sourceResult.release();
                }
            }
            Arrays.fill(this.mSourceResults, (Object) null);
        }
    }

    public boolean isClosed() {
        return this.mClosed;
    }

    public void finalize() {
        if (!this.mClosed) {
            SearchLog.e("QueryResult", "LEAK! Finalized without being closed: QueryResult[" + getQuery() + "]");
            close();
        }
    }

    public QueryInfo getQuery() {
        return this.mQuery;
    }

    public List<SourceResult> getSourceResults() {
        ArrayList arrayList;
        synchronized (this.mSourceResultLock) {
            arrayList = new ArrayList(this.mSourceResults.length);
            int i = 0;
            while (true) {
                SourceResult[] sourceResultArr = this.mSourceResults;
                if (i < sourceResultArr.length) {
                    if (sourceResultArr[i] != null) {
                        arrayList.add(sourceResultArr[i]);
                    }
                    i++;
                }
            }
        }
        return arrayList;
    }

    public String toString() {
        return "QueryResult@" + hashCode() + "{expectedSources=" + this.mExpectedSources + ",getSourceResultsCount()=" + getSourceResultsCount() + "}";
    }
}
