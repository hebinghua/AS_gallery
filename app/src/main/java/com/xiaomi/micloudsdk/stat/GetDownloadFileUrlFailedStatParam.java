package com.xiaomi.micloudsdk.stat;

import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class GetDownloadFileUrlFailedStatParam {
    public final String description;
    public final String exceptionName;
    public final int httpStatusCode;
    public final String reason;
    public final int responseCode;
    public final String url;

    public GetDownloadFileUrlFailedStatParam(String str, int i, String str2, int i2, String str3, String str4) {
        this.url = str;
        this.httpStatusCode = i;
        this.exceptionName = str2;
        this.responseCode = i2;
        this.description = str3;
        this.reason = str4;
    }

    public static GetDownloadFileUrlFailedStatParam createGetDownloadFileUrlFailedStatParam(String str, int i, String str2, String str3) {
        GetDownloadFileUrlFailedResponse createGetDownloadFileUrlFailedResponse = GetDownloadFileUrlFailedResponse.createGetDownloadFileUrlFailedResponse(str3);
        return new GetDownloadFileUrlFailedStatParam(str, i, str2, createGetDownloadFileUrlFailedResponse.responseCode, createGetDownloadFileUrlFailedResponse.description, createGetDownloadFileUrlFailedResponse.reason);
    }

    public String toString() {
        return "GetDownloadFileUrlFailedStatParam{url='" + this.url + CoreConstants.SINGLE_QUOTE_CHAR + ", httpStatusCode=" + this.httpStatusCode + ", exceptionName='" + this.exceptionName + CoreConstants.SINGLE_QUOTE_CHAR + ", responseCode=" + this.responseCode + ", description='" + this.description + CoreConstants.SINGLE_QUOTE_CHAR + ", reason='" + this.reason + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }

    /* loaded from: classes3.dex */
    public static class GetDownloadFileUrlFailedResponse {
        public final String description;
        public final String reason;
        public final int responseCode;

        public GetDownloadFileUrlFailedResponse(int i, String str, String str2) {
            this.responseCode = i;
            this.description = str;
            this.reason = str2;
        }

        public static GetDownloadFileUrlFailedResponse createGetDownloadFileUrlFailedResponse(String str) {
            if (str == null) {
                str = "";
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                return new GetDownloadFileUrlFailedResponse(jSONObject.optInt("code", -1), jSONObject.optString("description", ""), jSONObject.optString("reason", ""));
            } catch (JSONException e) {
                Log.e("GetDownloadFileUrlFailedStatParam", "createGetDownloadFileUrlFailedResponse: ", e);
                return new GetDownloadFileUrlFailedResponse(-1, "", "");
            }
        }
    }
}
