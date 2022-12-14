package com.baidu.mapapi;

import android.content.Context;
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapsdkplatform.comapi.c;
import com.baidu.mapsdkplatform.comapi.util.PermissionCheck;

/* loaded from: classes.dex */
public class SDKInitializer {
    public static final String SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR = "network error";
    public static final String SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR = "permission check error";
    public static final String SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK = "permission check ok";
    public static final String SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE = "error_code";
    public static final String SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_MESSAGE = "error_message";
    private static CoordType a = CoordType.BD09LL;

    private SDKInitializer() {
    }

    public static CoordType getCoordType() {
        return a;
    }

    public static void initialize(Context context) {
        c.a(context, false, null, null, null);
    }

    public static void initialize(Context context, boolean z, String str, String str2) {
        c.a(context, z, str, str2, null);
    }

    public static void initialize(String str, Context context) {
        c.a(context, false, null, str, null);
    }

    public static boolean isHttpsEnable() {
        return HttpClient.isHttpsEnable;
    }

    public static void setAgreePrivacy(Context context, boolean z) {
        c.a(context, z);
    }

    public static void setApiKey(String str) {
        PermissionCheck.setApiKey(str);
    }

    public static void setCoordType(CoordType coordType) {
        a = coordType;
    }

    public static void setHttpsEnable(boolean z) {
        HttpClient.isHttpsEnable = z;
    }
}
