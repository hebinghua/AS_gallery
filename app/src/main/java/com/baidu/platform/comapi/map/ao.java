package com.baidu.platform.comapi.map;

/* loaded from: classes.dex */
public class ao {
    private int a;
    private int b;
    private int c;
    private int d;

    public static int c(int i) {
        return ((i & 16711680) >> 16) | ((-16777216) & i) | ((i & 255) << 16) | (65280 & i);
    }

    public int a() {
        return this.d;
    }

    public ao a(int i) {
        this.a = i;
        return this;
    }

    public int b() {
        return this.a;
    }

    public ao b(int i) {
        this.b = i;
        return this;
    }

    public int c() {
        return this.b;
    }

    public int d() {
        return this.c;
    }

    public String toString() {
        return "Style: color:" + Integer.toHexString(this.a) + " width:" + this.b + " fillcolor:" + Integer.toHexString(this.c);
    }
}
