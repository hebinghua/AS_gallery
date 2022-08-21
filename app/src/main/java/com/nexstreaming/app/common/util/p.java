package com.nexstreaming.app.common.util;

import android.util.Log;
import ch.qos.logback.classic.pattern.CallerDataConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* compiled from: ZipUtil.java */
/* loaded from: classes3.dex */
public class p {
    public static void a(File file, File file2) throws IOException {
        Log.d("ZipUtil", "Unzipping '" + file + "' to '" + file2 + "'");
        if (!file2.mkdirs() && !file2.exists()) {
            throw new IOException("Failed to create directory: " + file2);
        }
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        while (true) {
            try {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    String name = nextEntry.getName();
                    if (name.contains(CallerDataConverter.DEFAULT_RANGE_DELIMITER)) {
                        throw new IOException("Relative paths not allowed");
                    }
                    File file3 = new File(file2, name);
                    if (nextEntry.isDirectory()) {
                        if (!file3.mkdirs() && !file3.exists()) {
                            throw new IOException("Failed to create directory: " + file3);
                        }
                        Log.d("ZipUtil", "  - unzip: made folder '" + name + "'");
                    } else {
                        Log.d("ZipUtil", "  - unzip: unzipping file '" + name + "' " + nextEntry.getCompressedSize() + "->" + nextEntry.getSize() + " (" + a(nextEntry.getMethod()) + ")");
                        FileOutputStream fileOutputStream = new FileOutputStream(file3);
                        a(zipInputStream, fileOutputStream);
                        b.a(fileOutputStream);
                    }
                } else {
                    b.a(zipInputStream);
                    Log.d("ZipUtil", "Unzipping DONE for: '" + file + "' to '" + file2 + "'");
                    return;
                }
            } catch (Throwable th) {
                b.a(zipInputStream);
                throw th;
            }
        }
    }

    private static String a(int i) {
        if (i != 0) {
            if (i == 8) {
                return "DEFLATED";
            }
            return "UNKNOWN_" + i;
        }
        return "STORED";
    }

    private static void a(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 != read) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public static boolean a(File file) {
        byte[] bArr;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            bArr = new byte[2];
            try {
                fileInputStream.read(bArr);
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        if (bArr[0] == 80 && bArr[1] == 75) {
            return true;
        }
        Log.d("ZipUtil", "magic[0]=" + ((int) bArr[0]) + ",magic[1]=" + ((int) bArr[1]));
        return false;
    }
}
