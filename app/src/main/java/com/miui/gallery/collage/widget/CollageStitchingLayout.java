package com.miui.gallery.collage.widget;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.OverScroller;
import com.miui.gallery.R;
import com.miui.gallery.app.MatrixEvaluator;
import com.miui.gallery.collage.CollageActivity;
import com.miui.gallery.collage.CollageUtils;
import com.miui.gallery.collage.core.stitching.StitchingModel;
import com.miui.gallery.collage.render.CollageRender;
import com.miui.gallery.collage.widget.ControlPopupWindow;
import com.miui.gallery.editor.photo.core.imports.obsoletes.RectFEvaluator;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class CollageStitchingLayout extends ViewGroup {
    public final String PROPERTY_NAME_ALPHA;
    public final String PROPERTY_NAME_MATRIX;
    public final String PROPERTY_NAME_RECT;
    public int mActiveLineWidth;
    public int mActiveLineWidthWhite;
    public CollageImageView mActiveView;
    public int mBackGroundColor;
    public Paint mBackgroundPaint;
    public BitmapPositionHolder mBitmapPositionHolder;
    public Bitmap[] mBitmaps;
    public RectF mCanvasRect;
    public ControlPopupWindow.ControlListener mControlListener;
    public ControlPopupWindow mControlPopupWindow;
    public Rect mDisplayRect;
    public GestureDetector mDragModeDetector;
    public boolean mEnableDragMode;
    public GestureDetector mGestureDetector;
    public Map<Bitmap, CollageImageView> mImageViewMap;
    public ItemDragHelper mItemDragHelper;
    public int mMaxScrollY;
    public int mMinScrollY;
    public Path mPath;
    public Paint mPathPaint;
    public RectF mRectF;
    public CollageActivity.ReplaceImageListener mReplaceImageListener;
    public ValueAnimator mScrollAnimator;
    public ValueAnimator.AnimatorUpdateListener mScrollAnimatorUpdateListener;
    public float mScrollSpeed;
    public OverScroller mScroller;
    public StitchingModel mStitchingModel;
    public int mStrokeColor;

    /* loaded from: classes.dex */
    public static class RenderData {
        public CollageRender.BitmapRenderData[] bitmapRenderDatas;
        public StitchingModel stitchingModel;
        public int viewWidth;
    }

    public CollageStitchingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.PROPERTY_NAME_ALPHA = "property_name_alpha";
        this.PROPERTY_NAME_MATRIX = "property_name_matrix";
        this.PROPERTY_NAME_RECT = "property_name_rect";
        this.mScrollSpeed = 0.0f;
        this.mEnableDragMode = false;
        this.mBackgroundPaint = new Paint(1);
        this.mActiveView = null;
        this.mImageViewMap = new HashMap();
        this.mRectF = new RectF();
        this.mCanvasRect = new RectF();
        this.mDisplayRect = new Rect();
        this.mPath = new Path();
        this.mPathPaint = new Paint();
        this.mScrollAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (CollageStitchingLayout.this.mScrollSpeed != 0.0f) {
                    CollageStitchingLayout collageStitchingLayout = CollageStitchingLayout.this;
                    int appendScroll = collageStitchingLayout.appendScroll(collageStitchingLayout.mScrollSpeed);
                    if (appendScroll == 0) {
                        return;
                    }
                    CollageStitchingLayout.this.mItemDragHelper.notifySwipe(appendScroll);
                }
            }
        };
        this.mControlListener = new ControlPopupWindow.ControlListener() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.5
            @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
            public void onDismiss() {
            }

            @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
            public void onRotate() {
            }

            @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
            public void onReplace() {
                if (CollageStitchingLayout.this.mReplaceImageListener == null || CollageStitchingLayout.this.mActiveView == null) {
                    return;
                }
                CollageStitchingLayout.this.mReplaceImageListener.onReplace(CollageStitchingLayout.this.mActiveView.getBitmap());
                CollageStitchingLayout.this.setActiveView(null);
            }

            @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
            public void onMirror() {
                if (CollageStitchingLayout.this.mActiveView != null) {
                    CollageStitchingLayout.this.mActiveView.mirror();
                }
            }
        };
        init();
    }

    public final void init() {
        setClipChildren(false);
        GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureListener());
        this.mGestureDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(true);
        GestureDetector gestureDetector2 = new GestureDetector(getContext(), new DragGestureListener());
        this.mDragModeDetector = gestureDetector2;
        gestureDetector2.setIsLongpressEnabled(false);
        this.mItemDragHelper = new ItemDragHelper();
        this.mBackGroundColor = -1;
        this.mBackgroundPaint.setColor(-1);
        this.mBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mScroller = new OverScroller(getContext());
        this.mStrokeColor = getResources().getColor(R.color.collage_table_text_color_checked);
        this.mActiveLineWidth = getResources().getDimensionPixelSize(R.dimen.collage_stroke_line_width);
        this.mActiveLineWidthWhite = getResources().getDimensionPixelSize(R.dimen.collage_stroke_line_width_white);
        this.mPathPaint.setStyle(Paint.Style.STROKE);
        this.mPathPaint.setStrokeWidth(this.mActiveLineWidth);
        int i = this.mActiveLineWidth;
        if (i % 2 != 0) {
            this.mActiveLineWidth = i + 1;
        }
        int i2 = this.mActiveLineWidthWhite;
        if (i2 % 2 != 0) {
            this.mActiveLineWidthWhite = i2 + 1;
        }
        ControlPopupWindow controlPopupWindow = new ControlPopupWindow(getContext(), false);
        this.mControlPopupWindow = controlPopupWindow;
        controlPopupWindow.setControlListener(this.mControlListener);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mGestureDetector.onTouchEvent(motionEvent);
            this.mDragModeDetector.onTouchEvent(motionEvent);
        } else if (action == 1 || action == 3) {
            this.mGestureDetector.onTouchEvent(motionEvent);
            this.mDragModeDetector.onTouchEvent(motionEvent);
            z = true;
            if (z && this.mEnableDragMode) {
                post(new Runnable() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CollageStitchingLayout.this.notifyZoomIn(0.0f);
                        CollageStitchingLayout.this.cancelContinueScroll();
                        CollageStitchingLayout.this.mItemDragHelper.disableDragMode();
                        CollageStitchingLayout.this.mEnableDragMode = false;
                    }
                });
            }
            return true;
        } else {
            this.mGestureDetector.onTouchEvent(motionEvent);
            if (this.mEnableDragMode) {
                this.mDragModeDetector.onTouchEvent(motionEvent);
            }
        }
        z = false;
        if (z) {
            post(new Runnable() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.1
                @Override // java.lang.Runnable
                public void run() {
                    CollageStitchingLayout.this.notifyZoomIn(0.0f);
                    CollageStitchingLayout.this.cancelContinueScroll();
                    CollageStitchingLayout.this.mItemDragHelper.disableDragMode();
                    CollageStitchingLayout.this.mEnableDragMode = false;
                }
            });
        }
        return true;
    }

    /* loaded from: classes.dex */
    public class GestureListener implements GestureDetector.OnGestureListener {
        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        public GestureListener() {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            CollageStitchingLayout.this.mScroller.abortAnimation();
            CollageStitchingLayout.this.mEnableDragMode = false;
            CollageStitchingLayout.this.mScrollSpeed = 0.0f;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            CollageStitchingLayout.this.setActiveView(CollageStitchingLayout.this.findSingleView(motionEvent.getX(), motionEvent.getY()));
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            CollageStitchingLayout.this.setActiveView(null);
            CollageStitchingLayout.this.appendScroll(f2);
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                LinearMotorHelper.performHapticFeedback(CollageStitchingLayout.this.getRootView(), LinearMotorHelper.HAPTIC_PICK_UP);
            } else {
                CollageStitchingLayout.this.performHapticFeedback(0);
            }
            CollageStitchingLayout.this.mEnableDragMode = true;
            CollageStitchingLayout.this.notifyZoomOut(motionEvent.getY());
            CollageStitchingLayout.this.startContinueScroll();
            CollageStitchingLayout.this.mItemDragHelper.enableDragMode(CollageStitchingLayout.this.findTargetView(motionEvent.getY()), motionEvent.getY());
            CollageStitchingLayout.this.setActiveView(null);
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            CollageStitchingLayout.this.mScroller.fling(CollageStitchingLayout.this.getScrollX(), CollageStitchingLayout.this.getScrollY(), Math.round(-f), Math.round(-f2), CollageStitchingLayout.this.getScrollX(), CollageStitchingLayout.this.getScrollX(), CollageStitchingLayout.this.mMinScrollY, CollageStitchingLayout.this.mMaxScrollY);
            CollageStitchingLayout.this.invalidate();
            return true;
        }
    }

    /* loaded from: classes.dex */
    public class DragGestureListener implements GestureDetector.OnGestureListener {
        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        public DragGestureListener() {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            CollageStitchingLayout.this.computeScrollSpeed(motionEvent2.getY() - motionEvent.getY());
            CollageStitchingLayout.this.mItemDragHelper.onScroll(f2);
            return true;
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        BitmapPositionHolder bitmapPositionHolder;
        int size = View.MeasureSpec.getSize(i);
        StitchingModel stitchingModel = this.mStitchingModel;
        if (stitchingModel == null || (bitmapPositionHolder = this.mBitmapPositionHolder) == null) {
            i3 = 0;
        } else {
            stitchingModel.countHeight(size, bitmapPositionHolder, this.mBitmaps);
            i3 = this.mBitmapPositionHolder.height;
        }
        this.mCanvasRect.set(0.0f, 0.0f, size, i3);
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mDisplayRect.set(0, 0, i3 - i, ((View) getParent()).getHeight());
        if (this.mBitmapPositionHolder == null) {
            return;
        }
        int childCount = getChildCount();
        BitmapPositionHolder bitmapPositionHolder = this.mBitmapPositionHolder;
        int i5 = bitmapPositionHolder.bitmapWidth;
        int i6 = bitmapPositionHolder.horizontalOffset;
        int i7 = bitmapPositionHolder.verticalOffset;
        int i8 = i6 > 0 ? i7 + 0 : 0;
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            int i10 = this.mBitmapPositionHolder.bitmapHeights[i9] + i8;
            int i11 = i5 + i6;
            childAt.layout(i6, i8, i11, i10);
            ((LayoutParams) childAt.getLayoutParams()).set(i6, i8, i11, i10);
            i8 = i10 + i7;
        }
        refreshScrollLimit();
        CollageImageView collageImageView = this.mItemDragHelper.mCollageImageView;
        if (collageImageView == null) {
            return;
        }
        getChildRect(this.mRectF, collageImageView);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        canvas.drawRect(this.mCanvasRect, this.mBackgroundPaint);
        super.dispatchDraw(canvas);
        if (this.mActiveView != null) {
            int scrollY = getScrollY();
            LayoutParams layoutParams = (LayoutParams) this.mActiveView.getLayoutParams();
            this.mRectF.set(layoutParams.left, layoutParams.top, layoutParams.right, layoutParams.bottom);
            this.mRectF.offset(0.0f, -scrollY);
            RectF rectF = this.mRectF;
            Rect rect = this.mDisplayRect;
            rectF.intersect(rect.left, rect.top, rect.right, rect.bottom);
            this.mRectF.offset(0.0f, scrollY);
            this.mPath.reset();
            this.mPath.addRect(this.mRectF, Path.Direction.CW);
            canvas.save();
            canvas.clipRect(this.mRectF);
            int i = this.mActiveLineWidth;
            this.mPathPaint.setStrokeWidth(this.mActiveLineWidthWhite + i);
            this.mPathPaint.setColor(-1);
            canvas.drawPath(this.mPath, this.mPathPaint);
            this.mPathPaint.setStrokeWidth(i);
            this.mPathPaint.setColor(this.mStrokeColor);
            canvas.drawPath(this.mPath, this.mPathPaint);
            canvas.restore();
        }
        this.mItemDragHelper.draw(canvas);
    }

    public final void swipeViewIndex(int i, int i2) {
        if (!this.mEnableDragMode) {
            return;
        }
        int min = Math.min(i, i2);
        int max = Math.max(i, i2);
        TransitionManager.beginDelayedTransition(this);
        LinearMotorHelper.performHapticFeedback(getRootView(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
        swipeArrays(min, max);
        View childAt = getChildAt(min);
        View childAt2 = getChildAt(max);
        removeView(childAt);
        removeView(childAt2);
        addView(childAt2, min);
        addView(childAt, max);
    }

    public final int appendScroll(float f) {
        int scrollY = getScrollY() + Math.round(f);
        int i = this.mMaxScrollY;
        if (scrollY > i || scrollY < (i = this.mMinScrollY)) {
            scrollY = i;
        }
        int scrollY2 = scrollY - getScrollY();
        if (scrollY2 != 0) {
            scrollTo(getScrollX(), scrollY);
            invalidate();
        }
        return scrollY2;
    }

    public final void scrollToMin() {
        scrollTo(getScrollX(), this.mMinScrollY);
        invalidate();
    }

    public final void refreshScrollLimit() {
        this.mMinScrollY = getTop();
        if (getBottom() - getTop() > this.mDisplayRect.height()) {
            this.mMaxScrollY = getBottom() - this.mDisplayRect.height();
        } else {
            this.mMaxScrollY = 0;
        }
    }

    public void setBitmaps(Bitmap[] bitmapArr) {
        removeAllViews();
        this.mBitmaps = bitmapArr;
        for (Bitmap bitmap : bitmapArr) {
            CollageImageView collageImageView = new CollageImageView(getContext());
            collageImageView.setBitmap(bitmap);
            collageImageView.setBackgroundColor(this.mBackGroundColor);
            addView(collageImageView, new LayoutParams());
            this.mImageViewMap.put(bitmap, collageImageView);
        }
        this.mBitmapPositionHolder = new BitmapPositionHolder(this.mBitmaps.length);
        requestLayout();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            invalidate();
        }
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View view, long j) {
        return super.drawChild(canvas, view, j);
    }

    public final void swipeArrays(int i, int i2) {
        Bitmap[] bitmapArr = this.mBitmaps;
        Bitmap bitmap = bitmapArr[i];
        bitmapArr[i] = bitmapArr[i2];
        bitmapArr[i2] = bitmap;
    }

    public final void notifyZoomOut(float f) {
        doZoomOutAnimator(f);
    }

    public final void notifyZoomIn(float f) {
        doZoomInAnimator();
    }

    public final void computeScrollSpeed(float f) {
        boolean z = f < 0.0f;
        float abs = Math.abs(f);
        if (abs < 100.0f) {
            this.mScrollSpeed = 0.0f;
            return;
        }
        if (abs > this.mDisplayRect.height()) {
            abs = this.mDisplayRect.height();
        }
        float height = (((abs - 100.0f) * 65.0f) / (this.mDisplayRect.height() - 100)) + 5.0f;
        this.mScrollSpeed = height;
        if (!z) {
            return;
        }
        this.mScrollSpeed = -height;
    }

    public final void startContinueScroll() {
        ValueAnimator valueAnimator = this.mScrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mScrollAnimator = ofFloat;
        ofFloat.setDuration(1000L);
        this.mScrollAnimator.setRepeatCount(-1);
        this.mScrollAnimator.addUpdateListener(this.mScrollAnimatorUpdateListener);
        this.mScrollAnimator.start();
    }

    public final void cancelContinueScroll() {
        ValueAnimator valueAnimator = this.mScrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mScrollAnimator = null;
        }
    }

    public final void doZoomOutAnimator(float f) {
        setPivotY(f);
        ViewPropertyAnimator animate = animate();
        animate.scaleX(0.6f);
        animate.scaleY(0.6f);
        animate.setDuration(300L);
        animate.start();
    }

    public final void doZoomInAnimator() {
        ViewPropertyAnimator animate = animate();
        animate.scaleX(1.0f);
        animate.scaleY(1.0f);
        animate.setDuration(300L);
        animate.start();
    }

    public final CollageImageView findTargetView(float f) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            CollageImageView collageImageView = (CollageImageView) getChildAt(i);
            getChildRect(this.mRectF, collageImageView);
            convertCoordinateToParent(this.mRectF);
            RectF rectF = this.mRectF;
            if (f > rectF.top && f <= rectF.bottom) {
                return collageImageView;
            }
        }
        return null;
    }

    /* loaded from: classes.dex */
    public class ItemDragHelper {
        public ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
        public RectF mClipRect;
        public CollageImageView mCollageImageView;
        public RectF mCurrentBitmapRect;
        public float mCurrentY;
        public Bitmap mDragBitmap;
        public RectF mDragBitmapRect;
        public ValueAnimator mHideAnimator;
        public Animator.AnimatorListener mHideListener;
        public boolean mInDragMode;
        public Matrix mMatrix;
        public Paint mPaint;
        public ValueAnimator mShowAnimator;
        public RectF mViewRect;

        public ItemDragHelper() {
            this.mPaint = new Paint(3);
            this.mDragBitmapRect = new RectF();
            this.mViewRect = new RectF();
            this.mClipRect = new RectF();
            this.mCurrentBitmapRect = new RectF();
            this.mMatrix = new Matrix();
            this.mShowAnimator = null;
            this.mHideAnimator = null;
            this.mInDragMode = false;
            this.mCurrentY = 0.0f;
            this.mHideListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.ItemDragHelper.1
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
                    ItemDragHelper.this.mCollageImageView.setVisibility(0);
                    ItemDragHelper itemDragHelper = ItemDragHelper.this;
                    itemDragHelper.mDragBitmap = null;
                    itemDragHelper.mCollageImageView = null;
                    CollageStitchingLayout.this.invalidate();
                }
            };
            this.mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.ItemDragHelper.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ItemDragHelper.this.mPaint.setAlpha(((Integer) valueAnimator.getAnimatedValue("property_name_alpha")).intValue());
                    Object animatedValue = valueAnimator.getAnimatedValue("property_name_matrix");
                    if (animatedValue != null) {
                        ItemDragHelper.this.mMatrix.set((Matrix) animatedValue);
                    }
                    Object animatedValue2 = valueAnimator.getAnimatedValue("property_name_rect");
                    if (animatedValue2 != null) {
                        ItemDragHelper.this.mClipRect.set((RectF) animatedValue2);
                    }
                    CollageStitchingLayout.this.invalidate();
                }
            };
        }

        public void enableDragMode(CollageImageView collageImageView, float f) {
            this.mInDragMode = false;
            if (collageImageView == null) {
                this.mCollageImageView = null;
                this.mDragBitmap = null;
                return;
            }
            Bitmap bitmap = collageImageView.getBitmap();
            this.mDragBitmap = bitmap;
            if (bitmap == null) {
                this.mCollageImageView = null;
                return;
            }
            this.mInDragMode = true;
            this.mCollageImageView = collageImageView;
            this.mDragBitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), this.mDragBitmap.getHeight());
            CollageStitchingLayout.this.getChildRect(this.mViewRect, collageImageView);
            CollageStitchingLayout.this.convertCoordinateToParent(this.mViewRect);
            this.mClipRect.set(this.mViewRect);
            CollageRender.initBitmapMatrix(this.mDragBitmapRect, this.mMatrix, this.mViewRect, collageImageView.isMirror(), collageImageView.getRotateDegree(), CollageStitchingLayout.this.mRectF);
            this.mCurrentY = f;
            this.mMatrix.mapRect(this.mCurrentBitmapRect, this.mDragBitmapRect);
            this.mCollageImageView.setVisibility(4);
            startShowAnimator();
        }

        public void disableDragMode() {
            if (!this.mInDragMode || this.mCollageImageView == null) {
                return;
            }
            startHideAnimator();
        }

        public void onScroll(float f) {
            if (!this.mInDragMode) {
                return;
            }
            this.mCurrentY -= f;
            float f2 = -f;
            this.mMatrix.postTranslate(0.0f, f2);
            this.mMatrix.mapRect(this.mCurrentBitmapRect, this.mDragBitmapRect);
            this.mClipRect.offset(0.0f, f2);
            notifySwipe(f2);
            CollageStitchingLayout.this.invalidate();
        }

        public void draw(Canvas canvas) {
            if (!this.mInDragMode || this.mDragBitmap == null) {
                return;
            }
            canvas.save();
            canvas.translate(0.0f, CollageStitchingLayout.this.getScrollY());
            canvas.clipRect(this.mClipRect);
            canvas.drawBitmap(this.mDragBitmap, this.mMatrix, this.mPaint);
            canvas.restore();
        }

        public void notifySwipe(float f) {
            int indexOfChild;
            int indexOfChild2;
            View childAt;
            if (!this.mInDragMode || this.mCollageImageView == null) {
                return;
            }
            DefaultLogger.d("CollageStitchingLayout", "notifySwipe distance : %f", Float.valueOf(f));
            if (f <= 0.0f) {
                if (f >= 0.0f || (childAt = CollageStitchingLayout.this.getChildAt((indexOfChild = CollageStitchingLayout.this.indexOfChild(this.mCollageImageView)) - 1)) == null) {
                    return;
                }
                CollageStitchingLayout collageStitchingLayout = CollageStitchingLayout.this;
                collageStitchingLayout.getChildRect(collageStitchingLayout.mRectF, childAt);
                CollageStitchingLayout collageStitchingLayout2 = CollageStitchingLayout.this;
                collageStitchingLayout2.convertCoordinateToParent(collageStitchingLayout2.mRectF);
                if (CollageStitchingLayout.this.mRectF.bottom - this.mClipRect.top < CollageStitchingLayout.this.mRectF.height()) {
                    return;
                }
                CollageStitchingLayout.this.swipeViewIndex(indexOfChild, indexOfChild2);
                return;
            }
            int indexOfChild3 = CollageStitchingLayout.this.indexOfChild(this.mCollageImageView);
            int i = indexOfChild3 + 1;
            View childAt2 = CollageStitchingLayout.this.getChildAt(i);
            if (childAt2 == null) {
                return;
            }
            CollageStitchingLayout collageStitchingLayout3 = CollageStitchingLayout.this;
            collageStitchingLayout3.getChildRect(collageStitchingLayout3.mRectF, childAt2);
            CollageStitchingLayout collageStitchingLayout4 = CollageStitchingLayout.this;
            collageStitchingLayout4.convertCoordinateToParent(collageStitchingLayout4.mRectF);
            if (this.mClipRect.bottom - CollageStitchingLayout.this.mRectF.top < CollageStitchingLayout.this.mRectF.height()) {
                return;
            }
            CollageStitchingLayout.this.swipeViewIndex(indexOfChild3, i);
        }

        public void startShowAnimator() {
            ValueAnimator valueAnimator = this.mShowAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofInt("property_name_alpha", 255, Math.round(127.5f)));
            this.mShowAnimator = ofPropertyValuesHolder;
            ofPropertyValuesHolder.setDuration(300L);
            this.mShowAnimator.addUpdateListener(this.mAnimatorUpdateListener);
            this.mShowAnimator.start();
        }

        public void startHideAnimator() {
            ValueAnimator valueAnimator = this.mShowAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            Matrix matrix = new Matrix();
            CollageStitchingLayout collageStitchingLayout = CollageStitchingLayout.this;
            collageStitchingLayout.getChildRect(collageStitchingLayout.mRectF, this.mCollageImageView);
            CollageStitchingLayout collageStitchingLayout2 = CollageStitchingLayout.this;
            collageStitchingLayout2.convertCoordinateToParent(collageStitchingLayout2.mRectF);
            CollageRender.initBitmapMatrix(this.mDragBitmapRect, matrix, CollageStitchingLayout.this.mRectF, this.mCollageImageView.isMirror(), this.mCollageImageView.getRotateDegree(), new RectF());
            ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofInt("property_name_alpha", this.mPaint.getAlpha(), 255), PropertyValuesHolder.ofObject("property_name_matrix", new MatrixEvaluator(), new Matrix(this.mMatrix), matrix), PropertyValuesHolder.ofObject("property_name_rect", new RectFEvaluator(), new RectF(this.mClipRect), CollageStitchingLayout.this.mRectF));
            this.mHideAnimator = ofPropertyValuesHolder;
            ofPropertyValuesHolder.setDuration(300L);
            this.mHideAnimator.addUpdateListener(this.mAnimatorUpdateListener);
            this.mHideAnimator.addListener(this.mHideListener);
            this.mHideAnimator.start();
        }
    }

    public final void convertCoordinateToParent(RectF rectF) {
        rectF.offset(0.0f, -getScrollY());
    }

    public final void getChildRect(RectF rectF, View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        rectF.set(layoutParams.left, layoutParams.top, layoutParams.right, layoutParams.bottom);
    }

    public void notifyBitmapReplace(Bitmap bitmap, Bitmap bitmap2) {
        int i = 0;
        while (true) {
            Bitmap[] bitmapArr = this.mBitmaps;
            if (i >= bitmapArr.length) {
                break;
            } else if (bitmapArr[i] == bitmap) {
                bitmapArr[i] = bitmap2;
                break;
            } else {
                i++;
            }
        }
        CollageImageView collageImageView = this.mImageViewMap.get(bitmap);
        collageImageView.setBitmap(bitmap2);
        this.mImageViewMap.remove(bitmap);
        this.mImageViewMap.put(bitmap2, collageImageView);
        requestLayout();
        post(new Runnable() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.3
            @Override // java.lang.Runnable
            public void run() {
                CollageStitchingLayout.this.scrollToMin();
            }
        });
    }

    public RenderData generateRenderData() {
        RenderData renderData = new RenderData();
        int childCount = getChildCount();
        renderData.bitmapRenderDatas = new CollageRender.BitmapRenderData[childCount];
        for (int i = 0; i < childCount; i++) {
            renderData.bitmapRenderDatas[i] = ((CollageImageView) getChildAt(i)).generateBitmapRenderData();
        }
        renderData.stitchingModel = this.mStitchingModel;
        renderData.viewWidth = getWidth();
        return renderData;
    }

    public void setStitchingModel(StitchingModel stitchingModel) {
        this.mStitchingModel = stitchingModel;
        setChildRadius(stitchingModel.radius);
        if (!TextUtils.isEmpty(stitchingModel.mask)) {
            Resources resources = getResources();
            setChildMask(CollageUtils.getDrawableByAssets(resources, stitchingModel.relativePath + File.separator + stitchingModel.mask));
        } else {
            setChildMask(null);
        }
        requestLayout();
        post(new Runnable() { // from class: com.miui.gallery.collage.widget.CollageStitchingLayout.4
            @Override // java.lang.Runnable
            public void run() {
                CollageStitchingLayout.this.scrollToMin();
            }
        });
    }

    public final CollageImageView findSingleView(float f, float f2) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            CollageImageView collageImageView = (CollageImageView) getChildAt(i);
            getChildRect(this.mRectF, collageImageView);
            convertCoordinateToParent(this.mRectF);
            if (this.mRectF.contains(f, f2)) {
                return collageImageView;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActiveView(CollageImageView collageImageView) {
        if (collageImageView != null && collageImageView != this.mActiveView) {
            this.mActiveView = collageImageView;
            this.mControlPopupWindow.showAtLocation((View) getParent(), this.mActiveView, true);
        } else {
            dismissControlWindow();
            this.mActiveView = null;
        }
        invalidate();
    }

    public void dismissControlWindow() {
        ControlPopupWindow controlPopupWindow = this.mControlPopupWindow;
        if (controlPopupWindow != null && controlPopupWindow.isShowing()) {
            this.mControlPopupWindow.dismiss();
        }
        if (this.mActiveView != null) {
            this.mActiveView = null;
            invalidate();
        }
    }

    public void setReplaceImageListener(CollageActivity.ReplaceImageListener replaceImageListener) {
        this.mReplaceImageListener = replaceImageListener;
    }

    private void setChildRadius(int i) {
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            ((CollageImageView) getChildAt(i2)).setRadius(i);
        }
    }

    private void setChildMask(Drawable drawable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ((CollageImageView) getChildAt(i)).setMask(drawable);
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int bottom;
        public int left;
        public int right;
        public int top;

        public LayoutParams() {
            super(0, 0);
        }

        public void set(int i, int i2, int i3, int i4) {
            this.left = i;
            this.top = i2;
            this.right = i3;
            this.bottom = i4;
        }
    }

    /* loaded from: classes.dex */
    public static class BitmapPositionHolder {
        public final int[] bitmapHeights;
        public int bitmapWidth;
        public int height;
        public int horizontalOffset;
        public int verticalOffset;

        public BitmapPositionHolder(int i) {
            this.bitmapHeights = new int[i];
        }
    }
}
