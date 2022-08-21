package miuix.pickerwidget.internal.util.async;

import android.os.HandlerThread;
import android.os.Looper;
import android.util.ArrayMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class WorkerThreads {
    public static final Map<String, ThreadWrapper> sWorkers = new ArrayMap();

    /* loaded from: classes3.dex */
    public static class ThreadWrapper {
        public int refCount = 1;
        public final HandlerThread thread;

        public ThreadWrapper(String str) {
            HandlerThread handlerThread = new HandlerThread(str);
            this.thread = handlerThread;
            handlerThread.start();
        }
    }

    public static synchronized Looper aquireWorker(String str) {
        Looper looper;
        synchronized (WorkerThreads.class) {
            Map<String, ThreadWrapper> map = sWorkers;
            ThreadWrapper threadWrapper = map.get(str);
            if (threadWrapper == null) {
                threadWrapper = new ThreadWrapper(str);
                map.put(str, threadWrapper);
            } else {
                threadWrapper.refCount++;
            }
            looper = threadWrapper.thread.getLooper();
        }
        return looper;
    }

    public static synchronized void releaseWorker(String str) {
        synchronized (WorkerThreads.class) {
            Map<String, ThreadWrapper> map = sWorkers;
            ThreadWrapper threadWrapper = map.get(str);
            if (threadWrapper != null) {
                int i = threadWrapper.refCount - 1;
                threadWrapper.refCount = i;
                if (i == 0) {
                    map.remove(str);
                    threadWrapper.thread.quitSafely();
                }
            }
        }
    }
}
