package okhttp3;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.RealCall;
import okhttp3.internal.Util;

/* loaded from: classes3.dex */
public final class Dispatcher {
    public ExecutorService executorService;
    public Runnable idleCallback;
    public int maxRequests = 64;
    public int maxRequestsPerHost = 5;
    public final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque();
    public final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque();
    public final Deque<RealCall> runningSyncCalls = new ArrayDeque();

    public synchronized ExecutorService executorService() {
        if (this.executorService == null) {
            this.executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return this.executorService;
    }

    public synchronized void enqueue(RealCall.AsyncCall asyncCall) {
        if (this.runningAsyncCalls.size() < this.maxRequests && runningCallsForHost(asyncCall) < this.maxRequestsPerHost) {
            this.runningAsyncCalls.add(asyncCall);
            executorService().execute(asyncCall);
        } else {
            this.readyAsyncCalls.add(asyncCall);
        }
    }

    public final void promoteCalls() {
        if (this.runningAsyncCalls.size() < this.maxRequests && !this.readyAsyncCalls.isEmpty()) {
            Iterator<RealCall.AsyncCall> it = this.readyAsyncCalls.iterator();
            while (it.hasNext()) {
                RealCall.AsyncCall next = it.next();
                if (runningCallsForHost(next) < this.maxRequestsPerHost) {
                    it.remove();
                    this.runningAsyncCalls.add(next);
                    executorService().execute(next);
                }
                if (this.runningAsyncCalls.size() >= this.maxRequests) {
                    return;
                }
            }
        }
    }

    public final int runningCallsForHost(RealCall.AsyncCall asyncCall) {
        int i = 0;
        for (RealCall.AsyncCall asyncCall2 : this.runningAsyncCalls) {
            if (asyncCall2.host().equals(asyncCall.host())) {
                i++;
            }
        }
        return i;
    }

    public void finished(RealCall.AsyncCall asyncCall) {
        finished(this.runningAsyncCalls, asyncCall, true);
    }

    public final <T> void finished(Deque<T> deque, T t, boolean z) {
        int runningCallsCount;
        Runnable runnable;
        synchronized (this) {
            if (!deque.remove(t)) {
                throw new AssertionError("Call wasn't in-flight!");
            }
            if (z) {
                promoteCalls();
            }
            runningCallsCount = runningCallsCount();
            runnable = this.idleCallback;
        }
        if (runningCallsCount != 0 || runnable == null) {
            return;
        }
        runnable.run();
    }

    public synchronized int runningCallsCount() {
        return this.runningAsyncCalls.size() + this.runningSyncCalls.size();
    }
}
