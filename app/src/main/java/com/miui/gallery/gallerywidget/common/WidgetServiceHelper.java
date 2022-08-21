package com.miui.gallery.gallerywidget.common;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;

/* loaded from: classes2.dex */
public class WidgetServiceHelper {
    public static void startForegroundServiceIfNeed(Context context, Intent intent) {
        ContextCompat.startForegroundService(context, intent);
    }
}
