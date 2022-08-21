package com.miui.gallery.ui.pictures.view;

import android.net.Uri;
import java.util.Objects;

/* loaded from: classes2.dex */
public class ImageCellData {
    public long mDate;
    public Uri mDownloadUri;
    public long mFileLength;
    public long mId;
    public String mLocalPath;
    public int mPosition;

    public ImageCellData(Builder builder) {
        this.mId = builder.mId;
        this.mDownloadUri = builder.mDownloadUri;
        this.mLocalPath = builder.mLocalPath;
        this.mPosition = builder.mPosition;
        this.mDate = builder.mDate;
        this.mFileLength = builder.mFileLength;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ImageCellData imageCellData = (ImageCellData) obj;
        return this.mId == imageCellData.mId && this.mFileLength == imageCellData.mFileLength && Objects.equals(this.mDownloadUri, imageCellData.mDownloadUri) && Objects.equals(this.mLocalPath, imageCellData.mLocalPath);
    }

    public int hashCode() {
        return (int) this.mId;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public long mDate;
        public Uri mDownloadUri;
        public long mFileLength;
        public long mId;
        public String mLocalPath;
        public int mPosition;

        public Builder setId(long j) {
            this.mId = j;
            return this;
        }

        public Builder setDownloadUri(Uri uri) {
            this.mDownloadUri = uri;
            return this;
        }

        public Builder setLocalPath(String str) {
            this.mLocalPath = str;
            return this;
        }

        public Builder setDate(long j) {
            this.mDate = j;
            return this;
        }

        public Builder setFileLength(long j) {
            this.mFileLength = j;
            return this;
        }

        public Builder setPosition(int i) {
            this.mPosition = i;
            return this;
        }

        public ImageCellData build() {
            return new ImageCellData(this);
        }
    }
}
