package com.miui.gallery.cloud.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class GallerySyncCommandReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("com.xiaomi.micloud.action.SYNC_COMMAND".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("key_command");
            DefaultLogger.i("GallerySyncCommandReceiver", "onReceive sync command: %s", stringExtra);
            if (!"value_command_cancel_sync".equals(stringExtra)) {
                return;
            }
            SyncUtil.stopSync(context);
        }
    }
}
