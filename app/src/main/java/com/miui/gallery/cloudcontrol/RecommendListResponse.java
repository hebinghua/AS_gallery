package com.miui.gallery.cloudcontrol;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RecommendListResponse extends CloudControlResponse {
    @SerializedName("recList")
    private ArrayList<RecommendFeatureProfile> mRecommendFeatureProfiles;

    @Override // com.miui.gallery.cloudcontrol.CloudControlResponse
    public ArrayList<FeatureProfile> getFeatureProfiles() {
        return this.mRecommendFeatureProfiles;
    }
}
