package com.miui.gallery.collage.app.layout;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.miui.gallery.R;
import com.miui.gallery.collage.core.layout.LayoutItemModel;
import com.miui.gallery.collage.core.layout.LayoutModel;

/* loaded from: classes.dex */
public class LayoutPreviewDrawable extends Drawable {
    public int mBackgroundColor;
    public LayoutModel mLayoutModel;
    public Paint mPaint = new Paint(1);
    public Path mPath = new Path();

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public LayoutPreviewDrawable(Resources resources) {
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(resources.getColor(R.color.collage_layout_menu_item_paint_color));
        this.mPaint.setStrokeWidth(3.0f);
        this.mBackgroundColor = resources.getColor(R.color.collage_layout_menu_item_background);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawColor(this.mBackgroundColor);
        if (this.mLayoutModel == null) {
            return;
        }
        Rect bounds = getBounds();
        float width = bounds.width();
        float height = bounds.height();
        for (LayoutItemModel layoutItemModel : this.mLayoutModel.items) {
            float[] fArr = layoutItemModel.data;
            this.mPath.reset();
            for (int i = 0; i < fArr.length; i += 2) {
                float f = fArr[i] * width;
                float f2 = fArr[i + 1] * height;
                if (this.mPath.isEmpty()) {
                    this.mPath.moveTo(f, f2);
                } else {
                    this.mPath.lineTo(f, f2);
                }
            }
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }

    public void setLayoutModel(LayoutModel layoutModel) {
        this.mLayoutModel = layoutModel;
        invalidateSelf();
    }
}
