package com.miui.gallery.card.scenario.time.holiday;

import android.content.res.Resources;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.util.assistant.HolidaysUtil;
import java.util.List;
import miuix.pickerwidget.date.Calendar;

/* loaded from: classes.dex */
public class HolidayTravelScenario extends BaseHolidayScenario {
    @Override // com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        HolidaysUtil.Holiday holiday = HolidaysUtil.getHoliday(currentTimeMillis);
        HolidaysUtil.Holiday holiday2 = HolidaysUtil.Holiday.E_HOLIDAY_NONE;
        if (holiday == holiday2 && HolidaysUtil.getChineseHoliday(currentTimeMillis) == holiday2) {
            Calendar calendar = new Calendar();
            long j = currentTimeMillis - 86400000;
            calendar.setTimeInMillis(j);
            HolidaysUtil.Holiday holiday3 = HolidaysUtil.getHoliday(calendar);
            this.mTargetHoliday = holiday3;
            if (holiday3 == holiday2) {
                this.mTargetHoliday = HolidaysUtil.getChineseHoliday(calendar);
            }
            long[] jArr = new long[2];
            HolidaysUtil.Holiday holiday4 = this.mTargetHoliday;
            if (holiday4 == HolidaysUtil.Holiday.E_SOLAR_NATIONAL_DAY_SEVEN || holiday4 == HolidaysUtil.Holiday.E_SOLAR_LABOUR_DAY_FIVE || holiday4 == HolidaysUtil.Holiday.E_CHINESE_NEW_YEAR_SIX) {
                jArr[0] = DateUtils.getDateTime(j) - ((this.mTargetHoliday.getDayNum() - 1) * 86400000);
                jArr[1] = jArr[0] + (this.mTargetHoliday.getTotalDay() * 86400000);
                setStartTime(jArr[0]);
                setEndTime(jArr[1]);
                setTargetTime(jArr[0]);
                return true;
            }
            List<Long> recordStartTimesFromRecordAndCards = getRecordStartTimesFromRecordAndCards(list, list2, true);
            for (int i = 3; i <= 5; i++) {
                long currentTimeMillis2 = DateUtils.getCurrentTimeMillis() - (i * 86400000);
                calendar.setTimeInMillis(currentTimeMillis2);
                HolidaysUtil.Holiday holiday5 = HolidaysUtil.getHoliday(calendar);
                this.mTargetHoliday = holiday5;
                if (holiday5 == HolidaysUtil.Holiday.E_HOLIDAY_NONE) {
                    this.mTargetHoliday = HolidaysUtil.getChineseHoliday(currentTimeMillis);
                }
                if (this.mTargetHoliday.getTotalDay() == 1) {
                    jArr[0] = DateUtils.getDateTime(currentTimeMillis2);
                    jArr[1] = jArr[0] + 86400000;
                    setStartTime(jArr[0]);
                    setEndTime(jArr[1]);
                    setTargetTime(jArr[0]);
                    return !recordStartTimesFromRecordAndCards.contains(Long.valueOf(getStartTime()));
                }
            }
            return false;
        }
        return false;
    }

    /* renamed from: com.miui.gallery.card.scenario.time.holiday.HolidayTravelScenario$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday;

        static {
            int[] iArr = new int[HolidaysUtil.Holiday.values().length];
            $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday = iArr;
            try {
                iArr[HolidaysUtil.Holiday.E_SOLAR_NATIONAL_DAY_SEVEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[HolidaysUtil.Holiday.E_SOLAR_LABOUR_DAY_FIVE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateTitle(Record record, List<MediaFeatureItem> list) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$util$assistant$HolidaysUtil$Holiday[this.mTargetHoliday.ordinal()];
        return i != 1 ? i != 2 ? resources.getString(R.string.holiday_travel_title_two) : resources.getString(R.string.holiday_travel_title_one, resources.getString(R.string.labour_day)) : resources.getString(R.string.holiday_travel_title_one, resources.getString(R.string.national_day));
    }

    @Override // com.miui.gallery.card.scenario.Scenario
    public String generateDescription(Record record, List<MediaFeatureItem> list) {
        long recordStartTime = getRecordStartTime(record);
        long recordEndTime = getRecordEndTime(record);
        return (recordStartTime <= 0 || recordEndTime <= 0) ? "" : DateUtils.getDatePeriodGraceful(recordStartTime, recordEndTime);
    }
}
