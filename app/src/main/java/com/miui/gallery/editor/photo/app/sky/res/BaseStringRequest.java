package com.miui.gallery.editor.photo.app.sky.res;

import ch.qos.logback.classic.spi.CallerData;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.miui.gallery.net.base.VolleyRequest;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public abstract class BaseStringRequest<T> extends VolleyRequest<String, T> {
    public Map<String, String> mHeaders;
    public int mMethod;
    public String mUrl;

    public abstract byte[] getRequestBody();

    public String getRequestBodyContentType() {
        return "application/json; charset=utf-8";
    }

    public String getRequestParamsEncoding() {
        return "application/json; charset=utf-8";
    }

    public BaseStringRequest(int i, String str) {
        this.mUrl = null;
        this.mMethod = 1;
        this.mUrl = str;
        this.mMethod = i;
    }

    public final void addHeader(String str, String str2) {
        if (this.mHeaders == null) {
            this.mHeaders = new HashMap();
        }
        this.mHeaders.put(str, str2);
    }

    @Override // com.miui.gallery.net.base.VolleyRequest
    public final Request<String> createVolleyRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String str = this.mUrl;
        String appendUrlParams = appendUrlParams();
        int i = this.mMethod;
        return new StringRequest(i, i == 0 ? appendUrlParams : str, listener, errorListener) { // from class: com.miui.gallery.editor.photo.app.sky.res.BaseStringRequest.1
            @Override // com.android.volley.Request
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                if (BaseStringRequest.this.mHeaders != null) {
                    hashMap.putAll(BaseStringRequest.this.mHeaders);
                }
                return hashMap;
            }

            @Override // com.android.volley.Request
            public byte[] getBody() throws AuthFailureError {
                return BaseStringRequest.this.getRequestBody();
            }

            @Override // com.android.volley.Request
            public String getBodyContentType() {
                return BaseStringRequest.this.getRequestBodyContentType();
            }

            @Override // com.android.volley.Request
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null) {
                    headers = new HashMap<>();
                }
                if (BaseStringRequest.this.mParams != null) {
                    headers.putAll(BaseStringRequest.this.mParams);
                }
                return super.getParams();
            }

            @Override // com.android.volley.Request
            public String getParamsEncoding() {
                return BaseStringRequest.this.getRequestParamsEncoding();
            }
        };
    }

    public final String appendUrlParams() {
        Map<String, String> map;
        if (this.mUrl != null && (map = this.mParams) != null && !map.isEmpty()) {
            StringBuilder sb = new StringBuilder(this.mUrl);
            if (this.mUrl.indexOf(63) >= 0) {
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
            DefaultLogger.d("Encoding not supported: " + str, (Throwable) e);
            return null;
        }
    }
}
