package com.miui.gallery.movie.ui.listener;

/* loaded from: classes2.dex */
public interface IShareDataCallback {
    void cancelExport();

    void handleShareEvent(String str);

    void resetShareData();

    void updateShareData(boolean z);
}
