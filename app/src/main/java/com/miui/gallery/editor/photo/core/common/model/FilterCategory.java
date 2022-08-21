package com.miui.gallery.editor.photo.core.common.model;

import android.os.Parcel;
import com.miui.gallery.editor.photo.core.Metadata;
import java.io.Serializable;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class FilterCategory extends Metadata implements Serializable {
    public int highlighColor;
    public int subHighlighColor;
    public int subItemSize;

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public abstract int getFilterCategory();

    public abstract List<? extends FilterData> getFilterDatas();

    public FilterCategory(short s, String str, int i) {
        super(s, str);
        this.highlighColor = i;
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.highlighColor);
    }

    public FilterCategory(Parcel parcel) {
        super(parcel);
        this.highlighColor = parcel.readInt();
    }
}
