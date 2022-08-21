package com.miui.gallery.editor.photo.widgets.glview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import com.miui.gallery.widget.imageview.SensitiveScaleGestureDetector;

/* loaded from: classes2.dex */
public class BitmapGestureGLView extends GLSurfaceView {
    public BitmapGestureParamsHolder mBitmapGestureParamsHolder;
    public CustomGesListener mCustomGesListener;
    public CustomScaleListener mCustomScaleListener;
    public FeatureGesListener mFeatureGestureListener;
    public float[] mGLPosition;
    public GestureDetector mGestureDetector;
    public boolean mIsShook;
    public Bitmap mOriginBitmap;
    public BitmapGestureParamsHolder.ParamsChangeListener mParamsChangeListener;
    public SensitiveScaleGestureDetector mScaleGestureDetector;
    public State mState;
    public float mTouchMinSize;
    public OnMatrixChangeListener onMatrixChangeListener;

    /* loaded from: classes2.dex */
    public interface FeatureGesListener extends ScaleGestureDetector.OnScaleGestureListener {
        void onActionUp(float f, float f2);

        boolean onDown(MotionEvent motionEvent);

        void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onSingleTapUp(MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    public interface OnMatrixChangeListener {
        void onBitmapMatrixChange();

        void onCanvasMatrixChange();
    }

    /* loaded from: classes2.dex */
    public enum State {
        NOT_DEFINE,
        SCALE_MOVE,
        BY_CHILD
    }

    public static final float convertBitmapX(float f, float f2) {
        return ((f - (f2 / 2.0f)) * 2.0f) / f2;
    }

    public static final float convertBitmapY(float f, float f2) {
        return ((-(f - (f2 / 2.0f))) * 2.0f) / f2;
    }

    public BitmapGestureGLView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mState = State.NOT_DEFINE;
        this.mCustomGesListener = new CustomGesListener();
        this.mCustomScaleListener = new CustomScaleListener();
        this.mGLPosition = new float[8];
        this.mParamsChangeListener = new BitmapGestureParamsHolder.ParamsChangeListener() { // from class: com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView.1
            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onBitmapMatrixChanged() {
                BitmapGestureGLView.this.onBitmapMatrixChange();
            }

            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onCanvasMatrixChange() {
                BitmapGestureGLView.this.onCanvasMatrixChange();
            }
        };
        init();
    }

    public final void init() {
        this.mTouchMinSize = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mGestureDetector = new GestureDetector(getContext(), this.mCustomGesListener);
        this.mScaleGestureDetector = new SensitiveScaleGestureDetector(getContext(), this.mCustomScaleListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
        this.mBitmapGestureParamsHolder = new BitmapGestureParamsHolder(this, this.mParamsChangeListener);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setZOrderOnTop(true);
        getHolder().setFormat(-2);
    }

    public void setBitmap(Bitmap bitmap) {
        this.mOriginBitmap = bitmap;
        this.mBitmapGestureParamsHolder.setBitmap(bitmap);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mBitmapGestureParamsHolder.onSizeChanged(i, i2, i3, i4);
    }

    public void setOnMatrixChangeListener(OnMatrixChangeListener onMatrixChangeListener) {
        this.onMatrixChangeListener = onMatrixChangeListener;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            this.mState = State.NOT_DEFINE;
        }
        if (motionEvent.getPointerCount() > 1) {
            this.mState = State.SCALE_MOVE;
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
        this.mBitmapGestureParamsHolder.fixMatrixWithAnim();
        FeatureGesListener featureGesListener = this.mFeatureGestureListener;
        if (featureGesListener != null) {
            featureGesListener.onActionUp(f, f2);
        }
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
            BitmapGestureGLView bitmapGestureGLView = BitmapGestureGLView.this;
            if (bitmapGestureGLView.mState == State.BY_CHILD) {
                if (bitmapGestureGLView.mFeatureGestureListener != null) {
                    return BitmapGestureGLView.this.mFeatureGestureListener.onDown(motionEvent);
                }
            } else if (motionEvent.getPointerCount() <= 1) {
                if (BitmapGestureGLView.this.mFeatureGestureListener != null) {
                    return BitmapGestureGLView.this.mFeatureGestureListener.onDown(motionEvent);
                }
            } else {
                BitmapGestureGLView.this.mState = State.SCALE_MOVE;
            }
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            BitmapGestureGLView bitmapGestureGLView = BitmapGestureGLView.this;
            if (bitmapGestureGLView.mState == State.SCALE_MOVE || bitmapGestureGLView.mFeatureGestureListener == null) {
                return true;
            }
            BitmapGestureGLView bitmapGestureGLView2 = BitmapGestureGLView.this;
            bitmapGestureGLView2.mState = State.BY_CHILD;
            bitmapGestureGLView2.mFeatureGestureListener.onSingleTapUp(motionEvent);
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            BitmapGestureGLView bitmapGestureGLView = BitmapGestureGLView.this;
            State state = bitmapGestureGLView.mState;
            State state2 = State.SCALE_MOVE;
            if (state == state2) {
                bitmapGestureGLView.mBitmapGestureParamsHolder.performTransition(-f, -f2);
            } else {
                State state3 = State.BY_CHILD;
                if (state == state3) {
                    if (bitmapGestureGLView.mFeatureGestureListener != null) {
                        BitmapGestureGLView.this.mFeatureGestureListener.onScroll(motionEvent, motionEvent2, f, f2);
                    }
                } else if (motionEvent2.getPointerCount() <= 1) {
                    if (((float) Math.hypot(motionEvent2.getX() - motionEvent.getX(), motionEvent2.getY() - motionEvent.getY())) > BitmapGestureGLView.this.mTouchMinSize) {
                        BitmapGestureGLView.this.mState = state3;
                    } else {
                        BitmapGestureGLView.this.mState = State.NOT_DEFINE;
                    }
                } else {
                    BitmapGestureGLView.this.mState = state2;
                }
            }
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public class CustomScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        public CustomScaleListener() {
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            BitmapGestureGLView bitmapGestureGLView = BitmapGestureGLView.this;
            if (bitmapGestureGLView.mState == State.BY_CHILD) {
                if (bitmapGestureGLView.mFeatureGestureListener != null) {
                    BitmapGestureGLView.this.mFeatureGestureListener.onScale(scaleGestureDetector);
                }
            } else {
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                BitmapGestureGLView bitmapGestureGLView2 = BitmapGestureGLView.this;
                bitmapGestureGLView2.mState = State.SCALE_MOVE;
                if (scaleFactor > 1.0f) {
                    float width = bitmapGestureGLView2.mBitmapGestureParamsHolder.mBitmapDisplayRect.width();
                    BitmapGestureGLView bitmapGestureGLView3 = BitmapGestureGLView.this;
                    if (width > bitmapGestureGLView3.mBitmapGestureParamsHolder.mMaxWidthScale) {
                        if (!bitmapGestureGLView3.mIsShook) {
                            LinearMotorHelper.performHapticFeedback(BitmapGestureGLView.this.getRootView(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
                            BitmapGestureGLView.this.mIsShook = true;
                        }
                        return false;
                    }
                    bitmapGestureGLView3.mIsShook = false;
                } else {
                    float width2 = bitmapGestureGLView2.mBitmapGestureParamsHolder.mBitmapDisplayRect.width();
                    BitmapGestureGLView bitmapGestureGLView4 = BitmapGestureGLView.this;
                    if (width2 < bitmapGestureGLView4.mBitmapGestureParamsHolder.mMinWidthScale) {
                        if (!bitmapGestureGLView4.mIsShook) {
                            LinearMotorHelper.performHapticFeedback(BitmapGestureGLView.this.getRootView(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
                            BitmapGestureGLView.this.mIsShook = true;
                        }
                        return false;
                    }
                    bitmapGestureGLView4.mIsShook = false;
                }
                BitmapGestureGLView.this.mBitmapGestureParamsHolder.performScale(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            }
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            BitmapGestureGLView bitmapGestureGLView = BitmapGestureGLView.this;
            if (bitmapGestureGLView.mState == State.BY_CHILD) {
                if (bitmapGestureGLView.mFeatureGestureListener == null) {
                    return true;
                }
                BitmapGestureGLView.this.mFeatureGestureListener.onScaleBegin(scaleGestureDetector);
                return true;
            }
            bitmapGestureGLView.mState = State.SCALE_MOVE;
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            BitmapGestureGLView bitmapGestureGLView = BitmapGestureGLView.this;
            if (bitmapGestureGLView.mState != State.BY_CHILD || bitmapGestureGLView.mFeatureGestureListener == null) {
                return;
            }
            BitmapGestureGLView.this.mFeatureGestureListener.onScaleEnd(scaleGestureDetector);
        }
    }

    public void convertPoint(float[] fArr) {
        for (int i = 0; i < fArr.length; i += 2) {
            fArr[i] = convertX(fArr[i]);
            int i2 = i + 1;
            fArr[i2] = convertY(fArr[i2]);
        }
    }

    public static void convertPointToBitmap(float[] fArr, float f, float f2) {
        for (int i = 0; i < fArr.length; i += 2) {
            fArr[i] = convertBitmapX(fArr[i], f);
            int i2 = i + 1;
            fArr[i2] = convertBitmapY(fArr[i2], f2);
        }
    }

    public final float convertX(float f) {
        return ((f - this.mBitmapGestureParamsHolder.mDisplayRect.centerX()) * 2.0f) / this.mBitmapGestureParamsHolder.mDisplayRect.width();
    }

    public final float convertY(float f) {
        return ((-(f - this.mBitmapGestureParamsHolder.mDisplayRect.centerY())) * 2.0f) / this.mBitmapGestureParamsHolder.mDisplayRect.height();
    }

    public static void generateVertexPositionToBitmap(RectF rectF, float[] fArr, float f, float f2) {
        float f3 = rectF.left;
        fArr[0] = f3;
        float f4 = rectF.bottom;
        fArr[1] = f4;
        float f5 = rectF.right;
        fArr[2] = f5;
        fArr[3] = f4;
        fArr[4] = f3;
        float f6 = rectF.top;
        fArr[5] = f6;
        fArr[6] = f5;
        fArr[7] = f6;
        convertPointToBitmap(fArr, f, f2);
    }

    public void generateVertexPosition(RectF rectF, float[] fArr) {
        float f = rectF.left;
        fArr[0] = f;
        float f2 = rectF.bottom;
        fArr[1] = f2;
        float f3 = rectF.right;
        fArr[2] = f3;
        fArr[3] = f2;
        fArr[4] = f;
        float f4 = rectF.top;
        fArr[5] = f4;
        fArr[6] = f3;
        fArr[7] = f4;
        convertPoint(fArr);
    }

    public void generateVertexPositionReverse(RectF rectF, float[] fArr) {
        float f = rectF.left;
        fArr[0] = f;
        float f2 = rectF.top;
        fArr[1] = f2;
        float f3 = rectF.right;
        fArr[2] = f3;
        fArr[3] = f2;
        fArr[4] = f;
        float f4 = rectF.bottom;
        fArr[5] = f4;
        fArr[6] = f3;
        fArr[7] = f4;
        convertPoint(fArr);
    }

    public void setFeatureGestureListener(FeatureGesListener featureGesListener) {
        this.mFeatureGestureListener = featureGesListener;
    }

    public float[] getGLPosition() {
        generateVertexPosition(this.mBitmapGestureParamsHolder.mBitmapDisplayRect, this.mGLPosition);
        return this.mGLPosition;
    }

    public void onBitmapMatrixChange() {
        OnMatrixChangeListener onMatrixChangeListener = this.onMatrixChangeListener;
        if (onMatrixChangeListener != null) {
            onMatrixChangeListener.onBitmapMatrixChange();
        }
    }

    public void onCanvasMatrixChange() {
        OnMatrixChangeListener onMatrixChangeListener = this.onMatrixChangeListener;
        if (onMatrixChangeListener != null) {
            onMatrixChangeListener.onCanvasMatrixChange();
        }
    }
}
