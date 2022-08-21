package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.Adler32;

/* loaded from: classes3.dex */
public class fn {
    public Context a;

    /* renamed from: a  reason: collision with other field name */
    public fs f362a;

    /* renamed from: a  reason: collision with other field name */
    public InputStream f363a;

    /* renamed from: a  reason: collision with other field name */
    public volatile boolean f366a;

    /* renamed from: a  reason: collision with other field name */
    public byte[] f367a;

    /* renamed from: a  reason: collision with other field name */
    public ByteBuffer f364a = ByteBuffer.allocate(2048);
    public ByteBuffer b = ByteBuffer.allocate(4);

    /* renamed from: a  reason: collision with other field name */
    public Adler32 f365a = new Adler32();

    /* renamed from: a  reason: collision with other field name */
    public fq f361a = new fq();

    public fn(InputStream inputStream, fs fsVar, Context context) {
        this.f363a = new BufferedInputStream(inputStream);
        this.f362a = fsVar;
        this.a = context;
    }

    public fl a() {
        int i;
        ByteBuffer m2166a;
        try {
            m2166a = m2166a();
            i = m2166a.position();
        } catch (IOException e) {
            e = e;
            i = 0;
        }
        try {
            m2166a.flip();
            m2166a.position(8);
            fl frVar = i == 8 ? new fr() : fl.a(m2166a.slice());
            com.xiaomi.channel.commonutils.logger.b.c("[Slim] Read {cmd=" + frVar.m2158a() + ";chid=" + frVar.a() + ";len=" + i + "}");
            return frVar;
        } catch (IOException e2) {
            e = e2;
            if (i == 0) {
                i = this.f364a.position();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[Slim] read Blob [");
            byte[] array = this.f364a.array();
            if (i > 128) {
                i = 128;
            }
            sb.append(ai.a(array, 0, i));
            sb.append("] Err:");
            sb.append(e.getMessage());
            com.xiaomi.channel.commonutils.logger.b.m1859a(sb.toString());
            throw e;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00cf  */
    /* renamed from: a  reason: collision with other method in class */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.nio.ByteBuffer m2166a() {
        /*
            Method dump skipped, instructions count: 266
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fn.m2166a():java.nio.ByteBuffer");
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2167a() {
        try {
            c();
        } catch (IOException e) {
            if (!this.f366a) {
                throw e;
            }
        }
    }

    public final void a(ByteBuffer byteBuffer, int i) {
        int position = byteBuffer.position();
        do {
            int read = this.f363a.read(byteBuffer.array(), position, i);
            if (read == -1) {
                throw new EOFException();
            }
            i -= read;
            position += read;
        } while (i > 0);
        byteBuffer.position(position);
    }

    public void b() {
        this.f366a = true;
    }

    public final void c() {
        String str;
        StringBuilder sb;
        boolean z = false;
        this.f366a = false;
        fl a = a();
        if ("CONN".equals(a.m2158a())) {
            dx$f a2 = dx$f.a(a.m2162a());
            if (a2.mo2116a()) {
                this.f362a.a(a2.mo2116a());
                z = true;
            }
            if (a2.c()) {
                dx$b mo2116a = a2.mo2116a();
                fl flVar = new fl();
                flVar.a("SYNC", "CONF");
                flVar.a(mo2116a.a(), (String) null);
                this.f362a.a(flVar);
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("[Slim] CONN: host = " + a2.mo2118b());
        }
        if (!z) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("[Slim] Invalid CONN");
            throw new IOException("Invalid Connection");
        }
        this.f367a = this.f362a.mo2190a();
        while (!this.f366a) {
            fl a3 = a();
            this.f362a.c();
            short m2160a = a3.m2160a();
            if (m2160a != 1) {
                if (m2160a != 2) {
                    if (m2160a != 3) {
                        str = "[Slim] unknow blob type " + ((int) a3.m2160a());
                        com.xiaomi.channel.commonutils.logger.b.m1859a(str);
                    } else {
                        try {
                            this.f362a.b(this.f361a.a(a3.m2162a(), this.f362a));
                        } catch (Exception e) {
                            e = e;
                            sb = new StringBuilder();
                            sb.append("[Slim] Parse packet from Blob chid=");
                            sb.append(a3.a());
                            sb.append("; Id=");
                            sb.append(a3.e());
                            sb.append(" failure:");
                            sb.append(e.getMessage());
                            str = sb.toString();
                            com.xiaomi.channel.commonutils.logger.b.m1859a(str);
                        }
                    }
                } else if ("SECMSG".equals(a3.m2158a()) && ((a3.a() == 2 || a3.a() == 3) && TextUtils.isEmpty(a3.m2164b()))) {
                    try {
                        this.f362a.b(this.f361a.a(a3.m2163a(com.xiaomi.push.service.bg.a().a(Integer.valueOf(a3.a()).toString(), a3.g()).h), this.f362a));
                    } catch (Exception e2) {
                        e = e2;
                        sb = new StringBuilder();
                        sb.append("[Slim] Parse packet from Blob chid=");
                        sb.append(a3.a());
                        sb.append("; Id=");
                        sb.append(a3.e());
                        sb.append(" failure:");
                        sb.append(e.getMessage());
                        str = sb.toString();
                        com.xiaomi.channel.commonutils.logger.b.m1859a(str);
                    }
                } else if (a3.a() == 10) {
                    a3.b(10);
                    a3.f357a.f881a = w.a(this.a);
                    a3.f357a.f883b = bj.e(this.a);
                    a3.f357a.f880a = System.currentTimeMillis();
                    com.xiaomi.channel.commonutils.logger.b.c("rcv blob from chid 10");
                }
            }
            this.f362a.a(a3);
        }
    }
}
