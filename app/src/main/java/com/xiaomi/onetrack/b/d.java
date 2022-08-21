package com.xiaomi.onetrack.b;

import android.content.Intent;

/* loaded from: classes3.dex */
public class d implements Runnable {
    public final /* synthetic */ Intent a;
    public final /* synthetic */ c b;

    public d(c cVar, Intent intent) {
        this.b = cVar;
        this.a = intent;
    }

    @Override // java.lang.Runnable
    public void run() {
        String action = this.a.getAction();
        if (action.equals("android.intent.action.SCREEN_OFF") || action.equals("android.intent.action.SCREEN_ON")) {
            com.xiaomi.onetrack.util.p.a("EventManager", "screen on/off");
            p.a().a(action.equals("android.intent.action.SCREEN_ON") ? 0 : 2, false);
        }
    }
}
