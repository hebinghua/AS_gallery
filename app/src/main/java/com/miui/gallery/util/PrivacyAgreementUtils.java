package com.miui.gallery.util;

import android.content.Context;
import com.miui.os.Rom;
import micloud.compat.independent.sync.GdprUtilsCompat;

/* loaded from: classes2.dex */
public class PrivacyAgreementUtils {
    public static final boolean PRIVACY_ENABLE;

    static {
        PRIVACY_ENABLE = Rom.IS_MIUI && Rom.IS_INTERNATIONAL;
    }

    public static boolean isGalleryAgreementEnable(Context context) {
        return !PRIVACY_ENABLE;
    }

    public static boolean isCloudServiceAgreementEnable(Context context) {
        if (!PRIVACY_ENABLE) {
            return true;
        }
        return CloudAgreementHelper.isCloudServiceAgreementEnable(context);
    }

    /* loaded from: classes2.dex */
    public static class CloudAgreementHelper {
        public static boolean isCloudServiceAgreementEnable(Context context) {
            try {
                return GdprUtilsCompat.isGdprPermissionGranted(context);
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
    }
}
