package com.baidu.b.c.a;

import java.security.SecureRandom;

/* loaded from: classes.dex */
public class c {
    public static final SecureRandom a = new SecureRandom();
    private e b;

    public c() {
        this.b = null;
        this.b = new e(new b(), 16);
    }

    public static byte[] a(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        c cVar = new c();
        cVar.a(2, bArr, bArr2);
        return cVar.a(bArr3);
    }

    public void a(int i, byte[] bArr, byte[] bArr2) {
        this.b.a(i, bArr, bArr2, a);
    }

    public final byte[] a(byte[] bArr) {
        if (bArr != null) {
            return this.b.a(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("Null input buffer");
    }
}
