package com.miui.gallery.editor.photo.core.imports.crop;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.common.model.IPositionChangeData;
import com.miui.gallery.editor.photo.core.imports.obsoletes.Crop;

/* loaded from: classes2.dex */
public class CropStateData extends RenderData implements IPositionChangeData {
    public static final Parcelable.Creator<CropStateData> CREATOR = new Parcelable.Creator<CropStateData>() { // from class: com.miui.gallery.editor.photo.core.imports.crop.CropStateData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public CropStateData mo784createFromParcel(Parcel parcel) {
            return new CropStateData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public CropStateData[] mo785newArray(int i) {
            return new CropStateData[i];
        }
    };
    public final Crop.ParcelableCropEntry mEntry;

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CropStateData(Crop.ParcelableCropEntry parcelableCropEntry) {
        this.mEntry = parcelableCropEntry;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderData, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mEntry, i);
    }

    public CropStateData(Parcel parcel) {
        super(parcel);
        this.mEntry = (Crop.ParcelableCropEntry) parcel.readParcelable(Crop.ParcelableCropEntry.class.getClassLoader());
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.IPositionChangeData
    public void refreshTargetAreaPosition(RectF rectF, RectF rectF2) {
        this.mEntry.refreshTargetAreaPosition(rectF, rectF2);
    }
}
