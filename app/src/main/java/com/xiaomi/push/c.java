package com.xiaomi.push;

import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public final class c {
    public final int a;

    /* renamed from: a  reason: collision with other field name */
    public final OutputStream f146a;

    /* renamed from: a  reason: collision with other field name */
    public final byte[] f147a;
    public int b;

    /* loaded from: classes3.dex */
    public static class a extends IOException {
        public a() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }
    }

    public c(OutputStream outputStream, byte[] bArr) {
        this.f146a = outputStream;
        this.f147a = bArr;
        this.b = 0;
        this.a = bArr.length;
    }

    public c(byte[] bArr, int i, int i2) {
        this.f146a = null;
        this.f147a = bArr;
        this.b = i;
        this.a = i + i2;
    }

    public static int a(int i) {
        if (i >= 0) {
            return d(i);
        }
        return 10;
    }

    public static int a(int i, int i2) {
        return c(i) + a(i2);
    }

    public static int a(int i, long j) {
        return c(i) + a(j);
    }

    public static int a(int i, com.xiaomi.push.a aVar) {
        return c(i) + a(aVar);
    }

    public static int a(int i, e eVar) {
        return c(i) + a(eVar);
    }

    public static int a(int i, String str) {
        return c(i) + a(str);
    }

    public static int a(int i, boolean z) {
        return c(i) + a(z);
    }

    public static int a(long j) {
        return c(j);
    }

    public static int a(com.xiaomi.push.a aVar) {
        return d(aVar.a()) + aVar.a();
    }

    public static int a(e eVar) {
        int mo2118b = eVar.mo2118b();
        return d(mo2118b) + mo2118b;
    }

    public static int a(String str) {
        try {
            byte[] bytes = str.getBytes(Keyczar.DEFAULT_ENCODING);
            return d(bytes.length) + bytes.length;
        } catch (UnsupportedEncodingException unused) {
            throw new RuntimeException("UTF-8 not supported.");
        }
    }

    public static int a(boolean z) {
        return 1;
    }

    public static c a(OutputStream outputStream) {
        return a(outputStream, 4096);
    }

    public static c a(OutputStream outputStream, int i) {
        return new c(outputStream, new byte[i]);
    }

    public static c a(byte[] bArr, int i, int i2) {
        return new c(bArr, i, i2);
    }

    public static int b(int i) {
        return d(i);
    }

    public static int b(int i, int i2) {
        return c(i) + b(i2);
    }

    public static int b(int i, long j) {
        return c(i) + b(j);
    }

    public static int b(long j) {
        return c(j);
    }

    public static int c(int i) {
        return d(f.a(i, 0));
    }

    public static int c(long j) {
        if (((-128) & j) == 0) {
            return 1;
        }
        if (((-16384) & j) == 0) {
            return 2;
        }
        if (((-2097152) & j) == 0) {
            return 3;
        }
        if (((-268435456) & j) == 0) {
            return 4;
        }
        if (((-34359738368L) & j) == 0) {
            return 5;
        }
        if (((-4398046511104L) & j) == 0) {
            return 6;
        }
        if (((-562949953421312L) & j) == 0) {
            return 7;
        }
        if (((-72057594037927936L) & j) == 0) {
            return 8;
        }
        return (j & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    public static int d(int i) {
        if ((i & (-128)) == 0) {
            return 1;
        }
        if ((i & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i) == 0) {
            return 3;
        }
        return (i & (-268435456)) == 0 ? 4 : 5;
    }

    public int a() {
        if (this.f146a == null) {
            return this.a - this.b;
        }
        throw new UnsupportedOperationException("spaceLeft() can only be called on CodedOutputStreams that are writing to a flat array.");
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1990a() {
        if (this.f146a != null) {
            c();
        }
    }

    public void a(byte b) {
        if (this.b == this.a) {
            c();
        }
        byte[] bArr = this.f147a;
        int i = this.b;
        this.b = i + 1;
        bArr[i] = b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1991a(int i) {
        if (i >= 0) {
            m2010d(i);
        } else {
            m2009c(i);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1992a(int i, int i2) {
        c(i, 0);
        m1991a(i2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1993a(int i, long j) {
        c(i, 0);
        m1998a(j);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1994a(int i, com.xiaomi.push.a aVar) {
        c(i, 2);
        m1999a(aVar);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1995a(int i, e eVar) {
        c(i, 2);
        m2000a(eVar);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1996a(int i, String str) {
        c(i, 2);
        m2001a(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1997a(int i, boolean z) {
        c(i, 0);
        m2002a(z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1998a(long j) {
        m2009c(j);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1999a(com.xiaomi.push.a aVar) {
        byte[] m1929a = aVar.m1929a();
        m2010d(m1929a.length);
        a(m1929a);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2000a(e eVar) {
        m2010d(eVar.mo2116a());
        eVar.a(this);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2001a(String str) {
        byte[] bytes = str.getBytes(Keyczar.DEFAULT_ENCODING);
        m2010d(bytes.length);
        a(bytes);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2002a(boolean z) {
        m2008c(z ? 1 : 0);
    }

    public void a(byte[] bArr) {
        m2003a(bArr, 0, bArr.length);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2003a(byte[] bArr, int i, int i2) {
        int i3 = this.a;
        int i4 = this.b;
        if (i3 - i4 >= i2) {
            System.arraycopy(bArr, i, this.f147a, i4, i2);
            this.b += i2;
            return;
        }
        int i5 = i3 - i4;
        System.arraycopy(bArr, i, this.f147a, i4, i5);
        int i6 = i + i5;
        int i7 = i2 - i5;
        this.b = this.a;
        c();
        if (i7 > this.a) {
            this.f146a.write(bArr, i6, i7);
            return;
        }
        System.arraycopy(bArr, i6, this.f147a, 0, i7);
        this.b = i7;
    }

    public void b() {
        if (a() == 0) {
            return;
        }
        throw new IllegalStateException("Did not write as much data as expected.");
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2004b(int i) {
        m2010d(i);
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2005b(int i, int i2) {
        c(i, 0);
        m2004b(i2);
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2006b(int i, long j) {
        c(i, 0);
        m2007b(j);
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2007b(long j) {
        m2009c(j);
    }

    public final void c() {
        OutputStream outputStream = this.f146a;
        if (outputStream != null) {
            outputStream.write(this.f147a, 0, this.b);
            this.b = 0;
            return;
        }
        throw new a();
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m2008c(int i) {
        a((byte) i);
    }

    public void c(int i, int i2) {
        m2010d(f.a(i, i2));
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m2009c(long j) {
        while (((-128) & j) != 0) {
            m2008c((((int) j) & BaiduSceneResult.BANK_CARD) | 128);
            j >>>= 7;
        }
        m2008c((int) j);
    }

    /* renamed from: d  reason: collision with other method in class */
    public void m2010d(int i) {
        while ((i & (-128)) != 0) {
            m2008c((i & BaiduSceneResult.BANK_CARD) | 128);
            i >>>= 7;
        }
        m2008c(i);
    }
}
