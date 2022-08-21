package com.miui.gallery.video.editor.util;

import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
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
    public static String generateTempOutputFilePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("FileHelper", "generateTempOutputFilePath");
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/videoEditorTemp");
        StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
        String fileName = BaseFileUtils.getFileName(str);
        return pathInPrimaryStorage + File.separator + fileName;
    }

    public static String generateOutputFilePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return generateOutputFilePath(new File(str));
    }

    public static String generateOutputFilePath(File file) {
        String name;
        String str;
        if (file == null) {
            return null;
        }
        int lastIndexOf = file.getName().lastIndexOf(".");
        if (lastIndexOf != -1 && lastIndexOf < file.getName().length() - 1) {
            str = file.getName().substring(lastIndexOf + 1);
            name = file.getName().substring(0, lastIndexOf);
        } else {
            name = file.getName();
            str = "";
        }
        File parentFile = file.getParentFile();
        String absolutePath = parentFile != null ? parentFile.getAbsolutePath() : null;
        if (TextUtils.isEmpty(absolutePath) && (absolutePath = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE)) == null) {
            return null;
        }
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String concat = BaseFileUtils.concat(absolutePath, name);
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT;
        if (!storageStrategyManager.checkPermission(concat, permission).granted) {
            absolutePath = StorageUtils.getPathInPrimaryStorage(StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), absolutePath));
            if (!StorageSolutionProvider.get().checkPermission(BaseFileUtils.concat(absolutePath, name), permission).granted && (absolutePath = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE)) == null) {
                return null;
            }
            StorageSolutionProvider.get().getDocumentFile(absolutePath, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("FileHelper", "generateOutputFilePath"));
        }
        return generateFileName(absolutePath, name, str, 0);
    }

    public static String generateFileName(String str, String str2, String str3, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("(");
        sb.append(String.valueOf(i));
        sb.append(")");
        if (!TextUtils.isEmpty(str3)) {
            sb.append(".");
            sb.append(str3);
        }
        File file = new File(str, sb.toString());
        if (file.exists()) {
            return generateFileName(str, str2, str3, i + 1);
        }
        return file.getAbsolutePath();
    }

    public static boolean createDir(String str) {
        File file = new File(str);
        if (file.exists()) {
            DefaultLogger.d("FileHelper", "The target directory already exists");
            return true;
        } else if (file.mkdirs()) {
            DefaultLogger.d("FileHelper", "create dir sucessed！");
            return true;
        } else {
            DefaultLogger.d("FileHelper", "create dir failed！ ");
            return false;
        }
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
