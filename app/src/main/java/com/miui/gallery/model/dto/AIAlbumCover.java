package com.miui.gallery.model.dto;

import android.os.Parcel;
import ch.qos.logback.core.CoreConstants;

/* loaded from: classes2.dex */
public class AIAlbumCover extends BaseAlbumCover {
    public String actionUri;
    public String type;

    public AIAlbumCover() {
    }

    public AIAlbumCover(Parcel parcel) {
        super(parcel);
        this.actionUri = parcel.readString();
        this.type = parcel.readString();
    }

    public String toString() {
        return "AIAlbumCover{actionUri='" + this.actionUri + CoreConstants.SINGLE_QUOTE_CHAR + ", type='" + this.type + CoreConstants.SINGLE_QUOTE_CHAR + ", coverId=" + this.coverId + ", coverPath='" + this.coverPath + CoreConstants.SINGLE_QUOTE_CHAR + ", coverSha1='" + this.coverSha1 + CoreConstants.SINGLE_QUOTE_CHAR + ", coverSyncState=" + this.coverSyncState + ", coverSize=" + this.coverSize + ", id=" + this.id + ", albumName='" + this.albumName + CoreConstants.SINGLE_QUOTE_CHAR + ", coverUri=" + this.coverUri + '}';
    }

    @Override // com.miui.gallery.model.dto.BaseAlbumCover, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.actionUri);
        parcel.writeString(this.type);
    }
}
