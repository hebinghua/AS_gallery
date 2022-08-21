package com.miui.gallery.editor.photo.app.sky.sdk;

/* loaded from: classes2.dex */
public interface IVideoExporter {

    /* loaded from: classes2.dex */
    public interface Callback {
        void onProgress(int i);
    }

    void cancel();

    int export(String str);

    void release();

    void setCallback(Callback callback);
}
