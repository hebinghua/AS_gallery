package com.miui.gallery.card.scenario.time.season;

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
public class SeasonScenario extends TimeScenario {
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
        if (BaseMiscUtil.isValid(this.mTagIdList)) {
            return getMediaIdsByStartEndTimeTags(this.mTagIdList, getStartTime(), getEndTime());
        }
        return null;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        if (recordStartTime > 0) {
            int season = DateUtils.getSeason(recordStartTime);
            int i = season != 0 ? season != 1 ? season != 2 ? season != 3 ? 0 : R.array.winter_season_title : R.array.autumn_season_title : R.array.summer_season_title : R.array.spring_season_title;
            return i != 0 ? getRandomArrayString(i) : "";
        }
        return "";
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        if (recordStartTime > 0) {
            String yearLocale = DateUtils.getYearLocale(recordStartTime);
            int season = DateUtils.getSeason(recordStartTime);
            String[] stringArray = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.season);
            if (stringArray.length <= 0 || stringArray.length <= season) {
                return "";
            }
            return yearLocale + stringArray[season];
        }
        return "";
    }
}
