package com.miui.gallery.vlog.clip.sort;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.miui.gallery.vlog.R$dimen;

/* loaded from: classes2.dex */
public class VlogSortView extends ImageView {
    public int mCorner;
    public boolean mEndCorner;
    public int mPadding;
    public Path mPath;
    public boolean mStartCorner;

    public VlogSortView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mStartCorner = true;
        this.mEndCorner = true;
        init(context);
    }

    public final void init(Context context) {
        this.mPath = new Path();
        this.mCorner = context.getResources().getDimensionPixelSize(R$dimen.vlog_seek_bar_thumbnail_corner);
        this.mPadding = context.getResources().getDimensionPixelSize(R$dimen.vlog_seek_bar_thumbnail_padding);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        int i;
        int width = getWidth();
        int height = getHeight();
        if (this.mStartCorner || this.mEndCorner) {
            this.mPath.reset();
            if (this.mStartCorner) {
                this.mPath.moveTo(this.mPadding + this.mCorner, 0.0f);
                Path path = this.mPath;
                int i2 = this.mPadding;
                path.quadTo(i2, 0.0f, i2, this.mCorner);
                this.mPath.lineTo(this.mPadding, height - this.mCorner);
                float f = height;
                this.mPath.quadTo(this.mPadding, f, i + this.mCorner, f);
            } else {
                this.mPath.moveTo(this.mPadding + this.mCorner, 0.0f);
                this.mPath.lineTo(0.0f, 0.0f);
                float f2 = height;
                this.mPath.lineTo(0.0f, f2);
                this.mPath.lineTo(this.mPadding + this.mCorner, f2);
            }
            if (this.mEndCorner) {
                float f3 = height;
                this.mPath.lineTo((width - this.mPadding) - this.mCorner, f3);
                Path path2 = this.mPath;
                int i3 = this.mPadding;
                path2.quadTo(width - i3, f3, width - i3, height - this.mCorner);
                this.mPath.lineTo(width - this.mPadding, this.mCorner);
                Path path3 = this.mPath;
                int i4 = this.mPadding;
                path3.quadTo(width - i4, 0.0f, (width - i4) - this.mCorner, 0.0f);
                this.mPath.lineTo(this.mPadding + this.mCorner, 0.0f);
            } else {
                float f4 = width;
                this.mPath.lineTo(f4, height);
                this.mPath.lineTo(f4, 0.0f);
                this.mPath.lineTo(this.mPadding + this.mCorner, 0.0f);
            }
            canvas.clipPath(this.mPath);
        }
        super.onDraw(canvas);
    }
}
