package com.baidu.vis.ClassIfication;

import android.graphics.Bitmap;
import com.baidu.vis.ClassIfication.SDKExceptions;
import java.io.File;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class Util {
    private static final String TAG = "Predictor";

    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteBuffer allocate = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(allocate);
        return allocate.array();
    }

    public static Bitmap bytes2Bitmap(byte[] bArr, int i, int i2, Bitmap.Config config) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, config);
        createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
        return createBitmap;
    }

    public static void checkFile(String str) throws SDKExceptions.NV21BytesLengthNotMatch {
        if (new File(str).exists()) {
            return;
        }
        throw new SDKExceptions.NV21BytesLengthNotMatch();
    }

    /* JADX WARN: Removed duplicated region for block: B:75:0x00ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x00a7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void copyAssets(android.content.Context r5, java.lang.String r6) throws com.baidu.vis.ClassIfication.SDKExceptions.NoSDCardPermission, com.baidu.vis.ClassIfication.SDKExceptions.MissingModleFileInAssetFolder, com.baidu.vis.ClassIfication.SDKExceptions.IlleagleCpuArch {
        /*
            java.lang.String r0 = android.os.Build.CPU_ABI
            java.lang.String r1 = "v7a"
            boolean r1 = r0.contains(r1)
            if (r1 != 0) goto L19
            java.lang.String r1 = "v8a"
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L13
            goto L19
        L13:
            com.baidu.vis.ClassIfication.SDKExceptions$IlleagleCpuArch r5 = new com.baidu.vis.ClassIfication.SDKExceptions$IlleagleCpuArch
            r5.<init>()
            throw r5
        L19:
            android.content.res.AssetManager r0 = r5.getAssets()
            java.io.InputStream r1 = r0.open(r6)     // Catch: java.lang.Throwable -> Lb6 java.io.IOException -> Lb8
            if (r1 == 0) goto L2b
            r1.close()     // Catch: java.io.IOException -> L27
            goto L2b
        L27:
            r1 = move-exception
            r1.printStackTrace()
        L2b:
            java.lang.String r1 = "android.permission.WRITE_EXTERNAL_STORAGE"
            int r1 = r5.checkCallingOrSelfPermission(r1)
            if (r1 != 0) goto Lb0
            java.io.File r1 = new java.io.File
            r2 = 0
            java.io.File r3 = r5.getExternalFilesDir(r2)
            r1.<init>(r3, r6)
            boolean r1 = r1.exists()
            if (r1 == 0) goto L4b
            java.lang.String r5 = "Predictor"
            java.lang.String r6 = "NN model on SD card"
            android.util.Log.d(r5, r6)
            return
        L4b:
            java.io.InputStream r0 = r0.open(r6)     // Catch: java.lang.Throwable -> L80 java.io.IOException -> L83
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L78 java.io.IOException -> L7c
            java.io.File r5 = r5.getExternalFilesDir(r2)     // Catch: java.lang.Throwable -> L78 java.io.IOException -> L7c
            r1.<init>(r5, r6)     // Catch: java.lang.Throwable -> L78 java.io.IOException -> L7c
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L78 java.io.IOException -> L7c
            r5.<init>(r1)     // Catch: java.lang.Throwable -> L78 java.io.IOException -> L7c
            r1 = 1024(0x400, float:1.435E-42)
            byte[] r1 = new byte[r1]     // Catch: java.lang.Throwable -> L74 java.io.IOException -> L76
        L61:
            int r2 = r0.read(r1)     // Catch: java.lang.Throwable -> L74 java.io.IOException -> L76
            r3 = -1
            if (r2 == r3) goto L6d
            r3 = 0
            r5.write(r1, r3, r2)     // Catch: java.lang.Throwable -> L74 java.io.IOException -> L76
            goto L61
        L6d:
            r0.close()     // Catch: java.io.IOException -> L70
        L70:
            r5.close()     // Catch: java.io.IOException -> La3
            goto La3
        L74:
            r6 = move-exception
            goto L7a
        L76:
            r1 = move-exception
            goto L7e
        L78:
            r6 = move-exception
            r5 = r2
        L7a:
            r2 = r0
            goto La5
        L7c:
            r1 = move-exception
            r5 = r2
        L7e:
            r2 = r0
            goto L85
        L80:
            r6 = move-exception
            r5 = r2
            goto La5
        L83:
            r1 = move-exception
            r5 = r2
        L85:
            java.lang.String r0 = "tag"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La4
            r3.<init>()     // Catch: java.lang.Throwable -> La4
            java.lang.String r4 = "Failed to copy asset file: "
            r3.append(r4)     // Catch: java.lang.Throwable -> La4
            r3.append(r6)     // Catch: java.lang.Throwable -> La4
            java.lang.String r6 = r3.toString()     // Catch: java.lang.Throwable -> La4
            android.util.Log.e(r0, r6, r1)     // Catch: java.lang.Throwable -> La4
            if (r2 == 0) goto La0
            r2.close()     // Catch: java.io.IOException -> La0
        La0:
            if (r5 == 0) goto La3
            goto L70
        La3:
            return
        La4:
            r6 = move-exception
        La5:
            if (r2 == 0) goto Laa
            r2.close()     // Catch: java.io.IOException -> Laa
        Laa:
            if (r5 == 0) goto Laf
            r5.close()     // Catch: java.io.IOException -> Laf
        Laf:
            throw r6
        Lb0:
            com.baidu.vis.ClassIfication.SDKExceptions$NoSDCardPermission r5 = new com.baidu.vis.ClassIfication.SDKExceptions$NoSDCardPermission
            r5.<init>()
            throw r5
        Lb6:
            r5 = move-exception
            goto Lbe
        Lb8:
            com.baidu.vis.ClassIfication.SDKExceptions$MissingModleFileInAssetFolder r5 = new com.baidu.vis.ClassIfication.SDKExceptions$MissingModleFileInAssetFolder     // Catch: java.lang.Throwable -> Lb6
            r5.<init>()     // Catch: java.lang.Throwable -> Lb6
            throw r5     // Catch: java.lang.Throwable -> Lb6
        Lbe:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.vis.ClassIfication.Util.copyAssets(android.content.Context, java.lang.String):void");
    }
}
