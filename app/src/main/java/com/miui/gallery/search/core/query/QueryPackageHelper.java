package com.miui.gallery.search.core.query;

import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.os.Handler;
import com.miui.gallery.search.SearchConstants;
import com.miui.gallery.search.core.Consumer;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.SearchContext;
import com.miui.gallery.search.core.context.TaskExecutor;
import com.miui.gallery.search.core.result.SourceResult;
import com.miui.gallery.search.core.source.Source;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes2.dex */
public class QueryPackageHelper {
    public static ControllableTask<QueryResult> getQueryResult(QueryInfo queryInfo, boolean z) {
        SearchContext searchContext = SearchContext.getInstance();
        List<Source> matchedSources = searchContext.getMatchedSources(queryInfo);
        if (matchedSources == null) {
            SearchLog.e("QueryPackageHelper", "No matched source for query info %s", queryInfo);
            return null;
        }
        return new BatchQueryTask(queryInfo, matchedSources, searchContext.getQueryTaskExecutor(), z);
    }

    /* loaded from: classes2.dex */
    public static class BatchQueryTask implements ControllableTask<QueryResult> {
        public Handler mPublishHandler;
        public final QueryInfo mQueryInfo;
        public QueryResult mQueryResult;
        public QueryTask[] mQueryTasks;
        public final boolean mReceiveSourceUpdates;
        public final List<Source> mSources;
        public final TaskExecutor<QueryTask> mTaskExecutor;
        public boolean mIsCancelled = false;
        public Consumer<SourceResult> mQueryTaskReceiver = new Consumer<SourceResult>() { // from class: com.miui.gallery.search.core.query.QueryPackageHelper.BatchQueryTask.1
            @Override // com.miui.gallery.search.core.Consumer
            public boolean consume(SourceResult sourceResult) {
                if (BatchQueryTask.this.mIsCancelled) {
                    return false;
                }
                if (sourceResult == null) {
                    throw new RuntimeException("Receive a null source result!");
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(sourceResult);
                boolean addSourceResults = BatchQueryTask.this.mQueryResult.addSourceResults(arrayList);
                if (addSourceResults && BatchQueryTask.this.mReceiveSourceUpdates) {
                    sourceResult.registerDataSetObserver(new SourceResultDataSetObserver(sourceResult));
                    sourceResult.registerContentObserver(new SourceResultContentObserver(sourceResult));
                }
                Source source = sourceResult.getSource();
                if (source != null) {
                    int indexOf = BatchQueryTask.this.mSources.indexOf(source);
                    if (indexOf >= 0) {
                        BatchQueryTask.this.mQueryTasks[indexOf] = null;
                    }
                    if (sourceResult.getResultExtras() != null && sourceResult.getResultExtras().getBoolean("need_requery", false)) {
                        if (BatchQueryTask.this.mIsCancelled) {
                            return false;
                        }
                        QueryInfo.Builder cloneFrom = new QueryInfo.Builder().cloneFrom(BatchQueryTask.this.mQueryInfo);
                        cloneFrom.removeParam("use_persistent_response");
                        QueryTask queryTask = new QueryTask(cloneFrom.build(), source, BatchQueryTask.this.mRequeryTaskReceiver, BatchQueryTask.this.mPublishHandler, 1);
                        BatchQueryTask.this.mQueryTasks[BatchQueryTask.this.mSources.size() + indexOf] = queryTask;
                        BatchQueryTask.this.mTaskExecutor.submit(queryTask);
                        SearchLog.d("QueryPackageHelper", "Submit requery task, source: [%s]", source.getName());
                    }
                }
                if (sourceResult.getSource() != null && BatchQueryTask.this.mSources.lastIndexOf(sourceResult.getSource()) == BatchQueryTask.this.mSources.size() - 1) {
                    ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.search.core.query.QueryPackageHelper.BatchQueryTask.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BatchQueryTask.this.mQueryResult.done();
                        }
                    });
                }
                return addSourceResults;
            }
        };
        public Consumer<SourceResult> mRequeryTaskReceiver = new Consumer<SourceResult>() { // from class: com.miui.gallery.search.core.query.QueryPackageHelper.BatchQueryTask.2
            @Override // com.miui.gallery.search.core.Consumer
            public boolean consume(SourceResult sourceResult) {
                int indexOf;
                if (BatchQueryTask.this.mIsCancelled) {
                    return false;
                }
                if (sourceResult == null) {
                    throw new RuntimeException("Receive a null source result!");
                }
                Source source = sourceResult.getSource();
                if (source != null && (indexOf = BatchQueryTask.this.mSources.indexOf(source)) >= 0) {
                    BatchQueryTask.this.mQueryTasks[BatchQueryTask.this.mSources.size() + indexOf] = null;
                }
                if (sourceResult.getErrorInfo() != null && SearchConstants.isErrorStatus(sourceResult.getErrorInfo().getErrorStatus())) {
                    return false;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(sourceResult);
                boolean addSourceResults = BatchQueryTask.this.mQueryResult.addSourceResults(arrayList);
                if (addSourceResults && BatchQueryTask.this.mReceiveSourceUpdates) {
                    sourceResult.registerDataSetObserver(new SourceResultDataSetObserver(sourceResult));
                    sourceResult.registerContentObserver(new SourceResultContentObserver(sourceResult));
                }
                return addSourceResults;
            }
        };

        public BatchQueryTask(final QueryInfo queryInfo, List<Source> list, TaskExecutor<QueryTask> taskExecutor, boolean z) {
            this.mQueryInfo = queryInfo;
            this.mSources = list;
            if (list.size() > 1) {
                Collections.sort(list, new Comparator<Source>() { // from class: com.miui.gallery.search.core.query.QueryPackageHelper.BatchQueryTask.3
                    @Override // java.util.Comparator
                    public int compare(Source source, Source source2) {
                        return -SearchConstants.comparePriority(source.getPriority(queryInfo), source2.getPriority(queryInfo));
                    }
                });
            }
            this.mTaskExecutor = taskExecutor;
            this.mReceiveSourceUpdates = z;
        }

        @Override // com.miui.gallery.search.core.query.ControllableTask
        public void start() {
            if (this.mIsCancelled) {
                throw new IllegalStateException("Call start() after cancel()!");
            }
            this.mPublishHandler = SearchContext.getInstance().acquirePublishHandler();
            this.mQueryTasks = new QueryTask[this.mSources.size() * 2];
            this.mQueryResult = new QueryResult(this.mQueryInfo, this.mSources);
            for (int i = 0; i < this.mSources.size(); i++) {
                querySource(i, true);
            }
        }

        @Override // com.miui.gallery.search.core.query.ControllableTask
        public boolean started() {
            return this.mQueryResult != null;
        }

        @Override // com.miui.gallery.search.core.query.ControllableTask
        public void cancel() {
            SearchLog.d("QueryPackageHelper", "Cancel batch query task for query [%s]", this.mQueryInfo);
            this.mIsCancelled = true;
            QueryTask[] queryTaskArr = this.mQueryTasks;
            if (queryTaskArr != null) {
                for (QueryTask queryTask : queryTaskArr) {
                    this.mTaskExecutor.cancel(queryTask);
                }
            }
            releasePublishHandler();
        }

        public void finalize() throws Throwable {
            releasePublishHandler();
        }

        public final void releasePublishHandler() {
            if (this.mPublishHandler != null) {
                SearchContext.getInstance().releasePublishHandler();
                this.mPublishHandler = null;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.search.core.query.ControllableTask
        /* renamed from: getResult */
        public QueryResult mo1323getResult() {
            return this.mQueryResult;
        }

        @Override // com.miui.gallery.search.core.query.ControllableTask
        public boolean isCanceled() {
            return this.mIsCancelled;
        }

        public final void querySource(int i, boolean z) {
            QueryTask queryTask = this.mQueryTasks[i];
            if (queryTask != null) {
                this.mTaskExecutor.cancel(queryTask);
            }
            QueryTask queryTask2 = new QueryTask(this.mQueryInfo, this.mSources.get(i), this.mQueryTaskReceiver, this.mPublishHandler, !z ? 1 : 0);
            this.mTaskExecutor.submit(queryTask2);
            this.mQueryTasks[i] = queryTask2;
        }

        /* loaded from: classes2.dex */
        public class SourceResultDataSetObserver extends DataSetObserver {
            public final WeakReference<SourceResult> mSourceResultWeakReference;

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
            }

            public SourceResultDataSetObserver(SourceResult sourceResult) {
                this.mSourceResultWeakReference = new WeakReference<>(sourceResult);
            }

            @Override // android.database.DataSetObserver
            public void onChanged() {
                if (this.mSourceResultWeakReference.get() == null || BatchQueryTask.this.mIsCancelled) {
                    return;
                }
                BatchQueryTask.this.onSourceResultChanged(this.mSourceResultWeakReference.get());
            }
        }

        /* loaded from: classes2.dex */
        public class SourceResultContentObserver extends ContentObserver {
            public final WeakReference<SourceResult> mSourceResultWeakReference;

            public SourceResultContentObserver(SourceResult sourceResult) {
                super(BatchQueryTask.this.mPublishHandler);
                this.mSourceResultWeakReference = new WeakReference<>(sourceResult);
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (this.mSourceResultWeakReference.get() == null || BatchQueryTask.this.mIsCancelled) {
                    return;
                }
                BatchQueryTask.this.onSourceResultChanged(this.mSourceResultWeakReference.get());
            }
        }

        public final void onSourceResultChanged(SourceResult sourceResult) {
            Source source = sourceResult.getSource();
            if (source != null) {
                int indexOf = this.mSources.indexOf(source);
                if (indexOf < 0) {
                    return;
                }
                querySource(indexOf, false);
                return;
            }
            SearchLog.w("QueryPackageHelper", "Couldn't re-query without source! [%s]", sourceResult);
        }
    }
}
