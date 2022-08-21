package com.miui.gallery.vlog.clip;

import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.vlog.entity.TransData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.VlogPreference;
import com.miui.gallery.vlog.tools.VlogUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TransRequest extends BaseResourceRequest {
    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        return 13341661432578144L;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        try {
            ((TransData) localResource).setTemplateKey(new JSONObject(localResource.extra).optString("templateKey"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void checkResourceVersionAndClear() {
        VlogUtils.checkResourceVersionAndClear(VlogPreference.PrefKeys.VLOG_TRANSITION_VERSION, 13341661432578144L, VlogConfig.TRANS_PATH);
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        return new TransData();
    }
}
