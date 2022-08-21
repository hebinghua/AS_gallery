package com.xiaomi.micloudsdk.sync;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.xiaomi.micloudsdk.utils.MiCloudConstants;

/* loaded from: classes3.dex */
public class SyncLogSenderFactory {
    public static final Uri URI_SYNC_LOG_PROVIDER = MiCloudConstants.SYNC_LOG.URI_PROVIDER;

    public static SyncLogSender newSyncLogSender(Context context, String str) {
        ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(URI_SYNC_LOG_PROVIDER);
        if (acquireContentProviderClient == null) {
            Log.i("SyncLogSender", "Create sync log sender: logcat.");
            return new LogcatSyncLogSender();
        }
        Log.i("SyncLogSender", "Create sync log sender: file.");
        return new FileSyncLogSender(acquireContentProviderClient, str);
    }
}
