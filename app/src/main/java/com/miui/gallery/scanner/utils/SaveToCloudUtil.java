package com.miui.gallery.scanner.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.Action;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.bulkoperator.IBulkInserter;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.model.SaveToCloud;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class SaveToCloudUtil {
    public static ScanResult saveToCloudDB(Context context, SaveParams saveParams) {
        if (saveParams == null) {
            return ScanResult.failed(ScanContracts$ScanResultReason.INVALID_SAVE_PARAMS).build();
        }
        SaveToCloud fromSaveParams = SaveToCloud.fromSaveParams(saveParams);
        if (fromSaveParams == null) {
            return ScanResult.failed(ScanContracts$ScanResultReason.PARSE_SAVE_PARAMS_FAILED).build();
        }
        if (IncompatibleMediaType.isUnsupportedMediaType(fromSaveParams.mMimeType)) {
            return ScanResult.failed(ScanContracts$ScanResultReason.UNSUPPORTED_MEDIA_TYPE).build();
        }
        SystemClock.uptimeMillis();
        ScanResult prepareInsert = prepareInsert(context, fromSaveParams, saveParams);
        if (saveParams.getInserter() == null && ScanContracts$ScanResultReason.UNSYNCED_MEDIA_UPDATED == prepareInsert.getReasonCode() && saveParams.isAlbumSyncable()) {
            SyncUtil.requestSync(context);
        }
        return prepareInsert.getReasonCode() == ScanContracts$ScanResultReason.DEFAULT ? insert(context, fromSaveParams, saveParams) : prepareInsert;
    }

    public static boolean needCheckExifSha1(String str) {
        return !TextUtils.isEmpty(str) && str.endsWith(".jpg");
    }

    public static boolean checkFileSize(SaveToCloud saveToCloud) {
        return saveToCloud.mSize > CloudUtils.getMinFileSizeLimit(saveToCloud.mMimeType);
    }

    public static ScanResult prepareInsert(Context context, SaveToCloud saveToCloud, SaveParams saveParams) {
        if (!checkFileSize(saveToCloud)) {
            return ScanResult.failed(ScanContracts$ScanResultReason.FILE_TOO_SMALL).build();
        }
        if (!saveParams.isCredible()) {
            ScanResult scanResult = null;
            if (saveParams.getDeDupStrategy() != null) {
                scanResult = saveParams.getDeDupStrategy().deDup(context, saveToCloud, saveParams);
            }
            if (scanResult != null) {
                return scanResult;
            }
        }
        if (saveParams.isSaveToSecretAlbum() && saveToCloud.mUbiEntry == null) {
            tryEncryptFilePathForSecret(saveToCloud);
        }
        return ScanResult.success(ScanContracts$ScanResultReason.DEFAULT).build();
    }

    public static void tryEncryptFilePathForSecret(SaveToCloud saveToCloud) {
        String encodeFileName = CloudUtils.SecretAlbumUtils.encodeFileName(saveToCloud.mName, BaseFileMimeUtil.isVideoFromMimeType(saveToCloud.mMimeType));
        if (!TextUtils.equals(encodeFileName, saveToCloud.mName)) {
            String str = saveToCloud.mPath;
            File file = new File(BaseFileUtils.getParentFolderPath(str), encodeFileName);
            if (!StorageSolutionProvider.get().moveFile(str, file.getAbsolutePath(), FileHandleRecordHelper.appendInvokerTag("SaveToCloudUtil", "tryEncryptFilePathForSecret"))) {
                return;
            }
            saveToCloud.mPath = file.getAbsolutePath();
            saveToCloud.mLastModify = file.lastModified();
        }
    }

    public static Uri getWriteUri(SaveParams saveParams) {
        if (ShareAlbumHelper.isOtherShareAlbumId(saveParams.getAlbumId())) {
            return GalleryContract.ShareImage.SHARE_URI;
        }
        if (saveParams.isBulkNotify()) {
            if (saveParams.isValidSyncValue()) {
                return GalleryContract.CloudWriteBulkNotify.CLOUD_WRITE_BULK_NOTIFY_URI.buildUpon().appendQueryParameter("URI_PARAM_REQUEST_SYNC", String.valueOf(saveParams.isAlbumSyncable())).build();
            }
            return GalleryContract.CloudWriteBulkNotify.CLOUD_WRITE_BULK_NOTIFY_URI;
        } else if (saveParams.isValidSyncValue()) {
            return GalleryContract.Cloud.CLOUD_URI.buildUpon().appendQueryParameter("URI_PARAM_REQUEST_SYNC", String.valueOf(saveParams.isAlbumSyncable())).build();
        } else {
            return GalleryContract.Cloud.CLOUD_URI;
        }
    }

    public static Uri getUbiSubUri(long j) {
        if (ShareAlbumHelper.isOtherShareAlbumId(j)) {
            return GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI.buildUpon().appendQueryParameter("URI_PARAM_REQUEST_SYNC", String.valueOf(false)).build();
        }
        return GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI.buildUpon().appendQueryParameter("URI_PARAM_REQUEST_SYNC", String.valueOf(false)).build();
    }

    public static Uri insertMedia(Context context, SaveParams saveParams, ContentValues contentValues) {
        return SafeDBUtil.safeInsert(context, getWriteUri(saveParams), contentValues);
    }

    public static void insertMedia(Context context, IBulkInserter iBulkInserter, ContentValues contentValues) {
        iBulkInserter.insert(context, contentValues);
    }

    public static ScanResult insertImmediately(Context context, SaveToCloud saveToCloud, SaveParams saveParams) {
        long albumId;
        SystemClock.uptimeMillis();
        if (ShareAlbumHelper.isOtherShareAlbumId(saveParams.getAlbumId())) {
            albumId = ShareAlbumHelper.getOriginalAlbumId(saveParams.getAlbumId());
        } else {
            albumId = saveParams.getAlbumId();
        }
        ContentValues values = toValues(context, saveToCloud, saveParams, albumId);
        SystemClock.uptimeMillis();
        Uri insertMedia = insertMedia(context, saveParams, values);
        if (insertMedia == null) {
            return ScanResult.failed(ScanContracts$ScanResultReason.INSERT_FAILED).build();
        }
        long parseId = ContentUris.parseId(insertMedia);
        ScanResult.Builder success = ScanResult.success(ScanContracts$ScanResultReason.DEFAULT);
        if (ShareAlbumHelper.isOtherShareAlbumId(saveParams.getAlbumId())) {
            parseId = ShareMediaManager.convertToMediaId(parseId);
        }
        return success.setMediaId(parseId).build();
    }

    public static ScanResult insert(Context context, SaveToCloud saveToCloud, SaveParams saveParams) {
        DefaultLogger.fd("SaveToCloudUtil", "bulk insert %s", saveToCloud.mPath);
        if (saveToCloud.mUbiEntry != null) {
            return insertUbi(context, saveToCloud, saveParams);
        }
        if (saveParams.getInserter() == null || ShareAlbumHelper.isOtherShareAlbumId(saveParams.getAlbumId())) {
            return insertImmediately(context, saveToCloud, saveParams);
        }
        SystemClock.uptimeMillis();
        insertMedia(context, saveParams.getInserter(), toValues(context, saveToCloud, saveParams, saveParams.getAlbumId()));
        return ScanResult.success(ScanContracts$ScanResultReason.BULK_INSERT).build();
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.scanner.core.task.eventual.ScanResult insertUbi(android.content.Context r10, com.miui.gallery.scanner.core.model.SaveToCloud r11, com.miui.gallery.scanner.core.model.SaveParams r12) {
        /*
            long r0 = r12.getAlbumId()
            android.content.ContentValues r0 = toValues(r10, r11, r12, r0)
            com.miui.gallery.scanner.core.model.UbifocusEntry r1 = r11.mUbiEntry
            java.util.List<com.miui.gallery.data.LocalUbifocus$SubFile> r1 = r1.mSubFiles
            int r1 = r1.size()
            int r2 = r1 + (-1)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.String r3 = "ubiSubImageCount"
            r0.put(r3, r2)
            com.miui.gallery.scanner.core.model.UbifocusEntry r2 = r11.mUbiEntry
            int r2 = r2.mOuterIndexForCloud
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.String r3 = "ubiFocusIndex"
            r0.put(r3, r2)
            com.miui.gallery.scanner.core.model.UbifocusEntry r2 = r11.mUbiEntry
            int r2 = r2.mMainIndexForCloud
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.String r3 = "ubiSubIndex"
            r0.put(r3, r2)
            android.net.Uri r0 = insertMedia(r10, r12, r0)
            if (r0 != 0) goto L46
            com.miui.gallery.scanner.core.ScanContracts$ScanResultReason r10 = com.miui.gallery.scanner.core.ScanContracts$ScanResultReason.INSERT_FAILED
            com.miui.gallery.scanner.core.task.eventual.ScanResult$Builder r10 = com.miui.gallery.scanner.core.task.eventual.ScanResult.failed(r10)
            com.miui.gallery.scanner.core.task.eventual.ScanResult r10 = r10.build()
            return r10
        L46:
            long r4 = android.content.ContentUris.parseId(r0)
            com.miui.gallery.scanner.core.model.UbifocusEntry r0 = r11.mUbiEntry
            java.util.List<com.miui.gallery.data.LocalUbifocus$SubFile> r0 = r0.mSubFiles
            java.util.Iterator r0 = r0.iterator()
        L52:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto Lf6
            java.lang.Object r2 = r0.next()
            com.miui.gallery.data.LocalUbifocus$SubFile r2 = (com.miui.gallery.data.LocalUbifocus.SubFile) r2
            java.io.File r6 = new java.io.File
            java.lang.String r7 = r2.getFilePath()
            r6.<init>(r7)
            boolean r7 = r6.exists()
            if (r7 != 0) goto L6f
            goto Lf6
        L6f:
            int r7 = r2.mIndex
            int r8 = com.miui.gallery.data.LocalUbifocus.getMainFileIndex()
            if (r7 != r8) goto L78
            goto L52
        L78:
            java.lang.String r7 = r2.getFilePath()
            r11.mPath = r7
            java.lang.String r7 = com.miui.gallery.util.BaseFileUtils.getFileName(r7)
            r11.mName = r7
            java.lang.String r7 = com.miui.gallery.util.BaseFileUtils.getFileTitle(r7)
            r11.mTitle = r7
            long r7 = r6.lastModified()
            r11.mLastModify = r7
            long r6 = r6.length()
            r11.mSize = r6
            java.lang.String r6 = r11.mName
            boolean r6 = needCheckExifSha1(r6)
            r7 = 0
            if (r6 == 0) goto Lb2
            java.lang.String r6 = r11.mPath     // Catch: java.lang.Exception -> Lac
            com.miui.gallery.util.ExifUtil$UserCommentData r6 = com.miui.gallery.util.ExifUtil.getUserCommentData(r6)     // Catch: java.lang.Exception -> Lac
            if (r6 == 0) goto Lb2
            java.lang.String r6 = r6.getSha1()     // Catch: java.lang.Exception -> Lac
            goto Lb3
        Lac:
            r6 = move-exception
            java.lang.String r8 = "SaveToCloudUtil"
            com.miui.gallery.util.logger.DefaultLogger.w(r8, r6)
        Lb2:
            r6 = r7
        Lb3:
            boolean r8 = android.text.TextUtils.isEmpty(r6)
            if (r8 == 0) goto Lc3
            r6 = 0
            r11.mIsExifSha1 = r6
            java.lang.String r6 = r11.mPath
            java.lang.String r6 = com.miui.gallery.util.FileUtils.getSha1(r6)
            goto Lc6
        Lc3:
            r8 = 1
            r11.mIsExifSha1 = r8
        Lc6:
            r11.mSha1 = r6
            r11.mUbiEntry = r7
            long r6 = r12.getAlbumId()
            android.content.ContentValues r6 = toValues(r10, r11, r12, r6)
            int r2 = r2.mIndex
            int r2 = com.miui.gallery.data.UbiIndexMapper.localToCloud(r2, r1)
            long r7 = (long) r2
            java.lang.Long r2 = java.lang.Long.valueOf(r4)
            java.lang.String r9 = "ubiLocalId"
            r6.put(r9, r2)
            java.lang.Long r2 = java.lang.Long.valueOf(r7)
            r6.put(r3, r2)
            long r7 = r12.getAlbumId()
            android.net.Uri r2 = getUbiSubUri(r7)
            com.miui.gallery.util.SafeDBUtil.safeInsert(r10, r2, r6)
            goto L52
        Lf6:
            com.miui.gallery.scanner.core.ScanContracts$ScanResultReason r10 = com.miui.gallery.scanner.core.ScanContracts$ScanResultReason.DEFAULT
            com.miui.gallery.scanner.core.task.eventual.ScanResult$Builder r10 = com.miui.gallery.scanner.core.task.eventual.ScanResult.success(r10)
            com.miui.gallery.scanner.core.task.eventual.ScanResult$Builder r10 = r10.setMediaId(r4)
            com.miui.gallery.scanner.core.task.eventual.ScanResult r10 = r10.build()
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.scanner.utils.SaveToCloudUtil.insertUbi(android.content.Context, com.miui.gallery.scanner.core.model.SaveToCloud, com.miui.gallery.scanner.core.model.SaveParams):com.miui.gallery.scanner.core.task.eventual.ScanResult");
    }

    public static ContentValues toValues(Context context, SaveToCloud saveToCloud, SaveParams saveParams, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", Integer.valueOf(saveParams.getLocalFlag()));
        contentValues.put("localGroupId", Long.valueOf(j));
        if (BaseFileMimeUtil.isImageFromMimeType(saveToCloud.mMimeType)) {
            contentValues.put("serverType", (Integer) 1);
            putValuesForImage(saveToCloud.mPath, contentValues, saveToCloud.mSpecifiedTaken, saveParams.getImageValueCalculator());
        } else if (BaseFileMimeUtil.isVideoFromMimeType(saveToCloud.mMimeType)) {
            contentValues.put("serverType", (Integer) 2);
            putValuesForVideo(context, saveToCloud.mPath, saveToCloud.mSize, contentValues, saveToCloud.mSpecifiedTaken, saveParams.getVideoValueCalculator());
        }
        contentValues.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(saveToCloud.mSize));
        long j2 = saveToCloud.mLastModify;
        if (j2 <= 1194192000000L) {
            j2 = contentValues.getAsLong("dateTaken").longValue();
        }
        long j3 = saveToCloud.mSpecifiedModified;
        if (j3 > 0) {
            j2 = j3;
        }
        contentValues.put("dateModified", Long.valueOf(j2));
        contentValues.put("realSize", Long.valueOf(saveParams.getFileState() == null ? saveToCloud.mSize : saveParams.getFileState().size));
        contentValues.put("realDateModified", Long.valueOf(saveParams.getFileState() == null ? saveToCloud.mLastModify : saveParams.getFileState().modified));
        contentValues.put("mimeType", saveToCloud.mMimeType);
        contentValues.put("title", saveToCloud.mTitle);
        contentValues.put("fileName", saveToCloud.mName);
        contentValues.put("localFile", saveToCloud.mPath);
        if (saveToCloud.mIsExifSha1) {
            contentValues.put("sha1", saveToCloud.mSha1);
            contentValues.put("thumbnailFile", saveToCloud.mPath);
        }
        LocationManager.getInstance().appendExtraGpsInfo(saveToCloud.mPath, contentValues);
        return contentValues;
    }

    public static void putValuesForImage(String str, ContentValues contentValues, long j, AbsImageValueCalculator absImageValueCalculator) {
        putValuesForImage(str, contentValues, j, false, absImageValueCalculator);
    }

    public static void putValuesForImage(String str, ContentValues contentValues, long j, boolean z, AbsImageValueCalculator absImageValueCalculator) {
        try {
            contentValues.put("sha1", absImageValueCalculator.calcSha1());
            long lastModified = new File(str).lastModified();
            if (BaseFileMimeUtil.hasExif(str)) {
                contentValues.put("exifImageWidth", Integer.valueOf(absImageValueCalculator.calcExifImageWidth()));
                contentValues.put("exifImageLength", Integer.valueOf(absImageValueCalculator.calcExifImageHeight()));
                contentValues.put("exifOrientation", Integer.valueOf(absImageValueCalculator.calcExifOrientation()));
                contentValues.put("exifMake", absImageValueCalculator.calcExifMake());
                contentValues.put("exifModel", absImageValueCalculator.calcExifModel());
                contentValues.put("exifFlash", absImageValueCalculator.calcExifFlash());
                contentValues.put("exifExposureTime", absImageValueCalculator.calcExifExposureTime());
                contentValues.put("exifFNumber", absImageValueCalculator.calcExifAperture());
                contentValues.put("exifISOSpeedRatings", absImageValueCalculator.calcExifISO());
                contentValues.put("exifGPSAltitude", absImageValueCalculator.calcExifGpsAltitude());
                contentValues.put("exifGPSAltitudeRef", Integer.valueOf(absImageValueCalculator.calcExifGpsAltitudeRef()));
                contentValues.put("exifWhiteBalance", Integer.valueOf(absImageValueCalculator.calcExifWhiteBalance()));
                contentValues.put("exifFocalLength", absImageValueCalculator.calcExifFocalLength());
                contentValues.put("exifGPSProcessingMethod", absImageValueCalculator.calcExifGpsProcessingMethod());
                Map<String, String> calcExifGpsLocation = absImageValueCalculator.calcExifGpsLocation();
                contentValues.put("exifGPSLatitudeRef", calcExifGpsLocation.get("GPSLatitudeRef"));
                contentValues.put("exifGPSLongitudeRef", calcExifGpsLocation.get("GPSLongitudeRef"));
                contentValues.put("exifGPSLatitude", calcExifGpsLocation.get("GPSLatitude"));
                contentValues.put("exifGPSLongitude", calcExifGpsLocation.get("GPSLongitude"));
                contentValues.put("exifGPSTimeStamp", absImageValueCalculator.calcExifGpsTimeStamp(j, z));
                contentValues.put("exifGPSDateStamp", absImageValueCalculator.calcExifGpsDateStamp(j, z));
                contentValues.put("exifDateTime", absImageValueCalculator.calcExifDateTime(j, z));
                long calcDateTaken = absImageValueCalculator.calcDateTaken(lastModified, j, z);
                contentValues.put("dateTaken", Long.valueOf(calcDateTaken));
                contentValues.put("mixedDateTime", Long.valueOf(calcDateTaken));
                long calcSpecialTypeFlags = absImageValueCalculator.calcSpecialTypeFlags();
                contentValues.put("specialTypeFlags", Long.valueOf(calcSpecialTypeFlags));
                contentValues.put("location", absImageValueCalculator.calcScreenshotsLocation());
                ExtraPhotoSDK.sendNewPhotoStatic(calcSpecialTypeFlags);
            } else {
                BitmapFactory.Options bitmapSize = miuix.graphics.BitmapFactory.getBitmapSize(str);
                int i = bitmapSize.outWidth;
                int i2 = bitmapSize.outHeight;
                contentValues.put("exifImageWidth", Integer.valueOf(i));
                contentValues.put("exifImageLength", Integer.valueOf(i2));
                contentValues.put("exifOrientation", (Integer) 0);
                if (j <= 0) {
                    j = lastModified;
                }
                contentValues.put("dateTaken", Long.valueOf(j));
                contentValues.put("mixedDateTime", Long.valueOf(j));
            }
        } catch (Exception e) {
            DefaultLogger.e("SaveToCloudUtil", "media scanner exif error %s", e);
        }
    }

    public static void putValuesForVideo(Context context, String str, long j, ContentValues contentValues, long j2, AbsVideoValueCalculator absVideoValueCalculator) {
        try {
            contentValues.put("sha1", absVideoValueCalculator.calcSha1());
            contentValues.put("duration", Long.valueOf(absVideoValueCalculator.calcDuration()));
            contentValues.put("exifImageWidth", Integer.valueOf(absVideoValueCalculator.calcExifImageWidth()));
            contentValues.put("exifImageLength", Integer.valueOf(absVideoValueCalculator.calcExifImageHeight()));
            contentValues.put("exifOrientation", Integer.valueOf(absVideoValueCalculator.calcExifOrientation()));
            long calcDateTaken = absVideoValueCalculator.calcDateTaken(-1L, j2, false);
            contentValues.put("dateTaken", Long.valueOf(calcDateTaken));
            contentValues.put("mixedDateTime", Long.valueOf(calcDateTaken));
            Map<String, String> calcExifGpsLocation = absVideoValueCalculator.calcExifGpsLocation();
            contentValues.put("exifGPSLatitude", calcExifGpsLocation.get("GPSLatitude"));
            contentValues.put("exifGPSLongitude", calcExifGpsLocation.get("GPSLongitude"));
            long calcSpecialTypeFlags = absVideoValueCalculator.calcSpecialTypeFlags();
            contentValues.put("specialTypeFlags", Long.valueOf(calcSpecialTypeFlags));
            ExtraPhotoSDK.sendNewPhotoStatic(calcSpecialTypeFlags);
            statVideoInfo(context, absVideoValueCalculator.calcDuration(), str, j);
        } catch (Exception e) {
            DefaultLogger.e("SaveToCloudUtil", "media scanner exif video error %s", e);
        }
    }

    public static void statVideoInfo(Context context, long j, String str, long j2) {
        if (j > 0) {
            HashMap hashMap = new HashMap();
            hashMap.put("elapse_time", String.valueOf(j));
            hashMap.put(Action.FILE_ATTRIBUTE, StorageUtils.getRelativePath(context, str));
            hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.format(Locale.US, "%.1fM", Float.valueOf(((float) j2) / 1000000.0f)));
            SamplingStatHelper.recordCountEvent("media_scanner", "video_duration", hashMap);
        }
    }
}
