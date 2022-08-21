package miui.cloud.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.micloudsdk.utils.ReflectUtils;
import com.xiaomi.mirror.synergy.CallMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes3.dex */
public class DeviceUtils {
    public static final String MARKET_NAME = getAndroidSystemProperties("ro.product.marketname", null);

    public static boolean isRedmiDigitSeries() {
        return isRedmiDigitSeries(getPhoneModel());
    }

    public static boolean isRedmiDigitSeries(String str) {
        return str.matches("(?i)^Redmi[\\s]*[0-9]+[^X]*$");
    }

    public static String getPhoneModel() {
        String str = MARKET_NAME;
        return !TextUtils.isEmpty(str) ? str : Build.MODEL;
    }

    public static String getAndroidSystemProperties(String str, String str2) {
        Class loadClass = ReflectUtils.loadClass("android.os.SystemProperties");
        if (loadClass == null) {
            Log.d("getAndroidSystemProperties", "class SystemProperties not found");
            return str2;
        }
        Method method = ReflectUtils.getMethod(loadClass, CallMethod.METHOD_GET, String.class, String.class);
        if (method == null) {
            Log.d("getAndroidSystemProperties", "no method get");
            return str2;
        }
        method.setAccessible(true);
        try {
            return (String) method.invoke(loadClass, str, str2);
        } catch (IllegalAccessException e) {
            Log.d("getAndroidSystemProperties", "error: " + e);
            return str2;
        } catch (InvocationTargetException e2) {
            Log.d("getAndroidSystemProperties", "error: " + e2);
            return str2;
        }
    }
}
