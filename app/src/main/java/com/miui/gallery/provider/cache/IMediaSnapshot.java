package com.miui.gallery.provider.cache;

import android.text.TextUtils;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: IMediaSnapshot.kt */
/* loaded from: classes2.dex */
public interface IMediaSnapshot extends IRecord {
    long getBurstGroupKey();

    int getBurstIndex();

    String getClearThumbnail();

    int getCreateDate();

    long getCreateTime();

    long getDuration();

    String getFilePath();

    int getHeight();

    String getLocation();

    String getMimeType();

    String getSha1();

    long getSize();

    String getSmallSizeThumb();

    long getSortTime();

    String getSourcePkg();

    long getSpecialTypeFlags();

    int getSyncState();

    byte[] getThumbBlob();

    String getThumbnail();

    String getTitle();

    int getType();

    int getWidth();

    boolean isFavorite();

    boolean isTimeBurst();

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    default boolean itemSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (other instanceof IMediaSnapshot) {
            return getId() == ((IMediaSnapshot) other).getId();
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    default boolean contentSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (other instanceof IMediaSnapshot) {
            IMediaSnapshot iMediaSnapshot = (IMediaSnapshot) other;
            return getSize() == iMediaSnapshot.getSize() && getSpecialTypeFlags() == iMediaSnapshot.getSpecialTypeFlags() && isFavorite() == iMediaSnapshot.isFavorite() && getSyncState() == iMediaSnapshot.getSyncState() && getBurstGroupKey() == iMediaSnapshot.getBurstGroupKey() && getDuration() == iMediaSnapshot.getDuration() && TextUtils.equals(getSmallSizeThumb(), iMediaSnapshot.getSmallSizeThumb()) && TextUtils.equals(getClearThumbnail(), iMediaSnapshot.getClearThumbnail()) && TextUtils.equals(getSmallSizeThumb(), iMediaSnapshot.getSmallSizeThumb());
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }
}
