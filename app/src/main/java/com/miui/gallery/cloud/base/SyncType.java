package com.miui.gallery.cloud.base;

import android.text.TextUtils;

/* loaded from: classes.dex */
public enum SyncType {
    UNKNOW("unknow", false),
    BACKGROUND("auto-background", false),
    NORMAL("auto-foreground", false),
    POWER_FORCE("manual-battery", true),
    GPRS_FORCE("manual-network", true);
    
    private final String mIdentifier;
    private final boolean mIsForce;

    SyncType(String str, boolean z) {
        this.mIdentifier = str;
        this.mIsForce = z;
    }

    public boolean isForce() {
        return this.mIsForce;
    }

    public String getIdentifier() {
        return this.mIdentifier;
    }

    public static SyncType fromIdentifier(String str) {
        SyncType[] values;
        if (TextUtils.isEmpty(str)) {
            return UNKNOW;
        }
        for (SyncType syncType : values()) {
            if (TextUtils.equals(syncType.getIdentifier(), str)) {
                return syncType;
            }
        }
        return UNKNOW;
    }

    public static int compare(SyncType syncType, SyncType syncType2) {
        return syncType.ordinal() - syncType2.ordinal();
    }
}
