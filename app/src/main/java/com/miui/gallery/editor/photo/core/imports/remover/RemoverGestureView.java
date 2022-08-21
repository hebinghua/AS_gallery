package com.miui.gallery.editor.photo.core.imports.remover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.remover.Inpaint;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelablePathUtils;
import com.miui.gallery.editor.photo.widgets.ProtectiveBitmapGestureView;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class RemoverGestureView extends ProtectiveBitmapGestureView {
    public Context mContext;
    public Paint mCurvePaint;
    public List<Curve> mCurves;
    public ElementType mElementType;
    public GesListener mGesListener;
    public boolean mHasRawYuv;
    public boolean mIsProcessing;
    public Bitmap mMaskBitmap;
    public RemoverCallback mRemoverCallback;
    public RemoverPaintData mRemoverPaintData;
    public int mRenderRecordIndex;

    /* loaded from: classes2.dex */
    public interface RemoverCallback {
        void onRemoveStart();

        void onScribble();

        void removeDone(RemoverPaintData removerPaintData);
    }

    public RemoverGestureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RemoverGestureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mElementType = ElementType.FREE;
        this.mCurves = new ArrayList();
        this.mGesListener = new GesListener();
        this.mContext = context;
        this.mCurvePaint = initCurvePaint(context);
        this.mIsProcessing = false;
        this.mHasRawYuv = false;
        this.mRemoverPaintData = new RemoverPaintData();
        setFeatureGestureListener(this.mGesListener);
    }

    public static Paint initCurvePaint(Context context) {
        Paint paint = new Paint(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(context.getResources().getColor(R.color.remover_curve_color));
        paint.setAlpha(context.getResources().getInteger(R.integer.remover_paint_alpha));
        return paint;
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onBitmapMatrixChanged() {
        this.mRemoverPaintData.mDrawableBounds.set(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        this.mRemoverPaintData.mViewBounds.set(this.mBitmapGestureParamsHolder.mDisplayRect);
        this.mRemoverPaintData.mDrawBitmapMatrix.set(this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
        this.mRemoverPaintData.mBmpBounds.set(this.mBitmapGestureParamsHolder.mBitmapRect);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onCanvasMatrixChange() {
        this.mRemoverPaintData.mDrawBitmapMatrix.reset();
        this.mRemoverPaintData.mDrawBitmapMatrix.set(this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
        this.mRemoverPaintData.mDrawBitmapMatrix.postConcat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mIsProcessing) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    /* loaded from: classes2.dex */
    public class GesListener implements BitmapGestureView.FeatureGesListener {
        public ElementType mPreType;

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        public GesListener() {
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            this.mPreType = null;
            if (RemoverGestureView.this.mIsProcessing) {
                return false;
            }
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            RemoverGestureView.this.mElementType.mBuilder.initDraft(RemoverGestureView.this.mCurvePaint);
            RemoverGestureView.this.mElementType.mBuilder.handleDown(x, y);
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            if (RemoverGestureView.this.mIsProcessing) {
                return;
            }
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (!RemoverGestureView.this.mBitmapGestureParamsHolder.mBitmapDisplayRect.contains(x, y)) {
                return;
            }
            this.mPreType = RemoverGestureView.this.mElementType;
            RemoverGestureView.this.mElementType = ElementType.POINT;
            RemoverGestureView.this.mElementType.mBuilder.initDraft(RemoverGestureView.this.mCurvePaint);
            RemoverGestureView.this.mElementType.mBuilder.handleDown(x, y);
            RemoverGestureView.this.invalidate();
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!RemoverGestureView.this.mIsProcessing && RemoverGestureView.this.mElementType.mBuilder.mDraft != 0) {
                float x = motionEvent2.getX();
                float y = motionEvent2.getY();
                if (!RemoverGestureView.this.mBitmapGestureParamsHolder.mBitmapDisplayRect.contains(x, y)) {
                    return;
                }
                RemoverGestureView.this.mElementType.mBuilder.handleMove(x, y);
                if (RemoverGestureView.this.mRemoverCallback != null) {
                    RemoverGestureView.this.mRemoverCallback.onScribble();
                }
                RemoverGestureView.this.invalidate();
            }
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            ElementType elementType;
            if (RemoverGestureView.this.mState != BitmapGestureView.State.BY_CHILD) {
                RemoverGestureView.this.mElementType.mBuilder.done();
            } else if (RemoverGestureView.this.mElementType.mBuilder.mDraft == 0) {
            } else {
                RemoverGestureView.this.mElementType.mBuilder.handleUp(f, f2);
                if (RemoverGestureView.this.mCurves == null) {
                    RemoverGestureView.this.mCurves = new ArrayList();
                }
                RemoverGestureView.this.mCurves.add(RemoverGestureView.this.mElementType.mBuilder.done());
                if (RemoverGestureView.this.mElementType == ElementType.POINT && (elementType = this.mPreType) != null) {
                    RemoverGestureView.this.mElementType = elementType;
                    RemoverGestureView.this.mElementType.mBuilder.done();
                }
                RemoverGestureView.this.doRemove();
                RemoverGestureView.this.invalidate();
            }
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void drawChild(Canvas canvas) {
        canvas.save();
        canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        List<Curve> list = this.mCurves;
        if (list != null) {
            for (Curve curve : list) {
                curve.draw(canvas);
            }
        }
        if (this.mElementType.activated()) {
            this.mElementType.mBuilder.draw(canvas);
        }
        canvas.restore();
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void setBitmap(Bitmap bitmap) {
        super.setBitmap(bitmap);
        this.mMaskBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ALPHA_8);
        if (!this.mHasRawYuv) {
            writeRecordFile();
            this.mHasRawYuv = true;
        }
        invalidate();
    }

    public Bitmap getPreview() {
        return this.mDisplayBitmap;
    }

    public boolean setElementType(ElementType elementType) {
        if (this.mElementType.activated()) {
            return false;
        }
        this.mElementType = elementType;
        return true;
    }

    public boolean setStrokeSize(int i) {
        if (this.mElementType.activated()) {
            return false;
        }
        this.mCurvePaint.setStrokeWidth(i);
        return true;
    }

    public static void export(Bitmap bitmap, RemoverPaintData removerPaintData, List<Curve> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        removerPaintData.mExportBounds.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        removerPaintData.mExportMatrix.setRectToRect(removerPaintData.mBmpBounds, removerPaintData.mExportBounds, Matrix.ScaleToFit.CENTER);
        bitmap.eraseColor(0);
        Canvas canvas = new Canvas(bitmap);
        removerPaintData.mDrawBitmapMatrix.invert(removerPaintData.mApplyDoodleMatrix);
        removerPaintData.mExportMatrix.preConcat(removerPaintData.mApplyDoodleMatrix);
        canvas.setMatrix(removerPaintData.mExportMatrix);
        for (Curve curve : list) {
            curve.draw(canvas);
        }
    }

    public static void getMaskBounds(RectF rectF, int i, int i2, RemoverPaintData removerPaintData, List<Curve> list) {
        RectF rectF2 = new RectF();
        for (Curve curve : list) {
            rectF2.setEmpty();
            curve.computeBounds(rectF2);
            if (!rectF2.isEmpty()) {
                rectF.union(rectF2);
            }
        }
        removerPaintData.mDrawBitmapMatrix.invert(removerPaintData.mApplyDoodleMatrix);
        float f = i;
        float f2 = i2;
        removerPaintData.mExportBounds.set(0.0f, 0.0f, f, f2);
        removerPaintData.mExportMatrix.setRectToRect(removerPaintData.mBmpBounds, removerPaintData.mExportBounds, Matrix.ScaleToFit.CENTER);
        removerPaintData.mExportMatrix.preConcat(removerPaintData.mApplyDoodleMatrix);
        removerPaintData.mExportMatrix.mapRect(rectF);
        if (!rectF.intersect(0.0f, 0.0f, f, f2)) {
            rectF.setEmpty();
        }
    }

    /* loaded from: classes2.dex */
    public enum ElementType {
        FREE(new Free.Builder()),
        LINE(new StraightLine.Builder()),
        POINT(new Point.Builder());
        
        private final Curve.Builder<?> mBuilder;

        ElementType(Curve.Builder builder) {
            this.mBuilder = builder;
        }

        public boolean activated() {
            return this.mBuilder.mDraft != 0;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Curve implements Parcelable {
        public Paint mPaint;
        public float mStrokeWidth;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public abstract boolean isEmpty(RectF rectF);

        public abstract void onComputeBounds(RectF rectF);

        public abstract void onDraw(Canvas canvas, Paint paint);

        public Curve(Paint paint) {
            this.mPaint = paint;
            this.mStrokeWidth = paint.getStrokeWidth();
        }

        public final void draw(Canvas canvas) {
            onDraw(canvas, this.mPaint);
        }

        public final void computeBounds(RectF rectF) {
            onComputeBounds(rectF);
            rectF.inset(-this.mPaint.getStrokeWidth(), -this.mPaint.getStrokeWidth());
        }

        /* loaded from: classes2.dex */
        public static abstract class Builder<T extends Curve> {
            public T mDraft;

            public abstract T onCreateDraft(Paint paint);

            public abstract void onDown(float f, float f2);

            public abstract void onMove(float f, float f2);

            public abstract void onUp(float f, float f2);

            public final void initDraft(Paint paint) {
                this.mDraft = onCreateDraft(paint);
            }

            public final void handleDown(float f, float f2) {
                onDown(f, f2);
            }

            public final void handleMove(float f, float f2) {
                onMove(f, f2);
            }

            public final void handleUp(float f, float f2) {
                onUp(f, f2);
            }

            public void draw(Canvas canvas) {
                this.mDraft.draw(canvas);
            }

            public Curve done() {
                T t = this.mDraft;
                this.mDraft = null;
                return t;
            }
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeFloat(this.mStrokeWidth);
        }

        public Curve(Parcel parcel) {
            this.mStrokeWidth = parcel.readFloat();
            Paint initCurvePaint = RemoverGestureView.initCurvePaint(GalleryApp.sGetAndroidContext());
            this.mPaint = initCurvePaint;
            initCurvePaint.setStrokeWidth(this.mStrokeWidth);
        }
    }

    /* loaded from: classes2.dex */
    public static class Free extends Curve {
        public static final Parcelable.Creator<Free> CREATOR = new Parcelable.Creator<Free>() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Free.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Free mo841createFromParcel(Parcel parcel) {
                return new Free(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Free[] mo842newArray(int i) {
                return new Free[i];
            }
        };
        public Path mPath;
        public List<PointF> mPointFList;

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public Free(Paint paint) {
            super(paint);
            this.mPath = new Path();
            this.mPointFList = new ArrayList();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public void onDraw(Canvas canvas, Paint paint) {
            canvas.drawPath(this.mPath, paint);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public boolean isEmpty(RectF rectF) {
            RectF rectF2 = new RectF();
            onComputeBounds(rectF2);
            return rectF2.isEmpty();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public void onComputeBounds(RectF rectF) {
            this.mPath.computeBounds(rectF, true);
        }

        /* loaded from: classes2.dex */
        public static class Builder extends Curve.Builder<Free> {
            public PointF mLastPoint = new PointF();

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onUp(float f, float f2) {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public Free onCreateDraft(Paint paint) {
                return new Free(new Paint(paint));
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onDown(float f, float f2) {
                ((Free) this.mDraft).mPath.moveTo(f, f2);
                this.mLastPoint.set(f, f2);
                ((Free) this.mDraft).mPointFList.clear();
                ((Free) this.mDraft).mPointFList.add(new PointF(f, f2));
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onMove(float f, float f2) {
                Path path = ((Free) this.mDraft).mPath;
                PointF pointF = this.mLastPoint;
                float f3 = pointF.x;
                float f4 = pointF.y;
                path.quadTo(f3, f4, (f + f3) / 2.0f, (f2 + f4) / 2.0f);
                this.mLastPoint.set(f, f2);
                ((Free) this.mDraft).mPointFList.add(new PointF(f, f2));
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeTypedList(this.mPointFList);
        }

        public Free(Parcel parcel) {
            super(parcel);
            ArrayList createTypedArrayList = parcel.createTypedArrayList(PointF.CREATOR);
            this.mPointFList = createTypedArrayList;
            this.mPath = ParcelablePathUtils.getPathFromPointList(createTypedArrayList);
        }
    }

    /* loaded from: classes2.dex */
    public static class StraightLine extends Curve {
        public static final Parcelable.Creator<StraightLine> CREATOR = new Parcelable.Creator<StraightLine>() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.StraightLine.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public StraightLine mo845createFromParcel(Parcel parcel) {
                return new StraightLine(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public StraightLine[] mo846newArray(int i) {
                return new StraightLine[i];
            }
        };
        public PointF mEnd;
        public PointF mStart;

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public StraightLine(Paint paint) {
            super(paint);
            this.mStart = new PointF();
            this.mEnd = new PointF();
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public void onDraw(Canvas canvas, Paint paint) {
            PointF pointF = this.mStart;
            float f = pointF.x;
            float f2 = pointF.y;
            PointF pointF2 = this.mEnd;
            canvas.drawLine(f, f2, pointF2.x, pointF2.y, paint);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public boolean isEmpty(RectF rectF) {
            boolean z;
            PointF pointF = this.mStart;
            if (!rectF.contains(pointF.x, pointF.y)) {
                PointF pointF2 = this.mEnd;
                if (!rectF.contains(pointF2.x, pointF2.y)) {
                    z = true;
                    return this.mStart.equals(this.mEnd) || z;
                }
            }
            z = false;
            if (this.mStart.equals(this.mEnd)) {
                return true;
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public void onComputeBounds(RectF rectF) {
            PointF pointF = this.mStart;
            float f = pointF.x;
            PointF pointF2 = this.mEnd;
            float f2 = pointF2.x;
            if (f < f2) {
                rectF.left = f;
                rectF.right = f2;
            } else {
                rectF.left = f2;
                rectF.right = f;
            }
            float f3 = pointF.y;
            float f4 = pointF2.y;
            if (f3 < f4) {
                rectF.top = f3;
                rectF.bottom = f4;
                return;
            }
            rectF.top = f4;
            rectF.bottom = f3;
        }

        /* loaded from: classes2.dex */
        public static class Builder extends Curve.Builder<StraightLine> {
            public Builder() {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public StraightLine onCreateDraft(Paint paint) {
                return new StraightLine(new Paint(paint));
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onDown(float f, float f2) {
                ((StraightLine) this.mDraft).mStart.set(f, f2);
                ((StraightLine) this.mDraft).mEnd.set(f, f2);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onMove(float f, float f2) {
                ((StraightLine) this.mDraft).mEnd.set(f, f2);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onUp(float f, float f2) {
                ((StraightLine) this.mDraft).mEnd.set(f, f2);
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeParcelable(this.mStart, i);
            parcel.writeParcelable(this.mEnd, i);
        }

        public StraightLine(Parcel parcel) {
            super(parcel);
            this.mStart = new PointF();
            this.mEnd = new PointF();
            this.mStart = (PointF) parcel.readParcelable(PointF.class.getClassLoader());
            this.mEnd = (PointF) parcel.readParcelable(PointF.class.getClassLoader());
        }
    }

    /* loaded from: classes2.dex */
    public static class Point extends Curve {
        public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() { // from class: com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Point.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Point mo843createFromParcel(Parcel parcel) {
                return new Point(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Point[] mo844newArray(int i) {
                return new Point[i];
            }
        };
        public PointF mPointF;
        public Paint mPointPaint;

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve, android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public Point(Paint paint) {
            super(paint);
            this.mPointF = new PointF();
            Paint paint2 = new Paint(paint);
            this.mPointPaint = paint2;
            paint2.setStyle(Paint.Style.FILL);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public void onDraw(Canvas canvas, Paint paint) {
            PointF pointF = this.mPointF;
            canvas.drawCircle(pointF.x, pointF.y, paint.getStrokeWidth() / 2.0f, this.mPointPaint);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public boolean isEmpty(RectF rectF) {
            PointF pointF = this.mPointF;
            return !rectF.contains(pointF.x, pointF.y);
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve
        public void onComputeBounds(RectF rectF) {
            PointF pointF = this.mPointF;
            float f = pointF.x;
            float f2 = pointF.y;
            rectF.set(f, f2, f, f2);
        }

        /* loaded from: classes2.dex */
        public static class Builder extends Curve.Builder<Point> {
            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onMove(float f, float f2) {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onUp(float f, float f2) {
            }

            public Builder() {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public Point onCreateDraft(Paint paint) {
                return new Point(paint);
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve.Builder
            public void onDown(float f, float f2) {
                ((Point) this.mDraft).mPointF.x = f;
                ((Point) this.mDraft).mPointF.y = f2;
            }
        }

        @Override // com.miui.gallery.editor.photo.core.imports.remover.RemoverGestureView.Curve, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeParcelable(this.mPointF, i);
        }

        public Point(Parcel parcel) {
            super(parcel);
            this.mPointF = new PointF();
            this.mPointF = (PointF) parcel.readParcelable(PointF.class.getClassLoader());
            Paint paint = new Paint(this.mPaint);
            this.mPointPaint = paint;
            paint.setStyle(Paint.Style.FILL);
        }
    }

    public final void doRemove() {
        boolean z;
        Iterator<Curve> it = this.mCurves.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (it.next().isEmpty(this.mBitmapGestureParamsHolder.mBitmapDisplayRect)) {
                z = true;
                break;
            }
        }
        if (z) {
            this.mCurves.clear();
        } else {
            new RemoveTask(this).execute(new Void[0]);
        }
    }

    /* loaded from: classes2.dex */
    public static class RemoveTask extends AsyncTask<Void, Void, Integer> {
        public WeakReference<RemoverGestureView> mWeakRemoverGestureView;

        public RemoveTask(RemoverGestureView removerGestureView) {
            this.mWeakRemoverGestureView = new WeakReference<>(removerGestureView);
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            RemoverGestureView removerGestureView = this.mWeakRemoverGestureView.get();
            if (removerGestureView == null || removerGestureView.mRemoverCallback == null) {
                return;
            }
            removerGestureView.mRemoverCallback.onRemoveStart();
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((RemoveTask) num);
            RemoverGestureView removerGestureView = this.mWeakRemoverGestureView.get();
            if (num == null || removerGestureView == null) {
                return;
            }
            if (removerGestureView.mRemoverCallback != null) {
                if (num.intValue() == -11) {
                    Toast.makeText(removerGestureView.mContext, (int) R.string.remover_erase_aera_too_big, 0).show();
                    removerGestureView.mRemoverCallback.removeDone(null);
                } else if (num.intValue() == -12) {
                    removerGestureView.mRemoverCallback.removeDone(null);
                } else if (num.intValue() < 0) {
                    removerGestureView.mRemoverCallback.removeDone(null);
                    DefaultLogger.e("RemoverGestureView", "remove sdk process error :%d", num);
                } else if (removerGestureView.mCurves != null && removerGestureView.mCurves.size() > 0) {
                    RemoverPaintData removerPaintData = new RemoverPaintData();
                    removerPaintData.mCurves = removerGestureView.mCurves;
                    removerPaintData.mApplyDoodleMatrix.set(removerGestureView.mRemoverPaintData.mApplyDoodleMatrix);
                    removerPaintData.mExportBounds.set(removerGestureView.mRemoverPaintData.mExportBounds);
                    removerPaintData.mDrawableBounds.set(removerGestureView.mRemoverPaintData.mDrawableBounds);
                    removerPaintData.mDrawBitmapMatrix.set(removerGestureView.mRemoverPaintData.mDrawBitmapMatrix);
                    removerPaintData.mExportMatrix.set(removerGestureView.mRemoverPaintData.mExportMatrix);
                    removerPaintData.mBmpBounds.set(removerGestureView.mRemoverPaintData.mBmpBounds);
                    removerPaintData.mViewBounds.set(removerGestureView.mRemoverPaintData.mViewBounds);
                    removerPaintData.mRemoverNNFData = removerGestureView.mRemoverPaintData.mRemoverNNFData;
                    removerGestureView.mRemoverCallback.removeDone(removerPaintData);
                }
            }
            if (removerGestureView.mElementType.activated()) {
                return;
            }
            removerGestureView.mCurves = null;
            removerGestureView.invalidate();
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            RemoverGestureView removerGestureView = this.mWeakRemoverGestureView.get();
            if (removerGestureView == null || removerGestureView.mDisplayBitmap == null) {
                return null;
            }
            RemoverGestureView.export(removerGestureView.mMaskBitmap, removerGestureView.mRemoverPaintData, removerGestureView.mCurves);
            RectF rectF = new RectF();
            Rect rect = new Rect();
            int width = removerGestureView.mMaskBitmap.getWidth();
            int height = removerGestureView.mMaskBitmap.getHeight();
            RemoverGestureView.getMaskBounds(rectF, width, height, removerGestureView.mRemoverPaintData, removerGestureView.mCurves);
            rectF.roundOut(rect);
            DefaultLogger.d("RemoverGestureView", "mask rect: %s, width: %s, height %s", rect, Integer.valueOf(width), Integer.valueOf(height));
            if (rect.isEmpty()) {
                return -12;
            }
            if (rect.width() >= width || rect.height() >= height) {
                return -11;
            }
            RemoverNNFData removerNNFData = new RemoverNNFData();
            removerGestureView.mRemoverPaintData.mRemoverNNFData = removerNNFData;
            return Integer.valueOf(Inpaint.inpaintBmpData(removerGestureView.mDisplayBitmap, removerGestureView.mMaskBitmap, removerGestureView.mDisplayBitmap.getWidth(), removerGestureView.mDisplayBitmap.getHeight(), rect.left, rect.top, rect.right, rect.bottom, removerNNFData));
        }
    }

    public void setRemoverCallback(RemoverCallback removerCallback) {
        this.mRemoverCallback = removerCallback;
    }

    public void writeRecordFile() {
        RandomAccessFile randomAccessFile;
        Throwable th;
        IOException e;
        if (this.mDisplayBitmap == null) {
            return;
        }
        this.mRenderRecordIndex = (this.mRenderRecordIndex + 1) % 10;
        try {
            try {
                randomAccessFile = new RandomAccessFile(getTmpRecordFile(this.mRenderRecordIndex), "rw");
                try {
                    this.mDisplayBitmap.copyPixelsToBuffer(randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, this.mDisplayBitmap.getByteCount()));
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    BaseMiscUtil.closeSilently(randomAccessFile);
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(randomAccessFile);
                throw th;
            }
        } catch (IOException e3) {
            randomAccessFile = null;
            e = e3;
        } catch (Throwable th3) {
            randomAccessFile = null;
            th = th3;
            BaseMiscUtil.closeSilently(randomAccessFile);
            throw th;
        }
        BaseMiscUtil.closeSilently(randomAccessFile);
    }

    public void renderPreviousBuffer() {
        int i = (this.mRenderRecordIndex - 1) % 10;
        this.mRenderRecordIndex = i;
        if (i < 0) {
            this.mRenderRecordIndex = i + 10;
        }
        readRecordBuffer();
        invalidate();
    }

    public void renderNextBuffer() {
        this.mRenderRecordIndex = (this.mRenderRecordIndex + 1) % 10;
        readRecordBuffer();
        invalidate();
    }

    public final void readRecordBuffer() {
        RandomAccessFile randomAccessFile;
        Throwable th;
        IOException e;
        try {
            try {
                randomAccessFile = new RandomAccessFile(getTmpRecordFile(this.mRenderRecordIndex), "r");
                try {
                    FileChannel channel = randomAccessFile.getChannel();
                    this.mDisplayBitmap.copyPixelsFromBuffer(channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size()));
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    BaseMiscUtil.closeSilently(randomAccessFile);
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(randomAccessFile);
                throw th;
            }
        } catch (IOException e3) {
            randomAccessFile = null;
            e = e3;
        } catch (Throwable th3) {
            randomAccessFile = null;
            th = th3;
            BaseMiscUtil.closeSilently(randomAccessFile);
            throw th;
        }
        BaseMiscUtil.closeSilently(randomAccessFile);
    }

    public final File getTmpRecordFile(int i) {
        File file = new File(this.mContext.getCacheDir(), "remover-records");
        if (file.exists() || file.mkdir()) {
            return new File(file, "remover_record_temp_" + i);
        }
        return null;
    }

    public void setIsProcessing(boolean z) {
        this.mIsProcessing = z;
    }
}
