package com.miui.gallery.editor.photo.core.imports.filter.render;

import android.opengl.GLES20;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;

/* loaded from: classes2.dex */
public class BoundLinesFilter extends BaseOriginalFilter {
    public int mBoundLocation;

    public BoundLinesFilter() {
        super("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "precision highp float;\n\nuniform vec2 bound;\nuniform sampler2D inputImageTexture;\nvarying vec2 textureCoordinate;\n\nvoid main() {\n    if( (textureCoordinate.x > 0.0 + bound.x * 0.2 && textureCoordinate.x <= bound.x * 1.2 ) ||     (textureCoordinate.x < 1.0 - bound.x * 0.2 && textureCoordinate.x >= 1.0 - bound.x * 1.2) ||\n    (textureCoordinate.y > 0.0 + bound.y * 0.2 && textureCoordinate.y <= bound.y * 1.2 ) ||      (textureCoordinate.y < 1.0 - bound.y * 0.2 && textureCoordinate.y >= 1.0 - bound.y * 1.2 )){\n        gl_FragColor.rgb = vec3(0,0,0);\n        gl_FragColor.a = 0.20;\n    }\n    else{\n        gl_FragColor.rgb = vec3(0.0,0.0,0.0);\n        gl_FragColor.a = 0.0;\n    }\n }");
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInit() {
        super.onInit();
        this.mBoundLocation = GLES20.glGetUniformLocation(getProgram(), "bound");
    }

    public void setImageSize(int i, int i2) {
        float f;
        float f2;
        if (i == 0 || i2 == 0) {
            return;
        }
        int i3 = this.mOutputWidth;
        float f3 = i;
        int i4 = this.mOutputHeight;
        float f4 = i2;
        if (i3 / f3 > i4 / f4) {
            f = i4;
            f2 = (f3 * f) / f4;
        } else {
            float f5 = i3;
            f = (f4 * f5) / f3;
            f2 = f5;
        }
        setFloatVec2(this.mBoundLocation, new float[]{1.0f / f2, 1.0f / f});
    }
}
