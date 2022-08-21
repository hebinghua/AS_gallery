package com.xiaomi.miplay.phoneclientsdk.info;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import ch.qos.logback.core.CoreConstants;

/* loaded from: classes3.dex */
public class MediaMetaData implements Parcelable {
    public static final Parcelable.Creator<MediaMetaData> CREATOR = new Parcelable.Creator<MediaMetaData>() { // from class: com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MediaMetaData mo1877createFromParcel(Parcel parcel) {
            return new MediaMetaData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MediaMetaData[] mo1878newArray(int i) {
            return new MediaMetaData[i];
        }
    };
    public Bitmap art;
    public String codec;
    public long duration;
    public int isDlnaCast;
    public String mux;
    public long position;
    public String propertiesInfo;
    public String reverso;
    public String title;
    public String url;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long j) {
        this.duration = j;
    }

    public long getPosition() {
        return this.position;
    }

    public void setPosition(long j) {
        this.position = j;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String toString() {
        return "MediaMetaData{title='" + this.title + CoreConstants.SINGLE_QUOTE_CHAR + ", duration=" + this.duration + ", art=" + this.art + ", position=" + this.position + ", url='" + this.url + CoreConstants.SINGLE_QUOTE_CHAR + ", mux='" + this.mux + CoreConstants.SINGLE_QUOTE_CHAR + ", codec='" + this.codec + CoreConstants.SINGLE_QUOTE_CHAR + ", reverso='" + this.reverso + CoreConstants.SINGLE_QUOTE_CHAR + ", isDlnaCast=" + this.isDlnaCast + ", propertiesInfo='" + this.propertiesInfo + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }

    public MediaMetaData() {
        this.title = "";
        this.duration = 0L;
        this.art = null;
        this.position = 0L;
        this.url = "";
        this.mux = "";
        this.codec = "";
        this.reverso = "";
        this.isDlnaCast = -1;
        this.propertiesInfo = "";
    }

    public MediaMetaData(Parcel parcel) {
        this.title = parcel.readString();
        this.duration = parcel.readLong();
        this.art = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
        this.position = parcel.readLong();
        this.url = parcel.readString();
        this.mux = parcel.readString();
        this.codec = parcel.readString();
        this.reverso = parcel.readString();
        this.isDlnaCast = parcel.readInt();
        this.propertiesInfo = parcel.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeLong(this.duration);
        parcel.writeParcelable(this.art, i);
        parcel.writeLong(this.position);
        parcel.writeString(this.url);
        parcel.writeString(this.mux);
        parcel.writeString(this.codec);
        parcel.writeString(this.reverso);
        parcel.writeInt(this.isDlnaCast);
        parcel.writeString(this.propertiesInfo);
    }
}
