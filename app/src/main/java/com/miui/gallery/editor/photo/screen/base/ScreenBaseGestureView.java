package com.miui.gallery.editor.photo.screen.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;

/* loaded from: classes2.dex */
public abstract class ScreenBaseGestureView extends View implements BitmapGestureParamsHolder.ParamsChangeListener {
    public BitmapGestureParamsHolder mBitmapGestureParamsHolder;
    public Paint mBitmapPaint;
    public FeatureGesListener mCropGestureListener;
    public FeatureGesListener mCurrentGestureListener;
    public CustomGesListener mCustomGesListener;
    public CustomScaleListener mCustomScaleListener;
    public Bitmap mDisplayBitmap;
    public Canvas mDisplayCanvas;
    public FeatureGesListener mFeatureGestureListener;
    public boolean mForceHandleEventByChild;
    public GestureDetector mGestureDetector;
    public boolean mIsCropEnable;
    public Bitmap mOriginBitmap;
    public ScaleGestureDetector mScaleGestureDetector;
    public State mState;
    public float mTouchMinSize;

    /* loaded from: classes2.dex */
    public interface FeatureGesListener extends ScaleGestureDetector.OnScaleGestureListener {
        void onActionUp(float f, float f2);

        boolean onDown(MotionEvent motionEvent);

        void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onSingleTapUp(MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    public enum State {
        NOT_DEFINE,
        SCALE_MOVE,
        BY_CHILD
    }

    public abstract void onActionUp();

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
    }

    public ScreenBaseGestureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCustomGesListener = new CustomGesListener();
        this.mCustomScaleListener = new CustomScaleListener();
        this.mBitmapPaint = new Paint(3);
        this.mState = State.NOT_DEFINE;
        this.mForceHandleEventByChild = false;
        this.mIsCropEnable = true;
        init();
    }

    private void init() {
        this.mBitmapGestureParamsHolder = new BitmapGestureParamsHolder(this, this);
        this.mGestureDetector = new GestureDetector(getContext(), this.mCustomGesListener);
        this.mScaleGestureDetector = new ScaleGestureDetector(getContext(), this.mCustomScaleListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
        this.mTouchMinSize = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        this.mOriginBitmap = bitmap;
        this.mDisplayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.mDisplayCanvas = new Canvas(this.mDisplayBitmap);
        refreshDisplayCanvas();
        this.mBitmapGestureParamsHolder.setBitmap(bitmap);
    }

    public void refreshDisplayCanvas() {
        this.mDisplayCanvas.drawBitmap(this.mOriginBitmap, 0.0f, 0.0f, this.mBitmapPaint);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mBitmapGestureParamsHolder.onSizeChanged(i, i2, i3, i4, false, false);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mDisplayBitmap == null) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            this.mState = State.NOT_DEFINE;
        }
        if (motionEvent.getPointerCount() > 1) {
            this.mState = State.SCALE_MOVE;
        }
        if (this.mForceHandleEventByChild) {
            this.mState = State.BY_CHILD;
        }
        this.mScaleGestureDetector.onTouchEvent(motionEvent);
        this.mGestureDetector.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        if (action == 1 || action == 3) {
            onActionUp(motionEvent.getX(), motionEvent.getY());
        }
        return true;
    }

    public final void onActionUp(float f, float f2) {
        FeatureGesListener featureGesListener = this.mCropGestureListener;
        if (featureGesListener != null) {
            featureGesListener.onActionUp(f, f2);
        } else {
            this.mBitmapGestureParamsHolder.fixMatrixWithAnim();
        }
        FeatureGesListener featureGesListener2 = this.mCurrentGestureListener;
        if (featureGesListener2 != null && featureGesListener2 != this.mCropGestureListener) {
            featureGesListener2.onActionUp(f, f2);
        }
        this.mState = State.NOT_DEFINE;
        onActionUp();
        invalidate();
    }

    /* loaded from: classes2.dex */
    public class CustomGesListener implements GestureDetector.OnGestureListener {
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

        public CustomGesListener() {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            ScreenBaseGestureView screenBaseGestureView = ScreenBaseGestureView.this;
            if (screenBaseGestureView.mState == State.BY_CHILD) {
                if (screenBaseGestureView.mCropGestureListener == null || !ScreenBaseGestureView.this.mIsCropEnable || !ScreenBaseGestureView.this.mCropGestureListener.onDown(motionEvent)) {
                    if (ScreenBaseGestureView.this.mFeatureGestureListener != null) {
                        ScreenBaseGestureView screenBaseGestureView2 = ScreenBaseGestureView.this;
                        screenBaseGestureView2.mCurrentGestureListener = screenBaseGestureView2.mFeatureGestureListener;
                        return ScreenBaseGestureView.this.mFeatureGestureListener.onDown(motionEvent);
                    }
                } else {
                    ScreenBaseGestureView screenBaseGestureView3 = ScreenBaseGestureView.this;
                    screenBaseGestureView3.mCurrentGestureListener = screenBaseGestureView3.mCropGestureListener;
                }
            } else if (motionEvent.getPointerCount() <= 1) {
                if (ScreenBaseGestureView.this.mCropGestureListener == null || !ScreenBaseGestureView.this.mIsCropEnable || !ScreenBaseGestureView.this.mCropGestureListener.onDown(motionEvent)) {
                    if (ScreenBaseGestureView.this.mFeatureGestureListener != null) {
                        ScreenBaseGestureView screenBaseGestureView4 = ScreenBaseGestureView.this;
                        screenBaseGestureView4.mCurrentGestureListener = screenBaseGestureView4.mFeatureGestureListener;
                        return ScreenBaseGestureView.this.mFeatureGestureListener.onDown(motionEvent);
                    }
                } else {
                    ScreenBaseGestureView screenBaseGestureView5 = ScreenBaseGestureView.this;
                    screenBaseGestureView5.mCurrentGestureListener = screenBaseGestureView5.mCropGestureListener;
                }
            } else {
                ScreenBaseGestureView.this.mState = State.SCALE_MOVE;
            }
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            ScreenBaseGestureView screenBaseGestureView = ScreenBaseGestureView.this;
            if (screenBaseGestureView.mState == State.SCALE_MOVE || screenBaseGestureView.mCurrentGestureListener == null) {
                return true;
            }
            ScreenBaseGestureView screenBaseGestureView2 = ScreenBaseGestureView.this;
            screenBaseGestureView2.mState = State.BY_CHILD;
            screenBaseGestureView2.mCurrentGestureListener.onSingleTapUp(motionEvent);
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            ScreenBaseGestureView screenBaseGestureView = ScreenBaseGestureView.this;
            State state = screenBaseGestureView.mState;
            State state2 = State.SCALE_MOVE;
            if (state == state2) {
                screenBaseGestureView.getBitmapGestureParamsHolder().mCanvasMatrix.postTranslate(-f, -f2);
                ScreenBaseGestureView.this.getBitmapGestureParamsHolder().performCanvasMatrixChange();
            } else {
                State state3 = State.BY_CHILD;
                if (state == state3) {
                    if (screenBaseGestureView.mCurrentGestureListener != null) {
                        ScreenBaseGestureView.this.mCurrentGestureListener.onScroll(motionEvent, motionEvent2, f, f2);
                    }
                } else if (motionEvent2.getPointerCount() <= 1) {
                    if (((float) Math.hypot(motionEvent2.getX() - motionEvent.getX(), motionEvent2.getY() - motionEvent.getY())) > ScreenBaseGestureView.this.mTouchMinSize) {
                        ScreenBaseGestureView.this.mState = state3;
                    } else {
                        ScreenBaseGestureView.this.mState = State.NOT_DEFINE;
                    }
                } else {
                    ScreenBaseGestureView.this.mState = state2;
                }
            }
            return true;
        }
    }

    public void convertPointToViewPortCoordinate(MotionEvent motionEvent, float[] fArr) {
        fArr[0] = motionEvent.getX();
        fArr[1] = motionEvent.getY();
        convertPointToViewPortCoordinate(fArr);
    }

    public void convertPointToViewPortCoordinate(float[] fArr) {
        this.mBitmapGestureParamsHolder.convertPointToViewPortCoordinate(fArr);
    }

    public void convertPointToBitmapCoordinate(MotionEvent motionEvent, float[] fArr) {
        fArr[0] = motionEvent.getX();
        fArr[1] = motionEvent.getY();
        convertPointToBitmapCoordinate(fArr);
    }

    public void convertPointToBitmapCoordinate(float[] fArr) {
        this.mBitmapGestureParamsHolder.convertPointToBitmapCoordinate(fArr);
    }

    public float convertDistanceX(float f) {
        return this.mBitmapGestureParamsHolder.convertDistanceX(f);
    }

    public float convertDistanceY(float f) {
        return this.mBitmapGestureParamsHolder.convertDistanceY(f);
    }

    public float convertDistanceInBitmap(float f) {
        return this.mBitmapGestureParamsHolder.convertDistanceInBitmap(f);
    }

    public int getRectOverScrollStatus(RectF rectF) {
        return getRectOverScrollStatus(rectF, 0.5f);
    }

    public int getRectOverScrollStatus(RectF rectF, float f) {
        int i;
        BitmapGestureParamsHolder bitmapGestureParamsHolder = this.mBitmapGestureParamsHolder;
        RectF rectF2 = bitmapGestureParamsHolder.mBitmapDisplayRect;
        RectF rectF3 = bitmapGestureParamsHolder.mDisplayRect;
        float width = rectF.width() * f;
        float height = rectF.height() * f;
        float max = Math.max(rectF2.left, rectF3.left);
        float max2 = Math.max(rectF2.top, rectF3.top);
        float min = Math.min(rectF2.right, rectF3.right);
        float min2 = Math.min(rectF2.bottom, rectF3.bottom);
        if (rectF.left - max < (-width)) {
            i = 8;
        } else {
            i = rectF.right > min + width ? 4 : 0;
        }
        return rectF.top - max2 < (-height) ? i | 2 : rectF.bottom > min2 + height ? i | 1 : i;
    }

    /* loaded from: classes2.dex */
    public class CustomScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        public CustomScaleListener() {
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            ScreenBaseGestureView screenBaseGestureView = ScreenBaseGestureView.this;
            if (screenBaseGestureView.mState == State.BY_CHILD) {
                if (screenBaseGestureView.mCurrentGestureListener == null) {
                    return true;
                }
                ScreenBaseGestureView.this.mCurrentGestureListener.onScale(scaleGestureDetector);
                return true;
            }
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            ScreenBaseGestureView screenBaseGestureView2 = ScreenBaseGestureView.this;
            screenBaseGestureView2.mState = State.SCALE_MOVE;
            if (screenBaseGestureView2.mCropGestureListener == null && scaleFactor > 1.0f && ScreenBaseGestureView.this.mBitmapGestureParamsHolder.mBitmapDisplayRect.width() > ScreenBaseGestureView.this.mBitmapGestureParamsHolder.mMaxWidthScale) {
                return false;
            }
            ScreenBaseGestureView.this.mBitmapGestureParamsHolder.performScale(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ScreenBaseGestureView screenBaseGestureView = ScreenBaseGestureView.this;
            if (screenBaseGestureView.mState == State.BY_CHILD) {
                if (screenBaseGestureView.mCurrentGestureListener == null) {
                    return true;
                }
                ScreenBaseGestureView.this.mCurrentGestureListener.onScaleBegin(scaleGestureDetector);
                return true;
            }
            screenBaseGestureView.mState = State.SCALE_MOVE;
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            ScreenBaseGestureView screenBaseGestureView = ScreenBaseGestureView.this;
            if (screenBaseGestureView.mState != State.BY_CHILD || screenBaseGestureView.mCurrentGestureListener == null) {
                return;
            }
            ScreenBaseGestureView.this.mCurrentGestureListener.onScaleEnd(scaleGestureDetector);
        }
    }

    public void enableChildHandleMode() {
        this.mForceHandleEventByChild = true;
    }

    public void disableChildHandleMode() {
        this.mForceHandleEventByChild = false;
    }

    public void setFeatureGestureListener(FeatureGesListener featureGesListener) {
        this.mFeatureGestureListener = featureGesListener;
    }

    public void setCropGestureListener(FeatureGesListener featureGesListener) {
        this.mCropGestureListener = featureGesListener;
    }

    public BitmapGestureParamsHolder getBitmapGestureParamsHolder() {
        return this.mBitmapGestureParamsHolder;
    }

    public Bitmap getOriginBitmap() {
        return this.mOriginBitmap;
    }

    public void setCropEnable(boolean z) {
        this.mIsCropEnable = z;
    }
}
