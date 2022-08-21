package com.market.sdk.reflect;

import com.xiaomi.stat.b.h;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class ReflectTool {
    public static Map<Character, Class> BASIC_TYPES;

    static {
        HashMap hashMap = new HashMap();
        BASIC_TYPES = hashMap;
        hashMap.put('V', Void.TYPE);
        BASIC_TYPES.put('Z', Boolean.TYPE);
        BASIC_TYPES.put('B', Byte.TYPE);
        BASIC_TYPES.put('C', Character.TYPE);
        BASIC_TYPES.put('S', Short.TYPE);
        BASIC_TYPES.put('I', Integer.TYPE);
        BASIC_TYPES.put('J', Long.TYPE);
        BASIC_TYPES.put('F', Float.TYPE);
        BASIC_TYPES.put('D', Double.TYPE);
    }

    public static Class<?>[] parseTypesFromSignature(String str) throws ClassNotFoundException {
        if (str != null && str != "") {
            String substring = str.substring(str.indexOf(40) + 1, str.indexOf(41));
            if (substring != null && substring != "") {
                ArrayList arrayList = new ArrayList();
                int i = -1;
                boolean z = false;
                for (int i2 = 0; i2 < substring.length(); i2++) {
                    char charAt = substring.charAt(i2);
                    if (i >= 0 || !BASIC_TYPES.containsKey(Character.valueOf(charAt))) {
                        if (charAt == '[') {
                            z = true;
                        } else if (charAt == 'L') {
                            if (i == -1) {
                                i = i2;
                            }
                        } else if (charAt == ';') {
                            String replaceAll = substring.substring(i + 1, i2).replaceAll(h.g, ".");
                            if (z) {
                                arrayList.add(Array.newInstance(Class.forName(replaceAll), 0).getClass());
                            } else {
                                arrayList.add(Class.forName(replaceAll));
                            }
                            i = -1;
                        }
                    } else if (z) {
                        arrayList.add(Array.newInstance(BASIC_TYPES.get(Character.valueOf(charAt)), 0).getClass());
                    } else {
                        arrayList.add(BASIC_TYPES.get(Character.valueOf(charAt)));
                    }
                    z = false;
                }
                Class<?>[] clsArr = new Class[arrayList.size()];
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    clsArr[i3] = (Class) arrayList.get(i3);
                }
                return clsArr;
            }
        }
        return null;
    }
}
