package com.miui.gallery.util;

import android.text.TextUtils;
import android.util.LruCache;
import com.miui.gallery.base.R$string;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.miai.api.StatusCode;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import miuix.core.util.Pools;
import miuix.pickerwidget.date.DateUtils;

/* loaded from: classes2.dex */
public class GalleryDateUtils {
    public static String sToday;
    public static String sYesterday;
    public static final byte[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final Pools.Pool<Calendar> CALENDAR_POOL = Pools.createSoftReferencePool(new Pools.Manager<Calendar>() { // from class: com.miui.gallery.util.GalleryDateUtils.1
        @Override // miuix.core.util.Pools.Manager
        /* renamed from: createInstance  reason: collision with other method in class */
        public Calendar mo2624createInstance() {
            return Calendar.getInstance();
        }
    }, 1);
    public static LruCache<Long, String> sDateCache = new LruCache<>(50);
    public static String sLocalLanguage = Locale.getDefault().toString();

    public static int getRelativeOnlyYear(int i) {
        return i >> 9;
    }

    public static boolean isSameMonth(long j, long j2) {
        long j3 = -32;
        return (j & j3) == (j2 & j3);
    }

    public static boolean isSameYear(long j, long j2) {
        long j3 = -512;
        return (j & j3) == (j2 & j3);
    }

    public static Calendar acquire() {
        return CALENDAR_POOL.acquire();
    }

    public static void release(Calendar calendar) {
        CALENDAR_POOL.release(calendar);
    }

    public static int format(long j) {
        Calendar acquire = acquire();
        acquire.setTimeInMillis(j);
        int i = (((acquire.get(1) << 4) | (acquire.get(2) + 1)) << 5) | acquire.get(5);
        release(acquire);
        return i;
    }

    public static long format(int i) {
        Calendar acquire = acquire();
        int i2 = i >> 5;
        acquire.set(5, i - (i2 << 5));
        int i3 = i2 >> 4;
        acquire.set(2, (i2 - (i3 << 4)) - 1);
        acquire.set(1, i3);
        long timeInMillis = acquire.getTimeInMillis();
        release(acquire);
        return timeInMillis;
    }

    public static void clearCache() {
        sToday = null;
        sYesterday = null;
        sDateCache.evictAll();
    }

    public static String getTodayTip() {
        if (sToday == null) {
            sToday = StaticContext.sGetAndroidContext().getResources().getString(R$string.today);
        }
        return sToday;
    }

    public static String getYesterdayTip() {
        if (sYesterday == null) {
            sYesterday = StaticContext.sGetAndroidContext().getResources().getString(R$string.yesterday);
        }
        return sYesterday;
    }

    public static String formatRelativeDate(long j) {
        String str;
        String locale = Locale.getDefault().toString();
        if (TextUtils.equals(sLocalLanguage, locale)) {
            str = sDateCache.get(Long.valueOf(j));
            DefaultLogger.d("GalleryDateUtils", "get cache: %s", String.valueOf(j));
        } else {
            clearCache();
            sLocalLanguage = locale;
            str = null;
        }
        if (str != null) {
            return str;
        }
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Pools.Pool<Calendar> pool = CALENDAR_POOL;
        Calendar acquire2 = pool.acquire();
        long currentTimeMillis = System.currentTimeMillis();
        acquire2.setTimeInMillis(currentTimeMillis);
        boolean z = true;
        int i = acquire2.get(1);
        int i2 = acquire2.get(6);
        acquire2.setTimeInMillis(j);
        if (i != acquire2.get(1)) {
            z = false;
        }
        if (z && i2 == acquire2.get(6)) {
            acquire.append(getTodayTip());
        } else if (z && Math.abs(acquire2.get(6) - i2) < 2 && j < currentTimeMillis) {
            acquire.append(getYesterdayTip());
        } else if (z) {
            DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), acquire, j, 4480);
        } else {
            DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), acquire, j, 4992);
        }
        pool.release(acquire2);
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        sDateCache.put(Long.valueOf(j), sb);
        return sb;
    }

    public static String formatRelativeOnlyDate(long j) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Pools.Pool<Calendar> pool = CALENDAR_POOL;
        Calendar acquire2 = pool.acquire();
        acquire2.setTimeInMillis(j);
        DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), acquire, j, 4224);
        pool.release(acquire2);
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static String formatRelativeOnlyMonth(long j) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Pools.Pool<Calendar> pool = CALENDAR_POOL;
        Calendar acquire2 = pool.acquire();
        acquire2.setTimeInMillis(j);
        DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), acquire, j, 4352);
        pool.release(acquire2);
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static String formatRelativeMonth(long j) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Pools.Pool<Calendar> pool = CALENDAR_POOL;
        Calendar acquire2 = pool.acquire();
        acquire2.setTimeInMillis(System.currentTimeMillis());
        acquire2.setTimeInMillis(j);
        DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), acquire, j, 4864);
        pool.release(acquire2);
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static String formatRelativeOnlyYear(long j) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Pools.Pool<Calendar> pool = CALENDAR_POOL;
        Calendar acquire2 = pool.acquire();
        acquire2.setTimeInMillis(j);
        DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), acquire, j, 512);
        pool.release(acquire2);
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static long daysBetween(long j, long j2) {
        return TimeUnit.MILLISECONDS.toDays(Math.abs(j2 - j));
    }

    public static long daysBeforeToday(long j) {
        long currentTimeMillis = System.currentTimeMillis();
        if (j >= currentTimeMillis) {
            return 0L;
        }
        Calendar acquire = acquire();
        acquire.setTimeInMillis(currentTimeMillis);
        acquire.set(11, 0);
        acquire.set(12, 0);
        acquire.set(13, 0);
        acquire.set(14, 0);
        long timeInMillis = acquire.getTimeInMillis();
        if (timeInMillis <= j) {
            return 0L;
        }
        long daysBetween = daysBetween(j, timeInMillis) + 1;
        release(acquire);
        return daysBetween;
    }

    public static int getYear(long j) {
        Calendar acquire = acquire();
        acquire.setTimeInMillis(j);
        int i = acquire.get(1);
        release(acquire);
        return i;
    }

    public static boolean isSameDate(Date date, Date date2) {
        if (date == date2) {
            return true;
        }
        if (date == null || date2 == null) {
            return false;
        }
        return Math.abs(date.getTime() - date2.getTime()) <= 86400000 && date.getDate() == date2.getDate();
    }

    public static boolean isLeapYear(int i) {
        return i % 4 == 0 && (i % 100 != 0 || i % StatusCode.BAD_REQUEST == 0);
    }

    public static int daysInMonth(boolean z, int i) {
        return (!z || i != 2) ? DAYS_IN_MONTH[i - 1] : DAYS_IN_MONTH[i - 1] + 1;
    }

    public static void invalidateCache() {
        ThreadManager.getMiscPool().submit(GalleryDateUtils$$ExternalSyntheticLambda0.INSTANCE);
    }
}
