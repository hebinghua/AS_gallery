package com.miui.gallery.gallerywidget.ui.editor;

import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public interface WidgetEditorContract$IWidgetEditorView {
    void loadDataFail();

    void loadDataSuccess();

    void loadPictureFailed();

    void loadPictureSuccess();

    void saveFailed();

    void saveSuccess();

    void setPreviewBitmap(Bitmap bitmap);
}
