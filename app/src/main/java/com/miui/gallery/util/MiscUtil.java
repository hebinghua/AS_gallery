package com.miui.gallery.util;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class MiscUtil extends BaseMiscUtil {
    public static final Map<String, Integer> APP_VERSION_CODE_CACHE = new Hashtable();

    public static boolean isKeyGuardLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
        return keyguardManager != null && keyguardManager.isKeyguardLocked();
    }

    public static String serverType2Text(int i) {
        if (i != -1) {
            if (i == 0) {
                return "group";
            }
            if (i == 1) {
                return "image";
            }
            if (i == 2) {
                return "video";
            }
            return "unknown:" + i;
        }
        return "invalid";
    }

    public static long[] ListToArray(List<Long> list) {
        if (list == null) {
            return null;
        }
        int size = list.size();
        long[] jArr = new long[size];
        for (int i = 0; i < size; i++) {
            Long l = list.get(i);
            jArr[i] = l == null ? 0L : l.longValue();
        }
        return jArr;
    }

    public static List<Long> arrayToList(long[] jArr) {
        ArrayList arrayList = new ArrayList(jArr.length);
        for (long j : jArr) {
            arrayList.add(Long.valueOf(j));
        }
        return arrayList;
    }

    public static long[] listToArray(List<Long> list) {
        if (list == null) {
            return null;
        }
        long[] jArr = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            jArr[i] = list.get(i).longValue();
        }
        return jArr;
    }

    public static String[] copyStringArray(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        String[] strArr2 = new String[strArr.length];
        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        return strArr2;
    }

    public static int getAppVersionCode() {
        return getAppVersionCode("com.miui.gallery", true);
    }

    public static int getAppVersionCode(String str) {
        return getAppVersionCode(str, false);
    }

    public static int getAppVersionCode(String str, boolean z) {
        int i = -1;
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        Map<String, Integer> map = APP_VERSION_CODE_CACHE;
        if (!map.containsKey(str) || !z) {
            try {
                i = GalleryApp.sGetAndroidContext().getPackageManager().getPackageInfo(str, 16384).versionCode;
                map.put(str, Integer.valueOf(i));
            } catch (PackageManager.NameNotFoundException unused) {
                return i;
            }
        }
        return map.get(str).intValue();
    }

    public static Object getAppLaunchIntent(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return GalleryApp.sGetAndroidContext().getPackageManager().getLaunchIntentForPackage(str);
    }

    public static List<ResolveInfo> queryActivitiesWithMeta(Intent intent) {
        if (intent == null) {
            return null;
        }
        return GalleryApp.sGetAndroidContext().getPackageManager().queryIntentActivities(intent, 128);
    }

    public static boolean isAppProcessInForeground() {
        return ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
    }

    @Deprecated
    public static boolean isAppProcessInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> list = null;
        try {
            list = activityManager.getRunningAppProcesses();
        } catch (Exception e) {
            DefaultLogger.e("MiscUtil", e);
        }
        if (!BaseMiscUtil.isValid(list)) {
            return false;
        }
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            if (runningAppProcessInfo.pid == myPid) {
                return runningAppProcessInfo.importance == 100;
            }
        }
        return false;
    }

    public static String getApplicationMetaData(String str, String str2) {
        Bundle bundle;
        try {
            ApplicationInfo applicationInfo = GalleryApp.sGetAndroidContext().getPackageManager().getApplicationInfo(str, 128);
            if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                return bundle.getString(str2);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getSimOperator() {
        return ((TelephonyManager) GalleryApp.sGetAndroidContext().getSystemService("phone")).getSimOperator();
    }

    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        return BaseMiscUtil.getDimensionPixelOffset(context.getResources(), "status_bar_height", "dimen", "android");
    }

    public static int getDefaultSplitActionBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.action_button_default_height);
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }

    public static void setRecyclerViewScrollToBottomListener(RecyclerView recyclerView, final Runnable runnable) {
        if (recyclerView == null || runnable == null) {
            return;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.util.MiscUtil.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView2, int i, int i2) {
                View childAt = recyclerView2.getChildAt(recyclerView2.getChildCount() - 1);
                if (childAt == null) {
                    return;
                }
                RecyclerView.Adapter adapter = recyclerView2.getAdapter();
                int itemCount = adapter == null ? 0 : adapter.getItemCount();
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
                if (layoutParams == null || layoutParams.getViewAdapterPosition() != itemCount - 1) {
                    return;
                }
                runnable.run();
            }
        });
    }

    public static boolean isNightMode(Context context) {
        return context != null && (context.getResources().getConfiguration().uiMode & 48) == 32;
    }

    public static int[] parseResolution(String str) {
        int indexOf;
        if (str == null || (indexOf = str.indexOf(120)) == -1) {
            return null;
        }
        try {
            return new int[]{Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(indexOf + 1))};
        } catch (Exception e) {
            DefaultLogger.w("MiscUtil", e);
            return null;
        }
    }

    public static int getNotchHeight(Context context) {
        try {
            int identifier = context.getResources().getIdentifier("edge_suppression_size", "dimen", "android");
            if (identifier <= 0) {
                return 0;
            }
            return context.getResources().getDimensionPixelSize(identifier);
        } catch (Resources.NotFoundException unused) {
            return 0;
        }
    }

    public static boolean isLandMode(Context context) {
        if (context == null) {
            return false;
        }
        try {
            int rotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
            return rotation == 1 || rotation == 3;
        } catch (Exception e) {
            DefaultLogger.e("MiscUtil", e);
            return context.getResources().getConfiguration().orientation == 2;
        }
    }

    public static boolean isLandModeAndSupportVersion(Context context) {
        return isLandMode(context) && VideoPlayerCompat.isVideoPlayerSupportShortLandscape();
    }

    public static boolean isUsedCutoutModeShortEdges(Context context) {
        return miui.gallery.support.MiuiSdkCompat.isSupportCutoutModeShortEdges(context) && VideoPlayerCompat.isVideoPlayerSupportCutoutModeShortEdges() && !ScreenUtils.isForceBlack(context) && !ScreenUtils.isOnTheCutoutBlacklist(context);
    }
}
