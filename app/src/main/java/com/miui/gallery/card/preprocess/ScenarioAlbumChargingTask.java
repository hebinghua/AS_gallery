package com.miui.gallery.card.preprocess;

import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ScenarioAlbumChargingTask extends ScenarioAlbumTask {
    @Override // com.miui.gallery.card.preprocess.ScenarioTask, com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return true;
    }

    public ScenarioAlbumChargingTask(int i) {
        super(i);
    }

    @Override // com.miui.gallery.card.preprocess.ScenarioAlbumTask, com.miui.gallery.card.preprocess.ScenarioTask
    public boolean onProcess(JSONObject jSONObject, long j) throws Exception {
        Record record = (Record) GalleryEntityManager.getInstance().find(Record.class, j);
        if (record != null && record.getState() != 2) {
            if (isCancelled()) {
                DefaultLogger.d("ScenarioAlbumChargingTask", "task is cancelled");
                return false;
            }
            DefaultLogger.d("ScenarioAlbumChargingTask", "start generate card");
            generateCard(jSONObject, null, record, true);
        }
        return false;
    }
}
