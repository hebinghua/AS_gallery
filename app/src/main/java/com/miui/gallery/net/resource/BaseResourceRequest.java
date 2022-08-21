package com.miui.gallery.net.resource;

import ch.qos.logback.core.joran.action.Action;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.gson.JsonParseException;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public abstract class BaseResourceRequest extends BaseGalleryRequest {
    public int type;

    public void checkResourceVersionAndClear() {
    }

    public abstract long getParentId();

    public abstract LocalResource newLocalResource();

    public abstract void setResult(LocalResource localResource);

    public BaseResourceRequest() {
        super(0, "https://i.mi.com/gallery/public/resource/info");
        addParam("id", Long.toString(getParentId()));
        setUseCache(true);
        setCacheExpires(2592000000L);
        setCacheSoftTtl(System.currentTimeMillis() + 86400000);
    }

    public BaseResourceRequest(int i) {
        super(0, "https://i.mi.com/gallery/public/resource/info");
        this.type = i;
        addParam("id", Long.toString(getParentId()));
        setUseCache(true);
        setCacheExpires(2592000000L);
        setCacheSoftTtl(System.currentTimeMillis() + 86400000);
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        try {
            ArrayList arrayList = new ArrayList();
            JSONArray jSONArray = jSONObject.getJSONArray("galleryResourceInfoList");
            long optLong = jSONObject.optLong("expireAt");
            if (optLong != 0) {
                setCacheSoftTtl(optLong);
            }
            if (jSONArray != null) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    LocalResource newLocalResource = newLocalResource();
                    newLocalResource.id = jSONObject2.optLong("id");
                    newLocalResource.parent = jSONObject2.optLong("id");
                    newLocalResource.icon = jSONObject2.optString(CallMethod.RESULT_ICON);
                    newLocalResource.label = jSONObject2.optString("text");
                    newLocalResource.extra = jSONObject2.optString("extraInfo");
                    newLocalResource.type = jSONObject2.optString(nexExportFormat.TAG_FORMAT_TYPE);
                    newLocalResource.size = jSONObject2.optLong(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
                    newLocalResource.key = jSONObject2.optString(Action.KEY_ATTRIBUTE);
                    JSONObject jSONObject3 = new JSONObject(newLocalResource.extra);
                    newLocalResource.nameKey = jSONObject3.optString("nameKey");
                    newLocalResource.index = jSONObject3.optInt(MapBundleKey.MapObjKey.OBJ_SL_INDEX);
                    setResult(newLocalResource);
                    arrayList.add(newLocalResource);
                }
            }
            deliverResponse(arrayList);
        } catch (JsonParseException e) {
            e.printStackTrace();
            deliverError(ErrorCode.PARSE_ERROR, e.getMessage(), jSONObject);
        } catch (Exception e2) {
            e2.printStackTrace();
            deliverError(ErrorCode.HANDLE_ERROR, e2.getMessage(), jSONObject);
        }
    }

    @Override // com.miui.gallery.net.base.BaseRequest
    public void deliverResponse(Object... objArr) {
        checkResourceVersionAndClear();
        super.deliverResponse(objArr);
    }
}
