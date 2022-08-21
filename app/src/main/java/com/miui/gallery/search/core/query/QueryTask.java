package com.miui.gallery.search.core.query;

import android.os.Handler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.search.core.Consumer;
import com.miui.gallery.search.core.ConsumerUtils;
import com.miui.gallery.search.core.QueryInfo;
import com.miui.gallery.search.core.context.PriorityTaskExecutor;
import com.miui.gallery.search.core.result.SuggestionResult;
import com.miui.gallery.search.core.source.SuggestionResultProvider;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.stat.SamplingStatHelper;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class QueryTask<C extends SuggestionResult> extends PriorityTaskExecutor.PriorityTask {
    public final Consumer<C> mConsumer;
    public final Handler mHandler;
    public final SuggestionResultProvider<C> mProvider;
    public final QueryInfo mQueryInfo;

    public QueryTask(QueryInfo queryInfo, SuggestionResultProvider<C> suggestionResultProvider, Consumer<C> consumer, Handler handler, int i) {
        this.mQueryInfo = queryInfo;
        this.mProvider = suggestionResultProvider;
        this.mConsumer = consumer;
        this.mHandler = handler;
        this.mPriority = i;
    }

    @Override // com.miui.gallery.concurrent.ThreadPool.Job
    /* renamed from: run  reason: collision with other method in class */
    public Void mo1807run(ThreadPool.JobContext jobContext) {
        String str;
        if (jobContext.isCancelled()) {
            return null;
        }
        setExcuteTime(System.currentTimeMillis());
        C mo1333getSuggestions = this.mProvider.mo1333getSuggestions(this.mQueryInfo);
        if (jobContext.isCancelled()) {
            return null;
        }
        ConsumerUtils.consumeAsync(this.mHandler, this.mConsumer, mo1333getSuggestions);
        setFinishTime(System.currentTimeMillis());
        String queryTask = toString();
        String valueOf = String.valueOf(getFinishTime() - getExcuteTime());
        if (mo1333getSuggestions == null || mo1333getSuggestions.isEmpty() || mo1333getSuggestions.getData() == null) {
            str = "is empty";
        } else {
            str = mo1333getSuggestions.getData().getCount() + " items@" + Integer.toHexString(mo1333getSuggestions.hashCode());
        }
        SearchLog.d("QueryTask", "%s cost %sms, result %s", queryTask, valueOf, str);
        HashMap hashMap = new HashMap();
        hashMap.put("trigger_time", String.valueOf(getSubmitTime() - getNewTime()));
        hashMap.put("elapse_time", String.valueOf(getExcuteTime() - getSubmitTime()));
        hashMap.put("cost_time", String.valueOf(getFinishTime() - getExcuteTime()));
        SamplingStatHelper.recordCountEvent(MiStat.Event.SEARCH, "search_query_task", hashMap);
        return null;
    }

    public String toString() {
        return "From " + this.mProvider + "[" + this.mQueryInfo + "]";
    }
}
