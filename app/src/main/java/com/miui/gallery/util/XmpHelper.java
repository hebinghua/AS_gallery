package com.miui.gallery.util;

import android.text.TextUtils;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class XmpHelper {
    public static final String TAG = "XmpHelper";

    /* loaded from: classes2.dex */
    public static class Section {
        public byte[] data;
        public int length;
        public int marker;

        public Section() {
        }
    }

    static {
        try {
            XMPMetaFactory.getSchemaRegistry().registerNamespace("http://ns.google.com/photos/1.0/camera/", "GCamera");
            XMPMetaFactory.getSchemaRegistry().registerNamespace("http://ns.xiaomi.com/photos/1.0/camera/", "MiCamera");
        } catch (XMPException e) {
            String str = TAG;
            DefaultLogger.d(str, "Failed to register ns http://ns.google.com/photos/1.0/camera/: " + e);
        }
    }

    public static long readMicroVideoOffset(String str) {
        File file;
        FileInputStream fileInputStream;
        XMPMeta extractXMPMeta;
        if (TextUtils.isEmpty(str)) {
            return 0L;
        }
        long currentTimeMillis = System.currentTimeMillis();
        FileInputStream fileInputStream2 = null;
        try {
            try {
                file = new File(str);
                fileInputStream = new FileInputStream(file);
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            extractXMPMeta = extractXMPMeta(fileInputStream);
        } catch (Exception e2) {
            e = e2;
            fileInputStream2 = fileInputStream;
            e.printStackTrace();
            BaseMiscUtil.closeSilently(fileInputStream2);
            return 0L;
        } catch (Throwable th2) {
            th = th2;
            fileInputStream2 = fileInputStream;
            BaseMiscUtil.closeSilently(fileInputStream2);
            throw th;
        }
        if (extractXMPMeta != null) {
            Integer propertyInteger = extractXMPMeta.getPropertyInteger("http://ns.google.com/photos/1.0/camera/", "MicroVideo");
            if (propertyInteger != null && propertyInteger.intValue() == 1) {
                Integer propertyInteger2 = extractXMPMeta.getPropertyInteger("http://ns.google.com/photos/1.0/camera/", "MicroVideoOffset");
                if (propertyInteger2 == null) {
                    BaseMiscUtil.closeSilently(fileInputStream);
                    return 0L;
                }
                DefaultLogger.d(TAG, "readMicroVideoOffset cost %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                long length = file.length() - propertyInteger2.intValue();
                BaseMiscUtil.closeSilently(fileInputStream);
                return length;
            }
            BaseMiscUtil.closeSilently(fileInputStream);
            return 0L;
        }
        BaseMiscUtil.closeSilently(fileInputStream);
        return 0L;
    }

    public static XMPMeta extractXMPMeta(InputStream inputStream) {
        List<Section> parse = parse(inputStream, true);
        if (parse != null) {
            Iterator<Section> it = parse.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Section next = it.next();
                if (hasXMPHeader(next.data)) {
                    int xMPContentEnd = getXMPContentEnd(next.data) - 29;
                    byte[] bArr = new byte[xMPContentEnd];
                    System.arraycopy(next.data, 29, bArr, 0, xMPContentEnd);
                    try {
                        return XMPMetaFactory.parseFromBuffer(bArr);
                    } catch (XMPException e) {
                        String str = TAG;
                        DefaultLogger.d(str, "XMP parse error: " + e);
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    public static boolean hasXMPHeader(byte[] bArr) {
        byte[] bArr2;
        if (bArr.length < 29) {
            return false;
        }
        try {
            bArr2 = new byte[29];
            System.arraycopy(bArr, 0, bArr2, 0, 29);
        } catch (UnsupportedEncodingException unused) {
        }
        return new String(bArr2, Keyczar.DEFAULT_ENCODING).equals("http://ns.adobe.com/xap/1.0/\u0000");
    }

    public static int getXMPContentEnd(byte[] bArr) {
        for (int length = bArr.length - 1; length >= 1; length--) {
            if (bArr[length] == 62 && bArr[length - 1] != 63) {
                return length + 1;
            }
        }
        return bArr.length;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0037, code lost:
        if (r9 != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0039, code lost:
        r9 = new com.miui.gallery.util.XmpHelper.Section(null);
        r9.marker = r3;
        r9.length = -1;
        r2 = new byte[r8.available()];
        r9.data = r2;
        r8.read(r2, 0, r2.length);
        r1.add(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0051, code lost:
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0089, code lost:
        r8.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.miui.gallery.util.XmpHelper.Section> parse(java.io.InputStream r8, boolean r9) {
        /*
            r0 = 0
            int r1 = r8.read()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r2 = 255(0xff, float:3.57E-43)
            if (r1 != r2) goto L91
            int r1 = r8.read()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r3 = 216(0xd8, float:3.03E-43)
            if (r1 == r3) goto L13
            goto L91
        L13:
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r1.<init>()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
        L18:
            int r3 = r8.read()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r4 = -1
            if (r3 == r4) goto L8d
            if (r3 == r2) goto L25
            r8.close()     // Catch: java.io.IOException -> L24
        L24:
            return r0
        L25:
            int r3 = r8.read()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            if (r3 != r2) goto L2c
            goto L25
        L2c:
            if (r3 != r4) goto L32
            r8.close()     // Catch: java.io.IOException -> L31
        L31:
            return r0
        L32:
            r5 = 218(0xda, float:3.05E-43)
            r6 = 0
            if (r3 != r5) goto L55
            if (r9 != 0) goto L51
            com.miui.gallery.util.XmpHelper$Section r9 = new com.miui.gallery.util.XmpHelper$Section     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r9.<init>()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r9.marker = r3     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r9.length = r4     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            int r2 = r8.available()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            byte[] r2 = new byte[r2]     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r9.data = r2     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            int r3 = r2.length     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r8.read(r2, r6, r3)     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r1.add(r9)     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
        L51:
            r8.close()     // Catch: java.io.IOException -> L54
        L54:
            return r1
        L55:
            int r5 = r8.read()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            int r7 = r8.read()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            if (r5 == r4) goto L89
            if (r7 != r4) goto L62
            goto L89
        L62:
            int r4 = r5 << 8
            r4 = r4 | r7
            if (r9 == 0) goto L73
            r5 = 225(0xe1, float:3.15E-43)
            if (r3 != r5) goto L6c
            goto L73
        L6c:
            int r4 = r4 + (-2)
            long r3 = (long) r4     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r8.skip(r3)     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            goto L18
        L73:
            com.miui.gallery.util.XmpHelper$Section r5 = new com.miui.gallery.util.XmpHelper$Section     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r5.<init>()     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r5.marker = r3     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r5.length = r4     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            int r4 = r4 + (-2)
            byte[] r3 = new byte[r4]     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r5.data = r3     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r8.read(r3, r6, r4)     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            r1.add(r5)     // Catch: java.lang.Throwable -> L95 java.io.IOException -> L97
            goto L18
        L89:
            r8.close()     // Catch: java.io.IOException -> L8c
        L8c:
            return r0
        L8d:
            r8.close()     // Catch: java.io.IOException -> L90
        L90:
            return r1
        L91:
            r8.close()     // Catch: java.io.IOException -> L94
        L94:
            return r0
        L95:
            r9 = move-exception
            goto Lb4
        L97:
            r9 = move-exception
            java.lang.String r1 = com.miui.gallery.util.XmpHelper.TAG     // Catch: java.lang.Throwable -> L95
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L95
            r2.<init>()     // Catch: java.lang.Throwable -> L95
            java.lang.String r3 = "Could not parse file: "
            r2.append(r3)     // Catch: java.lang.Throwable -> L95
            r2.append(r9)     // Catch: java.lang.Throwable -> L95
            java.lang.String r9 = r2.toString()     // Catch: java.lang.Throwable -> L95
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r9)     // Catch: java.lang.Throwable -> L95
            if (r8 == 0) goto Lb3
            r8.close()     // Catch: java.io.IOException -> Lb3
        Lb3:
            return r0
        Lb4:
            if (r8 == 0) goto Lb9
            r8.close()     // Catch: java.io.IOException -> Lb9
        Lb9:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.XmpHelper.parse(java.io.InputStream, boolean):java.util.List");
    }
}
