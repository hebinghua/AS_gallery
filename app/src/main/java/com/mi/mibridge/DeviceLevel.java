package com.mi.mibridge;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class DeviceLevel {
    public static int CPU;
    public static int GPU;
    public static int HIGH;
    public static boolean IS_MIUI_LITE_VERSION;
    public static int LOW;
    public static int MIDDLE;
    public static int RAM;
    public static int TOTAL_RAM;
    public static int UNKNOWN;
    public static Method a;
    public static Method b;
    public static Method c;
    public static Method d;
    public static Class e;
    public static PathClassLoader f;
    public static Application g;
    public static Context h;
    public static Constructor<Class> i;
    public static Object j;

    static {
        try {
            PathClassLoader pathClassLoader = new PathClassLoader("/system/framework/MiuiBooster.jar", ClassLoader.getSystemClassLoader());
            f = pathClassLoader;
            Class loadClass = pathClassLoader.loadClass("com.miui.performance.DeviceLevelUtils");
            e = loadClass;
            i = loadClass.getConstructor(Context.class);
            a = e.getDeclaredMethod("initDeviceLevel", new Class[0]);
            Class<?> cls = Integer.TYPE;
            b = e.getDeclaredMethod("getDeviceLevel", cls, cls);
            c = e.getDeclaredMethod("getDeviceLevel", cls);
            d = e.getDeclaredMethod("isSupportPrune", new Class[0]);
            RAM = ((Integer) a(e, "DEVICE_LEVEL_FOR_RAM")).intValue();
            CPU = ((Integer) a(e, "DEVICE_LEVEL_FOR_CPU")).intValue();
            GPU = ((Integer) a(e, "DEVICE_LEVEL_FOR_GPU")).intValue();
            LOW = ((Integer) a(e, "LOW_DEVICE")).intValue();
            MIDDLE = ((Integer) a(e, "MIDDLE_DEVICE")).intValue();
            HIGH = ((Integer) a(e, "HIGH_DEVICE")).intValue();
            UNKNOWN = ((Integer) a(e, "DEVICE_LEVEL_UNKNOWN")).intValue();
            IS_MIUI_LITE_VERSION = ((Boolean) a(e, "IS_MIUI_LITE_VERSION")).booleanValue();
            TOTAL_RAM = ((Integer) a(e, "TOTAL_RAM")).intValue();
        } catch (Exception e2) {
            Log.e("DeviceLevel", "MiDeviceLevelBridge(): Load Class Exception:" + e2);
        }
        if (h == null) {
            try {
                Application application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, null);
                g = application;
                if (application != null) {
                    h = application.getApplicationContext();
                }
            } catch (Exception e3) {
                Log.e("DeviceLevel", "android.app.ActivityThread Exception:" + e3);
            }
        }
        if (h == null) {
            try {
                Application application2 = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", new Class[0]).invoke(null, null);
                g = application2;
                if (application2 != null) {
                    h = application2.getApplicationContext();
                }
            } catch (Exception e4) {
                Log.e("DeviceLevel", "android.app.AppGlobals Exception:" + e4);
            }
        }
        try {
            Constructor<Class> constructor = i;
            if (constructor == null) {
                return;
            }
            j = constructor.newInstance(h);
        } catch (Exception e5) {
            Log.e("DeviceLevel", "DeviceLevelUtils(): newInstance Exception:" + e5);
            e5.printStackTrace();
        }
    }

    public static <T> T a(Class<?> cls, String str) {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return (T) declaredField.get(null);
    }

    public static int getDeviceLevel(int i2) {
        try {
            return ((Integer) c.invoke(j, Integer.valueOf(i2))).intValue();
        } catch (Exception e2) {
            Log.e("DeviceLevel", "getDeviceLevel failed , e:" + e2.toString());
            return -1;
        }
    }
}
