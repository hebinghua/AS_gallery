package com.miui.gallery.widget.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import androidx.annotation.Keep;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsule;
import com.miui.gallery.widget.recyclerview.FastScrollerThumbView;
import com.miui.gallery.widget.recyclerview.ProportionTagListWrapper;
import com.miui.gallery.widget.recyclerview.transition.PhysicBasedInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes3.dex */
public class FastScrollerBar extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener, FastScrollerThumbView.OnAnimatorListener, FastScrollerCapsule.OnAnimatorListener, FastScrollerCapsule.OnLocationChangedListener, ProportionTagListWrapper.OnAnimatorListener {
    public int mBottomMargin;
    public FastScrollerCapsuleCalculator mCapsuleCalculator;
    public ObjectAnimator mExpandCapsuleMarginAnimator;
    public FastScrollerCapsule mFastScrollerCapsule;
    public int mFastScrollerDragInitX;
    public int mFastScrollerDragInitY;
    public float mFastScrollerDragX;
    public FastScrollerThumbView mFastScrollerThumb;
    public int mFastScrollerThumbInitX;
    public int mFastScrollerThumbInitY;
    public int mFastScrollerThumbMargin;
    public int mFastScrollerThumbX;
    public int mFastScrollerThumbY;
    public int mFastThumbTouchAreaMarginEnd;
    public int mFastThumbTouchAreaMarginStart;
    public int mFastThumbTouchAreaMarginY;
    public int mFastTimeCapsuleMargin;
    public int mHorizontalMargin;
    public boolean mIsFastScrollerInvisible;
    public boolean mIsFastScrollerPressed;
    public boolean mIsInRight;
    public boolean mIsProportionTagViewPressed;
    public int mLayoutOrientation;
    public boolean mNeedDrawCapsule;
    public int mOffsetToThumbTop;
    public OnStateChangedListener mOnStateChangedListener;
    public ProportionTagListWrapper<Integer> mProportionTagListWrapper;
    public final GalleryRecyclerView mRecyclerView;
    public boolean mRecyclerViewChanged;
    public ObjectAnimator mResetCapsuleMarginAnimator;
    public ObjectAnimator mResetPositionAnimator;
    public ObjectAnimator mScrollAnimation;
    public int mScrollSlop;
    public int mScrollbarMinimumRange;
    public ProportionTagAdapterProvider<Integer> mTagAdapterProvider;
    public ProportionTagView mTargetProportionTagView;
    public int mTopMargin;
    public boolean mTopMarginChanged;
    public int mFastTimeCapsuleX = -1;
    public int mFastTimeCapsuleY = -1;
    public int mRecyclerViewWidth = 0;
    public int mRecyclerViewHeight = 0;
    public int mState = 0;
    public int mDragState = 0;
    public int mAnimationState = 0;
    public int mTimeCapsuleAnimationState = 0;
    public int mProportionTagAnimationState = 0;
    public int mProportionTagViewAnimationState = 2;
    public final int[] mVerticalRange = new int[2];
    public final int[] mHorizontalRange = new int[2];
    public boolean mNeedVerticalScrollbar = false;
    public boolean mNeedHorizontalScrollbar = false;
    public RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerBar.1
        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            if (FastScrollerBar.this.mIsFastScrollerInvisible) {
                FastScrollerBar.this.hideScrollerBar();
                FastScrollerBar.this.hideCapsule();
                FastScrollerBar.this.hideProportionTag();
            } else if (FastScrollerBar.this.isDragging() || i2 == 0) {
            } else {
                FastScrollerBar.this.updateThumbPositionByScrolling();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            if (i != 1) {
                FastScrollerBar.this.setState(0);
            } else if (FastScrollerBar.this.mIsFastScrollerPressed) {
            } else {
                FastScrollerBar.this.hideCapsuleByAnimator(10L);
                FastScrollerBar.this.hideProportionTagByAnimator(10);
            }
        }
    };
    public boolean mNeedDispatchFakeEvent = false;

    /* loaded from: classes3.dex */
    public interface OnStateChangedListener {
        void onStateChanged(int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListener = onStateChangedListener;
    }

    public FastScrollerBar(GalleryRecyclerView galleryRecyclerView, FastScrollerThumbView fastScrollerThumbView) {
        this.mRecyclerView = galleryRecyclerView;
        this.mFastScrollerThumb = fastScrollerThumbView;
        fastScrollerThumbView.setOnAnimatorListener(this);
        this.mFastScrollerThumbMargin = this.mFastScrollerThumb.getThumbMargin();
        this.mScrollbarMinimumRange = this.mFastScrollerThumb.getViewHeight();
        if (this.mFastScrollerThumb.isVertical()) {
            if (isLayoutRTL()) {
                this.mIsInRight = false;
                int i = this.mFastScrollerThumbMargin;
                this.mFastThumbTouchAreaMarginStart = i;
                this.mFastThumbTouchAreaMarginEnd = (160 - i) - this.mFastScrollerThumb.getViewWidth();
            } else {
                this.mIsInRight = true;
                this.mFastThumbTouchAreaMarginStart = (160 - this.mFastScrollerThumbMargin) - this.mFastScrollerThumb.getViewWidth();
                this.mFastThumbTouchAreaMarginEnd = this.mFastScrollerThumbMargin;
            }
        }
        this.mFastThumbTouchAreaMarginY = (160 - this.mFastScrollerThumb.getViewHeight()) >> 1;
        this.mScrollSlop = ViewConfiguration.get(galleryRecyclerView.getContext()).getScaledTouchSlop();
        this.mLayoutOrientation = galleryRecyclerView.getContext().getResources().getConfiguration().orientation;
    }

    public void setDefaultPosition() {
        if (this.mFastScrollerThumb.isVertical()) {
            int viewWidth = isLayoutRTL() ? this.mFastScrollerThumbMargin : (this.mRecyclerViewWidth - this.mFastScrollerThumb.getViewWidth()) - this.mFastScrollerThumbMargin;
            this.mFastScrollerThumbInitX = viewWidth;
            this.mFastScrollerThumbX = viewWidth;
        } else {
            int viewHeight = (this.mRecyclerViewHeight - this.mFastScrollerThumb.getViewHeight()) - this.mFastScrollerThumbMargin;
            this.mFastScrollerThumbInitY = viewHeight;
            this.mFastScrollerThumbY = viewHeight;
        }
        if (isNeedDrawCapsule()) {
            if (this.mIsInRight) {
                this.mFastTimeCapsuleX = (this.mFastScrollerThumbX - this.mFastTimeCapsuleMargin) - this.mFastScrollerCapsule.getViewWidth();
            } else {
                this.mFastTimeCapsuleX = this.mFastScrollerThumbX + this.mFastScrollerThumb.getViewWidth() + this.mFastTimeCapsuleMargin;
            }
            this.mFastTimeCapsuleY = this.mFastScrollerThumbY + ((this.mFastScrollerThumb.getViewHeight() - this.mFastScrollerCapsule.getViewHeight()) / 2);
        }
    }

    public void setStyle(int i) {
        this.mFastScrollerThumb.setStyle(i);
    }

    public void setCapsuleStyle(int i) {
        FastScrollerCapsule fastScrollerCapsule = this.mFastScrollerCapsule;
        if (fastScrollerCapsule != null) {
            fastScrollerCapsule.setStyle(i);
        }
    }

    public void setTopMargin(int i) {
        this.mTopMargin = i;
        this.mTopMarginChanged = true;
    }

    public void setBottomMargin(int i) {
        this.mBottomMargin = i;
    }

    public int getBottomMargin() {
        return this.mBottomMargin;
    }

    public int getTopMargin() {
        return this.mTopMargin;
    }

    public boolean isFastScrollerPressed() {
        return this.mIsFastScrollerPressed;
    }

    public boolean isProportionTagViewPressed() {
        return this.mIsProportionTagViewPressed;
    }

    public void setFastScrollerInvisible(boolean z) {
        this.mIsFastScrollerInvisible = z;
    }

    public final void setState(int i) {
        if (i == 2 && this.mState != 2) {
            this.mFastScrollerThumb.cancelHideScrollerBarAnimator();
            if (isNeedDrawCapsule()) {
                showCapsuleByAnimator();
                expandCapsuleMarginByAnimator();
            }
            freshTagProportions();
        }
        if (i == 3 && this.mState != 3) {
            recordFastScrollerBarDragging();
        }
        if (i == 0) {
            if (this.mState == 0) {
                requestRedraw();
            }
            int i2 = this.mState;
            if (i2 == 3 || i2 == 2 || i2 == 4) {
                hideScrollerBar(5000);
                if (this.mFastScrollerCapsule != null) {
                    hideCapsuleByAnimator(2000L);
                }
                if (this.mProportionTagListWrapper != null) {
                    hideProportionTagByAnimator(2000);
                }
                int i3 = this.mState;
                if (i3 == 3 || i3 == 2) {
                    resetScrollerBarPosition();
                }
            } else if (i2 != 0) {
                hideScrollerBar(3000);
            }
        } else {
            showScrollerBar();
            if (i == 5) {
                showCapsuleByAnimator();
                showProportionTagByAnimator();
            }
        }
        if (this.mState != i) {
            OnStateChangedListener onStateChangedListener = this.mOnStateChangedListener;
            if (onStateChangedListener != null) {
                onStateChangedListener.onStateChanged(i);
            }
            this.mState = i;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        int i = this.mRecyclerView.getContext().getResources().getConfiguration().orientation;
        if (this.mLayoutOrientation != i || this.mRecyclerViewWidth == 0 || this.mRecyclerViewHeight == 0) {
            this.mLayoutOrientation = i;
            this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
            this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
            setDefaultPosition();
            setState(0);
            return;
        }
        if (this.mTopMarginChanged) {
            int[] verticalRange = getVerticalRange();
            this.mFastScrollerThumbY = Math.min(verticalRange[1], verticalRange[0]);
            this.mTopMarginChanged = false;
        }
        if (this.mRecyclerViewWidth != this.mRecyclerView.getWidth()) {
            this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
            setDefaultPosition();
            this.mRecyclerViewChanged = true;
        }
        if (this.mRecyclerViewHeight != this.mRecyclerView.getHeight()) {
            this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
            this.mRecyclerViewChanged = true;
        }
        ProportionTagListWrapper<Integer> proportionTagListWrapper = this.mProportionTagListWrapper;
        if (proportionTagListWrapper != null && this.mProportionTagAnimationState != 0) {
            proportionTagListWrapper.draw(this.mRecyclerViewWidth, isLayoutRTL(), canvas);
        }
        if (isNeedDrawCapsule() && this.mTimeCapsuleAnimationState != 0) {
            if (this.mIsInRight) {
                this.mFastTimeCapsuleX = (this.mFastScrollerThumbX - this.mFastTimeCapsuleMargin) - this.mFastScrollerCapsule.getViewWidth();
            } else {
                this.mFastTimeCapsuleX = this.mFastScrollerThumbX + this.mFastScrollerThumb.getViewWidth() + this.mFastTimeCapsuleMargin;
            }
            int viewHeight = this.mFastScrollerThumbY + ((this.mFastScrollerThumb.getViewHeight() - this.mFastScrollerCapsule.getViewHeight()) / 2);
            this.mFastTimeCapsuleY = viewHeight;
            int i2 = this.mFastTimeCapsuleX;
            canvas.translate(i2, viewHeight);
            this.mFastScrollerCapsule.draw(canvas);
            canvas.translate(-i2, -viewHeight);
        }
        if (this.mAnimationState == 0) {
            return;
        }
        if (!this.mNeedVerticalScrollbar && !this.mNeedHorizontalScrollbar) {
            return;
        }
        int i3 = this.mFastScrollerThumbInitX;
        int i4 = this.mFastScrollerThumbY;
        canvas.translate(i3, i4);
        this.mFastScrollerThumb.draw(canvas);
        canvas.translate(-i3, -i4);
    }

    public void freshTagProportions() {
        ProportionTagAdapterProvider<Integer> proportionTagAdapterProvider;
        ProportionTagBaseAdapter<Integer> createTagAdapter;
        int[] verticalRange = getVerticalRange();
        int i = verticalRange[1] - verticalRange[0];
        if (i == 0 || (proportionTagAdapterProvider = this.mTagAdapterProvider) == null || !proportionTagAdapterProvider.isShowProportionTag() || (createTagAdapter = this.mTagAdapterProvider.createTagAdapter()) == null) {
            return;
        }
        if (this.mTagAdapterProvider.isProportionTagChanged() || this.mRecyclerViewChanged) {
            if (this.mProportionTagListWrapper == null) {
                ProportionTagListWrapper<Integer> proportionTagListWrapper = new ProportionTagListWrapper<>(createTagAdapter);
                this.mProportionTagListWrapper = proportionTagListWrapper;
                proportionTagListWrapper.setOnAnimatorListener(this);
                this.mProportionTagListWrapper.setIsLayoutRTL(isLayoutRTL());
            }
            DefaultLogger.d("FastScrollerBar", "start calculate");
            long currentTimeMillis = System.currentTimeMillis();
            this.mProportionTagListWrapper.refreshViews(this.mTagAdapterProvider.getProportionTagModel(), this.mTopMargin, i);
            DefaultLogger.d("FastScrollerBar", "calculate finish,last %s ms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            if (this.mRecyclerViewChanged) {
                this.mRecyclerViewChanged = false;
            }
        }
        showProportionTagByAnimator();
    }

    public void showScrollerBar() {
        int i = this.mAnimationState;
        if (i == 0) {
            this.mAnimationState = 1;
            this.mFastScrollerThumb.showScrollerBarAnimator();
        } else if (i != 3) {
        } else {
            this.mFastScrollerThumb.cancelHideScrollerBarAnimator();
            this.mAnimationState = 2;
            this.mFastScrollerThumb.setVisible();
        }
    }

    public void hideScrollerBar() {
        this.mAnimationState = 0;
        this.mFastScrollerThumb.setInvisible();
    }

    public void hideScrollerBar(int i) {
        int i2 = this.mAnimationState;
        if (i2 == 1) {
            this.mFastScrollerThumb.cancelShowScrollerBarAnimator();
            this.mFastScrollerThumb.setVisible();
        } else if (i2 != 2) {
            return;
        }
        this.mAnimationState = 3;
        this.mFastScrollerThumb.hideScrollerBarAnimator(i);
    }

    public void showCapsuleByAnimator() {
        int i = this.mTimeCapsuleAnimationState;
        if (i == 0) {
            this.mFastScrollerCapsule.showCapsuleByAnimator();
        } else if (i != 1 && i != 2 && i != 3) {
        } else {
            this.mFastScrollerCapsule.cancelHideCapsule();
            this.mFastScrollerCapsule.setVisible();
            this.mTimeCapsuleAnimationState = 2;
        }
    }

    public void hideCapsuleByAnimator(long j) {
        int i = this.mTimeCapsuleAnimationState;
        if (i != 1) {
            if (i != 2) {
                return;
            }
            this.mFastScrollerCapsule.hideCapsuleByAnimator(j);
            return;
        }
        this.mFastScrollerCapsule.cancelShowCapsule();
        this.mFastScrollerCapsule.setVisible();
        this.mFastScrollerCapsule.hideCapsuleByAnimator(j);
    }

    public void hideCapsule() {
        FastScrollerCapsule fastScrollerCapsule = this.mFastScrollerCapsule;
        if (fastScrollerCapsule != null) {
            fastScrollerCapsule.cancelHideCapsule();
            this.mFastScrollerCapsule.hideCapsule();
            this.mTimeCapsuleAnimationState = 0;
        }
    }

    public void showProportionTagByAnimator() {
        int i = this.mProportionTagAnimationState;
        if (i == 0) {
            this.mProportionTagListWrapper.showTagByAnimator();
        } else if (i != 1 && i != 2 && i != 3) {
        } else {
            this.mProportionTagListWrapper.cancelHideTagByAnimator();
            this.mProportionTagListWrapper.setVisible();
            this.mProportionTagAnimationState = 2;
        }
    }

    public void hideProportionTagByAnimator(int i) {
        int i2 = this.mProportionTagAnimationState;
        if (i2 != 1) {
            if (i2 != 2) {
                return;
            }
            this.mProportionTagListWrapper.hideTagByAnimator(i);
            return;
        }
        this.mProportionTagListWrapper.cancelShowTagByAnimator();
        this.mProportionTagListWrapper.setVisible();
        this.mProportionTagListWrapper.hideTagByAnimator(i);
    }

    public void hideProportionTag() {
        ProportionTagListWrapper<Integer> proportionTagListWrapper = this.mProportionTagListWrapper;
        if (proportionTagListWrapper != null) {
            proportionTagListWrapper.cancelHideTagByAnimator();
            this.mProportionTagListWrapper.setInvisible();
            this.mProportionTagAnimationState = 0;
        }
    }

    public void enLargeProportionTagViewByAnimator(ProportionTagView proportionTagView) {
        if (this.mProportionTagViewAnimationState == 3) {
            this.mProportionTagListWrapper.cancelScaleSmallAnimator();
        }
        this.mProportionTagViewAnimationState = 1;
        this.mProportionTagListWrapper.scaleLargeAnimator(proportionTagView);
    }

    public void smallerProportionTagViewByAnimator(ProportionTagView proportionTagView) {
        if (this.mProportionTagViewAnimationState == 1) {
            this.mProportionTagListWrapper.cancelScaleLargeAnimator();
        }
        this.mProportionTagViewAnimationState = 3;
        this.mProportionTagListWrapper.scaleSmallAnimator(proportionTagView);
    }

    public void updateThumbPositionByDragging(int i, int i2) {
        ProportionTagAdapterProvider<Integer> proportionTagAdapterProvider;
        ProportionTagListWrapper<Integer> proportionTagListWrapper;
        int i3 = this.mDragState;
        if (i3 == 1 || i3 == 2) {
            this.mNeedHorizontalScrollbar = true;
        }
        if (i3 == 3 || i3 == 4) {
            this.mNeedVerticalScrollbar = true;
        }
        int viewWidth = this.mFastScrollerThumb.getViewWidth() / 2;
        if (i < viewWidth) {
            i = viewWidth;
        }
        int i4 = this.mRecyclerViewWidth;
        if (i > i4 - viewWidth) {
            i = i4 - viewWidth;
        }
        this.mFastScrollerThumbX = i;
        if (Math.abs(this.mFastScrollerThumbY - i2) < this.mScrollSlop) {
            requestRedraw();
        }
        FastScrollerThumbView fastScrollerThumbView = this.mFastScrollerThumb;
        if (fastScrollerThumbView != null && this.mFastScrollerCapsule != null) {
            this.mFastTimeCapsuleY = ((fastScrollerThumbView.getViewHeight() - this.mFastScrollerCapsule.getViewHeight()) / 2) + i2;
        } else {
            this.mFastTimeCapsuleY = i2;
        }
        this.mFastScrollerThumbY = i2;
        if (this.mState == 3 && (proportionTagAdapterProvider = this.mTagAdapterProvider) != null && proportionTagAdapterProvider.isShowProportionTag() && (proportionTagListWrapper = this.mProportionTagListWrapper) != null && proportionTagListWrapper.isPerformHapticFeedback(this.mFastScrollerThumbY)) {
            performHapticFeedback();
        }
        changeThumbAndCapsulePositionX();
    }

    public final void performHapticFeedback() {
        if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
            LinearMotorHelper.performHapticFeedback(this.mRecyclerView.getContext(), LinearMotorHelper.HAPTIC_MESH_HEAVY);
        } else {
            this.mRecyclerView.performHapticFeedback(3);
        }
    }

    public void changeThumbAndCapsulePositionX() {
        int i;
        int i2;
        if (isLayoutLandScape()) {
            i2 = this.mRecyclerViewWidth / 2;
            i = i2;
        } else {
            int i3 = this.mRecyclerViewWidth;
            i = (i3 * 2) / 3;
            i2 = i3 / 3;
        }
        int i4 = this.mFastScrollerThumbX;
        if (i4 < i2) {
            this.mIsInRight = false;
            if (isNeedDrawCapsule()) {
                this.mFastScrollerCapsule.setIsInRight(this.mIsInRight);
                if (this.mFastScrollerThumbX > this.mFastTimeCapsuleX) {
                    this.mFastScrollerCapsule.showCapsuleByAnimator();
                }
            }
        } else if (i4 + this.mFastScrollerThumb.getViewWidth() >= i) {
            this.mIsInRight = true;
            if (isNeedDrawCapsule()) {
                this.mFastScrollerCapsule.setIsInRight(this.mIsInRight);
                if (this.mFastScrollerThumbX < this.mFastTimeCapsuleX) {
                    this.mFastScrollerCapsule.showCapsuleByAnimator();
                }
            }
        }
        if (isNeedDrawCapsule()) {
            if (this.mIsInRight) {
                this.mFastTimeCapsuleX = (this.mFastScrollerThumbX - this.mFastTimeCapsuleMargin) - this.mFastScrollerCapsule.getViewWidth();
            } else {
                this.mFastTimeCapsuleX = this.mFastScrollerThumbX + this.mFastScrollerThumb.getViewWidth() + this.mFastTimeCapsuleMargin;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0045, code lost:
        if (getProportionTagViewByPoint((int) r7.getX(), (int) r7.getY()) != null) goto L11;
     */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onInterceptTouchEvent(androidx.recyclerview.widget.RecyclerView r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            int r6 = r7.getActionMasked()
            r0 = 0
            r1 = 3
            r2 = 1
            if (r6 == 0) goto Ld
            if (r6 == r2) goto Ld
            if (r6 != r1) goto L14
        Ld:
            r3 = -1
            r5.mFastScrollerDragInitX = r3
            r5.mFastScrollerDragInitY = r3
            r5.mOffsetToThumbTop = r0
        L14:
            int r3 = r5.mAnimationState
            if (r3 == 0) goto L9f
            if (r6 != 0) goto L48
            float r6 = r7.getX()
            float r3 = r7.getY()
            boolean r6 = r5.isPointInsideFastScrollerThumb(r6, r3)
            if (r6 == 0) goto L37
            float r6 = r7.getX()
            int r6 = (int) r6
            r5.mFastScrollerDragInitX = r6
            float r6 = r7.getY()
            int r6 = (int) r6
            r5.mFastScrollerDragInitY = r6
            goto L6f
        L37:
            float r6 = r7.getX()
            int r6 = (int) r6
            float r7 = r7.getY()
            int r7 = (int) r7
            com.miui.gallery.widget.recyclerview.ProportionTagView r6 = r5.getProportionTagViewByPoint(r6, r7)
            if (r6 == 0) goto L96
            goto L6f
        L48:
            r3 = 2
            if (r6 != r3) goto L96
            int r6 = r5.mFastScrollerDragInitX
            if (r6 <= 0) goto L71
            float r6 = r7.getX()
            int r4 = r5.mFastScrollerDragInitX
            float r4 = (float) r4
            float r6 = r6 - r4
            float r6 = java.lang.Math.abs(r6)
            int r4 = r5.mScrollSlop
            float r4 = (float) r4
            int r6 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r6 <= 0) goto L71
            com.miui.gallery.widget.recyclerview.FastScrollerThumbView r6 = r5.mFastScrollerThumb
            boolean r6 = r6.isVertical()
            if (r6 == 0) goto L6d
            r5.mDragState = r2
            goto L6f
        L6d:
            r5.mDragState = r1
        L6f:
            r0 = r2
            goto L96
        L71:
            int r6 = r5.mFastScrollerDragInitY
            if (r6 <= 0) goto L96
            float r6 = r7.getY()
            int r7 = r5.mFastScrollerDragInitY
            float r7 = (float) r7
            float r6 = r6 - r7
            float r6 = java.lang.Math.abs(r6)
            r7 = 1084227584(0x40a00000, float:5.0)
            int r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1))
            if (r6 <= 0) goto L96
            com.miui.gallery.widget.recyclerview.FastScrollerThumbView r6 = r5.mFastScrollerThumb
            boolean r6 = r6.isVertical()
            if (r6 == 0) goto L92
            r5.mDragState = r3
            goto L6f
        L92:
            r6 = 4
            r5.mDragState = r6
            goto L6f
        L96:
            if (r0 == 0) goto L9a
            r5.mNeedDispatchFakeEvent = r2
        L9a:
            int r6 = r5.mState
            if (r6 != r1) goto L9f
            r0 = r2
        L9f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.FastScrollerBar.onInterceptTouchEvent(androidx.recyclerview.widget.RecyclerView, android.view.MotionEvent):boolean");
    }

    public final void dispatchFakeEventExceptSelf(MotionEvent motionEvent) {
        int itemTouchListenerCount = this.mRecyclerView.getItemTouchListenerCount();
        if (itemTouchListenerCount > 1) {
            DefaultLogger.d("FastScrollerBar", "dispatch fake event %s", motionEvent);
            for (int i = 0; i < itemTouchListenerCount; i++) {
                RecyclerView.OnItemTouchListener itemTouchListenerAt = this.mRecyclerView.getItemTouchListenerAt(i);
                if (itemTouchListenerAt != this) {
                    itemTouchListenerAt.onInterceptTouchEvent(this.mRecyclerView, motionEvent);
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        int i;
        FastScrollerCapsuleContentProvider capsuleContent;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0 || actionMasked == 1 || actionMasked == 3) {
            this.mFastScrollerDragInitX = -1;
            this.mFastScrollerDragInitY = -1;
            this.mOffsetToThumbTop = 0;
        }
        if (actionMasked == 0) {
            if (isPointInsideFastScrollerThumb(motionEvent.getX(), motionEvent.getY())) {
                this.mFastScrollerDragInitX = (int) motionEvent.getX();
                this.mFastScrollerDragInitY = (int) motionEvent.getY();
                setState(2);
                this.mIsFastScrollerPressed = true;
                int[] verticalRange = getVerticalRange();
                int i2 = verticalRange[1] - verticalRange[0];
                if (i2 != 0 && (capsuleContent = getCapsuleContent(MathUtils.clamp((this.mFastScrollerThumbY - this.mTopMargin) / i2, 0.0f, 1.0f), i2)) != null) {
                    this.mFastScrollerCapsule.setContent(capsuleContent);
                }
            } else {
                ProportionTagView proportionTagViewByPoint = getProportionTagViewByPoint((int) motionEvent.getX(), (int) motionEvent.getY());
                this.mTargetProportionTagView = proportionTagViewByPoint;
                if (proportionTagViewByPoint != null) {
                    this.mIsProportionTagViewPressed = true;
                    setState(5);
                    enLargeProportionTagViewByAnimator(this.mTargetProportionTagView);
                    recordFastScrollerBarTagClick();
                }
            }
        } else if (actionMasked == 2) {
            if (this.mState != 3) {
                if (this.mFastScrollerDragInitX > 0 && Math.abs(motionEvent.getX() - this.mFastScrollerDragInitX) > this.mScrollSlop) {
                    if (this.mFastScrollerThumb.isVertical()) {
                        this.mDragState = 1;
                        this.mOffsetToThumbTop = (int) (motionEvent.getY() - this.mFastScrollerThumbY);
                    } else {
                        this.mDragState = 3;
                    }
                    setState(3);
                } else if (this.mFastScrollerDragInitY > 0 && Math.abs(motionEvent.getY() - this.mFastScrollerDragInitY) > 5.0f) {
                    if (this.mFastScrollerThumb.isVertical()) {
                        this.mDragState = 2;
                        this.mOffsetToThumbTop = (int) (motionEvent.getY() - this.mFastScrollerThumbY);
                    } else {
                        this.mDragState = 4;
                    }
                    setState(3);
                }
            } else {
                showScrollerBar();
                int i3 = this.mDragState;
                if (i3 == 3 || i3 == 4) {
                    horizontalScrollTo(motionEvent.getX());
                }
                int i4 = this.mDragState;
                if (i4 == 1 || i4 == 2) {
                    verticalScrollTo(motionEvent.getX(), motionEvent.getY());
                }
            }
        }
        if (motionEvent.getAction() == 1 && ((i = this.mState) == 3 || i == 2)) {
            this.mFastScrollerDragX = 0.0f;
            setState(0);
            this.mDragState = 0;
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mIsFastScrollerPressed = false;
            this.mIsProportionTagViewPressed = false;
            if (this.mTargetProportionTagView != null) {
                scrollToTargetProportionTagView();
            }
        }
        if (this.mNeedDispatchFakeEvent) {
            this.mNeedDispatchFakeEvent = false;
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setAction(3);
            dispatchFakeEventExceptSelf(obtain);
        }
    }

    public final void scrollToTargetProportionTagView() {
        final int i = this.mFastScrollerThumbInitX;
        smallerProportionTagViewByAnimator(this.mTargetProportionTagView);
        if (this.mScrollAnimation == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mScrollAnimation = objectAnimator;
            objectAnimator.setTarget(this);
            this.mScrollAnimation.setPropertyName("scrollY");
            this.mScrollAnimation.setDuration(400L);
            this.mScrollAnimation.setInterpolator(new CubicEaseOutInterpolator());
            this.mScrollAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerBar.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    if (FastScrollerBar.this.mFastScrollerThumb.isVertical()) {
                        FastScrollerBar.this.verticalScrollTo(i, floatValue);
                    } else {
                        FastScrollerBar.this.horizontalScrollTo(i);
                    }
                }
            });
            this.mScrollAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerBar.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    FastScrollerBar.this.mTargetProportionTagView = null;
                    FastScrollerBar.this.mFastScrollerCapsule.setIsShowLocation(true);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    FastScrollerBar.this.mTargetProportionTagView = null;
                    FastScrollerBar.this.mFastScrollerCapsule.setIsShowLocation(true);
                    if (FastScrollerBar.this.mFastScrollerCapsule.getIsShowLocation()) {
                        FastScrollerBar.this.mFastScrollerCapsule.showLocationByAnimation();
                    }
                    FastScrollerBar.this.setState(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    super.onAnimationStart(animator);
                    FastScrollerBar.this.mFastScrollerCapsule.setIsShowLocation(false);
                    FastScrollerBar.this.setState(4);
                }
            });
        }
        this.mScrollAnimation.setFloatValues(this.mFastScrollerThumbY, this.mTargetProportionTagView.getPositionY() + 1);
        this.mScrollAnimation.start();
    }

    public final void recordFastScrollerBarDragging() {
        TrackController.trackFling("403.1.2.1.15770", AutoTracking.getRef());
    }

    public final void recordFastScrollerBarTagClick() {
        TrackController.trackClick("403.1.2.1.15769", AutoTracking.getRef());
    }

    public final void verticalScrollTo(float f, float f2) {
        FastScrollerCapsuleContentProvider capsuleContent;
        int[] verticalRange = getVerticalRange();
        int i = verticalRange[1] - verticalRange[0];
        if (i == 0) {
            return;
        }
        float clamp = MathUtils.clamp((this.mFastScrollerThumbY - this.mTopMargin) / i, 0.0f, 1.0f);
        int[] computeScrollPositionAndOffset = this.mRecyclerView.computeScrollPositionAndOffset(clamp);
        if (computeScrollPositionAndOffset != null && computeScrollPositionAndOffset.length == 2) {
            this.mRecyclerView.scrollToPositionWithOffset(computeScrollPositionAndOffset[0], computeScrollPositionAndOffset[1]);
        } else {
            int itemCount = this.mRecyclerView.getAdapter().getItemCount();
            this.mRecyclerView.scrollToPosition(MathUtils.clamp((int) (itemCount * clamp), 0, itemCount - 1));
        }
        if (isLayoutRTL()) {
            int i2 = this.mFastScrollerThumbInitX;
            if (f < i2) {
                f = i2;
            }
            this.mFastScrollerDragX = f;
        } else {
            int i3 = this.mFastScrollerThumbInitX;
            if (f > i3) {
                f = i3;
            }
            this.mFastScrollerDragX = f;
        }
        updateThumbPositionByDragging((int) this.mFastScrollerDragX, (int) Math.min(verticalRange[1], Math.max(f2 - this.mOffsetToThumbTop, verticalRange[0])));
        if (!isNeedDrawCapsule() || (capsuleContent = getCapsuleContent(clamp, i)) == null) {
            return;
        }
        this.mFastScrollerCapsule.setContent(capsuleContent);
    }

    public final void horizontalScrollTo(float f) {
        int scrollTo = scrollTo(this.mFastScrollerDragX, f, getHorizontalRange(), this.mRecyclerView.computeHorizontalScrollRange(), this.mRecyclerView.computeHorizontalScrollOffset(), this.mRecyclerViewWidth);
        if (scrollTo != 0) {
            this.mRecyclerView.scrollBy(scrollTo, 0);
        }
        this.mFastScrollerDragX = f;
    }

    public final int scrollTo(float f, float f2, int[] iArr, int i, int i2, int i3) {
        int i4 = iArr[1] - iArr[0];
        if (i4 == 0) {
            return 0;
        }
        int i5 = i - i3;
        int i6 = (int) (((f2 - f) / i4) * i5);
        int i7 = i2 + i6;
        if (i7 < i5 && i7 >= 0) {
            return i6;
        }
        return 0;
    }

    public final ProportionTagView getProportionTagViewByPoint(int i, int i2) {
        ProportionTagListWrapper<Integer> proportionTagListWrapper;
        if (this.mProportionTagAnimationState != 2 || (proportionTagListWrapper = this.mProportionTagListWrapper) == null) {
            return null;
        }
        return proportionTagListWrapper.getProportionTagViewByPoint(i, i2);
    }

    public boolean isPointInsideFastScrollerThumb(float f, float f2) {
        int i = this.mFastScrollerThumbX;
        if (f >= i - this.mFastThumbTouchAreaMarginStart && f <= i + this.mFastScrollerThumb.getViewWidth() + this.mFastThumbTouchAreaMarginEnd) {
            int i2 = this.mFastScrollerThumbY;
            if (f2 >= i2 - this.mFastThumbTouchAreaMarginY && f2 <= i2 + this.mFastScrollerThumb.getViewHeight() + this.mFastThumbTouchAreaMarginY) {
                return true;
            }
        }
        return false;
    }

    public void updateThumbPositionByScrolling() {
        int computeHorizontalScrollOffset = this.mRecyclerView.computeHorizontalScrollOffset();
        int computeScrollOffset = this.mRecyclerView.computeScrollOffset();
        int computeScrollRange = this.mRecyclerView.computeScrollRange();
        int i = this.mRecyclerViewHeight;
        int i2 = computeScrollRange - i;
        this.mNeedVerticalScrollbar = i2 > 0 && i >= (this.mScrollbarMinimumRange + this.mTopMargin) + this.mBottomMargin;
        int computeHorizontalScrollRange = this.mRecyclerView.computeHorizontalScrollRange();
        int i3 = this.mRecyclerViewWidth;
        int i4 = computeHorizontalScrollRange - i3;
        boolean z = i4 > 0 && i3 >= this.mScrollbarMinimumRange;
        this.mNeedHorizontalScrollbar = z;
        boolean z2 = this.mNeedVerticalScrollbar;
        if (!z2 && !z) {
            if (this.mState == 0) {
                return;
            }
            setState(0);
            return;
        }
        if (z2) {
            float f = (computeScrollOffset * 1.0f) / i2;
            int[] verticalRange = getVerticalRange();
            int min = Math.min(verticalRange[1], Math.max((int) (verticalRange[0] + ((verticalRange[1] - verticalRange[0]) * f)), verticalRange[0]));
            this.mFastScrollerThumbY = min;
            FastScrollerThumbView fastScrollerThumbView = this.mFastScrollerThumb;
            if (fastScrollerThumbView != null && this.mFastScrollerCapsule != null) {
                this.mFastTimeCapsuleY = min + ((fastScrollerThumbView.getViewHeight() - this.mFastScrollerCapsule.getViewHeight()) / 2);
            } else {
                this.mFastTimeCapsuleY = min;
            }
        }
        if (this.mNeedHorizontalScrollbar) {
            float f2 = (computeHorizontalScrollOffset * 1.0f) / i4;
            int[] horizontalRange = getHorizontalRange();
            int i5 = (int) (horizontalRange[0] + ((horizontalRange[1] - horizontalRange[0]) * f2));
            this.mFastScrollerThumbX = i5;
            this.mFastTimeCapsuleX = i5;
        }
        if (computeHorizontalScrollOffset == 0 && computeScrollOffset == 0) {
            setState(0);
        } else if (computeScrollRange < i * 2) {
        } else {
            setState(1);
        }
    }

    public void resetScrollerBarPosition() {
        this.mFastScrollerThumbInitX = isLayoutRTL() ? this.mFastScrollerThumbMargin : (this.mRecyclerView.getWidth() - this.mFastScrollerThumb.getViewWidth()) - this.mFastScrollerThumbMargin;
        if (this.mResetPositionAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mResetPositionAnimator = objectAnimator;
            objectAnimator.setTarget(this);
            this.mResetPositionAnimator.setPropertyName("FastScrollerThumbX");
            this.mResetPositionAnimator.setDuration(350L);
            this.mResetPositionAnimator.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.85f));
            this.mResetPositionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerBar.4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FastScrollerBar.this.changeThumbAndCapsulePositionX();
                    FastScrollerBar.this.requestRedraw();
                }
            });
            this.mResetPositionAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerBar.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    super.onAnimationStart(animator);
                    if (FastScrollerBar.this.isNeedDrawCapsule()) {
                        FastScrollerBar.this.resetCapsuleMarginByAnimator();
                    }
                }
            });
        }
        this.mResetPositionAnimator.setIntValues(this.mFastScrollerThumbX, this.mFastScrollerThumbInitX);
        this.mResetPositionAnimator.start();
    }

    public final void resetCapsuleMarginByAnimator() {
        if (this.mResetCapsuleMarginAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mResetCapsuleMarginAnimator = objectAnimator;
            objectAnimator.setTarget(this);
            this.mResetCapsuleMarginAnimator.setPropertyName("FastTimeCapsuleMargin");
            this.mResetCapsuleMarginAnimator.setDuration(350L);
            this.mResetCapsuleMarginAnimator.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.85f));
        }
        this.mResetCapsuleMarginAnimator.setIntValues(this.mFastTimeCapsuleMargin, this.mFastScrollerCapsule.getCapsuleMarginToThumb());
        this.mResetCapsuleMarginAnimator.start();
    }

    public final void expandCapsuleMarginByAnimator() {
        if (this.mExpandCapsuleMarginAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mExpandCapsuleMarginAnimator = objectAnimator;
            objectAnimator.setTarget(this);
            this.mExpandCapsuleMarginAnimator.setPropertyName("FastTimeCapsuleMargin");
            this.mExpandCapsuleMarginAnimator.setDuration(350L);
            this.mExpandCapsuleMarginAnimator.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.85f));
            this.mExpandCapsuleMarginAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerBar.6
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FastScrollerBar.this.requestRedraw();
                }
            });
        }
        this.mExpandCapsuleMarginAnimator.setIntValues(this.mFastTimeCapsuleMargin, 70);
        this.mExpandCapsuleMarginAnimator.start();
    }

    public void setProportionTagAdapterProvider(ProportionTagAdapterProvider<Integer> proportionTagAdapterProvider) {
        if (proportionTagAdapterProvider != null) {
            this.mTagAdapterProvider = proportionTagAdapterProvider;
        }
    }

    public void setFastScrollerCapsuleViewProvider(FastScrollerCapsuleViewProvider fastScrollerCapsuleViewProvider) {
        if (fastScrollerCapsuleViewProvider == null || !fastScrollerCapsuleViewProvider.isShowCapsule()) {
            return;
        }
        FastScrollerCapsule createFastScrollerCapsule = fastScrollerCapsuleViewProvider.createFastScrollerCapsule();
        this.mFastScrollerCapsule = createFastScrollerCapsule;
        createFastScrollerCapsule.setIsInRight(this.mIsInRight);
        this.mFastScrollerCapsule.setOnAnimatorListener(this);
        this.mFastScrollerCapsule.setOnLocationChangedListener(this);
        this.mFastTimeCapsuleMargin = this.mFastScrollerCapsule.getCapsuleMarginToThumb();
        this.mNeedDrawCapsule = true;
    }

    public void setCapsuleCalculator(FastScrollerCapsuleCalculator fastScrollerCapsuleCalculator) {
        this.mCapsuleCalculator = fastScrollerCapsuleCalculator;
        if (this.mFastScrollerCapsule != null && fastScrollerCapsuleCalculator != null) {
            this.mNeedDrawCapsule = true;
        } else {
            this.mNeedDrawCapsule = false;
        }
    }

    public FastScrollerCapsuleContentProvider getCapsuleContent(float f, int i) {
        FastScrollerCapsuleCalculator fastScrollerCapsuleCalculator = this.mCapsuleCalculator;
        if (fastScrollerCapsuleCalculator == null || this.mFastScrollerCapsule == null) {
            return null;
        }
        return this.mCapsuleCalculator.getCapsuleContent(fastScrollerCapsuleCalculator.getDataPositionByDrag((this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingLeft()) - this.mRecyclerView.getPaddingRight(), (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingTop()) - this.mRecyclerView.getPaddingBottom(), f, i));
    }

    public final int[] getVerticalRange() {
        int[] iArr = this.mVerticalRange;
        iArr[0] = this.mTopMargin;
        iArr[1] = (this.mRecyclerViewHeight - this.mBottomMargin) - this.mFastScrollerThumb.getViewHeight();
        return this.mVerticalRange;
    }

    public final int[] getHorizontalRange() {
        int[] iArr = this.mHorizontalRange;
        int i = this.mHorizontalMargin;
        iArr[0] = i;
        iArr[1] = (this.mRecyclerViewWidth - i) - this.mFastScrollerThumb.getViewWidth();
        return this.mHorizontalRange;
    }

    public final boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this.mRecyclerView) == 1;
    }

    public final boolean isLayoutLandScape() {
        return this.mRecyclerView.getContext().getResources().getConfiguration().orientation == 2;
    }

    public boolean isDragging() {
        return this.mState == 3;
    }

    public final boolean isNeedDrawCapsule() {
        return this.mFastScrollerCapsule != null && this.mNeedDrawCapsule;
    }

    public void attach() {
        setupCallbacks();
        updateThumbPositionByScrolling();
        requestRedraw();
    }

    public void detach() {
        destroyCallbacks();
    }

    public final void setupCallbacks() {
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    public final void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this);
        this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
    }

    public final void requestRedraw() {
        this.mRecyclerView.invalidate();
    }

    @Keep
    public void setFastScrollerThumbX(int i) {
        this.mFastScrollerThumbX = i;
    }

    @Keep
    public void setFastTimeCapsuleMargin(int i) {
        this.mFastTimeCapsuleMargin = i;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerThumbView.OnAnimatorListener
    public void onAnimatorFadeInEnd() {
        this.mAnimationState = 2;
        requestRedraw();
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerThumbView.OnAnimatorListener
    public void onAnimatorFadeOutEnd() {
        this.mAnimationState = 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerThumbView.OnAnimatorListener
    public void onAnimatorUpdate() {
        requestRedraw();
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule.OnAnimatorListener
    public void onTimeCapsuleAnimatorFadeInStart() {
        this.mTimeCapsuleAnimationState = 1;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule.OnAnimatorListener
    public void onTimeCapsuleAnimatorFadeInEnd() {
        this.mTimeCapsuleAnimationState = 2;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule.OnAnimatorListener
    public void onTimeCapsuleAnimatorFadeOutStart() {
        this.mTimeCapsuleAnimationState = 3;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule.OnAnimatorListener
    public void onTimeCapsuleAnimatorFadeOutEnd() {
        this.mTimeCapsuleAnimationState = 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule.OnAnimatorListener
    public void onTimeCapsuleAnimatorUpdate() {
        requestRedraw();
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule.OnLocationChangedListener
    public void onTimeCapsuleShowLocation() {
        if (this.mTimeCapsuleAnimationState == 2) {
            this.mFastScrollerCapsule.setIsShowLocation(true);
            this.mFastScrollerCapsule.showLocationByAnimation();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule.OnLocationChangedListener
    public void onTimeCapsuleHideLocation() {
        if (this.mTimeCapsuleAnimationState == 2) {
            this.mFastScrollerCapsule.hideLocationByAnimation();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.OnAnimatorListener
    public void onProportionTagAnimatorFadeInStart() {
        this.mProportionTagAnimationState = 1;
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.OnAnimatorListener
    public void onProportionTagAnimatorFadeInEnd() {
        this.mProportionTagAnimationState = 2;
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.OnAnimatorListener
    public void onProportionTagAnimatorFadeOutStart() {
        this.mProportionTagAnimationState = 3;
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.OnAnimatorListener
    public void onProportionTagAnimatorFadeOutEnd() {
        this.mProportionTagAnimationState = 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.OnAnimatorListener
    public void onProportionTagAnimatorUpdate() {
        requestRedraw();
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.OnAnimatorListener
    public void onProportionTagViewAnimatorLargeEnd() {
        this.mProportionTagViewAnimationState = 1;
    }

    @Override // com.miui.gallery.widget.recyclerview.ProportionTagListWrapper.OnAnimatorListener
    public void onProportionTagViewAnimatorSmallEnd() {
        this.mProportionTagViewAnimationState = 3;
    }
}
