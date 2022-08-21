package com.miui.gallery.cloud.syncstate;

import android.text.TextUtils;

/* loaded from: classes.dex */
public enum SyncStatus {
    SYNC_PENDING,
    SYNCING,
    SYNCING_METADATA,
    SYNC_META_PENDING,
    SYNCED,
    SYNC_PAUSE,
    SYNC_ERROR,
    SYNCED_WITH_OVERSIZED_FILE,
    NO_ACCOUNT,
    MASTER_SYNC_OFF,
    SYNC_OFF,
    CTA_NOT_ALLOW,
    MI_MOVER_RUNNING,
    DISCONNECTED,
    NO_WIFI,
    BATTERY_LOW,
    SYSTEM_SPACE_LOW,
    CLOUD_SPACE_FULL,
    UNKNOWN_ERROR,
    EXCEPTED_ERROR,
    UNAVAILABLE;

    public static SyncStatus fromName(String str) {
        SyncStatus[] values;
        if (TextUtils.isEmpty(str)) {
            return UNKNOWN_ERROR;
        }
        for (SyncStatus syncStatus : values()) {
            if (TextUtils.equals(syncStatus.name(), str)) {
                return syncStatus;
            }
        }
        return UNKNOWN_ERROR;
    }
}
