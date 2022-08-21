package com.miui.gallery.editor.photo.screen.mosaic.shader;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/* loaded from: classes2.dex */
public abstract class MosaicEntity extends MosaicData {
    public MosaicShaderHolder mMosaicShaderHolder;
    public final TYPE type;

    /* loaded from: classes2.dex */
    public enum TYPE {
        ORIGIN,
        NORMAL,
        TRIANGLE,
        TRIANGLE_RECT,
        BLUR,
        BITMAP
    }

    public abstract MosaicShaderHolder generateShader(Bitmap bitmap, float f, Matrix matrix);

    public MosaicShaderHolder getMosaicShaderHolder() {
        return this.mMosaicShaderHolder;
    }

    public MosaicEntity(String str, String str2, TYPE type) {
        super(str, str2);
        this.type = type;
    }

    public MosaicShaderHolder generateShader(Bitmap bitmap, Matrix matrix) {
        MosaicShaderHolder generateShader = generateShader(bitmap, 1.0f, matrix);
        this.mMosaicShaderHolder = generateShader;
        return generateShader;
    }

    public void clearShader() {
        this.mMosaicShaderHolder = null;
    }
}
