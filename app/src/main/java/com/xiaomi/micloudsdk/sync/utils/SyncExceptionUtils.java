package com.xiaomi.micloudsdk.sync.utils;

import android.content.SyncResult;

/* loaded from: classes3.dex */
public class SyncExceptionUtils {
    public static String getErrorStringForLog(SyncResult syncResult) {
        StringBuilder sb = new StringBuilder();
        if (syncResult.hasHardError()) {
            sb.append("HardError: ");
            sb.append("tooManyDeletions: ");
            sb.append(syncResult.tooManyDeletions);
            sb.append(" tooManyRetries: ");
            sb.append(syncResult.tooManyRetries);
            sb.append(" databaseError: ");
            sb.append(syncResult.databaseError);
            sb.append(" numAuthExceptions: ");
            sb.append(syncResult.stats.numAuthExceptions);
            sb.append(" numConflictDetectedExceptions: ");
            sb.append(syncResult.stats.numConflictDetectedExceptions);
        } else {
            sb.append("SoftError: ");
            sb.append("syncAlreadyInProgress: ");
            sb.append(syncResult.syncAlreadyInProgress);
            sb.append("numIoException: ");
            sb.append(syncResult.stats.numIoExceptions);
        }
        return sb.toString();
    }
}
