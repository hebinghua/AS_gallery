package com.miui.gallery.vlog.base.net;

import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.vlog.entity.FilterData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.VlogPreference;
import com.miui.gallery.vlog.tools.VlogUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class FilterRequest extends BaseResourceRequest {
    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        return 14819322090487872L;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        try {
            ((FilterData) localResource).setTemplateKey(new JSONObject(localResource.extra).optString("template_key"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void checkResourceVersionAndClear() {
        VlogUtils.checkResourceVersionAndClear(VlogPreference.PrefKeys.VLOG_FILTER_VERSION, 14819322090487872L, VlogConfig.FILTER_PATH);
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        return new FilterData();
    }
}
