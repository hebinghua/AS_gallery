package com.xiaomi.push;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class jb extends jf {
    public static final jk a = new jk();

    /* renamed from: a  reason: collision with other field name */
    public int f786a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f787a;

    /* renamed from: a  reason: collision with other field name */
    public byte[] f788a;
    public boolean b;

    /* renamed from: b  reason: collision with other field name */
    public byte[] f789b;
    public boolean c;

    /* renamed from: c  reason: collision with other field name */
    public byte[] f790c;
    public byte[] d;
    public byte[] e;
    public byte[] f;
    public byte[] g;
    public byte[] h;

    /* loaded from: classes3.dex */
    public static class a implements jh {
        public int a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f791a;
        public boolean b;

        public a() {
            this(false, true);
        }

        public a(boolean z, boolean z2) {
            this(z, z2, 0);
        }

        public a(boolean z, boolean z2, int i) {
            this.f791a = false;
            this.b = true;
            this.f791a = z;
            this.b = z2;
            this.a = i;
        }

        @Override // com.xiaomi.push.jh
        public jf a(jp jpVar) {
            jb jbVar = new jb(jpVar, this.f791a, this.b);
            int i = this.a;
            if (i != 0) {
                jbVar.b(i);
            }
            return jbVar;
        }
    }

    public jb(jp jpVar, boolean z, boolean z2) {
        super(jpVar);
        this.f787a = false;
        this.b = true;
        this.c = false;
        this.f788a = new byte[1];
        this.f789b = new byte[2];
        this.f790c = new byte[4];
        this.d = new byte[8];
        this.e = new byte[1];
        this.f = new byte[2];
        this.g = new byte[4];
        this.h = new byte[8];
        this.f787a = z;
        this.b = z2;
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public byte mo2391a() {
        if (((jf) this).a.b() < 1) {
            a(this.e, 0, 1);
            return this.e[0];
        }
        byte b = ((jf) this).a.a()[((jf) this).a.mo2395a()];
        ((jf) this).a.a(1);
        return b;
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public double mo2391a() {
        return Double.longBitsToDouble(mo2391a());
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public int mo2391a() {
        byte[] bArr = this.g;
        int i = 0;
        if (((jf) this).a.b() >= 4) {
            bArr = ((jf) this).a.a();
            i = ((jf) this).a.mo2395a();
            ((jf) this).a.a(4);
        } else {
            a(this.g, 0, 4);
        }
        return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
    }

    public final int a(byte[] bArr, int i, int i2) {
        c(i2);
        return ((jf) this).a.b(bArr, i, i2);
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public long mo2391a() {
        byte[] bArr = this.h;
        int i = 0;
        if (((jf) this).a.b() >= 8) {
            bArr = ((jf) this).a.a();
            i = ((jf) this).a.mo2395a();
            ((jf) this).a.a(8);
        } else {
            a(this.h, 0, 8);
        }
        return (bArr[i + 7] & 255) | ((bArr[i] & 255) << 56) | ((bArr[i + 1] & 255) << 48) | ((bArr[i + 2] & 255) << 40) | ((bArr[i + 3] & 255) << 32) | ((bArr[i + 4] & 255) << 24) | ((bArr[i + 5] & 255) << 16) | ((bArr[i + 6] & 255) << 8);
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public jc mo2391a() {
        byte mo2391a = mo2391a();
        return new jc("", mo2391a, mo2391a == 0 ? (short) 0 : mo2391a());
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public jd mo2391a() {
        return new jd(mo2391a(), mo2391a());
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public je mo2391a() {
        return new je(mo2391a(), mo2391a(), mo2391a());
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public jj mo2391a() {
        return new jj(mo2391a(), mo2391a());
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public jk mo2391a() {
        return a;
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public String mo2391a() {
        int mo2391a = mo2391a();
        if (((jf) this).a.b() >= mo2391a) {
            try {
                String str = new String(((jf) this).a.a(), ((jf) this).a.mo2395a(), mo2391a, Keyczar.DEFAULT_ENCODING);
                ((jf) this).a.a(mo2391a);
                return str;
            } catch (UnsupportedEncodingException unused) {
                throw new iz("JVM DOES NOT SUPPORT UTF-8");
            }
        }
        return mo2376a(mo2391a);
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public String mo2376a(int i) {
        try {
            c(i);
            byte[] bArr = new byte[i];
            ((jf) this).a.b(bArr, 0, i);
            return new String(bArr, Keyczar.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException unused) {
            throw new iz("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public ByteBuffer mo2391a() {
        int mo2391a = mo2391a();
        c(mo2391a);
        if (((jf) this).a.b() >= mo2391a) {
            ByteBuffer wrap = ByteBuffer.wrap(((jf) this).a.a(), ((jf) this).a.mo2395a(), mo2391a);
            ((jf) this).a.a(mo2391a);
            return wrap;
        }
        byte[] bArr = new byte[mo2391a];
        ((jf) this).a.b(bArr, 0, mo2391a);
        return ByteBuffer.wrap(bArr);
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public short mo2391a() {
        byte[] bArr = this.f;
        int i = 0;
        if (((jf) this).a.b() >= 2) {
            bArr = ((jf) this).a.a();
            i = ((jf) this).a.mo2395a();
            ((jf) this).a.a(2);
        } else {
            a(this.f, 0, 2);
        }
        return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a */
    public void mo2391a() {
    }

    @Override // com.xiaomi.push.jf
    public void a(byte b) {
        byte[] bArr = this.f788a;
        bArr[0] = b;
        ((jf) this).a.a(bArr, 0, 1);
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public void mo2376a(int i) {
        byte[] bArr = this.f790c;
        bArr[0] = (byte) ((i >> 24) & 255);
        bArr[1] = (byte) ((i >> 16) & 255);
        bArr[2] = (byte) ((i >> 8) & 255);
        bArr[3] = (byte) (i & 255);
        ((jf) this).a.a(bArr, 0, 4);
    }

    @Override // com.xiaomi.push.jf
    public void a(long j) {
        byte[] bArr = this.d;
        bArr[0] = (byte) ((j >> 56) & 255);
        bArr[1] = (byte) ((j >> 48) & 255);
        bArr[2] = (byte) ((j >> 40) & 255);
        bArr[3] = (byte) ((j >> 32) & 255);
        bArr[4] = (byte) ((j >> 24) & 255);
        bArr[5] = (byte) ((j >> 16) & 255);
        bArr[6] = (byte) ((j >> 8) & 255);
        bArr[7] = (byte) (j & 255);
        ((jf) this).a.a(bArr, 0, 8);
    }

    @Override // com.xiaomi.push.jf
    public void a(jc jcVar) {
        a(jcVar.a);
        a(jcVar.f793a);
    }

    @Override // com.xiaomi.push.jf
    public void a(jd jdVar) {
        a(jdVar.a);
        mo2376a(jdVar.f794a);
    }

    @Override // com.xiaomi.push.jf
    public void a(je jeVar) {
        a(jeVar.a);
        a(jeVar.b);
        mo2376a(jeVar.f795a);
    }

    @Override // com.xiaomi.push.jf
    public void a(jk jkVar) {
    }

    @Override // com.xiaomi.push.jf
    public void a(String str) {
        try {
            byte[] bytes = str.getBytes(Keyczar.DEFAULT_ENCODING);
            mo2376a(bytes.length);
            ((jf) this).a.a(bytes, 0, bytes.length);
        } catch (UnsupportedEncodingException unused) {
            throw new iz("JVM DOES NOT SUPPORT UTF-8");
        }
    }

    @Override // com.xiaomi.push.jf
    public void a(ByteBuffer byteBuffer) {
        int limit = (byteBuffer.limit() - byteBuffer.position()) - byteBuffer.arrayOffset();
        mo2376a(limit);
        ((jf) this).a.a(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), limit);
    }

    @Override // com.xiaomi.push.jf
    public void a(short s) {
        byte[] bArr = this.f789b;
        bArr[0] = (byte) ((s >> 8) & 255);
        bArr[1] = (byte) (s & 255);
        ((jf) this).a.a(bArr, 0, 2);
    }

    @Override // com.xiaomi.push.jf
    public void a(boolean z) {
        a(z ? (byte) 1 : (byte) 0);
    }

    @Override // com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2391a() {
        return mo2391a() == 1;
    }

    @Override // com.xiaomi.push.jf
    public void b() {
    }

    public void b(int i) {
        this.f786a = i;
        this.c = true;
    }

    @Override // com.xiaomi.push.jf
    public void c() {
        a((byte) 0);
    }

    public void c(int i) {
        if (i < 0) {
            throw new iz("Negative length: " + i);
        } else if (!this.c) {
        } else {
            int i2 = this.f786a - i;
            this.f786a = i2;
            if (i2 >= 0) {
                return;
            }
            throw new iz("Message length exceeded: " + i);
        }
    }

    @Override // com.xiaomi.push.jf
    public void d() {
    }

    @Override // com.xiaomi.push.jf
    public void e() {
    }

    @Override // com.xiaomi.push.jf
    public void f() {
    }

    @Override // com.xiaomi.push.jf
    public void g() {
    }

    @Override // com.xiaomi.push.jf
    public void h() {
    }

    @Override // com.xiaomi.push.jf
    public void i() {
    }

    @Override // com.xiaomi.push.jf
    public void j() {
    }
}
