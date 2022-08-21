package com.miui.gallery.editor.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.miui.gallery.editor.R$dimen;

/* loaded from: classes2.dex */
public class RoundImageView extends ImageView {
    public float bottomCorner;
    public float corner;
    public float height;
    public float mDefaultCorners;
    public float topCorner;
    public float width;

    public RoundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        float dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.editor_menu_filter_item_corner);
        this.mDefaultCorners = dimensionPixelSize;
        this.corner = dimensionPixelSize;
        this.topCorner = dimensionPixelSize;
        this.bottomCorner = dimensionPixelSize;
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.width = getWidth();
        this.height = getHeight();
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        float f = this.width;
        float f2 = this.corner;
        if (f > f2 && this.height > f2) {
            Path path = new Path();
            path.moveTo(this.topCorner, 0.0f);
            path.lineTo(this.width - this.corner, 0.0f);
            float f3 = this.width;
            path.quadTo(f3, 0.0f, f3, this.topCorner);
            path.lineTo(this.width, this.height - this.bottomCorner);
            float f4 = this.width;
            float f5 = this.height;
            path.quadTo(f4, f5, f4 - this.bottomCorner, f5);
            path.lineTo(this.bottomCorner, this.height);
            float f6 = this.height;
            path.quadTo(0.0f, f6, 0.0f, f6 - this.bottomCorner);
            path.lineTo(0.0f, this.topCorner);
            path.quadTo(0.0f, 0.0f, this.topCorner, 0.0f);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

    public void setCorner(float f) {
        this.corner = f;
        this.topCorner = f;
        this.bottomCorner = f;
        invalidate();
    }

    public void setTopCorner(boolean z) {
        this.topCorner = z ? this.corner : 0.0f;
        invalidate();
    }

    public void setBottomCorner(boolean z) {
        this.bottomCorner = z ? this.corner : 0.0f;
        invalidate();
    }
}
