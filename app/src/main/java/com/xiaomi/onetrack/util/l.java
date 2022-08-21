package com.xiaomi.onetrack.util;

import android.text.TextUtils;
import android.util.LruCache;
import com.xiaomi.onetrack.util.k;

/* loaded from: classes3.dex */
public final class l extends LruCache<String, k.a> {
    public l(int i) {
        super(i);
    }

    @Override // android.util.LruCache
    /* renamed from: a */
    public int sizeOf(String str, k.a aVar) {
        if (aVar == null || TextUtils.isEmpty(aVar.a)) {
            return 0;
        }
        return aVar.a.length();
    }
}
