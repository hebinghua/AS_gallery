package com.xiaomi.mediacodec;

import android.opengl.GLES30;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* loaded from: classes3.dex */
public abstract class BaseRenderDrawer {
    public final int VertexCount;
    public int height;
    private FloatBuffer mBackTextureBuffer;
    public int mBackTextureBufferId;
    private FloatBuffer mDisplayTextureBuffer;
    public int mDisplayTextureBufferId;
    private FloatBuffer mFrameTextureBuffer;
    public int mFrameTextureBufferId;
    private FloatBuffer mFrontTextureBuffer;
    public int mFrontTextureBufferId;
    public int mProgram;
    private FloatBuffer mVertexBuffer;
    public int mVertexBufferId;
    public float[] vertexData;
    public int width;
    public float[] frontTextureData = {1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
    public float[] backTextureData = {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f};
    public float[] displayTextureData = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    public float[] frameBufferData = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
    public final int CoordsPerVertexCount = 2;
    public final int VertexStride = 8;
    public final int CoordsPerTextureCount = 2;
    public final int TextureStride = 8;

    public abstract String getFragmentSource();

    public abstract int getOutputTextureId();

    public abstract String getVertexSource();

    public abstract void onChanged(int i, int i2);

    public abstract void onCreated();

    public abstract void onCroped(int i, int i2, int i3, int i4, int i5, int i6);

    public abstract void onDraw();

    public abstract void release();

    public abstract void setInputTextureId(int i);

    public abstract void setReserverResolution(boolean z);

    public BaseRenderDrawer() {
        float[] fArr = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        this.vertexData = fArr;
        this.VertexCount = fArr.length / 2;
    }

    public void create() {
        this.mProgram = GlesUtil.createProgram(getVertexSource(), getFragmentSource());
        initVertexBufferObjects();
        onCreated();
    }

    public void surfaceChangedSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        onChanged(i, i2);
    }

    public void cropSize(int i, int i2, int i3, int i4, int i5, int i6) {
        onCroped(i, i2, i3, i4, i5, i6);
    }

    public void draw(long j, float[] fArr) {
        clear();
        useProgram();
        viewPort(0, 0, this.width, this.height);
        onDraw();
    }

    public void clear() {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES30.glClear(16640);
    }

    public void initVertexBufferObjects() {
        int[] iArr = new int[5];
        GLES30.glGenBuffers(5, iArr, 0);
        FloatBuffer put = ByteBuffer.allocateDirect(this.vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.vertexData);
        this.mVertexBuffer = put;
        put.position(0);
        int i = iArr[0];
        this.mVertexBufferId = i;
        GLES30.glBindBuffer(34962, i);
        GLES30.glBufferData(34962, this.vertexData.length * 4, this.mVertexBuffer, 35044);
        FloatBuffer put2 = ByteBuffer.allocateDirect(this.backTextureData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.backTextureData);
        this.mBackTextureBuffer = put2;
        put2.position(0);
        int i2 = iArr[1];
        this.mBackTextureBufferId = i2;
        GLES30.glBindBuffer(34962, i2);
        GLES30.glBufferData(34962, this.backTextureData.length * 4, this.mBackTextureBuffer, 35044);
        FloatBuffer put3 = ByteBuffer.allocateDirect(this.frontTextureData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.frontTextureData);
        this.mFrontTextureBuffer = put3;
        put3.position(0);
        int i3 = iArr[2];
        this.mFrontTextureBufferId = i3;
        GLES30.glBindBuffer(34962, i3);
        GLES30.glBufferData(34962, this.frontTextureData.length * 4, this.mFrontTextureBuffer, 35044);
        FloatBuffer put4 = ByteBuffer.allocateDirect(this.displayTextureData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.displayTextureData);
        this.mDisplayTextureBuffer = put4;
        put4.position(0);
        int i4 = iArr[3];
        this.mDisplayTextureBufferId = i4;
        GLES30.glBindBuffer(34962, i4);
        GLES30.glBufferData(34962, this.displayTextureData.length * 4, this.mDisplayTextureBuffer, 35044);
        FloatBuffer put5 = ByteBuffer.allocateDirect(this.frameBufferData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(this.frameBufferData);
        this.mFrameTextureBuffer = put5;
        put5.position(0);
        int i5 = iArr[4];
        this.mFrameTextureBufferId = i5;
        GLES30.glBindBuffer(34962, i5);
        GLES30.glBufferData(34962, this.frameBufferData.length * 4, this.mFrameTextureBuffer, 35044);
        GLES30.glBindBuffer(34962, 0);
    }

    public void releaseVertexBufferObjects() {
        GLES30.glDeleteBuffers(1, new int[]{this.mVertexBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mBackTextureBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mFrontTextureBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mDisplayTextureBufferId}, 0);
        GLES30.glDeleteBuffers(1, new int[]{this.mFrameTextureBufferId}, 0);
        this.mVertexBuffer = null;
        this.mBackTextureBuffer = null;
        this.mFrontTextureBuffer = null;
        this.mFrameTextureBuffer = null;
    }

    public void destroy() {
        releaseVertexBufferObjects();
        int i = this.mProgram;
        if (i != 0) {
            GlesUtil.DestoryProgram(i);
        }
    }

    public void useProgram() {
        GLES30.glUseProgram(this.mProgram);
    }

    public void viewPort(int i, int i2, int i3, int i4) {
        GLES30.glViewport(i, i2, i3, i4);
    }
}
