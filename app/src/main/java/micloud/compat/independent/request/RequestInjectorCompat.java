package micloud.compat.independent.request;

import android.content.Context;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.utils.MiCloudSDKDependencyUtil;
import micloud.compat.independent.sync.CNPrivacyUtilsCompat;
import micloud.compat.independent.sync.GdprUtilsCompat;
import miui.cloud.common.XLogger;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class RequestInjectorCompat {
    public static final IRequestInjectorCompat sRequestInjectorCompat;

    static {
        if (MiCloudSDKDependencyUtil.SDKEnvironment >= 18) {
            sRequestInjectorCompat = new RequestInjectorCompact_V18();
        } else {
            sRequestInjectorCompat = new RequestInjectorCompat_Base();
        }
    }

    public static void handleCloudServerException(Context context, CloudServerException cloudServerException) {
        int i;
        int i2 = cloudServerException.code;
        if (i2 == 503 && (i = cloudServerException.retryTime) > 0) {
            sRequestInjectorCompat.sendDataInTransferBroadcast(context, i);
        } else if (i2 == 52003) {
            GdprUtilsCompat.notifyPrivacyDenied(context);
        } else if (i2 != 53003) {
        } else {
            CNPrivacyUtilsCompat.sendCNPrivacyDeniedBroadcast(context);
        }
    }

    public static void checkResponse(Context context, String str) {
        if (str == null) {
            return;
        }
        if (isPrivacyError(str)) {
            GdprUtilsCompat.notifyPrivacyDenied(context);
        }
        if (!isCNPrivacyError(str)) {
            return;
        }
        CNPrivacyUtilsCompat.sendCNPrivacyDeniedBroadcast(context);
    }

    public static boolean isPrivacyError(String str) {
        try {
        } catch (JSONException e) {
            XLogger.loge(e);
        }
        return new JSONObject(str).optInt("code", 0) == 52003;
    }

    public static boolean isCNPrivacyError(String str) {
        try {
        } catch (JSONException e) {
            XLogger.loge(e);
        }
        return new JSONObject(str).optInt("code", 0) == 53003;
    }
}
