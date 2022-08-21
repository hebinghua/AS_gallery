package com.miui.gallery.video.editor;

import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.video.editor.model.VideoEditorTemplateBaseModel;
import com.miui.gallery.video.editor.model.WaterMarkLocalResource;
import com.miui.gallery.video.editor.util.ToolsUtil;

/* loaded from: classes2.dex */
public class TextStyle extends VideoEditorTemplateBaseModel {
    public String mAssetName;
    public int mBgColor;
    public int mIconResId;
    public String mTemplateId;

    public TextStyle() {
    }

    public TextStyle(LocalResource localResource) {
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
            this.mIsTemplate = equals;
            if (localResource instanceof WaterMarkLocalResource) {
                WaterMarkLocalResource waterMarkLocalResource = (WaterMarkLocalResource) localResource;
                this.mAssetId = ToolsUtil.parseIntFromStr(waterMarkLocalResource.assetId);
                this.mAssetName = waterMarkLocalResource.assetName;
            }
            if (!this.mExtra) {
                this.mDownloadState = 17;
            }
            this.mLabel = localResource.label;
        }
    }

    public TextStyle(int i, String str) {
        this.mIconResId = i;
        this.mType = str;
        boolean equals = "ve_type_extra".equals(str);
        this.mExtra = equals;
        if (!equals) {
            this.mDownloadState = 17;
        }
    }

    public int getIconResId() {
        return this.mIconResId;
    }

    public void setTemplateId(String str) {
        this.mTemplateId = str;
    }

    public String getTemplateId() {
        return this.mTemplateId;
    }

    @Override // com.miui.gallery.video.editor.model.VideoEditorTemplateBaseModel
    public String getAssetName() {
        return this.mAssetName;
    }

    public void setIconResId(int i) {
        this.mIconResId = i;
    }

    public int getBgColor() {
        return this.mBgColor;
    }

    public void setBgColor(int i) {
        this.mBgColor = i;
    }
}
