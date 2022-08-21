package com.baidu.b.c.d;

import java.math.BigInteger;

/* loaded from: classes.dex */
public class e implements d {
    private BigInteger a;
    private BigInteger b;

    public e(byte[] bArr, byte[] bArr2) {
        this.a = new BigInteger(bArr);
        this.b = new BigInteger(bArr2);
    }

    @Override // com.baidu.b.c.d.d
    public BigInteger a() {
        return this.a;
    }

    @Override // com.baidu.b.c.d.d
    public BigInteger b() {
        return this.b;
    }
}
