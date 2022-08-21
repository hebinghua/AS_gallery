package com.miui.gallery.vlog.tools;

import android.text.TextUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* loaded from: classes2.dex */
public class FileHelper {
    public static String generateOutputFilePath(String str) {
        if (!TextUtils.isEmpty(str)) {
            return VlogUtils.generateOutputFilePath(new File(str));
        }
        return null;
    }

    public static String unZipFile(String str, String str2) {
        File file;
        ZipInputStream zipInputStream;
        File file2 = new File(str);
        ZipInputStream zipInputStream2 = null;
        if (!file2.exists()) {
            return null;
        }
        if (TextUtils.isEmpty(str2)) {
            file = new File(file2.getParent() + h.g + file2.getName().replaceAll("[.][^.]+$", ""));
        } else {
            file = new File(str2);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(file2));
            try {
                try {
                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                    byte[] bArr = new byte[1024];
                    while (nextEntry != null) {
                        if (nextEntry.isDirectory()) {
                            File file3 = new File(file, nextEntry.getName());
                            if (!file3.exists()) {
                                file3.mkdir();
                            }
                            nextEntry = zipInputStream.getNextEntry();
                        } else {
                            File file4 = new File(file, nextEntry.getName());
                            if (!file4.getParentFile().exists()) {
                                file4.getParentFile().mkdirs();
                            }
                            file4.createNewFile();
                            outputToFileStream(bArr, file4, zipInputStream);
                            nextEntry = zipInputStream.getNextEntry();
                        }
                    }
                    BaseMiscUtil.closeSilently(zipInputStream);
                    if (!file.exists()) {
                        return null;
                    }
                    if (file2.exists()) {
                        file2.delete();
                    }
                    return file.getAbsolutePath();
                } catch (IOException e) {
                    e = e;
                    if (file.exists()) {
                        file.delete();
                    }
                    DefaultLogger.e("FileHelper", e.toString());
                    BaseMiscUtil.closeSilently(zipInputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                zipInputStream2 = zipInputStream;
                BaseMiscUtil.closeSilently(zipInputStream2);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            zipInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            BaseMiscUtil.closeSilently(zipInputStream2);
            throw th;
        }
    }

    public static void outputToFileStream(byte[] bArr, File file, ZipInputStream zipInputStream) {
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                while (true) {
                    try {
                        int read = zipInputStream.read(bArr);
                        if (read > 0) {
                            fileOutputStream2.write(bArr, 0, read);
                        } else {
                            BaseMiscUtil.closeSilently(fileOutputStream2);
                            return;
                        }
                    } catch (FileNotFoundException e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                        DefaultLogger.e("FileHelper", e.toString());
                        BaseMiscUtil.closeSilently(fileOutputStream);
                        return;
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = fileOutputStream2;
                        DefaultLogger.e("FileHelper", e.toString());
                        BaseMiscUtil.closeSilently(fileOutputStream);
                        return;
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        BaseMiscUtil.closeSilently(fileOutputStream);
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (FileNotFoundException e3) {
            e = e3;
        } catch (IOException e4) {
            e = e4;
        }
    }
}
