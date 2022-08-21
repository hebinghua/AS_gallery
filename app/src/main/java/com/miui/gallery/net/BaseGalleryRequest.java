package com.miui.gallery.net;

import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.json.BaseJsonRequest;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class BaseGalleryRequest extends BaseJsonRequest<GalleryResponse> {
    public long mExecStartTime;

    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
    }

    public BaseGalleryRequest(int i, String str) {
        super(i, str);
    }

    @Override // com.miui.gallery.net.base.VolleyRequest
    public final void handleResponse(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has("code")) {
                    if (jSONObject.getInt("code") == ErrorCode.SUCCESS.CODE) {
                        if (jSONObject.isNull("data")) {
                            handleError(ErrorCode.BODY_EMPTY, "response empty data", jSONObject);
                        } else {
                            GalleryResponse galleryResponse = new GalleryResponse();
                            galleryResponse.data = jSONObject.optJSONObject("data");
                            galleryResponse.syncTag = jSONObject.optString("syncTag", null);
                            galleryResponse.syncToken = jSONObject.optString("syncToken", null);
                            galleryResponse.isLastPage = jSONObject.optBoolean("lastPage", true);
                            SamplingStatHelper.trackHttpEvent(getUrl(), System.currentTimeMillis() - this.mExecStartTime, 0L);
                            onRequestSuccess(galleryResponse);
                        }
                    } else {
                        handleError(ErrorCode.SERVER_ERROR, jSONObject.optString("description"), jSONObject);
                    }
                }
            } catch (Exception e) {
                handleError(ErrorCode.HANDLE_ERROR, e.getMessage(), e);
                return;
            }
        }
        handleError(ErrorCode.PARSE_ERROR, "response has no code", null);
    }

    public void onRequestSuccess(GalleryResponse galleryResponse) throws Exception {
        onRequestSuccess(galleryResponse.data);
    }

    @Override // com.miui.gallery.net.base.VolleyRequest, com.miui.gallery.net.base.BaseRequest
    public final void execute() {
        if (checkExecuteCondition()) {
            this.mExecStartTime = System.currentTimeMillis();
            super.execute();
        }
    }

    @Override // com.miui.gallery.net.base.VolleyRequest, com.miui.gallery.net.base.BaseRequest
    public final Object[] executeSync() throws RequestError {
        if (checkExecuteCondition()) {
            return super.executeSync();
        }
        throw this.mRequestError;
    }

    public final boolean checkExecuteCondition() {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            handleError(ErrorCode.NETWORK_NOT_CONNECTED, "CTA not confirmed.", null);
            return false;
        } else if (isUseCache() || BaseNetworkUtils.isNetworkConnected()) {
            return true;
        } else {
            handleError(ErrorCode.NETWORK_NOT_CONNECTED, "Network not connected.", null);
            return false;
        }
    }

    @Override // com.miui.gallery.net.json.BaseJsonRequest, com.miui.gallery.net.base.BaseRequest, com.miui.gallery.net.base.ResponseErrorHandler
    public void onRequestError(ErrorCode errorCode, String str, Object obj) {
        long currentTimeMillis = System.currentTimeMillis() - this.mExecStartTime;
        deliverError(errorCode, str, obj);
        DefaultLogger.w("BaseGalleryRequest", "%s onRequestError:%s | %s ", getClass().getSimpleName(), errorCode, str);
        if (obj instanceof Throwable) {
            DefaultLogger.w("BaseGalleryRequest", (Throwable) obj);
            SamplingStatHelper.trackHttpEvent(getUrl(), currentTimeMillis, 0L, errorCode.CODE, obj.getClass().getSimpleName());
            return;
        }
        if (obj != null) {
            DefaultLogger.d("BaseGalleryRequest", obj.toString());
        }
        SamplingStatHelper.trackHttpEvent(getUrl(), currentTimeMillis, 0L, errorCode.CODE);
    }
}
