package com.miui.gallery.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import java.util.Objects;

/* loaded from: classes2.dex */
public class LevelNinePathDrawable extends Drawable {
    public NinePatchDrawable mDrawable;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public LevelNinePathDrawable(NinePatchDrawable ninePatchDrawable) {
        Objects.requireNonNull(ninePatchDrawable);
        this.mDrawable = ninePatchDrawable;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        float level = getLevel() / 10000.0f;
        Rect bounds = getBounds();
        float width = bounds.width() * level;
        if (width > 10.0f) {
            NinePatchDrawable ninePatchDrawable = this.mDrawable;
            int i = bounds.left;
            ninePatchDrawable.setBounds(i, bounds.top, (int) (i + width), bounds.bottom);
            this.mDrawable.draw(canvas);
            return;
        }
        canvas.save();
        canvas.scale(level, 1.0f);
        this.mDrawable.setBounds(getBounds());
        this.mDrawable.draw(canvas);
        canvas.restore();
    }
}
