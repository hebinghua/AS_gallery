package com.miui.gallery.cloudcontrol;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.json.BaseJsonRequest;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RecommendListRequest extends BaseGalleryRequest {
    public RecommendListRequest(Builder builder) {
        super(builder.getMethod(), builder.getUrl());
        addParam("lang", String.format("%s_%s", Locale.getDefault().getLanguage(), Locale.getDefault().getCountry()));
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        try {
            deliverResponse((RecommendListResponse) BaseJsonRequest.fromJson(jSONObject.toString(), new TypeToken<RecommendListResponse>() { // from class: com.miui.gallery.cloudcontrol.RecommendListRequest.1
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
        public int mMethod = 1001;
        public String mUrl;

        public int getMethod() {
            return this.mMethod;
        }

        public Builder setMethod(boolean z) {
            if (z) {
                this.mMethod = 1001;
            } else {
                this.mMethod = 0;
            }
            return this;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public Builder setUrl(String str) {
            this.mUrl = str;
            return this;
        }

        public RecommendListRequest build() {
            return new RecommendListRequest(this);
        }
    }
}
