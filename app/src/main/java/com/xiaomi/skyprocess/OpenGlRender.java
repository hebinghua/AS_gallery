package com.xiaomi.skyprocess;

import android.util.Log;

/* loaded from: classes3.dex */
public class OpenGlRender {
    private static String TAG = "OpenGlRender";
    private int mAttribtexture;
    private int mAttribvertex;
    private int mTextureUniformU;
    private int mTextureUniformV;
    private int mTextureUniformY;
    private byte[] mTextureVertices_buffer;
    private int mTexture_u;
    private int mTexture_v;
    private int mTexture_y;
    private byte[] mVertexVertices_buffer;

    private static native void RenderFrameJni();

    private static native void SetCurrentGLContextForGraphJni();

    private static native void SetCurrentGLContextJni();

    private static native void SetOpengGlRenderParamsJni(int i, int i2, int i3, int i4, byte[] bArr, byte[] bArr2);

    private static native void SetWindowSizeJni(int i, int i2, int i3, int i4);

    private static native void setCopyTextureOfFBOJni(int i, int i2);

    public OpenGlRender() {
        Log.d(TAG, "construction");
    }

    public void SetCurrentGLContext() {
        Log.d(TAG, "SetCurrentGLContext");
        SetCurrentGLContextJni();
    }

    public void SetCurrentGLContextJniForGraph() {
        Log.d(TAG, "SetCurrentGLContext");
        SetCurrentGLContextForGraphJni();
    }

    public void SetOpengGlRenderParams(int i, int i2, int i3, int i4, byte[] bArr, byte[] bArr2) {
        Log.d(TAG, "SetOpengGlRenderParams");
        SetOpengGlRenderParamsJni(i, i2, i3, i4, bArr, bArr2);
    }

    public void setCopyTextureOfFBO(int i, int i2) {
        Log.d(TAG, "setCopyTextureOfFBO");
        setCopyTextureOfFBOJni(i, i2);
    }

    public void SetWindowSize(int i, int i2, int i3, int i4) {
        SetWindowSizeJni(i, i2, i3, i4);
    }

    public void RenderFrame() {
        RenderFrameJni();
    }
}
