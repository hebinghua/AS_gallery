package com.miui.gallery.util;

import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes2.dex */
public class ReflectUtils {
    public static Object getInstance(String str, Object... objArr) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("className can not be null");
        }
        try {
            Class<?> cls = Class.forName(str);
            if (objArr != null) {
                int length = objArr.length;
                Class<?>[] clsArr = new Class[length];
                for (int i = 0; i < length; i++) {
                    clsArr[i] = objArr[i].getClass();
                }
                Constructor<?> declaredConstructor = cls.getDeclaredConstructor(clsArr);
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance(objArr);
            }
            Constructor<?> declaredConstructor2 = cls.getDeclaredConstructor(new Class[0]);
            declaredConstructor2.setAccessible(true);
            return declaredConstructor2.newInstance(new Object[0]);
        } catch (Exception e) {
            DefaultLogger.e("ReflectUtils", "create instance[%s] error: %s", str, e);
            return null;
        }
    }

    public static Object invoke(String str, Object obj, String str2, Object... objArr) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("className can not be null");
        }
        if (str2 == null || str2.equals("")) {
            throw new IllegalArgumentException("methodNamecan not be null");
        }
        try {
            Class<?> cls = Class.forName(str);
            if (objArr != null) {
                int length = objArr.length;
                Class<?>[] clsArr = new Class[length];
                for (int i = 0; i < length; i++) {
                    clsArr[i] = objArr[i].getClass();
                }
                Method declaredMethod = cls.getDeclaredMethod(str2, clsArr);
                declaredMethod.setAccessible(true);
                return declaredMethod.invoke(obj, objArr);
            }
            Method declaredMethod2 = cls.getDeclaredMethod(str2, new Class[0]);
            declaredMethod2.setAccessible(true);
            return declaredMethod2.invoke(obj, new Object[0]);
        } catch (Exception e) {
            DefaultLogger.e("ReflectUtils", "invoke method[%s] of [%s] error: %s", str2, str, e);
            return null;
        }
    }

    public static Object invokeMethod(Object obj, Method method, Object... objArr) {
        if (method == null) {
            throw new IllegalArgumentException("method can not be null");
        }
        method.setAccessible(true);
        try {
            return method.invoke(obj, objArr);
        } catch (Exception e) {
            DefaultLogger.e("ReflectUtils", "invoke method[%s] of [%s] error: %s", method, obj, e);
            return null;
        }
    }

    public static Object getField(String str, Object obj, String str2) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("className can not be null");
        }
        try {
            return getField(Class.forName(str), obj, str2);
        } catch (Exception e) {
            DefaultLogger.e("ReflectUtils", "get field[%s] of [%s] error: %s", str2, str, e);
            return null;
        }
    }

    public static Object getField(Class cls, Object obj, String str) {
        Field field = getField(cls, str);
        if (field != null) {
            try {
                return field.get(obj);
            } catch (Exception e) {
                DefaultLogger.e("ReflectUtils", "get field[%s] of [%s] error: %s", str, obj, e);
                return null;
            }
        }
        return null;
    }

    public static Field getField(Class cls, String str) {
        if (cls == null) {
            throw new IllegalArgumentException("class can not be null");
        }
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("fieldName can not be null");
        }
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException e) {
            DefaultLogger.e("ReflectUtils", "get field[%s] of [%s] error: %s", str, cls, e);
            return null;
        }
    }

    public static void setField(String str, Object obj, String str2, Object obj2) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("className can not be null");
        }
        if (str2 == null || str2.equals("")) {
            throw new IllegalArgumentException("fieldName can not be null");
        }
        try {
            setField(obj, Class.forName(str).getDeclaredField(str2), obj2);
        } catch (Exception e) {
            DefaultLogger.e("ReflectUtils", "set field[%s] of [%s] error: %s", str2, str, e);
        }
    }

    public static void setField(Object obj, Field field, Object obj2) {
        if (field == null) {
            throw new IllegalArgumentException("field can not be null");
        }
        field.setAccessible(true);
        try {
            field.set(obj, obj2);
        } catch (Exception e) {
            DefaultLogger.e("ReflectUtils", "set field[%s] of [%s] error: %s", field, obj, e);
        }
    }

    public static Method getMethod(String str, String str2) {
        Method[] declaredMethods;
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("className can not be null");
        }
        if (str2 == null || str2.equals("")) {
            throw new IllegalArgumentException("methodName can not be null");
        }
        try {
            declaredMethods = Class.forName(str).getDeclaredMethods();
        } catch (Exception e) {
            DefaultLogger.e("ReflectUtils", "get method[%s] of [%s] error: %s", str2, str, e);
        }
        if (declaredMethods == null) {
            return null;
        }
        for (Method method : declaredMethods) {
            if (method.getName().equals(str2)) {
                return method;
            }
        }
        return null;
    }
}
