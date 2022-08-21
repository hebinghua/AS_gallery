package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;

/* loaded from: classes3.dex */
public class ImmersiveHeaderDrawer implements DrawView {
    public Drawable mBackground;
    public Context mContext;
    public int mViewHeight;
    public int mViewWidth;
    public int mAlpha = 255;
    public Paint mPaint = new Paint();

    public ImmersiveHeaderDrawer(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void setStyle(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R$styleable.ImmersiveHeaderDecoration, R.attr.immersiveHeaderDecorationStyle, i);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            if (index == 0) {
                this.mBackground = obtainStyledAttributes.getDrawable(index);
            } else if (index == 1) {
                this.mViewHeight = (int) obtainStyledAttributes.getDimension(index, 0.0f);
            } else if (index == 2) {
                this.mViewWidth = (int) obtainStyledAttributes.getDimension(index, 0.0f);
            }
        }
        obtainStyledAttributes.recycle();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            if (this.mViewWidth <= 0) {
                this.mViewWidth = drawable.getIntrinsicWidth();
            }
            if (this.mViewHeight <= 0) {
                this.mViewHeight = this.mBackground.getIntrinsicHeight();
            }
        }
        this.mPaint.setAntiAlias(true);
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void draw(Canvas canvas) {
        canvas.save();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setBounds(0, 0, this.mViewWidth, this.mViewHeight);
            this.mBackground.setAlpha(this.mAlpha);
            this.mBackground.draw(canvas);
        }
        canvas.restore();
    }

    public void setViewWidth(int i) {
        this.mViewWidth = i;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public int getViewWidth() {
        return this.mViewWidth;
    }
}
