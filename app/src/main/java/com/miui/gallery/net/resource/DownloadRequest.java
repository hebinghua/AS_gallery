package com.miui.gallery.net.resource;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.net.BaseGalleryRequest;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class DownloadRequest extends BaseGalleryRequest {
    public DownloadRequest(long j) {
        super(0, "https://i.mi.com/gallery/public/resource/download");
        addParam("id", String.valueOf(j));
        setUseCache(false);
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.url = jSONObject.getString(MapBundleKey.MapObjKey.OBJ_URL);
        downloadInfo.sha1 = jSONObject.getString("sha1Base16");
        deliverResponse(downloadInfo);
    }
}
