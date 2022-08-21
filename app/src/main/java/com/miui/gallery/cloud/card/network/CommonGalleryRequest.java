package com.miui.gallery.cloud.card.network;

import com.google.gson.JsonParseException;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.json.BaseJsonRequest;
import java.lang.reflect.Type;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CommonGalleryRequest extends BaseGalleryRequest {
    public Type mDataType;

    public CommonGalleryRequest(int i, String str, Type type) {
        super(i, str);
        this.mDataType = type;
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        try {
            if (jSONObject != null) {
                deliverResponse(BaseJsonRequest.fromJson(jSONObject.toString(), this.mDataType));
            } else {
                deliverError(ErrorCode.BODY_EMPTY, null, null);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            deliverError(ErrorCode.PARSE_ERROR, e.getMessage(), jSONObject);
        } catch (Exception e2) {
            e2.printStackTrace();
            deliverError(ErrorCode.HANDLE_ERROR, e2.getMessage(), jSONObject);
        }
    }
}
