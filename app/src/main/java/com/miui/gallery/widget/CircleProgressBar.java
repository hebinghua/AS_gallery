package com.miui.gallery.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import com.baidu.platform.comapi.UIMsg;

/* loaded from: classes2.dex */
public class CircleProgressBar extends ProgressBar {
    public Path mArcPath;
    public RectF mArcRect;
    public Bitmap mBitmapForSoftLayer;
    public Canvas mCanvasForSoftLayer;
    public Animator mChangeProgressAnimator;
    public int mCurrentLevel;
    public Drawable[] mLevelsBackDrawable;
    public Drawable[] mLevelsForeDrawable;
    public Drawable[] mLevelsMiddleDrawable;
    public Paint mPaint;
    public int mPrevAlpha;
    public int mPrevLevel;
    public OnProgressChangedListener mProgressChangedListener;
    public int[] mProgressLevels;
    public int mRotateVelocity;
    public Drawable mThumb;

    /* loaded from: classes2.dex */
    public interface OnProgressChangedListener {
        void onProgressChanged();
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mArcPath = new Path();
        this.mRotateVelocity = UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME;
        setIndeterminate(false);
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setColor(-16777216);
    }

    public void setProgressLevels(int[] iArr) {
        this.mProgressLevels = iArr;
    }

    public int getProgressLevelCount() {
        int[] iArr = this.mProgressLevels;
        if (iArr == null) {
            return 1;
        }
        return 1 + iArr.length;
    }

    public int getCurrentLevel() {
        return this.mCurrentLevel;
    }

    public void setDrawablesForLevels(Drawable[] drawableArr, Drawable[] drawableArr2, Drawable[] drawableArr3) {
        this.mLevelsBackDrawable = drawableArr;
        this.mLevelsMiddleDrawable = drawableArr2;
        this.mLevelsForeDrawable = drawableArr3;
        if (drawableArr != null) {
            for (Drawable drawable : drawableArr) {
                drawable.mutate();
            }
        }
        if (drawableArr2 != null) {
            for (Drawable drawable2 : drawableArr2) {
                drawable2.mutate();
            }
        }
        if (drawableArr3 != null) {
            for (Drawable drawable3 : drawableArr3) {
                drawable3.mutate();
            }
        }
        if (drawableArr2 != null) {
            for (Drawable drawable4 : drawableArr2) {
                if (drawable4 instanceof BitmapDrawable) {
                    ((BitmapDrawable) drawable4).getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                } else if (drawable4 instanceof NinePatchDrawable) {
                    ((NinePatchDrawable) drawable4).getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                } else {
                    throw new IllegalArgumentException("'middles' must a bitmap or nine patch drawable.");
                }
            }
        }
    }

    public void setDrawablesForLevels(int[] iArr, int[] iArr2, int[] iArr3) {
        setDrawablesForLevels(getDrawables(iArr), getDrawables(iArr2), getDrawables(iArr3));
        updateDrawableBounds();
    }

    public final Drawable[] getDrawables(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        Resources resources = getContext().getResources();
        Drawable[] drawableArr = new Drawable[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            drawableArr[i] = resources.getDrawable(iArr[i]);
        }
        return drawableArr;
    }

    public final void updateDrawableBounds() {
        int intrinsicWidth = getIntrinsicWidth();
        int intrinsicHeight = getIntrinsicHeight();
        Rect rect = new Rect();
        Drawable[] drawableArr = this.mLevelsBackDrawable;
        if (drawableArr != null) {
            for (Drawable drawable : drawableArr) {
                drawable.setBounds(getCenterAlignRect(rect, intrinsicWidth, intrinsicHeight, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
            }
        }
        Drawable[] drawableArr2 = this.mLevelsMiddleDrawable;
        if (drawableArr2 != null) {
            for (Drawable drawable2 : drawableArr2) {
                drawable2.setBounds(getCenterAlignRect(rect, intrinsicWidth, intrinsicHeight, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight()));
            }
            Drawable drawable3 = this.mLevelsMiddleDrawable[0];
            this.mArcRect = new RectF(drawable3.getBounds().left, drawable3.getBounds().top, drawable3.getBounds().right, drawable3.getBounds().bottom);
        }
        Drawable[] drawableArr3 = this.mLevelsForeDrawable;
        if (drawableArr3 != null) {
            for (Drawable drawable4 : drawableArr3) {
                drawable4.setBounds(getCenterAlignRect(rect, intrinsicWidth, intrinsicHeight, drawable4.getIntrinsicWidth(), drawable4.getIntrinsicHeight()));
            }
        }
    }

    public final Rect getCenterAlignRect(Rect rect, int i, int i2, int i3, int i4) {
        rect.set((i - i3) / 2, (i2 - i4) / 2, (i + i3) / 2, (i2 + i4) / 2);
        return rect;
    }

    public Drawable getBackDrawable(int i) {
        Drawable[] drawableArr = this.mLevelsBackDrawable;
        if (drawableArr == null) {
            return null;
        }
        return drawableArr[i];
    }

    public Drawable getMiddleDrawable(int i) {
        Drawable[] drawableArr = this.mLevelsMiddleDrawable;
        if (drawableArr == null) {
            return null;
        }
        return drawableArr[i];
    }

    public Drawable getForeDrawable(int i) {
        Drawable[] drawableArr = this.mLevelsForeDrawable;
        if (drawableArr == null) {
            return null;
        }
        return drawableArr[i];
    }

    public void setRotateVelocity(int i) {
        this.mRotateVelocity = i;
    }

    public void setProgressByAnimator(int i) {
        setProgressByAnimator(i, null);
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.mProgressChangedListener = onProgressChangedListener;
    }

    public void setProgressByAnimator(int i, Animator.AnimatorListener animatorListener) {
        stopProgressAnimator();
        int abs = Math.abs((int) (((i - getProgress()) / getMax()) * 360.0f));
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "progress", i);
        this.mChangeProgressAnimator = ofInt;
        ofInt.setDuration(calcDuration(abs));
        this.mChangeProgressAnimator.setInterpolator(getInterpolator());
        if (animatorListener != null) {
            this.mChangeProgressAnimator.addListener(animatorListener);
        }
        this.mChangeProgressAnimator.start();
    }

    public void stopProgressAnimator() {
        Animator animator = this.mChangeProgressAnimator;
        if (animator == null || !animator.isRunning()) {
            return;
        }
        this.mChangeProgressAnimator.cancel();
    }

    public final int calcDuration(int i) {
        return (i * 1000) / this.mRotateVelocity;
    }

    @Override // android.widget.ProgressBar, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int progressLevelCount = getProgressLevelCount();
        for (int i = 0; i < progressLevelCount; i++) {
            Drawable[] drawableArr = this.mLevelsBackDrawable;
            if (drawableArr != null) {
                drawableArr[i].setState(getDrawableState());
            }
            Drawable[] drawableArr2 = this.mLevelsMiddleDrawable;
            if (drawableArr2 != null) {
                drawableArr2[i].setState(getDrawableState());
            }
            Drawable[] drawableArr3 = this.mLevelsForeDrawable;
            if (drawableArr3 != null) {
                drawableArr3[i].setState(getDrawableState());
            }
        }
        invalidate();
    }

    @Override // android.widget.ProgressBar
    public synchronized void setProgress(int i) {
        int length;
        super.setProgress(i);
        int[] iArr = this.mProgressLevels;
        if (iArr == null) {
            length = 0;
        } else {
            length = iArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    i2 = -1;
                    break;
                } else if (i < this.mProgressLevels[i2]) {
                    break;
                } else {
                    i2++;
                }
            }
            if (i2 != -1) {
                length = i2;
            }
        }
        int i3 = this.mCurrentLevel;
        if (length != i3) {
            this.mPrevLevel = i3;
            this.mCurrentLevel = length;
            setPrevAlpha(255);
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "prevAlpha", 0);
            ofInt.setDuration(300L);
            ofInt.setInterpolator(new LinearInterpolator());
            ofInt.start();
        }
        OnProgressChangedListener onProgressChangedListener = this.mProgressChangedListener;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged();
        }
    }

    public float getRate() {
        return getProgress() / getMax();
    }

    private int getIntrinsicWidth() {
        int intrinsicWidth = this.mLevelsMiddleDrawable != null ? getMiddleDrawable(0).getIntrinsicWidth() : 0;
        Drawable[] drawableArr = this.mLevelsForeDrawable;
        if (drawableArr != null) {
            intrinsicWidth = Math.max(intrinsicWidth, drawableArr[0].getIntrinsicWidth());
        }
        Drawable[] drawableArr2 = this.mLevelsBackDrawable;
        return drawableArr2 != null ? Math.max(intrinsicWidth, drawableArr2[0].getIntrinsicWidth()) : intrinsicWidth;
    }

    private int getIntrinsicHeight() {
        int intrinsicHeight = getMiddleDrawable(0).getIntrinsicHeight();
        Drawable[] drawableArr = this.mLevelsForeDrawable;
        if (drawableArr != null) {
            intrinsicHeight = Math.max(intrinsicHeight, drawableArr[0].getIntrinsicHeight());
        }
        Drawable[] drawableArr2 = this.mLevelsBackDrawable;
        return drawableArr2 != null ? Math.max(intrinsicHeight, drawableArr2[0].getIntrinsicHeight()) : intrinsicHeight;
    }

    @Override // android.widget.ProgressBar, android.view.View
    public synchronized void onMeasure(int i, int i2) {
        setMeasuredDimension(getIntrinsicWidth(), getIntrinsicHeight());
    }

    @Override // android.widget.ProgressBar, android.view.View
    public synchronized void onDraw(Canvas canvas) {
        drawLayer(canvas, getBackDrawable(this.mCurrentLevel), getForeDrawable(this.mCurrentLevel), getMiddleDrawable(this.mCurrentLevel), getRate(), 255 - this.mPrevAlpha);
        if (this.mPrevAlpha >= 10) {
            drawLayer(canvas, getBackDrawable(this.mPrevLevel), getForeDrawable(this.mPrevLevel), getMiddleDrawable(this.mPrevLevel), getRate(), this.mPrevAlpha);
        }
    }

    public final void drawLayer(Canvas canvas, Drawable drawable, Drawable drawable2, Drawable drawable3, float f, int i) {
        if (drawable != null) {
            drawable.setAlpha(i);
            drawable.draw(canvas);
        }
        if (drawable3 != null) {
            if (canvas.isHardwareAccelerated()) {
                canvas.saveLayer(drawable3.getBounds().left, drawable3.getBounds().top, drawable3.getBounds().right, drawable3.getBounds().bottom, null);
                canvas.drawArc(this.mArcRect, -90.0f, f * 360.0f, true, this.mPaint);
                drawable3.setAlpha(i);
                drawable3.draw(canvas);
                canvas.restore();
            } else {
                if (this.mBitmapForSoftLayer == null) {
                    this.mBitmapForSoftLayer = Bitmap.createBitmap(drawable3.getBounds().width(), drawable3.getBounds().height(), Bitmap.Config.ARGB_8888);
                    this.mCanvasForSoftLayer = new Canvas(this.mBitmapForSoftLayer);
                }
                this.mBitmapForSoftLayer.eraseColor(0);
                this.mCanvasForSoftLayer.save();
                this.mCanvasForSoftLayer.translate(-drawable3.getBounds().left, -drawable3.getBounds().top);
                this.mCanvasForSoftLayer.drawArc(this.mArcRect, -90.0f, f * 360.0f, true, this.mPaint);
                drawable3.setAlpha(i);
                drawable3.draw(this.mCanvasForSoftLayer);
                this.mCanvasForSoftLayer.restore();
                canvas.drawBitmap(this.mBitmapForSoftLayer, drawable3.getBounds().left, drawable3.getBounds().top, (Paint) null);
            }
        }
        Drawable drawable4 = this.mThumb;
        if (drawable4 != null) {
            canvas.save();
            int width = ((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2;
            int height = ((getHeight() - getPaddingTop()) - getPaddingBottom()) / 2;
            int intrinsicWidth = drawable4.getIntrinsicWidth();
            int intrinsicHeight = drawable4.getIntrinsicHeight();
            canvas.rotate((getProgress() * 360.0f) / getMax(), width, height);
            int i2 = intrinsicWidth / 2;
            int i3 = intrinsicHeight / 2;
            drawable4.setBounds(width - i2, height - i3, width + i2, height + i3);
            drawable4.draw(canvas);
            canvas.restore();
        }
        if (drawable2 != null) {
            drawable2.setAlpha(i);
            drawable2.draw(canvas);
        }
    }

    public void setPrevAlpha(int i) {
        this.mPrevAlpha = i;
        invalidate();
    }

    public int getPrevAlpha() {
        return this.mPrevAlpha;
    }

    public void setThumb(int i) {
        setThumb(getResources().getDrawable(i));
    }

    public void setThumb(Drawable drawable) {
        this.mThumb = drawable;
    }
}
