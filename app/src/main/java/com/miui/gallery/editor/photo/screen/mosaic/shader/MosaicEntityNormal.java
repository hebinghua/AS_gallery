package com.miui.gallery.editor.photo.screen.mosaic.shader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MosaicEntityNormal extends MosaicEntityRely {
    public MosaicEntityNormal(String str, String str2) {
        super(str, str2, MosaicEntity.TYPE.NORMAL);
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity
    public MosaicShaderHolder generateShader(Bitmap bitmap, float f, Matrix matrix) {
        DefaultLogger.d("MosaicEntityRely", "MosaicEntityNormal  generateShader.");
        float f2 = f * 36.0f;
        if (bitmap.getWidth() < f2 || bitmap.getHeight() < f2) {
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            return new MosaicShaderHolder(new BitmapShader(bitmap, tileMode, tileMode), MosaicEntity.TYPE.NORMAL);
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() / f2), Math.round(bitmap.getHeight() / f2), false);
        Bitmap createScaledBitmap2 = Bitmap.createScaledBitmap(createScaledBitmap, Math.round(createScaledBitmap.getWidth() * f2), Math.round(createScaledBitmap.getHeight() * f2), false);
        Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
        return new MosaicShaderHolder(new BitmapShader(createScaledBitmap2, tileMode2, tileMode2), MosaicEntity.TYPE.NORMAL);
    }
}
