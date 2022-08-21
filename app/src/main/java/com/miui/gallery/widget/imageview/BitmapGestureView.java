package com.miui.gallery.widget.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.StrokeImageHelper;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;

/* loaded from: classes2.dex */
public class BitmapGestureView extends View {
    public BitmapGestureParamsHolder mBitmapGestureParamsHolder;
    public Paint mBitmapPaint;
    public CustomGesListener mCustomGesListener;
    public CustomScaleListener mCustomScaleListener;
    public Bitmap mDisplayBitmap;
    public Canvas mDisplayCanvas;
    public boolean mDrawOrigin;
    public FeatureGesListener mFeatureGestureListener;
    public boolean mForceHandleEventByChild;
    public GestureDetector mGestureDetector;
    public boolean mIsShook;
    public Bitmap mOriginBitmap;
    public BitmapGestureParamsHolder.ParamsChangeListener mParamsChangeListener;
    public SensitiveScaleGestureDetector mScaleGestureDetector;
    public State mState;
    public boolean mStrokeEnable;
    public StrokeImageHelper mStrokeImageHelper;
    public float mTouchMinSize;

    /* loaded from: classes2.dex */
    public interface FeatureGesListener extends ScaleGestureDetector.OnScaleGestureListener {
        void onActionUp(float f, float f2);

        default void onDefaultFeature() {
        }

        boolean onDown(MotionEvent motionEvent);

        default boolean onMultiPointDown(MotionEvent motionEvent) {
            return false;
        }

        default boolean onMultiPointMove(MotionEvent motionEvent) {
            return false;
        }

        void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onSingleTapUp(MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    public enum State {
        NOT_DEFINE,
        SCALE_MOVE,
        BY_CHILD
    }

    public void drawChild(Canvas canvas) {
    }

    public void onBitmapMatrixChanged() {
    }

    public void onCanvasMatrixChange() {
    }

    public void onClear() {
    }

    public BitmapGestureView(Context context) {
        super(context);
        this.mCustomGesListener = new CustomGesListener();
        this.mCustomScaleListener = new CustomScaleListener();
        this.mBitmapPaint = new Paint(3);
        this.mState = State.NOT_DEFINE;
        this.mDrawOrigin = false;
        this.mForceHandleEventByChild = false;
        this.mStrokeEnable = true;
        this.mParamsChangeListener = new BitmapGestureParamsHolder.ParamsChangeListener() { // from class: com.miui.gallery.widget.imageview.BitmapGestureView.1
            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onBitmapMatrixChanged() {
                BitmapGestureView.this.onBitmapMatrixChanged();
                BitmapGestureView.this.invalidate();
            }

            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onCanvasMatrixChange() {
                BitmapGestureView.this.onCanvasMatrixChange();
                BitmapGestureView.this.invalidate();
            }
        };
        init();
    }

    public BitmapGestureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCustomGesListener = new CustomGesListener();
        this.mCustomScaleListener = new CustomScaleListener();
        this.mBitmapPaint = new Paint(3);
        this.mState = State.NOT_DEFINE;
        this.mDrawOrigin = false;
        this.mForceHandleEventByChild = false;
        this.mStrokeEnable = true;
        this.mParamsChangeListener = new BitmapGestureParamsHolder.ParamsChangeListener() { // from class: com.miui.gallery.widget.imageview.BitmapGestureView.1
            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onBitmapMatrixChanged() {
                BitmapGestureView.this.onBitmapMatrixChanged();
                BitmapGestureView.this.invalidate();
            }

            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onCanvasMatrixChange() {
                BitmapGestureView.this.onCanvasMatrixChange();
                BitmapGestureView.this.invalidate();
            }
        };
        init();
    }

    public BitmapGestureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCustomGesListener = new CustomGesListener();
        this.mCustomScaleListener = new CustomScaleListener();
        this.mBitmapPaint = new Paint(3);
        this.mState = State.NOT_DEFINE;
        this.mDrawOrigin = false;
        this.mForceHandleEventByChild = false;
        this.mStrokeEnable = true;
        this.mParamsChangeListener = new BitmapGestureParamsHolder.ParamsChangeListener() { // from class: com.miui.gallery.widget.imageview.BitmapGestureView.1
            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onBitmapMatrixChanged() {
                BitmapGestureView.this.onBitmapMatrixChanged();
                BitmapGestureView.this.invalidate();
            }

            @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
            public void onCanvasMatrixChange() {
                BitmapGestureView.this.onCanvasMatrixChange();
                BitmapGestureView.this.invalidate();
            }
        };
        init();
    }

    private void init() {
        this.mBitmapGestureParamsHolder = new BitmapGestureParamsHolder(this, this.mParamsChangeListener);
        this.mGestureDetector = new GestureDetector(getContext(), this.mCustomGesListener);
        this.mScaleGestureDetector = new SensitiveScaleGestureDetector(getContext(), this.mCustomScaleListener);
        this.mGestureDetector.setIsLongpressEnabled(false);
        this.mTouchMinSize = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mStrokeImageHelper = new StrokeImageHelper(getContext());
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        this.mOriginBitmap = bitmap;
        this.mDisplayBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.mBitmapGestureParamsHolder.setBitmap(bitmap);
    }

    public void release() {
        this.mDisplayCanvas = null;
        Bitmap bitmap = this.mDisplayBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        this.mDisplayBitmap.recycle();
        this.mDisplayBitmap = null;
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mBitmapGestureParamsHolder.onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        Bitmap bitmap = this.mDisplayBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        canvas.save();
        canvas.concat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
        canvas.drawBitmap(this.mDrawOrigin ? this.mOriginBitmap : this.mDisplayBitmap, this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix, this.mBitmapPaint);
        canvas.restore();
        drawChild(canvas);
        if (!this.mStrokeEnable) {
            return;
        }
        this.mStrokeImageHelper.draw(canvas, this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
    }

    public void setStrokeEnable(boolean z) {
        this.mStrokeEnable = z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0047, code lost:
        if (r0 != 3) goto L25;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r4) {
        /*
            r3 = this;
            android.graphics.Bitmap r0 = r3.mDisplayBitmap
            if (r0 != 0) goto L6
            r4 = 0
            return r4
        L6:
            int r0 = r4.getAction()
            if (r0 != 0) goto L10
            com.miui.gallery.widget.imageview.BitmapGestureView$State r0 = com.miui.gallery.widget.imageview.BitmapGestureView.State.NOT_DEFINE
            r3.mState = r0
        L10:
            int r0 = r4.getAction()
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 5
            if (r0 != r1) goto L20
            com.miui.gallery.widget.imageview.BitmapGestureView$FeatureGesListener r0 = r3.mFeatureGestureListener
            if (r0 == 0) goto L20
            r0.onMultiPointDown(r4)
        L20:
            int r0 = r4.getPointerCount()
            r1 = 1
            if (r0 <= r1) goto L2b
            com.miui.gallery.widget.imageview.BitmapGestureView$State r0 = com.miui.gallery.widget.imageview.BitmapGestureView.State.SCALE_MOVE
            r3.mState = r0
        L2b:
            boolean r0 = r3.mForceHandleEventByChild
            if (r0 == 0) goto L33
            com.miui.gallery.widget.imageview.BitmapGestureView$State r0 = com.miui.gallery.widget.imageview.BitmapGestureView.State.BY_CHILD
            r3.mState = r0
        L33:
            com.miui.gallery.widget.imageview.SensitiveScaleGestureDetector r0 = r3.mScaleGestureDetector
            r0.onTouchEvent(r4)
            android.view.GestureDetector r0 = r3.mGestureDetector
            r0.onTouchEvent(r4)
            int r0 = r4.getAction()
            if (r0 == r1) goto L58
            r2 = 2
            if (r0 == r2) goto L4a
            r2 = 3
            if (r0 == r2) goto L58
            goto L63
        L4a:
            int r0 = r4.getPointerCount()
            if (r0 <= r1) goto L63
            com.miui.gallery.widget.imageview.BitmapGestureView$FeatureGesListener r0 = r3.mFeatureGestureListener
            if (r0 == 0) goto L63
            r0.onMultiPointMove(r4)
            goto L63
        L58:
            float r0 = r4.getX()
            float r4 = r4.getY()
            r3.onActionUp(r0, r4)
        L63:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.imageview.BitmapGestureView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void onActionUp(float f, float f2) {
        this.mBitmapGestureParamsHolder.fixMatrixWithAnim();
        FeatureGesListener featureGesListener = this.mFeatureGestureListener;
        if (featureGesListener != null) {
            featureGesListener.onActionUp(f, f2);
        }
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
            BitmapGestureView bitmapGestureView = BitmapGestureView.this;
            if (bitmapGestureView.mState == State.BY_CHILD) {
                if (bitmapGestureView.mFeatureGestureListener != null) {
                    return BitmapGestureView.this.mFeatureGestureListener.onDown(motionEvent);
                }
            } else if (motionEvent.getPointerCount() <= 1) {
                if (BitmapGestureView.this.mFeatureGestureListener != null) {
                    return BitmapGestureView.this.mFeatureGestureListener.onDown(motionEvent);
                }
            } else {
                BitmapGestureView.this.mState = State.SCALE_MOVE;
            }
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            BitmapGestureView bitmapGestureView = BitmapGestureView.this;
            if (bitmapGestureView.mState == State.SCALE_MOVE || bitmapGestureView.mFeatureGestureListener == null) {
                return true;
            }
            BitmapGestureView bitmapGestureView2 = BitmapGestureView.this;
            bitmapGestureView2.mState = State.BY_CHILD;
            bitmapGestureView2.mFeatureGestureListener.onSingleTapUp(motionEvent);
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            BitmapGestureView bitmapGestureView = BitmapGestureView.this;
            State state = bitmapGestureView.mState;
            State state2 = State.SCALE_MOVE;
            if (state == state2) {
                bitmapGestureView.mBitmapGestureParamsHolder.performTransition(-f, -f2);
            } else {
                State state3 = State.BY_CHILD;
                if (state == state3) {
                    if (bitmapGestureView.mFeatureGestureListener != null) {
                        BitmapGestureView.this.mFeatureGestureListener.onScroll(motionEvent, motionEvent2, f, f2);
                    }
                } else if (motionEvent2.getPointerCount() <= 1) {
                    if (((float) Math.hypot(motionEvent2.getX() - motionEvent.getX(), motionEvent2.getY() - motionEvent.getY())) > BitmapGestureView.this.mTouchMinSize) {
                        BitmapGestureView.this.mState = state3;
                    } else {
                        BitmapGestureView.this.mState = State.NOT_DEFINE;
                    }
                } else {
                    BitmapGestureView.this.mState = state2;
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

    public int getRectOverScrollStatusInBitmap(RectF rectF) {
        return getRectOverScrollStatusInBitmap(rectF, 0.5f);
    }

    public int getRectOverScrollStatusInBitmap(RectF rectF, float f) {
        int i;
        float width = rectF.width() * f;
        float height = rectF.height() * f;
        if (rectF.left - 0.0f < (-width)) {
            i = 8;
        } else {
            i = rectF.right > this.mBitmapGestureParamsHolder.mBitmapRect.right + width ? 4 : 0;
        }
        return rectF.top - 0.0f < (-height) ? i | 2 : rectF.bottom > this.mBitmapGestureParamsHolder.mBitmapRect.bottom + height ? i | 1 : i;
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

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            BitmapGestureView bitmapGestureView = BitmapGestureView.this;
            if (bitmapGestureView.mState == State.BY_CHILD) {
                if (bitmapGestureView.mFeatureGestureListener != null) {
                    BitmapGestureView.this.mFeatureGestureListener.onScale(scaleGestureDetector);
                }
            } else {
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                BitmapGestureView bitmapGestureView2 = BitmapGestureView.this;
                bitmapGestureView2.mState = State.SCALE_MOVE;
                if (scaleFactor > 1.0f) {
                    float width = bitmapGestureView2.mBitmapGestureParamsHolder.mBitmapDisplayRect.width();
                    BitmapGestureView bitmapGestureView3 = BitmapGestureView.this;
                    if (width > bitmapGestureView3.mBitmapGestureParamsHolder.mMaxWidthScale) {
                        if (!bitmapGestureView3.mIsShook) {
                            LinearMotorHelper.performHapticFeedback(BitmapGestureView.this.getRootView(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
                            BitmapGestureView.this.mIsShook = true;
                        }
                        return false;
                    }
                    bitmapGestureView3.mIsShook = false;
                } else {
                    float width2 = bitmapGestureView2.mBitmapGestureParamsHolder.mBitmapDisplayRect.width();
                    BitmapGestureView bitmapGestureView4 = BitmapGestureView.this;
                    if (width2 < bitmapGestureView4.mBitmapGestureParamsHolder.mMinWidthScale) {
                        if (!bitmapGestureView4.mIsShook) {
                            LinearMotorHelper.performHapticFeedback(BitmapGestureView.this.getRootView(), LinearMotorHelper.HAPTIC_MESH_NORMAL);
                            BitmapGestureView.this.mIsShook = true;
                        }
                        return false;
                    }
                    bitmapGestureView4.mIsShook = false;
                }
                BitmapGestureView.this.mBitmapGestureParamsHolder.performScale(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            }
            if (BitmapGestureView.this.mFeatureGestureListener != null) {
                BitmapGestureView.this.mFeatureGestureListener.onDefaultFeature();
            }
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            BitmapGestureView bitmapGestureView = BitmapGestureView.this;
            if (bitmapGestureView.mState == State.BY_CHILD) {
                if (bitmapGestureView.mFeatureGestureListener == null) {
                    return true;
                }
                BitmapGestureView.this.mFeatureGestureListener.onScaleBegin(scaleGestureDetector);
                return true;
            }
            bitmapGestureView.mState = State.SCALE_MOVE;
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            BitmapGestureView bitmapGestureView = BitmapGestureView.this;
            if (bitmapGestureView.mState != State.BY_CHILD || bitmapGestureView.mFeatureGestureListener == null) {
                return;
            }
            BitmapGestureView.this.mFeatureGestureListener.onScaleEnd(scaleGestureDetector);
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

    public void drawOrigin(boolean z) {
        this.mDrawOrigin = z;
        invalidate();
    }
}
