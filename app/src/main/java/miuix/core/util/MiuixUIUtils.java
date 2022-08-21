package miuix.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.WindowManager;
import miuix.reflect.Reflects;

/* loaded from: classes3.dex */
public class MiuixUIUtils {
    public static boolean isNavigationBarFullScreen(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        String str = SystemProperties.get("qemu.hw.mainkeys");
        if ("1".equals(str)) {
            return false;
        }
        if ("0".equals(str)) {
            return true;
        }
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (identifier <= 0) {
            return false;
        }
        return resources.getBoolean(identifier);
    }

    public static int getRealNavigationBarHeight(Context context) {
        int i = 0;
        if (!checkDeviceHasNavigationBar(context)) {
            return 0;
        }
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier > 0) {
            i = resources.getDimensionPixelSize(identifier);
        }
        Log.i("MiuixUtils", "getNavigationBarHeightFromProp = " + i);
        return i;
    }

    public static boolean isSupportGestureLine(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "use_gesture_version_three", 0) != 0;
    }

    public static boolean isEnableGestureLine(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "hide_gesture_line", 0) == 0;
    }

    public static boolean isShowNavigationHandle(Context context) {
        return isEnableGestureLine(context) && isNavigationBarFullScreen(context) && isSupportGestureLine(context);
    }

    public static int getNavigationBarHeight(Context context) {
        int i = 0;
        int realNavigationBarHeight = (isShowNavigationHandle(context) || !isNavigationBarFullScreen(context)) ? getRealNavigationBarHeight(context) : 0;
        if (realNavigationBarHeight >= 0) {
            i = realNavigationBarHeight;
        }
        Log.i("MiuixUtils", "getNavigationBarHeight = " + i);
        return i;
    }

    public static boolean isInMultiWindowMode(Context context) {
        boolean z;
        if (context instanceof Activity) {
            return checkMultiWindow((Activity) context);
        }
        if (!(context instanceof ContextThemeWrapper)) {
            return false;
        }
        do {
            context = ((ContextThemeWrapper) context).getBaseContext();
            z = context instanceof Activity;
            if (z) {
                break;
            }
        } while (context instanceof ContextThemeWrapper);
        if (!z) {
            return false;
        }
        return checkMultiWindow((Activity) context);
    }

    public static boolean checkMultiWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= 24) {
            return activity.isInMultiWindowMode();
        }
        return false;
    }

    public static int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static boolean isFreeformMode(Context context) {
        Point point = new Point();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getSize(point);
        Point physicalSize = getPhysicalSize(context);
        return context.getResources().getConfiguration().toString().contains("mWindowingMode=freeform") && ((((float) point.x) + 0.0f) / ((float) physicalSize.x) <= 0.9f || (((float) point.y) + 0.0f) / ((float) physicalSize.y) <= 0.9f);
    }

    public static Point getPhysicalSize(Context context) {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Display defaultDisplay = windowManager.getDefaultDisplay();
        try {
            Object obj = Reflects.get(defaultDisplay, Reflects.getDeclaredField(defaultDisplay.getClass(), "mDisplayInfo"));
            point.x = ((Integer) Reflects.get(obj, Reflects.getField(obj.getClass(), "logicalWidth"))).intValue();
            point.y = ((Integer) Reflects.get(obj, Reflects.getField(obj.getClass(), "logicalHeight"))).intValue();
        } catch (Exception e) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            point.x = displayMetrics.widthPixels;
            point.y = displayMetrics.heightPixels;
            Log.w("MiuixUtils", "catch error! failed to get physical size", e);
        }
        return point;
    }
}
