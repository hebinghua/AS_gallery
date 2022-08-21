package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes.dex */
public class AssistantScenarioStrategy extends BaseStrategy {
    @SerializedName("cloudTimeScenarioRules")
    private List<CloudTimeScenarioRule> mCloudTimeScenarioRules;
    @SerializedName("defaultMaxImageCount")
    private int mDefaultMaxImageCount;
    @SerializedName("defaultMaxSelectedImageCount")
    private int mDefaultMaxSelectedImageCount;
    @SerializedName("defaultMinImageCount")
    private int mDefaultMinImageCount;
    @SerializedName("defaultMinSelectedImageCount")
    private int mDefaultMinSelectedImageCount;
    @SerializedName("localScenarioRules")
    private List<ScenarioRule> mLocalScenarioRules;

    public int getDefaultMinImageCount() {
        int i = this.mDefaultMinImageCount;
        if (i > 0) {
            return i;
        }
        return 20;
    }

    public int getDefaultMaxImageCount() {
        int i = this.mDefaultMaxImageCount;
        if (i > 0) {
            return i;
        }
        return 500;
    }

    public int getDefaultMinSelectedImageCount() {
        int i = this.mDefaultMinSelectedImageCount;
        if (i > 0) {
            return i;
        }
        return 6;
    }

    public int getDefaultMaxSelectedImageCount() {
        int i = this.mDefaultMaxSelectedImageCount;
        if (i > 0) {
            return i;
        }
        return 80;
    }

    public List<ScenarioRule> getLocalScenarioRules() {
        return this.mLocalScenarioRules;
    }

    public List<CloudTimeScenarioRule> getCloudTimeScenarioRules() {
        return this.mCloudTimeScenarioRules;
    }

    public String toString() {
        return "AssistantScenarioStrategy{mDefaultMinImageCount=" + this.mDefaultMinImageCount + ", mDefaultMaxImageCount=" + this.mDefaultMaxImageCount + ", mDefaultMinSelectedImageCount=" + this.mDefaultMinSelectedImageCount + ", mDefaultMaxSelectedImageCount=" + this.mDefaultMaxSelectedImageCount + ", mLocalScenarioRules=" + this.mLocalScenarioRules + ", mCloudTimeScenarioRules=" + this.mCloudTimeScenarioRules + '}';
    }

    /* loaded from: classes.dex */
    public static class ScenarioRule {
        @SerializedName("hasPastYear")
        public boolean mHasPastYear;
        @SerializedName("holidayId")
        public int mHolidayId;
        @SerializedName("tagList")
        private List<String> mKnowledgeIds;
        @SerializedName("maxCount")
        public int mMaxImageCount;
        @SerializedName("maxSelectedCount")
        public int mMaxSelectedImageCount;
        @SerializedName("minCount")
        public int mMinImageCount;
        @SerializedName("minSelectedCount")
        public int mMinSelectedImageCount;
        @SerializedName("scenarioId")
        public int mScenarioId;

        public int getScenarioId() {
            return this.mScenarioId;
        }

        public int getHolidayId() {
            return this.mHolidayId;
        }

        public boolean hasPastYear() {
            return this.mHasPastYear;
        }

        public int getMinImageCount() {
            return this.mMinImageCount;
        }

        public int getMaxImageCount() {
            return this.mMaxImageCount;
        }

        public int getMinSelectedImageCount() {
            return this.mMinSelectedImageCount;
        }

        public int getMaxSelectedImageCount() {
            return this.mMaxSelectedImageCount;
        }

        public List<String> getKnowledgeIds() {
            return this.mKnowledgeIds;
        }

        public int getScenarioTimeType() {
            return (this.mScenarioId / 100) * 100;
        }

        public void setScenarioId(int i) {
            this.mScenarioId = i;
        }

        public void setHolidayId(int i) {
            this.mHolidayId = i;
        }

        public void setMinImageCount(int i) {
            this.mMinImageCount = i;
        }

        public void setMinSelectedImageCount(int i) {
            this.mMinSelectedImageCount = i;
        }

        public void setKnowledgeIds(List<String> list) {
            this.mKnowledgeIds = list;
        }

        public String toString() {
            return "ScenarioRule{mScenarioId=" + this.mScenarioId + ", mHasPastYear=" + this.mHasPastYear + ", mHolidayId=" + this.mHolidayId + ", mMinImageCount=" + this.mMinImageCount + ", mMaxImageCount=" + this.mMaxImageCount + ", mMinSelectedImageCount=" + this.mMinSelectedImageCount + ", mMaxSelectedImageCount=" + this.mMaxSelectedImageCount + ", mKnowledgeIds=" + this.mKnowledgeIds + '}';
        }
    }

    /* loaded from: classes.dex */
    public static class CloudTimeScenarioRule extends ScenarioRule {
        @SerializedName("description")
        private String mDescription;
        @SerializedName("endTime")
        private long mEndTime;
        @SerializedName("startTime")
        private long mStartTime;
        @SerializedName("title")
        private String mTitle;

        public String getTitle() {
            return this.mTitle;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public long getEndTime() {
            return this.mEndTime;
        }
    }
}
