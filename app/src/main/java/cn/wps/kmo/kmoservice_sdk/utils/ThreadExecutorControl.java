package cn.wps.kmo.kmoservice_sdk.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ThreadExecutorControl {
    public ExecutorService mCorTaskExecutor;

    public ExecutorService getCorTaskExecutor() {
        if (this.mCorTaskExecutor == null) {
            this.mCorTaskExecutor = createExecutor();
        }
        return this.mCorTaskExecutor;
    }

    public final ExecutorService createExecutor() {
        return new ThreadPoolExecutor(7, 7, 0L, TimeUnit.MILLISECONDS, new LILOLinkedBlockingDeque());
    }

    /* loaded from: classes.dex */
    public class LILOLinkedBlockingDeque<T> extends LinkedBlockingDeque<T> {
        private static final long serialVersionUID = -4114786347960826193L;

        public LILOLinkedBlockingDeque() {
        }

        @Override // java.util.concurrent.LinkedBlockingDeque, java.util.Queue, java.util.concurrent.BlockingDeque, java.util.concurrent.BlockingQueue, java.util.Deque
        public boolean offer(T t) {
            return super.offerLast(t);
        }

        @Override // java.util.concurrent.LinkedBlockingDeque, java.util.AbstractQueue, java.util.Queue, java.util.concurrent.BlockingDeque, java.util.Deque
        public T remove() {
            return (T) super.removeLast();
        }
    }
}
