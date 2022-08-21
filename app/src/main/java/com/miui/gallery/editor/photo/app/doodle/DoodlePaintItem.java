package com.miui.gallery.editor.photo.app.doodle;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.miui.gallery.R;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class DoodlePaintItem extends Drawable {
    public float mAlphaProgress;
    public int mBigSize;
    public int mCurrentColor;
    public Paint mPaint;
    public float mScale = 1.0f;
    public boolean mSelect;
    public int mSmallDefaultColor;
    public float mSmallSize;
    public int mStrokeColor;
    public final PaintType paintType;

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

    /* loaded from: classes2.dex */
    public enum PaintType {
        HEAVY(13.333f),
        MEDIUM(4.333f),
        LIGHT(1.333f);
        
        public final float paintSize;

        PaintType(float f) {
            this.paintSize = f;
        }
    }

    public DoodlePaintItem(PaintType paintType, Resources resources) {
        this.paintType = paintType;
        this.mBigSize = Math.round(resources.getDimension(R.dimen.doodle_paint_item_big_circle_width));
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType[paintType.ordinal()];
        if (i == 1) {
            this.mSmallSize = resources.getDimension(R.dimen.doodle_paint_item_small_circle_heavy);
        } else if (i == 2) {
            this.mSmallSize = resources.getDimension(R.dimen.doodle_paint_item_small_circle_medium);
        } else if (i == 3) {
            this.mSmallSize = resources.getDimension(R.dimen.doodle_paint_item_small_circle_light);
        }
        this.mStrokeColor = resources.getColor(R.color.doodle_paint_stroke_color);
        this.mSmallDefaultColor = resources.getColor(R.color.doodle_paint_small_color);
        this.mPaint = new Paint(1);
    }

    /* renamed from: com.miui.gallery.editor.photo.app.doodle.DoodlePaintItem$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType;

        static {
            int[] iArr = new int[PaintType.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType = iArr;
            try {
                iArr[PaintType.HEAVY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType[PaintType.MEDIUM.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType[PaintType.LIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public boolean isContain(float f, float f2) {
        return getBounds().contains((int) f, (int) f2);
    }

    public int centerX() {
        return getBounds().centerX();
    }

    public void offset(int i, int i2) {
        getBounds().offset(i, i2);
    }

    public static List<DoodlePaintItem> getList(Resources resources) {
        return Arrays.asList(new DoodlePaintItem(PaintType.LIGHT, resources), new DoodlePaintItem(PaintType.MEDIUM, resources), new DoodlePaintItem(PaintType.HEAVY, resources));
    }

    public void setSelect(boolean z) {
        this.mSelect = z;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.save();
        float f = this.mScale;
        canvas.scale(f, f, bounds.centerX(), bounds.centerY());
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(getSuggestColor(-1, !this.mSelect));
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), this.mBigSize / 2.0f, this.mPaint);
        boolean z = this.mSelect;
        int i = z ? this.mCurrentColor : this.mSmallDefaultColor;
        this.mPaint.setColor(getSuggestColor(i, !z));
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), this.mSmallSize / 2.0f, this.mPaint);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(1.0f);
        this.mPaint.setColor(getSuggestColor(this.mStrokeColor, !this.mSelect));
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), this.mBigSize / 2.0f, this.mPaint);
        if (i == -1) {
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), this.mSmallSize / 2.0f, this.mPaint);
        }
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mBigSize;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mBigSize;
    }

    public void setAlpha(float f) {
        this.mAlphaProgress = f;
    }

    public void setCurrentColor(int i) {
        this.mCurrentColor = i;
    }

    public final int getSuggestColor(int i, boolean z) {
        return !z ? i : getColorWithAlphaProgress(i, this.mAlphaProgress);
    }

    public static int getColorWithAlphaProgress(int i, float f) {
        return Color.argb((int) (Color.alpha(i) * f), Color.red(i), Color.green(i), Color.blue(i));
    }

    public void setScale(float f) {
        this.mScale = f;
    }

    public void setBigSize(int i) {
        this.mBigSize = i;
    }
}
