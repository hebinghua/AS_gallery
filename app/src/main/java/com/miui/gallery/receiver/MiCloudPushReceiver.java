package com.miui.gallery.receiver;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryMiCloudUtil;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.security.RiskController;
import com.miui.gallery.settingssync.GallerySettingsSyncHelper;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MiCloudPushReceiver extends WakefulBroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        DefaultLogger.i("MiCloudPushReceiver", "onReceive: %s", intent);
        String stringExtra = intent.getStringExtra("pushType");
        String stringExtra2 = intent.getStringExtra("pushName");
        String stringExtra3 = intent.getStringExtra("pushData");
        DefaultLogger.i("MiCloudPushReceiver", "pushType: %s, pushName: %s, pushData: %s", stringExtra, stringExtra2, stringExtra3);
        if ("notification".equals(stringExtra)) {
            if ("micloud.gallery.setting".equals(stringExtra2)) {
                DefaultLogger.i("MiCloudPushReceiver", "setting changed");
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.receiver.MiCloudPushReceiver.1
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public Object mo1807run(ThreadPool.JobContext jobContext) {
                        GallerySettingsSyncHelper.doDownload(GalleryApp.sGetAndroidContext());
                        return null;
                    }
                });
            }
            if ("micloud.quota.change".equals(stringExtra2)) {
                DefaultLogger.i("MiCloudPushReceiver", "handleSpaceFull");
                if (SpaceFullHandler.isOwnerSpaceFull()) {
                    SpaceFullHandler.removeOwnerSpaceFull();
                    DefaultLogger.i("MiCloudPushReceiver", "remove space tag from quota receiver");
                }
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.receiver.MiCloudPushReceiver.2
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public Object mo1807run(ThreadPool.JobContext jobContext) {
                        TrashUtils.requestVipInfo();
                        return null;
                    }
                });
                GalleryMiCloudUtil.clearSpaceFullTipsShowCount();
            }
            if (!"micloud.gallery.risk.control.delete".equals(stringExtra2)) {
                return;
            }
            DefaultLogger.i("MiCloudPushReceiver", "handle wind control");
            RiskController.handleDataDeleted(stringExtra3);
            return;
        }
        "watermark".equals(stringExtra);
    }
}
