package com.miui.gallery.util;

import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/* loaded from: classes2.dex */
public class NoMediaRecorder {
    public static final String RECORD_FILE_PATH = StorageUtils.getFilePathUnder(StorageUtils.getPrimaryStoragePath(), "/Android/data/com.miui.gallery/files/noMediaRecord/NoMediaRecord");
    public static volatile NoMediaRecorder sInstance;
    public MappedByteBuffer mRecordBuffer;
    public RandomAccessFile mRecordFile;
    public final byte[] mRecordHeader = new byte[5];
    public final Object mLock = new Object();

    public static NoMediaRecorder getInstance() {
        if (sInstance == null) {
            synchronized (NoMediaRecorder.class) {
                if (sInstance == null) {
                    sInstance = new NoMediaRecorder();
                }
            }
        }
        return sInstance;
    }

    public NoMediaRecorder() {
        File file = new File(RECORD_FILE_PATH);
        if (!file.exists()) {
            createRecordFile(file);
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            this.mRecordFile = randomAccessFile;
            this.mRecordBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, this.mRecordFile.length());
        } catch (IOException e) {
            DefaultLogger.e("NoMediaRecorder", "failed to init NoMediaRecorder, %s.", e, toString());
        }
    }

    public final void createRecordFile(File file) {
        StorageSolutionProvider.get().getDocumentFile(StorageUtils.getFilePathUnder(StorageUtils.getPrimaryStoragePath(), "/Android/data/com.miui.gallery/files/noMediaRecord"), IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("NoMediaRecorder", "createRecordFile"));
        try {
            file.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            this.mRecordFile = randomAccessFile;
            randomAccessFile.setLength(10240L);
        } catch (Exception e) {
            DefaultLogger.d("NoMediaRecorder", "create record file failed. %s", e.toString());
        }
    }

    public final int lookup(String str) {
        MappedByteBuffer mappedByteBuffer = this.mRecordBuffer;
        if (mappedByteBuffer == null || mappedByteBuffer.limit() == 0) {
            DefaultLogger.w("NoMediaRecorder", "mRecordBuffer is invalid.");
            return -1;
        }
        this.mRecordBuffer.position(0);
        byte[] bytes = str.toLowerCase().getBytes();
        while (this.mRecordBuffer.position() + this.mRecordHeader.length < this.mRecordBuffer.limit()) {
            MappedByteBuffer mappedByteBuffer2 = this.mRecordBuffer;
            byte[] bArr = this.mRecordHeader;
            mappedByteBuffer2.get(bArr, 0, bArr.length);
            int readInt = readInt(this.mRecordHeader, 0);
            if (readInt <= 0) {
                MappedByteBuffer mappedByteBuffer3 = this.mRecordBuffer;
                mappedByteBuffer3.position(mappedByteBuffer3.position() - this.mRecordHeader.length);
                return 1;
            } else if (readInt == bytes.length) {
                byte[] bArr2 = new byte[readInt];
                this.mRecordBuffer.get(bArr2, 0, readInt);
                if (StringUtils.equalsIgnoreCase(new String(bytes), new String(bArr2))) {
                    return this.mRecordHeader[4] == 1 ? 0 : 2;
                }
            } else {
                try {
                    MappedByteBuffer mappedByteBuffer4 = this.mRecordBuffer;
                    mappedByteBuffer4.position(mappedByteBuffer4.position() + readInt);
                } catch (IllegalArgumentException unused) {
                    DefaultLogger.e("NoMediaRecorder", "something wrong happened with record file, clear it.");
                    this.mRecordBuffer.position(0);
                    MappedByteBuffer mappedByteBuffer5 = this.mRecordBuffer;
                    mappedByteBuffer5.put(new byte[mappedByteBuffer5.limit()]);
                    this.mRecordBuffer.clear();
                    return -2;
                }
            }
        }
        return 1;
    }

    public boolean match(String str) {
        boolean z;
        synchronized (this.mLock) {
            z = lookup(str) == 0;
        }
        return z;
    }

    public void add(String str) {
        synchronized (this.mLock) {
            int lookup = lookup(str);
            if (lookup == -2 || lookup == 1) {
                writeInt(this.mRecordHeader, 0, str.getBytes().length);
                this.mRecordHeader[4] = 1;
                byte[] bytes = str.getBytes();
                resizeRecordFileIfNeed(this.mRecordHeader, bytes);
                this.mRecordBuffer.put(this.mRecordHeader);
                this.mRecordBuffer.put(bytes);
                DefaultLogger.d("NoMediaRecorder", "add %s.", str);
            } else if (lookup == 2) {
                MappedByteBuffer mappedByteBuffer = this.mRecordBuffer;
                mappedByteBuffer.position((mappedByteBuffer.position() - str.getBytes().length) - 1);
                this.mRecordBuffer.put((byte) 1);
                DefaultLogger.d("NoMediaRecorder", "revalidly %s.", str);
            }
        }
    }

    public final void resizeRecordFileIfNeed(byte[] bArr, byte[] bArr2) {
        try {
            if (this.mRecordBuffer.position() + bArr.length + bArr2.length < this.mRecordFile.length()) {
                return;
            }
            RandomAccessFile randomAccessFile = this.mRecordFile;
            randomAccessFile.setLength(randomAccessFile.length() + 10240);
            this.mRecordBuffer = this.mRecordFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, this.mRecordFile.length());
        } catch (Exception e) {
            DefaultLogger.d("NoMediaRecorder", "failed to resize record file, %s." + e.toString());
        }
    }

    public void remove(String str) {
        synchronized (this.mLock) {
            if (lookup(str) == 0) {
                MappedByteBuffer mappedByteBuffer = this.mRecordBuffer;
                mappedByteBuffer.position((mappedByteBuffer.position() - str.getBytes().length) - 1);
                this.mRecordBuffer.put((byte) 0);
                DefaultLogger.d("NoMediaRecorder", "nullify %s.", str);
            }
        }
    }

    public final int readInt(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16);
    }

    public final void writeInt(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            bArr[i + i3] = (byte) (i2 & 255);
            i2 >>= 8;
        }
    }
}
