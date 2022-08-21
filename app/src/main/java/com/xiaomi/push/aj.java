package com.xiaomi.push;

import android.content.Context;
import android.os.Build;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/* loaded from: classes3.dex */
public class aj {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.io.Closeable] */
    public static boolean a(Context context, String str, long j) {
        RandomAccessFile randomAccessFile;
        ?? r2 = 23;
        if (Build.VERSION.SDK_INT < 23 || h.c(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            FileLock fileLock = null;
            try {
                try {
                    File file = new File(new File(context.getExternalFilesDir(null), "/.vdevdir/"), "lcfp.lock");
                    ab.m1930a(file);
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    try {
                        fileLock = randomAccessFile.getChannel().lock();
                        boolean b = b(context, str, j);
                        if (fileLock != null && fileLock.isValid()) {
                            try {
                                fileLock.release();
                            } catch (IOException unused) {
                            }
                        }
                        ab.a(randomAccessFile);
                        return b;
                    } catch (IOException e) {
                        e = e;
                        e.printStackTrace();
                        if (fileLock != null && fileLock.isValid()) {
                            try {
                                fileLock.release();
                            } catch (IOException unused2) {
                            }
                        }
                        ab.a(randomAccessFile);
                        return true;
                    }
                } catch (Throwable th) {
                    th = th;
                    if (0 != 0 && fileLock.isValid()) {
                        try {
                            fileLock.release();
                        } catch (IOException unused3) {
                        }
                    }
                    ab.a((Closeable) r2);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                randomAccessFile = null;
            } catch (Throwable th2) {
                th = th2;
                r2 = 0;
                if (0 != 0) {
                    fileLock.release();
                }
                ab.a((Closeable) r2);
                throw th;
            }
        } else {
            return true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00d7 A[Catch: IOException -> 0x00e7, all -> 0x00f9, LOOP:0: B:39:0x00d1->B:41:0x00d7, LOOP_END, TRY_LEAVE, TryCatch #0 {IOException -> 0x00e7, blocks: (B:38:0x00cd, B:39:0x00d1, B:41:0x00d7), top: B:56:0x00cd }] */
    /* JADX WARN: Type inference failed for: r10v0, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean b(android.content.Context r16, java.lang.String r17, long r18) {
        /*
            Method dump skipped, instructions count: 254
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.aj.b(android.content.Context, java.lang.String, long):boolean");
    }
}
