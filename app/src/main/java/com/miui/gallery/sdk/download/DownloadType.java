package com.miui.gallery.sdk.download;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public enum DownloadType implements Parcelable {
    ORIGIN(10, false),
    ORIGIN_FORCE(11, false),
    THUMBNAIL(12, false),
    MICRO(13, false),
    THUMBNAIL_BATCH(7, true),
    ORIGIN_BATCH(10, true),
    MICRO_BATCH(8, true);
    
    public static final Parcelable.Creator<DownloadType> CREATOR = new Parcelable.Creator<DownloadType>() { // from class: com.miui.gallery.sdk.download.DownloadType.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DownloadType mo1312createFromParcel(Parcel parcel) {
            return DownloadType.values()[parcel.readInt()];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DownloadType[] mo1313newArray(int i) {
            return new DownloadType[i];
        }
    };
    private final boolean mIsBackground;
    private final int mPriority;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    DownloadType(int i, boolean z) {
        this.mPriority = i;
        this.mIsBackground = z;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public boolean isBackground() {
        return this.mIsBackground;
    }

    public static boolean isOrigin(DownloadType downloadType) {
        return downloadType == ORIGIN || downloadType == ORIGIN_FORCE;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ordinal());
    }
}
