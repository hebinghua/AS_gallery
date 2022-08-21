package com.miui.gallery.magic.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.miui.gallery.magic.widget.portrait.PortraitEditView;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;

/* loaded from: classes2.dex */
public class MagicPortraitEditorView extends PortraitEditView {
    public Matrix mMatrix;
    public OnMatrixUpdateListener mMatrixUpdateListener;

    /* loaded from: classes2.dex */
    public interface OnMatrixUpdateListener {
        void onUpdate(Matrix matrix, float f, float f2);
    }

    public MagicPortraitEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMatrix = new Matrix();
        init();
    }

    public void init() {
        setStrokeEnable(false);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override // com.miui.gallery.magic.widget.portrait.PortraitEditView, com.miui.gallery.widget.imageview.BitmapGestureView
    public void onBitmapMatrixChanged() {
        super.onBitmapMatrixChanged();
        if (this.mMatrixUpdateListener != null) {
            BitmapGestureParamsHolder bitmapGestureParamsHolder = this.mBitmapGestureParamsHolder;
            RectF rectF = bitmapGestureParamsHolder.mDisplayInitRect;
            RectF rectF2 = bitmapGestureParamsHolder.mBitmapDisplayRect;
            this.mMatrix.reset();
            this.mMatrix.setTranslate((rectF.width() - rectF2.width()) / 2.0f, (rectF.height() - rectF2.height()) / 2.0f);
            this.mMatrixUpdateListener.onUpdate(this.mMatrix, rectF2.width(), rectF2.height());
        }
    }

    public void setMatrixUpdateListener(OnMatrixUpdateListener onMatrixUpdateListener) {
        this.mMatrixUpdateListener = onMatrixUpdateListener;
    }
}
