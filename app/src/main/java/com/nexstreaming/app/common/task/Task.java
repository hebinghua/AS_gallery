package com.nexstreaming.app.common.task;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.xiaomi.stat.b.h;
import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes3.dex */
public class Task {
    public static final int INVALID_TASK_ID = 0;
    private static int sTaskId;
    private AtomicBoolean mCancelRequested;
    private boolean mCancellable;
    private Handler mHandler;
    private CountDownLatch mLatch;
    private List<Pair<OnTaskEventListener, Event>> mListeners;
    private final long mLongTaskId;
    private int mMaxProgress;
    private int mProgress;
    private boolean mProgressSignalled;
    private boolean mRegistered;
    private EnumSet<Event> mSignalledEvents;
    private TaskError mTaskError;
    private final int mTaskId;
    private Runnable mTimeoutRunnable;
    private static long sLongTaskId = (SystemClock.uptimeMillis() & (-1)) << 32;
    private static Map<Long, WeakReference<Task>> sLongIdTaskMap = new HashMap();
    private static SparseArray<WeakReference<Task>> sIntIdTaskMap = new SparseArray<>();
    private static int sRegisterCount = 0;
    public static final Task COMPLETED_TASK = new Task(Event.COMPLETE, Event.SUCCESS);
    public static final TaskError UNKNOWN_ERROR = new TaskError() { // from class: com.nexstreaming.app.common.task.Task.1
        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public Exception getException() {
            return null;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getMessage() {
            return "Unknown";
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getLocalizedMessage(Context context) {
            return getMessage();
        }
    };
    public static final TaskError TIMEOUT = new TaskError() { // from class: com.nexstreaming.app.common.task.Task.5
        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public Exception getException() {
            return null;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getMessage() {
            return "Timeout";
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getLocalizedMessage(Context context) {
            return getMessage();
        }
    };
    public static final TaskError NO_RESULT_AVAILABLE = new TaskError() { // from class: com.nexstreaming.app.common.task.Task.6
        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public Exception getException() {
            return null;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getMessage() {
            return "No result available";
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getLocalizedMessage(Context context) {
            return getMessage();
        }
    };

    /* loaded from: classes3.dex */
    public enum Event {
        SUCCESS,
        FAIL,
        COMPLETE,
        CANCEL,
        PROGRESS,
        RESULT_AVAILABLE,
        UPDATE_OR_RESULT_AVAILABLE
    }

    /* loaded from: classes3.dex */
    public interface OnFailListener {
        void onFail(Task task, Event event, TaskError taskError);
    }

    /* loaded from: classes3.dex */
    public interface OnProgressListener {
        void onProgress(Task task, Event event, int i, int i2);
    }

    /* loaded from: classes3.dex */
    public interface OnTaskEventListener {
        void onTaskEvent(Task task, Event event);
    }

    /* loaded from: classes3.dex */
    public interface TaskError {
        Exception getException();

        String getLocalizedMessage(Context context);

        String getMessage();
    }

    /* loaded from: classes3.dex */
    public static class TaskErrorException extends Exception {
        private static final long serialVersionUID = 1;
        private final TaskError taskError;

        public TaskErrorException(TaskError taskError) {
            super(taskError.getMessage());
            this.taskError = taskError;
        }

        public TaskErrorException(TaskError taskError, Throwable th) {
            super(taskError.getMessage(), th);
            this.taskError = taskError;
        }

        public TaskError getTaskError() {
            return this.taskError;
        }
    }

    /* loaded from: classes3.dex */
    public static class SimpleTaskError implements TaskError {
        private final Exception mException;
        private final String mMessage;

        public SimpleTaskError(Exception exc, String str) {
            this.mException = exc;
            this.mMessage = str;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public Exception getException() {
            return this.mException;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getMessage() {
            String str = this.mMessage;
            return str != null ? str : this.mException.getMessage() != null ? this.mException.getMessage() : "Unknown";
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getLocalizedMessage(Context context) {
            String str = this.mMessage;
            if (str != null) {
                return str;
            }
            if (this.mException.getLocalizedMessage() != null) {
                return this.mException.getLocalizedMessage();
            }
            return this.mException.getMessage() != null ? this.mException.getMessage() : "Unknown";
        }

        public String toString() {
            if (getException() != null) {
                return "<SimpleTaskError: " + getException().getClass().getName() + ">";
            }
            return "<SimpleTaskError: " + getMessage() + ">";
        }
    }

    public String toString() {
        Iterator it = this.mSignalledEvents.iterator();
        boolean z = true;
        String str = "";
        while (it.hasNext()) {
            Event event = (Event) it.next();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(z ? "" : ",");
            sb.append(event.name());
            str = sb.toString();
            z = false;
        }
        if (str.length() < 1) {
            str = "none";
        }
        if (this.mProgressSignalled) {
            return "[Task " + this.mTaskId + ": progress=" + this.mProgress + h.g + this.mMaxProgress + " events=" + str + "]";
        }
        return "[Task " + this.mTaskId + ": events=" + str + "]";
    }

    public static TaskError makeTaskError(Exception exc) {
        return new SimpleTaskError(exc, null);
    }

    public static TaskError makeTaskError(String str, Exception exc) {
        return new SimpleTaskError(exc, str);
    }

    public static TaskError makeTaskError(String str) {
        return new SimpleTaskError(null, str);
    }

    public static Task makeFailedTask(Exception exc) {
        Task task = new Task();
        task.sendFailure(new SimpleTaskError(exc, null));
        return task;
    }

    public static Task makeFailedTask(String str, Exception exc) {
        Task task = new Task();
        task.sendFailure(new SimpleTaskError(exc, str));
        return task;
    }

    public static Task makeFailedTask(String str) {
        Task task = new Task();
        task.sendFailure(new SimpleTaskError(null, str));
        return task;
    }

    public static Task makeFailedTask(TaskError taskError) {
        Task task = new Task();
        task.sendFailure(taskError);
        return task;
    }

    /* renamed from: setTimeout */
    public Task mo1854setTimeout(long j) {
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        Runnable runnable = this.mTimeoutRunnable;
        if (runnable == null) {
            this.mTimeoutRunnable = new Runnable() { // from class: com.nexstreaming.app.common.task.Task.7
                @Override // java.lang.Runnable
                public void run() {
                    if (Task.this.isRunning()) {
                        Task.this.sendFailure(Task.TIMEOUT);
                    }
                    Task.this.mTimeoutRunnable = null;
                    Task.this.mHandler = null;
                }
            };
        } else {
            this.mHandler.removeCallbacks(runnable);
        }
        this.mHandler.postDelayed(this.mTimeoutRunnable, j);
        return this;
    }

    public void cancel() {
        if (!this.mCancellable) {
            throw new RuntimeException("Not a cancellable task");
        }
        this.mCancelRequested.set(true);
    }

    public boolean isCancelRequested() {
        if (!this.mCancellable) {
            throw new RuntimeException("Not a cancellable task");
        }
        return this.mCancelRequested.get();
    }

    public boolean isCancellable() {
        return this.mCancellable;
    }

    public void setCancellable(boolean z) {
        this.mCancellable = z;
        if (z && this.mCancelRequested == null) {
            this.mCancelRequested = new AtomicBoolean(false);
        } else if (z) {
        } else {
            this.mCancelRequested = null;
        }
    }

    public Task() {
        this.mListeners = new LinkedList();
        this.mSignalledEvents = EnumSet.noneOf(Event.class);
        this.mProgress = 0;
        this.mMaxProgress = 0;
        this.mProgressSignalled = false;
        this.mRegistered = false;
        this.mTaskError = null;
        this.mCancellable = false;
        this.mHandler = null;
        this.mTimeoutRunnable = null;
        this.mLatch = null;
        int i = sTaskId + 1;
        sTaskId = i;
        long j = sLongTaskId + 1;
        sLongTaskId = j;
        if (i == 0) {
            sTaskId = i + 1;
        }
        if (j == 0) {
            sLongTaskId = j + 1;
        }
        this.mTaskId = sTaskId;
        this.mLongTaskId = sLongTaskId;
    }

    private Task(Event... eventArr) {
        this();
        signalEvent(eventArr);
    }

    public synchronized void makeWaitable() {
        if (this.mLatch == null) {
            this.mLatch = new CountDownLatch(1);
        }
        if (!isRunning()) {
            signalCompletionEvent();
        }
    }

    public Task awaitTaskCompletion() {
        if (this.mLatch == null) {
            throw new RuntimeException("Not a waitable task");
        }
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            throw new RuntimeException("Would block on UI thread");
        }
        boolean z = false;
        while (true) {
            try {
                this.mLatch.await();
                break;
            } catch (InterruptedException unused) {
                z = true;
            } catch (Throwable th) {
                if (z) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    private void signalCompletionEvent() {
        CountDownLatch countDownLatch = this.mLatch;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public void register() {
        if (this.mRegistered) {
            return;
        }
        sIntIdTaskMap.put(this.mTaskId, new WeakReference<>(this));
        sLongIdTaskMap.put(Long.valueOf(this.mLongTaskId), new WeakReference<>(this));
        int i = sRegisterCount + 1;
        sRegisterCount = i;
        if (i > 32) {
            ArrayList<Integer> arrayList = new ArrayList();
            int size = sIntIdTaskMap.size();
            for (int i2 = 0; i2 < size; i2++) {
                int keyAt = sIntIdTaskMap.keyAt(i2);
                WeakReference<Task> valueAt = sIntIdTaskMap.valueAt(i2);
                if (valueAt == null || valueAt.get() == null) {
                    arrayList.add(Integer.valueOf(keyAt));
                }
            }
            for (Integer num : arrayList) {
                sIntIdTaskMap.remove(num.intValue());
            }
            ArrayList<Long> arrayList2 = new ArrayList();
            for (Map.Entry<Long, WeakReference<Task>> entry : sLongIdTaskMap.entrySet()) {
                if (entry.getValue() == null || entry.getValue().get() == null) {
                    arrayList2.add(entry.getKey());
                }
            }
            for (Long l : arrayList2) {
                sLongIdTaskMap.remove(l);
            }
            sRegisterCount = 0;
        }
        this.mRegistered = true;
    }

    public static Task findTaskById(int i) {
        WeakReference<Task> weakReference = sIntIdTaskMap.get(i);
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public static Task findTaskByLongId(long j) {
        WeakReference<Task> weakReference = sLongIdTaskMap.get(Long.valueOf(j));
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public long getLongTaskId() {
        return this.mLongTaskId;
    }

    public Task onEvent(Event event, OnTaskEventListener onTaskEventListener) {
        if (event != null && onTaskEventListener != null) {
            if (didSignalEvent(event)) {
                onTaskEventListener.onTaskEvent(this, event);
                return this;
            }
            this.mListeners.add(new Pair<>(onTaskEventListener, event));
        }
        return this;
    }

    public boolean isRepeatableEvent(Event event) {
        return event == Event.PROGRESS || event == Event.UPDATE_OR_RESULT_AVAILABLE;
    }

    public boolean didSignalEvent(Event event) {
        return this.mSignalledEvents.contains(event);
    }

    /* renamed from: onSuccess */
    public Task mo1853onSuccess(OnTaskEventListener onTaskEventListener) {
        return onEvent(Event.SUCCESS, onTaskEventListener);
    }

    /* renamed from: onFailure */
    public Task mo1851onFailure(final OnFailListener onFailListener) {
        return onEvent(Event.FAIL, new OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.Task.8
            @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
            public void onTaskEvent(Task task, Event event) {
                onFailListener.onFail(task, event, Task.this.getTaskError());
            }
        });
    }

    public Task onFailure(final Task task) {
        if (task == this) {
            throw new InvalidParameterException();
        }
        return onEvent(Event.FAIL, new OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.Task.9
            @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
            public void onTaskEvent(Task task2, Event event) {
                task.sendFailure(Task.this.getTaskError());
            }
        });
    }

    /* renamed from: onComplete */
    public Task mo1850onComplete(OnTaskEventListener onTaskEventListener) {
        return onEvent(Event.COMPLETE, onTaskEventListener);
    }

    /* renamed from: onCancel */
    public Task mo1849onCancel(OnTaskEventListener onTaskEventListener) {
        return onEvent(Event.CANCEL, onTaskEventListener);
    }

    /* renamed from: onProgress */
    public Task mo1852onProgress(final OnProgressListener onProgressListener) {
        return onEvent(Event.PROGRESS, new OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.Task.10
            @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
            public void onTaskEvent(Task task, Event event) {
                onProgressListener.onProgress(task, event, Task.this.getProgress(), Task.this.getMaxProgress());
            }
        });
    }

    public void setProgress(int i, int i2) {
        this.mProgress = i;
        this.mMaxProgress = i2;
        this.mProgressSignalled = true;
        signalOneEvent(Event.PROGRESS);
    }

    public boolean isProgressAvailable() {
        return this.mProgressSignalled;
    }

    public int getProgress() {
        if (!this.mProgressSignalled) {
            throw new ProgressNotAvailableException();
        }
        return this.mProgress;
    }

    public int getMaxProgress() {
        if (!this.mProgressSignalled) {
            throw new ProgressNotAvailableException();
        }
        return this.mMaxProgress;
    }

    public TaskError getTaskError() {
        if (!didSignalEvent(Event.FAIL)) {
            throw new RuntimeException("Error not available (task did not fail)");
        }
        TaskError taskError = this.mTaskError;
        return taskError == null ? UNKNOWN_ERROR : taskError;
    }

    public void setTaskError(TaskError taskError) {
        this.mTaskError = taskError;
    }

    public void signalEvent(Event... eventArr) {
        for (Event event : eventArr) {
            if (event != Event.PROGRESS) {
                signalOneEvent(event);
            }
        }
    }

    public void sendFailure(TaskError taskError) {
        setTaskError(taskError);
        signalEvent(Event.FAIL);
    }

    public synchronized void removeListenerForFail() {
        ArrayList arrayList = new ArrayList(8);
        for (Pair<OnTaskEventListener, Event> pair : this.mListeners) {
            if (pair.second == Event.FAIL) {
                arrayList.add(pair);
            }
        }
        this.mListeners.removeAll(arrayList);
    }

    private synchronized void signalOneEvent(Event event) {
        if (event != null) {
            if (!didSignalEvent(event)) {
                Event event2 = Event.CANCEL;
                if (didSignalEvent(event2)) {
                    Log.w("Task", "Ingoring attempt to signal a cancelled task.");
                    return;
                }
                Event event3 = Event.FAIL;
                if (event == event3 && didSignalEvent(Event.SUCCESS)) {
                    Log.w("Task", "Ingoring attempt to signal failure on task that already succeeded.");
                    return;
                }
                if (event == Event.COMPLETE) {
                    Event event4 = Event.SUCCESS;
                    if (!didSignalEvent(event4) && !didSignalEvent(event3) && !didSignalEvent(event2)) {
                        signalOneEvent(event4);
                    }
                }
                if (!isRepeatableEvent(event)) {
                    this.mSignalledEvents.add(event);
                }
                ArrayList<Pair> arrayList = new ArrayList(8);
                for (Pair<OnTaskEventListener, Event> pair : this.mListeners) {
                    if (pair.second == event) {
                        arrayList.add(pair);
                    }
                }
                if (!isRepeatableEvent(event)) {
                    this.mListeners.removeAll(arrayList);
                }
                for (Pair pair2 : arrayList) {
                    ((OnTaskEventListener) pair2.first).onTaskEvent(this, event);
                }
                if (event == Event.COMPLETE || event == Event.FAIL || event == Event.CANCEL) {
                    signalCompletionEvent();
                }
            }
        }
    }

    public boolean isRunning() {
        return !didSignalEvent(Event.COMPLETE) && !didSignalEvent(Event.FAIL) && !didSignalEvent(Event.CANCEL);
    }

    public boolean isComplete() {
        return didSignalEvent(Event.COMPLETE);
    }

    /* loaded from: classes3.dex */
    public static class ProgressNotAvailableException extends RuntimeException {
        private static final long serialVersionUID = 1;

        public ProgressNotAvailableException() {
        }

        public ProgressNotAvailableException(String str, Throwable th) {
            super(str, th);
        }

        public ProgressNotAvailableException(String str) {
            super(str);
        }

        public ProgressNotAvailableException(Throwable th) {
            super(th);
        }
    }

    /* loaded from: classes3.dex */
    public static class MultiplexTask extends Task {
        public final Task[] tasks;

        /* loaded from: classes3.dex */
        public interface OnAllTasksSignalledListener {
            void onAllTasksSignalled(MultiplexTask multiplexTask, Task[] taskArr, Event event);
        }

        private MultiplexTask(Task[] taskArr) {
            this.tasks = taskArr;
        }

        public Task[] getTasks() {
            return this.tasks;
        }

        public Task onAllTasksSignalled(final OnAllTasksSignalledListener onAllTasksSignalledListener) {
            return onEvent(Event.COMPLETE, new OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.Task.MultiplexTask.1
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task, Event event) {
                    OnAllTasksSignalledListener onAllTasksSignalledListener2 = onAllTasksSignalledListener;
                    MultiplexTask multiplexTask = MultiplexTask.this;
                    onAllTasksSignalledListener2.onAllTasksSignalled(multiplexTask, multiplexTask.tasks, event);
                }
            });
        }
    }

    public static Task combinedTask(Collection<? extends Task> collection) {
        final Task task = new Task();
        final ArrayList<Task> arrayList = new ArrayList(collection);
        for (Task task2 : arrayList) {
            task2.mo1852onProgress(new OnProgressListener() { // from class: com.nexstreaming.app.common.task.Task.11
                @Override // com.nexstreaming.app.common.task.Task.OnProgressListener
                public void onProgress(Task task3, Event event, int i, int i2) {
                    Iterator it = arrayList.iterator();
                    boolean z = false;
                    int i3 = 0;
                    int i4 = 0;
                    while (true) {
                        if (!it.hasNext()) {
                            z = true;
                            break;
                        }
                        Task task4 = (Task) it.next();
                        if (!task4.isProgressAvailable()) {
                            break;
                        }
                        i3 += task4.getProgress();
                        i4 += task4.getMaxProgress();
                    }
                    if (z) {
                        task.setProgress(i3, i4);
                    }
                }
            });
            task2.mo1850onComplete(new OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.Task.12
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task3, Event event) {
                    boolean z;
                    Iterator it = arrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z = true;
                            break;
                        } else if (!((Task) it.next()).didSignalEvent(Event.COMPLETE)) {
                            z = false;
                            break;
                        }
                    }
                    if (z) {
                        task.signalEvent(Event.COMPLETE);
                    }
                }
            });
            task2.mo1853onSuccess(new OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.Task.2
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task3, Event event) {
                    boolean z;
                    Iterator it = arrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z = true;
                            break;
                        } else if (!((Task) it.next()).didSignalEvent(Event.SUCCESS)) {
                            z = false;
                            break;
                        }
                    }
                    if (z) {
                        task.signalEvent(Event.SUCCESS);
                    }
                }
            });
            task2.mo1851onFailure(new OnFailListener() { // from class: com.nexstreaming.app.common.task.Task.3
                @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                public void onFail(Task task3, Event event, TaskError taskError) {
                    Task.this.sendFailure(taskError);
                }
            });
        }
        return task;
    }

    public static MultiplexTask waitForAll(final Task... taskArr) {
        final MultiplexTask multiplexTask = new MultiplexTask(taskArr);
        final Event[] eventArr = new Event[taskArr.length];
        for (int i = 0; i < taskArr.length; i++) {
            eventArr[i] = null;
        }
        OnTaskEventListener onTaskEventListener = new OnTaskEventListener() { // from class: com.nexstreaming.app.common.task.Task.4
            @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
            public void onTaskEvent(Task task, Event event) {
                Event event2 = Event.COMPLETE;
                int i2 = 0;
                while (true) {
                    Task[] taskArr2 = taskArr;
                    if (i2 >= taskArr2.length) {
                        break;
                    } else if (taskArr2[i2] == task) {
                        eventArr[i2] = event;
                        break;
                    } else {
                        i2++;
                    }
                }
                for (int i3 = 0; i3 < taskArr.length; i3++) {
                    Event[] eventArr2 = eventArr;
                    if (eventArr2[i3] == null) {
                        return;
                    }
                    Event event3 = eventArr2[i3];
                    Event event4 = Event.FAIL;
                    if (event3 == event4) {
                        event2 = event4;
                    } else {
                        Event event5 = eventArr2[i3];
                        Event event6 = Event.CANCEL;
                        if (event5 == event6 && event2 == Event.COMPLETE) {
                            event2 = event6;
                        }
                    }
                }
                multiplexTask.signalEvent(event2);
            }
        };
        for (Task task : taskArr) {
            task.onEvent(Event.COMPLETE, onTaskEventListener);
            task.onEvent(Event.FAIL, onTaskEventListener);
            task.onEvent(Event.CANCEL, onTaskEventListener);
        }
        return multiplexTask;
    }
}
