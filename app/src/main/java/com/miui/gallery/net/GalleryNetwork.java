package com.miui.gallery.net;

import android.util.Pair;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.json.JsonObjectRequest;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.HttpUtils;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class GalleryNetwork extends BasicNetwork {
    public GalleryNetwork() {
        super(new HurlStack(new GalleryUrlRewriter()));
    }

    @Override // com.android.volley.toolbox.BasicNetwork, com.android.volley.Network
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        String securePost;
        if (!BaseNetworkUtils.isNetworkConnected()) {
            throw new RequestError(ErrorCode.NETWORK_NOT_CONNECTED, "Network not connected.", null);
        }
        int method = request.getMethod();
        if (method >= 1001) {
            try {
                if (!(request instanceof JsonObjectRequest)) {
                    throw new VolleyError("Not support this request type");
                }
                JsonObjectRequest jsonObjectRequest = (JsonObjectRequest) request;
                if (method == 1001) {
                    securePost = com.xiaomi.micloudsdk.request.utils.Request.secureGet(request.getUrl(), appendExtraParams(jsonObjectRequest.getParams()));
                } else if (method == 1002) {
                    securePost = com.xiaomi.micloudsdk.request.utils.Request.securePost(request.getUrl(), appendExtraParams(jsonObjectRequest.getParams()));
                } else {
                    throw new VolleyError("No such method " + method);
                }
                return new NetworkResponse(securePost.getBytes());
            } catch (CloudServerException e) {
                throw new VolleyError(e);
            } catch (Exception e2) {
                throw new VolleyError(e2);
            }
        }
        return super.performRequest(request);
    }

    public static Map<String, String> appendExtraParams(Map<String, String> map) {
        HashMap hashMap;
        if (map != null) {
            hashMap = new HashMap(map);
        } else {
            hashMap = new HashMap();
        }
        Pair<String, String> appLifecycleParameter = HttpUtils.getAppLifecycleParameter();
        hashMap.put((String) appLifecycleParameter.first, (String) appLifecycleParameter.second);
        Pair<String, String> apkVersionParameter = HttpUtils.getApkVersionParameter();
        hashMap.put((String) apkVersionParameter.first, (String) apkVersionParameter.second);
        return hashMap;
    }

    /* loaded from: classes2.dex */
    public static class GalleryUrlRewriter implements HurlStack.UrlRewriter {
        public GalleryUrlRewriter() {
        }

        @Override // com.android.volley.toolbox.HurlStack.UrlRewriter
        public String rewriteUrl(String str) {
            return HttpUtils.appendAppLifecycleParameter(str);
        }
    }
}
