package com.miui.gallery.editor.photo.screen.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import com.miui.gallery.editor.photo.core.common.model.BaseDrawNode;
import com.miui.gallery.editor.photo.core.common.model.IDrawNode;
import com.miui.gallery.editor.photo.core.imports.doodle.DoodleManager;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleShapeNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode;
import com.miui.gallery.editor.photo.core.imports.longcrop.LongBitmapDrawable;
import com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView;
import com.miui.gallery.editor.photo.screen.base.IScreenOperation;
import com.miui.gallery.editor.photo.screen.base.IScreenOperationEditor;
import com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView;
import com.miui.gallery.editor.photo.screen.core.ScreenDrawEntry;
import com.miui.gallery.editor.photo.screen.core.ScreenProviderManager;
import com.miui.gallery.editor.photo.screen.core.ScreenRenderData;
import com.miui.gallery.editor.photo.screen.crop.ScreenCropEntry;
import com.miui.gallery.editor.photo.screen.crop.ScreenCropView;
import com.miui.gallery.editor.photo.screen.doodle.ScreenDoodleView;
import com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper;
import com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicProvider;
import com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicView;
import com.miui.gallery.editor.photo.screen.shell.IScreenShellOperation;
import com.miui.gallery.editor.photo.screen.shell.ScreenShellEntry;
import com.miui.gallery.editor.photo.screen.shell.ScreenShellView;
import com.miui.gallery.editor.photo.screen.text.ScreenTextDrawNode;
import com.miui.gallery.editor.photo.screen.text.ScreenTextView;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class ScreenEditorView extends ScreenBaseGestureView implements IScreenEditorController {
    public ScreenEditViewAnimatorHelper mAnimatorHelper;
    public Bitmap mBaseBitmap;
    public Canvas mBaseCanvas;
    public Bitmap mBaseTempBitmap;
    public Canvas mBaseTempCanvas;
    public Paint mClearPaint;
    public IScreenOperationEditor mCurrentScreenEditor;
    public RectF mDisplayInOriginBitmapRect;
    public DoodlePen mDoodlePen;
    public List<BaseDrawNode> mDrawNodeList;
    public Bitmap mErasableBitmap;
    public Canvas mErasableCanvas;
    public Bitmap[] mErasableCanvasSnapshots;
    public boolean mIsLongCrop;
    public boolean mIsShowHistoryNodeSnapshot;
    public List<BaseDrawNode> mLastNodeList;
    public IScreenOperationEditor mLastScreenEditor;
    public LongScreenshotCropEditorView.Entry mLongCropEntry;
    public Map<IDrawNode, Integer> mNodeIntegerMap;
    public Canvas mNonErasabCanvas;
    public Bitmap mNonErasableBitmap;
    public Bitmap[] mNonErasableCanvasSnapshots;
    public OnScreenCropStatusChangeListener mOnScreenCropStatusChangeListener;
    public OperationUpdateListener mOperationUpdateListener;
    public RectF mOriginBitmapRectF;
    public RectF mOriginBitmapRenderRectF;
    public Drawable mOriginalBmp;
    public Paint mPaint;
    public LinkedList<BaseDrawNode> mRevokedNodeList;
    public List<BaseDrawNode> mSavingNodeList;
    public ScreenCropView mScreenCrop;
    public ScreenDoodleView mScreenDoodle;
    public ScreenEditViewAnimatorHelper.Callback mScreenEditViewAnimatorCallback;
    public ScreenMosaicView mScreenMosaic;
    public ScreenShellView mScreenShell;
    public ScreenTextView mScreenText;
    public ThumbnailAnimatorCallback mThumbnailAnimatorCallback;
    public Bitmap mTmpBitmap;
    public Canvas mTmpBitmapCanvas;
    public int mTopPixel;

    public static /* synthetic */ void $r8$lambda$ZmzthL5kCN__ih0Upy_d9qgiT2c(ScreenEditorView screenEditorView, BaseDrawNode baseDrawNode) {
        screenEditorView.lambda$drawNode$0(baseDrawNode);
    }

    /* renamed from: $r8$lambda$p_rV27-BTJKCx_v5iaP87TzBKyk */
    public static /* synthetic */ void m929$r8$lambda$p_rV27BTJKCx_v5iaP87TzBKyk(ScreenEditorView screenEditorView, List list, ObservableEmitter observableEmitter) {
        screenEditorView.lambda$drawHistoryNodeSnapshot$1(list, observableEmitter);
    }

    public static /* synthetic */ void lambda$drawHistoryNodeSnapshot$2(Bitmap bitmap) throws Exception {
    }

    public ScreenEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDrawNodeList = new ArrayList();
        this.mRevokedNodeList = new LinkedList<>();
        this.mSavingNodeList = new ArrayList();
        this.mLastNodeList = new ArrayList();
        this.mLongCropEntry = new LongScreenshotCropEditorView.Entry();
        this.mPaint = new Paint();
        this.mClearPaint = new Paint();
        this.mNodeIntegerMap = new HashMap();
        this.mScreenEditViewAnimatorCallback = new ScreenEditViewAnimatorHelper.Callback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorView.1
            {
                ScreenEditorView.this = this;
            }

            @Override // com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper.Callback
            public Bitmap getOriginBitmap() {
                return ScreenEditorView.this.mOriginBitmap;
            }

            @Override // com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper.Callback
            public void onInvalidate() {
                ScreenEditorView.this.invalidate();
            }

            @Override // com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper.Callback
            public RectF getShowRect() {
                return ScreenEditorView.this.getBitmapGestureParamsHolder().mBitmapDisplayInitRect;
            }

            @Override // com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper.Callback
            public void onAnimationUpdate(float f) {
                if (ScreenEditorView.this.mThumbnailAnimatorCallback != null) {
                    ScreenEditorView.this.mThumbnailAnimatorCallback.onAnimationUpdate(f);
                }
            }

            @Override // com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper.Callback
            public void onAnimationStart() {
                if (ScreenEditorView.this.mThumbnailAnimatorCallback != null) {
                    ScreenEditorView.this.mThumbnailAnimatorCallback.onAnimationStart();
                }
            }
        };
    }

    public void init() {
        this.mErasableCanvasSnapshots = new Bitmap[2];
        this.mNonErasableCanvasSnapshots = new Bitmap[2];
        this.mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        if (!this.mIsLongCrop) {
            ScreenCropView screenCropView = new ScreenCropView(this);
            this.mScreenCrop = screenCropView;
            screenCropView.setOnCropStatusChangeListener(this.mOnScreenCropStatusChangeListener);
            setCurrentScreenEditor(6);
        } else {
            setCurrentScreenEditor(2);
        }
        disableChildHandleMode();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void startScreenThumbnailAnimate(ThumbnailAnimatorCallback thumbnailAnimatorCallback) {
        if (this.mAnimatorHelper == null) {
            this.mAnimatorHelper = new ScreenEditViewAnimatorHelper();
        }
        this.mThumbnailAnimatorCallback = thumbnailAnimatorCallback;
        this.mAnimatorHelper.startEnterAnimation(getContext(), this.mScreenEditViewAnimatorCallback, thumbnailAnimatorCallback.getThumbnailRect());
    }

    @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView
    public void onActionUp() {
        if (this.mDrawNodeList.size() > 0) {
            Map<IDrawNode, Integer> map = this.mNodeIntegerMap;
            List<BaseDrawNode> list = this.mDrawNodeList;
            map.put(list.get(list.size() - 1), 1);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView, android.view.View
    public void onDraw(Canvas canvas) {
        Bitmap bitmap = this.mDisplayBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        ScreenEditViewAnimatorHelper screenEditViewAnimatorHelper = this.mAnimatorHelper;
        if (screenEditViewAnimatorHelper != null && !screenEditViewAnimatorHelper.isAnimatorEnd()) {
            this.mAnimatorHelper.draw(canvas);
            return;
        }
        savedHistoryNodeSnapshot();
        canvas.save();
        ScreenShellView screenShellView = this.mScreenShell;
        if (screenShellView != null && screenShellView.isWithShell()) {
            this.mScreenShell.clipCanvas(canvas);
            this.mScreenShell.drawTopBlackView(canvas);
        }
        Drawable drawable = this.mOriginalBmp;
        if (drawable != null && this.mIsLongCrop) {
            this.mScreenCrop.drawOriginalBmp(canvas, drawable);
        }
        canvas.concat(this.mBitmapGestureParamsHolder.mCanvasMatrix);
        canvas.drawBitmap(this.mDisplayBitmap, this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix, this.mBitmapPaint);
        canvas.clipRect(getBitmapGestureParamsHolder().mBitmapDisplayInitRect);
        drawNode(canvas);
        this.mIsShowHistoryNodeSnapshot = false;
        IScreenOperationEditor iScreenOperationEditor = this.mCurrentScreenEditor;
        ScreenTextView screenTextView = this.mScreenText;
        if (iScreenOperationEditor == screenTextView) {
            screenTextView.drawCurrentNode(this.mNonErasabCanvas, getDisplayInOriginBitmapRectF());
        }
        canvas.restore();
        this.mCurrentScreenEditor.drawOverlay(canvas);
        ScreenShellView screenShellView2 = this.mScreenShell;
        if (screenShellView2 != null && this.mCurrentScreenEditor != screenShellView2) {
            screenShellView2.drawOverlay(canvas);
        }
        ScreenCropView screenCropView = this.mScreenCrop;
        if (screenCropView == null || this.mCurrentScreenEditor == screenCropView) {
            return;
        }
        ScreenShellView screenShellView3 = this.mScreenShell;
        screenCropView.setShellMargin((screenShellView3 == null || !screenShellView3.isWithShell()) ? null : this.mScreenShell.getShellFitMargin());
        this.mScreenCrop.drawOverlay(canvas);
    }

    public void setOriginalBitmap(Bitmap bitmap, float f, float f2) {
        this.mOriginalBmp = new LongBitmapDrawable(bitmap);
        Rect rect = new Rect();
        rect.left = 0;
        rect.right = bitmap.getWidth();
        rect.top = Math.round(bitmap.getHeight() * f);
        rect.bottom = Math.round(bitmap.getHeight() * f2);
        this.mOriginalBmp.setBounds(rect);
    }

    public final void drawNode(Canvas canvas) {
        showNonErasableHistoryNodeSnapshot();
        if (this.mIsShowHistoryNodeSnapshot) {
            this.mDrawNodeList.forEach(new Consumer() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorView$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScreenEditorView.$r8$lambda$ZmzthL5kCN__ih0Upy_d9qgiT2c(ScreenEditorView.this, (BaseDrawNode) obj);
                }
            });
            showErasableHistoryNodeSnapshot();
        }
        canvas.drawBitmap(this.mErasableBitmap, this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix, this.mPaint);
        for (BaseDrawNode baseDrawNode : this.mDrawNodeList) {
            this.mPaint.setAlpha(255);
            baseDrawNode.setBitmapRects(getOriginBitmapRectF(), getDisplayInOriginBitmapRectF());
            int intValue = this.mNodeIntegerMap.get(baseDrawNode).intValue();
            int i = 3;
            if (intValue == 1) {
                this.mPaint.setAlpha(baseDrawNode.getAlpha());
                if (isShape(baseDrawNode) || isText(baseDrawNode)) {
                    baseDrawNode.draw(this.mNonErasabCanvas, this.mOriginBitmapRectF);
                } else {
                    this.mErasableCanvas.drawBitmap(this.mTmpBitmap, 0.0f, 0.0f, this.mPaint);
                    this.mTmpBitmapCanvas.drawPaint(this.mClearPaint);
                    this.mNodeIntegerMap.put(baseDrawNode, 3);
                }
            } else if (intValue == 0 || intValue == 2) {
                boolean z = baseDrawNode.getDoodlePen() != null && baseDrawNode.getDoodlePen().isEraser();
                if (isShape(baseDrawNode) || intValue == 2) {
                    this.mTmpBitmapCanvas.drawPaint(this.mClearPaint);
                }
                this.mTmpBitmapCanvas.save();
                baseDrawNode.draw((!z || isShape(baseDrawNode)) ? this.mTmpBitmapCanvas : this.mErasableCanvas, this.mOriginBitmapRectF);
                this.mPaint.setAlpha(baseDrawNode.getAlpha());
                this.mTmpBitmapCanvas.restore();
                if (intValue == 2) {
                    (isShape(baseDrawNode) ? this.mNonErasabCanvas : this.mErasableCanvas).drawBitmap(this.mTmpBitmap, 0.0f, 0.0f, this.mPaint);
                    this.mTmpBitmapCanvas.drawPaint(this.mClearPaint);
                    Map<IDrawNode, Integer> map = this.mNodeIntegerMap;
                    if (isShape(baseDrawNode) || (baseDrawNode instanceof ScreenTextDrawNode)) {
                        i = 1;
                    }
                    map.put(baseDrawNode, Integer.valueOf(i));
                } else {
                    canvas.drawBitmap(this.mTmpBitmap, this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix, this.mPaint);
                }
            }
        }
        this.mPaint.setAlpha(255);
        canvas.drawBitmap(this.mNonErasableBitmap, this.mBitmapGestureParamsHolder.mBitmapToDisplayMatrix, this.mPaint);
    }

    public /* synthetic */ void lambda$drawNode$0(BaseDrawNode baseDrawNode) {
        baseDrawNode.reset();
        this.mNodeIntegerMap.put(baseDrawNode, 2);
        baseDrawNode.setBitmapRects(getOriginBitmapRectF(), getDisplayInOriginBitmapRectF());
    }

    public final void savedHistoryNodeSnapshot() {
        if (this.mDrawNodeList.size() > 10) {
            List<BaseDrawNode> list = this.mDrawNodeList;
            BaseDrawNode remove = list.remove(list.size() - 1);
            if (this.mSavingNodeList.isEmpty()) {
                this.mErasableCanvasSnapshots[0] = this.mErasableBitmap.copy(Bitmap.Config.ARGB_8888, true);
                this.mNonErasableCanvasSnapshots[0] = this.mNonErasableBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
            if (this.mSavingNodeList.size() == 10) {
                this.mErasableCanvasSnapshots[1] = this.mErasableBitmap.copy(Bitmap.Config.ARGB_8888, true);
                this.mNonErasableCanvasSnapshots[1] = this.mNonErasableBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
            if (this.mSavingNodeList.size() > 10) {
                Bitmap[] bitmapArr = this.mErasableCanvasSnapshots;
                bitmapArr[0] = bitmapArr[1];
                bitmapArr[1] = this.mErasableBitmap.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap[] bitmapArr2 = this.mNonErasableCanvasSnapshots;
                bitmapArr2[0] = bitmapArr2[1];
                bitmapArr2[1] = this.mNonErasableBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
            this.mSavingNodeList.addAll(this.mDrawNodeList);
            this.mDrawNodeList.clear();
            this.mDrawNodeList.add(remove);
        }
    }

    public final void drawHistoryNodeSnapshot(final List<BaseDrawNode> list) {
        Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditorView$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ScreenEditorView.m929$r8$lambda$p_rV27BTJKCx_v5iaP87TzBKyk(ScreenEditorView.this, list, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(ScreenEditorView$$ExternalSyntheticLambda1.INSTANCE);
    }

    public /* synthetic */ void lambda$drawHistoryNodeSnapshot$1(List list, ObservableEmitter observableEmitter) throws Exception {
        this.mBaseCanvas.drawPaint(this.mClearPaint);
        ArrayList<BaseDrawNode> arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            BaseDrawNode baseDrawNode = (BaseDrawNode) it.next();
            baseDrawNode.reset();
            this.mNodeIntegerMap.put(baseDrawNode, 2);
            if (isShape(baseDrawNode)) {
                arrayList.add(baseDrawNode);
            } else {
                this.mPaint.setAlpha(255);
                this.mBaseTempCanvas.save();
                this.mBaseTempCanvas.drawPaint(this.mClearPaint);
                baseDrawNode.draw(this.mBaseTempCanvas, this.mOriginBitmapRenderRectF);
                this.mBaseTempCanvas.restore();
                this.mPaint.setAlpha(baseDrawNode.getAlpha());
                this.mBaseCanvas.drawBitmap(this.mBaseTempBitmap, 0.0f, 0.0f, this.mPaint);
                this.mNodeIntegerMap.put(baseDrawNode, 3);
            }
        }
        this.mErasableCanvasSnapshots[0] = this.mBaseBitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.mBaseCanvas.drawPaint(this.mClearPaint);
        for (BaseDrawNode baseDrawNode2 : arrayList) {
            this.mPaint.setAlpha(255);
            this.mBaseTempCanvas.save();
            this.mBaseTempCanvas.drawPaint(this.mClearPaint);
            baseDrawNode2.draw(this.mBaseTempCanvas, this.mOriginBitmapRenderRectF);
            this.mBaseTempCanvas.restore();
            this.mPaint.setAlpha(baseDrawNode2.getAlpha());
            this.mBaseCanvas.drawBitmap(this.mBaseTempBitmap, 0.0f, 0.0f, this.mPaint);
        }
        this.mNonErasableCanvasSnapshots[0] = this.mBaseBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public final void showErasableHistoryNodeSnapshot() {
        this.mPaint.setAlpha(255);
        this.mErasableCanvas.drawPaint(this.mClearPaint);
        Bitmap[] bitmapArr = this.mErasableCanvasSnapshots;
        if (bitmapArr[1] != null) {
            this.mErasableCanvas.drawBitmap(bitmapArr[1], 0.0f, 0.0f, this.mPaint);
        } else if (bitmapArr[0] == null) {
        } else {
            this.mErasableCanvas.drawBitmap(bitmapArr[0], 0.0f, 0.0f, this.mPaint);
        }
    }

    public final void showNonErasableHistoryNodeSnapshot() {
        this.mPaint.setAlpha(255);
        this.mNonErasabCanvas.drawPaint(this.mClearPaint);
        Bitmap[] bitmapArr = this.mNonErasableCanvasSnapshots;
        if (bitmapArr[1] != null) {
            this.mNonErasabCanvas.drawBitmap(bitmapArr[1], 0.0f, 0.0f, this.mPaint);
        } else if (bitmapArr[0] == null) {
        } else {
            this.mNonErasabCanvas.drawBitmap(bitmapArr[0], 0.0f, 0.0f, this.mPaint);
        }
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
    public void onBitmapMatrixChanged() {
        this.mIsShowHistoryNodeSnapshot = true;
        ScreenCropView screenCropView = this.mScreenCrop;
        if (screenCropView != null) {
            screenCropView.onStart();
            this.mScreenCrop.bitmapMatrixChange();
        }
        ScreenTextView screenTextView = this.mScreenText;
        if (screenTextView != null) {
            screenTextView.bitmapMatrixChange();
        }
        ScreenDoodleView screenDoodleView = this.mScreenDoodle;
        if (screenDoodleView != null) {
            screenDoodleView.bitmapMatrixChange();
        }
        invalidate();
    }

    @Override // com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.ParamsChangeListener
    public void onCanvasMatrixChange() {
        this.mCurrentScreenEditor.canvasMatrixChange();
        ScreenCropView screenCropView = this.mScreenCrop;
        if (screenCropView != null) {
            screenCropView.canvasMatrixChange();
        }
        invalidate();
        notifyOperationUpdate();
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ScreenCropView screenCropView = this.mScreenCrop;
        if (screenCropView != null) {
            screenCropView.onDetachedFromWindow();
        }
        ScreenTextView screenTextView = this.mScreenText;
        if (screenTextView != null) {
            screenTextView.onDetachedFromWindow();
        }
        ScreenDoodleView screenDoodleView = this.mScreenDoodle;
        if (screenDoodleView != null) {
            screenDoodleView.onDetachedFromWindow();
        }
        ScreenMosaicView screenMosaicView = this.mScreenMosaic;
        if (screenMosaicView != null) {
            screenMosaicView.onDetachedFromWindow();
        }
        this.mThumbnailAnimatorCallback = null;
    }

    public RectF getOriginBitmapRectF() {
        return this.mOriginBitmapRectF;
    }

    public RectF getDisplayInOriginBitmapRectF() {
        return this.mDisplayInOriginBitmapRect;
    }

    public void addDrawNode(BaseDrawNode baseDrawNode) {
        this.mDrawNodeList.add(baseDrawNode);
        this.mNodeIntegerMap.put(baseDrawNode, Integer.valueOf(isText(baseDrawNode) ? 1 : 0));
        baseDrawNode.setBitmapRects(this.mOriginBitmapRectF, this.mDisplayInOriginBitmapRect);
        clearRevokeNode();
        notifyOperationUpdate();
    }

    public void removeDrawNode(BaseDrawNode baseDrawNode) {
        this.mDrawNodeList.remove(baseDrawNode);
        notifyOperationUpdate();
    }

    public void addRevokedDrawNode(BaseDrawNode baseDrawNode) {
        this.mRevokedNodeList.add(baseDrawNode);
    }

    public void removeRevokedDrawNode(BaseDrawNode baseDrawNode) {
        this.mRevokedNodeList.remove(baseDrawNode);
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public boolean isModified() {
        ScreenCropView screenCropView;
        ScreenShellView screenShellView;
        return this.mDrawNodeList.size() > 0 || this.mSavingNodeList.size() > 0 || ((screenCropView = this.mScreenCrop) != null && screenCropView.isModified()) || ((screenShellView = this.mScreenShell) != null && screenShellView.isWithShell());
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void checkTextEditor(boolean z) {
        IScreenOperationEditor iScreenOperationEditor = this.mCurrentScreenEditor;
        ScreenTextView screenTextView = this.mScreenText;
        if (iScreenOperationEditor == screenTextView) {
            screenTextView.onChangeOperation(!z);
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void export() {
        IScreenOperationEditor iScreenOperationEditor = this.mCurrentScreenEditor;
        ScreenTextView screenTextView = this.mScreenText;
        if (iScreenOperationEditor == screenTextView) {
            screenTextView.onChangeOperation(false);
        }
    }

    public boolean isCanDoSaveOperation() {
        ScreenCropView screenCropView = this.mScreenCrop;
        return (screenCropView == null || !screenCropView.isCropAnimatorFinished() || this.mState == ScreenBaseGestureView.State.SCALE_MOVE) ? false : true;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public boolean isModifiedBaseLast() {
        boolean z = this.mLastNodeList.size() != this.mDrawNodeList.size() + this.mSavingNodeList.size() || !this.mLastNodeList.containsAll(this.mDrawNodeList) || !this.mLastNodeList.containsAll(this.mSavingNodeList);
        this.mLastNodeList.clear();
        this.mLastNodeList.addAll(this.mDrawNodeList);
        this.mLastNodeList.addAll(this.mSavingNodeList);
        if (z) {
            return true;
        }
        ScreenCropView screenCropView = this.mScreenCrop;
        if (screenCropView != null && screenCropView.isModifiedBaseLast()) {
            return true;
        }
        ScreenShellView screenShellView = this.mScreenShell;
        return screenShellView != null && screenShellView.isWithShell();
    }

    public boolean canRevoke() {
        IScreenOperationEditor iScreenOperationEditor = this.mCurrentScreenEditor;
        ScreenTextView screenTextView = this.mScreenText;
        return (iScreenOperationEditor == screenTextView && screenTextView.canRevoke()) || !this.mDrawNodeList.isEmpty() || !this.mSavingNodeList.isEmpty();
    }

    public boolean canRevert() {
        return !this.mRevokedNodeList.isEmpty();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void doRevoke() {
        this.mCurrentScreenEditor.clearActivation();
        IScreenOperationEditor iScreenOperationEditor = this.mCurrentScreenEditor;
        ScreenTextView screenTextView = this.mScreenText;
        if (iScreenOperationEditor == screenTextView && screenTextView.canRevoke()) {
            this.mScreenText.doRevoke();
            notifyOperationUpdate();
            return;
        }
        if (this.mDrawNodeList.size() <= 0) {
            if (this.mSavingNodeList.size() >= 10) {
                if (this.mSavingNodeList.size() == 10) {
                    this.mErasableCanvasSnapshots[0] = null;
                    this.mNonErasableCanvasSnapshots[0] = null;
                }
                if (this.mSavingNodeList.size() == 20) {
                    this.mErasableCanvasSnapshots[1] = null;
                    this.mNonErasableCanvasSnapshots[1] = null;
                }
                if (this.mSavingNodeList.size() >= 30) {
                    Bitmap[] bitmapArr = this.mErasableCanvasSnapshots;
                    bitmapArr[1] = bitmapArr[0];
                    Bitmap[] bitmapArr2 = this.mNonErasableCanvasSnapshots;
                    bitmapArr2[1] = bitmapArr2[0];
                    bitmapArr[0] = null;
                    bitmapArr2[0] = null;
                    List<BaseDrawNode> list = this.mSavingNodeList;
                    drawHistoryNodeSnapshot(list.subList(0, list.size() - 20));
                }
                List<BaseDrawNode> list2 = this.mDrawNodeList;
                List<BaseDrawNode> list3 = this.mSavingNodeList;
                list2.addAll(list3.subList(list3.size() - 10, this.mSavingNodeList.size()));
                List<BaseDrawNode> list4 = this.mSavingNodeList;
                this.mSavingNodeList = list4.subList(0, list4.size() - 10);
            } else {
                this.mDrawNodeList.addAll(this.mSavingNodeList);
                this.mSavingNodeList.clear();
            }
            refreshDisplayCanvas();
        }
        List<BaseDrawNode> list5 = this.mDrawNodeList;
        BaseDrawNode baseDrawNode = list5.get(list5.size() - 1);
        this.mDrawNodeList.remove(baseDrawNode);
        this.mRevokedNodeList.add(baseDrawNode);
        this.mIsShowHistoryNodeSnapshot = true;
        invalidate();
        notifyOperationUpdate();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void doRevert() {
        this.mCurrentScreenEditor.clearActivation();
        BaseDrawNode removeLast = this.mRevokedNodeList.removeLast();
        if (this.mCurrentScreenEditor == this.mScreenText && (removeLast instanceof ScreenTextDrawNode) && !((ScreenTextDrawNode) removeLast).isSaved()) {
            this.mScreenText.doRevert();
            notifyOperationUpdate();
            return;
        }
        this.mDrawNodeList.add(removeLast);
        this.mIsShowHistoryNodeSnapshot = true;
        invalidate();
        notifyOperationUpdate();
    }

    public final void clearRevokeNode() {
        this.mRevokedNodeList.clear();
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public ScreenRenderData onExport() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mSavingNodeList);
        arrayList.addAll(this.mDrawNodeList);
        ScreenDrawEntry screenDrawEntry = new ScreenDrawEntry(this.mIsLongCrop, this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect, arrayList);
        ScreenCropView screenCropView = this.mScreenCrop;
        ScreenShellEntry screenShellEntry = null;
        ScreenCropEntry export = screenCropView == null ? null : screenCropView.export();
        ScreenShellView screenShellView = this.mScreenShell;
        if (screenShellView != null && screenShellView.isWithShell()) {
            screenShellEntry = this.mScreenShell.export();
        }
        return new ScreenRenderData(screenDrawEntry, export, screenShellEntry);
    }

    public IScreenOperationEditor getCurrentScreenEditor() {
        return this.mCurrentScreenEditor;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setOperationUpdateListener(OperationUpdateListener operationUpdateListener) {
        this.mOperationUpdateListener = operationUpdateListener;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setOnCropStatusChangeListener(OnScreenCropStatusChangeListener onScreenCropStatusChangeListener) {
        this.mOnScreenCropStatusChangeListener = onScreenCropStatusChangeListener;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setLongCrop(boolean z) {
        this.mIsLongCrop = z;
        ScreenCropView screenCropView = this.mScreenCrop;
        if (screenCropView != null) {
            screenCropView.setIsLongCrop(z);
        }
        if (this.mIsLongCrop) {
            enableChildHandleMode();
        }
    }

    public void notifyOperationUpdate() {
        OperationUpdateListener operationUpdateListener = this.mOperationUpdateListener;
        if (operationUpdateListener != null) {
            operationUpdateListener.onOperationUpdate(isModified(), canRevoke(), canRevert());
        }
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setPreviewBitmap(Bitmap bitmap) {
        this.mOriginBitmap = bitmap;
        this.mTopPixel = (int) ((bitmap.getHeight() * this.mLongCropEntry.mTopRatio) + 0.5f);
        int height = ((int) ((bitmap.getHeight() * this.mLongCropEntry.mBottomRatio) + 0.5f)) - this.mTopPixel;
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), height, Bitmap.Config.ARGB_8888);
        this.mDisplayBitmap = createBitmap;
        this.mBitmapGestureParamsHolder.setBitmap(createBitmap);
        this.mDisplayCanvas = new Canvas(this.mDisplayBitmap);
        this.mTmpBitmap = Bitmap.createBitmap(bitmap.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mTmpBitmap);
        this.mTmpBitmapCanvas = canvas;
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Bitmap createBitmap2 = Bitmap.createBitmap(bitmap.getWidth(), height, Bitmap.Config.ARGB_8888);
        this.mErasableBitmap = createBitmap2;
        this.mBitmapGestureParamsHolder.setBitmap(createBitmap2);
        this.mErasableCanvas = new Canvas(this.mErasableBitmap);
        this.mNonErasableBitmap = Bitmap.createBitmap(bitmap.getWidth(), height, Bitmap.Config.ARGB_8888);
        this.mNonErasabCanvas = new Canvas(this.mNonErasableBitmap);
        this.mBaseBitmap = Bitmap.createBitmap(bitmap.getWidth(), height, Bitmap.Config.ARGB_8888);
        this.mBaseCanvas = new Canvas(this.mBaseBitmap);
        this.mBaseTempBitmap = Bitmap.createBitmap(bitmap.getWidth(), height, Bitmap.Config.ARGB_8888);
        this.mBaseTempCanvas = new Canvas(this.mBaseTempBitmap);
        updateBitmapRect();
        refreshDisplayCanvas();
    }

    @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView
    public void refreshDisplayCanvas() {
        this.mDisplayCanvas.drawBitmap(this.mOriginBitmap, 0.0f, -this.mTopPixel, this.mBitmapPaint);
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setLongCropEntry(LongScreenshotCropEditorView.Entry entry) {
        if (!this.mLongCropEntry.equals(entry)) {
            this.mLongCropEntry = entry;
            setPreviewBitmap(this.mOriginBitmap);
        }
    }

    public final void updateBitmapRect() {
        if (this.mOriginBitmap == null) {
            return;
        }
        this.mOriginBitmapRectF = new RectF(0.0f, 0.0f, this.mOriginBitmap.getWidth(), this.mOriginBitmap.getHeight());
        this.mDisplayInOriginBitmapRect = new RectF(0.0f, this.mTopPixel, this.mDisplayBitmap.getWidth(), this.mTopPixel + this.mDisplayBitmap.getHeight());
        if (!this.mIsLongCrop) {
            this.mOriginBitmapRenderRectF = new RectF(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect);
            return;
        }
        RectF rectF = new RectF(this.mBitmapGestureParamsHolder.mBitmapDisplayInitRect);
        this.mOriginBitmapRenderRectF = rectF;
        float height = rectF.height();
        LongScreenshotCropEditorView.Entry entry = this.mLongCropEntry;
        float f = entry.mBottomRatio;
        float f2 = entry.mTopRatio;
        float f3 = height / (f - f2);
        RectF rectF2 = this.mOriginBitmapRenderRectF;
        rectF2.top -= f2 * f3;
        rectF2.bottom += f3 * (1.0f - f);
    }

    @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        updateBitmapRect();
    }

    public final void updateScreenCrop() {
        ScreenShellView screenShellView;
        ScreenCropView screenCropView = this.mScreenCrop;
        if (screenCropView == null || (screenShellView = this.mScreenShell) == null) {
            return;
        }
        screenCropView.setVisible(!screenShellView.isWithShell());
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public boolean setCurrentScreenEditor(int i) {
        ScreenShellView screenShellView;
        IScreenOperationEditor iScreenOperationEditor = this.mCurrentScreenEditor;
        this.mLastScreenEditor = iScreenOperationEditor;
        if (iScreenOperationEditor != null) {
            iScreenOperationEditor.onChangeOperation(false);
        }
        if (i == 9 || i == 10 || i == 11) {
            if (this.mScreenDoodle == null) {
                ScreenDoodleView screenDoodleView = new ScreenDoodleView(this);
                this.mScreenDoodle = screenDoodleView;
                screenDoodleView.setDoodleData(DoodleManager.getDefaultScreenDoodleData(), 0);
            }
            this.mScreenDoodle.setDoodlePen(this.mDoodlePen);
            this.mCurrentScreenEditor = this.mScreenDoodle;
            updateScreenCrop();
        } else if (i == 2 || i == 8) {
            if (this.mScreenDoodle == null) {
                ScreenDoodleView screenDoodleView2 = new ScreenDoodleView(this);
                this.mScreenDoodle = screenDoodleView2;
                screenDoodleView2.setDoodleData(DoodleManager.getDefaultScreenDoodleData(), 0);
            }
            this.mCurrentScreenEditor = this.mScreenDoodle;
            updateScreenCrop();
        } else if (i == 3) {
            if (this.mScreenText == null) {
                this.mScreenText = new ScreenTextView(this);
            }
            this.mCurrentScreenEditor = this.mScreenText;
            updateScreenCrop();
        } else if (i == 4) {
            if (this.mScreenMosaic == null) {
                ScreenMosaicProvider screenMosaicProvider = (ScreenMosaicProvider) ScreenProviderManager.INSTANCE.getProvider(ScreenMosaicProvider.class);
                if (!screenMosaicProvider.isInitialized()) {
                    DefaultLogger.w("ScreenEditorView", "ScreenMosaicProvider has not initialized");
                    return false;
                }
                ScreenMosaicView screenMosaicView = new ScreenMosaicView(this);
                this.mScreenMosaic = screenMosaicView;
                screenMosaicView.setMosaicData(screenMosaicProvider.getDefaultData(), 0);
            }
            this.mCurrentScreenEditor = this.mScreenMosaic;
            updateScreenCrop();
        } else if (i == 6) {
            if (this.mScreenCrop == null) {
                ScreenCropView screenCropView = new ScreenCropView(this);
                this.mScreenCrop = screenCropView;
                screenCropView.setOnCropStatusChangeListener(this.mOnScreenCropStatusChangeListener);
            }
            this.mScreenCrop.setVisible(true);
            this.mCurrentScreenEditor = this.mScreenCrop;
        } else if (i == 7 && (screenShellView = this.mScreenShell) != null) {
            if (this.mLastScreenEditor instanceof ScreenTextView) {
                screenShellView.setFeatureGestureListener();
            }
            this.mScreenShell.changeShellStatus();
            this.mCurrentScreenEditor = this.mScreenShell;
            updateScreenCrop();
        }
        IScreenOperationEditor iScreenOperationEditor2 = this.mCurrentScreenEditor;
        if (iScreenOperationEditor2 != null) {
            iScreenOperationEditor2.onChangeOperation(true);
        }
        notifyOperationUpdate();
        return true;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public void setDoodlePen(DoodlePen doodlePen) {
        this.mDoodlePen = doodlePen;
    }

    @Override // com.miui.gallery.editor.photo.screen.home.IScreenEditorController
    public <T extends IScreenOperation> T getScreenOperation(Class<T> cls) {
        if (cls.isInstance(this.mScreenDoodle)) {
            return this.mScreenDoodle;
        }
        if (cls.isInstance(this.mScreenMosaic)) {
            return this.mScreenMosaic;
        }
        if (cls.isInstance(this.mScreenText)) {
            return this.mScreenText;
        }
        if (cls.isInstance(this.mScreenCrop)) {
            return this.mScreenCrop;
        }
        if (cls == IScreenShellOperation.class) {
            if (this.mScreenShell == null) {
                this.mScreenShell = new ScreenShellView(this);
            }
            return this.mScreenShell;
        }
        DefaultLogger.e("ScreenEditorView", "getScreenOperation impossible error");
        return null;
    }

    public final boolean isShape(BaseDrawNode baseDrawNode) {
        return (baseDrawNode instanceof DoodleVectorNode) || (baseDrawNode instanceof DoodleShapeNode);
    }

    public final boolean isText(BaseDrawNode baseDrawNode) {
        return baseDrawNode instanceof ScreenTextDrawNode;
    }
}
