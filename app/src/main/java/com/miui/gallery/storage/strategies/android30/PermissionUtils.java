package com.miui.gallery.storage.strategies.android30;

import android.app.AppOpsManager;
import android.content.Context;
import com.miui.gallery.util.LazyValue;

/* loaded from: classes2.dex */
public class PermissionUtils {
    public static final LazyValue<AppInfo, Boolean> CACHED_WRITE_IMAGE_VIDEO_APP_OPS = new LazyValue<AppInfo, Boolean>() { // from class: com.miui.gallery.storage.strategies.android30.PermissionUtils.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(AppInfo appInfo) {
            boolean z;
            try {
                if (!PermissionUtils.checkAppOp(appInfo.context, "android:write_media_images", appInfo.uid, appInfo.packageName) && !PermissionUtils.checkAppOp(appInfo.context, "android:write_media_video", appInfo.uid, appInfo.packageName)) {
                    z = false;
                    return Boolean.valueOf(z);
                }
                z = true;
                return Boolean.valueOf(z);
            } catch (NoSuchMethodError unused) {
                return Boolean.FALSE;
            }
        }
    };

    public static boolean checkWriteImagesOrVideoAppOps(Context context, int i, String str) {
        return CACHED_WRITE_IMAGE_VIDEO_APP_OPS.get(new AppInfo(context, i, str)).booleanValue();
    }

    public static boolean checkAppOp(Context context, String str, int i, String str2) {
        int noteOpNoThrow = ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteOpNoThrow(str, i, str2, null, null);
        if (noteOpNoThrow != 0) {
            if (noteOpNoThrow == 1 || noteOpNoThrow == 2 || noteOpNoThrow == 3) {
                return false;
            }
            throw new IllegalStateException(str + " has unknown mode " + noteOpNoThrow);
        }
        return true;
    }

    /* loaded from: classes2.dex */
    public static class AppInfo {
        public Context context;
        public String packageName;
        public int uid;

        public AppInfo(Context context, int i, String str) {
            this.context = context;
            this.uid = i;
            this.packageName = str;
        }
    }
}
