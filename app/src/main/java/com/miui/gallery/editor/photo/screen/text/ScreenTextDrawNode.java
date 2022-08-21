package com.miui.gallery.editor.photo.screen.text;

import android.graphics.Canvas;
import android.graphics.Matrix;
import com.miui.gallery.editor.photo.core.common.model.BaseDrawNode;
import com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig;

/* loaded from: classes2.dex */
public class ScreenTextDrawNode extends BaseDrawNode {
    public Matrix mDisplayToBitmapMatrix;
    public boolean mIsSaved;
    public ITextDialogConfig textDialogConfig;

    public ScreenTextDrawNode setTextDialogConfig(ITextDialogConfig iTextDialogConfig) {
        this.textDialogConfig = iTextDialogConfig;
        return this;
    }

    public void setDisplayToBitmapMatrix(Matrix matrix) {
        this.mDisplayToBitmapMatrix = new Matrix(matrix);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.concat(this.mDisplayToBitmapMatrix);
        ITextDialogConfig iTextDialogConfig = this.textDialogConfig;
        if (iTextDialogConfig != null) {
            iTextDialogConfig.draw(canvas);
        }
        canvas.restore();
    }

    public boolean isSaved() {
        return this.mIsSaved;
    }

    public void setSaved(boolean z) {
        this.mIsSaved = z;
    }
}
