package com.baidu.b.a;

/* loaded from: classes.dex */
class f {
    public static int a = 5;
    public static int b = 40;
    private b c;

    public f() {
        b bVar = new b(b);
        this.c = bVar;
        bVar.a(0, b, true);
    }

    public void a(b bVar, int i, int i2, int i3) {
        b c = this.c.c(i, i + i2);
        if (i3 != 0) {
            if (i3 != 1) {
                if (i3 == 2) {
                    c.d(bVar);
                } else if (i3 == 3) {
                    c.b(bVar);
                }
            }
            c.c(bVar);
        } else {
            c.a(bVar);
        }
        for (int i4 = 0; i4 < i2; i4++) {
            this.c.a(i + i4, c.c(i4));
        }
    }

    public byte[] a() {
        return this.c.a();
    }
}
