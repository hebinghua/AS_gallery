package com.miui.gallery.editor.photo.widgets.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import com.miui.filtersdk.utils.Rotation;
import com.miui.filtersdk.utils.TextureRotationUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public abstract class MagicBaseView extends GLSurfaceView implements GLSurfaceView.Renderer {
    public FloatBuffer gLCubeBuffer;
    public FloatBuffer gLTextureBuffer;
    public int imageHeight;
    public int imageWidth;
    public ScaleType scaleType;
    public int surfaceHeight;
    public int surfaceWidth;
    public int textureId;

    /* loaded from: classes2.dex */
    public enum ScaleType {
        CENTER_INSIDE,
        CENTER_CROP,
        FIT_XY
    }

    public final float addDistance(float f, float f2) {
        return f == 0.0f ? f2 : 1.0f - f2;
    }

    public MagicBaseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.textureId = -1;
        this.scaleType = ScaleType.FIT_XY;
        float[] fArr = TextureRotationUtil.CUBE;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.gLCubeBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr).position(0);
        float[] fArr2 = TextureRotationUtil.TEXTURE_NO_ROTATION;
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.gLTextureBuffer = asFloatBuffer2;
        asFloatBuffer2.put(fArr2).position(0);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(0);
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glDisable(3024);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(2884);
        GLES20.glEnable(2929);
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
        this.surfaceWidth = i;
        this.surfaceHeight = i2;
    }

    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(16640);
    }

    public void adjustSize(int i, boolean z, boolean z2) {
        adjustSize(this.imageWidth, this.imageHeight, this.surfaceWidth, this.surfaceHeight, i, z, z2, this.gLCubeBuffer, this.gLTextureBuffer);
    }

    public void adjustSize(int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        float f;
        float f2;
        float f3;
        float f4;
        float[] rotation = TextureRotationUtil.getRotation(Rotation.fromInt(i5), z, z2);
        float[] fArr = TextureRotationUtil.CUBE;
        float f5 = i3;
        float f6 = i4;
        float max = Math.max(f5 / i, f6 / i2);
        float round = Math.round(f * max) / f5;
        float round2 = Math.round(f2 * max) / f6;
        ScaleType scaleType = this.scaleType;
        if (scaleType == ScaleType.CENTER_INSIDE) {
            fArr = new float[]{fArr[0] / round2, fArr[1] / round, fArr[2] / round2, fArr[3] / round, fArr[4] / round2, fArr[5] / round, fArr[6] / round2, fArr[7] / round};
        } else if (scaleType != ScaleType.FIT_XY && scaleType == ScaleType.CENTER_CROP) {
            if (Rotation.fromInt(i5) == Rotation.ROTATION_90 || Rotation.fromInt(i5) == Rotation.ROTATION_270) {
                float f7 = (1.0f - (1.0f / round)) / 2.0f;
                f3 = (1.0f - (1.0f / round2)) / 2.0f;
                f4 = f7;
            } else {
                f3 = (1.0f - (1.0f / round)) / 2.0f;
                f4 = (1.0f - (1.0f / round2)) / 2.0f;
            }
            rotation = new float[]{addDistance(rotation[0], f3), addDistance(rotation[1], f4), addDistance(rotation[2], f3), addDistance(rotation[3], f4), addDistance(rotation[4], f3), addDistance(rotation[5], f4), addDistance(rotation[6], f3), addDistance(rotation[7], f4)};
        }
        floatBuffer.clear();
        floatBuffer.put(fArr).position(0);
        floatBuffer2.clear();
        floatBuffer2.put(rotation).position(0);
    }
}
