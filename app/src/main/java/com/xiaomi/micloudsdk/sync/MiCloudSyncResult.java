package com.xiaomi.micloudsdk.sync;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes3.dex */
public class MiCloudSyncResult {
    public final Throwable error;
    public final int errorCodeLegacy;
    public final String errorReasonForStats;
    public final boolean isSuccess;

    public MiCloudSyncResult(boolean z, Throwable th, int i, String str) {
        this.isSuccess = z;
        this.error = th;
        this.errorCodeLegacy = i;
        this.errorReasonForStats = str;
    }

    public static MiCloudSyncResult createSuccessResult() {
        return new MiCloudSyncResult(true, null, 0, "no_error");
    }

    public static MiCloudSyncResult createFailResult(Throwable th, int i, String str) {
        if (th == null) {
            throw new IllegalArgumentException("error must be nonNull");
        }
        return new MiCloudSyncResult(false, th, i, str);
    }

    public String toString() {
        return "MiCloudSyncResult{isSuccess=" + this.isSuccess + ", error=" + this.error + ", errorCodeLegacy=" + this.errorCodeLegacy + ", errorReasonForStats='" + this.errorReasonForStats + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
