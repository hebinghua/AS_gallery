package com.miui.gallery.editor.photo.core.imports.mosaic.shader;

import com.miui.gallery.util.ScreenUtils;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GLMosaicShader extends GLTextureSizeShader {
    public GLMosaicShader(int i, int i2) {
        super(String.format(Locale.US, "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nuniform int textureWidth;\nuniform int textureHeight;\nuniform highp float scale;\nuniform highp float mosaicScale;\n\nvoid main(void)\n{\n    lowp vec2 mosaicSize = vec2(%d.0, %d.0);\n    mosaicSize.x = mosaicSize.x * scale;\n    mosaicSize.y = mosaicSize.y * scale * mosaicScale;\n\n    highp vec2 texSize = vec2(float(textureWidth), float(textureHeight));\n\n    highp vec2 xy = vec2(textureCoordinate.x * texSize.x, textureCoordinate.y * texSize.y);\n\n    highp vec2 xyMosaic = vec2(floor(xy.x / mosaicSize.x) * mosaicSize.x + mosaicSize.x/2.0,\n         floor(xy.y / mosaicSize.y) * mosaicSize.y + mosaicSize.y/2.0);\n\n    highp vec2 uvMosaic = vec2(xyMosaic.x / texSize.x, xyMosaic.y / texSize.y);\n    gl_FragColor = texture2D( inputImageTexture, uvMosaic );\n}", Integer.valueOf(calMosaicSize(i)), Integer.valueOf(calMosaicSize(i))), i, i2);
    }

    public static int calMosaicSize(int i) {
        if (i >= ScreenUtils.getScreenWidth()) {
            return 50;
        }
        int i2 = i / 30;
        if (i2 >= 2) {
            return i2;
        }
        return 2;
    }
}
