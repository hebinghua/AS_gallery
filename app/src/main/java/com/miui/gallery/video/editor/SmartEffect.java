package com.miui.gallery.video.editor;

import com.google.common.collect.ImmutableMap;
import com.miui.gallery.R;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.video.editor.model.SmartEffectLocalResource;
import com.miui.gallery.video.editor.model.VideoEditorTemplateBaseModel;
import com.miui.gallery.video.editor.util.ToolsUtil;
import com.nexstreaming.nexeditorsdk.nexTemplateManager;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class SmartEffect extends VideoEditorTemplateBaseModel {
    public static final HashMap<String, Integer> smartEffectNames = new HashMap<>(ImmutableMap.builder().put("smart_effect_none", Integer.valueOf((int) R.string.video_editor_smart_effect_none)).put("smart_effect_movie", Integer.valueOf((int) R.string.video_editor_smart_effect_movie)).put("smart_effect_big_film", Integer.valueOf((int) R.string.video_editor_smart_effect_big_film)).put("smart_effect_street", Integer.valueOf((int) R.string.video_editor_smart_effect_street)).put("smart_effect_halo", Integer.valueOf((int) R.string.video_editor_smart_effect_halo)).put("smart_effect_radical", Integer.valueOf((int) R.string.video_editor_smart_effect_radical)).put("smart_effect_freeze", Integer.valueOf((int) R.string.video_editor_smart_effect_freesze)).put("smart_effect_dynamic", Integer.valueOf((int) R.string.video_editor_smart_effect_dynamic)).build());
    public final String TAG = "SmartEffect";
    public int mBgColor;
    public String mEnName;
    public boolean mHasSpeedChange;
    public int mIconResId;
    public int mLongTime;
    public int mNameResId;
    public int mShortTime;
    public nexTemplateManager.Template mTemplate;

    public SmartEffect() {
    }

    public SmartEffect(LocalResource localResource) {
        if (localResource != null) {
            this.mID = localResource.id;
            this.mNameKey = localResource.nameKey;
            this.mLabel = localResource.label;
            this.mIconResId = localResource.imageId;
            this.mIconUrl = localResource.icon;
            String str = localResource.type;
            this.mType = str;
            boolean equals = "ve_type_extra".equals(str);
            this.mExtra = equals;
            if (!equals) {
                this.mDownloadState = 17;
            }
            if (localResource instanceof SmartEffectLocalResource) {
                SmartEffectLocalResource smartEffectLocalResource = (SmartEffectLocalResource) localResource;
                this.mAssetId = ToolsUtil.parseIntFromStr(smartEffectLocalResource.assetId);
                this.mAssetName = smartEffectLocalResource.assetName;
                this.mEnName = smartEffectLocalResource.enName;
                this.mLongTime = ToolsUtil.parseIntFromStr(smartEffectLocalResource.longTime);
                this.mShortTime = ToolsUtil.parseIntFromStr(smartEffectLocalResource.shortTime);
                this.mHasSpeedChange = ToolsUtil.parseIntFromStr(smartEffectLocalResource.hasSpeedChange) != 1 ? false : true;
            }
            this.mIsTemplate = this.mExtra;
        }
    }

    public SmartEffect(String str, int i, String str2, boolean z) {
        this.mNameKey = str;
        this.mIconResId = i;
        this.mType = str2;
        boolean equals = "ve_type_extra".equals(str2);
        this.mExtra = equals;
        if (!equals) {
            this.mDownloadState = 17;
        }
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public void setTemplate(nexTemplateManager.Template template) {
        this.mTemplate = template;
    }

    public nexTemplateManager.Template getTemplate() {
        return this.mTemplate;
    }

    public int getNameResId() {
        HashMap<String, Integer> hashMap;
        if (this.mNameResId == 0 && (hashMap = smartEffectNames) != null && hashMap.containsKey(this.mNameKey)) {
            this.mNameResId = hashMap.get(this.mNameKey).intValue();
        }
        return this.mNameResId;
    }

    public int getShortTime() {
        return this.mShortTime;
    }

    public int getLongTime() {
        return this.mLongTime;
    }

    public boolean isLimitSixtySeconds() {
        return this.mLongTime == 60000;
    }

    public boolean isLimitFourtySeconds() {
        return this.mLongTime == 40000;
    }

    public boolean isHasSpeedChange() {
        return this.mHasSpeedChange;
    }

    public String getEnName() {
        return this.mEnName;
    }

    public void setBgColor(int i) {
        this.mBgColor = i;
    }

    public int getBgColor() {
        return this.mBgColor;
    }

    public void setIconResId(int i) {
        this.mIconResId = i;
    }
}
