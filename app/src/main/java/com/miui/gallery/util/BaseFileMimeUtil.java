package com.miui.gallery.util;

import android.text.TextUtils;
import com.android.internal.MediaFileCompat;
import com.miui.gallery.util.MediaFile;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: classes2.dex */
public class BaseFileMimeUtil {
    public static final int MAX_END_LENGTH;
    public static final int MAX_HEAD_LENGTH;
    public static final Mime[] IMAGE_MIMES = {new Mime("image/jpeg", new byte[]{-1, -40}, new byte[]{-1, -39}), new Mime("image/jpeg", new byte[]{-1, -40}, new byte[]{0, 0}), new Mime("image/png", new byte[]{-119, 80, 78, 71, 13, 10, 26, 10}, null), new Mime("image/tga", new byte[]{0, 0, 2, 0, 0}, null), new Mime("image/tga", new byte[]{0, 0, 16, 0, 0}, null), new Mime("image/gif", new byte[]{71, 73, 70, 56, 55, 97}, null), new Mime("image/gif", new byte[]{71, 73, 70, 56, 57, 97}, null), new Mime("image/bmp", new byte[]{66, 77}, null), new Mime("image/tiff", new byte[]{77, 77}, null), new Mime("image/tiff", new byte[]{73, 73}, null), new Mime("image/webp", new byte[]{82, 73}, null)};
    public static final Mime[] VIDEO_MIMES = {new Mime("video/mp4", new byte[]{0, 0, 0, 28, 102, 116, 121, 112, 110, 118, 114, 49, 0, 1, 0, 1}, null), new Mime("video/3gpp", new byte[]{0, 0, 0, 0, 105, 115, 111, 109, 51, 103, 112, 52}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 24, 102, 116, 121, 112, 105, 115, 111, 109}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 24, 102, 116, 121, 112, 109, 112, 52, 50}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 24, 102, 116, 121, 112, 51, 103, 112}, null), new Mime("video/3gp", new byte[]{0, 0, 0, 28, 102, 116, 121, 112, 51, 103, 112, 52}, null), new Mime("video/quicktime", new byte[]{0, 0, 0, 20, 102, 116, 121, 112, 113, 116}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 32, 102, 116, 121, 112, 97, 118, 99, 49}, null), new Mime("video/quicktime", new byte[]{0, 0, 0, 28, 102, 116, 121, 112, 109, 112, 52, 50}, null), new Mime("video/mp4", new byte[]{0, 0, 0, 32, 102, 116, 121, 112, 105, 115, 111, 109}, null)};

    /* loaded from: classes2.dex */
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
        Mime[] mimes;
        int i = 0;
        int i2 = 0;
        for (Mime mime : getMimes()) {
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

    public static String rawGetMimeType(String str, Mime[] mimeArr) throws IOException {
        int i = MAX_HEAD_LENGTH;
        byte[] bArr = new byte[i];
        int i2 = MAX_END_LENGTH;
        byte[] bArr2 = new byte[i2];
        RandomAccessFile randomAccessFile = null;
        try {
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(str, "r");
            long j = 0;
            try {
                randomAccessFile2.seek(0L);
                int read = randomAccessFile2.read(bArr, 0, i);
                long length = randomAccessFile2.length();
                long min = Math.min(i2, length);
                int i3 = (min > 0L ? 1 : (min == 0L ? 0 : -1));
                if (i3 < 0 || min > 2147483647L) {
                    DefaultLogger.e("BaseFileMimeUtil", String.format("unexpected error, endBufferedLength: %d, file length: %d", Long.valueOf(min), Long.valueOf(length)));
                    BaseMiscUtil.closeSilently(randomAccessFile2);
                    return null;
                } else if (i3 == 0) {
                    DefaultLogger.d("BaseFileMimeUtil", "endBufferedLength is 0, just return null mime type");
                    BaseMiscUtil.closeSilently(randomAccessFile2);
                    return null;
                } else {
                    randomAccessFile2.seek(length - min);
                    if (min == randomAccessFile2.read(bArr2, 0, (int) min)) {
                        j = min;
                    }
                    for (Mime mime : mimeArr) {
                        if ((!mime.hasHead() || (read >= mime.getHeadLength() && mime.isHeadRight(bArr))) && (!mime.hasEnd() || (j >= mime.getEndLength() && mime.isEndRight(bArr2)))) {
                            String mimeType = mime.getMimeType();
                            BaseMiscUtil.closeSilently(randomAccessFile2);
                            return mimeType;
                        }
                    }
                    BaseMiscUtil.closeSilently(randomAccessFile2);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                randomAccessFile = randomAccessFile2;
                BaseMiscUtil.closeSilently(randomAccessFile);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static Mime[] getMimes() {
        int length = IMAGE_MIMES.length + VIDEO_MIMES.length;
        Mime[] mimeArr = new Mime[length];
        for (int i = 0; i < length; i++) {
            Mime[] mimeArr2 = IMAGE_MIMES;
            if (i < mimeArr2.length) {
                mimeArr[i] = mimeArr2[i];
            } else {
                mimeArr[i] = VIDEO_MIMES[i - mimeArr2.length];
            }
        }
        return mimeArr;
    }

    public static String getMimeTypeByParseFile(String str) {
        try {
            return rawGetMimeType(str, getMimes());
        } catch (IOException e) {
            DefaultLogger.w("BaseFileMimeUtil", e);
            return "*/*";
        }
    }

    public static String getMimeType(String str) {
        String mimeTypeForFile = !TextUtils.isEmpty(str) ? MediaFileCompat.getMimeTypeForFile(str) : "*/*";
        return TextUtils.isEmpty(mimeTypeForFile) ? "*/*" : mimeTypeForFile;
    }

    public static boolean isVideoFromMimeType(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("video");
    }

    public static boolean isImageFromMimeType(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("image");
    }

    public static boolean isMediaTypeFromPath(String str) {
        String mimeType = getMimeType(str);
        if ("*/*".equals(mimeType)) {
            mimeType = getMimeTypeByParseFile(str);
        }
        return isImageFromMimeType(mimeType) || isVideoFromMimeType(mimeType);
    }

    public static boolean isJpegImageFromMimeType(String str) {
        return TextUtils.equals(str, "image/jpeg");
    }

    public static boolean isPngImageFromMimeType(String str) {
        return TextUtils.equals(str, "image/png");
    }

    public static boolean isRawFromMimeType(String str) {
        return TextUtils.equals(str, "image/x-adobe-dng");
    }

    public static boolean isGifFromMimeType(String str) {
        return TextUtils.equals(str, "image/gif");
    }

    public static boolean isJpgFromMimeType(String str) {
        return TextUtils.equals(str, "image/jpeg");
    }

    public static boolean isBmpFromMimeType(String str) {
        return TextUtils.equals(str, "image/x-ms-bmp");
    }

    public static boolean isWebpFromMimeType(String str) {
        return TextUtils.equals(str, "image/webp");
    }

    public static boolean isWbmpFromMimeType(String str) {
        return TextUtils.equals(str, "image/vnd.wap.wbmp");
    }

    public static boolean isHeifMimeType(String str) {
        return TextUtils.equals(str, "image/heif") || TextUtils.equals(str, "image/heic");
    }

    public static boolean isPdfFromMimeType(String str) {
        return TextUtils.equals(str, "application/pdf");
    }

    public static boolean isHeifMimeTypeByPath(String str) {
        MediaFile.MediaFileType fileType = MediaFile.getFileType(str);
        return fileType != null && fileType.fileType == 38;
    }

    public static boolean isMp4MimeType(String str) {
        return TextUtils.equals(str, "video/mp4");
    }

    public static boolean hasExif(String str) {
        String mimeTypeForFile = MediaFileCompat.getMimeTypeForFile(str);
        return isJpgFromMimeType(mimeTypeForFile) || isRawFromMimeType(mimeTypeForFile) || isHeifMimeType(mimeTypeForFile);
    }

    public static boolean isExifSupportModification(String str) {
        return isJpgFromMimeType(MediaFileCompat.getMimeTypeForFile(str));
    }
}
