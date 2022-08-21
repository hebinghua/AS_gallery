package com.miui.gallery.assistant.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryConstantsHelper;
import com.miui.gallery.assistant.library.LibraryDownloadTask;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.assistant.model.FaceInfo;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.cloud.control.BatteryMonitor;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ExistAnalyticFaceAndSceneTask extends ExistImageFeatureTask {
    public int mImageCount;
    public PowerChangedReceiver mPowerChangedReceiver;
    public Set<Long> mSubmitIds;
    public int mVideoCount;

    public int getProcessBatchCount() {
        return 2;
    }

    public ExistAnalyticFaceAndSceneTask(int i) {
        super(i);
        this.mSubmitIds = new HashSet();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.assistant.process.ExistImageFeatureTask, com.miui.gallery.pendingtask.base.PendingTask
    public boolean process(JSONObject jSONObject) throws Exception {
        initPowerState(GalleryApp.sGetAndroidContext());
        try {
            if (!GalleryPreferences.Sync.getPowerCanSync() && !GalleryPreferences.Sync.getIsPlugged()) {
                DefaultLogger.e(this.TAG, "The power is weak and not charging,abort processing!");
                statisticTriggerEvent(11, "failed", 0, 0L);
                recordTriggerEvent("failed");
                PendingTaskManager.getInstance().postTask(12, null, ExistAnalyticFaceAndSceneChargingTask.class.getSimpleName());
            } else {
                DefaultLogger.d(this.TAG, "Start process exist media");
                this.mStartTime = System.currentTimeMillis();
                processInternal(jSONObject, 12, getProcessBatchCount());
                processHeatmapImage();
            }
            return false;
        } finally {
            unregisterPowerReceiver(GalleryApp.sGetAndroidContext());
        }
    }

    public void processHeatmapImage() {
        String str;
        int i;
        int processBatchCount;
        ArrayList arrayList;
        int i2 = this.mImageCount + this.mVideoCount;
        String str2 = Card.BASE_UI_CARD_SELECTION;
        List<String> assistantHeatmapCalculated = GalleryPreferences.Assistant.getAssistantHeatmapCalculated();
        if (BaseMiscUtil.isValid(assistantHeatmapCalculated)) {
            str = str2 + " AND " + j.c + " NOT IN (" + TextUtils.join(",", arrayList) + ")";
            assistantHeatmapCalculated = new ArrayList(assistantHeatmapCalculated);
        } else {
            str = str2;
        }
        List query = GalleryEntityManager.getInstance().query(Card.class, str, null, "createTime desc", "1");
        if (BaseMiscUtil.isValid(query)) {
            List<String> selectedMediaSha1s = ((Card) query.get(0)).getSelectedMediaSha1s();
            if (assistantHeatmapCalculated == null) {
                assistantHeatmapCalculated = new ArrayList<>();
            }
            assistantHeatmapCalculated.add(String.valueOf(((Card) query.get(0)).getRowId()));
            List<MediaFeatureItem> allProcessedSuccessHeatMapMedias = getAllProcessedSuccessHeatMapMedias(BaseImageTask.queryMediaItem(ScenarioConstants.IMAGE_FEATURE_CALCULATION_SELECTION + " AND sha1 in ('" + TextUtils.join("','", selectedMediaSha1s) + "')"));
            if (BaseMiscUtil.isValid(allProcessedSuccessHeatMapMedias)) {
                if (LibraryManager.getInstance().loadLibrary(LibraryConstantsHelper.sAnalyticFaceAndSceneSelectionLibraries)) {
                    DefaultLogger.d(this.TAG, "processing %d media", Integer.valueOf(allProcessedSuccessHeatMapMedias.size()));
                    for (MediaFeatureItem mediaFeatureItem : allProcessedSuccessHeatMapMedias) {
                        AnalyticFaceAndSceneManager.getInstance().analyticSceneTagSync(mediaFeatureItem, false);
                    }
                }
                i = allProcessedSuccessHeatMapMedias.size();
                GalleryPreferences.Assistant.setAssistantHeatmapCalculated(assistantHeatmapCalculated);
                processBatchCount = ((getProcessBatchCount() * 100) - i2) - i;
                if (processBatchCount > 0 || !BaseMiscUtil.isValid(getToProcessHeatmapItems(processBatchCount))) {
                }
                List<MediaFeatureItem> queryMediaItem = BaseImageTask.queryMediaItem(ScenarioConstants.IMAGE_FEATURE_CALCULATION_SELECTION + " AND " + j.c + " in (" + TextUtils.join(",", getToProcessHeatmapItems(processBatchCount)) + ")");
                if (!BaseMiscUtil.isValid(queryMediaItem) || !LibraryManager.getInstance().loadLibrary(LibraryConstantsHelper.sAnalyticFaceAndSceneSelectionLibraries)) {
                    return;
                }
                DefaultLogger.d(this.TAG, "processing %d media", Integer.valueOf(queryMediaItem.size()));
                for (MediaFeatureItem mediaFeatureItem2 : queryMediaItem) {
                    AnalyticFaceAndSceneManager.getInstance().analyticSceneTagSync(mediaFeatureItem2, false);
                }
                return;
            }
        }
        i = 0;
        GalleryPreferences.Assistant.setAssistantHeatmapCalculated(assistantHeatmapCalculated);
        processBatchCount = ((getProcessBatchCount() * 100) - i2) - i;
        if (processBatchCount > 0) {
        }
    }

    public Set<Long> getToProcessHeatmapItems(int i) {
        HashSet hashSet = new HashSet();
        Cursor rawQuery = GalleryEntityManager.getInstance().rawQuery(MediaScene.class, new String[]{"mediaId"}, "version = 1 AND leftTopX < 0", null, null, null, "mediaId DESC", null);
        if (rawQuery != null) {
            do {
                try {
                    if (!rawQuery.moveToNext()) {
                        break;
                    }
                    hashSet.add(Long.valueOf(Entity.getLong(rawQuery, "mediaId")));
                } finally {
                    rawQuery.close();
                }
            } while (hashSet.size() < i);
        }
        return hashSet;
    }

    public List<MediaFeatureItem> getAllProcessedSuccessHeatMapMedias(List<MediaFeatureItem> list) {
        if (BaseMiscUtil.isValid(list)) {
            HashSet hashSet = new HashSet();
            for (MediaFeatureItem mediaFeatureItem : list) {
                hashSet.add(Long.valueOf(mediaFeatureItem.getId()));
            }
            if (!BaseMiscUtil.isValid(hashSet)) {
                return null;
            }
            Cursor rawQuery = GalleryEntityManager.getInstance().rawQuery(MediaScene.class, new String[]{"mediaId"}, "version = 1 AND leftTopX >= 0 AND mediaId IN (" + TextUtils.join(",", hashSet) + ")", null, null, null, "mediaId DESC", null);
            if (rawQuery != null) {
                while (rawQuery.moveToNext()) {
                    try {
                        hashSet.remove(Long.valueOf(Entity.getLong(rawQuery, "mediaId")));
                    } finally {
                        rawQuery.close();
                    }
                }
            }
            ArrayList arrayList = new ArrayList();
            for (MediaFeatureItem mediaFeatureItem2 : list) {
                if (hashSet.contains(Long.valueOf(mediaFeatureItem2.getId()))) {
                    arrayList.add(mediaFeatureItem2);
                }
            }
            return arrayList;
        }
        return null;
    }

    @Override // com.miui.gallery.assistant.process.BaseImageTask
    public boolean processItems(JSONObject jSONObject, List<MediaFeatureItem> list, boolean z, boolean z2) {
        if (BaseMiscUtil.isValid(list)) {
            LibraryManager libraryManager = LibraryManager.getInstance();
            Long[] lArr = LibraryConstantsHelper.sAnalyticFaceAndSceneSelectionLibraries;
            if (libraryManager.loadLibrary(lArr)) {
                DefaultLogger.d(this.TAG, "processing %d media", Integer.valueOf(list.size()));
                if (AnalyticFaceAndSceneManager.getInstance().analyticFaceAndSceneSync(list).getResultCode() != 0) {
                    return false;
                }
                for (MediaFeatureItem mediaFeatureItem : list) {
                    this.mSubmitIds.add(Long.valueOf(mediaFeatureItem.getId()));
                    if (mediaFeatureItem.isImage()) {
                        this.mImageCount++;
                    } else {
                        this.mVideoCount++;
                    }
                }
                return true;
            }
            DefaultLogger.d(this.TAG, "loadLibrary fail, schedule download task!");
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("originalType", getType());
                jSONObject2.put("originalData", jSONObject);
                jSONObject2.put("libraryIds", GsonUtils.toString(lArr));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingTaskManager.getInstance().postTask(7, jSONObject2, LibraryDownloadTask.class.getSimpleName());
            return false;
        }
        return true;
    }

    @Override // com.miui.gallery.assistant.process.ExistImageFeatureTask
    public void initPowerState(Context context) {
        unregisterPowerReceiver(context);
        PowerChangedReceiver powerChangedReceiver = new PowerChangedReceiver();
        this.mPowerChangedReceiver = powerChangedReceiver;
        Intent registerReceiver = context.registerReceiver(powerChangedReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            GalleryPreferences.Sync.setPowerCanSync(BatteryMonitor.isPowerCanSync(context, registerReceiver));
        }
    }

    public void unregisterPowerReceiver(Context context) {
        PowerChangedReceiver powerChangedReceiver = this.mPowerChangedReceiver;
        if (powerChangedReceiver != null) {
            context.unregisterReceiver(powerChangedReceiver);
            this.mPowerChangedReceiver = null;
        }
    }

    @Override // com.miui.gallery.assistant.process.ExistImageFeatureTask
    public List<MediaFeatureItem> getToProcessItems(boolean z) {
        List<MediaFeatureItem> queryMediaItem = BaseImageTask.queryMediaItem(ScenarioConstants.ALL_MEDIA_CALCULATION_SELECTION);
        DefaultLogger.d(this.TAG, "FaceAndScene:allImages.size()=%d", Integer.valueOf(queryMediaItem.size()));
        Set<Long> allProcessedSuccessMedias = getAllProcessedSuccessMedias();
        DefaultLogger.d(this.TAG, "FaceAndScene:processSuccessIds.size()=%d", Integer.valueOf(allProcessedSuccessMedias.size()));
        ArrayList arrayList = new ArrayList(100);
        if (!BaseMiscUtil.isValid(queryMediaItem)) {
            return arrayList;
        }
        for (MediaFeatureItem mediaFeatureItem : queryMediaItem) {
            if (!this.mSubmitIds.contains(Long.valueOf(mediaFeatureItem.getId())) && !allProcessedSuccessMedias.contains(Long.valueOf(mediaFeatureItem.getId())) && (!TextUtils.isEmpty(mediaFeatureItem.getOriginPath()) || (mediaFeatureItem.isImage() && !TextUtils.isEmpty(mediaFeatureItem.getImagePath())))) {
                arrayList.add(mediaFeatureItem);
                if (arrayList.size() == 100) {
                    break;
                }
            }
        }
        DefaultLogger.d(this.TAG, "Processing %d media!", Integer.valueOf(arrayList.size()));
        return arrayList;
    }

    public static Set<Long> getAllProcessedSuccessMedias() {
        HashSet hashSet = new HashSet();
        Cursor rawQuery = GalleryEntityManager.getInstance().rawQuery(FaceInfo.class, new String[]{"mediaId"}, "version = 1", null, null, null, "mediaId DESC", null);
        if (rawQuery != null) {
            while (rawQuery.moveToNext()) {
                try {
                    hashSet.add(Long.valueOf(Entity.getLong(rawQuery, "mediaId")));
                } finally {
                    rawQuery.close();
                }
            }
        }
        return hashSet;
    }

    @Override // com.miui.gallery.assistant.process.ExistImageFeatureTask
    public void recordTriggerEvent(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("trigger_time", DateUtils.getDateFormat(System.currentTimeMillis()));
        if ("success".equals(str)) {
            hashMap.put("cost_time", String.valueOf((System.currentTimeMillis() - this.mStartTime) / 1000));
            hashMap.put("video_count", String.valueOf(this.mVideoCount));
            hashMap.put("photo_count", String.valueOf(this.mImageCount));
            PowerChangedReceiver powerChangedReceiver = this.mPowerChangedReceiver;
            if (powerChangedReceiver != null && !powerChangedReceiver.isEverCharged()) {
                hashMap.put("power_reduce", String.valueOf(this.mPowerChangedReceiver.getPowerReduce()));
            }
        }
        SamplingStatHelper.recordCountEvent("assistant", "assistant_analytic_face_and_scene_task_" + str, hashMap);
        DefaultLogger.d(this.TAG, "on filtered recordCountEvent:params:%s", hashMap);
    }

    /* loaded from: classes.dex */
    public class PowerChangedReceiver extends BroadcastReceiver {
        public int mPowerReduce;
        public boolean mIsEverCharged = false;
        public int mStartPower = -1;

        public PowerChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean z = this.mIsEverCharged || intent.getIntExtra("status", -1) == 2;
            this.mIsEverCharged = z;
            DefaultLogger.d(ExistAnalyticFaceAndSceneTask.this.TAG, "is ever charged %s", Boolean.valueOf(z));
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int intExtra = (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
                int i = this.mStartPower;
                if (i < 0) {
                    this.mStartPower = intExtra;
                    return;
                }
                int i2 = i - intExtra;
                this.mPowerReduce = i2;
                DefaultLogger.d(ExistAnalyticFaceAndSceneTask.this.TAG, "power reduce %d", Integer.valueOf(i2));
            }
        }

        public boolean isEverCharged() {
            return this.mIsEverCharged;
        }

        public int getPowerReduce() {
            return this.mPowerReduce;
        }
    }
}
