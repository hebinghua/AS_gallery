package com.miui.gallery.util;

import android.graphics.BitmapFactory;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class HeifUtil {
    public static void releaseMemoryHeap() {
        InputStream inputStream;
        Throwable th;
        if (!BaseGalleryPreferences.Debug.isHookHeifDecoder()) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            try {
                inputStream = StaticContext.sGetAndroidContext().getAssets().open("hack.HEIC");
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                if (inputStream != null) {
                    inputStream.close();
                }
                DefaultLogger.d("HeifUtil", "hack heif decoder cost %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            } catch (Throwable th3) {
                th = th3;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            inputStream = null;
            th = th4;
        }
    }
}
