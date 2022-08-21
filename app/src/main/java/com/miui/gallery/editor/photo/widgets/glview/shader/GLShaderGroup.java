package com.miui.gallery.editor.photo.widgets.glview.shader;

import android.opengl.GLES20;
import com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLTextureSizeShader;
import com.miui.gallery.editor.photo.widgets.glview.GLFBOManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class GLShaderGroup extends GLTextureSizeShader {
    public GLFBOManager[] mGLFBOManagers;
    public List<GLTextureShader> mShaders;
    public int mTextureHeight;
    public int mTextureWidth;
    public int mViewHeight;
    public int mViewWidth;

    @Override // com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader
    public void draw(int i, float[] fArr, float[] fArr2) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLTextureSizeShader, com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader
    public void init(String str, String str2) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.mosaic.shader.GLTextureSizeShader, com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader
    public void onPreDraw() {
    }

    public GLShaderGroup(int i, int i2, int i3, int i4) {
        super(null, null, i, i2);
        this.mShaders = new ArrayList();
        this.mTextureWidth = i;
        this.mTextureHeight = i2;
        this.mViewWidth = i3;
        this.mViewHeight = i4;
    }

    public void addShader(GLTextureShader gLTextureShader) {
        this.mShaders.add(gLTextureShader);
    }

    public int getEffectedTexture(int i) {
        GLFBOManager[] gLFBOManagerArr;
        onPreDraw();
        for (int i2 = 0; i2 < this.mShaders.size(); i2++) {
            GLTextureShader gLTextureShader = this.mShaders.get(i2);
            this.mGLFBOManagers[i2].bind();
            GLES20.glClear(16640);
            int i3 = i2 - 1;
            if (i3 >= 0) {
                gLTextureShader.drawFBO(this.mGLFBOManagers[i3].getTextureId());
            } else {
                gLTextureShader.draw(i);
            }
            this.mGLFBOManagers[i2].unBind();
        }
        return this.mGLFBOManagers[gLFBOManagerArr.length - 1].getTextureId();
    }

    public void notifyShaderAdded() {
        this.mGLFBOManagers = new GLFBOManager[this.mShaders.size()];
        int i = 0;
        while (true) {
            GLFBOManager[] gLFBOManagerArr = this.mGLFBOManagers;
            if (i < gLFBOManagerArr.length) {
                gLFBOManagerArr[i] = new GLFBOManager(this.mTextureWidth, this.mTextureHeight, this.mViewWidth, this.mViewHeight);
                i++;
            } else {
                return;
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader
    public void destroy() {
        for (GLTextureShader gLTextureShader : this.mShaders) {
            gLTextureShader.destroy();
        }
        GLFBOManager[] gLFBOManagerArr = this.mGLFBOManagers;
        if (gLFBOManagerArr != null) {
            for (GLFBOManager gLFBOManager : gLFBOManagerArr) {
                if (gLFBOManager != null) {
                    gLFBOManager.clear();
                }
            }
        }
    }
}
