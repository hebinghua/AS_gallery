package com.miui.gallery.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class ServiceBase extends Service {
    public long mLastNotificationTime = 0;
    public String mTag;

    public abstract Notification getNotification();

    public abstract int getNotificationId();

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String getTag() {
        if (this.mTag == null) {
            this.mTag = getClass().getSimpleName();
        }
        return this.mTag;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        DefaultLogger.fd(getTag(), "onCreate");
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        DefaultLogger.fd(getTag(), "onStartCommand");
        if (Build.VERSION.SDK_INT >= 26 && !isFastNotification()) {
            DefaultLogger.fd(getTag(), "onStartCommand and startForeground: %d", Integer.valueOf(i2));
            startForeground(getNotificationId(), getNotification());
        }
        return super.onStartCommand(intent, i, i2);
    }

    public void stopSelfSafely() {
        if (Build.VERSION.SDK_INT >= 26 && !isFastNotification()) {
            DefaultLogger.fd(getTag(), "stopSelf and startForeground");
            startForeground(getNotificationId(), getNotification());
        }
        stopSelf();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean stopService(Intent intent) {
        if (Build.VERSION.SDK_INT >= 26 && !isFastNotification()) {
            DefaultLogger.fd(getTag(), "stopService and startForeground");
            startForeground(getNotificationId(), getNotification());
        }
        return super.stopService(intent);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mLastNotificationTime = 0L;
        DefaultLogger.fd(getTag(), "onDestroy");
    }

    public final boolean isFastNotification() {
        if (Math.abs(System.currentTimeMillis() - this.mLastNotificationTime) < 50) {
            return true;
        }
        this.mLastNotificationTime = System.currentTimeMillis();
        return false;
    }
}
