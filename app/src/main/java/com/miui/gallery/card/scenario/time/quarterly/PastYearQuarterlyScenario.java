package com.miui.gallery.card.scenario.time.quarterly;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import java.util.List;

/* loaded from: classes.dex */
public class PastYearQuarterlyScenario extends QuarterlyScenario {
    public int MAX_QUARTERLY_YEAR_COUNT = 3;

    @Override // com.miui.gallery.card.scenario.time.quarterly.QuarterlyScenario, com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        int ceil = ((int) Math.ceil((currentTimeMillis - DateUtils.getSeasonStartTime(currentTimeMillis)) / 86400000)) - 14;
        if ((ceil & 1) != 0) {
            ceil++;
        }
        int i = ceil >> 1;
        if (i > 0 && i <= this.MAX_QUARTERLY_YEAR_COUNT) {
            long lastNYearDateTime = DateUtils.getLastNYearDateTime(i, currentTimeMillis);
            if (lastNYearDateTime <= 0) {
                return false;
            }
            long seasonStartTime = DateUtils.getSeasonStartTime(lastNYearDateTime);
            long seasonEndTime = DateUtils.getSeasonEndTime(lastNYearDateTime);
            if (seasonStartTime >= 0 && seasonEndTime >= 0) {
                setStartTime(seasonStartTime);
                setEndTime(seasonEndTime);
                return true;
            }
        }
        return false;
    }
}
