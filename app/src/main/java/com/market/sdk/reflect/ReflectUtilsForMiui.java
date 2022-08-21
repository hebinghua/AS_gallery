package com.market.sdk.reflect;

import ch.qos.logback.core.CoreConstants;
import com.xiaomi.stat.b.h;
import java.util.HashMap;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public class ReflectUtilsForMiui {
    public static Class<?>[] PRIMITIVE_CLASSES = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE};
    public static String[] SIGNATURE_OF_PRIMTIVE_CLASSES = {"Z", "B", "C", "S", "I", "J", "F", "D", "V"};
    public static final WeakHashMap<Object, HashMap<String, Object>> sAdditionalFields = new WeakHashMap<>();

    public static String getSignature(Class<?> cls) {
        int i = 0;
        while (true) {
            Class<?>[] clsArr = PRIMITIVE_CLASSES;
            if (i < clsArr.length) {
                if (cls == clsArr[i]) {
                    return SIGNATURE_OF_PRIMTIVE_CLASSES[i];
                }
                i++;
            } else {
                return getSignature(cls.getName());
            }
        }
    }

    public static String getSignature(String str) {
        int i = 0;
        while (true) {
            Class<?>[] clsArr = PRIMITIVE_CLASSES;
            if (i >= clsArr.length) {
                break;
            }
            if (clsArr[i].getName().equals(str)) {
                str = SIGNATURE_OF_PRIMTIVE_CLASSES[i];
            }
            i++;
        }
        String replace = str.replace(".", h.g);
        if (replace.startsWith("[")) {
            return replace;
        }
        return "L" + replace + ";";
    }

    public static String getSignature(Class<?>[] clsArr, Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        sb.append(CoreConstants.LEFT_PARENTHESIS_CHAR);
        if (clsArr != null) {
            for (Class<?> cls2 : clsArr) {
                sb.append(getSignature(cls2));
            }
        }
        sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
        sb.append(getSignature(cls));
        return sb.toString();
    }
}
