package com.miui.gallery.magic.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.special.effects.video.util.VideoHelper;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.util.BaseMiscUtil;

/* loaded from: classes2.dex */
public class VideoSpecialProgress extends View {
    public final String[] colors;
    public VideoHelper helper;
    public OnProgressChangeListener listener;
    public Drawable mAngleLeftIcon;
    public Drawable mAngleRightIcon;
    public float mBitmapHeight;
    public int mCurrentColor;
    public Bitmap mDst;
    public float mDuration;
    public float mHeight;
    public boolean mIsTouch;
    public float mMaxProgress;
    public float mMinProgress;
    public float mProgress;
    public Drawable mProgressIcon;
    public float mStarTime;
    public int mWidth;
    public ObjectAnimator valueAnimator;

    /* loaded from: classes2.dex */
    public interface OnProgressChangeListener {
        void onProgressChange(float f, OnProgressType onProgressType);
    }

    /* loaded from: classes2.dex */
    public enum OnProgressType {
        END,
        START,
        RUN
    }

    public float getProgress() {
        if (BaseMiscUtil.isRTLDirection()) {
            float f = this.mProgress;
            if (f <= 0.0f) {
                return this.mDuration;
            }
            float f2 = this.mDuration;
            return Math.abs(f2 - ((f / 100.0f) * f2));
        }
        float f3 = this.mProgress;
        if (f3 > 0.0f) {
            return (f3 / 100.0f) * this.mDuration;
        }
        return 0.0f;
    }

    public void setProgress(float f, int i) {
        if (this.mDuration <= 0.0f) {
            return;
        }
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("progressColor-- " + ((f / this.mDuration) * 100.0f));
        MagicLog magicLog2 = MagicLog.INSTANCE;
        magicLog2.showLog("progressColor-- " + Math.ceil(f / this.mDuration));
        MagicLog magicLog3 = MagicLog.INSTANCE;
        magicLog3.showLog("progressColor--1 " + f);
        MagicLog magicLog4 = MagicLog.INSTANCE;
        magicLog4.showLog("progressColor--1 " + this.mDuration);
        if (BaseMiscUtil.isRTLDirection()) {
            this.mProgress = 100.0f - ((f / this.mDuration) * 100.0f);
        } else {
            this.mProgress = (f / this.mDuration) * 100.0f;
        }
        if (!this.mIsTouch) {
            setProgressColor(i, f);
        }
        invalidate();
    }

    public final void onProgressChange(OnProgressType onProgressType) {
        OnProgressChangeListener onProgressChangeListener = this.listener;
        if (onProgressChangeListener != null) {
            onProgressChangeListener.onProgressChange(getProgress(), onProgressType);
        }
    }

    public void setProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.listener = onProgressChangeListener;
    }

    public VideoSpecialProgress(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMinProgress = 0.0f;
        this.mMaxProgress = 100.0f;
        this.mProgress = 0.0f;
        this.helper = null;
        this.colors = new String[]{"#00000000", "#B38438FF", "#B33188D8", "#B32EC0FF", "#B333E4C5", "#B361D638", "#B3EBE329", "#B3E7B22C", "#B3FF7B1C", "#B3FF451C", "#B3C41CFF", "#B3526B1C", "#B3F8851C", "#B3785CFF"};
        init();
    }

    @SuppressLint({"ObjectAnimatorBinding"})
    public void init() {
        this.mProgressIcon = getContext().getResources().getDrawable(R$drawable.magic_video_progress_thumb);
        this.mAngleLeftIcon = getContext().getResources().getDrawable(R$drawable.magic_left_icon);
        this.mAngleRightIcon = getContext().getResources().getDrawable(R$drawable.magic_right_icon);
        this.mBitmapHeight = getContext().getResources().getDimension(R$dimen.magic_body_image_height);
        this.mHeight = getContext().getResources().getDimension(R$dimen.magic_px_100);
        if (BaseMiscUtil.isRTLDirection()) {
            this.mProgress = 100.0f;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0011, code lost:
        if (r1 != 3) goto L8;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            float r0 = r5.getX()
            int r1 = r5.getAction()
            r2 = 1
            if (r1 == 0) goto L46
            if (r1 == r2) goto L39
            r2 = 2
            if (r1 == r2) goto L14
            r0 = 3
            if (r1 == r0) goto L39
            goto L41
        L14:
            float r1 = r4.mMaxProgress
            float r2 = r4.mMinProgress
            float r3 = r1 - r2
            float r3 = r3 * r0
            int r0 = r4.mWidth
            float r0 = (float) r0
            float r3 = r3 / r0
            float r3 = r3 + r2
            r4.mProgress = r3
            float r0 = java.lang.Math.min(r1, r3)
            r4.mProgress = r0
            float r1 = r4.mMinProgress
            float r0 = java.lang.Math.max(r0, r1)
            r4.mProgress = r0
            com.miui.gallery.magic.widget.VideoSpecialProgress$OnProgressType r0 = com.miui.gallery.magic.widget.VideoSpecialProgress.OnProgressType.RUN
            r4.onProgressChange(r0)
            r4.invalidate()
            goto L41
        L39:
            r0 = 0
            r4.mIsTouch = r0
            com.miui.gallery.magic.widget.VideoSpecialProgress$OnProgressType r0 = com.miui.gallery.magic.widget.VideoSpecialProgress.OnProgressType.END
            r4.onProgressChange(r0)
        L41:
            boolean r5 = super.onTouchEvent(r5)
            return r5
        L46:
            r4.mIsTouch = r2
            com.miui.gallery.magic.widget.VideoSpecialProgress$OnProgressType r5 = com.miui.gallery.magic.widget.VideoSpecialProgress.OnProgressType.START
            r4.onProgressChange(r5)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.magic.widget.VideoSpecialProgress.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mWidth = View.MeasureSpec.getSize(i);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mMaxProgress <= 0.0f) {
            this.mMaxProgress = getWidth();
        }
        int widthForWeight = (int) getWidthForWeight(this.mProgress, this.mMaxProgress);
        int intrinsicWidth = this.mProgressIcon.getIntrinsicWidth();
        int i = widthForWeight - intrinsicWidth;
        if (widthForWeight <= intrinsicWidth) {
            widthForWeight = intrinsicWidth;
            i = 0;
        }
        int i2 = this.mWidth;
        int i3 = i2 - intrinsicWidth;
        if (i >= i3) {
            i = i3;
            widthForWeight = i2;
        }
        Bitmap bitmap = this.mDst;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, (int) Math.floor((this.mHeight - this.mBitmapHeight) / 2.0f), (Paint) null);
        }
        drawAngle(canvas);
        MagicLog.INSTANCE.showLog("left: " + i + ", right: " + widthForWeight);
        this.mProgressIcon.setBounds(i, 0, widthForWeight, getHeight());
        this.mProgressIcon.draw(canvas);
    }

    public final void drawAngle(Canvas canvas) {
        int intrinsicWidth = this.mAngleLeftIcon.getIntrinsicWidth();
        this.mAngleLeftIcon.getIntrinsicHeight();
        float f = (this.mHeight - this.mBitmapHeight) / 2.0f;
        int i = (int) f;
        this.mAngleLeftIcon.setBounds(0, i, intrinsicWidth, (int) Math.ceil((getHeight() - f) + 0.5d));
        this.mAngleRightIcon.setBounds(getRight() - intrinsicWidth, i, getRight(), (int) Math.ceil((getHeight() - f) + 0.5d));
        this.mAngleLeftIcon.draw(canvas);
        this.mAngleRightIcon.draw(canvas);
    }

    public float getWidthForWeight(float f, float f2) {
        return (getWidth() * (f / f2)) + 0.5f;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        ObjectAnimator objectAnimator = this.valueAnimator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.valueAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    public void setProgressDuration(float f) {
        this.mDuration = f;
        Bitmap createBitmap = Bitmap.createBitmap(this.mWidth, (int) Math.ceil(this.mBitmapHeight), Bitmap.Config.ARGB_8888);
        this.mDst = createBitmap;
        this.helper = VideoHelper.create(createBitmap, f);
    }

    public final void setProgressColor(int i, float f) {
        if (i < -1) {
            return;
        }
        if (i == -1) {
            i = 0;
        }
        float progress = getProgress();
        if (this.mCurrentColor != i || this.mStarTime >= f) {
            return;
        }
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("ProgressColor end:" + progress);
        if (i == 0) {
            this.helper.clearProcess(f);
        } else {
            this.helper.process(f);
        }
    }

    public void undo() {
        this.mDst = this.helper.undo();
        invalidate();
    }

    public void startType(int i) {
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("ProgressColor start:" + i + " progress: " + getProgress());
        if (i < -1) {
            return;
        }
        if (i == -1) {
            i = 0;
        }
        this.mCurrentColor = i;
        String str = this.colors[i];
        this.mStarTime = getProgress();
        MagicLog magicLog2 = MagicLog.INSTANCE;
        magicLog2.showLog("ProgressColor start:" + this.mStarTime);
        this.helper.start(this.mStarTime, Color.parseColor(str));
    }
}
