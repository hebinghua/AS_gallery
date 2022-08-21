package com.xiaomi.push;

/* loaded from: classes3.dex */
public class ae {
    public static int a;

    /* renamed from: a  reason: collision with other field name */
    public static final String f83a;

    /* renamed from: a  reason: collision with other field name */
    public static final boolean f84a;
    public static final boolean b;
    public static final boolean c;
    public static final boolean d;
    public static boolean e;
    public static final boolean f;
    public static final boolean g;

    static {
        int i;
        String str = ah.f85a ? "ONEBOX" : "@SHIP.TO.2A2FE0D7@";
        f83a = str;
        boolean contains = str.contains("2A2FE0D7");
        f84a = contains;
        boolean z = false;
        b = contains || "DEBUG".equalsIgnoreCase(str);
        c = "LOGABLE".equalsIgnoreCase(str);
        d = str.contains("YY");
        e = str.equalsIgnoreCase("TEST");
        f = "BETA".equalsIgnoreCase(str);
        if (str.startsWith("RC")) {
            z = true;
        }
        g = z;
        a = 1;
        if (str.equalsIgnoreCase("SANDBOX")) {
            i = 2;
        } else if (!str.equalsIgnoreCase("ONEBOX")) {
            a = 1;
            return;
        } else {
            i = 3;
        }
        a = i;
    }

    public static int a() {
        return a;
    }

    public static void a(int i) {
        a = i;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m1932a() {
        return a == 2;
    }

    public static boolean b() {
        return a == 3;
    }
}
