package com.miui.gallery.base_optimization.util;

import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class GenericUtils {
    public static <T> T getSuperClassT(Object obj, int i) {
        try {
            Class superClass = getSuperClass(obj, i);
            if (superClass == null) {
                return null;
            }
            return (T) superClass.newInstance();
        } catch (Exception e) {
            DefaultLogger.e("GenericUtils", e);
            return null;
        }
    }

    public static <T> Class<T> getSuperClass(Object obj, int i) {
        Type genericSuperclass = obj.getClass().getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType) && obj.getClass().getSuperclass() != null) {
            genericSuperclass = obj.getClass().getSuperclass().getGenericSuperclass();
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        if (parameterizedType != null) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > i && (actualTypeArguments[i] instanceof Class)) {
                return (Class) actualTypeArguments[i];
            }
            return null;
        }
        return null;
    }

    public static <T> T getSelfClassT(Object obj, int i) {
        try {
            return (T) ((Class) obj.getClass().getTypeParameters()[i].getBounds()[i]).newInstance();
        } catch (Exception e) {
            DefaultLogger.e("GenericUtils", e);
            return null;
        }
    }

    public static <T> Class<T> getInterfaceClass(Object obj, int i) {
        try {
            Type[] genericInterfaces = obj.getClass().getGenericInterfaces();
            if (genericInterfaces != null && genericInterfaces.length > i) {
                Type type = genericInterfaces[i];
                if (type instanceof ParameterizedType) {
                    return (Class) ((ParameterizedType) type).getActualTypeArguments()[i];
                }
                return null;
            }
            return null;
        } catch (ClassCastException e) {
            DefaultLogger.e("GenericUtils", e);
            return null;
        }
    }
}
