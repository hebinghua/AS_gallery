package com.xiaomi.micloudsdk.sync;

/* loaded from: classes3.dex */
public abstract class SyncLogSender {
    public abstract void openSyncLog();

    public abstract void release();

    public abstract void sendLog(String str, String str2);
}
