package com.xiaomi.onetrack.api;

import android.content.Intent;
import android.text.TextUtils;
import com.xiaomi.onetrack.util.p;

/* loaded from: classes3.dex */
public class y implements Runnable {
    public final /* synthetic */ Intent a;
    public final /* synthetic */ x b;

    public y(x xVar, Intent intent) {
        this.b = xVar;
        this.a = intent;
    }

    @Override // java.lang.Runnable
    public void run() {
        d dVar;
        try {
            String action = this.a.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (!action.equals("android.intent.action.SCREEN_OFF") && !action.equals("android.intent.action.SCREEN_ON")) {
                return;
            }
            dVar = this.b.a.b;
            com.xiaomi.onetrack.b.h.a(dVar);
        } catch (Exception e) {
            p.a("OneTrackImp", "screenReceiver exception: ", e);
        }
    }
}
