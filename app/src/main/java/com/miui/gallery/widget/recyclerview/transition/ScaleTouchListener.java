package com.miui.gallery.widget.recyclerview.transition;

import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class ScaleTouchListener implements RecyclerView.OnItemTouchListener, ScaleGestureDetector.OnScaleGestureListener {
    public final ScaleGestureDetector mDetector;
    public final WeakReference<RecyclerView> mRecycler;
    public final OnScaleListener mScaleListener;
    public int mSupportedZoomFlag = 0;
    public int mCurZoomType = 0;
    public boolean mInterceptTouch = false;
    public boolean mUnsupportedZoom = false;

    /* loaded from: classes3.dex */
    public interface OnScaleListener {
        boolean onScale(RecyclerView recyclerView, int i, float f);

        boolean onScaleBegin(RecyclerView recyclerView, int i, float f, float f2, float f3);

        void onScaleEnd(RecyclerView recyclerView, int i, float f);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public ScaleTouchListener(RecyclerView recyclerView, OnScaleListener onScaleListener) {
        this.mRecycler = new WeakReference<>(recyclerView);
        this.mScaleListener = onScaleListener;
        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(recyclerView.getContext(), this);
        this.mDetector = scaleGestureDetector;
        scaleGestureDetector.setScaleMinSpan((int) (ViewConfiguration.get(recyclerView.getContext()).getScaledTouchSlop() * 1.2f));
    }

    public void updateSupportedZoomFlag(int i) {
        this.mSupportedZoomFlag = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            disallowInterceptTouchEvent(false);
            this.mInterceptTouch = false;
            this.mUnsupportedZoom = false;
        } else if (actionMasked == 5) {
            DefaultLogger.d("ScaleTouchListener", "secondary pointer down, disallow intercept touch event");
            this.mInterceptTouch = true;
            disallowInterceptTouchEvent(true);
        } else if (actionMasked == 6 && motionEvent.getPointerCount() <= 2 && this.mUnsupportedZoom) {
            disallowInterceptTouchEvent(false);
        }
        this.mDetector.onTouchEvent(motionEvent);
        return this.mInterceptTouch;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mInterceptTouch = false;
            this.mUnsupportedZoom = false;
        } else if (actionMasked == 5) {
            DefaultLogger.d("ScaleTouchListener", "secondary pointer down, disallow intercept touch event");
            this.mInterceptTouch = true;
            disallowInterceptTouchEvent(true);
        } else if (actionMasked == 6 && motionEvent.getPointerCount() <= 2 && this.mUnsupportedZoom) {
            disallowInterceptTouchEvent(false);
        }
        this.mDetector.onTouchEvent(motionEvent);
    }

    public final boolean isViewDetached() {
        if (this.mRecycler.get() == null) {
            DefaultLogger.w("ScaleTouchListener", "RecyclerView is detached");
            this.mInterceptTouch = false;
            return true;
        }
        return false;
    }

    public final boolean supportZoomIn() {
        return (this.mSupportedZoomFlag & 1) == 1;
    }

    public final boolean supportZoomOut() {
        return (this.mSupportedZoomFlag & 2) == 2;
    }

    @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        boolean z = false;
        if (this.mUnsupportedZoom) {
            DefaultLogger.d("ScaleTouchListener", "onScale, unsupported zoom %d", Integer.valueOf(this.mCurZoomType));
            return false;
        }
        float scaleFactor = scaleGestureDetector.getScaleFactor();
        if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
            DefaultLogger.e("ScaleTouchListener", "illegal scale factor %s", Float.valueOf(scaleFactor));
            return false;
        }
        DefaultLogger.d("ScaleTouchListener", "onScale, scaleFactor %s", Float.valueOf(scaleFactor));
        if (this.mCurZoomType == 0) {
            int i = (scaleFactor > 1.0f ? 1 : (scaleFactor == 1.0f ? 0 : -1));
            if (i > 0 || scaleFactor < 1.0f) {
                if (i > 0 && supportZoomIn()) {
                    this.mCurZoomType = 1;
                    z = true;
                }
                if (scaleFactor < 1.0f && supportZoomOut()) {
                    this.mCurZoomType = 2;
                    z = true;
                }
                if (z) {
                    OnScaleListener onScaleListener = this.mScaleListener;
                    if (onScaleListener != null) {
                        this.mInterceptTouch = onScaleListener.onScaleBegin(this.mRecycler.get(), this.mCurZoomType, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                    }
                } else {
                    this.mUnsupportedZoom = true;
                }
            }
        } else if (isViewDetached()) {
            return false;
        } else {
            OnScaleListener onScaleListener2 = this.mScaleListener;
            if (onScaleListener2 != null) {
                this.mInterceptTouch = onScaleListener2.onScale(this.mRecycler.get(), this.mCurZoomType, scaleFactor);
            }
        }
        return this.mInterceptTouch;
    }

    @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        this.mInterceptTouch = true;
        this.mCurZoomType = 0;
        disallowInterceptTouchEvent(true);
        DefaultLogger.d("ScaleTouchListener", "onScaleBegin, scaleFactor %s", Float.valueOf(scaleGestureDetector.getScaleFactor()));
        return true;
    }

    @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        if (isViewDetached()) {
            return;
        }
        if (this.mUnsupportedZoom) {
            DefaultLogger.d("ScaleTouchListener", "onScaleEnd, unsupported zoom %d", Integer.valueOf(this.mCurZoomType));
            return;
        }
        float scaleFactor = scaleGestureDetector.getScaleFactor();
        DefaultLogger.d("ScaleTouchListener", "onScaleEnd, scaleFactor %s", Float.valueOf(scaleFactor));
        OnScaleListener onScaleListener = this.mScaleListener;
        if (onScaleListener == null) {
            return;
        }
        onScaleListener.onScaleEnd(this.mRecycler.get(), this.mCurZoomType, scaleFactor);
    }

    public final void disallowInterceptTouchEvent(boolean z) {
        RecyclerView recyclerView = this.mRecycler.get();
        if (recyclerView == null) {
            DefaultLogger.w("ScaleTouchListener", "the recycler view has recycled");
            return;
        }
        ViewParent parent = recyclerView.getParent();
        if (parent == null) {
            DefaultLogger.w("ScaleTouchListener", "parent of the recycler view is null");
            return;
        }
        parent.requestDisallowInterceptTouchEvent(z);
        ViewUtils.requestParentDisallowInterceptTouchEvent(recyclerView, z);
    }
}
