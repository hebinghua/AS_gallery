package com.miui.gallery.magic.util;

import android.net.Uri;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class ImageFormatUtils {
    public static final String[] SUPPORT_IMAGE_FORMAT = {"jpg", "png", "jpeg", "bmp", "wbmp", "webp", "heif", "heic"};

    public static boolean isSupportImageFormat(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        int lastIndexOf = lastPathSegment.lastIndexOf(".") + 1;
        int length = lastPathSegment.length();
        if (lastIndexOf >= length) {
            return false;
        }
        return Arrays.asList(SUPPORT_IMAGE_FORMAT).contains(lastPathSegment.substring(lastIndexOf, length).toLowerCase());
    }
}
