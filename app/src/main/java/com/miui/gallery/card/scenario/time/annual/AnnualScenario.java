package com.miui.gallery.card.scenario.time.annual;

import android.content.res.Resources;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.time.TimeScenario;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes.dex */
public class AnnualScenario extends TimeScenario {
    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        boolean hasTriggeredNewScenario = GalleryPreferences.Assistant.hasTriggeredNewScenario();
        List<Long> recordStartTimesFromRecordAndCards = getRecordStartTimesFromRecordAndCards(list, list2, true);
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        long firstDayOfYearTime = DateUtils.getFirstDayOfYearTime(currentTimeMillis);
        if ((currentTimeMillis <= firstDayOfYearTime || currentTimeMillis >= CoreConstants.MILLIS_IN_ONE_WEEK + firstDayOfYearTime) && hasTriggeredNewScenario) {
            return false;
        }
        setStartTime(DateUtils.getFirstDayOfYearTime(firstDayOfYearTime - 86400000));
        setEndTime(firstDayOfYearTime);
        return !recordStartTimesFromRecordAndCards.contains(Long.valueOf(getStartTime()));
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
        int scenarioType = getScenarioType();
        if (scenarioType != 1) {
            if (scenarioType == 2) {
                return getRandomArrayString(R.array.annual_foods_title);
            }
            if (scenarioType == 3) {
                return resources.getString(R.string.annual_babies_title);
            }
            if (scenarioType == 4) {
                return resources.getString(R.string.annual_group_people_title);
            }
            if (scenarioType == 5) {
                return getRandomArrayString(R.array.important_people_title);
            }
            if (scenarioType == 9) {
                return resources.getString(R.string.annula_sports_title);
            }
            return resources.getString(R.string.annual_year_title);
        }
        return resources.getString(R.string.annual_pets_title);
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        return recordStartTime > 0 ? DateUtils.getYearLocale(recordStartTime) : "";
    }
}
