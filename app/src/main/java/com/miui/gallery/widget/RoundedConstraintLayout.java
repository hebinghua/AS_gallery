package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;

/* loaded from: classes2.dex */
public class RoundedConstraintLayout extends ConstraintLayout {
    public Path mPath;
    public final int mRadius;

    public RoundedConstraintLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundedConstraintLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRadius = RoundedViewHelper.obtainRoundedCornerRadius(context, attributeSet);
        setWillNotDraw(false);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        Path path = new Path();
        this.mPath = path;
        RectF rectF = new RectF(0.0f, 0.0f, i, i2);
        int i5 = this.mRadius;
        path.addRoundRect(rectF, i5, i5, Path.Direction.CW);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(this.mPath);
        super.draw(canvas);
        canvas.restoreToCount(save);
    }
}
