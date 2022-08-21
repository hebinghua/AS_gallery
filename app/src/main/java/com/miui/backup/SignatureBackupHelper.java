package com.miui.backup;

import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.stat.b.h;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* loaded from: classes.dex */
public class SignatureBackupHelper {
    private static final String SIGNATURE_FOLDER = "signature_folder";
    private static final String SIGNATURE_ZIP_NAME = "signature_folder.zip";
    private static final String TAG = "SignatureBackupHelper";

    private SignatureBackupHelper() {
    }

    public static void backupSignature(MiuiBackupAgentImpl miuiBackupAgentImpl, String str) {
        if (!TextUtils.isEmpty(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            String str2 = File.separator;
            sb.append(str2);
            sb.append(SIGNATURE_FOLDER);
            String sb2 = sb.toString();
            if (!new File(sb2).exists() || !createZipFile(sb2, str, SIGNATURE_ZIP_NAME)) {
                return;
            }
            miuiBackupAgentImpl.addAttachedFile(str + str2 + SIGNATURE_ZIP_NAME);
        }
    }

    public static void restoreSignature(String str, BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor, String str2) {
        if (backupMeta.version >= 2 || str2.endsWith(SIGNATURE_ZIP_NAME)) {
            FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            String str3 = str + h.g + SIGNATURE_ZIP_NAME;
            File file = new File(str3);
            writeToLocal(str3, fileInputStream);
            if (!file.exists()) {
                return;
            }
            try {
                UnZipFolder(str2, str);
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeToLocal(String str, FileInputStream fileInputStream) {
        byte[] bArr = new byte[1024];
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(str);
                while (true) {
                    try {
                        int read = fileInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream2.write(bArr, 0, read);
                        fileOutputStream2.flush();
                    } catch (IOException e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                        e.printStackTrace();
                        closeSilently(fileOutputStream);
                        closeSilently(fileInputStream);
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        closeSilently(fileOutputStream);
                        closeSilently(fileInputStream);
                        throw th;
                    }
                }
                fileOutputStream2.close();
                closeSilently(fileOutputStream2);
            } catch (IOException e2) {
                e = e2;
            }
            closeSilently(fileInputStream);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00da A[Catch: IOException -> 0x00d6, TRY_LEAVE, TryCatch #1 {IOException -> 0x00d6, blocks: (B:45:0x00d2, B:49:0x00da), top: B:55:0x00d2 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00d2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean createZipFile(java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 226
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.backup.SignatureBackupHelper.createZipFile(java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    private static void writeZip(File file, String str, ZipOutputStream zipOutputStream) {
        StringBuilder sb;
        FileInputStream fileInputStream;
        if (file.exists()) {
            if (file.isDirectory()) {
                String str2 = str + file.getName() + File.separator;
                File[] listFiles = file.listFiles();
                if (listFiles.length != 0) {
                    for (File file2 : listFiles) {
                        writeZip(file2, str2, zipOutputStream);
                    }
                    return;
                }
                try {
                    zipOutputStream.putNextEntry(new ZipEntry(str2));
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            FileInputStream fileInputStream2 = null;
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                } catch (IOException e2) {
                    e = e2;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                zipOutputStream.putNextEntry(new ZipEntry(str + file.getName()));
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read != -1) {
                        zipOutputStream.write(bArr, 0, read);
                        zipOutputStream.flush();
                    } else {
                        try {
                            fileInputStream.close();
                            return;
                        } catch (IOException e3) {
                            e = e3;
                            sb = new StringBuilder();
                            sb.append("create zip file failed!");
                            sb.append(e.getMessage());
                            Log.e(TAG, sb.toString());
                        }
                    }
                }
            } catch (IOException e4) {
                e = e4;
                fileInputStream2 = fileInputStream;
                Log.e(TAG, "create zip file failed!" + e.getMessage());
                if (fileInputStream2 == null) {
                    return;
                }
                try {
                    fileInputStream2.close();
                } catch (IOException e5) {
                    e = e5;
                    sb = new StringBuilder();
                    sb.append("create zip file failed!");
                    sb.append(e.getMessage());
                    Log.e(TAG, sb.toString());
                }
            } catch (Throwable th2) {
                th = th2;
                fileInputStream2 = fileInputStream;
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException e6) {
                        Log.e(TAG, "create zip file failed!" + e6.getMessage());
                    }
                }
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.io.FileOutputStream] */
    public static void UnZipFolder(String str, String str2) throws Exception {
        ?? r3;
        ZipInputStream zipInputStream = null;
        try {
            try {
                ZipInputStream zipInputStream2 = new ZipInputStream(new FileInputStream(str));
                while (true) {
                    try {
                        ZipEntry nextEntry = zipInputStream2.getNextEntry();
                        if (nextEntry != null) {
                            String name = nextEntry.getName();
                            if (nextEntry.isDirectory()) {
                                new File(str2 + File.separator + name.substring(0, name.length() - 1)).mkdirs();
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(str2);
                                String str3 = File.separator;
                                sb.append(str3);
                                sb.append(name);
                                Log.e(TAG, sb.toString());
                                File file = new File(str2 + str3 + name);
                                if (!file.exists()) {
                                    Log.e(TAG, "Create the file:" + str2 + str3 + name);
                                    file.getParentFile().mkdirs();
                                    file.createNewFile();
                                }
                                try {
                                    r3 = new FileOutputStream(file);
                                    try {
                                        try {
                                            byte[] bArr = new byte[1024];
                                            while (true) {
                                                int read = zipInputStream2.read(bArr);
                                                if (read == -1) {
                                                    break;
                                                }
                                                r3.write(bArr, 0, read);
                                                r3.flush();
                                            }
                                        } catch (IOException e) {
                                            e = e;
                                            e.printStackTrace();
                                            closeSilently(r3);
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        zipInputStream = r3;
                                        closeSilently(zipInputStream);
                                        throw th;
                                    }
                                } catch (IOException e2) {
                                    e = e2;
                                    r3 = 0;
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                                closeSilently(r3);
                            }
                        } else {
                            closeSilently(zipInputStream2);
                            return;
                        }
                    } catch (IOException e3) {
                        e = e3;
                        zipInputStream = zipInputStream2;
                        e.printStackTrace();
                        closeSilently(zipInputStream);
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        zipInputStream = zipInputStream2;
                        closeSilently(zipInputStream);
                        throw th;
                    }
                }
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (IOException e4) {
            e = e4;
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            Log.e(TAG, "close fail", e);
        }
    }
}
