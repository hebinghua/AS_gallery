package com.miui.filtersdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

/* loaded from: classes.dex */
public class OpenGlUtils {
    public static int loadTexture(Bitmap bitmap, int i) {
        return loadTexture(bitmap, i, false);
    }

    public static int loadTexture(Bitmap bitmap, int i, boolean z) {
        if (bitmap == null) {
            return -1;
        }
        int[] iArr = new int[1];
        if (i == -1) {
            GLES20.glGenTextures(1, iArr, 0);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
        } else {
            GLES20.glBindTexture(3553, i);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            iArr[0] = i;
        }
        if (z) {
            bitmap.recycle();
        }
        return iArr[0];
    }

    public static int loadTexture(Context context, String str) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        if (iArr[0] != 0) {
            Bitmap imageFromAssetsFile = getImageFromAssetsFile(context, str);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLUtils.texImage2D(3553, 0, imageFromAssetsFile, 0);
            imageFromAssetsFile.recycle();
        }
        if (iArr[0] == 0) {
            Log.e("OpenGlUtils", String.format("loadTexture failed,path:%s", str));
            return -1;
        }
        return iArr[0];
    }

    public static void loadYuvToTextures(Buffer buffer, Buffer buffer2, int i, int i2, int[] iArr) {
        int i3;
        float f;
        float f2;
        if (buffer == null || buffer2 == null || iArr == null || iArr.length < 2) {
            return;
        }
        if (iArr[0] == -1) {
            GLES20.glGenTextures(1, iArr, 0);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, iArr[0]);
            i3 = 3553;
            f = 9729.0f;
            f2 = 33071.0f;
            GLES20.glTexImage2D(3553, 0, 6409, i, i2, 0, 6409, 5121, buffer);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
        } else {
            i3 = 3553;
            f = 9729.0f;
            f2 = 33071.0f;
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexImage2D(3553, 0, 6409, i, i2, 0, 6409, 5121, buffer);
        }
        if (iArr[1] == -1) {
            GLES20.glGenTextures(1, iArr, 1);
            GLES20.glActiveTexture(33985);
            GLES20.glBindTexture(i3, iArr[1]);
            GLES20.glTexImage2D(3553, 0, 6410, i / 2, i2 / 2, 0, 6410, 5121, buffer2);
            GLES20.glTexParameterf(i3, 10240, f);
            GLES20.glTexParameterf(i3, 10241, f);
            GLES20.glTexParameterf(i3, 10242, f2);
            GLES20.glTexParameterf(i3, 10243, f2);
            return;
        }
        GLES20.glActiveTexture(33985);
        GLES20.glBindTexture(i3, iArr[1]);
        GLES20.glTexImage2D(3553, 0, 6410, i / 2, i2 / 2, 0, 6410, 5121, buffer2);
    }

    public static Bitmap getImageFromAssetsFile(Context context, String str) {
        Bitmap bitmap;
        InputStream open;
        InputStream inputStream = null;
        Bitmap bitmap2 = null;
        inputStream = null;
        try {
            try {
                open = context.getResources().getAssets().open(str);
            } catch (IOException e) {
                e = e;
                bitmap = null;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            bitmap2 = BitmapFactory.decodeStream(open);
            open.close();
            try {
                open.close();
                return bitmap2;
            } catch (IOException e2) {
                e2.printStackTrace();
                return bitmap2;
            }
        } catch (IOException e3) {
            e = e3;
            Bitmap bitmap3 = bitmap2;
            inputStream = open;
            bitmap = bitmap3;
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return bitmap;
        } catch (Throwable th2) {
            th = th2;
            inputStream = open;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static int loadProgram(String str, String str2) {
        int[] iArr = new int[1];
        int loadShader = loadShader(str, 35633);
        if (loadShader == 0) {
            Log.d("Load Program", "Vertex Shader Failed");
            return 0;
        }
        int loadShader2 = loadShader(str2, 35632);
        if (loadShader2 == 0) {
            Log.d("Load Program", "Fragment Shader Failed");
            return 0;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(glCreateProgram, loadShader);
        GLES20.glAttachShader(glCreateProgram, loadShader2);
        GLES20.glLinkProgram(glCreateProgram);
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] <= 0) {
            Log.d("Load Program", "Linking Failed");
            return 0;
        }
        GLES20.glDeleteShader(loadShader);
        GLES20.glDeleteShader(loadShader2);
        return glCreateProgram;
    }

    public static int loadShader(String str, int i) {
        int[] iArr = new int[1];
        int glCreateShader = GLES20.glCreateShader(i);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] == 0) {
            Log.e("Load Shader Failed", "Compilation\n" + GLES20.glGetShaderInfoLog(glCreateShader));
            return 0;
        }
        return glCreateShader;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0026 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Bitmap getImageFromPath(java.lang.String r2) {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L15 java.io.IOException -> L17
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L15 java.io.IOException -> L17
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeStream(r1)     // Catch: java.io.IOException -> L13 java.lang.Throwable -> L22
            r1.close()     // Catch: java.io.IOException -> Le
            goto L21
        Le:
            r2 = move-exception
            r2.printStackTrace()
            goto L21
        L13:
            r2 = move-exception
            goto L19
        L15:
            r2 = move-exception
            goto L24
        L17:
            r2 = move-exception
            r1 = r0
        L19:
            r2.printStackTrace()     // Catch: java.lang.Throwable -> L22
            if (r1 == 0) goto L21
            r1.close()     // Catch: java.io.IOException -> Le
        L21:
            return r0
        L22:
            r2 = move-exception
            r0 = r1
        L24:
            if (r0 == 0) goto L2e
            r0.close()     // Catch: java.io.IOException -> L2a
            goto L2e
        L2a:
            r0 = move-exception
            r0.printStackTrace()
        L2e:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.filtersdk.utils.OpenGlUtils.getImageFromPath(java.lang.String):android.graphics.Bitmap");
    }
}
