package com.miui.gallery.editor.photo.screen.mosaic.shader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MosaicEntityBitmap extends MosaicEntity {
    public String mResourcePath;
    public Shader.TileMode mTileMode;

    public MosaicEntityBitmap(String str, String str2, String str3, Shader.TileMode tileMode) {
        super(str, str2, MosaicEntity.TYPE.BITMAP);
        this.mResourcePath = str3;
        this.mTileMode = tileMode;
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity
    public MosaicShaderHolder generateShader(Bitmap bitmap, float f, Matrix matrix) {
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        float f2 = fArr[0];
        Bitmap decodeAsset = Bitmaps.decodeAsset(StaticContext.sGetAndroidContext(), Scheme.ASSETS.crop(this.mResourcePath), null);
        if (decodeAsset == null) {
            DefaultLogger.e("MosaicEntityBitmap", "loaded resource bitmap is null, what should not happen");
            decodeAsset = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
        Shader.TileMode tileMode = this.mTileMode;
        BitmapShader bitmapShader = new BitmapShader(decodeAsset, tileMode, tileMode);
        Matrix matrix2 = new Matrix();
        float f3 = 0.8f / f2;
        matrix2.postScale(f3, f3);
        bitmapShader.setLocalMatrix(matrix2);
        return new MosaicShaderHolder(bitmapShader, MosaicEntity.TYPE.BITMAP);
    }
}
