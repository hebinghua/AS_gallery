package com.xiaomi.onetrack.b;

/* loaded from: classes3.dex */
public class n implements Runnable {
    public final /* synthetic */ m a;

    public n(m mVar) {
        this.a = mVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.a.a.a();
        try {
            boolean a = com.xiaomi.onetrack.f.c.a();
            com.xiaomi.onetrack.util.p.a("UploadTimer", "UploadTimer netReceiver, 网络是否可用=" + a);
            if (!a) {
                return;
            }
            int[] iArr = {0, 1, 2};
            for (int i = 0; i < 3; i++) {
                int i2 = iArr[i];
                int a2 = com.xiaomi.onetrack.a.m.a(i2);
                if (!this.a.a.hasMessages(i2)) {
                    this.a.a.sendEmptyMessageDelayed(i2, a2);
                }
            }
        } catch (Exception e) {
            com.xiaomi.onetrack.util.p.a("UploadTimer", "netReceiver: " + e);
        }
    }
}
