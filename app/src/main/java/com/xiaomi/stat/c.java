package com.xiaomi.stat;

import android.os.Handler;
import android.os.HandlerThread;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/* loaded from: classes3.dex */
public class c {
    private static final String a = "DBExecutor";
    private static String b = "mistat_db";
    private static final String c = "mistat";
    private static final String d = "db.lk";
    private static Handler e;
    private static FileLock f;
    private static FileChannel g;

    private static void c() {
        if (e == null) {
            synchronized (c.class) {
                if (e == null) {
                    HandlerThread handlerThread = new HandlerThread(b);
                    handlerThread.start();
                    e = new Handler(handlerThread.getLooper());
                }
            }
        }
    }

    public static void a(Runnable runnable) {
        c();
        e.post(new a(runnable));
    }

    /* loaded from: classes3.dex */
    public static class a implements Runnable {
        private Runnable a;

        public a(Runnable runnable) {
            this.a = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!c.d()) {
                return;
            }
            Runnable runnable = this.a;
            if (runnable != null) {
                runnable.run();
            }
            c.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean d() {
        File file = new File(ak.a().getFilesDir(), c);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            try {
                FileChannel channel = new FileOutputStream(new File(file, d)).getChannel();
                g = channel;
                f = channel.lock();
                com.xiaomi.stat.d.k.c(a, "acquire lock for db");
                return true;
            } catch (Exception e2) {
                com.xiaomi.stat.d.k.c(a, "acquire lock for db failed with " + e2);
                try {
                    g.close();
                    g = null;
                } catch (Exception e3) {
                    com.xiaomi.stat.d.k.c(a, "close file stream failed with " + e3);
                }
                return false;
            }
        } catch (Exception e4) {
            com.xiaomi.stat.d.k.c(a, "acquire lock for db failed with " + e4);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void e() {
        try {
            FileLock fileLock = f;
            if (fileLock != null) {
                fileLock.release();
                f = null;
            }
            com.xiaomi.stat.d.k.c(a, "release sDBFileLock for db");
        } catch (Exception e2) {
            com.xiaomi.stat.d.k.c(a, "release sDBFileLock for db failed with " + e2);
        }
        try {
            FileChannel fileChannel = g;
            if (fileChannel != null) {
                fileChannel.close();
                g = null;
            }
            com.xiaomi.stat.d.k.c(a, "release sLockFileChannel for db");
        } catch (Exception e3) {
            com.xiaomi.stat.d.k.c(a, "release sLockFileChannel for db failed with " + e3);
        }
    }
}
