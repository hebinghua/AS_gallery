package com.xiaomi.stat;

import android.os.FileObserver;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ad extends FileObserver {
    public final /* synthetic */ ab a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ad(ab abVar, String str) {
        super(str);
        this.a = abVar;
    }

    @Override // android.os.FileObserver
    public void onEvent(int i, String str) {
        if (i != 2) {
            return;
        }
        synchronized (this.a) {
            this.a.b();
        }
        b.n();
    }
}
