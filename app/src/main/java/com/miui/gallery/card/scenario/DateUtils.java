package com.miui.gallery.card.scenario;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.util.StaticContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class DateUtils {
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long getFirstDayOfMonthTime(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long getLastDayEndOfMonthTime(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i = calendar.get(2);
        if (i == 11) {
            calendar.set(1, calendar.get(1) + 1);
            calendar.set(2, 0);
            calendar.set(5, 1);
        } else {
            calendar.set(2, i + 1);
            calendar.set(5, 1);
        }
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long getDateTime(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long getLastNYearDateTime(int i, long j) {
        if (i < 1) {
            i = 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i2 = calendar.get(5);
        calendar.set(1, calendar.get(1) - i);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (i2 != calendar.get(5)) {
            return -1L;
        }
        return calendar.getTimeInMillis();
    }

    public static String getDatePeriodGraceful(long j, long j2) {
        String yearMonthDayLocale;
        String yearMonthDayLocale2;
        if (isSameDay(j, j2)) {
            return getYearMonthDayLocale(j);
        }
        if (j <= 0 || j2 <= j) {
            return "";
        }
        String language = Locale.getDefault().getLanguage();
        boolean isSameYear = isSameYear(j, j2);
        boolean z = isSameYear && isSameMonth(j, j2);
        if (new Locale("zh").getLanguage().equals(language)) {
            yearMonthDayLocale = getYearMonthDayLocale(j);
            if (z) {
                yearMonthDayLocale2 = getDayLocale(j2);
            } else if (isSameYear) {
                yearMonthDayLocale2 = getMonthDayLocale(j2);
            } else {
                yearMonthDayLocale2 = getYearMonthDayLocale(j2);
            }
        } else if (!new Locale("en").getLanguage().equals(language)) {
            return GalleryApp.sGetAndroidContext().getResources().getString(R.string.card_description_from_to, getYearMonthDayLocale(j), getYearMonthDayLocale(j2));
        } else {
            if (z) {
                yearMonthDayLocale = getMonthDayLocale(j);
                yearMonthDayLocale2 = getDayLocale(j2) + ", " + getYearLocale(j2);
            } else if (isSameYear) {
                yearMonthDayLocale = getMonthDayLocale(j);
                yearMonthDayLocale2 = getYearMonthDayLocale(j2);
            } else {
                yearMonthDayLocale = getYearMonthDayLocale(j);
                yearMonthDayLocale2 = getYearMonthDayLocale(j2);
            }
        }
        return GalleryApp.sGetAndroidContext().getResources().getString(R.string.card_description_from_to, yearMonthDayLocale, yearMonthDayLocale2);
    }

    public static String getMonthPeriodGraceful(long j, long j2) {
        String yearMonthLocale;
        String yearMonthLocale2;
        if (isSameMonth(j, j2)) {
            return getYearMonthLocale(j);
        }
        if (j <= 0 || j2 <= j) {
            return "";
        }
        String language = Locale.getDefault().getLanguage();
        boolean isSameYear = isSameYear(j, j2);
        if (new Locale("zh").getLanguage().equals(language)) {
            if (isSameYear) {
                yearMonthLocale = getYearMonthLocale(j);
                yearMonthLocale2 = getMonthLocale(j2);
            } else {
                yearMonthLocale = getYearMonthLocale(j);
                yearMonthLocale2 = getYearMonthLocale(j2);
            }
        } else if (!new Locale("en").getLanguage().equals(language)) {
            return GalleryApp.sGetAndroidContext().getResources().getString(R.string.card_description_from_to, getYearMonthLocale(j), getYearMonthLocale(j2));
        } else {
            if (isSameYear) {
                yearMonthLocale = getMonthLocale(j);
                yearMonthLocale2 = getYearMonthLocale(j2);
            } else {
                yearMonthLocale = getYearMonthLocale(j);
                yearMonthLocale2 = getYearMonthLocale(j2);
            }
        }
        return GalleryApp.sGetAndroidContext().getResources().getString(R.string.card_description_from_to, yearMonthLocale, yearMonthLocale2);
    }

    public static String getYearMonthDayLocale(long j) {
        return miuix.pickerwidget.date.DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 896);
    }

    public static String getYearMonthLocale(long j) {
        return miuix.pickerwidget.date.DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 768);
    }

    public static String getMonthLocale(long j) {
        return miuix.pickerwidget.date.DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 256);
    }

    public static String getDayLocale(long j) {
        return miuix.pickerwidget.date.DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 128);
    }

    public static String getMonthDayLocale(long j) {
        return miuix.pickerwidget.date.DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 384);
    }

    public static String getYearLocale(long j) {
        return miuix.pickerwidget.date.DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), j, 512);
    }

    public static String getDateFormat(long j) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(j));
    }

    public static String getDateStamp(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(j));
    }

    public static long getFirstDayOfYearTime(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.set(2, 0);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndDayOfYearTime(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        calendar.set(2, 11);
        calendar.set(5, 31);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static int getMonth(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return calendar.get(2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static long getSeasonStartTime(long j) {
        int i;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i2 = calendar.get(2);
        int i3 = calendar.get(1);
        switch (i2) {
            case 0:
            case 1:
                i3--;
                i = 11;
                break;
            case 2:
            case 3:
            case 4:
                i = 2;
                break;
            case 5:
            case 6:
            case 7:
                i = 5;
                break;
            case 8:
            case 9:
            case 10:
                i = 8;
                break;
            default:
                i = 11;
                break;
        }
        calendar.set(1, i3);
        calendar.set(2, i);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long getSeasonEndTime(long j) {
        int i;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        int i2 = calendar.get(2);
        int i3 = calendar.get(1);
        switch (i2) {
            case 2:
            case 3:
            case 4:
                i = 4;
                break;
            case 5:
            case 6:
            case 7:
                i = 7;
                break;
            case 8:
            case 9:
            case 10:
                i = 10;
                break;
            case 11:
                i3++;
            default:
                i = 1;
                break;
        }
        calendar.set(1, i3);
        calendar.set(2, i + 1);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static int getSeason(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        switch (calendar.get(2)) {
            case 2:
            case 3:
            case 4:
                return 0;
            case 5:
            case 6:
            case 7:
                return 1;
            case 8:
            case 9:
            case 10:
                return 2;
            default:
                return 3;
        }
    }

    public static boolean isEvenMonth(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return (calendar.get(2) & 1) != 0;
    }

    public static boolean isSameDay(long j, long j2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j2);
        return calendar.get(1) == calendar2.get(1) && calendar.get(2) == calendar2.get(2) && calendar.get(5) == calendar2.get(5);
    }

    public static boolean isSameMonth(long j, long j2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j2);
        return calendar.get(1) == calendar2.get(1) && calendar.get(2) == calendar2.get(2);
    }

    public static boolean isSameYear(long j, long j2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j2);
        return calendar.get(1) == calendar2.get(1);
    }

    public static boolean withinTime(long j, long j2, long j3) {
        return Math.abs(j - j2) <= j3;
    }

    public static int getDaysBetween(long j, long j2) {
        if (j > j2) {
            return -1;
        }
        return (int) ((j2 - j) / 86400000);
    }

    public static int getDayOfMonth(long j) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        return calendar.get(5);
    }
}
