package com.miui.gallery.model.dto;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Objects;

/* loaded from: classes2.dex */
public class BaseAlbumCover implements Parcelable {
    public static final Parcelable.Creator<BaseAlbumCover> CREATOR = new Parcelable.Creator<BaseAlbumCover>() { // from class: com.miui.gallery.model.dto.BaseAlbumCover.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public BaseAlbumCover mo1139createFromParcel(Parcel parcel) {
            return new BaseAlbumCover(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public BaseAlbumCover[] mo1140newArray(int i) {
            return new BaseAlbumCover[i];
        }
    };
    public String albumName;
    public long coverId;
    public String coverPath;
    public String coverSha1;
    public long coverSize;
    public int coverSyncState;
    public Uri coverUri;
    public long id;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public BaseAlbumCover() {
    }

    public BaseAlbumCover(Parcel parcel) {
        this.coverId = parcel.readLong();
        this.coverPath = parcel.readString();
        this.coverSha1 = parcel.readString();
        this.coverSyncState = parcel.readInt();
        this.coverSize = parcel.readLong();
        this.id = parcel.readLong();
        this.albumName = parcel.readString();
        this.coverUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseAlbumCover baseAlbumCover = (BaseAlbumCover) obj;
        return this.coverId == baseAlbumCover.coverId && this.coverSyncState == baseAlbumCover.coverSyncState && this.coverSize == baseAlbumCover.coverSize && Objects.equals(this.coverPath, baseAlbumCover.coverPath) && Objects.equals(this.coverSha1, baseAlbumCover.coverSha1);
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.coverId), this.coverPath, this.coverSha1, Integer.valueOf(this.coverSyncState), Long.valueOf(this.coverSize));
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(this.coverPath) || !Objects.isNull(this.coverUri);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.coverId);
        parcel.writeString(this.coverPath);
        parcel.writeString(this.coverSha1);
        parcel.writeInt(this.coverSyncState);
        parcel.writeLong(this.coverSize);
        parcel.writeLong(this.id);
        parcel.writeString(this.albumName);
        parcel.writeParcelable(this.coverUri, i);
    }
}
