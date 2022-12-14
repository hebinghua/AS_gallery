package com.adobe.xmp;

import java.util.Calendar;
import java.util.TimeZone;

/* loaded from: classes.dex */
public interface XMPDateTime extends Comparable {
    Calendar getCalendar();

    int getDay();

    int getHour();

    int getMinute();

    int getMonth();

    int getNanoSecond();

    int getSecond();

    TimeZone getTimeZone();

    int getYear();

    void setDay(int i);

    void setHour(int i);

    void setMinute(int i);

    void setMonth(int i);

    void setNanoSecond(int i);

    void setSecond(int i);

    void setTimeZone(TimeZone timeZone);

    void setYear(int i);
}
