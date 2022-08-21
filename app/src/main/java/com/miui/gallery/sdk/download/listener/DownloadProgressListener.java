package com.miui.gallery.sdk.download.listener;

import android.net.Uri;
import com.miui.gallery.sdk.download.DownloadType;

/* loaded from: classes2.dex */
public interface DownloadProgressListener {
    void onDownloadProgress(Uri uri, DownloadType downloadType, long j, long j2);
}
