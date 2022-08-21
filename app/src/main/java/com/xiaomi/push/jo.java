package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class jo extends jp {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public byte[] f799a;
    public int b;

    @Override // com.xiaomi.push.jp
    /* renamed from: a */
    public int mo2395a() {
        return this.a;
    }

    @Override // com.xiaomi.push.jp
    /* renamed from: a */
    public int mo2394a(byte[] bArr, int i, int i2) {
        int b = b();
        if (i2 > b) {
            i2 = b;
        }
        if (i2 > 0) {
            System.arraycopy(this.f799a, this.a, bArr, i, i2);
            a(i2);
        }
        return i2;
    }

    @Override // com.xiaomi.push.jp
    public void a(int i) {
        this.a += i;
    }

    public void a(byte[] bArr) {
        b(bArr, 0, bArr.length);
    }

    @Override // com.xiaomi.push.jp
    /* renamed from: a  reason: collision with other method in class */
    public void mo2394a(byte[] bArr, int i, int i2) {
        throw new UnsupportedOperationException("No writing allowed!");
    }

    @Override // com.xiaomi.push.jp
    /* renamed from: a  reason: collision with other method in class */
    public byte[] mo2395a() {
        return this.f799a;
    }

    @Override // com.xiaomi.push.jp
    public int b() {
        return this.b - this.a;
    }

    @Override // com.xiaomi.push.jp
    public void b(byte[] bArr, int i, int i2) {
        this.f799a = bArr;
        this.a = i;
        this.b = i + i2;
    }
}
