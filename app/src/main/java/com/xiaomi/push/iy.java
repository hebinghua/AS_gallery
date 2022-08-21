package com.xiaomi.push;

/* loaded from: classes3.dex */
public class iy {
    public final jf a;

    /* renamed from: a  reason: collision with other field name */
    public final jo f780a;

    public iy(jh jhVar) {
        jo joVar = new jo();
        this.f780a = joVar;
        this.a = jhVar.a(joVar);
    }

    public void a(iu iuVar, byte[] bArr) {
        try {
            this.f780a.a(bArr);
            iuVar.a(this.a);
        } finally {
            this.a.k();
        }
    }
}
