package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.miui.gallery.stat.SamplingStatHelper;

/* loaded from: classes2.dex */
public class StatReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("count_event".equals(intent.getStringExtra("stat_type")) && intent.getStringExtra("category").equals("screenshot") && intent.getStringExtra("event").equals("param_keys")) {
            SamplingStatHelper.recordCountEvent("photo", "screenshot_touch_assistant");
            return;
        }
        Log.e("StatReceiver", "stat broadcast not registered" + intent.toUri(0));
    }
}
