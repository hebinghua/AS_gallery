package com.xiaomi.push;

import android.text.TextUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class fl {

    /* renamed from: a  reason: collision with other field name */
    public int f355a;

    /* renamed from: a  reason: collision with other field name */
    public dx$a f356a;

    /* renamed from: a  reason: collision with other field name */
    public com.xiaomi.push.service.ao f357a;

    /* renamed from: a  reason: collision with other field name */
    public String f358a;

    /* renamed from: a  reason: collision with other field name */
    public short f359a;

    /* renamed from: b  reason: collision with other field name */
    public byte[] f360b;
    public static String b = gy.a(5) + "-";
    public static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    public static final byte[] f354a = new byte[0];

    public fl() {
        this.f359a = (short) 2;
        this.f360b = f354a;
        this.f358a = null;
        this.f357a = null;
        this.f356a = new dx$a();
        this.f355a = 1;
    }

    public fl(dx$a dx_a, short s, byte[] bArr) {
        this.f359a = (short) 2;
        this.f360b = f354a;
        this.f358a = null;
        this.f357a = null;
        this.f356a = dx_a;
        this.f359a = s;
        this.f360b = bArr;
        this.f355a = 2;
    }

    @Deprecated
    public static fl a(gn gnVar, String str) {
        int i;
        fl flVar = new fl();
        try {
            i = Integer.parseInt(gnVar.k());
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Blob parse chid err " + e.getMessage());
            i = 1;
        }
        flVar.a(i);
        flVar.a(gnVar.j());
        flVar.c(gnVar.m());
        flVar.b(gnVar.n());
        flVar.a("XMLMSG", (String) null);
        try {
            flVar.a(gnVar.m2199a().getBytes("utf8"), str);
            if (TextUtils.isEmpty(str)) {
                flVar.a((short) 3);
            } else {
                flVar.a((short) 2);
                flVar.a("SECMSG", (String) null);
            }
        } catch (UnsupportedEncodingException e2) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Blob setPayload err： " + e2.getMessage());
        }
        return flVar;
    }

    public static fl a(ByteBuffer byteBuffer) {
        try {
            ByteBuffer slice = byteBuffer.slice();
            short s = slice.getShort(0);
            short s2 = slice.getShort(2);
            int i = slice.getInt(4);
            dx$a dx_a = new dx$a();
            dx_a.a(slice.array(), slice.arrayOffset() + 8, (int) s2);
            byte[] bArr = new byte[i];
            slice.position(s2 + 8);
            slice.get(bArr, 0, i);
            return new fl(dx_a, s, bArr);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("read Blob err :" + e.getMessage());
            throw new IOException("Malformed Input");
        }
    }

    public static synchronized String d() {
        String sb;
        synchronized (fl.class) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(b);
            long j = a;
            a = 1 + j;
            sb2.append(Long.toString(j));
            sb = sb2.toString();
        }
        return sb;
    }

    public int a() {
        return this.f356a.c();
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2158a() {
        return this.f356a.m2061c();
    }

    /* renamed from: a  reason: collision with other method in class */
    public ByteBuffer mo2159a(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(c());
        }
        byteBuffer.putShort(this.f359a);
        byteBuffer.putShort((short) this.f356a.mo2116a());
        byteBuffer.putInt(this.f360b.length);
        int position = byteBuffer.position();
        this.f356a.m2120a(byteBuffer.array(), byteBuffer.arrayOffset() + position, this.f356a.mo2116a());
        byteBuffer.position(position + this.f356a.mo2116a());
        byteBuffer.put(this.f360b);
        return byteBuffer;
    }

    /* renamed from: a  reason: collision with other method in class */
    public short m2160a() {
        return this.f359a;
    }

    public void a(int i) {
        this.f356a.a(i);
    }

    public void a(long j, String str, String str2) {
        if (j != 0) {
            this.f356a.a(j);
        }
        if (!TextUtils.isEmpty(str)) {
            this.f356a.a(str);
        }
        if (!TextUtils.isEmpty(str2)) {
            this.f356a.b(str2);
        }
    }

    public void a(String str) {
        this.f356a.e(str);
    }

    public void a(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            this.f356a.c(str);
            this.f356a.mo2116a();
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            this.f356a.d(str2);
            return;
        }
        throw new IllegalArgumentException("command should not be empty");
    }

    public void a(short s) {
        this.f359a = s;
    }

    public void a(byte[] bArr, String str) {
        if (!TextUtils.isEmpty(str)) {
            this.f356a.c(1);
            this.f360b = com.xiaomi.push.service.bp.a(com.xiaomi.push.service.bp.a(str, e()), bArr);
            return;
        }
        this.f356a.c(0);
        this.f360b = bArr;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2161a() {
        return this.f356a.j();
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m2162a() {
        return fm.a(this, this.f360b);
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m2163a(String str) {
        if (this.f356a.e() == 1) {
            return fm.a(this, com.xiaomi.push.service.bp.a(com.xiaomi.push.service.bp.a(str, e()), this.f360b));
        }
        if (this.f356a.e() == 0) {
            return fm.a(this, this.f360b);
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("unknow cipher = " + this.f356a.e());
        return fm.a(this, this.f360b);
    }

    public int b() {
        return this.f356a.f();
    }

    /* renamed from: b  reason: collision with other method in class */
    public String m2164b() {
        return this.f356a.m2063d();
    }

    public void b(int i) {
        com.xiaomi.push.service.ao aoVar = new com.xiaomi.push.service.ao();
        this.f357a = aoVar;
        aoVar.a = i;
    }

    public void b(String str) {
        this.f358a = str;
    }

    public int c() {
        return this.f356a.mo2118b() + 8 + this.f360b.length;
    }

    /* renamed from: c  reason: collision with other method in class */
    public String m2165c() {
        return this.f356a.m2067f();
    }

    public void c(String str) {
        if (!TextUtils.isEmpty(str)) {
            int indexOf = str.indexOf("@");
            try {
                long parseLong = Long.parseLong(str.substring(0, indexOf));
                int indexOf2 = str.indexOf(com.xiaomi.stat.b.h.g, indexOf);
                String substring = str.substring(indexOf + 1, indexOf2);
                String substring2 = str.substring(indexOf2 + 1);
                this.f356a.a(parseLong);
                this.f356a.a(substring);
                this.f356a.b(substring2);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("Blob parse user err " + e.getMessage());
            }
        }
    }

    public String e() {
        String m2065e = this.f356a.m2065e();
        if ("ID_NOT_AVAILABLE".equals(m2065e)) {
            return null;
        }
        if (this.f356a.g()) {
            return m2065e;
        }
        String d = d();
        this.f356a.e(d);
        return d;
    }

    public String f() {
        return this.f358a;
    }

    public String g() {
        if (this.f356a.mo2118b()) {
            return Long.toString(this.f356a.mo2116a()) + "@" + this.f356a.mo2116a() + com.xiaomi.stat.b.h.g + this.f356a.mo2118b();
        }
        return null;
    }

    public String toString() {
        return "Blob [chid=" + a() + "; Id=" + com.xiaomi.push.service.bd.a(e()) + "; cmd=" + m2158a() + "; type=" + ((int) m2160a()) + "; from=" + g() + " ]";
    }
}
