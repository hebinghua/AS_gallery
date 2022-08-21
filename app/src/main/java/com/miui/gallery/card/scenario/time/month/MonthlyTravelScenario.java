package com.miui.gallery.card.scenario.time.month;

import android.content.res.Resources;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import java.util.List;

/* loaded from: classes.dex */
public class MonthlyTravelScenario extends MonthlyScenario {
    @Override // com.miui.gallery.card.scenario.time.month.MonthlyScenario, com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return getTravelMediaIdsByStartEndTimeAndTag(getStartTime(), getEndTime(), this.mTagIdList);
    }

    @Override // com.miui.gallery.card.scenario.time.month.MonthlyScenario, com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        long recordStartTime = getRecordStartTime(record);
        return recordStartTime > 0 ? resources.getString(R.string.monthly_travel_title, DateUtils.getMonthLocale(recordStartTime)) : "";
    }
}
