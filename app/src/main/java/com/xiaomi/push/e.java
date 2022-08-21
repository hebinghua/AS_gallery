package com.xiaomi.push;

import java.io.IOException;

/* loaded from: classes3.dex */
public abstract class e {
    /* renamed from: a */
    public abstract int mo2116a();

    /* renamed from: a */
    public abstract e mo2115a(b bVar);

    public e a(byte[] bArr) {
        return a(bArr, 0, bArr.length);
    }

    public e a(byte[] bArr, int i, int i2) {
        try {
            b a = b.a(bArr, i, i2);
            mo2115a(a);
            a.m1950a(0);
            return this;
        } catch (d e) {
            throw e;
        } catch (IOException unused) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
    }

    public abstract void a(c cVar);

    /* renamed from: a  reason: collision with other method in class */
    public void m2120a(byte[] bArr, int i, int i2) {
        try {
            c a = c.a(bArr, i, i2);
            a(a);
            a.b();
        } catch (IOException unused) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).");
        }
    }

    public boolean a(b bVar, int i) {
        return bVar.m1952a(i);
    }

    public byte[] a() {
        int mo2118b = mo2118b();
        byte[] bArr = new byte[mo2118b];
        m2120a(bArr, 0, mo2118b);
        return bArr;
    }

    /* renamed from: b */
    public abstract int mo2118b();
}
