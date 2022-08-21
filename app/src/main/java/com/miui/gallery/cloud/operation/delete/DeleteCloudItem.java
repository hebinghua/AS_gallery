package com.miui.gallery.cloud.operation.delete;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.UbiFocusUtils;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.deprecated.Storage;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.io.File;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DeleteCloudItem extends RequestOperationBase {
    public DeleteCloudItem(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestCloudItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        if (tryDeleteDirtyItem(requestCloudItem.dbCloud)) {
            SyncLogger.e(getTag(), "serverId is null means item is deleted by user, delete this dirty record: %s", requestCloudItem.dbCloud.getFileName());
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        RequestOperationBase.Request.Builder builder = new RequestOperationBase.Request.Builder();
        if (requestCloudItem.dbCloud.isItemType()) {
            DBImage dBImage = requestCloudItem.dbCloud;
            String requestId = dBImage.getRequestId();
            if (TextUtils.isEmpty(requestId)) {
                return null;
            }
            String deleteUrl = CloudUrlProvider.getUrlProvider(dBImage.isShareItem(), dBImage.isVideoType()).getDeleteUrl(account.name, requestId);
            ArrayList arrayList = new ArrayList();
            if (dBImage.isShareItem()) {
                arrayList.add(new BasicNameValuePair("sharedGalleryId", requestId));
            }
            arrayList.add(new BasicNameValuePair("devTag", GalleryPreferences.UUID.get()));
            builder.setUrl(deleteUrl).setMethod(2).setParams(arrayList).setPostData(new JSONObject().put(MiStat.Param.CONTENT, requestCloudItem.dbCloud.toJSONObject())).setRetryTimes(requestItemBase.otherRetryTimes).setNeedReRequest(false);
        }
        return builder.build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        if (Preference.sGetCloudGalleryRecyclebinFull()) {
            Preference.sSetCloudGalleryRecyclebinFull(false);
        }
        try {
            updateForDeleteOrPurgedOnLocal(requestCloudItem.dbCloud.getBaseUri(), this.mContext, requestCloudItem.dbCloud, jSONObject);
        } catch (StoragePermissionMissingException unused) {
            SyncLogger.w("galleryAction_DeleteCloudItem", "unexpected delete [%s] failed since storage permission missing.", requestCloudItem.dbCloud);
        }
        SyncLogger.d(getTag(), "Delete item success: %s, type: %s", requestCloudItem.dbCloud.getFileName(), Integer.valueOf(requestCloudItem.dbCloud.getServerType()));
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode == GallerySyncCode.OK || gallerySyncCode == GallerySyncCode.ALBUM_NOT_EXIST) {
            return;
        }
        String tag = getTag();
        SyncLogger.e(tag, "request error: " + gallerySyncCode);
        requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
    }

    public static boolean tryDeleteDirtyItem(DBImage dBImage) {
        int safeDelete = GalleryUtils.safeDelete(dBImage.getBaseUri(), "_id = ? AND serverId IS NULL ", new String[]{dBImage.getId()});
        UbiFocusUtils.safeDeleteSubUbi(dBImage);
        return safeDelete > 0;
    }

    public static void updateForDeleteOrPurgedOnLocal(Uri uri, Context context, DBImage dBImage, JSONObject jSONObject) throws StoragePermissionMissingException, JSONException {
        if (dBImage.getServerType() == 1 || dBImage.getServerType() == 2) {
            if (!dBImage.isShareItem()) {
                handleTrashItem(context, dBImage, jSONObject, "galleryAction_DeleteCloudItem");
            }
            String thumbnailFile = TextUtils.isEmpty(dBImage.getLocalFile()) ? dBImage.getThumbnailFile() : dBImage.getLocalFile();
            String serverId = dBImage.getServerId();
            String serverStatus = dBImage.getServerStatus();
            updateForDeleteOrPurgedOnLocalNotDeleteFile(uri, context, dBImage, jSONObject);
            MediaFeatureManager.getInstance().onImageDelete(dBImage.getId());
            if (jSONObject.has(MiStat.Param.CONTENT)) {
                jSONObject = jSONObject.getJSONObject(MiStat.Param.CONTENT);
            }
            if (jSONObject.has("status")) {
                String optString = jSONObject.optString("status");
                if (!TextUtils.isEmpty(serverId) && TrashManager.getInstance().getItemByServerId(serverId) == null && serverStatus.equals("recovery")) {
                    SyncLogger.e("galleryAction_DeleteCloudItem", "server purged but local recovered, ignore");
                    if (!TextUtils.isEmpty(thumbnailFile)) {
                        ScannerEngine.getInstance().scanPathAsync(thumbnailFile, 0);
                    } else {
                        ScannerEngine.getInstance().triggerScan();
                    }
                    SamplingStatHelper.recordCountEvent("Sync", "server_purge_local_recover");
                    return;
                } else if ("purged".equalsIgnoreCase(optString)) {
                    for (String str : StorageUtils.getMicroThumbnailDirectories(context)) {
                        FileHandleManager.deleteFile(str + File.separator + dBImage.getSha1FileName(), 1, "galleryAction_DeleteCloudItem");
                    }
                }
            }
            for (String str2 : Storage.getCloudThumbnailFilePath()) {
                FileHandleManager.deleteFile(str2 + File.separator + dBImage.getSha1FileName(), 1, "galleryAction_DeleteCloudItem");
            }
            deleteOriginalFileAndThumbnail(dBImage);
            return;
        }
        DefaultLogger.e("galleryAction_DeleteCloudItem", "error call:%s", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
    }

    public static void handleDeleteOrPurgeStatusForNewImage(Context context, String str, String str2, JSONObject jSONObject, String str3) throws StoragePermissionMissingException {
        DBImage itemByServerID;
        if (("deleted".equalsIgnoreCase(str) || "purged".equalsIgnoreCase(str)) && (itemByServerID = CloudUtils.getItemByServerID(context, str2)) != null) {
            handleTrashItem(context, itemByServerID, jSONObject, str3);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x0199, code lost:
        if (android.text.TextUtils.equals(null, java.lang.Long.toString(1000)) != false) goto L78;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void handleTrashItem(android.content.Context r33, com.miui.gallery.data.DBImage r34, org.json.JSONObject r35, java.lang.String r36) throws com.miui.gallery.storage.exceptions.StoragePermissionMissingException {
        /*
            Method dump skipped, instructions count: 624
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.operation.delete.DeleteCloudItem.handleTrashItem(android.content.Context, com.miui.gallery.data.DBImage, org.json.JSONObject, java.lang.String):void");
    }

    public static void deleteOriginalFileAndThumbnail(DBImage dBImage) {
        String localFile = dBImage.getLocalFile();
        FileHandleManager.deleteFile(localFile, 1, "galleryAction_DeleteCloudItem");
        String thumbnailFile = dBImage.getThumbnailFile();
        if (!TextUtils.isEmpty(thumbnailFile) && !TextUtils.equals(localFile, thumbnailFile)) {
            FileHandleManager.deleteFile(thumbnailFile, 1, "galleryAction_DeleteCloudItem");
        }
        String fileName = dBImage.getFileName();
        if (dBImage.isShareItem()) {
            fileName = DownloadPathHelper.getDownloadFileNameNotSecret(dBImage, fileName);
        }
        for (String str : DownloadPathHelper.getAllFilePathForRead(StorageUtils.getPathsInExternalStorage(GalleryApp.sGetAndroidContext(), DownloadPathHelper.getDownloadFolderRelativePath(dBImage)), fileName)) {
            String userCommentSha1 = ExifUtil.getUserCommentSha1(str);
            if (TextUtils.isEmpty(userCommentSha1)) {
                userCommentSha1 = FileUtils.getSha1(str);
            }
            if (TextUtils.equals(userCommentSha1, dBImage.getSha1())) {
                FileHandleManager.deleteFile(str, 1, "galleryAction_DeleteCloudItem");
            }
        }
    }

    public static void updateForDeleteOrPurgedOnLocalNotDeleteFile(Uri uri, Context context, DBImage dBImage, JSONObject jSONObject) throws JSONException {
        ContentValues contentValuesForUploadDeletePurged = CloudUtils.getContentValuesForUploadDeletePurged(jSONObject);
        contentValuesForUploadDeletePurged.putNull("thumbnailFile");
        contentValuesForUploadDeletePurged.putNull("microthumbfile");
        if (dBImage.getLocalFlag() == 15) {
            contentValuesForUploadDeletePurged.remove("localFlag");
        }
        DefaultLogger.d("galleryAction_DeleteCloudItem", "update db : cloud id [%s] count [%d] values [%s]", dBImage.getId(), Integer.valueOf(GalleryUtils.safeUpdate(uri, contentValuesForUploadDeletePurged, "_id = '" + dBImage.getId() + "'", null)), Util.desensitization(contentValuesForUploadDeletePurged));
        UbiFocusUtils.safeDeleteSubUbi(dBImage);
    }
}
