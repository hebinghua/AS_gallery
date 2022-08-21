package com.miui.gallery;

import com.miui.gallery.sdk.download.DownloadType;

/* loaded from: classes.dex */
public class Config$ImageDownload {
    public static boolean requireWLAN(DownloadType downloadType) {
        return downloadType == DownloadType.ORIGIN || downloadType == DownloadType.ORIGIN_BATCH || downloadType == DownloadType.THUMBNAIL_BATCH;
    }
}
