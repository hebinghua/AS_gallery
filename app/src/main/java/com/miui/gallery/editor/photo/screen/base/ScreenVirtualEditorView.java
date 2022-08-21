package com.miui.gallery.editor.photo.screen.base;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.miui.gallery.editor.photo.core.common.model.BaseDrawNode;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorView;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;

/* loaded from: classes2.dex */
public abstract class ScreenVirtualEditorView {
    public Context mContext;
    public ScreenEditorView mEditorView;

    public ScreenVirtualEditorView(ScreenEditorView screenEditorView) {
        this.mEditorView = screenEditorView;
        this.mContext = screenEditorView.getContext();
    }

    public final void invalidate() {
        this.mEditorView.invalidate();
    }

    public final Matrix getImageMatrix() {
        return getBitmapGestureParamsHolder().mCanvasMatrix;
    }

    public BitmapGestureParamsHolder getBitmapGestureParamsHolder() {
        return this.mEditorView.getBitmapGestureParamsHolder();
    }

    public void setImageMatrix(Matrix matrix) {
        getBitmapGestureParamsHolder().mCanvasMatrix.set(matrix);
        performCanvasMatrixChange();
    }

    public final RectF getBounds() {
        return getBitmapGestureParamsHolder().mDisplayInitRect;
    }

    public final RectF getImageBounds() {
        return getBitmapGestureParamsHolder().mBitmapRect;
    }

    public final RectF getImageDisplayRect() {
        return getBitmapGestureParamsHolder().mBitmapDisplayRect;
    }

    public final RectF getBitmapDisplayInitRect() {
        return getBitmapGestureParamsHolder().mBitmapDisplayInitRect;
    }

    public final void performCanvasMatrixChange() {
        getBitmapGestureParamsHolder().performCanvasMatrixChange();
    }

    public void addDrawNode(BaseDrawNode baseDrawNode) {
        this.mEditorView.addDrawNode(baseDrawNode);
    }

    public void removeDrawNode(BaseDrawNode baseDrawNode) {
        this.mEditorView.removeDrawNode(baseDrawNode);
    }
}
