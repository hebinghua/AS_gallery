package com.baidu.platform.comapi.pano;

/* loaded from: classes.dex */
public class c {
    public String a;
    public PanoStateError b;
    public int c;

    public c() {
    }

    public c(PanoStateError panoStateError) {
        this.b = panoStateError;
    }

    public PanoStateError a() {
        return this.b;
    }

    public void a(int i) {
        this.c = i;
    }

    public void a(String str) {
        this.a = str;
    }

    public String b() {
        return this.a;
    }

    public int c() {
        return this.c;
    }
}
