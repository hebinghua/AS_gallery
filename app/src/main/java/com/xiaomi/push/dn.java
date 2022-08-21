package com.xiaomi.push;

/* loaded from: classes3.dex */
public class dn {
    public static volatile dn a;

    /* renamed from: a  reason: collision with other field name */
    public dm f216a;

    public static dn a() {
        if (a == null) {
            synchronized (dn.class) {
                if (a == null) {
                    a = new dn();
                }
            }
        }
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public dm m2044a() {
        return this.f216a;
    }

    public void a(dm dmVar) {
        this.f216a = dmVar;
    }
}
