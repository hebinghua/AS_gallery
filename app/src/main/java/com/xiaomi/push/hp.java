package com.xiaomi.push;

/* loaded from: classes3.dex */
public enum hp {
    MISC_CONFIG(1),
    PLUGIN_CONFIG(2);
    

    /* renamed from: a  reason: collision with other field name */
    private final int f475a;

    hp(int i) {
        this.f475a = i;
    }

    public static hp a(int i) {
        if (i != 1) {
            if (i == 2) {
                return PLUGIN_CONFIG;
            }
            return null;
        }
        return MISC_CONFIG;
    }

    public int a() {
        return this.f475a;
    }
}
