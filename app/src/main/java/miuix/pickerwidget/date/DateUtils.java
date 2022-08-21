package miuix.pickerwidget.date;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import java.util.TimeZone;
import miuix.core.util.Pools;
import miuix.pickerwidget.R$plurals;
import miuix.pickerwidget.R$string;

/* loaded from: classes3.dex */
public class DateUtils {
    public static final Pools.Pool<Calendar> CALENDAR_POOL = Pools.createSoftReferencePool(new Pools.Manager<Calendar>() { // from class: miuix.pickerwidget.date.DateUtils.1
        @Override // miuix.core.util.Pools.Manager
        /* renamed from: createInstance  reason: collision with other method in class */
        public Calendar mo2624createInstance() {
            return new Calendar();
        }
    }, 1);

    public static String formatDateTime(Context context, long j, int i) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        String sb = formatDateTime(context, acquire, j, i, null).toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static StringBuilder formatDateTime(Context context, StringBuilder sb, long j, int i) {
        return formatDateTime(context, sb, j, i, null);
    }

    public static StringBuilder formatDateTime(Context context, StringBuilder sb, long j, int i, TimeZone timeZone) {
        if ((i & 16) == 0 && (i & 32) == 0) {
            i |= DateFormat.is24HourFormat(context) ? 32 : 16;
        }
        String string = context.getString(getFormatResId(i));
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        Calendar acquire2 = CALENDAR_POOL.acquire();
        acquire2.setTimeZone(timeZone);
        acquire2.setTimeInMillis(j);
        int length = string.length();
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = string.charAt(i2);
            if (charAt == 'D') {
                acquire.append(context.getString(getDatePatternResId(i)));
            } else if (charAt == 'T') {
                acquire.append(context.getString(getTimePatternResId(acquire2, i)));
            } else if (charAt == 'W') {
                acquire.append(context.getString(getWeekdayPatternResId(i)));
            } else {
                acquire.append(charAt);
            }
        }
        acquire2.format(context, sb, acquire);
        Pools.getStringBuilderPool().release(acquire);
        CALENDAR_POOL.release(acquire2);
        return sb;
    }

    public static int getTimePatternResId(Calendar calendar, int i) {
        if ((i & 16384) == 16384 && (((i & 1) != 1 || calendar.get(22) == 0) && (i & 14) != 0)) {
            i &= -2;
            if (((i & 2) != 2 || calendar.get(21) == 0) && (i & 12) != 0) {
                i &= -3;
                if (calendar.get(20) == 0 && (i & 8) != 0) {
                    i &= -5;
                }
            }
        }
        if ((i & 8) != 8) {
            if ((i & 4) == 4) {
                if ((i & 2) != 2) {
                    return R$string.fmt_time_minute;
                }
                if ((i & 1) == 1) {
                    return R$string.fmt_time_minute_second_millis;
                }
                return R$string.fmt_time_minute_second;
            } else if ((i & 2) == 2) {
                if ((i & 1) == 1) {
                    return R$string.fmt_time_second_millis;
                }
                return R$string.fmt_time_second;
            } else if ((i & 1) == 1) {
                return R$string.fmt_time_millis;
            } else {
                throw new IllegalArgumentException("no any time date");
            }
        } else if ((i & 16) != 16) {
            if ((i & 4) != 4) {
                return R$string.fmt_time_24hour;
            }
            if ((i & 2) != 2) {
                return R$string.fmt_time_24hour_minute;
            }
            if ((i & 1) == 1) {
                return R$string.fmt_time_24hour_minute_second_millis;
            }
            return R$string.fmt_time_24hour_minute_second;
        } else if ((i & 64) == 64) {
            if ((i & 4) != 4) {
                return R$string.fmt_time_12hour;
            }
            if ((i & 2) != 2) {
                return R$string.fmt_time_12hour_minute;
            }
            if ((i & 1) == 1) {
                return R$string.fmt_time_12hour_minute_second_millis;
            }
            return R$string.fmt_time_12hour_minute_second;
        } else if ((i & 4) != 4) {
            return R$string.fmt_time_12hour_pm;
        } else {
            if ((i & 2) != 2) {
                return R$string.fmt_time_12hour_minute_pm;
            }
            if ((i & 1) == 1) {
                return R$string.fmt_time_12hour_minute_second_millis_pm;
            }
            return R$string.fmt_time_12hour_minute_second_pm;
        }
    }

    public static int getDatePatternResId(int i) {
        if ((i & 32768) == 32768) {
            if ((i & 512) == 512) {
                if ((i & 256) != 256) {
                    return R$string.fmt_date_numeric_year;
                }
                if ((i & 128) == 128) {
                    return R$string.fmt_date_numeric_year_month_day;
                }
                return R$string.fmt_date_numeric_year_month;
            } else if ((i & 256) == 256) {
                if ((i & 128) == 128) {
                    return R$string.fmt_date_numeric_month_day;
                }
                return R$string.fmt_date_numeric_month;
            } else if ((i & 128) == 128) {
                return R$string.fmt_date_numeric_day;
            } else {
                throw new IllegalArgumentException("no any time date");
            }
        } else if ((i & 4096) == 4096) {
            if ((i & 512) == 512) {
                if ((i & 256) != 256) {
                    return R$string.fmt_date_year;
                }
                if ((i & 128) == 128) {
                    return R$string.fmt_date_short_year_month_day;
                }
                return R$string.fmt_date_short_year_month;
            } else if ((i & 256) == 256) {
                if ((i & 128) == 128) {
                    return R$string.fmt_date_short_month_day;
                }
                return R$string.fmt_date_short_month;
            } else if ((i & 128) == 128) {
                return R$string.fmt_date_day;
            } else {
                throw new IllegalArgumentException("no any time date");
            }
        } else if ((i & 512) == 512) {
            if ((i & 256) != 256) {
                return R$string.fmt_date_year;
            }
            if ((i & 128) == 128) {
                return R$string.fmt_date_long_year_month_day;
            }
            return R$string.fmt_date_long_year_month;
        } else if ((i & 256) == 256) {
            if ((i & 128) == 128) {
                return R$string.fmt_date_long_month_day;
            }
            return R$string.fmt_date_long_month;
        } else if ((i & 128) == 128) {
            return R$string.fmt_date_day;
        } else {
            throw new IllegalArgumentException("no any time date");
        }
    }

    public static int getWeekdayPatternResId(int i) {
        return (i & 8192) == 8192 ? R$string.fmt_weekday_short : R$string.fmt_weekday_long;
    }

    public static int getFormatResId(int i) {
        return (i & 1024) == 1024 ? (i & 896) != 0 ? (i & 15) != 0 ? (i & 2048) == 2048 ? R$string.fmt_weekday_date_time_timezone : R$string.fmt_weekday_date_time : (i & 2048) == 2048 ? R$string.fmt_weekday_date_timezone : R$string.fmt_weekday_date : (i & 15) != 0 ? (i & 2048) == 2048 ? R$string.fmt_weekday_time_timezone : R$string.fmt_weekday_time : (i & 2048) == 2048 ? R$string.fmt_weekday_timezone : R$string.fmt_weekday : (i & 896) != 0 ? (i & 15) != 0 ? (i & 2048) == 2048 ? R$string.fmt_date_time_timezone : R$string.fmt_date_time : (i & 2048) == 2048 ? R$string.fmt_date_timezone : R$string.fmt_date : (i & 15) != 0 ? (i & 2048) == 2048 ? R$string.fmt_time_timezone : R$string.fmt_time : (i & 2048) == 2048 ? R$string.fmt_timezone : R$string.empty;
    }

    public static String formatRelativeTime(Context context, long j, boolean z) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        String sb = formatRelativeTime(context, acquire, j, z, null).toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static StringBuilder formatRelativeTime(Context context, StringBuilder sb, long j, boolean z, TimeZone timeZone) {
        int i;
        long currentTimeMillis = System.currentTimeMillis();
        boolean z2 = currentTimeMillis >= j;
        long abs = Math.abs(currentTimeMillis - j) / 60000;
        Resources resources = context.getResources();
        int i2 = (abs > 60L ? 1 : (abs == 60L ? 0 : -1));
        if (i2 > 0 || z) {
            Pools.Pool<Calendar> pool = CALENDAR_POOL;
            Calendar acquire = pool.acquire();
            acquire.setTimeZone(timeZone);
            acquire.setTimeInMillis(currentTimeMillis);
            int i3 = acquire.get(1);
            int i4 = acquire.get(12);
            int i5 = acquire.get(14);
            acquire.setTimeInMillis(j);
            boolean z3 = i3 == acquire.get(1);
            if (z3 && i4 == acquire.get(12)) {
                formatDateTime(context, sb, j, 12300, timeZone);
            } else if (z3 && Math.abs(i4 - acquire.get(12)) < 2) {
                sb.append(resources.getString(z2 ? R$string.yesterday : R$string.tomorrow));
                sb.append(' ');
                formatDateTime(context, sb, j, 12300, timeZone);
            } else {
                if (z3 && Math.abs(i4 - acquire.get(12)) < 7) {
                    if (z2 == (i5 > acquire.get(14))) {
                        formatDateTime(context, sb, j, 13324, timeZone);
                    }
                }
                if (z3) {
                    formatDateTime(context, sb, j, 12288 | (z ? 396 : 384), timeZone);
                } else {
                    formatDateTime(context, sb, j, 12288 | (z ? 908 : 896), timeZone);
                }
            }
            pool.release(acquire);
        } else {
            if (z2) {
                if (i2 == 0) {
                    i = R$plurals.abbrev_a_hour_ago;
                } else if (abs == 30) {
                    i = R$plurals.abbrev_half_hour_ago;
                } else if (abs == 0) {
                    i = R$plurals.abbrev_less_than_one_minute_ago;
                } else {
                    i = R$plurals.abbrev_num_minutes_ago;
                }
            } else if (i2 == 0) {
                i = R$plurals.abbrev_in_a_hour;
            } else if (abs == 30) {
                i = R$plurals.abbrev_in_half_hour;
            } else if (abs == 0) {
                i = R$plurals.abbrev_in_less_than_one_minute;
            } else {
                i = R$plurals.abbrev_in_num_minutes;
            }
            sb.append(String.format(resources.getQuantityString(i, (int) abs), Long.valueOf(abs)));
        }
        return sb;
    }
}
