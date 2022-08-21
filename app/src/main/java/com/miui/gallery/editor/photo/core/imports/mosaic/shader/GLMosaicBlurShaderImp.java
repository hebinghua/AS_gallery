package com.miui.gallery.editor.photo.core.imports.mosaic.shader;

import android.opengl.GLES20;
import com.miui.gallery.editor.photo.widgets.glview.shader.GLShaderGroup;
import com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader;

/* loaded from: classes2.dex */
public class GLMosaicBlurShaderImp extends GLShaderGroup {
    public float mRatio;

    public GLMosaicBlurShaderImp(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
        addShader(new GLMosaicBlurShader(i, i2));
        addShader(new GLMosaicBlurShader(i, i2));
        this.mRatio = 1.7f;
        notifyShaderAdded();
    }

    @Override // com.miui.gallery.editor.photo.widgets.glview.shader.GLShaderGroup, com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLTextureSizeShader, com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader
    public void onPreDraw() {
        GLTextureShader gLTextureShader = this.mShaders.get(0);
        float f = this.mRatio;
        float f2 = this.mScale;
        float f3 = (f * f2) / ((GLShaderGroup) this).mTextureWidth;
        float f4 = (f * f2) / ((GLShaderGroup) this).mTextureHeight;
        int glGetUniformLocation = GLES20.glGetUniformLocation(gLTextureShader.getProgram(), "texelWidthOffset");
        int glGetUniformLocation2 = GLES20.glGetUniformLocation(gLTextureShader.getProgram(), "texelHeightOffset");
        gLTextureShader.setFloat(glGetUniformLocation, f4);
        gLTextureShader.setFloat(glGetUniformLocation2, 0.0f);
        GLTextureShader gLTextureShader2 = this.mShaders.get(1);
        int glGetUniformLocation3 = GLES20.glGetUniformLocation(gLTextureShader2.getProgram(), "texelWidthOffset");
        int glGetUniformLocation4 = GLES20.glGetUniformLocation(gLTextureShader2.getProgram(), "texelHeightOffset");
        gLTextureShader2.setFloat(glGetUniformLocation3, 0.0f);
        gLTextureShader2.setFloat(glGetUniformLocation4, f3);
    }
}
