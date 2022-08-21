package miui.external;

import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes3.dex */
public class SdkHelper {
    public static boolean isMiuiSystem() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.code", ""));
    }

    public static String getSystemProperty(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getDeclaredMethod(CallMethod.METHOD_GET, String.class, String.class).invoke(null, str, str2);
        } catch (Exception e) {
            Log.e("miuisdk", "getSystemProperty error", e);
            return str2;
        }
    }
}
