package com.miui.gallery.card.scenario.time.month;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import java.util.List;

/* loaded from: classes.dex */
public class PastYearMonthScenario extends MonthlyScenario {
    @Override // com.miui.gallery.card.scenario.time.month.MonthlyScenario, com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        int ceil = ((int) Math.ceil((currentTimeMillis - DateUtils.getFirstDayOfMonthTime(currentTimeMillis)) / 86400000)) - 7;
        if ((ceil & 1) != 0) {
            ceil++;
        }
        int i = ceil >> 1;
        if (i <= 0 || i > 3) {
            return false;
        }
        long lastNYearDateTime = DateUtils.getLastNYearDateTime(i, currentTimeMillis);
        if (lastNYearDateTime <= 0) {
            return false;
        }
        setStartTime(DateUtils.getFirstDayOfMonthTime(lastNYearDateTime));
        setEndTime(DateUtils.getLastDayEndOfMonthTime(lastNYearDateTime));
        return true;
    }
}
