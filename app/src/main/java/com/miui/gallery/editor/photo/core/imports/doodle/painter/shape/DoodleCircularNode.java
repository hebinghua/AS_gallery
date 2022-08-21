package com.miui.gallery.editor.photo.core.imports.doodle.painter.shape;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;

/* loaded from: classes2.dex */
public class DoodleCircularNode extends DoodleShapeNode {
    public static final DoodleItem DOODLE_ITEM = DoodleItem.CIRCULAR;
    public static final Parcelable.Creator<DoodleCircularNode> CREATOR = new Parcelable.Creator<DoodleCircularNode>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleCircularNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DoodleCircularNode mo794createFromParcel(Parcel parcel) {
            return new DoodleCircularNode(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DoodleCircularNode[] mo795newArray(int i) {
            return new DoodleCircularNode[i];
        }
    };

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public DoodleCircularNode(Resources resources) {
        super(resources);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void init(Resources resources) {
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void draw(Canvas canvas) {
        canvas.save();
        configCanvas(canvas, true);
        canvas.drawOval(this.mRectF, this.mPaint);
        canvas.restore();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public String getDoodleName() {
        return DOODLE_ITEM.name();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleShapeNode, com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mRectF, i);
    }

    public DoodleCircularNode(Parcel parcel) {
        super(parcel);
        this.mRectF = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
    }
}
