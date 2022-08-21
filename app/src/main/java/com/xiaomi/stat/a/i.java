package com.xiaomi.stat.a;

import android.database.DatabaseUtils;
import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class i implements Callable<Long> {
    public final /* synthetic */ c a;

    public i(c cVar) {
        this.a = cVar;
    }

    @Override // java.util.concurrent.Callable
    /* renamed from: a */
    public Long call() {
        a aVar;
        aVar = this.a.l;
        return Long.valueOf(DatabaseUtils.queryNumEntries(aVar.getReadableDatabase(), j.b));
    }
}
