package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class TimeChangedReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        action.hashCode();
        char c = 65535;
        switch (action.hashCode()) {
            case 502473491:
                if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                    c = 0;
                    break;
                }
                break;
            case 505380757:
                if (action.equals("android.intent.action.TIME_SET")) {
                    c = 1;
                    break;
                }
                break;
            case 1041332296:
                if (action.equals("android.intent.action.DATE_CHANGED")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                invalidateDateCache();
                invalidateCloudCache();
                return;
            default:
                return;
        }
    }

    public final void invalidateDateCache() {
        GalleryDateUtils.invalidateCache();
    }

    public final void invalidateCloudCache() {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.receiver.TimeChangedReceiver.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                GalleryUtils.safeDelete(GalleryCloudUtils.CLOUD_CACHE_URI, null, null);
                return null;
            }
        });
    }
}
