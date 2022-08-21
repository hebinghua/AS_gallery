package com.miui.gallery.card.scenario.time.holiday;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.util.assistant.HolidaysUtil;
import java.util.List;
import miuix.pickerwidget.date.Calendar;

/* loaded from: classes.dex */
public class PastYearHolidayScenario extends HolidayScenario {
    @Override // com.miui.gallery.card.scenario.time.holiday.HolidayScenario, com.miui.gallery.card.scenario.Scenario
    public boolean onPrepare(List<Record> list, List<Card> list2) {
        boolean z;
        long lastNYearDateTime;
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        Calendar calendar = new Calendar();
        calendar.setTimeInMillis(currentTimeMillis);
        HolidaysUtil.Holiday holiday = HolidaysUtil.getHoliday(calendar);
        HolidaysUtil.Holiday holiday2 = HolidaysUtil.Holiday.E_HOLIDAY_NONE;
        if (holiday == holiday2) {
            holiday = HolidaysUtil.getChineseHoliday(calendar);
            z = true;
        } else {
            z = false;
        }
        long[] jArr = new long[2];
        if (holiday == holiday2 || (z && this.mScenarioId >= 1151)) {
            return false;
        }
        if ((z || this.mScenarioId >= 1151) && holiday.getKey() >= this.mHolidayId && holiday.getKey() < this.mHolidayId + holiday.getTotalDay()) {
            if (z) {
                lastNYearDateTime = HolidaysUtil.getChineseHolidayDatetimeOfPastYear(calendar, holiday.getDayNum());
            } else {
                lastNYearDateTime = DateUtils.getLastNYearDateTime(holiday.getDayNum(), currentTimeMillis);
            }
            if (lastNYearDateTime <= 0) {
                return false;
            }
            if (holiday.getTotalDay() == 1) {
                if (holiday == HolidaysUtil.Holiday.E_SOLAR_CHRISTMAS_DAY || holiday == HolidaysUtil.Holiday.E_SOLAR_HALLOWEEN_DAY) {
                    jArr[0] = DateUtils.getDateTime(lastNYearDateTime) - 86400000;
                    jArr[1] = jArr[0] + 172800000;
                } else {
                    jArr[0] = DateUtils.getDateTime(lastNYearDateTime);
                    jArr[1] = jArr[0] + 86400000;
                }
                setTargetTime(DateUtils.getDateTime(lastNYearDateTime));
            } else {
                jArr[0] = DateUtils.getDateTime(lastNYearDateTime) - ((holiday.getDayNum() - 1) * 86400000);
                jArr[1] = jArr[0] + (holiday.getTotalDay() * 86400000);
                setTargetTime(jArr[0]);
            }
            setStartTime(jArr[0]);
            setEndTime(jArr[1]);
            return true;
        }
        return false;
    }
}
