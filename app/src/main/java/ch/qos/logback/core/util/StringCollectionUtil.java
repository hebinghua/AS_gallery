package ch.qos.logback.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class StringCollectionUtil {
    public static void retainMatching(Collection<String> collection, String... strArr) {
        retainMatching(collection, Arrays.asList(strArr));
    }

    public static void retainMatching(Collection<String> collection, Collection<String> collection2) {
        if (collection2.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList(collection.size());
        for (String str : collection2) {
            Pattern compile = Pattern.compile(str);
            for (String str2 : collection) {
                if (compile.matcher(str2).matches()) {
                    arrayList.add(str2);
                }
            }
        }
        collection.retainAll(arrayList);
    }

    public static void removeMatching(Collection<String> collection, String... strArr) {
        removeMatching(collection, Arrays.asList(strArr));
    }

    public static void removeMatching(Collection<String> collection, Collection<String> collection2) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (String str : collection2) {
            Pattern compile = Pattern.compile(str);
            for (String str2 : collection) {
                if (compile.matcher(str2).matches()) {
                    arrayList.add(str2);
                }
            }
        }
        collection.removeAll(arrayList);
    }
}
