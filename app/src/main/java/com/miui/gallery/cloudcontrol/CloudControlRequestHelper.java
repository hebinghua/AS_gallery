package com.miui.gallery.cloudcontrol;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloudcontrol.CloudControlRequest;
import com.miui.gallery.cloudcontrol.RecommendListRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CloudControlRequestHelper {
    public Context mContext;

    public final String formatEventName(boolean z, boolean z2) {
        return z ? z2 ? "recommend_cloud_control_real_name_request_error" : "recommend_cloud_control_anonymous_request_error" : z2 ? "cloud_control_real_name_request_error" : "cloud_control_anonymous_request_error";
    }

    public CloudControlRequestHelper(Context context) {
        this.mContext = context;
    }

    public boolean execRequestSync(boolean z) {
        boolean execRequestSyncInternal = execRequestSyncInternal(z);
        if (execRequestSyncInternal) {
            try {
                doPostJobs();
            } catch (Exception e) {
                DefaultLogger.e("CloudControlRequestHelper", "Error occurred while executing post cloud control request job, %s", e);
            }
        }
        return execRequestSyncInternal;
    }

    public boolean execRequestSync() {
        return execRequestSync(SyncUtil.existXiaomiAccount(this.mContext));
    }

    public final boolean execRequestSyncInternal(boolean z) {
        if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(this.mContext)) {
            DefaultLogger.w("CloudControlRequestHelper", "Request failed: privacy agreement disabled");
            return false;
        } else if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            DefaultLogger.e("CloudControlRequestHelper", "Request failed: CTA not confirmed.");
            return false;
        } else {
            execRecommendRequest(z);
            try {
                CloudControlResponse cloudControlResponse = (CloudControlResponse) new CloudControlRequest.Builder().setMethod(z ? 1002 : 1).setUrl(z ? HostManager.CloudControl.getUrl() : HostManager.CloudControl.getAnonymousUrl()).setSyncToken(GalleryPreferences.CloudControl.getSyncToken()).build().simpleExecuteSync();
                if (cloudControlResponse == null) {
                    handleError(ErrorCode.BODY_EMPTY, "Response is empty", "Response is empty", z, false);
                    return false;
                }
                handleResponse(cloudControlResponse);
                GalleryPreferences.CloudControl.setLastRequestSucceedTime(System.currentTimeMillis());
                return true;
            } catch (RequestError e) {
                handleError(e.getErrorCode(), e.getMessage(), e.getResponseData(), z, false);
                return false;
            } catch (Exception e2) {
                handleError(ErrorCode.HANDLE_ERROR, e2.getMessage(), null, z, false);
                return false;
            } finally {
                GalleryPreferences.CloudControl.setLastRequestTime(System.currentTimeMillis());
            }
        }
    }

    public boolean execRecommendRequest() {
        return execRecommendRequest(SyncUtil.existXiaomiAccount(this.mContext));
    }

    public boolean execRecommendRequest(boolean z) {
        RecommendListRequest build;
        if (!MediaFeatureManager.isDeviceSupportStoryFunction()) {
            return false;
        }
        if (z) {
            build = new RecommendListRequest.Builder().setMethod(true).setUrl(HostManager.RecommendList.getUrl()).build();
        } else {
            build = new RecommendListRequest.Builder().setMethod(false).setUrl(HostManager.RecommendList.getAnonymousUrl()).build();
        }
        try {
            RecommendListResponse recommendListResponse = (RecommendListResponse) build.simpleExecuteSync();
            if (recommendListResponse == null) {
                handleError(ErrorCode.BODY_EMPTY, "Response is empty", "Response is empty", z, true);
                return false;
            }
            handleResponse(recommendListResponse, true);
            return true;
        } catch (RequestError e) {
            handleError(e.getErrorCode(), e.getMessage(), e.getResponseData(), z, true);
            return false;
        } catch (Exception e2) {
            handleError(ErrorCode.HANDLE_ERROR, e2.getMessage(), null, z, true);
            return false;
        }
    }

    public final void handleResponse(CloudControlResponse cloudControlResponse) {
        handleResponse(cloudControlResponse, false);
    }

    public final void handleResponse(CloudControlResponse cloudControlResponse, boolean z) {
        if (BaseMiscUtil.isValid(cloudControlResponse.getFeatureProfiles())) {
            boolean z2 = false;
            Iterator<FeatureProfile> it = cloudControlResponse.getFeatureProfiles().iterator();
            while (it.hasNext()) {
                FeatureProfile next = it.next();
                CloudControlManager.getInstance().insertToCache(next);
                if (CloudControlDBHelper.tryInsertToDB(this.mContext, next) == 0) {
                    z2 = true;
                    DefaultLogger.e("CloudControlRequestHelper", "Persist error: %s", String.valueOf(next));
                }
            }
            if (z2 && !z) {
                HashMap hashMap = new HashMap();
                hashMap.put("result", String.valueOf(cloudControlResponse));
                SamplingStatHelper.recordCountEvent("cloud_control", "cloud_control_persist_error", hashMap);
            }
        }
        if (TextUtils.isEmpty(cloudControlResponse.getSyncToken()) || z) {
            return;
        }
        GalleryPreferences.CloudControl.setSyncToken(cloudControlResponse.getSyncToken());
    }

    public final void handleError(ErrorCode errorCode, String str, Object obj, boolean z, boolean z2) {
        String name = errorCode != null ? errorCode.name() : "UNKNOWN";
        DefaultLogger.e("CloudControlRequestHelper", "Request failed, errorCode: %s, errorMessage: %s, responseData: %s, isLoggedIn: %b.", name, str, String.valueOf(obj), Boolean.valueOf(z));
        HashMap hashMap = new HashMap();
        if (!z2) {
            hashMap.put("request_interval(minutes)", String.valueOf(getRequestIntervalMinutes()));
        }
        hashMap.put("error", name);
        hashMap.put("error_extra", str);
        hashMap.put("result", String.valueOf(obj));
        SamplingStatHelper.recordCountEvent("cloud_control", formatEventName(z2, z), hashMap);
    }

    public final int getRequestIntervalMinutes() {
        long lastRequestTime = GalleryPreferences.CloudControl.getLastRequestTime();
        if (lastRequestTime == 0) {
            return Integer.MAX_VALUE;
        }
        return (int) (((System.currentTimeMillis() - lastRequestTime) / 1000) / 60);
    }

    public final void doPostJobs() {
        AIAlbumStatusHelper.doPostCloudControlJob();
    }
}
