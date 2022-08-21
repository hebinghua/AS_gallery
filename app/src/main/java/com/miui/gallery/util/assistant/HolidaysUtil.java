package com.miui.gallery.util.assistant;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.xiaomi.stat.c.b;
import miuix.pickerwidget.date.Calendar;

/* loaded from: classes2.dex */
public class HolidaysUtil {
    public static final Holiday[] CHINESE_HOLIDAYS = {Holiday.E_CHINESE_NEW_YEAR_EVE, Holiday.E_CHINESE_NEW_YEAR, Holiday.E_CHINESE_NEW_YEAR_TWO, Holiday.E_CHINESE_NEW_YEAR_THREE, Holiday.E_CHINESE_NEW_YEAR_FOUR, Holiday.E_CHINESE_NEW_YEAR_FIVE, Holiday.E_CHINESE_NEW_YEAR_SIX, Holiday.E_CHINESE_LANTERN_FESTIVAL, Holiday.E_CHINESE_DRAGON_HEAD_UP_FESTIVAL, Holiday.E_CHINESE_DRAGON_BOAT_FESTIVAL, Holiday.E_CHINESE_NIGHT_OF_SEVENS, Holiday.E_CHINESE_MID_AUTUMN_FESTIVAL};
    public static final Holiday[] SOLAR_HOLIDAYS = {Holiday.E_SOLAR_NEW_YEAR, Holiday.E_SOLAR_VALENTINES_DAY, Holiday.E_SOLAR_LABOUR_DAY, Holiday.E_SOLAR_LABOUR_DAY_TWO, Holiday.E_SOLAR_LABOUR_DAY_THREE, Holiday.E_SOLAR_LABOUR_DAY_FOUR, Holiday.E_SOLAR_LABOUR_DAY_FIVE, Holiday.E_SOLAR_CHILDREN_DAY, Holiday.E_SOLAR_NATIONAL_DAY, Holiday.E_SOLAR_NATIONAL_DAY_TWO, Holiday.E_SOLAR_NATIONAL_DAY_THREE, Holiday.E_SOLAR_NATIONAL_DAY_FOUR, Holiday.E_SOLAR_NATIONAL_DAY_FIVE, Holiday.E_SOLAR_NATIONAL_DAY_SIX, Holiday.E_SOLAR_NATIONAL_DAY_SEVEN, Holiday.E_SOLAR_HALLOWEEN_DAY, Holiday.E_SOLAR_CHRISTMAS_DAY, Holiday.E_SOLAR_NEW_YEAR_EVE};

    /* loaded from: classes2.dex */
    public enum Holiday {
        E_HOLIDAY_NONE(0, 0, 0),
        E_CHINESE_NEW_YEAR_EVE(-1, 1, 1),
        E_CHINESE_NEW_YEAR(101, 1, 6),
        E_CHINESE_NEW_YEAR_TWO(102, 2, 6),
        E_CHINESE_NEW_YEAR_THREE(103, 3, 6),
        E_CHINESE_NEW_YEAR_FOUR(104, 4, 6),
        E_CHINESE_NEW_YEAR_FIVE(105, 5, 6),
        E_CHINESE_NEW_YEAR_SIX(106, 6, 6),
        E_CHINESE_LANTERN_FESTIVAL(115, 1, 1),
        E_CHINESE_DRAGON_HEAD_UP_FESTIVAL(202, 1, 1),
        E_CHINESE_DRAGON_BOAT_FESTIVAL(505, 1, 1),
        E_CHINESE_NIGHT_OF_SEVENS(707, 1, 1),
        E_CHINESE_MID_AUTUMN_FESTIVAL(815, 1, 1),
        E_SOLAR_NEW_YEAR(101, 1, 1),
        E_SOLAR_VALENTINES_DAY(214, 1, 1),
        E_SOLAR_LABOUR_DAY(501, 1, 5),
        E_SOLAR_LABOUR_DAY_TWO(502, 2, 5),
        E_SOLAR_LABOUR_DAY_THREE(503, 3, 5),
        E_SOLAR_LABOUR_DAY_FOUR(504, 4, 5),
        E_SOLAR_LABOUR_DAY_FIVE(505, 5, 5),
        E_SOLAR_CHILDREN_DAY(601, 1, 1),
        E_SOLAR_NATIONAL_DAY(1001, 1, 7),
        E_SOLAR_NATIONAL_DAY_TWO(1002, 2, 7),
        E_SOLAR_NATIONAL_DAY_THREE(1003, 3, 7),
        E_SOLAR_NATIONAL_DAY_FOUR(1004, 4, 7),
        E_SOLAR_NATIONAL_DAY_FIVE(1005, 5, 7),
        E_SOLAR_NATIONAL_DAY_SIX(1006, 6, 7),
        E_SOLAR_NATIONAL_DAY_SEVEN(b.g, 7, 7),
        E_SOLAR_HALLOWEEN_DAY(1101, 1, 1),
        E_SOLAR_CHRISTMAS_DAY(1225, 1, 1),
        E_SOLAR_NEW_YEAR_EVE(1231, 1, 1);
        
        private final int dayNum;
        private final int key;
        private final int totalDay;

        Holiday(int i, int i2, int i3) {
            this.key = i;
            this.dayNum = i2;
            this.totalDay = i3;
        }

        public int getKey() {
            return this.key;
        }

        public int getDayNum() {
            return this.dayNum;
        }

        public int getTotalDay() {
            return this.totalDay;
        }
    }

    public static Holiday getHoliday(Calendar calendar) {
        Holiday[] holidayArr;
        int i = ((calendar.get(5) + 1) * 100) + calendar.get(9);
        for (Holiday holiday : SOLAR_HOLIDAYS) {
            if (holiday.getKey() == i) {
                return holiday;
            }
        }
        return Holiday.E_HOLIDAY_NONE;
    }

    public static Holiday getHoliday(long j) {
        Calendar calendar = new Calendar();
        calendar.setTimeInMillis(j);
        return getHoliday(calendar);
    }

    public static Holiday getChineseHoliday(Calendar calendar) {
        Holiday[] holidayArr;
        if (!calendar.outOfChineseCalendarRange()) {
            int i = ((calendar.get(6) + 1) * 100) + calendar.get(10);
            for (Holiday holiday : CHINESE_HOLIDAYS) {
                if (holiday.getKey() == -1) {
                    if (daysInChineseYear(calendar) == calendar.get(13)) {
                        return holiday;
                    }
                } else if (!calendar.isChineseLeapMonth() && i == holiday.getKey()) {
                    return holiday;
                }
            }
        }
        return Holiday.E_HOLIDAY_NONE;
    }

    public static Holiday getChineseHoliday(long j) {
        Calendar calendar = new Calendar();
        calendar.setTimeInMillis(j);
        return getChineseHoliday(calendar);
    }

    public static int daysInChineseYear(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(2, calendar.get(2) + 1);
        calendar2.set(6, 0);
        calendar2.set(10, 1);
        calendar2.setTimeInMillis(calendar2.getTimeInMillis() - 86400000);
        return calendar2.get(13);
    }

    public static long getChineseHolidayDatetimeOfPastYear(Calendar calendar, int i) {
        if (getChineseHoliday(calendar) != Holiday.E_HOLIDAY_NONE) {
            Calendar calendar2 = (Calendar) calendar.clone();
            calendar2.set(2, calendar.get(2) - i);
            int daysInChineseYear = daysInChineseYear(calendar2);
            if (daysInChineseYear == calendar.get(13)) {
                calendar2.set(13, daysInChineseYear);
            }
            calendar2.set(18, 0);
            calendar2.set(20, 0);
            calendar2.set(21, 0);
            calendar2.set(22, 0);
            return calendar2.getTimeInMillis();
        }
        return -1L;
    }

    public static String getZodiacYear(long j) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.chinese_zodiac_year)[calendar.get(1) % 12];
    }
}
