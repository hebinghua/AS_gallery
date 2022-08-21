package com.miui.gallery.editor.photo.core.imports.doodle.painter.vector;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class DoodleArrowNode extends DoodleVectorNode {
    public static Reference<Drawable> sBackground;
    public float mArrowWidth;
    public Drawable mBackground;
    public ColorFilter mColorFilter;
    public float[] mPoint_1;
    public float[] mPoint_2;
    public Matrix mRotateMatrix;
    public static final DoodleItem DOODLE_ITEM = DoodleItem.ARROW;
    public static final Parcelable.Creator<DoodleArrowNode> CREATOR = new Parcelable.Creator<DoodleArrowNode>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleArrowNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DoodleArrowNode mo798createFromParcel(Parcel parcel) {
            return new DoodleArrowNode(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DoodleArrowNode[] mo799newArray(int i) {
            return new DoodleArrowNode[i];
        }
    };

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public DoodleArrowNode(Resources resources) {
        super(resources, true);
        this.mRotateMatrix = new Matrix();
        this.mPoint_1 = new float[2];
        this.mPoint_2 = new float[2];
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void init(Resources resources) {
        Reference<Drawable> reference = sBackground;
        if (reference == null || reference.get() == null) {
            sBackground = new WeakReference(resources.getDrawable(R.drawable.doodle_arrow));
        }
        this.mBackground = sBackground.get();
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mColorFilter = new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_IN);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode
    public void drawSafely(Canvas canvas) {
        canvas.save();
        configCanvas(canvas, false);
        PointF pointF = this.mEndPoint;
        float f = pointF.y;
        PointF pointF2 = this.mStartPoint;
        float f2 = pointF2.y;
        float f3 = pointF.x;
        float f4 = pointF2.x;
        float[] fArr = this.mPoint_1;
        fArr[0] = f4;
        fArr[1] = f2;
        float[] fArr2 = this.mPoint_2;
        fArr2[0] = f3;
        fArr2[1] = f;
        double degrees = Math.toDegrees(Math.atan2(f - f2, f3 - f4));
        float[] fArr3 = this.mPoint_1;
        this.mRotateMatrix.setRotate((float) (-degrees), fArr3[0], fArr3[1]);
        this.mRotateMatrix.mapPoints(this.mPoint_2);
        float f5 = this.mPoint_2[0] - this.mPoint_1[0];
        this.mArrowWidth = f5;
        PointF pointF3 = this.mStartPoint;
        canvas.translate(pointF3.x, pointF3.y);
        canvas.rotate(45.0f);
        canvas.rotate((float) degrees);
        int sqrt = (int) (f5 / Math.sqrt(2.0d));
        this.mBackground.setBounds(0, -sqrt, sqrt, 0);
        this.mBackground.setColorFilter(this.mColorFilter);
        this.mBackground.draw(canvas);
        canvas.restore();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode
    public void refreshRectByPoint() {
        super.refreshRectByPoint();
        this.mRectF.inset(0.0f, -((this.mArrowWidth * 0.18f) / 2.0f));
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void setPaintColor(int i) {
        super.setPaintColor(i);
        this.mColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public String getDoodleName() {
        return DOODLE_ITEM.name();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode, com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public DoodleArrowNode(Parcel parcel) {
        super(parcel);
        this.mRotateMatrix = new Matrix();
        this.mPoint_1 = new float[2];
        this.mPoint_2 = new float[2];
    }
}
