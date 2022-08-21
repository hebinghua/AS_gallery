package com.miui.gallery.scanner.core.scanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.model.OwnerItemEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.nio.file.Path;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ParallelProcessingImageScanner implements IMediaScanner<OwnerAlbumEntry, OwnerItemEntry> {
    @Override // com.miui.gallery.scanner.core.scanner.IMediaScanner
    public ScanResult scanFile(Context context, Path path, OwnerAlbumEntry ownerAlbumEntry, ScanTaskConfig scanTaskConfig) {
        ParallelProcessingImage fromPath = ParallelProcessingImage.fromPath(context, path);
        if (fromPath == null) {
            return ScanResult.failed(ScanContracts$ScanResultReason.PARALLEL_PROCESSING_IMAGE_NOT_FOUND).build();
        }
        return fromPath.scan(context);
    }

    /* loaded from: classes2.dex */
    public static class ParallelProcessingImage {
        public static final String[] PROJECTION = {"media_store_id", "media_path", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "mime_type", "date_taken", "jpeg_rotation", "bucket_id", nexExportFormat.TAG_FORMAT_WIDTH, nexExportFormat.TAG_FORMAT_HEIGHT};
        public final int bucketId;
        public final long dateTaken;
        public final int height;
        public final String mediaPath;
        public final long mediaStoreId;
        public final String mimeType;
        public final int orientation;
        public final long size;
        public final int width;

        public ParallelProcessingImage(Cursor cursor) {
            this.mediaStoreId = cursor.getLong(0);
            this.mediaPath = cursor.getString(1);
            this.size = cursor.getLong(2);
            this.mimeType = cursor.getString(3);
            this.dateTaken = cursor.getLong(4);
            this.orientation = cursor.getInt(5);
            this.bucketId = cursor.getInt(6);
            this.width = cursor.getInt(7);
            this.height = cursor.getInt(8);
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x0047, code lost:
            r8.close();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static com.miui.gallery.scanner.core.scanner.ParallelProcessingImageScanner.ParallelProcessingImage fromPath(android.content.Context r8, java.nio.file.Path r9) {
            /*
                r0 = 0
                if (r9 != 0) goto L4
                return r0
            L4:
                java.lang.String r9 = r9.toString()
                boolean r1 = android.text.TextUtils.isEmpty(r9)
                if (r1 == 0) goto Lf
                return r0
            Lf:
                android.net.Uri r3 = com.miui.gallery.photosapi.PhotosOemApi.getQueryProcessingUri(r8)
                java.lang.String[] r4 = com.miui.gallery.scanner.core.scanner.ParallelProcessingImageScanner.ParallelProcessingImage.PROJECTION
                r1 = 1
                java.lang.String[] r6 = new java.lang.String[r1]
                r2 = 0
                r6[r2] = r9
                r7 = 0
                java.lang.String r5 = "media_path=?"
                r2 = r8
                android.database.Cursor r8 = com.miui.gallery.util.SafeDBUtil.queryToCursor(r2, r3, r4, r5, r6, r7)
                if (r8 == 0) goto L45
                int r9 = r8.getCount()     // Catch: java.lang.Throwable -> L3b
                if (r9 != r1) goto L45
                boolean r9 = r8.moveToFirst()     // Catch: java.lang.Throwable -> L3b
                if (r9 != 0) goto L32
                goto L45
            L32:
                com.miui.gallery.scanner.core.scanner.ParallelProcessingImageScanner$ParallelProcessingImage r9 = new com.miui.gallery.scanner.core.scanner.ParallelProcessingImageScanner$ParallelProcessingImage     // Catch: java.lang.Throwable -> L3b
                r9.<init>(r8)     // Catch: java.lang.Throwable -> L3b
                r8.close()
                return r9
            L3b:
                r9 = move-exception
                r8.close()     // Catch: java.lang.Throwable -> L40
                goto L44
            L40:
                r8 = move-exception
                r9.addSuppressed(r8)
            L44:
                throw r9
            L45:
                if (r8 == 0) goto L4a
                r8.close()
            L4a:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.scanner.core.scanner.ParallelProcessingImageScanner.ParallelProcessingImage.fromPath(android.content.Context, java.nio.file.Path):com.miui.gallery.scanner.core.scanner.ParallelProcessingImageScanner$ParallelProcessingImage");
        }

        public ScanResult scan(Context context) {
            DefaultLogger.v("ParallelProcessingImageScanner", "scan parallel processing image: [%s]", toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(this.size));
            contentValues.put("dateModified", Long.valueOf(this.dateTaken));
            contentValues.put("mimeType", this.mimeType);
            String fileName = BaseFileUtils.getFileName(this.mediaPath);
            contentValues.put("title", BaseFileUtils.getFileTitle(fileName));
            contentValues.put("fileName", fileName);
            contentValues.put("dateTaken", Long.valueOf(this.dateTaken));
            contentValues.put("serverType", (Integer) 1);
            contentValues.put("exifImageWidth", Integer.valueOf(this.width));
            contentValues.put("exifImageLength", Integer.valueOf(this.height));
            contentValues.put("exifOrientation", Integer.valueOf(this.orientation));
            contentValues.put("localFlag", (Integer) 7);
            contentValues.put("localFile", this.mediaPath);
            contentValues.put("mixedDateTime", Long.valueOf(this.dateTaken));
            contentValues.put("specialTypeFlags", (Integer) 0);
            contentValues.put("realSize", Long.valueOf(this.size));
            contentValues.put("realDateModified", Long.valueOf(this.dateTaken));
            contentValues.put("sha1", String.valueOf(hashCode()));
            String relativePath = StorageUtils.getRelativePath(context, BaseFileUtils.getParentFolderPath(this.mediaPath));
            OwnerAlbumEntry fromLocalPath = OwnerAlbumEntry.fromLocalPath(context, relativePath);
            if (fromLocalPath == null) {
                fromLocalPath = MediaScannerHelper.queryOrInsertAlbum(context, relativePath);
            }
            contentValues.put("localGroupId", Long.valueOf(fromLocalPath.mId));
            SafeDBUtil.safeInsert(context, GalleryContract.Cloud.CLOUD_URI, contentValues);
            return ScanResult.success(ScanContracts$ScanResultReason.DEFAULT).build();
        }

        public String toString() {
            return String.format(Locale.US, "mediaStoreId=%d, mediaPath='%s', size=%d, mimeType=%s, dateTaken=%d, orientation=%d, bucketId=%d, width=%d, height=%d", Long.valueOf(this.mediaStoreId), this.mediaPath, Long.valueOf(this.size), this.mimeType, Long.valueOf(this.dateTaken), Integer.valueOf(this.orientation), Integer.valueOf(this.bucketId), Integer.valueOf(this.width), Integer.valueOf(this.height));
        }
    }
}
