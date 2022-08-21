package com.miui.gallery.adapter;

import android.view.View;

/* loaded from: classes.dex */
public interface CheckableAdapter {
    View getCheckableView(View view);

    /* loaded from: classes.dex */
    public static class CheckedItem {
        public int mHeight;
        public final long mId;
        public final String mMicroThumbnailFile;
        public final String mMimeType;
        public final String mOriginFile;
        public int mServerType;
        public final String mSha1;
        public long mSize;
        public final String mThumbnailFile;
        public int mWidth;

        public CheckedItem(Builder builder) {
            this.mId = builder.mId;
            this.mSha1 = builder.mSha1;
            this.mMicroThumbnailFile = builder.mMicroThumbnailFile;
            this.mThumbnailFile = builder.mThumbnailFile;
            this.mOriginFile = builder.mOriginFile;
            this.mMimeType = builder.mMimeType;
        }

        /* loaded from: classes.dex */
        public static class Builder {
            public long mId;
            public String mMicroThumbnailFile;
            public String mMimeType;
            public String mOriginFile;
            public String mSha1;
            public String mThumbnailFile;

            public Builder setId(long j) {
                this.mId = j;
                return this;
            }

            public Builder setSha1(String str) {
                this.mSha1 = str;
                return this;
            }

            public Builder setMicroThumbnailFile(String str) {
                this.mMicroThumbnailFile = str;
                return this;
            }

            public Builder setThumbnailFile(String str) {
                this.mThumbnailFile = str;
                return this;
            }

            public Builder setOriginFile(String str) {
                this.mOriginFile = str;
                return this;
            }

            public Builder setMimeType(String str) {
                this.mMimeType = str;
                return this;
            }

            public CheckedItem build() {
                return new CheckedItem(this);
            }
        }

        public long getId() {
            return this.mId;
        }

        public String getSha1() {
            return this.mSha1;
        }

        public String getThumbnailFile() {
            return this.mThumbnailFile;
        }

        public String getOriginFile() {
            return this.mOriginFile;
        }

        public String getMimeType() {
            return this.mMimeType;
        }

        public long getSize() {
            return this.mSize;
        }

        public void setSize(long j) {
            this.mSize = j;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public void setHeight(int i) {
            this.mHeight = i;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public void setWidth(int i) {
            this.mWidth = i;
        }

        public int getServerType() {
            return this.mServerType;
        }

        public void setServerType(int i) {
            this.mServerType = i;
        }
    }
}
