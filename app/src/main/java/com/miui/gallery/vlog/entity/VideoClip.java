package com.miui.gallery.vlog.entity;

import android.os.Parcel;
import android.os.Parcelable;
import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class VideoClip implements Parcelable {
    public static final Parcelable.Creator<VideoClip> CREATOR = new Parcelable.Creator<VideoClip>() { // from class: com.miui.gallery.vlog.entity.VideoClip.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public VideoClip mo1789createFromParcel(Parcel parcel) {
            return new VideoClip(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public VideoClip[] mo1790newArray(int i) {
            return new VideoClip[i];
        }
    };
    public String mFilePath;
    public int mHeight;
    public long mTrimIn;
    public long mTrimOut;
    public long mWholeDuration;
    public int mWidth;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public VideoClip(String str, long j, long j2, long j3, int i, int i2) {
        this.mFilePath = str;
        this.mWholeDuration = j;
        this.mTrimIn = j2;
        this.mTrimOut = j3;
        this.mWidth = i;
        this.mHeight = i2;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public long getWholeDuration() {
        return this.mWholeDuration;
    }

    public long getTrimIn() {
        return this.mTrimIn;
    }

    public long getTrimOut() {
        return this.mTrimOut;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public VideoClip(Parcel parcel) {
        this.mFilePath = parcel.readString();
        this.mWholeDuration = parcel.readLong();
        this.mTrimIn = parcel.readLong();
        this.mTrimOut = parcel.readLong();
        this.mWidth = parcel.readInt();
        this.mHeight = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mFilePath);
        parcel.writeLong(this.mWholeDuration);
        parcel.writeLong(this.mTrimIn);
        parcel.writeLong(this.mTrimOut);
        parcel.writeInt(this.mWidth);
        parcel.writeInt(this.mHeight);
    }

    public String toString() {
        return "VideoClip{mFilePath='" + this.mFilePath + CoreConstants.SINGLE_QUOTE_CHAR + ", mTotalDuration=" + this.mWholeDuration + ", mTrimIn=" + this.mTrimIn + ", mTrimOut=" + this.mTrimOut + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + '}';
    }
}
