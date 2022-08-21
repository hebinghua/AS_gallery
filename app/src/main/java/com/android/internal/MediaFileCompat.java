package com.android.internal;

import android.media.MediaFile;

/* loaded from: classes.dex */
public class MediaFileCompat {
    public static String getMimeTypeForFile(String str) {
        return MediaFile.getMimeTypeForFile(str);
    }
}
