package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Parcel;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;

/* loaded from: classes2.dex */
public abstract class MosaicGLEntity extends MosaicData {
    public final String talkbackName;
    public final TYPE type;

    /* loaded from: classes2.dex */
    public enum TYPE {
        ORIGIN,
        EFFECT,
        RESOURCE
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.MosaicData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MosaicGLEntity(short s, String str, String str2, TYPE type, String str3) {
        super(s, str, str2, str3);
        this.type = type;
        this.talkbackName = str3;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.MosaicData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        TYPE type = this.type;
        parcel.writeInt(type == null ? -1 : type.ordinal());
        parcel.writeString(this.talkbackName);
    }

    public MosaicGLEntity(Parcel parcel) {
        super(parcel);
        int readInt = parcel.readInt();
        this.type = readInt == -1 ? null : TYPE.values()[readInt];
        this.talkbackName = parcel.readString();
    }
}
