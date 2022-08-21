package com.miui.gallery.util.cloudimageloader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import com.miui.gallery.sdk.download.DownloadType;

/* loaded from: classes2.dex */
public class CloudImageLoadingListenerAdapter implements CloudImageLoadingListener {
    @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
    public void onLoadingCancelled(Uri uri, DownloadType downloadType, View view) {
    }

    @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
    public void onLoadingComplete(Uri uri, DownloadType downloadType, View view, Bitmap bitmap) {
    }

    @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
    public void onLoadingStarted(Uri uri, DownloadType downloadType, View view) {
    }
}
