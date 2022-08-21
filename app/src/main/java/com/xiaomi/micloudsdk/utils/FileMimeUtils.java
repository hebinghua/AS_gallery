package com.xiaomi.micloudsdk.utils;

import android.util.Log;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: classes3.dex */
public class FileMimeUtils {
    public static final int MAX_END_LENGTH;
    public static final int MAX_HEAD_LENGTH;
    public static final Mime[] MIMES;

    /* loaded from: classes3.dex */
    public static class Mime {
        public final byte[] mEnd;
        public final byte[] mHead;
        public final String mMime;

        public Mime(String str, byte[] bArr, byte[] bArr2) {
            this.mMime = str;
            this.mHead = bArr;
            this.mEnd = bArr2;
        }

        public boolean hasHead() {
            return this.mHead != null;
        }

        public boolean hasEnd() {
            return this.mEnd != null;
        }

        public String getMimeType() {
            return this.mMime;
        }

        public int getHeadLength() {
            return this.mHead.length;
        }

        public int getEndLength() {
            return this.mEnd.length;
        }

        public boolean isHeadRight(byte[] bArr) {
            int length = this.mHead.length;
            for (int i = 0; i < length; i++) {
                if (bArr[i] != this.mHead[i]) {
                    return false;
                }
            }
            return true;
        }

        public boolean isEndRight(byte[] bArr) {
            int length = this.mEnd.length;
            int length2 = bArr.length - length;
            int i = 0;
            while (i < length) {
                if (bArr[length2] != this.mEnd[i]) {
                    return false;
                }
                i++;
                length2++;
            }
            return true;
        }
    }

    static {
        Mime[] mimeArr = {new Mime("image/jpeg", new byte[]{-1, -40}, new byte[]{-1, -39}), new Mime("image/jpeg", new byte[]{-1, -40}, new byte[]{0, 0}), new Mime("image/png", new byte[]{-119, 80, 78, 71, 13, 10, 26, 10}, null), new Mime("image/tga", new byte[]{0, 0, 2, 0, 0}, null), new Mime("image/tga", new byte[]{0, 0, 16, 0, 0}, null), new Mime("image/gif", new byte[]{71, 73, 70, 56, 55, 97}, null), new Mime("image/gif", new byte[]{71, 73, 70, 56, 57, 97}, null), new Mime("image/bmp", new byte[]{66, 77}, null), new Mime("image/tiff", new byte[]{77, 77}, null), new Mime("image/tiff", new byte[]{73, 73}, null), new Mime("video/3gpp", new byte[]{0, 0, 0, 0, 105, 115, 111, 109, 51, 103, 112, 52}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 24, 102, 116, 121, 112, 105, 115, 111, 109}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 24, 102, 116, 121, 112, 109, 112, 52, 50}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 24, 102, 116, 121, 112, 51, 103, 112}, null), new Mime("video/3gp", new byte[]{0, 0, 0, 28, 102, 116, 121, 112, 51, 103, 112, 52}, null), new Mime("video/quicktime", new byte[]{0, 0, 0, 20, 102, 116, 121, 112, 113, 116}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 32, 102, 116, 121, 112, 97, 118, 99, 49}, null), new Mime("video/quicktime", new byte[]{0, 0, 0, 28, 102, 116, 121, 112, 109, 112, 52, 50}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 32, 102, 116, 121, 112, 105, 115, 111, 109}, null)};
        MIMES = mimeArr;
        int i = 0;
        int i2 = 0;
        for (Mime mime : mimeArr) {
            if (mime.hasHead() && mime.getHeadLength() > i) {
                i = mime.getHeadLength();
            }
            if (mime.hasEnd() && mime.getEndLength() > i2) {
                i2 = mime.getEndLength();
            }
        }
        MAX_HEAD_LENGTH = i;
        MAX_END_LENGTH = i2;
    }

    public static String getMimeType(String str) {
        try {
            return rawGetMimeType(str);
        } catch (IOException e) {
            Log.d("FileMimeUtils", "rawGetMimeType", e);
            return null;
        }
    }

    public static String rawGetMimeType(String str) throws IOException {
        Mime[] mimeArr;
        RandomAccessFile randomAccessFile = new RandomAccessFile(str, "r");
        int i = MAX_HEAD_LENGTH;
        byte[] bArr = new byte[i];
        int i2 = MAX_END_LENGTH;
        byte[] bArr2 = new byte[i2];
        long j = 0;
        try {
            randomAccessFile.seek(0L);
            int read = randomAccessFile.read(bArr, 0, i);
            long length = randomAccessFile.length();
            long min = Math.min(i2, length);
            int i3 = (min > 0L ? 1 : (min == 0L ? 0 : -1));
            if (i3 >= 0 && min <= 2147483647L) {
                if (i3 == 0) {
                    Log.d("FileMimeUtils", "endBufferedLength is 0, just return null mime type");
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                        Log.d("FileMimeUtils", "close image file failed.", e);
                    }
                    return null;
                }
                randomAccessFile.seek(length - min);
                if (min == randomAccessFile.read(bArr2, 0, (int) min)) {
                    j = min;
                }
                for (Mime mime : MIMES) {
                    if ((!mime.hasHead() || (read >= mime.getHeadLength() && mime.isHeadRight(bArr))) && (!mime.hasEnd() || (j >= mime.getEndLength() && mime.isEndRight(bArr2)))) {
                        String mimeType = mime.getMimeType();
                        try {
                            randomAccessFile.close();
                        } catch (IOException e2) {
                            Log.d("FileMimeUtils", "close image file failed.", e2);
                        }
                        return mimeType;
                    }
                }
                try {
                    randomAccessFile.close();
                } catch (IOException e3) {
                    Log.d("FileMimeUtils", "close image file failed.", e3);
                }
                return null;
            }
            Log.e("FileMimeUtils", String.format("unexpected error, endBufferedLength: %d, file length: %d", Long.valueOf(min), Long.valueOf(length)));
            try {
                randomAccessFile.close();
            } catch (IOException e4) {
                Log.d("FileMimeUtils", "close image file failed.", e4);
            }
            return null;
        } catch (Throwable th) {
            try {
                randomAccessFile.close();
            } catch (IOException e5) {
                Log.d("FileMimeUtils", "close image file failed.", e5);
            }
            throw th;
        }
    }
}
