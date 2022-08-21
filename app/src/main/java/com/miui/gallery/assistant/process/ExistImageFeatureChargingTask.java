package com.miui.gallery.assistant.process;

import org.json.JSONObject;

/* loaded from: classes.dex */
public class ExistImageFeatureChargingTask extends ExistImageFeatureTask {
    @Override // com.miui.gallery.assistant.process.ExistImageFeatureTask, com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return true;
    }

    public ExistImageFeatureChargingTask(int i) {
        super(i);
    }

    @Override // com.miui.gallery.assistant.process.ExistImageFeatureTask, com.miui.gallery.pendingtask.base.PendingTask
    public boolean process(JSONObject jSONObject) throws Exception {
        processInternal(jSONObject, 9, 5);
        return false;
    }
}
