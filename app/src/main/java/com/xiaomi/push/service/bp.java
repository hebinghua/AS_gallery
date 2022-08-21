package com.xiaomi.push.service;

import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class bp {
    public int d = -666;
    public byte[] a = new byte[256];
    public int c = 0;
    public int b = 0;

    public static int a(byte b) {
        return b >= 0 ? b : b + Keyczar.FORMAT_VERSION;
    }

    public static void a(byte[] bArr, int i, int i2) {
        byte b = bArr[i];
        bArr[i] = bArr[i2];
        bArr[i2] = b;
    }

    public static byte[] a(String str, String str2) {
        byte[] m1977a = com.xiaomi.push.bm.m1977a(str);
        byte[] bytes = str2.getBytes();
        byte[] bArr = new byte[m1977a.length + 1 + bytes.length];
        for (int i = 0; i < m1977a.length; i++) {
            bArr[i] = m1977a[i];
        }
        bArr[m1977a.length] = 95;
        for (int i2 = 0; i2 < bytes.length; i2++) {
            bArr[m1977a.length + 1 + i2] = bytes[i2];
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr, String str) {
        return a(bArr, com.xiaomi.push.bm.m1977a(str));
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr2.length];
        bp bpVar = new bp();
        bpVar.a(bArr);
        bpVar.m2502a();
        for (int i = 0; i < bArr2.length; i++) {
            bArr3[i] = (byte) (bArr2[i] ^ bpVar.a());
        }
        return bArr3;
    }

    public static byte[] a(byte[] bArr, byte[] bArr2, boolean z, int i, int i2) {
        byte[] bArr3;
        int i3;
        if (i < 0 || i > bArr2.length || i + i2 > bArr2.length) {
            throw new IllegalArgumentException("start = " + i + " len = " + i2);
        }
        if (!z) {
            bArr3 = new byte[i2];
            i3 = 0;
        } else {
            bArr3 = bArr2;
            i3 = i;
        }
        bp bpVar = new bp();
        bpVar.a(bArr);
        bpVar.m2502a();
        for (int i4 = 0; i4 < i2; i4++) {
            bArr3[i3 + i4] = (byte) (bArr2[i + i4] ^ bpVar.a());
        }
        return bArr3;
    }

    public byte a() {
        int i = (this.b + 1) % 256;
        this.b = i;
        int a = (this.c + a(this.a[i])) % 256;
        this.c = a;
        a(this.a, this.b, a);
        byte[] bArr = this.a;
        return bArr[(a(bArr[this.b]) + a(this.a[this.c])) % 256];
    }

    /* renamed from: a  reason: collision with other method in class */
    public final void m2502a() {
        this.c = 0;
        this.b = 0;
    }

    public final void a(int i, byte[] bArr, boolean z) {
        int length = bArr.length;
        for (int i2 = 0; i2 < 256; i2++) {
            this.a[i2] = (byte) i2;
        }
        this.c = 0;
        this.b = 0;
        while (true) {
            int i3 = this.b;
            if (i3 >= i) {
                break;
            }
            int a = ((this.c + a(this.a[i3])) + a(bArr[this.b % length])) % 256;
            this.c = a;
            a(this.a, this.b, a);
            this.b++;
        }
        if (i != 256) {
            this.d = ((this.c + a(this.a[i])) + a(bArr[i % length])) % 256;
        }
        if (z) {
            StringBuilder sb = new StringBuilder();
            sb.append("S_");
            int i4 = i - 1;
            sb.append(i4);
            sb.append(":");
            for (int i5 = 0; i5 <= i; i5++) {
                sb.append(" ");
                sb.append(a(this.a[i5]));
            }
            sb.append("   j_");
            sb.append(i4);
            sb.append("=");
            sb.append(this.c);
            sb.append("   j_");
            sb.append(i);
            sb.append("=");
            sb.append(this.d);
            sb.append("   S_");
            sb.append(i4);
            sb.append("[j_");
            sb.append(i4);
            sb.append("]=");
            sb.append(a(this.a[this.c]));
            sb.append("   S_");
            sb.append(i4);
            sb.append("[j_");
            sb.append(i);
            sb.append("]=");
            sb.append(a(this.a[this.d]));
            if (this.a[1] != 0) {
                sb.append("   S[1]!=0");
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a(sb.toString());
        }
    }

    public final void a(byte[] bArr) {
        a(256, bArr, false);
    }
}
