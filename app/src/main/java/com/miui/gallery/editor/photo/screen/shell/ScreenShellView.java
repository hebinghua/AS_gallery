package com.miui.gallery.editor.photo.screen.shell;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView;
import com.miui.gallery.editor.photo.screen.base.ScreenVirtualEditorView;
import com.miui.gallery.editor.photo.screen.home.ScreenEditorView;
import com.miui.gallery.editor.photo.screen.shell.ShellResThread;

/* loaded from: classes2.dex */
public class ScreenShellView extends ScreenVirtualEditorView implements IScreenShellOperation {
    public Matrix mClipMatrix;
    public RectF mClipOriRect;
    public RectF mClipRealRect;
    public ScreenShellEntry mCurrentEntity;
    public Path mFinalPath;
    public ScreenBaseGestureView.FeatureGesListener mGesListener;
    public boolean mIsWithShell;
    public boolean mLastShellStatus;
    public RectF mShellFitMargin;
    public ShellResThread.ShellResListener mShellResListener;
    public ShellResThread mShellResThread;

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void canvasMatrixChange() {
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void clearActivation() {
    }

    public ScreenShellView(ScreenEditorView screenEditorView) {
        super(screenEditorView);
        this.mIsWithShell = false;
        this.mLastShellStatus = false;
        this.mClipOriRect = new RectF();
        this.mClipRealRect = new RectF();
        this.mClipMatrix = new Matrix();
        this.mFinalPath = new Path();
        this.mGesListener = new ScreenBaseGestureView.FeatureGesListener() { // from class: com.miui.gallery.editor.photo.screen.shell.ScreenShellView.1
            @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
            public void onActionUp(float f, float f2) {
            }

            @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                return false;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return false;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            }

            @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
            public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            }

            @Override // com.miui.gallery.editor.photo.screen.base.ScreenBaseGestureView.FeatureGesListener
            public void onSingleTapUp(MotionEvent motionEvent) {
            }
        };
        this.mShellResListener = new ShellResThread.ShellResListener() { // from class: com.miui.gallery.editor.photo.screen.shell.ScreenShellView.2
            @Override // com.miui.gallery.editor.photo.screen.shell.ShellResThread.ShellResListener
            public void onLoadEnd(boolean z) {
                ScreenShellView.this.invalidate();
            }
        };
        init();
    }

    public final void init() {
        this.mCurrentEntity = new ScreenShellEntry();
        this.mShellResThread = new ShellResThread();
        this.mShellFitMargin = new RectF();
        this.mShellResThread.setShellResListener(this.mShellResListener);
        generateBitmap();
    }

    public void setFeatureGestureListener() {
        this.mEditorView.setFeatureGestureListener(this.mGesListener);
    }

    public final void generateBitmap() {
        this.mShellResThread.sendGenerateShellMsg(this.mCurrentEntity, this.mEditorView.getOriginBitmap(), this.mEditorView.getBitmapGestureParamsHolder().mBitmapToDisplayMatrix);
    }

    public void changeShellStatus() {
        this.mIsWithShell = !this.mIsWithShell;
        invalidate();
    }

    @Override // com.miui.gallery.editor.photo.screen.shell.IScreenShellOperation
    public boolean isWithShell() {
        return this.mIsWithShell;
    }

    @Override // com.miui.gallery.editor.photo.screen.shell.IScreenShellOperation
    public RectF getShellFitMargin() {
        if (this.mCurrentEntity.getShellInfo() != null) {
            ShellInfoBean shellInfo = this.mCurrentEntity.getShellInfo();
            float width = getBitmapGestureParamsHolder().mBitmapDisplayInitRect.width() / shellInfo.with;
            float height = getBitmapGestureParamsHolder().mBitmapDisplayInitRect.height() / shellInfo.height;
            float height2 = this.mCurrentEntity.getTopBlackViewRect(getBitmapGestureParamsHolder().mBitmapDisplayInitRect).height();
            RectF rectF = this.mShellFitMargin;
            rectF.left = shellInfo.leftMargin * width;
            rectF.top = (shellInfo.topMargin * height) + height2;
            rectF.right = shellInfo.rightMargin * width;
            rectF.bottom = shellInfo.bottomMargin * height;
        }
        return this.mShellFitMargin;
    }

    @Override // com.miui.gallery.editor.photo.screen.shell.IScreenShellOperation
    public boolean isShellStatusChangedForLastRequest() {
        boolean z = this.mLastShellStatus;
        boolean z2 = this.mIsWithShell;
        if (z != z2) {
            this.mLastShellStatus = z2;
            return true;
        }
        return false;
    }

    public void clipCanvas(Canvas canvas) {
        ScreenShellEntry screenShellEntry = this.mCurrentEntity;
        if (screenShellEntry == null || screenShellEntry.getShellSvgPath() == null || this.mCurrentEntity.getShellInfo() == null) {
            return;
        }
        ShellInfoBean shellInfo = this.mCurrentEntity.getShellInfo();
        this.mClipOriRect.set(0.0f, 0.0f, shellInfo.with, shellInfo.height);
        float height = this.mCurrentEntity.getTopBlackViewRect(getBitmapGestureParamsHolder().mBitmapDisplayRect).height();
        this.mClipRealRect.set(getBitmapGestureParamsHolder().mBitmapDisplayRect);
        this.mClipRealRect.left = getBitmapGestureParamsHolder().mBitmapDisplayRect.left;
        this.mClipRealRect.top = getBitmapGestureParamsHolder().mBitmapDisplayRect.top - height;
        this.mClipRealRect.right = getBitmapGestureParamsHolder().mBitmapDisplayRect.right;
        this.mClipRealRect.bottom = getBitmapGestureParamsHolder().mBitmapDisplayRect.bottom;
        this.mClipMatrix.reset();
        this.mClipMatrix.setRectToRect(this.mClipOriRect, this.mClipRealRect, Matrix.ScaleToFit.CENTER);
        this.mFinalPath.reset();
        this.mFinalPath.addPath(this.mCurrentEntity.getShellSvgPath(), this.mClipMatrix);
        canvas.clipPath(this.mFinalPath);
    }

    public void drawTopBlackView(Canvas canvas) {
        this.mCurrentEntity.drawTopBlackView(canvas, getBitmapGestureParamsHolder().mBitmapDisplayRect);
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenEditor
    public void drawOverlay(Canvas canvas) {
        if (this.mIsWithShell) {
            this.mCurrentEntity.apply(canvas, getBitmapGestureParamsHolder().mBitmapDisplayRect);
        }
    }

    public ScreenShellEntry export() {
        return this.mCurrentEntity;
    }

    @Override // com.miui.gallery.editor.photo.screen.base.IScreenOperation
    public void onChangeOperation(boolean z) {
        if (!z) {
            invalidate();
        }
    }
}
