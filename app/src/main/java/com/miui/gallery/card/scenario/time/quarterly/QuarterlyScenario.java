package com.miui.gallery.card.scenario.time.quarterly;

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
public class QuarterlyScenario extends TimeScenario {
    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        List<Long> recordStartTimesFromRecordAndCards = getRecordStartTimesFromRecordAndCards(list, list2, true);
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        long seasonStartTime = DateUtils.getSeasonStartTime(currentTimeMillis);
        if (currentTimeMillis > seasonStartTime && currentTimeMillis < CoreConstants.MILLIS_IN_ONE_WEEK + seasonStartTime) {
            long j = seasonStartTime - 86400000;
            long seasonStartTime2 = DateUtils.getSeasonStartTime(j);
            long seasonEndTime = DateUtils.getSeasonEndTime(j);
            if (seasonStartTime2 >= 0 && seasonEndTime >= 0 && !recordStartTimesFromRecordAndCards.contains(Long.valueOf(seasonStartTime2))) {
                setStartTime(seasonStartTime2);
                setEndTime(seasonEndTime);
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
            int season = DateUtils.getSeason(recordStartTime);
            int scenarioType = getScenarioType();
            if (scenarioType == 1) {
                return resources.getString(R.string.quarterly_pets_title);
            }
            if (scenarioType == 2) {
                String[] stringArray = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.quarterly_foods_title);
                if (stringArray != null && stringArray.length > season) {
                    return stringArray[season];
                }
            } else if (scenarioType == 3) {
                String[] stringArray2 = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.quarterly_babies_title);
                if (stringArray2 != null && stringArray2.length > season) {
                    return stringArray2[season];
                }
            } else if (scenarioType == 4) {
                return getRandomArrayString(R.array.group_people_title);
            } else {
                if (scenarioType == 5) {
                    return getRandomArrayString(R.array.important_people_title);
                }
            }
        }
        return "";
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        if (recordStartTime > 0) {
            int season = DateUtils.getSeason(recordStartTime);
            String[] stringArray = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.season);
            if (stringArray != null && stringArray.length > season) {
                return DateUtils.getYearLocale(recordStartTime) + stringArray[season];
            }
        }
        return "";
    }
}
