package com.miui.gallery.provider.cache;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: Records.kt */
/* loaded from: classes2.dex */
public final class MediaGroup extends Record implements IMedia {
    public final /* synthetic */ IMedia $$delegate_0;
    public final List<IMedia> medias;

    @Override // com.miui.gallery.provider.cache.IMedia
    public Long getAlbumId() {
        return this.$$delegate_0.getAlbumId();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getBurstGroupKey() {
        return this.$$delegate_0.getBurstGroupKey();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getBurstIndex() {
        return this.$$delegate_0.getBurstIndex();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getClearThumbnail() {
        return this.$$delegate_0.getClearThumbnail();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getCreateDate() {
        return this.$$delegate_0.getCreateDate();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getCreateTime() {
        return this.$$delegate_0.getCreateTime();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getCreatorId() {
        return this.$$delegate_0.getCreatorId();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public long getDateModified() {
        return this.$$delegate_0.getDateModified();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getDuration() {
        return this.$$delegate_0.getDuration();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getFilePath() {
        return this.$$delegate_0.getFilePath();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getHeight() {
        return this.$$delegate_0.getHeight();
    }

    @Override // com.miui.gallery.provider.cache.IRecord
    public long getId() {
        return this.$$delegate_0.getId();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getLatitude() {
        return this.$$delegate_0.getLatitude();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Character getLatitudeRef() {
        return this.$$delegate_0.getLatitudeRef();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getLocation() {
        return this.$$delegate_0.getLocation();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getLongitude() {
        return this.$$delegate_0.getLongitude();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Character getLongitudeRef() {
        return this.$$delegate_0.getLongitudeRef();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getMimeType() {
        return this.$$delegate_0.getMimeType();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public int getModifyDate() {
        return this.$$delegate_0.getModifyDate();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public long getOrderDate(int i) {
        return this.$$delegate_0.getOrderDate(i);
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public byte[] getSecretKey() {
        return this.$$delegate_0.getSecretKey();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSha1() {
        return this.$$delegate_0.getSha1();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSize() {
        return this.$$delegate_0.getSize();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSmallSizeThumb() {
        return this.$$delegate_0.getSmallSizeThumb();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public int getSortDate() {
        return this.$$delegate_0.getSortDate();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSortTime() {
        return this.$$delegate_0.getSortTime();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSourcePkg() {
        return this.$$delegate_0.getSourcePkg();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSpecialTypeFlags() {
        return this.$$delegate_0.getSpecialTypeFlags();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getSyncState() {
        return this.$$delegate_0.getSyncState();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public byte[] getThumbBlob() {
        return this.$$delegate_0.getThumbBlob();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getThumbnail() {
        return this.$$delegate_0.getThumbnail();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getTitle() {
        return this.$$delegate_0.getTitle();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getType() {
        return this.$$delegate_0.getType();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getWidth() {
        return this.$$delegate_0.getWidth();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public boolean hasValidLocationInfo() {
        return this.$$delegate_0.hasValidLocationInfo();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isFavorite() {
        return this.$$delegate_0.isFavorite();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isTimeBurst() {
        return this.$$delegate_0.isTimeBurst();
    }

    public final List<IMedia> getMedias() {
        return this.medias;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public MediaGroup(List<? extends IMedia> medias, int i) {
        super(null);
        Intrinsics.checkNotNullParameter(medias, "medias");
        this.medias = medias;
        this.$$delegate_0 = (IMedia) medias.get(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    public boolean itemSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (other instanceof MediaGroup) {
            return getId() == ((MediaGroup) other).getId();
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    public boolean contentSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (other instanceof MediaGroup) {
            return this.medias.size() == ((MediaGroup) other).medias.size() && super.contentSameAs(other);
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    public boolean genericItemSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return Intrinsics.areEqual(Reflection.getOrCreateKotlinClass(other.getClass()), Reflection.getOrCreateKotlinClass(MediaGroup.class)) && itemSameAs((MediaGroup) other);
    }

    @Override // com.miui.gallery.widget.recyclerview.DiffableItem
    public boolean genericContentSameAs(Object other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return Intrinsics.areEqual(Reflection.getOrCreateKotlinClass(other.getClass()), Reflection.getOrCreateKotlinClass(MediaGroup.class)) && contentSameAs((MediaGroup) other);
    }
}
