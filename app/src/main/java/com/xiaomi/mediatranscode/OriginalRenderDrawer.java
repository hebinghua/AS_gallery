package com.xiaomi.mediatranscode;

import android.opengl.GLES30;
import android.opengl.Matrix;

/* loaded from: classes3.dex */
public class OriginalRenderDrawer extends BaseRenderDrawer {
    private int af_Position;
    private int av_Position;
    private int av_cropBottom;
    private int av_cropLeft;
    private int av_cropRight;
    private int av_cropTop;
    private int av_format;
    private int av_height;
    private int av_width;
    private int decodeHeight;
    private int decodeWidth;
    private int lut_Texture;
    private int mInputTextureId;
    private int mLutTextureId;
    private int mOutputTextureId;
    private int s_Texture;
    private int s_mvp;
    private final float[] modelMatrix = new float[16];
    private boolean mReserverResolution = true;
    private int cropTop = 0;
    private int cropLeft = 0;
    private int cropBottom = 0;
    private int cropRight = 0;
    private int format = 0;

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public String getFragmentSource() {
        return "#extension GL_OES_EGL_image_external : require \nprecision highp float; varying vec2 v_texPo; uniform samplerExternalOES s_Texture; uniform sampler2D lut_Texture; uniform int av_cropTop; uniform int av_cropLeft; uniform int av_cropBottom; uniform int av_cropRight ;uniform int av_format ;uniform int av_width; uniform int av_height; vec4 result;float m1 = 2610.0 / 16384.0;float m2 = 2523.0 / 4096.0 * 128.0;float c1 = 3423.0 / 4096.0;float c2 = 2413.0 / 4096.0 * 32.0;float c3 = 2392.0 / 4096.0 * 32.0;float linearProc(float src) {    float A = max(pow(src, 1.0 / m2) - c1, 0.0);    float B = c2 - c3 * pow(src, 1.0 / m2);    return pow(A / B, (1.0 / m1)) * 10.0;}float gammaProc(float src) {    return min(max(pow(src, 0.45), 0.0), 1.0);}float PQCurl(float src) {   return texture2D(lut_Texture, vec2(src, 0.5)).a;} vec3 applyMat(vec3 incolor) {   mat3 m = mat3(1.3436, -0.2822, -0.0614,                 -0.0653, 1.07578, -0.0105,                 -0.0028, -0.0196, 1.0168);    return (incolor.rgb * m); } void main() {     vec2 uv = v_texPo;     if(av_width - av_cropRight > 1) {         uv.x = uv.x * ( float(av_cropRight) + 1.0) / (float(av_width) * 1.0);     }     if(av_height - av_cropBottom > 1) {         uv.y = uv.y * (float(av_cropBottom) * 1.0 + 1.0)  / (float(av_height) * 1.0);     }     gl_FragColor = texture2D(s_Texture, uv);     gl_FragColor.a = 1.0 ;} ";
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public String getVertexSource() {
        return "attribute vec4 av_Position; attribute vec2 af_Position; varying vec2 v_texPo; uniform mat4 modelViewProjectionMatrix;void main() {     v_texPo = af_Position;     gl_Position = modelViewProjectionMatrix * av_Position; }";
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void onCreated() {
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void release() {
    }

    private static void checkNoGLES2Error() {
        int glGetError = GLES30.glGetError();
        if (glGetError != 0) {
            Logg.LogE("GLES30 error:" + glGetError);
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

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
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
        this.av_format = GLES30.glGetUniformLocation(this.mProgram, "av_format");
        checkNoGLES2Error();
        this.s_Texture = GLES30.glGetUniformLocation(this.mProgram, "s_Texture");
        this.s_mvp = GLES30.glGetUniformLocation(this.mProgram, "modelViewProjectionMatrix");
        this.lut_Texture = GLES30.glGetUniformLocation(this.mProgram, "lut_Texture");
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void onCroped(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.cropTop = i3;
        this.cropLeft = i4;
        this.cropBottom = i5;
        this.cropRight = i6;
        this.decodeWidth = i;
        this.decodeHeight = i2;
        this.format = i7;
        Logg.LogI(" onCroped width:" + i + " height :" + i2 + " cropTop:" + i3 + " cropLeft:" + i4 + " cropBottom:" + i5 + " cropRight:" + i6 + " format " + i7);
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void onDraw() {
        if (this.mInputTextureId == 0 || this.mOutputTextureId == 0) {
            Logg.LogI("not inited");
            return;
        }
        Matrix.setIdentityM(this.modelMatrix, 0);
        Matrix.rotateM(this.modelMatrix, 0, 0.0f, 0.0f, 0.0f, 1.0f);
        int i = 1;
        GLES30.glUniformMatrix4fv(this.s_mvp, 1, false, this.modelMatrix, 0);
        GLES30.glUniform1i(this.av_cropTop, this.cropTop);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_cropLeft, this.cropLeft);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_cropBottom, this.cropBottom);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_cropRight, this.cropRight);
        checkNoGLES2Error();
        int i2 = this.av_format;
        if (this.format != 2141391881) {
            i = 0;
        }
        GLES30.glUniform1i(i2, i);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_width, this.decodeWidth);
        checkNoGLES2Error();
        GLES30.glUniform1i(this.av_height, this.decodeHeight);
        checkNoGLES2Error();
        GLES30.glEnableVertexAttribArray(this.av_Position);
        GLES30.glEnableVertexAttribArray(this.af_Position);
        GLES30.glBindBuffer(34962, this.mVertexBufferId);
        GLES30.glVertexAttribPointer(this.av_Position, 2, 5126, false, 0, 0);
        GLES30.glBindBuffer(34962, this.mFrameTextureBufferId);
        GLES30.glVertexAttribPointer(this.af_Position, 2, 5126, false, 0, 0);
        GLES30.glBindBuffer(34962, 0);
        bindTexture(this.mInputTextureId, this.mLutTextureId);
        GLES30.glDrawArrays(5, 0, this.VertexCount);
        GLES30.glGenerateMipmap(3553);
        unBindTexure();
        GLES30.glDisableVertexAttribArray(this.av_Position);
        GLES30.glDisableVertexAttribArray(this.af_Position);
    }

    private void bindTexture(int i, int i2) {
        GLES30.glActiveTexture(33984);
        GLES30.glBindTexture(36197, i);
        GLES30.glUniform1i(this.s_Texture, 0);
    }

    private void unBindTexure() {
        GLES30.glBindTexture(36197, 0);
    }

    public void setLutTextureId(int i) {
        this.mLutTextureId = i;
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void setInputTextureId(int i) {
        this.mInputTextureId = i;
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public int getOutputTextureId() {
        return this.mOutputTextureId;
    }

    @Override // com.xiaomi.mediatranscode.BaseRenderDrawer
    public void setReserverResolution(boolean z) {
        this.mReserverResolution = z;
    }
}
