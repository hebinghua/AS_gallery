package com.miui.gallery.model;

/* loaded from: classes2.dex */
public class DiscoveryMessage {
    public boolean isConsumed;
    public String mActionUri;
    public long mExpireTime;
    public String mMessage;
    public BaseMessageDetail mMessageDetail;
    public long mMessageId;
    public String mMessageSource;
    public int mPriority;
    public long mReceiveTime;
    public String mSubTitle;
    public String mTitle;
    public long mTriggerTime;
    public int mType;
    public long mUpdateTime;

    /* loaded from: classes2.dex */
    public static abstract class BaseMessageDetail {
        public abstract String toJson();
    }

    public DiscoveryMessage() {
    }

    public DiscoveryMessage(Builder builder) {
        long currentTimeMillis = System.currentTimeMillis();
        this.mMessageId = builder.messageId;
        this.mMessage = builder.message;
        this.mTitle = builder.title;
        this.mSubTitle = builder.subTitle;
        this.mType = builder.type;
        this.mPriority = builder.priority;
        if (builder.receiveTime == 0) {
            this.mReceiveTime = currentTimeMillis;
        } else {
            this.mReceiveTime = builder.receiveTime;
        }
        if (builder.updateTime == 0) {
            this.mUpdateTime = currentTimeMillis;
        } else {
            this.mUpdateTime = builder.updateTime;
        }
        if (builder.updateTime == 0) {
            this.mTriggerTime = currentTimeMillis;
        } else {
            this.mTriggerTime = builder.triggerTime;
        }
        if (builder.updateTime == 0) {
            this.mExpireTime = currentTimeMillis;
        } else {
            this.mExpireTime = builder.expireTime;
        }
        this.isConsumed = builder.isConsumed;
        this.mActionUri = builder.actionUri;
        this.mMessageSource = builder.messageSource;
        this.mMessageDetail = builder.messageDetail;
    }

    public void setMessage(String str) {
        this.mMessage = str;
    }

    public void setConsumed(boolean z) {
        this.isConsumed = z;
    }

    public void setMessageDetail(BaseMessageDetail baseMessageDetail) {
        this.mMessageDetail = baseMessageDetail;
    }

    public long getMessageId() {
        return this.mMessageId;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getSubTitle() {
        return this.mSubTitle;
    }

    public int getType() {
        return this.mType;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public long getReceiveTime() {
        return this.mReceiveTime;
    }

    public long getUpdateTime() {
        return this.mUpdateTime;
    }

    public long getTriggerTime() {
        return this.mTriggerTime;
    }

    public long getExpireTime() {
        return this.mExpireTime;
    }

    public boolean isConsumed() {
        return this.isConsumed;
    }

    public String getActionUri() {
        return this.mActionUri;
    }

    public String getMessageSource() {
        return this.mMessageSource;
    }

    public BaseMessageDetail getMessageDetail() {
        return this.mMessageDetail;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public long messageId = -1;
        public String message = "";
        public String title = "";
        public String subTitle = "";
        public int type = 1;
        public int priority = 5;
        public long receiveTime = 0;
        public long updateTime = 0;
        public long triggerTime = 0;
        public long expireTime = 0;
        public boolean isConsumed = false;
        public String actionUri = "";
        public String messageSource = "";
        public BaseMessageDetail messageDetail = null;

        public Builder messageId(long j) {
            this.messageId = j;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder title(String str) {
            this.title = str;
            return this;
        }

        public Builder subTitle(String str) {
            this.subTitle = str;
            return this;
        }

        public Builder type(int i) {
            this.type = i;
            return this;
        }

        public Builder priority(int i) {
            this.priority = i;
            return this;
        }

        public Builder receiveTime(long j) {
            this.receiveTime = j;
            return this;
        }

        public Builder updateTime(long j) {
            this.updateTime = j;
            return this;
        }

        public Builder triggerTime(long j) {
            this.triggerTime = j;
            return this;
        }

        public Builder expireTime(long j) {
            this.expireTime = j;
            return this;
        }

        public Builder consumed(boolean z) {
            this.isConsumed = z;
            return this;
        }

        public Builder actionUri(String str) {
            this.actionUri = str;
            return this;
        }

        public Builder messageSource(String str) {
            this.messageSource = str;
            return this;
        }

        public DiscoveryMessage build() {
            return new DiscoveryMessage(this);
        }
    }
}
