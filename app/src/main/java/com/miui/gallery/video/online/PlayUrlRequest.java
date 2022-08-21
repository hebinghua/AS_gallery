package com.miui.gallery.video.online;

import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PlayUrlRequest extends BaseGalleryRequest {
    public PlayUrlRequest(String str) {
        super(1001, HostManager.OwnerVideo.getPlayInfoUrl(str));
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        String optString = jSONObject != null ? jSONObject.optString("play_url") : "";
        DefaultLogger.d("PlayUrlRequest", "return data %s", jSONObject);
        deliverResponse(optString);
    }

    public String request() throws UrlRequestError {
        try {
            return (String) executeSync()[0];
        } catch (RequestError e) {
            Object responseData = e.getResponseData();
            Error error = Error.UNKNOWN;
            String str = "";
            if (responseData != null) {
                try {
                    JSONObject jSONObject = new JSONObject(responseData.toString());
                    str = jSONObject.optString("reason");
                    error = packageError(jSONObject.optInt("code"));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            throw new UrlRequestError(str, error);
        }
    }

    public final Error packageError(int i) {
        Error[] values;
        for (Error error : Error.values()) {
            if (error.CODE == i) {
                return error;
            }
        }
        return Error.UNKNOWN;
    }
}
