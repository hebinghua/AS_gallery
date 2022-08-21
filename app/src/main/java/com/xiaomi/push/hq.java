package com.xiaomi.push;

/* loaded from: classes3.dex */
public enum hq {
    INT(1),
    LONG(2),
    STRING(3),
    BOOLEAN(4);
    

    /* renamed from: a  reason: collision with other field name */
    private final int f477a;

    hq(int i) {
        this.f477a = i;
    }

    public static hq a(int i) {
        if (i != 1) {
            if (i == 2) {
                return LONG;
            }
            if (i == 3) {
                return STRING;
            }
            if (i == 4) {
                return BOOLEAN;
            }
            return null;
        }
        return INT;
    }
}
