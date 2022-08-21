package com.miui.gallery.share;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.xiaomi.stat.b.h;
import java.util.WeakHashMap;

/* loaded from: classes2.dex */
public class Path implements Parcelable {
    public long mId;
    public boolean mIsBabyAlbum;
    public boolean mIsOtherShared;
    public static WeakHashMap<Path, CloudSharerMediaSet> sSetCache = new WeakHashMap<>();
    public static final Parcelable.Creator<Path> CREATOR = new Parcelable.Creator<Path>() { // from class: com.miui.gallery.share.Path.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Path mo1377createFromParcel(Parcel parcel) {
            return new Path(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Path[] mo1378newArray(int i) {
            return new Path[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Path() {
    }

    public Path(long j, boolean z) {
        this.mId = j;
        this.mIsOtherShared = z;
    }

    public Path(long j, boolean z, boolean z2) {
        this.mId = j;
        this.mIsOtherShared = z;
        this.mIsBabyAlbum = z2;
    }

    public Path(Parcel parcel) {
        boolean z = true;
        this.mIsOtherShared = parcel.readByte() != 0;
        this.mIsBabyAlbum = parcel.readByte() == 0 ? false : z;
        this.mId = parcel.readLong();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.mIsOtherShared ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsBabyAlbum ? (byte) 1 : (byte) 0);
        parcel.writeLong(this.mId);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mIsOtherShared ? "other" : "owner");
        sb.append(h.g);
        sb.append(String.valueOf(this.mId));
        return sb.toString();
    }

    public static Path fromString(String str) {
        if (TextUtils.isEmpty(str) || str.indexOf(h.g) == -1) {
            return null;
        }
        Path path = new Path();
        int indexOf = str.indexOf(h.g);
        path.mIsOtherShared = str.indexOf("other") != -1;
        path.mId = Long.parseLong(str.substring(indexOf + 1));
        return path;
    }

    public boolean isBabyAlbum() {
        return this.mIsBabyAlbum;
    }

    public boolean isOtherShared() {
        return this.mIsOtherShared;
    }

    public long getId() {
        return this.mId;
    }

    public CloudSharerMediaSet getMediaSet() {
        if (sSetCache.containsKey(this)) {
            return sSetCache.get(this);
        }
        CloudSharerMediaSet cloudSharerMediaSet = new CloudSharerMediaSet(this);
        sSetCache.put(this, cloudSharerMediaSet);
        return cloudSharerMediaSet;
    }
}
