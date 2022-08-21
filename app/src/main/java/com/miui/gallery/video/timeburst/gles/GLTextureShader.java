package com.miui.gallery.video.timeburst.gles;

import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class GLTextureShader {
    public int mGLAttribPosition;
    public int mGLAttribTextureCoordinate;
    public FloatBuffer mGLCubeBuffer;
    public int mGLProgId;
    public FloatBuffer mGLTextureBuffer;
    public int mGLUniformTexture;
    public final LinkedList<Runnable> mRunOnDraw;
    public static final float[] TEXTURE_NO_ROTATION = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    public static final float[] TEXTURE_90_ROTATION = {1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
    public static final float[] TEXTURE_180_ROTATION = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
    public static final float[] TEXTURE_270_ROTATION = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    public static final float[] CUBE = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    public static final float[] CUBE_REVERSE = {-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f};

    public void onPreDraw() {
    }

    public GLTextureShader() {
        this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    }

    public GLTextureShader(String str, String str2) {
        init(str, str2);
        this.mRunOnDraw = new LinkedList<>();
    }

    public void init(String str, String str2) {
        int loadProgram = OpenGlUtils.loadProgram(str, str2);
        this.mGLProgId = loadProgram;
        this.mGLAttribPosition = GLES20.glGetAttribLocation(loadProgram, "position");
        this.mGLUniformTexture = GLES20.glGetUniformLocation(this.mGLProgId, "inputImageTexture");
        this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
        float[] fArr = TEXTURE_NO_ROTATION;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLTextureBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr).position(0);
        float[] fArr2 = CUBE;
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLCubeBuffer = asFloatBuffer2;
        asFloatBuffer2.put(fArr2).position(0);
    }

    public void draw(int i) {
        draw(i, CUBE);
    }

    public void draw(int i, float[] fArr) {
        draw(i, fArr, TEXTURE_NO_ROTATION);
    }

    public void draw(int i, float[] fArr, float[] fArr2) {
        GLES20.glUseProgram(this.mGLProgId);
        onPreDraw();
        runPendingOnDrawTasks();
        this.mGLCubeBuffer.put(fArr);
        this.mGLCubeBuffer.position(0);
        GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
        GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, (Buffer) this.mGLCubeBuffer);
        this.mGLTextureBuffer.put(fArr2);
        this.mGLTextureBuffer.position(0);
        GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
        GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, (Buffer) this.mGLTextureBuffer);
        if (i != -1) {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, i);
            GLES20.glUniform1i(this.mGLUniformTexture, 0);
        }
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
        GLES20.glBindTexture(3553, 0);
    }

    public void runPendingOnDrawTasks() {
        while (!this.mRunOnDraw.isEmpty()) {
            this.mRunOnDraw.removeFirst().run();
        }
    }

    public void destroy() {
        GLES20.glDeleteProgram(this.mGLProgId);
    }
}
