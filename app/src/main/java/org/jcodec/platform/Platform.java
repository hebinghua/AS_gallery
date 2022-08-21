package org.jcodec.platform;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class Platform {
    public static final Map<Class, Class> boxed2primitive;

    public static long unsignedInt(int i) {
        return i & 4294967295L;
    }

    static {
        HashMap hashMap = new HashMap();
        boxed2primitive = hashMap;
        hashMap.put(Void.class, Void.TYPE);
        hashMap.put(Byte.class, Byte.TYPE);
        hashMap.put(Short.class, Short.TYPE);
        hashMap.put(Character.class, Character.TYPE);
        hashMap.put(Integer.class, Integer.TYPE);
        hashMap.put(Long.class, Long.TYPE);
        hashMap.put(Float.class, Float.TYPE);
        hashMap.put(Double.class, Double.TYPE);
    }

    public static <T> T newInstance(Class<T> cls, Object[] objArr) {
        try {
            return cls.getConstructor(classes(objArr)).newInstance(objArr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class[] classes(Object[] objArr) {
        Class[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            Class<?> cls = objArr[i].getClass();
            Map<Class, Class> map = boxed2primitive;
            if (map.containsKey(cls)) {
                clsArr[i] = map.get(cls);
            } else {
                clsArr[i] = cls;
            }
        }
        return clsArr;
    }

    public static String stringFromCharset(byte[] bArr, String str) {
        return new String(bArr, Charset.forName(str));
    }

    public static byte[] getBytesForCharset(String str, String str2) {
        return str.getBytes(Charset.forName(str2));
    }

    public static byte[] getBytes(String str) {
        try {
            return str.getBytes("iso8859-1");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String stringFromBytes(byte[] bArr) {
        try {
            return new String(bArr, "iso8859-1");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static boolean isAssignableFrom(Class cls, Class cls2) {
        if (cls == cls2 || cls.equals(cls2)) {
            return true;
        }
        return cls.isAssignableFrom(cls2);
    }
}
