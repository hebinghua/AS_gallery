package com.miui.gallery.video.editor.model;

/* loaded from: classes2.dex */
public class VideoEditorBaseModel {
    public boolean mExtra;
    public String mFileName;
    public long mID;
    public String mIconUrl;
    public boolean mIsTemplate;
    public String mNameKey;
    public String mType;
    public String mUnZipPath;
    public int mDownloadState = 19;
    public String mLabel = "";

    public String getLabel() {
        return this.mLabel;
    }

    public long getId() {
        return this.mID;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public void setUnZipPath(String str) {
        this.mUnZipPath = str;
    }

    public void setDownloadState(int i) {
        this.mDownloadState = i;
    }

    public boolean isDownloadSuccess() {
        return this.mDownloadState == 0;
    }

    public boolean isDownloaded() {
        return this.mDownloadState == 17;
    }

    public int getDownloadState() {
        return this.mDownloadState;
    }

    public boolean isLocal() {
        return "ve_type_local".equals(this.mType);
    }

    public boolean isCustom() {
        return "ve_type_custom".equals(this.mType);
    }

    public boolean isNone() {
        return "ve_type_none".equals(this.mType);
    }

    public boolean isExtra() {
        return this.mExtra;
    }

    public boolean isTemplate() {
        return this.mIsTemplate;
    }
}
