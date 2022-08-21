package com.miui.gallery.util;

import android.text.TextUtils;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: classes2.dex */
public class GalleryTimeUtils {
    public static ThreadLocal<SimpleDateFormat> sUTCFormatter = new ThreadLocal<>();
    public static ThreadLocal<SimpleDateFormat> sDefaultFormatter = new ThreadLocal<>();

    public static SimpleDateFormat getUTCDateFormat() {
        if (sUTCFormatter.get() == null) {
            sUTCFormatter.set(createSimpleDateFormat(true));
        }
        return sUTCFormatter.get();
    }

    public static SimpleDateFormat getDefaultDateFormat() {
        if (sDefaultFormatter.get() == null) {
            sDefaultFormatter.set(createSimpleDateFormat(false));
        }
        return sDefaultFormatter.get();
    }

    public static SimpleDateFormat createSimpleDateFormat(boolean z) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        if (z) {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
        }
        return simpleDateFormat;
    }

    public static long getDateTime(String str, String str2) {
        Date date = getDate(str);
        if (date == null) {
            return -1L;
        }
        long time = date.getTime();
        if (str2 == null) {
            return time;
        }
        if (str2.length() > 3) {
            str2 = str2.substring(0, 3);
        }
        try {
            return time + Long.parseLong(str2);
        } catch (NumberFormatException unused) {
            return time;
        }
    }

    public static long getDateTime(String str) {
        Date date = getDate(str);
        if (date != null) {
            return date.getTime();
        }
        return -1L;
    }

    public static Date getDate(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return getDefaultDateFormat().parse(str, new ParsePosition(0));
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public static long getGpsDateTime(String str, String str2) {
        Date gpsDate = getGpsDate(str, str2);
        if (gpsDate != null) {
            return gpsDate.getTime();
        }
        return -1L;
    }

    public static Date getGpsDate(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            String str3 = str + ' ' + str2;
            if (str3 == null) {
                return null;
            }
            try {
                return getUTCDateFormat().parse(str3, new ParsePosition(0));
            } catch (IllegalArgumentException unused) {
            }
        }
        return null;
    }

    public static long getTakenDateTime(String str, String str2, String str3, long j, long j2) {
        Date gpsDate = getGpsDate(str, str2);
        if (gpsDate == null) {
            long dateTime = getDateTime(str3);
            return dateTime == -1 ? j > 0 ? j : j2 : dateTime;
        }
        Date date = getDate(str3);
        if (date != null && !GalleryDateUtils.isSameDate(date, gpsDate)) {
            return date.getTime();
        }
        return gpsDate.getTime();
    }
}
