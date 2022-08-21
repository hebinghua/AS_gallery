package com.miui.gallery.card.scenario.time.month;

import android.content.res.Resources;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.time.TimeScenario;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes.dex */
public class MonthlyScenario extends TimeScenario {
    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        List<Long> recordStartTimesFromRecordAndCards = getRecordStartTimesFromRecordAndCards(list, list2, true);
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        long firstDayOfMonthTime = DateUtils.getFirstDayOfMonthTime(currentTimeMillis);
        if (currentTimeMillis > firstDayOfMonthTime && currentTimeMillis < CoreConstants.MILLIS_IN_ONE_WEEK + firstDayOfMonthTime) {
            long j = firstDayOfMonthTime - 86400000;
            long firstDayOfMonthTime2 = DateUtils.getFirstDayOfMonthTime(j);
            long lastDayEndOfMonthTime = DateUtils.getLastDayEndOfMonthTime(j);
            if (firstDayOfMonthTime2 >= 0 && lastDayEndOfMonthTime >= 0 && !recordStartTimesFromRecordAndCards.contains(Long.valueOf(firstDayOfMonthTime2))) {
                setStartTime(firstDayOfMonthTime2);
                setEndTime(lastDayEndOfMonthTime);
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        if (getScenarioType() == 3) {
            return getMediaIdsByStartEndTimeAndAge(getStartTime(), getEndTime(), 1, 12);
        }
        if (!BaseMiscUtil.isValid(this.mTagIdList)) {
            return null;
        }
        return getMediaIdsByStartEndTimeTags(this.mTagIdList, getStartTime(), getEndTime());
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        long recordStartTime = getRecordStartTime(record);
        if (recordStartTime > 0) {
            int scenarioType = getScenarioType();
            if (scenarioType == 1) {
                return resources.getString(R.string.monthly_pets_title);
            }
            if (scenarioType == 2) {
                return DateUtils.getMonthLocale(recordStartTime) + getRandomArrayString(R.array.monthly_foods_title);
            } else if (scenarioType == 3) {
                return getRandomArrayString(R.array.monthly_babies_title);
            } else {
                if (scenarioType == 4) {
                    return getRandomArrayString(R.array.group_people_title);
                }
                if (scenarioType == 5) {
                    return getRandomArrayString(R.array.important_people_title);
                }
                if (scenarioType == 9) {
                    return resources.getString(R.string.monthly_sports_title);
                }
            }
        }
        return "";
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        return recordStartTime > 0 ? DateUtils.getYearMonthLocale(recordStartTime) : "";
    }
}
