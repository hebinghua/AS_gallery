package com.miui.gallery.vlog.caption;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.caption.widget.IVideoFrameView;
import com.miui.gallery.vlog.caption.widget.VideoClipInfo;
import com.miui.gallery.vlog.sdk.manager.MiVideoCaptionManager;
import com.miui.gallery.vlog.sdk.models.NvsCompoundCaptionWrapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class CaptionListView extends HorizontalScrollView {
    public CaptionContainerView mCaptionContainerView;
    public int mCaptionItemHeight;
    public int mCaptionShadowHeight;
    public List<CaptionViewModel> mCaptionViewModels;
    public HandleBarView mHandleBarView;
    public int mHeadTailPadding;
    public InteractionCallback mInteractionCallback;
    public double mPixelPerMicroSeconds;
    public int[] mPointBuffer;
    public ValueAnimator mSettleAnimator;
    public int mSettleStatus;
    public long mVideoDuration;
    public IVideoFrameView mVideoFrameView;

    /* loaded from: classes2.dex */
    public interface InteractionCallback {
        void onCaptionDragRelease(int i);

        void onCaptionDragging(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper, int i, long j, long j2);

        void onScrollChange(View view, int i, int i2);

        void onSelectCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper);

        void onSettleStatusChange(int i);

        void onUnSelectCaption();
    }

    public CaptionListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPointBuffer = new int[2];
        init(context);
    }

    public final boolean isLTR() {
        return getLayoutDirection() == 0;
    }

    public final void init(Context context) {
        setLayoutDirection(0);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(2);
        this.mCaptionViewModels = new ArrayList();
        LayoutInflater.from(context).inflate(R$layout.vlog_caption_list_view, this);
        FrameLayout frameLayout = (FrameLayout) findViewById(R$id.caption_container);
        CaptionContainerView captionContainerView = new CaptionContainerView(context);
        this.mCaptionContainerView = captionContainerView;
        frameLayout.addView(captionContainerView, new FrameLayout.LayoutParams(-2, -1));
        this.mHandleBarView = new HandleBarView(context);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.vlog_caption_block_height);
        this.mCaptionItemHeight = dimensionPixelSize;
        this.mHandleBarView.setBarHeight(dimensionPixelSize);
        this.mHandleBarView.setVisibility(4);
        frameLayout.addView(this.mHandleBarView, new FrameLayout.LayoutParams(-1, -1));
        IVideoFrameView iVideoFrameView = (IVideoFrameView) findViewById(R$id.frame_view);
        this.mVideoFrameView = iVideoFrameView;
        iVideoFrameView.setThumbnailAspectRatio(1.0f);
        this.mVideoFrameView.setThumbnailImageFillMode(1);
        this.mCaptionShadowHeight = getResources().getDimensionPixelSize(R$dimen.vlog_caption_block_shadow_height);
    }

    public void setThumbnailImageList(ArrayList<VideoClipInfo> arrayList) {
        this.mVideoFrameView.setThumbnailSequenceDescArray(arrayList);
    }

    public static <T> boolean emptyCollection(Collection<T> collection) {
        return collection == null || collection.size() == 0;
    }

    public void setPixelPerMicroSeconds(double d) {
        this.mPixelPerMicroSeconds = d;
        this.mVideoFrameView.setPixelPerMicrosecond(d);
    }

    /* loaded from: classes2.dex */
    public static class CaptionViewModel {
        public NvsCompoundCaptionWrapper mCaption;
        public CaptionTextView mContentView;

        public CaptionViewModel() {
        }

        public long getInPoint() {
            return this.mCaption.mMiCaption.getInPoint();
        }
    }

    @Override // android.view.View
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        InteractionCallback interactionCallback = this.mInteractionCallback;
        if (interactionCallback != null) {
            interactionCallback.onScrollChange(this, i, i3);
        }
        if (this.mSettleStatus == 0) {
            List<CaptionViewModel> list = this.mCaptionViewModels;
            CaptionViewModel captionViewModel = null;
            for (int i5 = 0; i5 < list.size(); i5++) {
                CaptionViewModel captionViewModel2 = list.get(i5);
                if (this.mHeadTailPadding + i >= captionViewModel2.mContentView.getLeft() && this.mHeadTailPadding + i <= captionViewModel2.mContentView.getRight()) {
                    captionViewModel = captionViewModel2;
                }
            }
            if (captionViewModel != null) {
                return;
            }
            this.mHandleBarView.detachCaption();
            InteractionCallback interactionCallback2 = this.mInteractionCallback;
            if (interactionCallback2 == null) {
                return;
            }
            interactionCallback2.onUnSelectCaption();
        }
    }

    public void unSelectCaption() {
        this.mHandleBarView.detachCaption();
        InteractionCallback interactionCallback = this.mInteractionCallback;
        if (interactionCallback != null) {
            interactionCallback.onUnSelectCaption();
        }
    }

    public void setInteractionCallback(InteractionCallback interactionCallback) {
        this.mInteractionCallback = interactionCallback;
    }

    public void deleteCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        if (nvsCompoundCaptionWrapper != null && !emptyCollection(this.mCaptionViewModels)) {
            CaptionViewModel captionViewModel = null;
            Iterator<CaptionViewModel> it = this.mCaptionViewModels.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CaptionViewModel next = it.next();
                if (nvsCompoundCaptionWrapper == next.mCaption) {
                    captionViewModel = next;
                    break;
                }
            }
            if (captionViewModel == null) {
                return;
            }
            if (captionViewModel == this.mHandleBarView.mAttachedCaption) {
                this.mHandleBarView.detachCaption();
            }
            this.mCaptionViewModels.remove(captionViewModel);
            this.mCaptionContainerView.removeView(captionViewModel.mContentView);
        }
    }

    public final CaptionTextView createCaptionView(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        CaptionTextView captionTextView = new CaptionTextView(getContext());
        captionTextView.setGravity(17);
        captionTextView.setLines(1);
        captionTextView.setEllipsize(TextUtils.TruncateAt.END);
        captionTextView.setTextColor(getResources().getColor(R$color.vlog_caption_view_text_color));
        captionTextView.setTextSize(0, getResources().getDimension(R$dimen.vlog_caption_text_size));
        captionTextView.setBackgroundResource(R$drawable.vlog_caption_block_background);
        captionTextView.setText(MiVideoCaptionManager.getWholeText(nvsCompoundCaptionWrapper));
        int dimension = (int) getResources().getDimension(R$dimen.vlog_caption_text_padding_left_right);
        captionTextView.setPadding(dimension, 0, dimension, 0);
        return captionTextView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void settleToLeft(CaptionViewModel captionViewModel) {
        settleTo(captionViewModel.mContentView.getLeft() - this.mHeadTailPadding, isLTR() ? 1 : 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void settleToRight(CaptionViewModel captionViewModel) {
        settleTo(captionViewModel.mContentView.getRight() - this.mHeadTailPadding, isLTR() ? 2 : 1);
    }

    public final void settleTo(int i, final int i2) {
        int scrollX;
        ValueAnimator valueAnimator = this.mSettleAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mSettleAnimator.cancel();
            this.mSettleStatus = 0;
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(getScrollX(), i);
        this.mSettleAnimator = ofInt;
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.vlog.caption.CaptionListView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                CaptionListView.this.setScrollX(((Integer) valueAnimator2.getAnimatedValue()).intValue());
            }
        });
        long min = Math.min(200.0f, Math.abs(i - scrollX) / 1.0f);
        this.mSettleAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.vlog.caption.CaptionListView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                CaptionListView.this.mSettleStatus = 0;
                if (CaptionListView.this.mInteractionCallback != null) {
                    CaptionListView.this.mInteractionCallback.onSettleStatusChange(i2);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                CaptionListView.this.mSettleStatus = 0;
                if (CaptionListView.this.mInteractionCallback != null) {
                    CaptionListView.this.mInteractionCallback.onSettleStatusChange(i2);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                CaptionListView.this.mSettleStatus = i2;
                if (CaptionListView.this.mInteractionCallback != null) {
                    CaptionListView.this.mInteractionCallback.onSettleStatusChange(i2);
                }
            }
        });
        this.mSettleAnimator.setInterpolator(new CubicEaseOutInterpolator());
        this.mSettleAnimator.setDuration(min);
        this.mSettleAnimator.start();
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mSettleStatus != 0) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void clearCaptions() {
        this.mCaptionViewModels.clear();
        this.mHandleBarView.detachCaption();
        this.mCaptionContainerView.removeAllViews();
    }

    public void addCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        if (nvsCompoundCaptionWrapper == null) {
            return;
        }
        CaptionTextView createCaptionView = createCaptionView(nvsCompoundCaptionWrapper);
        CaptionViewModel captionViewModel = new CaptionViewModel();
        captionViewModel.mCaption = nvsCompoundCaptionWrapper;
        captionViewModel.mContentView = createCaptionView;
        this.mCaptionViewModels.add(captionViewModel);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(timeToLength(nvsCompoundCaptionWrapper.mMiCaption.getOutPoint() - nvsCompoundCaptionWrapper.mMiCaption.getInPoint()), this.mCaptionItemHeight);
        layoutParams.leftMargin = timeToLength(nvsCompoundCaptionWrapper.mMiCaption.getInPoint());
        layoutParams.topMargin = this.mCaptionShadowHeight / 2;
        layoutParams.gravity = 16;
        this.mCaptionContainerView.addView(createCaptionView, layoutParams);
    }

    public void updateCaption(NvsCompoundCaptionWrapper nvsCompoundCaptionWrapper) {
        if (nvsCompoundCaptionWrapper == null) {
            return;
        }
        CaptionViewModel captionViewModel = null;
        Iterator<CaptionViewModel> it = this.mCaptionViewModels.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            CaptionViewModel next = it.next();
            if (next.mCaption == nvsCompoundCaptionWrapper) {
                captionViewModel = next;
                break;
            }
        }
        if (captionViewModel == null) {
            return;
        }
        captionViewModel.mContentView.setText(MiVideoCaptionManager.getWholeText(captionViewModel.mCaption));
    }

    public void setCaptions(List<NvsCompoundCaptionWrapper> list) {
        HandleBarView handleBarView = this.mHandleBarView;
        if (handleBarView != null) {
            handleBarView.detachCaption();
        }
        if (emptyCollection(list)) {
            this.mCaptionViewModels.clear();
            this.mCaptionContainerView.removeAllViews();
            return;
        }
        this.mCaptionViewModels.clear();
        this.mCaptionContainerView.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            addCaption(list.get(i));
        }
    }

    public final void moveCaptionLeftHandle(CaptionViewModel captionViewModel, int i) {
        if (i == 0) {
            return;
        }
        CaptionTextView captionTextView = captionViewModel.mContentView;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) captionTextView.getLayoutParams();
        layoutParams.leftMargin += i;
        layoutParams.width = captionTextView.getWidth() - i;
        captionTextView.setLayoutParams(layoutParams);
    }

    public final void moveCaptionRightHandle(CaptionViewModel captionViewModel, int i) {
        if (i == 0) {
            return;
        }
        CaptionTextView captionTextView = captionViewModel.mContentView;
        int width = captionTextView.getWidth() + i;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) captionTextView.getLayoutParams();
        layoutParams.width = width;
        captionTextView.setLayoutParams(layoutParams);
    }

    public final void moveCaption(CaptionViewModel captionViewModel, int i) {
        if (i == 0) {
            return;
        }
        CaptionTextView captionTextView = captionViewModel.mContentView;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) captionTextView.getLayoutParams();
        layoutParams.leftMargin += i;
        captionTextView.setLayoutParams(layoutParams);
    }

    public boolean isScrollFinish() {
        try {
            Field declaredField = getClass().getSuperclass().getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(this);
            Method method = declaredField.getType().getMethod("isFinished", new Class[0]);
            method.setAccessible(true);
            return ((Boolean) method.invoke(obj, new Object[0])).booleanValue();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            return false;
        } catch (NoSuchMethodException unused) {
            return false;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public int getSettleStatus() {
        return this.mSettleStatus;
    }

    public final int timeToLength(long j) {
        return (int) (this.mPixelPerMicroSeconds * j);
    }

    public final long lengthToTime(int i) {
        return (long) (i / this.mPixelPerMicroSeconds);
    }

    public final int videoSpaceToViewSpace(long j) {
        int i;
        int timeToLength;
        if (isLTR()) {
            i = this.mHeadTailPadding;
            timeToLength = timeToLength(j);
        } else {
            i = this.mHeadTailPadding;
            timeToLength = timeToLength(this.mVideoDuration - j);
        }
        return i + timeToLength;
    }

    public final long viewSpaceToVideoSpace(int i) {
        if (isLTR()) {
            return lengthToTime(i - this.mHeadTailPadding);
        }
        return this.mVideoDuration - lengthToTime(i - this.mHeadTailPadding);
    }

    public final boolean inViewArea(View view, MotionEvent motionEvent) {
        view.getLocationOnScreen(this.mPointBuffer);
        return motionEvent.getRawX() > ((float) this.mPointBuffer[0]) && motionEvent.getRawX() < ((float) (this.mPointBuffer[0] + view.getWidth())) && motionEvent.getRawY() > ((float) this.mPointBuffer[1]) && motionEvent.getRawY() < ((float) (this.mPointBuffer[1] + view.getHeight()));
    }

    public void setVideoDuration(long j) {
        this.mVideoDuration = j;
        int timeToLength = timeToLength(j);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mCaptionContainerView.getLayoutParams();
        layoutParams.width = timeToLength;
        this.mCaptionContainerView.setLayoutParams(layoutParams);
    }

    public void release() {
        IVideoFrameView iVideoFrameView = this.mVideoFrameView;
        if (iVideoFrameView != null) {
            iVideoFrameView.release();
        }
    }

    public void reInit() {
        IVideoFrameView iVideoFrameView = this.mVideoFrameView;
        if (iVideoFrameView != null) {
            iVideoFrameView.reInit();
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i) / 2;
        this.mHeadTailPadding = size;
        this.mVideoFrameView.setStartPadding(size);
        this.mVideoFrameView.setEndPadding(this.mHeadTailPadding);
        super.onMeasure(i, i2);
    }

    /* loaded from: classes2.dex */
    public class CaptionContainerView extends FrameLayout implements GestureDetector.OnGestureListener {
        public int mDraggableLeftBoundary;
        public int mDraggableRightBoundary;
        public CaptionViewModel mDraggingCaption;
        public GestureDetector mGestureDetector;
        public float mLastX;

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        public CaptionContainerView(Context context) {
            super(context);
            init(context);
        }

        public final void init(Context context) {
            this.mGestureDetector = new GestureDetector(context, this);
        }

        @Override // android.view.View
        public void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            DefaultLogger.d("CaptionListView", "onSizeChanged");
            final CaptionViewModel captionViewModel = CaptionListView.this.mHandleBarView.mAttachedCaption;
            if (captionViewModel == null) {
                return;
            }
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.caption.CaptionListView.CaptionContainerView.1
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    CaptionContainerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    CaptionListView.this.mHandleBarView.attachToCaption(captionViewModel);
                }
            });
        }

        @Override // android.widget.FrameLayout, android.view.View
        public void onMeasure(int i, int i2) {
            if (getLayoutParams().width >= 0) {
                i = View.MeasureSpec.makeMeasureSpec(getLayoutParams().width + (CaptionListView.this.mHeadTailPadding * 2), 1073741824);
            }
            setPadding(CaptionListView.this.mHeadTailPadding, 0, CaptionListView.this.mHeadTailPadding, 0);
            super.onMeasure(i, i2);
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x001a, code lost:
            if (r0 != 6) goto L15;
         */
        @Override // android.view.View
        @android.annotation.SuppressLint({"ClickableViewAccessibility"})
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onTouchEvent(android.view.MotionEvent r11) {
            /*
                r10 = this;
                android.view.GestureDetector r0 = r10.mGestureDetector
                boolean r0 = r0.onTouchEvent(r11)
                r1 = 1
                if (r0 == 0) goto La
                return r1
            La:
                int r0 = r11.getActionMasked()
                if (r0 == 0) goto Ldb
                r2 = 0
                if (r0 == r1) goto L9f
                r3 = 2
                if (r0 == r3) goto L1e
                r11 = 3
                if (r0 == r11) goto L9f
                r11 = 6
                if (r0 == r11) goto L9f
                goto Le1
            L1e:
                boolean r0 = r10.isDraggingCaption()
                if (r0 == 0) goto L9b
                r10.requestDisallowInterceptTouchEvent(r1)
                float r11 = r11.getX()
                float r0 = r10.mLastX
                float r0 = r11 - r0
                int r0 = (int) r0
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r2 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView$CaptionTextView r2 = r2.mContentView
                int r2 = r2.getLeft()
                int r2 = r2 + r0
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r3 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView$CaptionTextView r3 = r3.mContentView
                int r3 = r3.getRight()
                int r3 = r3 + r0
                int r4 = r10.mDraggableLeftBoundary
                if (r2 >= r4) goto L51
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r0 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView$CaptionTextView r0 = r0.mContentView
                int r0 = r0.getLeft()
                int r0 = r4 - r0
                goto L5f
            L51:
                int r2 = r10.mDraggableRightBoundary
                if (r3 <= r2) goto L5f
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r0 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView$CaptionTextView r0 = r0.mContentView
                int r0 = r0.getRight()
                int r0 = r2 - r0
            L5f:
                com.miui.gallery.vlog.caption.CaptionListView r2 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r3 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView.access$600(r2, r3, r0)
                com.miui.gallery.vlog.caption.CaptionListView r2 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$InteractionCallback r2 = com.miui.gallery.vlog.caption.CaptionListView.access$200(r2)
                if (r2 == 0) goto L98
                com.miui.gallery.vlog.caption.CaptionListView r2 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$InteractionCallback r3 = com.miui.gallery.vlog.caption.CaptionListView.access$200(r2)
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r2 = r10.mDraggingCaption
                com.miui.gallery.vlog.sdk.models.NvsCompoundCaptionWrapper r4 = r2.mCaption
                r5 = 0
                com.miui.gallery.vlog.caption.CaptionListView r6 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$CaptionTextView r2 = r2.mContentView
                int r2 = r2.getLeft()
                int r2 = r2 + r0
                long r6 = com.miui.gallery.vlog.caption.CaptionListView.access$700(r6, r2)
                com.miui.gallery.vlog.caption.CaptionListView r2 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r8 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView$CaptionTextView r8 = r8.mContentView
                int r8 = r8.getRight()
                int r8 = r8 + r0
                long r8 = com.miui.gallery.vlog.caption.CaptionListView.access$700(r2, r8)
                r3.onCaptionDragging(r4, r5, r6, r8)
            L98:
                r10.mLastX = r11
                goto Le1
            L9b:
                r10.requestDisallowInterceptTouchEvent(r2)
                goto Le1
            L9f:
                boolean r11 = r10.isDraggingCaption()
                if (r11 == 0) goto Le1
                r10.requestDisallowInterceptTouchEvent(r2)
                com.miui.gallery.vlog.caption.CaptionListView r11 = com.miui.gallery.vlog.caption.CaptionListView.this
                boolean r11 = com.miui.gallery.vlog.caption.CaptionListView.access$800(r11)
                if (r11 == 0) goto Lb8
                com.miui.gallery.vlog.caption.CaptionListView r11 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r0 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView.access$900(r11, r0)
                goto Lbf
            Lb8:
                com.miui.gallery.vlog.caption.CaptionListView r11 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r0 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView.access$1000(r11, r0)
            Lbf:
                com.miui.gallery.vlog.caption.CaptionListView r11 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$InteractionCallback r11 = com.miui.gallery.vlog.caption.CaptionListView.access$200(r11)
                if (r11 == 0) goto Ld0
                com.miui.gallery.vlog.caption.CaptionListView r11 = com.miui.gallery.vlog.caption.CaptionListView.this
                com.miui.gallery.vlog.caption.CaptionListView$InteractionCallback r11 = com.miui.gallery.vlog.caption.CaptionListView.access$200(r11)
                r11.onCaptionDragRelease(r2)
            Ld0:
                com.miui.gallery.vlog.caption.CaptionListView$CaptionViewModel r11 = r10.mDraggingCaption
                com.miui.gallery.vlog.caption.CaptionListView$CaptionTextView r11 = r11.mContentView
                r11.clearAnimation()
                r11 = 0
                r10.mDraggingCaption = r11
                goto Le1
            Ldb:
                float r11 = r11.getX()
                r10.mLastX = r11
            Le1:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.caption.CaptionListView.CaptionContainerView.onTouchEvent(android.view.MotionEvent):boolean");
        }

        public final boolean isDraggingCaption() {
            return this.mDraggingCaption != null;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            List list = CaptionListView.this.mCaptionViewModels;
            CaptionViewModel captionViewModel = null;
            for (int i = 0; i < list.size(); i++) {
                CaptionViewModel captionViewModel2 = (CaptionViewModel) list.get(i);
                if (CaptionListView.this.inViewArea(captionViewModel2.mContentView, motionEvent)) {
                    captionViewModel = captionViewModel2;
                }
            }
            if (captionViewModel == null) {
                CaptionListView.this.mHandleBarView.detachCaption();
                if (CaptionListView.this.mInteractionCallback != null) {
                    CaptionListView.this.mInteractionCallback.onUnSelectCaption();
                }
                return false;
            } else if (CaptionListView.this.mHandleBarView.isAttached(captionViewModel)) {
                CaptionListView.this.mHandleBarView.detachCaption();
                if (CaptionListView.this.mInteractionCallback == null) {
                    return true;
                }
                CaptionListView.this.mInteractionCallback.onUnSelectCaption();
                return true;
            } else {
                CaptionListView.this.mHandleBarView.attachToCaption(captionViewModel);
                if (CaptionListView.this.mInteractionCallback != null) {
                    CaptionListView.this.mInteractionCallback.onSelectCaption(captionViewModel.mCaption);
                }
                if (CaptionListView.this.isLTR()) {
                    CaptionListView.this.settleToLeft(captionViewModel);
                    return true;
                }
                CaptionListView.this.settleToRight(captionViewModel);
                return true;
            }
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (CaptionListView.this.isScrollFinish()) {
                List list = CaptionListView.this.mCaptionViewModels;
                for (int i = 0; i < list.size(); i++) {
                    CaptionViewModel captionViewModel = (CaptionViewModel) list.get(i);
                    if (CaptionListView.this.inViewArea(captionViewModel.mContentView, motionEvent)) {
                        CaptionListView.this.mHandleBarView.detachCaption();
                        this.mDraggingCaption = captionViewModel;
                        this.mDraggableLeftBoundary = CaptionListView.this.leftBoundaryPosition(captionViewModel);
                        this.mDraggableRightBoundary = CaptionListView.this.rightBoundaryPosition(this.mDraggingCaption);
                        LinearMotorHelper.performHapticFeedback(getContext(), LinearMotorHelper.HAPTIC_PICK_UP);
                        animateDraggingCaption();
                        return;
                    }
                }
            }
        }

        public final void animateDraggingCaption() {
            this.mDraggingCaption.mContentView.clearAnimation();
            AnimationSet animationSet = new AnimationSet(false);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
            TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -10.0f);
            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(translateAnimation);
            animationSet.setDuration(150L);
            animationSet.setFillAfter(true);
            this.mDraggingCaption.mContentView.startAnimation(animationSet);
        }
    }

    public final CaptionViewModel rightNextCaptionModel(CaptionViewModel captionViewModel) {
        CaptionViewModel captionViewModel2 = null;
        for (CaptionViewModel captionViewModel3 : this.mCaptionViewModels) {
            if (captionViewModel3 != captionViewModel && captionViewModel3.getInPoint() > captionViewModel.getInPoint() && (captionViewModel2 == null || captionViewModel3.getInPoint() < captionViewModel2.getInPoint())) {
                captionViewModel2 = captionViewModel3;
            }
        }
        return captionViewModel2;
    }

    public final CaptionViewModel leftNextCaptionModel(CaptionViewModel captionViewModel) {
        CaptionViewModel captionViewModel2 = null;
        for (CaptionViewModel captionViewModel3 : this.mCaptionViewModels) {
            if (captionViewModel3 != captionViewModel && captionViewModel3.getInPoint() < captionViewModel.getInPoint() && (captionViewModel2 == null || captionViewModel3.getInPoint() > captionViewModel2.getInPoint())) {
                captionViewModel2 = captionViewModel3;
            }
        }
        return captionViewModel2;
    }

    public int leftBoundaryPosition(CaptionViewModel captionViewModel) {
        if (captionViewModel == null) {
            return 0;
        }
        CaptionViewModel leftNextCaptionModel = leftNextCaptionModel(captionViewModel);
        if (leftNextCaptionModel == null) {
            return videoSpaceToViewSpace(0L);
        }
        return leftNextCaptionModel.mContentView.getRight() + 1;
    }

    public int rightBoundaryPosition(CaptionViewModel captionViewModel) {
        if (captionViewModel == null) {
            return 0;
        }
        CaptionViewModel rightNextCaptionModel = rightNextCaptionModel(captionViewModel);
        if (rightNextCaptionModel == null) {
            return videoSpaceToViewSpace(this.mVideoDuration);
        }
        return rightNextCaptionModel.mContentView.getLeft() - 1;
    }

    /* loaded from: classes2.dex */
    public static class CaptionTextView extends TextView {
        public int mClipBottom;
        public int mClipLeft;
        public int mClipRight;
        public int mClipTop;

        public CaptionTextView(Context context) {
            super(context);
        }

        public void setClipBorder(int i, int i2, int i3, int i4) {
            this.mClipLeft = i;
            this.mClipTop = i2;
            this.mClipRight = i3;
            this.mClipBottom = i4;
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            Rect clipBounds = canvas.getClipBounds();
            clipBounds.left += this.mClipLeft;
            clipBounds.right -= this.mClipRight;
            clipBounds.top += this.mClipTop;
            clipBounds.bottom -= this.mClipBottom;
            canvas.clipRect(clipBounds);
            super.draw(canvas);
        }
    }

    /* loaded from: classes2.dex */
    public class HandleBarView extends View {
        public CaptionViewModel mAttachedCaption;
        public int mBarHeight;
        public int mBarStrokeBottomExtra;
        public int mBarStrokeTopExtra;
        public int mBarWidth;
        public Drawable mDragBar;
        public Drawable mDragBarDisable;
        public Drawable mDragBarEnable;
        public float mLastX;
        public int mLeft;
        public int mLeftBoundary;
        public int mRight;
        public int mRightBoundary;
        public int mTouchArea;
        public int mTriggerHeight;
        public int mTriggerWidth;

        public HandleBarView(Context context) {
            super(context);
            this.mDragBarEnable = getResources().getDrawable(R$drawable.vlog_clip_frame_enable);
            this.mDragBarDisable = getResources().getDrawable(R$drawable.vlog_clip_frame_disable);
            this.mTriggerWidth = getResources().getDimensionPixelSize(R$dimen.vlog_caption_block_handle_trigger_width);
            this.mTriggerHeight = getResources().getDimensionPixelSize(R$dimen.vlog_caption_block_handle_trigger_height);
            this.mBarWidth = getContext().getResources().getDimensionPixelSize(R$dimen.vlog_drag_bar_width);
            this.mBarStrokeTopExtra = getContext().getResources().getDimensionPixelSize(com.miui.gallery.editor.R$dimen.px_6);
            this.mBarStrokeBottomExtra = getContext().getResources().getDimensionPixelSize(com.miui.gallery.editor.R$dimen.px_16);
            this.mDragBar = this.mDragBarEnable;
        }

        public void setBarHeight(int i) {
            this.mBarHeight = i;
        }

        public boolean isAttached(CaptionViewModel captionViewModel) {
            return this.mAttachedCaption == captionViewModel;
        }

        public void attachToCaption(CaptionViewModel captionViewModel) {
            CaptionViewModel captionViewModel2 = this.mAttachedCaption;
            if (captionViewModel2 != captionViewModel && captionViewModel2 != null) {
                captionViewModel2.mContentView.setClipBorder(0, 0, 0, 0);
                this.mAttachedCaption.mContentView.setBackgroundResource(R$drawable.vlog_caption_block_background);
            }
            this.mAttachedCaption = captionViewModel;
            if (captionViewModel == null) {
                setVisibility(4);
                return;
            }
            setVisibility(0);
            this.mLeft = this.mAttachedCaption.mContentView.getLeft();
            this.mRight = this.mAttachedCaption.mContentView.getRight();
            invalidate();
            this.mLeftBoundary = CaptionListView.this.leftBoundaryPosition(this.mAttachedCaption);
            this.mRightBoundary = CaptionListView.this.rightBoundaryPosition(this.mAttachedCaption);
            this.mAttachedCaption.mContentView.setBackgroundColor(getResources().getColor(R$color.vlog_caption_block_select_color));
        }

        public final int getTopBound() {
            return (getHeight() - this.mBarHeight) / 2;
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int topBound = getTopBound();
            Drawable drawable = this.mDragBar;
            if (drawable != null) {
                int i = this.mLeft;
                int i2 = this.mBarWidth;
                drawable.setBounds(i - i2, topBound - this.mBarStrokeTopExtra, this.mRight + i2, topBound + this.mBarHeight + this.mBarStrokeBottomExtra);
                this.mDragBar.draw(canvas);
            }
        }

        public void detachCaption() {
            attachToCaption(null);
        }

        public final boolean isDragging() {
            int i = this.mTouchArea;
            return i == 1 || i == 2;
        }

        @Override // android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            int i = 2;
            if (actionMasked == 0) {
                if (inLeftHandle(motionEvent)) {
                    this.mTouchArea = 1;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else if (inRightHandle(motionEvent)) {
                    this.mTouchArea = 2;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                this.mLastX = motionEvent.getX();
                return isDragging();
            }
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    float x = motionEvent.getX();
                    if (isDragging()) {
                        int timeToLength = CaptionListView.this.timeToLength(500000L);
                        int i2 = (int) (x - this.mLastX);
                        if (this.mTouchArea == 1) {
                            int i3 = this.mLeft;
                            int i4 = i2 + i3;
                            int i5 = this.mLeftBoundary;
                            if (i4 < i5) {
                                i4 = i5;
                            }
                            int i6 = this.mRight;
                            if (i6 - i4 < timeToLength) {
                                i4 = i6 - timeToLength;
                            }
                            int i7 = i4 - i3;
                            this.mLeft = i4;
                            CaptionListView.this.moveCaptionLeftHandle(this.mAttachedCaption, i7);
                            int i8 = this.mLeft;
                            if (i8 == this.mLeftBoundary || this.mRight - i8 == timeToLength) {
                                this.mDragBar = this.mDragBarDisable;
                            } else {
                                this.mDragBar = this.mDragBarEnable;
                            }
                            if (CaptionListView.this.mInteractionCallback != null) {
                                InteractionCallback interactionCallback = CaptionListView.this.mInteractionCallback;
                                CaptionViewModel captionViewModel = this.mAttachedCaption;
                                interactionCallback.onCaptionDragging(captionViewModel.mCaption, 1, CaptionListView.this.viewSpaceToVideoSpace(captionViewModel.mContentView.getLeft() + i7), CaptionListView.this.viewSpaceToVideoSpace(this.mAttachedCaption.mContentView.getRight()));
                            }
                            invalidate();
                        } else {
                            int i9 = this.mRight;
                            int i10 = i2 + i9;
                            int i11 = this.mRightBoundary;
                            if (i10 > i11) {
                                i10 = i11;
                            }
                            int i12 = this.mLeft;
                            if (i10 - i12 < timeToLength) {
                                i10 = i12 + timeToLength;
                            }
                            int i13 = i10 - i9;
                            this.mRight = i10;
                            CaptionListView.this.moveCaptionRightHandle(this.mAttachedCaption, i13);
                            int i14 = this.mRight;
                            if (i14 == this.mRightBoundary || i14 - this.mLeft == timeToLength) {
                                this.mDragBar = this.mDragBarDisable;
                            } else {
                                this.mDragBar = this.mDragBarEnable;
                            }
                            if (CaptionListView.this.mInteractionCallback != null) {
                                InteractionCallback interactionCallback2 = CaptionListView.this.mInteractionCallback;
                                CaptionViewModel captionViewModel2 = this.mAttachedCaption;
                                interactionCallback2.onCaptionDragging(captionViewModel2.mCaption, 2, CaptionListView.this.viewSpaceToVideoSpace(captionViewModel2.mContentView.getLeft()), CaptionListView.this.viewSpaceToVideoSpace(this.mAttachedCaption.mContentView.getRight() + i13));
                            }
                            invalidate();
                        }
                        this.mLastX = motionEvent.getX();
                        return true;
                    }
                } else if (actionMasked != 3 && actionMasked != 6) {
                    return false;
                }
            }
            int i15 = this.mTouchArea;
            if (i15 == 1) {
                CaptionListView.this.settleToLeft(this.mAttachedCaption);
                if (CaptionListView.this.mInteractionCallback != null) {
                    InteractionCallback interactionCallback3 = CaptionListView.this.mInteractionCallback;
                    if (CaptionListView.this.isLTR()) {
                        i = 1;
                    }
                    interactionCallback3.onCaptionDragRelease(i);
                }
            } else if (i15 == 2) {
                CaptionListView.this.settleToRight(this.mAttachedCaption);
                if (CaptionListView.this.mInteractionCallback != null) {
                    InteractionCallback interactionCallback4 = CaptionListView.this.mInteractionCallback;
                    if (!CaptionListView.this.isLTR()) {
                        i = 1;
                    }
                    interactionCallback4.onCaptionDragRelease(i);
                }
            }
            this.mDragBar = this.mDragBarEnable;
            this.mTouchArea = 0;
            invalidate();
            return false;
        }

        public final boolean inLeftHandle(MotionEvent motionEvent) {
            return motionEvent.getX() > ((float) ((this.mLeft - this.mBarWidth) - ((this.mTriggerWidth - this.mBarWidth) / 2))) && motionEvent.getX() < ((float) this.mLeft);
        }

        public final boolean inRightHandle(MotionEvent motionEvent) {
            return motionEvent.getX() > ((float) this.mRight) && motionEvent.getX() < ((float) ((this.mRight + this.mBarWidth) + ((this.mTriggerWidth - this.mBarWidth) / 2)));
        }
    }
}
