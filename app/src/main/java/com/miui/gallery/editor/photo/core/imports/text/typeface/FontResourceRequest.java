package com.miui.gallery.editor.photo.core.imports.text.typeface;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class FontResourceRequest extends BaseResourceRequest {
    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        return 14423014048465056L;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        if (localResource instanceof TextStyle) {
            TextStyle textStyle = (TextStyle) localResource;
            try {
                JSONObject jSONObject = new JSONObject(localResource.extra);
                int optInt = jSONObject.optInt(MapBundleKey.MapObjKey.OBJ_SL_INDEX);
                int optInt2 = jSONObject.optInt("darkmode");
                textStyle.setIndex(optInt);
                textStyle.setDarkmode(optInt2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        return new TextStyle();
    }
}
