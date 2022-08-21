package com.miui.gallery.net.hardwareauth;

import android.content.Context;
import android.util.Base64;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.miai.api.AuthHardwareAuth;
import com.xiaomi.miai.api.common.APIUtils;
import javax.net.ssl.SSLHandshakeException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class HardwareAuthTokenManager {
    public static String BID_PARAM = "bid";
    public static String FID_PARAM = "fid";
    public static String ORG_ID = "CL20652";
    public static String ORG_ID_PARAM = "orgId";
    public static String SID = "miai-sod";
    public static String SID_PARAM = "sid";
    public static String SIGN_PARAM = "sign";
    public static String mBid;

    public static String getAuthTokenSync(Context context, boolean z, int i, String str) {
        String authenTokenString;
        mBid = str;
        return (z || (authenTokenString = BaseGalleryPreferences.AuthenToken.getAuthenTokenString()) == null) ? generateAuthenToken(context, i, false) : authenTokenString;
    }

    public static String generateAuthenToken(Context context, int i, boolean z) {
        NetworkResponse networkResponse;
        String fidFromCloudService = DeviceCredentialManager.getFidFromCloudService(context);
        if (fidFromCloudService == null) {
            DefaultLogger.e("HardwareAuthTokenManager", "credential sign is null");
            return null;
        }
        try {
            Object[] executeSync = new GetNonceRequest(fidFromCloudService, i).executeSync();
            if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof AuthHardwareAuth.NonceResponse)) {
                String nonce = ((AuthHardwareAuth.NonceResponse) executeSync[0]).getNonce();
                String nonceCert = ((AuthHardwareAuth.NonceResponse) executeSync[0]).getNonceCert();
                if (nonce != null && nonceCert != null) {
                    return internalGetAuthToken(context, fidFromCloudService, nonce, nonceCert, i, false);
                }
            }
            return null;
        } catch (RequestError e) {
            if ((e.getResponseData() instanceof SSLHandshakeException) && !z) {
                return generateAuthenToken(context, i, true);
            }
            Object responseData = e.getResponseData();
            if (responseData != null && (responseData instanceof VolleyError) && (networkResponse = ((VolleyError) responseData).networkResponse) != null) {
                DefaultLogger.e("HardwareAuthTokenManager", "nonce request error %s", Integer.valueOf(networkResponse.statusCode));
            }
            return null;
        }
    }

    public static String internalGetAuthToken(Context context, String str, String str2, String str3, int i, boolean z) {
        NetworkResponse networkResponse;
        String signSyncFromCloudService = DeviceCredentialManager.signSyncFromCloudService(context, Base64.decode(str2, 8));
        if (signSyncFromCloudService == null) {
            DefaultLogger.e("HardwareAuthTokenManager", "credential sign is null");
            return null;
        }
        try {
            Object[] executeSync = new GetAuthTokenRequest(signSyncFromCloudService, str, str2, str3, i).executeSync();
            if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof AuthHardwareAuth.HardwareTokenResponse)) {
                return ((AuthHardwareAuth.HardwareTokenResponse) executeSync[0]).getToken();
            }
            return null;
        } catch (RequestError e) {
            if ((e.getResponseData() instanceof SSLHandshakeException) && !z) {
                return internalGetAuthToken(context, str, str2, str3, i, true);
            }
            Object responseData = e.getResponseData();
            if (responseData != null && (responseData instanceof VolleyError) && (networkResponse = ((VolleyError) responseData).networkResponse) != null) {
                DefaultLogger.e("HardwareAuthTokenManager", "authen token request error %s", Integer.valueOf(networkResponse.statusCode));
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class GetNonceRequest extends OCRRelatedRequest {
        @Override // com.miui.gallery.net.hardwareauth.OCRRelatedRequest
        public boolean checkStatus(JSONObject jSONObject) {
            return true;
        }

        public GetNonceRequest(String str, int i) {
            super(0, "https://api.open.ai.xiaomi.com/auth/v1/hardwareNonce");
            addHeader("User-Agent", String.format("MiuiGallery/%s", Integer.valueOf(i)));
            addParam(HardwareAuthTokenManager.FID_PARAM, str);
            addParam(HardwareAuthTokenManager.ORG_ID_PARAM, HardwareAuthTokenManager.ORG_ID);
            addParam(HardwareAuthTokenManager.SID_PARAM, HardwareAuthTokenManager.SID);
            addParam(HardwareAuthTokenManager.BID_PARAM, HardwareAuthTokenManager.mBid);
        }

        @Override // com.miui.gallery.net.hardwareauth.OCRRelatedRequest
        public void processData(JSONObject jSONObject) {
            AuthHardwareAuth.NonceResponse nonceResponse = (AuthHardwareAuth.NonceResponse) APIUtils.fromJsonString(jSONObject.toString(), AuthHardwareAuth.NonceResponse.class);
            if (nonceResponse != null) {
                deliverResponse(nonceResponse);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class GetAuthTokenRequest extends OCRRelatedRequest {
        @Override // com.miui.gallery.net.hardwareauth.OCRRelatedRequest
        public boolean checkStatus(JSONObject jSONObject) {
            return true;
        }

        public GetAuthTokenRequest(String str, String str2, String str3, String str4, int i) {
            super(0, "https://api.open.ai.xiaomi.com/auth/v1/hardwareAuthToken");
            addHeader("User-Agent", String.format("MiuiGallery/%s", Integer.valueOf(i)));
            addParam(HardwareAuthTokenManager.FID_PARAM, str2);
            addParam("nonce", str3);
            addParam("nonceCert", str4);
            addParam(HardwareAuthTokenManager.ORG_ID_PARAM, HardwareAuthTokenManager.ORG_ID);
            addParam(HardwareAuthTokenManager.SIGN_PARAM, str);
            addParam(HardwareAuthTokenManager.SID_PARAM, HardwareAuthTokenManager.SID);
            addParam(HardwareAuthTokenManager.BID_PARAM, HardwareAuthTokenManager.mBid);
        }

        @Override // com.miui.gallery.net.hardwareauth.OCRRelatedRequest
        public void processData(JSONObject jSONObject) {
            AuthHardwareAuth.HardwareTokenResponse hardwareTokenResponse = (AuthHardwareAuth.HardwareTokenResponse) APIUtils.fromJsonString(jSONObject.toString(), AuthHardwareAuth.HardwareTokenResponse.class);
            if (hardwareTokenResponse != null) {
                HardwareAuthTokenManager.persistAuthTokenResponse(hardwareTokenResponse);
                deliverResponse(hardwareTokenResponse);
            }
        }
    }

    public static void persistAuthTokenResponse(AuthHardwareAuth.HardwareTokenResponse hardwareTokenResponse) {
        BaseGalleryPreferences.AuthenToken.setAuthenTokenString(hardwareTokenResponse.getToken(), hardwareTokenResponse.getExpiredInMillis());
    }
}
