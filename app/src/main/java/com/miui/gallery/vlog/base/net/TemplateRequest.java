package com.miui.gallery.vlog.base.net;

import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.template.TemplateProfileUtils;
import com.miui.gallery.vlog.template.TemplateResource;
import com.miui.gallery.vlog.tools.VlogPreference;
import com.miui.gallery.vlog.tools.VlogUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TemplateRequest extends BaseResourceRequest {
    public static final long TEMPLATE_PARENT_ID = TemplateProfileUtils.getTemplateId();

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        return TEMPLATE_PARENT_ID;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        try {
            ((TemplateResource) localResource).setNameColor(new JSONObject(localResource.extra).optString("nameColor"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void checkResourceVersionAndClear() {
        VlogUtils.checkResourceVersionAndClear(VlogPreference.PrefKeys.VLOG_TEMPLATE_VERSION, TEMPLATE_PARENT_ID, VlogConfig.TEMPALTE_PATH);
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        return new TemplateResource();
    }
}
