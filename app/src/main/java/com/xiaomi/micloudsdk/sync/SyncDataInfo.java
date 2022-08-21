package com.xiaomi.micloudsdk.sync;

import android.content.Context;
import miui.cloud.sync.SyncInfoHelper;
import miui.cloud.sync.SyncInfoUnavailableException;

/* loaded from: classes3.dex */
public class SyncDataInfo {
    public String mAuthority;
    public int mSyncedCount;
    public int mUnSyncedDataCount;
    public int mUnSyncedSecretDataCount;
    public int mWifiOnlyUnsyncedDataCount;

    public static SyncDataInfo generateCurrentSyncDataInfo(Context context, String str) {
        return new SyncDataInfo(context, str);
    }

    public SyncDataInfo(Context context, String str) {
        this.mAuthority = str;
        try {
            this.mSyncedCount = SyncInfoHelper.getSyncedDataCount(context, str);
        } catch (SyncInfoUnavailableException unused) {
            this.mSyncedCount = -2;
        }
        try {
            this.mUnSyncedDataCount = SyncInfoHelper.getUnsyncedDataCount(context, str);
        } catch (SyncInfoUnavailableException unused2) {
            this.mUnSyncedDataCount = -2;
        }
        try {
            this.mUnSyncedSecretDataCount = SyncInfoHelper.getUnSyncedSecretDataCount(context, str);
        } catch (SyncInfoUnavailableException unused3) {
            this.mUnSyncedSecretDataCount = -2;
        }
        try {
            this.mWifiOnlyUnsyncedDataCount = SyncInfoHelper.getWifiOnlyUnsyncedDataCount(context, str);
        } catch (SyncInfoUnavailableException unused4) {
            this.mWifiOnlyUnsyncedDataCount = -2;
        }
    }

    public int getSyncedCount() {
        return this.mSyncedCount;
    }

    public int getUnSyncedDataCount() {
        return this.mUnSyncedDataCount;
    }

    public int getUnSyncedSecretDataCount() {
        return this.mUnSyncedSecretDataCount;
    }

    public int getWifiOnlyUnsyncedDataCount() {
        return this.mWifiOnlyUnsyncedDataCount;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SyncDataInfo[" + this.mAuthority + "]  {");
        appendSyncedDataCount(sb).append(", ");
        appendUnSyncedDataCount(sb).append(", ");
        appendUnSyncedSecretDataCount(sb).append(", ");
        appendWifiOnlyUnsyncedDataCount(sb).append("}");
        return sb.toString();
    }

    public final StringBuilder appendSyncedDataCount(StringBuilder sb) {
        sb.append("synced count:");
        int syncedCount = getSyncedCount();
        if (syncedCount != -2) {
            sb.append(syncedCount);
        } else {
            sb.append("null");
        }
        return sb;
    }

    public final StringBuilder appendUnSyncedDataCount(StringBuilder sb) {
        sb.append("unsynced count:");
        int unSyncedDataCount = getUnSyncedDataCount();
        if (unSyncedDataCount != -2) {
            sb.append(unSyncedDataCount);
        } else {
            sb.append("null");
        }
        return sb;
    }

    public final StringBuilder appendWifiOnlyUnsyncedDataCount(StringBuilder sb) {
        sb.append("unsynced wifi only count:");
        int wifiOnlyUnsyncedDataCount = getWifiOnlyUnsyncedDataCount();
        if (wifiOnlyUnsyncedDataCount != -2) {
            sb.append(wifiOnlyUnsyncedDataCount);
        } else {
            sb.append("null");
        }
        return sb;
    }

    public final StringBuilder appendUnSyncedSecretDataCount(StringBuilder sb) {
        sb.append("unsynced secret count:");
        int unSyncedSecretDataCount = getUnSyncedSecretDataCount();
        if (unSyncedSecretDataCount != -2) {
            sb.append(unSyncedSecretDataCount);
        } else {
            sb.append("null");
        }
        return sb;
    }
}
