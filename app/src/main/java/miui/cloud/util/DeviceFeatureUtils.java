package miui.cloud.util;

import android.util.Log;
import java.util.Arrays;

/* loaded from: classes3.dex */
public class DeviceFeatureUtils {
    public static boolean hasDeviceFeature(String str) {
        String[] allDeviceFeaturesOrNull = getAllDeviceFeaturesOrNull();
        if (allDeviceFeaturesOrNull == null) {
            return false;
        }
        return Arrays.asList(allDeviceFeaturesOrNull).contains(str);
    }

    public static String[] getAllDeviceFeaturesOrNull() {
        try {
            Class<?> cls = Class.forName("miui.cloud.DeviceFeature");
            return (String[]) cls.getField("features").get(cls);
        } catch (ClassNotFoundException e) {
            Log.e("DeviceFeatureUtils", "failed to find features from miclousdk, " + e);
            return null;
        } catch (IllegalAccessException e2) {
            Log.e("DeviceFeatureUtils", "failed to find features from miclousdk, " + e2);
            return null;
        } catch (NoSuchFieldException e3) {
            Log.e("DeviceFeatureUtils", "failed to find features from miclousdk, " + e3);
            return null;
        }
    }
}
