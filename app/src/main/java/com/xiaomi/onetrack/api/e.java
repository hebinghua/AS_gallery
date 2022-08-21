package com.xiaomi.onetrack.api;

import android.os.Process;
import com.xiaomi.onetrack.util.b;
import com.xiaomi.onetrack.util.p;
import java.lang.Thread;
import java.util.Date;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class e implements Thread.UncaughtExceptionHandler {
    public Thread.UncaughtExceptionHandler b;
    public final Date g = new Date();
    public int i = 50;
    public int j = 50;
    public int k = 200;
    public boolean l = true;
    public boolean m = true;

    public void a() {
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler instanceof e) {
            return;
        }
        this.b = defaultUncaughtExceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        p.a("OneTrackExceptionHandler", "uncaughtException start");
        FutureTask futureTask = new FutureTask(new f(this, thread, th), null);
        com.xiaomi.onetrack.b.a.a(futureTask);
        try {
            futureTask.get(2L, TimeUnit.SECONDS);
        } catch (Exception e) {
            p.b("OneTrackExceptionHandler", "handleException timeout :" + e.getMessage());
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.b;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:1|(2:2|3)|(3:5|6|(1:8))|10|11|12|13|14|15|16|17|18|(15:20|21|23|24|(1:26)|27|(1:46)|33|(1:35)|36|(1:38)|39|40|41|42)(1:66)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0087, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0089, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x008b, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x008e, code lost:
        r9 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x008f, code lost:
        com.xiaomi.onetrack.util.p.b("OneTrackExceptionHandler", "JavaCrashHandler getEmergency failed", r0);
        r0 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0097 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a(java.lang.Thread r16, java.lang.Throwable r17) {
        /*
            Method dump skipped, instructions count: 351
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.onetrack.api.e.a(java.lang.Thread, java.lang.Throwable):void");
    }

    public final String a(Date date, Thread thread, String str) {
        return b.a(this.g, date, "java", com.xiaomi.onetrack.e.a.e(), b.a(com.xiaomi.onetrack.e.a.b())) + "pid: " + Process.myPid() + ", tid: " + Process.myTid() + ", name: " + thread.getName() + "  >>> " + b.a(com.xiaomi.onetrack.e.a.b(), Process.myPid()) + " <<<\n\njava stacktrace:\n" + str + "\n";
    }
}
