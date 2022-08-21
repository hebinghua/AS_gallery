package com.miui.gallery.card.scenario.time.lastsomeday;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import java.util.List;

/* loaded from: classes.dex */
public class LastSomeDayImportantPeopleScenario extends LastSomeDayScenario {
    public List<Long> mImportantPeopleIds;

    @Override // com.miui.gallery.card.scenario.time.lastsomeday.LastSomeDayScenario, com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        long dateTime = DateUtils.getDateTime(currentTimeMillis - 172800000);
        setTargetTime(dateTime);
        setStartTime(dateTime);
        setEndTime(dateTime + 172800000);
        List<Long> topNumPeopleIdsByTime = getTopNumPeopleIdsByTime(DateUtils.getDateTime(currentTimeMillis - 15552000000L), DateUtils.getDateTime(currentTimeMillis), 3);
        this.mImportantPeopleIds = topNumPeopleIdsByTime;
        return topNumPeopleIdsByTime != null && topNumPeopleIdsByTime.size() >= 2;
    }

    @Override // com.miui.gallery.card.scenario.time.lastsomeday.LastSomeDayScenario, com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return getMediaIdsByTimeGroupPeopleIds(2, 10, getStartTime(), getEndTime(), this.mImportantPeopleIds);
    }
}
