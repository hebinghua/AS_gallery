package micloud.compat.independent.sync;

import android.content.Context;
import com.xiaomi.micloudsdk.utils.MiCloudSDKDependencyUtil;

/* loaded from: classes3.dex */
public class CNPrivacyUtilsCompat {
    public static final ICNPrivacyUtilsCompat sCNPrivacyUtilsCompatImpl;

    static {
        if (MiCloudSDKDependencyUtil.SDKEnvironment >= 36) {
            sCNPrivacyUtilsCompatImpl = new CNPrivacyUtilsCompat_V36();
        } else {
            sCNPrivacyUtilsCompatImpl = new CNPrivacyUtilsCompat_Base();
        }
    }

    public static void sendCNPrivacyDeniedBroadcast(Context context) {
        sCNPrivacyUtilsCompatImpl.sendCNPrivacyDeniedBroadcast(context);
    }
}
