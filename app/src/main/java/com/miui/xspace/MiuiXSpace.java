package com.miui.xspace;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes3.dex */
public class MiuiXSpace implements IXSpaceWrapper {
    @Override // com.miui.xspace.IXSpaceWrapper
    public boolean isXSpaceEnable(Context context) {
        try {
            return ((Boolean) invokeSafely(Class.forName("android.provider.MiuiSettings$Secure"), "getBoolean", new Class[]{ContentResolver.class, String.class, Boolean.TYPE}, context.getContentResolver(), "xspace_enabled", Boolean.FALSE)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // com.miui.xspace.IXSpaceWrapper
    public File getXSpacePath() {
        try {
            return (File) invokeSafely(Class.forName("android.os.Environment$UserEnvironment").getDeclaredConstructor(Integer.TYPE).newInstance(999), "getExternalStorageDirectory", null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getDeclaredMethod(Object obj, String str, Class<?>[] clsArr) {
        if (obj instanceof Class) {
            try {
                return ((Class) obj).getMethod(str, clsArr);
            } catch (Exception unused) {
                return null;
            }
        }
        for (Class<?> cls = obj.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            try {
                return cls.getDeclaredMethod(str, clsArr);
            } catch (Exception unused2) {
            }
        }
        return null;
    }

    public static Object invokeSafely(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        try {
            Method declaredMethod = getDeclaredMethod(obj, str, clsArr);
            if (declaredMethod == null) {
                return null;
            }
            if (!declaredMethod.isAccessible()) {
                declaredMethod.setAccessible(true);
            }
            return declaredMethod.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            Log.e("MiuiXSpace", "", e);
            return null;
        } catch (IllegalArgumentException e2) {
            Log.e("MiuiXSpace", "", e2);
            return null;
        } catch (SecurityException e3) {
            Log.e("MiuiXSpace", "", e3);
            return null;
        } catch (InvocationTargetException e4) {
            Log.e("MiuiXSpace", "", e4);
            return null;
        } catch (Exception e5) {
            Log.e("MiuiXSpace", "", e5);
            return null;
        }
    }
}
