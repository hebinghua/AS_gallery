package com.miui.gallery.editor.photo.core.imports.doodle.painter.vector;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;

/* loaded from: classes2.dex */
public class DoodleLineNode extends DoodleVectorNode {
    public static final DoodleItem DOODLE_ITEM = DoodleItem.LINE;
    public static final Parcelable.Creator<DoodleLineNode> CREATOR = new Parcelable.Creator<DoodleLineNode>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleLineNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DoodleLineNode mo800createFromParcel(Parcel parcel) {
            return new DoodleLineNode(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DoodleLineNode[] mo801newArray(int i) {
            return new DoodleLineNode[i];
        }
    };

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void init(Resources resources) {
    }

    public DoodleLineNode(Resources resources) {
        super(resources);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode
    public void drawSafely(Canvas canvas) {
        canvas.save();
        configCanvas(canvas, false);
        PointF pointF = this.mStartPoint;
        float f = pointF.x;
        float f2 = pointF.y;
        PointF pointF2 = this.mEndPoint;
        canvas.drawLine(f, f2, pointF2.x, pointF2.y, this.mPaint);
        canvas.restore();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public String getDoodleName() {
        return DOODLE_ITEM.name();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode, com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public DoodleLineNode(Parcel parcel) {
        super(parcel);
    }
}
