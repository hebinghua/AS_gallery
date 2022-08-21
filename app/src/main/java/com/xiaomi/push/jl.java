package com.xiaomi.push;

import com.xiaomi.push.jb;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class jl extends jb {
    public static int b = 10000;
    public static int c = 10000;
    public static int d = 10000;
    public static int e = 10485760;
    public static int f = 104857600;

    /* loaded from: classes3.dex */
    public static class a extends jb.a {
        public a() {
            super(false, true);
        }

        public a(boolean z, boolean z2, int i) {
            super(z, z2, i);
        }

        @Override // com.xiaomi.push.jb.a, com.xiaomi.push.jh
        public jf a(jp jpVar) {
            jl jlVar = new jl(jpVar, ((jb.a) this).f791a, this.b);
            int i = ((jb.a) this).a;
            if (i != 0) {
                jlVar.b(i);
            }
            return jlVar;
        }
    }

    public jl(jp jpVar, boolean z, boolean z2) {
        super(jpVar, z, z2);
    }

    @Override // com.xiaomi.push.jb, com.xiaomi.push.jf
    /* renamed from: a */
    public jd mo2391a() {
        byte mo2391a = mo2391a();
        int mo2391a2 = mo2391a();
        if (mo2391a2 <= c) {
            return new jd(mo2391a, mo2391a2);
        }
        throw new jg(3, "Thrift list size " + mo2391a2 + " out of range!");
    }

    @Override // com.xiaomi.push.jb, com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public je mo2391a() {
        byte mo2391a = mo2391a();
        byte mo2391a2 = mo2391a();
        int mo2391a3 = mo2391a();
        if (mo2391a3 <= b) {
            return new je(mo2391a, mo2391a2, mo2391a3);
        }
        throw new jg(3, "Thrift map size " + mo2391a3 + " out of range!");
    }

    @Override // com.xiaomi.push.jb, com.xiaomi.push.jf
    /* renamed from: a */
    public jj mo2391a() {
        byte mo2391a = mo2391a();
        int mo2391a2 = mo2391a();
        if (mo2391a2 <= d) {
            return new jj(mo2391a, mo2391a2);
        }
        throw new jg(3, "Thrift set size " + mo2391a2 + " out of range!");
    }

    @Override // com.xiaomi.push.jb, com.xiaomi.push.jf
    /* renamed from: a  reason: collision with other method in class */
    public String mo2391a() {
        int mo2391a = mo2391a();
        if (mo2391a > e) {
            throw new jg(3, "Thrift string size " + mo2391a + " out of range!");
        } else if (((jf) this).a.b() < mo2391a) {
            return mo2376a(mo2391a);
        } else {
            try {
                String str = new String(((jf) this).a.a(), ((jf) this).a.mo2395a(), mo2391a, Keyczar.DEFAULT_ENCODING);
                ((jf) this).a.a(mo2391a);
                return str;
            } catch (UnsupportedEncodingException unused) {
                throw new iz("JVM DOES NOT SUPPORT UTF-8");
            }
        }
    }

    @Override // com.xiaomi.push.jb, com.xiaomi.push.jf
    /* renamed from: a */
    public ByteBuffer mo2391a() {
        int mo2391a = mo2391a();
        if (mo2391a > f) {
            throw new jg(3, "Thrift binary size " + mo2391a + " out of range!");
        }
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
}
