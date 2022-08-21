package com.miui.gallery.provider.cache;

/* compiled from: IMedia.kt */
/* loaded from: classes2.dex */
public interface IMedia extends IMediaSnapshot {
    Long getAlbumId();

    String getCreatorId();

    long getDateModified();

    String getLatitude();

    Character getLatitudeRef();

    String getLongitude();

    Character getLongitudeRef();

    int getModifyDate();

    long getOrderDate(int i);

    byte[] getSecretKey();

    int getSortDate();

    boolean hasValidLocationInfo();
}
