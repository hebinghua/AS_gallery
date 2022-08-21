package com.miui.gallery.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.android.internal.WindowCompat;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public class ScreenUtils extends BaseScreenUtil {
    public static final Set<String> DEVICES_DISABLE_CUTOUT;
    public static boolean sDeviceSupportScreenScene = false;
    public static boolean sHasRegistered = false;
    public static LazyValue<Void, Boolean> sIsSettingEnableScreenScene = null;
    public static boolean sOptimizeForLowMemory = false;
    public static float sPixelDensity = -1.0f;
    public static ContentObserver sSettingObserver = null;
    public static String[] sSupportScreenSceneDevices = null;
    public static int value_of_force_black_v1 = -1;
    public static int value_of_force_black_v2 = -1;

    static {
        String[] strArr = {"venus", "star", "mars", "cetus", "renoir", "odin", "argo", "vili", "zeus", "cupid", "ingres", "zizhan", "thor", "loki", "diting", "mayfly"};
        sSupportScreenSceneDevices = strArr;
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 24) {
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (strArr[i].equalsIgnoreCase(Build.DEVICE)) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            sDeviceSupportScreenScene = z;
        } else {
            sDeviceSupportScreenScene = false;
        }
        sIsSettingEnableScreenScene = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.ScreenUtils.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public Boolean mo1272onInit(Void r6) {
                ContentResolver contentResolver = StaticContext.sGetAndroidContext().getContentResolver();
                String string = Settings.Global.getString(contentResolver, "screen_enhance_engine_gallery_ai_mode_status");
                if (string != null) {
                    if (!ScreenUtils.sHasRegistered) {
                        contentResolver.registerContentObserver(Settings.Global.getUriFor("screen_enhance_engine_gallery_ai_mode_status"), false, ScreenUtils.sSettingObserver);
                        boolean unused = ScreenUtils.sHasRegistered = true;
                    }
                    boolean equals = string.equals("true");
                    DefaultLogger.d("ScreenUtils", "ScreenSceneClassification sIsSettingEnableScreenScene %s", Boolean.valueOf(equals));
                    return Boolean.valueOf(equals);
                }
                DefaultLogger.d("ScreenUtils", "ScreenSceneClassification sIsSettingEnableScreenScene false");
                return Boolean.FALSE;
            }
        };
        sSettingObserver = new ContentObserver(ThreadManager.getMainHandler()) { // from class: com.miui.gallery.util.ScreenUtils.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z2) {
                DefaultLogger.d("ScreenUtils", "ScreenSceneClassification setting changed %s", Boolean.valueOf(z2));
                super.onChange(z2);
                ScreenUtils.sIsSettingEnableScreenScene.reset();
            }
        };
        DEVICES_DISABLE_CUTOUT = new HashSet(Arrays.asList("merlin"));
    }

    public static void initialize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        sPixelDensity = displayMetrics.density;
        sOptimizeForLowMemory = (Runtime.getRuntime().maxMemory() >> 20) <= 64 && displayMetrics.widthPixels * displayMetrics.heightPixels >= 921600;
    }

    public static float dpToPixel(float f) {
        if (sPixelDensity < 0.0f) {
            initialize(StaticContext.sGetAndroidContext());
        }
        return sPixelDensity * f;
    }

    public static boolean needOptimizeForLowMemory() {
        return sOptimizeForLowMemory;
    }

    public static int getScreenVertical(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenHorizontal(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHorizontalInDp() {
        DisplayMetrics displayMetrics = StaticContext.sGetAndroidContext().getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    public static int getScreenVerticalInDp() {
        DisplayMetrics displayMetrics = StaticContext.sGetAndroidContext().getResources().getDisplayMetrics();
        return (int) (displayMetrics.heightPixels / displayMetrics.density);
    }

    public static int getCurScreenWidth() {
        return StaticContext.sGetAndroidContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getCurDisplayWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) activity.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getCurDisplayHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) activity.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getPhysicalScreenSmallWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getPhysicalScreenLargeWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getCurDisplayFullScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) activity.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = StaticContext.sGetAndroidContext().getResources().getDisplayMetrics();
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = StaticContext.sGetAndroidContext().getResources().getDisplayMetrics();
        return Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getFullScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getExactScreenHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        if (ViewCompat.getSystemWindowInsetBottom(activity.getWindow().getDecorView()) > 0) {
            int screenHeight = getScreenHeight();
            return Build.VERSION.SDK_INT >= 28 ? screenHeight + WindowCompat.getTopNotchHeight(activity) : screenHeight;
        }
        return getFullScreenHeight(activity);
    }

    public static int getExactScreenVertical(Activity activity) {
        if (activity == null) {
            return 0;
        }
        int screenVertical = getScreenVertical(activity);
        return (ViewCompat.getSystemWindowInsetBottom(activity.getWindow().getDecorView()) <= 0 || Build.VERSION.SDK_INT < 28) ? screenVertical : screenVertical + WindowCompat.getTopNotchHeight(activity);
    }

    public static int getFullScreenVertical(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getNavBarHeight(Context context) {
        Resources resources;
        int identifier;
        if ((!BaseScreenUtil.isFullScreenGestureNav(context) || isEnableGestureLine(context)) && (identifier = (resources = context.getResources()).getIdentifier("navigation_bar_height", "dimen", "android")) > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static int getFullScreenNavBarHeight(Context context) {
        Resources resources;
        int identifier;
        if (BaseScreenUtil.isFullScreenGestureNav(context) && isEnableGestureLine(context) && (identifier = (resources = context.getResources()).getIdentifier("navigation_bar_height", "dimen", "android")) != 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static boolean isRtl(Context context) {
        return context != null && context.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public static boolean isEnableGestureLine(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "hide_gesture_line", 1) == 0;
    }

    public static void noteForceBlackState(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        value_of_force_black_v1 = Settings.Global.getInt(contentResolver, "force_black", 0);
        value_of_force_black_v2 = Settings.Global.getInt(contentResolver, "force_black_v2", 0);
    }

    public static boolean isForceBlack(Context context) {
        if (value_of_force_black_v1 == -1 || value_of_force_black_v2 == -1) {
            noteForceBlackState(context);
        }
        return value_of_force_black_v1 == 1 || value_of_force_black_v2 == 1;
    }

    public static boolean isOnTheCutoutBlacklist(Context context) {
        return DEVICES_DISABLE_CUTOUT.contains(Build.DEVICE);
    }

    public static int checkCutoutLimit(Context context) {
        if (context == null || !MiuiSdkCompat.isSupportCutoutModeShortEdges(context) || !isOnTheCutoutBlacklist(context)) {
            return MiuiSdkCompat.getMIUISdkLevel(context);
        }
        return 0;
    }

    public static boolean isUseScreenSceneMode() {
        return isScreenSceneEnableBySystem();
    }

    public static boolean isScreenSceneEnableBySystem() {
        return sDeviceSupportScreenScene && sIsSettingEnableScreenScene.get(null).booleanValue();
    }

    public static boolean isDeviceSupportAIMode() {
        return sDeviceSupportScreenScene;
    }
}
