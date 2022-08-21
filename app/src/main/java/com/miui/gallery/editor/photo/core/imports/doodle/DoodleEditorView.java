package com.miui.gallery.editor.photo.core.imports.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.editor.photo.app.OperationUpdateListener;
import com.miui.gallery.editor.photo.app.doodle.DoodlePaintView;
import com.miui.gallery.editor.photo.core.imports.doodle.PaintElementOperationDrawable;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode;
import com.miui.gallery.editor.photo.widgets.ProtectiveBitmapGestureView;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class DoodleEditorView extends ProtectiveBitmapGestureView {
    public int mActivationIndex;
    public int mColor;
    public DoodleItem mCurrentDoodleItem;
    public DoodleCallback mDoodleCallback;
    public ArrayList<DoodleNode> mDoodleNodeList;
    public GesListener mGesListener;
    public boolean mIsAddNew;
    public Matrix mMatrix;
    public float[] mMatrixValues;
    public PaintElementOperationDrawable mOperationDrawable;
    public OperationUpdateListener mOperationUpdateListener;
    public float mPaintSize;
    public RectF mRectFTemp;
    public boolean mRenderOriginOnly;
    public LinkedList<DoodleNode> mRevokedDoodleNodeList;

    /* loaded from: classes2.dex */
    public interface DoodleCallback {
        void onDoodleGenerate(String str, int i);
    }

    /* loaded from: classes2.dex */
    public enum TouchAction {
        NONE,
        DELETE,
        SCALE,
        ROTATE
    }

    public DoodleEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRectFTemp = new RectF();
        this.mMatrixValues = new float[9];
        this.mIsAddNew = false;
        this.mGesListener = new GesListener(this, null);
        this.mDoodleNodeList = new ArrayList<>();
        this.mRevokedDoodleNodeList = new LinkedList<>();
        this.mCurrentDoodleItem = DoodleItem.PATH;
        this.mColor = -16777216;
        this.mActivationIndex = -1;
        this.mMatrix = new Matrix();
        init();
    }

    public final void init() {
        setBackground(null);
        setFeatureGestureListener(this.mGesListener);
        this.mOperationDrawable = new PaintElementOperationDrawable(getResources());
        this.mPaintSize = getResources().getDisplayMetrics().density * DoodlePaintView.PaintType.MEDIUM.paintSize;
    }

    public void setRenderOriginOnly(boolean z) {
        this.mRenderOriginOnly = z;
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void drawChild(Canvas canvas) {
        if (this.mRenderOriginOnly) {
            return;
        }
        canvas.save();
        canvas.concat(this.mBitmapGestureParamsHolder.getMatrix());
        canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapRect);
        for (int i = 0; i < this.mDoodleNodeList.size(); i++) {
            this.mDoodleNodeList.get(i).draw(canvas);
        }
        canvas.restore();
        canvas.save();
        canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        if (this.mActivationIndex != -1) {
            this.mOperationDrawable.draw(canvas);
        }
        canvas.restore();
    }

    public final void configOperationPosition(DoodleNode doodleNode) {
        float userLocationX = doodleNode.getUserLocationX();
        float userLocationY = doodleNode.getUserLocationY();
        doodleNode.getStrokeRectF(this.mRectFTemp);
        this.mRectFTemp.offset(userLocationX, userLocationY);
        this.mOperationDrawable.configDecorationPositon(this.mRectFTemp, getTotalMatrix(), doodleNode.getRotateDegrees(), doodleNode.getRotateX() + userLocationX, doodleNode.getRotateY() + userLocationY);
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onBitmapMatrixChanged() {
        super.onBitmapMatrixChanged();
        Iterator<DoodleNode> it = this.mDoodleNodeList.iterator();
        while (it.hasNext()) {
            configOperationPosition(it.next());
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onCanvasMatrixChange() {
        int i = this.mActivationIndex;
        if (i != -1) {
            configOperationPosition(this.mDoodleNodeList.get(i));
        }
        invalidate();
    }

    /* loaded from: classes2.dex */
    public class GesListener implements BitmapGestureView.FeatureGesListener {
        public DoodleNode mActivationNode;
        public DoodleNode mCurrentNode;
        public int mDownIndex;
        public DoodleNode mDownNode;
        public float mDownX;
        public float mDownY;
        public float[] mPointTemp1;
        public float[] mPointTemp2;
        public float[] mPointTemp3;
        public boolean mScaleMode;
        public float mScrollX;
        public float mScrollY;
        public TouchAction mTouchAction;

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        public GesListener() {
            this.mTouchAction = TouchAction.NONE;
            this.mDownIndex = -1;
            this.mScaleMode = false;
            this.mPointTemp1 = new float[2];
            this.mPointTemp2 = new float[2];
            this.mPointTemp3 = new float[2];
        }

        public /* synthetic */ GesListener(DoodleEditorView doodleEditorView, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            DefaultLogger.d("DoodleEditorView", "onDown");
            DoodleEditorView.this.convertPointToBitmapCoordinate(motionEvent, this.mPointTemp1);
            float[] fArr = this.mPointTemp1;
            float f = fArr[0];
            this.mDownX = f;
            float f2 = fArr[1];
            this.mDownY = f2;
            this.mScrollX = f;
            this.mScrollY = f2;
            TouchAction touchAction = TouchAction.NONE;
            this.mTouchAction = touchAction;
            this.mCurrentNode = null;
            this.mDownNode = null;
            this.mActivationNode = null;
            DoodleEditorView.this.mIsAddNew = false;
            this.mScaleMode = false;
            if (DoodleEditorView.this.mActivationIndex != -1) {
                this.mActivationNode = (DoodleNode) DoodleEditorView.this.mDoodleNodeList.get(DoodleEditorView.this.mActivationIndex);
            }
            int findItemByEvent = findItemByEvent(this.mDownX, this.mDownY);
            this.mDownIndex = findItemByEvent;
            if (findItemByEvent != -1) {
                this.mDownNode = (DoodleNode) DoodleEditorView.this.mDoodleNodeList.get(this.mDownIndex);
            }
            if (this.mActivationNode != null) {
                this.mTouchAction = findTouchActionWithAction(motionEvent.getX(), motionEvent.getY());
                this.mActivationNode.processOnDownEvent(this.mDownX, this.mDownY);
            } else {
                this.mTouchAction = touchAction;
            }
            DefaultLogger.d("DoodleEditorView", "mTouchAction %s", this.mTouchAction);
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            TouchAction touchAction = this.mTouchAction;
            if (touchAction != TouchAction.NONE) {
                if (touchAction != TouchAction.DELETE) {
                    return;
                }
                DoodleEditorView.this.deleteItem(this.mActivationNode);
                return;
            }
            int i = this.mDownIndex;
            if (i != -1) {
                if (i == DoodleEditorView.this.mActivationIndex) {
                    return;
                }
                DoodleEditorView.this.setActivation(this.mDownIndex);
                return;
            }
            DoodleEditorView.this.clearActivation();
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            float[] fArr = this.mPointTemp1;
            float[] fArr2 = this.mPointTemp2;
            float eventTime = (float) (motionEvent2.getEventTime() - motionEvent2.getDownTime());
            float convertDistanceInBitmap = DoodleEditorView.this.convertDistanceInBitmap(f);
            float convertDistanceInBitmap2 = DoodleEditorView.this.convertDistanceInBitmap(f2);
            this.mScrollX -= convertDistanceInBitmap;
            this.mScrollY -= convertDistanceInBitmap2;
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$DoodleEditorView$TouchAction[this.mTouchAction.ordinal()];
            if (i != 1) {
                if (i == 2) {
                    DoodleEditorView.this.convertPointToBitmapCoordinate(motionEvent, fArr);
                    DoodleEditorView.this.convertPointToBitmapCoordinate(motionEvent2, fArr2);
                    DoodleEditorView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.ROTATE, DoodleEditorView.this.mRectFTemp);
                    float[] fArr3 = this.mPointTemp3;
                    fArr3[0] = DoodleEditorView.this.mRectFTemp.centerX();
                    fArr3[1] = DoodleEditorView.this.mRectFTemp.centerY();
                    DoodleEditorView.this.convertPointToBitmapCoordinate(fArr3);
                    this.mActivationNode.processRotateEvent(fArr[0], fArr[1], fArr2[0], fArr2[1], convertDistanceInBitmap, convertDistanceInBitmap2, fArr3[0], fArr3[1]);
                    DoodleEditorView.this.mOperationDrawable.setDrawDecoration(false);
                } else if (i == 3) {
                    DoodleEditorView.this.convertPointToBitmapCoordinate(motionEvent, fArr);
                    DoodleEditorView.this.convertPointToBitmapCoordinate(motionEvent2, fArr2);
                    DoodleEditorView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.SCALE, DoodleEditorView.this.mRectFTemp);
                    float[] fArr4 = this.mPointTemp3;
                    fArr4[0] = DoodleEditorView.this.mRectFTemp.centerX();
                    fArr4[1] = DoodleEditorView.this.mRectFTemp.centerY();
                    DoodleEditorView.this.convertPointToBitmapCoordinate(fArr4);
                    this.mActivationNode.processScaleEvent(fArr[0], fArr[1], fArr2[0], fArr2[1], convertDistanceInBitmap, convertDistanceInBitmap2, fArr4[0], fArr4[1]);
                    DoodleEditorView.this.mOperationDrawable.setDrawDecoration(false);
                }
            } else if (this.mScaleMode) {
                DoodleNode doodleNode = this.mActivationNode;
                if (doodleNode != null) {
                    moveDoodle(convertDistanceInBitmap, convertDistanceInBitmap2, doodleNode);
                }
            } else {
                DoodleNode doodleNode2 = this.mActivationNode;
                if (doodleNode2 != null) {
                    DoodleNode doodleNode3 = this.mDownNode;
                    if (doodleNode3 != null && doodleNode3 == doodleNode2) {
                        moveDoodle(convertDistanceInBitmap, convertDistanceInBitmap2, doodleNode2);
                    } else if (motionEvent2.getPointerCount() == 1) {
                        generateDoodle(this.mScrollX, this.mScrollY, eventTime);
                    }
                } else {
                    if (this.mCurrentNode == null) {
                        generateDoodle(this.mDownX, this.mDownY, eventTime);
                    }
                    DoodleEditorView.this.convertPointToBitmapCoordinate(motionEvent2, fArr2);
                    if (motionEvent2.getPointerCount() == 1) {
                        generateDoodle(fArr2[0], fArr2[1], eventTime);
                    }
                }
            }
            DoodleNode doodleNode4 = this.mActivationNode;
            if (doodleNode4 != null) {
                DoodleEditorView.this.configOperationPosition(doodleNode4);
            }
            DoodleEditorView.this.invalidate();
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            if (DoodleEditorView.this.mIsAddNew) {
                DoodleNode doodleNode = (DoodleNode) DoodleEditorView.this.mDoodleNodeList.get(DoodleEditorView.this.mDoodleNodeList.size() - 1);
                doodleNode.countSize();
                if (doodleNode.getDoodleType() != DoodleNode.DoodleDrawableType.PATH) {
                    DoodleEditorView doodleEditorView = DoodleEditorView.this;
                    doodleEditorView.setActivation(doodleEditorView.mDoodleNodeList.size() - 1);
                    DoodleEditorView.this.invalidate();
                }
                DoodleEditorView.this.mRevokedDoodleNodeList.clear();
                DoodleEditorView.this.notifyOperationUpdate();
            } else {
                DoodleNode doodleNode2 = this.mActivationNode;
                if (doodleNode2 != null) {
                    doodleNode2.processOnUp();
                    DoodleEditorView.this.invalidate();
                    DoodleEditorView.this.notifyOperationUpdate();
                }
            }
            DoodleEditorView.this.mOperationDrawable.setDrawDecoration(true);
            DoodleEditorView.this.invalidate();
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            DefaultLogger.d("DoodleEditorView", "onScale : %f", Float.valueOf(scaleFactor));
            DoodleNode doodleNode = this.mActivationNode;
            if (doodleNode != null) {
                doodleNode.appendScale(scaleFactor);
            }
            DoodleEditorView.this.invalidate();
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (DoodleEditorView.this.mActivationIndex != -1) {
                this.mActivationNode = (DoodleNode) DoodleEditorView.this.mDoodleNodeList.get(DoodleEditorView.this.mActivationIndex);
                this.mScaleMode = true;
                return false;
            }
            return false;
        }

        public final void generateDoodle(float f, float f2, float f3) {
            if (this.mCurrentNode == null) {
                DoodleNode doodleDrawable = DoodleEditorView.this.mCurrentDoodleItem.getDoodleDrawable(DoodleEditorView.this.getContext().getResources());
                this.mCurrentNode = doodleDrawable;
                doodleDrawable.setImageDisplayMatrix(DoodleEditorView.this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
                DoodleEditorView.this.addNewItem(this.mCurrentNode);
                DoodleEditorView.this.mIsAddNew = true;
                if (DoodleEditorView.this.mDoodleCallback != null) {
                    DoodleEditorView.this.mDoodleCallback.onDoodleGenerate(this.mCurrentNode.getDoodleName(), this.mCurrentNode.getPaintColor());
                }
            }
            this.mCurrentNode.receivePosition(f, f2, f3);
        }

        public final void moveDoodle(float f, float f2, DoodleNode doodleNode) {
            doodleNode.getStrokeRectF(DoodleEditorView.this.mRectFTemp);
            DoodleEditorView.this.mMatrix.reset();
            DoodleEditorView.this.mMatrix.postRotate(doodleNode.getRotateDegrees(), doodleNode.getRotateX(), doodleNode.getRotateY());
            DoodleEditorView.this.mMatrix.postTranslate(doodleNode.getUserLocationX(), doodleNode.getUserLocationY());
            DoodleEditorView.this.mMatrix.mapRect(DoodleEditorView.this.mRectFTemp);
            DoodleEditorView doodleEditorView = DoodleEditorView.this;
            int rectOverScrollStatusInBitmap = doodleEditorView.getRectOverScrollStatusInBitmap(doodleEditorView.mRectFTemp);
            DefaultLogger.d("DoodleEditorView", "scroll rect : %s", DoodleEditorView.this.mRectFTemp);
            if ((rectOverScrollStatusInBitmap & 8) == 0 ? !((rectOverScrollStatusInBitmap & 4) == 0 || f >= 0.0f) : f > 0.0f) {
                f = 0.0f;
            }
            if ((rectOverScrollStatusInBitmap & 2) == 0 ? !((rectOverScrollStatusInBitmap & 1) == 0 || f2 >= 0.0f) : f2 > 0.0f) {
                f2 = 0.0f;
            }
            doodleNode.appendUserLocationX(-f);
            doodleNode.appendUserLocationY(-f2);
            DoodleEditorView.this.mOperationDrawable.setDrawDecoration(false);
        }

        public final TouchAction findTouchActionWithAction(float f, float f2) {
            DoodleEditorView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.DELETE, DoodleEditorView.this.mRectFTemp);
            if (!DoodleEditorView.this.mRectFTemp.contains(f, f2)) {
                DoodleEditorView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.ROTATE, DoodleEditorView.this.mRectFTemp);
                if (!DoodleEditorView.this.mRectFTemp.contains(f, f2)) {
                    DoodleEditorView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.SCALE, DoodleEditorView.this.mRectFTemp);
                    if (DoodleEditorView.this.mRectFTemp.contains(f, f2)) {
                        return TouchAction.SCALE;
                    }
                    return TouchAction.NONE;
                }
                return TouchAction.ROTATE;
            }
            return TouchAction.DELETE;
        }

        public final int findItemByEvent(float f, float f2) {
            if (DoodleEditorView.this.mActivationIndex == -1 || !((DoodleNode) DoodleEditorView.this.mDoodleNodeList.get(DoodleEditorView.this.mActivationIndex)).contains(f, f2)) {
                for (int size = DoodleEditorView.this.mDoodleNodeList.size() - 1; size >= 0; size--) {
                    if (((DoodleNode) DoodleEditorView.this.mDoodleNodeList.get(size)).contains(f, f2)) {
                        return size;
                    }
                }
                return -1;
            }
            return DoodleEditorView.this.mActivationIndex;
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.doodle.DoodleEditorView$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$DoodleEditorView$TouchAction;

        static {
            int[] iArr = new int[TouchAction.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$DoodleEditorView$TouchAction = iArr;
            try {
                iArr[TouchAction.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$DoodleEditorView$TouchAction[TouchAction.ROTATE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$doodle$DoodleEditorView$TouchAction[TouchAction.SCALE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public void setCurrentDoodleItem(DoodleItem doodleItem) {
        this.mCurrentDoodleItem = doodleItem;
    }

    public void clearActivation() {
        this.mActivationIndex = -1;
        invalidate();
        disableChildHandleMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActivation(int i) {
        this.mActivationIndex = i;
        if (i != -1) {
            DoodleNode doodleNode = this.mDoodleNodeList.get(i);
            configOperationPosition(doodleNode);
            configOperationDecoration(doodleNode);
        }
        invalidate();
        enableChildHandleMode();
    }

    public final void configOperationDecoration(DoodleNode doodleNode) {
        if (doodleNode.getDoodleType() == DoodleNode.DoodleDrawableType.VECTOR) {
            if (doodleNode.getDoodleName().equals(DoodleItem.ARROW.name())) {
                this.mOperationDrawable.configActionPosition(null, PaintElementOperationDrawable.Action.DELETE, null, PaintElementOperationDrawable.Action.SCALE, getResources());
                return;
            } else {
                this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.DELETE, null, PaintElementOperationDrawable.Action.SCALE, null, getResources());
                return;
            }
        }
        this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.DELETE, PaintElementOperationDrawable.Action.ROTATE, PaintElementOperationDrawable.Action.SCALE, null, getResources());
    }

    private Matrix getTotalMatrix() {
        Matrix matrix = new Matrix(this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix);
        matrix.postConcat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
        return matrix;
    }

    public final void addNewItem(DoodleNode doodleNode) {
        getTotalMatrix().getValues(this.mMatrixValues);
        doodleNode.setPaintSize(this.mPaintSize / this.mMatrixValues[0]);
        doodleNode.setPaintColor(this.mColor);
        this.mDoodleNodeList.add(doodleNode);
    }

    public boolean isEmpty() {
        return this.mDoodleNodeList.isEmpty();
    }

    public final void deleteItem(DoodleNode doodleNode) {
        this.mActivationIndex = -1;
        this.mDoodleNodeList.remove(doodleNode);
        invalidate();
        disableChildHandleMode();
    }

    public boolean canRevoke() {
        return !this.mDoodleNodeList.isEmpty();
    }

    public boolean canRevert() {
        return !this.mRevokedDoodleNodeList.isEmpty();
    }

    public void doRevoke() {
        this.mActivationIndex = -1;
        LinkedList<DoodleNode> linkedList = this.mRevokedDoodleNodeList;
        ArrayList<DoodleNode> arrayList = this.mDoodleNodeList;
        linkedList.add(arrayList.remove(arrayList.size() - 1));
        invalidate();
        notifyOperationUpdate();
        disableChildHandleMode();
    }

    public void doRevert() {
        this.mActivationIndex = -1;
        this.mDoodleNodeList.add(this.mRevokedDoodleNodeList.removeLast());
        invalidate();
        notifyOperationUpdate();
        disableChildHandleMode();
    }

    public DoodleEntry export() {
        clearActivation();
        return new DoodleEntry(this.mBitmapGestureParamsHolder.mBitmapRect, this.mDoodleNodeList);
    }

    /* loaded from: classes2.dex */
    public static class DoodleEntry implements Parcelable {
        public static final Parcelable.Creator<DoodleEntry> CREATOR = new Parcelable.Creator<DoodleEntry>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.DoodleEditorView.DoodleEntry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public DoodleEntry mo786createFromParcel(Parcel parcel) {
                return new DoodleEntry(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public DoodleEntry[] mo787newArray(int i) {
                return new DoodleEntry[i];
            }
        };
        public List<DoodleNode> mDoodleNodeList;
        public RectF mRectF;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public DoodleEntry(RectF rectF, List<DoodleNode> list) {
            RectF rectF2 = new RectF();
            this.mRectF = rectF2;
            rectF2.set(rectF);
            this.mDoodleNodeList = new ArrayList(list);
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
            matrix.setRectToRect(this.mRectF, rectF, Matrix.ScaleToFit.FILL);
            Canvas canvas = new Canvas(bitmap);
            canvas.concat(matrix);
            for (DoodleNode doodleNode : this.mDoodleNodeList) {
                doodleNode.initForParcelable(GalleryApp.sGetAndroidContext());
                doodleNode.draw(canvas);
            }
            return bitmap;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mRectF, i);
            parcel.writeInt(this.mDoodleNodeList.size());
            for (DoodleNode doodleNode : this.mDoodleNodeList) {
                parcel.writeString(doodleNode.getClass().getName());
                parcel.writeParcelable(doodleNode, i);
            }
        }

        public DoodleEntry(Parcel parcel) {
            this.mRectF = new RectF();
            this.mRectF = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
            int readInt = parcel.readInt();
            this.mDoodleNodeList = new ArrayList(readInt);
            for (int i = 0; i < readInt; i++) {
                try {
                    this.mDoodleNodeList.add((DoodleNode) parcel.readParcelable(Class.forName(parcel.readString()).getClassLoader()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setColor(int i) {
        this.mColor = i;
    }

    public void setDoodleCallback(DoodleCallback doodleCallback) {
        this.mDoodleCallback = doodleCallback;
    }

    public void setPaintSize(float f) {
        this.mPaintSize = f;
    }

    public void setOperationUpdateListener(OperationUpdateListener operationUpdateListener) {
        this.mOperationUpdateListener = operationUpdateListener;
    }

    public final void notifyOperationUpdate() {
        OperationUpdateListener operationUpdateListener = this.mOperationUpdateListener;
        if (operationUpdateListener != null) {
            operationUpdateListener.onOperationUpdate();
        }
    }
}
