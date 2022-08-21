package com.miui.gallery.editor.photo.screen.mosaic;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Shader;
import com.miui.gallery.editor.photo.core.common.model.BaseDrawNode;

/* loaded from: classes2.dex */
public abstract class MosaicNode extends BaseDrawNode {
    public PointF mStartPoint = new PointF();
    public PointF mEndPoint = new PointF();
    public boolean mIsInit = false;
    public Matrix mBitmapDisplayMatrix = new Matrix();

    public abstract void init();

    public abstract void onReceivePosition(float f, float f2, boolean z);

    public abstract void setPaintSize(float f);

    public abstract void setShader(Shader shader);

    public MosaicNode() {
        init();
    }

    public void setImageDisplayMatrix(Matrix matrix) {
        this.mBitmapDisplayMatrix = matrix;
    }

    public void receivePosition(float f, float f2) {
        if (!this.mIsInit) {
            this.mStartPoint.set(f, f2);
            this.mIsInit = true;
            onReceivePosition(f, f2, true);
            return;
        }
        this.mEndPoint.set(f, f2);
        onReceivePosition(f, f2, false);
    }
}
