package com.miui.gallery.editor.photo.screen.mosaic.shader;

import android.graphics.BitmapShader;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;

/* loaded from: classes2.dex */
public class MosaicShaderHolder {
    public BitmapShader bitmapShader;
    public final MosaicEntity.TYPE type;

    public MosaicShaderHolder(BitmapShader bitmapShader, MosaicEntity.TYPE type) {
        this.bitmapShader = bitmapShader;
        this.type = type;
    }

    public BitmapShader getBitmapShader() {
        return this.bitmapShader;
    }
}
