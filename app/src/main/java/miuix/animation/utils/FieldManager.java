package miuix.animation.utils;

import android.util.ArrayMap;
import android.util.Log;
import com.xiaomi.mirror.synergy.CallMethod;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/* loaded from: classes3.dex */
public class FieldManager {
    public Map<String, MethodInfo> mMethodMap = new ArrayMap();
    public Map<String, FieldInfo> mFieldMap = new ArrayMap();

    /* loaded from: classes3.dex */
    public static class FieldInfo {
        public Field field;
    }

    /* loaded from: classes3.dex */
    public static class MethodInfo {
        public Method method;
    }

    public synchronized <T> T getField(Object obj, String str, Class<T> cls) {
        if (obj != null && str != null) {
            if (str.length() != 0) {
                MethodInfo methodInfo = this.mMethodMap.get(str);
                if (methodInfo == null) {
                    methodInfo = getMethod(obj, getMethodName(str, CallMethod.METHOD_GET), this.mMethodMap, new Class[0]);
                }
                Method method = methodInfo.method;
                if (method != null) {
                    return (T) retToClz(invokeMethod(obj, method, new Object[0]), cls);
                }
                FieldInfo fieldInfo = this.mFieldMap.get(str);
                if (fieldInfo == null) {
                    fieldInfo = getField(obj, str, cls, this.mFieldMap);
                }
                Field field = fieldInfo.field;
                if (field == null) {
                    return null;
                }
                return (T) getValueByField(obj, field);
            }
        }
        return null;
    }

    public synchronized <T> boolean setField(Object obj, String str, Class<T> cls, T t) {
        if (obj != null && str != null) {
            if (str.length() != 0) {
                MethodInfo methodInfo = this.mMethodMap.get(str);
                if (methodInfo == null) {
                    methodInfo = getMethod(obj, getMethodName(str, "set"), this.mMethodMap, cls);
                }
                Method method = methodInfo.method;
                if (method != null) {
                    invokeMethod(obj, method, t);
                    return true;
                }
                FieldInfo fieldInfo = this.mFieldMap.get(str);
                if (fieldInfo == null) {
                    fieldInfo = getField(obj, str, cls, this.mFieldMap);
                }
                Field field = fieldInfo.field;
                if (field == null) {
                    return false;
                }
                setValueByField(obj, field, t);
                return true;
            }
        }
        return false;
    }

    public static <T> T retToClz(Object obj, Class<T> cls) {
        if (!(obj instanceof Number)) {
            return null;
        }
        Number number = (Number) obj;
        if (cls == Float.class || cls == Float.TYPE) {
            return (T) Float.valueOf(number.floatValue());
        }
        if (cls == Integer.class || cls == Integer.TYPE) {
            return (T) Integer.valueOf(number.intValue());
        }
        throw new IllegalArgumentException("getPropertyValue, clz must be float or int instead of " + cls);
    }

    public static String getMethodName(String str, String str2) {
        return str2 + Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static <T> T getValueByField(Object obj, Field field) {
        try {
            return (T) field.get(obj);
        } catch (Exception unused) {
            return null;
        }
    }

    public static <T> void setValueByField(Object obj, Field field, T t) {
        try {
            field.set(obj, t);
        } catch (Exception unused) {
        }
    }

    public static MethodInfo getMethod(Object obj, String str, Map<String, MethodInfo> map, Class<?>... clsArr) {
        MethodInfo methodInfo = map.get(str);
        if (methodInfo == null) {
            MethodInfo methodInfo2 = new MethodInfo();
            methodInfo2.method = getMethod(obj, str, clsArr);
            map.put(str, methodInfo2);
            return methodInfo2;
        }
        return methodInfo;
    }

    public static Method getMethod(Object obj, String str, Class<?>... clsArr) {
        try {
            try {
                Method declaredMethod = obj.getClass().getDeclaredMethod(str, clsArr);
                declaredMethod.setAccessible(true);
                return declaredMethod;
            } catch (NoSuchMethodException unused) {
                return obj.getClass().getMethod(str, clsArr);
            }
        } catch (NoSuchMethodException unused2) {
            return null;
        }
    }

    public static FieldInfo getField(Object obj, String str, Class<?> cls, Map<String, FieldInfo> map) {
        FieldInfo fieldInfo = map.get(str);
        if (fieldInfo == null) {
            FieldInfo fieldInfo2 = new FieldInfo();
            fieldInfo2.field = getFieldByType(obj, str, cls);
            map.put(str, fieldInfo2);
            return fieldInfo2;
        }
        return fieldInfo;
    }

    public static Field getFieldByType(Object obj, String str, Class<?> cls) {
        Field field;
        try {
            field = obj.getClass().getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            field = null;
        }
        try {
            field.setAccessible(true);
        } catch (NoSuchFieldException unused2) {
            try {
                field = obj.getClass().getField(str);
            } catch (NoSuchFieldException unused3) {
            }
            if (field != null) {
            }
            return field;
        }
        if (field != null || field.getType() == cls) {
            return field;
        }
        return null;
    }

    public static <T> T invokeMethod(Object obj, Method method, Object... objArr) {
        if (method != null) {
            try {
                return (T) method.invoke(obj, objArr);
            } catch (Exception e) {
                Log.d("miuix_anim", "ValueProperty.invokeMethod failed, " + method.getName(), e);
                return null;
            }
        }
        return null;
    }
}
