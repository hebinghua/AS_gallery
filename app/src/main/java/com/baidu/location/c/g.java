package com.baidu.location.c;

import android.location.OnNmeaMessageListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class g implements OnNmeaMessageListener {
    public final /* synthetic */ f a;

    public g(f fVar) {
        this.a = fVar;
    }

    @Override // android.location.OnNmeaMessageListener
    public void onNmeaMessage(String str, long j) {
        if (this.a.J != null) {
            this.a.J.sendMessage(this.a.J.obtainMessage(5, str));
        }
    }
}
