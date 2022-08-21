package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class OnlineVideoStrategy extends BaseStrategy {
    @SerializedName("enabled")
    private boolean mEnabled = false;
    @SerializedName("show-vip-guide")
    private boolean mShowGuide = false;
    @SerializedName("supported-min-size")
    private long mSupportedMinSize = 0;
    @SerializedName("vip-transcoding-valid-period")
    private long mVipTranscodingValidPeriod = 600000;
    @SerializedName("support-share-media")
    private boolean mSupportShareMedia = false;
    @SerializedName("vip-tip-waiting-time")
    private long mWaitingTimeForShown = 30000;
    @SerializedName("vip-tip-size-limit")
    private long mSizeLimitForShown = 104857600;
    @SerializedName("vip-tip-shown-interval")
    private long mIntervalForShown = 86400000;
    @SerializedName("vip-tip-shown-after-ignored")
    private boolean mShownAfterIgnored = false;

    public static OnlineVideoStrategy createDefault() {
        return new OnlineVideoStrategy();
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isShowGuide() {
        return this.mShowGuide;
    }

    public long getSupportedMinSize() {
        return this.mSupportedMinSize;
    }

    public boolean isSupportShareMedia() {
        return this.mSupportShareMedia;
    }

    public long getWaitingTimeForShown() {
        return this.mWaitingTimeForShown;
    }

    public long getSizeLimitForShown() {
        return this.mSizeLimitForShown;
    }

    public long getIntervalForShown() {
        return this.mIntervalForShown;
    }

    public boolean isShownAfterIgnored() {
        return this.mShownAfterIgnored;
    }
}
