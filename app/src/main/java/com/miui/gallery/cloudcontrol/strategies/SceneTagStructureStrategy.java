package com.miui.gallery.cloudcontrol.strategies;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.annotations.SerializedName;
import com.xiaomi.onetrack.util.oaid.a;
import java.util.List;

/* loaded from: classes.dex */
public class SceneTagStructureStrategy extends BaseStrategy {
    @SerializedName("tags")
    private List<SceneTag> mSceneTags;

    public List<SceneTag> getSceneTags() {
        return this.mSceneTags;
    }

    /* loaded from: classes.dex */
    public static class SceneTag {
        @SerializedName(a.a)
        private String mKgId;
        @SerializedName("b")
        private List<SceneTag> mSubTags;
        @SerializedName("c")
        private float mThreshold;

        public String getKgId() {
            return this.mKgId;
        }

        public List<SceneTag> getSubTags() {
            return this.mSubTags;
        }

        public float getThreshold() {
            return this.mThreshold;
        }

        public String toString() {
            return "SceneTag{mKgId='" + this.mKgId + CoreConstants.SINGLE_QUOTE_CHAR + ", mSubTags=" + this.mSubTags + ", mThreshold=" + this.mThreshold + '}';
        }
    }

    public String toString() {
        return "SceneTagStructureStrategy{mSceneTags=" + this.mSceneTags + '}';
    }
}
