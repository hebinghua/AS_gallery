package com.github.chrisbanes.photoview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Scroller;
import com.android.internal.CompatHandler;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.chrisbanes.photoview.RotateGestureDetector;
import com.github.chrisbanes.photoview.log.LogManager;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.ktx.DisplayKt;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MatrixUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.photoview.BitmapRecycleCallback;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.TileBitProvider;
import com.miui.gallery.util.photoview.TileView;
import com.miui.gallery.util.photoview.TrimMemoryCallback;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.ValueTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.utils.EaseManager;
import miuix.springback.view.SpringBackLayout;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes.dex */
public class PhotoViewAttacher implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    public boolean isSharedElementTransitionRunning;
    public Drawable mAlphaDrawable;
    public OnBackgroundAlphaChangedListener mBackgroundAlphaChangedListener;
    public float mBaseRotation;
    public FlingRunnable mCurrentFlingRunnable;
    public int mDestStrokeAlpha;
    public boolean mDisableDragDownOut;
    public float mDragDownOutDx;
    public float mDragDownOutDy;
    public float mDragDownOutThreshold;
    public float mDragDownOutY;
    public boolean mDragHandled;
    public OnExitListener mExitListener;
    public GestureDetector mGestureDetector;
    public boolean mHasOperated;
    public boolean mHasScale;
    public float mHighScaleLevelLowerBound;
    public ImageView mImageView;
    public boolean mIntercepted;
    public boolean mIsDragDownOut;
    public boolean mIsLastTimeExiting;
    public boolean mIsSlipping;
    public int mIvBottom;
    public int mIvLeft;
    public int mIvRight;
    public int mIvTop;
    public float mLastScaleFocusX;
    public float mLastScaleFocusY;
    public View.OnLongClickListener mLongClickListener;
    public float mMaxDoubleTapScale;
    public float mMaxPointsScale;
    public GestureDetector.OnDoubleTapListener mNewOnDoubleTapListener;
    public View.OnClickListener mOnClickListener;
    public OnHandleTouchEventListener mOnHandleTouchEventListener;
    public OnPhotoViewDragDownOutListener mOnPhotoViewDragDownOutListener;
    public OnViewDragListener mOnViewDragListener;
    public int mOriginHeight;
    public int mOriginWidth;
    public OnOutsidePhotoTapListener mOutsidePhotoTapListener;
    public OnPhotoTapListener mPhotoTapListener;
    public OnPhotoViewTransitionListener mPhotoViewTransitionListener;
    public float mProgress;
    public RotateManager mRotateDetector;
    public OnScaleChangeListener mScaleChangeListener;
    public CustomGestureDetector mScaleDragDetector;
    public boolean mScaleDragEnabled;
    public OnScaleLevelChangedListener mScaleStageChangedListener;
    public OnScaleStateChangeListener mScaleStateChangeListener;
    public OnSingleFlingListener mSingleFlingListener;
    public int mSlippedRectBottom;
    public int mSlippedRectTop;
    public int mStrokeAlpha;
    public int mStrokeColor;
    public Paint mStrokePaint;
    public int mStrokeWidth;
    public boolean mSupportHd;
    public TileView mTileView;
    public float mTouchDownY;
    public Transition mTransition;
    public boolean mUseFillWidthMode;
    public OnViewSingleTapListener mViewSingleTapListener;
    public OnViewTapListener mViewTapListener;
    public WaitingTransition mWaitingTransition;
    public boolean mZoomEnabled;
    public final Matrix mBaseMatrix = new Matrix();
    public final Matrix mDrawMatrix = new Matrix();
    public final Matrix mSuppMatrix = new Matrix();
    public final RectF mDisplayRect = new RectF();
    public final float[] mMatrixValues = new float[9];
    public Interpolator mZoomInterpolator = new AccelerateDecelerateInterpolator();
    public int mZoomDuration = 200;
    public float mMinScale = 1.0f;
    public float mMidScale = 1.75f;
    public float mMaxScale = 2.5f;
    public float mExitScale = 0.8f;
    public float mDownScale = 1.0f;
    public float mPointsScaleEnlargeFactor = 2.0f;
    public float mZoomDurationLengthenFactor = 1.5f;
    public float mBaseScale = 1.0f;
    public boolean mAllowParentInterceptOnEdge = true;
    public Set<OnMatrixChangedListener> mMatrixChangeListeners = ConcurrentHashMap.newKeySet();
    public Set<OnRotateListener> mOnRotateListeners = new CopyOnWriteArraySet();
    public int mHorizontalScrollEdge = 2;
    public int mVerticalScrollEdge = 2;
    public Rect mOutOfBoundsRect = new Rect(0, 0, 0, 0);
    public boolean mIsDragInsideBoundsEnabled = false;
    public boolean mIsTouchInProgress = false;
    public boolean mPendingDisableDragInsideBounds = false;
    public boolean mRotateEnabled = true;
    public ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;
    public boolean mIsRegionDecodeEnable = true;
    public int mAnim = 0;
    public float mAlpha = 1.0f;
    public OnGestureListener mOnGestureListener = new OnGestureListener() { // from class: com.github.chrisbanes.photoview.PhotoViewAttacher.1
        {
            PhotoViewAttacher.this = this;
        }

        @Override // com.github.chrisbanes.photoview.OnGestureListener
        public void onDrag(float f, float f2) {
            if (PhotoViewAttacher.this.interceptDrag()) {
                return;
            }
            if (PhotoViewAttacher.this.getScale() < PhotoViewAttacher.this.mMinScale && !PhotoViewAttacher.this.mIsDragDownOut) {
                return;
            }
            if (PhotoViewAttacher.this.mOnViewDragListener != null) {
                PhotoViewAttacher.this.mOnViewDragListener.onDrag(f, f2);
            }
            if (!PhotoViewAttacher.this.mHasScale && (PhotoViewAttacher.this.mIsDragDownOut || (PhotoViewAttacher.this.mVerticalScrollEdge == 2 && Math.abs(f2) > Math.abs(f) && PhotoViewAttacher.this.mTouchDownY > PhotoViewAttacher.this.mImageView.getResources().getDisplayMetrics().density * 25.0f))) {
                PhotoViewAttacher.access$816(PhotoViewAttacher.this, f2);
                if (!PhotoViewAttacher.this.mIsDragDownOut && PhotoViewAttacher.this.mDragDownOutY >= PhotoViewAttacher.this.mDragDownOutThreshold && !PhotoViewAttacher.this.mDisableDragDownOut) {
                    PhotoViewAttacher.this.mIsDragDownOut = true;
                    PhotoViewAttacher.this.mDragHandled = true;
                }
                if (PhotoViewAttacher.this.mIsDragDownOut && PhotoViewAttacher.this.mDragDownOutY >= PhotoViewAttacher.this.mDragDownOutThreshold && PhotoViewAttacher.this.mOnPhotoViewDragDownOutListener != null) {
                    PhotoViewAttacher.this.mOnPhotoViewDragDownOutListener.onPhotoDragDownOut();
                }
            }
            if (!PhotoViewAttacher.this.mIsDragDownOut) {
                if (PhotoViewAttacher.this.mZoomEnabled) {
                    PhotoViewAttacher.this.postTranslate(f, f2);
                }
            } else {
                float scale = PhotoViewAttacher.this.getScale();
                PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
                int imageViewWidth = photoViewAttacher.getImageViewWidth(photoViewAttacher.mImageView);
                PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
                int imageViewHeight = photoViewAttacher2.getImageViewHeight(photoViewAttacher2.mImageView);
                PhotoViewAttacher photoViewAttacher3 = PhotoViewAttacher.this;
                float value = photoViewAttacher3.getValue(photoViewAttacher3.mSuppMatrix, 5);
                float f3 = imageViewHeight;
                float f4 = ((1.0f - (f2 / f3)) * 0.5f) + 0.5f;
                if (f2 < 0.0f && scale >= 1.0f) {
                    f4 = 1.0f;
                }
                PhotoViewAttacher.this.postScale(f4, f4, imageViewWidth / 2.0f, (f3 + value) / 2.0f);
                float calculateDy = calculateDy(scale, value, f2);
                float f5 = f / 2.0f;
                PhotoViewAttacher.access$1816(PhotoViewAttacher.this, f5);
                PhotoViewAttacher.access$1916(PhotoViewAttacher.this, calculateDy);
                PhotoViewAttacher.this.postTranslate(f5, calculateDy);
                PhotoViewAttacher.this.updateBackgroundAlpha(1.0f - (value / f3));
                PhotoViewAttacher photoViewAttacher4 = PhotoViewAttacher.this;
                photoViewAttacher4.dispatchScaleStageChange(f4, scale, photoViewAttacher4.getScale(), false);
            }
            PhotoViewAttacher.this.checkAndDisplayMatrix();
            ViewParent parent = PhotoViewAttacher.this.mImageView.getParent();
            if (PhotoViewAttacher.this.mAllowParentInterceptOnEdge) {
                if (Math.abs(f) > Math.abs(f2)) {
                    if (PhotoViewAttacher.this.mHorizontalScrollEdge == 2 || ((PhotoViewAttacher.this.mHorizontalScrollEdge == 0 && f >= 1.0f) || (PhotoViewAttacher.this.mHorizontalScrollEdge == 1 && f <= -1.0f))) {
                        if (parent == null || PhotoViewAttacher.this.mDragHandled) {
                            return;
                        }
                        PhotoViewAttacher.this.requestDisallowInterceptTouchEvent(false);
                        return;
                    }
                } else if (PhotoViewAttacher.this.mVerticalScrollEdge == 2 || ((PhotoViewAttacher.this.mVerticalScrollEdge == 0 && f2 >= 1.0f) || (PhotoViewAttacher.this.mVerticalScrollEdge == 1 && f2 <= -1.0f))) {
                    if (parent == null || PhotoViewAttacher.this.mDragHandled) {
                        return;
                    }
                    PhotoViewAttacher.this.requestDisallowInterceptTouchEvent(false);
                    return;
                }
            } else if (parent != null) {
                PhotoViewAttacher.this.requestDisallowInterceptTouchEvent(true);
            }
            PhotoViewAttacher.this.mDragHandled = true;
        }

        public final float calculateDy(float f, float f2, float f3) {
            int i = (f3 > 0.0f ? 1 : (f3 == 0.0f ? 0 : -1));
            float f4 = 1.0f;
            float f5 = (f2 * 1.0f) / (i < 0 ? 1500.0f : 1300.0f);
            if (Math.abs(f5) <= 1.0f) {
                f4 = Math.abs(f5);
            }
            float min = Math.min((((1.5f * f4) * f4) * f4) - ((0.5f * f4) * f4), 0.9f) * f3;
            return i < 0 ? f3 + min : f3 - min;
        }

        @Override // com.github.chrisbanes.photoview.OnGestureListener
        public void onFling(float f, float f2, float f3, float f4) {
            if (PhotoViewAttacher.this.mZoomEnabled && !PhotoViewAttacher.this.interceptDrag() && PhotoViewAttacher.this.getScale() >= PhotoViewAttacher.this.mMinScale) {
                RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
                if (PhotoViewAttacher.this.mImageView == null || displayRect == null) {
                    return;
                }
                PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
                int imageViewHeight = photoViewAttacher.getImageViewHeight(photoViewAttacher.mImageView);
                PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
                int imageViewWidth = photoViewAttacher2.getImageViewWidth(photoViewAttacher2.mImageView);
                if (Math.floor(displayRect.left) > SearchStatUtils.POW || Math.floor(displayRect.top) > SearchStatUtils.POW) {
                    return;
                }
                if (imageViewHeight > 0 && Math.ceil(displayRect.bottom) < imageViewHeight) {
                    return;
                }
                if (imageViewWidth > 0 && Math.ceil(displayRect.right) < imageViewWidth) {
                    return;
                }
                PhotoViewAttacher photoViewAttacher3 = PhotoViewAttacher.this;
                photoViewAttacher3.mCurrentFlingRunnable = new FlingRunnable(photoViewAttacher3.mImageView.getContext());
                PhotoViewAttacher.this.mCurrentFlingRunnable.fling(imageViewWidth, imageViewHeight, (int) f3, (int) f4);
                PhotoViewAttacher.this.mImageView.post(PhotoViewAttacher.this.mCurrentFlingRunnable);
            }
        }

        @Override // com.github.chrisbanes.photoview.OnGestureListener
        public void onScale(float f, float f2, float f3) {
            if (!PhotoViewAttacher.this.mZoomEnabled) {
                return;
            }
            PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
            photoViewAttacher.mHasOperated = photoViewAttacher.mHasScale = true;
            PhotoViewAttacher.this.mIsDragDownOut = false;
            float scale = PhotoViewAttacher.this.getScale();
            if (scale >= PhotoViewAttacher.this.getMaxPointsScalingScale() && f >= 1.0f) {
                return;
            }
            PhotoViewAttacher.this.mLastScaleFocusX = f2;
            PhotoViewAttacher.this.mLastScaleFocusY = f3;
            PhotoViewAttacher.this.postScale(f, f, f2, f3);
            PhotoViewAttacher.this.checkAndDisplayMatrix();
            float scale2 = PhotoViewAttacher.this.getScale();
            PhotoViewAttacher.this.updateBackgroundAlpha(scale2);
            if (scale < PhotoViewAttacher.this.getMaxPointsScalingScale() && scale2 >= PhotoViewAttacher.this.getMaxPointsScalingScale()) {
                LinearMotorHelper.performHapticFeedback(PhotoViewAttacher.this.mImageView, LinearMotorHelper.HAPTIC_MESH_NORMAL);
            }
            PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
            photoViewAttacher2.dispatchScaleStageChange(f, scale, scale2, photoViewAttacher2.isAnimRunning(1));
        }

        @Override // com.github.chrisbanes.photoview.OnGestureListener
        public void oScaleEnd() {
            PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
            photoViewAttacher.trackPhotoScaleChange(false, photoViewAttacher.getScale());
            if (PhotoViewAttacher.this.mScaleStateChangeListener != null) {
                PhotoViewAttacher.this.mScaleStateChangeListener.onScaleEnd();
            }
        }

        @Override // com.github.chrisbanes.photoview.OnGestureListener
        public void onScaleBegin() {
            if (PhotoViewAttacher.this.mScaleStateChangeListener != null) {
                PhotoViewAttacher.this.mScaleStateChangeListener.onScaleStart();
            }
        }
    };
    public GestureDetector.OnDoubleTapListener mDefaultOnDoubleTapListener = new GestureDetector.OnDoubleTapListener() { // from class: com.github.chrisbanes.photoview.PhotoViewAttacher.2
        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }

        {
            PhotoViewAttacher.this = this;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (PhotoViewAttacher.this.mOnClickListener != null) {
                PhotoViewAttacher.this.mOnClickListener.onClick(PhotoViewAttacher.this.mImageView);
            }
            RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (PhotoViewAttacher.this.mViewTapListener != null) {
                PhotoViewAttacher.this.mViewTapListener.onViewTap(PhotoViewAttacher.this.mImageView, x, y);
            }
            if (displayRect != null) {
                if (!displayRect.contains(x, y)) {
                    if (PhotoViewAttacher.this.mOutsidePhotoTapListener == null) {
                        return false;
                    }
                    PhotoViewAttacher.this.mOutsidePhotoTapListener.onOutsidePhotoTap(PhotoViewAttacher.this.mImageView);
                    return false;
                }
                float width = (x - displayRect.left) / displayRect.width();
                float height = (y - displayRect.top) / displayRect.height();
                if (PhotoViewAttacher.this.mPhotoTapListener == null) {
                    return true;
                }
                PhotoViewAttacher.this.mPhotoTapListener.onPhotoTap(PhotoViewAttacher.this.mImageView, width, height);
                return true;
            }
            return false;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (PhotoViewAttacher.this.mViewSingleTapListener != null) {
                return false;
            }
            try {
                PhotoViewAttacher.this.doDoubleTap(motionEvent.getX(), motionEvent.getY());
                return true;
            } catch (ArrayIndexOutOfBoundsException unused) {
                return true;
            }
        }
    };
    public final boolean mIsLowRamDevice = BaseBuildUtil.isLowRamDevice();

    public Bitmap getVisibleRectangleBitmap() {
        return null;
    }

    public static /* synthetic */ float access$1816(PhotoViewAttacher photoViewAttacher, float f) {
        float f2 = photoViewAttacher.mDragDownOutDx + f;
        photoViewAttacher.mDragDownOutDx = f2;
        return f2;
    }

    public static /* synthetic */ float access$1916(PhotoViewAttacher photoViewAttacher, float f) {
        float f2 = photoViewAttacher.mDragDownOutDy + f;
        photoViewAttacher.mDragDownOutDy = f2;
        return f2;
    }

    public static /* synthetic */ float access$816(PhotoViewAttacher photoViewAttacher, float f) {
        float f2 = photoViewAttacher.mDragDownOutY + f;
        photoViewAttacher.mDragDownOutY = f2;
        return f2;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public PhotoViewAttacher(ImageView imageView) {
        this.mImageView = imageView;
        imageView.setOnTouchListener(this);
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener(this);
        }
        setImageViewScaleTypeMatrix(imageView);
        if (imageView.isInEditMode()) {
            return;
        }
        setZoomable(true);
        this.mDragDownOutThreshold = imageView.getResources().getDisplayMetrics().density * 10.0f;
        this.mBaseRotation = 0.0f;
        this.mScaleDragDetector = new CustomGestureDetector(imageView.getContext(), this.mOnGestureListener);
        GestureDetector gestureDetector = new GestureDetector(imageView.getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.github.chrisbanes.photoview.PhotoViewAttacher.3
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            {
                PhotoViewAttacher.this = this;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                if (PhotoViewAttacher.this.mLongClickListener != null) {
                    PhotoViewAttacher.this.mLongClickListener.onLongClick(PhotoViewAttacher.this.mImageView);
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoViewAttacher.this.mSingleFlingListener == null || PhotoViewAttacher.this.getScale() > 1.0f || motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1) {
                    return false;
                }
                return PhotoViewAttacher.this.mSingleFlingListener.onFling(motionEvent, motionEvent2, f, f2);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (PhotoViewAttacher.this.mViewSingleTapListener != null) {
                    return PhotoViewAttacher.this.mViewSingleTapListener.onViewSingleTap(PhotoViewAttacher.this.mImageView, motionEvent.getX(), motionEvent.getY());
                }
                return false;
            }
        }, ThreadManager.getMainHandler());
        this.mGestureDetector = gestureDetector;
        gestureDetector.setOnDoubleTapListener(this.mDefaultOnDoubleTapListener);
    }

    public static void setImageViewScaleTypeMatrix(ImageView imageView) {
        if (imageView == null || (imageView instanceof PhotoView) || ImageView.ScaleType.MATRIX.equals(imageView.getScaleType())) {
            return;
        }
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
    }

    /* JADX WARN: Code restructure failed: missing block: B:163:0x0156, code lost:
        if (r11.mGestureDetector.onTouchEvent(r13) != false) goto L14;
     */
    @Override // android.view.View.OnTouchListener
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouch(android.view.View r12, android.view.MotionEvent r13) {
        /*
            Method dump skipped, instructions count: 375
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.chrisbanes.photoview.PhotoViewAttacher.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        ImageView imageView = this.mImageView;
        if (imageView == null || this.isSharedElementTransitionRunning) {
            return;
        }
        int top = imageView.getTop();
        int right = this.mImageView.getRight();
        int bottom = this.mImageView.getBottom();
        int left = this.mImageView.getLeft();
        if (top == this.mIvTop && bottom == this.mIvBottom && left == this.mIvLeft && right == this.mIvRight) {
            return;
        }
        updateBaseMatrix(this.mImageView.getDrawable(), trimRotation(getRotate()));
        resetMatrix();
        this.mIvTop = top;
        this.mIvRight = right;
        this.mIvBottom = bottom;
        this.mIvLeft = left;
    }

    public final void requestDisallowInterceptTouchEvent(boolean z) {
        ViewParent parent = getImageView().getParent();
        parent.requestDisallowInterceptTouchEvent(z);
        while (parent != null) {
            if (parent instanceof SpringBackLayout) {
                ((SpringBackLayout) parent).internalRequestDisallowInterceptTouchEvent(z);
            }
            parent = parent.getParent();
        }
    }

    public void animEnter(ItemViewInfo itemViewInfo, TransitionListener transitionListener) {
        if (itemViewInfo == null || getTransition() == null || this.mImageView == null) {
            if (transitionListener == null) {
                return;
            }
            transitionListener.onTransitEnd();
            return;
        }
        getTransition().stop();
        RectF displayRect = getDisplayRect();
        if (displayRect != null) {
            ItemViewInfo imageInfo = ItemViewInfo.getImageInfo((View) this.mImageView, 0);
            float max = Math.max(itemViewInfo.getWidth() / displayRect.width(), itemViewInfo.getHeight() / displayRect.height());
            float scale = getScale();
            float width = displayRect.width() * max;
            float height = displayRect.height() * max;
            int height2 = (int) (((itemViewInfo.getHeight() - height) / 2.0f) + itemViewInfo.getY());
            int x = (int) ((imageInfo.getX() + displayRect.left) - ((int) (((itemViewInfo.getWidth() - width) / 2.0f) + itemViewInfo.getX())));
            float f = displayRect.top;
            int y = (int) ((imageInfo.getY() + f) - height2);
            postScale(max, max, displayRect.left, f);
            postTranslate(-x, -y);
            setImageViewMatrix(getDrawMatrix());
            getTransition(false).scale(max, scale);
            getTransition(false).translate(0, 0, x, y);
            if (itemViewInfo.getWidth() < width || itemViewInfo.getHeight() < height) {
                float clamp = BaseMiscUtil.clamp(itemViewInfo.getWidth() / width, 0.0f, 1.0f);
                float clamp2 = BaseMiscUtil.clamp(itemViewInfo.getHeight() / height, 0.0f, 1.0f);
                getTransition(false).clip(clamp, clamp2, 1.0f - clamp, 1.0f - clamp2);
            }
            if (!BaseMiscUtil.floatEquals(itemViewInfo.getCoverWidthRatio(), 0.0f) || !BaseMiscUtil.floatEquals(itemViewInfo.getCoverHeightRatio(), 0.0f)) {
                getTransition(false).coverClip(itemViewInfo.getCoverWidthRatio(), itemViewInfo.getCoverHeightRatio(), -itemViewInfo.getCoverWidthRatio(), -itemViewInfo.getCoverHeightRatio());
            }
            getTransition(false).ensureAlpha(true);
            getTransition(false).alpha(this.mAlpha, 1.0f);
            if (itemViewInfo.getViewRadius() > 0.0f) {
                getTransition(false).clipPath(itemViewInfo.getViewRadius(), 0.0f);
            }
            getTransition(false).setTransitionListener(transitionListener);
            getTransition(false).start(false);
        } else if (isWaitingTransition()) {
        } else {
            CompatHandler mainHandler = ThreadManager.getMainHandler();
            WaitingTransition waitingTransition = new WaitingTransition(itemViewInfo, transitionListener);
            this.mWaitingTransition = waitingTransition;
            mainHandler.postDelayed(waitingTransition, 200L);
        }
    }

    public void animExit(ItemViewInfo itemViewInfo, TransitionListener transitionListener) {
        if (itemViewInfo == null || !itemViewInfo.isLocationValid() || getTransition() == null || this.mImageView == null) {
            if (transitionListener == null) {
                return;
            }
            transitionListener.onTransitEnd();
            return;
        }
        getTransition().stop();
        RectF displayRect = getDisplayRect(getDrawMatrix());
        if (displayRect != null) {
            RectF rectF = new RectF(displayRect);
            ItemViewInfo imageInfo = ItemViewInfo.getImageInfo((View) this.mImageView, 0);
            RectF displayRect2 = getDisplayRect(this.mBaseMatrix);
            if (displayRect2 == null) {
                return;
            }
            float width = itemViewInfo.getWidth() / displayRect2.width();
            float height = itemViewInfo.getHeight() / displayRect2.height();
            if (width <= height) {
                width = height;
            }
            float scale = getScale();
            float width2 = displayRect2.width() * width;
            float height2 = displayRect2.height() * width;
            int height3 = (int) (((itemViewInfo.getHeight() - height2) / 2.0f) + itemViewInfo.getY());
            int x = (int) ((imageInfo.getX() + rectF.left) - ((int) (((itemViewInfo.getWidth() - width2) / 2.0f) + itemViewInfo.getX())));
            int y = (int) ((imageInfo.getY() + rectF.top) - height3);
            if (itemViewInfo.getWidth() < width2 || itemViewInfo.getHeight() < height2) {
                getTransition(true).clip(1.0f, 1.0f, (itemViewInfo.getWidth() / width2) - 1.0f, BaseMiscUtil.clamp(itemViewInfo.getHeight() / height2, 0.0f, 1.0f) - 1.0f);
            }
            if (!BaseMiscUtil.floatEquals(itemViewInfo.getCoverWidthRatio(), 0.0f) || !BaseMiscUtil.floatEquals(itemViewInfo.getCoverHeightRatio(), 0.0f)) {
                getTransition(true).coverClip(0.0f, 0.0f, itemViewInfo.getCoverWidthRatio(), itemViewInfo.getCoverHeightRatio());
            }
            getTransition(true).scale(scale, width);
            getTransition(true).translate(0, 0, -x, -y);
        }
        getTransition(true).setTransitionListener(transitionListener);
        getTransition(true).ensureAlpha(false);
        getTransition(true).alpha(this.mAlpha, 0.0f);
        if (itemViewInfo.getViewRadius() > 0.0f) {
            getTransition(true).clipPath(0.0f, itemViewInfo.getViewRadius());
        }
        getTransition(true).start(true);
    }

    public void update(boolean z) {
        ImageView imageView = this.mImageView;
        if (imageView != null) {
            if (this.mZoomEnabled) {
                setImageViewScaleTypeMatrix(imageView);
            }
            if (!isAnimRunning(8)) {
                updateBaseMatrix(this.mImageView.getDrawable(), z ? trimRotation(getRotate()) : 0);
            }
            if (!this.mZoomEnabled || !z) {
                resetMatrix();
            } else {
                setImageViewMatrix(getDrawMatrix());
            }
        }
    }

    public void detach() {
        ImageView imageView = this.mImageView;
        if (imageView == null) {
            return;
        }
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this);
        }
        this.mImageView.setOnTouchListener(null);
        this.mImageView.setTransitionName(null);
        this.mImageView = null;
    }

    public void release() {
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.setOnDoubleTapListener(null);
        }
        this.mMatrixChangeListeners.clear();
        this.mPhotoTapListener = null;
        this.mViewTapListener = null;
        this.mViewSingleTapListener = null;
        this.mLongClickListener = null;
        this.mScaleChangeListener = null;
        this.mScaleStateChangeListener = null;
        this.mExitListener = null;
        releaseTile();
    }

    public ImageView getImageView() {
        return this.mImageView;
    }

    public void requestAlignBounds() {
        tryAlignBounds();
    }

    public boolean translateBy(float f, float f2) {
        if (this.mZoomEnabled) {
            postTranslate(f, f2);
            checkAndDisplayMatrix();
            return true;
        }
        return false;
    }

    public boolean beforeDraw(Canvas canvas) {
        drawBackground(canvas);
        if (getTransition().isRunning()) {
            if (getTransition().getClipRect() != null) {
                canvas.clipRect(getTransition().getClipRect());
            }
            if (getTransition().getClipPath() != null) {
                canvas.clipPath(getTransition().getClipPath());
            }
            return true;
        }
        return !getTransition().isExited();
    }

    public void afterDraw(Canvas canvas) {
        if (needDrawTile()) {
            this.mTileView.draw(canvas, tryGetViewPaint());
        }
        drawStroke(canvas);
    }

    public void setupTile(TileBitProvider tileBitProvider, BitmapRecycleCallback bitmapRecycleCallback, TrimMemoryCallback trimMemoryCallback) {
        if (this.mTileView == null) {
            this.mTileView = new TileView(tileBitProvider, this.mImageView, bitmapRecycleCallback, trimMemoryCallback);
        }
        this.mTileView.setViewPort(new Rect(0, 0, getImageViewWidth(this.mImageView), getImageViewHeight(this.mImageView)));
        calculateScales();
        notifyTileViewInvalidate();
    }

    public void releaseTile() {
        TileView tileView = this.mTileView;
        if (tileView != null) {
            tileView.cleanup();
        }
        this.mTileView = null;
    }

    public void setScaleMinSpan(int i) {
        CustomGestureDetector customGestureDetector = this.mScaleDragDetector;
        if (customGestureDetector != null) {
            customGestureDetector.setScaleMinSpan(i);
        }
    }

    public void setBaseScale(float f) {
        this.mBaseScale = f;
    }

    public void setScale(float f, boolean z) {
        ImageView imageView = this.mImageView;
        if (imageView != null) {
            setScale(f, imageView.getRight() / 2.0f, this.mImageView.getBottom() / 2.0f, z);
        }
    }

    public void setScale(float f, float f2, float f3, boolean z) {
        if (this.mImageView != null) {
            if (f < this.mMinScale || f > getMaximumScale()) {
                LogManager.getLogger().i("PhotoViewAttacher", "Scale must be within the range of minScale and maxScale");
            } else if (z) {
                this.mImageView.post(new AnimatedZoomRunnable(getScale(), f, f2, f3));
            } else {
                this.mSuppMatrix.setScale(f, f, f2, f3);
                onScaleChanged(f, f, f2, f3);
                checkAndDisplayMatrix();
            }
        }
    }

    public boolean setDisplayMatrix(Matrix matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null");
        }
        ImageView imageView = this.mImageView;
        if (imageView == null || imageView.getDrawable() == null) {
            return false;
        }
        this.mSuppMatrix.set(matrix);
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
        return true;
    }

    public void setZoomable(boolean z) {
        this.mZoomEnabled = z;
        ImageView imageView = this.mImageView;
        if (imageView == null || imageView.getDrawable() == null) {
            return;
        }
        update(true);
    }

    public void setScaleDragEnable(boolean z) {
        this.mScaleDragEnabled = z;
    }

    public void setRotatable(boolean z) {
        this.mRotateEnabled = z;
        if (!z) {
            update(false);
        }
    }

    public void setBaseRotation(float f) {
        this.mBaseRotation = f % 360.0f;
        update(true);
        setRotationBy(this.mBaseRotation);
    }

    public void setRotationTo(float f) {
        setRotate(f % 360.0f, 0.0f, 0.0f);
        checkAndDisplayMatrix();
    }

    public void setRotationBy(float f) {
        postRotate(f % 360.0f, 0.0f, 0.0f);
        checkAndDisplayMatrix();
    }

    public void setSlippedRect(int i, int i2) {
        this.mSlippedRectTop = i;
        this.mSlippedRectBottom = i2;
    }

    public void setInterceptTouch(boolean z) {
        this.mIntercepted = z;
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.mAllowParentInterceptOnEdge = z;
    }

    public void setDraggableViewportInsets(Rect rect) {
        this.mOutOfBoundsRect.set(Math.max(0, rect.left), Math.max(0, rect.top), Math.max(0, rect.right), Math.max(0, rect.bottom));
    }

    public void setStroke(int i, int i2) {
        int alpha = Color.alpha(i);
        this.mDestStrokeAlpha = alpha;
        setStroke(i, i2, alpha);
    }

    public void setBackgroundAlpha(float f) {
        updateBackgroundAlpha(f);
        ImageView imageView = this.mImageView;
        if (imageView != null) {
            imageView.invalidate();
        }
    }

    public void setAlphaBackground(Drawable drawable) {
        this.mAlphaDrawable = drawable;
        ImageView imageView = this.mImageView;
        if (imageView != null) {
            imageView.invalidate();
        }
    }

    public void setHDState(int i, int i2, boolean z) {
        this.mOriginWidth = i;
        this.mOriginHeight = i2;
        this.mSupportHd = z;
    }

    public void setRegionDecodeEnable(boolean z) {
        if (this.mIsRegionDecodeEnable != z) {
            this.mIsRegionDecodeEnable = z;
            if (z) {
                notifyTileViewInvalidate();
                return;
            }
            ImageView imageView = this.mImageView;
            if (imageView == null) {
                return;
            }
            imageView.invalidate();
        }
    }

    public void setDisableDragDownOut(boolean z) {
        this.mDisableDragDownOut = z;
    }

    public void setUseFillWidthMode(boolean z) {
        this.mUseFillWidthMode = z;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener onOutsidePhotoTapListener) {
        this.mOutsidePhotoTapListener = onOutsidePhotoTapListener;
    }

    public void setOnViewDragListener(OnViewDragListener onViewDragListener) {
        this.mOnViewDragListener = onViewDragListener;
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        this.mSingleFlingListener = onSingleFlingListener;
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        ensureTapDetector();
        if (onDoubleTapListener != null) {
            this.mNewOnDoubleTapListener = onDoubleTapListener;
            this.mGestureDetector.setOnDoubleTapListener(onDoubleTapListener);
            return;
        }
        this.mGestureDetector.setOnDoubleTapListener(this.mDefaultOnDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.mScaleChangeListener = onScaleChangeListener;
    }

    public void setOnScaleStateChangeListener(OnScaleStateChangeListener onScaleStateChangeListener) {
        this.mScaleStateChangeListener = onScaleStateChangeListener;
    }

    public void setOnScaleStageChangedListener(OnScaleLevelChangedListener onScaleLevelChangedListener) {
        this.mScaleStageChangedListener = onScaleLevelChangedListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mLongClickListener = onLongClickListener;
    }

    public void addOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        if (onMatrixChangedListener != null) {
            this.mMatrixChangeListeners.add(onMatrixChangedListener);
        }
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.mExitListener = onExitListener;
    }

    public void setOnPhotoViewDragDownOutListener(OnPhotoViewDragDownOutListener onPhotoViewDragDownOutListener) {
        this.mOnPhotoViewDragDownOutListener = onPhotoViewDragDownOutListener;
    }

    public void setOnBackgroundAlphaChangedListener(OnBackgroundAlphaChangedListener onBackgroundAlphaChangedListener) {
        this.mBackgroundAlphaChangedListener = onBackgroundAlphaChangedListener;
    }

    public void setOnPhotoViewTransitionListener(OnPhotoViewTransitionListener onPhotoViewTransitionListener) {
        this.mPhotoViewTransitionListener = onPhotoViewTransitionListener;
    }

    public void removeOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        if (onMatrixChangedListener != null) {
            this.mMatrixChangeListeners.remove(onMatrixChangedListener);
        }
    }

    public void addOnRotateListener(OnRotateListener onRotateListener) {
        if (onRotateListener != null) {
            this.mOnRotateListeners.add(onRotateListener);
        }
    }

    public void removeOnRotateListener(OnRotateListener onRotateListener) {
        if (onRotateListener != null) {
            this.mOnRotateListeners.remove(onRotateListener);
        }
    }

    public void setOnHandleTouchEventListener(OnHandleTouchEventListener onHandleTouchEventListener) {
        this.mOnHandleTouchEventListener = onHandleTouchEventListener;
    }

    public OnViewTapListener getOnViewTapListener() {
        return this.mViewTapListener;
    }

    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.mViewTapListener = onViewTapListener;
    }

    public void setOnViewSingleTapListener(OnViewSingleTapListener onViewSingleTapListener) {
        this.mViewSingleTapListener = onViewSingleTapListener;
    }

    public OnPhotoTapListener getOnPhotoTapListener() {
        return this.mPhotoTapListener;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.mPhotoTapListener = onPhotoTapListener;
    }

    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (!Util.isSupportedScaleType(scaleType) || scaleType == this.mScaleType) {
            return;
        }
        this.mScaleType = scaleType;
        update(true);
    }

    public float getScale() {
        return MatrixUtil.getScale(this.mSuppMatrix);
    }

    public void setScale(float f) {
        setScale(f, false);
    }

    public float getPointsScaleEnlargeFactor() {
        return this.mPointsScaleEnlargeFactor;
    }

    public void setPointsScaleEnlargeFactor(float f) {
        this.mPointsScaleEnlargeFactor = f;
        calculateScales();
    }

    public ScaleLevel getScaleLevel() {
        float scale = getScale();
        if (scale < getMediumScale() - 0.001f) {
            return ScaleLevel.LOW;
        }
        if (scale >= getMediumScale() - 0.001f && scale < this.mHighScaleLevelLowerBound - 0.001f) {
            return ScaleLevel.MIDDLE;
        }
        return ScaleLevel.HIGH;
    }

    public RectF getDrawableSize() {
        Drawable drawable;
        ImageView imageView = this.mImageView;
        if (imageView != null && getImageViewHeight(imageView) > 0 && getImageViewWidth(this.mImageView) > 0 && (drawable = this.mImageView.getDrawable()) != null) {
            return new RectF(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        return new RectF(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public RectF getDisplayRect() {
        checkMatrixBounds();
        RectF displayRect = getDisplayRect(getDrawMatrix());
        if (displayRect != null) {
            return new RectF(displayRect);
        }
        return null;
    }

    public RectF getBaseDisplayRect() {
        RectF displayRect = getDisplayRect(this.mBaseMatrix);
        if (displayRect != null) {
            return new RectF(displayRect);
        }
        return null;
    }

    public boolean getAbsoluteRect(RectF rectF) {
        return getDrawMatrix().mapRect(rectF);
    }

    public Rect getViewPort() {
        return new Rect(0, 0, getImageViewWidth(this.mImageView), getImageViewHeight(this.mImageView));
    }

    public Matrix getBaseMatrix() {
        return new Matrix(this.mBaseMatrix);
    }

    public Matrix getSuppMatrix() {
        return new Matrix(this.mSuppMatrix);
    }

    public Matrix getDisplayMatrix() {
        return new Matrix(getDrawMatrix());
    }

    public float getMinimumScale() {
        return this.mMinScale;
    }

    public void setMinimumScale(float f) {
        Util.checkZoomLevels(f, this.mMidScale, this.mMaxScale);
        this.mMinScale = f;
        this.mExitScale = f * 0.8f;
    }

    public float getMediumScale() {
        return this.mMidScale;
    }

    public void setMediumScale(float f) {
        Util.checkZoomLevels(this.mMinScale, f, this.mMaxScale);
        this.mMidScale = f;
    }

    public float getMaximumScale() {
        float f = this.mMaxDoubleTapScale;
        return f > 0.0f ? f : this.mMaxScale;
    }

    public void setMaximumScale(float f) {
        Util.checkZoomLevels(this.mMinScale, this.mMidScale, f);
        this.mMaxScale = f;
    }

    public boolean canZoom() {
        return this.mZoomEnabled;
    }

    public int getZoomDuration() {
        return this.mZoomDuration;
    }

    public void setZoomDuration(int i) {
        if (i < 0) {
            i = 200;
        }
        this.mZoomDuration = i;
    }

    public Interpolator getZoomInterpolator() {
        return this.mZoomInterpolator;
    }

    public void setZoomInterpolator(Interpolator interpolator) {
        this.mZoomInterpolator = interpolator;
    }

    public float getZoomDurationLengthenFactor() {
        return this.mZoomDurationLengthenFactor;
    }

    public void setZoomDurationLengthenFactor(float f) {
        this.mZoomDurationLengthenFactor = f;
    }

    public boolean canRotatable() {
        return this.mRotateEnabled;
    }

    public float getBaseScale() {
        return this.mBaseScale;
    }

    public float getRotate() {
        return MatrixUtil.getRotate(this.mSuppMatrix) + MatrixUtil.getRotate(this.mBaseMatrix);
    }

    public float getSlipProgress() {
        return this.mProgress;
    }

    public void setSlipProgress(float f) {
        this.mProgress = f;
        if (f > 0.0f) {
            setIsSlipping(true);
        } else {
            setIsSlipping(false);
        }
        setStroke(this.mStrokeColor, this.mStrokeWidth, (int) (this.mProgress * this.mDestStrokeAlpha));
    }

    public int getSlippedRectTop() {
        return this.mSlippedRectTop;
    }

    public int getSlippedRectBottom() {
        return this.mSlippedRectBottom;
    }

    public boolean isGestureOperating() {
        RotateManager rotateManager;
        CustomGestureDetector customGestureDetector = this.mScaleDragDetector;
        return (customGestureDetector != null && (customGestureDetector.isDragging() || this.mScaleDragDetector.isScaling())) || ((rotateManager = this.mRotateDetector) != null && rotateManager.isRotating());
    }

    public void setDragInsideBoundsEnabled(boolean z) {
        if (z) {
            if (this.mPendingDisableDragInsideBounds) {
                this.mPendingDisableDragInsideBounds = false;
            }
            this.mIsDragInsideBoundsEnabled = true;
            return;
        }
        this.mPendingDisableDragInsideBounds = false;
        if (!this.mIsDragInsideBoundsEnabled) {
            return;
        }
        if (this.mIsTouchInProgress || isAniming()) {
            this.mPendingDisableDragInsideBounds = true;
            return;
        }
        this.mIsDragInsideBoundsEnabled = false;
        tryAlignBounds();
    }

    public final void doDoubleTap(float f, float f2) {
        if (Util.hasDrawable(this.mImageView) && !isAnimRunning(1)) {
            float scale = getScale();
            if (scale < getMediumScale() - 0.001f) {
                setScale(getMediumScale(), f, f2, true);
                if (this.mScaleStageChangedListener != null && getMediumScale() < getMaximumScale()) {
                    this.mScaleStageChangedListener.onMidScaleLevel(true, true);
                }
            } else if (scale >= getMediumScale() - 0.001f && scale < getHighScaleLevelLowerBound() - 0.001f) {
                setScale(getMaximumScale(), f, f2, true);
                OnScaleLevelChangedListener onScaleLevelChangedListener = this.mScaleStageChangedListener;
                if (onScaleLevelChangedListener != null) {
                    onScaleLevelChangedListener.onHighScaleLevel(true, true);
                }
            } else {
                setScale(getMinimumScale(), f, f2, true);
            }
            trackPhotoScaleChange(true, getScale());
        }
    }

    public final void setIsSlipping(boolean z) {
        this.mIsSlipping = z;
    }

    public final RectF getSlippedModeRect(int i, int i2) {
        int i3 = i2 - i;
        ImageView imageView = this.mImageView;
        if (imageView != null && getImageViewHeight(imageView) > 0 && getImageViewWidth(this.mImageView) > 0) {
            int displayWidth = DisplayKt.getDisplayWidth(this.mImageView);
            Drawable drawable = this.mImageView.getDrawable();
            if (drawable == null) {
                return null;
            }
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (intrinsicWidth != 0 && intrinsicHeight != 0) {
                double d = 1.0d;
                double d2 = intrinsicHeight;
                double d3 = (i3 * 1.0d) / d2;
                double d4 = intrinsicWidth * d3;
                int i4 = (int) d4;
                if (i4 < 450) {
                    d = 450.0d / i4;
                }
                int i5 = (int) (d4 * d);
                int i6 = (i2 + i) / 2;
                int i7 = ((int) ((d2 * d3) * d)) / 2;
                return new RectF((displayWidth - i5) / 2, i6 + i7, (displayWidth + i5) / 2, i6 - i7);
            }
        }
        return null;
    }

    public final void ensureStrokePaint() {
        if (this.mStrokePaint == null) {
            Paint paint = new Paint();
            this.mStrokePaint = paint;
            paint.setAntiAlias(false);
            this.mStrokePaint.setStyle(Paint.Style.STROKE);
        }
    }

    public final void setStroke(int i, int i2, int i3) {
        if (this.mStrokeColor == i && this.mStrokeWidth == i2 && this.mStrokeAlpha == i3) {
            return;
        }
        this.mStrokeColor = i;
        this.mStrokeWidth = i2;
        this.mStrokeAlpha = i3;
        if (i2 > 0) {
            ensureStrokePaint();
            this.mStrokePaint.setColor(this.mStrokeColor);
            this.mStrokePaint.setStrokeWidth(this.mStrokeWidth);
            int i4 = this.mStrokeAlpha;
            if (i4 >= 0 && i4 <= 255) {
                this.mStrokePaint.setAlpha(i4);
            }
        }
        ImageView imageView = this.mImageView;
        if (imageView == null) {
            return;
        }
        imageView.invalidate();
    }

    public final void appendAnim(int i) {
        this.mAnim = i | this.mAnim;
    }

    public final void clearAnim(int i) {
        this.mAnim = (~i) & this.mAnim;
        if (!isAniming()) {
            consumePendingDisableDisplayOutOfBounds();
        }
    }

    public final boolean isAnimRunning(int i) {
        return (this.mAnim & i) == i;
    }

    public final float getMaxPointsScale() {
        float f = this.mMaxPointsScale;
        return f > 0.0f ? f : getMaximumScale();
    }

    public final float getMaxPointsScalingScale() {
        return (3.0f / getPointsScaleEnlargeFactor()) * getMaxPointsScale();
    }

    public final float getHighScaleLevelLowerBound() {
        float f = this.mHighScaleLevelLowerBound;
        return f > 0.0f ? f : getMaximumScale();
    }

    public final boolean interceptDrag() {
        RotateManager rotateManager;
        CustomGestureDetector customGestureDetector = this.mScaleDragDetector;
        return (customGestureDetector != null && customGestureDetector.isScaling() && !this.mScaleDragEnabled) || ((rotateManager = this.mRotateDetector) != null && rotateManager.isRotating());
    }

    public final boolean interceptTouch() {
        RotateManager rotateManager;
        return getTransition().isRunning() || ((rotateManager = this.mRotateDetector) != null && rotateManager.isAutoRotating());
    }

    public final boolean interceptMotionEnd() {
        RotateManager rotateManager = this.mRotateDetector;
        return rotateManager != null && rotateManager.isRotating();
    }

    public final boolean interceptDrawTiles() {
        RotateManager rotateManager;
        return !this.mIsRegionDecodeEnable || getTransition().isRunning() || isViewAnimationRunning() || ((rotateManager = this.mRotateDetector) != null && rotateManager.isRotating());
    }

    public final boolean isViewAnimationRunning() {
        Animation animation;
        ImageView imageView = this.mImageView;
        return imageView != null && (animation = imageView.getAnimation()) != null && animation.hasStarted() && !animation.hasEnded();
    }

    public final boolean interceptCheckBounds() {
        RotateManager rotateManager;
        Transition transition = getTransition();
        return this.mIsDragDownOut || (transition != null && transition.isRunning()) || ((rotateManager = this.mRotateDetector) != null && rotateManager.isRotating());
    }

    public final void ensureScaleDragDetector() {
        if (this.mScaleDragDetector != null || this.mImageView == null) {
            return;
        }
        this.mScaleDragDetector = new CustomGestureDetector(this.mImageView.getContext(), this.mOnGestureListener);
    }

    public final void ensureTapDetector() {
        if (this.mGestureDetector != null || this.mImageView == null) {
            return;
        }
        GestureDetector gestureDetector = new GestureDetector(this.mImageView.getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.github.chrisbanes.photoview.PhotoViewAttacher.4
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            {
                PhotoViewAttacher.this = this;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                if (PhotoViewAttacher.this.mLongClickListener != null) {
                    PhotoViewAttacher.this.mLongClickListener.onLongClick(PhotoViewAttacher.this.mImageView);
                }
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (PhotoViewAttacher.this.mSingleFlingListener == null || PhotoViewAttacher.this.getScale() > 1.0f || motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1) {
                    return false;
                }
                return PhotoViewAttacher.this.mSingleFlingListener.onFling(motionEvent, motionEvent2, f, f2);
            }
        }, ThreadManager.getMainHandler());
        this.mGestureDetector = gestureDetector;
        gestureDetector.setOnDoubleTapListener(this.mDefaultOnDoubleTapListener);
    }

    public final void ensureRotateDetector() {
        if (this.mRotateDetector == null) {
            this.mRotateDetector = new RotateManager();
        }
    }

    public final void tryAlignBounds() {
        checkAndDisplayMatrix(true, true);
    }

    public final void consumePendingDisableDisplayOutOfBounds() {
        if (!this.mPendingDisableDragInsideBounds || isAniming() || this.mIsTouchInProgress) {
            return;
        }
        this.mPendingDisableDragInsideBounds = false;
        this.mIsDragInsideBoundsEnabled = false;
        tryAlignBounds();
    }

    public final void resetDragDownOutState() {
        this.mIsDragDownOut = false;
        this.mDragDownOutY = 0.0f;
    }

    public final void dispatchScaleStageChange(float f, float f2, float f3, boolean z) {
        OnScaleLevelChangedListener onScaleLevelChangedListener = this.mScaleStageChangedListener;
        if (onScaleLevelChangedListener != null) {
            if (f < 1.0f) {
                float f4 = this.mHighScaleLevelLowerBound;
                if (f3 < f4 && f2 >= f4) {
                    onScaleLevelChangedListener.onHighScaleLevel(false, z);
                    return;
                }
                float f5 = this.mMidScale;
                if (f3 >= f5 || f2 < f5) {
                    return;
                }
                onScaleLevelChangedListener.onMidScaleLevel(false, z);
                return;
            }
            float f6 = this.mHighScaleLevelLowerBound;
            if (f2 >= f6 || f3 < f6) {
                return;
            }
            onScaleLevelChangedListener.onHighScaleLevel(true, z);
        }
    }

    public final void trackPhotoScaleChange(boolean z, float f) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.11.4.1.14414");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "double_click" : "dual_finger");
        hashMap.put("value", String.valueOf(f));
        TrackController.trackDualFinger(hashMap);
    }

    public final void dispatchScaleChanged(float f, float f2, float f3, float f4, float f5) {
        OnScaleChangeListener onScaleChangeListener = this.mScaleChangeListener;
        if (onScaleChangeListener != null) {
            onScaleChangeListener.onScaleChange(f, f2, f3, f4, f5);
        }
    }

    public final Matrix getDrawMatrix() {
        RectF baseDisplayRect;
        RectF slippedModeRect;
        this.mDrawMatrix.set(this.mBaseMatrix);
        if (this.mIsSlipping && (baseDisplayRect = getBaseDisplayRect()) != null && (slippedModeRect = getSlippedModeRect(this.mSlippedRectTop, this.mSlippedRectBottom)) != null) {
            Matrix matrix = this.mSuppMatrix;
            float f = slippedModeRect.right;
            float f2 = slippedModeRect.left;
            float f3 = baseDisplayRect.right;
            float f4 = baseDisplayRect.left;
            float f5 = this.mProgress;
            matrix.setScale(((((f - f2) / (f3 - f4)) - 1.0f) * f5) + 1.0f, ((((f - f2) / (f3 - f4)) - 1.0f) * f5) + 1.0f, this.mImageView.getRight() / 2.0f, this.mImageView.getBottom() / 2.0f);
        }
        this.mDrawMatrix.postConcat(this.mSuppMatrix);
        return this.mDrawMatrix;
    }

    public final void cancelFling() {
        FlingRunnable flingRunnable = this.mCurrentFlingRunnable;
        if (flingRunnable != null) {
            flingRunnable.cancelFling();
            this.mCurrentFlingRunnable = null;
        }
    }

    public final void checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(getDrawMatrix());
        }
    }

    public final void checkAndDisplayMatrix(boolean z, boolean z2) {
        PointF checkMatrixBounds = checkMatrixBounds(z);
        if (checkMatrixBounds != null) {
            float f = checkMatrixBounds.x;
            if (f == 0.0f && checkMatrixBounds.y == 0.0f) {
                return;
            }
            if (z2) {
                if (this.mImageView == null) {
                    return;
                }
                float[] translate = MatrixUtil.getTranslate(this.mSuppMatrix);
                ImageView imageView = this.mImageView;
                imageView.post(new AnimatedTranslateRunnable(imageView.getContext()).translate(Math.round(translate[0]), Math.round(translate[1]), Math.round(checkMatrixBounds.x), Math.round(checkMatrixBounds.y)));
                return;
            }
            postTranslate(f, checkMatrixBounds.y);
            setImageViewMatrix(getDrawMatrix());
        }
    }

    public final void checkImageViewScaleType() {
        ImageView imageView = this.mImageView;
        if (imageView == null || (imageView instanceof PhotoView) || ImageView.ScaleType.MATRIX.equals(imageView.getScaleType())) {
            return;
        }
        throw new IllegalStateException("The ImageView's ScaleType has been changed since attaching a PhotoViewAttacher");
    }

    public final boolean checkMatrixBounds() {
        PointF checkMatrixBounds = checkMatrixBounds(false);
        if (checkMatrixBounds != null) {
            postTranslate(checkMatrixBounds.x, checkMatrixBounds.y);
            return true;
        }
        return false;
    }

    public final PointF checkMatrixBounds(boolean z) {
        RectF displayRect;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        if (interceptCheckBounds()) {
            return new PointF();
        }
        if (this.mImageView == null || (displayRect = getDisplayRect(getDrawMatrix())) == null) {
            return null;
        }
        float height = displayRect.height();
        float width = displayRect.width();
        int imageViewHeight = getImageViewHeight(this.mImageView);
        double d = imageViewHeight;
        float f6 = 0.0f;
        if (Math.floor(height) <= d) {
            int i = AnonymousClass5.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()];
            if (i != 1) {
                if (i == 2) {
                    f4 = imageViewHeight - height;
                    f5 = displayRect.top;
                } else {
                    f4 = (imageViewHeight - height) / 2.0f;
                    f5 = displayRect.top;
                }
                f = f4 - f5;
            } else {
                f = -displayRect.top;
            }
            this.mVerticalScrollEdge = 2;
        } else if (Math.floor(displayRect.top) >= SearchStatUtils.POW) {
            if (this.mIsDragInsideBoundsEnabled && !z) {
                double floor = Math.floor(displayRect.top);
                int i2 = this.mOutOfBoundsRect.top;
                if (floor >= i2) {
                    this.mVerticalScrollEdge = 0;
                    f = (-displayRect.top) + i2;
                } else {
                    this.mVerticalScrollEdge = -1;
                    f = 0.0f;
                }
            } else {
                this.mVerticalScrollEdge = 0;
                f = -displayRect.top;
            }
        } else {
            if (Math.floor(displayRect.bottom) <= d) {
                if (this.mIsDragInsideBoundsEnabled && !z) {
                    double floor2 = Math.floor(displayRect.bottom);
                    int i3 = this.mOutOfBoundsRect.bottom;
                    if (floor2 <= imageViewHeight - i3) {
                        this.mVerticalScrollEdge = 1;
                        f = (imageViewHeight - i3) - displayRect.bottom;
                    } else {
                        this.mVerticalScrollEdge = -1;
                    }
                } else {
                    f = imageViewHeight - displayRect.bottom;
                    this.mVerticalScrollEdge = 1;
                }
            } else {
                this.mVerticalScrollEdge = -1;
            }
            f = 0.0f;
        }
        int imageViewWidth = getImageViewWidth(this.mImageView);
        float f7 = imageViewWidth;
        if (width <= f7) {
            int i4 = AnonymousClass5.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()];
            if (i4 != 1) {
                if (i4 == 2) {
                    f2 = f7 - width;
                    f3 = displayRect.left;
                } else {
                    f2 = (f7 - width) / 2.0f;
                    f3 = displayRect.left;
                }
                f6 = f2 - f3;
            } else {
                f6 = -displayRect.left;
            }
            this.mHorizontalScrollEdge = 2;
        } else if (Math.floor(displayRect.left) >= SearchStatUtils.POW) {
            if (this.mIsDragInsideBoundsEnabled && !z) {
                double floor3 = Math.floor(displayRect.left);
                int i5 = this.mOutOfBoundsRect.left;
                if (floor3 >= i5) {
                    this.mHorizontalScrollEdge = 0;
                    f6 = (-displayRect.left) + i5;
                } else {
                    this.mHorizontalScrollEdge = -1;
                }
            } else {
                this.mHorizontalScrollEdge = 0;
                f6 = -displayRect.left;
            }
        } else if (Math.floor(displayRect.right) <= imageViewWidth) {
            if (this.mIsDragInsideBoundsEnabled && !z) {
                double floor4 = Math.floor(displayRect.right);
                int i6 = this.mOutOfBoundsRect.right;
                if (floor4 <= imageViewWidth - i6) {
                    this.mHorizontalScrollEdge = 1;
                    f6 = (imageViewWidth - i6) - displayRect.right;
                } else {
                    this.mHorizontalScrollEdge = -1;
                }
            } else {
                f6 = f7 - displayRect.right;
                this.mHorizontalScrollEdge = 1;
            }
        } else {
            this.mHorizontalScrollEdge = -1;
        }
        return new PointF(f6, f);
    }

    /* renamed from: com.github.chrisbanes.photoview.PhotoViewAttacher$5 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            int[] iArr = new int[ImageView.ScaleType.values().length];
            $SwitchMap$android$widget$ImageView$ScaleType = iArr;
            try {
                iArr[ImageView.ScaleType.FIT_START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_XY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public RectF getDisplayRect(Matrix matrix) {
        Drawable drawable;
        ImageView imageView = this.mImageView;
        if (imageView == null || getImageViewHeight(imageView) <= 0 || getImageViewWidth(this.mImageView) <= 0 || (drawable = this.mImageView.getDrawable()) == null) {
            return null;
        }
        this.mDisplayRect.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        matrix.mapRect(this.mDisplayRect);
        return this.mDisplayRect;
    }

    public final float getValue(Matrix matrix, int i) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[i];
    }

    public void resetMatrix() {
        RectF displayRect;
        float scale = getScale();
        if (!getTransition().isRunning()) {
            this.mSuppMatrix.reset();
        }
        setRotationBy(this.mBaseRotation);
        setImageViewMatrix(getDrawMatrix());
        OnScaleLevelChangedListener onScaleLevelChangedListener = this.mScaleStageChangedListener;
        if (onScaleLevelChangedListener != null) {
            if (scale >= this.mMaxDoubleTapScale) {
                onScaleLevelChangedListener.onHighScaleLevel(false, false);
            } else if (scale >= this.mMidScale) {
                onScaleLevelChangedListener.onMidScaleLevel(false, false);
            }
        }
        if (!getTransition().isRunning() && (displayRect = getDisplayRect(this.mBaseMatrix)) != null) {
            dispatchScaleChanged(1.0f, 1.0f, displayRect.centerX(), displayRect.centerY(), getScale());
        }
        checkMatrixBounds();
    }

    public final void setImageViewMatrix(Matrix matrix) {
        RectF displayRect;
        if (this.mImageView != null) {
            checkImageViewScaleType();
            this.mImageView.setImageMatrix(matrix);
            notifyTileViewInvalidate();
            Set<OnMatrixChangedListener> set = this.mMatrixChangeListeners;
            if (set == null || set.size() <= 0 || (displayRect = getDisplayRect(matrix)) == null) {
                return;
            }
            RectF rectF = new RectF(displayRect);
            for (OnMatrixChangedListener onMatrixChangedListener : this.mMatrixChangeListeners) {
                onMatrixChangedListener.onMatrixChanged(rectF);
            }
        }
    }

    public final void updateBaseMatrix(Drawable drawable, int i) {
        RectF calculateBaseRect = calculateBaseRect(drawable, i);
        if (calculateBaseRect == null) {
            return;
        }
        this.mBaseMatrix.reset();
        this.mBaseMatrix.postRotate(0 - i, calculateBaseRect.centerX(), calculateBaseRect.centerY());
        RectF displayRect = getDisplayRect(this.mBaseMatrix);
        if (displayRect != null) {
            this.mBaseMatrix.postTranslate(calculateBaseRect.centerX() - displayRect.centerX(), calculateBaseRect.centerY() - displayRect.centerY());
            this.mBaseMatrix.postScale(calculateBaseRect.width() / displayRect.width(), calculateBaseRect.height() / displayRect.height(), calculateBaseRect.centerX(), calculateBaseRect.centerY());
        }
        onBaseMatrixChanged();
    }

    public final void onBaseMatrixChanged() {
        animEnterAfterDrawableBound();
        calculateScales();
        TileView tileView = this.mTileView;
        if (tileView != null) {
            tileView.setViewPort(new Rect(0, 0, getImageViewWidth(this.mImageView), getImageViewHeight(this.mImageView)));
        }
    }

    public final int getDrawableWidth(int i) {
        Drawable drawable;
        ImageView imageView = this.mImageView;
        if (imageView == null || (drawable = imageView.getDrawable()) == null) {
            return 0;
        }
        return (((i > 0 ? i + 45 : i + (-45)) / 90) & 1) == 0 ? drawable.getIntrinsicWidth() : drawable.getIntrinsicHeight();
    }

    public final int getDrawableHeight(int i) {
        Drawable drawable;
        ImageView imageView = this.mImageView;
        if (imageView == null || (drawable = imageView.getDrawable()) == null) {
            return 0;
        }
        return (((i > 0 ? i + 45 : i + (-45)) / 90) & 1) == 0 ? drawable.getIntrinsicHeight() : drawable.getIntrinsicWidth();
    }

    public final RectF calculateBaseRect(Drawable drawable, int i) {
        ImageView imageView = this.mImageView;
        if (imageView != null && drawable != null) {
            float imageViewWidth = getImageViewWidth(imageView);
            float imageViewHeight = getImageViewHeight(this.mImageView);
            int drawableWidth = getDrawableWidth(i);
            int drawableHeight = getDrawableHeight(i);
            if (imageViewWidth != 0.0f && imageViewHeight != 0.0f && drawableWidth != 0 && drawableHeight != 0) {
                Matrix matrix = new Matrix();
                float f = drawableWidth;
                float f2 = imageViewWidth / f;
                float f3 = drawableHeight;
                float f4 = imageViewHeight / f3;
                if (this.mUseFillWidthMode) {
                    matrix.postScale(f2, f2);
                } else {
                    ImageView.ScaleType scaleType = this.mScaleType;
                    if (scaleType == ImageView.ScaleType.CENTER) {
                        matrix.postTranslate((imageViewWidth - f) / 2.0f, (imageViewHeight - f3) / 2.0f);
                    } else if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                        float max = Math.max(f2, f4);
                        matrix.postScale(max, max);
                        matrix.postTranslate((imageViewWidth - (f * max)) / 2.0f, (imageViewHeight - (max * f3)) / 2.0f);
                    } else if (scaleType == ImageView.ScaleType.CENTER_INSIDE) {
                        float min = Math.min(1.0f, Math.min(f2, f4));
                        matrix.postScale(min, min);
                        matrix.postTranslate((imageViewWidth - (f * min)) / 2.0f, (imageViewHeight - (min * f3)) / 2.0f);
                    } else {
                        RectF rectF = new RectF(0.0f, 0.0f, f, f3);
                        RectF rectF2 = new RectF(0.0f, 0.0f, imageViewWidth, imageViewHeight);
                        int i2 = AnonymousClass5.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()];
                        if (i2 == 1) {
                            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.START);
                        } else if (i2 == 2) {
                            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.END);
                        } else if (i2 == 3) {
                            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
                        } else if (i2 == 4) {
                            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.FILL);
                        }
                    }
                }
                float f5 = this.mBaseScale;
                matrix.postScale(f5, f5, imageViewWidth / 2.0f, imageViewHeight / 2.0f);
                RectF rectF3 = new RectF();
                rectF3.set(0.0f, 0.0f, f, f3);
                matrix.mapRect(rectF3);
                return rectF3;
            }
        }
        return null;
    }

    public final int getCorrectWidth(int i, int i2) {
        RectF displayRect = getDisplayRect(this.mBaseMatrix);
        if (displayRect != null) {
            return displayRect.width() > displayRect.height() ? Math.max(i, i2) : Math.min(i, i2);
        }
        return 0;
    }

    public final int getCorrectHeight(int i, int i2) {
        RectF displayRect = getDisplayRect(this.mBaseMatrix);
        if (displayRect != null) {
            return displayRect.height() > displayRect.width() ? Math.max(i, i2) : Math.min(i, i2);
        }
        return 0;
    }

    public final void calculateScales() {
        ImageView imageView = this.mImageView;
        if (imageView == null) {
            return;
        }
        float imageViewWidth = getImageViewWidth(imageView);
        float imageViewHeight = getImageViewHeight(this.mImageView);
        int rotate = (int) getRotate();
        int drawableWidth = getDrawableWidth(rotate);
        int drawableHeight = getDrawableHeight(rotate);
        if (imageViewWidth == 0.0f || imageViewHeight == 0.0f || drawableWidth == 0 || drawableHeight == 0) {
            return;
        }
        this.mMaxDoubleTapScale = 0.0f;
        this.mMaxPointsScale = 0.0f;
        RectF displayRect = getDisplayRect(this.mBaseMatrix);
        if (displayRect == null) {
            return;
        }
        float width = displayRect.width();
        float maximumScale = (getMaximumScale() * width) / imageViewWidth;
        float height = (displayRect.height() * getMaximumScale()) / imageViewHeight;
        int enlargeMode = getEnlargeMode(width, imageViewWidth);
        if (enlargeMode == 0) {
            if (maximumScale < height) {
                this.mMidScale = ((double) maximumScale) < 1.0d ? imageViewWidth / displayRect.width() : getMaximumScale();
                this.mMaxDoubleTapScale = getCorrectWidth(this.mOriginWidth, this.mOriginHeight) / displayRect.width();
            } else {
                this.mMidScale = ((double) height) < 1.0d ? imageViewHeight / displayRect.height() : getMaximumScale();
                this.mMaxDoubleTapScale = getCorrectHeight(this.mOriginWidth, this.mOriginHeight) / displayRect.height();
            }
            if (this.mMidScale > this.mMaxDoubleTapScale) {
                this.mMaxDoubleTapScale = this.mMaxScale;
            }
        } else if (enlargeMode == 1) {
            if (maximumScale < 1.0d) {
                this.mMaxDoubleTapScale = imageViewWidth / displayRect.width();
            } else {
                this.mMaxDoubleTapScale = getMaximumScale();
            }
            this.mMidScale = this.mMaxDoubleTapScale;
        } else if (enlargeMode == 2) {
            if (height < 1.0d) {
                this.mMaxDoubleTapScale = imageViewHeight / displayRect.height();
            } else {
                this.mMaxDoubleTapScale = getMaximumScale();
            }
            this.mMidScale = this.mMaxDoubleTapScale;
        }
        float f = this.mMaxDoubleTapScale;
        float f2 = this.mMidScale;
        if (f > f2 * 2.0f) {
            this.mHighScaleLevelLowerBound = f - (((f - f2) * 2.0f) / 3.0f);
        } else {
            this.mHighScaleLevelLowerBound = f;
        }
        this.mMaxPointsScale = this.mPointsScaleEnlargeFactor * f;
    }

    public final int getEnlargeMode(float f, float f2) {
        if (this.mSupportHd) {
            return 0;
        }
        return Math.round(f) + 10 < Math.round(f2) ? 1 : 2;
    }

    public final int getImageViewWidth(ImageView imageView) {
        if (imageView == null) {
            return 0;
        }
        return (imageView.getWidth() - imageView.getPaddingLeft()) - imageView.getPaddingRight();
    }

    public final int getImageViewHeight(ImageView imageView) {
        if (imageView == null) {
            return 0;
        }
        return (imageView.getHeight() - imageView.getPaddingTop()) - imageView.getPaddingBottom();
    }

    public final void drawBackground(Canvas canvas) {
        Drawable drawable;
        if (this.mImageView == null || (drawable = this.mAlphaDrawable) == null) {
            return;
        }
        drawable.setAlpha((int) (this.mAlpha * 255.0f));
        this.mAlphaDrawable.setBounds(0, 0, getImageViewWidth(this.mImageView), getImageViewHeight(this.mImageView));
        this.mAlphaDrawable.draw(canvas);
    }

    public final Paint tryGetViewPaint() {
        if (Util.hasDrawable(this.mImageView)) {
            Drawable drawable = this.mImageView.getDrawable();
            if (!(drawable instanceof BitmapDrawable)) {
                return null;
            }
            return ((BitmapDrawable) drawable).getPaint();
        }
        return null;
    }

    public final void drawStroke(Canvas canvas) {
        RectF displayRect;
        if (this.mStrokeWidth <= 0 || this.mStrokePaint == null || (displayRect = getDisplayRect()) == null) {
            return;
        }
        if (this.mIsSlipping) {
            displayRect.left = Math.max(0.0f, displayRect.left);
            displayRect.top = Math.max(displayRect.top, 0.0f);
            displayRect.right = Math.min(displayRect.right, getImageViewWidth(this.mImageView));
            displayRect.bottom = Math.min(displayRect.bottom, getImageViewHeight(this.mImageView));
        }
        int i = this.mStrokeWidth;
        displayRect.inset(i, i);
        canvas.drawRect(displayRect, this.mStrokePaint);
    }

    public final Transition getTransition() {
        return getTransition(this.mIsLastTimeExiting);
    }

    public final Transition getTransition(boolean z) {
        ImageView imageView;
        if ((this.mTransition == null || this.mIsLastTimeExiting != z) && (imageView = this.mImageView) != null) {
            this.mTransition = new Transition(imageView.getContext(), z);
            this.mIsLastTimeExiting = z;
        }
        return this.mTransition;
    }

    public final void removeWaitingTransition() {
        if (this.mWaitingTransition != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mWaitingTransition);
            this.mWaitingTransition = null;
        }
    }

    public final boolean isWaitingTransition() {
        return this.mWaitingTransition != null && ThreadManager.getMainHandler().hasCallbacksCompat(this.mWaitingTransition);
    }

    public final void animEnterAfterDrawableBound() {
        if (isWaitingTransition()) {
            WaitingTransition waitingTransition = this.mWaitingTransition;
            animEnter(waitingTransition.mEnterInfo, waitingTransition.mTransitionListener);
            removeWaitingTransition();
        }
    }

    public final boolean isAniming() {
        return this.mAnim != 0;
    }

    public final boolean needDrawTile() {
        ImageView imageView;
        if (!interceptDrawTiles() && this.mTileView != null && (imageView = this.mImageView) != null && imageView.getDrawable() != null) {
            Drawable drawable = this.mImageView.getDrawable();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int tileProviderWidth = this.mTileView.getTileProviderWidth();
            int tileProviderHeight = this.mTileView.getTileProviderHeight();
            if (tileProviderWidth <= 0 || tileProviderHeight <= 0) {
                LogManager.getLogger().w("PhotoViewAttacher", String.format(Locale.US, "invalid tile size[%dx%d]", Integer.valueOf(tileProviderWidth), Integer.valueOf(tileProviderHeight)));
            } else {
                if (((this.mTileView.getTileProviderRotation() / 90) & 1) == 1) {
                    tileProviderWidth = this.mTileView.getTileProviderHeight();
                    tileProviderHeight = this.mTileView.getTileProviderWidth();
                }
                float f = (intrinsicWidth * 1.0f) / intrinsicHeight;
                float f2 = (tileProviderWidth * 1.0f) / tileProviderHeight;
                if (BaseMiscUtil.floatNear(f, f2, 0.5f)) {
                    return getDisplayRect() != null && getScale() >= 1.0f;
                }
                LogManager.getLogger().w("PhotoViewAttacher", String.format(Locale.US, "drawable w/h not equal to tile w/h: %.2f, %.2f", Float.valueOf(f), Float.valueOf(f2)));
                if (BaseMiscUtil.floatEquals(Math.min(intrinsicWidth, intrinsicHeight) / Math.max(intrinsicWidth, intrinsicHeight), Math.min(tileProviderWidth, tileProviderHeight) / Math.max(tileProviderWidth, tileProviderHeight))) {
                    LogManager.getLogger().e("PhotoViewAttacher", String.format("drawable w[%s], h[%s] not equal to tile w[%s], h[%s]; tile rotation[%s]", Integer.valueOf(intrinsicWidth), Integer.valueOf(intrinsicHeight), Integer.valueOf(tileProviderWidth), Integer.valueOf(tileProviderHeight), Integer.valueOf(this.mTileView.getTileProviderRotation())));
                    HashMap hashMap = new HashMap();
                    hashMap.put(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, String.valueOf(this.mTileView.getTileProviderRotation()));
                    SamplingStatHelper.recordCountEvent("photo", "photo_tile_orientation_error", hashMap);
                }
            }
        }
        return false;
    }

    public final void notifyTileViewInvalidate() {
        if (needDrawTile()) {
            this.mTileView.notifyInvalidate(getDisplayRect(), getRotate());
        }
    }

    public final void updateBackgroundAlpha(float f) {
        float clamp = BaseMiscUtil.clamp(f, 0.0f, 1.0f);
        this.mAlpha = clamp;
        OnBackgroundAlphaChangedListener onBackgroundAlphaChangedListener = this.mBackgroundAlphaChangedListener;
        if (onBackgroundAlphaChangedListener != null) {
            onBackgroundAlphaChangedListener.onAlphaChanged(clamp);
        }
    }

    public final void updateTransitionProgress(float f, boolean z) {
        OnPhotoViewTransitionListener onPhotoViewTransitionListener = this.mPhotoViewTransitionListener;
        if (onPhotoViewTransitionListener != null) {
            onPhotoViewTransitionListener.onProgressChanged(f, z);
        }
    }

    public final void postScale(float f, float f2, float f3, float f4) {
        this.mSuppMatrix.postScale(f, f2, f3, f4);
        onScaleChanged(f, f2, f3, f4);
    }

    public final void postTranslate(float f, float f2) {
        this.mSuppMatrix.postTranslate(f, f2);
    }

    public final void postRotate(float f, float f2, float f3) {
        this.mSuppMatrix.postRotate(f, f2, f3);
    }

    public final void onScaleChanged(float f, float f2, float f3, float f4) {
        dispatchScaleChanged(f, f2, f3, f4, getScale());
    }

    public final void setRotate(float f, float f2, float f3) {
        this.mSuppMatrix.setRotate(f, f2, f3);
    }

    public final int trimRotation(float f) {
        return ((((int) (f > 0.0f ? f + 45.0f : f - 45.0f)) / 90) * 90) % 360;
    }

    public void startSharedElementTransition() {
        this.isSharedElementTransitionRunning = true;
    }

    public void endSharedElementTransition() {
        this.isSharedElementTransitionRunning = false;
    }

    public void resetDefaultPhotoStatus() {
        ImageView imageView = this.mImageView;
        if (imageView == null || !this.mHasOperated) {
            return;
        }
        updateBaseMatrix(imageView.getDrawable(), trimRotation(this.mBaseRotation));
        resetMatrix();
    }

    /* loaded from: classes.dex */
    public class AnimatedTranslateRunnable implements Runnable {
        public int mCurrentX;
        public int mCurrentY;
        public final OverScroller mScroller;

        public AnimatedTranslateRunnable(Context context) {
            PhotoViewAttacher.this = r2;
            this.mScroller = new OverScroller(context, new CubicEaseOutInterpolator());
        }

        public AnimatedTranslateRunnable translate(int i, int i2, int i3, int i4) {
            this.mCurrentX = i;
            this.mCurrentY = i2;
            if (i3 != 0 || i4 != 0) {
                this.mScroller.startScroll(i, i2, i3, i4, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
            }
            return this;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mScroller.isFinished()) {
                PhotoViewAttacher.this.clearAnim(22);
            } else if (PhotoViewAttacher.this.mImageView == null) {
                PhotoViewAttacher.this.clearAnim(22);
            } else {
                PhotoViewAttacher.this.appendAnim(22);
                if (!this.mScroller.computeScrollOffset()) {
                    PhotoViewAttacher.this.clearAnim(22);
                    return;
                }
                int currX = this.mScroller.getCurrX() - this.mCurrentX;
                int currY = this.mScroller.getCurrY() - this.mCurrentY;
                this.mCurrentX = this.mScroller.getCurrX();
                this.mCurrentY = this.mScroller.getCurrY();
                PhotoViewAttacher.this.postTranslate(currX, currY);
                PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
                photoViewAttacher.setImageViewMatrix(photoViewAttacher.getDrawMatrix());
                PhotoViewAttacher.this.mImageView.postOnAnimation(this);
            }
        }
    }

    /* loaded from: classes.dex */
    public class AnimatedZoomRunnable implements Runnable {
        public final float mFocalX;
        public final float mFocalY;
        public final long mStartTime = System.currentTimeMillis();
        public final int mZoomDuration;
        public final float mZoomEnd;
        public final float mZoomStart;

        public AnimatedZoomRunnable(float f, float f2, float f3, float f4) {
            PhotoViewAttacher.this = r1;
            this.mFocalX = f3;
            this.mFocalY = f4;
            this.mZoomStart = f;
            this.mZoomEnd = f2;
            this.mZoomDuration = (f2 <= f || BaseMiscUtil.floatEquals(r1.mHighScaleLevelLowerBound, r1.mMaxDoubleTapScale) || f >= r1.mHighScaleLevelLowerBound || !BaseMiscUtil.floatNear(f2, r1.mMaxDoubleTapScale, 1.0E-5f)) ? r1.mZoomDuration : (int) (r1.mZoomDuration * r1.mZoomDurationLengthenFactor);
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PhotoViewAttacher.this.mImageView == null) {
                PhotoViewAttacher.this.clearAnim(1);
                return;
            }
            PhotoViewAttacher.this.appendAnim(1);
            float interpolate = interpolate();
            float f = this.mZoomStart;
            PhotoViewAttacher.this.mOnGestureListener.onScale((f + ((this.mZoomEnd - f) * interpolate)) / PhotoViewAttacher.this.getScale(), this.mFocalX, this.mFocalY);
            if (interpolate < 1.0f) {
                PhotoViewAttacher.this.mImageView.postOnAnimation(this);
                return;
            }
            PhotoViewAttacher.this.clearAnim(1);
            PhotoViewAttacher.this.tryAlignBounds();
        }

        public final float interpolate() {
            return PhotoViewAttacher.this.mZoomInterpolator.getInterpolation(Math.min(1.0f, (((float) (System.currentTimeMillis() - this.mStartTime)) * 1.0f) / this.mZoomDuration));
        }
    }

    /* loaded from: classes.dex */
    public class FlingRunnable implements Runnable {
        public int mCurrentX;
        public int mCurrentY;
        public final OverScroller mScroller;

        public FlingRunnable(Context context) {
            PhotoViewAttacher.this = r1;
            this.mScroller = new OverScroller(context);
        }

        public void cancelFling() {
            this.mScroller.forceFinished(true);
        }

        public void fling(int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
            if (displayRect == null) {
                return;
            }
            int round = Math.round(-displayRect.left);
            float f = i;
            if (f < displayRect.width()) {
                i6 = Math.round(displayRect.width() - f);
                i5 = 0;
            } else {
                i5 = round;
                i6 = i5;
            }
            int round2 = Math.round(-displayRect.top);
            float f2 = i2;
            if (f2 < displayRect.height()) {
                i8 = Math.round(displayRect.height() - f2);
                i7 = 0;
            } else {
                i7 = round2;
                i8 = i7;
            }
            this.mCurrentX = round;
            this.mCurrentY = round2;
            if (round == i6 && round2 == i8) {
                return;
            }
            this.mScroller.fling(round, round2, i3, i4, i5, i6, i7, i8, 0, 0);
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mScroller.isFinished()) {
                PhotoViewAttacher.this.clearAnim(2);
                return;
            }
            PhotoViewAttacher.this.appendAnim(2);
            if (PhotoViewAttacher.this.mImageView == null || !this.mScroller.computeScrollOffset()) {
                PhotoViewAttacher.this.clearAnim(2);
                return;
            }
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            PhotoViewAttacher.this.postTranslate(this.mCurrentX - currX, this.mCurrentY - currY);
            PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
            photoViewAttacher.setImageViewMatrix(photoViewAttacher.getDrawMatrix());
            this.mCurrentX = currX;
            this.mCurrentY = currY;
            PhotoViewAttacher.this.mImageView.postOnAnimation(this);
        }
    }

    /* loaded from: classes.dex */
    public class Transition {
        public boolean isClip;
        public boolean isExited;
        public boolean isRunning;
        public boolean isTranslateOnly;
        public float mAlphaEnd;
        public float mAlphaStart;
        public AnimConfig mAnimConfig;
        public IStateStyle mAnimStateStyle;
        public final ValueTarget mAnimTarget;
        public float mClipDX;
        public float mClipDY;
        public RoundRect mClipPath;
        public RectF mClipRect;
        public float mClipXFrom;
        public float mClipYFrom;
        public float mCoverClipDX;
        public float mCoverClipDY;
        public float mCoverClipXFrom;
        public float mCoverClipYFrom;
        public int mCurrentX;
        public int mCurrentY;
        public TransitionListener mListener;
        public RectF mOriginClipRect;
        public float mScaleFrom;
        public float mScaleTo;
        public int mTranslateDX;
        public int mTranslateDY;
        public final ValueProperty PROGRESS = new ValueProperty("progress");
        public final ValueProperty ALPHA = new ValueProperty("alpha");
        public final IntValueProperty TRANSLATE_X = new IntValueProperty("translateX");
        public final IntValueProperty TRANSLATE_Y = new IntValueProperty("translateY");
        public final ValueProperty SCALE = new ValueProperty("scale");
        public final ValueProperty CLIP_X = new ValueProperty("clipX");
        public final ValueProperty CLIP_Y = new ValueProperty("clipY");
        public final ValueProperty COVER_CLIP_X = new ValueProperty("coverClipX");
        public final ValueProperty COVER_CLIP_Y = new ValueProperty("coverClipY");
        public final ValueProperty CLIP_PATH = new ValueProperty("clipPath");
        public final ValueProperty ALPHA_END = new ValueProperty("alphaEnd");
        public Matrix mClipMatrix = new Matrix();

        public static /* synthetic */ int access$7312(Transition transition, int i) {
            int i2 = transition.mCurrentX + i;
            transition.mCurrentX = i2;
            return i2;
        }

        public static /* synthetic */ int access$7412(Transition transition, int i) {
            int i2 = transition.mCurrentY + i;
            transition.mCurrentY = i2;
            return i2;
        }

        public Transition(Context context, boolean z) {
            PhotoViewAttacher.this = r3;
            ValueTarget valueTarget = new ValueTarget();
            this.mAnimTarget = valueTarget;
            valueTarget.setFlags(1L);
            buildAnimConfigs(z);
        }

        public void buildAnimConfigs(boolean z) {
            this.mAnimConfig = new AnimConfig().setEase(EaseManager.getStyle(6, 250.0f)).setSpecial(this.PROGRESS, EaseManager.getStyle(1, 250.0f), new float[0]).setSpecial(this.ALPHA_END, EaseManager.getStyle(16, 450.0f), new float[0]);
        }

        public void setTransitionListener(TransitionListener transitionListener) {
            this.mListener = transitionListener;
        }

        public void translate(int i, int i2, int i3, int i4) {
            this.mCurrentX = i;
            this.mCurrentY = i2;
            this.mTranslateDX = i3;
            this.mTranslateDY = i4;
        }

        public void scale(float f, float f2) {
            this.mScaleFrom = f;
            this.mScaleTo = f2;
        }

        public void clip(float f, float f2, float f3, float f4) {
            PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
            RectF displayRect = photoViewAttacher.getDisplayRect(photoViewAttacher.getDrawMatrix());
            if (displayRect == null) {
                return;
            }
            this.isClip = true;
            this.mClipMatrix.setScale(f, f2, (displayRect.left + displayRect.right) / 2.0f, (displayRect.top + displayRect.bottom) / 2.0f);
            RectF rectF = new RectF();
            this.mClipRect = rectF;
            this.mClipMatrix.mapRect(rectF, displayRect);
            this.mOriginClipRect = new RectF(this.mClipRect);
            this.mClipXFrom = f;
            this.mClipYFrom = f2;
            this.mClipDX = f3;
            this.mClipDY = f4;
        }

        public void coverClip(float f, float f2, float f3, float f4) {
            PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
            RectF displayRect = photoViewAttacher.getDisplayRect(photoViewAttacher.getDrawMatrix());
            if (displayRect == null) {
                return;
            }
            if (!this.isClip) {
                this.mClipRect = new RectF(displayRect);
            }
            if (f > 0.0f) {
                RectF rectF = this.mClipRect;
                rectF.left += rectF.width() * f;
            } else {
                RectF rectF2 = this.mClipRect;
                rectF2.right += rectF2.width() * f;
            }
            if (f2 > 0.0f) {
                RectF rectF3 = this.mClipRect;
                rectF3.top += rectF3.height() * f2;
            } else {
                RectF rectF4 = this.mClipRect;
                rectF4.bottom += rectF4.height() * f2;
            }
            this.mOriginClipRect = new RectF(this.mClipRect);
            this.mCoverClipXFrom = f;
            this.mCoverClipYFrom = f2;
            this.mCoverClipDX = f3;
            this.mCoverClipDY = f4;
        }

        public void alpha(float f, float f2) {
            this.mAlphaStart = f;
            this.mAlphaEnd = f2;
        }

        public void clipPath(float f, float f2) {
            PhotoViewAttacher photoViewAttacher;
            RectF displayRect;
            this.mClipPath = new RoundRect(f, f2);
            if (this.mClipRect == null && (displayRect = (photoViewAttacher = PhotoViewAttacher.this).getDisplayRect(photoViewAttacher.getDrawMatrix())) != null) {
                this.mClipRect = new RectF(displayRect);
                this.mOriginClipRect = new RectF(this.mClipRect);
            }
        }

        public RectF getClipRect() {
            return this.mClipRect;
        }

        public Path getClipPath() {
            RoundRect roundRect = this.mClipPath;
            if (roundRect == null) {
                return null;
            }
            return roundRect.get();
        }

        public boolean isRunning() {
            return this.isRunning;
        }

        public boolean isExited() {
            return this.isExited;
        }

        public void start(boolean z) {
            if (PhotoViewAttacher.this.mImageView != null) {
                this.isRunning = true;
                this.mAnimStateStyle = Folme.useValue(this.mAnimTarget).addListener(new AnonymousClass1(z));
                AnimState add = new AnimState("start").add(this.PROGRESS, SearchStatUtils.POW).add(this.ALPHA, this.mAlphaStart).add(this.TRANSLATE_X, this.mCurrentX).add(this.TRANSLATE_Y, this.mCurrentY).add(this.SCALE, this.mScaleFrom).add(this.CLIP_X, this.mClipXFrom).add(this.CLIP_Y, this.mClipYFrom).add(this.COVER_CLIP_X, this.mCoverClipXFrom).add(this.COVER_CLIP_Y, this.mCoverClipYFrom);
                AnimState add2 = new AnimState("end").add(this.PROGRESS, 1.0d).add(this.ALPHA, this.mAlphaEnd).add(this.TRANSLATE_X, this.mCurrentX + this.mTranslateDX).add(this.TRANSLATE_Y, this.mCurrentY + this.mTranslateDY).add(this.SCALE, this.mScaleTo).add(this.CLIP_X, this.mClipXFrom + this.mClipDX).add(this.CLIP_Y, this.mClipYFrom + this.mClipDY).add(this.COVER_CLIP_X, this.mCoverClipXFrom + this.mCoverClipDX).add(this.COVER_CLIP_Y, this.mCoverClipYFrom + this.mCoverClipDY);
                if (this.mClipPath != null) {
                    add.add(this.CLIP_PATH, SearchStatUtils.POW);
                    add2.add(this.CLIP_PATH, 1.0d);
                    if (z) {
                        add.add(this.ALPHA_END, SearchStatUtils.POW);
                        add2.add(this.ALPHA_END, 1.7999999523162842d);
                    }
                }
                this.mAnimStateStyle.fromTo(add, add2, this.mAnimConfig);
            }
        }

        /* renamed from: com.github.chrisbanes.photoview.PhotoViewAttacher$Transition$1 */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends miuix.animation.listener.TransitionListener {
            public float lastClipPath = 0.0f;
            public float lastClipX;
            public float lastClipY;
            public float lastCoverClipX;
            public float lastCoverClipY;
            public final /* synthetic */ boolean val$isExit;

            public static /* synthetic */ void $r8$lambda$6OsvktJ7bIjboA3E4dj3VuhgfHs(AnonymousClass1 anonymousClass1) {
                anonymousClass1.lambda$onComplete$0();
            }

            public AnonymousClass1(boolean z) {
                Transition.this = r1;
                this.val$isExit = z;
                this.lastClipX = r1.mClipXFrom;
                this.lastClipY = r1.mClipYFrom;
                this.lastCoverClipX = r1.mCoverClipXFrom;
                this.lastCoverClipY = r1.mCoverClipYFrom;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                PhotoViewAttacher.this.appendAnim(4);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                UpdateInfo findBy = UpdateInfo.findBy(collection, Transition.this.PROGRESS);
                UpdateInfo findBy2 = UpdateInfo.findBy(collection, Transition.this.ALPHA);
                UpdateInfo findBy3 = UpdateInfo.findBy(collection, Transition.this.TRANSLATE_X);
                UpdateInfo findBy4 = UpdateInfo.findBy(collection, Transition.this.TRANSLATE_Y);
                UpdateInfo findBy5 = UpdateInfo.findBy(collection, Transition.this.SCALE);
                UpdateInfo findBy6 = UpdateInfo.findBy(collection, Transition.this.CLIP_X);
                UpdateInfo findBy7 = UpdateInfo.findBy(collection, Transition.this.CLIP_Y);
                UpdateInfo findBy8 = UpdateInfo.findBy(collection, Transition.this.COVER_CLIP_X);
                UpdateInfo findBy9 = UpdateInfo.findBy(collection, Transition.this.COVER_CLIP_Y);
                UpdateInfo findBy10 = UpdateInfo.findBy(collection, Transition.this.CLIP_PATH);
                UpdateInfo findBy11 = UpdateInfo.findBy(collection, Transition.this.ALPHA_END);
                if (findBy != null && !Transition.this.isTranslateOnly) {
                    float floatValue = findBy.getFloatValue();
                    PhotoViewAttacher.this.updateTransitionProgress(floatValue, this.val$isExit);
                    TransitionListener transitionListener = Transition.this.mListener;
                    if (transitionListener != null) {
                        transitionListener.onTransitUpdate(floatValue);
                    }
                }
                if (findBy2 != null) {
                    PhotoViewAttacher.this.updateBackgroundAlpha(findBy2.getFloatValue());
                }
                int i = 0;
                int intValue = findBy3 != null ? findBy3.getIntValue() - Transition.this.mCurrentX : 0;
                if (findBy4 != null) {
                    i = findBy4.getIntValue() - Transition.this.mCurrentY;
                }
                Transition.access$7312(Transition.this, intValue);
                Transition.access$7412(Transition.this, i);
                PhotoViewAttacher.this.postTranslate(intValue, i);
                if (findBy5 != null && !Transition.this.isTranslateOnly) {
                    float floatValue2 = findBy5.getFloatValue() / PhotoViewAttacher.this.getScale();
                    PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
                    RectF displayRect = photoViewAttacher.getDisplayRect(photoViewAttacher.getDrawMatrix());
                    if (displayRect != null) {
                        PhotoViewAttacher.this.postScale(floatValue2, floatValue2, displayRect.left, displayRect.top);
                    }
                }
                if (findBy11 != null && !Transition.this.isTranslateOnly) {
                    float floatValue3 = (findBy11.getFloatValue() - 1.0f) / 0.79999995f;
                    if (floatValue3 > 0.0f && floatValue3 < 1.0f && PhotoViewAttacher.this.mImageView != null) {
                        PhotoViewAttacher.this.mImageView.setAlpha(1.0f - floatValue3);
                    }
                }
                PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
                RectF displayRect2 = photoViewAttacher2.getDisplayRect(photoViewAttacher2.getDrawMatrix());
                if (displayRect2 == null) {
                    if (PhotoViewAttacher.this.mImageView == null) {
                        return;
                    }
                    PhotoViewAttacher.this.mImageView.invalidate();
                    return;
                }
                if (Transition.this.mClipRect != null && !Transition.this.isTranslateOnly) {
                    if (findBy6 != null) {
                        this.lastClipX = findBy6.getFloatValue();
                    }
                    if (findBy7 != null) {
                        this.lastClipY = findBy7.getFloatValue();
                    }
                    Transition.this.mClipMatrix.setScale(this.lastClipX, this.lastClipY, (displayRect2.left + displayRect2.right) / 2.0f, (displayRect2.top + displayRect2.bottom) / 2.0f);
                    Transition.this.mClipMatrix.mapRect(Transition.this.mClipRect, displayRect2);
                    if (!this.val$isExit) {
                        Transition.this.mClipRect.set(Math.min(Transition.this.mClipRect.left, Transition.this.mOriginClipRect.left), Math.min(Transition.this.mClipRect.top, Transition.this.mOriginClipRect.top), Math.max(Transition.this.mClipRect.right, Transition.this.mOriginClipRect.right), Math.max(Transition.this.mClipRect.bottom, Transition.this.mOriginClipRect.bottom));
                    }
                    if (findBy8 != null) {
                        this.lastCoverClipX = findBy8.getFloatValue();
                    }
                    if (findBy9 != null) {
                        this.lastCoverClipY = findBy9.getFloatValue();
                    }
                    if (!Transition.this.isClip) {
                        Transition.this.mClipRect.set(displayRect2);
                    }
                    if (this.lastCoverClipX > 0.0f) {
                        Transition.this.mClipRect.left -= this.lastCoverClipX * Transition.this.mClipRect.width();
                    } else {
                        Transition.this.mClipRect.right -= this.lastCoverClipX * Transition.this.mClipRect.width();
                    }
                    if (this.lastCoverClipY > 0.0f) {
                        Transition.this.mClipRect.top += this.lastCoverClipY * Transition.this.mClipRect.height();
                    } else {
                        Transition.this.mClipRect.bottom += this.lastCoverClipY * Transition.this.mClipRect.height();
                    }
                    if (findBy10 != null) {
                        this.lastClipPath = findBy10.getFloatValue();
                    }
                    if (Transition.this.mClipPath != null) {
                        Transition.this.mClipPath.set(Transition.this.mClipRect, this.lastClipPath);
                    }
                }
                PhotoViewAttacher photoViewAttacher3 = PhotoViewAttacher.this;
                photoViewAttacher3.setImageViewMatrix(photoViewAttacher3.getDrawMatrix());
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                Folme.clean(Transition.this.mAnimTarget);
                PhotoViewAttacher.this.clearAnim(4);
                if (!Transition.this.isRunning) {
                    return;
                }
                Transition.this.mClipRect = null;
                Transition.this.mOriginClipRect = null;
                Transition.this.isRunning = false;
                Transition.this.isTranslateOnly = false;
                if (this.val$isExit) {
                    Transition.this.isExited = true;
                } else {
                    Transition.this.isExited = false;
                    PhotoViewAttacher.this.resetMatrix();
                }
                if (PhotoViewAttacher.this.mImageView == null) {
                    return;
                }
                PhotoViewAttacher.this.mImageView.postDelayed(new Runnable() { // from class: com.github.chrisbanes.photoview.PhotoViewAttacher$Transition$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoViewAttacher.Transition.AnonymousClass1.$r8$lambda$6OsvktJ7bIjboA3E4dj3VuhgfHs(PhotoViewAttacher.Transition.AnonymousClass1.this);
                    }
                }, 20L);
            }

            public /* synthetic */ void lambda$onComplete$0() {
                TransitionListener transitionListener = Transition.this.mListener;
                if (transitionListener != null) {
                    transitionListener.onTransitEnd();
                    Transition.this.mListener = null;
                }
            }
        }

        public void stop() {
            this.isClip = false;
            this.isRunning = false;
            IStateStyle iStateStyle = this.mAnimStateStyle;
            if (iStateStyle != null) {
                iStateStyle.cancel();
            }
        }

        public void ensureAlpha(boolean z) {
            float f = 1.0f;
            float f2 = 0.0f;
            if (z) {
                if (!BaseMiscUtil.floatEquals(PhotoViewAttacher.this.mAlpha, 1.0f)) {
                    f2 = PhotoViewAttacher.this.mAlpha;
                }
            } else {
                if (!BaseMiscUtil.floatEquals(PhotoViewAttacher.this.mAlpha, 0.0f)) {
                    f = PhotoViewAttacher.this.mAlpha;
                }
                f2 = f;
            }
            PhotoViewAttacher.this.updateBackgroundAlpha(f2);
        }

        /* loaded from: classes.dex */
        public class RoundRect {
            public float from;
            public float to;
            public Path.Direction direction = Path.Direction.CW;
            public Path path = new Path();

            public RoundRect(float f, float f2) {
                Transition.this = r1;
                this.from = f;
                this.to = f2;
            }

            public void set(RectF rectF, float f) {
                float f2 = this.from;
                float f3 = f2 + ((this.to - f2) * f);
                this.path.reset();
                this.path.addRoundRect(rectF, new float[]{f3, f3, f3, f3, f3, f3, f3, f3}, this.direction);
            }

            public Path get() {
                return this.path;
            }
        }

        public void setTranslateOnly(boolean z) {
            this.isTranslateOnly = z;
        }
    }

    /* loaded from: classes.dex */
    public class WaitingTransition implements Runnable {
        public final ItemViewInfo mEnterInfo;
        public final long mStartTime;
        public final TransitionListener mTransitionListener;

        public WaitingTransition(ItemViewInfo itemViewInfo, TransitionListener transitionListener) {
            PhotoViewAttacher.this = r1;
            this.mEnterInfo = itemViewInfo;
            this.mTransitionListener = transitionListener;
            this.mStartTime = System.currentTimeMillis();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (PhotoViewAttacher.this.getTransition(false) != null && PhotoViewAttacher.this.mImageView != null) {
                PhotoViewAttacher.this.getTransition(false).ensureAlpha(true);
                PhotoViewAttacher.this.getTransition(false).alpha(PhotoViewAttacher.this.mAlpha, 1.0f);
                PhotoViewAttacher.this.getTransition(false).setTransitionListener(this.mTransitionListener);
                PhotoViewAttacher.this.getTransition(false).start(false);
            }
            LogManager.getLogger().w("PhotoViewAttacher", "Loading drawable is slow, transiting only with alpha");
        }
    }

    /* loaded from: classes.dex */
    public class RotateManager implements RotateGestureDetector.OnRotationGestureListener {
        public final float CRITICAL_VELOCITY;
        public AdjustAnimation mAdjustAnim;
        public RotateGestureDetector mRotateDetector;
        public float mRotatedDegrees;

        public final int calculateRotateDuration(float f, float f2) {
            return UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME;
        }

        public final int calculateScaleDuration(float f, float f2) {
            return UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME;
        }

        public RotateManager() {
            PhotoViewAttacher.this = r3;
            Context context = r3.mImageView.getContext();
            this.mRotateDetector = new RotateGestureDetector(context, this);
            this.mAdjustAnim = new AdjustAnimation(context);
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            this.CRITICAL_VELOCITY = Math.min(viewConfiguration.getScaledMinimumFlingVelocity() * 10.0f, viewConfiguration.getScaledMaximumFlingVelocity() / 10.0f);
        }

        public final void rotateBy(float f, float f2, float f3) {
            PhotoViewAttacher.this.postRotate(f, f2, f3);
            PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
            photoViewAttacher.setImageViewMatrix(photoViewAttacher.getDrawMatrix());
        }

        public boolean isRotating() {
            return isManualRotating() || isAutoRotating();
        }

        public boolean isManualRotating() {
            return this.mRotateDetector.isInProgress();
        }

        public boolean isAutoRotating() {
            return this.mAdjustAnim.isRunning();
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mRotateDetector.onTouchEvent(motionEvent);
        }

        @Override // com.github.chrisbanes.photoview.RotateGestureDetector.OnRotationGestureListener
        public boolean onRotate(RotateGestureDetector rotateGestureDetector) {
            float rotateDegrees = rotateGestureDetector.getRotateDegrees();
            if (Float.isNaN(rotateDegrees) || Float.isInfinite(rotateDegrees)) {
                return false;
            }
            PhotoViewAttacher.this.mHasOperated = true;
            this.mRotatedDegrees += rotateDegrees;
            rotateBy(rotateDegrees, rotateGestureDetector.getFocusX(), rotateGestureDetector.getFocusY());
            float rotate = PhotoViewAttacher.this.getRotate();
            for (OnRotateListener onRotateListener : PhotoViewAttacher.this.mOnRotateListeners) {
                onRotateListener.onRotate(rotateDegrees, rotate, rotateGestureDetector.getFocusX(), rotateGestureDetector.getFocusY());
            }
            return true;
        }

        @Override // com.github.chrisbanes.photoview.RotateGestureDetector.OnRotationGestureListener
        public boolean onRotateBegin(RotateGestureDetector rotateGestureDetector) {
            PhotoViewAttacher.this.appendAnim(8);
            this.mRotatedDegrees = 0.0f;
            float rotate = PhotoViewAttacher.this.getRotate();
            for (OnRotateListener onRotateListener : PhotoViewAttacher.this.mOnRotateListeners) {
                onRotateListener.onRotateBegin(rotate);
            }
            return true;
        }

        public final int calculateFinalDegrees(boolean z, float f) {
            float abs;
            int i;
            float f2;
            float rotate = PhotoViewAttacher.this.getRotate();
            float f3 = this.mRotatedDegrees % 90.0f;
            if ((f3 > 0.0f && !z) || (f3 < 0.0f && z)) {
                abs = 90.0f - Math.abs(f3);
            } else {
                abs = Math.abs(f3);
            }
            if (Math.abs(f) > this.CRITICAL_VELOCITY) {
                if (abs > 20.0f) {
                    float f4 = rotate % 90.0f;
                    if (f4 > 0.0f) {
                        if (!z) {
                            f2 = 90.0f - f4;
                        }
                        f2 = -f4;
                    } else {
                        if (z) {
                            f2 = (-90.0f) - f4;
                        }
                        f2 = -f4;
                    }
                    return PhotoViewAttacher.this.trimRotation(rotate + f2);
                }
                i = ((int) rotate) / 90;
            } else if (abs > 45.0f) {
                return PhotoViewAttacher.this.trimRotation(rotate);
            } else {
                i = ((int) rotate) / 90;
            }
            return i * 90;
        }

        @Override // com.github.chrisbanes.photoview.RotateGestureDetector.OnRotationGestureListener
        public void onRotateEnd(RotateGestureDetector rotateGestureDetector, boolean z, float f) {
            adjustAfterRotate(rotateGestureDetector, z, f);
        }

        public final void adjustAfterRotate(RotateGestureDetector rotateGestureDetector, boolean z, float f) {
            int i;
            if (PhotoViewAttacher.this.mImageView == null) {
                return;
            }
            this.mAdjustAnim.stop();
            float rotate = PhotoViewAttacher.this.getRotate();
            float calculateFinalDegrees = calculateFinalDegrees(z, f);
            Matrix matrix = new Matrix(PhotoViewAttacher.this.getDrawMatrix());
            float f2 = rotate - calculateFinalDegrees;
            matrix.postRotate(f2, rotateGestureDetector.getFocusX(), rotateGestureDetector.getFocusY());
            int calculateRotateDuration = calculateRotateDuration(Math.abs(f2), f);
            this.mAdjustAnim.rotate(rotate, calculateFinalDegrees, rotateGestureDetector.getFocusX(), rotateGestureDetector.getFocusY(), calculateRotateDuration);
            float[] calculateTranslate = calculateTranslate(matrix);
            if (calculateTranslate != null) {
                this.mAdjustAnim.translate(0.0f, 0.0f, calculateTranslate[0], calculateTranslate[1], calculateRotateDuration);
            }
            float calculateScale = calculateScale(matrix);
            if (!Float.isNaN(calculateScale)) {
                float scale = MatrixUtil.getScale(PhotoViewAttacher.this.mSuppMatrix);
                float f3 = scale * calculateScale;
                i = calculateScaleDuration(calculateScale, f);
                this.mAdjustAnim.scale(scale, f3, i);
            } else {
                i = calculateRotateDuration;
            }
            this.mAdjustAnim.alpha(PhotoViewAttacher.this.mAlpha, 1.0f, Math.max(calculateRotateDuration, i));
            this.mAdjustAnim.start();
        }

        public final boolean needTrimToBaseRect(RectF rectF, RectF rectF2) {
            return !PhotoViewAttacher.this.mZoomEnabled || rectF.width() < rectF2.width() * 1.2f || rectF.height() < rectF2.height() * 1.2f;
        }

        /* JADX WARN: Removed duplicated region for block: B:60:0x00a8  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x00d3  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final float[] calculateTranslate(android.graphics.Matrix r7) {
            /*
                Method dump skipped, instructions count: 231
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.github.chrisbanes.photoview.PhotoViewAttacher.RotateManager.calculateTranslate(android.graphics.Matrix):float[]");
        }

        public final float calculateScale(Matrix matrix) {
            if (PhotoViewAttacher.this.mImageView == null) {
                return Float.NaN;
            }
            RectF calculateBaseRect = PhotoViewAttacher.this.calculateBaseRect(PhotoViewAttacher.this.mImageView.getDrawable(), (int) MatrixUtil.getRotate(matrix));
            if (calculateBaseRect == null) {
                return Float.NaN;
            }
            RectF rectF = new RectF(PhotoViewAttacher.this.getDisplayRect(matrix));
            if (!needTrimToBaseRect(rectF, calculateBaseRect)) {
                return Float.NaN;
            }
            return Math.max(calculateBaseRect.width() / rectF.width(), calculateBaseRect.height() / rectF.height());
        }

        /* loaded from: classes.dex */
        public class AdjustAnimation implements Runnable {
            public boolean isRunning;
            public Scroller mAlphaScroller;
            public float mRotateFocusX;
            public float mRotateFocusY;
            public Scroller mRotateScroller;
            public Scroller mScaleScroller;
            public Scroller mTranslateScroller;
            public int mTranslateX;
            public int mTranslateY;

            public final int precise(float f) {
                return (int) (f * 10000.0f);
            }

            public final float unPrecise(int i) {
                return (i * 1.0f) / 10000.0f;
            }

            public AdjustAnimation(Context context) {
                RotateManager.this = r2;
                CubicEaseOutInterpolator cubicEaseOutInterpolator = new CubicEaseOutInterpolator();
                this.mRotateScroller = new Scroller(context, cubicEaseOutInterpolator);
                this.mScaleScroller = new Scroller(context, cubicEaseOutInterpolator);
                this.mTranslateScroller = new Scroller(context, cubicEaseOutInterpolator);
                this.mAlphaScroller = new Scroller(context, cubicEaseOutInterpolator);
            }

            public boolean isRunning() {
                return this.isRunning;
            }

            public void rotate(float f, float f2, float f3, float f4, int i) {
                this.mRotateScroller.forceFinished(true);
                this.mRotateFocusX = f3;
                this.mRotateFocusY = f4;
                this.mRotateScroller.startScroll(precise(f), 0, precise(f2 - f), 0, i);
            }

            public void scale(float f, float f2, int i) {
                this.mScaleScroller.forceFinished(true);
                this.mScaleScroller.startScroll(precise(f), 0, precise(f2 - f), 0, i);
            }

            public void translate(float f, float f2, float f3, float f4, int i) {
                this.mTranslateScroller.forceFinished(true);
                this.mTranslateX = precise(f);
                this.mTranslateY = precise(f2);
                this.mTranslateScroller.startScroll(precise(f), precise(f2), precise(f3), precise(f4), i);
            }

            public void alpha(float f, float f2, int i) {
                this.mAlphaScroller.forceFinished(true);
                this.mAlphaScroller.startScroll(precise(f), 0, precise(f2 - f), 0, i);
            }

            public void start() {
                if (PhotoViewAttacher.this.mImageView != null) {
                    this.isRunning = true;
                    PhotoViewAttacher.this.mImageView.postOnAnimation(this);
                }
            }

            public void stop() {
                this.mRotateScroller.forceFinished(true);
                this.mScaleScroller.forceFinished(true);
                this.mTranslateScroller.forceFinished(true);
                if (this.isRunning) {
                    this.isRunning = false;
                    PhotoViewAttacher.this.clearAnim(8);
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean z;
                if (isRunning()) {
                    if (PhotoViewAttacher.this.mImageView == null) {
                        return;
                    }
                    boolean z2 = true;
                    if (this.mRotateScroller.computeScrollOffset()) {
                        PhotoViewAttacher.this.postRotate(PhotoViewAttacher.this.getRotate() - unPrecise(this.mRotateScroller.getCurrX()), this.mRotateFocusX, this.mRotateFocusY);
                        z = true;
                    } else {
                        z = false;
                    }
                    if (this.mScaleScroller.computeScrollOffset()) {
                        float unPrecise = unPrecise(this.mScaleScroller.getCurrX()) / MatrixUtil.getScale(PhotoViewAttacher.this.mSuppMatrix);
                        PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
                        RectF displayRect = photoViewAttacher.getDisplayRect(photoViewAttacher.getDrawMatrix());
                        if (displayRect != null) {
                            PhotoViewAttacher.this.postScale(unPrecise, unPrecise, displayRect.centerX(), displayRect.centerY());
                        }
                        z = true;
                    }
                    if (this.mTranslateScroller.computeScrollOffset()) {
                        int currX = this.mTranslateScroller.getCurrX();
                        int currY = this.mTranslateScroller.getCurrY();
                        float unPrecise2 = unPrecise(currX - this.mTranslateX);
                        float unPrecise3 = unPrecise(currY - this.mTranslateY);
                        this.mRotateFocusX += unPrecise2;
                        this.mRotateFocusY += unPrecise3;
                        PhotoViewAttacher.this.postTranslate(unPrecise2, unPrecise3);
                        this.mTranslateX = currX;
                        this.mTranslateY = currY;
                    } else {
                        z2 = z;
                    }
                    if (this.mAlphaScroller.computeScrollOffset()) {
                        PhotoViewAttacher.this.updateBackgroundAlpha(unPrecise(this.mAlphaScroller.getCurrX()));
                    }
                    PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
                    photoViewAttacher2.setImageViewMatrix(photoViewAttacher2.getDrawMatrix());
                    if (z2) {
                        PhotoViewAttacher.this.mImageView.postOnAnimation(this);
                        return;
                    }
                    checkBounds();
                    updateMatrix();
                    PhotoViewAttacher.this.updateBackgroundAlpha(1.0f);
                    this.isRunning = false;
                    PhotoViewAttacher photoViewAttacher3 = PhotoViewAttacher.this;
                    photoViewAttacher3.setImageViewMatrix(photoViewAttacher3.getDrawMatrix());
                    PhotoViewAttacher.this.clearAnim(8);
                    float rotate = PhotoViewAttacher.this.getRotate();
                    for (OnRotateListener onRotateListener : PhotoViewAttacher.this.mOnRotateListeners) {
                        onRotateListener.onRotateEnd(rotate);
                    }
                    return;
                }
                stop();
            }

            public final void updateMatrix() {
                float f;
                float f2;
                if (PhotoViewAttacher.this.mImageView != null) {
                    float rotate = PhotoViewAttacher.this.getRotate();
                    PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
                    RectF rectF = new RectF(photoViewAttacher.getDisplayRect(photoViewAttacher.getDrawMatrix()));
                    Drawable drawable = PhotoViewAttacher.this.mImageView.getDrawable();
                    PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
                    photoViewAttacher2.updateBaseMatrix(drawable, photoViewAttacher2.trimRotation(rotate));
                    PhotoViewAttacher photoViewAttacher3 = PhotoViewAttacher.this;
                    RectF rectF2 = new RectF(photoViewAttacher3.getDisplayRect(photoViewAttacher3.mBaseMatrix));
                    PhotoViewAttacher.this.mSuppMatrix.reset();
                    if (!RotateManager.this.needTrimToBaseRect(rectF, rectF2)) {
                        float width = rectF.width() / rectF2.width();
                        float height = rectF.height() / rectF2.height();
                        Matrix matrix = new Matrix();
                        matrix.postScale(width, height, rectF2.centerX(), rectF2.centerY());
                        matrix.postTranslate(rectF.centerX() - rectF2.centerX(), rectF.centerY() - rectF2.centerY());
                        PhotoViewAttacher.this.mSuppMatrix.set(matrix);
                        f2 = height;
                        f = width;
                    } else {
                        f = 1.0f;
                        f2 = 1.0f;
                    }
                    PhotoViewAttacher.this.dispatchScaleChanged(f, f2, rectF2.centerX(), rectF2.centerY(), PhotoViewAttacher.this.getScale());
                }
            }

            public final void checkBounds() {
                if (PhotoViewAttacher.this.mImageView == null) {
                    return;
                }
                PhotoViewAttacher photoViewAttacher = PhotoViewAttacher.this;
                RectF displayRect = photoViewAttacher.getDisplayRect(photoViewAttacher.getDrawMatrix());
                float rotate = PhotoViewAttacher.this.getRotate();
                int trimRotation = PhotoViewAttacher.this.trimRotation(rotate);
                if (displayRect != null) {
                    float f = trimRotation;
                    if (!BaseMiscUtil.floatEquals(rotate, f)) {
                        PhotoViewAttacher.this.postRotate(rotate - f, displayRect.centerX(), displayRect.centerY());
                    }
                }
                float[] calculateTranslate = RotateManager.this.calculateTranslate(PhotoViewAttacher.this.getDrawMatrix());
                if (calculateTranslate != null) {
                    PhotoViewAttacher.this.postTranslate(calculateTranslate[0], calculateTranslate[1]);
                }
                RectF calculateBaseRect = PhotoViewAttacher.this.calculateBaseRect(PhotoViewAttacher.this.mImageView.getDrawable(), (int) PhotoViewAttacher.this.getRotate());
                if (calculateBaseRect == null) {
                    LogManager.getLogger().e("PhotoViewAttacher", "calculate base display null");
                    return;
                }
                PhotoViewAttacher photoViewAttacher2 = PhotoViewAttacher.this;
                RectF displayRect2 = photoViewAttacher2.getDisplayRect(photoViewAttacher2.getDrawMatrix());
                float width = displayRect2.width() < calculateBaseRect.width() ? calculateBaseRect.width() / displayRect2.width() : 1.0f;
                float width2 = ((displayRect2.width() * width) * (calculateBaseRect.height() / calculateBaseRect.width())) / displayRect2.height();
                if (BaseMiscUtil.floatEquals(width, 1.0f) && BaseMiscUtil.floatEquals(width2, 1.0f)) {
                    return;
                }
                PhotoViewAttacher.this.postScale(width, width2, displayRect2.centerX(), displayRect2.centerY());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public OnExitListener mExitListener;
        public View.OnLongClickListener mLongClickListener;
        public Set<OnMatrixChangedListener> mMatrixChangeListeners;
        public GestureDetector.OnDoubleTapListener mNewOnDoubleTapListener;
        public OnPhotoTapListener mPhotoTapListener;
        public boolean mRotatable;
        public OnScaleChangeListener mScaleChangeListener;
        public OnScaleStateChangeListener mScaleStateChangeListener;
        public float mSlipProgress;
        public int mSlippedRectBottom;
        public int mSlippedRectTop;
        public TileView mTileView;
        public OnViewSingleTapListener mViewSingleTapListener;
        public OnViewTapListener mViewTapListener;
        public boolean mZoomable;
        public float pointsScaleEnlargeFactor;
        public int zoomDuration;
        public float zoomDurationLengthenFactor;
        public Interpolator zoomInterpolator;

        public Builder cloneFrom(PhotoViewAttacher photoViewAttacher) {
            this.pointsScaleEnlargeFactor = photoViewAttacher.getPointsScaleEnlargeFactor();
            this.zoomDuration = photoViewAttacher.getZoomDuration();
            this.zoomDurationLengthenFactor = photoViewAttacher.getZoomDurationLengthenFactor();
            this.zoomInterpolator = photoViewAttacher.getZoomInterpolator();
            this.mSlipProgress = photoViewAttacher.getSlipProgress();
            this.mSlippedRectTop = photoViewAttacher.getSlippedRectTop();
            this.mSlippedRectBottom = photoViewAttacher.getSlippedRectBottom();
            this.mRotatable = photoViewAttacher.canRotatable();
            this.mZoomable = photoViewAttacher.canZoom();
            this.mNewOnDoubleTapListener = photoViewAttacher.mNewOnDoubleTapListener;
            this.mMatrixChangeListeners = photoViewAttacher.mMatrixChangeListeners;
            this.mPhotoTapListener = photoViewAttacher.mPhotoTapListener;
            this.mViewTapListener = photoViewAttacher.mViewTapListener;
            this.mViewSingleTapListener = photoViewAttacher.mViewSingleTapListener;
            this.mLongClickListener = photoViewAttacher.mLongClickListener;
            this.mScaleChangeListener = photoViewAttacher.mScaleChangeListener;
            this.mScaleStateChangeListener = photoViewAttacher.mScaleStateChangeListener;
            this.mExitListener = photoViewAttacher.mExitListener;
            this.mTileView = photoViewAttacher.mTileView;
            return this;
        }

        public PhotoViewAttacher build(ImageView imageView) {
            PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
            photoViewAttacher.setPointsScaleEnlargeFactor(this.pointsScaleEnlargeFactor);
            photoViewAttacher.setZoomDuration(this.zoomDuration);
            photoViewAttacher.setZoomDurationLengthenFactor(this.zoomDurationLengthenFactor);
            Interpolator interpolator = this.zoomInterpolator;
            if (interpolator != null) {
                photoViewAttacher.setZoomInterpolator(interpolator);
            }
            photoViewAttacher.setSlipProgress(this.mSlipProgress);
            photoViewAttacher.setSlippedRect(this.mSlippedRectTop, this.mSlippedRectBottom);
            photoViewAttacher.setRotatable(this.mRotatable);
            photoViewAttacher.setZoomable(this.mZoomable);
            Set<OnMatrixChangedListener> set = this.mMatrixChangeListeners;
            if (set != null && set.size() > 0) {
                for (OnMatrixChangedListener onMatrixChangedListener : this.mMatrixChangeListeners) {
                    photoViewAttacher.addOnMatrixChangeListener(onMatrixChangedListener);
                }
            }
            photoViewAttacher.setOnDoubleTapListener(this.mNewOnDoubleTapListener);
            photoViewAttacher.setOnPhotoTapListener(this.mPhotoTapListener);
            photoViewAttacher.setOnViewTapListener(this.mViewTapListener);
            photoViewAttacher.setOnViewSingleTapListener(this.mViewSingleTapListener);
            photoViewAttacher.setOnLongClickListener(this.mLongClickListener);
            photoViewAttacher.setOnScaleChangeListener(this.mScaleChangeListener);
            photoViewAttacher.setOnScaleStateChangeListener(this.mScaleStateChangeListener);
            photoViewAttacher.setOnExitListener(this.mExitListener);
            photoViewAttacher.mTileView = this.mTileView;
            return photoViewAttacher;
        }
    }
}
