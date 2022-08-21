package com.miui.gallery.vlog.clip.single.seekbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.Keep;
import com.miui.gallery.baseui.R$color;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.clip.single.seekbar.VideoBar;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;

/* loaded from: classes2.dex */
public class SingleVideoClipBar extends View {
    public AutoMoveDirection mAutoMoveDirection;
    public int mAutoMoveSpeed;
    public float mAutoMoveWidthThresholdMax;
    public ClipBox mClipBox;
    public int mClipBoxMarginTop;
    public Drawable mClipProgressDrawable;
    public float mCustomTouchSlop;
    public DragState mDragState;
    public boolean mIsAutoMoving;
    public boolean mIsLongPressRunnablePosted;
    public boolean mIsShowProgress;
    public int mLeftAutoMoveMaxX;
    public float mLeftThumbOffsetRightDistance;
    public float mLeftTrimAxisHotAreaAddOn;
    public int mLimitMinClipTime;
    public int mLockedAreaEndTime;
    public int mLockedAreaStartTime;
    public LongPressedRunnable mLongPressedRunnable;
    public float mPostLongPressStartX;
    public int mProgressTime;
    public int mRightAutoMoveMinX;
    public float mRightThumbOffsetLeftDistance;
    public float mRightTrimAxisHotAreaAddOn;
    public SeekBarCallback mSeekBarCallback;
    public int mTotalTime;
    public TouchArea mTouchArea;
    public float mTouchDownX;
    public DisabledRangeDrawable mTranslucentMaskDrawable;
    public VideoBar mVideoBar;
    public VideoBar.Listener mVideoBarListener;
    public Animator mZoomAnimation;
    public ZoomState mZoomState;

    /* loaded from: classes2.dex */
    public enum AutoMoveDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    /* loaded from: classes2.dex */
    public enum DragState {
        NOT_DRAGGING,
        DRAGGING_LEFT_AXIS,
        DRAGGING_RIGHT_AXIS,
        DRAGGING_PROGRESS
    }

    /* loaded from: classes2.dex */
    public interface SeekBarCallback {
        void onAutoMoveLockAreaEndTimeChanged(int i);

        void onAutoMoveLockAreaStartTimeChanged(int i);

        void onDragLeftAxis(int i);

        void onDragProgress(int i);

        void onDragRightAxis(int i);

        void onTouchStateChanged(boolean z);

        void onZoomStateChanged(ZoomState zoomState);
    }

    /* loaded from: classes2.dex */
    public enum TouchArea {
        LEFT_AXIS,
        RIGHT_AXIS,
        PROGRESS,
        OTHER
    }

    /* loaded from: classes2.dex */
    public enum ZoomState {
        NORMAL,
        ZOOMING,
        ZOOMED,
        RECOVERING
    }

    public SingleVideoClipBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLimitMinClipTime = 1000;
        this.mTotalTime = 0;
        this.mIsShowProgress = true;
        this.mLockedAreaStartTime = 0;
        this.mZoomState = ZoomState.NORMAL;
        this.mTouchArea = TouchArea.OTHER;
        this.mDragState = DragState.NOT_DRAGGING;
        this.mVideoBarListener = new VideoBar.Listener() { // from class: com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.1
            @Override // com.miui.gallery.vlog.clip.single.seekbar.VideoBar.Listener
            public void onLoadFrameFinished(Bitmap bitmap) {
                SingleVideoClipBar.this.invalidate();
            }
        };
        init(context);
    }

    @Keep
    public void setVideoBarStartTime(int i) {
        this.mVideoBar.setStartTime(i);
    }

    @Keep
    public void setVideoBarEndTime(int i) {
        this.mVideoBar.setEndTime(i);
        updateDrawables(false);
        invalidate();
    }

    public void setShowProgress(boolean z, boolean z2) {
        this.mIsShowProgress = z;
        DragState dragState = this.mDragState;
        if (dragState == DragState.DRAGGING_LEFT_AXIS) {
            this.mProgressTime = this.mLockedAreaStartTime;
        } else if (dragState == DragState.DRAGGING_RIGHT_AXIS) {
            this.mProgressTime = this.mLockedAreaEndTime;
        }
        if (z2) {
            invalidate();
        }
    }

    public final void init(Context context) {
        this.mClipBox = new ClipBox(context);
        this.mClipProgressDrawable = getResources().getDrawable(R$drawable.vlog_clip_progress, null);
        this.mVideoBar = new VideoBar(this.mVideoBarListener);
        this.mTranslucentMaskDrawable = new DisabledRangeDrawable(new ColorDrawable(getResources().getColor(R$color.black_30_transparent)));
        this.mClipBox.setCallback(this);
        this.mVideoBar.setCallback(this);
        this.mTranslucentMaskDrawable.setCallback(this);
        DefaultLogger.d("SingleVideoClipFrameBar", "getWidth()=%d", Integer.valueOf(getWidth()));
        this.mCustomTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() / 2.0f;
        float round = Math.round(getResources().getDimension(R$dimen.px_20));
        this.mLeftTrimAxisHotAreaAddOn = round;
        this.mRightTrimAxisHotAreaAddOn = round;
        this.mClipBoxMarginTop = Math.round(getResources().getDimension(R$dimen.px_0));
        DefaultLogger.d("SingleVideoClipFrameBar", "mCustomTouchSlop=%f", Float.valueOf(this.mCustomTouchSlop));
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        prepareDrawables();
        updateDrawables(false);
    }

    public final void prepareDrawables() {
        int paddingLeft = getPaddingLeft();
        int i = this.mClipBoxMarginTop;
        int width = getWidth() - getPaddingRight();
        int leftClipAxisHeight = this.mClipBox.getLeftClipAxisHeight() + i;
        this.mClipBox.setVisibleArea(paddingLeft, i, width, leftClipAxisHeight);
        int leftClipAxisWidth = paddingLeft + this.mClipBox.getLeftClipAxisWidth();
        int rightClipAxisWidth = width - this.mClipBox.getRightClipAxisWidth();
        int lineWidth = i + this.mClipBox.getLineWidth();
        int lineWidth2 = leftClipAxisHeight - this.mClipBox.getLineWidth();
        this.mVideoBar.setVisibleArea(leftClipAxisWidth, lineWidth, rightClipAxisWidth, lineWidth2);
        this.mTranslucentMaskDrawable.setBounds(leftClipAxisWidth, lineWidth, rightClipAxisWidth, lineWidth2);
        int round = Math.round(this.mVideoBar.getWidth() * 0.07f);
        this.mAutoMoveWidthThresholdMax = round;
        this.mLeftAutoMoveMaxX = leftClipAxisWidth + round;
        this.mRightAutoMoveMinX = rightClipAxisWidth - round;
        this.mClipProgressDrawable.setBounds(this.mVideoBar.getLeft() - (this.mClipProgressDrawable.getIntrinsicWidth() / 2), this.mClipBox.getTop(), this.mVideoBar.getLeft() + (this.mClipProgressDrawable.getIntrinsicWidth() / 2), this.mClipBox.getBottom());
    }

    public final void updateDrawables(boolean z) {
        int round = Math.round(this.mVideoBar.convertTimeToXCoordinate(this.mLockedAreaStartTime));
        int round2 = Math.round(this.mVideoBar.convertTimeToXCoordinate(this.mLockedAreaEndTime));
        int round3 = Math.round(this.mVideoBar.convertTimeToXCoordinate(this.mProgressTime) - (this.mClipProgressDrawable.getIntrinsicWidth() / 2.0f));
        this.mClipBox.setClipBoxLeftAndRight(round - this.mClipBox.getLeftClipAxisWidth(), this.mClipBox.getRightClipAxisWidth() + round2);
        this.mClipProgressDrawable.setBounds(round3, this.mClipBox.getTop(), this.mClipProgressDrawable.getIntrinsicWidth() + round3, this.mClipBox.getBottom());
        this.mTranslucentMaskDrawable.setLeftMaskEndX(round);
        this.mTranslucentMaskDrawable.setRightMaskStartX(round2);
        if (z) {
            invalidate();
        }
    }

    public final void updateClipProgressDrawable() {
        int round = Math.round(this.mVideoBar.convertTimeToXCoordinate(this.mProgressTime) - (this.mClipProgressDrawable.getIntrinsicWidth() / 2.0f));
        this.mClipProgressDrawable.setBounds(round, this.mClipBox.getTop(), this.mClipProgressDrawable.getIntrinsicWidth() + round, this.mClipBox.getBottom());
        invalidate();
    }

    public final void postLongPressRunnableDelay(float f, long j) {
        DefaultLogger.d("SingleVideoClipFrameBar", "postLongPressRunnable");
        if (this.mLongPressedRunnable == null) {
            this.mLongPressedRunnable = new LongPressedRunnable();
        }
        postDelayed(this.mLongPressedRunnable, j);
        this.mIsLongPressRunnablePosted = true;
        this.mPostLongPressStartX = f;
    }

    public final void removeLongPressRunnable() {
        LongPressedRunnable longPressedRunnable = this.mLongPressedRunnable;
        if (longPressedRunnable != null) {
            removeCallbacks(longPressedRunnable);
        }
        this.mIsLongPressRunnablePosted = false;
    }

    public void updateSpeed(double d) {
        this.mVideoBar.updateSpeed(d);
    }

    public final TouchArea computeTouchArea(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (y < this.mClipBox.getTop() || y > this.mClipBox.getBottom()) {
            return TouchArea.OTHER;
        }
        if (x < this.mClipBox.getLeft() - this.mLeftTrimAxisHotAreaAddOn || x > this.mClipBox.getRight() + this.mRightTrimAxisHotAreaAddOn) {
            return TouchArea.OTHER;
        }
        if (x <= this.mClipBox.getLeft() + this.mClipBox.getLeftClipAxisWidth() + this.mLeftTrimAxisHotAreaAddOn) {
            TouchArea touchArea = TouchArea.LEFT_AXIS;
            this.mLeftThumbOffsetRightDistance = (this.mClipBox.getLeft() + this.mClipBox.getLeftClipAxisWidth()) - x;
            return touchArea;
        } else if (x >= (this.mClipBox.getRight() - this.mClipBox.getRightClipAxisWidth()) - this.mRightTrimAxisHotAreaAddOn) {
            TouchArea touchArea2 = TouchArea.RIGHT_AXIS;
            this.mRightThumbOffsetLeftDistance = x - (this.mClipBox.getRight() - this.mClipBox.getRightClipAxisWidth());
            return touchArea2;
        } else {
            return TouchArea.PROGRESS;
        }
    }

    public final void preHandleLongPress(MotionEvent motionEvent) {
        if (this.mIsLongPressRunnablePosted) {
            if (Math.abs(motionEvent.getX() - this.mPostLongPressStartX) < this.mCustomTouchSlop) {
                return;
            }
            removeLongPressRunnable();
            return;
        }
        postLongPressRunnableDelay(motionEvent.getX(), 1000L);
    }

    public void setSeekBarCallback(SeekBarCallback seekBarCallback) {
        this.mSeekBarCallback = seekBarCallback;
    }

    public final void onTouchStateChanged(boolean z) {
        SeekBarCallback seekBarCallback = this.mSeekBarCallback;
        if (seekBarCallback != null) {
            seekBarCallback.onTouchStateChanged(z);
        }
    }

    public final void startDrag(MotionEvent motionEvent) {
        updateDragState();
        dragging(motionEvent);
    }

    public final void updateDragState() {
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$TouchArea[this.mTouchArea.ordinal()];
        if (i == 1) {
            this.mDragState = DragState.DRAGGING_LEFT_AXIS;
            this.mIsShowProgress = false;
        } else if (i == 2) {
            this.mDragState = DragState.DRAGGING_RIGHT_AXIS;
            this.mIsShowProgress = false;
        } else if (i == 3) {
            this.mDragState = DragState.DRAGGING_PROGRESS;
        } else if (i != 4) {
        } else {
            this.mDragState = DragState.NOT_DRAGGING;
        }
    }

    public final void draggingProgress(MotionEvent motionEvent) {
        int convertXCoordinateToTime = this.mVideoBar.convertXCoordinateToTime(motionEvent.getX());
        this.mProgressTime = convertXCoordinateToTime;
        int i = this.mLockedAreaStartTime;
        if (convertXCoordinateToTime < i) {
            this.mProgressTime = i;
        } else {
            int i2 = this.mLockedAreaEndTime;
            if (convertXCoordinateToTime > i2) {
                this.mProgressTime = i2;
            }
        }
        onDragProgress(this.mProgressTime);
        updateClipProgressDrawable();
    }

    public final void onDragProgress(int i) {
        SeekBarCallback seekBarCallback = this.mSeekBarCallback;
        if (seekBarCallback != null) {
            seekBarCallback.onDragProgress(i);
        }
    }

    public final int calcAutoMoveSpeed(float f) {
        return Math.round((450.0f / this.mAutoMoveWidthThresholdMax) * f) + 150;
    }

    public final void onDragLeftAxis(int i) {
        SeekBarCallback seekBarCallback = this.mSeekBarCallback;
        if (seekBarCallback != null) {
            seekBarCallback.onDragLeftAxis(i);
        }
    }

    public final void onDragRightAxis(int i) {
        SeekBarCallback seekBarCallback = this.mSeekBarCallback;
        if (seekBarCallback != null) {
            seekBarCallback.onDragRightAxis(i);
        }
    }

    public final void draggingLeftAxis(MotionEvent motionEvent) {
        preHandleLongPress(motionEvent);
        float x = motionEvent.getX() + this.mLeftThumbOffsetRightDistance;
        setLockedAreaStartTime(this.mVideoBar.convertXCoordinateToTime(x));
        onDragLeftAxis(this.mLockedAreaStartTime);
        this.mProgressTime = this.mLockedAreaStartTime;
        if (this.mZoomState == ZoomState.ZOOMED) {
            if (x <= this.mLeftAutoMoveMaxX) {
                this.mAutoMoveSpeed = calcAutoMoveSpeed(this.mAutoMoveWidthThresholdMax - (x - this.mVideoBar.getLeft()));
                if (!this.mIsAutoMoving && this.mVideoBar.getStartTime() != 0 && this.mLockedAreaStartTime < this.mLockedAreaEndTime - this.mLimitMinClipTime) {
                    startAutoMove(AutoMoveDirection.LEFT_TO_RIGHT);
                }
            } else if (this.mIsAutoMoving && this.mAutoMoveDirection == AutoMoveDirection.LEFT_TO_RIGHT) {
                stopAutoMove();
            }
            if (x >= this.mRightAutoMoveMinX) {
                this.mAutoMoveSpeed = calcAutoMoveSpeed(this.mAutoMoveWidthThresholdMax - (this.mVideoBar.getRight() - x));
                if (!this.mIsAutoMoving && this.mVideoBar.getEndTime() != this.mTotalTime && this.mLockedAreaStartTime < this.mLockedAreaEndTime - this.mLimitMinClipTime) {
                    startAutoMove(AutoMoveDirection.RIGHT_TO_LEFT);
                }
            } else if (this.mIsAutoMoving && this.mAutoMoveDirection == AutoMoveDirection.RIGHT_TO_LEFT) {
                stopAutoMove();
            }
        }
        if (!this.mIsAutoMoving) {
            updateDrawables(true);
        }
    }

    public final void draggingRightAxis(MotionEvent motionEvent) {
        preHandleLongPress(motionEvent);
        float x = motionEvent.getX() - this.mRightThumbOffsetLeftDistance;
        setLockedAreaEndTime(this.mVideoBar.convertXCoordinateToTime(x));
        onDragRightAxis(this.mLockedAreaEndTime);
        this.mProgressTime = this.mLockedAreaEndTime;
        if (this.mZoomState == ZoomState.ZOOMED) {
            if (x <= this.mLeftAutoMoveMaxX) {
                this.mAutoMoveSpeed = calcAutoMoveSpeed(this.mAutoMoveWidthThresholdMax - (x - this.mVideoBar.getLeft()));
                if (!this.mIsAutoMoving && this.mVideoBar.getStartTime() != 0 && this.mLockedAreaEndTime > this.mLockedAreaStartTime + this.mLimitMinClipTime) {
                    startAutoMove(AutoMoveDirection.LEFT_TO_RIGHT);
                }
            } else if (this.mIsAutoMoving && this.mAutoMoveDirection == AutoMoveDirection.LEFT_TO_RIGHT) {
                stopAutoMove();
            }
            if (x >= this.mRightAutoMoveMinX) {
                this.mAutoMoveSpeed = calcAutoMoveSpeed(this.mAutoMoveWidthThresholdMax - (this.mVideoBar.getRight() - x));
                if (!this.mIsAutoMoving && this.mVideoBar.getEndTime() != this.mTotalTime && this.mLockedAreaEndTime > this.mLockedAreaStartTime + this.mLimitMinClipTime) {
                    startAutoMove(AutoMoveDirection.RIGHT_TO_LEFT);
                }
            } else if (this.mIsAutoMoving && this.mAutoMoveDirection == AutoMoveDirection.RIGHT_TO_LEFT) {
                stopAutoMove();
            }
        }
        if (!this.mIsAutoMoving) {
            updateDrawables(true);
        }
    }

    public final void stopAutoMove() {
        this.mIsAutoMoving = false;
    }

    public final void startAutoMove(AutoMoveDirection autoMoveDirection) {
        this.mIsAutoMoving = true;
        this.mAutoMoveDirection = autoMoveDirection;
        autoMoveNextFrame();
    }

    public final void onAutoMoveLockAreaStartTimeChanged() {
        SeekBarCallback seekBarCallback = this.mSeekBarCallback;
        if (seekBarCallback != null) {
            seekBarCallback.onAutoMoveLockAreaStartTimeChanged(this.mLockedAreaStartTime);
        }
    }

    public final void onAutoMoveLockAreaEndTimeChanged() {
        SeekBarCallback seekBarCallback = this.mSeekBarCallback;
        if (seekBarCallback != null) {
            seekBarCallback.onAutoMoveLockAreaEndTimeChanged(this.mLockedAreaEndTime);
        }
    }

    public final void autoMoveNextFrame() {
        int endTime;
        int round = Math.round(this.mVideoBar.convertDistanceToTimeLength(this.mAutoMoveSpeed) / 60.0f);
        this.mVideoBar.getStartTime();
        this.mVideoBar.getEndTime();
        int i = 0;
        if (this.mAutoMoveDirection == AutoMoveDirection.LEFT_TO_RIGHT) {
            int startTime = this.mVideoBar.getStartTime() - round;
            endTime = this.mVideoBar.getEndTime() - round;
            if (startTime < 0) {
                stopAutoMove();
            } else {
                int i2 = this.mTotalTime;
                if (endTime > i2) {
                    stopAutoMove();
                    endTime = i2;
                }
                i = startTime;
            }
            DragState dragState = this.mDragState;
            if (dragState == DragState.DRAGGING_LEFT_AXIS) {
                setLockedAreaStartTime(this.mLockedAreaStartTime - round);
                onAutoMoveLockAreaStartTimeChanged();
            } else if (dragState == DragState.DRAGGING_RIGHT_AXIS) {
                setLockedAreaEndTime(this.mLockedAreaEndTime - round);
                onAutoMoveLockAreaEndTimeChanged();
            }
        } else {
            int startTime2 = this.mVideoBar.getStartTime() + round;
            endTime = this.mVideoBar.getEndTime() + round;
            if (startTime2 < 0) {
                stopAutoMove();
            } else {
                int i3 = this.mTotalTime;
                if (endTime > i3) {
                    stopAutoMove();
                    endTime = i3;
                }
                i = startTime2;
            }
            DragState dragState2 = this.mDragState;
            if (dragState2 == DragState.DRAGGING_LEFT_AXIS) {
                setLockedAreaStartTime(this.mLockedAreaStartTime + round);
                onAutoMoveLockAreaStartTimeChanged();
            } else if (dragState2 == DragState.DRAGGING_RIGHT_AXIS) {
                setLockedAreaEndTime(this.mLockedAreaEndTime + round);
                onAutoMoveLockAreaEndTimeChanged();
            }
        }
        setVideoBarStartTime(i);
        setVideoBarEndTime(endTime);
    }

    private void setLockedAreaEndTime(int i) {
        int i2 = this.mTotalTime;
        if (i > i2) {
            i = i2;
        }
        int i3 = this.mLockedAreaStartTime + this.mLimitMinClipTime;
        if (i < i3) {
            this.mClipBox.setState(1);
            stopAutoMove();
            i = i3;
        } else {
            this.mClipBox.setState(0);
        }
        this.mLockedAreaEndTime = i;
    }

    private void setLockedAreaStartTime(int i) {
        if (i < 0) {
            i = 0;
        }
        int i2 = this.mLockedAreaEndTime - this.mLimitMinClipTime;
        if (i2 < 0) {
            i2 = 0;
        }
        if (i > i2) {
            this.mClipBox.setState(1);
            stopAutoMove();
            i = i2;
        } else {
            this.mClipBox.setState(0);
        }
        this.mLockedAreaStartTime = i;
    }

    /* renamed from: com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$DragState;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$TouchArea;

        static {
            int[] iArr = new int[DragState.values().length];
            $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$DragState = iArr;
            try {
                iArr[DragState.DRAGGING_LEFT_AXIS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$DragState[DragState.DRAGGING_RIGHT_AXIS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$DragState[DragState.DRAGGING_PROGRESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[TouchArea.values().length];
            $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$TouchArea = iArr2;
            try {
                iArr2[TouchArea.LEFT_AXIS.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$TouchArea[TouchArea.RIGHT_AXIS.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$TouchArea[TouchArea.PROGRESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$TouchArea[TouchArea.OTHER.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public final void dragging(MotionEvent motionEvent) {
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$vlog$clip$single$seekbar$SingleVideoClipBar$DragState[this.mDragState.ordinal()];
        if (i == 1) {
            draggingLeftAxis(motionEvent);
        } else if (i == 2) {
            draggingRightAxis(motionEvent);
        } else if (i != 3) {
        } else {
            draggingProgress(motionEvent);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ZoomState zoomState;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTouchDownX = motionEvent.getX();
            this.mTouchArea = computeTouchArea(motionEvent);
            onTouchStateChanged(true);
            if (this.mTouchArea == TouchArea.OTHER) {
                return true;
            }
            preHandleLongPress(motionEvent);
        } else if (action != 1) {
            if (action != 2 || this.mTouchArea == TouchArea.OTHER || (zoomState = this.mZoomState) == ZoomState.ZOOMING || zoomState == ZoomState.RECOVERING) {
                return true;
            }
            if (this.mDragState == DragState.NOT_DRAGGING) {
                if (Math.abs(motionEvent.getX() - this.mTouchDownX) > this.mCustomTouchSlop) {
                    startDrag(motionEvent);
                }
            } else {
                dragging(motionEvent);
            }
        } else {
            onTouchStateChanged(false);
            removeLongPressRunnable();
            this.mClipBox.setState(0);
            setShowProgress(true, false);
            ZoomState zoomState2 = this.mZoomState;
            if (zoomState2 == ZoomState.ZOOMED) {
                if (this.mIsAutoMoving) {
                    stopAutoMove();
                }
                recoverySequenceFrameToNormal();
            } else if (zoomState2 == ZoomState.ZOOMING) {
                Animator animator = this.mZoomAnimation;
                if (animator != null) {
                    animator.cancel();
                }
                recoverySequenceFrameToNormal();
            } else if (zoomState2 == ZoomState.NORMAL) {
                updateClipProgressDrawable();
            }
            this.mDragState = DragState.NOT_DRAGGING;
        }
        return true;
    }

    public final void onZoomStateChanged(ZoomState zoomState) {
        SeekBarCallback seekBarCallback = this.mSeekBarCallback;
        if (seekBarCallback != null) {
            seekBarCallback.onZoomStateChanged(zoomState);
        }
    }

    public final void playZoomAnimation(int i, int i2, int i3, int i4) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500L);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(ObjectAnimator.ofInt(this, "VideoBarStartTime", i, i2), ObjectAnimator.ofInt(this, "VideoBarEndTime", i3, i4));
        animatorSet.start();
        this.mZoomAnimation = animatorSet;
        ZoomState zoomState = ZoomState.ZOOMING;
        this.mZoomState = zoomState;
        onZoomStateChanged(zoomState);
        animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.2
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
                if (SingleVideoClipBar.this.mVideoBar.getStartTime() == 0 && SingleVideoClipBar.this.mVideoBar.getEndTime() == SingleVideoClipBar.this.mTotalTime) {
                    SingleVideoClipBar.this.mZoomState = ZoomState.NORMAL;
                    SingleVideoClipBar singleVideoClipBar = SingleVideoClipBar.this;
                    singleVideoClipBar.onZoomStateChanged(singleVideoClipBar.mZoomState);
                    return;
                }
                SingleVideoClipBar.this.mZoomState = ZoomState.ZOOMED;
                SingleVideoClipBar singleVideoClipBar2 = SingleVideoClipBar.this;
                singleVideoClipBar2.onZoomStateChanged(singleVideoClipBar2.mZoomState);
            }
        });
    }

    public final void zoomInSequenceFrame(int i) {
        float endTime = i / (this.mVideoBar.getEndTime() - this.mVideoBar.getStartTime());
        int i2 = this.mTotalTime;
        float f = i2 * endTime;
        float f2 = f - (f / 5.0f);
        this.mIsShowProgress = false;
        playZoomAnimation(0, Math.round(f2), this.mTotalTime, Math.round((i2 / 5) + f2));
    }

    public final void recoverySequenceFrameToNormal() {
        playZoomAnimation(this.mVideoBar.getStartTime(), 0, this.mVideoBar.getEndTime(), this.mTotalTime);
    }

    public void setTotalTime(int i) {
        DefaultLogger.d("SingleVideoClipFrameBar", "setTotalTime:" + i);
        this.mTotalTime = i;
        this.mVideoBar.setEndTime(i);
        if (this.mLockedAreaEndTime == 0) {
            this.mLockedAreaEndTime = i;
        }
        this.mVideoBar.setTotalTime(i);
    }

    public void updateTimes(int i, int i2, int i3, int i4) {
        setTotalTime(i);
        this.mLockedAreaStartTime = i2;
        this.mLockedAreaEndTime = i3;
        this.mProgressTime = i4;
        updateDrawables(true);
    }

    public void setThumbnailAspectRatio(float f) {
        this.mVideoBar.setAspectRatio(f);
        updateDrawables(true);
    }

    public void setProgress(int i) {
        this.mProgressTime = i;
        updateClipProgressDrawable();
    }

    public TouchArea getTouchArea() {
        return this.mTouchArea;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mTotalTime == 0) {
            return;
        }
        this.mVideoBar.draw(canvas);
        this.mTranslucentMaskDrawable.draw(canvas);
        this.mClipBox.draw(canvas);
        drawClipProgress(canvas);
        if (!this.mIsAutoMoving) {
            return;
        }
        autoMoveNextFrame();
    }

    public final void drawClipProgress(Canvas canvas) {
        int i;
        if (!this.mIsShowProgress || (i = this.mProgressTime) < this.mLockedAreaStartTime || i > this.mLockedAreaEndTime) {
            return;
        }
        canvas.save();
        canvas.clipRect(this.mVideoBar.getLeft() - (this.mClipProgressDrawable.getIntrinsicWidth() / 2), this.mClipBox.getTop(), this.mVideoBar.getRight() + (this.mClipProgressDrawable.getIntrinsicWidth() / 2), this.mClipBox.getBottom());
        this.mClipProgressDrawable.draw(canvas);
        canvas.restore();
    }

    public void setVideoFrameLoader(VideoFrameLoader videoFrameLoader) {
        this.mVideoBar.setFrameLoader(videoFrameLoader);
    }

    public void setVideoPath(String str) {
        this.mVideoBar.setVideoPath(str);
    }

    /* loaded from: classes2.dex */
    public class LongPressedRunnable implements Runnable {
        public LongPressedRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DefaultLogger.d("SingleVideoClipFrameBar", "run: LongPressedRunnable,mZoomState=%d,mDragState=%d,mTouchArea=%d", SingleVideoClipBar.this.mZoomState, SingleVideoClipBar.this.mDragState, SingleVideoClipBar.this.mTouchArea);
            if (SingleVideoClipBar.this.mZoomState == ZoomState.NORMAL) {
                if (SingleVideoClipBar.this.mTouchArea != TouchArea.LEFT_AXIS) {
                    if (SingleVideoClipBar.this.mTouchArea != TouchArea.RIGHT_AXIS) {
                        return;
                    }
                    SingleVideoClipBar singleVideoClipBar = SingleVideoClipBar.this;
                    singleVideoClipBar.zoomInSequenceFrame(singleVideoClipBar.mLockedAreaEndTime);
                    return;
                }
                SingleVideoClipBar singleVideoClipBar2 = SingleVideoClipBar.this;
                singleVideoClipBar2.zoomInSequenceFrame(singleVideoClipBar2.mLockedAreaStartTime);
            }
        }
    }
}
