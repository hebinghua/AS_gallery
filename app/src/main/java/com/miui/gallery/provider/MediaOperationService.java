package com.miui.gallery.provider;

import android.app.Notification;
import android.content.Intent;
import com.miui.gallery.service.IntentServiceBase;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MediaOperationService extends IntentServiceBase {
    public static String EXTRA_ARGUMENT = "argument";
    public static String EXTRA_BUNDLE = "bundle";
    public static String EXTRA_METHOD = "method";

    @Override // com.miui.gallery.service.IntentServiceBase
    public int getNotificationId() {
        return 15;
    }

    @Override // com.miui.gallery.service.IntentServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.IntentServiceBase, android.app.IntentService
    public void onHandleIntent(Intent intent) {
        DefaultLogger.d("MediaOperationService", "On start handling intent %s", intent);
        startForeground(5, NotificationHelper.getEmptyNotification(getApplicationContext()));
        try {
            try {
                DefaultLogger.d("MediaOperationService", "Done handling intent %s, result %s", intent, getBaseContext().getContentResolver().call(GalleryContract.AUTHORITY_URI, intent.getStringExtra(EXTRA_METHOD), intent.getStringExtra(EXTRA_ARGUMENT), intent.getBundleExtra(EXTRA_BUNDLE)));
            } catch (Exception unused) {
                DefaultLogger.e("MediaOperationService", "Error occurred while executing intent %s", intent);
            }
        } finally {
            stopForeground(true);
        }
    }
}
