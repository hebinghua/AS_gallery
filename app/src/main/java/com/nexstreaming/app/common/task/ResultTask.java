package com.nexstreaming.app.common.task;

import com.nexstreaming.app.common.task.Task;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class ResultTask<T> extends Task {
    private T mResult = null;
    private long mResultTime;

    /* loaded from: classes3.dex */
    public interface OnResultAvailableListener<T> {
        void onResultAvailable(ResultTask<T> resultTask, Task.Event event, T t);
    }

    @Override // com.nexstreaming.app.common.task.Task
    /* renamed from: setTimeout */
    public ResultTask<T> mo1854setTimeout(long j) {
        super.mo1854setTimeout(j);
        return this;
    }

    @Override // com.nexstreaming.app.common.task.Task
    /* renamed from: onFailure */
    public ResultTask<T> mo1851onFailure(Task.OnFailListener onFailListener) {
        super.mo1851onFailure(onFailListener);
        return this;
    }

    @Override // com.nexstreaming.app.common.task.Task
    /* renamed from: onCancel */
    public ResultTask<T> mo1849onCancel(Task.OnTaskEventListener onTaskEventListener) {
        super.mo1849onCancel(onTaskEventListener);
        return this;
    }

    @Override // com.nexstreaming.app.common.task.Task
    /* renamed from: onComplete */
    public ResultTask<T> mo1850onComplete(Task.OnTaskEventListener onTaskEventListener) {
        super.mo1850onComplete(onTaskEventListener);
        return this;
    }

    @Override // com.nexstreaming.app.common.task.Task
    /* renamed from: onProgress */
    public ResultTask<T> mo1852onProgress(Task.OnProgressListener onProgressListener) {
        super.mo1852onProgress(onProgressListener);
        return this;
    }

    @Override // com.nexstreaming.app.common.task.Task
    /* renamed from: onSuccess */
    public ResultTask<T> mo1853onSuccess(Task.OnTaskEventListener onTaskEventListener) {
        super.mo1853onSuccess(onTaskEventListener);
        return this;
    }

    public long getTimeSinceResult() {
        if (!didSignalEvent(Task.Event.RESULT_AVAILABLE)) {
            return -1L;
        }
        return (System.nanoTime() - this.mResultTime) / 1000000;
    }

    public void setResult(T t) {
        boolean z = this.mResult != t;
        this.mResult = t;
        this.mResultTime = System.nanoTime();
        signalEvent(Task.Event.RESULT_AVAILABLE);
        if (z) {
            signalEvent(Task.Event.UPDATE_OR_RESULT_AVAILABLE);
        }
    }

    public T getResult() {
        if (!didSignalEvent(Task.Event.RESULT_AVAILABLE)) {
            throw new RuntimeException("Result not available");
        }
        return this.mResult;
    }

    public void sendResult(T t) {
        setResult(t);
        super.signalEvent(Task.Event.RESULT_AVAILABLE, Task.Event.SUCCESS, Task.Event.COMPLETE);
    }

    public ResultTask<T> onResultAvailable(final OnResultAvailableListener<T> onResultAvailableListener) {
        onEvent(Task.Event.RESULT_AVAILABLE, new Task.OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.ResultTask.1
            @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
            public void onTaskEvent(Task task, Task.Event event) {
                ResultTask<T> resultTask = (ResultTask) task;
                onResultAvailableListener.onResultAvailable(resultTask, event, resultTask.getResult());
                ResultTask.this.removeListenerForFail();
            }
        });
        return this;
    }

    public ResultTask<T> onUpdateOrResultAvailable(final OnResultAvailableListener<T> onResultAvailableListener) {
        Task.Event event = Task.Event.UPDATE_OR_RESULT_AVAILABLE;
        onEvent(event, new Task.OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.ResultTask.2
            @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
            public void onTaskEvent(Task task, Task.Event event2) {
                ResultTask<T> resultTask = (ResultTask) task;
                onResultAvailableListener.onResultAvailable(resultTask, event2, resultTask.getResult());
            }
        });
        if (didSignalEvent(Task.Event.RESULT_AVAILABLE)) {
            onResultAvailableListener.onResultAvailable(this, event, this.mResult);
        }
        return this;
    }

    public T awaitResult() throws Task.TaskErrorException {
        makeWaitable();
        awaitTaskCompletion();
        if (didSignalEvent(Task.Event.FAIL)) {
            throw new Task.TaskErrorException(getTaskError());
        }
        if (!didSignalEvent(Task.Event.RESULT_AVAILABLE)) {
            throw new Task.TaskErrorException(Task.NO_RESULT_AVAILABLE);
        }
        return getResult();
    }

    public static <T> ResultTask<List<T>> combineResults(ResultTask<Collection<T>>... resultTaskArr) {
        return combineResults(Arrays.asList(resultTaskArr));
    }

    public static <T> ResultTask<T> completedResultTask(T t) {
        ResultTask<T> resultTask = new ResultTask<>();
        resultTask.sendResult(t);
        return resultTask;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <INDIVIDUAL_RESULT_TYPE, RESULT_COLLECTION extends Collection<INDIVIDUAL_RESULT_TYPE>, TASK_COLLECTION extends Collection<ResultTask<RESULT_COLLECTION>>> ResultTask<List<INDIVIDUAL_RESULT_TYPE>> combineResults(final TASK_COLLECTION task_collection) {
        ResultTask<List<INDIVIDUAL_RESULT_TYPE>> resultTask = new ResultTask<>();
        final HashMap hashMap = new HashMap();
        Iterator it = task_collection.iterator();
        while (it.hasNext()) {
            ((ResultTask) it.next()).onResultAvailable(new OnResultAvailableListener<RESULT_COLLECTION>() { // from class: com.nexstreaming.app.common.task.ResultTask.4
                /* JADX WARN: Incorrect types in method signature: (Lcom/nexstreaming/app/common/task/ResultTask<TRESULT_COLLECTION;>;Lcom/nexstreaming/app/common/task/Task$Event;TRESULT_COLLECTION;)V */
                @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
                /* renamed from: a */
                public void onResultAvailable(ResultTask resultTask2, Task.Event event, Collection collection) {
                    if (ResultTask.this.isRunning()) {
                        hashMap.put(resultTask2, collection);
                        if (hashMap.size() < task_collection.size()) {
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        for (ResultTask resultTask3 : task_collection) {
                            arrayList.addAll((Collection) hashMap.get(resultTask3));
                        }
                        ResultTask.this.sendResult(arrayList);
                    }
                }
            }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.app.common.task.ResultTask.3
                @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                    ResultTask.this.sendFailure(taskError);
                    hashMap.clear();
                }
            });
        }
        return resultTask;
    }

    public static <T> ResultTask<T> failedResultTask(Task.TaskError taskError) {
        ResultTask<T> resultTask = new ResultTask<>();
        resultTask.sendFailure(taskError);
        return resultTask;
    }
}
