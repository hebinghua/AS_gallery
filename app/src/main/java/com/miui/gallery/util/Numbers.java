package com.miui.gallery.util;

import android.database.Cursor;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class Numbers {
    public static Long parse(String str, Long l) {
        try {
            return Long.valueOf(Long.parseLong(str));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return l;
        }
    }

    public static void ensurePositive(long... jArr) {
        Objects.requireNonNull(jArr, "nums can not be null");
        if (jArr.length == 0) {
            throw new IllegalArgumentException("nums can not be empty");
        }
        for (long j : jArr) {
            if (Long.valueOf(j).longValue() <= 0) {
                throw new IllegalArgumentException("negative or zero received");
            }
        }
    }

    public static <T extends Number & Comparable<T>> int compare(T t, T t2) {
        if (t == null || t2 == null) {
            if (t != null) {
                return 1;
            }
            return t2 != null ? -1 : 0;
        }
        return ((Comparable) t).compareTo(t2);
    }

    public static <T extends Number & Comparable<T>> boolean equals(T t, T t2) {
        if (t == null || t2 == null) {
            return t == null && t2 == null;
        }
        return t.equals(t2);
    }

    public static <T extends Number & Comparable<T>> boolean equals(T t, long j) {
        return t != null && t.longValue() == j;
    }

    public static <T extends Number & Comparable<T>> boolean equals(T t, int i) {
        return t != null && t.intValue() == i;
    }

    public static int unbox(Integer num, int i) {
        return num == null ? i : num.intValue();
    }

    public static long unbox(Long l, long j) {
        return l == null ? j : l.longValue();
    }

    public static Long getLong(Cursor cursor, int i) {
        if (cursor.isNull(i)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(i));
    }

    public static long[] toArray(List<Long> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        long[] jArr = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            jArr[i] = list.get(i).longValue();
        }
        return jArr;
    }
}
