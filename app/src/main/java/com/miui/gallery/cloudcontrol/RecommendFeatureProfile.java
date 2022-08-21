package com.miui.gallery.cloudcontrol;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class RecommendFeatureProfile extends FeatureProfile {
    @SerializedName("sublist")
    public ArrayList<RecommendItem> mSublist;

    @Override // com.miui.gallery.cloudcontrol.FeatureProfile
    public String getName() {
        return "recommendation";
    }

    @Override // com.miui.gallery.cloudcontrol.FeatureProfile
    public String getStrategy() {
        HashMap hashMap = new HashMap();
        hashMap.put("sublist", this.mSublist);
        return new Gson().toJson(hashMap);
    }
}
