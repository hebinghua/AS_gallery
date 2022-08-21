package com.miui.gallery.editor.photo.screen.home;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen;
import com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView;
import com.miui.gallery.editor.photo.screen.base.IScreenOperation;
import com.miui.gallery.editor.photo.screen.core.ScreenRenderData;

/* loaded from: classes2.dex */
public interface IScreenEditorController {
    void checkTextEditor(boolean z);

    void doRevert();

    void doRevoke();

    void export();

    <T extends IScreenOperation> T getScreenOperation(Class<T> cls);

    boolean isModified();

    boolean isModifiedBaseLast();

    ScreenRenderData onExport();

    boolean setCurrentScreenEditor(int i);

    void setDoodlePen(DoodlePen doodlePen);

    void setLongCrop(boolean z);

    void setLongCropEntry(LongScreenshotCropEditorView.Entry entry);

    void setOnCropStatusChangeListener(OnScreenCropStatusChangeListener onScreenCropStatusChangeListener);

    void setOperationUpdateListener(OperationUpdateListener operationUpdateListener);

    void setPreviewBitmap(Bitmap bitmap);

    void startScreenThumbnailAnimate(ThumbnailAnimatorCallback thumbnailAnimatorCallback);
}
