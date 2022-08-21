package com.xiaomi.clientreport.data;

import android.content.Context;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.util.FileSize;
import com.xiaomi.push.bt;

/* loaded from: classes3.dex */
public class Config {
    public String mAESKey;
    public boolean mEventEncrypted;
    public long mEventUploadFrequency;
    public boolean mEventUploadSwitchOpen;
    public long mMaxFileLength;
    public long mPerfUploadFrequency;
    public boolean mPerfUploadSwitchOpen;

    /* loaded from: classes3.dex */
    public static class Builder {
        public int mEventEncrypted = -1;
        public int mEventUploadSwitchOpen = -1;
        public int mPerfUploadSwitchOpen = -1;
        public String mAESKey = null;
        public long mMaxFileLength = -1;
        public long mEventUploadFrequency = -1;
        public long mPerfUploadFrequency = -1;

        public Config build(Context context) {
            return new Config(context, this);
        }

        public Builder setAESKey(String str) {
            this.mAESKey = str;
            return this;
        }

        public Builder setEventEncrypted(boolean z) {
            this.mEventEncrypted = z ? 1 : 0;
            return this;
        }

        public Builder setEventUploadFrequency(long j) {
            this.mEventUploadFrequency = j;
            return this;
        }

        public Builder setEventUploadSwitchOpen(boolean z) {
            this.mEventUploadSwitchOpen = z ? 1 : 0;
            return this;
        }

        public Builder setMaxFileLength(long j) {
            this.mMaxFileLength = j;
            return this;
        }

        public Builder setPerfUploadFrequency(long j) {
            this.mPerfUploadFrequency = j;
            return this;
        }

        public Builder setPerfUploadSwitchOpen(boolean z) {
            this.mPerfUploadSwitchOpen = z ? 1 : 0;
            return this;
        }
    }

    public Config(Context context, Builder builder) {
        this.mEventEncrypted = true;
        this.mEventUploadSwitchOpen = false;
        this.mPerfUploadSwitchOpen = false;
        long j = FileSize.MB_COEFFICIENT;
        this.mMaxFileLength = FileSize.MB_COEFFICIENT;
        this.mEventUploadFrequency = 86400L;
        this.mPerfUploadFrequency = 86400L;
        if (builder.mEventEncrypted == 0) {
            this.mEventEncrypted = false;
        } else {
            int unused = builder.mEventEncrypted;
            this.mEventEncrypted = true;
        }
        this.mAESKey = !TextUtils.isEmpty(builder.mAESKey) ? builder.mAESKey : bt.a(context);
        this.mMaxFileLength = builder.mMaxFileLength > -1 ? builder.mMaxFileLength : j;
        if (builder.mEventUploadFrequency > -1) {
            this.mEventUploadFrequency = builder.mEventUploadFrequency;
        } else {
            this.mEventUploadFrequency = 86400L;
        }
        if (builder.mPerfUploadFrequency > -1) {
            this.mPerfUploadFrequency = builder.mPerfUploadFrequency;
        } else {
            this.mPerfUploadFrequency = 86400L;
        }
        if (builder.mEventUploadSwitchOpen != 0 && builder.mEventUploadSwitchOpen == 1) {
            this.mEventUploadSwitchOpen = true;
        } else {
            this.mEventUploadSwitchOpen = false;
        }
        if (builder.mPerfUploadSwitchOpen != 0 && builder.mPerfUploadSwitchOpen == 1) {
            this.mPerfUploadSwitchOpen = true;
        } else {
            this.mPerfUploadSwitchOpen = false;
        }
    }

    public static Config defaultConfig(Context context) {
        return getBuilder().setEventEncrypted(true).setAESKey(bt.a(context)).setMaxFileLength(FileSize.MB_COEFFICIENT).setEventUploadSwitchOpen(false).setEventUploadFrequency(86400L).setPerfUploadSwitchOpen(false).setPerfUploadFrequency(86400L).build(context);
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public long getEventUploadFrequency() {
        return this.mEventUploadFrequency;
    }

    public long getMaxFileLength() {
        return this.mMaxFileLength;
    }

    public long getPerfUploadFrequency() {
        return this.mPerfUploadFrequency;
    }

    public boolean isEventEncrypted() {
        return this.mEventEncrypted;
    }

    public boolean isEventUploadSwitchOpen() {
        return this.mEventUploadSwitchOpen;
    }

    public boolean isPerfUploadSwitchOpen() {
        return this.mPerfUploadSwitchOpen;
    }

    public String toString() {
        return "Config{mEventEncrypted=" + this.mEventEncrypted + ", mAESKey='" + this.mAESKey + CoreConstants.SINGLE_QUOTE_CHAR + ", mMaxFileLength=" + this.mMaxFileLength + ", mEventUploadSwitchOpen=" + this.mEventUploadSwitchOpen + ", mPerfUploadSwitchOpen=" + this.mPerfUploadSwitchOpen + ", mEventUploadFrequency=" + this.mEventUploadFrequency + ", mPerfUploadFrequency=" + this.mPerfUploadFrequency + '}';
    }
}
