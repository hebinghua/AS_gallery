package com.miui.gallery.editor.photo.screen.longcrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView;
import com.miui.gallery.editor.photo.screen.core.ScreenDrawEntry;

/* loaded from: classes2.dex */
public class ScreenLongCropEditView extends LongScreenshotCropEditorView {
    public ScreenDrawEntry mScreenDrawEntry;

    public ScreenLongCropEditView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView
    public void drawChild(Canvas canvas, RectF rectF) {
        ScreenDrawEntry screenDrawEntry = this.mScreenDrawEntry;
        if (screenDrawEntry != null) {
            screenDrawEntry.draw(canvas, rectF);
        }
    }

    public void setScreenDrawEntry(ScreenDrawEntry screenDrawEntry) {
        this.mScreenDrawEntry = screenDrawEntry;
    }
}
