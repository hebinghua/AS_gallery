package com.larvalabs.svgandroid;

/* compiled from: ParserHelper.java */
/* loaded from: classes.dex */
public class a {
    public static final double[] e = new double[128];
    public int a;
    public char b;
    public CharSequence c;
    public int d;

    public a(CharSequence charSequence, int i) {
        this.c = charSequence;
        this.a = i;
        this.d = charSequence.length();
        this.b = charSequence.charAt(i);
    }

    public final char f() {
        int i = this.a;
        int i2 = this.d;
        if (i < i2) {
            this.a = i + 1;
        }
        int i3 = this.a;
        if (i3 == i2) {
            return (char) 0;
        }
        return this.c.charAt(i3);
    }

    public void a() {
        while (true) {
            int i = this.a;
            if (i >= this.d || !Character.isWhitespace(this.c.charAt(i))) {
                return;
            }
            c();
        }
    }

    public void b() {
        while (true) {
            int i = this.a;
            if (i < this.d) {
                char charAt = this.c.charAt(i);
                if (charAt != '\t' && charAt != '\n' && charAt != ' ' && charAt != ',') {
                    return;
                }
                c();
            } else {
                return;
            }
        }
    }

    public void c() {
        this.b = f();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0025 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0028 A[LOOP:0: B:13:0x0028->B:17:0x0034, LOOP_START] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0059  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x009a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00c2 A[LOOP:3: B:62:0x00c2->B:63:0x00c8, LOOP_START] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00e9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public float d() {
        /*
            Method dump skipped, instructions count: 508
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.larvalabs.svgandroid.a.d():float");
    }

    public final void a(char c) {
        throw new RuntimeException("Unexpected char '" + c + "'.");
    }

    public static float a(int i, int i2) {
        if (i2 < -125 || i == 0) {
            return 0.0f;
        }
        if (i2 >= 128) {
            return i > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        } else if (i2 == 0) {
            return i;
        } else {
            if (i >= 67108864) {
                i++;
            }
            double d = i;
            double[] dArr = e;
            return (float) (i2 > 0 ? d * dArr[i2] : d / dArr[-i2]);
        }
    }

    static {
        int i = 0;
        while (true) {
            double[] dArr = e;
            if (i < dArr.length) {
                dArr[i] = Math.pow(10.0d, i);
                i++;
            } else {
                return;
            }
        }
    }

    public float e() {
        a();
        float d = d();
        b();
        return d;
    }
}
