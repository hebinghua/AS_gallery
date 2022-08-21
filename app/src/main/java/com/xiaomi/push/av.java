package com.xiaomi.push;

import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* loaded from: classes3.dex */
public class av implements au, InvocationHandler {
    public static final String[][] a = {new String[]{"com.bun.supplier.IIdentifierListener", "com.bun.supplier.IdSupplier"}, new String[]{"com.bun.miitmdid.core.IIdentifierListener", "com.bun.miitmdid.supplier.IdSupplier"}};

    /* renamed from: a  reason: collision with other field name */
    public Context f106a;

    /* renamed from: a  reason: collision with other field name */
    public Class f108a = null;
    public Class b = null;

    /* renamed from: a  reason: collision with other field name */
    public Method f110a = null;

    /* renamed from: b  reason: collision with other field name */
    public Method f111b = null;
    public Method c = null;
    public Method d = null;
    public Method e = null;
    public Method f = null;
    public Method g = null;

    /* renamed from: a  reason: collision with other field name */
    public final Object f109a = new Object();

    /* renamed from: a  reason: collision with other field name */
    public volatile int f104a = 0;

    /* renamed from: a  reason: collision with other field name */
    public volatile long f105a = 0;

    /* renamed from: a  reason: collision with other field name */
    public volatile a f107a = null;

    /* loaded from: classes3.dex */
    public class a {

        /* renamed from: a  reason: collision with other field name */
        public Boolean f112a;

        /* renamed from: a  reason: collision with other field name */
        public String f113a;
        public String b;
        public String c;
        public String d;

        public a() {
            this.f112a = null;
            this.f113a = null;
            this.b = null;
            this.c = null;
            this.d = null;
        }

        public boolean a() {
            if (!TextUtils.isEmpty(this.f113a) || !TextUtils.isEmpty(this.b) || !TextUtils.isEmpty(this.c) || !TextUtils.isEmpty(this.d)) {
                this.f112a = Boolean.TRUE;
            }
            return this.f112a != null;
        }
    }

    public av(Context context) {
        this.f106a = context.getApplicationContext();
        a(context);
        b(context);
    }

    public static Class<?> a(Context context, String str) {
        try {
            return v.a(context, str);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static <T> T a(Method method, Object obj, Object... objArr) {
        if (method != null) {
            try {
                T t = (T) method.invoke(obj, objArr);
                if (t == null) {
                    return null;
                }
                return t;
            } catch (Throwable unused) {
                return null;
            }
        }
        return null;
    }

    public static Method a(Class<?> cls, String str, Class<?>... clsArr) {
        if (cls != null) {
            try {
                return cls.getMethod(str, clsArr);
            } catch (Throwable unused) {
                return null;
            }
        }
        return null;
    }

    public static boolean a(Object obj) {
        return (obj instanceof Boolean) || (obj instanceof Character) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Double);
    }

    public static void b(String str) {
        com.xiaomi.channel.commonutils.logger.b.m1859a("mdid:" + str);
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public String mo1967a() {
        a("getOAID");
        if (this.f107a == null) {
            return null;
        }
        return this.f107a.b;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a  reason: collision with other method in class */
    public final void mo1967a() {
        synchronized (this.f109a) {
            try {
                this.f109a.notifyAll();
            } catch (Exception unused) {
            }
        }
    }

    public final void a(Context context) {
        Class<?> a2 = a(context, "com.bun.miitmdid.core.MdidSdk");
        Class<?> cls = null;
        Class<?> cls2 = null;
        int i = 0;
        while (true) {
            String[][] strArr = a;
            if (i >= strArr.length) {
                break;
            }
            String[] strArr2 = strArr[i];
            Class<?> a3 = a(context, strArr2[0]);
            Class<?> a4 = a(context, strArr2[1]);
            if (a3 != null && a4 != null) {
                b("found class in index " + i);
                cls2 = a4;
                cls = a3;
                break;
            }
            i++;
            cls2 = a4;
            cls = a3;
        }
        this.f108a = a2;
        this.f110a = a(a2, "InitSdk", Context.class, cls);
        this.b = cls;
        this.f111b = a(cls2, "getUDID", new Class[0]);
        this.c = a(cls2, "getOAID", new Class[0]);
        this.d = a(cls2, "getVAID", new Class[0]);
        this.e = a(cls2, "getAAID", new Class[0]);
        this.f = a(cls2, "isSupported", new Class[0]);
        this.g = a(cls2, "shutDown", new Class[0]);
    }

    public final void a(String str) {
        if (this.f107a != null) {
            return;
        }
        long j = this.f105a;
        long elapsedRealtime = SystemClock.elapsedRealtime() - Math.abs(j);
        int i = this.f104a;
        if (elapsedRealtime > 3000 && i < 3) {
            synchronized (this.f109a) {
                if (this.f105a == j && this.f104a == i) {
                    b("retry, current count is " + i);
                    this.f104a = this.f104a + 1;
                    b(this.f106a);
                    j = this.f105a;
                    elapsedRealtime = SystemClock.elapsedRealtime() - Math.abs(j);
                }
            }
        }
        if (this.f107a != null || j < 0 || elapsedRealtime > 3000 || Looper.myLooper() == Looper.getMainLooper()) {
            return;
        }
        synchronized (this.f109a) {
            if (this.f107a == null) {
                try {
                    b(str + " wait...");
                    this.f109a.wait(3000L);
                } catch (Exception unused) {
                }
            }
        }
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public boolean mo1967a() {
        a("isSupported");
        return this.f107a != null && Boolean.TRUE.equals(this.f107a.f112a);
    }

    public final void b(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = -elapsedRealtime;
        Class cls = this.b;
        if (cls != null) {
            try {
                ClassLoader classLoader = cls.getClassLoader();
                if (classLoader == null) {
                    classLoader = context.getClassLoader();
                }
                a(this.f110a, this.f108a.newInstance(), context, Proxy.newProxyInstance(classLoader, new Class[]{this.b}, this));
            } catch (Throwable th) {
                b("call init sdk error:" + th);
            }
            this.f105a = elapsedRealtime;
        }
        elapsedRealtime = j;
        this.f105a = elapsedRealtime;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) {
        this.f105a = SystemClock.elapsedRealtime();
        if (objArr != null) {
            a aVar = new a();
            int length = objArr.length;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Object obj2 = objArr[i];
                if (obj2 != null && !a(obj2)) {
                    aVar.f113a = (String) a(this.f111b, obj2, new Object[0]);
                    aVar.b = (String) a(this.c, obj2, new Object[0]);
                    aVar.c = (String) a(this.d, obj2, new Object[0]);
                    aVar.d = (String) a(this.e, obj2, new Object[0]);
                    aVar.f112a = (Boolean) a(this.f, obj2, new Object[0]);
                    a(this.g, obj2, new Object[0]);
                    if (aVar.a()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("has get succ, check duplicate:");
                        if (this.f107a != null) {
                            z = true;
                        }
                        sb.append(z);
                        b(sb.toString());
                        synchronized (av.class) {
                            if (this.f107a == null) {
                                this.f107a = aVar;
                            }
                        }
                    }
                }
                i++;
            }
        }
        mo1967a();
        return null;
    }
}
