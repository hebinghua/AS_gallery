package com.miui.gallery.vlog.base.net;

import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.vlog.entity.HeaderTailData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.VlogPreference;
import com.miui.gallery.vlog.tools.VlogUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TitleStyleRequest extends BaseResourceRequest {
    public static final long TITLE_STYLE_PARENT_ID;

    static {
        TITLE_STYLE_PARENT_ID = BaseBuildUtil.isInternational() ? 15975393516257440L : 16352630088269984L;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        return TITLE_STYLE_PARENT_ID;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        try {
            JSONObject jSONObject = new JSONObject(localResource.extra);
            String optString = jSONObject.optString("assetid");
            String optString2 = jSONObject.optString("assetName");
            localResource.assetId = optString;
            localResource.assetName = optString2;
            ((HeaderTailData) localResource).mainTitleNumber = jSONObject.optInt("mainTitleNumber");
            ((HeaderTailData) localResource).subTitleNumber = jSONObject.optInt("subTitleNumber");
            ((HeaderTailData) localResource).setTemplateKey(localResource.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void checkResourceVersionAndClear() {
        VlogUtils.checkResourceVersionAndClear(VlogPreference.PrefKeys.VLOG_HEADERTAIL_VERSION, TITLE_STYLE_PARENT_ID, VlogConfig.HEADER_TAIL_ASSET_PATH);
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        return new HeaderTailData();
    }
}
