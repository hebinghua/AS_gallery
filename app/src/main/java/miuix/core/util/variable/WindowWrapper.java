package miuix.core.util.variable;

import android.os.Build;
import android.view.View;
import android.view.Window;
import java.lang.reflect.Method;
import miuix.reflect.Reflects;

/* loaded from: classes3.dex */
public class WindowWrapper {
    public static Method setExtraFlags;

    static {
        try {
            Class cls = Integer.TYPE;
            setExtraFlags = Reflects.getMethod(Window.class, "setExtraFlags", cls, cls);
        } catch (Exception unused) {
            setExtraFlags = null;
        }
    }

    public static boolean setTranslucentStatus(Window window, int i) {
        boolean z = false;
        if (setExtraFlags == null) {
            return false;
        }
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 23) {
            if (i == 0) {
                window.clearFlags(Integer.MIN_VALUE);
            } else {
                window.addFlags(Integer.MIN_VALUE);
                View decorView = window.getDecorView();
                if (i == 1) {
                    decorView.setSystemUiVisibility(8208);
                } else {
                    decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (-8193) & (-17));
                }
            }
        }
        if (i2 >= 19) {
            if (i == 0) {
                window.clearFlags(67108864);
            } else {
                window.setFlags(67108864, 67108864);
            }
        }
        try {
            if (i == 0) {
                setExtraFlags.invoke(window, 0, 17);
            } else {
                setExtraFlags.invoke(window, Integer.valueOf(i == 1 ? 17 : 1), 17);
            }
            z = true;
            return true;
        } catch (Exception unused) {
            return z;
        }
    }
}
