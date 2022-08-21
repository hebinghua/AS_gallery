package com.miui.gallery.video.editor.net;

import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.net.resource.BaseResourceRequest;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.video.editor.factory.VideoEditorModuleFactory;
import com.miui.gallery.video.editor.model.SmartEffectLocalResource;
import com.miui.gallery.video.editor.model.VideoEditorBaseLocalResource;
import com.miui.gallery.video.editor.model.WaterMarkLocalResource;
import com.miui.gallery.video.editor.util.ToolsUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class VideoEditorResourceRequest extends BaseResourceRequest {
    public final long RESOURCE_AUDIO_PARENT_ID;
    public final long RESOURCE_SMART_EFFECT_PARENT_ID;
    public final long RESOURCE_TEXT_PARENT_ID;
    public VideoEditorModuleFactory mModuleFactory;

    public VideoEditorResourceRequest(int i, VideoEditorModuleFactory videoEditorModuleFactory) {
        super(i);
        this.RESOURCE_SMART_EFFECT_PARENT_ID = 9506731220402368L;
        this.RESOURCE_AUDIO_PARENT_ID = 9507179096834080L;
        this.RESOURCE_TEXT_PARENT_ID = 9468682483925152L;
        this.mModuleFactory = videoEditorModuleFactory;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public long getParentId() {
        int i = this.type;
        if (i != R.id.video_editor_audio) {
            if (i == R.id.video_editor_smart_effect) {
                return 9506731220402368L;
            }
            return i != R.id.video_editor_water_mark ? 0L : 9468682483925152L;
        }
        return 9507179096834080L;
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public LocalResource newLocalResource() {
        int i = this.type;
        if (i == R.id.video_editor_smart_effect) {
            return new SmartEffectLocalResource(this.mModuleFactory);
        }
        if (i == R.id.video_editor_water_mark) {
            return new WaterMarkLocalResource(this.mModuleFactory);
        }
        return new VideoEditorBaseLocalResource(this.mModuleFactory);
    }

    @Override // com.miui.gallery.net.resource.BaseResourceRequest
    public void setResult(LocalResource localResource) {
        if (localResource instanceof SmartEffectLocalResource) {
            SmartEffectLocalResource smartEffectLocalResource = (SmartEffectLocalResource) localResource;
            try {
                JSONObject jSONObject = new JSONObject(localResource.extra);
                String optString = jSONObject.optString("assetid");
                String optString2 = jSONObject.optString("update");
                String optString3 = jSONObject.optString("assetName");
                String optString4 = jSONObject.optString("longTime");
                String optString5 = jSONObject.optString("shortTime");
                String optString6 = jSONObject.optString("hasSpeedChange");
                String optString7 = jSONObject.optString("enName");
                smartEffectLocalResource.assetId = ToolsUtil.getTrimedString(optString);
                smartEffectLocalResource.update = ToolsUtil.getTrimedString(optString2);
                smartEffectLocalResource.assetName = ToolsUtil.getTrimedString(optString3);
                smartEffectLocalResource.longTime = ToolsUtil.getTrimedString(optString4);
                smartEffectLocalResource.shortTime = ToolsUtil.getTrimedString(optString5);
                smartEffectLocalResource.hasSpeedChange = ToolsUtil.getTrimedString(optString6);
                smartEffectLocalResource.enName = ToolsUtil.getTrimedString(optString7);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (localResource instanceof WaterMarkLocalResource) {
            WaterMarkLocalResource waterMarkLocalResource = (WaterMarkLocalResource) localResource;
            try {
                JSONObject jSONObject2 = new JSONObject(localResource.extra);
                String optString8 = jSONObject2.optString("assetid");
                String optString9 = jSONObject2.optString("update");
                String optString10 = jSONObject2.optString("assetName");
                String optString11 = jSONObject2.optString("cn");
                String str = "";
                waterMarkLocalResource.assetId = TextUtils.isEmpty(optString8) ? str : optString8.trim();
                waterMarkLocalResource.update = TextUtils.isEmpty(optString9) ? str : optString9.trim();
                if (!TextUtils.isEmpty(optString10)) {
                    str = optString10.trim();
                }
                waterMarkLocalResource.assetName = str;
                waterMarkLocalResource.isInternational = ToolsUtil.parseIntFromStr(ToolsUtil.getTrimedString(optString11));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }
}
