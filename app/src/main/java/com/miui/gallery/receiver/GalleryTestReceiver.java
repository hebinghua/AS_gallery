package com.miui.gallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.process.ExistAnalyticFaceAndSceneChargingTask;
import com.miui.gallery.assistant.process.ExistImageFeatureTask;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.settingssync.GallerySettingsSyncHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class GalleryTestReceiver extends BroadcastReceiver {
    public static AtomicBoolean isStoryCalculationRunning = new AtomicBoolean(false);

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, final Intent intent) {
        if ("com.miui.gallery.action.REQUEST_CLOUD_CONTROL_DATA".equals(intent.getAction())) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.receiver.GalleryTestReceiver.1
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Object mo1807run(ThreadPool.JobContext jobContext) {
                    if (intent.hasExtra(nexExportFormat.TAG_FORMAT_TYPE)) {
                        String stringExtra = intent.getStringExtra(nexExportFormat.TAG_FORMAT_TYPE);
                        if ("real_name".equals(stringExtra)) {
                            new CloudControlRequestHelper(GalleryApp.sGetAndroidContext()).execRequestSync(true);
                            return null;
                        } else if ("anonymous".equals(stringExtra)) {
                            new CloudControlRequestHelper(GalleryApp.sGetAndroidContext()).execRequestSync(false);
                            return null;
                        }
                    }
                    new CloudControlRequestHelper(GalleryApp.sGetAndroidContext()).execRequestSync();
                    return null;
                }
            });
        } else if ("com.miui.gallery.action.REQUEST_SYNC_SETTINGS".equals(intent.getAction())) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.receiver.GalleryTestReceiver.2
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run  reason: collision with other method in class */
                public Void mo1807run(ThreadPool.JobContext jobContext) {
                    GallerySettingsSyncHelper.doUpload(GalleryApp.sGetAndroidContext());
                    return null;
                }
            });
        } else if ("com.miui.gallery.action.REQUEST_STORY_MEDIA_CALCULATION".equals(intent.getAction())) {
            new Thread(new Runnable() { // from class: com.miui.gallery.receiver.GalleryTestReceiver.3
                @Override // java.lang.Runnable
                public void run() {
                    if (GalleryTestReceiver.isStoryCalculationRunning.compareAndSet(false, true)) {
                        if (MediaFeatureManager.isStoryGenerateEnable()) {
                            ExistImageFeatureTask existImageFeatureTask = new ExistImageFeatureTask(6);
                            ExistAnalyticFaceAndSceneChargingTask existAnalyticFaceAndSceneChargingTask = new ExistAnalyticFaceAndSceneChargingTask(11);
                            PowerManager.WakeLock newWakeLock = ((PowerManager) GalleryApp.sGetAndroidContext().getSystemService("power")).newWakeLock(1, "com.miui.gallery.action.REQUEST_STORY_MEDIA_CALCULATION");
                            try {
                                try {
                                    newWakeLock.acquire();
                                    existImageFeatureTask.processInternal(null, 6, 10);
                                    DefaultLogger.d("GalleryTestReceiver", "ExistImageFeatureTask calculate finish");
                                    existAnalyticFaceAndSceneChargingTask.processInternal(null, 11, 10);
                                    DefaultLogger.d("GalleryTestReceiver", "ExistAnalyticFaceAndSceneTask calculate finish");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } finally {
                                newWakeLock.release();
                            }
                        }
                        GalleryTestReceiver.isStoryCalculationRunning.set(false);
                        return;
                    }
                    DefaultLogger.d("GalleryTestReceiver", "story calculation already running.");
                }
            }).start();
        } else if (!"com.miui.gallery.action.REQUEST_CARD_GENERATED".equals(intent.getAction())) {
        } else {
            if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.receiver.GalleryTestReceiver.4
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run  reason: collision with other method in class */
                    public Void mo1807run(ThreadPool.JobContext jobContext) {
                        CardManager.getInstance().triggerScenarios();
                        CardManager.getInstance().updateCardCovers();
                        return null;
                    }
                });
            }
            DefaultLogger.i("GalleryTestReceiver", "received broadcast and start triggerScenarios");
        }
    }
}
