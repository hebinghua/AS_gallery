package com.miui.gallery.util;

import android.content.ContentResolver;
import android.content.Context;
import android.text.TextUtils;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.Config$SecretAlbumConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.request.OCRRequestHelper;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;

/* loaded from: classes2.dex */
public class PhotoOperationsUtil {
    public static String TAG = "PhotoOperationsUtil";
    public static Class sMiuiSettingSystem;

    public static boolean isSupportedOptions(long j, long j2) {
        return (j & j2) != 0;
    }

    public static Class getMiuiSettingSystem() throws ClassNotFoundException {
        Class cls = sMiuiSettingSystem;
        if (cls != null) {
            return cls;
        }
        Class<?> cls2 = Class.forName("android.provider.MiuiSettings$Secure");
        sMiuiSettingSystem = cls2;
        return cls2;
    }

    public static boolean isScreenProjectOn(ContentResolver contentResolver, boolean z) {
        try {
            Class miuiSettingSystem = getMiuiSettingSystem();
            boolean booleanValue = ((Boolean) miuiSettingSystem.getMethod("getBoolean", ContentResolver.class, String.class, Boolean.TYPE).invoke(miuiSettingSystem, contentResolver, "screen_project_in_screening", Boolean.valueOf(z))).booleanValue();
            String str = TAG;
            DefaultLogger.d(str, "isScreenProjectOn value[screen_project_in_screening] = " + booleanValue);
            return booleanValue;
        } catch (Exception e) {
            String str2 = TAG;
            DefaultLogger.i(str2, "Error: isScreenProjectOn value[screen_project_in_screening] : " + e);
            e.printStackTrace();
            return z;
        }
    }

    public static long getVideoSupportedOperations(String str) {
        long j = VideoWallpaperUtils.isSupported() ? 3216516L : 3212420L;
        if (TextUtils.isEmpty(str) || !StorageUtils.isInExternalStorage(GalleryApp.sGetAndroidContext(), str)) {
            return j;
        }
        long j2 = j | 1 | 512;
        return (!ApplicationHelper.isSecretAlbumFeatureOpen() || !Config$SecretAlbumConfig.isVideoSupported()) ? j2 : j2 | 262144;
    }

    public static long getImageSupportedOperations(String str, String str2, double d, double d2) {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        long j = WallpaperUtils.supported(sGetAndroidContext) ? 3367980L : 3367948L;
        if (!Rom.IS_INTERNATIONAL && Rom.IS_MIUI) {
            j |= 2048;
        }
        if (OCRRequestHelper.SUPPORT_OCR.get(sGetAndroidContext).booleanValue()) {
            j |= 4194304;
        }
        if (PicToPdfHelper.isPicToPdfSupportType(str2) && PicToPdfHelper.isPicToPdfSupport()) {
            j |= 16777216;
        }
        if (!TextUtils.isEmpty(str) && StorageUtils.isInExternalStorage(sGetAndroidContext, str)) {
            j |= 1;
            if (!TextUtils.equals(str2, "image/gif") && !DownloadPathHelper.isShareFolderRelativePath(StorageUtils.getRelativePath(sGetAndroidContext, str))) {
                j |= 512;
            }
        }
        if (BaseFileMimeUtil.isGifFromMimeType(str2)) {
            j = j & (-33) & (-2049) & (-4194305);
        }
        if (BitmapUtils.isSupportedByRegionDecoder(str2)) {
            j |= 64;
        }
        if (BitmapUtils.isRotationSupported(str2)) {
            j |= 2;
        }
        if (LocationUtil.isValidateCoordinate(d, d2)) {
            j |= 16;
        }
        if (ApplicationHelper.isSecretAlbumFeatureOpen()) {
            j |= 262144;
        }
        return ExtraPhotoSDK.isDeviceSupportWatermark(sGetAndroidContext) ? j | 33554432 : j;
    }
}
