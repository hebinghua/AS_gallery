package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity;
import com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLMosaicBlurShaderImp;
import com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLMosaicShader;
import com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLMosaicTriangleShader;
import com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLTextureSizeShader;

/* loaded from: classes2.dex */
public class MosaicGLEffectEntity extends MosaicGLEntity {
    public static final Parcelable.Creator<MosaicGLEffectEntity> CREATOR = new Parcelable.Creator<MosaicGLEffectEntity>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEffectEntity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MosaicGLEffectEntity mo822createFromParcel(Parcel parcel) {
            return new MosaicGLEffectEntity(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MosaicGLEffectEntity[] mo823newArray(int i) {
            return new MosaicGLEffectEntity[i];
        }
    };
    public final String mEffectType;

    @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity, com.miui.gallery.editor.photo.core.common.model.MosaicData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MosaicGLEffectEntity(short s, String str, String str2, String str3, String str4) {
        super(s, str, str2, MosaicGLEntity.TYPE.EFFECT, str4);
        this.mEffectType = str3;
    }

    public GLTextureSizeShader generateSpecificShader(int i, int i2, int i3, int i4) {
        if ("NORMAL".equals(this.mEffectType)) {
            return new GLMosaicShader(i, i2);
        }
        if ("BLUR".equals(this.mEffectType)) {
            return new GLMosaicBlurShaderImp(i, i2, i3, i4);
        }
        if (!"TRIANGLE".equals(this.mEffectType)) {
            return null;
        }
        return new GLMosaicTriangleShader(i, i2);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity, com.miui.gallery.editor.photo.core.common.model.MosaicData, com.miui.gallery.editor.photo.core.Metadata, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mEffectType);
    }

    public MosaicGLEffectEntity(Parcel parcel) {
        super(parcel);
        this.mEffectType = parcel.readString();
    }
}
