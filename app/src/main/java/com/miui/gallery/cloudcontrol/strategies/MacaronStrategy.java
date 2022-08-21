package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class MacaronStrategy extends BaseStrategy {
    @SerializedName("macaron_enable")
    private boolean mEnable;
    @SerializedName("intent_action")
    private String mIntentAction;
    @SerializedName("min_version")
    private long mMinVersion;
    @SerializedName("macaron_package_name")
    private String mPhotoPrintPackageName;

    public String getPackageName() {
        return this.mPhotoPrintPackageName;
    }

    public long getMinVersion() {
        return this.mMinVersion;
    }

    public String getIntentAction() {
        return this.mIntentAction;
    }
}
