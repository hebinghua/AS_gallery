package com.miui.gallery.util;

/* loaded from: classes2.dex */
public class UnzipUtils {
    public static final String TAG = "UnzipUtils";

    /* JADX WARN: Removed duplicated region for block: B:63:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00c3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean unZipFile(java.lang.String r7) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r7)
            boolean r7 = r0.exists()
            r1 = 0
            if (r7 != 0) goto Ld
            return r1
        Ld:
            java.io.File r7 = new java.io.File
            java.lang.String r2 = r0.getParent()
            r7.<init>(r2)
            r2 = 0
            java.util.zip.ZipInputStream r3 = new java.util.zip.ZipInputStream     // Catch: java.lang.Throwable -> L93 java.io.IOException -> L95
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L93 java.io.IOException -> L95
            r4.<init>(r0)     // Catch: java.lang.Throwable -> L93 java.io.IOException -> L95
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L93 java.io.IOException -> L95
            java.util.zip.ZipEntry r2 = r3.getNextEntry()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            r4 = 1048576(0x100000, float:1.469368E-39)
            byte[] r4 = new byte[r4]     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
        L29:
            if (r2 == 0) goto L7e
            boolean r5 = r2.isDirectory()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            if (r5 == 0) goto L44
            java.io.File r5 = new java.io.File     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            java.lang.String r2 = r2.getName()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            r5.<init>(r7, r2)     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            boolean r2 = r5.exists()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            if (r2 != 0) goto L79
            r5.mkdir()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            goto L79
        L44:
            java.io.File r5 = new java.io.File     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            java.lang.String r2 = r2.getName()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            r5.<init>(r7, r2)     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            java.io.File r2 = r5.getParentFile()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            boolean r2 = r2.exists()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            if (r2 != 0) goto L5e
            java.io.File r2 = r5.getParentFile()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            r2.mkdirs()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
        L5e:
            boolean r2 = r5.exists()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            if (r2 != 0) goto L79
            r5.createNewFile()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            r2.<init>(r5)     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
        L6c:
            int r5 = r3.read(r4)     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            if (r5 <= 0) goto L76
            r2.write(r4, r1, r5)     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            goto L6c
        L76:
            r2.close()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
        L79:
            java.util.zip.ZipEntry r2 = r3.getNextEntry()     // Catch: java.io.IOException -> L91 java.lang.Throwable -> Lbf
            goto L29
        L7e:
            r3.close()     // Catch: java.io.IOException -> L82
            goto L86
        L82:
            r7 = move-exception
            r7.printStackTrace()
        L86:
            boolean r7 = r0.exists()
            if (r7 == 0) goto L8f
            r0.delete()
        L8f:
            r7 = 1
            return r7
        L91:
            r2 = move-exception
            goto L99
        L93:
            r7 = move-exception
            goto Lc1
        L95:
            r3 = move-exception
            r6 = r3
            r3 = r2
            r2 = r6
        L99:
            boolean r4 = r7.exists()     // Catch: java.lang.Throwable -> Lbf
            if (r4 == 0) goto La2
            r7.delete()     // Catch: java.lang.Throwable -> Lbf
        La2:
            java.lang.String r7 = com.miui.gallery.util.UnzipUtils.TAG     // Catch: java.lang.Throwable -> Lbf
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> Lbf
            com.miui.gallery.util.logger.DefaultLogger.e(r7, r2)     // Catch: java.lang.Throwable -> Lbf
            if (r3 == 0) goto Lb5
            r3.close()     // Catch: java.io.IOException -> Lb1
            goto Lb5
        Lb1:
            r7 = move-exception
            r7.printStackTrace()
        Lb5:
            boolean r7 = r0.exists()
            if (r7 == 0) goto Lbe
            r0.delete()
        Lbe:
            return r1
        Lbf:
            r7 = move-exception
            r2 = r3
        Lc1:
            if (r2 == 0) goto Lcb
            r2.close()     // Catch: java.io.IOException -> Lc7
            goto Lcb
        Lc7:
            r1 = move-exception
            r1.printStackTrace()
        Lcb:
            boolean r1 = r0.exists()
            if (r1 == 0) goto Ld4
            r0.delete()
        Ld4:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.UnzipUtils.unZipFile(java.lang.String):boolean");
    }
}
