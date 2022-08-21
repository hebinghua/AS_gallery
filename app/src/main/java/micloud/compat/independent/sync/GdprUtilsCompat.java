package micloud.compat.independent.sync;

import android.content.Context;
import com.xiaomi.micloudsdk.utils.MiCloudSDKDependencyUtil;

/* loaded from: classes3.dex */
public class GdprUtilsCompat {
    public static final IGdprUtilsCompat sGdprUtilsCompatImpl;

    static {
        int i = MiCloudSDKDependencyUtil.SDKEnvironment;
        if (i >= 24) {
            sGdprUtilsCompatImpl = new GdprUtilsCompat_V24();
        } else if (i >= 23) {
            sGdprUtilsCompatImpl = new GdprUtilsCompat_V23();
        } else {
            sGdprUtilsCompatImpl = new GdprUtilsCompat_Base();
        }
    }

    public static boolean isGdprPermissionGranted(Context context) {
        return sGdprUtilsCompatImpl.isGdprPermissionGranted(context);
    }

    public static void notifyPrivacyDenied(Context context) {
        sGdprUtilsCompatImpl.notifyPrivacyDenied(context);
    }
}
