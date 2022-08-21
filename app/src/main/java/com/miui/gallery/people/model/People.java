package com.miui.gallery.people.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.face.FaceRegionRectF;

/* loaded from: classes2.dex */
public class People implements Parcelable {
    public static final Parcelable.Creator<People> CREATOR = new Parcelable.Creator<People>() { // from class: com.miui.gallery.people.model.People.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public People mo1170createFromParcel(Parcel parcel) {
            People people = new People();
            people.readFromParcel(parcel);
            return people;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public People[] mo1171newArray(int i) {
            return new People[i];
        }
    };
    public FaceRegionRectF mCoverFaceRect;
    public long mCoverImageId;
    public String mCoverPath;
    public DownloadType mCoverType;
    public int mFaceCount;
    public long mId;
    public String mName;
    public String mRelationText;
    public int mRelationType;
    public String mServerId;
    public int mVisibilityType;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long j) {
        this.mId = j;
    }

    public void setServerId(String str) {
        this.mServerId = str;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public int getRelationType() {
        return this.mRelationType;
    }

    public void setRelationType(int i) {
        this.mRelationType = i;
    }

    public void setRelationText(String str) {
        this.mRelationText = str;
    }

    public long getCoverImageId() {
        return this.mCoverImageId;
    }

    public void setCoverImageId(long j) {
        this.mCoverImageId = j;
    }

    public FaceRegionRectF getCoverFaceRect() {
        return this.mCoverFaceRect;
    }

    public void setCoverFaceRect(FaceRegionRectF faceRegionRectF) {
        this.mCoverFaceRect = faceRegionRectF;
    }

    public int getFaceCount() {
        return this.mFaceCount;
    }

    public void setFaceCount(int i) {
        this.mFaceCount = i;
    }

    public String getCoverPath() {
        return this.mCoverPath;
    }

    public void setCoverPath(String str) {
        this.mCoverPath = str;
    }

    public DownloadType getCoverType() {
        return this.mCoverType;
    }

    public void setCoverType(DownloadType downloadType) {
        this.mCoverType = downloadType;
    }

    public void setVisibilityType(int i) {
        this.mVisibilityType = i;
    }

    public void readFromParcel(Parcel parcel) {
        this.mId = parcel.readLong();
        this.mServerId = parcel.readString();
        this.mName = parcel.readString();
        this.mRelationType = parcel.readInt();
        this.mRelationText = parcel.readString();
        this.mCoverImageId = parcel.readLong();
        this.mCoverPath = parcel.readString();
        this.mCoverType = (DownloadType) parcel.readParcelable(DownloadType.class.getClassLoader());
        this.mCoverFaceRect = (FaceRegionRectF) parcel.readParcelable(FaceRegionRectF.class.getClassLoader());
        this.mFaceCount = parcel.readInt();
        this.mVisibilityType = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mId);
        parcel.writeString(this.mServerId);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mRelationType);
        parcel.writeString(this.mRelationText);
        parcel.writeLong(this.mCoverImageId);
        parcel.writeString(this.mCoverPath);
        parcel.writeParcelable(this.mCoverType, i);
        parcel.writeParcelable(this.mCoverFaceRect, i);
        parcel.writeInt(this.mFaceCount);
        parcel.writeInt(this.mVisibilityType);
    }
}
