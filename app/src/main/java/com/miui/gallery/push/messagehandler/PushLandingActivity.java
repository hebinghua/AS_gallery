package com.miui.gallery.push.messagehandler;

import android.content.Intent;
import android.os.Bundle;
import com.miui.gallery.app.activity.AndroidActivity;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes2.dex */
public class PushLandingActivity extends AndroidActivity {
    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent != null) {
            Intent intent2 = (Intent) intent.getParcelableExtra("notification_content_intent");
            int intExtra = intent.getIntExtra("notification_content_id", 0);
            if (intent2 != null) {
                try {
                    startActivity(intent2);
                } catch (Exception unused) {
                    DefaultLogger.e("PushLandingActivity", "failed to start activity: %s", intent2.getData());
                }
                HashMap hashMap = new HashMap();
                hashMap.put("pushContent", String.format(Locale.US, "%d_%s", Integer.valueOf(intExtra), intent2.getDataString()));
                StatHelper.recordCountEvent("push_notification", "notification_click", hashMap);
            } else {
                DefaultLogger.e("PushLandingActivity", "empty content intent");
            }
        }
        finish();
    }
}
