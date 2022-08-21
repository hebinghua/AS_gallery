package com.miui.gallery.cloudcontrol;

import ch.qos.logback.core.joran.action.Action;
import com.google.gson.annotations.SerializedName;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes.dex */
public class RecommendItem {
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName(Action.KEY_ATTRIBUTE)
    private String mKey;
    @SerializedName("landingUrl")
    private String mLandingUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("seqId")
    private String mSeqId;
    @SerializedName("status")
    private String mStatus;
    @SerializedName(nexExportFormat.TAG_FORMAT_TYPE)
    private String mType;

    public String getName() {
        return this.mName;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public String getLandingUrl() {
        return this.mLandingUrl;
    }

    public String getSeqId() {
        return this.mSeqId;
    }

    public String getKey() {
        return this.mKey;
    }
}
