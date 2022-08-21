package com.miui.gallery.magic.widget.idphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.widget.detector.TranslateDetector;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import com.miui.gallery.widget.imageview.SensitiveScaleGestureDetector;

/* loaded from: classes2.dex */
public class IDPhotoView extends View {
    public final float MAX_SCALE;
    public final float MIN_SCALE;
    public final String TAG;
    public boolean isFirstSizeChanged;
    public Rect mBgBounds;
    public Rect mBgPadding;
    public Bitmap mBitmap;
    public RectF mBitmapBounds;
    public RectF mBitmapDisplayBounds;
    public Matrix mBitmapToCanvas;
    public RectF mCanvasBounds;
    public Rect mClipBounds;
    public RectF mCropArea;
    public int mCurrentBgColor;
    public float mCurrentScale;
    public PaintFlagsDrawFilter mDrawFilter;
    public EventState mEventState;
    public boolean mFirstIn;
    public final int mMaskColor;
    public Paint mMaskPaint;
    public Matrix mOriBitmapToOriStyleBitmap;
    public RectF mOriStyleBitmapBounds;
    public Matrix mOriStyleBitmapToCanvas;
    public Paint mPaint;
    public ScaleGestureDetector mScaleGestureDetector;
    public RectF mTouchBounds;
    public TranslateDetector mTranslateDetector;
    public Matrix mUnmodifiedMatrix;

    /* loaded from: classes2.dex */
    public enum EventState {
        IDLE,
        SCALE,
        TRANSLATE,
        SKIP
    }

    public static /* synthetic */ float access$332(IDPhotoView iDPhotoView, float f) {
        float f2 = iDPhotoView.mCurrentScale * f;
        iDPhotoView.mCurrentScale = f2;
        return f2;
    }

    public IDPhotoView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public IDPhotoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.TAG = "IDPhotoView";
        this.MAX_SCALE = 2.0f;
        this.MIN_SCALE = 0.5f;
        this.mMaskColor = Color.parseColor("#ff000000");
        this.mCurrentScale = 1.0f;
        this.mCurrentBgColor = -1;
        this.isFirstSizeChanged = false;
        this.mFirstIn = true;
        this.mCanvasBounds = new RectF();
        this.mBitmapBounds = new RectF();
        this.mOriStyleBitmapBounds = new RectF();
        this.mBitmapDisplayBounds = new RectF();
        this.mBitmapToCanvas = new Matrix();
        this.mOriStyleBitmapToCanvas = new Matrix();
        this.mOriBitmapToOriStyleBitmap = new Matrix();
        this.mPaint = new Paint(7);
        this.mDrawFilter = new PaintFlagsDrawFilter(0, 3);
        this.mCropArea = new RectF();
        this.mUnmodifiedMatrix = new Matrix();
        this.mBgPadding = new Rect();
        this.mBgBounds = new Rect();
        this.mClipBounds = new Rect();
        this.mEventState = EventState.IDLE;
        this.mTouchBounds = new RectF();
        this.mMaskPaint = new Paint();
        initialize();
    }

    public final void initialize() {
        this.mTranslateDetector = new TranslateDetector(new TranslateListener(this, null));
        this.mScaleGestureDetector = new SensitiveScaleGestureDetector(getContext(), new ScaleListener(this, null));
        this.mMaskPaint.setStyle(Paint.Style.FILL);
        setBackgroundColor(this.mCurrentBgColor);
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
        if (getWindowPaddingRect() != null) {
            rect.set(getWindowPaddingRect());
        }
        this.mCanvasBounds.left = getPaddingLeft();
        this.mCanvasBounds.top = getPaddingTop() + rect.top;
        this.mCanvasBounds.right = i - getPaddingRight();
        this.mCanvasBounds.bottom = (i2 - getPaddingBottom()) - rect.bottom;
        this.mOriStyleBitmapToCanvas.setRectToRect(this.mOriStyleBitmapBounds, this.mCanvasBounds, Matrix.ScaleToFit.CENTER);
        this.mOriStyleBitmapToCanvas.mapRect(this.mCropArea, this.mOriStyleBitmapBounds);
        resetMatrix();
        if (!this.isFirstSizeChanged) {
            this.isFirstSizeChanged = true;
            onStart();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.getSaveCount();
        canvas.setDrawFilter(this.mDrawFilter);
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, this.mBitmapToCanvas, this.mPaint);
        }
        drawOverlay(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            if (!this.mCropArea.contains(motionEvent.getX(), motionEvent.getY())) {
                return false;
            }
            if (!this.mTouchBounds.contains(motionEvent.getX(), motionEvent.getY())) {
                if (this.mEventState != EventState.IDLE) {
                    return false;
                }
                this.mEventState = EventState.SKIP;
            } else {
                this.mEventState = EventState.IDLE;
            }
        } else if (this.mEventState == EventState.SKIP && this.mTouchBounds.contains(motionEvent.getX(), motionEvent.getY())) {
            this.mEventState = EventState.IDLE;
            motionEvent.setAction(0);
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$magic$widget$idphoto$IDPhotoView$EventState[this.mEventState.ordinal()];
        if (i != 1) {
            if (i == 2) {
                if (motionEvent.getPointerCount() >= 2) {
                    this.mScaleGestureDetector.onTouchEvent(motionEvent);
                }
                this.mTranslateDetector.onTouchEvent(motionEvent);
            } else if (i != 3 && i != 4) {
                return false;
            } else {
                this.mScaleGestureDetector.onTouchEvent(motionEvent);
                this.mTranslateDetector.onTouchEvent(motionEvent);
            }
        }
        return true;
    }

    /* renamed from: com.miui.gallery.magic.widget.idphoto.IDPhotoView$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$magic$widget$idphoto$IDPhotoView$EventState;

        static {
            int[] iArr = new int[EventState.values().length];
            $SwitchMap$com$miui$gallery$magic$widget$idphoto$IDPhotoView$EventState = iArr;
            try {
                iArr[EventState.SKIP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$magic$widget$idphoto$IDPhotoView$EventState[EventState.TRANSLATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$magic$widget$idphoto$IDPhotoView$EventState[EventState.SCALE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$magic$widget$idphoto$IDPhotoView$EventState[EventState.IDLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public final void resetMatrix() {
        this.mOriStyleBitmapToCanvas.setRectToRect(this.mOriStyleBitmapBounds, this.mCanvasBounds, Matrix.ScaleToFit.CENTER);
        this.mOriStyleBitmapToCanvas.mapRect(this.mCropArea, this.mOriStyleBitmapBounds);
        this.mBitmapToCanvas.set(this.mOriBitmapToOriStyleBitmap);
        this.mBitmapToCanvas.postConcat(this.mOriStyleBitmapToCanvas);
        this.mBitmapDisplayBounds.set(this.mBitmapBounds);
        this.mBitmapToCanvas.mapRect(this.mBitmapDisplayBounds, this.mBitmapBounds);
        this.mUnmodifiedMatrix.set(this.mOriStyleBitmapToCanvas);
    }

    public final void resetCurrentScale() {
        this.mCurrentScale = 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RectF getCroppedSize() {
        return this.mCropArea;
    }

    public final void onStart() {
        if (this.mFirstIn) {
            this.mFirstIn = false;
            this.mOriStyleBitmapToCanvas.mapRect(this.mCropArea, this.mOriStyleBitmapBounds);
            this.mUnmodifiedMatrix.set(this.mOriStyleBitmapToCanvas);
            this.mTouchBounds.set(getBounds());
            resetCurrentScale();
        }
    }

    public final void drawOverlay(Canvas canvas) {
        this.mBgBounds.set(Math.round(this.mCropArea.left - this.mBgPadding.left), Math.round(this.mCropArea.top - this.mBgPadding.top), Math.round(this.mCropArea.right + this.mBgPadding.right), Math.round(this.mCropArea.bottom + this.mBgPadding.bottom));
        getImageMatrix().mapRect(getImageDisplayBounds(), getImageBounds());
        canvas.getClipBounds(this.mClipBounds);
        this.mMaskPaint.setColor(this.mMaskColor);
        this.mMaskPaint.setAlpha(255);
        int save = canvas.save();
        canvas.clipOutRect(this.mCropArea);
        canvas.drawColor(this.mMaskPaint.getColor());
        canvas.restoreToCount(save);
    }

    private Rect getWindowPaddingRect() {
        return this.mBgPadding;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Matrix getImageMatrix() {
        return this.mBitmapToCanvas;
    }

    private RectF getBounds() {
        return this.mCanvasBounds;
    }

    private RectF getImageBounds() {
        return this.mBitmapBounds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RectF getImageDisplayBounds() {
        return this.mBitmapDisplayBounds;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap bitmap, boolean z, int i, int i2, Rect rect) {
        this.mBitmap = bitmap;
        if (bitmap == null) {
            RectF rectF = this.mBitmapBounds;
            rectF.right = 0.0f;
            rectF.bottom = 0.0f;
        } else {
            this.mBitmapBounds.right = bitmap.getWidth();
            this.mBitmapBounds.bottom = bitmap.getHeight();
        }
        if (z) {
            this.mOriStyleBitmapBounds.set(0.0f, 0.0f, i, i2);
            this.mOriBitmapToOriStyleBitmap.set(getOriBitmapToOriStyleBitmapMatrix(new Size(i, i2), rect));
            resetMatrix();
            resetCurrentScale();
        }
        invalidate();
    }

    public final Matrix getOriBitmapToOriStyleBitmapMatrix(Size size, Rect rect) {
        int width = size.getWidth();
        int height = size.getHeight();
        double width2 = (size.getWidth() * 1.0f) / size.getHeight();
        boolean z = width2 > 0.9d && width2 < 1.1d;
        float f = width;
        float f2 = height;
        float height2 = (f2 - ((0.05f * f2) * 2.0f)) / rect.height();
        float height3 = (f - (((z ? 0.2f : 0.15f) * f) * 2.0f)) / rect.height();
        if (height2 > height3) {
            height2 = height3;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(height2, height2);
        matrix.postTranslate((f / 2.0f) - ((rect.left + (rect.width() / 2.0f)) * height2), ((z ? 60.0f : 73.0f) / (295.0f / size.getWidth())) - (rect.top * height2));
        return matrix;
    }

    public int getCurrentBgColor() {
        return this.mCurrentBgColor;
    }

    public void setCurrentBgColor(int i) {
        if (i == this.mCurrentBgColor) {
            return;
        }
        this.mCurrentBgColor = i;
        setBackgroundColor(i);
    }

    public Bitmap getProcessBitmap() {
        if (getBitmap() != null && this.mOriStyleBitmapBounds.width() > 0.0f && this.mOriStyleBitmapBounds.height() > 0.0f) {
            Matrix matrix = new Matrix();
            Matrix matrix2 = new Matrix();
            this.mUnmodifiedMatrix.invert(matrix2);
            matrix.set(getImageMatrix());
            matrix.postConcat(matrix2);
            Bitmap createBitmap = Bitmap.createBitmap((int) this.mOriStyleBitmapBounds.width(), (int) this.mOriStyleBitmapBounds.height(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawColor(getCurrentBgColor());
            canvas.drawBitmap(getBitmap(), matrix, new Paint(3));
            return createBitmap;
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        public ScaleListener() {
        }

        public /* synthetic */ ScaleListener(IDPhotoView iDPhotoView, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float f;
            IDPhotoView.this.mEventState = EventState.SCALE;
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f2 = 2.0f;
            if (IDPhotoView.this.mCurrentScale * scaleFactor > 2.0f) {
                f = IDPhotoView.this.mCurrentScale;
            } else {
                f2 = 0.5f;
                if (IDPhotoView.this.mCurrentScale * scaleFactor < 0.5f) {
                    f = IDPhotoView.this.mCurrentScale;
                }
                IDPhotoView.access$332(IDPhotoView.this, scaleFactor);
                IDPhotoView.this.getImageMatrix().postScale(scaleFactor, scaleFactor, IDPhotoView.this.getCroppedSize().centerX(), IDPhotoView.this.getCroppedSize().centerY());
                IDPhotoView.this.invalidate();
                return true;
            }
            scaleFactor = f2 / f;
            IDPhotoView.access$332(IDPhotoView.this, scaleFactor);
            IDPhotoView.this.getImageMatrix().postScale(scaleFactor, scaleFactor, IDPhotoView.this.getCroppedSize().centerX(), IDPhotoView.this.getCroppedSize().centerY());
            IDPhotoView.this.invalidate();
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public class TranslateListener implements TranslateDetector.OnTranslateListener {
        public TranslateListener() {
        }

        public /* synthetic */ TranslateListener(IDPhotoView iDPhotoView, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.widget.detector.TranslateDetector.OnTranslateListener
        public boolean onTranslateBegin() {
            if (IDPhotoView.this.mEventState != EventState.IDLE) {
                return IDPhotoView.this.mEventState == EventState.SCALE;
            }
            IDPhotoView.this.mEventState = EventState.TRANSLATE;
            return true;
        }

        @Override // com.miui.gallery.widget.detector.TranslateDetector.OnTranslateListener
        public void onTranslate(float f, float f2) {
            if (IDPhotoView.this.getImageDisplayBounds().left > IDPhotoView.this.mCropArea.left) {
                IDPhotoView.this.getImageMatrix().postTranslate(f / 1.0f, f2 / 1.0f);
            } else if (IDPhotoView.this.getImageDisplayBounds().top > IDPhotoView.this.mCropArea.top) {
                IDPhotoView.this.getImageMatrix().postTranslate(f / 1.0f, f2 / 1.0f);
            } else if (IDPhotoView.this.getImageDisplayBounds().right < IDPhotoView.this.mCropArea.right) {
                IDPhotoView.this.getImageMatrix().postTranslate(f / 1.0f, f2 / 1.0f);
            } else if (IDPhotoView.this.getImageDisplayBounds().bottom < IDPhotoView.this.mCropArea.bottom) {
                IDPhotoView.this.getImageMatrix().postTranslate(f / 1.0f, f2 / 1.0f);
            } else {
                IDPhotoView.this.getImageMatrix().postTranslate(f, f2);
            }
            IDPhotoView.this.invalidate();
        }

        @Override // com.miui.gallery.widget.detector.TranslateDetector.OnTranslateListener
        public void onTranslateEnd() {
            IDPhotoView.this.mEventState = EventState.IDLE;
        }
    }
}
