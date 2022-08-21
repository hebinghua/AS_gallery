package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLView;

/* loaded from: classes2.dex */
public class MosaicRenderData extends RenderData {
    public static final Parcelable.Creator<MosaicRenderData> CREATOR = new Parcelable.Creator<MosaicRenderData>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicRenderData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MosaicRenderData mo835createFromParcel(Parcel parcel) {
            return new MosaicRenderData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MosaicRenderData[] mo836newArray(int i) {
            return new MosaicRenderData[i];
        }
    };
    public MosaicGLView.MosaicEntry mMosaicEntry;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MosaicRenderData(MosaicGLView.MosaicEntry mosaicEntry) {
        this.mMosaicEntry = mosaicEntry;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mMosaicEntry, i);
    }

    public MosaicRenderData(Parcel parcel) {
        super(parcel);
        this.mMosaicEntry = (MosaicGLView.MosaicEntry) parcel.readParcelable(MosaicGLView.MosaicEntry.class.getClassLoader());
    }
}
