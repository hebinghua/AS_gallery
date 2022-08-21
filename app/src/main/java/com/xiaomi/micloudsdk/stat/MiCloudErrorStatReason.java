package com.xiaomi.micloudsdk.stat;

import android.content.SyncResult;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.exception.SyncLocalException;

/* loaded from: classes3.dex */
public class MiCloudErrorStatReason {
    public static String getLocalExceptionReasonForStatsByCode(int i) {
        if (i != 3000) {
            if (i == 3001) {
                return "masterkey_not_exist";
            }
            switch (i) {
                case 1000:
                    return "cta_error";
                case 1001:
                    return "activated_fail";
                case 1002:
                    return "permission_limit";
                case 1003:
                    return "secure_space_limit";
                default:
                    switch (i) {
                        case 2000:
                            return "gdpr_deny";
                        case 2001:
                            return "cloud_space_full";
                        case 2002:
                            return "wlan_only_error";
                        default:
                            return "sync_unknown_error";
                    }
            }
        }
        return "masterkey_expired";
    }

    public static String getServerExceptionReasonForStats(CloudServerException cloudServerException) {
        int i = cloudServerException.statusCode;
        if (i != -10001) {
            return i != 403 ? i != 406 ? i != 400 ? i != 401 ? cloudServerException.code == 52003 ? "gdpr_deny" : cloudServerException.is5xxServerException() ? "server_5xx_error" : "sync_unknown_error" : "sever_error_unauthorized" : "sever_error_bad_request" : "sever_error_not-acceptable" : "sever_error_forbidden";
        }
        return getLocalExceptionReasonForStatsByCode(cloudServerException.code);
    }

    public static String getLocalExceptionReasonForStats(SyncLocalException syncLocalException) {
        return getLocalExceptionReasonForStatsByCode(syncLocalException.getErrorCode());
    }

    public static String getSyncResultErrorReasonForStats(SyncResult syncResult) {
        return syncResult.hasHardError() ? "sync_hard_error" : "sync_soft_error";
    }
}
