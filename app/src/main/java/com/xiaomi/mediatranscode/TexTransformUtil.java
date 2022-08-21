package com.xiaomi.mediatranscode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* loaded from: classes3.dex */
public class TexTransformUtil {
    public static final float[] TEX_COORDS = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
    public static final float[] VERTEX_COORDS = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};

    public static float[] getTexCoords() {
        return TEX_COORDS;
    }

    public static float[] getVertexCoords() {
        return VERTEX_COORDS;
    }

    public static float[] getHFlipTexCoords() {
        return getHFlipTexCoords(TEX_COORDS);
    }

    public static float[] getHFlipTexCoords(float[] fArr) {
        float[] fArr2 = new float[fArr.length];
        System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
        System.arraycopy(fArr, 2, fArr2, 0, 2);
        System.arraycopy(fArr, 0, fArr2, 2, 2);
        System.arraycopy(fArr, 6, fArr2, 4, 2);
        System.arraycopy(fArr, 4, fArr2, 6, 2);
        return fArr2;
    }

    public static float[] getVFlipTexCoords() {
        return getVFlipTexCoords(TEX_COORDS);
    }

    public static float[] getVFlipTexCoords(float[] fArr) {
        float[] fArr2 = new float[fArr.length];
        System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
        System.arraycopy(fArr, 4, fArr2, 0, 2);
        System.arraycopy(fArr, 6, fArr2, 2, 2);
        System.arraycopy(fArr, 0, fArr2, 4, 2);
        System.arraycopy(fArr, 2, fArr2, 6, 2);
        return fArr2;
    }

    public static float[] getRotateTexCoords(int i) {
        return getRotateTexCoords(TEX_COORDS, i);
    }

    public static float[] getRotateTexCoords(float[] fArr, int i) {
        float[] fArr2 = new float[fArr.length];
        System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
        if (i == 90) {
            System.arraycopy(fArr, 2, fArr2, 0, 2);
            System.arraycopy(fArr, 6, fArr2, 2, 2);
            System.arraycopy(fArr, 0, fArr2, 4, 2);
            System.arraycopy(fArr, 4, fArr2, 6, 2);
        } else if (i == 180) {
            System.arraycopy(fArr, 6, fArr2, 0, 2);
            System.arraycopy(fArr, 4, fArr2, 2, 2);
            System.arraycopy(fArr, 2, fArr2, 4, 2);
            System.arraycopy(fArr, 0, fArr2, 6, 2);
        } else if (i == 270) {
            System.arraycopy(fArr, 4, fArr2, 0, 2);
            System.arraycopy(fArr, 0, fArr2, 2, 2);
            System.arraycopy(fArr, 6, fArr2, 4, 2);
            System.arraycopy(fArr, 2, fArr2, 6, 2);
        }
        return fArr2;
    }

    public static FloatBuffer createFloatBuffer(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        return asFloatBuffer;
    }
}
