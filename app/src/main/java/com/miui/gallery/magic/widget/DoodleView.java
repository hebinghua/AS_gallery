package com.miui.gallery.magic.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.PathResult;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicMatrixUtil;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class DoodleView extends RelativeLayout {
    public static final int MASK_COLOR = Color.parseColor("#98FFCE16");
    public final int DEFAULT_ANIMATOR_ZOOM;
    public boolean isItTouch;
    public boolean isScale;
    public BackgroundView mBackgroundView;
    public Canvas mBufferCanvas;
    public int mCanvasHeight;
    public Path mCurrentZoomPath;
    public float mDefaultScale;
    public GestureDetector mGestureListener;
    public float mLastZoomY;
    public int mMaxZoomRadius;
    public MoveGestureDetector mMoveGestureDetector;
    public Paint mPaint;
    public int mPaintAlpha;
    public Paint mPaintCircle;
    public float mPaintCircleWidth;
    public Paint mPaintClear;
    public int mPaintColor;
    public Paint mPaintRoundLine;
    public PathChangeListener mPathChangeListener;
    public int mPersonIndex;
    public boolean mReCanvas;
    public LinkedList<ItemPath> mRedoList;
    public RectF mRightRect;
    public float mSX;
    public float mSY;
    public float mScale;
    public ScaleGestureDetector mScaleGestureDetector;
    public MattingInvoker.SegmentResult mSegmentResult;
    public boolean mShowZoom;
    public final int mStartZoomRadius;
    public float mStrokeWidth;
    public float mTop;
    public float mTouchX;
    public float mTouchY;
    public float mTouchZoomX;
    public float mTouchZoomY;
    public Xfermode mXFermode;
    public Bitmap mZoomBaseBitmap;
    public float mZoomHorizonX;
    public Path mZoomLinePath;
    public LinkedList<ItemPath> mZoomList;
    public Path mZoomPath;
    public float[] mZoomPoints;
    public float mZoomRadius;
    public RectF mZoomRect;
    public float mZoomRound;
    public float mZoomRoundLineWidth;
    public ObjectAnimator mZoomScalaAnimator;
    public float mZoomScale;
    public ObjectAnimator mZoomXAnimator;
    public boolean onBackPressFalse;

    /* loaded from: classes2.dex */
    public interface PathChangeListener {
        void change(boolean z, boolean z2);
    }

    public DoodleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DEFAULT_ANIMATOR_ZOOM = 200;
        this.mStartZoomRadius = 100;
        this.mPaint = new Paint();
        this.mPaintClear = new Paint();
        this.mPaintCircle = new Paint();
        this.mPaintRoundLine = new Paint();
        this.mXFermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        this.mRedoList = new LinkedList<>();
        this.mZoomList = new LinkedList<>();
        this.mZoomBaseBitmap = null;
        this.mPersonIndex = 0;
        this.mPaintColor = MASK_COLOR;
        this.mPaintAlpha = -1;
        this.mStrokeWidth = 50.0f;
        this.mZoomRadius = 292.0f;
        this.mTop = 24.0f;
        this.mLastZoomY = 10.0f;
        this.mZoomScale = 1.0f;
        this.mDefaultScale = 1.0f;
        this.mShowZoom = false;
        this.isScale = false;
        this.mReCanvas = true;
        this.onBackPressFalse = false;
        this.isItTouch = false;
        this.mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() { // from class: com.miui.gallery.magic.widget.DoodleView.1
            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                return false;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                DoodleView.this.isScale = true;
                DoodleView.this.invalidate();
                return false;
            }
        });
        this.mScale = 1.0f;
        init();
    }

    public void setZoomHorizonX(float f) {
        this.mZoomHorizonX = f;
        invalidate();
    }

    public void setZoomRadius(float f) {
        this.mZoomRadius = f;
        resetPath();
        invalidate();
    }

    public void setStrokeWidth(float f) {
        this.mStrokeWidth = f;
    }

    public final void init() {
        initDimension(getContext());
        initPaint();
        this.mBackgroundView = new BackgroundView(getContext());
        new ObjectAnimator();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "zoomHorizonX", this.mZoomHorizonX);
        this.mZoomXAnimator = ofFloat;
        ofFloat.setDuration(200L);
        this.mZoomXAnimator.setInterpolator(new LinearInterpolator());
        new ObjectAnimator();
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "zoomRadius", this.mZoomRadius);
        this.mZoomScalaAnimator = ofFloat2;
        ofFloat2.setDuration(200L);
        this.mZoomScalaAnimator.setInterpolator(new LinearInterpolator());
        this.mGestureListener = new GestureDetector(getContext(), new MoveGestureListener());
        addView(this.mBackgroundView, new ViewGroup.LayoutParams(-1, -1));
    }

    public final void initPath() {
        resetPath();
        this.mZoomHorizonX = this.mTop;
        this.mScale = 1.0f;
    }

    public final void resetPath() {
        this.mZoomPath = new Path();
        this.mZoomLinePath = new Path();
        float f = this.mMaxZoomRadius - this.mZoomRadius;
        float f2 = this.mZoomRadius;
        this.mZoomRect = new RectF(f, f, f2, f2);
        this.mRightRect = new RectF((getWidth() - this.mTop) - this.mZoomRadius, 0.0f, getWidth() - this.mTop, this.mZoomRadius);
        Path path = this.mZoomPath;
        RectF rectF = this.mZoomRect;
        float f3 = this.mZoomRound;
        path.addRoundRect(rectF, f3, f3, Path.Direction.CCW);
        Path path2 = this.mZoomLinePath;
        float f4 = this.mZoomRadius;
        RectF rectF2 = new RectF(f, f, f4, f4);
        float f5 = this.mZoomRound;
        path2.addRoundRect(rectF2, f5, f5, Path.Direction.CCW);
    }

    public final void initDimension(Context context) {
        this.mZoomRoundLineWidth = (int) context.getResources().getDimension(R$dimen.magic_px_8);
        this.mPaintCircleWidth = (int) context.getResources().getDimension(R$dimen.magic_px_6);
        this.mMaxZoomRadius = (int) context.getResources().getDimension(R$dimen.magic_px_300);
        this.mZoomRound = 25.0f;
        this.mTop = (int) context.getResources().getDimension(R$dimen.magic_px_20);
        this.mLastZoomY = (int) context.getResources().getDimension(R$dimen.magic_px_21);
    }

    public final void initPaint() {
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaintClear.setStyle(Paint.Style.STROKE);
        this.mPaintClear.setStrokeWidth(this.mStrokeWidth);
        this.mPaintClear.setAntiAlias(true);
        this.mPaintClear.setStrokeCap(Paint.Cap.ROUND);
        this.mPaintClear.setColor(0);
        this.mPaintClear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mPaintClear.setAlpha(0);
        this.mPaintCircle.setStyle(Paint.Style.STROKE);
        this.mPaintCircle.setAntiAlias(true);
        this.mPaintCircle.setStrokeWidth(this.mPaintCircleWidth);
        this.mPaintCircle.setColor(-1);
        this.mPaintRoundLine.setStyle(Paint.Style.STROKE);
        this.mPaintRoundLine.setAntiAlias(true);
        this.mPaintRoundLine.setStrokeWidth(this.mZoomRoundLineWidth);
        this.mPaintRoundLine.setColor(Color.parseColor("#ffffff"));
    }

    public final float transformX(float f) {
        return (((f - this.mZoomPoints[0]) / this.mZoomScale) - this.mSX) / this.mScale;
    }

    public final float transformY(float f) {
        return (((f - this.mZoomPoints[1]) / this.mZoomScale) - this.mSY) / this.mScale;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001e, code lost:
        if (r0 != 3) goto L10;
     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchTouchEvent(android.view.MotionEvent r4) {
        /*
            r3 = this;
            int r0 = r4.getAction()
            if (r0 != 0) goto L9
            r0 = 0
            r3.isScale = r0
        L9:
            android.view.ScaleGestureDetector r0 = r3.mScaleGestureDetector
            r0.onTouchEvent(r4)
            android.view.GestureDetector r0 = r3.mGestureListener
            r0.onTouchEvent(r4)
            int r0 = r4.getAction()
            r1 = 1
            if (r0 == r1) goto L32
            r1 = 2
            if (r0 == r1) goto L21
            r1 = 3
            if (r0 == r1) goto L32
            goto L74
        L21:
            com.miui.gallery.magic.widget.DoodleView$MoveGestureDetector r0 = r3.mMoveGestureDetector
            if (r0 == 0) goto L74
            boolean r1 = r0.isLong
            if (r1 == 0) goto L74
            r0.setCurrMotionEven(r4)
            com.miui.gallery.magic.widget.DoodleView$MoveGestureDetector r0 = r3.mMoveGestureDetector
            r3.onMove(r0)
            goto L74
        L32:
            com.miui.gallery.magic.widget.DoodleView$MoveGestureDetector r0 = r3.mMoveGestureDetector
            r1 = 0
            if (r0 == 0) goto L41
            r0.setCurrMotionEven(r4)
            com.miui.gallery.magic.widget.DoodleView$MoveGestureDetector r0 = r3.mMoveGestureDetector
            r3.onMoveEnd(r0)
            r3.mMoveGestureDetector = r1
        L41:
            boolean r0 = r3.isItTouch
            if (r0 == 0) goto L5a
            boolean r0 = r3.isScale
            if (r0 != 0) goto L5a
            com.miui.gallery.magic.widget.DoodleView$MoveGestureDetector r0 = new com.miui.gallery.magic.widget.DoodleView$MoveGestureDetector
            r0.<init>()
            r3.mMoveGestureDetector = r0
            r0.setCurrMotionEven(r4)
            com.miui.gallery.magic.widget.DoodleView$MoveGestureDetector r0 = r3.mMoveGestureDetector
            r3.onTouch(r0)
            r3.mMoveGestureDetector = r1
        L5a:
            com.miui.gallery.magic.util.MagicLog r0 = com.miui.gallery.magic.util.MagicLog.INSTANCE
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "MotionEvent.ACTION_CANCEL isItTouch "
            r1.append(r2)
            boolean r2 = r3.isItTouch
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "MagicLogger DoodleView"
            r0.showLog(r2, r1)
        L74:
            boolean r4 = super.dispatchTouchEvent(r4)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.magic.widget.DoodleView.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        initPath();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mBackgroundView.invalidate();
        drawZoom(canvas);
    }

    public final void drawZoom(Canvas canvas) {
        if (!this.mShowZoom || this.isScale) {
            return;
        }
        canvas.drawCircle(this.mTouchX, this.mTouchY, this.mStrokeWidth / 2.0f, this.mPaintCircle);
        canvas.save();
        if (this.mRightRect.contains(this.mTouchX, this.mTouchY)) {
            float f = this.mZoomHorizonX;
            float f2 = this.mRightRect.left;
            if (f == f2) {
                this.mZoomXAnimator.setFloatValues(f2 + 1.0f, this.mTop);
                this.mZoomXAnimator.start();
            }
        }
        if (this.mZoomRect.contains(this.mTouchX, this.mTouchY)) {
            float f3 = this.mZoomHorizonX;
            float f4 = this.mTop;
            if (f3 == f4) {
                this.mZoomXAnimator.setFloatValues(f4 + 1.0f, this.mRightRect.left);
                this.mZoomXAnimator.start();
            }
        }
        canvas.translate(this.mZoomHorizonX, this.mLastZoomY);
        canvas.clipPath(this.mZoomPath);
        canvas.drawColor(-16777216);
        canvas.save();
        int i = this.mMaxZoomRadius;
        canvas.translate((-this.mTouchX) + (i / 2), (-this.mTouchY) + (i / 2));
        super.dispatchDraw(canvas);
        canvas.drawCircle(this.mTouchX, this.mTouchY, this.mStrokeWidth / 2.0f, this.mPaintCircle);
        canvas.restore();
        canvas.drawPath(this.mZoomLinePath, this.mPaintRoundLine);
        canvas.restore();
    }

    public void undo() {
        if (this.mRedoList.size() > 0) {
            this.mZoomList.add(this.mRedoList.removeLast());
        }
        this.mReCanvas = false;
        invalidate();
    }

    public void redo() {
        if (this.mZoomList.size() > 0) {
            this.mRedoList.add(this.mZoomList.removeLast());
        }
        this.mReCanvas = true;
        invalidate();
    }

    public boolean getShowUndo() {
        return this.mRedoList.size() > 0;
    }

    public boolean getShowRedo() {
        return this.mZoomList.size() > 0;
    }

    public int getRedoListSize() {
        return this.mZoomList.size();
    }

    public int getZoomListSize() {
        return this.mRedoList.size();
    }

    public void setSegment(MattingInvoker.SegmentResult segmentResult) {
        this.mSegmentResult = segmentResult;
    }

    public void setDefaultScale(float f) {
        this.mDefaultScale = f;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mBackgroundView.setBitmap(bitmap);
    }

    public Bitmap apply() {
        return this.mZoomBaseBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void setPersonIndex(int i) {
        this.mPersonIndex = i;
    }

    public void showMask() {
        setRubber(1);
        this.mZoomList.clear();
        Bitmap bitmap = this.mZoomBaseBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mZoomBaseBitmap = null;
        }
        invalidate();
    }

    public void setPathChangeListener(PathChangeListener pathChangeListener) {
        this.mPathChangeListener = pathChangeListener;
    }

    /* loaded from: classes2.dex */
    public class ItemPath {
        public int alpha;
        public int color;
        public Xfermode model;
        public Path path;
        public float strokeWidth;
        public Paint.Style style = Paint.Style.STROKE;

        public ItemPath(Path path, float f) {
            this.path = path;
            this.color = DoodleView.this.mPaintColor;
            this.strokeWidth = DoodleView.this.mStrokeWidth / f;
            this.alpha = DoodleView.this.mPaintAlpha;
            this.model = DoodleView.this.mXFermode;
        }

        public Paint getPaint(Paint paint) {
            return getPaint(paint, DoodleView.this.mDefaultScale);
        }

        public Paint getPaint(Paint paint, float f) {
            paint.setXfermode(this.model);
            paint.setColor(this.color);
            paint.setStrokeWidth(this.strokeWidth / f);
            paint.setStyle(this.style);
            return paint;
        }
    }

    public void setRubber(int i) {
        if (i == 1) {
            this.mPaintColor = MASK_COLOR;
            this.mPaintAlpha = 0;
            this.mXFermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        } else if (i != 2) {
        } else {
            this.mPaintColor = 0;
            this.mXFermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
            this.mPaintAlpha = 0;
        }
    }

    /* loaded from: classes2.dex */
    public class BackgroundView extends BitmapGestureView {
        public Matrix mBitmapToDisplayMatrix;
        public Matrix mMatrix;

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView
        public void onBitmapMatrixChanged() {
            super.onBitmapMatrixChanged();
            BitmapGestureParamsHolder bitmapGestureParamsHolder = this.mBitmapGestureParamsHolder;
            RectF rectF = bitmapGestureParamsHolder.mDisplayInitRect;
            RectF rectF2 = bitmapGestureParamsHolder.mBitmapDisplayRect;
            this.mBitmapToDisplayMatrix = bitmapGestureParamsHolder.mBitmapToDisplayMatrix;
            this.mMatrix.reset();
            this.mMatrix.setTranslate((rectF.width() - rectF2.width()) / 2.0f, (rectF.height() - rectF2.height()) / 2.0f);
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView
        public void drawChild(Canvas canvas) {
            super.drawChild(canvas);
            canvas.concat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
            RectF rectF = this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect;
            DoodleView.this.pathToCanvas(canvas, rectF.width(), rectF.height(), this.mMatrix, this.mOriginBitmap, this.mBitmapToDisplayMatrix);
        }

        public BackgroundView(Context context) {
            super(context);
            this.mMatrix = new Matrix();
            setStrokeEnable(false);
        }

        public boolean getResScale(float f, float f2) {
            DoodleView doodleView = DoodleView.this;
            doodleView.mZoomScale = MagicMatrixUtil.getScale(doodleView.mBackgroundView.mBitmapGestureParamsHolder.mCanvasMatrix);
            DoodleView doodleView2 = DoodleView.this;
            doodleView2.mZoomPoints = MagicMatrixUtil.getTranslate(doodleView2.mBackgroundView.mBitmapGestureParamsHolder.mCanvasMatrix);
            return this.mBitmapGestureParamsHolder.mBitmapDisplayInsideRect.contains(f, f2);
        }
    }

    public final void pathToCanvas(Canvas canvas, float f, float f2, Matrix matrix, Bitmap bitmap, Matrix matrix2) {
        int i = this.mCanvasHeight;
        if (i > 0 && i != canvas.getHeight()) {
            this.mZoomList.clear();
        }
        this.mCanvasHeight = canvas.getHeight();
        if (this.mZoomBaseBitmap == null || this.mReCanvas) {
            this.mReCanvas = false;
            this.mZoomBaseBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(this.mZoomBaseBitmap);
            this.mBufferCanvas = canvas2;
            canvas2.drawColor(0);
            this.mSegmentResult.drawPerson(bitmap, this.mPersonIndex, this.mZoomBaseBitmap, 0, 0, MASK_COLOR);
        }
        PathResult personDrawPath = this.mSegmentResult.getPersonDrawPath(f, f2, this.mPersonIndex, matrix);
        this.mScale = personDrawPath.getScale();
        float[] translate = MagicMatrixUtil.getTranslate(matrix);
        this.mSX = personDrawPath.getX() + translate[0];
        this.mSY = personDrawPath.getY() + translate[1];
        Iterator<ItemPath> it = this.mZoomList.iterator();
        while (it.hasNext()) {
            ItemPath next = it.next();
            this.mBufferCanvas.drawPath(next.path, next.getPaint(this.mPaint));
        }
        Paint paint = new Paint();
        paint.setColor(MASK_COLOR);
        canvas.drawBitmap(this.mZoomBaseBitmap, matrix2, paint);
    }

    /* loaded from: classes2.dex */
    public class MoveGestureListener implements GestureDetector.OnGestureListener {
        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        public MoveGestureListener() {
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            DoodleView.this.isItTouch = true;
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (motionEvent2.getPointerCount() == 1) {
                if (DoodleView.this.mMoveGestureDetector != null) {
                    DoodleView.this.mMoveGestureDetector.setCurrMotionEven(motionEvent2);
                    MagicLog.INSTANCE.showLog("MagicLogger DoodleView", "onScroll  --> onMove ");
                    DoodleView doodleView = DoodleView.this;
                    doodleView.onMove(doodleView.mMoveGestureDetector);
                } else {
                    DoodleView doodleView2 = DoodleView.this;
                    doodleView2.mMoveGestureDetector = new MoveGestureDetector();
                    DoodleView.this.mMoveGestureDetector.setCurrMotionEven(motionEvent2);
                    DoodleView doodleView3 = DoodleView.this;
                    doodleView3.onMoveBegin(doodleView3.mMoveGestureDetector);
                }
            }
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            DoodleView.this.isItTouch = false;
            DoodleView doodleView = DoodleView.this;
            doodleView.mMoveGestureDetector = new MoveGestureDetector();
            DoodleView.this.mMoveGestureDetector.setCurrMotionEven(motionEvent);
            DoodleView.this.mMoveGestureDetector.setLong(true);
            DoodleView doodleView2 = DoodleView.this;
            doodleView2.onMoveBegin(doodleView2.mMoveGestureDetector);
        }
    }

    public boolean onMove(MoveGestureDetector moveGestureDetector) {
        this.isItTouch = false;
        float x = moveGestureDetector.getX();
        float y = moveGestureDetector.getY();
        if (!this.mBackgroundView.getResScale(x, y)) {
            this.mShowZoom = false;
            invalidate();
            return false;
        }
        float transformX = transformX(x);
        float transformY = transformY(y);
        Path path = this.mCurrentZoomPath;
        float f = this.mTouchZoomX;
        float f2 = this.mTouchZoomY;
        path.quadTo(f, f2, (transformX + f) / 2.0f, (transformY + f2) / 2.0f);
        this.mTouchX = x;
        this.mTouchY = y;
        this.mTouchZoomX = transformX;
        this.mTouchZoomY = transformY;
        this.mShowZoom = true;
        invalidate();
        return this.onBackPressFalse;
    }

    public boolean onMoveBegin(MoveGestureDetector moveGestureDetector) {
        float x = moveGestureDetector.getX();
        float y = moveGestureDetector.getY();
        if (!this.mBackgroundView.getResScale(x, y)) {
            MagicLog.INSTANCE.showLog("MagicLogger DoodleView", "onScroll  --> onMoveBegin null onMoveBegin ");
            float transformX = transformX(x);
            float transformY = transformY(y);
            Path path = new Path();
            this.mCurrentZoomPath = path;
            this.mZoomList.add(new ItemPath(path, this.mZoomScale));
            this.mCurrentZoomPath.moveTo(transformX, transformY);
            this.mTouchX = x;
            this.mTouchY = y;
            this.mTouchZoomX = transformX;
            this.mTouchZoomY = transformY;
            this.mShowZoom = true;
            ObjectAnimator objectAnimator = this.mZoomScalaAnimator;
            int i = this.mMaxZoomRadius;
            objectAnimator.setFloatValues((float) (i * 0.85d), i);
            this.mZoomScalaAnimator.start();
            invalidate();
            return false;
        }
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("MagicLogger DoodleView", "onScroll  --> onMoveBegin new Graffiti create mZoomScale " + this.mZoomScale);
        float transformX2 = transformX(x);
        float transformY2 = transformY(y);
        Path path2 = new Path();
        this.mCurrentZoomPath = path2;
        this.mZoomList.add(new ItemPath(path2, this.mZoomScale));
        this.mCurrentZoomPath.moveTo(transformX2, transformY2);
        this.mTouchX = x;
        this.mTouchY = y;
        this.mTouchZoomX = transformX2;
        this.mTouchZoomY = transformY2;
        this.mShowZoom = true;
        ObjectAnimator objectAnimator2 = this.mZoomScalaAnimator;
        int i2 = this.mMaxZoomRadius;
        objectAnimator2.setFloatValues((float) (i2 * 0.85d), i2);
        this.mZoomScalaAnimator.start();
        invalidate();
        return true;
    }

    public void onMoveEnd(MoveGestureDetector moveGestureDetector) {
        float x = moveGestureDetector.getX();
        float y = moveGestureDetector.getY();
        if (!this.mBackgroundView.getResScale(x, y)) {
            this.mCurrentZoomPath = null;
            this.mShowZoom = false;
            return;
        }
        Path path = this.mCurrentZoomPath;
        if (path != null) {
            path.quadTo(this.mTouchZoomX, this.mTouchZoomY, (transformX(x) + this.mTouchZoomX) / 2.0f, (transformY(y) + this.mTouchZoomY) / 2.0f);
            this.mCurrentZoomPath = null;
        }
        this.mShowZoom = false;
        PathChangeListener pathChangeListener = this.mPathChangeListener;
        if (pathChangeListener != null) {
            pathChangeListener.change(getShowRedo(), getShowUndo());
        }
        invalidate();
    }

    public void onTouch(MoveGestureDetector moveGestureDetector) {
        float x = moveGestureDetector.getX();
        float y = moveGestureDetector.getY();
        this.mBackgroundView.getResScale(x, y);
        Path path = new Path();
        this.mCurrentZoomPath = path;
        this.mZoomList.add(new ItemPath(path, this.mZoomScale));
        this.mTouchZoomX = transformX(x);
        float transformY = transformY(y);
        this.mTouchZoomY = transformY;
        this.mCurrentZoomPath.moveTo(this.mTouchZoomX, transformY);
        Path path2 = this.mCurrentZoomPath;
        float f = this.mTouchZoomX;
        float f2 = this.mTouchZoomY;
        path2.quadTo(f, f2, f, f2);
        this.mCurrentZoomPath = null;
        this.mShowZoom = false;
        PathChangeListener pathChangeListener = this.mPathChangeListener;
        if (pathChangeListener != null) {
            pathChangeListener.change(getShowRedo(), getShowUndo());
        }
        invalidate();
    }

    /* loaded from: classes2.dex */
    public class MoveGestureDetector {
        public boolean isLong;
        public MotionEvent mCurrMotionEven;

        public MoveGestureDetector() {
        }

        public void setLong(boolean z) {
            this.isLong = z;
        }

        public void setCurrMotionEven(MotionEvent motionEvent) {
            this.mCurrMotionEven = motionEvent;
        }

        public float getX() {
            return this.mCurrMotionEven.getX();
        }

        public float getY() {
            return this.mCurrMotionEven.getY();
        }
    }
}
