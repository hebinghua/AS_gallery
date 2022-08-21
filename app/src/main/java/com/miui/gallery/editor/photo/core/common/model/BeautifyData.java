package com.miui.gallery.editor.photo.core.common.model;

import android.os.Parcel;

/* loaded from: classes2.dex */
public abstract class BeautifyData extends RenderMetaData {
    public final int icon;

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public BeautifyData(short s, String str, int i) {
        super(s, str);
        this.icon = i;
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.icon);
    }

    public BeautifyData(Parcel parcel) {
        super(parcel);
        this.icon = parcel.readInt();
    }
}
