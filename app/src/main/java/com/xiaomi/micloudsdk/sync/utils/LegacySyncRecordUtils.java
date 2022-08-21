package com.xiaomi.micloudsdk.sync.utils;

import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.util.Log;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.exception.SyncLocalException;

/* loaded from: classes3.dex */
public class LegacySyncRecordUtils {
    public static final Uri SYNC_RESULT_URI = Uri.parse("content://com.miui.micloud/sync_result");

    public static int getAuthTokenErrorLegacyCode() {
        return 100;
    }

    public static int getPauseLimitErrorLegacyCode() {
        return -1;
    }

    public static int getSyncSwitchOffErrorLegacyCode() {
        return -1;
    }

    public static void recordSyncResultSuccess(Context context, String str) {
        Log.d("SyncUtils", "recordSyncResultSuccess");
        recordSyncResult(context, str, 0);
    }

    public static void recordSyncResult(Context context, String str, int i) {
        SyncContentUtils.recordSyncResult(context, str, i);
    }

    public static int getErrorCodeByServerException(CloudServerException cloudServerException) {
        int i = cloudServerException.statusCode;
        return i != -10001 ? i : cloudServerException.code;
    }

    public static int getErrorCodeByLocalException(SyncLocalException syncLocalException) {
        switch (syncLocalException.getErrorCode()) {
            case 1000:
                return 1000;
            case 1001:
                return 1001;
            case 1002:
                return 1002;
            case 1003:
                return 1003;
            default:
                return -1;
        }
    }

    public static int getErrorCodeBySyncResult(SyncResult syncResult) {
        return syncResult.hasHardError() ? 2 : 1;
    }
}
