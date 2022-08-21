package com.miui.gallery.editor.photo.core.imports.filter;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.gallery.editor.photo.core.common.model.AdjustData;
import com.miui.gallery.editor.photo.core.imports.filter.adjust.GPUBrightnessFilter;
import com.miui.gallery.editor.photo.core.imports.filter.adjust.GPUContrastFilter;
import com.miui.gallery.editor.photo.core.imports.filter.adjust.GPUImageSaturationFilter;
import com.miui.gallery.editor.photo.core.imports.filter.adjust.GPUImageSharpenFilter;
import com.miui.gallery.editor.photo.core.imports.filter.adjust.GPUImageVignetteFilter;

/* loaded from: classes2.dex */
public class FilterAdjust extends AdjustData {
    public static final Parcelable.Creator<FilterAdjust> CREATOR = new Parcelable.Creator<FilterAdjust>() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterAdjust.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FilterAdjust mo802createFromParcel(Parcel parcel) {
            return new FilterAdjust(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FilterAdjust[] mo803newArray(int i) {
            return new FilterAdjust[i];
        }
    };
    public int mId;
    public boolean mIsMid;

    @Override // com.miui.gallery.editor.photo.core.common.model.AdjustData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.AdjustData
    public int getMax() {
        return 100;
    }

    public FilterAdjust(int i, short s, String str, int i2, boolean z, int i3) {
        super(s, str, i2, i3);
        this.mId = i;
        this.mIsMid = z;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.AdjustData
    public int getMin() {
        return this.mIsMid ? -100 : 0;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.AdjustData
    public boolean isMid() {
        return this.mIsMid;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.Renderable
    public BaseOriginalFilter instantiate() {
        int min = ((this.progress - getMin()) * getMax()) / (getMax() - getMin());
        int i = this.mId;
        if (i != 0) {
            if (i == 1) {
                return new GPUContrastFilter(min);
            }
            if (i == 2) {
                return new GPUImageSaturationFilter(min);
            }
            if (i == 3) {
                return new GPUImageSharpenFilter(min);
            }
            if (i == 4) {
                return new GPUImageVignetteFilter(min);
            }
            throw new IllegalArgumentException("not support adjust id: " + this.mId);
        }
        return new GPUBrightnessFilter(min);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.AdjustData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mId);
        parcel.writeByte(this.mIsMid ? (byte) 1 : (byte) 0);
    }

    public FilterAdjust(Parcel parcel) {
        super(parcel);
        this.mId = parcel.readInt();
        this.mIsMid = parcel.readByte() != 0;
    }
}
