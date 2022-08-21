package com.miui.gallery.vlog.base.net;

import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.vlog.audio.AudioResource;
import com.miui.gallery.vlog.entity.AudioData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.VlogPreference;
import com.miui.gallery.vlog.tools.VlogUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AudioRequest extends BaseResourceRequest {
    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        return 14808325639176192L;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        try {
            ((AudioResource) localResource).setNameColor(new JSONObject(localResource.extra).optString("nameColor"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void checkResourceVersionAndClear() {
        VlogUtils.checkResourceVersionAndClear(VlogPreference.PrefKeys.VLOG_AUDIO_VERSION, 14808325639176192L, VlogConfig.AUDIO_PATH);
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        return new AudioData();
    }
}
