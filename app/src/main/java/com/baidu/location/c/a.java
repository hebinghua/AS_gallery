package com.baidu.location.c;

import java.util.Locale;

/* loaded from: classes.dex */
public class a {
    public int a;
    public long b;
    public int c;
    public int d;
    public int e;
    public int f;
    public long g;
    public int h;
    public char i;
    public int j;
    public int k;
    public int l;
    public String m;
    public String n;
    private boolean o;

    public a() {
        this.a = -1;
        this.b = -1L;
        this.c = -1;
        this.d = -1;
        this.e = Integer.MAX_VALUE;
        this.f = Integer.MAX_VALUE;
        this.g = 0L;
        this.h = -1;
        this.i = '0';
        this.j = Integer.MAX_VALUE;
        this.k = 0;
        this.l = 0;
        this.m = null;
        this.n = null;
        this.o = false;
        this.g = System.currentTimeMillis();
    }

    public a(int i, long j, int i2, int i3, int i4, char c, int i5) {
        this.a = -1;
        this.b = -1L;
        this.c = -1;
        this.d = -1;
        this.e = Integer.MAX_VALUE;
        this.f = Integer.MAX_VALUE;
        this.g = 0L;
        this.h = -1;
        this.i = '0';
        this.j = Integer.MAX_VALUE;
        this.k = 0;
        this.l = 0;
        this.m = null;
        this.n = null;
        this.o = false;
        this.a = i;
        this.b = j;
        this.c = i2;
        this.d = i3;
        this.h = i4;
        this.i = c;
        this.g = System.currentTimeMillis();
        this.j = i5;
    }

    public a(a aVar) {
        this(aVar.a, aVar.b, aVar.c, aVar.d, aVar.h, aVar.i, aVar.j);
        this.g = aVar.g;
        this.m = aVar.m;
        this.k = aVar.k;
        this.n = aVar.n;
        this.l = aVar.l;
    }

    public boolean a() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.g;
        return currentTimeMillis - j > 0 && currentTimeMillis - j < 3000;
    }

    public boolean a(a aVar) {
        return this.a == aVar.a && this.b == aVar.b && this.d == aVar.d && this.c == aVar.c;
    }

    public boolean b() {
        return this.a > -1 && this.b > 0;
    }

    public boolean c() {
        return this.a == -1 && this.b == -1 && this.d == -1 && this.c == -1;
    }

    public boolean d() {
        return this.a > -1 && this.b > -1 && this.d == -1 && this.c == -1;
    }

    public boolean e() {
        return this.a > -1 && this.b > -1 && this.d > -1 && this.c > -1;
    }

    public void f() {
        this.o = true;
    }

    public String g() {
        return String.format(Locale.CHINA, "%d|%d|%d|%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.a), Long.valueOf(this.b));
    }

    public String h() {
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&nw=");
        stringBuffer.append(this.i);
        stringBuffer.append(String.format(Locale.CHINA, "&cl=%d|%d|%d|%d&cl_s=%d&clp=%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.a), Long.valueOf(this.b), Integer.valueOf(this.h), Integer.valueOf(this.k)));
        if (this.j != Integer.MAX_VALUE) {
            stringBuffer.append("&cl_cs=");
            stringBuffer.append(this.j);
        }
        if (this.o) {
            stringBuffer.append("&newcl=1");
        }
        stringBuffer.append("&cl_api=");
        stringBuffer.append(this.l);
        if (this.n != null) {
            stringBuffer.append("&clnrs=");
            stringBuffer.append(this.n);
        }
        return stringBuffer.toString();
    }

    public String i() {
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&nw2=");
        stringBuffer.append(this.i);
        stringBuffer.append(String.format(Locale.CHINA, "&cl2=%d|%d|%d|%d&cl_s2=%d&clp2=%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.a), Long.valueOf(this.b), Integer.valueOf(this.h), Integer.valueOf(this.k)));
        if (this.j != Integer.MAX_VALUE) {
            stringBuffer.append("&cl_cs2=");
            stringBuffer.append(this.j);
        }
        if (this.n != null) {
            stringBuffer.append("&clnrs2=");
            stringBuffer.append(this.n);
        }
        return stringBuffer.toString();
    }
}
