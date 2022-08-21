package com.miui.gallery.card.scenario.time.holiday;

import com.miui.gallery.card.scenario.time.TimeScenario;
import com.miui.gallery.util.assistant.HolidaysUtil;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseHolidayScenario extends TimeScenario {
    public HolidaysUtil.Holiday mTargetHoliday;
    public int mYear = 1;

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        List<Integer> list = this.mTagIdList;
        if (list == null || list.size() <= 0) {
            return null;
        }
        return getMediaIdsByStartEndTimeTags(this.mTagIdList, getStartTime(), getEndTime());
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public String getPrimaryKey() {
        return String.valueOf(this.mYear);
    }
}
