package com.miui.gallery.assistant.process;

import org.json.JSONObject;

/* loaded from: classes.dex */
public class ExistAnalyticFaceAndSceneChargingTask extends ExistAnalyticFaceAndSceneTask {
    @Override // com.miui.gallery.assistant.process.ExistAnalyticFaceAndSceneTask
    public int getProcessBatchCount() {
        return 5;
    }

    @Override // com.miui.gallery.assistant.process.ExistImageFeatureTask, com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return true;
    }

    public ExistAnalyticFaceAndSceneChargingTask(int i) {
        super(i);
    }

    @Override // com.miui.gallery.assistant.process.ExistAnalyticFaceAndSceneTask, com.miui.gallery.assistant.process.ExistImageFeatureTask, com.miui.gallery.pendingtask.base.PendingTask
    public boolean process(JSONObject jSONObject) throws Exception {
        processInternal(jSONObject, 12, getProcessBatchCount());
        processHeatmapImage();
        return false;
    }
}
