package com.miui.mediaeditor.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;

/* loaded from: classes3.dex */
public class MediaEditorUtils {
    public static boolean isMediaEditorAvailable() {
        boolean isPackageInstalled = BaseMiscUtil.isPackageInstalled("com.miui.mediaeditor");
        boolean isAllowedUseMediaEditor = isAllowedUseMediaEditor();
        DefaultLogger.d("MediaEditorUtils", "isMediaEditorInstalled -> %b , isAllowedUseMediaEditor -> %b", Boolean.valueOf(isPackageInstalled), Boolean.valueOf(isAllowedUseMediaEditor));
        return isPackageInstalled && isAllowedUseMediaEditor;
    }

    public static boolean isAllowedUseMediaEditor() {
        return !BaseBuildUtil.isPad() || isMediaEditorSupportPad();
    }

    public static boolean isMediaEditorSupportPad() {
        return getMediaEditorApiForGalleryVersionCode() >= 3;
    }

    public static boolean isMediaEditorSupportVideoPostIn8450() {
        return getMediaEditorApiForGalleryVersionCode() >= 4;
    }

    public static boolean isMediaEditorSupportSecretAlbum() {
        return getMediaEditorApiForGalleryVersionCode() >= 5;
    }

    public static int getMediaEditorApiForGalleryVersionCode() {
        Bundle bundle;
        int i = 0;
        try {
            ApplicationInfo applicationInfo = StaticContext.sGetAndroidContext().getPackageManager().getApplicationInfo("com.miui.mediaeditor", 128);
            if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                i = bundle.getInt("mediaeditor_api_for_gallery_version_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultLogger.d("MediaEditorUtils", "mediaeditor_api_for_gallery_version_code is %d", Integer.valueOf(i));
        return i;
    }

    public static boolean isSecurityCenterSupportMediaEditor(Context context) {
        if (context == null) {
            return false;
        }
        if (Rom.IS_INTERNATIONAL) {
            return true;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.miui.securitycenter", 0);
            if (packageInfo != null) {
                DefaultLogger.d("MediaEditorUtils", "%s versionName: %s", "com.miui.securitycenter", packageInfo.versionName);
                DefaultLogger.d("MediaEditorUtils", "%s versionCode: %d", "com.miui.securitycenter", Integer.valueOf(packageInfo.versionCode));
                if (Rom.IS_STABLE) {
                    return packageInfo.versionCode >= (BaseBuildUtil.isPad() ? 30000603 : 30000541);
                } else if (Rom.IS_DEV) {
                    return packageInfo.versionCode >= (BaseBuildUtil.isPad() ? 40000603 : 40000519);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isMiShareSupportMediaEditor(Context context) {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.miui.mishare.connectivity", 0);
            if (packageInfo != null) {
                DefaultLogger.d("MediaEditorUtils", "%s versionName: %s", "com.miui.mishare.connectivity", packageInfo.versionName);
                DefaultLogger.d("MediaEditorUtils", "%s versionCode: %d", "com.miui.mishare.connectivity", Integer.valueOf(packageInfo.versionCode));
                return packageInfo.versionCode >= 45;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
