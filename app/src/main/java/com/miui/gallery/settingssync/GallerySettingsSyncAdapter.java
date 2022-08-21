package com.miui.gallery.settingssync;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GallerySettingsSyncAdapter implements GallerySettingsSyncContract$SyncAdapter {
    public Context mContext;
    public GallerySettingsSyncContract$Model mModel;

    public GallerySettingsSyncAdapter(Context context, GallerySettingsSyncContract$Model gallerySettingsSyncContract$Model) {
        this.mContext = context;
        this.mModel = gallerySettingsSyncContract$Model;
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncAdapter
    public void performUpload() {
        if (!checkCondition()) {
            DefaultLogger.e("GallerySettingsSyncAdapter", "performUpload failed");
        } else if (!this.mModel.isDirty()) {
        } else {
            boolean doUpload = doUpload();
            if (doUpload) {
                this.mModel.markDirty(false);
            }
            DefaultLogger.d("GallerySettingsSyncAdapter", "Upload settings result %s", Boolean.valueOf(doUpload));
        }
    }

    @Override // com.miui.gallery.settingssync.GallerySettingsSyncContract$SyncAdapter
    public void performDownload() {
        if (!checkCondition()) {
            DefaultLogger.e("GallerySettingsSyncAdapter", "performDownload failed");
        } else if (this.mModel.isDirty()) {
            boolean doUpload = doUpload();
            if (doUpload) {
                this.mModel.markDirty(false);
            }
            DefaultLogger.d("GallerySettingsSyncAdapter", "Upload settings result %s", Boolean.valueOf(doUpload));
        } else {
            boolean doDownload = doDownload();
            GalleryPreferences.SettingsSync.setLastDownloadTime(System.currentTimeMillis());
            DefaultLogger.d("GallerySettingsSyncAdapter", "Download settings result %s", Boolean.valueOf(doDownload));
        }
    }

    public final boolean checkCondition() {
        if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            DefaultLogger.e("GallerySettingsSyncAdapter", "Sync settings failed: CTA not confirmed");
            return false;
        } else if (!CloudUtils.checkAccount(null, true, null)) {
            DefaultLogger.e("GallerySettingsSyncAdapter", "Sync settings failed: check account failed");
            return false;
        } else if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(GalleryApp.sGetAndroidContext())) {
            DefaultLogger.e("GallerySettingsSyncAdapter", "Sync settings failed: cloud service agreement disabled");
            return false;
        } else if (SyncUtil.isGalleryCloudSyncable(this.mContext)) {
            return true;
        } else {
            DefaultLogger.e("GallerySettingsSyncAdapter", "Sync settings failed: sync off");
            return false;
        }
    }

    public final boolean doUpload() {
        JSONObject uploadSettings = this.mModel.getUploadSettings();
        if (uploadSettings == null) {
            DefaultLogger.w("GallerySettingsSyncAdapter", "No syncable settings found to upload");
            return true;
        }
        try {
            Object[] executeSync = new GallerySettingUploadRequest(uploadSettings).executeSync();
            if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof Boolean)) {
                return ((Boolean) executeSync[0]).booleanValue();
            }
        } catch (RequestError e) {
            DefaultLogger.e("GallerySettingsSyncAdapter", "Post setting failed, %s", e);
        }
        return false;
    }

    public final boolean doDownload() {
        JSONObject jSONObject = null;
        try {
            Object[] executeSync = new GallerySettingDownloadRequest().executeSync();
            if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof JSONObject)) {
                jSONObject = (JSONObject) executeSync[0];
            }
        } catch (RequestError e) {
            DefaultLogger.e("GallerySettingsSyncAdapter", "Get setting failed, %s", e);
        }
        if (jSONObject == null) {
            DefaultLogger.w("GallerySettingsSyncAdapter", "No syncable settings found to update");
            return true;
        }
        return this.mModel.onDownloadSettings(jSONObject);
    }

    /* loaded from: classes2.dex */
    public static class GallerySettingUploadRequest extends BaseGalleryRequest {
        public GallerySettingUploadRequest(JSONObject jSONObject) {
            super(1002, HostManager.Setting.getSyncUrl());
            addParam("data", jSONObject.toString());
        }

        @Override // com.miui.gallery.net.BaseGalleryRequest
        public void onRequestSuccess(JSONObject jSONObject) throws Exception {
            DefaultLogger.w("GallerySettingsSyncAdapter", "No data is expected here, what are you [%s]", jSONObject);
            deliverResponse(Boolean.TRUE);
        }

        @Override // com.miui.gallery.net.BaseGalleryRequest, com.miui.gallery.net.json.BaseJsonRequest, com.miui.gallery.net.base.BaseRequest, com.miui.gallery.net.base.ResponseErrorHandler
        public void onRequestError(ErrorCode errorCode, String str, Object obj) {
            if (errorCode == ErrorCode.BODY_EMPTY) {
                deliverResponse(Boolean.TRUE);
            } else {
                deliverError(errorCode, str, obj);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class GallerySettingDownloadRequest extends BaseGalleryRequest {
        public GallerySettingDownloadRequest() {
            super(1001, HostManager.Setting.getSyncUrl());
        }

        @Override // com.miui.gallery.net.BaseGalleryRequest
        public void onRequestSuccess(JSONObject jSONObject) throws Exception {
            deliverResponse(jSONObject);
        }
    }
}
