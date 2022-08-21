package com.baidu.location.e;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public abstract class e {
    public String g = null;
    public int h = 1;
    public String i = null;
    public Map<String, Object> j = null;
    public String k = null;
    public byte[] l = null;
    public byte[] m = null;
    public String n = null;
    public static int f = a.g;
    private static String a = "10.0.0.172";
    private static int b = 80;
    public static int o = 0;

    public abstract void a();

    public void a(ExecutorService executorService, String str) {
        try {
            executorService.execute(new h(this, str));
        } catch (Throwable unused) {
            a(false);
        }
    }

    public void a(ExecutorService executorService, boolean z, String str) {
        try {
            executorService.execute(new f(this, str, z));
        } catch (Throwable unused) {
            a(false);
        }
    }

    public abstract void a(boolean z);

    public void b(String str) {
        try {
            new g(this, str).start();
        } catch (Throwable unused) {
            a(false);
        }
    }
}
