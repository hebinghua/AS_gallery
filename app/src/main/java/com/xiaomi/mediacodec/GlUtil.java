package com.xiaomi.mediacodec;

import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.os.Build;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes3.dex */
public class GlUtil {
    public static float[] location = null;
    public static String locationString = null;
    public static int mHeight = 4320;
    public static int mPictureRotation = 0;
    public static int mWidht = 7680;

    public static int createProgram(String str, String str2) {
        int glCreateProgram = GLES30.glCreateProgram();
        checkGlError("glCreateProgram fail");
        int loadShader = loadShader(35633, str);
        int loadShader2 = loadShader(35632, str2);
        GLES30.glAttachShader(glCreateProgram, loadShader);
        checkGlError("glAttachVertexShader fail");
        GLES30.glAttachShader(glCreateProgram, loadShader2);
        checkGlError("glAttachFragmentShader fail");
        GLES30.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES30.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] != 1) {
            GLES30.glDeleteProgram(glCreateProgram);
            throw new RuntimeException("Could not link program");
        }
        GLES30.glDeleteShader(loadShader);
        GLES30.glDeleteShader(loadShader2);
        return glCreateProgram;
    }

    public static int loadShader(int i, String str) {
        int glCreateShader = GLES30.glCreateShader(i);
        checkGlError("glCreateShader fail, type = " + i);
        GLES30.glShaderSource(glCreateShader, str);
        GLES30.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES30.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        GLES30.glDeleteShader(glCreateShader);
        throw new RuntimeException("glCompileShader fail");
    }

    public static int genTextureId(int i) {
        int[] iArr = new int[1];
        GLES30.glGenTextures(1, iArr, 0);
        checkGlError("glGenTextures");
        int i2 = iArr[0];
        GLES30.glBindTexture(i, i2);
        checkGlError("glBindTexture");
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.MODEL ");
        String str = Build.MODEL;
        sb.append(str);
        Logg.LogI(sb.toString());
        if (str.toLowerCase().equals("redmi note 5a")) {
            Logg.LogI("in " + str + " we use gl_linear");
            GLES30.glTexParameterf(i, 10241, 9729.0f);
        } else {
            GLES30.glTexParameterf(i, 10241, 9987.0f);
        }
        GLES30.glTexParameterf(i, 10240, 9729.0f);
        GLES30.glTexParameteri(i, 10242, 33071);
        GLES30.glTexParameteri(i, 10243, 33071);
        checkGlError("glTexParameter");
        GLES30.glBindTexture(i, 0);
        return i2;
    }

    public static void checkGlError(String str) {
        int glGetError = GLES30.glGetError();
        if (glGetError == 0) {
            return;
        }
        throw new RuntimeException(str + " :0x" + glGetError);
    }

    public static void checkLocation(int i, String str) {
        if (i >= 0) {
            return;
        }
        throw new RuntimeException("Unable to locate '" + str + "' in program");
    }

    public static File saveFile(Bitmap bitmap, String str, String str2) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(str, str2);
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return file2;
    }
}
