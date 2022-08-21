package com.miui.gallery.editor.photo.screen.longcrop;

import com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView;
import com.miui.gallery.editor.photo.screen.core.ScreenDrawEntry;

/* loaded from: classes2.dex */
public interface ILongCropEditorController {
    boolean isModifiedBaseLast();

    LongScreenshotCropEditorView.Entry onExport();

    void setScreenDrawEntry(ScreenDrawEntry screenDrawEntry);
}
