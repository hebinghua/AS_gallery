package com.miui.filtersdk.filter.base;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.miui.filtersdk.utils.OpenGlUtils;
import com.miui.filtersdk.utils.Rotation;
import com.miui.filtersdk.utils.TextureRotationUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class GPUImageFilter {
    public final String mFragmentShader;
    public int[] mFrameBufferTextures;
    public int[] mFrameBuffers;
    public int mFrameHeight;
    public int mFrameWidth;
    public int mGLAttribPosition;
    public int mGLAttribTextureCoordinate;
    public FloatBuffer mGLCubeBuffer;
    public int mGLProgId;
    public FloatBuffer mGLTextureBuffer;
    public int mGLUniformTexture;
    public int mInputHeight;
    public int mInputWidth;
    public boolean mIsInitialized;
    public int mOutputHeight;
    public int mOutputWidth;
    public final LinkedList<Runnable> mRunOnDraw;
    public int mTextureId;
    public final String mVertexShader;

    public void onDrawArraysAfter() {
    }

    public void onDrawArraysPre() {
    }

    public void onInitialized() {
    }

    public GPUImageFilter() {
        this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    }

    public GPUImageFilter(String str, String str2) {
        this.mTextureId = -1;
        this.mFrameBuffers = null;
        this.mFrameBufferTextures = null;
        this.mFrameWidth = -1;
        this.mFrameHeight = -1;
        str = str == null ? "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}" : str;
        str2 = str2 == null ? "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}" : str2;
        this.mRunOnDraw = new LinkedList<>();
        this.mVertexShader = str;
        this.mFragmentShader = str2;
        float[] fArr = TextureRotationUtil.CUBE;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLCubeBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr).position(0);
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mGLTextureBuffer = asFloatBuffer2;
        asFloatBuffer2.put(TextureRotationUtil.getRotation(Rotation.NORMAL, false, true)).position(0);
    }

    public void init() {
        onInit();
        this.mIsInitialized = true;
        onInitialized();
    }

    public void onInit() {
        int loadProgram = OpenGlUtils.loadProgram(this.mVertexShader, this.mFragmentShader);
        this.mGLProgId = loadProgram;
        this.mGLAttribPosition = GLES20.glGetAttribLocation(loadProgram, "position");
        this.mGLUniformTexture = GLES20.glGetUniformLocation(this.mGLProgId, "inputImageTexture");
        this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
        this.mIsInitialized = true;
    }

    public final void destroy() {
        this.mIsInitialized = false;
        GLES20.glDeleteProgram(this.mGLProgId);
        onDestroy();
    }

    public void onDestroy() {
        destroyFrameBuffers();
    }

    public void onInputSizeChanged(int i, int i2) {
        this.mInputWidth = i;
        this.mInputHeight = i2;
    }

    public int onDrawFrame(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        GLES20.glUseProgram(this.mGLProgId);
        runPendingOnDrawTasks();
        if (!this.mIsInitialized) {
            return -1;
        }
        floatBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
        floatBuffer2.position(0);
        GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, (Buffer) floatBuffer2);
        GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
        if (i != -1) {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, i);
            GLES20.glUniform1i(this.mGLUniformTexture, 0);
        }
        onDrawArraysPre();
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
        onDrawArraysAfter();
        GLES20.glBindTexture(3553, 0);
        return 1;
    }

    public int onDrawToTexture(int i) {
        return onDrawToTexture(i, this.mGLCubeBuffer, this.mGLTextureBuffer);
    }

    public int onDrawToTexture(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (this.mFrameBuffers == null) {
            return -1;
        }
        GLES20.glUseProgram(this.mGLProgId);
        runPendingOnDrawTasks();
        if (!isInitialized()) {
            return -1;
        }
        GLES20.glViewport(0, 0, this.mFrameWidth, this.mFrameHeight);
        GLES20.glBindFramebuffer(36160, this.mFrameBuffers[0]);
        floatBuffer.position(0);
        GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
        floatBuffer2.position(0);
        GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, (Buffer) floatBuffer2);
        GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
        if (i != -1) {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, i);
            GLES20.glUniform1i(this.mGLUniformTexture, 0);
        }
        onDrawArraysPre();
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
        onDrawArraysAfter();
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glViewport(0, 0, this.mOutputWidth, this.mOutputHeight);
        return this.mFrameBufferTextures[0];
    }

    public void initFrameBuffers(int i, int i2) {
        if (this.mFrameBuffers != null && (this.mFrameWidth != i || this.mFrameHeight != i2)) {
            destroyFrameBuffers();
        }
        if (this.mFrameBuffers == null) {
            this.mFrameWidth = i;
            this.mFrameHeight = i2;
            int[] iArr = new int[1];
            this.mFrameBuffers = iArr;
            this.mFrameBufferTextures = new int[1];
            GLES20.glGenFramebuffers(1, iArr, 0);
            GLES20.glGenTextures(1, this.mFrameBufferTextures, 0);
            GLES20.glBindTexture(3553, this.mFrameBufferTextures[0]);
            GLES20.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glBindFramebuffer(36160, this.mFrameBuffers[0]);
            GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mFrameBufferTextures[0], 0);
            GLES20.glBindTexture(3553, 0);
            GLES20.glBindFramebuffer(36160, 0);
        }
    }

    public void destroyFrameBuffers() {
        int[] iArr = this.mFrameBufferTextures;
        if (iArr != null) {
            GLES20.glDeleteTextures(1, iArr, 0);
            this.mFrameBufferTextures = null;
        }
        int[] iArr2 = this.mFrameBuffers;
        if (iArr2 != null) {
            GLES20.glDeleteFramebuffers(1, iArr2, 0);
            this.mFrameBuffers = null;
        }
        this.mFrameWidth = -1;
        this.mFrameHeight = -1;
    }

    public void runPendingOnDrawTasks() {
        synchronized (this.mRunOnDraw) {
            while (!this.mRunOnDraw.isEmpty()) {
                this.mRunOnDraw.removeFirst().run();
            }
        }
    }

    public boolean isInitialized() {
        return this.mIsInitialized;
    }

    public int getProgram() {
        return this.mGLProgId;
    }

    public void setFloat(final int i, final float f) {
        runOnDraw(new Runnable() { // from class: com.miui.filtersdk.filter.base.GPUImageFilter.2
            @Override // java.lang.Runnable
            public void run() {
                GLES20.glUniform1f(i, f);
            }
        });
    }

    public void setFloatVec2(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: com.miui.filtersdk.filter.base.GPUImageFilter.3
            @Override // java.lang.Runnable
            public void run() {
                GLES20.glUniform2fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    public void setFloatVec3(final int i, final float[] fArr) {
        runOnDraw(new Runnable() { // from class: com.miui.filtersdk.filter.base.GPUImageFilter.4
            @Override // java.lang.Runnable
            public void run() {
                GLES20.glUniform3fv(i, 1, FloatBuffer.wrap(fArr));
            }
        });
    }

    public void setPoint(final int i, final PointF pointF) {
        runOnDraw(new Runnable() { // from class: com.miui.filtersdk.filter.base.GPUImageFilter.7
            @Override // java.lang.Runnable
            public void run() {
                PointF pointF2 = pointF;
                GLES20.glUniform2fv(i, 1, new float[]{pointF2.x, pointF2.y}, 0);
            }
        });
    }

    public void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.addLast(runnable);
        }
    }

    public void onDisplaySizeChanged(int i, int i2) {
        this.mOutputWidth = i;
        this.mOutputHeight = i2;
    }
}
