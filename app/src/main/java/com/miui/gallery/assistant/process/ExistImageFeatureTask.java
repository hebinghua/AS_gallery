package com.miui.gallery.assistant.process;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.cloud.control.BatteryMonitor;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ExistImageFeatureTask extends BaseImageTask {
    public long mStartTime;

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public int getNetworkType() {
        return 0;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return false;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireDeviceIdle() {
        return true;
    }

    public ExistImageFeatureTask(int i) {
        super(i);
    }

    @Override // com.miui.gallery.assistant.process.BaseImageTask
    public DownloadType onGetImageDownloadType() {
        return DownloadType.MICRO;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean process(JSONObject jSONObject) throws Exception {
        initPowerState(GalleryApp.sGetAndroidContext());
        if (!GalleryPreferences.Sync.getPowerCanSync() && !GalleryPreferences.Sync.getIsPlugged()) {
            DefaultLogger.e(this.TAG, "The power is weak and not charging,abort processing!");
            statisticTriggerEvent(6, "failed", 0, 0L);
            recordTriggerEvent("failed");
            PendingTaskManager.getInstance().postTask(9, null, ExistImageFeatureChargingTask.class.getSimpleName());
            return false;
        }
        DefaultLogger.d(this.TAG, "Start process exist images");
        this.mStartTime = System.currentTimeMillis();
        processInternal(jSONObject, 9, 2);
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0012 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void processInternal(org.json.JSONObject r11, int r12, int r13) {
        /*
            r10 = this;
            java.lang.String r0 = "success"
            r1 = 1
            if (r13 > 0) goto L6
            r13 = r1
        L6:
            r2 = 0
            java.lang.String r3 = r10.TAG     // Catch: java.lang.Throwable -> La4
            java.lang.String r4 = "Start process exist images"
            com.miui.gallery.util.logger.DefaultLogger.d(r3, r4)     // Catch: java.lang.Throwable -> La4
            r3 = r2
            r7 = r3
        L10:
            if (r3 >= r13) goto L78
            java.util.List r4 = r10.getToProcessItems(r1)     // Catch: java.lang.Throwable -> L75
            boolean r5 = com.miui.gallery.util.BaseMiscUtil.isValid(r4)     // Catch: java.lang.Throwable -> L75
            if (r5 != 0) goto L3a
            java.lang.String r11 = r10.TAG     // Catch: java.lang.Throwable -> L75
            java.lang.String r13 = "taskType=%s,Have no unProcessed images"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r12)     // Catch: java.lang.Throwable -> L75
            com.miui.gallery.util.logger.DefaultLogger.d(r11, r13, r1)     // Catch: java.lang.Throwable -> L75
            long r1 = java.lang.System.currentTimeMillis()
            long r3 = r10.mStartTime
            long r8 = r1 - r3
            java.lang.String r6 = "success"
            r4 = r10
            r5 = r12
            r4.statisticTriggerEvent(r5, r6, r7, r8)
        L36:
            r10.recordTriggerEvent(r0)
            return
        L3a:
            java.lang.String r5 = r10.TAG     // Catch: java.lang.Throwable -> L75
            java.lang.String r6 = "process %d hundreds images，image batch count:%d"
            int r3 = r3 + 1
            java.lang.Integer r8 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Throwable -> L75
            int r9 = r4.size()     // Catch: java.lang.Throwable -> L75
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch: java.lang.Throwable -> L75
            com.miui.gallery.util.logger.DefaultLogger.d(r5, r6, r8, r9)     // Catch: java.lang.Throwable -> L75
            boolean r5 = r10.processItems(r11, r4, r1, r2)     // Catch: java.lang.Throwable -> L75
            if (r5 == 0) goto L5a
            int r6 = r4.size()     // Catch: java.lang.Throwable -> L75
            int r7 = r7 + r6
        L5a:
            if (r5 == 0) goto L64
            int r4 = r4.size()     // Catch: java.lang.Throwable -> L75
            r5 = 100
            if (r4 >= r5) goto L10
        L64:
            long r1 = java.lang.System.currentTimeMillis()
            long r3 = r10.mStartTime
            long r5 = r1 - r3
            java.lang.String r3 = "success"
            r1 = r10
            r2 = r12
            r4 = r7
            r1.statisticTriggerEvent(r2, r3, r4, r5)
            goto L36
        L75:
            r11 = move-exception
            r4 = r7
            goto La6
        L78:
            if (r12 <= 0) goto L91
            java.lang.String r11 = r10.TAG     // Catch: java.lang.Throwable -> L75
            java.lang.String r13 = "Have more un processed images,schedule next FeatureTask"
            com.miui.gallery.util.logger.DefaultLogger.d(r11, r13)     // Catch: java.lang.Throwable -> L75
            com.miui.gallery.pendingtask.PendingTaskManager r11 = com.miui.gallery.pendingtask.PendingTaskManager.getInstance()     // Catch: java.lang.Throwable -> L75
            r13 = 0
            java.lang.Class r1 = r10.getClass()     // Catch: java.lang.Throwable -> L75
            java.lang.String r1 = r1.getSimpleName()     // Catch: java.lang.Throwable -> L75
            r11.postTask(r12, r13, r1)     // Catch: java.lang.Throwable -> L75
        L91:
            long r1 = java.lang.System.currentTimeMillis()
            long r3 = r10.mStartTime
            long r8 = r1 - r3
            java.lang.String r6 = "success"
            r4 = r10
            r5 = r12
            r4.statisticTriggerEvent(r5, r6, r7, r8)
            r10.recordTriggerEvent(r0)
            return
        La4:
            r11 = move-exception
            r4 = r2
        La6:
            long r1 = java.lang.System.currentTimeMillis()
            long r5 = r10.mStartTime
            long r5 = r1 - r5
            java.lang.String r3 = "success"
            r1 = r10
            r2 = r12
            r1.statisticTriggerEvent(r2, r3, r4, r5)
            r10.recordTriggerEvent(r0)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.assistant.process.ExistImageFeatureTask.processInternal(org.json.JSONObject, int, int):void");
    }

    public void initPowerState(Context context) {
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            GalleryPreferences.Sync.setPowerCanSync(BatteryMonitor.isPowerCanSync(context, registerReceiver));
        }
    }

    public List<MediaFeatureItem> getToProcessItems(boolean z) {
        List<MediaFeatureItem> queryMediaItem = BaseImageTask.queryMediaItem(ScenarioConstants.IMAGE_FEATURE_CALCULATION_SELECTION);
        DefaultLogger.d(this.TAG, "MediaFeature:allImages.size()=%d", Integer.valueOf(queryMediaItem.size()));
        List<Long> allProcessedSuccessFeatureImages = BaseImageTask.getAllProcessedSuccessFeatureImages();
        DefaultLogger.d(this.TAG, "MediaFeature:processedSuccessImages.size()=%d", Integer.valueOf(allProcessedSuccessFeatureImages.size()));
        ArrayList arrayList = new ArrayList(100);
        if (!BaseMiscUtil.isValid(queryMediaItem)) {
            return arrayList;
        }
        for (MediaFeatureItem mediaFeatureItem : queryMediaItem) {
            if (!allProcessedSuccessFeatureImages.contains(Long.valueOf(mediaFeatureItem.getId())) && (!z || !TextUtils.isEmpty(mediaFeatureItem.getImagePath()))) {
                arrayList.add(mediaFeatureItem);
                if (arrayList.size() == 100) {
                    break;
                }
            }
        }
        DefaultLogger.d(this.TAG, "Processing %d images!", Integer.valueOf(arrayList.size()));
        if (arrayList.isEmpty()) {
            return arrayList;
        }
        List<MediaFeatureItem> queryNearByMediaItems = MediaFeatureManager.queryNearByMediaItems(((MediaFeatureItem) arrayList.get(0)).getDateTime());
        CardUtil.bindMediaFeatures(queryNearByMediaItems);
        arrayList.addAll(MediaFeatureManager.filterNearByImages(queryNearByMediaItems));
        DefaultLogger.d(this.TAG, "Processing %d images after add previous images!", Integer.valueOf(arrayList.size()));
        return arrayList;
    }

    public void recordTriggerEvent(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("trigger_time", DateUtils.getDateFormat(System.currentTimeMillis()));
        SamplingStatHelper.recordCountEvent("assistant", "assistant_schedule_exist_image_task_" + str, hashMap);
    }

    public void statisticTriggerEvent(int i, String str, int i2, long j) {
        BaseImageTask.TriggerTaskType triggerTaskType;
        HashMap hashMap = new HashMap();
        if (i == 6) {
            triggerTaskType = BaseImageTask.TriggerTaskType.IMAGE_FEATURE;
        } else if (i == 9) {
            triggerTaskType = BaseImageTask.TriggerTaskType.IMAGE_FEATURE_CHARGING;
        } else if (i == 11) {
            triggerTaskType = BaseImageTask.TriggerTaskType.FACE_AND_SCENE;
        } else if (i == 12) {
            triggerTaskType = BaseImageTask.TriggerTaskType.FACE_AND_SCENE_CHARGING;
        } else {
            triggerTaskType = BaseImageTask.TriggerTaskType.NONE;
        }
        hashMap.put("tip", "403.38.0.1.16474");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, triggerTaskType.name());
        hashMap.put("status", str);
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i2));
        hashMap.put("value", Long.valueOf(j));
        TrackController.trackStats(hashMap);
    }
}
