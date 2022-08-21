package com.baidu.b.c.d;

/* loaded from: classes.dex */
public class a {
    private c a;

    private a() {
    }

    public static a a() {
        a aVar = new a();
        c cVar = new c();
        aVar.a = cVar;
        cVar.a("PKCS1Padding");
        return aVar;
    }

    public void a(int i, d dVar) {
        this.a.a(i, dVar, b.a);
    }

    public final byte[] a(byte[] bArr) {
        if (bArr != null) {
            return this.a.a(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("Null input buffer");
    }
}
