package com.miui.gallery.scanner.utils;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.util.FileSize;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.scanner.core.model.ExifCloudEntry;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.model.SaveToCloud;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.SafeDBUtil;

/* loaded from: classes2.dex */
public class DefaultDeDupStrategy extends AbsDeDupStrategy {
    /* JADX WARN: Removed duplicated region for block: B:101:0x029c  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02a7  */
    @Override // com.miui.gallery.scanner.utils.AbsDeDupStrategy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.miui.gallery.scanner.core.task.eventual.ScanResult doDeDup(android.content.Context r28, com.miui.gallery.scanner.core.model.SaveToCloud r29, com.miui.gallery.scanner.core.model.SaveParams r30) {
        /*
            Method dump skipped, instructions count: 1427
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.scanner.utils.DefaultDeDupStrategy.doDeDup(android.content.Context, com.miui.gallery.scanner.core.model.SaveToCloud, com.miui.gallery.scanner.core.model.SaveParams):com.miui.gallery.scanner.core.task.eventual.ScanResult");
    }

    public static boolean needCheckExifSha1(String str) {
        return !TextUtils.isEmpty(str) && str.endsWith(".jpg");
    }

    public static void update(Context context, ContentValues contentValues, ExifCloudEntry exifCloudEntry, SaveParams saveParams) {
        contentValues.put("realDateModified", Long.valueOf(saveParams.getFileState() == null ? saveParams.getSaveFile().lastModified() : saveParams.getFileState().modified));
        contentValues.put("realSize", Long.valueOf(saveParams.getFileState() == null ? saveParams.getSaveFile().length() : saveParams.getFileState().size));
        if (exifCloudEntry.mLocalFlag == 7) {
            contentValues.put("dateModified", Long.valueOf(saveParams.getSaveFile().lastModified()));
            contentValues.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(saveParams.getSaveFile().length()));
        }
        Uri writeUri = SaveToCloudUtil.getWriteUri(saveParams);
        if (saveParams.getBatchOperator() != null) {
            saveParams.getBatchOperator().add(context, ContentProviderOperation.newUpdate(writeUri).withSelection("_id=?", new String[]{String.valueOf(exifCloudEntry.mID)}).withValues(contentValues).build());
        } else {
            SafeDBUtil.safeUpdate(context, writeUri, contentValues, "_id=?", new String[]{String.valueOf(exifCloudEntry.mID)});
        }
    }

    public static ContentValues genUpdateValues(Context context, SaveToCloud saveToCloud, AbsImageValueCalculator absImageValueCalculator, AbsVideoValueCalculator absVideoValueCalculator) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(saveToCloud.mSize));
        contentValues.put("dateModified", Long.valueOf(saveToCloud.mLastModify));
        contentValues.put("sha1", saveToCloud.mSha1);
        if (BaseFileMimeUtil.isImageFromMimeType(saveToCloud.mMimeType)) {
            contentValues.put("serverType", (Integer) 1);
            SaveToCloudUtil.putValuesForImage(saveToCloud.mPath, contentValues, saveToCloud.mSpecifiedTaken, absImageValueCalculator);
        } else if (BaseFileMimeUtil.isVideoFromMimeType(saveToCloud.mMimeType)) {
            contentValues.put("serverType", (Integer) 2);
            SaveToCloudUtil.putValuesForVideo(context, saveToCloud.mPath, saveToCloud.mSize, contentValues, saveToCloud.mSpecifiedTaken, absVideoValueCalculator);
        }
        return contentValues;
    }

    public static boolean maybeThumbnail(SaveToCloud saveToCloud) {
        return needCheckExifSha1(saveToCloud.mName) && saveToCloud.mSize < FileSize.MB_COEFFICIENT;
    }
}
