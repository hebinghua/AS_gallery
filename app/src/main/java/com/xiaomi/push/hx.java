package com.xiaomi.push;

/* loaded from: classes3.dex */
public enum hx {
    RegIdExpired(0),
    PackageUnregistered(1),
    Init(2);
    

    /* renamed from: a  reason: collision with other field name */
    private final int f535a;

    hx(int i) {
        this.f535a = i;
    }

    public static hx a(int i) {
        if (i != 0) {
            if (i == 1) {
                return PackageUnregistered;
            }
            if (i == 2) {
                return Init;
            }
            return null;
        }
        return RegIdExpired;
    }

    public int a() {
        return this.f535a;
    }
}
