package com.miui.gallery.card.scenario.time.quarterly;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import java.util.List;

/* loaded from: classes.dex */
public class PastYearQuarterlyImportantPeopleScenario extends PastYearQuarterlyScenario {
    public List<Long> mImportantPeopleIds;

    @Override // com.miui.gallery.card.scenario.time.quarterly.PastYearQuarterlyScenario, com.miui.gallery.card.scenario.time.quarterly.QuarterlyScenario, com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        if (super.onPrepare(list, list2)) {
            List<Long> topNumPeopleIdsByTime = getTopNumPeopleIdsByTime(DateUtils.getDateTime(getStartTime() - 15552000000L), DateUtils.getDateTime(getStartTime()), 3);
            this.mImportantPeopleIds = topNumPeopleIdsByTime;
            return topNumPeopleIdsByTime != null && topNumPeopleIdsByTime.size() >= 2;
        }
        return false;
    }

    @Override // com.miui.gallery.card.scenario.time.quarterly.QuarterlyScenario, com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return getMediaIdsByTimeGroupPeopleIds(2, 10, getStartTime(), getEndTime(), this.mImportantPeopleIds);
    }
}