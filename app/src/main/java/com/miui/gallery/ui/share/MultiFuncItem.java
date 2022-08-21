package com.miui.gallery.ui.share;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.sdk.download.DownloadType;

/* loaded from: classes2.dex */
public class MultiFuncItem implements DownloadItem, DecryptItem, ConvertItem {
    public static final Parcelable.Creator<MultiFuncItem> CREATOR = new Parcelable.Creator<MultiFuncItem>() { // from class: com.miui.gallery.ui.share.MultiFuncItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MultiFuncItem mo1666createFromParcel(Parcel parcel) {
            return new MultiFuncItem(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MultiFuncItem[] mo1667newArray(int i) {
            return new MultiFuncItem[i];
        }
    };
    public DownloadType mDownloadType;
    public String mFileTitle;
    public int mFlags;
    public Uri mPreparedUri;
    public long mSecretId;
    public byte[] mSecretKey;
    public Uri mSrcUri;
    public Uri mTempUri;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MultiFuncItem(Builder builder) {
        this.mSrcUri = builder.mSrcUri;
        this.mFileTitle = builder.mFileTitle;
        this.mDownloadType = builder.mDownloadType;
        this.mSecretKey = builder.mSecretKey;
        this.mSecretId = builder.mSecretId;
        this.mFlags = builder.mFlags;
    }

    public MultiFuncItem(Parcel parcel) {
        this.mSrcUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        this.mFileTitle = parcel.readString();
        this.mDownloadType = (DownloadType) parcel.readParcelable(DownloadType.class.getClassLoader());
        int readInt = parcel.readInt();
        if (readInt > 0) {
            byte[] bArr = new byte[readInt];
            this.mSecretKey = bArr;
            parcel.readByteArray(bArr);
        }
        this.mSecretId = parcel.readLong();
        this.mFlags = parcel.readInt();
        this.mPreparedUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
    }

    @Override // com.miui.gallery.ui.share.DecryptItem
    public byte[] getSecretKey() {
        return this.mSecretKey;
    }

    @Override // com.miui.gallery.ui.share.DecryptItem
    public long getSecretId() {
        return this.mSecretId;
    }

    @Override // com.miui.gallery.ui.share.DownloadItem
    public DownloadType getDownloadType() {
        return this.mDownloadType;
    }

    @Override // com.miui.gallery.ui.share.PrepareItem
    public Uri getSrcUri() {
        return this.mSrcUri;
    }

    @Override // com.miui.gallery.ui.share.ConvertItem
    public String getFileTitle() {
        return this.mFileTitle;
    }

    @Override // com.miui.gallery.ui.share.PrepareItem
    public Uri getPreparedUri() {
        return this.mPreparedUri;
    }

    @Override // com.miui.gallery.ui.share.PrepareItem
    public int getFlags() {
        return this.mFlags;
    }

    @Override // com.miui.gallery.ui.share.PrepareItem
    public void onPrepared(Uri uri) {
        this.mPreparedUri = uri;
    }

    @Override // com.miui.gallery.ui.share.PrepareItem
    public Uri getPreparedUriInLastStep() {
        return this.mTempUri;
    }

    @Override // com.miui.gallery.ui.share.PrepareItem
    public void onStepPrepared(Uri uri, int i) {
        this.mTempUri = uri;
        this.mFlags &= ~i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mSrcUri, i);
        parcel.writeString(this.mFileTitle);
        parcel.writeParcelable(this.mDownloadType, i);
        byte[] bArr = this.mSecretKey;
        int length = bArr != null ? bArr.length : 0;
        parcel.writeInt(length);
        if (length > 0) {
            parcel.writeByteArray(this.mSecretKey);
        }
        parcel.writeLong(this.mSecretId);
        parcel.writeInt(this.mFlags);
        parcel.writeParcelable(this.mPreparedUri, i);
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public DownloadType mDownloadType;
        public String mFileTitle;
        public int mFlags;
        public long mSecretId;
        public byte[] mSecretKey;
        public Uri mSrcUri;

        public Builder setSrcUri(Uri uri) {
            this.mSrcUri = uri;
            return this;
        }

        public Builder setFileTitle(String str) {
            this.mFileTitle = str;
            return this;
        }

        public Builder setDownloadType(DownloadType downloadType) {
            this.mDownloadType = downloadType;
            return this;
        }

        public Builder setSecretKey(byte[] bArr) {
            this.mSecretKey = bArr;
            return this;
        }

        public Builder setSecretId(long j) {
            this.mSecretId = j;
            return this;
        }

        public Builder setFlags(int i) {
            this.mFlags = i;
            return this;
        }

        public MultiFuncItem build() {
            return new MultiFuncItem(this);
        }

        public Builder cloneFrom(MultiFuncItem multiFuncItem) {
            this.mSrcUri = multiFuncItem.mSrcUri;
            this.mFileTitle = multiFuncItem.mFileTitle;
            this.mDownloadType = multiFuncItem.mDownloadType;
            this.mSecretKey = multiFuncItem.mSecretKey;
            this.mSecretId = multiFuncItem.mSecretId;
            this.mFlags = multiFuncItem.mFlags;
            return this;
        }
    }
}
