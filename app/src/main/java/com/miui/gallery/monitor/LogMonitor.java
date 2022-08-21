package com.miui.gallery.monitor;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes2.dex */
public class LogMonitor {
    public final long mBlockTime;
    public long mStart;
    public final long mTraceStackTime;
    public final Handler mWorkHandler;

    public LogMonitor(Looper looper, long j) {
        long max = Math.max(30L, Math.min(j, 3000L));
        this.mBlockTime = max;
        this.mTraceStackTime = ((float) max) * 0.8f;
        HandlerThread handlerThread = new HandlerThread("monitor work", 10);
        handlerThread.start();
        this.mWorkHandler = new WorkHandler(handlerThread.getLooper(), looper);
    }

    public void startMonitor(String str) {
        this.mStart = SystemClock.uptimeMillis();
        this.mWorkHandler.sendEmptyMessageDelayed(1, this.mTraceStackTime);
    }

    public void stopMonitor(String str) {
        this.mWorkHandler.removeMessages(1);
        if (this.mStart > 0) {
            long uptimeMillis = SystemClock.uptimeMillis() - this.mStart;
            if (uptimeMillis <= this.mBlockTime || TextUtils.isEmpty(str)) {
                return;
            }
            this.mWorkHandler.obtainMessage(2, String.format("%s ########### Processing: %sms", str, Long.valueOf(uptimeMillis))).sendToTarget();
        }
    }

    /* loaded from: classes2.dex */
    public static class WorkHandler extends Handler {
        public final FileLog mFileLog;
        public final Looper mMonitorLooper;
        public String mStackTrace;

        public WorkHandler(Looper looper, Looper looper2) {
            super(looper);
            this.mMonitorLooper = looper2;
            this.mFileLog = new FileLog();
        }

        public final void traceStack() {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : this.mMonitorLooper.getThread().getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            this.mStackTrace = sb.toString();
        }

        public final void printLog(String str) {
            String str2 = this.mStackTrace;
            if (str2 != null) {
                Log.w("LOOPER_MONITOR", str2);
            }
            if (str != null) {
                Log.w("LOOPER_MONITOR", str);
            }
            this.mFileLog.log(this.mStackTrace + str + "\n------------------------------------\n");
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                traceStack();
            } else if (i == 2) {
                printLog((String) message.obj);
            }
            super.handleMessage(message);
        }
    }
}
