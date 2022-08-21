package com.miui.gallery.collage;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class CollageUtils {
    public static final String[] sSupportImageMimeType = {"image/jpeg", "image/jpg", "image/png", "image/x-ms-bmp", "image/vnd.wap.wbmp", "image/heic", "image/webp", "image/gif", "image/heif", "image/x-adobe-dng"};

    public static Drawable getDrawableByAssets(Resources resources, String str) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        Drawable drawable = null;
        try {
            inputStream = resources.getAssets().open(str);
            try {
                try {
                    drawable = Drawable.createFromStream(inputStream, null);
                } catch (IOException unused) {
                    DefaultLogger.e("CollageUtils", "load poster element img fail ! path %s", str);
                    IoUtils.close("CollageUtils", inputStream);
                    return drawable;
                }
            } catch (Throwable th) {
                th = th;
                inputStream2 = inputStream;
                IoUtils.close("CollageUtils", inputStream2);
                throw th;
            }
        } catch (IOException unused2) {
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.close("CollageUtils", inputStream2);
            throw th;
        }
        IoUtils.close("CollageUtils", inputStream);
        return drawable;
    }

    public static String loadResourceFileString(AssetManager assetManager, String str) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        String str2 = null;
        try {
            inputStream = assetManager.open(str);
            try {
                try {
                    str2 = IoUtils.readInputStreamToString("CollageUtils", inputStream);
                } catch (IOException e) {
                    e = e;
                    DefaultLogger.e("CollageUtils", e);
                    IoUtils.close(inputStream);
                    return str2;
                }
            } catch (Throwable th) {
                th = th;
                inputStream2 = inputStream;
                IoUtils.close(inputStream2);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.close(inputStream2);
            throw th;
        }
        IoUtils.close(inputStream);
        return str2;
    }

    public static Gson generateCustomGson() {
        return new GsonBuilder().create();
    }

    public static boolean isMimeTypeSupport(String str) {
        for (String str2 : sSupportImageMimeType) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
