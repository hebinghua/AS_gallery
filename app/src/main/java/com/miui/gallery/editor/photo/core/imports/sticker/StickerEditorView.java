package com.miui.gallery.editor.photo.core.imports.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.sticker.StickerNode;
import com.miui.gallery.editor.photo.widgets.ProtectiveBitmapGestureView;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class StickerEditorView extends ProtectiveBitmapGestureView {
    public Matrix mBitmapToCanvas;
    public StickerNode.Cache mCache;
    public Matrix mCanvasToBitmap;
    public float mInitialSize;
    public float[] mMatrixValue;
    public float mMaxRadius;
    public float mMinRadius;
    public StickerNode.Mutator mMutator;
    public OnEmptyCallback mOnEmptyCallback;
    public PointF mPrePoint;
    public RectF mRectTemp;
    public PointF mReusePoint;
    public State mState;

    /* loaded from: classes2.dex */
    public interface OnEmptyCallback {
        void onEmpty();
    }

    /* loaded from: classes2.dex */
    public enum State {
        IDLE,
        PENDING,
        MOVE,
        MIRROR,
        SCALE,
        DELETE
    }

    public StickerEntry export() {
        return new StickerEntry(this.mBitmapGestureParamsHolder.mBitmapRect, getCacheNode());
    }

    public List<StickerNode> getCacheNode() {
        StickerNode.Mutator mutator = this.mMutator;
        if (mutator != null && !mutator.isIdle()) {
            this.mCache.append(this.mMutator.unbind());
        }
        return this.mCache.getNodes();
    }

    public StickerEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mState = State.IDLE;
        this.mPrePoint = new PointF();
        this.mReusePoint = new PointF();
        this.mCanvasToBitmap = new Matrix();
        this.mBitmapToCanvas = new Matrix();
        this.mRectTemp = new RectF();
        this.mMatrixValue = new float[9];
        init();
    }

    public final void init() {
        setFeatureGestureListener(new CustomGestureListener());
    }

    public void setOnEmptyCallback(OnEmptyCallback onEmptyCallback) {
        this.mOnEmptyCallback = onEmptyCallback;
    }

    /* loaded from: classes2.dex */
    public class CustomGestureListener implements BitmapGestureView.FeatureGesListener {
        public float mDownX;
        public float mDownY;
        public float mFirstSpan;
        public float[] mPoint;
        public float mPreScale;

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        public CustomGestureListener() {
            this.mPoint = new float[2];
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            if (StickerEditorView.this.mMutator == null) {
                return false;
            }
            this.mDownX = motionEvent.getX();
            this.mDownY = motionEvent.getY();
            StickerEditorView.this.convertPointToViewPortCoordinate(motionEvent, this.mPoint);
            if (!StickerEditorView.this.mMutator.isIdle()) {
                if (!StickerEditorView.this.mMutator.isScale(this.mDownX, this.mDownY)) {
                    if (!StickerEditorView.this.mMutator.isDelete(this.mDownX, this.mDownY)) {
                        if (!StickerEditorView.this.mMutator.isMirror(this.mDownX, this.mDownY)) {
                            if (StickerEditorView.this.mMutator.contains(this.mDownX, this.mDownY)) {
                                StickerEditorView.this.mPrePoint.set(this.mDownX, this.mDownY);
                                StickerEditorView.this.mState = State.PENDING;
                            } else {
                                StickerEditorView.this.mState = State.IDLE;
                            }
                        } else {
                            StickerEditorView.this.mState = State.MIRROR;
                            StickerEditorView.this.mMutator.setMirrorAlpha(153);
                        }
                    } else {
                        StickerEditorView.this.mState = State.DELETE;
                        StickerEditorView.this.mMutator.setDeleteAlpha(153);
                    }
                } else {
                    StickerEditorView.this.mState = State.SCALE;
                    StickerEditorView.this.mMutator.setScaleAlpha(153);
                }
            } else {
                StickerEditorView.this.mState = State.IDLE;
            }
            StickerEditorView.this.invalidate();
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            if (StickerEditorView.this.mMutator == null) {
                return;
            }
            if (StickerEditorView.this.mMutator.isIdle() || StickerEditorView.this.mState != State.IDLE) {
                if (StickerEditorView.this.mState != State.MIRROR || !StickerEditorView.this.mMutator.isMirror(motionEvent.getX(), motionEvent.getY())) {
                    if (StickerEditorView.this.mState != State.DELETE || !StickerEditorView.this.mMutator.isDelete(motionEvent.getX(), motionEvent.getY())) {
                        int find = StickerEditorView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
                        if (find == -1) {
                            return;
                        }
                        bindItem(find);
                        return;
                    }
                    StickerEditorView.this.mMutator.unbind();
                    StickerEditorView.this.disableChildHandleMode();
                    StickerEditorView.this.mState = State.IDLE;
                    StickerEditorView.this.invalidate();
                    if (StickerEditorView.this.mCache == null || !StickerEditorView.this.mCache.isEmpty() || StickerEditorView.this.mMutator == null || !StickerEditorView.this.mMutator.isIdle() || StickerEditorView.this.mOnEmptyCallback == null) {
                        return;
                    }
                    StickerEditorView.this.mOnEmptyCallback.onEmpty();
                    return;
                }
                StickerEditorView.this.mMutator.mirror();
                StickerEditorView.this.mState = State.IDLE;
                StickerEditorView.this.invalidate();
                return;
            }
            int find2 = StickerEditorView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
            if (find2 == -1) {
                StickerEditorView.this.mCache.append(StickerEditorView.this.mMutator.unbind());
                StickerEditorView.this.disableChildHandleMode();
                StickerEditorView.this.invalidate();
                return;
            }
            bindItem(find2);
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (StickerEditorView.this.mMutator == null) {
                return;
            }
            float x = motionEvent2.getX();
            float y = motionEvent2.getY();
            if (StickerEditorView.this.mState != State.IDLE || StickerEditorView.this.mMutator.isIdle()) {
                State state = StickerEditorView.this.mState;
                State state2 = State.MOVE;
                if (state != state2) {
                    if (StickerEditorView.this.mState == State.PENDING) {
                        StickerEditorView.this.mState = state2;
                        return;
                    } else if (StickerEditorView.this.mState == State.SCALE) {
                        PointF pointF = StickerEditorView.this.mReusePoint;
                        pointF.set(StickerEditorView.this.mMutator.getVertex(3)[0], StickerEditorView.this.mMutator.getVertex(3)[1]);
                        float centerX = StickerEditorView.this.mMutator.getDrawBounds().centerX();
                        float centerY = StickerEditorView.this.mMutator.getDrawBounds().centerY();
                        float f3 = x - centerX;
                        float f4 = y - centerY;
                        double hypot = Math.hypot(f3, f4);
                        double hypot2 = Math.hypot(pointF.x - centerX, pointF.y - centerY);
                        float currentCanvasScale = StickerEditorView.this.getCurrentCanvasScale();
                        float f5 = StickerEditorView.this.mMaxRadius * currentCanvasScale;
                        double d = StickerEditorView.this.mMinRadius * currentCanvasScale;
                        if (hypot < d) {
                            float f6 = (float) (d / hypot);
                            hypot = d;
                            y = (f4 * f6) + centerY;
                            x = (f3 * f6) + centerX;
                        }
                        double d2 = f5;
                        if (hypot > d2) {
                            float f7 = (float) (d2 / hypot);
                            x = ((x - centerX) * f7) + centerX;
                            y = ((y - centerY) * f7) + centerY;
                            hypot = d2;
                        }
                        StickerEditorView.this.mMutator.scale((float) (hypot / hypot2));
                        pointF.set(StickerEditorView.this.mMutator.getVertex(3)[0], StickerEditorView.this.mMutator.getVertex(3)[1]);
                        double hypot3 = Math.hypot(x - pointF.x, y - pointF.y);
                        double d3 = (((hypot * hypot) * 2.0d) - (hypot3 * hypot3)) / ((2.0d * hypot) * hypot);
                        StickerEditorView.this.mMutator.rotate((float) ((((pointF.x - centerX) * d3) - (x - centerX)) / (pointF.y - centerY)), (float) d3);
                        StickerEditorView.this.invalidate();
                        return;
                    } else if (!StickerEditorView.this.mMutator.isIdle()) {
                        return;
                    } else {
                        int find = StickerEditorView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
                        if (find != -1) {
                            bindItem(find);
                        }
                        StickerEditorView.this.invalidate();
                        return;
                    }
                }
                moveItem(motionEvent, motionEvent2, f, f2);
                return;
            }
            int find2 = StickerEditorView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
            if (find2 != -1) {
                bindItem(find2);
            }
            StickerEditorView.this.mState = State.MOVE;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            if (StickerEditorView.this.mMutator == null) {
                return;
            }
            StickerEditorView.this.mState = State.IDLE;
            StickerEditorView.this.mMutator.setMirrorAlpha(255);
            StickerEditorView.this.mMutator.setDeleteAlpha(255);
            StickerEditorView.this.mMutator.setScaleAlpha(255);
            StickerEditorView.this.invalidate();
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (StickerEditorView.this.mMutator == null) {
                return false;
            }
            float currentSpan = scaleGestureDetector.getCurrentSpan() / this.mFirstSpan;
            float f = this.mPreScale;
            float f2 = currentSpan / f;
            this.mPreScale = f * f2;
            if (!StickerEditorView.this.mMutator.isIdle()) {
                float radius = StickerEditorView.this.mMutator.getRadius();
                float currentCanvasScale = StickerEditorView.this.getCurrentCanvasScale();
                float f3 = StickerEditorView.this.mMinRadius * currentCanvasScale;
                float f4 = StickerEditorView.this.mMaxRadius * currentCanvasScale;
                if (f2 <= 1.0f && radius < f3) {
                    return false;
                }
                if (f2 > 1.0f && radius > f4) {
                    return false;
                }
                StickerEditorView.this.mMutator.scale(f2);
                StickerEditorView.this.mState = State.MOVE;
                StickerEditorView.this.invalidate();
            }
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            this.mFirstSpan = scaleGestureDetector.getCurrentSpan();
            this.mPreScale = 1.0f;
            return false;
        }

        public final void moveItem(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            StickerEditorView.this.mRectTemp.set(StickerEditorView.this.mMutator.getDrawBounds());
            StickerEditorView stickerEditorView = StickerEditorView.this;
            int rectOverScrollStatus = stickerEditorView.getRectOverScrollStatus(stickerEditorView.mRectTemp);
            if ((rectOverScrollStatus & 8) == 0 ? !((rectOverScrollStatus & 4) == 0 || f >= 0.0f) : f > 0.0f) {
                f = 0.0f;
            }
            if ((rectOverScrollStatus & 2) == 0 ? !((rectOverScrollStatus & 1) == 0 || f2 >= 0.0f) : f2 > 0.0f) {
                f2 = 0.0f;
            }
            PointF pointF = StickerEditorView.this.mReusePoint;
            pointF.set(-f, -f2);
            StickerEditorView.this.resetTransByBounds(pointF);
            StickerEditorView.this.mMutator.translate(pointF.x, pointF.y);
            StickerEditorView.this.mPrePoint.set(motionEvent2.getX(), motionEvent2.getY());
            StickerEditorView.this.invalidate();
        }

        public final void bindItem(int i) {
            StickerEditorView.this.mState = State.PENDING;
            StickerNode remove = StickerEditorView.this.mCache.remove(i);
            StickerEditorView.this.mCache.append(StickerEditorView.this.mMutator.unbind());
            StickerEditorView.this.mMutator.bind(remove);
            StickerEditorView.this.enableChildHandleMode();
            StickerEditorView.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getCurrentCanvasScale() {
        this.mBitmapGestureParamsHolder.mCanvasMatrix.getValues(this.mMatrixValue);
        return this.mMatrixValue[0];
    }

    public final void resetTransByBounds(PointF pointF) {
        RectF rectF = this.mBitmapGestureParamsHolder.mDisplayRect;
        float f = pointF.x;
        if (f > 0.0f) {
            float width = (f + this.mMutator.getDrawBounds().right) - (this.mMutator.getDrawBounds().width() * 0.8f);
            float f2 = rectF.right;
            if (width > f2) {
                pointF.x = (f2 - this.mMutator.getDrawBounds().right) + (this.mMutator.getDrawBounds().width() * 0.8f);
            }
        }
        float f3 = pointF.x;
        if (f3 < 0.0f) {
            float width2 = f3 + this.mMutator.getDrawBounds().left + (this.mMutator.getDrawBounds().width() * 0.8f);
            float f4 = rectF.left;
            if (width2 < f4) {
                pointF.x = (f4 - this.mMutator.getDrawBounds().left) - (this.mMutator.getDrawBounds().width() * 0.8f);
            }
        }
        float f5 = pointF.y;
        if (f5 > 0.0f) {
            float height = (f5 + this.mMutator.getDrawBounds().bottom) - (this.mMutator.getDrawBounds().height() * 0.8f);
            float f6 = rectF.bottom;
            if (height > f6) {
                pointF.y = (f6 - this.mMutator.getDrawBounds().bottom) + (this.mMutator.getDrawBounds().height() * 0.8f);
            }
        }
        float f7 = pointF.y;
        if (f7 < 0.0f) {
            float f8 = rectF.top;
            if (f7 + this.mMutator.getDrawBounds().top + (this.mMutator.getDrawBounds().height() * 0.8f) >= f8) {
                return;
            }
            pointF.y = (f8 - this.mMutator.getDrawBounds().top) - (this.mMutator.getDrawBounds().height() * 0.8f);
        }
    }

    public void add(Bitmap bitmap, String str, long j, String str2) {
        if (this.mMutator == null) {
            return;
        }
        StickerNode stickerNode = new StickerNode(bitmap, str, j, str2);
        if (!this.mMutator.isIdle()) {
            this.mCache.append(this.mMutator.unbind());
        }
        BitmapGestureParamsHolder bitmapGestureParamsHolder = this.mBitmapGestureParamsHolder;
        stickerNode.init(bitmapGestureParamsHolder.mCanvasMatrixInvert, this.mDisplayBitmap, bitmapGestureParamsHolder.mDisplayRect.height());
        this.mMutator.bind(stickerNode);
        float currentCanvasScale = this.mMinRadius * getCurrentCanvasScale();
        float radius = this.mMutator.getRadius();
        float f = currentCanvasScale / radius;
        StickerNode.Mutator mutator = this.mMutator;
        mutator.translate((-mutator.getBorderBounds().width()) / 2.0f, (-this.mMutator.getBorderBounds().height()) / 2.0f);
        if (radius < currentCanvasScale) {
            this.mMutator.scale(f);
        }
        if (this.mCache.isEmpty()) {
            StickerNode.Mutator mutator2 = this.mMutator;
            float centerX = this.mBitmapGestureParamsHolder.mBitmapDisplayInsideRect.centerX();
            BitmapGestureParamsHolder bitmapGestureParamsHolder2 = this.mBitmapGestureParamsHolder;
            mutator2.translate(centerX - bitmapGestureParamsHolder2.mBitmapDisplayRect.left, bitmapGestureParamsHolder2.mBitmapDisplayInsideRect.centerY() - this.mBitmapGestureParamsHolder.mBitmapDisplayRect.top);
        } else {
            float f2 = this.mBitmapGestureParamsHolder.mBitmapDisplayInsideRect.left - this.mMutator.getBorderBounds().left;
            float f3 = this.mBitmapGestureParamsHolder.mBitmapDisplayInsideRect.top - this.mMutator.getBorderBounds().top;
            this.mMutator.translate(f2 + ((float) (Math.random() * (this.mBitmapGestureParamsHolder.mBitmapDisplayInsideRect.width() - this.mMutator.getBorderBounds().width()))), f3 + ((float) (Math.random() * (this.mBitmapGestureParamsHolder.mBitmapDisplayInsideRect.height() - this.mMutator.getBorderBounds().height()))));
        }
        enableChildHandleMode();
        invalidate();
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onClear() {
        StickerNode.Cache cache = this.mCache;
        if (cache != null) {
            cache.clear();
        }
        StickerNode.Mutator mutator = this.mMutator;
        if (mutator != null) {
            mutator.unbind();
        }
        invalidate();
    }

    public void onRelease() {
        StickerNode.Cache cache = this.mCache;
        if (cache != null) {
            cache.release();
        }
        StickerNode.Mutator mutator = this.mMutator;
        if (mutator != null) {
            mutator.unbind();
        }
    }

    public void onStart() {
        this.mInitialSize = this.mBitmapToCanvas.mapRadius(getContext().getResources().getDimensionPixelSize(R.dimen.sticker_initial_size));
        this.mMinRadius = getContext().getResources().getDimensionPixelSize(R.dimen.sticker_min_radius);
        this.mMaxRadius = getContext().getResources().getDimensionPixelSize(R.dimen.sticker_max_radius);
        StickerNode.Mutator mutator = this.mMutator;
        if (mutator == null) {
            this.mMutator = new StickerNode.Mutator(getContext(), this.mBitmapToCanvas, this.mCanvasToBitmap);
        } else {
            mutator.updateDisplayInfo();
        }
        if (this.mCache == null) {
            this.mCache = new StickerNode.Cache(this.mDisplayBitmap, this.mCanvasToBitmap, this.mBitmapToCanvas);
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onBitmapMatrixChanged() {
        refreshMatrix();
        onStart();
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onCanvasMatrixChange() {
        StickerNode.Mutator mutator = this.mMutator;
        if (mutator != null && !mutator.isIdle()) {
            this.mMutator.updateDisplayInfo();
        }
        refreshMatrix();
    }

    public final void refreshMatrix() {
        this.mBitmapToCanvas.reset();
        this.mBitmapToCanvas.set(this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
        this.mBitmapToCanvas.postConcat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
        this.mBitmapToCanvas.invert(this.mCanvasToBitmap);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void drawChild(Canvas canvas) {
        if (this.mCache == null || this.mMutator == null) {
            return;
        }
        canvas.save();
        canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        this.mCache.draw(canvas);
        StickerNode.Mutator mutator = this.mMutator;
        State state = this.mState;
        boolean z = false;
        boolean z2 = state == State.MOVE || state == State.SCALE;
        if (state == State.SCALE) {
            z = true;
        }
        mutator.draw(canvas, z2, z);
        canvas.restore();
    }

    public void onDestroy() {
        StickerNode.Mutator mutator = this.mMutator;
        if (mutator != null && !mutator.isIdle()) {
            this.mCache.append(this.mMutator.unbind());
        }
        this.mMutator = null;
        this.mCache = null;
        this.mState = State.IDLE;
    }

    /* loaded from: classes2.dex */
    public static class StickerEntry implements Parcelable {
        public static final Parcelable.Creator<StickerEntry> CREATOR = new Parcelable.Creator<StickerEntry>() { // from class: com.miui.gallery.editor.photo.core.imports.sticker.StickerEditorView.StickerEntry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public StickerEntry mo871createFromParcel(Parcel parcel) {
                return new StickerEntry(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public StickerEntry[] mo872newArray(int i) {
                return new StickerEntry[i];
            }
        };
        public List<StickerNode> mItems;
        public RectF mPreviewBounds;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public StickerEntry(RectF rectF, List<StickerNode> list) {
            this.mPreviewBounds = new RectF(rectF);
            this.mItems = list;
        }

        public Bitmap apply(Bitmap bitmap) {
            if (!bitmap.isMutable()) {
                bitmap = Bitmaps.copyBitmapAndRecycle(bitmap);
            }
            if (bitmap == null) {
                return null;
            }
            RectF rectF = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
            Matrix matrix = new Matrix();
            matrix.setRectToRect(this.mPreviewBounds, rectF, Matrix.ScaleToFit.FILL);
            Canvas canvas = new Canvas(bitmap);
            canvas.concat(matrix);
            for (StickerNode stickerNode : this.mItems) {
                stickerNode.draw(canvas);
            }
            return bitmap;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mPreviewBounds, i);
            parcel.writeList(this.mItems);
        }

        public StickerEntry(Parcel parcel) {
            this.mPreviewBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
            ArrayList arrayList = new ArrayList();
            this.mItems = arrayList;
            parcel.readList(arrayList, StickerNode.class.getClassLoader());
        }
    }
}
