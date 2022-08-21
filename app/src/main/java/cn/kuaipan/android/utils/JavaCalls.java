package cn.kuaipan.android.utils;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

/* loaded from: classes.dex */
public class JavaCalls {
    public static final HashMap<Class<?>, Class<?>> PRIMITIVE_MAP;

    /* loaded from: classes.dex */
    public static class JavaParam<T> {
        public final Class<? extends T> clazz;
        public final T obj;
    }

    static {
        HashMap<Class<?>, Class<?>> hashMap = new HashMap<>();
        PRIMITIVE_MAP = hashMap;
        Class<?> cls = Boolean.TYPE;
        hashMap.put(Boolean.class, cls);
        hashMap.put(Byte.class, Byte.TYPE);
        hashMap.put(Character.class, Character.TYPE);
        hashMap.put(Short.class, Short.TYPE);
        Class<?> cls2 = Integer.TYPE;
        hashMap.put(Integer.class, cls2);
        Class<?> cls3 = Float.TYPE;
        hashMap.put(Float.class, cls3);
        Class<?> cls4 = Long.TYPE;
        hashMap.put(Long.class, cls4);
        hashMap.put(Double.class, Double.TYPE);
        hashMap.put(cls, cls);
        Class<?> cls5 = Byte.TYPE;
        hashMap.put(cls5, cls5);
        Class<?> cls6 = Character.TYPE;
        hashMap.put(cls6, cls6);
        Class<?> cls7 = Short.TYPE;
        hashMap.put(cls7, cls7);
        hashMap.put(cls2, cls2);
        hashMap.put(cls3, cls3);
        hashMap.put(cls4, cls4);
        Class<?> cls8 = Double.TYPE;
        hashMap.put(cls8, cls8);
    }

    public static <T> T callMethod(Object obj, String str, Object... objArr) {
        try {
            return (T) callMethodOrThrow(obj, str, objArr);
        } catch (Exception e) {
            Log.w("JavaCalls", "Meet exception when call Method '" + str + "' in " + obj, e);
            return null;
        }
    }

    public static <T> T callMethodOrThrow(Object obj, String str, Object... objArr) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return (T) getDeclaredMethod(obj.getClass(), str, getParameterTypes(objArr)).invoke(obj, getParameters(objArr));
    }

    public static <T> T callStaticMethod(String str, String str2, Object... objArr) {
        try {
            return (T) callStaticMethodOrThrow(Class.forName(str), str2, objArr);
        } catch (Exception e) {
            Log.w("JavaCalls", "Meet exception when call Method '" + str2 + "' in " + str, e);
            return null;
        }
    }

    public static Method getDeclaredMethod(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        return findMethodByName(cls.getDeclaredMethods(), str, clsArr);
    }

    public static Method findMethodByName(Method[] methodArr, String str, Class<?>[] clsArr) throws NoSuchMethodException {
        Objects.requireNonNull(str, "Method name must not be null.");
        for (Method method : methodArr) {
            if (method.getName().equals(str) && compareClassLists(method.getParameterTypes(), clsArr)) {
                return method;
            }
        }
        throw new NoSuchMethodException(str);
    }

    public static boolean compareClassLists(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr == null) {
            return clsArr2 == null || clsArr2.length == 0;
        }
        int length = clsArr.length;
        if (clsArr2 == null) {
            return length == 0;
        } else if (length != clsArr2.length) {
            return false;
        } else {
            for (int i = length - 1; i >= 0; i--) {
                if (!clsArr[i].isAssignableFrom(clsArr2[i])) {
                    HashMap<Class<?>, Class<?>> hashMap = PRIMITIVE_MAP;
                    if (!hashMap.containsKey(clsArr[i]) || !hashMap.get(clsArr[i]).equals(hashMap.get(clsArr2[i]))) {
                    }
                }
                return true;
            }
            return false;
        }
    }

    public static <T> T callStaticMethodOrThrow(Class<?> cls, String str, Object... objArr) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return (T) getDeclaredMethod(cls, str, getParameterTypes(objArr)).invoke(null, getParameters(objArr));
    }

    public static Class<?>[] getParameterTypes(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return null;
        }
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            if (obj != null && (obj instanceof JavaParam)) {
                clsArr[i] = ((JavaParam) obj).clazz;
            } else {
                clsArr[i] = obj == null ? null : obj.getClass();
            }
        }
        return clsArr;
    }

    public static Object[] getParameters(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return null;
        }
        Object[] objArr2 = new Object[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            if (obj != null && (obj instanceof JavaParam)) {
                objArr2[i] = ((JavaParam) obj).obj;
            } else {
                objArr2[i] = obj;
            }
        }
        return objArr2;
    }
}
