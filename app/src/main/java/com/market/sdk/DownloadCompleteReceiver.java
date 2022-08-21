package com.market.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.market.sdk.utils.Client;
import com.market.sdk.utils.Log;

/* loaded from: classes.dex */
public class DownloadCompleteReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (Client.isMiui() && "android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
            long longExtra = intent.getLongExtra("extra_download_id", -1L);
            Log.d("MarketSDKDownloadReceiver", "on sdk download complete : id = " + longExtra);
            if (longExtra == -1) {
                return;
            }
            DownloadInstallManager.getManager(context).handleDownloadComplete(longExtra);
        }
    }
}
