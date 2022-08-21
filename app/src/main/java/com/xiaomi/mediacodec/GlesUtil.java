package com.xiaomi.mediacodec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;

/* loaded from: classes3.dex */
public class GlesUtil {
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
            Log.e("checkError", "elg error: " + GLES30.glGetError());
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
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        GLES30.glBindTexture(36197, iArr[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.MODEL ");
        String str = Build.MODEL;
        sb.append(str);
        Logg.LogI(sb.toString());
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

    public static void DestoryProgram(int i) {
        GLES30.glDeleteProgram(i);
    }
}
