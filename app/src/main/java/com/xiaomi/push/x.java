package com.xiaomi.push;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes3.dex */
public final class x {
    public static final Set<String> a = Collections.synchronizedSet(new HashSet());

    /* renamed from: a  reason: collision with other field name */
    public Context f993a;

    /* renamed from: a  reason: collision with other field name */
    public RandomAccessFile f994a;

    /* renamed from: a  reason: collision with other field name */
    public String f995a;

    /* renamed from: a  reason: collision with other field name */
    public FileLock f996a;

    public x(Context context) {
        this.f993a = context;
    }

    public static x a(Context context, File file) {
        com.xiaomi.channel.commonutils.logger.b.c("Locking: " + file.getAbsolutePath());
        String str = file.getAbsolutePath() + ".LOCK";
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.getParentFile().mkdirs();
            file2.createNewFile();
        }
        Set<String> set = a;
        if (set.add(str)) {
            x xVar = new x(context);
            xVar.f995a = str;
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
                xVar.f994a = randomAccessFile;
                xVar.f996a = randomAccessFile.getChannel().lock();
                com.xiaomi.channel.commonutils.logger.b.c("Locked: " + str + " :" + xVar.f996a);
                if (xVar.f996a == null) {
                    RandomAccessFile randomAccessFile2 = xVar.f994a;
                    if (randomAccessFile2 != null) {
                        ab.a(randomAccessFile2);
                    }
                    set.remove(xVar.f995a);
                }
                return xVar;
            } catch (Throwable th) {
                if (xVar.f996a == null) {
                    RandomAccessFile randomAccessFile3 = xVar.f994a;
                    if (randomAccessFile3 != null) {
                        ab.a(randomAccessFile3);
                    }
                    a.remove(xVar.f995a);
                }
                throw th;
            }
        }
        throw new IOException("abtain lock failure");
    }

    public void a() {
        com.xiaomi.channel.commonutils.logger.b.c("unLock: " + this.f996a);
        FileLock fileLock = this.f996a;
        if (fileLock != null && fileLock.isValid()) {
            try {
                this.f996a.release();
            } catch (IOException unused) {
            }
            this.f996a = null;
        }
        RandomAccessFile randomAccessFile = this.f994a;
        if (randomAccessFile != null) {
            ab.a(randomAccessFile);
        }
        a.remove(this.f995a);
    }
}
