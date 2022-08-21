package com.miui.gallery.editor.photo.core.imports.obsoletes;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.app.MatrixEvaluator;
import com.miui.gallery.widget.StrokeImageHelper;

/* loaded from: classes2.dex */
public class EditorView extends View {
    public Bitmap mBitmap;
    public RectF mBitmapBounds;
    public RectF mBitmapDisplayBounds;
    public Matrix mBitmapToCanvas;
    public RectF mCanvasBounds;
    public boolean mDrawBoundLine;
    public PaintFlagsDrawFilter mDrawFilter;
    public EventHandler mEventHandler;
    public Paint mPaint;
    public Plugin mPlugin;
    public Rect mRect;
    public StrokeImageHelper mStrokeImageHelper;

    /* loaded from: classes2.dex */
    public static abstract class Plugin {
        public EditorView mEditorView;
        public EventHandler mEventHandler;
        public ValueAnimator mImageAnimator;
        public float mMinBounds;
        public float mScaleFactor;
        public float mScaleSize;
        public PropertyValuesHolder mTransValues;
        public ValueAnimator.AnimatorUpdateListener mTranslateUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Plugin.this.getImageMatrix().set((Matrix) Plugin.this.mImageAnimator.getAnimatedValue());
                Plugin.this.invalidate();
            }
        };

        /* loaded from: classes2.dex */
        public interface BoundsFixCallback {
            void onDone();
        }

        public void config(Canvas canvas) {
        }

        public boolean draw(Canvas canvas) {
            return false;
        }

        public abstract void drawOverlay(Canvas canvas);

        public abstract Rect getWindowPaddingRect();

        public abstract boolean isCropEventStateIDLE();

        public void onResetMatrix() {
        }

        public void onSizeChanged(RectF rectF, RectF rectF2) {
        }

        public abstract void onStart();

        public abstract void onStop();

        public abstract boolean onTouchEvent(MotionEvent motionEvent);

        public void start() {
            onStart();
            invalidate();
        }

        public void stop() {
            onStop();
            invalidate();
        }

        public final EventHandler getEventHandler() {
            ensureStarted();
            return this.mEventHandler;
        }

        public final void invalidate() {
            ensureStarted();
            this.mEditorView.invalidate();
        }

        public final Matrix getImageMatrix() {
            ensureStarted();
            return this.mEditorView.mBitmapToCanvas;
        }

        public final RectF getBounds() {
            ensureStarted();
            return this.mEditorView.mCanvasBounds;
        }

        public final RectF getImageBounds() {
            ensureStarted();
            return this.mEditorView.mBitmapBounds;
        }

        public final RectF getImageDisplayBounds() {
            ensureStarted();
            return this.mEditorView.mBitmapDisplayBounds;
        }

        public final Context getContext() {
            return this.mEditorView.getContext();
        }

        public final Bitmap getImage() {
            return this.mEditorView.mBitmap;
        }

        public final void ensureStarted() {
            if (this.mEditorView != null) {
                return;
            }
            throw new IllegalStateException("Current plugin is not installed.");
        }

        public void setupImageAnimator(Matrix matrix, Matrix matrix2, OneShotAnimateListener oneShotAnimateListener) {
            if (this.mImageAnimator == null) {
                this.mImageAnimator = new ValueAnimator();
                PropertyValuesHolder ofObject = PropertyValuesHolder.ofObject("matrix", new MatrixEvaluator(), matrix, matrix2);
                this.mTransValues = ofObject;
                this.mImageAnimator.setObjectValues(ofObject);
                this.mImageAnimator.addUpdateListener(this.mTranslateUpdateListener);
            }
            this.mTransValues.setObjectValues(matrix, matrix2);
            this.mImageAnimator.setDuration(300L);
            this.mImageAnimator.setValues(this.mTransValues);
            if (oneShotAnimateListener != null) {
                this.mImageAnimator.addListener(oneShotAnimateListener);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x0023  */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static void resolveTranslate(android.graphics.RectF r5, android.graphics.RectF r6, android.graphics.PointF r7) {
            /*
                boolean r0 = r5.contains(r6)
                r1 = 0
                if (r0 != 0) goto L35
                float r0 = r6.top
                float r2 = r5.top
                int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r3 >= 0) goto L11
            Lf:
                float r0 = r0 - r2
                goto L1b
            L11:
                float r0 = r6.bottom
                float r2 = r5.bottom
                int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r3 <= 0) goto L1a
                goto Lf
            L1a:
                r0 = r1
            L1b:
                float r2 = r6.left
                float r3 = r5.left
                int r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
                if (r4 >= 0) goto L26
                float r1 = r2 - r3
                goto L30
            L26:
                float r6 = r6.right
                float r5 = r5.right
                int r2 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1))
                if (r2 <= 0) goto L30
                float r1 = r6 - r5
            L30:
                r7.x = r1
                r7.y = r0
                goto L39
            L35:
                r7.x = r1
                r7.y = r1
            L39:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin.resolveTranslate(android.graphics.RectF, android.graphics.RectF, android.graphics.PointF):void");
        }

        public static float resolveScale(RectF rectF, RectF rectF2) {
            float width = rectF2.width() > rectF.width() ? rectF2.width() / rectF.width() : 1.0f;
            return rectF2.height() > rectF.height() ? Math.max(width, rectF2.height() / rectF.height()) : width;
        }

        public void fixImageBounds(RectF rectF, BoundsFixCallback boundsFixCallback) {
            Matrix imageMatrix = getImageMatrix();
            RectF imageBounds = getImageBounds();
            Matrix matrix = new Matrix();
            imageMatrix.invert(matrix);
            RectF rectF2 = new RectF();
            matrix.mapRect(rectF2, rectF);
            BoundsFixListener boundsFixListener = null;
            if (!imageBounds.contains(rectF2)) {
                Matrix matrix2 = new Matrix(imageMatrix);
                Matrix matrix3 = new Matrix(imageMatrix);
                if (rectF2.width() > imageBounds.width() || rectF2.height() > imageBounds.height()) {
                    float resolveScale = resolveScale(imageBounds, rectF2);
                    matrix3.preScale(resolveScale, resolveScale, rectF2.centerX(), rectF2.centerY());
                }
                matrix3.invert(matrix);
                matrix.mapRect(rectF2, rectF);
                if (!imageBounds.contains(rectF2)) {
                    PointF pointF = new PointF();
                    resolveTranslate(getImageBounds(), rectF2, pointF);
                    matrix3.preTranslate(pointF.x, pointF.y);
                }
                if (boundsFixCallback != null) {
                    boundsFixListener = new BoundsFixListener(boundsFixCallback);
                }
                setupImageAnimator(matrix2, matrix3, boundsFixListener);
                this.mImageAnimator.start();
            } else if (this.mScaleFactor * this.mScaleSize <= this.mMinBounds) {
                if (boundsFixCallback == null) {
                    return;
                }
                boundsFixCallback.onDone();
            } else {
                Matrix matrix4 = new Matrix(imageMatrix);
                Matrix matrix5 = new Matrix(imageMatrix);
                float f = this.mMinBounds / this.mScaleSize;
                this.mScaleFactor = f;
                matrix5.preScale(f, f, rectF2.centerX(), rectF2.centerY());
                matrix5.invert(matrix);
                matrix.mapRect(rectF2, rectF);
                if (!imageBounds.contains(rectF2)) {
                    PointF pointF2 = new PointF();
                    resolveTranslate(getImageBounds(), rectF2, pointF2);
                    matrix5.preTranslate(pointF2.x, pointF2.y);
                }
                if (boundsFixCallback != null) {
                    boundsFixListener = new BoundsFixListener(boundsFixCallback);
                }
                setupImageAnimator(matrix4, matrix5, boundsFixListener);
                this.mImageAnimator.start();
            }
        }

        public boolean isCanDoSaveOperation() {
            ValueAnimator valueAnimator = this.mImageAnimator;
            return (valueAnimator == null || !valueAnimator.isStarted()) && isCropEventStateIDLE();
        }

        /* loaded from: classes2.dex */
        public static class BoundsFixListener extends OneShotAnimateListener {
            public BoundsFixCallback mCallback;

            public BoundsFixListener(BoundsFixCallback boundsFixCallback) {
                this.mCallback = boundsFixCallback;
            }

            @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                this.mCallback.onDone();
            }
        }
    }

    public EditorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public EditorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCanvasBounds = new RectF();
        this.mBitmapBounds = new RectF();
        this.mBitmapDisplayBounds = new RectF();
        this.mBitmapToCanvas = new Matrix();
        this.mPlugin = null;
        this.mRect = new Rect();
        this.mPaint = new Paint(7);
        this.mDrawFilter = new PaintFlagsDrawFilter(0, 3);
        this.mDrawBoundLine = true;
        initialize();
        setBackground(null);
    }

    public final void initialize() {
        this.mEventHandler = new EventHandler();
        this.mStrokeImageHelper = new StrokeImageHelper(getContext());
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        Bitmap bitmap;
        super.onMeasure(i, i2);
        if (View.MeasureSpec.getMode(i) == Integer.MIN_VALUE && View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            if (this.mBitmap != null) {
                float f = size;
                float width = f / bitmap.getWidth();
                float f2 = size2;
                float height = f2 / this.mBitmap.getHeight();
                if (width > height) {
                    size = (int) ((f * height) / width);
                } else {
                    size2 = (int) ((f2 * width) / height);
                }
            }
            setMeasuredDimension(size, size2);
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int i3 = paddingLeft + paddingRight;
        Bitmap bitmap2 = this.mBitmap;
        if (bitmap2 != null) {
            i3 += bitmap2.getWidth();
        }
        int i4 = paddingTop + paddingBottom;
        Bitmap bitmap3 = this.mBitmap;
        if (bitmap3 != null) {
            i4 += bitmap3.getHeight();
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(i3, getSuggestedMinimumWidth()), i, 0), View.resolveSizeAndState(Math.max(i4, getSuggestedMinimumHeight()), i2, 0));
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        Rect rect = new Rect();
        if (this.mPlugin.getWindowPaddingRect() != null) {
            rect.set(this.mPlugin.getWindowPaddingRect());
        }
        if (this.mCanvasBounds.isEmpty()) {
            this.mCanvasBounds.left = getPaddingLeft();
            this.mCanvasBounds.top = getPaddingTop() + rect.top;
            this.mCanvasBounds.right = i - getPaddingRight();
            this.mCanvasBounds.bottom = (i2 - getPaddingBottom()) - rect.bottom;
            resetMatrix();
        } else {
            RectF rectF = new RectF();
            rectF.set(this.mCanvasBounds);
            this.mCanvasBounds.left = getPaddingLeft();
            this.mCanvasBounds.top = getPaddingTop() + rect.top;
            this.mCanvasBounds.right = i - getPaddingRight();
            this.mCanvasBounds.bottom = (i2 - getPaddingBottom()) - rect.bottom;
            Plugin plugin = this.mPlugin;
            if (plugin != null) {
                plugin.onSizeChanged(rectF, this.mCanvasBounds);
            }
        }
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Bitmap bitmap;
        super.onDraw(canvas);
        int saveCount = canvas.getSaveCount();
        canvas.setDrawFilter(this.mDrawFilter);
        Plugin plugin = this.mPlugin;
        if (plugin != null) {
            plugin.config(canvas);
        }
        Plugin plugin2 = this.mPlugin;
        if ((plugin2 == null || !plugin2.draw(canvas)) && (bitmap = this.mBitmap) != null) {
            canvas.drawBitmap(bitmap, this.mBitmapToCanvas, this.mPaint);
        }
        Plugin plugin3 = this.mPlugin;
        if (plugin3 != null) {
            plugin3.drawOverlay(canvas);
        }
        if (this.mDrawBoundLine) {
            this.mStrokeImageHelper.draw(canvas, this.mBitmapBounds, this.mBitmapToCanvas);
        }
        canvas.restoreToCount(saveCount);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Plugin plugin = this.mPlugin;
        return plugin != null && plugin.onTouchEvent(motionEvent);
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        if (bitmap == null) {
            RectF rectF = this.mBitmapBounds;
            rectF.right = 0.0f;
            rectF.bottom = 0.0f;
        } else {
            this.mBitmapBounds.right = bitmap.getWidth();
            this.mBitmapBounds.bottom = bitmap.getHeight();
        }
        resetMatrix();
        invalidate();
    }

    public void install(Plugin plugin) {
        plugin.mEditorView = this;
        plugin.mEventHandler = this.mEventHandler;
        this.mPlugin = plugin;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public final void resetMatrix() {
        this.mBitmapToCanvas.setRectToRect(this.mBitmapBounds, this.mCanvasBounds, Matrix.ScaleToFit.CENTER);
        this.mBitmapDisplayBounds.set(this.mBitmapBounds);
        this.mBitmapToCanvas.mapRect(this.mBitmapDisplayBounds, this.mBitmapBounds);
        notifyMatrixChanged();
    }

    public final void notifyMatrixChanged() {
        Plugin plugin = this.mPlugin;
        if (plugin != null) {
            plugin.onResetMatrix();
        }
    }

    public void setDrawBoundLine(boolean z) {
        this.mDrawBoundLine = z;
    }
}
