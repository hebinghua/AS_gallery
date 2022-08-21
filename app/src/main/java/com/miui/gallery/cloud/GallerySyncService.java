package com.miui.gallery.cloud;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.miui.gallery.util.SyncLogger;

/* loaded from: classes.dex */
public class GallerySyncService extends Service {
    public static GallerySyncAdapter sSyncAdapter;
    public static final Object sSyncAdapterLock = new Object();

    @Override // android.app.Service
    public void onDestroy() {
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new GallerySyncAdapter(getApplicationContext(), true);
            }
            SyncLogger.d("GallerySyncService", "onCreate");
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        SyncLogger.d("GallerySyncService", "onBind");
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
