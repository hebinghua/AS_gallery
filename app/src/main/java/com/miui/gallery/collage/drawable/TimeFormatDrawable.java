package com.miui.gallery.collage.drawable;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.Keep;
import com.miui.gallery.R;
import com.miui.gallery.collage.core.poster.SpecifyDrawableModel;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.util.BaseBuildUtil;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Keep
/* loaded from: classes.dex */
public class TimeFormatDrawable extends Drawable {
    private AutoLineLayout mAutoLineLayout = new AutoLineLayout();
    private int mHeight;
    private int mWidth;

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

    public TimeFormatDrawable(Resources resources, SpecifyDrawableModel specifyDrawableModel) {
        float f = resources.getDisplayMetrics().density;
        SpecifyDrawableModel.Extras extras = specifyDrawableModel.extras;
        String str = extras.textColor;
        float f2 = extras.textSize;
        float f3 = extras.letterSpace;
        Paint paint = this.mAutoLineLayout.getPaint();
        paint.setTextSize(f2 * f);
        paint.setColor(Color.parseColor(str));
        String dataStringByFormat = getDataStringByFormat(resources.getString(R.string.collage_drawable_normal_date_format));
        this.mAutoLineLayout.setLetterSpace(f3);
        this.mAutoLineLayout.setText(dataStringByFormat);
        this.mAutoLineLayout.countText();
        RectF rectF = new RectF();
        this.mAutoLineLayout.getTextRect(rectF);
        this.mWidth = Math.round(rectF.width());
        this.mHeight = Math.round(rectF.height());
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float width = bounds.width() / getIntrinsicWidth();
        canvas.save();
        canvas.translate(bounds.centerX(), bounds.centerY());
        canvas.scale(width, width);
        this.mAutoLineLayout.draw(canvas);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mHeight;
    }

    private static String getDataStringByFormat(String str) {
        return new SimpleDateFormat(str, BaseBuildUtil.isInternational() ? Locale.getDefault() : Locale.US).format(Long.valueOf(System.currentTimeMillis()));
    }
}
