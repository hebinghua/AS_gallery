package com.miui.gallery.editor.photo.core.common.model;

import android.os.Parcel;

/* loaded from: classes2.dex */
public abstract class AdjustData extends RenderMetaData {
    public final int icon;
    public int iconJson;
    public int progress;

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public abstract int getMax();

    public abstract int getMin();

    public abstract boolean isMid();

    public AdjustData(short s, String str, int i, int i2) {
        super(s, str);
        this.iconJson = i2;
        this.icon = i;
        this.progress = getMin();
    }

    @Override // com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.icon);
        parcel.writeInt(this.progress);
    }

    public AdjustData(Parcel parcel) {
        super(parcel);
        this.icon = parcel.readInt();
        this.progress = parcel.readInt();
    }
}
