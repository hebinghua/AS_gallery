package com.xiaomi.push;

/* loaded from: classes3.dex */
public class bg implements bi {
    public final String a;
    public final String b;

    public bg(String str, String str2) {
        if (str != null) {
            this.a = str;
            this.b = str2;
            return;
        }
        throw new IllegalArgumentException("Name may not be null");
    }

    @Override // com.xiaomi.push.bi
    public String a() {
        return this.a;
    }

    @Override // com.xiaomi.push.bi
    public String b() {
        return this.b;
    }
}
