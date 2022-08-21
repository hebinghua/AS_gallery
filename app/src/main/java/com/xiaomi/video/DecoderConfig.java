package com.xiaomi.video;

/* loaded from: classes3.dex */
public class DecoderConfig {
    public final int colorFormat;
    public final int maxHeight;
    public final int maxWidth;
    public final String mediaName;

    public DecoderConfig(Builder builder) {
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.colorFormat = builder.colorFormat;
        this.mediaName = builder.mediaName;
    }

    public boolean hasMaxSize() {
        return this.maxWidth > 0 && this.maxHeight > 0;
    }

    public boolean hasMediaName() {
        return this.mediaName != null;
    }

    /* loaded from: classes3.dex */
    public static class Builder {
        private int maxHeight;
        private int maxWidth;
        private int colorFormat = -1;
        private String mediaName = null;

        public Builder setMax(int i, int i2) {
            this.maxWidth = i;
            this.maxHeight = i2;
            return this;
        }

        public Builder setColorFormat(int i) {
            this.colorFormat = i;
            return this;
        }

        public Builder setMediaName(String str) {
            this.mediaName = str;
            return this;
        }

        public DecoderConfig build() {
            return new DecoderConfig(this);
        }
    }
}
