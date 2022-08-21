package com.miui.gallery.util;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.json.BaseJsonRequest;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class NetworkTestUtils {
    public static boolean testDNS(String str) {
        try {
            BaseJsonRequest<JSONObject> baseJsonRequest = new BaseJsonRequest<JSONObject>(1, "http://120.92.96.4:80/dns/public/gallery/log/upload") { // from class: com.miui.gallery.util.NetworkTestUtils.1
                @Override // com.miui.gallery.net.base.VolleyRequest
                public void handleResponse(JSONObject jSONObject) {
                    if (jSONObject != null) {
                        try {
                            if (jSONObject.has("code")) {
                                if (jSONObject.getInt("code") == ErrorCode.SUCCESS.CODE) {
                                    onRequestSuccess(jSONObject);
                                } else {
                                    handleError(ErrorCode.SERVER_ERROR, jSONObject.toString(), jSONObject);
                                }
                            }
                        } catch (Exception e) {
                            handleError(ErrorCode.HANDLE_ERROR, e.getMessage(), e);
                            return;
                        }
                    }
                    handleError(ErrorCode.PARSE_ERROR, "response has no code", null);
                }
            };
            baseJsonRequest.addParam(MapBundleKey.MapObjKey.OBJ_URL, str);
            baseJsonRequest.executeSync();
            return true;
        } catch (RequestError e) {
            e.printStackTrace();
            return false;
        }
    }
}
