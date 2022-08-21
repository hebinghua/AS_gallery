package com.miui.gallery.net.json;

import android.text.TextUtils;
import ch.qos.logback.classic.spi.CallerData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.VolleyRequest;
import com.miui.gallery.util.Encode;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public abstract class BaseJsonRequest<T> extends VolleyRequest<JSONObject, T> {
    public Map<String, String> mHeaders;
    public JsonObject mJsonObject;
    public int mMethod;
    public String mUrl;
    public boolean mUseJsonContentType;

    public BaseJsonRequest(int i, String str) {
        this.mUrl = null;
        this.mMethod = 1;
        this.mMethod = i;
        this.mUrl = str;
    }

    public final String getUrl() {
        return this.mUrl;
    }

    public final void addHeader(String str, String str2) {
        if (this.mHeaders == null) {
            this.mHeaders = new HashMap();
        }
        this.mHeaders.put(str, str2);
    }

    public void onRequestSuccess(T t) throws Exception {
        deliverResponse(t);
    }

    @Override // com.miui.gallery.net.base.BaseRequest, com.miui.gallery.net.base.ResponseErrorHandler
    public void onRequestError(ErrorCode errorCode, String str, Object obj) {
        deliverError(errorCode, str, obj);
    }

    @Override // com.miui.gallery.net.base.VolleyRequest
    public final Request<JSONObject> createVolleyRequest(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String str = this.mUrl;
        String appendUrlParams = appendUrlParams();
        if (this.mMethod == 0) {
            str = appendUrlParams;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(this.mMethod, str, listener, errorListener);
        Map<String, String> map = this.mParams;
        if (map != null) {
            jsonObjectRequest.setParams(map);
        }
        Map<String, String> map2 = this.mHeaders;
        if (map2 != null) {
            jsonObjectRequest.setHeaders(map2);
        }
        JsonObject jsonObject = this.mJsonObject;
        if (jsonObject != null) {
            jsonObjectRequest.setJsonBody(jsonObject);
        }
        boolean z = this.mUseJsonContentType;
        if (z) {
            jsonObjectRequest.setUseJsonContentType(z);
        }
        jsonObjectRequest.setCacheKey(generateCacheKey(appendUrlParams));
        return jsonObjectRequest;
    }

    public void setBodyJson(JsonObject jsonObject) {
        this.mJsonObject = jsonObject;
    }

    public void setUseJsonContentType(boolean z) {
        this.mUseJsonContentType = z;
    }

    public static <T> T fromJson(String str, Type type) {
        return (T) new Gson().fromJson(str, type);
    }

    public final String appendUrlParams() {
        Map<String, String> map;
        if (this.mUrl != null && (map = this.mParams) != null && !map.isEmpty()) {
            StringBuilder sb = new StringBuilder(this.mUrl);
            if (this.mUrl.indexOf(63) > 0) {
                if (!this.mUrl.endsWith(CallerData.NA) && !this.mUrl.endsWith("&")) {
                    sb.append("&");
                }
                sb.append(encodeParameters(this.mParams, Keyczar.DEFAULT_ENCODING));
                return sb.toString();
            }
            sb.append(CallerData.NA);
            sb.append(encodeParameters(this.mParams, Keyczar.DEFAULT_ENCODING));
            return sb.toString();
        }
        return this.mUrl;
    }

    public final String encodeParameters(Map<String, String> map, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), str));
                sb.append('=');
                sb.append(URLEncoder.encode(entry.getValue(), str));
                sb.append('&');
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported: " + str, e);
        }
    }

    public final String generateCacheKey(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return Encode.SHA1Encode(str.getBytes(Keyczar.DEFAULT_ENCODING));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }
}
