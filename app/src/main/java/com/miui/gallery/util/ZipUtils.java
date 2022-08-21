package com.miui.gallery.util;

import com.miui.gallery.util.logger.DefaultLogger;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes2.dex */
public abstract class ZipUtils {
    public static void unzip(File file, File file2) throws FileNotFoundException, IOException {
        DefaultLogger.d("ZipUtils", "unzip zipfile[%s] to [%s]", file, file2);
        Objects.requireNonNull(file, "zip file can't be null");
        Objects.requireNonNull(file2, "unzip folder can't be null");
        if (!file.exists()) {
            throw new FileNotFoundException("zip file not exists");
        }
        if (!file2.exists() && !file2.mkdirs()) {
            throw new IOException("create folder failed");
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            try {
                ZipEntry nextElement = entries.nextElement();
                InputStream inputStream = zipFile.getInputStream(nextElement);
                File file3 = new File(file2, nextElement.getName());
                DefaultLogger.d("ZipUtils", "unzipping %s", nextElement.getName());
                if (nextElement.isDirectory()) {
                    if (!file3.mkdirs()) {
                        throw new IOException(String.format(Locale.US, "mkdir for %s failed", nextElement.getName()));
                    }
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e = e;
                        e.printStackTrace();
                    }
                } else {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file3));
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        bufferedOutputStream.write(bArr, 0, read);
                    }
                    bufferedOutputStream.flush();
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        inputStream.close();
                    } catch (IOException e3) {
                        e = e3;
                        e.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                try {
                    zipFile.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                throw th;
            }
        }
        try {
            zipFile.close();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
    }
}
