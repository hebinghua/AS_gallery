package com.miui.gallery.vlog.base.net;

import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.vlog.entity.CaptionStyleData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.VlogPreference;
import com.miui.gallery.vlog.tools.VlogUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CaptionStyleRequest extends BaseResourceRequest {
    public static final long CAPTION_STYLE_PARENT_ID;

    static {
        CAPTION_STYLE_PARENT_ID = BaseBuildUtil.isInternational() ? 15062832259072192L : 15062563609968640L;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        return CAPTION_STYLE_PARENT_ID;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        try {
            JSONObject jSONObject = new JSONObject(localResource.extra);
            String optString = jSONObject.optString("assetid");
            String optString2 = jSONObject.optString("assetName");
            localResource.assetId = optString;
            localResource.assetName = optString2;
            ((CaptionStyleData) localResource).assetSingleId = jSONObject.optString("assetSingleId");
            ((CaptionStyleData) localResource).assetSingleName = jSONObject.optString("assetSingleName");
            ((CaptionStyleData) localResource).assetDoubleId = jSONObject.optString("assetDoubleId");
            ((CaptionStyleData) localResource).assetDoubleName = jSONObject.optString("assetDoubleName");
            ((CaptionStyleData) localResource).iconColor = jSONObject.optString("iconColor");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest, com.miui.gallery.net.base.BaseRequest
    public void deliverResponse(Object... objArr) {
        VlogUtils.checkResourceVersionAndClear(VlogPreference.PrefKeys.VLOG_CAPTION_VERSION, CAPTION_STYLE_PARENT_ID, VlogConfig.CAPTION_ASSET_PATH);
        super.deliverResponse(objArr);
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        return new CaptionStyleData();
    }
}
