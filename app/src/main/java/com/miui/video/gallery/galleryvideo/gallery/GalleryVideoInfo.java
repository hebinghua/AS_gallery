package com.miui.video.gallery.galleryvideo.gallery;

import android.os.Parcel;
import android.os.Parcelable;
import ch.qos.logback.core.CoreConstants;

/* loaded from: classes3.dex */
public class GalleryVideoInfo implements Parcelable {
    public static final Parcelable.Creator<GalleryVideoInfo> CREATOR = new Parcelable.Creator<GalleryVideoInfo>() { // from class: com.miui.video.gallery.galleryvideo.gallery.GalleryVideoInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public GalleryVideoInfo mo1845createFromParcel(Parcel parcel) {
            return new GalleryVideoInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public GalleryVideoInfo[] mo1846newArray(int i) {
            return new GalleryVideoInfo[i];
        }
    };
    public String captureFps;
    public String date;
    public long duration;
    public int fps;
    public int height;
    public boolean isHdr;
    public boolean isMiMovie;
    public boolean isRecordLog;
    public boolean isSubtitleVideo;
    public boolean isTagVideo;
    public int leftSlidePosition;
    public String maker;
    public int rightSlidePosition;
    public int rotation;
    public boolean supportFrame;
    public int trackCount;
    public int type;
    public String url;
    public long videoDuration;
    public String videoTrack;
    public int width;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public GalleryVideoInfo() {
        this.fps = 30;
        this.isRecordLog = false;
        this.supportFrame = false;
    }

    public long getDuration() {
        return this.duration;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isHdr() {
        return this.isHdr;
    }

    public boolean isSupportFrame() {
        return this.supportFrame;
    }

    public String toString() {
        return "GalleryVideoInfo{, url='" + this.url + CoreConstants.SINGLE_QUOTE_CHAR + ", duration=" + this.duration + ", width=" + this.width + ", height=" + this.height + ", rotation=" + this.rotation + ", fps=" + this.fps + ", type=" + this.type + ", leftSlidePosition=" + this.leftSlidePosition + ", rightSlidePosition=" + this.rightSlidePosition + ", isMiMovie=" + this.isMiMovie + ", date='" + this.date + CoreConstants.SINGLE_QUOTE_CHAR + ", maker='" + this.maker + CoreConstants.SINGLE_QUOTE_CHAR + ", trackCount=" + this.trackCount + ", captureFps='" + this.captureFps + CoreConstants.SINGLE_QUOTE_CHAR + ", videoTrack='" + this.videoTrack + CoreConstants.SINGLE_QUOTE_CHAR + ", isSubtitleVideo=" + this.isSubtitleVideo + ", isTagVideo=" + this.isTagVideo + ", isHdr=" + this.isHdr + ", isRecordLog=" + this.isRecordLog + ", supportFrame=" + this.supportFrame + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeLong(this.duration);
        parcel.writeLong(this.videoDuration);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeInt(this.rotation);
        parcel.writeInt(this.fps);
        parcel.writeInt(this.type);
        parcel.writeInt(this.leftSlidePosition);
        parcel.writeInt(this.rightSlidePosition);
        parcel.writeByte(this.isMiMovie ? (byte) 1 : (byte) 0);
        parcel.writeString(this.date);
        parcel.writeString(this.maker);
        parcel.writeInt(this.trackCount);
        parcel.writeString(this.captureFps);
        parcel.writeString(this.videoTrack);
        parcel.writeByte(this.isSubtitleVideo ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isTagVideo ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isHdr ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isRecordLog ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.supportFrame ? (byte) 1 : (byte) 0);
    }

    public GalleryVideoInfo(Parcel parcel) {
        this.fps = 30;
        boolean z = false;
        this.isRecordLog = false;
        this.supportFrame = false;
        this.url = parcel.readString();
        this.duration = parcel.readLong();
        this.videoDuration = parcel.readLong();
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.rotation = parcel.readInt();
        this.fps = parcel.readInt();
        this.type = parcel.readInt();
        this.leftSlidePosition = parcel.readInt();
        this.rightSlidePosition = parcel.readInt();
        this.isMiMovie = parcel.readByte() != 0;
        this.date = parcel.readString();
        this.maker = parcel.readString();
        this.trackCount = parcel.readInt();
        this.captureFps = parcel.readString();
        this.videoTrack = parcel.readString();
        this.isSubtitleVideo = parcel.readByte() != 0;
        this.isTagVideo = parcel.readByte() != 0;
        this.isHdr = parcel.readByte() != 0;
        this.isRecordLog = parcel.readByte() != 0;
        this.supportFrame = parcel.readByte() != 0 ? true : z;
    }
}
