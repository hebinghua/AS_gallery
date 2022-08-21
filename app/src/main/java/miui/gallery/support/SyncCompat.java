package miui.gallery.support;

import android.annotation.SuppressLint;
import android.content.SyncRequest;
import android.util.Log;

/* loaded from: classes3.dex */
public class SyncCompat {
    public static volatile Boolean sSupportRequiresCharging;

    @SuppressLint({"NewApi"})
    public static void setRequiresCharging(SyncRequest.Builder builder, boolean z) {
        if (sSupportRequiresCharging == null) {
            try {
                builder.setRequiresCharging(z);
                setRequiresCharging(true);
            } catch (NoSuchMethodError e) {
                Log.w("SyncCompat", "setRequiresCharging is not supported", e);
                setRequiresCharging(false);
            }
        } else if (!sSupportRequiresCharging.booleanValue()) {
        } else {
            builder.setRequiresCharging(z);
        }
    }

    public static synchronized void setRequiresCharging(boolean z) {
        synchronized (SyncCompat.class) {
            sSupportRequiresCharging = Boolean.valueOf(z);
        }
    }
}
