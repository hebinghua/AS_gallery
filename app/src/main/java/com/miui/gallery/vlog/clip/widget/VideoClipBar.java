package com.miui.gallery.vlog.clip.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$drawable;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class VideoClipBar extends View {
    public static int EDGE_WIDTH;
    public int mBarWidth;
    public float mCurrentDragX;
    public Drawable mDragBar;
    public Drawable mDragBarDisable;
    public Drawable mDragBarEnable;
    public int mEndOffset;
    public boolean mIsAddHaptic;
    public boolean mIsDragEnable;
    public boolean mIsDragLeftBar;
    public boolean mIsDragRightBar;
    public boolean mIsDragToLeftEdge;
    public boolean mIsMoved;
    public int mLeft;
    public int mLeftDelta;
    public OnVideoClipRegionChangedListener mListener;
    public int mMaxWidth;
    public int mMinWidth;
    public Runnable mPendingScrollRunnable;
    public double mRatio;
    public Scroller mReboundScroller;
    public int mRightDelta;
    public int mScrollReferPosition;
    public Runnable mScrollRunnable;
    public Scroller mScroller;
    public int mStartOffset;
    public float mTouchInterval;
    public int mTouchSlop;

    /* loaded from: classes2.dex */
    public interface OnVideoClipRegionChangedListener {
        void onVideoClipRegionChanged(boolean z, int i, int i2, int i3, int i4);

        void onVideoClipRegionDragEnd(int i, int i2, int i3, int i4);

        void onVideoClipRegionDragStart();

        void onVideoClipRegionScroll(int i);
    }

    public static /* synthetic */ void $r8$lambda$PKPQQYwubWM5OawQ8SsNarWWBw0(VideoClipBar videoClipBar, boolean z) {
        videoClipBar.lambda$startPendingScroll$0(z);
    }

    public VideoClipBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsDragEnable = true;
        this.mIsAddHaptic = true;
        this.mScrollRunnable = new Runnable() { // from class: com.miui.gallery.vlog.clip.widget.VideoClipBar.1
            {
                VideoClipBar.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (VideoClipBar.this.mScroller == null || !VideoClipBar.this.mScroller.computeScrollOffset()) {
                    VideoClipBar.this.stopScroll("complete");
                } else {
                    int currX = VideoClipBar.this.mScroller.getCurrX();
                    if (!VideoClipBar.this.mIsDragLeftBar) {
                        if (VideoClipBar.this.mIsDragRightBar) {
                            VideoClipBar videoClipBar = VideoClipBar.this;
                            videoClipBar.mEndOffset = videoClipBar.fixEndOffset(currX);
                            VideoClipBar videoClipBar2 = VideoClipBar.this;
                            videoClipBar2.mLeft = (int) (videoClipBar2.getFixDragX() - VideoClipBar.this.getClipWidth());
                            VideoClipBar.this.onVideoClipRegionChanged(false);
                        }
                    } else {
                        VideoClipBar videoClipBar3 = VideoClipBar.this;
                        videoClipBar3.mStartOffset = videoClipBar3.fixStartOffset(currX);
                        VideoClipBar videoClipBar4 = VideoClipBar.this;
                        videoClipBar4.mLeft = (int) videoClipBar4.getFixDragX();
                        VideoClipBar.this.onVideoClipRegionChanged(true);
                    }
                    VideoClipBar.this.adjustScrollSpeed();
                    VideoClipBar.this.invalidate();
                }
                if (VideoClipBar.this.mReboundScroller == null || !VideoClipBar.this.mReboundScroller.computeScrollOffset()) {
                    VideoClipBar.this.stopReboundScroll();
                    return;
                }
                VideoClipBar videoClipBar5 = VideoClipBar.this;
                videoClipBar5.mLeft = videoClipBar5.mReboundScroller.getCurrX();
                if (VideoClipBar.this.mListener != null) {
                    VideoClipBar.this.mListener.onVideoClipRegionScroll(VideoClipBar.this.mLeft);
                }
                VideoClipBar.this.invalidate();
            }
        };
        init();
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        EDGE_WIDTH = (int) (ScreenUtils.getCurScreenWidth() * 0.25f);
    }

    public final void init() {
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        EDGE_WIDTH = (int) (ScreenUtils.getCurScreenWidth() * 0.25f);
        this.mDragBarEnable = getResources().getDrawable(R$drawable.vlog_clip_frame_enable);
        this.mDragBarDisable = getResources().getDrawable(R$drawable.vlog_clip_frame_disable);
        this.mDragBar = this.mDragBarEnable;
    }

    public void setBarWidth(int i) {
        this.mBarWidth = i;
    }

    public void setListener(OnVideoClipRegionChangedListener onVideoClipRegionChangedListener) {
        this.mListener = onVideoClipRegionChangedListener;
    }

    public void setClipRegion(int i, int i2, int i3, int i4, int i5, int i6, int i7, double d) {
        EDGE_WIDTH = (int) (ScreenUtils.getCurScreenWidth() * 0.25f);
        this.mRatio = d;
        this.mStartOffset = convert(i);
        this.mEndOffset = convert(i2);
        this.mMaxWidth = convert(i3);
        this.mMinWidth = convert(i4);
        this.mLeft = i5;
        this.mLeftDelta = i6;
        this.mRightDelta = i7;
        updateDragBar();
        invalidate();
    }

    public final int convert(int i) {
        return (int) (i * this.mRatio);
    }

    public final int convertReverse(int i) {
        return (int) (i / this.mRatio);
    }

    public void setDragEnable(boolean z) {
        this.mIsDragEnable = z;
    }

    public int getClipLeft() {
        return this.mLeft;
    }

    public int getClipLeftDelata() {
        return this.mLeftDelta;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int clipWidth = this.mLeft + getClipWidth();
        Drawable drawable = this.mDragBar;
        int i = this.mLeft;
        int i2 = this.mBarWidth;
        drawable.setBounds(i - i2, 0, clipWidth + i2, getHeight());
        this.mDragBar.draw(canvas);
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0017, code lost:
        if (r5 != 3) goto L12;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            android.widget.Scroller r0 = r4.mReboundScroller
            r1 = 1
            if (r0 == 0) goto L6
            return r1
        L6:
            float r0 = r5.getX()
            int r5 = r5.getAction()
            r2 = 0
            if (r5 == 0) goto L53
            r3 = 2
            if (r5 == r1) goto L1b
            if (r5 == r3) goto L91
            r1 = 3
            if (r5 == r1) goto L1b
            goto L96
        L1b:
            boolean r5 = r4.mIsMoved
            if (r5 != 0) goto L22
            r4.onMoveEvent(r0)
        L22:
            int r5 = r4.getWidth()
            int r5 = r5 / r3
            boolean r0 = r4.mIsDragLeftBar
            if (r0 == 0) goto L33
            int r0 = r4.mLeft
            int r5 = r5 + (-5)
            r4.startReboundScroll(r0, r5)
            goto L49
        L33:
            boolean r0 = r4.mIsDragRightBar
            if (r0 == 0) goto L49
            int r0 = r4.mLeft
            int r1 = r4.getClipWidth()
            int r5 = r5 - r1
            int r1 = r4.mLeftDelta
            int r5 = r5 + r1
            int r1 = r4.mRightDelta
            int r5 = r5 + r1
            int r5 = r5 + 5
            r4.startReboundScroll(r0, r5)
        L49:
            r4.resetDragState()
            r4.updateDragBar()
            r4.invalidate()
            goto L96
        L53:
            boolean r5 = r4.mIsDragEnable
            if (r5 != 0) goto L58
            return r2
        L58:
            r4.mIsMoved = r2
            r4.resetDragState()
            boolean r5 = r4.isDragLeftBar(r0)
            if (r5 == 0) goto L6d
            int r5 = r4.mLeft
            float r5 = (float) r5
            float r5 = r0 - r5
            r4.mTouchInterval = r5
            r4.mIsDragLeftBar = r1
            goto L81
        L6d:
            boolean r5 = r4.isDragRightBar(r0)
            if (r5 == 0) goto L81
            int r5 = r4.mLeft
            int r3 = r4.getClipWidth()
            int r5 = r5 + r3
            float r5 = (float) r5
            float r5 = r0 - r5
            r4.mTouchInterval = r5
            r4.mIsDragRightBar = r1
        L81:
            boolean r5 = r4.isDragging()
            if (r5 == 0) goto L91
            r4.mCurrentDragX = r0
            com.miui.gallery.vlog.clip.widget.VideoClipBar$OnVideoClipRegionChangedListener r5 = r4.mListener
            if (r5 == 0) goto L90
            r5.onVideoClipRegionDragStart()
        L90:
            return r1
        L91:
            r4.mIsMoved = r1
            r4.onMoveEvent(r0)
        L96:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.clip.widget.VideoClipBar.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public void computeScroll() {
        postOnAnimation(this.mScrollRunnable);
    }

    public final void onMoveEvent(float f) {
        if (isDragging()) {
            float f2 = f - this.mCurrentDragX;
            this.mCurrentDragX = f;
            if (this.mPendingScrollRunnable != null || this.mScroller != null) {
                float fixDragX = getFixDragX();
                if (this.mIsDragToLeftEdge) {
                    this.mScrollReferPosition = (int) Math.min(this.mScrollReferPosition, fixDragX);
                } else {
                    this.mScrollReferPosition = (int) Math.max(this.mScrollReferPosition, fixDragX);
                }
                boolean z = this.mIsDragToLeftEdge;
                boolean z2 = (!z || fixDragX >= ((float) (this.mScrollReferPosition + this.mTouchSlop))) && (z || fixDragX <= ((float) (this.mScrollReferPosition - this.mTouchSlop)));
                if (this.mScroller != null) {
                    if (!z2) {
                        return;
                    }
                    stopScroll("drag");
                } else if (z2) {
                    cancelPendingScroll("drag");
                }
            }
            int width = getWidth();
            if (this.mIsDragLeftBar) {
                this.mStartOffset = fixStartOffset((int) (this.mStartOffset + f2 + 0.5f));
                int fixDragX2 = (int) getFixDragX();
                this.mLeft = fixDragX2;
                int i = EDGE_WIDTH;
                if (fixDragX2 >= width - i && f2 > 0.0f) {
                    startPendingScroll(false);
                } else if (fixDragX2 <= i && f2 < 0.0f) {
                    startPendingScroll(true);
                }
                onVideoClipRegionChanged(true);
                invalidate();
            } else if (!this.mIsDragRightBar) {
            } else {
                this.mEndOffset = fixEndOffset((int) ((this.mEndOffset - f2) + 0.5f));
                int clipWidth = getClipWidth();
                int fixDragX3 = (int) getFixDragX();
                this.mLeft = fixDragX3 - clipWidth;
                int i2 = EDGE_WIDTH;
                if (fixDragX3 <= i2 && f2 < 0.0f) {
                    startPendingScroll(true);
                } else if (fixDragX3 >= width - i2 && f2 > 0.0f) {
                    startPendingScroll(false);
                }
                onVideoClipRegionChanged(false);
                invalidate();
            }
        }
    }

    public final void startPendingScroll(final boolean z) {
        if (this.mPendingScrollRunnable == null) {
            this.mIsDragToLeftEdge = z;
            this.mScrollReferPosition = (int) getFixDragX();
            Runnable runnable = new Runnable() { // from class: com.miui.gallery.vlog.clip.widget.VideoClipBar$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoClipBar.$r8$lambda$PKPQQYwubWM5OawQ8SsNarWWBw0(VideoClipBar.this, z);
                }
            };
            this.mPendingScrollRunnable = runnable;
            postDelayed(runnable, 0L);
        }
    }

    public /* synthetic */ void lambda$startPendingScroll$0(boolean z) {
        if (this.mIsDragLeftBar) {
            if (z) {
                startScroll(this.mStartOffset, 0);
            } else {
                startScroll(this.mStartOffset, (this.mMaxWidth - this.mEndOffset) - this.mMinWidth);
            }
        } else if (z) {
            startScroll(this.mEndOffset, (this.mMaxWidth - this.mStartOffset) - this.mMinWidth);
        } else {
            startScroll(this.mEndOffset, 0);
        }
        this.mPendingScrollRunnable = null;
    }

    public final void cancelPendingScroll(String str) {
        if (this.mPendingScrollRunnable != null) {
            DefaultLogger.d("VideoClipBar", "cancelPendingScroll %s", str);
            removeCallbacks(this.mPendingScrollRunnable);
            this.mPendingScrollRunnable = null;
        }
    }

    public final void startScroll(int i, int i2) {
        if (this.mScroller != null || i == i2) {
            return;
        }
        Scroller scroller = new Scroller(getContext(), new LinearInterpolator());
        this.mScroller = scroller;
        int i3 = i2 - i;
        scroller.startScroll(i, 0, i3, 0, Math.abs((int) (i3 / getScrollSpeed())));
        invalidate();
    }

    public final void stopScroll(String str) {
        if (this.mScroller != null) {
            DefaultLogger.d("VideoClipBar", "stopScroll %s", str);
            this.mScroller = null;
        }
    }

    public final void adjustScrollSpeed() {
        Scroller scroller = this.mScroller;
        if (scroller == null || scroller.isFinished()) {
            return;
        }
        int currX = this.mScroller.getCurrX();
        int finalX = this.mScroller.getFinalX() - currX;
        this.mScroller.startScroll(currX, 0, finalX, 0, Math.abs((int) (finalX / getScrollSpeed())));
    }

    private float getScrollSpeed() {
        int width;
        int fixDragX = (int) getFixDragX();
        if (this.mIsDragToLeftEdge) {
            if (this.mIsDragLeftBar) {
                fixDragX -= this.mBarWidth;
            }
        } else {
            if (this.mIsDragLeftBar) {
                width = getWidth();
            } else {
                width = getWidth() - fixDragX;
                fixDragX = this.mBarWidth;
            }
            fixDragX = width - fixDragX;
        }
        return ((1.0f - ((Math.max(Math.min(EDGE_WIDTH, fixDragX), 0) * 1.0f) / EDGE_WIDTH)) * 1.0500001f) + 0.15f;
    }

    public final void startReboundScroll(int i, int i2) {
        if (this.mReboundScroller == null) {
            DefaultLogger.d("VideoClipBar", "startReboundScroll from %d to %d", Integer.valueOf(i), Integer.valueOf(i2));
            if (i != i2) {
                Scroller scroller = new Scroller(getContext(), new CubicEaseOutInterpolator());
                this.mReboundScroller = scroller;
                int i3 = i2 - i;
                scroller.startScroll(i, 0, i3, 0, Math.abs((int) (i3 / 1.0f)));
                invalidate();
                return;
            }
            onDragEnd();
        }
    }

    public final void stopReboundScroll() {
        if (this.mReboundScroller != null) {
            DefaultLogger.d("VideoClipBar", "stopReboundScroll");
            this.mReboundScroller = null;
            onDragEnd();
        }
    }

    public final void onDragEnd() {
        DefaultLogger.d("VideoClipBar", "onDragEnd");
        OnVideoClipRegionChangedListener onVideoClipRegionChangedListener = this.mListener;
        if (onVideoClipRegionChangedListener != null) {
            onVideoClipRegionChangedListener.onVideoClipRegionDragEnd(convertReverse(this.mMaxWidth), convertReverse(this.mStartOffset), convertReverse(this.mEndOffset), this.mLeft);
        }
    }

    public final void onVideoClipRegionChanged(boolean z) {
        if (this.mDragBar == this.mDragBarDisable) {
            if (this.mIsAddHaptic) {
                LinearMotorHelper.performHapticFeedback(getRootView(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
                this.mIsAddHaptic = false;
            }
        } else {
            this.mIsAddHaptic = true;
        }
        updateDragBar();
        OnVideoClipRegionChangedListener onVideoClipRegionChangedListener = this.mListener;
        if (onVideoClipRegionChangedListener != null) {
            onVideoClipRegionChangedListener.onVideoClipRegionChanged(z, convertReverse(this.mMaxWidth), convertReverse(this.mStartOffset), convertReverse(this.mEndOffset), this.mLeft);
        }
    }

    public final void updateDragBar() {
        int clipWidth = getClipWidth();
        this.mDragBar = (!isDragging() || (clipWidth < this.mMaxWidth && clipWidth > this.mMinWidth && ((!this.mIsDragLeftBar || this.mStartOffset != 0) && (!this.mIsDragRightBar || this.mEndOffset != 0)))) ? this.mDragBarEnable : this.mDragBarDisable;
    }

    public final void resetDragState() {
        this.mIsDragLeftBar = false;
        this.mIsDragRightBar = false;
        this.mCurrentDragX = 0.0f;
        this.mTouchInterval = 0.0f;
        stopScroll("reset");
        cancelPendingScroll("reset");
    }

    public final int fixStartOffset(int i) {
        if (i < 0) {
            i = 0;
        }
        int i2 = this.mMaxWidth;
        int i3 = this.mMinWidth;
        if (i2 <= i3) {
            return 0;
        }
        int i4 = this.mEndOffset;
        return (i2 - i) - i4 < i3 ? (i2 - i4) - i3 : i;
    }

    public final int fixEndOffset(int i) {
        if (i < 0) {
            i = 0;
        }
        int i2 = this.mMaxWidth;
        int i3 = this.mMinWidth;
        if (i2 <= i3) {
            return 0;
        }
        int i4 = this.mStartOffset;
        return (i2 - i) - i4 < i3 ? (i2 - i4) - i3 : i;
    }

    public float getFixDragX() {
        return this.mCurrentDragX - this.mTouchInterval;
    }

    public int getClipWidth() {
        return (this.mMaxWidth - this.mStartOffset) - this.mEndOffset;
    }

    public final boolean isDragging() {
        return this.mIsDragLeftBar || this.mIsDragRightBar;
    }

    public final boolean isDragLeftBar(float f) {
        int i = this.mLeft;
        return f >= ((float) ((i - this.mBarWidth) + (-10))) && f <= ((float) (i + 10));
    }

    public final boolean isDragRightBar(float f) {
        int clipWidth = this.mLeft + getClipWidth();
        return f >= ((float) (clipWidth + (-10))) && f <= ((float) ((clipWidth + this.mBarWidth) + 10));
    }
}
