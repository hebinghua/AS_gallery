package com.xiaomi.push;

/* loaded from: classes3.dex */
public abstract class jp {
    /* renamed from: a */
    public int mo2395a() {
        return 0;
    }

    /* renamed from: a */
    public abstract int mo2394a(byte[] bArr, int i, int i2);

    public void a(int i) {
    }

    public abstract void a(byte[] bArr, int i, int i2);

    public byte[] a() {
        return null;
    }

    public int b() {
        return -1;
    }

    public int b(byte[] bArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            int mo2394a = mo2394a(bArr, i + i3, i2 - i3);
            if (mo2394a <= 0) {
                throw new jq("Cannot read. Remote side has closed. Tried to read " + i2 + " bytes, but only got " + i3 + " bytes.");
            }
            i3 += mo2394a;
        }
        return i3;
    }
}
