package com.miui.gallery.cloudcontrol;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CloudControlResponse {
    @SerializedName("expairAt")
    private long mExpireAt;
    @SerializedName("modules")
    private ArrayList<FeatureProfile> mFeatureProfiles;
    @SerializedName("syncToken")
    private String mSyncToken;

    public String getSyncToken() {
        return this.mSyncToken;
    }

    public ArrayList<FeatureProfile> getFeatureProfiles() {
        return this.mFeatureProfiles;
    }

    public String toString() {
        return "CloudControlResponse{mSyncToken='" + this.mSyncToken + CoreConstants.SINGLE_QUOTE_CHAR + ", mExpireAt=" + this.mExpireAt + ", mFeatureProfiles=" + this.mFeatureProfiles + '}';
    }
}
