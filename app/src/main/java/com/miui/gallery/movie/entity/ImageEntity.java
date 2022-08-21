package com.miui.gallery.movie.entity;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class ImageEntity implements Parcelable {
    public static final Parcelable.Creator<ImageEntity> CREATOR = new Parcelable.Creator<ImageEntity>() { // from class: com.miui.gallery.movie.entity.ImageEntity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ImageEntity mo1144createFromParcel(Parcel parcel) {
            return new ImageEntity(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ImageEntity[] mo1145newArray(int i) {
            return new ImageEntity[i];
        }
    };
    public String image;
    public String sha1;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ImageEntity(String str, String str2) {
        this.image = str;
        this.sha1 = str2;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.image);
        parcel.writeString(this.sha1);
    }

    public ImageEntity(Parcel parcel) {
        this.image = parcel.readString();
        this.sha1 = parcel.readString();
    }
}
