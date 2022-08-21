package com.xiaomi.milab.videosdk.utils;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes3.dex */
public final class BackgroundExecutor {
    private static final ThreadLocal<String> CURRENT_SERIAL;
    public static final Executor DEFAULT_EXECUTOR;
    private static final String TAG = "BackgroundExecutor";
    private static final List<Task> TASKS;
    private static Executor executor;

    static {
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        DEFAULT_EXECUTOR = newScheduledThreadPool;
        executor = newScheduledThreadPool;
        TASKS = new ArrayList();
        CURRENT_SERIAL = new ThreadLocal<>();
    }

    private BackgroundExecutor() {
    }

    private static Future<?> directExecute(Runnable runnable, long j) {
        if (j > 0) {
            Executor executor2 = executor;
            if (!(executor2 instanceof ScheduledExecutorService)) {
                throw new IllegalArgumentException("The executor set does not support scheduling");
            }
            return ((ScheduledExecutorService) executor2).schedule(runnable, j, TimeUnit.MILLISECONDS);
        }
        Executor executor3 = executor;
        if (executor3 instanceof ExecutorService) {
            return ((ExecutorService) executor3).submit(runnable);
        }
        executor3.execute(runnable);
        return null;
    }

    public static synchronized void execute(Task task) {
        synchronized (BackgroundExecutor.class) {
            Future<?> future = null;
            if (task.serial == null || !hasSerialRunning(task.serial)) {
                task.executionAsked = true;
                future = directExecute(task, task.remainingDelay);
            }
            if ((task.id != null || task.serial != null) && !task.managed.get()) {
                task.future = future;
                TASKS.add(task);
            }
        }
    }

    private static boolean hasSerialRunning(String str) {
        for (Task task : TASKS) {
            if (task.executionAsked && str.equals(task.serial)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Task take(String str) {
        int size = TASKS.size();
        for (int i = 0; i < size; i++) {
            List<Task> list = TASKS;
            if (str.equals(list.get(i).serial)) {
                return list.remove(i);
            }
        }
        return null;
    }

    public static synchronized void cancelAll(String str, boolean z) {
        synchronized (BackgroundExecutor.class) {
            for (int size = TASKS.size() - 1; size >= 0; size--) {
                List<Task> list = TASKS;
                Task task = list.get(size);
                if (str.equals(task.id)) {
                    if (task.future != null) {
                        task.future.cancel(z);
                        if (!task.managed.getAndSet(true)) {
                            task.postExecute();
                        }
                    } else if (task.executionAsked) {
                        Log.w(TAG, "A task with id " + task.id + " cannot be cancelled (the executor set does not support it)");
                    } else {
                        list.remove(size);
                    }
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Task implements Runnable {
        private boolean executionAsked;
        private Future<?> future;
        private String id;
        private AtomicBoolean managed = new AtomicBoolean();
        private long remainingDelay;
        private String serial;
        private long targetTimeMillis;

        public abstract void execute();

        public Task(String str, long j, String str2) {
            if (!"".equals(str)) {
                this.id = str;
            }
            if (j > 0) {
                this.remainingDelay = j;
                this.targetTimeMillis = System.currentTimeMillis() + j;
            }
            if (!"".equals(str2)) {
                this.serial = str2;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.managed.getAndSet(true)) {
                return;
            }
            try {
                BackgroundExecutor.CURRENT_SERIAL.set(this.serial);
                execute();
            } finally {
                postExecute();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postExecute() {
            Task take;
            if (this.id == null && this.serial == null) {
                return;
            }
            BackgroundExecutor.CURRENT_SERIAL.set(null);
            synchronized (BackgroundExecutor.class) {
                BackgroundExecutor.TASKS.remove(this);
                String str = this.serial;
                if (str != null && (take = BackgroundExecutor.take(str)) != null) {
                    if (take.remainingDelay != 0) {
                        take.remainingDelay = Math.max(0L, this.targetTimeMillis - System.currentTimeMillis());
                    }
                    BackgroundExecutor.execute(take);
                }
            }
        }
    }
}
