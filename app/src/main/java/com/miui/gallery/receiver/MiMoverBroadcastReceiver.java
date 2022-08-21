package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import com.miui.gallery.scanner.provider.GalleryMediaScannerProviderContract;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MiMoverBroadcastReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        if (extras != null && !extras.getBoolean("isSender")) {
            if ("huanji.action.transfer.end".equals(action)) {
                sendEvent(context, "mi_mover_event_end");
            } else if (!"huanji.action.transfer.start".equals(action)) {
            } else {
                sendEvent(context, "mi_mover_event_start");
            }
        }
    }

    public final void sendEvent(Context context, String str) {
        try {
            ContentProviderClient acquireContentProviderClient = context.getContentResolver().acquireContentProviderClient(GalleryMediaScannerProviderContract.AUTHORITY_URI);
            Bundle bundle = new Bundle();
            bundle.putString("param_mi_mover_event", str);
            if (acquireContentProviderClient != null) {
                acquireContentProviderClient.call("mi_mover_event", null, bundle);
            }
            if (acquireContentProviderClient == null) {
                return;
            }
            acquireContentProviderClient.close();
        } catch (RemoteException e) {
            DefaultLogger.d("MiMoverBroadcastReceiver", e.getMessage());
        }
    }
}
