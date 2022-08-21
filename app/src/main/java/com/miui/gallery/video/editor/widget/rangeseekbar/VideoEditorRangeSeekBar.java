package com.miui.gallery.video.editor.widget.rangeseekbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import com.miui.gallery.R;
import com.miui.gallery.util.ViewUtils;
import com.miui.gallery.video.editor.widget.rangeseekbar.drawable.DisabledRangeDrawable;
import com.miui.gallery.video.editor.widget.rangeseekbar.drawable.SlideDrawable;
import com.miui.gallery.video.editor.widget.rangeseekbar.drawable.VideoThumbnailBackgroundDrawable;

/* loaded from: classes2.dex */
public class VideoEditorRangeSeekBar extends View {
    public static final int LONGPRESS_TIMEOUT = ViewUtils.getRealLongPressedTimeout();
    public float autoMoveSpeed;
    public boolean autoMoving;
    public int mAvailableAreaLeft;
    public float mAvailableAreaOffset;
    public int mAvailableAreaRight;
    public VideoThumbnailBackgroundDrawable mBackgroundDrawable;
    public int mContentPaddingBottom;
    public int mContentPaddingTop;
    public int mDragSlop;
    public int mDragState;
    public int mEndRange;
    public Drawable mFrameDrawable;
    public int mFrameWidth;
    public int mHeight;
    public Animator mHideProgressAnimator;
    public Runnable mHideProgressBarAction;
    public boolean mIsLongPress;
    public boolean mIsShowProgress;
    public boolean mIsZooming;
    public float mLastTouchDownX;
    public float mLeftRangeFloat;
    public SlideDrawable mLeftSlideDrawable;
    public int mLockedAvailableAreaLeft;
    public int mLockedAvailableAreaRight;
    public float mLongPressMoveDistanceFlag;
    public LongPressedRunnable mLongPressedRunnable;
    public int mMax;
    public OnSeekBarChangeListener mOnSeekBarChangeListener;
    public Paint mPaint;
    public int mProgress;
    public Rect mProgressBounds;
    public float mProgressFloat;
    public DisabledRangeDrawable mRangeDrawable;
    public float mRightRangeFloat;
    public SlideDrawable mRightSlideDrawable;
    public int mScaledTouchSlop;
    public float mStartLongPressMoveDistanceDownXFlag;
    public int mStartRange;
    public boolean mStopSlide;
    public int mThumbOffset;
    public int mTotal;
    public float mTouchDownX;
    public float mTouchMoveX;
    public int mTouchState;
    public int mVisibleAreaBottom;
    public int mVisibleAreaLeft;
    public int mVisibleAreaRight;
    public int mVisibleAreaTop;
    public int mWidth;
    public SlideDrawable progressDrawable;

    /* loaded from: classes2.dex */
    public interface ISeekbarZooming {
        void onAnimationEnd();
    }

    /* loaded from: classes2.dex */
    public interface OnSeekBarChangeListener {
        void onProgressChanged(VideoEditorRangeSeekBar videoEditorRangeSeekBar, int i, int i2, boolean z);

        void onProgressPreview(VideoEditorRangeSeekBar videoEditorRangeSeekBar, int i, int i2, boolean z);

        void onStartTrackingTouch(VideoEditorRangeSeekBar videoEditorRangeSeekBar, int i, int i2);

        void onTouchSeekBar(boolean z);
    }

    public VideoEditorRangeSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mThumbOffset = 3;
        this.mStopSlide = false;
        this.mTotal = Integer.MAX_VALUE;
        this.mStartRange = 0;
        this.mEndRange = Integer.MAX_VALUE;
        this.mLeftRangeFloat = 0.0f;
        this.mRightRangeFloat = 1.0f;
        this.mProgressFloat = 0.0f;
        this.mDragState = -1;
        this.mIsShowProgress = true;
        this.mTouchState = 0;
        this.mTouchMoveX = 0.0f;
        this.mLastTouchDownX = 0.0f;
        this.mAvailableAreaLeft = Integer.MAX_VALUE;
        this.mAvailableAreaRight = Integer.MAX_VALUE;
        this.mAvailableAreaOffset = 0.0f;
        this.autoMoving = false;
        this.autoMoveSpeed = 0.0f;
        this.mProgressBounds = new Rect();
        this.mIsLongPress = false;
        init();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    public final void init() {
        this.mLeftSlideDrawable = new SlideDrawable(getResources().getDrawable(R.drawable.video_editor_slide_left));
        this.mRightSlideDrawable = new SlideDrawable(getResources().getDrawable(R.drawable.video_editor_slide_right));
        this.progressDrawable = new SlideDrawable(getResources().getDrawable(R.drawable.video_editor_seekbar_progress));
        VideoThumbnailBackgroundDrawable videoThumbnailBackgroundDrawable = new VideoThumbnailBackgroundDrawable();
        this.mBackgroundDrawable = videoThumbnailBackgroundDrawable;
        videoThumbnailBackgroundDrawable.setCLayoutDirection(getLayoutDirection() == 0 ? 0 : 1);
        this.mRangeDrawable = new DisabledRangeDrawable(new ColorDrawable(getResources().getColor(R.color.video_editor_trim_mask_bg)));
        this.mLeftSlideDrawable.setCallback(this);
        this.mRightSlideDrawable.setCallback(this);
        this.mBackgroundDrawable.setCallback(this);
        this.mRangeDrawable.setCallback(this);
        this.mDragSlop = (int) (this.progressDrawable.getIntrinsicWidth() * 2.5d);
        this.mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.video_editor_video_trim_seek_bar_content_padding_top);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.video_editor_video_trim_progress_bar_content_padding_top);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.video_editor_video_trim_progress_bar_content_padding_bottom);
        int dimensionPixelSize4 = getResources().getDimensionPixelSize(R.dimen.video_editor_video_trim_seek_bar_content_padding_bottom);
        this.mRangeDrawable.setPadding(0, dimensionPixelSize, 0, dimensionPixelSize4);
        this.progressDrawable.setPadding(0, dimensionPixelSize2, 0, dimensionPixelSize3);
        this.mLeftSlideDrawable.setPadding(0, dimensionPixelSize, 0, dimensionPixelSize4);
        this.mRightSlideDrawable.setPadding(0, dimensionPixelSize, 0, dimensionPixelSize4);
        this.mContentPaddingTop = dimensionPixelSize;
        this.mContentPaddingBottom = dimensionPixelSize4;
        this.mFrameWidth = getResources().getDimensionPixelOffset(R.dimen.video_editor_frame_width);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(getResources().getColor(R.color.video_editor_frame_color));
        this.mPaint.setStrokeWidth(this.mFrameWidth);
        this.mFrameDrawable = getContext().getResources().getDrawable(R.drawable.video_editor_background_frame);
        this.progressDrawable.setPadding(0, this.mContentPaddingTop + ((int) this.mPaint.getStrokeWidth()), 0, this.mContentPaddingBottom + ((int) this.mPaint.getStrokeWidth()));
        this.mBackgroundDrawable.setPadding(0, dimensionPixelSize + ((int) this.mPaint.getStrokeWidth()), 0, dimensionPixelSize4 + ((int) this.mPaint.getStrokeWidth()));
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        this.mBackgroundDrawable.setCLayoutDirection(i == 0 ? 0 : 1);
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.mWidth = i3 - i;
            this.mHeight = i4 - i2;
            SlideDrawable slideDrawable = this.mLeftSlideDrawable;
            slideDrawable.setSize(slideDrawable.getIntrinsicWidth(), this.mHeight);
            SlideDrawable slideDrawable2 = this.mRightSlideDrawable;
            slideDrawable2.setSize(slideDrawable2.getIntrinsicWidth(), this.mHeight);
            SlideDrawable slideDrawable3 = this.progressDrawable;
            slideDrawable3.setSize(slideDrawable3.getIntrinsicWidth(), this.mHeight);
            this.mVisibleAreaLeft = this.mLeftSlideDrawable.getPaddingLeft() + getPaddingLeft();
            this.mVisibleAreaRight = (this.mWidth - this.mRightSlideDrawable.getPaddingRight()) - getPaddingRight();
            this.mVisibleAreaTop = getPaddingTop();
            this.mVisibleAreaBottom = this.mHeight - getPaddingBottom();
            this.mBackgroundDrawable.setDrawingArea(this.mVisibleAreaLeft + this.mLeftSlideDrawable.getIntrinsicWidth(), this.mVisibleAreaRight - this.mRightSlideDrawable.getIntrinsicWidth());
            this.mRangeDrawable.setDrawingArea(this.mVisibleAreaLeft + this.mLeftSlideDrawable.getIntrinsicWidth(), this.mVisibleAreaRight - this.mRightSlideDrawable.getIntrinsicWidth());
            if (this.mAvailableAreaLeft == Integer.MAX_VALUE) {
                int intrinsicWidth = this.mVisibleAreaLeft + this.mLeftSlideDrawable.getIntrinsicWidth();
                this.mAvailableAreaLeft = intrinsicWidth;
                this.mLockedAvailableAreaLeft = intrinsicWidth;
            }
            if (this.mAvailableAreaRight == Integer.MAX_VALUE) {
                int intrinsicWidth2 = this.mVisibleAreaRight - this.mRightSlideDrawable.getIntrinsicWidth();
                this.mAvailableAreaRight = intrinsicWidth2;
                this.mLockedAvailableAreaRight = intrinsicWidth2;
            }
            this.mFrameDrawable.setBounds(this.mVisibleAreaLeft, this.mContentPaddingTop, this.mVisibleAreaRight, this.mHeight - this.mContentPaddingBottom);
            updateState();
        }
    }

    public final void updateState() {
        int availableWidth = getAvailableWidth();
        float f = this.mAvailableAreaOffset;
        this.mProgressBounds.set((int) (this.mAvailableAreaLeft + f), this.mVisibleAreaTop, (int) (this.mAvailableAreaRight + f), this.mVisibleAreaBottom);
        this.mBackgroundDrawable.setBounds(this.mProgressBounds);
        DisabledRangeDrawable disabledRangeDrawable = this.mRangeDrawable;
        Rect rect = this.mProgressBounds;
        int i = rect.left;
        int i2 = rect.top;
        int i3 = this.mFrameWidth;
        disabledRangeDrawable.setBounds(i, i2 + i3, rect.right, rect.bottom - i3);
        this.mBackgroundDrawable.setBounds(this.mProgressBounds);
        float f2 = this.mAvailableAreaOffset;
        float f3 = availableWidth;
        int i4 = this.mAvailableAreaLeft;
        int i5 = (int) ((this.mLeftRangeFloat * f3) + i4 + 0.5f + f2);
        int i6 = (int) ((this.mRightRangeFloat * f3) + i4 + 0.5f + f2);
        if (this.mDragState == 2 && this.mTouchState == 2) {
            int i7 = (int) (f2 + (f3 * this.mProgressFloat) + i4);
            if (i7 <= i5) {
                i7 = i5;
            } else if (i7 >= i6) {
                i7 = i6;
            }
            this.progressDrawable.moveProgressThumb(i7, this.mHeight);
        } else {
            this.progressDrawable.moveProgressThumb((int) (f2 + i5 + ((i6 - i5) * this.mProgressFloat)), this.mHeight);
        }
        if (!this.mIsShowProgress) {
            this.progressDrawable.moveProgressThumb(i5, this.mHeight);
        }
        int intrinsicWidth = i5 - this.mLeftSlideDrawable.getIntrinsicWidth();
        int i8 = this.mVisibleAreaLeft;
        if (intrinsicWidth < i8) {
            this.mLeftSlideDrawable.moveTo(i8, this.mHeight);
        } else {
            SlideDrawable slideDrawable = this.mLeftSlideDrawable;
            slideDrawable.moveTo(i5 - slideDrawable.getIntrinsicWidth(), this.mHeight);
        }
        if (i6 > this.mVisibleAreaRight - this.mRightSlideDrawable.getIntrinsicWidth()) {
            SlideDrawable slideDrawable2 = this.mRightSlideDrawable;
            slideDrawable2.moveTo(this.mVisibleAreaRight - slideDrawable2.getIntrinsicWidth(), this.mHeight);
        } else {
            this.mRightSlideDrawable.moveTo(i6, this.mHeight);
        }
        this.mRangeDrawable.setStartRangeScale(this.mLeftRangeFloat);
        this.mRangeDrawable.setEndRangeScale(this.mRightRangeFloat);
        invalidate();
    }

    public int getAvailableWidth() {
        return this.mAvailableAreaRight - this.mAvailableAreaLeft;
    }

    public int getVisibleAreaWidth() {
        return this.mVisibleAreaRight - this.mVisibleAreaLeft;
    }

    public int getAvailableHeight() {
        return (this.mHeight - this.mBackgroundDrawable.getPaddingTop()) - this.mBackgroundDrawable.getPaddingBottom();
    }

    public final void onStartTrackingTouch() {
        if (this.mOnSeekBarChangeListener != null) {
            int i = 1;
            if (getLayoutDirection() == 0) {
                OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                int i2 = this.mDragState;
                if (i2 == 0) {
                    i = 0;
                }
                onSeekBarChangeListener.onStartTrackingTouch(this, i, i2 == 0 ? this.mStartRange : this.mEndRange);
                return;
            }
            OnSeekBarChangeListener onSeekBarChangeListener2 = this.mOnSeekBarChangeListener;
            int i3 = this.mDragState;
            if (i3 != 0) {
                i = 0;
            }
            onSeekBarChangeListener2.onStartTrackingTouch(this, i, i3 == 0 ? this.mEndRange : this.mStartRange);
        }
    }

    public final void onStopTrackingTouch() {
        if (this.mOnSeekBarChangeListener != null) {
            int i = 1;
            if (getLayoutDirection() == 0) {
                OnSeekBarChangeListener onSeekBarChangeListener = this.mOnSeekBarChangeListener;
                int i2 = this.mDragState;
                if (i2 == 0) {
                    i = 0;
                }
                onSeekBarChangeListener.onStartTrackingTouch(this, i, i2 == 0 ? this.mStartRange : this.mEndRange);
                return;
            }
            OnSeekBarChangeListener onSeekBarChangeListener2 = this.mOnSeekBarChangeListener;
            int i3 = this.mDragState;
            if (i3 != 0) {
                i = 0;
            }
            onSeekBarChangeListener2.onStartTrackingTouch(this, i, i3 == 0 ? this.mEndRange : this.mStartRange);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        int i;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTouchDownX = motionEvent.getX();
            this.mDragState = -1;
            this.mTouchState = 1;
            this.mTouchMoveX = 0.0f;
            this.mLongPressMoveDistanceFlag = 0.0f;
            this.mStartLongPressMoveDistanceDownXFlag = motionEvent.getX();
            LongPressedRunnable longPressedRunnable = new LongPressedRunnable();
            this.mLongPressedRunnable = longPressedRunnable;
            postDelayed(longPressedRunnable, LONGPRESS_TIMEOUT);
            this.mOnSeekBarChangeListener.onTouchSeekBar(true);
        } else if (action == 1) {
            this.mTouchState = 0;
            LongPressedRunnable longPressedRunnable2 = this.mLongPressedRunnable;
            if (longPressedRunnable2 != null) {
                longPressedRunnable2.cancel();
                this.mLongPressedRunnable = null;
            }
            if (this.mDragState != -1) {
                onStopTrackingTouch();
                this.mDragState = -1;
                this.autoMoving = false;
            }
            showProgressBar();
            recoverToLockedArea();
            this.mOnSeekBarChangeListener.onTouchSeekBar(false);
        } else if (action == 2) {
            this.mOnSeekBarChangeListener.onTouchSeekBar(true);
            this.mTouchState = 2;
            this.mLongPressMoveDistanceFlag = motionEvent.getX() - this.mStartLongPressMoveDistanceDownXFlag;
            this.mTouchMoveX = motionEvent.getX() - this.mTouchDownX;
            if (this.mDragState != -1) {
                int i2 = this.mRightSlideDrawable.getBounds().left - this.mLeftSlideDrawable.getBounds().right;
                if (!this.mIsLongPress || i2 > this.mLeftSlideDrawable.getIntrinsicWidth() * 2) {
                    if (this.mAvailableAreaLeft == this.mLockedAvailableAreaLeft || this.mAvailableAreaRight == this.mLockedAvailableAreaRight) {
                        if (!this.autoMoving && Math.abs(this.mLongPressMoveDistanceFlag) < this.mScaledTouchSlop / 2) {
                            if (this.mLongPressedRunnable == null) {
                                LongPressedRunnable longPressedRunnable3 = new LongPressedRunnable();
                                this.mLongPressedRunnable = longPressedRunnable3;
                                postDelayed(longPressedRunnable3, LONGPRESS_TIMEOUT);
                            }
                        } else {
                            LongPressedRunnable longPressedRunnable4 = this.mLongPressedRunnable;
                            if (longPressedRunnable4 != null) {
                                longPressedRunnable4.cancel();
                                this.mLongPressedRunnable = null;
                            }
                            this.mLongPressMoveDistanceFlag = 0.0f;
                            this.mStartLongPressMoveDistanceDownXFlag = motionEvent.getX();
                        }
                    }
                    if (this.mStopSlide) {
                        int i3 = this.mDragState;
                        if (i3 == 0 && this.mTouchMoveX < 0.0f) {
                            trackTouchEvent(motionEvent.getX());
                        } else if (i3 == 1 && this.mTouchMoveX > 0.0f) {
                            trackTouchEvent(motionEvent.getX());
                        } else if (i3 == 2) {
                            trackTouchEvent(motionEvent.getX());
                        } else {
                            this.mDragState = 2;
                            trackTouchEvent(motionEvent.getX());
                        }
                    } else {
                        trackTouchEvent(motionEvent.getX());
                    }
                }
            } else {
                float x = motionEvent.getX() - this.mTouchDownX;
                this.mTouchMoveX = x;
                if (Math.abs(x) > this.mScaledTouchSlop) {
                    int nearbyThumbId = getNearbyThumbId((int) this.mTouchDownX, this.mTouchMoveX < 0.0f);
                    if (nearbyThumbId == 1) {
                        this.mDragState = 0;
                    } else if (nearbyThumbId == 2) {
                        this.mDragState = 1;
                    } else if (nearbyThumbId == 3) {
                        this.mDragState = 2;
                    } else {
                        this.mDragState = -1;
                    }
                    if (this.mDragState != -1) {
                        onStartTrackingTouch();
                    } else {
                        int availableWidth = getAvailableWidth();
                        float f2 = availableWidth;
                        int i4 = (int) (this.mAvailableAreaOffset + (this.mRightRangeFloat * f2) + this.mAvailableAreaLeft + 0.5f);
                        float f3 = this.mTouchDownX;
                        if (f3 > ((int) ((this.mLeftRangeFloat * f2) + i + 0.5f + f)) && f3 < i4) {
                            this.progressDrawable.moveProgressThumb((int) f3, this.mHeight);
                            this.mOnSeekBarChangeListener.onProgressPreview(this, -1, (int) ((((this.mTouchDownX - this.mAvailableAreaLeft) - this.mAvailableAreaOffset) / f2) * this.mTotal), true);
                        }
                    }
                }
            }
        }
        return true;
    }

    public final void recoverToLockedArea() {
        if (this.mAvailableAreaLeft == this.mLockedAvailableAreaLeft && this.mAvailableAreaRight == this.mLockedAvailableAreaRight && Float.compare(this.mAvailableAreaOffset, 0.0f) == 0) {
            return;
        }
        if (Float.compare(this.mAvailableAreaOffset, 0.0f) != 0) {
            adjustThumb();
        } else {
            zoomAvailableAreaTo(this.mLockedAvailableAreaLeft, this.mLockedAvailableAreaRight, true, null);
        }
    }

    public final void adjustThumb() {
        int i = this.mLockedAvailableAreaLeft;
        int i2 = this.mLockedAvailableAreaRight;
        float f = i + ((i2 - i) * this.mLeftRangeFloat);
        float f2 = i + ((i2 - i) * this.mRightRangeFloat);
        if (f >= this.mVisibleAreaLeft && f2 <= this.mVisibleAreaRight) {
            clearAvailableAreaOffset();
            zoomAvailableAreaTo(this.mLockedAvailableAreaLeft, this.mLockedAvailableAreaRight, true, null);
        } else if (f2 - f < getVisibleAreaWidth()) {
            int i3 = this.mVisibleAreaRight;
            if (f2 > i3) {
                float f3 = i3 - f2;
                int i4 = (int) ((f2 + f3) - 0.5f);
                if (((int) ((f + f3) - 0.5f)) < this.mVisibleAreaLeft || i4 > i3) {
                    return;
                }
                clearAvailableAreaOffset();
                zoomAvailableAreaTo((int) (this.mLockedAvailableAreaLeft + f3), (int) (this.mLockedAvailableAreaRight + f3), true, null);
                return;
            }
            int i5 = this.mVisibleAreaLeft;
            if (f >= i5) {
                clearAvailableAreaOffset();
                lockFloatRangeTo(this.mLeftRangeFloat, this.mRightRangeFloat, null);
                return;
            }
            float f4 = i5 - f;
            int i6 = (int) (f2 + f4 + 0.5f);
            if (((int) (f + f4 + 0.5f)) < i5 || i6 > i3) {
                return;
            }
            clearAvailableAreaOffset();
            zoomAvailableAreaTo((int) (this.mLockedAvailableAreaLeft + f4), (int) (this.mLockedAvailableAreaRight + f4), true, null);
        } else {
            clearAvailableAreaOffset();
            lockFloatRangeTo(this.mLeftRangeFloat, this.mRightRangeFloat, null);
        }
    }

    public final void clearAvailableAreaOffset() {
        float f = this.mAvailableAreaOffset;
        this.mAvailableAreaLeft = (int) (this.mAvailableAreaLeft + f);
        this.mAvailableAreaRight = (int) (this.mAvailableAreaRight + f);
        this.mAvailableAreaOffset = 0.0f;
    }

    public void lockRangeTo(int i, int i2, ISeekbarZooming iSeekbarZooming) {
        if (i2 > this.mTotal || i >= i2 || i2 - i <= this.mWidth) {
            return;
        }
        if (getLayoutDirection() == 0) {
            int i3 = this.mTotal;
            lockFloatRangeTo(i / i3, i2 / i3, iSeekbarZooming);
            return;
        }
        int i4 = this.mTotal;
        lockFloatRangeTo(1.0f - (i2 / i4), 1.0f - (i / i4), iSeekbarZooming);
    }

    public final void lockFloatRangeTo(float f, float f2, ISeekbarZooming iSeekbarZooming) {
        int visibleAreaWidth = getVisibleAreaWidth();
        zoomAvailableAreaTo((int) (((((this.mVisibleAreaLeft + visibleAreaWidth) - this.mLeftSlideDrawable.getIntrinsicWidth()) * f) - ((this.mVisibleAreaLeft + this.mLeftSlideDrawable.getIntrinsicWidth()) * f2)) / (f - f2)), (int) ((((1.0f - f) * ((visibleAreaWidth + this.mVisibleAreaLeft) - this.mLeftSlideDrawable.getIntrinsicWidth())) - ((1.0f - f2) * (this.mVisibleAreaLeft + this.mLeftSlideDrawable.getIntrinsicWidth()))) / (f2 - f)), true, iSeekbarZooming);
    }

    public final void scaleTo(float f, float f2, boolean z) {
        float availableWidth = getAvailableWidth() * (f - 1.0f);
        zoomAvailableAreaTo((int) (this.mAvailableAreaLeft - (availableWidth * f2)), (int) (this.mAvailableAreaRight + (availableWidth * (1.0f - f2))), z, null);
    }

    public final void zoomAvailableAreaTo(int i, int i2, boolean z, final ISeekbarZooming iSeekbarZooming) {
        if (i2 - i < this.mTotal) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(500L);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.playTogether(ObjectAnimator.ofInt(this, "AvailableAreaLeft", this.mAvailableAreaLeft, i), ObjectAnimator.ofInt(this, "AvailableAreaRight", this.mAvailableAreaRight, i2));
            animatorSet.start();
            this.mIsZooming = true;
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ISeekbarZooming iSeekbarZooming2 = iSeekbarZooming;
                    if (iSeekbarZooming2 != null) {
                        iSeekbarZooming2.onAnimationEnd();
                    }
                    VideoEditorRangeSeekBar.this.mIsZooming = false;
                }
            });
            if (!z) {
                return;
            }
            this.mLockedAvailableAreaLeft = i;
            this.mLockedAvailableAreaRight = i2;
        }
    }

    public void setAvailableAreaLeft(int i) {
        this.mAvailableAreaLeft = i;
        updateState();
    }

    public void setAvailableAreaRight(int i) {
        this.mAvailableAreaRight = i;
        updateState();
    }

    public int getNearbyThumbId(int i, boolean z) {
        int abs = Math.abs(this.mLeftSlideDrawable.getLocationX() - i);
        int abs2 = Math.abs(this.mRightSlideDrawable.getLocationX() - i);
        int abs3 = Math.abs(this.progressDrawable.getLocationX() - i);
        int abs4 = Math.abs(this.mLeftSlideDrawable.getLocationX() - this.progressDrawable.getLocationX());
        int abs5 = Math.abs(this.mRightSlideDrawable.getLocationX() - this.progressDrawable.getLocationX());
        int i2 = this.mDragSlop;
        boolean z2 = abs4 <= i2;
        boolean z3 = abs5 <= i2;
        if (z) {
            if (z2 && (abs < i2 || abs3 < i2)) {
                return 1;
            }
            if (z3 && (abs2 < i2 || abs3 < i2)) {
                return 2;
            }
            if (this.mIsShowProgress && abs3 < i2) {
                return 3;
            }
            if (abs < i2) {
                return 1;
            }
            if (abs2 < i2) {
                return 2;
            }
        } else if (z3 && (abs2 < i2 || abs3 < i2)) {
            return 2;
        } else {
            if (z2 && this.mIsShowProgress && (abs < i2 || abs3 < i2)) {
                return 1;
            }
            if (this.mIsShowProgress && abs3 < i2) {
                return 3;
            }
            if (abs2 < i2) {
                return 2;
            }
            if (abs < i2) {
                return 1;
            }
        }
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00c4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void trackTouchEvent(float r7) {
        /*
            Method dump skipped, instructions count: 242
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.trackTouchEvent(float):void");
    }

    public final boolean canSlip() {
        return this.mProgress > this.mStartRange + (this.progressDrawable.getIntrinsicWidth() / 2) && this.mProgress < this.mEndRange + (this.progressDrawable.getIntrinsicWidth() / 2);
    }

    public final void updateRangeValue() {
        if (this.mDragState == 2) {
            this.mProgress = (int) ((this.mTotal * this.mProgressFloat) + 0.5f);
        } else if (getLayoutDirection() == 0) {
            int i = this.mTotal;
            this.mStartRange = (int) ((i * this.mLeftRangeFloat) + 0.5f);
            this.mEndRange = (int) ((i * this.mRightRangeFloat) + 0.5f);
        } else {
            int i2 = this.mTotal;
            this.mStartRange = (int) ((i2 * (1.0f - this.mRightRangeFloat)) + 0.5f);
            this.mEndRange = (int) ((i2 * (1.0f - this.mLeftRangeFloat)) + 0.5f);
        }
    }

    public int getStartRange() {
        return this.mStartRange;
    }

    public int getEndRange() {
        return this.mEndRange;
    }

    public void setTotal(int i) {
        this.mTotal = i;
        updateRangeValue();
    }

    public void setStartRange(int i) {
        if (i < 0) {
            this.mStartRange = 0;
        } else {
            int i2 = this.mEndRange;
            if (i > i2) {
                this.mStartRange = i2;
            } else {
                this.mStartRange = i;
            }
        }
        if (getLayoutDirection() == 0) {
            this.mLeftRangeFloat = this.mStartRange / this.mTotal;
        } else {
            this.mRightRangeFloat = 1.0f - (this.mStartRange / this.mTotal);
        }
        updateState();
    }

    public void setMax(int i) {
        int i2 = this.mMax;
        this.mProgress = (int) (i2 * (this.mProgress / i2));
        this.mMax = i;
        if (getLayoutDirection() == 0) {
            this.mProgressFloat = this.mProgress / this.mMax;
        } else {
            this.mProgressFloat = 1.0f - (this.mProgress / this.mMax);
        }
    }

    public void setThumbnailAspectRatio(float f) {
        this.mBackgroundDrawable.setAspectRatio(f);
    }

    public void setProgressBarAlpha(int i) {
        this.progressDrawable.setAlpha(i);
        invalidate();
    }

    public void showProgressBar() {
        if (!this.mIsShowProgress) {
            Runnable runnable = this.mHideProgressBarAction;
            if (runnable != null) {
                removeCallbacks(runnable);
                this.mHideProgressBarAction = null;
            }
            Animator animator = this.mHideProgressAnimator;
            if (animator != null) {
                animator.cancel();
                this.mHideProgressAnimator = null;
            }
            this.mIsShowProgress = true;
            setProgressBarAlpha(255);
        }
    }

    public void hideProgressBar() {
        if (this.mIsShowProgress) {
            hideProgressBar(false);
        }
    }

    public void hideProgressBar(boolean z) {
        if (z) {
            ObjectAnimator duration = ObjectAnimator.ofInt(this, "ProgressBarAlpha", 255, 0).setDuration(1000L);
            this.mHideProgressAnimator = duration;
            duration.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    VideoEditorRangeSeekBar.this.mIsShowProgress = false;
                    VideoEditorRangeSeekBar.this.mHideProgressAnimator = null;
                }
            });
            this.mHideProgressAnimator.start();
            return;
        }
        this.mIsShowProgress = false;
        setProgressBarAlpha(0);
    }

    public void setProgress(int i) {
        if (i < 0) {
            this.mProgress = 0;
        } else {
            int i2 = this.mMax;
            if (i > i2) {
                this.mProgress = i2;
            } else {
                this.mProgress = i;
            }
        }
        if (getLayoutDirection() == 0) {
            this.mProgressFloat = this.mProgress / this.mMax;
        } else {
            this.mProgressFloat = 1.0f - (this.mProgress / this.mMax);
        }
        updateState();
    }

    public void setEndRange(int i) {
        int i2 = this.mTotal;
        if (i > i2) {
            this.mEndRange = i2;
        } else {
            int i3 = this.mStartRange;
            if (i < i3) {
                this.mEndRange = i3;
            } else {
                this.mEndRange = i;
            }
        }
        if (getLayoutDirection() == 0) {
            this.mRightRangeFloat = this.mEndRange / this.mTotal;
        } else {
            this.mLeftRangeFloat = 1.0f - (this.mEndRange / this.mTotal);
        }
        updateState();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mFrameDrawable.draw(canvas);
        this.mBackgroundDrawable.draw(canvas);
        this.mRangeDrawable.draw(canvas);
        this.mLeftSlideDrawable.draw(canvas);
        this.mRightSlideDrawable.draw(canvas);
        if (this.mIsShowProgress && this.progressDrawable.getBounds().left >= this.mVisibleAreaLeft && this.progressDrawable.getBounds().right <= this.mVisibleAreaRight) {
            this.progressDrawable.draw(canvas);
        }
        if (this.autoMoving) {
            autoMove();
        }
        int availableWidth = getAvailableWidth();
        float f = this.mAvailableAreaOffset;
        float f2 = availableWidth;
        int i = this.mAvailableAreaLeft;
        int i2 = (int) ((this.mLeftRangeFloat * f2) + i + 0.5f + f);
        int i3 = (int) (f + (f2 * this.mRightRangeFloat) + i + 0.5f);
        if (i2 < this.mVisibleAreaLeft + this.mLeftSlideDrawable.getIntrinsicWidth()) {
            i2 = this.mLeftSlideDrawable.getIntrinsicWidth() + this.mVisibleAreaLeft;
        }
        if (i3 > this.mVisibleAreaRight - this.mRightSlideDrawable.getIntrinsicWidth()) {
            i3 = this.mVisibleAreaRight - this.mRightSlideDrawable.getIntrinsicWidth();
        }
        float f3 = i2;
        float f4 = i3;
        canvas.drawLine(f3, this.mContentPaddingTop + (this.mPaint.getStrokeWidth() / 2.0f), f4, this.mContentPaddingTop + (this.mPaint.getStrokeWidth() / 2.0f), this.mPaint);
        canvas.drawLine(f3, (this.mHeight - this.mContentPaddingBottom) - (this.mPaint.getStrokeWidth() / 2.0f), f4, (this.mHeight - this.mContentPaddingBottom) - (this.mPaint.getStrokeWidth() / 2.0f), this.mPaint);
    }

    public final void autoMove() {
        if (this.autoMoving) {
            float f = this.mAvailableAreaOffset + this.autoMoveSpeed;
            this.mAvailableAreaOffset = f;
            int i = this.mAvailableAreaLeft;
            float f2 = i + f;
            int i2 = this.mVisibleAreaLeft;
            if (f2 > i2) {
                this.mAvailableAreaOffset = i2 - i;
            }
            int i3 = this.mAvailableAreaRight;
            float f3 = i3 + this.mAvailableAreaOffset;
            int i4 = this.mVisibleAreaRight;
            if (f3 < i4) {
                this.mAvailableAreaOffset = i4 - i3;
            }
            trackTouchEvent(this.mLastTouchDownX);
            invalidate();
        }
    }

    public void setBitmapProvider(VideoThumbnailBackgroundDrawable.BitmapProvider bitmapProvider) {
        this.mBackgroundDrawable.setmBitmapProvider(new VideoEditorRangeSeekBarBitmapProviderWrapper(bitmapProvider));
    }

    /* loaded from: classes2.dex */
    public class VideoEditorRangeSeekBarBitmapProviderWrapper implements VideoThumbnailBackgroundDrawable.BitmapProvider {
        public VideoThumbnailBackgroundDrawable.BitmapProvider mWrapped;

        public VideoEditorRangeSeekBarBitmapProviderWrapper(VideoThumbnailBackgroundDrawable.BitmapProvider bitmapProvider) {
            this.mWrapped = bitmapProvider;
        }

        @Override // com.miui.gallery.video.editor.widget.rangeseekbar.drawable.VideoThumbnailBackgroundDrawable.BitmapProvider
        public Bitmap getBitmap(int i, int i2) {
            return this.mWrapped.getBitmap((int) (VideoEditorRangeSeekBar.this.mTotal * (i / i2)), VideoEditorRangeSeekBar.this.mTotal);
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.total = this.mTotal;
        savedState.startRange = this.mStartRange;
        savedState.endRange = this.mEndRange;
        savedState.max = this.mMax;
        savedState.progress = this.mProgress;
        savedState.availableAreaLeft = this.mAvailableAreaLeft;
        savedState.availableAreaRight = this.mAvailableAreaRight;
        savedState.lockedAvailableAreaLeft = this.mLockedAvailableAreaLeft;
        savedState.lockedAvailableAreaRight = this.mLockedAvailableAreaRight;
        savedState.rightRangeFloat = this.mRightRangeFloat;
        savedState.leftRangeFloat = this.mLeftRangeFloat;
        savedState.progressFloat = this.mProgressFloat;
        return savedState;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !(parcelable instanceof SavedState)) {
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mTotal = savedState.total;
        this.mStartRange = savedState.startRange;
        this.mEndRange = savedState.endRange;
        this.mMax = savedState.max;
        this.mProgress = savedState.progress;
        this.mAvailableAreaLeft = savedState.availableAreaLeft;
        this.mAvailableAreaRight = savedState.availableAreaRight;
        this.mLockedAvailableAreaLeft = savedState.lockedAvailableAreaLeft;
        this.mLockedAvailableAreaRight = savedState.lockedAvailableAreaRight;
        this.mRightRangeFloat = savedState.rightRangeFloat;
        this.mLeftRangeFloat = savedState.leftRangeFloat;
        this.mProgressFloat = savedState.progressFloat;
    }

    /* loaded from: classes2.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.miui.gallery.video.editor.widget.rangeseekbar.VideoEditorRangeSeekBar.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public SavedState mo1769createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public SavedState[] mo1770newArray(int i) {
                return new SavedState[i];
            }
        };
        public int availableAreaLeft;
        public int availableAreaRight;
        public int endRange;
        public float leftRangeFloat;
        public int lockedAvailableAreaLeft;
        public int lockedAvailableAreaRight;
        public int max;
        public int progress;
        public float progressFloat;
        public float rightRangeFloat;
        public int startRange;
        public Parcelable superState;
        public int total;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public SavedState(Parcel parcel) {
            this.total = Integer.MAX_VALUE;
            this.startRange = 0;
            this.endRange = Integer.MAX_VALUE;
            this.superState = parcel.readParcelable(getClass().getClassLoader());
            this.total = parcel.readInt();
            this.startRange = parcel.readInt();
            this.endRange = parcel.readInt();
            this.max = parcel.readInt();
            this.progress = parcel.readInt();
            this.availableAreaLeft = parcel.readInt();
            this.availableAreaRight = parcel.readInt();
            this.lockedAvailableAreaLeft = parcel.readInt();
            this.lockedAvailableAreaRight = parcel.readInt();
            this.leftRangeFloat = parcel.readFloat();
            this.rightRangeFloat = parcel.readFloat();
            this.progressFloat = parcel.readFloat();
        }

        public SavedState(Parcelable parcelable) {
            this.total = Integer.MAX_VALUE;
            this.startRange = 0;
            this.endRange = Integer.MAX_VALUE;
            this.superState = parcelable;
        }

        public Parcelable getSuperState() {
            return this.superState;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.superState, i);
            parcel.writeInt(this.total);
            parcel.writeInt(this.startRange);
            parcel.writeInt(this.endRange);
            parcel.writeInt(this.max);
            parcel.writeInt(this.progress);
            parcel.writeInt(this.availableAreaLeft);
            parcel.writeInt(this.availableAreaRight);
            parcel.writeInt(this.lockedAvailableAreaLeft);
            parcel.writeInt(this.lockedAvailableAreaRight);
            parcel.writeFloat(this.leftRangeFloat);
            parcel.writeFloat(this.rightRangeFloat);
            parcel.writeFloat(this.progressFloat);
        }
    }

    /* loaded from: classes2.dex */
    public class LongPressedRunnable implements Runnable {
        public boolean mIsCanceled;

        public LongPressedRunnable() {
            this.mIsCanceled = false;
        }

        public void cancel() {
            this.mIsCanceled = true;
            VideoEditorRangeSeekBar.this.mIsLongPress = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.mIsCanceled) {
                if ((VideoEditorRangeSeekBar.this.mTouchState != 1 && VideoEditorRangeSeekBar.this.mTouchState != 2) || VideoEditorRangeSeekBar.this.mLongPressMoveDistanceFlag >= VideoEditorRangeSeekBar.this.mScaledTouchSlop) {
                    return;
                }
                VideoEditorRangeSeekBar videoEditorRangeSeekBar = VideoEditorRangeSeekBar.this;
                int nearbyThumbId = videoEditorRangeSeekBar.getNearbyThumbId((int) videoEditorRangeSeekBar.mStartLongPressMoveDistanceDownXFlag, VideoEditorRangeSeekBar.this.mLongPressMoveDistanceFlag < 0.0f);
                if (nearbyThumbId == 1) {
                    VideoEditorRangeSeekBar.this.mIsLongPress = true;
                    VideoEditorRangeSeekBar.this.hideProgressBar();
                    VideoEditorRangeSeekBar videoEditorRangeSeekBar2 = VideoEditorRangeSeekBar.this;
                    videoEditorRangeSeekBar2.scaleTo(5.0f, videoEditorRangeSeekBar2.mLeftRangeFloat, false);
                } else if (nearbyThumbId != 2) {
                } else {
                    VideoEditorRangeSeekBar.this.mIsLongPress = true;
                    VideoEditorRangeSeekBar.this.hideProgressBar();
                    VideoEditorRangeSeekBar videoEditorRangeSeekBar3 = VideoEditorRangeSeekBar.this;
                    videoEditorRangeSeekBar3.scaleTo(5.0f, videoEditorRangeSeekBar3.mRightRangeFloat, false);
                }
            }
        }
    }

    public void setStopSlide(boolean z) {
        this.mStopSlide = z;
    }

    public boolean isZooming() {
        return this.mIsZooming;
    }

    public boolean isTouching() {
        return this.mTouchState != 0;
    }
}
