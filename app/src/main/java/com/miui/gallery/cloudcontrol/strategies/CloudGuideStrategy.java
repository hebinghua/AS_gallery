package com.miui.gallery.cloudcontrol.strategies;

import ch.qos.logback.core.util.FileSize;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class CloudGuideStrategy extends BaseStrategy {
    @SerializedName("life_before_slimmed")
    private long mLifeBeforeSlimmed;
    @SerializedName("slim_immediately_size")
    private long mSlimImmediatelySize;
    @SerializedName("strict_strategy_low_space_percentage")
    private long mStrictStrategyLowSpacePercentage;
    @SerializedName("strict_strategy_media_size_percentage")
    private long mStrictStrategyMediaSizePercentage;
    @SerializedName("strict_strategy_media_size_when_low_space")
    private long mStrictStrategyMediaSizeWhenLowSpace;
    @SerializedName("weak_strategy_image_count")
    private long mWeakStrategyImageCount;
    @SerializedName("weak_strategy_low_space_percentage")
    private long mWeakStrategyLowSpacePercentage;
    @SerializedName("weak_strategy_media_size")
    private long mWeakStrategyMediaSize;
    @SerializedName("weak_strategy_video_count")
    private long mWeakStrategyVideoCount;

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        this.mLifeBeforeSlimmed *= 86400000;
        this.mSlimImmediatelySize *= FileSize.MB_COEFFICIENT;
        this.mWeakStrategyMediaSize *= FileSize.MB_COEFFICIENT;
        this.mStrictStrategyMediaSizeWhenLowSpace *= FileSize.MB_COEFFICIENT;
    }

    public String toString() {
        return "CloudGuideStrategy{mLifeBeforeSlimmed=" + this.mLifeBeforeSlimmed + ", mSlimImmediatelySize=" + this.mSlimImmediatelySize + ", mWeakStrategyVideoCount=" + this.mWeakStrategyVideoCount + ", mWeakStrategyImageCount=" + this.mWeakStrategyImageCount + ", mWeakStrategyMediaSize=" + this.mWeakStrategyMediaSize + ", mWeakStrategyLowSpacePercentage=" + this.mWeakStrategyLowSpacePercentage + ", mStrictStrategyMediaSizePercentage=" + this.mStrictStrategyMediaSizePercentage + ", mStrictStrategyLowSpacePercentage=" + this.mStrictStrategyLowSpacePercentage + ", mStrictStrategyMediaSizeWhenLowSpace=" + this.mStrictStrategyMediaSizeWhenLowSpace + '}';
    }
}
