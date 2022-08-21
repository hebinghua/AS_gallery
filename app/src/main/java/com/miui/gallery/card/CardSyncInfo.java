package com.miui.gallery.card;

import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes.dex */
public class CardSyncInfo {
    @SerializedName("description")
    private String description;
    @SerializedName("duplicateKey")
    private String duplicateKey;
    @SerializedName("extraInfo")
    private String extraInfo;
    @SerializedName("mediaInfo")
    private MediaInfoBean mediaInfo;
    @SerializedName("name")
    private String name;
    @SerializedName("scenarioId")
    private int scenarioId;
    @SerializedName("sortTime")
    private long sortTime;
    @SerializedName("status")
    private String status;

    public CardSyncInfo(Builder builder) {
        setScenarioId(builder.scenarioId);
        setName(builder.name);
        setDescription(builder.description);
        setStatus(builder.status);
        setDuplicateKey(builder.duplicateKey);
        setMediaInfo(new MediaInfoBean(builder.coverMediaList, builder.mediaList, builder.allMediaList));
        setExtraInfo(builder.extraInfo);
        setSortTime(builder.sortTime);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void setScenarioId(int i) {
        this.scenarioId = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public void setDuplicateKey(String str) {
        this.duplicateKey = str;
    }

    public void setMediaInfo(MediaInfoBean mediaInfoBean) {
        this.mediaInfo = mediaInfoBean;
    }

    public void setExtraInfo(String str) {
        this.extraInfo = str;
    }

    public void setSortTime(long j) {
        this.sortTime = j;
    }

    public boolean isEmptyCard() {
        MediaInfoBean mediaInfoBean = this.mediaInfo;
        return mediaInfoBean == null || mediaInfoBean.isEmpty();
    }

    /* loaded from: classes.dex */
    public static class MediaInfoBean {
        @SerializedName("allMediaList")
        private final List<Long> allMediaList;
        @SerializedName("coverMediaList")
        private final List<Long> coverMediaList;
        @SerializedName("mediaList")
        private final List<Long> mediaList;

        public MediaInfoBean(List<Long> list, List<Long> list2, List<Long> list3) {
            this.coverMediaList = list;
            this.mediaList = list2;
            this.allMediaList = list3;
        }

        public boolean isEmpty() {
            return !BaseMiscUtil.isValid(this.mediaList) || !BaseMiscUtil.isValid(this.allMediaList);
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        public List<Long> allMediaList;
        public List<Long> coverMediaList;
        public String description;
        public String duplicateKey;
        public String extraInfo;
        public List<Long> mediaList;
        public String name;
        public int scenarioId;
        public long sortTime;
        public String status;

        public Builder() {
        }

        public Builder setScenarioId(int i) {
            this.scenarioId = i;
            return this;
        }

        public Builder setName(String str) {
            this.name = str;
            return this;
        }

        public Builder setDescription(String str) {
            this.description = str;
            return this;
        }

        public Builder setStatus(String str) {
            this.status = str;
            return this;
        }

        public Builder setDuplicateKey(String str) {
            this.duplicateKey = str;
            return this;
        }

        public Builder setCoverMediaList(List<Long> list) {
            this.coverMediaList = list;
            return this;
        }

        public Builder setMediaList(List<Long> list) {
            this.mediaList = list;
            return this;
        }

        public Builder setAllMediaList(List<Long> list) {
            this.allMediaList = list;
            return this;
        }

        public Builder setExtraInfo(String str) {
            this.extraInfo = str;
            return this;
        }

        public Builder setSortTime(long j) {
            this.sortTime = j;
            return this;
        }

        public CardSyncInfo build() {
            return new CardSyncInfo(this);
        }
    }
}
