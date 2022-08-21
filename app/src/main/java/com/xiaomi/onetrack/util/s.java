package com.xiaomi.onetrack.util;

import com.xiaomi.onetrack.util.r;

/* loaded from: classes3.dex */
public final class s implements r.a {
    public final /* synthetic */ boolean a;

    public s(boolean z) {
        this.a = z;
    }

    @Override // com.xiaomi.onetrack.util.r.a
    public boolean a(Object obj) {
        if (this.a) {
            return r.a(obj);
        }
        return r.b(obj);
    }
}
