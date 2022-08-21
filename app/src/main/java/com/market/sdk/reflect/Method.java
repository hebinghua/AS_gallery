package com.market.sdk.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class Method {
    public final java.lang.reflect.Method mMethod;

    public Method(java.lang.reflect.Method method) {
        this.mMethod = method;
    }

    public static Method of(Class<?> cls, String str, String str2) throws NoSuchMethodException {
        try {
            return new Method(cls.getMethod(str, ReflectTool.parseTypesFromSignature(str2)));
        } catch (ClassNotFoundException e) {
            throw new NoSuchMethodException(e.getMessage());
        } catch (NoSuchMethodException e2) {
            throw new NoSuchMethodException(e2.getMessage());
        }
    }

    public void invoke(Class<?> cls, Object obj, Object... objArr) throws IllegalArgumentException {
        java.lang.reflect.Method method = this.mMethod;
        if (method == null) {
            return;
        }
        try {
            method.setAccessible(true);
            this.mMethod.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e2) {
            throw new IllegalArgumentException(e2.getMessage());
        }
    }
}
