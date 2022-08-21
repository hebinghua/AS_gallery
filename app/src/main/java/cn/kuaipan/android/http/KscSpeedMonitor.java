package cn.kuaipan.android.http;

/* loaded from: classes.dex */
public class KscSpeedMonitor {
    public final String mHost;
    public long mLatestUpdate = KscSpeedManager.current();
    public final KscSpeedManager mManager;

    public KscSpeedMonitor(KscSpeedManager kscSpeedManager, String str) {
        this.mManager = kscSpeedManager;
        this.mHost = str;
    }

    public void recode(long j, long j2, long j3) {
        KscSpeedManager kscSpeedManager = this.mManager;
        if (kscSpeedManager != null) {
            kscSpeedManager.recoder(this.mHost, j, j2, (float) j3);
            if (j2 <= this.mLatestUpdate) {
                return;
            }
            this.mLatestUpdate = j2;
        }
    }

    public void recode(long j) {
        if (this.mManager != null) {
            long current = KscSpeedManager.current();
            this.mManager.recoder(this.mHost, this.mLatestUpdate, current, (float) j);
            this.mLatestUpdate = current;
        }
    }
}
