package com.xiaomi.mipush.sdk;

import android.text.TextUtils;

/* loaded from: classes3.dex */
public class x {
    public int a = 0;

    /* renamed from: a  reason: collision with other field name */
    public String f79a = "";

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof x)) {
            return false;
        }
        x xVar = (x) obj;
        return !TextUtils.isEmpty(xVar.f79a) && xVar.f79a.equals(this.f79a);
    }
}
