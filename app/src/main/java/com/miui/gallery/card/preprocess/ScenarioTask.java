package com.miui.gallery.card.preprocess;

import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class ScenarioTask extends BaseImageTask {
    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public int getNetworkType() {
        return 2;
    }

    public abstract boolean onProcess(JSONObject jSONObject, long j) throws Exception;

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return false;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireDeviceIdle() {
        return true;
    }

    public ScenarioTask(int i) {
        super(i);
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public final boolean process(JSONObject jSONObject) throws Exception {
        long parseRecordId = parseRecordId(jSONObject);
        if (parseRecordId == 0) {
            DefaultLogger.e("ScenarioTask", "data parse failed");
            return false;
        }
        return onProcess(jSONObject, parseRecordId);
    }

    public static JSONObject wrapTaskData(long j) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("recordId", j);
        } catch (JSONException e) {
            DefaultLogger.e("ScenarioTask", "wrap task data error.\n", e);
        }
        return jSONObject;
    }

    public static long parseRecordId(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return 0L;
        }
        return jSONObject.getLong("recordId");
    }

    public static void post(int i, String str, long j) {
        PendingTaskManager.getInstance().postTask(i, wrapTaskData(j), str);
    }
}
