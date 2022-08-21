package com.miui.gallery.push;

import android.content.Context;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/* loaded from: classes2.dex */
public class GalleryPushReceiver extends PushMessageReceiver {
    private static final String TAG = "GalleryPushReceiver";

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        DefaultLogger.d(TAG, "onReceivePassThroughMessage: %s", String.valueOf(miPushMessage));
        PushMessageDispatcher.dispatch(context, miPushMessage);
    }

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        DefaultLogger.d(TAG, "onNotificationMessageClicked: %s", String.valueOf(miPushMessage));
    }

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        DefaultLogger.d(TAG, "onNotificationMessageArrived: %s", String.valueOf(miPushMessage));
    }

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        DefaultLogger.d(TAG, "onCommandResult: %s", String.valueOf(miPushCommandMessage));
    }

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        DefaultLogger.d(TAG, "onReceiveRegisterResult: %s", String.valueOf(miPushCommandMessage));
        if (!"register".equals(miPushCommandMessage.getCommand()) || miPushCommandMessage.getResultCode() != 0) {
            return;
        }
        GalleryPushManager.getInstance().setUserAccountAndTopics(context);
    }
}
