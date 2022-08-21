package com.miui.gallery.cloudcontrol;

import android.os.Build;
import android.text.TextUtils;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.json.BaseJsonRequest;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.Encode;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.security.id.IdentifierManager;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CloudControlRequest extends BaseGalleryRequest {
    public final String ensureNotNull(String str) {
        return str != null ? str : "";
    }

    public CloudControlRequest(Builder builder) {
        super(builder.getMethod(), builder.getUrl());
        addParam("data", ensureNotNull(builder.getDataParam()));
        addParam("syncToken", ensureNotNull(builder.getSyncToken()));
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        try {
            deliverResponse((CloudControlResponse) BaseJsonRequest.fromJson(jSONObject.toString(), new TypeToken<CloudControlResponse>() { // from class: com.miui.gallery.cloudcontrol.CloudControlRequest.1
            }.getType()));
        } catch (JsonParseException e) {
            e.printStackTrace();
            deliverError(ErrorCode.PARSE_ERROR, e.getMessage(), jSONObject);
        } catch (Exception e2) {
            e2.printStackTrace();
            deliverError(ErrorCode.HANDLE_ERROR, e2.getMessage(), jSONObject);
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public String mDataParam;
        public int mMethod = 1002;
        public String mSyncToken;
        public String mUrl;

        public Builder() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("appVersion", String.valueOf(MiscUtil.getAppVersionCode()));
                jSONObject.put("romVersion", "MIUI/" + Build.VERSION.INCREMENTAL);
                String simOperator = MiscUtil.getSimOperator();
                String str = null;
                if (TextUtils.isEmpty(simOperator)) {
                    simOperator = null;
                }
                jSONObject.put("operator", simOperator);
                jSONObject.put("sdkVersion", String.valueOf(Build.VERSION.SDK_INT));
                if (AccountCache.getAccount() != null) {
                    String oaid = IdentifierManager.getOAID(StaticContext.sGetAndroidContext());
                    str = !TextUtils.isEmpty(oaid) ? Encode.SHA1Encode(oaid.getBytes()) : oaid;
                    if (TextUtils.isEmpty(str) && !BaseBuildUtil.isPad()) {
                        CloudControlRequest.reportOAIDIsNull();
                    }
                }
                if (TextUtils.isEmpty(str)) {
                    str = GalleryPreferences.UUID.get();
                }
                jSONObject.put("rateKey", str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.mDataParam = jSONObject.toString();
        }

        public Builder setMethod(int i) {
            this.mMethod = i;
            return this;
        }

        public Builder setUrl(String str) {
            this.mUrl = str;
            return this;
        }

        public String getDataParam() {
            return this.mDataParam;
        }

        public int getMethod() {
            return this.mMethod;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public String getSyncToken() {
            return this.mSyncToken;
        }

        public Builder setSyncToken(String str) {
            this.mSyncToken = str;
            return this;
        }

        public CloudControlRequest build() {
            return new CloudControlRequest(this);
        }
    }

    public static void reportOAIDIsNull() {
        SamplingStatHelper.recordCountEvent("cloud_control", "oaid_is_null", new HashMap());
    }
}
