package com.xiaomi.push;

import java.io.ByteArrayOutputStream;

/* loaded from: classes3.dex */
public class ja {
    public jf a;

    /* renamed from: a  reason: collision with other field name */
    public final jm f784a;

    /* renamed from: a  reason: collision with other field name */
    public final ByteArrayOutputStream f785a;

    public ja(jh jhVar) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.f785a = byteArrayOutputStream;
        jm jmVar = new jm(byteArrayOutputStream);
        this.f784a = jmVar;
        this.a = jhVar.a(jmVar);
    }

    public byte[] a(iu iuVar) {
        this.f785a.reset();
        iuVar.b(this.a);
        return this.f785a.toByteArray();
    }
}
