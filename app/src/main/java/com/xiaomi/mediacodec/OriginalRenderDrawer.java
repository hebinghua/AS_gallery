package com.xiaomi.mediacodec;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

/* loaded from: classes3.dex */
public class OriginalRenderDrawer extends BaseRenderDrawer {
    private int af_Position;
    private int av_Position;
    private int av_cropBottom;
    private int av_cropLeft;
    private int av_cropRight;
    private int av_cropTop;
    private int av_height;
    private int av_width;
    private int decodeHeight;
    private int decodeWidth;
    private int mInputTextureId;
    private int mOutputTextureId;
    private int s_Texture;
    private int s_mvp;
    private final float[] modelMatrix = new float[16];
    private boolean mReserverResolution = true;
    private int cropTop = 0;
    private int cropLeft = 0;
    private int cropBottom = 0;
    private int cropRight = 0;

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public String getFragmentSource() {
        return "#extension GL_OES_EGL_image_external : require \nprecision mediump float; varying vec2 v_texPo; uniform samplerExternalOES s_Texture; uniform int av_cropTop; uniform int av_cropLeft; uniform int av_cropBottom; uniform int av_cropRight ;uniform int av_width; uniform int av_height; void main() {     vec2 uv = v_texPo;     if(av_width - av_cropRight > 1) {         uv.x = uv.x * ( float(av_cropRight) + 1.0) / (float(av_width) * 1.0);     }     if(av_height - av_cropBottom > 1) {         uv.y = uv.y * (float(av_cropBottom) * 1.0 + 1.0)  / (float(av_height) * 1.0);     }     gl_FragColor = texture2D(s_Texture, uv); } ";
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public String getVertexSource() {
        return "attribute vec4 av_Position; attribute vec2 af_Position; varying vec2 v_texPo; uniform mat4 modelViewProjectionMatrix;void main() {     v_texPo = af_Position;     gl_Position = modelViewProjectionMatrix * av_Position; }";
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onCreated() {
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void release() {
    }

    private static void checkNoGLES2Error() {
        int glGetError = GLES30.glGetError();
        if (glGetError != 0) {
            Log.e("checkNoGLES2Error", "GLES30 error:" + glGetError);
        }
        boolean z = glGetError == 0;
        abortUnless(z, "GLES30 error: " + glGetError);
    }

    private static void abortUnless(boolean z, String str) {
        if (z) {
            return;
        }
        throw new RuntimeException(str);
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onChanged(int i, int i2) {
        this.mOutputTextureId = GlesUtil.createFrameTexture(i, i2, this.mReserverResolution);
        this.av_Position = GLES30.glGetAttribLocation(this.mProgram, "av_Position");
        this.af_Position = GLES30.glGetAttribLocation(this.mProgram, "af_Position");
        this.av_width = GLES30.glGetUniformLocation(this.mProgram, "av_width");
        checkNoGLES2Error();
        this.av_height = GLES30.glGetUniformLocation(this.mProgram, "av_height");
        checkNoGLES2Error();
        this.av_cropTop = GLES30.glGetUniformLocation(this.mProgram, "av_cropTop");
        checkNoGLES2Error();
        this.av_cropLeft = GLES30.glGetUniformLocation(this.mProgram, "av_cropLeft");
        checkNoGLES2Error();
        this.av_cropBottom = GLES30.glGetUniformLocation(this.mProgram, "av_cropBottom");
        checkNoGLES2Error();
        this.av_cropRight = GLES30.glGetUniformLocation(this.mProgram, "av_cropRight");
        checkNoGLES2Error();
        this.s_Texture = GLES30.glGetUniformLocation(this.mProgram, "s_Texture");
        this.s_mvp = GLES30.glGetUniformLocation(this.mProgram, "modelViewProjectionMatrix");
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onCroped(int i, int i2, int i3, int i4, int i5, int i6) {
        this.cropTop = i3;
        this.cropLeft = i4;
        this.cropBottom = i5;
        this.cropRight = i6;
        this.decodeWidth = i;
        this.decodeHeight = i2;
        Logg.LogI(" onCroped width:" + i + " height :" + i2 + " cropTop:" + i3 + " cropLeft:" + i4 + " cropBottom:" + i5 + " cropRight:" + i6);
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void onDraw() {
        if (this.mInputTextureId == 0 || this.mOutputTextureId == 0) {
            Logg.LogI("not inited");
            return;
        }
        Matrix.setIdentityM(this.modelMatrix, 0);
        Matrix.rotateM(this.modelMatrix, 0, 0.0f, 0.0f, 0.0f, 1.0f);
        GLES30.glUniformMatrix4fv(this.s_mvp, 1, false, this.modelMatrix, 0);
        GLES30.glUniform1i(this.av_cropTop, this.cropTop);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_cropLeft, this.cropLeft);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_cropBottom, this.cropBottom);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_cropRight, this.cropRight);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_width, this.decodeWidth);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_height, this.decodeHeight);
        checkNoGLES2Error();
        GLES30.glEnableVertexAttribArray(this.av_Position);
        checkNoGLES2Error();
        GLES30.glEnableVertexAttribArray(this.af_Position);
        checkNoGLES2Error();
        GLES30.glBindBuffer(34962, this.mVertexBufferId);
        checkNoGLES2Error();
        GLES30.glVertexAttribPointer(this.av_Position, 2, 5126, false, 0, 0);
        checkNoGLES2Error();
        GLES30.glBindBuffer(34962, this.mFrameTextureBufferId);
        checkNoGLES2Error();
        GLES30.glVertexAttribPointer(this.af_Position, 2, 5126, false, 0, 0);
        GLES30.glBindBuffer(34962, 0);
        checkNoGLES2Error();
        bindTexture(this.mInputTextureId);
        checkNoGLES2Error();
        GLES30.glDrawArrays(5, 0, this.VertexCount);
        checkNoGLES2Error();
        checkNoGLES2Error();
        unBindTexure();
        checkNoGLES2Error();
        GLES30.glDisableVertexAttribArray(this.av_Position);
        checkNoGLES2Error();
        GLES30.glDisableVertexAttribArray(this.af_Position);
        checkNoGLES2Error();
    }

    private void bindTexture(int i) {
        GLES30.glActiveTexture(33984);
        GLES30.glBindTexture(36197, i);
        GLES30.glUniform1i(this.s_Texture, 0);
        checkNoGLES2Error();
    }

    private void unBindTexure() {
        GLES30.glBindTexture(36197, 0);
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void setInputTextureId(int i) {
        this.mInputTextureId = i;
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public int getOutputTextureId() {
        return this.mOutputTextureId;
    }

    @Override // com.xiaomi.mediacodec.BaseRenderDrawer
    public void setReserverResolution(boolean z) {
        this.mReserverResolution = z;
    }
}
