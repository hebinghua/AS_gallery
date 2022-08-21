package com.miui.gallery.editor.photo.core.imports.frame;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.model.IPositionChangeData;
import com.miui.gallery.editor.photo.core.imports.frame.FrameEditorView;

/* loaded from: classes2.dex */
public class FrameRenderData extends RenderData implements IPositionChangeData {
    public static final Parcelable.Creator<FrameRenderData> CREATOR = new Parcelable.Creator<FrameRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.frame.FrameRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FrameRenderData mo816createFromParcel(Parcel parcel) {
            return new FrameRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FrameRenderData[] mo817newArray(int i) {
            return new FrameRenderData[i];
        }
    };
    public FrameEditorView.FrameEntry mFrameEntry;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public FrameRenderData(FrameEditorView.FrameEntry frameEntry) {
        this.mFrameEntry = frameEntry;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mFrameEntry, i);
    }

    public FrameRenderData(Parcel parcel) {
        super(parcel);
        this.mFrameEntry = (FrameEditorView.FrameEntry) parcel.readParcelable(FrameEditorView.FrameEntry.class.getClassLoader());
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.IPositionChangeData
    public void refreshTargetAreaPosition(RectF rectF, RectF rectF2) {
        this.mFrameEntry.refreshTargetAreaPosition(rectF, rectF2);
    }
}
