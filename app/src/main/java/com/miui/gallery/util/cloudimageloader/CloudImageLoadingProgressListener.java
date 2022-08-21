package com.miui.gallery.util.cloudimageloader;

import android.net.Uri;
import android.view.View;
import com.miui.gallery.sdk.download.DownloadType;

/* loaded from: classes2.dex */
public interface CloudImageLoadingProgressListener {
    void onProgressUpdate(Uri uri, DownloadType downloadType, View view, int i, int i2);
}
