package com.miui.gallery.editor.photo.screen.mosaic.shader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;

/* loaded from: classes2.dex */
public class MosaicEntityOrigin extends MosaicEntity {
    public MosaicEntityOrigin(String str, String str2) {
        super(str, str2, MosaicEntity.TYPE.ORIGIN);
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity
    public MosaicShaderHolder generateShader(Bitmap bitmap, float f, Matrix matrix) {
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(null, tileMode, tileMode);
        bitmapShader.setLocalMatrix(matrix);
        return new MosaicShaderHolder(bitmapShader, MosaicEntity.TYPE.ORIGIN);
    }
}
