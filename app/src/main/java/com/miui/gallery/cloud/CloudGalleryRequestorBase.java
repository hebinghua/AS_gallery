package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Pair;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.HttpUtils;
import com.miui.gallery.util.Log2File;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.file.MiCloudFileRequestor;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.opensdk.file.model.CommitParameter;
import com.xiaomi.opensdk.file.model.RequestUploadParameter;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class CloudGalleryRequestorBase extends MiCloudFileRequestor<RequestCloudItem> {
    public Account mAccount;
    public final CloudUrlProvider mUrlProvider;

    public abstract void connectSubUbi(RequestCloudItem requestCloudItem);

    public abstract String getAlbumId(RequestCloudItem requestCloudItem);

    public abstract DBImage getItemByRequestId(String str, RequestCloudItem requestCloudItem);

    public abstract SpaceFullHandler.SpaceFullListener getSpaceFullListener();

    public abstract String parseRequestId(JSONObject jSONObject, RequestCloudItem requestCloudItem) throws JSONException;

    public void putAlbumIdInValues(RequestCloudItem requestCloudItem, JSONObject jSONObject, ContentValues contentValues) {
    }

    public CloudGalleryRequestorBase(Account account, CloudUrlProvider cloudUrlProvider) {
        super(GalleryApp.sGetAndroidContext());
        this.mAccount = account;
        this.mUrlProvider = cloudUrlProvider;
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public Map<String, String> getRequestUploadParams(RequestCloudItem requestCloudItem, RequestUploadParameter requestUploadParameter) throws JSONException {
        HashMap hashMap = new HashMap();
        addRetryParameters(hashMap, requestCloudItem.createRetryTimes, requestCloudItem.needReRequest);
        addUbiParam(hashMap, requestCloudItem);
        hashMap.put("data", getRequestUploadPostString(requestCloudItem, requestUploadParameter));
        return hashMap;
    }

    public final void addUbiParam(Map<String, String> map, RequestCloudItem requestCloudItem) {
        if (requestCloudItem.dbCloud.isUbiFocus()) {
            map.put("isUbiImage", String.valueOf(true));
        }
        if (requestCloudItem.dbCloud.isSubUbiFocus()) {
            map.put("ubiIndex", String.valueOf(requestCloudItem.dbCloud.getSubUbiIndex()));
        }
    }

    public void addCreateImageReplaceId(JSONObject jSONObject, RequestCloudItem requestCloudItem) throws JSONException {
        Object[] serverIdAndSha1ByLocalId;
        String localImageId = requestCloudItem.dbCloud.getLocalImageId();
        if (requestCloudItem.dbCloud.isShareItem() || TextUtils.isEmpty(localImageId) || (serverIdAndSha1ByLocalId = CloudUtils.getServerIdAndSha1ByLocalId(GalleryApp.sGetAndroidContext(), localImageId, requestCloudItem)) == null || serverIdAndSha1ByLocalId.length != 2 || TextUtils.isEmpty(serverIdAndSha1ByLocalId[0])) {
            return;
        }
        jSONObject.put("replaceId", Long.valueOf(serverIdAndSha1ByLocalId[0]));
        jSONObject.put("replaceSha1", serverIdAndSha1ByLocalId[1]);
    }

    public String getRequestUploadPostString(RequestCloudItem requestCloudItem, RequestUploadParameter requestUploadParameter) throws JSONException {
        JSONObject jSONObject = new JSONObject(requestUploadParameter.getKssString());
        requestCloudItem.dbCloud.setSha1(requestUploadParameter.getFileSHA1());
        JSONObject jSONObject2 = requestCloudItem.dbCloud.toJSONObject();
        jSONObject2.remove("creatorId");
        jSONObject2.remove("shareId");
        String localFile = requestCloudItem.dbCloud.getLocalFile();
        if (!TextUtils.isEmpty(localFile) && BaseFileMimeUtil.isJpegImageFromMimeType(BaseFileMimeUtil.getMimeType(localFile))) {
            jSONObject2.put("isFrontCamera", ExifUtil.isFromFrontCamera(localFile));
        }
        jSONObject.put(MiStat.Param.CONTENT, jSONObject2);
        addCreateImageReplaceId(jSONObject, requestCloudItem);
        return jSONObject.toString();
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public RequestCloudItem handleRequestUploadResultJson(RequestCloudItem requestCloudItem, JSONObject jSONObject) throws UnretriableException, RetriableException, AuthenticationException {
        DBImage itemByRequestId;
        try {
            GallerySyncResult<JSONObject> checkXMResultCode = CheckResult.checkXMResultCode(jSONObject, requestCloudItem, getSpaceFullListener());
            boolean z = true;
            if (checkXMResultCode.code != GallerySyncCode.OK) {
                statFailEvent("requestUpload", checkXMResultCode);
                DefaultLogger.d("CloudGalleryRequestorBase", "upload request error %s", jSONObject);
                requestCloudItem.result = checkXMResultCode.code;
                requestCloudItem.createRetryTimes++;
                return requestCloudItem;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("data");
            JSONObject jSONObject3 = jSONObject2.getJSONObject(MiStat.Param.CONTENT);
            String parseRequestId = parseRequestId(jSONObject3, requestCloudItem);
            jSONObject2.put("upload_id", parseRequestId);
            String string = jSONObject3.getString("fileName");
            if (!string.equals(requestCloudItem.dbCloud.getFileName())) {
                SyncLogger.d("CloudGalleryRequestorBase", "create image name changed from:" + requestCloudItem.dbCloud.getFileName() + " to:" + string);
            }
            synchronized (requestCloudItem.dbCloud.getBaseUri()) {
                if (!requestCloudItem.dbCloud.isSubUbiFocus() && (itemByRequestId = getItemByRequestId(parseRequestId, requestCloudItem)) != null && !itemByRequestId.getId().equals(requestCloudItem.dbCloud.getId())) {
                    CloudUtils.deleteDirty(itemByRequestId);
                }
                new ContentValues();
                ContentValues contentValuesForAll = CloudUtils.getContentValuesForAll(jSONObject3, requestCloudItem.dbCloud.isShareItem());
                putAlbumIdInValues(requestCloudItem, jSONObject, contentValuesForAll);
                if (!jSONObject2.has("kss") && jSONObject2.has("fileExists") && jSONObject2.getBoolean("fileExists")) {
                    if (requestCloudItem.isSecretItem()) {
                        encodeSecretFiles(requestCloudItem.dbCloud, contentValuesForAll);
                    }
                    updateLocalDBForSyncAndConnectUbi(requestCloudItem, contentValuesForAll);
                } else {
                    z = false;
                    CloudUtils.updateToLocalDB(requestCloudItem.dbCloud.getBaseUri(), contentValuesForAll, requestCloudItem.dbCloud);
                }
            }
            if (!z) {
                return null;
            }
            return requestCloudItem;
        } catch (JSONException e) {
            statFailEvent("requestUpload", e);
            throw new UnretriableException(e);
        }
    }

    public final void updateLocalDBForSyncAndConnectUbi(RequestCloudItem requestCloudItem, ContentValues contentValues) throws JSONException {
        CloudUtils.updateToLocalDBForSync(requestCloudItem.dbCloud.getBaseUri(), contentValues, requestCloudItem.dbCloud);
        if (requestCloudItem.dbCloud.isUbiFocus()) {
            connectSubUbi(requestCloudItem);
        }
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public Map<String, String> getCommitUploadParams(RequestCloudItem requestCloudItem, CommitParameter commitParameter) throws JSONException {
        HashMap hashMap = new HashMap();
        addRetryParameters(hashMap, requestCloudItem.commitRetryTimes, requestCloudItem.needReRequest);
        hashMap.put("data", getCommitUploadPostString(requestCloudItem, commitParameter));
        return hashMap;
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public String getRequestUploadURL(RequestCloudItem requestCloudItem, RequestUploadParameter requestUploadParameter) {
        String albumId = getAlbumId(requestCloudItem);
        if (TextUtils.isEmpty(albumId)) {
            return null;
        }
        if (requestCloudItem.dbCloud.isSubUbiFocus()) {
            if (!TextUtils.isEmpty(requestCloudItem.dbCloud.getUbiServerId()) && requestCloudItem.dbCloud.getSubUbiIndex() != -1) {
                return getCreateSubUbiUrl(this.mAccount.name, requestCloudItem.dbCloud.getUbiServerId(), requestCloudItem.dbCloud.getSubUbiIndex());
            }
            return null;
        }
        return getCreateUrl(this.mAccount.name, albumId);
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public RequestCloudItem handleCommitUploadResult(RequestCloudItem requestCloudItem, JSONObject jSONObject) throws UnretriableException, RetriableException, AuthenticationException {
        try {
            GallerySyncResult<JSONObject> checkXMResultCode = CheckResult.checkXMResultCode(jSONObject, requestCloudItem, getSpaceFullListener());
            if (checkXMResultCode.code != GallerySyncCode.OK) {
                statFailEvent("commitUpload", checkXMResultCode);
                DefaultLogger.d("CloudGalleryRequestorBase", "upload commit error %s", jSONObject);
                requestCloudItem.result = checkXMResultCode.code;
                requestCloudItem.commitRetryTimes++;
                return null;
            }
            putCommitResult(jSONObject.getJSONObject("data"), requestCloudItem);
            if (Log2File.getInstance().canLog()) {
                Log2File.getInstance().flushLog("CloudGalleryRequestorBase", "upload a pic:" + requestCloudItem.dbCloud.getFileName(), null);
            }
            return requestCloudItem;
        } catch (JSONException e) {
            statFailEvent("commitUpload", e);
            throw new UnretriableException(e);
        }
    }

    public final void putCommitResult(JSONObject jSONObject, RequestCloudItem requestCloudItem) throws JSONException {
        ContentValues contentValuesForAll = CloudUtils.getContentValuesForAll(jSONObject, requestCloudItem.dbCloud.isShareItem());
        putAlbumIdInValues(requestCloudItem, jSONObject, contentValuesForAll);
        if (requestCloudItem.isSecretItem()) {
            encodeSecretFiles(requestCloudItem.dbCloud, contentValuesForAll);
        }
        if (!TextUtils.isEmpty(requestCloudItem.dbCloud.getLocalImageId())) {
            CloudUtils.putLocalImageIdColumnsNull(contentValuesForAll);
        }
        if (!requestCloudItem.dbCloud.isSubUbiFocus()) {
            updateLocalDBForSyncAndConnectUbi(requestCloudItem, contentValuesForAll);
            return;
        }
        CloudUtils.updateToLocalDBForSync(requestCloudItem.dbCloud.isShareItem() ? GalleryCloudUtils.SHARE_IMAGE_URI : GalleryCloudUtils.CLOUD_URI, contentValuesForAll, ((DBImage.SubUbiImage) requestCloudItem.dbCloud).getUbiLocalId());
        JSONObject optJSONObject = jSONObject.getJSONObject(MiStat.Param.CONTENT).getJSONObject("ubiSubImageContentMap").optJSONObject(String.valueOf(requestCloudItem.dbCloud.getSubUbiIndex()));
        if (optJSONObject == null) {
            return;
        }
        ContentValues contentValuesForAll2 = CloudUtils.getContentValuesForAll(optJSONObject, requestCloudItem.dbCloud.isShareItem());
        putAlbumIdInValues(requestCloudItem, optJSONObject, contentValuesForAll2);
        CloudUtils.updateToLocalDBForSync(requestCloudItem.dbCloud.getBaseUri(), contentValuesForAll2, requestCloudItem.dbCloud);
    }

    public static boolean isSynced(ContentValues contentValues) {
        return "custom".equalsIgnoreCase(contentValues.getAsString("serverStatus"));
    }

    public final void encodeSecretFiles(DBImage dBImage, ContentValues contentValues) {
        if (isSynced(contentValues)) {
            CloudUtils.SecretAlbumUtils.encryptFiles(dBImage, contentValues);
        }
    }

    public String getCommitUploadPostString(RequestCloudItem requestCloudItem, CommitParameter commitParameter) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("kss", new JSONObject(commitParameter.getKssString()));
        addCreateImageReplaceId(jSONObject, requestCloudItem);
        return jSONObject.toString();
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public Map<String, String> getRequestDownloadParams(RequestCloudItem requestCloudItem) {
        HashMap hashMap = new HashMap();
        addExtraParameters(hashMap, requestCloudItem.otherRetryTimes, requestCloudItem.needReRequest, (requestCloudItem.getDownloadType() != 3 || (!requestCloudItem.dbCloud.isUbiFocus() && !requestCloudItem.dbCloud.isSubUbiFocus())) ? -1 : requestCloudItem.dbCloud.getSubUbiIndex());
        return hashMap;
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public boolean handleRequestDownloadResultJson(RequestCloudItem requestCloudItem, JSONObject jSONObject) throws UnretriableException, RetriableException, AuthenticationException {
        try {
            GallerySyncResult<JSONObject> checkXMResultCode = CheckResult.checkXMResultCode(jSONObject, requestCloudItem, getSpaceFullListener());
            if (checkXMResultCode.code == GallerySyncCode.OK) {
                return true;
            }
            statFailEvent("requestDownload", checkXMResultCode);
            GallerySyncCode gallerySyncCode = checkXMResultCode.code;
            requestCloudItem.result = gallerySyncCode;
            requestCloudItem.otherRetryTimes++;
            if (gallerySyncCode != GallerySyncCode.SERVER_INVALID) {
                return false;
            }
            requestCloudItem.setIsCloudInvalid(true);
            return false;
        } catch (JSONException e) {
            statFailEvent("requestDownload", e);
            throw new UnretriableException(e);
        }
    }

    public final void statFailEvent(String str, GallerySyncResult gallerySyncResult) {
        HashMap hashMap = new HashMap();
        hashMap.put("from", str);
        hashMap.put("result", gallerySyncResult.toString());
        SamplingStatHelper.recordCountEvent("Sync", "download_upload_error", hashMap);
    }

    public final void statFailEvent(String str, Exception exc) {
        HashMap hashMap = new HashMap();
        hashMap.put("from", str);
        hashMap.put("exception", exc.toString());
        SamplingStatHelper.recordCountEvent("Sync", "download_upload_error", hashMap);
    }

    public final void addRetryParameters(Map<String, String> map, int i, boolean z) {
        addExtraParameters(map, i, z, -1);
    }

    public final void addExtraParameters(Map<String, String> map, int i, boolean z, int i2) {
        if (i > 0) {
            map.put("retry", Integer.toString(i));
        }
        if (z) {
            map.put("needReRequest", String.valueOf(z));
        }
        if (i2 >= 0) {
            map.put("ubiIndex", String.valueOf(i2));
        }
        Pair<String, String> appLifecycleParameter = HttpUtils.getAppLifecycleParameter();
        map.put((String) appLifecycleParameter.first, (String) appLifecycleParameter.second);
        Pair<String, String> apkVersionParameter = HttpUtils.getApkVersionParameter();
        map.put((String) apkVersionParameter.first, (String) apkVersionParameter.second);
    }

    public final String getCreateUrl(String str, String str2) {
        return this.mUrlProvider.getCreateUrl(str, str2);
    }

    public final String getCreateSubUbiUrl(String str, String str2, int i) {
        return this.mUrlProvider.getCreateSubUbiUrl(str, str2, i);
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public final String getCommitUploadURL(RequestCloudItem requestCloudItem, CommitParameter commitParameter) {
        String uploadId = commitParameter.getUploadId();
        if (requestCloudItem.dbCloud.isSubUbiFocus()) {
            return this.mUrlProvider.getCommitSubUbiUrl(this.mAccount.name, requestCloudItem.dbCloud.getUbiServerId(), requestCloudItem.dbCloud.getSubUbiIndex());
        }
        return this.mUrlProvider.getCommitUrl(this.mAccount.name, uploadId);
    }

    @Override // com.xiaomi.micloudsdk.file.MiCloudFileRequestor
    public final String getRequestDownloadURL(RequestCloudItem requestCloudItem) {
        return this.mUrlProvider.getRequestDownloadUrl(this.mAccount.name, requestCloudItem.dbCloud.getRequestId());
    }
}
