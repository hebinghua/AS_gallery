package com.miui.gallery.cloud.operation.create;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.joran.action.Action;
import cn.kuaipan.android.kss.upload.UploadDescriptorFile;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.Operation;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.ThumbnailMetaWriter;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.StorageUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public abstract class CreateImageBase implements Operation<DBImage> {
    public Context mContext;

    public abstract void doUpload(RequestCloudItem requestCloudItem, UploadDescriptorFile uploadDescriptorFile) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException;

    public abstract DBImage getDBImage(RequestCloudItem requestCloudItem);

    public final boolean isNormalFlag(int i) {
        return (i == -1 || i == 2) ? false : true;
    }

    public CreateImageBase(Context context) {
        this.mContext = context;
    }

    /* JADX WARN: Removed duplicated region for block: B:165:0x04b1  */
    @Override // com.miui.gallery.cloud.Operation
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.miui.gallery.cloud.base.GallerySyncResult<com.miui.gallery.data.DBImage> execute(android.accounts.Account r26, com.miui.gallery.cloud.base.GalleryExtendedAuthToken r27, java.lang.String r28, com.miui.gallery.cloud.RequestItemBase r29) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 1384
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.operation.create.CreateImageBase.execute(android.accounts.Account, com.miui.gallery.cloud.base.GalleryExtendedAuthToken, java.lang.String, com.miui.gallery.cloud.RequestItemBase):com.miui.gallery.cloud.base.GallerySyncResult");
    }

    public int checkState(RequestItemBase requestItemBase) {
        return SyncConditionManager.checkForItem(requestItemBase);
    }

    public final void statCannotUpload(DBImage dBImage, int i, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Action.FILE_ATTRIBUTE, BaseFileUtils.getParentFolderPath(dBImage.getLocalFile()));
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, MiscUtil.serverType2Text(dBImage.getServerType()));
        hashMap.put("error", String.valueOf(i));
        SamplingStatHelper.recordCountEvent("Sync", "sync_cannot_upload_v2", hashMap);
        if (z) {
            hashMap.clear();
            hashMap.put(Action.FILE_ATTRIBUTE, dBImage.getLocalFile());
            SamplingStatHelper.recordCountEvent("Sync", "sync_invalid_data", hashMap);
        }
    }

    public final void statRejectUpload(DBImage dBImage, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(Action.FILE_ATTRIBUTE, BaseFileUtils.getParentFolderPath(dBImage.getLocalFile()));
        hashMap.put("error", String.valueOf(i));
        SamplingStatHelper.recordCountEvent("Sync", "sync_reject_upload", hashMap);
    }

    public final void statUploadError(DBImage dBImage, Exception exc, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, MiscUtil.serverType2Text(dBImage.getServerType()));
        hashMap.put(Action.FILE_ATTRIBUTE, BaseFileUtils.getParentFolderPath(dBImage.getLocalFile()));
        hashMap.put("error_extra", exc.getMessage());
        hashMap.put("error", str);
        SamplingStatHelper.recordCountEvent("Sync", "sync_upload_error", hashMap);
    }

    public final boolean isInvalidData(DBImage dBImage) {
        if (dBImage == null) {
            return false;
        }
        if (dBImage.getLocalFlag() != 7 && dBImage.getLocalFlag() != 8) {
            return false;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(dBImage.getLocalFile(), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CreateImageBase", "isInvalidData"));
        if (documentFile == null || !documentFile.exists()) {
            return false;
        }
        if (String.valueOf(-1000L).equals(dBImage.getLocalGroupId())) {
            return true;
        }
        DBImage item = CloudUtils.getItem(GalleryCloudUtils.CLOUD_URI, this.mContext, j.c, dBImage.getLocalGroupId());
        return item != null && !TextUtils.isEmpty(item.getLocalFile()) && item.getLocalFile().equalsIgnoreCase(BaseFileUtils.getParentFolderPath(StorageUtils.getRelativePath(this.mContext, dBImage.getLocalFile())));
    }

    public final void statImageCreate(DBImage dBImage, DocumentFile documentFile, long j) {
        String str = dBImage.isImageType() ? "image" : dBImage.isVideoType() ? "video" : "other";
        HashMap hashMap = new HashMap();
        Locale locale = Locale.US;
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.format(locale, "%.2f", Float.valueOf((((float) documentFile.length()) * 1.0f) / 1024.0f)));
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        hashMap.put("cost_time", String.format(locale, "%.2f", Float.valueOf((((float) j) * 1.0f) / 1000.0f)));
        SamplingStatHelper.recordCountEvent("Sync", "sync_upload_time_one_photo_v2", hashMap);
        float currentTimeMillis = (((float) (System.currentTimeMillis() - documentFile.lastModified())) * 1.0f) / 60000.0f;
        long j2 = Long.MAX_VALUE;
        try {
            j2 = Long.parseLong(dBImage.getAlbumId());
        } catch (NumberFormatException unused) {
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        if (j2 <= 1000) {
            hashMap2.put("album", dBImage.getAlbumId());
        }
        hashMap2.put("cost_time", String.format(Locale.US, "%.2f", Float.valueOf(currentTimeMillis)));
        SamplingStatHelper.recordCountEvent("Sync", "sync_upload_timely_v2", hashMap2);
    }

    public final int checkExifAndSha1Valid(final DBImage dBImage) throws IOException, StoragePermissionMissingException {
        long dateModified = dBImage.getDateModified();
        long size = dBImage.getSize();
        String localFile = dBImage.getLocalFile();
        File file = new File(localFile);
        String userCommentSha1 = ExifUtil.getUserCommentSha1(localFile);
        String readFileNameFromExif = CloudUtils.readFileNameFromExif(localFile);
        if (dateModified != file.lastModified() || size != file.length() || !TextUtils.isEmpty(userCommentSha1) || !TextUtils.isEmpty(readFileNameFromExif)) {
            ContentValues contentValues = new ContentValues();
            if (!TextUtils.isEmpty(userCommentSha1) || !TextUtils.isEmpty(readFileNameFromExif)) {
                IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(localFile, IStoragePermissionStrategy.Permission.UPDATE);
                if (!checkPermission.granted) {
                    if (checkPermission.applicable) {
                        throw new StoragePermissionMissingException(Collections.singletonList(checkPermission));
                    }
                    return 11;
                }
                boolean write = new ThumbnailMetaWriter(dBImage, "", "").write(localFile, false);
                dBImage.setOriginInfo(userCommentSha1, readFileNameFromExif);
                if (!write) {
                    return 11;
                }
                HashMap hashMap = new HashMap();
                hashMap.put("model", Build.MODEL);
                hashMap.put("version", Build.VERSION.INCREMENTAL);
                hashMap.put(CallMethod.ARG_EXTRA_STRING, String.valueOf(!TextUtils.isEmpty(userCommentSha1)));
                SamplingStatHelper.recordCountEvent("micloud_sync", "upload_thumbnail", hashMap);
                contentValues.put("sha1", FileUtils.getSha1(localFile));
            }
            contentValues.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(file.length()));
            contentValues.put("dateModified", Long.valueOf(file.lastModified()));
            if (dBImage.isVideoType()) {
                if (!GalleryCloudUtils.putValuesForVideo(localFile, contentValues)) {
                    return 12;
                }
            } else {
                GalleryCloudUtils.putValuesForImage(localFile, contentValues);
            }
            GalleryUtils.safeUpdate(dBImage.getBaseUri(), contentValues, "_id = ? ", new String[]{dBImage.getId()});
            GalleryUtils.safeQuery(CloudUtils.getLimitUri(dBImage.getBaseUri(), 1), CloudUtils.getProjectionAll(), "_id = ? ", new String[]{dBImage.getId()}, (String) null, new GalleryUtils.QueryHandler<Void>() { // from class: com.miui.gallery.cloud.operation.create.CreateImageBase.1
                @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
                /* renamed from: handle  reason: collision with other method in class */
                public Void mo1712handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    dBImage.reloadData(cursor);
                    return null;
                }
            });
            return 0;
        }
        return 0;
    }
}
