package com.miui.gallery.search.core.query;

import android.content.Context;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.OperationCanceledException;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.resultprocessor.ResultProcessor;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class QueryLoader<T extends SuggestionResult> extends ExtendedAsyncTaskLoader<T> {
    public final QueryInfo mQueryInfo;
    public ControllableTask<QueryResult> mQueryTask;
    public boolean mReceiveResultUpdates;
    public boolean mReceiveSourceUpdates;
    public boolean mReportTillDone;
    public T mResult;
    public ContentObserver mResultContentObserver;
    public final ResultProcessor<T> mResultProcessor;
    public DataSetObserver mSourceDataSetObserver;

    public void onCanceled(T t) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.loader.content.Loader
    public /* bridge */ /* synthetic */ void deliverResult(Object obj) {
        deliverResult((QueryLoader<T>) ((SuggestionResult) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.loader.content.AsyncTaskLoader
    public /* bridge */ /* synthetic */ void onCanceled(Object obj) {
        onCanceled((QueryLoader<T>) ((SuggestionResult) obj));
    }

    public QueryLoader(Context context, QueryInfo queryInfo, ResultProcessor<T> resultProcessor, boolean z, boolean z2, boolean z3) {
        super(context);
        this.mQueryInfo = queryInfo;
        this.mResultProcessor = resultProcessor;
        this.mReceiveSourceUpdates = z;
        this.mReceiveResultUpdates = z2;
        this.mReportTillDone = z3;
    }

    public QueryLoader(Context context, QueryInfo queryInfo, ResultProcessor<T> resultProcessor) {
        this(context, queryInfo, resultProcessor, false, false, false);
    }

    public QueryInfo getQueryInfo() {
        return this.mQueryInfo;
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStartLoading() {
        if (this.mQueryTask == null) {
            this.mQueryTask = QueryPackageHelper.getQueryResult(this.mQueryInfo, this.mReceiveSourceUpdates);
        }
        ControllableTask<QueryResult> controllableTask = this.mQueryTask;
        if (controllableTask == null) {
            SearchLog.e("QueryLoader", "No query task was created for query %s", this.mQueryInfo);
        } else if (controllableTask.isCanceled()) {
            throw new RuntimeException("Invalid inner source, query task has been cancelled!");
        } else {
            T t = this.mResult;
            if (t != null) {
                deliverResult((QueryLoader<T>) t);
            }
            if (!this.mQueryTask.started()) {
                this.mQueryTask.start();
                this.mQueryTask.mo1323getResult().registerDataSetObserver(getSourceDataSetObserver());
            }
            if (!takeContentChanged()) {
                return;
            }
            forceLoad();
        }
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public T mo1444loadInBackground() {
        String str;
        if (isLoadInBackgroundCanceled()) {
            throw new OperationCanceledException();
        }
        long currentTimeMillis = System.currentTimeMillis();
        T process = this.mResultProcessor.process(this.mQueryTask.started() ? this.mQueryTask.mo1323getResult().getSourceResults() : null);
        if (process != null) {
            boolean isDone = this.mQueryTask.mo1323getResult().isDone();
            Bundle resultExtras = process.getResultExtras();
            if (resultExtras == Bundle.EMPTY) {
                resultExtras = new Bundle();
            }
            resultExtras.putBoolean("is_done", isDone);
            process.setResultExtras(resultExtras);
        }
        QueryInfo queryInfo = this.mQueryInfo;
        String valueOf = String.valueOf(System.currentTimeMillis() - currentTimeMillis);
        if (process == null) {
            str = "is null";
        } else if (process.isEmpty() || process.getData() == null) {
            str = "is empty";
        } else {
            str = "has " + process.getData().getCount() + "items";
        }
        SearchLog.d("QueryLoader", "Load result for {%s} cost %s ms, result %s", queryInfo, valueOf, str);
        return process;
    }

    public final DataSetObserver getSourceDataSetObserver() {
        if (this.mSourceDataSetObserver == null) {
            this.mSourceDataSetObserver = new DataSetObserver() { // from class: com.miui.gallery.search.core.query.QueryLoader.1
                @Override // android.database.DataSetObserver
                public void onChanged() {
                    if (!QueryLoader.this.mReportTillDone || (QueryLoader.this.mQueryTask != null && QueryLoader.this.mQueryTask.mo1323getResult() != null && ((QueryResult) QueryLoader.this.mQueryTask.mo1323getResult()).isDone())) {
                        QueryLoader.this.onContentChanged();
                    } else {
                        SearchLog.e("QueryLoader", "On block query loader update");
                    }
                }
            };
        }
        return this.mSourceDataSetObserver;
    }

    public final ContentObserver getResultContentObserver() {
        if (this.mResultContentObserver == null) {
            this.mResultContentObserver = new ContentObserver(ThreadManager.getMainHandler()) { // from class: com.miui.gallery.search.core.query.QueryLoader.2
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    QueryLoader.this.onContentChanged();
                }
            };
        }
        return this.mResultContentObserver;
    }

    public void deliverResult(T t) {
        T t2;
        if (isReset()) {
            onReleaseResources(t);
            return;
        }
        T t3 = this.mResult;
        this.mResult = t;
        if (this.mReceiveResultUpdates && t != t3 && t != null && !t.isEmpty()) {
            this.mResult.registerContentObserver(getResultContentObserver());
        }
        if (isStarted() && (t2 = this.mResult) != t3) {
            super.deliverResult((QueryLoader<T>) t2);
        }
        if (t3 == this.mResult || t3 == null) {
            return;
        }
        onReleaseResources(t3);
    }

    @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
    public void onStopLoading() {
        cancelLoad();
    }

    @Override // androidx.loader.content.Loader
    public void onReset() {
        super.onReset();
        onStopLoading();
        ControllableTask<QueryResult> controllableTask = this.mQueryTask;
        if (controllableTask != null) {
            controllableTask.cancel();
            if (this.mQueryTask.mo1323getResult() != null && !this.mQueryTask.mo1323getResult().isClosed()) {
                this.mQueryTask.mo1323getResult().close();
            }
            this.mQueryTask = null;
        }
        onReleaseResources(this.mResult);
        this.mResult = null;
    }

    public final void onReleaseResources(T t) {
        if (t == null || t.isClosed()) {
            return;
        }
        t.release();
    }

    @Override // androidx.loader.content.Loader
    public String toString() {
        return super.toString() + "," + this.mQueryInfo;
    }
}
