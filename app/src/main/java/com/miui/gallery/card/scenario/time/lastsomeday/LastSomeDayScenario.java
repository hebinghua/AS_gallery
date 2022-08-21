package com.miui.gallery.card.scenario.time.lastsomeday;

import android.content.res.Resources;
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
public class LastSomeDayScenario extends TimeScenario {
    public List<Long> mSelectIds;

    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        long dateTime = DateUtils.getDateTime(DateUtils.getCurrentTimeMillis() - 172800000);
        setTargetTime(dateTime);
        setStartTime(dateTime);
        setEndTime(dateTime + 172800000);
        if (getScenarioType() == 3) {
            this.mSelectIds = getMediaIdsByStartEndTimeAndAge(getStartTime(), getEndTime(), 1, 12);
        } else if (BaseMiscUtil.isValid(this.mTagIdList)) {
            this.mSelectIds = getMediaIdsByStartEndTimeTags(this.mTagIdList, getStartTime(), getEndTime());
        }
        List<Long> list3 = this.mSelectIds;
        return list3 != null && list3.size() >= getMinImageCount();
    }

    @Override // com.miui.gallery.card.scenario.time.TimeScenario, com.miui.gallery.card.scenario.Scenario
    public List<Long> loadMediaItem() {
        return this.mSelectIds;
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        switch (getScenarioType()) {
            case 1:
                return resources.getString(R.string.last_some_day_pets_title);
            case 2:
                return getRandomArrayString(R.array.last_some_day_foods_title);
            case 3:
                return getRandomArrayString(R.array.last_some_day_babies_title);
            case 4:
                return getRandomArrayString(R.array.group_people_title);
            case 5:
                return getRandomArrayString(R.array.important_people_title);
            case 6:
                return getRandomArrayString(R.array.last_some_day_wedding_title);
            case 7:
                return resources.getString(R.string.last_some_performance_title);
            case 8:
                return getRandomArrayString(R.array.last_some_day_playground_title);
            case 9:
                return getRandomArrayString(R.array.last_some_day_sports_title);
            case 10:
                return getRandomArrayString(R.array.last_some_day_ski_title);
            case 11:
                return getRandomArrayString(R.array.last_some_day_fit_title);
            case 12:
                return resources.getString(R.string.last_some_day_surfing_title);
            default:
                return "";
        }
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        return recordStartTime > 0 ? DateUtils.getYearMonthDayLocale(recordStartTime) : "";
    }
}
