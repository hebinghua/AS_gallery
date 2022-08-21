package com.miui.imagecleanlib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface;
import androidx.heifwriter.HeifWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes3.dex */
public class ImageCleanUtils {
    public static void clearImageInfo(File file, boolean z, boolean z2) {
        if (z || z2) {
            if (!haveCameraInfo(file) && !haveLocationInfo(file)) {
                Log.d("zman_share_clean_lib", "not need Hide image info");
                return;
            }
            String fileExtension = getFileExtension(file.getAbsolutePath());
            if (!TextUtils.isEmpty(fileExtension) && TextUtils.equals(fileExtension.toLowerCase(), "heic") && Build.VERSION.SDK_INT >= 28) {
                try {
                    int rotationDegrees = new ExifInterface(file.getAbsolutePath()).getRotationDegrees();
                    Log.d("zman_share_clean_lib", "is HEIC ExifInterface : " + rotationDegrees);
                    Bitmap decodeFile = BitmapFactory.decodeFile(file.getAbsolutePath());
                    HeifWriter.Builder builder = new HeifWriter.Builder(file.getAbsolutePath(), decodeFile.getWidth(), decodeFile.getHeight(), 2);
                    builder.setRotation(rotationDegrees);
                    HeifWriter build = builder.setQuality(90).build();
                    build.start();
                    build.addBitmap(decodeFile);
                    build.stop(0L);
                    build.close();
                    return;
                } catch (Exception e) {
                    Log.e("zman_share_clean_lib", "HeifWriter Exception : ", e);
                }
            }
            try {
                ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                if (z) {
                    exifInterface.setAttribute("GPSAltitudeRef", "");
                    exifInterface.setAttribute("GPSAltitude", "0/1");
                    exifInterface.setAttribute("GPSLatitude", "0/1,0/1,0/1");
                    exifInterface.setAttribute("GPSLatitudeRef", "");
                    exifInterface.setAttribute("GPSLongitudeRef", "");
                    exifInterface.setAttribute("GPSLongitude", "0/1,0/1,0/1");
                    exifInterface.setAttribute("GPSTimeStamp", "00:00:00");
                    exifInterface.setAttribute("GPSProcessingMethod", "");
                    exifInterface.setAttribute("GPSDateStamp", "00:00:00");
                }
                if (z2) {
                    exifInterface.setAttribute("FNumber", "0");
                    exifInterface.setAttribute("ShutterSpeedValue", "1");
                    exifInterface.setAttribute("FocalLength", "0/0");
                    exifInterface.setAttribute("ApertureValue", "0");
                    exifInterface.setAttribute("ExposureTime", "1");
                    exifInterface.setAttribute("DateTime", "");
                    exifInterface.setAttribute("DateTimeOriginal", "");
                    exifInterface.setAttribute("DateTimeDigitized", "");
                    exifInterface.setAttribute("SubSecTime", "");
                    exifInterface.setAttribute("SubSecTimeOriginal", "");
                    exifInterface.setAttribute("SubSecTimeDigitized", "");
                    exifInterface.setAttribute("Model", "--");
                    exifInterface.setAttribute("XiaomiProduct", "--");
                    exifInterface.setAttribute("SmartFusion", "0");
                    exifInterface.setAttribute("Make", "--");
                    exifInterface.setAttribute("ISOSpeedRatings", "0");
                    exifInterface.setAttribute("WhiteBalance", "0");
                    exifInterface.setAttribute("Flash", "0");
                }
                exifInterface.saveAttributes();
            } catch (Exception unused) {
                Log.i("zman_share_clean_lib", "ExifInterface Exception : ");
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0099, code lost:
        if ("00:00:00".equals(r1) != false) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean haveLocationInfo(java.io.File r10) {
        /*
            r0 = 0
            androidx.exifinterface.media.ExifInterface r1 = new androidx.exifinterface.media.ExifInterface     // Catch: java.lang.Exception -> L9f
            java.lang.String r10 = r10.getAbsolutePath()     // Catch: java.lang.Exception -> L9f
            r1.<init>(r10)     // Catch: java.lang.Exception -> L9f
            java.lang.String r10 = "GPSAltitudeRef"
            java.lang.String r10 = r1.getAttribute(r10)     // Catch: java.lang.Exception -> L9f
            java.lang.String r2 = "GPSAltitude"
            java.lang.String r2 = r1.getAttribute(r2)     // Catch: java.lang.Exception -> L9f
            java.lang.String r3 = "GPSLatitude"
            java.lang.String r3 = r1.getAttribute(r3)     // Catch: java.lang.Exception -> L9f
            java.lang.String r4 = "GPSLatitudeRef"
            java.lang.String r4 = r1.getAttribute(r4)     // Catch: java.lang.Exception -> L9f
            java.lang.String r5 = "GPSLongitudeRef"
            java.lang.String r5 = r1.getAttribute(r5)     // Catch: java.lang.Exception -> L9f
            java.lang.String r6 = "GPSLongitude"
            java.lang.String r6 = r1.getAttribute(r6)     // Catch: java.lang.Exception -> L9f
            java.lang.String r7 = "GPSTimeStamp"
            java.lang.String r7 = r1.getAttribute(r7)     // Catch: java.lang.Exception -> L9f
            java.lang.String r8 = "GPSProcessingMethod"
            java.lang.String r8 = r1.getAttribute(r8)     // Catch: java.lang.Exception -> L9f
            java.lang.String r9 = "GPSDateStamp"
            java.lang.String r1 = r1.getAttribute(r9)     // Catch: java.lang.Exception -> L9f
            boolean r10 = android.text.TextUtils.isEmpty(r10)     // Catch: java.lang.Exception -> L9f
            r9 = 1
            if (r10 == 0) goto L9c
            boolean r10 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Exception -> L9f
            if (r10 != 0) goto L55
            java.lang.String r10 = "0/1"
            boolean r10 = r10.equals(r2)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
        L55:
            boolean r10 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Exception -> L9f
            java.lang.String r2 = "0/1,0/1,0/1"
            if (r10 != 0) goto L63
            boolean r10 = r2.equals(r3)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
        L63:
            boolean r10 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
            boolean r10 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
            boolean r10 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Exception -> L9f
            if (r10 != 0) goto L7b
            boolean r10 = r2.equals(r6)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
        L7b:
            boolean r10 = android.text.TextUtils.isEmpty(r7)     // Catch: java.lang.Exception -> L9f
            java.lang.String r2 = "00:00:00"
            if (r10 != 0) goto L89
            boolean r10 = r2.equals(r7)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
        L89:
            boolean r10 = android.text.TextUtils.isEmpty(r8)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
            boolean r10 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Exception -> L9f
            if (r10 != 0) goto L9b
            boolean r10 = r2.equals(r1)     // Catch: java.lang.Exception -> L9f
            if (r10 == 0) goto L9c
        L9b:
            r0 = r9
        L9c:
            r10 = r0 ^ 1
            return r10
        L9f:
            java.lang.String r10 = "zman_share_clean_lib"
            java.lang.String r1 = "haveLocationInfo Exception: "
            android.util.Log.d(r10, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.imagecleanlib.ImageCleanUtils.haveLocationInfo(java.io.File):boolean");
    }

    public static boolean haveCameraInfo(File file) {
        boolean z = false;
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            String attribute = exifInterface.getAttribute("ISO");
            String attribute2 = exifInterface.getAttribute("FocalLength");
            String attribute3 = exifInterface.getAttribute("Flash");
            String attribute4 = exifInterface.getAttribute("DateTime");
            String attribute5 = exifInterface.getAttribute("DateTimeOriginal");
            String attribute6 = exifInterface.getAttribute("DateTimeDigitized");
            String attribute7 = exifInterface.getAttribute("SubSecTime");
            String attribute8 = exifInterface.getAttribute("SubSecTimeOriginal");
            String attribute9 = exifInterface.getAttribute("SubSecTimeDigitized");
            String attribute10 = exifInterface.getAttribute("Model");
            String attribute11 = exifInterface.getAttribute("XiaomiProduct");
            String attribute12 = exifInterface.getAttribute("SmartFusion");
            String attribute13 = exifInterface.getAttribute("Make");
            if (TextUtils.isEmpty(attribute4) && TextUtils.isEmpty(attribute5) && TextUtils.isEmpty(attribute6) && TextUtils.isEmpty(attribute7) && TextUtils.isEmpty(attribute8) && TextUtils.isEmpty(attribute9) && ((TextUtils.isEmpty(attribute10) || "--".equals(attribute10)) && ((TextUtils.isEmpty(attribute11) || "--".equals(attribute11)) && ((TextUtils.isEmpty(attribute12) || "0".equals(attribute12)) && ((TextUtils.isEmpty(attribute13) || "--".equals(attribute13)) && TextUtils.isEmpty(attribute) && TextUtils.isEmpty(attribute2)))))) {
                if (TextUtils.isEmpty(attribute3)) {
                    z = true;
                }
            }
            return !z;
        } catch (Exception unused) {
            Log.d("zman_share_clean_lib", "haveCameraInfo Exception: ");
            return false;
        }
    }

    public static String getFileExtension(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        return (lastIndexOf == -1 || str.lastIndexOf(File.separator) >= lastIndexOf) ? "" : str.substring(lastIndexOf + 1);
    }

    public static void copyFile(File file, File file2) {
        if (file == null || !file.exists()) {
            Log.e("zman_share_clean_lib", "src: is null");
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    fileInputStream.close();
                    return;
                }
            }
        } catch (IOException e) {
            Log.e("zman_share_clean_lib", "IOException: copy File", e);
        }
    }
}
