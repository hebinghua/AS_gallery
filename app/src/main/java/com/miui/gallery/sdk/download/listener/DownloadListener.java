package com.miui.gallery.sdk.download.listener;

import android.net.Uri;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadedItem;

/* loaded from: classes2.dex */
public interface DownloadListener {
    default void onDownloadCancel(Uri uri, DownloadType downloadType) {
    }

    default void onDownloadFail(Uri uri, DownloadType downloadType, DownloadFailReason downloadFailReason) {
    }

    default void onDownloadStarted(Uri uri, DownloadType downloadType) {
    }

    default void onDownloadSuccess(Uri uri, DownloadType downloadType, DownloadedItem downloadedItem) {
    }
}
