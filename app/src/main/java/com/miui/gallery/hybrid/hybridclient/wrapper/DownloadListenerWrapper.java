package com.miui.gallery.hybrid.hybridclient.wrapper;

import android.webkit.DownloadListener;

/* loaded from: classes2.dex */
public class DownloadListenerWrapper implements DownloadListener {
    public DownloadListener downloadListener;

    public DownloadListenerWrapper(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override // android.webkit.DownloadListener
    public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        DownloadListener downloadListener = this.downloadListener;
        if (downloadListener != null) {
            downloadListener.onDownloadStart(str, str2, str3, str4, j);
        }
    }

    public DownloadListener getWrapped() {
        return this.downloadListener;
    }
}
