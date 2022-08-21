package com.miui.gallery.model.dto;

import android.os.Parcel;
import android.os.Parcelable;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.util.face.FaceRegionRectF;
import java.util.Objects;

/* loaded from: classes2.dex */
public class FaceAlbumCover extends AIAlbumCover {
    public static final Parcelable.Creator<FaceAlbumCover> CREATOR = new Parcelable.Creator<FaceAlbumCover>() { // from class: com.miui.gallery.model.dto.FaceAlbumCover.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FaceAlbumCover mo1141createFromParcel(Parcel parcel) {
            return new FaceAlbumCover(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FaceAlbumCover[] mo1142newArray(int i) {
            return new FaceAlbumCover[i];
        }
    };
    public FaceRegionRectF faceRectF;

    @Override // com.miui.gallery.model.dto.BaseAlbumCover, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public FaceAlbumCover() {
        this.type = "ai_cover_face";
    }

    public FaceAlbumCover(Parcel parcel) {
        super(parcel);
        this.faceRectF = (FaceRegionRectF) parcel.readParcelable(FaceRegionRectF.class.getClassLoader());
        this.type = "ai_cover_face";
    }

    @Override // com.miui.gallery.model.dto.BaseAlbumCover
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FaceAlbumCover) || !super.equals(obj)) {
            return false;
        }
        return Objects.equals(this.faceRectF, ((FaceAlbumCover) obj).faceRectF);
    }

    @Override // com.miui.gallery.model.dto.BaseAlbumCover
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), this.faceRectF);
    }

    @Override // com.miui.gallery.model.dto.AIAlbumCover
    public String toString() {
        return "FaceAlbumCover{faceRectF=" + this.faceRectF + ", coverId=" + this.coverId + ", coverPath='" + this.coverPath + CoreConstants.SINGLE_QUOTE_CHAR + ", coverSha1='" + this.coverSha1 + CoreConstants.SINGLE_QUOTE_CHAR + ", coverSyncState=" + this.coverSyncState + ", coverSize=" + this.coverSize + '}';
    }

    @Override // com.miui.gallery.model.dto.AIAlbumCover, com.miui.gallery.model.dto.BaseAlbumCover, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.faceRectF, i);
    }
}
