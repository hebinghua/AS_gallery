package com.miui.gallery.editor.photo.core.common.model;

import android.os.Parcel;
import java.util.Objects;

/* loaded from: classes2.dex */
public abstract class FilterData extends RenderMetaData {
    public final int icon;
    public int progress;
    public int state;

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public abstract String getMaterialName();

    public abstract boolean isBuiltIn();

    public abstract boolean isNone();

    public abstract boolean isPortraitColor();

    public FilterData(short s, String str, int i) {
        super(s, str);
        this.state = 17;
        this.icon = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FilterData filterData = (FilterData) obj;
        return this.priority == filterData.priority && Objects.equals(this.name, filterData.name);
    }

    public int hashCode() {
        return Objects.hash(this.name, Integer.valueOf(this.priority));
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.icon);
        parcel.writeInt(this.progress);
    }

    public FilterData(Parcel parcel) {
        super(parcel);
        this.state = 17;
        this.icon = parcel.readInt();
        this.progress = parcel.readInt();
    }
}
