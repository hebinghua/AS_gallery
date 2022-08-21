package com.miui.gallery.sdk;

/* loaded from: classes2.dex */
public enum SyncStatus {
    STATUS_NONE,
    STATUS_INIT,
    STATUS_INTERRUPT,
    STATUS_SUCCESS,
    STATUS_ABADON;

    public static SyncStatus toSyncStatus(int i) {
        if (i != -1) {
            if (i == 0) {
                return STATUS_INIT;
            }
            if (i == 1) {
                return STATUS_INTERRUPT;
            }
            if (i == 2) {
                return STATUS_SUCCESS;
            }
            if (i == 3) {
                return STATUS_ABADON;
            }
            throw new IllegalArgumentException("unknown status: " + i);
        }
        return STATUS_NONE;
    }
}
