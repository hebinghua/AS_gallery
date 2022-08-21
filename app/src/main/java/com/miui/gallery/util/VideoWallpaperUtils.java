package com.miui.gallery.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes2.dex */
public class VideoWallpaperUtils {
    public static final String[] sBlackList;
    public static boolean sLoaded;
    public static final boolean sSupported;

    static {
        String[] strArr = {"mocha", "latte", "cappu", "clover", "veux"};
        sBlackList = strArr;
        boolean z = true;
        sLoaded = true;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            if (Build.DEVICE.equals(strArr[i])) {
                sLoaded = false;
                break;
            }
            i++;
        }
        if (!Rom.IS_MIUI || !sLoaded || ((MiscUtil.getAppVersionCode("com.android.thememanager") <= 500 && (Rom.IS_INTERNATIONAL || MiscUtil.getAppVersionCode("com.android.thememanager") < 490)) || ((BuildUtil.isMiuiLite() && BaseBuildUtil.isNewDevice()) || disableVideoWallpaper()))) {
            z = false;
        }
        sSupported = z;
    }

    public static void setVideoWallpaper(Context context, String str) {
        try {
            Intent intent = new Intent("miui.intent.action.START_VIDEO_DETAIL");
            if (BuildUtil.isFoldingDevice()) {
                intent.setComponent(new ComponentName("com.android.thememanager", "com.android.thememanager.detail.video.view.activity.VideoDetailActivity"));
            }
            intent.addCategory("android.intent.category.DEFAULT");
            intent.putExtra(nexExportFormat.TAG_FORMAT_PATH, str);
            intent.putExtra("miref", "com.miui.gallery");
            context.startActivity(intent);
            SamplingStatHelper.recordCountEvent("video", "set_as_video_wallpaper");
        } catch (ActivityNotFoundException e) {
            DefaultLogger.w("VideoWallpaperUtils", e);
            SamplingStatHelper.recordCountEvent("video", "set_slide_wallpaper_fail");
        }
    }

    public static boolean disableVideoWallpaper() {
        try {
            Resources resourcesForApplication = StaticContext.sGetAndroidContext().getPackageManager().getResourcesForApplication("com.android.thememanager");
            int identifier = resourcesForApplication.getIdentifier("disableVideoWallpaper", "boolean", "com.android.thememanager");
            if (identifier == 0) {
                return false;
            }
            return resourcesForApplication.getBoolean(identifier);
        } catch (Exception e) {
            DefaultLogger.e("VideoWallpaperUtils", e);
            return false;
        }
    }

    public static boolean isSupported() {
        return sSupported;
    }
}
