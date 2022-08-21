package com.xiaomi.milab.videosdk.interfaces;

/* loaded from: classes3.dex */
public interface ExportCallback {
    void onExportCancel();

    void onExportFail();

    void onExportProgress(int i);

    void onExportSuccess();
}
