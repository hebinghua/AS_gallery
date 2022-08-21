package com.miui.gallery.editor.photo.screen.home;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.android.systemui.screenshot.IBitmapService;
import com.android.systemui.screenshot.IScreenShotCallback;
import com.miui.gallery.service.ServiceBase;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ScreenShotService extends ServiceBase {
    public RemoteCallbackList<IScreenShotCallback> mListenerList = new RemoteCallbackList<>();
    public IBitmapService.Stub mStub = new IBitmapService.Stub() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenShotService.1
        @Override // com.android.systemui.screenshot.IBitmapService
        public void registerCallback(IScreenShotCallback iScreenShotCallback) throws RemoteException {
            try {
                DefaultLogger.d("ScreenShotService", "registerCallback");
                ScreenShotService.this.mListenerList.register(iScreenShotCallback);
            } catch (Exception e) {
                DefaultLogger.e("ScreenShotService", "registerCallback: %s", e.toString());
            }
        }

        @Override // com.android.systemui.screenshot.IBitmapService
        public void unregisterCallback(IScreenShotCallback iScreenShotCallback) throws RemoteException {
            try {
                DefaultLogger.d("ScreenShotService", "unregisterCallback");
                ScreenShotService.this.mListenerList.unregister(iScreenShotCallback);
            } catch (Exception e) {
                DefaultLogger.e("ScreenShotService", "unregisterCallback: %s", e.toString());
            }
        }
    };

    @Override // com.miui.gallery.service.ServiceBase
    public int getNotificationId() {
        return 14;
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        if (intent == null || !intent.getBooleanExtra("quit_thumnail", false)) {
            return 3;
        }
        quitThumbnail();
        stopSelfSafely();
        return 3;
    }

    public final void quitThumbnail() {
        int beginBroadcast = this.mListenerList.beginBroadcast();
        DefaultLogger.d("ScreenShotService", "quitThumbnail count: " + beginBroadcast);
        for (int i = 0; i < beginBroadcast; i++) {
            IScreenShotCallback broadcastItem = this.mListenerList.getBroadcastItem(i);
            if (broadcastItem != null) {
                try {
                    broadcastItem.quitThumnail();
                } catch (RemoteException e) {
                    DefaultLogger.d("ScreenShotService", "quitThumbnail: %s", e.toString());
                }
            }
        }
        this.mListenerList.finishBroadcast();
    }

    @Override // com.miui.gallery.service.ServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mStub;
    }
}
