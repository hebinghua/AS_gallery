package com.miui.video.localvideoplayer;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import java.io.FileDescriptor;

/* loaded from: classes3.dex */
public class FrameParams implements Parcelable {
    public static final Parcelable.Creator<FrameParams> CREATOR = new Parcelable.Creator<FrameParams>() { // from class: com.miui.video.localvideoplayer.FrameParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FrameParams mo1847createFromParcel(Parcel parcel) {
            return new FrameParams(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FrameParams[] mo1848newArray(int i) {
            return new FrameParams[i];
        }
    };
    public Bitmap.Config mConfig;
    public int mCount;
    public ParcelFileDescriptor mFd;
    public int mHeight;
    public int mWidth;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public FrameParams() {
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getCount() {
        return this.mCount;
    }

    public Bitmap.Config getConfig() {
        return this.mConfig;
    }

    public FileDescriptor getFileDescriptor() {
        ParcelFileDescriptor parcelFileDescriptor = this.mFd;
        if (parcelFileDescriptor != null) {
            return parcelFileDescriptor.getFileDescriptor();
        }
        return null;
    }

    public FrameParams(Parcel parcel) {
        this.mWidth = parcel.readInt();
        this.mHeight = parcel.readInt();
        this.mCount = parcel.readInt();
        this.mConfig = Bitmap.Config.valueOf(parcel.readString());
        this.mFd = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mWidth);
        parcel.writeInt(this.mHeight);
        parcel.writeInt(this.mCount);
        parcel.writeString(this.mConfig.name());
        this.mFd.writeToParcel(parcel, i);
    }
}
