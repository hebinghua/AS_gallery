package com.xiaomi.push;

/* loaded from: classes3.dex */
public class jn extends jp {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public ix f798a;

    public jn(int i) {
        this.f798a = new ix(i);
    }

    @Override // com.xiaomi.push.jp
    /* renamed from: a */
    public int mo2394a(byte[] bArr, int i, int i2) {
        byte[] m2361a = this.f798a.m2361a();
        if (i2 > this.f798a.a() - this.a) {
            i2 = this.f798a.a() - this.a;
        }
        if (i2 > 0) {
            System.arraycopy(m2361a, this.a, bArr, i, i2);
            this.a += i2;
        }
        return i2;
    }

    @Override // com.xiaomi.push.jp
    /* renamed from: a  reason: collision with other method in class */
    public void mo2394a(byte[] bArr, int i, int i2) {
        this.f798a.write(bArr, i, i2);
    }

    public int a_() {
        return this.f798a.size();
    }
}
