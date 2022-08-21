package com.baidu.location.e;

import android.content.Context;
import android.os.Build;
import com.baidu.lbsapi.auth.LBSAuthManager;

/* loaded from: classes.dex */
public class b {
    public static String e = null;
    public static String f = null;
    public static String g = null;
    public static String h = null;
    public static int i = 0;
    public static int j = -2;
    public static long k = -1;
    public String a;
    public String b;
    public String c;
    public String d;
    private boolean l;

    /* loaded from: classes.dex */
    public static class a {
        public static final b a = new b();
    }

    private b() {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.l = false;
        if (com.baidu.location.f.getServiceContext() != null) {
            a(com.baidu.location.f.getServiceContext());
        }
    }

    public static b a() {
        return a.a;
    }

    public String a(boolean z) {
        return a(z, (String) null);
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x010a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String a(boolean r4, java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 295
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.b.a(boolean, java.lang.String):java.lang.String");
    }

    public void a(Context context) {
        if (context == null || this.l) {
            return;
        }
        try {
            this.c = LBSAuthManager.getInstance(context).getCUID();
        } catch (Exception unused) {
            this.c = null;
        }
        try {
            e = context.getPackageName();
        } catch (Exception unused2) {
            e = null;
        }
        j.o = "" + this.c;
        this.l = true;
    }

    public void a(String str, String str2) {
        f = str;
        e = str2;
    }

    public String b() {
        String str;
        StringBuffer stringBuffer = new StringBuffer(200);
        if (this.c != null) {
            stringBuffer.append("&cu=");
            str = this.c;
        } else {
            stringBuffer.append("&im=");
            str = this.a;
        }
        stringBuffer.append(str);
        try {
            stringBuffer.append("&mb=");
            stringBuffer.append(Build.MODEL);
        } catch (Exception unused) {
        }
        stringBuffer.append("&pack=");
        try {
            stringBuffer.append(e);
        } catch (Exception unused2) {
        }
        stringBuffer.append("&sdk=");
        stringBuffer.append(9.16f);
        return stringBuffer.toString();
    }
}
