package com.miui.gallery.card.scenario.time.annual;

import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes.dex */
public class AnnualImportantPeopleScenario extends AnnualScenario {
    @Override // com.miui.gallery.card.scenario.time.annual.AnnualScenario, com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        List<Long> topNumPeopleIdsByTime = getTopNumPeopleIdsByTime(getStartTime(), getEndTime(), 3);
        if (BaseMiscUtil.isValid(topNumPeopleIdsByTime)) {
            return getMediaIdsByTimeGroupPeopleIds(2, 10, getStartTime(), getEndTime(), topNumPeopleIdsByTime);
        }
        return null;
    }
}
