package micloud.compat.v18.sync;

import android.content.Context;
import android.content.SyncResult;
import com.xiaomi.micloudsdk.utils.MiCloudSdkBuild;
import micloud.compat.independent.sync.GdprUtilsCompat;

/* loaded from: classes3.dex */
public class SyncAdapterBaseInjectorCompat {
    public static final ISyncAdapterBaseInjectorCompat sSyncAdapterBaseInjectorImpl;

    static {
        if (MiCloudSdkBuild.CURRENT_VERSION >= 23) {
            sSyncAdapterBaseInjectorImpl = new SyncAdapterBaseInjectorCompat_V23();
        } else {
            sSyncAdapterBaseInjectorImpl = new SyncAdapterBaseInjectorCompat_Base();
        }
    }

    public static boolean isGdprPermissionGranted(Context context, SyncResult syncResult) {
        boolean isGdprPermissionGranted = GdprUtilsCompat.isGdprPermissionGranted(context);
        sSyncAdapterBaseInjectorImpl.setResultByGDPRStatus(isGdprPermissionGranted, syncResult);
        return isGdprPermissionGranted;
    }
}
