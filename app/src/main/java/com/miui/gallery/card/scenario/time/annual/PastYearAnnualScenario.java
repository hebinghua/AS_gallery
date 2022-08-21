package com.miui.gallery.card.scenario.time.annual;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.preference.GalleryPreferences;
import java.util.List;

/* loaded from: classes.dex */
public class PastYearAnnualScenario extends AnnualScenario {
    @Override // com.miui.gallery.card.scenario.time.annual.AnnualScenario, com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        boolean hasTriggeredNewScenario = GalleryPreferences.Assistant.hasTriggeredNewScenario();
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        int ceil = (((int) Math.ceil((currentTimeMillis - DateUtils.getFirstDayOfYearTime(currentTimeMillis)) / 86400000)) - 7) + 1;
        if ((ceil <= 1 || ceil > 5) && hasTriggeredNewScenario) {
            return false;
        }
        if (!hasTriggeredNewScenario) {
            ceil = 2;
        }
        long lastNYearDateTime = DateUtils.getLastNYearDateTime(ceil, currentTimeMillis);
        if (lastNYearDateTime <= 0) {
            return false;
        }
        setStartTime(DateUtils.getFirstDayOfYearTime(lastNYearDateTime));
        setEndTime(DateUtils.getEndDayOfYearTime(lastNYearDateTime));
        return true;
    }
}
