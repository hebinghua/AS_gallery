package com.miui.gallery.monitor;

import android.os.Looper;
import android.util.Printer;

/* loaded from: classes2.dex */
public class LooperBlockDetector {
    public static void start(Looper looper, long j) {
        looper.setMessageLogging(new MessageLoggingPrinter(new LogMonitor(looper, j)));
    }

    /* loaded from: classes2.dex */
    public static class MessageLoggingPrinter implements Printer {
        public LogMonitor mMonitor;

        public MessageLoggingPrinter(LogMonitor logMonitor) {
            this.mMonitor = logMonitor;
        }

        @Override // android.util.Printer
        public void println(String str) {
            if (str.startsWith(">>>>> Dispatching to")) {
                this.mMonitor.startMonitor(str);
            } else if (!str.startsWith("<<<<< Finished to")) {
            } else {
                this.mMonitor.stopMonitor(str);
            }
        }
    }
}
