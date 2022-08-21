package com.miui.gallery.card.scenario.time.lasthalfYear;

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
public class LastHalfYearScenario extends TimeScenario {
    public final int MONTH_NUM = 6;

    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        List<Long> recordStartTimesFromRecordAndCards = getRecordStartTimesFromRecordAndCards(list, list2, true);
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        boolean isEvenMonth = DateUtils.isEvenMonth(currentTimeMillis);
        long firstDayOfMonthTime = DateUtils.getFirstDayOfMonthTime(currentTimeMillis);
        if (isEvenMonth && currentTimeMillis > CoreConstants.MILLIS_IN_ONE_WEEK + firstDayOfMonthTime && currentTimeMillis < firstDayOfMonthTime + 1209600000) {
            long j = currentTimeMillis - 15552000000L;
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
        if (BaseMiscUtil.isValid(this.mTagIdList)) {
            return getMediaIdsByStartEndTimeTags(this.mTagIdList, getStartTime(), getEndTime());
        }
        return null;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        switch (this.mScenarioId % 100) {
            case 13:
                return getRandomArrayString(R.array.flowers_title);
            case 14:
                return getRandomArrayString(R.array.sunrise_sunset_title);
            case 15:
                return getRandomArrayString(R.array.forest_title);
            case 16:
                return resources.getString(R.string.sea_title);
            case 17:
                return getRandomArrayString(R.array.mountain_title);
            case 18:
                return getRandomArrayString(R.array.city_title);
            default:
                return "";
        }
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        long recordEndTime = getRecordEndTime(record);
        return (recordStartTime <= 0 || recordEndTime <= 0) ? "" : DateUtils.getMonthPeriodGraceful(recordStartTime, recordEndTime);
    }
}
