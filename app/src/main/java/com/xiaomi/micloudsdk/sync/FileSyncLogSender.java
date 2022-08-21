package com.xiaomi.micloudsdk.sync;

import android.content.ContentProviderClient;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import miui.cloud.sync.IRemoteFileSyncLogger;

/* loaded from: classes3.dex */
public class FileSyncLogSender extends SyncLogSender {
    public final String mAuthority;
    public IRemoteFileSyncLogger mRemoteLogger;
    public final ContentProviderClient mSyncProvider;

    public FileSyncLogSender(ContentProviderClient contentProviderClient, String str) {
        if (contentProviderClient == null) {
            throw new IllegalArgumentException("Must give a valid provider client.");
        }
        this.mSyncProvider = contentProviderClient;
        this.mAuthority = str;
    }

    @Override // com.xiaomi.micloudsdk.sync.SyncLogSender
    public void openSyncLog() {
        Bundle bundle = new Bundle();
        bundle.putString("sync_log_key_authority", this.mAuthority);
        IBinder iBinder = null;
        try {
            iBinder = this.mSyncProvider.call("OPEN_SYNC_LOG", null, bundle).getBinder("sync_log_key_remote_logger");
        } catch (RemoteException unused) {
            Log.e("SyncLogSender", "open sync log: server is died.");
        }
        if (iBinder == null) {
            Log.e("SyncLogSender", "open sync log: binder is null.");
            this.mSyncProvider.release();
            return;
        }
        this.mRemoteLogger = IRemoteFileSyncLogger.Stub.asInterface(iBinder);
    }

    @Override // com.xiaomi.micloudsdk.sync.SyncLogSender
    public void sendLog(String str, String str2) {
        IRemoteFileSyncLogger iRemoteFileSyncLogger = this.mRemoteLogger;
        if (iRemoteFileSyncLogger == null) {
            Log.i(str, str2);
            return;
        }
        try {
            iRemoteFileSyncLogger.syncLog(str, str2);
        } catch (RemoteException unused) {
            Log.e("SyncLogSender", "send log: server is died.");
            Log.i(str, str2);
        }
    }

    @Override // com.xiaomi.micloudsdk.sync.SyncLogSender
    public void release() {
        if (this.mRemoteLogger == null) {
            return;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            this.mRemoteLogger.release(new ResultReceiver(null) { // from class: com.xiaomi.micloudsdk.sync.FileSyncLogSender.1
                @Override // android.os.ResultReceiver
                public void onReceiveResult(int i, Bundle bundle) {
                    countDownLatch.countDown();
                }
            });
            if (!countDownLatch.await(AbstractComponentTracker.LINGERING_TIMEOUT, TimeUnit.MILLISECONDS)) {
                Log.e("SyncLogSender", "Too long to wait for releasing provider. Release now.");
            }
        } catch (RemoteException unused) {
            Log.e("SyncLogSender", "Remote sync provider is died! Release now.");
        } catch (InterruptedException unused2) {
            Log.e("SyncLogSender", "Release is interrupted by cancel sync.");
            Thread.currentThread().interrupt();
        }
        this.mSyncProvider.release();
        this.mRemoteLogger = null;
    }
}
