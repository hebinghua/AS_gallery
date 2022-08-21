package com.miui.gallery.collage.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Keep;
import com.miui.gallery.R;
import com.miui.gallery.app.MatrixEvaluator;
import com.miui.gallery.collage.ClipType;
import com.miui.gallery.collage.CollageActivity;
import com.miui.gallery.collage.render.CollageRender;
import com.miui.gallery.collage.widget.ControlPopupWindow;
import com.miui.gallery.editor.photo.core.imports.obsoletes.RectFEvaluator;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import com.miui.gallery.widget.imageview.SensitiveScaleGestureDetector;
import miuix.view.animation.CubicEaseInOutInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes.dex */
public class CollageLayout extends ViewGroup {
    public boolean mActivating;
    public int mActiveIndex;
    public int mActiveLineWidth;
    public int mActiveLineWidthWhite;
    public BitmapExchangeListener mBitmapExchangeListener;
    public Context mContext;
    public ControlPopupWindow mControlPopupWindow;
    public float mDensity;
    public DragImageHolder mDragImageHolder;
    public GestureDetector mGestureDetector;
    public CustomGestureListener mGestureListener;
    public boolean mIgnoreEdgeMargin;
    public CollageRender.ImageLocationProcessor mImageLocationProcessor;
    public float mMargin;
    public Paint mPathPaint;
    public PopupListener mPopupListener;
    public CollageActivity.ReplaceImageListener mReplaceImageListener;
    public ScaleGestureDetector mScaleGestureDetector;
    public int mStrokeColor;

    /* loaded from: classes.dex */
    public interface BitmapExchangeListener {
        void onBitmapExchange(Bitmap bitmap, Bitmap bitmap2);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
    }

    public CollageLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mGestureListener = new CustomGestureListener();
        this.mDragImageHolder = new DragImageHolder();
        this.mPopupListener = new PopupListener();
        this.mPathPaint = new Paint();
        this.mMargin = 0.0f;
        this.mActiveIndex = -1;
        this.mImageLocationProcessor = new CollageRender.ImageLocationProcessor();
        this.mActivating = false;
        this.mIgnoreEdgeMargin = false;
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        this.mGestureDetector = new GestureDetector(this.mContext, this.mGestureListener);
        this.mScaleGestureDetector = new SensitiveScaleGestureDetector(this.mContext, this.mGestureListener);
        setWillNotDraw(false);
        ControlPopupWindow controlPopupWindow = new ControlPopupWindow(context);
        this.mControlPopupWindow = controlPopupWindow;
        controlPopupWindow.setControlListener(this.mPopupListener);
        this.mStrokeColor = context.getResources().getColor(R.color.collage_table_text_color_checked);
        this.mActiveLineWidth = context.getResources().getDimensionPixelSize(R.dimen.collage_stroke_line_width);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.collage_stroke_line_width_white);
        this.mActiveLineWidthWhite = dimensionPixelSize;
        int i = this.mActiveLineWidth;
        if (i % 2 != 0) {
            this.mActiveLineWidth = i + 1;
        }
        if (dimensionPixelSize % 2 != 0) {
            this.mActiveLineWidthWhite = dimensionPixelSize + 1;
        }
        this.mPathPaint.setStyle(Paint.Style.STROKE);
        this.mPathPaint.setStrokeWidth(10.0f);
        this.mDensity = context.getResources().getDisplayMetrics().density;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        measureChildren(i, i2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int i = this.mActiveIndex;
        if (i != -1) {
            LayoutParams layoutParams = (LayoutParams) ((CollageImageView) getChildAt(i)).getLayoutParams();
            canvas.save();
            canvas.clipPath(layoutParams.mImageLocation.getPathForClip());
            int i2 = this.mActiveLineWidth;
            this.mPathPaint.setStrokeWidth(this.mActiveLineWidthWhite + i2);
            this.mPathPaint.setColor(-1);
            canvas.drawPath(layoutParams.mImageLocation.getPathForClip(), this.mPathPaint);
            this.mPathPaint.setStrokeWidth(i2);
            this.mPathPaint.setColor(this.mStrokeColor);
            canvas.drawPath(layoutParams.mImageLocation.getPathForClip(), this.mPathPaint);
            canvas.restore();
        }
        this.mDragImageHolder.draw(canvas);
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View view, long j) {
        canvas.save();
        canvas.clipPath(((LayoutParams) view.getLayoutParams()).mImageLocation.getPathForClip());
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int leftPadding = getLeftPadding();
        int rightPadding = getRightPadding();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = (getWidth() - leftPadding) - rightPadding;
        int height = (getHeight() - paddingTop) - paddingBottom;
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                this.mImageLocationProcessor.processorImageLocation(layoutParams.mImageLocation, width, height, this.mMargin, this.mIgnoreEdgeMargin);
                childAt.layout(layoutParams.mImageLocation.getLeft() + leftPadding, layoutParams.mImageLocation.getTop() + paddingTop, layoutParams.mImageLocation.getRight() + leftPadding, layoutParams.mImageLocation.getBottom() + paddingTop);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0015, code lost:
        if (r0 != 3) goto L7;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r4) {
        /*
            r3 = this;
            com.miui.gallery.widget.imageview.ScaleGestureDetector r0 = r3.mScaleGestureDetector
            r0.onTouchEvent(r4)
            android.view.GestureDetector r0 = r3.mGestureDetector
            r0.onTouchEvent(r4)
            int r0 = r4.getAction()
            r1 = 1
            if (r0 == r1) goto L1e
            r2 = 2
            if (r0 == r2) goto L18
            r4 = 3
            if (r0 == r4) goto L1e
            goto L23
        L18:
            com.miui.gallery.collage.widget.CollageLayout$CustomGestureListener r0 = r3.mGestureListener
            r0.onActionMove(r4)
            goto L23
        L1e:
            com.miui.gallery.collage.widget.CollageLayout$CustomGestureListener r4 = r3.mGestureListener
            r4.onUp()
        L23:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.collage.widget.CollageLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    /* loaded from: classes.dex */
    public class CustomGestureListener implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
        public CollageImageView mDownView;
        public CollageImageView mDragTargetView;
        public boolean mIsLongPressMode;
        public float mLastX;
        public float mLastY;

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        public CustomGestureListener() {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            this.mDownView = CollageLayout.this.findSingleView(motionEvent.getX(), motionEvent.getY());
            this.mLastX = motionEvent.getX();
            this.mLastY = motionEvent.getY();
            this.mIsLongPressMode = false;
            CollageLayout.this.mActivating = true;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (this.mDownView != null) {
                if (CollageLayout.this.mActiveIndex == CollageLayout.this.indexOfChild(this.mDownView)) {
                    CollageLayout.this.mActiveIndex = -1;
                    CollageLayout.this.dismissControlWindow();
                } else {
                    CollageLayout.this.setActive(this.mDownView);
                    CollageLayout.this.getLocationInWindow(new int[2]);
                    CollageLayout.this.mControlPopupWindow.showAtLocation(CollageLayout.this, this.mDownView);
                    CollageLayout.this.mPopupListener.setCollageSingleView(this.mDownView);
                }
            } else {
                CollageLayout.this.mActiveIndex = -1;
                CollageLayout.this.dismissControlWindow();
            }
            CollageLayout.this.invalidate();
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            CollageImageView collageImageView = this.mDownView;
            if (collageImageView == null || this.mIsLongPressMode) {
                return false;
            }
            collageImageView.transition(-f, -f2);
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            DefaultLogger.d("CollageLayout", "onLongPress");
            if (this.mDownView != null) {
                CollageLayout.this.dismissControlWindow();
                this.mIsLongPressMode = true;
                motionEvent.getX();
                motionEvent.getY();
                CollageLayout.this.mDragImageHolder.enableDragMode(this.mDownView);
                CollageLayout.this.mActiveIndex = -1;
                this.mDownView.setDrawBitmap(false);
                if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
                    LinearMotorHelper.performHapticFeedback(this.mDownView, LinearMotorHelper.HAPTIC_PICK_UP);
                } else {
                    CollageLayout.this.performHapticFeedback(0);
                }
                CollageLayout.this.invalidate();
            }
        }

        public void onUp() {
            if (this.mIsLongPressMode) {
                this.mIsLongPressMode = false;
                CollageImageView collageImageView = this.mDragTargetView;
                if (collageImageView == null || collageImageView == this.mDownView) {
                    CollageLayout.this.mDragImageHolder.resetBitmapWithAnim();
                } else {
                    CollageLayout.this.mActiveIndex = -1;
                    CollageLayout.this.mDragImageHolder.exchangeBitmapWithAnim(this.mDragTargetView);
                }
            } else {
                CollageImageView collageImageView2 = this.mDownView;
                if (collageImageView2 != null) {
                    collageImageView2.resetBitmapLocation();
                    CollageLayout.this.mActivating = false;
                }
            }
            CollageLayout.this.invalidate();
        }

        public void onActionMove(MotionEvent motionEvent) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            float f = x - this.mLastX;
            float f2 = y - this.mLastY;
            if (this.mDownView != null && this.mIsLongPressMode) {
                CollageLayout.this.mDragImageHolder.receiveScrollEvent(f, f2);
                CollageImageView findSingleView = CollageLayout.this.findSingleView(x, y);
                this.mDragTargetView = findSingleView;
                if (findSingleView == null || findSingleView == this.mDownView) {
                    CollageLayout.this.mActiveIndex = -1;
                } else {
                    int i = CollageLayout.this.mActiveIndex;
                    CollageLayout.this.setActive(this.mDragTargetView);
                    if (CollageLayout.this.mActiveIndex != i && CollageLayout.this.mActiveIndex >= 0) {
                        LinearMotorHelper.performHapticFeedback(this.mDownView, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                    }
                }
            }
            this.mLastX = x;
            this.mLastY = y;
            CollageLayout.this.invalidate();
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (this.mDownView != null) {
                this.mDownView.appendScale(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                return true;
            }
            return true;
        }
    }

    public final CollageImageView findSingleView(float f, float f2) {
        for (int i = 0; i < getChildCount(); i++) {
            CollageImageView collageImageView = (CollageImageView) getChildAt(i);
            if (((LayoutParams) collageImageView.getLayoutParams()).contains(f, f2)) {
                return collageImageView;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActive(View view) {
        this.mActiveIndex = indexOfChild(view);
    }

    public void dismissControlWindow() {
        ControlPopupWindow controlPopupWindow = this.mControlPopupWindow;
        if (controlPopupWindow == null || !controlPopupWindow.isShowing()) {
            return;
        }
        this.mControlPopupWindow.dismiss();
    }

    /* loaded from: classes.dex */
    public class DragImageHolder {
        public CollageImageView mDownView;
        public DragBitmapItem mDragBitmapItem;
        public boolean mDragEnable;
        public boolean mExchangeEnable;
        public DragBitmapItem mTargetBitmapItem;
        public ValueAnimator.AnimatorUpdateListener mUpdateListener;

        public DragImageHolder() {
            this.mDragEnable = false;
            this.mExchangeEnable = false;
            this.mDragBitmapItem = new DragBitmapItem();
            this.mTargetBitmapItem = new DragBitmapItem();
            this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.collage.widget.CollageLayout.DragImageHolder.3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    CollageLayout.this.invalidate();
                }
            };
        }

        public void enableDragMode(CollageImageView collageImageView) {
            this.mDownView = collageImageView;
            this.mDragBitmapItem.setCollageImageView(collageImageView);
            this.mDragEnable = true;
            this.mExchangeEnable = false;
            this.mDragBitmapItem.show(this.mUpdateListener);
        }

        public void disableDragMode() {
            this.mDragEnable = false;
            this.mExchangeEnable = false;
            CollageLayout.this.mActivating = false;
            CollageLayout.this.invalidate();
        }

        public void receiveScrollEvent(float f, float f2) {
            this.mDragBitmapItem.receiveScrollEvent(f, f2);
            CollageLayout.this.invalidate();
        }

        public void draw(Canvas canvas) {
            if (!this.mDragEnable) {
                return;
            }
            if (this.mExchangeEnable) {
                this.mTargetBitmapItem.draw(canvas);
            }
            this.mDragBitmapItem.draw(canvas);
        }

        public void resetBitmapWithAnim() {
            this.mDragBitmapItem.reset(this.mUpdateListener, new Animator.AnimatorListener() { // from class: com.miui.gallery.collage.widget.CollageLayout.DragImageHolder.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (DragImageHolder.this.mDragBitmapItem.mShowAnimator != null) {
                        DragImageHolder.this.mDragBitmapItem.mShowAnimator.cancel();
                    }
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    DragImageHolder.this.mDownView.setDrawBitmap(true);
                    DragImageHolder.this.mDragBitmapItem.release();
                    DragImageHolder.this.disableDragMode();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    onAnimationEnd(animator);
                }
            });
        }

        public void exchangeBitmapWithAnim(final CollageImageView collageImageView) {
            this.mTargetBitmapItem.setCollageImageView(collageImageView);
            RectF rectF = new RectF();
            CollageLayout.getCollageImageViewRect(rectF, this.mDownView);
            ObjectAnimator transitionAnimator = this.mTargetBitmapItem.getTransitionAnimator(rectF, this.mUpdateListener);
            RectF rectF2 = new RectF();
            CollageLayout.getCollageImageViewRect(rectF2, collageImageView);
            ObjectAnimator transitionAnimator2 = this.mDragBitmapItem.getTransitionAnimator(rectF2, this.mUpdateListener);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(350L);
            animatorSet.setInterpolator(new CubicEaseInOutInterpolator());
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.collage.widget.CollageLayout.DragImageHolder.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    collageImageView.setDrawBitmap(false);
                    DragImageHolder.this.mExchangeEnable = true;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    DragImageHolder.this.mDownView.resetDrawData(DragImageHolder.this.mTargetBitmapItem.mBitmap, DragImageHolder.this.mTargetBitmapItem.mRotateDegree, DragImageHolder.this.mTargetBitmapItem.mMirror);
                    collageImageView.resetDrawData(DragImageHolder.this.mDragBitmapItem.mBitmap, DragImageHolder.this.mDragBitmapItem.mRotateDegree, DragImageHolder.this.mDragBitmapItem.mMirror);
                    if (CollageLayout.this.mBitmapExchangeListener != null) {
                        CollageLayout.this.mBitmapExchangeListener.onBitmapExchange(DragImageHolder.this.mDragBitmapItem.mBitmap, DragImageHolder.this.mTargetBitmapItem.mBitmap);
                    }
                    DragImageHolder.this.mTargetBitmapItem.release();
                    DragImageHolder.this.mDragBitmapItem.release();
                    DragImageHolder.this.disableDragMode();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    onAnimationEnd(animator);
                }
            });
            animatorSet.playTogether(transitionAnimator2, transitionAnimator);
            animatorSet.start();
        }
    }

    public static void getCollageImageViewRect(RectF rectF, CollageImageView collageImageView) {
        collageImageView.getDisplayRect(rectF);
        rectF.offset(collageImageView.getLeft(), collageImageView.getTop());
    }

    /* loaded from: classes.dex */
    public static class DragBitmapItem {
        public Bitmap mBitmap;
        public Matrix mBitmapMatrix;
        public Paint mBitmapPaint;
        public RectF mBitmapRect;
        public RectF mDisplayOriginRect;
        public RectF mDisplayRect;
        public boolean mMirror;
        public RectF mRectTemp;
        public int mRotateDegree;
        public float mScale;
        public ObjectAnimator mShowAnimator;
        public ObjectAnimator mTransitionAnimator;
        public Matrix mUserMatrix;

        public DragBitmapItem() {
            this.mBitmapRect = new RectF();
            this.mDisplayOriginRect = new RectF();
            this.mDisplayRect = new RectF();
            this.mRectTemp = new RectF();
            this.mBitmapMatrix = new Matrix();
            this.mUserMatrix = new Matrix();
            this.mBitmapPaint = new Paint(3);
            this.mScale = 1.0f;
            this.mRotateDegree = 0;
            this.mMirror = false;
        }

        public void setCollageImageView(CollageImageView collageImageView) {
            this.mBitmap = collageImageView.getBitmap();
            this.mRotateDegree = collageImageView.getRotateDegree();
            this.mMirror = collageImageView.isMirror();
            this.mBitmapRect.set(0.0f, 0.0f, this.mBitmap.getWidth(), this.mBitmap.getHeight());
            CollageLayout.getCollageImageViewRect(this.mDisplayOriginRect, collageImageView);
            this.mDisplayRect.set(this.mDisplayOriginRect);
            refreshBitmapMatrix();
            collageImageView.getCanvasMatrix(this.mUserMatrix);
            this.mScale = 1.0f;
            this.mBitmapPaint.setAlpha(255);
        }

        public void receiveScrollEvent(float f, float f2) {
            this.mDisplayRect.offset(f, f2);
            refreshBitmapMatrix();
        }

        public void refreshBitmapMatrix() {
            CollageRender.initBitmapMatrix(this.mBitmapRect, this.mBitmapMatrix, this.mDisplayRect, this.mMirror, this.mRotateDegree, this.mRectTemp);
        }

        public void show(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("scale", this.mScale, 1.1f), PropertyValuesHolder.ofInt("alpha", this.mBitmapPaint.getAlpha(), 200));
            this.mShowAnimator = ofPropertyValuesHolder;
            ofPropertyValuesHolder.setDuration(380L);
            this.mShowAnimator.setInterpolator(new CubicEaseInOutInterpolator());
            this.mShowAnimator.addUpdateListener(animatorUpdateListener);
            this.mShowAnimator.start();
        }

        public void reset(ValueAnimator.AnimatorUpdateListener animatorUpdateListener, Animator.AnimatorListener animatorListener) {
            ObjectAnimator objectAnimator = this.mShowAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator transitionAnimator = getTransitionAnimator(this.mDisplayOriginRect, false, animatorUpdateListener);
            this.mTransitionAnimator = transitionAnimator;
            transitionAnimator.setDuration(220L);
            this.mTransitionAnimator.setInterpolator(new CubicEaseOutInterpolator());
            this.mTransitionAnimator.addListener(animatorListener);
            this.mTransitionAnimator.start();
        }

        public ObjectAnimator getTransitionAnimator(RectF rectF, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
            return getTransitionAnimator(rectF, true, animatorUpdateListener);
        }

        public ObjectAnimator getTransitionAnimator(RectF rectF, boolean z, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
            ObjectAnimator ofPropertyValuesHolder;
            if (z) {
                ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("scale", this.mScale, 1.0f), PropertyValuesHolder.ofInt("alpha", this.mBitmapPaint.getAlpha(), 255), PropertyValuesHolder.ofObject("displayRect", new RectFEvaluator(), new RectF(this.mDisplayRect), rectF), PropertyValuesHolder.ofObject("userMatrix", new MatrixEvaluator(), new Matrix(this.mUserMatrix), new Matrix()));
            } else {
                ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("scale", this.mScale, 1.0f), PropertyValuesHolder.ofInt("alpha", this.mBitmapPaint.getAlpha(), 255), PropertyValuesHolder.ofObject("displayRect", new RectFEvaluator(), new RectF(this.mDisplayRect), rectF));
            }
            ofPropertyValuesHolder.addUpdateListener(animatorUpdateListener);
            return ofPropertyValuesHolder;
        }

        public void draw(Canvas canvas) {
            canvas.save();
            float f = this.mScale;
            canvas.scale(f, f, this.mDisplayRect.centerX(), this.mDisplayRect.centerY());
            canvas.clipRect(this.mDisplayRect);
            RectF rectF = this.mDisplayRect;
            canvas.translate(rectF.left, rectF.top);
            canvas.concat(this.mUserMatrix);
            RectF rectF2 = this.mDisplayRect;
            canvas.translate(-rectF2.left, -rectF2.top);
            canvas.drawBitmap(this.mBitmap, this.mBitmapMatrix, this.mBitmapPaint);
            canvas.restore();
        }

        public void release() {
            this.mBitmap = null;
        }

        @Keep
        public void setScale(float f) {
            this.mScale = f;
        }

        @Keep
        public void setAlpha(int i) {
            this.mBitmapPaint.setAlpha(i);
        }

        @Keep
        public void setDisplayRect(RectF rectF) {
            this.mDisplayRect.set(rectF);
            refreshBitmapMatrix();
        }

        @Keep
        public void setUserMatrix(Matrix matrix) {
            this.mUserMatrix.set(matrix);
        }
    }

    /* loaded from: classes.dex */
    public class PopupListener implements ControlPopupWindow.ControlListener {
        public CollageImageView mCollageImageView;

        public PopupListener() {
        }

        @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
        public void onDismiss() {
            CollageLayout.this.mActiveIndex = -1;
            this.mCollageImageView = null;
            CollageLayout.this.invalidate();
        }

        @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
        public void onReplace() {
            if (CollageLayout.this.mReplaceImageListener != null) {
                CollageLayout.this.mReplaceImageListener.onReplace(this.mCollageImageView.getBitmap());
            }
        }

        @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
        public void onRotate() {
            CollageImageView collageImageView = this.mCollageImageView;
            if (collageImageView != null) {
                collageImageView.rotate();
            }
        }

        @Override // com.miui.gallery.collage.widget.ControlPopupWindow.ControlListener
        public void onMirror() {
            CollageImageView collageImageView = this.mCollageImageView;
            if (collageImageView != null) {
                collageImageView.mirror();
            }
        }

        public void setCollageSingleView(CollageImageView collageImageView) {
            this.mCollageImageView = collageImageView;
        }
    }

    private int getLeftPadding() {
        return Math.max(getPaddingLeft(), getPaddingStart());
    }

    private int getRightPadding() {
        return Math.max(getPaddingRight(), getPaddingEnd());
    }

    public void setCollageMargin(float f, boolean z) {
        this.mIgnoreEdgeMargin = z;
        this.mMargin = f;
        requestLayout();
    }

    public void setMasks(Drawable[] drawableArr) {
        int childCount = getChildCount();
        boolean z = drawableArr != null && drawableArr.length > 0;
        for (int i = 0; i < childCount; i++) {
            CollageImageView collageImageView = (CollageImageView) getChildAt(i);
            if (z && i < drawableArr.length) {
                collageImageView.setMask(drawableArr[i]);
            } else {
                collageImageView.setMask(null);
            }
        }
    }

    public void setRadius(float[] fArr) {
        int childCount = getChildCount();
        boolean z = fArr != null && fArr.length > 0;
        for (int i = 0; i < childCount; i++) {
            CollageImageView collageImageView = (CollageImageView) getChildAt(i);
            if (z && i < fArr.length) {
                collageImageView.setRadius(fArr[i]);
            } else {
                collageImageView.setRadius(0.0f);
            }
        }
    }

    public void setReplaceImageListener(CollageActivity.ReplaceImageListener replaceImageListener) {
        this.mReplaceImageListener = replaceImageListener;
    }

    public void setBitmapExchangeListener(BitmapExchangeListener bitmapExchangeListener) {
        this.mBitmapExchangeListener = bitmapExchangeListener;
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public CollageRender.ImageLocation mImageLocation;

        public LayoutParams(ClipType clipType, float[] fArr) {
            super(0, 0);
            this.mImageLocation = new CollageRender.ImageLocation(clipType, fArr);
        }

        public boolean contains(float f, float f2) {
            return this.mImageLocation.getPathRegion().contains(Math.round(f), Math.round(f2));
        }
    }

    public float getMargin() {
        return this.mMargin;
    }

    public boolean isIgnoreEdgeMargin() {
        return this.mIgnoreEdgeMargin;
    }

    public boolean isActivating() {
        return this.mActivating || isChildActivating();
    }

    public final boolean isChildActivating() {
        for (int i = 0; i < getChildCount(); i++) {
            if (((CollageImageView) getChildAt(i)).isActivating()) {
                return true;
            }
        }
        return false;
    }
}
