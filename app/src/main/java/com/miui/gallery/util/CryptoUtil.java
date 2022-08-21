package com.miui.gallery.util;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.util.FileSize;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes2.dex */
public class CryptoUtil {
    public static final byte[] sAesIv = {17, 19, 33, 35, 49, 51, 65, 67, 81, 83, 97, 102, 103, 104, 113, 114};
    public static final byte[] sRandomKey = new byte[16];

    public static byte[] generateEmptyKey() {
        return new byte[16];
    }

    public static byte[] encrypt(byte[] bArr, byte[] bArr2) {
        if (bArr != null && bArr2 != null && bArr2.length == 16) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(sAesIv);
            try {
                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher.init(1, secretKeySpec, ivParameterSpec);
                return cipher.doFinal(bArr);
            } catch (InvalidAlgorithmParameterException e) {
                DefaultLogger.e("CryptoUtil", e);
            } catch (InvalidKeyException e2) {
                DefaultLogger.e("CryptoUtil", e2);
            } catch (NoSuchAlgorithmException e3) {
                DefaultLogger.e("CryptoUtil", e3);
            } catch (BadPaddingException e4) {
                DefaultLogger.e("CryptoUtil", e4);
            } catch (IllegalBlockSizeException e5) {
                DefaultLogger.e("CryptoUtil", e5);
            } catch (NoSuchPaddingException e6) {
                DefaultLogger.e("CryptoUtil", e6);
            }
        }
        return null;
    }

    public static byte[] decrypt(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null || bArr2.length != 16) {
            throw new IllegalArgumentException("IllegalArgument or Key length should be 16.");
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(2, secretKeySpec, new IvParameterSpec(sAesIv));
            return cipher.doFinal(bArr);
        } catch (InvalidAlgorithmParameterException e) {
            DefaultLogger.e("CryptoUtil", e);
            return null;
        } catch (InvalidKeyException e2) {
            DefaultLogger.e("CryptoUtil", e2);
            return null;
        } catch (NoSuchAlgorithmException e3) {
            DefaultLogger.e("CryptoUtil", e3);
            return null;
        } catch (BadPaddingException e4) {
            DefaultLogger.e("CryptoUtil", e4);
            return null;
        } catch (IllegalBlockSizeException e5) {
            DefaultLogger.e("CryptoUtil", e5);
            return null;
        } catch (NoSuchPaddingException e6) {
            DefaultLogger.e("CryptoUtil", e6);
            return null;
        }
    }

    public static boolean encryptFile(String str, String str2, byte[] bArr) {
        OutputStream outputStream;
        CipherInputStream cipherInputStream;
        if (bArr == null || bArr.length != 16) {
            return false;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(sAesIv);
        OutputStream outputStream2 = null;
        try {
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CryptoUtil", "encryptFile");
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile != null && documentFile.exists() && documentFile.isFile()) {
                InputStream openInputStream = StorageSolutionProvider.get().openInputStream(documentFile);
                try {
                    cipherInputStream = new CipherInputStream(openInputStream, cipher);
                } catch (Exception e) {
                    e = e;
                    cipherInputStream = null;
                    outputStream2 = openInputStream;
                    outputStream = null;
                } catch (Throwable th) {
                    th = th;
                    cipherInputStream = null;
                    outputStream2 = openInputStream;
                    outputStream = null;
                }
                try {
                    DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
                    if (documentFile2 == null) {
                        BaseMiscUtil.closeSilently(openInputStream);
                        BaseMiscUtil.closeSilently(null);
                        BaseMiscUtil.closeSilently(cipherInputStream);
                        return false;
                    }
                    outputStream2 = StorageSolutionProvider.get().openOutputStream(documentFile2);
                    byte[] bArr2 = new byte[1024];
                    while (true) {
                        int read = cipherInputStream.read(bArr2);
                        if (read == -1) {
                            outputStream2.flush();
                            BaseMiscUtil.closeSilently(openInputStream);
                            BaseMiscUtil.closeSilently(outputStream2);
                            BaseMiscUtil.closeSilently(cipherInputStream);
                            return true;
                        }
                        outputStream2.write(bArr2, 0, read);
                    }
                } catch (Exception e2) {
                    e = e2;
                    OutputStream outputStream3 = outputStream2;
                    outputStream2 = openInputStream;
                    outputStream = outputStream3;
                    try {
                        DefaultLogger.e("CryptoUtil", e);
                        BaseMiscUtil.closeSilently(outputStream2);
                        BaseMiscUtil.closeSilently(outputStream);
                        BaseMiscUtil.closeSilently(cipherInputStream);
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        BaseMiscUtil.closeSilently(outputStream2);
                        BaseMiscUtil.closeSilently(outputStream);
                        BaseMiscUtil.closeSilently(cipherInputStream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    OutputStream outputStream4 = outputStream2;
                    outputStream2 = openInputStream;
                    outputStream = outputStream4;
                    BaseMiscUtil.closeSilently(outputStream2);
                    BaseMiscUtil.closeSilently(outputStream);
                    BaseMiscUtil.closeSilently(cipherInputStream);
                    throw th;
                }
            }
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            return false;
        } catch (Exception e3) {
            e = e3;
            outputStream = null;
            cipherInputStream = null;
        } catch (Throwable th4) {
            th = th4;
            outputStream = null;
            cipherInputStream = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.security.Key, javax.crypto.spec.SecretKeySpec] */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r4v1, types: [javax.crypto.Cipher] */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v6, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r8v8, types: [java.io.Closeable, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v10, types: [java.io.OutputStream, java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r9v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r9v6 */
    /* JADX WARN: Type inference failed for: r9v8 */
    public static boolean decryptFile(String str, String str2, byte[] bArr) {
        Throwable th;
        CipherOutputStream cipherOutputStream;
        Exception e;
        if (bArr == null || bArr.length != 16) {
            return false;
        }
        ?? secretKeySpec = new SecretKeySpec(bArr, "AES");
        try {
            try {
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CryptoUtil", "decryptFile");
                ?? cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher.init(2, secretKeySpec, new IvParameterSpec(sAesIv));
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile((String) str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                if (documentFile == null) {
                    BaseMiscUtil.closeSilently(null);
                } else {
                    str = StorageSolutionProvider.get().openInputStream(documentFile);
                    try {
                        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile((String) str2, IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
                        if (documentFile2 == null) {
                            BaseMiscUtil.closeSilently(str);
                        } else {
                            str2 = StorageSolutionProvider.get().openOutputStream(documentFile2);
                            try {
                                cipherOutputStream = new CipherOutputStream(str2, cipher);
                                try {
                                    byte[] bArr2 = new byte[1024];
                                    while (true) {
                                        int read = str.read(bArr2);
                                        if (read == -1) {
                                            cipherOutputStream.flush();
                                            BaseMiscUtil.closeSilently(str);
                                            BaseMiscUtil.closeSilently(str2);
                                            BaseMiscUtil.closeSilently(cipherOutputStream);
                                            return true;
                                        }
                                        cipherOutputStream.write(bArr2, 0, read);
                                    }
                                } catch (Exception e2) {
                                    e = e2;
                                    DefaultLogger.e("CryptoUtil", e);
                                    BaseMiscUtil.closeSilently(str);
                                    BaseMiscUtil.closeSilently(str2);
                                    BaseMiscUtil.closeSilently(cipherOutputStream);
                                    return false;
                                }
                            } catch (Exception e3) {
                                cipherOutputStream = null;
                                e = e3;
                            } catch (Throwable th2) {
                                secretKeySpec = 0;
                                th = th2;
                                BaseMiscUtil.closeSilently(str);
                                BaseMiscUtil.closeSilently(str2);
                                BaseMiscUtil.closeSilently(secretKeySpec);
                                throw th;
                            }
                        }
                    } catch (Exception e4) {
                        cipherOutputStream = null;
                        e = e4;
                        str2 = 0;
                    } catch (Throwable th3) {
                        secretKeySpec = 0;
                        th = th3;
                        str2 = 0;
                    }
                }
                BaseMiscUtil.closeSilently(null);
                BaseMiscUtil.closeSilently(null);
                return false;
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (Exception e5) {
            str2 = 0;
            cipherOutputStream = null;
            e = e5;
            str = 0;
        } catch (Throwable th5) {
            str2 = 0;
            secretKeySpec = 0;
            th = th5;
            str = 0;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean encryptFileHeader(String str, String str2, byte[] bArr) {
        FileChannel fileChannel;
        FileInputStream fileInputStream;
        ParcelFileDescriptor parcelFileDescriptor;
        FileChannel fileChannel2;
        FileChannel fileChannel3;
        String str3;
        File file;
        byte[] bArr2;
        ParcelFileDescriptor parcelFileDescriptor2;
        ParcelFileDescriptor parcelFileDescriptor3;
        byte[] bArr3;
        FileChannel fileChannel4;
        byte[] bArr4;
        DocumentFile documentFile;
        byte[] bArr5 = bArr;
        if (bArr5 == null || bArr5.length != 16) {
            return false;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CryptoUtil", "encryptFileHeader");
        FileChannel fileChannel5 = null;
        try {
            if (ExtraTextUtils.equalsIgnoreCase(str, str2)) {
                file = new File(str2 + "." + System.currentTimeMillis() + ".tmp");
            } else {
                file = new File(str2);
            }
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile2 != null && documentFile2.exists() && documentFile2.isFile()) {
                ParcelFileDescriptor openFileDescriptor = StorageSolutionProvider.get().openFileDescriptor(documentFile2, "r");
                try {
                    fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
                    try {
                        DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
                        if (documentFile3 == null) {
                            BaseMiscUtil.closeSilently(openFileDescriptor);
                            BaseMiscUtil.closeSilently(fileInputStream);
                            BaseMiscUtil.closeSilently(null);
                            BaseMiscUtil.closeSilently(null);
                            BaseMiscUtil.closeSilently(null);
                        } else {
                            parcelFileDescriptor = StorageSolutionProvider.get().openFileDescriptor(documentFile3, "w");
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                                try {
                                    fileChannel3 = fileInputStream.getChannel();
                                    try {
                                        try {
                                            fileChannel5 = fileOutputStream.getChannel();
                                            long size = fileChannel3.size();
                                            long transferTo = fileChannel3.transferTo(0L, size, fileChannel5);
                                            if (transferTo == size) {
                                                int max = Math.max((int) Math.min((long) FileSize.KB_COEFFICIENT, size), 16);
                                                byte[] bArr6 = new byte[max];
                                                try {
                                                } catch (Exception e) {
                                                    e = e;
                                                    bArr5 = "CryptoUtil";
                                                }
                                                try {
                                                    if (fileInputStream.read(bArr6) != Math.min(max, size)) {
                                                        DefaultLogger.e("CryptoUtil", "read header bytes error");
                                                    } else {
                                                        byte[] encrypt = encrypt(bArr6, bArr);
                                                        if (encrypt != null && encrypt.length == max) {
                                                            if (fileChannel5.write(ByteBuffer.wrap(encrypt), 0L) == max) {
                                                                if (ExtraTextUtils.equalsIgnoreCase(file.getAbsolutePath(), str2)) {
                                                                    BaseMiscUtil.closeSilently(openFileDescriptor);
                                                                    BaseMiscUtil.closeSilently(fileInputStream);
                                                                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                                    BaseMiscUtil.closeSilently(fileOutputStream);
                                                                    BaseMiscUtil.closeSilently(fileChannel3);
                                                                    BaseMiscUtil.closeSilently(fileChannel5);
                                                                    return true;
                                                                }
                                                                boolean moveFile = StorageSolutionProvider.get().moveFile(file.getAbsolutePath(), str2, "CryptoUtil_encryptFileHeader");
                                                                if (!moveFile && (documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                                                                    documentFile.delete();
                                                                }
                                                                BaseMiscUtil.closeSilently(openFileDescriptor);
                                                                BaseMiscUtil.closeSilently(fileInputStream);
                                                                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                                BaseMiscUtil.closeSilently(fileOutputStream);
                                                                BaseMiscUtil.closeSilently(fileChannel3);
                                                                BaseMiscUtil.closeSilently(fileChannel5);
                                                                return moveFile;
                                                            }
                                                            DefaultLogger.e("CryptoUtil", "write encrypted header bytes error");
                                                        }
                                                        DefaultLogger.e("CryptoUtil", "encrypt header bytes error");
                                                    }
                                                    BaseMiscUtil.closeSilently(openFileDescriptor);
                                                    BaseMiscUtil.closeSilently(fileInputStream);
                                                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                    BaseMiscUtil.closeSilently(fileOutputStream);
                                                    BaseMiscUtil.closeSilently(fileChannel3);
                                                    BaseMiscUtil.closeSilently(fileChannel5);
                                                    return false;
                                                } catch (Exception e2) {
                                                    e = e2;
                                                    fileChannel = fileChannel5;
                                                    bArr4 = bArr5;
                                                    fileChannel4 = fileOutputStream;
                                                    fileChannel5 = openFileDescriptor;
                                                    str3 = bArr4;
                                                    fileChannel2 = fileChannel4;
                                                    try {
                                                        DefaultLogger.e(str3, e);
                                                        BaseMiscUtil.closeSilently(fileChannel5);
                                                        BaseMiscUtil.closeSilently(fileInputStream);
                                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                        BaseMiscUtil.closeSilently(fileChannel2);
                                                        BaseMiscUtil.closeSilently(fileChannel3);
                                                        BaseMiscUtil.closeSilently(fileChannel);
                                                        return false;
                                                    } catch (Throwable th) {
                                                        th = th;
                                                        BaseMiscUtil.closeSilently(fileChannel5);
                                                        BaseMiscUtil.closeSilently(fileInputStream);
                                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                        BaseMiscUtil.closeSilently(fileChannel2);
                                                        BaseMiscUtil.closeSilently(fileChannel3);
                                                        BaseMiscUtil.closeSilently(fileChannel);
                                                        throw th;
                                                    }
                                                }
                                            }
                                            DefaultLogger.e("CryptoUtil", "transfer error, expect count %s, actual count %s", Long.valueOf(size), Long.valueOf(transferTo));
                                            BaseMiscUtil.closeSilently(openFileDescriptor);
                                            BaseMiscUtil.closeSilently(fileInputStream);
                                            BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                            BaseMiscUtil.closeSilently(fileOutputStream);
                                            BaseMiscUtil.closeSilently(fileChannel3);
                                        } catch (Exception e3) {
                                            e = e3;
                                            bArr5 = "CryptoUtil";
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        fileChannel = null;
                                        fileChannel2 = fileOutputStream;
                                        fileChannel5 = openFileDescriptor;
                                        BaseMiscUtil.closeSilently(fileChannel5);
                                        BaseMiscUtil.closeSilently(fileInputStream);
                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                        BaseMiscUtil.closeSilently(fileChannel2);
                                        BaseMiscUtil.closeSilently(fileChannel3);
                                        BaseMiscUtil.closeSilently(fileChannel);
                                        throw th;
                                    }
                                } catch (Exception e4) {
                                    e = e4;
                                    bArr4 = "CryptoUtil";
                                    fileChannel = null;
                                    fileChannel3 = null;
                                    fileChannel4 = fileOutputStream;
                                } catch (Throwable th3) {
                                    th = th3;
                                    fileChannel = null;
                                    fileChannel3 = null;
                                    fileChannel2 = fileOutputStream;
                                }
                            } catch (Exception e5) {
                                e = e5;
                                bArr3 = "CryptoUtil";
                                fileChannel = null;
                                parcelFileDescriptor3 = null;
                                fileChannel3 = parcelFileDescriptor3;
                                bArr4 = bArr3;
                                fileChannel4 = parcelFileDescriptor3;
                                fileChannel5 = openFileDescriptor;
                                str3 = bArr4;
                                fileChannel2 = fileChannel4;
                                DefaultLogger.e(str3, e);
                                BaseMiscUtil.closeSilently(fileChannel5);
                                BaseMiscUtil.closeSilently(fileInputStream);
                                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                BaseMiscUtil.closeSilently(fileChannel2);
                                BaseMiscUtil.closeSilently(fileChannel3);
                                BaseMiscUtil.closeSilently(fileChannel);
                                return false;
                            } catch (Throwable th4) {
                                th = th4;
                                fileChannel = null;
                                parcelFileDescriptor2 = null;
                                fileChannel3 = parcelFileDescriptor2;
                                fileChannel2 = parcelFileDescriptor2;
                                fileChannel5 = openFileDescriptor;
                                BaseMiscUtil.closeSilently(fileChannel5);
                                BaseMiscUtil.closeSilently(fileInputStream);
                                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                BaseMiscUtil.closeSilently(fileChannel2);
                                BaseMiscUtil.closeSilently(fileChannel3);
                                BaseMiscUtil.closeSilently(fileChannel);
                                throw th;
                            }
                        }
                        BaseMiscUtil.closeSilently(fileChannel5);
                        return false;
                    } catch (Exception e6) {
                        e = e6;
                        bArr2 = "CryptoUtil";
                        fileChannel = null;
                        parcelFileDescriptor = null;
                        parcelFileDescriptor3 = parcelFileDescriptor;
                        bArr3 = bArr2;
                        fileChannel3 = parcelFileDescriptor3;
                        bArr4 = bArr3;
                        fileChannel4 = parcelFileDescriptor3;
                        fileChannel5 = openFileDescriptor;
                        str3 = bArr4;
                        fileChannel2 = fileChannel4;
                        DefaultLogger.e(str3, e);
                        BaseMiscUtil.closeSilently(fileChannel5);
                        BaseMiscUtil.closeSilently(fileInputStream);
                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                        BaseMiscUtil.closeSilently(fileChannel2);
                        BaseMiscUtil.closeSilently(fileChannel3);
                        BaseMiscUtil.closeSilently(fileChannel);
                        return false;
                    } catch (Throwable th5) {
                        th = th5;
                        fileChannel = null;
                        parcelFileDescriptor = null;
                        parcelFileDescriptor2 = parcelFileDescriptor;
                        fileChannel3 = parcelFileDescriptor2;
                        fileChannel2 = parcelFileDescriptor2;
                        fileChannel5 = openFileDescriptor;
                        BaseMiscUtil.closeSilently(fileChannel5);
                        BaseMiscUtil.closeSilently(fileInputStream);
                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                        BaseMiscUtil.closeSilently(fileChannel2);
                        BaseMiscUtil.closeSilently(fileChannel3);
                        BaseMiscUtil.closeSilently(fileChannel);
                        throw th;
                    }
                } catch (Exception e7) {
                    e = e7;
                    bArr2 = "CryptoUtil";
                    fileChannel = null;
                    fileInputStream = null;
                    parcelFileDescriptor = null;
                } catch (Throwable th6) {
                    th = th6;
                    fileChannel = null;
                    fileInputStream = null;
                    parcelFileDescriptor = null;
                }
            }
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(fileChannel5);
            return false;
        } catch (Exception e8) {
            e = e8;
            str3 = "CryptoUtil";
            fileChannel = null;
            fileInputStream = null;
            parcelFileDescriptor = null;
            fileChannel2 = null;
            fileChannel3 = null;
        } catch (Throwable th7) {
            th = th7;
            fileChannel = null;
            fileInputStream = null;
            parcelFileDescriptor = null;
            fileChannel2 = null;
            fileChannel3 = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean decryptFileHeader(String str, String str2, byte[] bArr) {
        FileChannel fileChannel;
        FileInputStream fileInputStream;
        ParcelFileDescriptor parcelFileDescriptor;
        FileChannel fileChannel2;
        FileChannel fileChannel3;
        String str3;
        File file;
        byte[] bArr2;
        ParcelFileDescriptor parcelFileDescriptor2;
        ParcelFileDescriptor parcelFileDescriptor3;
        byte[] bArr3;
        FileChannel fileChannel4;
        byte[] bArr4;
        int max;
        byte[] bArr5;
        DocumentFile documentFile;
        byte[] bArr6 = bArr;
        if (bArr6 == null || bArr6.length != 16) {
            return false;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CryptoUtil", "decryptFileHeader");
        FileChannel fileChannel5 = null;
        try {
            if (ExtraTextUtils.equalsIgnoreCase(str, str2)) {
                file = new File(str2 + "." + System.currentTimeMillis() + ".tmp");
            } else {
                file = new File(str2);
            }
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile2 != null && documentFile2.exists() && documentFile2.isFile()) {
                ParcelFileDescriptor openFileDescriptor = StorageSolutionProvider.get().openFileDescriptor(documentFile2, "r");
                try {
                    fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
                    try {
                        DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
                        if (documentFile3 == null) {
                            BaseMiscUtil.closeSilently(openFileDescriptor);
                            BaseMiscUtil.closeSilently(fileInputStream);
                            BaseMiscUtil.closeSilently(null);
                            BaseMiscUtil.closeSilently(null);
                            BaseMiscUtil.closeSilently(null);
                        } else {
                            parcelFileDescriptor = StorageSolutionProvider.get().openFileDescriptor(documentFile3, "w");
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                                try {
                                    fileChannel3 = fileInputStream.getChannel();
                                    try {
                                        try {
                                            fileChannel5 = fileOutputStream.getChannel();
                                            long size = fileChannel3.size();
                                            long transferTo = fileChannel3.transferTo(0L, size, fileChannel5);
                                            if (transferTo == size) {
                                                try {
                                                    max = Math.max((int) Math.min((long) FileSize.KB_COEFFICIENT, fileChannel3.size()), 16);
                                                    bArr5 = new byte[max];
                                                } catch (Exception e) {
                                                    e = e;
                                                    bArr6 = "CryptoUtil";
                                                }
                                                try {
                                                    if (fileInputStream.read(bArr5) != Math.min(max, size)) {
                                                        DefaultLogger.e("CryptoUtil", "read encrypted header bytes error");
                                                    } else {
                                                        byte[] decrypt = decrypt(bArr5, bArr);
                                                        if (decrypt != null && decrypt.length == max) {
                                                            if (decrypt.length == 16) {
                                                                int i = 15;
                                                                while (i >= 0 && decrypt[i] == 0) {
                                                                    i--;
                                                                }
                                                                if (i == -1) {
                                                                    DefaultLogger.e("CryptoUtil", "Encrypted file is malformed.");
                                                                } else if (i < 15) {
                                                                    max = i + 1;
                                                                    byte[] bArr7 = new byte[max];
                                                                    System.arraycopy(decrypt, 0, bArr7, 0, max);
                                                                    decrypt = (byte[]) bArr7.clone();
                                                                    fileChannel5.truncate(max);
                                                                }
                                                            }
                                                            if (fileChannel5.write(ByteBuffer.wrap(decrypt), 0L) == max) {
                                                                if (ExtraTextUtils.equalsIgnoreCase(file.getAbsolutePath(), str2)) {
                                                                    BaseMiscUtil.closeSilently(openFileDescriptor);
                                                                    BaseMiscUtil.closeSilently(fileInputStream);
                                                                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                                    BaseMiscUtil.closeSilently(fileOutputStream);
                                                                    BaseMiscUtil.closeSilently(fileChannel3);
                                                                    BaseMiscUtil.closeSilently(fileChannel5);
                                                                    return true;
                                                                }
                                                                boolean moveFile = StorageSolutionProvider.get().moveFile(file.getAbsolutePath(), str2, appendInvokerTag);
                                                                if (!moveFile && (documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                                                                    documentFile.delete();
                                                                }
                                                                BaseMiscUtil.closeSilently(openFileDescriptor);
                                                                BaseMiscUtil.closeSilently(fileInputStream);
                                                                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                                BaseMiscUtil.closeSilently(fileOutputStream);
                                                                BaseMiscUtil.closeSilently(fileChannel3);
                                                                BaseMiscUtil.closeSilently(fileChannel5);
                                                                return moveFile;
                                                            }
                                                            DefaultLogger.e("CryptoUtil", "write decrypted header bytes error");
                                                        }
                                                        DefaultLogger.e("CryptoUtil", "decrypt header bytes error");
                                                    }
                                                    BaseMiscUtil.closeSilently(openFileDescriptor);
                                                    BaseMiscUtil.closeSilently(fileInputStream);
                                                    BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                    BaseMiscUtil.closeSilently(fileOutputStream);
                                                    BaseMiscUtil.closeSilently(fileChannel3);
                                                    BaseMiscUtil.closeSilently(fileChannel5);
                                                    return false;
                                                } catch (Exception e2) {
                                                    e = e2;
                                                    fileChannel = fileChannel5;
                                                    bArr4 = bArr6;
                                                    fileChannel4 = fileOutputStream;
                                                    fileChannel5 = openFileDescriptor;
                                                    str3 = bArr4;
                                                    fileChannel2 = fileChannel4;
                                                    try {
                                                        DefaultLogger.e(str3, e);
                                                        BaseMiscUtil.closeSilently(fileChannel5);
                                                        BaseMiscUtil.closeSilently(fileInputStream);
                                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                        BaseMiscUtil.closeSilently(fileChannel2);
                                                        BaseMiscUtil.closeSilently(fileChannel3);
                                                        BaseMiscUtil.closeSilently(fileChannel);
                                                        return false;
                                                    } catch (Throwable th) {
                                                        th = th;
                                                        BaseMiscUtil.closeSilently(fileChannel5);
                                                        BaseMiscUtil.closeSilently(fileInputStream);
                                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                                        BaseMiscUtil.closeSilently(fileChannel2);
                                                        BaseMiscUtil.closeSilently(fileChannel3);
                                                        BaseMiscUtil.closeSilently(fileChannel);
                                                        throw th;
                                                    }
                                                }
                                            }
                                            DefaultLogger.e("CryptoUtil", "transfer error, expect count %s, actual count %s", Long.valueOf(size), Long.valueOf(transferTo));
                                            BaseMiscUtil.closeSilently(openFileDescriptor);
                                            BaseMiscUtil.closeSilently(fileInputStream);
                                            BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                            BaseMiscUtil.closeSilently(fileOutputStream);
                                            BaseMiscUtil.closeSilently(fileChannel3);
                                        } catch (Exception e3) {
                                            e = e3;
                                            bArr6 = "CryptoUtil";
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        fileChannel = null;
                                        fileChannel2 = fileOutputStream;
                                        fileChannel5 = openFileDescriptor;
                                        BaseMiscUtil.closeSilently(fileChannel5);
                                        BaseMiscUtil.closeSilently(fileInputStream);
                                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                        BaseMiscUtil.closeSilently(fileChannel2);
                                        BaseMiscUtil.closeSilently(fileChannel3);
                                        BaseMiscUtil.closeSilently(fileChannel);
                                        throw th;
                                    }
                                } catch (Exception e4) {
                                    e = e4;
                                    bArr4 = "CryptoUtil";
                                    fileChannel = null;
                                    fileChannel3 = null;
                                    fileChannel4 = fileOutputStream;
                                } catch (Throwable th3) {
                                    th = th3;
                                    fileChannel = null;
                                    fileChannel3 = null;
                                    fileChannel2 = fileOutputStream;
                                }
                            } catch (Exception e5) {
                                e = e5;
                                bArr3 = "CryptoUtil";
                                fileChannel = null;
                                parcelFileDescriptor3 = null;
                                fileChannel3 = parcelFileDescriptor3;
                                bArr4 = bArr3;
                                fileChannel4 = parcelFileDescriptor3;
                                fileChannel5 = openFileDescriptor;
                                str3 = bArr4;
                                fileChannel2 = fileChannel4;
                                DefaultLogger.e(str3, e);
                                BaseMiscUtil.closeSilently(fileChannel5);
                                BaseMiscUtil.closeSilently(fileInputStream);
                                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                BaseMiscUtil.closeSilently(fileChannel2);
                                BaseMiscUtil.closeSilently(fileChannel3);
                                BaseMiscUtil.closeSilently(fileChannel);
                                return false;
                            } catch (Throwable th4) {
                                th = th4;
                                fileChannel = null;
                                parcelFileDescriptor2 = null;
                                fileChannel3 = parcelFileDescriptor2;
                                fileChannel2 = parcelFileDescriptor2;
                                fileChannel5 = openFileDescriptor;
                                BaseMiscUtil.closeSilently(fileChannel5);
                                BaseMiscUtil.closeSilently(fileInputStream);
                                BaseMiscUtil.closeSilently(parcelFileDescriptor);
                                BaseMiscUtil.closeSilently(fileChannel2);
                                BaseMiscUtil.closeSilently(fileChannel3);
                                BaseMiscUtil.closeSilently(fileChannel);
                                throw th;
                            }
                        }
                        BaseMiscUtil.closeSilently(fileChannel5);
                        return false;
                    } catch (Exception e6) {
                        e = e6;
                        bArr2 = "CryptoUtil";
                        fileChannel = null;
                        parcelFileDescriptor = null;
                        parcelFileDescriptor3 = parcelFileDescriptor;
                        bArr3 = bArr2;
                        fileChannel3 = parcelFileDescriptor3;
                        bArr4 = bArr3;
                        fileChannel4 = parcelFileDescriptor3;
                        fileChannel5 = openFileDescriptor;
                        str3 = bArr4;
                        fileChannel2 = fileChannel4;
                        DefaultLogger.e(str3, e);
                        BaseMiscUtil.closeSilently(fileChannel5);
                        BaseMiscUtil.closeSilently(fileInputStream);
                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                        BaseMiscUtil.closeSilently(fileChannel2);
                        BaseMiscUtil.closeSilently(fileChannel3);
                        BaseMiscUtil.closeSilently(fileChannel);
                        return false;
                    } catch (Throwable th5) {
                        th = th5;
                        fileChannel = null;
                        parcelFileDescriptor = null;
                        parcelFileDescriptor2 = parcelFileDescriptor;
                        fileChannel3 = parcelFileDescriptor2;
                        fileChannel2 = parcelFileDescriptor2;
                        fileChannel5 = openFileDescriptor;
                        BaseMiscUtil.closeSilently(fileChannel5);
                        BaseMiscUtil.closeSilently(fileInputStream);
                        BaseMiscUtil.closeSilently(parcelFileDescriptor);
                        BaseMiscUtil.closeSilently(fileChannel2);
                        BaseMiscUtil.closeSilently(fileChannel3);
                        BaseMiscUtil.closeSilently(fileChannel);
                        throw th;
                    }
                } catch (Exception e7) {
                    e = e7;
                    bArr2 = "CryptoUtil";
                    fileChannel = null;
                    fileInputStream = null;
                    parcelFileDescriptor = null;
                } catch (Throwable th6) {
                    th = th6;
                    fileChannel = null;
                    fileInputStream = null;
                    parcelFileDescriptor = null;
                }
            }
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(null);
            BaseMiscUtil.closeSilently(fileChannel5);
            return false;
        } catch (Exception e8) {
            e = e8;
            str3 = "CryptoUtil";
            fileChannel = null;
            fileInputStream = null;
            parcelFileDescriptor = null;
            fileChannel2 = null;
            fileChannel3 = null;
        } catch (Throwable th7) {
            th = th7;
            fileChannel = null;
            fileInputStream = null;
            parcelFileDescriptor = null;
            fileChannel2 = null;
            fileChannel3 = null;
        }
    }

    public static synchronized byte[] generateRandomKey() {
        byte[] generateEmptyKey;
        synchronized (CryptoUtil.class) {
            generateEmptyKey = generateEmptyKey();
            SecureRandom secureRandom = new SecureRandom();
            byte[] bArr = sRandomKey;
            secureRandom.nextBytes(bArr);
            System.arraycopy(bArr, 0, generateEmptyKey, 0, bArr.length);
        }
        return generateEmptyKey;
    }

    public static Cipher getCipher(byte[] bArr, int i) {
        Cipher cipher = null;
        if (bArr == null || bArr.length != 16) {
            return null;
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(sAesIv);
        try {
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(i, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (InvalidAlgorithmParameterException e) {
            DefaultLogger.e("CryptoUtil", e);
            return cipher;
        } catch (InvalidKeyException e2) {
            DefaultLogger.e("CryptoUtil", e2);
            return cipher;
        } catch (NoSuchAlgorithmException e3) {
            DefaultLogger.e("CryptoUtil", e3);
            return cipher;
        } catch (NoSuchPaddingException e4) {
            DefaultLogger.e("CryptoUtil", e4);
            return cipher;
        }
    }

    public static CipherInputStream getDecryptCipherInputStream(InputStream inputStream, byte[] bArr) {
        return new CipherInputStream(inputStream, getCipher(bArr, 2));
    }

    public static InputStream getDecryptCipherInputStream(String str, byte[] bArr) throws FileNotFoundException {
        InputStream openInputStream = StorageSolutionProvider.get().openInputStream(StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CryptoUtil", "getDecryptCipherInputStream")));
        return bArr != null ? new CipherInputStream(openInputStream, getCipher(bArr, 2)) : openInputStream;
    }

    public static InputStream getDecryptCipherInputStream(ContentResolver contentResolver, Uri uri, byte[] bArr) throws FileNotFoundException {
        InputStream openInputStream = contentResolver.openInputStream(uri);
        return bArr != null ? new CipherInputStream(openInputStream, getCipher(bArr, 2)) : openInputStream;
    }
}
