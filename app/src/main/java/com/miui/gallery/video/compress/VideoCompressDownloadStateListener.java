package com.miui.gallery.video.compress;

/* loaded from: classes2.dex */
public interface VideoCompressDownloadStateListener {
    void onDownloadStart();

    void onDownloading(int i);

    void onFinish(boolean z, int i);
}
