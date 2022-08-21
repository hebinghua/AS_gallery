package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.xiaomi.stat.MiStat;

/* loaded from: classes.dex */
public class SearchStrategy extends BaseStrategy {
    @SerializedName("search-data")
    private String mAIAlbum;
    public boolean mAIAlbumEnabled;
    @SerializedName("search-box")
    private String mSearchBar;
    public boolean mSearchBarEnabled;

    public SearchStrategy(boolean z, boolean z2) {
        FeatureProfile.Status status = FeatureProfile.Status.UNAVAILABLE;
        this.mSearchBar = status.getValue();
        this.mAIAlbum = status.getValue();
        this.mSearchBarEnabled = false;
        this.mAIAlbumEnabled = false;
        this.mSearchBarEnabled = z;
        this.mAIAlbumEnabled = z2;
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        FeatureProfile.Status status = FeatureProfile.Status.ENABLE;
        boolean equalsIgnoreCase = status.getValue().equalsIgnoreCase(this.mAIAlbum);
        this.mAIAlbumEnabled = equalsIgnoreCase;
        this.mSearchBarEnabled = equalsIgnoreCase && status.getValue().equalsIgnoreCase(this.mSearchBar);
    }

    public boolean isSearchBarEnabled() {
        return this.mSearchBarEnabled;
    }

    public boolean isAIAlbumEnabled() {
        return this.mAIAlbumEnabled;
    }

    public static SearchStrategy createDefault() {
        boolean z = CloudControlManager.getInstance().queryFeatureStatus(MiStat.Event.SEARCH) == FeatureProfile.Status.ENABLE;
        return new SearchStrategy(z, z);
    }
}
