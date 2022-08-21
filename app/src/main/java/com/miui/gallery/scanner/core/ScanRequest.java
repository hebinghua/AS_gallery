package com.miui.gallery.scanner.core;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class ScanRequest implements Parcelable {
    public static final Parcelable.Creator<ScanRequest> CREATOR = new Parcelable.Creator<ScanRequest>() { // from class: com.miui.gallery.scanner.core.ScanRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ScanRequest mo1269createFromParcel(Parcel parcel) {
            return new ScanRequest(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ScanRequest[] mo1270newArray(int i) {
            return new ScanRequest[i];
        }
    };
    public boolean mMediaStoreSupportGalleryScan;
    public List<String> mPaths;
    public int mSceneCode;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ScanRequest(Builder builder) {
        int i = builder.sceneCode;
        this.mSceneCode = i;
        if (i == 0) {
            this.mMediaStoreSupportGalleryScan = builder.mediaStoreSupportGalleryScan;
        } else if (i == 1) {
        } else {
            this.mPaths = builder.paths;
        }
    }

    public ScanRequest(Parcel parcel) {
        this.mSceneCode = parcel.readInt();
        boolean z = true;
        this.mMediaStoreSupportGalleryScan = parcel.readByte() != 0;
        if (parcel.readByte() == 0 ? false : z) {
            ArrayList arrayList = new ArrayList(parcel.readInt());
            this.mPaths = arrayList;
            parcel.readStringList(arrayList);
        }
    }

    public boolean isMediaStoreSupportGalleryScan() {
        return this.mMediaStoreSupportGalleryScan;
    }

    public List<String> getPaths() {
        return this.mPaths;
    }

    public int getSceneCode() {
        return this.mSceneCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ScanRequest)) {
            return false;
        }
        ScanRequest scanRequest = (ScanRequest) obj;
        return this.mMediaStoreSupportGalleryScan == scanRequest.mMediaStoreSupportGalleryScan && Objects.equals(this.mPaths, scanRequest.mPaths) && this.mSceneCode == scanRequest.mSceneCode;
    }

    public int hashCode() {
        int i = 527 + (!this.mMediaStoreSupportGalleryScan ? 1 : 0);
        if (BaseMiscUtil.isValid(this.mPaths)) {
            i = (i * 31) + this.mPaths.hashCode();
        }
        return (i * 31) + this.mSceneCode;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mSceneCode);
        parcel.writeByte(this.mMediaStoreSupportGalleryScan ? (byte) 1 : (byte) 0);
        parcel.writeByte(BaseMiscUtil.isValid(this.mPaths) ? (byte) 1 : (byte) 0);
        if (BaseMiscUtil.isValid(this.mPaths)) {
            parcel.writeInt(this.mPaths.size());
            parcel.writeStringList(this.mPaths);
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public boolean mediaStoreSupportGalleryScan;
        public List<String> paths;
        public int sceneCode;

        public Builder isMediaStoreSupportGalleryScan(boolean z) {
            this.mediaStoreSupportGalleryScan = z;
            return this;
        }

        public Builder setPaths(List<String> list) {
            this.paths = list;
            return this;
        }

        public Builder setSceneCode(int i) {
            this.sceneCode = i;
            return this;
        }

        public ScanRequest build() {
            return new ScanRequest(this);
        }
    }
}
