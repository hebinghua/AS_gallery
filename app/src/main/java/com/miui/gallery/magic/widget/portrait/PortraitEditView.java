package com.miui.gallery.magic.widget.portrait;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SizeF;
import android.view.MotionEvent;
import com.miui.gallery.baseui.R$dimen;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicMatrixUtil;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.widget.gesture.RotateGestureDetector;
import com.miui.gallery.magic.widget.portrait.BackgroundAdaptationHelper;
import com.miui.gallery.magic.widget.portrait.PortraitNode;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import com.miui.gallery.widget.imageview.BitmapGestureView;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;

/* loaded from: classes2.dex */
public class PortraitEditView extends BitmapGestureView {
    public float[] firstValue;
    public AnimConfig mAlphaHideAnimConfig;
    public AnimConfig mAlphaShowAnimConfig;
    public BackgroundAdaptationHelper mBackgroundAdaptationHelper;
    public float mBackgroundPreH;
    public float mBackgroundPreW;
    public int mBackgroundSegmentCount;
    public Matrix mBitmapToCanvas;
    public PortraitNode.Cache mCache;
    public Matrix mCanvasToBitmap;
    public boolean mChangeBackground;
    public int mComparatorIndex;
    public boolean mEndTransparent;
    public boolean mIsBackgroundOrigin;
    public boolean mIsMove;
    public RectF mLastDisplayRect;
    public OnManualMattingOnLister mManualMattingLister;
    public float[] mMatrixValue;
    public float mMaxRadius;
    public float mMinRadius;
    public PortraitNode.Mutator mMutator;
    public int mOpState;
    public PointF mPrePoint;
    public RectF mRectTemp;
    public PointF mReusePoint;
    public RotateGestureDetector mRotateDetector;
    public float mStartRotate;
    public State mState;
    public IStateStyle mStateStyle;
    public Bitmap mTransparent;
    public int mUpdateViewAlpha;
    public int viewAlpha;

    /* loaded from: classes2.dex */
    public interface OnManualMattingOnLister {
        PortraitNode addNewNode(PortraitNode portraitNode);

        void firstMove(int i);

        void onCanvasMatrixChange(long j, List<PortraitNode> list, int i, int... iArr);

        void onClick(int i);
    }

    /* loaded from: classes2.dex */
    public enum State {
        IDLE,
        PENDING,
        MOVE,
        MIRROR,
        ADD,
        SCALE,
        DELETE,
        MAGIC
    }

    /* renamed from: $r8$lambda$G5O5Lpd-Jcjh-XY6ju4eITgQwAw */
    public static /* synthetic */ void m1076$r8$lambda$G5O5LpdJcjhXY6ju4eITgQwAw(PortraitEditView portraitEditView) {
        portraitEditView.lambda$add$0();
    }

    public void changePersonBitmap(Bitmap bitmap) {
        if (this.mMutator.getItem() == null) {
            ToastUtils.makeText(getContext(), getContext().getResources().getString(R$string.magic_select_people));
            return;
        }
        this.mMutator.getItem().changePersonBitmap(bitmap);
        changeCanvas(true);
        invalidate();
    }

    public int getPersonIndex() {
        return this.mMutator.getPersonIndex();
    }

    public PortraitNode getCurrentItem() {
        return this.mMutator.getItem();
    }

    public void reAdd(PortraitNode portraitNode) {
        if (portraitNode.isIdle()) {
            this.mMutator.bind(portraitNode);
        } else {
            this.mCache.append(portraitNode);
        }
    }

    public void isBackgroundOrigin(boolean z) {
        this.mIsBackgroundOrigin = z;
    }

    public void setBitmap(Bitmap bitmap, boolean z) {
        boolean z2 = this.mOriginBitmap == null;
        this.mChangeBackground = checkBackgroundChange(bitmap);
        setBitmap(bitmap);
        if (!z2) {
            this.mBitmapGestureParamsHolder.fixMatrixWithAnim();
        }
    }

    public final boolean checkBackgroundChange(Bitmap bitmap) {
        Bitmap bitmap2 = this.mDisplayBitmap;
        if (bitmap2 == null || bitmap == null) {
            return false;
        }
        this.mBackgroundPreH = bitmap2.getHeight();
        this.mBackgroundPreW = this.mDisplayBitmap.getWidth();
        this.mLastDisplayRect = new RectF(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        return (((float) bitmap.getWidth()) == this.mBackgroundPreW && ((float) bitmap.getHeight()) == this.mBackgroundPreH) ? false : true;
    }

    public void removeNodeByPersonIndex(int i) {
        if (this.mMutator.getItem() != null && this.mMutator.getItem().getPersonIndex() == i) {
            this.mMutator.unbind();
            invalidate();
            return;
        }
        this.mCache.removeIndexByPerson(i);
        invalidate();
    }

    public void removeIndex(int i) {
        bindItemByPersonIndex(i);
        this.mOpState = 5;
        deleteMutator();
    }

    public boolean isInPainting() {
        return this.mBackgroundSegmentCount > 0;
    }

    public void addManualMattingOnLister(OnManualMattingOnLister onManualMattingOnLister) {
        this.mManualMattingLister = onManualMattingOnLister;
    }

    public Portrait export() {
        return new Portrait(this.mBitmapGestureParamsHolder.mBitmapRect, getNodes());
    }

    public PortraitEditView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsMove = false;
        this.mState = State.IDLE;
        this.mPrePoint = new PointF();
        this.mReusePoint = new PointF();
        this.mCanvasToBitmap = new Matrix();
        this.mBitmapToCanvas = new Matrix();
        this.mRectTemp = new RectF();
        this.mMatrixValue = new float[9];
        this.firstValue = new float[9];
        this.mIsBackgroundOrigin = true;
        this.mOpState = 0;
        this.mBackgroundSegmentCount = 0;
        this.mComparatorIndex = 0;
        this.mChangeBackground = false;
        this.mUpdateViewAlpha = -1;
        init();
    }

    private void init() {
        setFeatureGestureListener(new CustomGestureListener());
        this.mRotateDetector = new RotateGestureDetector(getContext(), new GestureListener());
        Bitmap decodeResource = BitmapFactory.decodeResource(getContext().getResources(), R$drawable.magic_matting_transparent);
        this.mTransparent = decodeResource.copy(Bitmap.Config.ARGB_8888, true);
        decodeResource.recycle();
        this.mAlphaShowAnimConfig = new AnimConfig().setEase(18, 1000.0f);
        this.mAlphaHideAnimConfig = new AnimConfig().setEase(18, 1300.0f);
        this.mBackgroundAdaptationHelper = new BackgroundAdaptationHelper();
    }

    /* loaded from: classes2.dex */
    public class GestureListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        public GestureListener() {
            PortraitEditView.this = r1;
        }

        @Override // com.miui.gallery.magic.widget.gesture.RotateGestureDetector.SimpleOnRotateGestureListener, com.miui.gallery.magic.widget.gesture.RotateGestureDetector.OnRotateGestureListener
        public boolean onRotateBegin(RotateGestureDetector rotateGestureDetector) {
            PortraitEditView.this.mStartRotate = rotateGestureDetector.getRotationDegreesDelta();
            return super.onRotateBegin(rotateGestureDetector);
        }

        @Override // com.miui.gallery.magic.widget.gesture.RotateGestureDetector.OnRotateGestureListener
        public boolean onRotate(RotateGestureDetector rotateGestureDetector) {
            float rotationDegreesDelta = rotateGestureDetector.getRotationDegreesDelta();
            float abs = Math.abs(Math.abs(rotationDegreesDelta) - Math.abs(PortraitEditView.this.mStartRotate));
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("旋转角度", "step----：" + abs);
            if (PortraitEditView.this.mMutator.getItem() != null && abs > 0.1f) {
                PortraitEditView.this.mState = State.SCALE;
                PortraitEditView.this.mMutator.rotate(-rotationDegreesDelta);
            }
            PortraitEditView.this.invalidate();
            return true;
        }

        @Override // com.miui.gallery.magic.widget.gesture.RotateGestureDetector.SimpleOnRotateGestureListener, com.miui.gallery.magic.widget.gesture.RotateGestureDetector.OnRotateGestureListener
        public void onRotateEnd(RotateGestureDetector rotateGestureDetector) {
            super.onRotateEnd(rotateGestureDetector);
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger PortraitEditView", "旋转结束----onRotateEnd：" + rotateGestureDetector.getRotationDegreesDelta());
        }
    }

    /* loaded from: classes2.dex */
    public class CustomGestureListener implements BitmapGestureView.FeatureGesListener {
        public float mDownX;
        public float mDownY;
        public float mFirstSpan;
        public float[] mPoint;
        public float mPreScale;
        public Map<String, String> params;

        public CustomGestureListener() {
            PortraitEditView.this = r1;
            this.mPoint = new float[2];
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public boolean onDown(MotionEvent motionEvent) {
            this.mDownX = motionEvent.getX();
            this.mDownY = motionEvent.getY();
            PortraitEditView.this.convertPointToViewPortCoordinate(motionEvent, this.mPoint);
            if (!PortraitEditView.this.mMutator.isIdle()) {
                if (!PortraitEditView.this.mMutator.isScale(this.mDownX, this.mDownY)) {
                    if (!PortraitEditView.this.mMutator.isDelete(this.mDownX, this.mDownY)) {
                        if (!PortraitEditView.this.mMutator.isMirror(this.mDownX, this.mDownY)) {
                            if (!PortraitEditView.this.mMutator.isAdd(this.mDownX, this.mDownY)) {
                                if (PortraitEditView.this.mMutator.contains(this.mDownX, this.mDownY)) {
                                    PortraitEditView.this.mPrePoint.set(this.mDownX, this.mDownY);
                                    PortraitEditView.this.mState = State.PENDING;
                                } else {
                                    PortraitEditView.this.mState = State.IDLE;
                                }
                            } else {
                                PortraitEditView.this.mState = State.ADD;
                                PortraitEditView.this.mMutator.setAddAlpha(153);
                            }
                        } else {
                            PortraitEditView.this.mState = State.MIRROR;
                            PortraitEditView.this.mMutator.setMirrorAlpha(153);
                        }
                    } else {
                        PortraitEditView.this.mState = State.DELETE;
                        PortraitEditView.this.mMutator.setDeleteAlpha(153);
                    }
                } else {
                    PortraitEditView.this.mState = State.MAGIC;
                    PortraitEditView.this.mMutator.setScaleAlpha(153);
                }
            } else {
                PortraitEditView.this.mState = State.IDLE;
            }
            PortraitEditView.this.invalidate();
            return true;
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onSingleTapUp(MotionEvent motionEvent) {
            if (!PortraitEditView.this.mMutator.isIdle() && PortraitEditView.this.mState == State.IDLE) {
                int find = PortraitEditView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
                if (find == -1) {
                    PortraitEditView.this.mCache.append(PortraitEditView.this.mMutator.unbind());
                    PortraitEditView.this.disableChildHandleMode();
                    PortraitEditView.this.invalidate();
                    return;
                }
                PortraitEditView.this.bindItem(find);
                PortraitEditView.this.shinePeople();
                return;
            }
            if (this.params == null) {
                this.params = new HashMap();
            }
            this.params.clear();
            if (PortraitEditView.this.mState != State.MIRROR || !PortraitEditView.this.mMutator.isMirror(motionEvent.getX(), motionEvent.getY())) {
                if (PortraitEditView.this.mState != State.DELETE || !PortraitEditView.this.mMutator.isDelete(motionEvent.getX(), motionEvent.getY())) {
                    if (PortraitEditView.this.mState != State.ADD || !PortraitEditView.this.mMutator.isAdd(motionEvent.getX(), motionEvent.getY())) {
                        if (PortraitEditView.this.mState != State.MAGIC || !PortraitEditView.this.mMutator.isScale(motionEvent.getX(), motionEvent.getY())) {
                            int find2 = PortraitEditView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
                            if (find2 == -1) {
                                return;
                            }
                            PortraitEditView.this.bindItem(find2);
                            PortraitEditView.this.shinePeople();
                            return;
                        }
                        PortraitEditView portraitEditView = PortraitEditView.this;
                        OnManualMattingOnLister onManualMattingOnLister = portraitEditView.mManualMattingLister;
                        if (onManualMattingOnLister != null) {
                            onManualMattingOnLister.onClick(portraitEditView.getPersonIndex());
                        }
                        this.params.put(nexExportFormat.TAG_FORMAT_TYPE, "manual");
                        MagicSampler.getInstance().recordCategory("matting", "operation", this.params);
                        return;
                    } else if (PortraitEditView.this.getNodes().size() >= 20) {
                        return;
                    } else {
                        PortraitEditView portraitEditView2 = PortraitEditView.this;
                        portraitEditView2.add(portraitEditView2.mManualMattingLister.addNewNode(portraitEditView2.mMutator.getItem()));
                        this.params.put(nexExportFormat.TAG_FORMAT_TYPE, "copy");
                        MagicSampler.getInstance().recordCategory("matting", "operation", this.params);
                        return;
                    }
                }
                PortraitEditView.this.deleteMutator();
                this.params.put(nexExportFormat.TAG_FORMAT_TYPE, "delete");
                MagicSampler.getInstance().recordCategory("matting", "operation", this.params);
                return;
            }
            PortraitEditView.this.mMutator.mirror();
            PortraitEditView portraitEditView3 = PortraitEditView.this;
            portraitEditView3.showFirstTransparent(portraitEditView3.mMutator.getItem());
            PortraitEditView.this.mState = State.IDLE;
            PortraitEditView.this.invalidate();
            PortraitEditView.this.changeCanvas(true);
            this.params.put(nexExportFormat.TAG_FORMAT_TYPE, "image");
            MagicSampler.getInstance().recordCategory("matting", "operation", this.params);
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (PortraitEditView.this.mState != State.IDLE || PortraitEditView.this.mMutator.isIdle()) {
                State state = PortraitEditView.this.mState;
                State state2 = State.MOVE;
                if (state != state2) {
                    if (PortraitEditView.this.mState != State.SCALE && motionEvent2.getPointerCount() != 2) {
                        if (PortraitEditView.this.mState == State.PENDING) {
                            PortraitEditView.this.mState = state2;
                        } else if (PortraitEditView.this.mMutator.isIdle()) {
                            int find = PortraitEditView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
                            if (find != -1) {
                                PortraitEditView.this.bindItem(find);
                            }
                            PortraitEditView.this.invalidate();
                        }
                    }
                } else {
                    moveItem(motionEvent2, f, f2);
                }
            } else {
                int find2 = PortraitEditView.this.mCache.find(motionEvent.getX(), motionEvent.getY());
                if (find2 != -1) {
                    PortraitEditView.this.bindItem(find2);
                }
                PortraitEditView.this.mState = State.MOVE;
            }
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger PortraitEditView", "onScroll  mState = " + PortraitEditView.this.mState);
        }

        @Override // com.miui.gallery.widget.imageview.BitmapGestureView.FeatureGesListener
        public void onActionUp(float f, float f2) {
            PortraitEditView portraitEditView = PortraitEditView.this;
            portraitEditView.changeCanvas(portraitEditView.mState == State.MOVE || PortraitEditView.this.mState == State.SCALE);
            PortraitEditView.this.mState = State.IDLE;
            PortraitEditView.this.mMutator.setMirrorAlpha(255);
            PortraitEditView.this.mMutator.setDeleteAlpha(255);
            PortraitEditView.this.mMutator.setScaleAlpha(255);
            PortraitEditView.this.mMutator.setAddAlpha(255);
            PortraitEditView.this.invalidate();
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float currentSpan = scaleGestureDetector.getCurrentSpan() / this.mFirstSpan;
            float f = this.mPreScale;
            float f2 = currentSpan / f;
            this.mPreScale = f * f2;
            if (!PortraitEditView.this.mMutator.isIdle()) {
                float radius = PortraitEditView.this.mMutator.getRadius();
                float currentCanvasScale = PortraitEditView.this.getCurrentCanvasScale();
                float f3 = PortraitEditView.this.mMinRadius * currentCanvasScale;
                float f4 = PortraitEditView.this.mMaxRadius * currentCanvasScale;
                if (f2 <= 1.0f && radius < f3) {
                    return false;
                }
                if (f2 > 1.0f && radius > f4) {
                    return false;
                }
                PortraitEditView.this.mMutator.scale(f2);
                PortraitEditView.this.invalidate();
            }
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            this.mFirstSpan = scaleGestureDetector.getCurrentSpan();
            this.mPreScale = 1.0f;
            PortraitEditView portraitEditView = PortraitEditView.this;
            portraitEditView.showFirstSleepTransparentStart(portraitEditView.mMutator.getItem());
            return false;
        }

        @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            PortraitEditView portraitEditView = PortraitEditView.this;
            portraitEditView.resetImageWithBackground(portraitEditView.mMutator.getItem());
        }

        public final void moveItem(MotionEvent motionEvent, float f, float f2) {
            if (motionEvent.getPointerCount() == 2) {
                return;
            }
            PortraitEditView portraitEditView = PortraitEditView.this;
            portraitEditView.showFirstSleepTransparentStart(portraitEditView.mMutator.getItem());
            PortraitEditView.this.mRectTemp.set(PortraitEditView.this.mMutator.getDrawBounds());
            PortraitEditView portraitEditView2 = PortraitEditView.this;
            int rectOverScrollStatus = portraitEditView2.getRectOverScrollStatus(portraitEditView2.mRectTemp);
            if ((rectOverScrollStatus & 8) == 0 ? !((rectOverScrollStatus & 4) == 0 || f >= 0.0f) : f > 0.0f) {
                f = 0.0f;
            }
            if ((rectOverScrollStatus & 2) == 0 ? !((rectOverScrollStatus & 1) == 0 || f2 >= 0.0f) : f2 > 0.0f) {
                f2 = 0.0f;
            }
            PointF pointF = PortraitEditView.this.mReusePoint;
            MagicLog.INSTANCE.showLog("mReusePoint1: pt:x " + pointF.x + ", y" + pointF.y);
            pointF.set(-f, -f2);
            PortraitEditView.this.resetTransByBounds(pointF);
            MagicLog.INSTANCE.showLog("mReusePoint2: pt:x " + pointF.x + ", y" + pointF.y);
            PortraitEditView.this.mMutator.translate(pointF.x, pointF.y);
            PortraitEditView.this.mPrePoint.set(motionEvent.getX(), motionEvent.getY());
            PortraitEditView portraitEditView3 = PortraitEditView.this;
            portraitEditView3.resetImageWithBackground(portraitEditView3.mMutator.getItem());
            PortraitEditView.this.invalidate();
        }
    }

    public final void resetImageWithBackground(PortraitNode portraitNode) {
        if (portraitNode == null) {
            return;
        }
        for (PortraitNode portraitNode2 : getNodes()) {
            portraitNode2.setScaleResult(null);
        }
    }

    public final void bindItem(int i) {
        this.mState = State.PENDING;
        PortraitNode remove = this.mCache.remove(i);
        this.mCache.append(this.mMutator.unbind());
        int i2 = this.mComparatorIndex;
        this.mComparatorIndex = i2 + 1;
        remove.setShotIndex(i2);
        this.mMutator.bind(remove);
        enableChildHandleMode();
        if (remove.mIsOrigin) {
            this.mMutator.getFirstMatrix(this.firstValue);
        }
        invalidate();
    }

    public void bindItemByPersonIndex(int i) {
        this.mState = State.IDLE;
        PortraitNode removePersonIndex = this.mCache.removePersonIndex(i);
        this.mCache.append(this.mMutator.unbind());
        int i2 = this.mComparatorIndex;
        this.mComparatorIndex = i2 + 1;
        removePersonIndex.setShotIndex(i2);
        this.mMutator.bind(removePersonIndex);
        enableChildHandleMode();
        if (removePersonIndex.mIsOrigin) {
            this.mMutator.getFirstMatrix(this.firstValue);
        }
        invalidate();
    }

    public final void deleteMutator() {
        showFirstTransparent(this.mMutator.getItem());
        this.mMutator.unbind();
        disableChildHandleMode();
        this.mState = State.IDLE;
        changeCanvas(true);
        invalidate();
    }

    public final void showFirstTransparent(PortraitNode portraitNode) {
        if (portraitNode != null && portraitNode.mIsOrigin && this.mIsBackgroundOrigin) {
            portraitNode.mIsOrigin = false;
            this.mCache.drawTransparen(this.mMutator.getItem().mPersonBitmap, this.firstValue, this.mTransparent);
            this.mManualMattingLister.firstMove(this.mMutator.getPersonIndex());
            this.mBackgroundSegmentCount++;
            return;
        }
        this.mIsMove = true;
    }

    public final void showFirstSleepTransparentStart(PortraitNode portraitNode) {
        if (portraitNode != null && portraitNode.mIsOrigin && this.mIsBackgroundOrigin) {
            portraitNode.mIsOrigin = false;
            this.mCache.drawTransparen(this.mMutator.getItem().mPersonBitmap, this.firstValue, this.mTransparent);
            this.mEndTransparent = true;
            return;
        }
        this.mIsMove = true;
    }

    public final void showFirstSleepTransparentEnd() {
        this.mManualMattingLister.firstMove(this.mMutator.getPersonIndex());
        this.mBackgroundSegmentCount++;
    }

    public void changeCanvas(boolean z) {
        if (this.mManualMattingLister == null || !z || this.mMutator == null) {
            return;
        }
        if (this.mEndTransparent) {
            this.mEndTransparent = false;
            showFirstSleepTransparentEnd();
        }
        notifyCanvasChange();
    }

    public final void notifyCanvasChange() {
        long j;
        int i;
        if (this.mMutator.getItem() != null) {
            j = this.mMutator.getItem().mPersonId;
            i = this.mMutator.getItem().getPersonIndex();
        } else {
            j = 0;
            i = 0;
        }
        this.mManualMattingLister.onCanvasMatrixChange(j, getNodes(), i, this.mOpState);
        this.mOpState = 0;
    }

    public void firstAddNode() {
        this.mOpState = 4;
        notifyCanvasChange();
    }

    public List<PortraitNode> getNodes() {
        ArrayList arrayList = new ArrayList();
        if (this.mMutator.getItem() != null) {
            arrayList.add(this.mMutator.getItem());
        }
        arrayList.addAll(this.mCache.getNodes());
        return arrayList;
    }

    public float getCurrentCanvasScale() {
        this.mBitmapGestureParamsHolder.mCanvasMatrix.getValues(this.mMatrixValue);
        return this.mMatrixValue[0];
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mRotateDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
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

    public final PointF getTransByBounds(PortraitNode portraitNode, PointF pointF) {
        RectF rectF = this.mBitmapGestureParamsHolder.mBitmapDisplayRect;
        if ((pointF.x + this.mMutator.getDrawBounds().right) - (this.mMutator.getDrawBounds().width() * 0.5f) > rectF.right) {
            RectF rectF2 = portraitNode.mDrawBounds;
            pointF.x = (-rectF2.left) - (rectF2.width() * 0.5f);
            RectF rectF3 = portraitNode.mDrawBounds;
            pointF.y = (-rectF3.top) - (rectF3.width() * 0.5f);
        }
        if ((pointF.y + this.mMutator.getDrawBounds().bottom) - (this.mMutator.getDrawBounds().height() * 0.5f) > rectF.bottom) {
            RectF rectF4 = portraitNode.mDrawBounds;
            pointF.x = (-rectF4.left) - (rectF4.width() * 0.5f);
            RectF rectF5 = portraitNode.mDrawBounds;
            pointF.y = (-rectF5.top) - (rectF5.width() * 0.5f);
        }
        return pointF;
    }

    public final void add(PortraitNode portraitNode) {
        if (!this.mMutator.isIdle()) {
            this.mCache.append(this.mMutator.unbind());
        }
        portraitNode.init(this.mBitmapGestureParamsHolder.mCanvasMatrixInvert, this.mDisplayBitmap);
        ParcelableMatrix parcelableMatrix = new ParcelableMatrix();
        float[] fArr = new float[9];
        portraitNode.mMatrix.getValues(fArr);
        parcelableMatrix.setValues(fArr);
        portraitNode.mMatrix = parcelableMatrix;
        int i = this.mComparatorIndex;
        this.mComparatorIndex = i + 1;
        portraitNode.setShotIndex(i);
        float currentCanvasScale = getCurrentCanvasScale();
        PointF transByBounds = getTransByBounds(portraitNode, new PointF(50.0f, 50.0f));
        portraitNode.mMatrix.postScale(currentCanvasScale, currentCanvasScale);
        portraitNode.mMatrix.postTranslate(transByBounds.x, transByBounds.y);
        portraitNode.mMatrix.mapRect(portraitNode.mDrawBounds, portraitNode.mImageBounds);
        this.mMutator.bind(portraitNode);
        enableChildHandleMode();
        invalidate();
        new Handler().postDelayed(new Runnable() { // from class: com.miui.gallery.magic.widget.portrait.PortraitEditView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PortraitEditView.m1076$r8$lambda$G5O5LpdJcjhXY6ju4eITgQwAw(PortraitEditView.this);
            }
        }, 200L);
    }

    public /* synthetic */ void lambda$add$0() {
        this.mOpState = 2;
        changeCanvas(true);
    }

    public void addNodeToView(PortraitNode portraitNode) {
        if (!this.mMutator.isIdle()) {
            this.mCache.append(this.mMutator.unbind());
        }
        this.mCache.append(portraitNode);
        enableChildHandleMode();
        invalidate();
    }

    public void add(Bitmap bitmap, long j, Rect rect, int i) {
        if (this.mMutator == null) {
            return;
        }
        RectF rectF = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        RectF rectF2 = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        PortraitNode portraitNode = new PortraitNode(bitmap, j);
        portraitNode.mIsOrigin = true;
        portraitNode.mDrawBounds = rectF;
        portraitNode.mImageBounds = rectF2;
        portraitNode.mMatrix = new ParcelableMatrix();
        int i2 = this.mComparatorIndex;
        this.mComparatorIndex = i2 + 1;
        portraitNode.setShotIndex(i2);
        portraitNode.mMatrix.postTranslate(rect.left, rect.top);
        portraitNode.mMatrix.mapRect(portraitNode.mDrawBounds, portraitNode.mImageBounds);
        portraitNode.setPersonIndex(i);
        PortraitNode.Cache cache = this.mCache;
        if (cache == null) {
            return;
        }
        cache.append(portraitNode);
    }

    public void onStart() {
        this.mMinRadius = getContext().getResources().getDimensionPixelSize(R$dimen.sticker_min_radius);
        this.mMaxRadius = getContext().getResources().getDimensionPixelSize(R$dimen.sticker_max_radius);
        this.mMutator = getMutator();
        this.mCache = getCache();
    }

    private PortraitNode.Mutator getMutator() {
        PortraitNode.Mutator mutator = this.mMutator;
        PortraitNode item = mutator != null ? mutator.getItem() : null;
        PortraitNode.Mutator mutator2 = new PortraitNode.Mutator(getContext(), this.mBitmapToCanvas, this.mCanvasToBitmap);
        this.mMutator = mutator2;
        if (item == null) {
            return mutator2;
        }
        PortraitNode cloneNode = item.cloneNode();
        changeBackgroundAdaptation(cloneNode);
        this.mMutator.bind(cloneNode);
        this.mMutator.lambda$new$0();
        return this.mMutator;
    }

    public final void changeBackgroundAdaptation(PortraitNode portraitNode) {
        if (!this.mChangeBackground) {
            return;
        }
        float width = this.mOriginBitmap.getWidth();
        float height = this.mOriginBitmap.getHeight();
        float f = this.mBackgroundPreW;
        float f2 = this.mBackgroundPreH;
        BackgroundAdaptationHelper.ScaleResult scaleResult = portraitNode.getScaleResult();
        float scale = MagicMatrixUtil.getScale(portraitNode.mMatrix);
        if (scaleResult == null) {
            scaleResult = portraitNode.getScaleResult(this.mBackgroundAdaptationHelper.resetImageWithBackground(portraitNode.mDrawBounds, scale, new SizeF(f, f2)));
        }
        BackgroundAdaptationHelper.ScaleResult calcImagePosition = this.mBackgroundAdaptationHelper.calcImagePosition(new SizeF(width, height), scaleResult);
        float top = calcImagePosition.getTop();
        float left = calcImagePosition.getLeft();
        float scale2 = calcImagePosition.getScale();
        portraitNode.mDrawBounds = new RectF(left, top, portraitNode.mImageBounds.width() + left, portraitNode.mImageBounds.height() + top);
        float f3 = scale2 / scale;
        float[] translate = MagicMatrixUtil.getTranslate(portraitNode.mMatrix);
        portraitNode.mMatrix.postTranslate(-translate[0], -translate[1]);
        portraitNode.mMatrix.postScale(f3, f3);
        portraitNode.mMatrix.postTranslate(left, top);
        portraitNode.mMatrix.mapRect(portraitNode.mDrawBounds, portraitNode.mImageBounds);
    }

    private PortraitNode.Cache getCache() {
        PortraitNode.Cache cache = this.mCache;
        if (cache == null) {
            return new PortraitNode.Cache(this.mDisplayBitmap, this.mCanvasToBitmap, this.mBitmapToCanvas);
        }
        List<PortraitNode> nodes = cache.getNodes();
        PortraitNode.Cache cache2 = new PortraitNode.Cache(this.mDisplayBitmap, this.mCanvasToBitmap, this.mBitmapToCanvas);
        this.mCache = cache2;
        if (nodes == null) {
            return cache2;
        }
        for (PortraitNode portraitNode : nodes) {
            changeBackgroundAdaptation(portraitNode);
            this.mCache.append(portraitNode);
        }
        return this.mCache;
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onBitmapMatrixChanged() {
        refreshMatrix();
        onStart();
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureView
    public void onCanvasMatrixChange() {
        PortraitNode.Mutator mutator = this.mMutator;
        if (mutator != null && !mutator.isIdle()) {
            this.mMutator.lambda$new$0();
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
        if (this.mCache == null && this.mMutator == null) {
            return;
        }
        MagicLog.INSTANCE.showLog("MagicLogger PortraitEditView", "drawChild method mState " + this.mState);
        canvas.save();
        canvas.clipRect(this.mBitmapGestureParamsHolder.mBitmapDisplayRect);
        this.mCache.draw(canvas);
        canvas.restore();
        if (this.mMutator == null) {
            return;
        }
        boolean z = true;
        if (getNodes().size() == 20) {
            this.mMutator.setDisShow(true);
        } else {
            this.mMutator.setDisShow(false);
        }
        PortraitNode.Mutator mutator = this.mMutator;
        State state = this.mState;
        boolean z2 = state == State.PENDING;
        if (state != State.MOVE && state != State.SCALE) {
            z = false;
        }
        mutator.draw(canvas, z2, z, this.mUpdateViewAlpha);
    }

    public void clear() {
        PortraitNode.Mutator mutator = this.mMutator;
        if (mutator != null && !mutator.isIdle()) {
            this.mCache.append(this.mMutator.unbind());
        }
        this.mCache.clear();
        this.mState = State.IDLE;
        invalidate();
    }

    /* loaded from: classes2.dex */
    public static class Portrait implements Parcelable {
        public static final Parcelable.Creator<Portrait> CREATOR = new Parcelable.Creator<Portrait>() { // from class: com.miui.gallery.magic.widget.portrait.PortraitEditView.Portrait.1
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Portrait mo1077createFromParcel(Parcel parcel) {
                return new Portrait(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Portrait[] mo1078newArray(int i) {
                return new Portrait[i];
            }
        };
        public List<PortraitNode> mItems;
        public RectF mPreviewBounds;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public Portrait(RectF rectF, List<PortraitNode> list) {
            this.mPreviewBounds = new RectF(rectF);
            this.mItems = list;
        }

        public List<PortraitNode> getItems() {
            return this.mItems;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mPreviewBounds, i);
            parcel.writeList(this.mItems);
        }

        public Portrait(Parcel parcel) {
            this.mPreviewBounds = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
            ArrayList arrayList = new ArrayList();
            this.mItems = arrayList;
            parcel.readList(arrayList, PortraitNode.class.getClassLoader());
        }
    }

    public final void shinePeople() {
        IStateStyle iStateStyle = this.mStateStyle;
        if (iStateStyle != null) {
            iStateStyle.cancel();
        }
        this.mStateStyle = Folme.useValue(new Object[0]).addListener(new TransitionListener() { // from class: com.miui.gallery.magic.widget.portrait.PortraitEditView.1
            {
                PortraitEditView.this = this;
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                super.onUpdate(obj, collection);
                UpdateInfo findByName = UpdateInfo.findByName(collection, "alpha");
                if (findByName != null) {
                    PortraitEditView.this.viewAlpha = findByName.getIntValue();
                    PortraitEditView portraitEditView = PortraitEditView.this;
                    portraitEditView.updateViewAlpha(portraitEditView.viewAlpha);
                    PortraitEditView.this.invalidate();
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                if (obj.toString().equals("hide")) {
                    PortraitEditView.this.hintView();
                }
                PortraitEditView.this.invalidate();
            }
        }).set("start").add("alpha", 0).set("show").add("alpha", 153).set("hide").add("alpha", 0).setTo("start").to("show", this.mAlphaShowAnimConfig).then("hide", this.mAlphaHideAnimConfig);
    }

    public final void updateViewAlpha(int i) {
        this.mUpdateViewAlpha = i;
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("updateViewAlpha: " + i);
    }

    public final void hintView() {
        this.mUpdateViewAlpha = -1;
        MagicLog.INSTANCE.showLog("updateViewAlpha: hintView");
    }
}
