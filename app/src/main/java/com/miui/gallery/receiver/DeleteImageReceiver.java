package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.miui.gallery.util.logger.DefaultLogger;

@Deprecated
/* loaded from: classes2.dex */
public class DeleteImageReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        DefaultLogger.e("DeleteImageReceiver", "someone is accessing DeleteImageReceiver");
    }
}
