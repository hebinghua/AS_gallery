package com.miui.gallery.net.json;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.miui.gallery.net.base.Cacheable;
import com.xiaomi.stat.d;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class JsonObjectRequest extends Request<JSONObject> implements Cacheable {
    public static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", "utf-8");
    public String mCacheKey;
    public volatile byte[] mData;
    public Map<String, String> mHeaders;
    public volatile boolean mIsFromCache;
    public JsonObject mJsonObject;
    public Response.Listener<JSONObject> mListener;
    public Map<String, String> mParams;
    public boolean mShouldGzip;
    public boolean mUseJsonContentType;

    public JsonObjectRequest(int i, String str, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mParams = null;
        this.mHeaders = null;
        this.mData = null;
        this.mIsFromCache = false;
        this.mCacheKey = null;
        this.mShouldGzip = false;
        this.mListener = listener;
    }

    public void setParams(Map<String, String> map) {
        this.mParams = map;
    }

    @Override // com.android.volley.Request
    public Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> map = this.mParams;
        return map != null ? map : super.getParams();
    }

    public void setJsonBody(JsonObject jsonObject) {
        this.mJsonObject = jsonObject;
    }

    public void setHeaders(Map<String, String> map) {
        this.mHeaders = map;
        if (map == null || !map.containsKey("Content-Encoding") || !d.aj.equals(map.get("Content-Encoding"))) {
            return;
        }
        this.mShouldGzip = true;
    }

    public void setUseJsonContentType(boolean z) {
        this.mUseJsonContentType = z;
    }

    @Override // com.android.volley.Request
    public String getBodyContentType() {
        if (this.mUseJsonContentType) {
            return PROTOCOL_CONTENT_TYPE;
        }
        return super.getBodyContentType();
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = this.mHeaders;
        return map != null ? map : super.getHeaders();
    }

    @Override // com.android.volley.Request
    public void deliverResponse(JSONObject jSONObject) {
        Response.Listener<JSONObject> listener = this.mListener;
        if (listener != null) {
            listener.onResponse(jSONObject);
        }
    }

    @Override // com.android.volley.Request
    public byte[] getBody() throws AuthFailureError {
        JsonObject jsonObject;
        boolean z = this.mShouldGzip;
        if (!z) {
            return super.getBody();
        }
        if (z && (jsonObject = this.mJsonObject) != null) {
            try {
                return compress(jsonObject.toString().getBytes("utf-8"));
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public final byte[] compress(byte[] bArr) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return byteArray;
        } catch (Exception unused) {
            return new byte[0];
        }
    }

    @Override // com.android.volley.Request
    public Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            this.mIsFromCache = networkResponse.headers.containsKey("From-Cache");
            this.mData = networkResponse.data;
            return Response.success(new JSONObject(new String(networkResponse.data, parseCharset(networkResponse.headers, "utf-8"))), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e2) {
            return Response.error(new ParseError(e2));
        }
    }

    public static String parseCharset(Map<String, String> map, String str) {
        String str2 = map.get("Content-Type");
        if (str2 != null) {
            String[] split = str2.split(";");
            for (int i = 1; i < split.length; i++) {
                String[] split2 = split[i].trim().split("=");
                if (split2.length == 2 && split2[0].equals("charset")) {
                    return split2[1];
                }
            }
        }
        return str;
    }

    @Override // com.android.volley.Request
    public String getCacheKey() {
        String str = this.mCacheKey;
        return str == null ? super.getCacheKey() : str;
    }

    @Override // com.miui.gallery.net.base.Cacheable
    public byte[] getData() {
        return this.mData;
    }

    @Override // com.miui.gallery.net.base.Cacheable
    public boolean isFromCache() {
        return this.mIsFromCache;
    }

    public void setCacheKey(String str) {
        this.mCacheKey = str;
    }
}
