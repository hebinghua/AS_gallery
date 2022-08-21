package com.baidu.mapsdkplatform.comapi.b.a;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;

/* loaded from: classes.dex */
public class a implements Thread.UncaughtExceptionHandler {
    private static int a = 10240;
    private static volatile boolean c = false;
    private String b;
    private Thread.UncaughtExceptionHandler d;

    /* renamed from: com.baidu.mapsdkplatform.comapi.b.a.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0015a {
        private static final a a = new a();
    }

    private a() {
        this.b = "";
        this.d = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static a a() {
        return C0015a.a;
    }

    private void a(Throwable th) {
        String str;
        if (th == null) {
            return;
        }
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            Throwable cause = th.getCause();
            if (cause != null) {
                cause.printStackTrace(printWriter);
            }
            printWriter.close();
            String obj = stringWriter.toString();
            if (obj.isEmpty()) {
                return;
            }
            int length = obj.length();
            int i = a;
            if (length > i) {
                obj = obj.substring(0, i);
            }
            if (obj.contains("BDMapSDKException")) {
                return;
            }
            if ((obj.contains("com.baidu.platform") || obj.contains("com.baidu.mapsdkplatform")) && (str = this.b) != null && !str.isEmpty()) {
                c.a().a(this.b + (System.currentTimeMillis() / 1000) + ".txt", obj);
            }
        } catch (Exception unused) {
        }
    }

    public void a(String str) {
        this.b = str;
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof a)) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (c) {
            return;
        }
        c = true;
        a(th);
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.d;
        if (uncaughtExceptionHandler == null) {
            return;
        }
        uncaughtExceptionHandler.uncaughtException(thread, th);
    }
}
