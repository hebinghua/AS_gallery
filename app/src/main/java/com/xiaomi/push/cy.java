package com.xiaomi.push;

import android.content.Context;
import java.util.List;

/* loaded from: classes3.dex */
public abstract class cy {
    public int a;

    public cy(int i) {
        this.a = i;
    }

    public int a() {
        return this.a;
    }

    public abstract String a(Context context, String str, List<bi> list);

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2035a(Context context, String str, List<bi> list) {
        return true;
    }
}
