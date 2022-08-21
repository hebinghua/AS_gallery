package cn.wps.kmo.kmoservice_sdk.utils;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class Version {
    public static int SUPPORT_MIN_VERSIONCODE = 810;
    public static int SUPPORT_MIN_VERSIONCODE_INTER_LITE = 0;
    public static int SUPPORT_MIN_VERSIONCODE_LITE = 221;

    public static int getSupportMinVersionCode(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (str.equals(TypeUtils.KMO)) {
            return SUPPORT_MIN_VERSIONCODE;
        }
        if (str.equals(TypeUtils.KMO_LITE)) {
            return SUPPORT_MIN_VERSIONCODE_LITE;
        }
        if (!str.equals(TypeUtils.KMO_INTER_LITE)) {
            return 0;
        }
        return SUPPORT_MIN_VERSIONCODE_INTER_LITE;
    }
}
