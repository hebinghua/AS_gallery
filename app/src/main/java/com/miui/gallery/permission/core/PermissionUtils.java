package com.miui.gallery.permission.core;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class PermissionUtils {
    public static final LazyValue<Context, Boolean> CUSTOM_REQUEST_PERMISSION = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.permission.core.PermissionUtils.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            if (Rom.IS_INTERNATIONAL) {
                return Boolean.FALSE;
            }
            if (!Rom.IS_MIUI) {
                return Boolean.FALSE;
            }
            try {
                return Boolean.valueOf(context.getPackageManager().getPackageInfo("com.lbe.security.miui", 16384).versionCode >= 111);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    };
    public static final LazyValue<Context, Boolean> CAN_ACCESS_STORAGE = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.permission.core.PermissionUtils.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            boolean z = true;
            if (Build.VERSION.SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager() || !PermissionUtils.checkStoragePermission(context) || !AppOpUtils.isStorageAllow(context)) {
                    z = false;
                }
                return Boolean.valueOf(z);
            }
            if (!PermissionUtils.checkStoragePermission(context) || !AppOpUtils.isStorageAllow(context)) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
    };

    public static int getPermissionState(Activity activity, Permission permission) {
        if (activity == null) {
            return -1;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString("permissionName", permission.mName);
            Bundle call = activity.getContentResolver().call(Uri.parse("content://com.lbe.security.miui.autostartmgr"), "getPermissionState", (String) null, bundle);
            if (call != null) {
                return call.getInt("flag");
            }
        } catch (Exception e) {
            DefaultLogger.e("PermissionUtils", e);
        }
        return checkPermission(activity, permission.mName) ? 0 : -1;
    }

    public static boolean canAccessStorage(Context context) {
        return canAccessStorage(context, false);
    }

    public static boolean canAccessStorage(Context context, boolean z) {
        if (z) {
            CAN_ACCESS_STORAGE.reset();
        }
        return CAN_ACCESS_STORAGE.get(context).booleanValue();
    }

    public static boolean checkPermission(Activity activity, String str) {
        return !supportRuntimePermissionCheck() || ContextCompat.checkSelfPermission(activity, str) == 0;
    }

    public static boolean checkPermission(Context context, String str) {
        return !supportRuntimePermissionCheck() || ContextCompat.checkSelfPermission(context, str) == 0;
    }

    public static boolean checkLocationPermissions(Activity activity) {
        if (supportRuntimePermissionCheck()) {
            return checkPermission(activity, "android.permission.ACCESS_COARSE_LOCATION") || checkPermission(activity, "android.permission.ACCESS_FINE_LOCATION");
        }
        return false;
    }

    public static boolean checkStoragePermission(Context context) {
        if (!checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            DefaultLogger.w("PermissionUtils", "Can't access external storage, relate permission is not granted");
            return false;
        }
        return true;
    }

    public static void requestPermissions(Fragment fragment, Permission[] permissionArr, int i) {
        String[] strArr;
        if (supportRuntimePermissionCheck()) {
            int i2 = 0;
            if (!CUSTOM_REQUEST_PERMISSION.get(fragment.getActivity()).booleanValue()) {
                strArr = new String[permissionArr.length];
                while (i2 < permissionArr.length) {
                    strArr[i2] = permissionArr[i2].mName;
                    i2++;
                }
            } else {
                strArr = new String[permissionArr.length * 2];
                while (i2 < permissionArr.length) {
                    int i3 = i2 * 2;
                    strArr[i3] = permissionArr[i2].mName;
                    strArr[i3 + 1] = permissionArr[i2].mDesc;
                    i2++;
                }
            }
            fragment.requestPermissions(strArr, i);
        }
    }

    public static boolean supportRuntimePermissionCheck() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static String[] getUngrantedPermissions(Activity activity, String[] strArr) {
        if (!supportRuntimePermissionCheck() || strArr == null || strArr.length <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str) && !checkPermission(activity, str)) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            return new String[0];
        }
        String[] strArr2 = new String[arrayList.size()];
        arrayList.toArray(strArr2);
        return strArr2;
    }
}
