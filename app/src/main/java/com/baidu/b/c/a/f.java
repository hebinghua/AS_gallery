package com.baidu.b.c.a;

/* loaded from: classes.dex */
abstract class f {
    public final b b;
    public final int c;
    public byte[] d;

    public f(b bVar) {
        this.b = bVar;
        this.c = bVar.a();
    }

    public abstract void a();

    public abstract void a(boolean z, String str, byte[] bArr, byte[] bArr2);

    public abstract void a(byte[] bArr, int i, int i2, byte[] bArr2, int i3);

    public abstract void b();

    public abstract void b(byte[] bArr, int i, int i2, byte[] bArr2, int i3);

    public abstract void c();

    public void c(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        a(bArr, i, i2, bArr2, i3);
    }

    public void d(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        b(bArr, i, i2, bArr2, i3);
    }
}
