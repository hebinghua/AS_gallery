package com.baidu.b.a;

import java.util.Arrays;

/* loaded from: classes.dex */
public class a {
    public g[] a = {new h(8, 0), new j(0, 1), new j(1, 1), new h(7, 1)};

    public byte[] a(byte[] bArr) {
        f fVar = new f();
        byte[] a = c.a(bArr, bArr.length + ((this.a.length + 1) * f.a));
        c.a(a, fVar.a(), bArr.length);
        int i = 0;
        while (true) {
            g[] gVarArr = this.a;
            if (i < gVarArr.length) {
                g gVar = gVarArr[i];
                i++;
                int length = bArr.length + (f.a * i);
                fVar.a(gVar.a(a, 0, length), gVar.a(), gVar.b(), gVar.c());
                c.a(a, fVar.a(), length);
            } else {
                return Arrays.copyOf(fVar.a(), f.a);
            }
        }
    }
}
