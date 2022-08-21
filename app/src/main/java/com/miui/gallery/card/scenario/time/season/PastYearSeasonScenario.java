package com.miui.gallery.card.scenario.time.season;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import java.util.List;

/* loaded from: classes.dex */
public class PastYearSeasonScenario extends SeasonScenario {
    public int MAX_SEARCH_YEAR_COUNT = 5;

    @Override // com.miui.gallery.card.scenario.time.season.SeasonScenario, com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        int ceil = ((int) Math.ceil((currentTimeMillis - DateUtils.getSeasonStartTime(currentTimeMillis)) / 86400000)) - 7;
        if (ceil > 0 && ceil <= this.MAX_SEARCH_YEAR_COUNT) {
            long lastNYearDateTime = DateUtils.getLastNYearDateTime(ceil, currentTimeMillis);
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
