package com.xiaomi.mediatranscode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import java.nio.FloatBuffer;

/* loaded from: classes3.dex */
public class GlesUtil {
    private static float[] PQLut = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.07187E-4f, 1.24172E-4f, 1.41156E-4f, 1.5814E-4f, 1.75124E-4f, 2.23127E-4f, 2.7113E-4f, 3.19133E-4f, 3.67136E-4f, 4.34898E-4f, 5.0266E-4f, 5.70421E-4f, 6.38183E-4f, 7.26731E-4f, 8.15279E-4f, 9.03826E-4f, 9.92374E-4f, 0.001044716f, 0.001097057f, 0.001149398f, 0.00120174f, 0.001323033f, 0.001444327f, 0.001565621f, 0.001686915f, 0.001911643f, 0.00213637f, 0.002361098f, 0.002585826f, 0.002672449f, 0.002759072f, 0.002845695f, 0.002932318f, 0.003228283f, 0.003524248f, 0.003820212f, 0.004116177f, 0.004344184f, 0.00457219f, 0.004800197f, 0.005028203f, 0.005559912f, 0.00609162f, 0.006623329f, 0.007155037f, 0.007621651f, 0.008088264f, 0.008554878f, 0.009021492f, 0.00954764f, 0.010073787f, 0.010599935f, 0.011126082f, 0.011712911f, 0.01229974f, 0.012886568f, 0.013473397f, 0.014121973f, 0.014770549f, 0.015419125f, 0.0160677f, 0.017288558f, 0.018509416f, 0.019730274f, 0.020951131f, 0.021769084f, 0.022587037f, 0.02340499f, 0.024222942f, 0.026055055f, 0.027887167f, 0.029719278f, 0.03155139f, 0.032920673f, 0.034289952f, 0.03565923f, 0.037028514f, 0.038909543f, 0.040790573f, 0.0426716f, 0.044552628f, 0.046624877f, 0.048697125f, 0.050769377f, 0.052841626f, 0.05463912f, 0.056436617f, 0.05823411f, 0.060031608f, 0.063480645f, 0.06692969f, 0.07037873f, 0.073827766f, 0.077096134f, 0.08036451f, 0.08363288f, 0.086901255f, 0.091084465f, 0.095267676f, 0.099450886f, 0.1036341f, 0.107541576f, 0.111449055f, 0.115356535f, 0.119264014f, 0.124943085f, 0.13062215f, 0.13630122f, 0.14198029f, 0.14659609f, 0.1512119f, 0.1558277f, 0.16044351f, 0.16794835f, 0.1754532f, 0.18295804f, 0.19046287f, 0.19772708f, 0.2049913f, 0.21225551f, 0.21951972f, 0.22633989f, 0.23316006f, 0.23998025f, 0.24680042f, 0.25840434f, 0.2700083f, 0.28161222f, 0.29321614f, 0.30352655f, 0.313837f, 0.3241474f, 0.3344578f, 0.346787f, 0.35911623f, 0.37144545f, 0.38377464f, 0.39703417f, 0.4102937f, 0.42355323f, 0.43681276f, 0.4539677f, 0.47112262f, 0.48827755f, 0.5054325f, 0.5207768f, 0.53612113f, 0.5514654f, 0.5668097f, 0.5814435f, 0.5960772f, 0.610711f, 0.6253447f, 0.63901365f, 0.65268254f, 0.66635144f, 0.68002033f, 0.6870897f, 0.6941591f, 0.70122844f, 0.7082978f, 0.71552455f, 0.7227513f, 0.729978f, 0.7372048f, 0.7427285f, 0.74825215f, 0.75377584f, 0.7592996f, 0.7649123f, 0.7705251f, 0.7761379f, 0.7817507f, 0.78745276f, 0.7931549f, 0.79885703f, 0.8045591f, 0.8103508f, 0.81614244f, 0.8219341f, 0.82772577f, 0.83360726f, 0.83948874f, 0.8453702f, 0.85125166f, 0.85722315f, 0.8631946f, 0.8691661f, 0.87513757f, 0.8811993f, 0.88726103f, 0.89332277f, 0.8993845f, 0.90347594f, 0.9075673f, 0.9116587f, 0.91575015f, 0.9178109f, 0.91987175f, 0.9219325f, 0.92399335f, 0.9260642f, 0.9281351f, 0.930206f, 0.93227684f, 0.9364489f, 0.9406209f, 0.9447929f, 0.94896495f, 0.9510661f, 0.95316726f, 0.95526844f, 0.95736957f, 0.9616022f, 0.9658349f, 0.97006756f, 0.9743002f, 0.98072517f, 0.9871501f, 0.99357504f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private static String TAG = "GlesUtil";

    public static int createProgram(String str, String str2) {
        int loadShader = loadShader(35633, str);
        int loadShader2 = loadShader(35632, str2);
        int glCreateProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(glCreateProgram, loadShader);
        GLES30.glAttachShader(glCreateProgram, loadShader2);
        GLES30.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES30.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] != 1) {
            Logg.LogE("createProgam: link error");
            Logg.LogE("createProgam: " + GLES30.glGetProgramInfoLog(glCreateProgram));
            GLES30.glDeleteProgram(glCreateProgram);
            return 0;
        }
        GLES30.glDeleteShader(loadShader);
        GLES30.glDeleteShader(loadShader2);
        return glCreateProgram;
    }

    public static int loadShader(int i, String str) {
        int glCreateShader = GLES30.glCreateShader(i);
        GLES30.glShaderSource(glCreateShader, str);
        GLES30.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES30.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] == 0) {
            Logg.LogE("loadShader: compiler error");
            Logg.LogE("loadShader: " + GLES30.glGetShaderInfoLog(glCreateShader));
            GLES30.glDeleteShader(glCreateShader);
            return 0;
        }
        return glCreateShader;
    }

    public static void checkFrameBufferError() {
        int glCheckFramebufferStatus = GLES30.glCheckFramebufferStatus(36160);
        if (glCheckFramebufferStatus == 36053) {
            return;
        }
        Logg.LogE("checkFrameBuffer error: " + glCheckFramebufferStatus);
        throw new RuntimeException("status:" + glCheckFramebufferStatus + ", hex:" + Integer.toHexString(glCheckFramebufferStatus));
    }

    public static void checkError() {
        if (GLES30.glGetError() != 0) {
            Logg.LogI("checkError: " + GLES30.glGetError());
        }
    }

    public static int createPixelsBuffer(int i, int i2) {
        int[] iArr = new int[1];
        GLES30.glGenBuffers(1, iArr, 0);
        checkError();
        return iArr[0];
    }

    public static void createPixelsBuffers(int[] iArr, int i, int i2) {
        GLES30.glGenBuffers(iArr.length, iArr, 0);
        for (int i3 : iArr) {
            GLES30.glBindBuffer(35051, i3);
            GLES30.glBufferData(35051, i * i2 * 4, null, 35049);
        }
        GLES30.glBindBuffer(35051, 0);
    }

    public static int createFrameBuffer() {
        int[] iArr = new int[1];
        GLES30.glGenFramebuffers(1, iArr, 0);
        checkError();
        return iArr[0];
    }

    public static int createRenderBuffer() {
        int[] iArr = new int[1];
        GLES30.glGenRenderbuffers(1, iArr, 0);
        checkError();
        return iArr[0];
    }

    public static int createCameraTexture(boolean z) {
        Logg.LogI("createCameraTexture");
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        GLES30.glBindTexture(36197, iArr[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.MODEL ");
        String str = Build.MODEL;
        sb.append(str);
        Logg.LogI(sb.toString());
        Logg.LogI("createCameraTexture: glGenTextures is " + iArr[0]);
        if (z) {
            GLES30.glTexParameterf(36197, 10241, 9728.0f);
        } else if (str.toLowerCase().equals("redmi note 5a")) {
            Logg.LogI("in " + str + " we use gl_linear");
            GLES30.glTexParameterf(36197, 10241, 9729.0f);
        } else {
            GLES30.glTexParameterf(36197, 10241, 9987.0f);
        }
        GLES30.glTexParameterf(36197, 10240, 9729.0f);
        GLES30.glTexParameteri(36197, 10242, 33071);
        GLES30.glTexParameteri(36197, 10243, 33071);
        GLES30.glBindTexture(36197, 0);
        checkError();
        return iArr[0];
    }

    public static int createLutTexture() {
        Logg.LogI("createLutTexture");
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        if (iArr[0] == 0) {
            Logg.LogE("createFrameTexture: glGenTextures is 0");
            return -1;
        }
        Logg.LogI("android.os.Build.MODEL " + Build.MODEL);
        Logg.LogI("createLutTexture: glGenTextures is " + iArr[0]);
        GLES30.glBindTexture(3553, iArr[0]);
        GLES30.glTexImage2D(3553, 0, 6406, 256, 1, 0, 6406, 5126, FloatBuffer.wrap(PQLut));
        checkError();
        GLES30.glTexParameteri(3553, 10242, 33071);
        GLES30.glTexParameteri(3553, 10243, 33071);
        GLES30.glTexParameteri(3553, 10241, 9729);
        GLES30.glTexParameteri(3553, 10240, 9729);
        GLES30.glBindTexture(3553, 0);
        checkError();
        return iArr[0];
    }

    public static int createFrameTexture(int i, int i2, boolean z) {
        if (i <= 0 || i2 <= 0) {
            Logg.LogE("createOutputTexture: width or height is 0");
            return -1;
        }
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        if (iArr[0] == 0) {
            Logg.LogE("createFrameTexture: glGenTextures is 0");
            return -1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.MODEL ");
        String str = Build.MODEL;
        sb.append(str);
        Logg.LogI(sb.toString());
        GLES30.glBindTexture(3553, iArr[0]);
        GLES30.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
        GLES30.glTexParameteri(3553, 10242, 33071);
        GLES30.glTexParameteri(3553, 10243, 33071);
        if (z) {
            GLES30.glTexParameteri(3553, 10241, 9728);
        } else if (str.toLowerCase().equals("redmi note 5a")) {
            Logg.LogI("in " + str + " we use gl_linear");
            GLES30.glTexParameteri(3553, 10241, 9729);
        } else {
            GLES30.glTexParameteri(3553, 10241, 9987);
        }
        GLES30.glTexParameteri(3553, 10240, 9729);
        checkError();
        return iArr[0];
    }

    public static int loadBitmapTexture(Bitmap bitmap) {
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        if (iArr[0] == 0) {
            Logg.LogE("loadBitmapTexture: glGenTextures is 0");
            return -1;
        }
        GLES30.glBindTexture(3553, iArr[0]);
        String str = Build.MODEL;
        if (str.toLowerCase().equals("redmi note 5a")) {
            Logg.LogI("in " + str + " we use gl_linear");
            GLES30.glTexParameterf(3553, 10241, 9729.0f);
        } else {
            GLES30.glTexParameterf(3553, 10241, 9987.0f);
        }
        GLES30.glTexParameterf(3553, 10240, 9729.0f);
        GLES30.glTexParameterf(3553, 10242, 33071.0f);
        GLES30.glTexParameterf(3553, 10243, 33071.0f);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES30.glGenerateMipmap(3553);
        GLES30.glBindTexture(3553, 0);
        return iArr[0];
    }

    public static int loadBitmapTexture(Context context, int i) {
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
        if (decodeResource == null) {
            Logg.LogE("loadBitmapTexture:bitmap is null");
            return -1;
        }
        int loadBitmapTexture = loadBitmapTexture(decodeResource);
        decodeResource.recycle();
        return loadBitmapTexture;
    }

    public static void bindFrameTexture(int i, int i2) {
        GLES30.glBindFramebuffer(36160, i);
        GLES30.glBindTexture(3553, i2);
        GLES30.glFramebufferTexture2D(36160, 36064, 3553, i2, 0);
        GLES30.glBindTexture(3553, 0);
        GLES30.glBindFramebuffer(36160, 0);
        checkError();
    }

    public static void bindFrameRender(int i, int i2, int i3, int i4) {
        GLES30.glBindFramebuffer(36160, i);
        GLES30.glBindRenderbuffer(36161, i2);
        GLES30.glRenderbufferStorage(36161, 33189, i3, i4);
        GLES30.glFramebufferRenderbuffer(36160, 36096, 36161, i2);
        GLES30.glBindRenderbuffer(36161, 0);
        GLES30.glBindFramebuffer(36160, 0);
    }

    public static void bindFrameBuffer(int i, int i2) {
        GLES30.glBindFramebuffer(36160, i);
        GLES30.glFramebufferTexture2D(36160, 36064, 3553, i2, 0);
    }

    public static void unBindFrameBuffer() {
        GLES30.glBindFramebuffer(36160, 0);
    }

    public static void deleteFrameBuffer(int i, int i2) {
        GLES30.glDeleteFramebuffers(1, new int[]{i}, 0);
        GLES30.glDeleteTextures(1, new int[]{i2}, 0);
    }
}
