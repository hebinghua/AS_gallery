package com.miui.gallery.movie.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MovieInfo implements Parcelable {
    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() { // from class: com.miui.gallery.movie.entity.MovieInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MovieInfo mo1146createFromParcel(Parcel parcel) {
            return new MovieInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MovieInfo[] mo1147newArray(int i) {
            return new MovieInfo[i];
        }
    };
    public String audio;
    public List<ImageEntity> extraList;
    public List<ImageEntity> imageList;
    public boolean isFromStory;
    public boolean isShortVideo;
    public String subTitle;
    public String template;
    public String title;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MovieInfo(List<ImageEntity> list) {
        this.audio = "default";
        this.imageList = list;
        this.isShortVideo = list.size() <= 5;
    }

    public boolean changeToShortVideo() {
        this.isShortVideo = true;
        if (this.imageList.size() > 5) {
            List<ImageEntity> list = this.imageList;
            this.extraList = list.subList(5, list.size());
            ArrayList arrayList = new ArrayList();
            List<ImageEntity> subList = this.imageList.subList(0, 5);
            this.imageList = subList;
            arrayList.addAll(subList);
            this.imageList = arrayList;
            return true;
        }
        this.extraList = null;
        return false;
    }

    public boolean backToLongVideo() {
        this.isShortVideo = false;
        List<ImageEntity> list = this.extraList;
        if (list != null) {
            this.imageList.addAll(list);
            this.extraList = null;
            return true;
        }
        return false;
    }

    public boolean discardToLongVideo() {
        this.isShortVideo = false;
        if (this.extraList != null) {
            this.extraList = null;
            return true;
        }
        return false;
    }

    public boolean discardToShortVideo() {
        this.isShortVideo = true;
        if (this.extraList != null) {
            this.extraList = null;
            return true;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.imageList);
        parcel.writeString(this.template);
        parcel.writeString(this.audio);
        parcel.writeByte(this.isFromStory ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isShortVideo ? (byte) 1 : (byte) 0);
        parcel.writeString(this.title);
        parcel.writeString(this.subTitle);
        parcel.writeList(this.extraList);
    }

    public MovieInfo(Parcel parcel) {
        this.audio = "default";
        ArrayList arrayList = new ArrayList();
        this.imageList = arrayList;
        parcel.readList(arrayList, ImageEntity.class.getClassLoader());
        this.template = parcel.readString();
        this.audio = parcel.readString();
        boolean z = true;
        this.isFromStory = parcel.readByte() != 0;
        this.isShortVideo = parcel.readByte() == 0 ? false : z;
        this.title = parcel.readString();
        this.subTitle = parcel.readString();
        ArrayList arrayList2 = new ArrayList();
        this.extraList = arrayList2;
        parcel.readList(arrayList2, ImageEntity.class.getClassLoader());
    }
}
