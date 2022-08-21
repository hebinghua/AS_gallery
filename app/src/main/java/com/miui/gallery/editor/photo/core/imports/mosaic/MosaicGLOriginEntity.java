package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity;

/* loaded from: classes2.dex */
public class MosaicGLOriginEntity extends MosaicGLEntity {
    public static final Parcelable.Creator<MosaicGLOriginEntity> CREATOR = new Parcelable.Creator<MosaicGLOriginEntity>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLOriginEntity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MosaicGLOriginEntity mo824createFromParcel(Parcel parcel) {
            return new MosaicGLOriginEntity(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MosaicGLOriginEntity[] mo825newArray(int i) {
            return new MosaicGLOriginEntity[i];
        }
    };

    public MosaicGLOriginEntity(short s, String str, String str2, String str3) {
        super(s, str, str2, MosaicGLEntity.TYPE.ORIGIN, str3);
    }

    public MosaicGLOriginEntity(Parcel parcel) {
        super(parcel);
    }
}
