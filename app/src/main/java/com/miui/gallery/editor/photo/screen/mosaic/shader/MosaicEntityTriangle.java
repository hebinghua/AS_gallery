package com.miui.gallery.editor.photo.screen.mosaic.shader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.reflect.Array;

/* loaded from: classes2.dex */
public class MosaicEntityTriangle extends MosaicEntityRely {
    public MosaicEntityTriangle(String str, String str2) {
        super(str, str2, MosaicEntity.TYPE.TRIANGLE);
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity
    public MosaicShaderHolder generateShader(Bitmap bitmap, float f, Matrix matrix) {
        int round = Math.round(36.0f * f);
        if (bitmap.getWidth() < round || bitmap.getHeight() < round) {
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            return new MosaicShaderHolder(new BitmapShader(bitmap, tileMode, tileMode), MosaicEntity.TYPE.TRIANGLE);
        }
        long currentTimeMillis = System.currentTimeMillis();
        int i = 0;
        int[][] iArr = (int[][]) Array.newInstance(int.class, (bitmap.getHeight() / round) + 1, (bitmap.getWidth() / round) + 1);
        int[][] iArr2 = (int[][]) Array.newInstance(int.class, (bitmap.getHeight() / round) + 1, (bitmap.getWidth() / round) + 1);
        DefaultLogger.d("MosaicEntityTriangle", "allocate cache costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        int i2 = 0;
        for (int i3 = 0; i3 < bitmap.getHeight(); i3 += round) {
            int i4 = 0;
            for (int i5 = 0; i5 < bitmap.getWidth(); i5 += round) {
                int i6 = round / 4;
                int i7 = i5 + i6;
                if (i7 >= bitmap.getWidth()) {
                    i7 = (bitmap.getWidth() + i5) / 2;
                }
                int i8 = i6 + i3;
                if (i8 >= bitmap.getHeight()) {
                    i8 = (bitmap.getHeight() + i3) / 2;
                }
                iArr[i2][i4] = bitmap.getPixel(i7, i8);
                int i9 = (round * 3) / 4;
                int i10 = i5 + i9;
                if (i10 >= bitmap.getWidth()) {
                    i10 = (bitmap.getWidth() + i5) / 2;
                }
                int i11 = i9 + i3;
                if (i11 >= bitmap.getHeight()) {
                    i11 = (bitmap.getHeight() + i3) / 2;
                }
                iArr2[i2][i4] = bitmap.getPixel(i10, i11);
                i4++;
            }
            i2++;
        }
        DefaultLogger.d("MosaicEntityTriangle", "init cache costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        int width = bitmap.getWidth() * bitmap.getHeight();
        int[] iArr3 = new int[width];
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        while (i12 < width) {
            int i15 = i;
            int i16 = i15;
            int i17 = i16;
            while (i15 < bitmap.getWidth()) {
                if (i13 < round - i16) {
                    iArr3[i12] = iArr[i14][i17];
                } else {
                    iArr3[i12] = iArr2[i14][i17];
                }
                i16++;
                if (i16 == round) {
                    i17++;
                    i16 = 0;
                }
                i15++;
                i12++;
            }
            i13++;
            if (i13 == round) {
                i14++;
                i = 0;
                i13 = 0;
            } else {
                i = 0;
            }
        }
        DefaultLogger.d("MosaicEntityTriangle", "generate pixels costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        Bitmap createBitmap = Bitmap.createBitmap(iArr3, bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        DefaultLogger.d("MosaicEntityTriangle", "finish costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        Shader.TileMode tileMode2 = Shader.TileMode.CLAMP;
        return new MosaicShaderHolder(new BitmapShader(createBitmap, tileMode2, tileMode2), MosaicEntity.TYPE.TRIANGLE);
    }
}
