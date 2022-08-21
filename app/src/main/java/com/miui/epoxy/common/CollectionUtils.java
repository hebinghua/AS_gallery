package com.miui.epoxy.common;

import android.os.Build;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class CollectionUtils {
    public static final <T> List<T> emptyList() {
        return new ArrayList(0);
    }

    public static <T> List<T> arrayToList(T... tArr) {
        return (List) arrayToCollection(tArr);
    }

    public static <T> List<T> singletonList(T t) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(t);
        return arrayList;
    }

    public static <T> Collection<T> arrayToCollection(T... tArr) {
        if (tArr == null) {
            return emptyList();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            return (Collection) Arrays.stream(tArr).collect(Collectors.toList());
        }
        ArrayList arrayList = new ArrayList(tArr.length);
        for (T t : tArr) {
            arrayList.add(t);
        }
        return arrayList;
    }
}
