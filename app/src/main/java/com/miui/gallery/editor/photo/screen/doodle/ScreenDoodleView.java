package com.miui.gallery.editor.photo.screen.doodle;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.miui.gallery.editor.photo.app.doodle.DoodlePaintItem;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.imports.doodle.DoodleConfig;
import com.miui.gallery.editor.photo.core.imports.doodle.PaintElementOperationDrawable;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleShapeNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode;
import com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView;
import com.miui.gallery.editor.photo.screen.base.ScreenVirtualEditorView;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorView;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class ScreenDoodleView extends ScreenVirtualEditorView implements IScreenDoodleOperation {
    public int mActivationIndex;
    public int mColor;
    public DoodleItem mCurrentDoodleItem;
    public int mCurrentMenuItemIndex;
    public ArrayList<DoodleNode> mDoodleNodeList;
    public DoodlePen mDoodlePen;
    public ScreenBaseGestureView.FeatureGesListener mGesListener;
    public boolean mIsAddNew;
    public Matrix mMatrix;
    public PaintElementOperationDrawable mOperationDrawable;
    public DoodlePaintItem.PaintType mPaintType;
    public RectF mRectFTemp;

    /* loaded from: classes2.dex */
    public enum TouchAction {
        NONE,
        DELETE,
        SCALE,
        ROTATE
    }

    public void onDetachedFromWindow() {
    }

    public ScreenDoodleView(ScreenEditorView screenEditorView) {
        super(screenEditorView);
        this.mRectFTemp = new RectF();
        this.mIsAddNew = false;
        this.mGesListener = new GesListener(this, null);
        this.mDoodleNodeList = new ArrayList<>();
        this.mCurrentDoodleItem = DoodleItem.SCREEN_PATH;
        this.mPaintType = DoodlePaintItem.PaintType.MEDIUM;
        this.mMatrix = new Matrix();
        this.mColor = -35801;
        this.mActivationIndex = -1;
        this.mEditorView.setFeatureGestureListener(this.mGesListener);
        this.mOperationDrawable = new PaintElementOperationDrawable(this.mContext.getResources());
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void drawOverlay(Canvas canvas) {
        canvas.save();
        canvas.clipRect(getBitmapGestureParamsHolder().mBitmapDisplayRect);
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
        this.mOperationDrawable.configDecorationPositon(this.mRectFTemp, getBitmapGestureParamsHolder().getMatrix(), doodleNode.getRotateDegrees(), doodleNode.getRotateX() + userLocationX, doodleNode.getRotateY() + userLocationY);
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void canvasMatrixChange() {
        int i = this.mActivationIndex;
        if (i != -1) {
            configOperationPosition(this.mDoodleNodeList.get(i));
        }
    }

    public void bitmapMatrixChange() {
        clearActivation();
    }

    /* loaded from: classes2.dex */
    public class GesListener implements ScreenBaseGestureView.FeatureGesListener {
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

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
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

        public /* synthetic */ GesListener(ScreenDoodleView screenDoodleView, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            DefaultLogger.d("ScreenDoodleView", "onDown");
            ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent, this.mPointTemp1);
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
            ScreenDoodleView.this.mIsAddNew = false;
            this.mScaleMode = false;
            if (ScreenDoodleView.this.mActivationIndex != -1) {
                this.mActivationNode = (DoodleNode) ScreenDoodleView.this.mDoodleNodeList.get(ScreenDoodleView.this.mActivationIndex);
            }
            int findItemByEvent = findItemByEvent(this.mDownX, this.mDownY);
            this.mDownIndex = findItemByEvent;
            if (findItemByEvent != -1) {
                this.mDownNode = (DoodleNode) ScreenDoodleView.this.mDoodleNodeList.get(this.mDownIndex);
            }
            if (this.mActivationNode != null) {
                this.mTouchAction = findTouchActionWithAction(motionEvent.getX(), motionEvent.getY());
                this.mActivationNode.processOnDownEvent(this.mDownX, this.mDownY);
            } else {
                this.mTouchAction = touchAction;
            }
            DefaultLogger.d("ScreenDoodleView", "mTouchAction %s", this.mTouchAction);
            return true;
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            TouchAction touchAction = this.mTouchAction;
            if (touchAction != TouchAction.NONE) {
                if (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$screen$doodle$ScreenDoodleView$TouchAction[touchAction.ordinal()] != 1) {
                    return;
                }
                ScreenDoodleView.this.deleteItem(this.mActivationNode);
                return;
            }
            int i = this.mDownIndex;
            if (i != -1) {
                if (i == ScreenDoodleView.this.mActivationIndex) {
                    return;
                }
                ScreenDoodleView.this.setActivation(this.mDownIndex);
                return;
            }
            ScreenDoodleView.this.clearActivation();
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            float[] fArr = this.mPointTemp1;
            float[] fArr2 = this.mPointTemp2;
            float eventTime = (float) (motionEvent2.getEventTime() - motionEvent2.getDownTime());
            float convertDistanceInBitmap = ScreenDoodleView.this.mEditorView.convertDistanceInBitmap(f);
            float convertDistanceInBitmap2 = ScreenDoodleView.this.mEditorView.convertDistanceInBitmap(f2);
            this.mScrollX -= convertDistanceInBitmap;
            this.mScrollY -= convertDistanceInBitmap2;
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$screen$doodle$ScreenDoodleView$TouchAction[this.mTouchAction.ordinal()];
            if (i != 2) {
                if (i == 3) {
                    ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent, fArr);
                    ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent2, fArr2);
                    ScreenDoodleView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.ROTATE, ScreenDoodleView.this.mRectFTemp);
                    float[] fArr3 = this.mPointTemp3;
                    fArr3[0] = ScreenDoodleView.this.mRectFTemp.centerX();
                    fArr3[1] = ScreenDoodleView.this.mRectFTemp.centerY();
                    ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(fArr3);
                    this.mActivationNode.processRotateEvent(fArr[0], fArr[1], fArr2[0], fArr2[1], convertDistanceInBitmap, convertDistanceInBitmap2, fArr3[0], fArr3[1]);
                    ScreenDoodleView.this.mOperationDrawable.setDrawDecoration(false);
                } else if (i == 4) {
                    ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent, fArr);
                    ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent2, fArr2);
                    ScreenDoodleView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.SCALE, ScreenDoodleView.this.mRectFTemp);
                    float[] fArr4 = this.mPointTemp3;
                    fArr4[0] = ScreenDoodleView.this.mRectFTemp.centerX();
                    fArr4[1] = ScreenDoodleView.this.mRectFTemp.centerY();
                    ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(fArr4);
                    this.mActivationNode.processScaleEvent(fArr[0], fArr[1], fArr2[0], fArr2[1], convertDistanceInBitmap, convertDistanceInBitmap2, fArr4[0], fArr4[1]);
                    ScreenDoodleView.this.mOperationDrawable.setDrawDecoration(false);
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
                    ScreenDoodleView.this.mEditorView.convertPointToBitmapCoordinate(motionEvent2, fArr2);
                    if (motionEvent2.getPointerCount() == 1) {
                        generateDoodle(fArr2[0], fArr2[1], eventTime);
                    }
                }
            }
            DoodleNode doodleNode4 = this.mActivationNode;
            if (doodleNode4 != null) {
                ScreenDoodleView.this.configOperationPosition(doodleNode4);
            }
            ScreenDoodleView.this.invalidate();
        }

        @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            if (ScreenDoodleView.this.mIsAddNew) {
                DoodleNode doodleNode = (DoodleNode) ScreenDoodleView.this.mDoodleNodeList.get(ScreenDoodleView.this.mDoodleNodeList.size() - 1);
                doodleNode.countSize();
                if (doodleNode.getDoodleType() != DoodleNode.DoodleDrawableType.PATH) {
                    ScreenDoodleView screenDoodleView = ScreenDoodleView.this;
                    screenDoodleView.setActivation(screenDoodleView.mDoodleNodeList.size() - 1);
                    ScreenDoodleView.this.invalidate();
                }
            } else {
                DoodleNode doodleNode2 = this.mActivationNode;
                if (doodleNode2 != null) {
                    doodleNode2.processOnUp();
                    ScreenDoodleView.this.invalidate();
                }
            }
            ScreenDoodleView.this.mOperationDrawable.setDrawDecoration(true);
            ScreenDoodleView.this.invalidate();
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            DefaultLogger.d("ScreenDoodleView", "onScale : %f", Float.valueOf(scaleFactor));
            DoodleNode doodleNode = this.mActivationNode;
            if (doodleNode != null) {
                doodleNode.appendScale(scaleFactor);
            }
            ScreenDoodleView.this.invalidate();
            return false;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (ScreenDoodleView.this.mActivationIndex != -1) {
                this.mActivationNode = (DoodleNode) ScreenDoodleView.this.mDoodleNodeList.get(ScreenDoodleView.this.mActivationIndex);
                this.mScaleMode = true;
                return false;
            }
            return false;
        }

        public final void generateDoodle(float f, float f2, float f3) {
            if (this.mCurrentNode == null) {
                DoodleNode doodleDrawable = ScreenDoodleView.this.mCurrentDoodleItem.getDoodleDrawable(ScreenDoodleView.this.mEditorView.getContext().getResources());
                this.mCurrentNode = doodleDrawable;
                doodleDrawable.setImageDisplayMatrix(ScreenDoodleView.this.getBitmapGestureParamsHolder().mBitmapToDisplayMatrix);
                ScreenDoodleView.this.addNewItem(this.mCurrentNode);
                ScreenDoodleView.this.mIsAddNew = true;
            }
            this.mCurrentNode.receivePosition(f, f2, f3);
        }

        public final void moveDoodle(float f, float f2, DoodleNode doodleNode) {
            doodleNode.getStrokeRectF(ScreenDoodleView.this.mRectFTemp);
            ScreenDoodleView.this.mMatrix.reset();
            ScreenDoodleView.this.mMatrix.postRotate(doodleNode.getRotateDegrees(), doodleNode.getRotateX(), doodleNode.getRotateY());
            ScreenDoodleView.this.mMatrix.postTranslate(doodleNode.getUserLocationX(), doodleNode.getUserLocationY());
            ScreenDoodleView.this.mMatrix.postConcat(ScreenDoodleView.this.getBitmapGestureParamsHolder().getMatrix());
            ScreenDoodleView.this.mMatrix.mapRect(ScreenDoodleView.this.mRectFTemp);
            int rectOverScrollStatus = ScreenDoodleView.this.mEditorView.getRectOverScrollStatus(ScreenDoodleView.this.mRectFTemp);
            DefaultLogger.d("ScreenDoodleView", "scroll rect : %s", ScreenDoodleView.this.mRectFTemp);
            if ((rectOverScrollStatus & 8) == 0 ? !((rectOverScrollStatus & 4) == 0 || f >= 0.0f) : f > 0.0f) {
                f = 0.0f;
            }
            if ((rectOverScrollStatus & 2) == 0 ? !((rectOverScrollStatus & 1) == 0 || f2 >= 0.0f) : f2 > 0.0f) {
                f2 = 0.0f;
            }
            doodleNode.appendUserLocationX(-f);
            doodleNode.appendUserLocationY(-f2);
            ScreenDoodleView.this.mOperationDrawable.setDrawDecoration(false);
        }

        public final TouchAction findTouchActionWithAction(float f, float f2) {
            ScreenDoodleView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.DELETE, ScreenDoodleView.this.mRectFTemp);
            if (!ScreenDoodleView.this.mRectFTemp.contains(f, f2)) {
                ScreenDoodleView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.ROTATE, ScreenDoodleView.this.mRectFTemp);
                if (!ScreenDoodleView.this.mRectFTemp.contains(f, f2)) {
                    ScreenDoodleView.this.mOperationDrawable.getDecorationRect(PaintElementOperationDrawable.Action.SCALE, ScreenDoodleView.this.mRectFTemp);
                    if (ScreenDoodleView.this.mRectFTemp.contains(f, f2)) {
                        return TouchAction.SCALE;
                    }
                    return TouchAction.NONE;
                }
                return TouchAction.ROTATE;
            }
            return TouchAction.DELETE;
        }

        public final int findItemByEvent(float f, float f2) {
            if (ScreenDoodleView.this.mActivationIndex == -1 || !((DoodleNode) ScreenDoodleView.this.mDoodleNodeList.get(ScreenDoodleView.this.mActivationIndex)).contains(f, f2)) {
                for (int size = ScreenDoodleView.this.mDoodleNodeList.size() - 1; size >= 0; size--) {
                    DoodleNode doodleNode = (DoodleNode) ScreenDoodleView.this.mDoodleNodeList.get(size);
                    if (doodleNode.contains(f, f2) && doodleNode.isCanSelected()) {
                        return size;
                    }
                }
                return -1;
            }
            return ScreenDoodleView.this.mActivationIndex;
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.screen.doodle.ScreenDoodleView$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$screen$doodle$ScreenDoodleView$TouchAction;

        static {
            int[] iArr = new int[TouchAction.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$screen$doodle$ScreenDoodleView$TouchAction = iArr;
            try {
                iArr[TouchAction.DELETE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$doodle$ScreenDoodleView$TouchAction[TouchAction.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$doodle$ScreenDoodleView$TouchAction[TouchAction.ROTATE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$doodle$ScreenDoodleView$TouchAction[TouchAction.SCALE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public void setCurrentDoodleItem(DoodleItem doodleItem) {
        this.mCurrentDoodleItem = doodleItem;
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void clearActivation() {
        this.mActivationIndex = -1;
        invalidate();
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void onChangeOperation(boolean z) {
        if (z) {
            this.mEditorView.setFeatureGestureListener(this.mGesListener);
            return;
        }
        Iterator<DoodleNode> it = this.mDoodleNodeList.iterator();
        while (it.hasNext()) {
            it.next().setCanSelected(false);
        }
        clearActivation();
    }

    public final void setActivation(int i) {
        if (i != -1) {
            DoodleNode doodleNode = this.mDoodleNodeList.get(i);
            if ((doodleNode instanceof DoodleShapeNode) || (doodleNode instanceof DoodleVectorNode)) {
                this.mActivationIndex = i;
                configOperationPosition(doodleNode);
                configOperationDecoration(doodleNode);
            }
        }
        invalidate();
        this.mEditorView.enableChildHandleMode();
    }

    public final void configOperationDecoration(DoodleNode doodleNode) {
        if (doodleNode.getDoodleType() == DoodleNode.DoodleDrawableType.VECTOR) {
            if (doodleNode.getDoodleName().equals(DoodleItem.ARROW.name())) {
                this.mOperationDrawable.configActionPosition(null, PaintElementOperationDrawable.Action.DELETE, null, PaintElementOperationDrawable.Action.SCALE, this.mEditorView.getResources());
                return;
            } else {
                this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.DELETE, null, PaintElementOperationDrawable.Action.SCALE, null, this.mEditorView.getResources());
                return;
            }
        }
        this.mOperationDrawable.configActionPosition(PaintElementOperationDrawable.Action.DELETE, PaintElementOperationDrawable.Action.ROTATE, PaintElementOperationDrawable.Action.SCALE, null, this.mEditorView.getResources());
    }

    public final void addNewItem(DoodleNode doodleNode) {
        if (doodleNode == null) {
            return;
        }
        doodleNode.setPaintSize((this.mPaintType.paintSize * this.mContext.getResources().getDisplayMetrics().density) / getBitmapGestureParamsHolder().getMatrixValues()[0]);
        doodleNode.setPaintColor(this.mColor);
        doodleNode.setDoodlePen(this.mDoodlePen);
        this.mDoodleNodeList.add(doodleNode);
        addDrawNode(doodleNode);
    }

    public final void deleteItem(DoodleNode doodleNode) {
        this.mActivationIndex = -1;
        this.mDoodleNodeList.remove(doodleNode);
        removeDrawNode(doodleNode);
        invalidate();
        this.mEditorView.disableChildHandleMode();
    }

    @Override // com.miui.gallery.editor.photo.screen.doodle.IScreenDoodleOperation
    public void setColor(int i) {
        this.mColor = i;
        this.mDoodlePen.setColorInt(i);
    }

    @Override // com.miui.gallery.editor.photo.screen.doodle.IScreenDoodleOperation
    public void setAlpha(float f) {
        this.mDoodlePen.setAlpha(f);
    }

    @Override // com.miui.gallery.editor.photo.screen.doodle.IScreenDoodleOperation
    public void setSize(int i) {
        this.mDoodlePen.setSize(i);
    }

    @Override // com.miui.gallery.editor.photo.screen.doodle.IScreenDoodleOperation
    public void setDoodleData(DoodleData doodleData, int i) {
        setCurrentDoodleItem(((DoodleConfig) doodleData).getDoodleItem());
        clearActivation();
        this.mCurrentMenuItemIndex = i;
    }

    public void setDoodlePen(DoodlePen doodlePen) {
        this.mDoodlePen = doodlePen;
    }
}
