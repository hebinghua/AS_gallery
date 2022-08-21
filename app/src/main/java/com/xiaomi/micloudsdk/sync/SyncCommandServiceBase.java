package com.xiaomi.micloudsdk.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

@Deprecated
/* loaded from: classes3.dex */
public abstract class SyncCommandServiceBase extends IntentService {
    public abstract void handleCommand(String str);

    public SyncCommandServiceBase() {
        super("SyncCommandServiceBase");
    }

    @Override // android.app.IntentService
    public void onHandleIntent(Intent intent) {
        if (intent == null || !"com.xiaomi.micloud.action.SYNC_COMMAND".equals(intent.getAction())) {
            Log.e("SyncCommandServiceBase", "illegal arguments");
        } else {
            handleCommand(intent.getStringExtra("key_command"));
        }
    }
}
