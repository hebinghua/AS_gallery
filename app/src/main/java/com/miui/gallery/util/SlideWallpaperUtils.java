package com.miui.gallery.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes2.dex */
public class SlideWallpaperUtils {
    public static void setSlideWallpaper(Context context, Uri uri, String str) {
        Intent intent;
        try {
            if (!supported(context)) {
                intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.mfashiongallery.emag&back=true&ref=MiuiGallery&startDownload=true"));
            } else {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                Uri translateToContent = GalleryOpenProvider.translateToContent(uri.getPath());
                intent2.setData(Uri.parse("mifg://fashiongallery/addcw?from=MiuiGallery").buildUpon().appendQueryParameter(CallMethod.ARG_URI, translateToContent.toString()).appendQueryParameter("sha1", str).build());
                context.grantUriPermission("com.mfashiongallery.emag", translateToContent, 1);
                intent = intent2;
            }
            context.startActivity(intent);
            SamplingStatHelper.recordCountEvent("photo", "set_as_slide_wallpaper");
        } catch (ActivityNotFoundException e) {
            DefaultLogger.w("SlideWallpaperUtils", e);
            SamplingStatHelper.recordCountEvent("photo", "set_slide_wallpaper_fail");
        }
    }

    public static boolean supported(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("mifg://fashiongallery/addcw?uri=/DCIM/Camera/IMG.jpg&from=test"));
        return intent.resolveActivityInfo(context.getPackageManager(), 0) != null;
    }
}
