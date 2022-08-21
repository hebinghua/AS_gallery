package cn.kuaipan.android.http;

import com.xiaomi.opensdk.file.model.MiCloudTransferStopper;

/* loaded from: classes.dex */
public interface KssTransferStopper {
    boolean checkStop();

    /* loaded from: classes.dex */
    public static class KssTransferStopperFromMiCloudTransferStopper implements KssTransferStopper {
        public final MiCloudTransferStopper mMiCloudTransferStopper;

        public static KssTransferStopper get(MiCloudTransferStopper miCloudTransferStopper) {
            if (miCloudTransferStopper == null) {
                return null;
            }
            return new KssTransferStopperFromMiCloudTransferStopper(miCloudTransferStopper);
        }

        public KssTransferStopperFromMiCloudTransferStopper(MiCloudTransferStopper miCloudTransferStopper) {
            this.mMiCloudTransferStopper = miCloudTransferStopper;
        }

        @Override // cn.kuaipan.android.http.KssTransferStopper
        public boolean checkStop() {
            return this.mMiCloudTransferStopper.checkStop();
        }
    }
}
