package com.xiaomi.push;

/* loaded from: classes3.dex */
public class ji {
    public static int a = Integer.MAX_VALUE;

    public static void a(jf jfVar, byte b) {
        a(jfVar, b, a);
    }

    public static void a(jf jfVar, byte b, int i) {
        if (i > 0) {
            int i2 = 0;
            switch (b) {
                case 2:
                    jfVar.m2389a();
                    return;
                case 3:
                    jfVar.mo2391a();
                    return;
                case 4:
                    jfVar.a();
                    return;
                case 5:
                case 7:
                case 9:
                default:
                    return;
                case 6:
                    jfVar.m2387a();
                    return;
                case 8:
                    jfVar.m2378a();
                    return;
                case 10:
                    jfVar.m2379a();
                    return;
                case 11:
                    jfVar.m2386a();
                    return;
                case 12:
                    jfVar.m2384a();
                    while (true) {
                        byte b2 = jfVar.m2380a().a;
                        if (b2 == 0) {
                            jfVar.f();
                            return;
                        } else {
                            a(jfVar, b2, i - 1);
                            jfVar.g();
                        }
                    }
                case 13:
                    je m2382a = jfVar.m2382a();
                    while (i2 < m2382a.f795a) {
                        int i3 = i - 1;
                        a(jfVar, m2382a.a, i3);
                        a(jfVar, m2382a.b, i3);
                        i2++;
                    }
                    jfVar.h();
                    return;
                case 14:
                    jj m2383a = jfVar.m2383a();
                    while (i2 < m2383a.f796a) {
                        a(jfVar, m2383a.a, i - 1);
                        i2++;
                    }
                    jfVar.j();
                    return;
                case 15:
                    jd m2381a = jfVar.m2381a();
                    while (i2 < m2381a.f794a) {
                        a(jfVar, m2381a.a, i - 1);
                        i2++;
                    }
                    jfVar.i();
                    return;
            }
        } else {
            throw new iz("Maximum skip depth exceeded");
        }
    }
}
