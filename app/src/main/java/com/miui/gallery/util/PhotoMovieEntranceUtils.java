package com.miui.gallery.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.ui.activity.MovieActivity;
import com.miui.mediaeditor.api.MediaEditorApiHelper;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.xiaomi.stat.c.b;

/* loaded from: classes2.dex */
public class PhotoMovieEntranceUtils {
    public static final String[] sBlackList;
    public static boolean sLoaded;

    static {
        String[] strArr = {"dandelion", "angelica", "angelicain", "cattail", "angelican", "lime", "citrus", "lemon", "pomelo", "mocha", "latte", "cappu", "clover", "lilac", "A101XM", "XIG02", "iris"};
        sBlackList = strArr;
        sLoaded = true;
        for (String str : strArr) {
            if (Build.DEVICE.equals(str)) {
                sLoaded = false;
                return;
            }
        }
    }

    public static boolean isPhotoMovieUseMiSDK() {
        return isDeviceSupportPhotoMovie() && MovieConfig.isUserXmSdk();
    }

    public static boolean isDeviceSupportPhotoMovie() {
        return Build.VERSION.SDK_INT >= 21 && !BuildUtil.isBlackShark() && sLoaded;
    }

    public static void startPicker(Context context) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.putExtra("pick-upper-bound", 20);
        intent.putExtra("pick-lower-bound", 3);
        intent.putExtra("extra_filter_media_type", new String[]{"image/x-adobe-dng"});
        Intent intent2 = new Intent();
        if (MediaEditorUtils.isMediaEditorAvailable() && isPhotoMovieUseMiSDK() && MediaEditorApiHelper.isDeviceSupportPhotoMovie()) {
            if (MediaEditorApiHelper.isPhotoMovieAvailable()) {
                intent2.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.movie.ui.activity.MovieActivity"));
            } else {
                intent2.putExtra("loadType", "photoMovie");
                intent2.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.mediaeditor.activity.DownloadLibraryActivity"));
            }
        } else {
            intent2 = new Intent(context, MovieActivity.class);
        }
        intent.putExtra("pick_intent", intent2);
        intent.putExtra("pick_close_type", 3);
        intent.putExtra("extra_from_type", b.j);
        intent.setPackage("com.miui.gallery");
        context.startActivity(intent);
    }
}
