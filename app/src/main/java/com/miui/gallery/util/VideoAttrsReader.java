package com.miui.gallery.util;

import android.text.TextUtils;
import android.text.format.Time;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class VideoAttrsReader {
    public final String mPath;
    public String mTitle;
    public int mWidth = -1;
    public int mHeight = -1;
    public long mDuration = -1;
    public long mDateTaken = -1;
    public double mLatitude = SearchStatUtils.POW;
    public double mLongitude = SearchStatUtils.POW;
    public int mOrientation = 0;

    /* loaded from: classes2.dex */
    public static class VideoAttrsUnretrievableException extends IOException {
        private static final long serialVersionUID = -4165084914066776321L;

        public VideoAttrsUnretrievableException(String str, Throwable th) {
            super(str, th);
        }
    }

    public static VideoAttrsReader read(String str) throws IOException {
        return new VideoAttrsReader(str);
    }

    public VideoAttrsReader(String str) throws IOException {
        this.mPath = str;
        initByMediaMediaPlayer(str);
    }

    public String getTitle() {
        return this.mTitle;
    }

    public int getVideoWidth() {
        return this.mWidth;
    }

    public int getVideoHeight() {
        return this.mHeight;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public long getDateTaken() {
        return this.mDateTaken;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public double[] getLatLong() {
        return new double[]{this.mLatitude, this.mLongitude};
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x00e9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void initByMediaMediaPlayer(java.lang.String r9) throws java.io.IOException {
        /*
            r8 = this;
            java.lang.String r0 = "VideoAttrsReader"
            r1 = 0
            android.media.MediaMetadataRetriever r2 = new android.media.MediaMetadataRetriever     // Catch: java.lang.Throwable -> Ldb java.lang.RuntimeException -> Lde
            r2.<init>()     // Catch: java.lang.Throwable -> Ldb java.lang.RuntimeException -> Lde
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r3 = com.miui.gallery.storage.StorageSolutionProvider.get()     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            java.lang.String r4 = "camera_first"
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r5 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.QUERY     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            java.lang.String r6 = "initByMediaMediaPlayer"
            java.lang.String r6 = com.miui.gallery.util.FileHandleRecordHelper.appendInvokerTag(r0, r6)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            androidx.documentfile.provider.DocumentFile r3 = r3.getDocumentFile(r4, r9, r5, r6)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            if (r3 == 0) goto Lc5
            boolean r4 = r3.exists()     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            if (r4 != 0) goto L24
            goto Lc5
        L24:
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r4 = com.miui.gallery.storage.StorageSolutionProvider.get()     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            java.lang.String r5 = "r"
            android.os.ParcelFileDescriptor r1 = r4.openFileDescriptor(r3, r5)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            if (r1 != 0) goto L3c
            java.lang.String r3 = "pfd is null"
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r3)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r2.release()     // Catch: java.lang.RuntimeException -> L38
        L38:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            return
        L3c:
            java.io.FileDescriptor r0 = r1.getFileDescriptor()     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r2.setDataSource(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r0 = 7
            java.lang.String r0 = r2.extractMetadata(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r8.mTitle = r0     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r0 = 18
            java.lang.String r0 = r2.extractMetadata(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            int r0 = r8.parseIntSafely(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r8.mWidth = r0     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r0 = 19
            java.lang.String r0 = r2.extractMetadata(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            int r0 = r8.parseIntSafely(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r8.mHeight = r0     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r0 = 9
            java.lang.String r0 = r2.extractMetadata(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            long r3 = r8.parseLongSafely(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r8.mDuration = r3     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r0 = 5
            java.lang.String r0 = r2.extractMetadata(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            long r3 = r8.calculateTaken(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r8.mDateTaken = r3     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r0 = 24
            java.lang.String r0 = r2.extractMetadata(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            int r0 = r8.parseIntSafely(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            int r0 = com.miui.gallery.util.ExifUtil.degreesToExifOrientation(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r8.mOrientation = r0     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r0 = 23
            java.lang.String r0 = r2.extractMetadata(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            if (r0 == 0) goto L94
            r8.getLatLong(r0)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
        L94:
            r2.release()     // Catch: java.lang.RuntimeException -> L97
        L97:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            java.lang.String r0 = r8.mTitle
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto Lac
            java.lang.String r0 = com.miui.gallery.util.BaseFileUtils.getFileName(r9)
            java.lang.String r0 = com.miui.gallery.util.BaseFileUtils.getFileTitle(r0)
            r8.mTitle = r0
        Lac:
            long r0 = r8.mDateTaken
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 > 0) goto Lbf
            java.io.File r0 = new java.io.File
            r0.<init>(r9)
            long r0 = r0.lastModified()
            r8.mDateTaken = r0
        Lbf:
            java.lang.String r9 = "final result"
            r8.dump(r9)
            return
        Lc5:
            java.lang.String r3 = "file is null"
            com.miui.gallery.util.logger.DefaultLogger.e(r0, r3)     // Catch: java.lang.Throwable -> Ld1 java.lang.RuntimeException -> Ld6
            r2.release()     // Catch: java.lang.RuntimeException -> Lcd
        Lcd:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r1)
            return
        Ld1:
            r9 = move-exception
            r7 = r2
            r2 = r1
            r1 = r7
            goto Le7
        Ld6:
            r0 = move-exception
            r7 = r2
            r2 = r1
            r1 = r7
            goto Le0
        Ldb:
            r9 = move-exception
            r2 = r1
            goto Le7
        Lde:
            r0 = move-exception
            r2 = r1
        Le0:
            com.miui.gallery.util.VideoAttrsReader$VideoAttrsUnretrievableException r3 = new com.miui.gallery.util.VideoAttrsReader$VideoAttrsUnretrievableException     // Catch: java.lang.Throwable -> Le6
            r3.<init>(r9, r0)     // Catch: java.lang.Throwable -> Le6
            throw r3     // Catch: java.lang.Throwable -> Le6
        Le6:
            r9 = move-exception
        Le7:
            if (r1 == 0) goto Lec
            r1.release()     // Catch: java.lang.RuntimeException -> Lec
        Lec:
            com.miui.gallery.util.BaseMiscUtil.closeSilently(r2)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.VideoAttrsReader.initByMediaMediaPlayer(java.lang.String):void");
    }

    public final void getLatLong(String str) {
        DefaultLogger.d("VideoAttrsReader", "location:%s", str);
        Matcher matcher = Pattern.compile("([+-]\\d+\\.\\d+)([+-]\\d+\\.\\d+)").matcher(str);
        if (matcher.find()) {
            this.mLatitude = Double.parseDouble(matcher.group(1));
            this.mLongitude = Double.parseDouble(matcher.group(2));
        }
    }

    public final long calculateTaken(String str) {
        Date date;
        if (!TextUtils.isEmpty(str)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                date = simpleDateFormat.parse(str);
            } catch (Exception unused) {
                DefaultLogger.i("VideoAttrsReader", "simple format error %s", str);
                return new Time(str).toMillis(true);
            }
        } else {
            date = null;
        }
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }

    public final int parseIntSafely(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public final long parseLongSafely(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return -1L;
        }
    }

    public final void dump(String str) {
        DefaultLogger.d("VideoAttrsReader", "msg=" + str + "\r\nfile=" + this.mPath + "\r\ntitle=" + this.mTitle + "\r\nwidth=" + this.mWidth + "\r\nheight=" + this.mHeight + "\r\norientation=" + this.mOrientation + "\r\nduration=" + this.mDuration + "\r\ndatataken=" + this.mDateTaken + "\r\n");
    }
}
