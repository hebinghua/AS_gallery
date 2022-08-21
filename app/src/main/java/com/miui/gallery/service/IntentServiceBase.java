package com.miui.gallery.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class IntentServiceBase extends IntentService {
    public String mTag;

    public abstract Notification getNotification();

    public abstract int getNotificationId();

    public String getTag() {
        if (this.mTag == null) {
            this.mTag = String.format("%s(%s)", getClass().getSimpleName(), Integer.toHexString(hashCode()));
        }
        return this.mTag;
    }

    public IntentServiceBase() {
        super("IntentServiceBase");
    }

    @Override // android.app.IntentService
    public void onHandleIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            DefaultLogger.fd(getTag(), "startForeground");
            startForeground(getNotificationId(), getNotification());
        }
    }

    @Override // android.app.IntentService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        DefaultLogger.fd(getTag(), "onDestroy");
    }
}
