package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity;

/* loaded from: classes2.dex */
public class MosaicGLResourceEntity extends MosaicGLEntity {
    public static final Parcelable.Creator<MosaicGLResourceEntity> CREATOR = new Parcelable.Creator<MosaicGLResourceEntity>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLResourceEntity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MosaicGLResourceEntity mo826createFromParcel(Parcel parcel) {
            return new MosaicGLResourceEntity(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MosaicGLResourceEntity[] mo827newArray(int i) {
            return new MosaicGLResourceEntity[i];
        }
    };
    public final String mResourceAssetPath;
    public final String mTileMode;
    public final String talkbackName;

    @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity, com.miui.gallery.editor.photo.core.common.model.MosaicData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MosaicGLResourceEntity(short s, String str, String str2, String str3, String str4, String str5) {
        super(s, str, str2, MosaicGLEntity.TYPE.RESOURCE, str5);
        this.mResourceAssetPath = str3;
        this.mTileMode = str4;
        this.talkbackName = str5;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity, com.miui.gallery.editor.photo.core.common.model.MosaicData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mResourceAssetPath);
        parcel.writeString(this.mTileMode);
        parcel.writeString(this.talkbackName);
    }

    public MosaicGLResourceEntity(Parcel parcel) {
        super(parcel);
        this.mResourceAssetPath = parcel.readString();
        this.mTileMode = parcel.readString();
        this.talkbackName = parcel.readString();
    }
}
