package com.miui.gallery.magic.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.xiaomi.stat.d;
import java.lang.ref.WeakReference;
import java.util.Map;

/* loaded from: classes2.dex */
public class MagicThreadHandler {
    public static MagicThreadHandler sThreadHandler;
    public Handler mBackgroundHandler;
    public Handler mHandler;
    public HandlerThread mProfileHandlerThread;

    public MagicThreadHandler() {
        this.mHandler = null;
        this.mBackgroundHandler = null;
        this.mProfileHandlerThread = null;
        HandlerThread handlerThread = new HandlerThread("MagicThreadHandler");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        HandlerThread handlerThread2 = new HandlerThread("Profile_Handler_Thread");
        this.mProfileHandlerThread = handlerThread2;
        handlerThread2.start();
        this.mBackgroundHandler = new BackgroundHandler(this.mProfileHandlerThread.getLooper(), handlerThread.getName());
    }

    public static MagicThreadHandler getInstance() {
        if (sThreadHandler == null) {
            sThreadHandler = new MagicThreadHandler();
        }
        return sThreadHandler;
    }

    public static void post(Runnable runnable) {
        getInstance().mHandler.post(new WrapperRunnable(runnable, getInstance().mBackgroundHandler));
    }

    /* loaded from: classes2.dex */
    public static class WrapperRunnable implements Runnable {
        public WeakReference<Handler> mProfileReference;
        public Runnable mRunnable;

        public WrapperRunnable(Runnable runnable, Handler handler) {
            if (runnable == null) {
                throw new RuntimeException("runnable must be not null.");
            }
            this.mRunnable = runnable;
            this.mProfileReference = new WeakReference<>(handler);
        }

        @Override // java.lang.Runnable
        public void run() {
            Handler handler;
            Handler handler2;
            WeakReference<Handler> weakReference = this.mProfileReference;
            if (weakReference != null && (handler2 = weakReference.get()) != null) {
                if (handler2.hasMessages(1000)) {
                    handler2.removeMessages(1000);
                }
                ((BackgroundHandler) handler2).startFlag();
                handler2.sendEmptyMessageDelayed(1000, 5000L);
            }
            this.mRunnable.run();
            WeakReference<Handler> weakReference2 = this.mProfileReference;
            if (weakReference2 == null || (handler = weakReference2.get()) == null) {
                return;
            }
            handler.removeMessages(1000);
        }
    }

    /* loaded from: classes2.dex */
    public static class BackgroundHandler extends Handler {
        public static final String TAG = BackgroundHandler.class.getSimpleName();
        public String mThreadName;
        public long start;

        public BackgroundHandler(Looper looper, String str) {
            super(looper);
            this.mThreadName = str;
        }

        public void startFlag() {
            this.start = SystemClock.elapsedRealtime();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.start;
            String str = TAG;
            Log.d(str, " run method exe spend time : " + elapsedRealtime + d.H);
            if (elapsedRealtime >= 5000) {
                printThreadStackByName(this.mThreadName);
            }
        }

        public final void printThreadStackByName(String str) {
            for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
                if (str.equals(entry.getKey().getName())) {
                    Log.e(TAG, " --------------------------------------STAR--------------------------------------");
                    StackTraceElement[] value = entry.getValue();
                    for (int i = 0; i < value.length; i++) {
                        Log.e(str, "    " + value[i].getClassName() + "." + value[i].getMethodName() + "(" + value[i].getFileName() + ":" + value[i].getLineNumber() + ")");
                    }
                    Log.e(TAG, " --------------------------------------END--------------------------------------");
                }
            }
        }
    }

    public static void removeCallbacksAndMessages() {
        getInstance().mHandler.removeCallbacksAndMessages(null);
        getInstance().mBackgroundHandler.removeCallbacksAndMessages(null);
    }
}
