package com.miui.gallery.net.hardwareauth;

import com.miui.gallery.net.json.BaseJsonRequest;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public abstract class OCRRelatedRequest extends BaseJsonRequest<JSONObject> {
    public abstract boolean checkStatus(JSONObject jSONObject);

    public abstract void processData(JSONObject jSONObject);

    public OCRRelatedRequest(int i, String str) {
        super(i, str);
    }

    @Override // com.miui.gallery.net.base.VolleyRequest
    public void handleResponse(JSONObject jSONObject) {
        if (jSONObject != null && checkStatus(jSONObject.optJSONObject("status"))) {
            processData(jSONObject.optJSONObject("data"));
        }
    }
}
