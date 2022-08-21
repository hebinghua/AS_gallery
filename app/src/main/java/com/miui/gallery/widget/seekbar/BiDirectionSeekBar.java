package com.miui.gallery.widget.seekbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.widget.SeekBar;
import com.miui.gallery.baseui.R$dimen;

/* loaded from: classes3.dex */
public class BiDirectionSeekBar extends BasicSeekBar {
    public int mAnchor;
    public boolean mIsMoved;
    public int mLastX;
    public int mMax;
    public OnSeekBarProgressListener mProgressListener;
    public boolean mStickyState;
    public int mTouchSlop;

    /* loaded from: classes3.dex */
    public interface OnSeekBarProgressListener {
        void onProgressChanged(float f);
    }

    public BiDirectionSeekBar(Context context) {
        this(context, null);
    }

    public BiDirectionSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842875);
    }

    public BiDirectionSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsMoved = false;
        this.mMax = getMax() / 2;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 2;
        setLayerType(2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x0109  */
    @Override // android.widget.AbsSeekBar, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r11) {
        /*
            Method dump skipped, instructions count: 271
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.seekbar.BiDirectionSeekBar.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // com.miui.gallery.widget.seekbar.BasicSeekBar, android.widget.SeekBar
    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        super.setOnSeekBarChangeListener(new SeekBarChangeDelegator(onSeekBarChangeListener));
    }

    @Override // android.widget.ProgressBar
    public void setProgressDrawable(Drawable drawable) {
        if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable.mutate();
            Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(16908301);
            if (findDrawableByLayerId != null) {
                layerDrawable.setDrawableByLayerId(16908301, new BiDirectionDrawable(findDrawableByLayerId, getResources()));
                super.setProgressDrawable(layerDrawable);
                return;
            }
        } else if (drawable instanceof DrawableContainer) {
            drawable.mutate();
        }
        super.setProgressDrawable(new BiDirectionDrawable(drawable, getResources()));
    }

    public void setMaxValue(int i) {
        setMax(i * 2);
        this.mMax = i;
    }

    public int getMaxValue() {
        return this.mMax;
    }

    public int getCurrentValue() {
        return getProgress() - this.mMax;
    }

    public void setCurrentValue(int i) {
        setProgress(i + this.mMax);
    }

    @Override // com.miui.gallery.widget.seekbar.BasicSeekBar
    public void updateThumb(int i) {
        super.updateThumb(i - this.mMax);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        int i5 = this.mMax;
        if (i5 > 0) {
            this.mTouchSlop = (i / i5) / 2;
        }
    }

    /* loaded from: classes3.dex */
    public class SeekBarChangeDelegator implements SeekBar.OnSeekBarChangeListener {
        public SeekBar.OnSeekBarChangeListener mDelegated;

        public SeekBarChangeDelegator(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
            this.mDelegated = onSeekBarChangeListener;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegated;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onProgressChanged(seekBar, i - BiDirectionSeekBar.this.mMax, z);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegated;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onStartTrackingTouch(seekBar);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = this.mDelegated;
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onStopTrackingTouch(seekBar);
            }
        }
    }

    public void setOnSeekBarProgressListener(OnSeekBarProgressListener onSeekBarProgressListener) {
        this.mProgressListener = onSeekBarProgressListener;
    }

    /* loaded from: classes3.dex */
    public static class BiDirectionDrawable extends Drawable {
        public Paint mBlankPaint;
        public float mBlankWidth;
        public Drawable mDrawable;
        public Rect mPadding;

        public BiDirectionDrawable(Drawable drawable, Resources resources) {
            Rect rect = new Rect();
            this.mPadding = rect;
            this.mDrawable = drawable;
            drawable.getPadding(rect);
            Paint paint = new Paint(1);
            this.mBlankPaint = paint;
            paint.setStyle(Paint.Style.FILL);
            this.mBlankPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            this.mBlankWidth = resources.getDimension(R$dimen.photo_editor_bi_direction_seek_bar_blank);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return this.mDrawable.getOpacity();
        }

        @Override // android.graphics.drawable.Drawable
        public void setChangingConfigurations(int i) {
            this.mDrawable.setChangingConfigurations(i);
        }

        @Override // android.graphics.drawable.Drawable
        public int getChangingConfigurations() {
            return this.mDrawable.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable
        public void setDither(boolean z) {
            this.mDrawable.setDither(z);
        }

        @Override // android.graphics.drawable.Drawable
        public void setFilterBitmap(boolean z) {
            this.mDrawable.setFilterBitmap(z);
        }

        @Override // android.graphics.drawable.Drawable
        public int getAlpha() {
            return this.mDrawable.getAlpha();
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(int i, PorterDuff.Mode mode) {
            this.mDrawable.setColorFilter(i, mode);
        }

        @Override // android.graphics.drawable.Drawable
        public void clearColorFilter() {
            this.mDrawable.clearColorFilter();
        }

        @Override // android.graphics.drawable.Drawable
        public boolean isStateful() {
            return this.mDrawable.isStateful();
        }

        @Override // android.graphics.drawable.Drawable
        public boolean setState(int[] iArr) {
            return this.mDrawable.setState(iArr);
        }

        @Override // android.graphics.drawable.Drawable
        public int[] getState() {
            return this.mDrawable.getState();
        }

        @Override // android.graphics.drawable.Drawable
        public void jumpToCurrentState() {
            this.mDrawable.jumpToCurrentState();
        }

        @Override // android.graphics.drawable.Drawable
        public Region getTransparentRegion() {
            Rect bounds = getBounds();
            this.mDrawable.setBounds(bounds.centerX(), bounds.top, bounds.right, bounds.bottom);
            Region transparentRegion = this.mDrawable.getTransparentRegion();
            if (transparentRegion == null) {
                return null;
            }
            this.mDrawable.setBounds(bounds.left, bounds.top, bounds.centerX(), bounds.bottom);
            transparentRegion.union(this.mDrawable.getTransparentRegion().getBounds());
            return transparentRegion;
        }

        @Override // android.graphics.drawable.Drawable
        public boolean getPadding(Rect rect) {
            if (!this.mPadding.isEmpty()) {
                rect.set(this.mPadding);
                rect.left = rect.right;
                return true;
            }
            return false;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            int i;
            Rect bounds = getBounds();
            int centerX = bounds.centerX();
            this.mDrawable.setBounds(centerX - this.mPadding.left, bounds.top, bounds.right, bounds.bottom);
            int level = getLevel();
            int i2 = 0;
            if (level > 5000) {
                i = 0;
                i2 = (level - 5000) * 2;
            } else {
                i = level < 5000 ? (5000 - level) * 2 : 0;
            }
            this.mDrawable.setLevel(i2);
            this.mDrawable.draw(canvas);
            canvas.save();
            canvas.scale(-1.0f, 1.0f, bounds.centerX(), 0.0f);
            this.mDrawable.setLevel(i);
            this.mDrawable.draw(canvas);
            canvas.restore();
            float f = centerX;
            float f2 = this.mBlankWidth;
            canvas.drawRect(f - f2, bounds.top, f + f2, bounds.bottom, this.mBlankPaint);
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.mDrawable.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.mDrawable.setColorFilter(colorFilter);
        }
    }
}
