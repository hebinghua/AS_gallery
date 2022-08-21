package com.miui.gallery.editor.photo.core.imports.doodle.painter.shape;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;

/* loaded from: classes2.dex */
public class DoodleRectangleNode extends DoodleShapeNode {
    public static final DoodleItem DOODLE_ITEM = DoodleItem.RECTANGLE;
    public static final Parcelable.Creator<DoodleRectangleNode> CREATOR = new Parcelable.Creator<DoodleRectangleNode>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleRectangleNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DoodleRectangleNode mo796createFromParcel(Parcel parcel) {
            return new DoodleRectangleNode(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DoodleRectangleNode[] mo797newArray(int i) {
            return new DoodleRectangleNode[i];
        }
    };

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void init(Resources resources) {
    }

    public DoodleRectangleNode(Resources resources) {
        super(resources);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void draw(Canvas canvas) {
        canvas.save();
        configCanvas(canvas, true);
        canvas.drawRect(this.mRectF, this.mPaint);
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

    public DoodleRectangleNode(Parcel parcel) {
        super(parcel);
        this.mRectF = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
    }
}
