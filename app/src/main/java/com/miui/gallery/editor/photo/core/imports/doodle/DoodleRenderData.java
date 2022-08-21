package com.miui.gallery.editor.photo.core.imports.doodle;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.imports.doodle.DoodleEditorView;

/* loaded from: classes2.dex */
public class DoodleRenderData extends RenderData {
    public static final Parcelable.Creator<DoodleRenderData> CREATOR = new Parcelable.Creator<DoodleRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.DoodleRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DoodleRenderData mo788createFromParcel(Parcel parcel) {
            return new DoodleRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DoodleRenderData[] mo789newArray(int i) {
            return new DoodleRenderData[i];
        }
    };
    public DoodleEditorView.DoodleEntry mDoodleEntry;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public DoodleRenderData(DoodleEditorView.DoodleEntry doodleEntry) {
        this.mDoodleEntry = doodleEntry;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mDoodleEntry, i);
    }

    public DoodleRenderData(Parcel parcel) {
        super(parcel);
        this.mDoodleEntry = (DoodleEditorView.DoodleEntry) parcel.readParcelable(DoodleEditorView.DoodleEntry.class.getClassLoader());
    }
}
