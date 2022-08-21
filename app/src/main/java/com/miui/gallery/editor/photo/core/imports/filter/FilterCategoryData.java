package com.miui.gallery.editor.photo.core.imports.filter;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.common.model.FilterCategory;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterCategoryData extends FilterCategory {
    public static final Parcelable.Creator<FilterCategoryData> CREATOR = new Parcelable.Creator<FilterCategoryData>() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterCategoryData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FilterCategoryData mo806createFromParcel(Parcel parcel) {
            return new FilterCategoryData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FilterCategoryData[] mo807newArray(int i) {
            return new FilterCategoryData[i];
        }
    };
    private int mCategory;
    private float mDownloadPercent;
    private int mDownloadState;

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterCategory, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public FilterCategoryData(int i, String str, int i2) {
        super((short) 4, str, i2);
        this.mDownloadState = 0;
        this.mCategory = i;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterCategory
    public List<FilterItem> getFilterDatas() {
        return FilterManager.getFiltersByCategory(this.mCategory);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterCategory
    public int getFilterCategory() {
        return this.mCategory;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.FilterCategory, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mCategory);
    }

    public FilterCategoryData(Parcel parcel) {
        super(parcel);
        this.mDownloadState = 0;
        this.mCategory = parcel.readInt();
    }
}
