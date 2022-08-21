package com.miui.gallery.provider.cache;

import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.StringUtils;

/* compiled from: Records.kt */
/* loaded from: classes2.dex */
public final class Media extends Record implements IMedia {
    public final long albumId;
    public final String albumServerId;
    public final long burstGroupKey;
    public final int burstIndex;
    public final String clearThumbnail;
    public final int createDate;
    public final long createTime;
    public final String creatorId;
    public final long dateModified;
    public final long duration;
    public final String filePath;
    public final int height;
    public final long id;
    public final boolean isFavorite;
    public final boolean isHidden;
    public final boolean isRubbish;
    public final boolean isTimeBurst;
    public final String latitude;
    public final Character latitudeRef;
    public final long localFlag;
    public final String location;
    public final String longitude;
    public final Character longitudeRef;
    public final String microThumb;
    public final String mimeType;
    public final int modifyDate;
    public final long orderDate;
    public final int orientation;
    public final byte[] secretKey;
    public final String serverId;
    public final String serverStatus;
    public final long serverTag;
    public final String sha1;
    public final boolean showInHomePage;
    public final long size;
    public final String smallSizeThumb;
    public final int sortDate;
    public final long sortTime;
    public final String sourcePkg;
    public final long specialTypeFlags;
    public final int syncState;
    public final byte[] thumbBlob;
    public final String thumbnail;
    public final String title;
    public final int type;
    public final int width;

    @Override // com.miui.gallery.provider.cache.IRecord
    public long getId() {
        return this.id;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSha1() {
        return this.sha1;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Long getAlbumId() {
        return Long.valueOf(this.albumId);
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getThumbnail() {
        return this.thumbnail;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getFilePath() {
        return this.filePath;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getType() {
        return this.type;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getTitle() {
        return this.title;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getDuration() {
        return this.duration;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSize() {
        return this.size;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getMimeType() {
        return this.mimeType;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getLocation() {
        return this.location;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getLatitude() {
        return this.latitude;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Character getLatitudeRef() {
        return this.latitudeRef;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getLongitude() {
        return this.longitude;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Character getLongitudeRef() {
        return this.longitudeRef;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSmallSizeThumb() {
        return this.smallSizeThumb;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getCreateTime() {
        return this.createTime;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getCreateDate() {
        return this.createDate;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getSyncState() {
        return this.syncState;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public byte[] getSecretKey() {
        return this.secretKey;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getWidth() {
        return this.width;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getHeight() {
        return this.height;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getCreatorId() {
        return this.creatorId;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public long getDateModified() {
        return this.dateModified;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public int getModifyDate() {
        return this.modifyDate;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getClearThumbnail() {
        return this.clearThumbnail;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isFavorite() {
        return this.isFavorite;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSpecialTypeFlags() {
        return this.specialTypeFlags;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSortTime() {
        return this.sortTime;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public int getSortDate() {
        return this.sortDate;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getBurstGroupKey() {
        return this.burstGroupKey;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isTimeBurst() {
        return this.isTimeBurst;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getBurstIndex() {
        return this.burstIndex;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public byte[] getThumbBlob() {
        return this.thumbBlob;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSourcePkg() {
        return this.sourcePkg;
    }

    public Media(long j, String str, long j2, String str2, String str3, String str4, int i, String str5, long j3, long j4, String str6, String str7, String str8, Character ch2, String str9, Character ch3, String str10, long j5, int i2, int i3, byte[] bArr, int i4, int i5, boolean z, String str11, long j6, boolean z2, long j7, String str12, long j8, int i6, String str13, boolean z3, String str14, long j9, long j10, int i7, int i8, long j11, String str15, boolean z4, boolean z5, int i9, byte[] bArr2, String str16, long j12) {
        super(null);
        this.id = j;
        this.sha1 = str;
        this.albumId = j2;
        this.microThumb = str2;
        this.thumbnail = str3;
        this.filePath = str4;
        this.type = i;
        this.title = str5;
        this.duration = j3;
        this.size = j4;
        this.mimeType = str6;
        this.location = str7;
        this.latitude = str8;
        this.latitudeRef = ch2;
        this.longitude = str9;
        this.longitudeRef = ch3;
        this.smallSizeThumb = str10;
        this.createTime = j5;
        this.createDate = i2;
        this.syncState = i3;
        this.secretKey = bArr;
        this.width = i4;
        this.height = i5;
        this.showInHomePage = z;
        this.creatorId = str11;
        this.localFlag = j6;
        this.isHidden = z2;
        this.serverTag = j7;
        this.serverId = str12;
        this.dateModified = j8;
        this.modifyDate = i6;
        this.clearThumbnail = str13;
        this.isFavorite = z3;
        this.albumServerId = str14;
        this.specialTypeFlags = j9;
        this.sortTime = j10;
        this.sortDate = i7;
        this.orientation = i8;
        this.burstGroupKey = j11;
        this.serverStatus = str15;
        this.isRubbish = z4;
        this.isTimeBurst = z5;
        this.burstIndex = i9;
        this.thumbBlob = bArr2;
        this.sourcePkg = str16;
        this.orderDate = j12;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public long getOrderDate(int i) {
        return this.orderDate;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public boolean hasValidLocationInfo() {
        if (StringUtils.isEmpty(getLatitude()) || StringUtils.isEmpty(getLongitude())) {
            return false;
        }
        String latitude = getLatitude();
        Character latitudeRef = getLatitudeRef();
        String str = null;
        double convertRationalLatLonToDouble = LocationUtil.convertRationalLatLonToDouble(latitude, latitudeRef == null ? null : latitudeRef.toString());
        String longitude = getLongitude();
        Character longitudeRef = getLongitudeRef();
        if (longitudeRef != null) {
            str = longitudeRef.toString();
        }
        double convertRationalLatLonToDouble2 = LocationUtil.convertRationalLatLonToDouble(longitude, str);
        if (convertRationalLatLonToDouble == SearchStatUtils.POW) {
            if (convertRationalLatLonToDouble2 == SearchStatUtils.POW) {
                return false;
            }
        }
        return true;
    }
}
