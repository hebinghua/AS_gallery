package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.provider.MediaSortDateHelper;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.Filter;
import com.miui.gallery.sdk.SyncStatus;
import com.miui.gallery.sdk.uploadstatus.ItemType;
import com.miui.gallery.sdk.uploadstatus.SyncProxy;
import com.miui.gallery.sdk.uploadstatus.UploadStatusItem;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class MediaCacheItem implements CacheItem, IMedia {
    public Long mAlbumAttributes;
    public Long mAlbumId;
    public String mAlbumServerId;
    public int mAliasCreateDate;
    public int mAliasModifyDate;
    public int mAliasSortDate;
    public long mAliasSortTime;
    public long mAliasSyncState;
    public long mBurstGroupKey;
    public int mBurstIndex;
    public String mCreatorId;
    public long mDateModified;
    public String mDescription;
    public Long mDuration;
    public FavoritesDelegate mFavoritesDelegate;
    public String mFilePath;
    public Integer mHeight;
    public long mId;
    public boolean mIsFavorite;
    public Boolean mIsSyncing;
    public long mIsTimeBurst;
    public String mLatitude;
    public Character mLatitudeRef;
    public Long mLocalFlag;
    public String mLocation;
    public String mLongitude;
    public Character mLongitudeRef;
    public String mMicroThumb;
    public String mMimeType;
    public long mMixedTime;
    public Integer mOrientation;
    public byte[] mSecretKey;
    public String mServerId;
    public String mServerStatus;
    public Long mServerTag;
    public String mSha1;
    public Long mSize;
    public String mSourcePkg;
    public Long mSpecialTypeFlags;
    public String mThumbnail;
    public String mTitle;
    public Integer mType;
    public Integer mWidth;

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public byte[] getThumbBlob() {
        return null;
    }

    public boolean columnEquals(CacheItem cacheItem, int i) {
        return CacheUtils.columnEquals(this, cacheItem, i);
    }

    public long getLong(int i) {
        return CacheUtils.getLong(this, i);
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public Object get(int i, boolean z) {
        switch (i) {
            case 0:
                return Long.valueOf(this.mId);
            case 1:
                String str = this.mSha1;
                if (str != null) {
                    return str;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 2:
                Long l = this.mAlbumId;
                if (l != null) {
                    return l;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_LONG;
            case 3:
                String str2 = this.mMicroThumb;
                if (str2 != null) {
                    return str2;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 4:
                String str3 = this.mThumbnail;
                if (str3 != null) {
                    return str3;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 5:
                String str4 = this.mFilePath;
                if (str4 != null) {
                    return str4;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 6:
                Integer num = this.mType;
                if (num != null) {
                    return num;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_INT;
            case 7:
                String str5 = this.mTitle;
                if (str5 != null) {
                    return str5;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 8:
                Long l2 = this.mDuration;
                if (l2 != null) {
                    return l2;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_LONG;
            case 9:
                String str6 = this.mDescription;
                if (str6 != null) {
                    return str6;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 10:
                return this.mSize;
            case 11:
                String str7 = this.mMimeType;
                if (str7 != null) {
                    return str7;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 12:
                String str8 = this.mLocation;
                if (str8 != null) {
                    return str8;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 13:
                String str9 = this.mLatitude;
                if (str9 != null) {
                    return str9;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_LONG;
            case 14:
                Character ch2 = this.mLatitudeRef;
                return ch2 != null ? ch2 : z ? ' ' : null;
            case 15:
                String str10 = this.mLongitude;
                if (str10 != null) {
                    return str10;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_LONG;
            case 16:
                Character ch3 = this.mLongitudeRef;
                return ch3 != null ? ch3 : z ? ' ' : null;
            case 17:
                String aliasMicroThumbnail = getAliasMicroThumbnail();
                if (aliasMicroThumbnail != null) {
                    return aliasMicroThumbnail;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_LONG;
            case 18:
                return Long.valueOf(this.mMixedTime);
            case 19:
                return Integer.valueOf(this.mAliasCreateDate);
            case 20:
                return getAliasSyncState();
            case 21:
                byte[] bArr = this.mSecretKey;
                if (bArr != null) {
                    return bArr;
                }
                if (!z) {
                    return null;
                }
                return new byte[0];
            case 22:
                Integer num2 = this.mWidth;
                if (num2 != null) {
                    return num2;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_INT;
            case 23:
                Integer num3 = this.mHeight;
                if (num3 != null) {
                    return num3;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_INT;
            case 24:
                return CacheItem.TRUE;
            case 25:
                return this.mCreatorId;
            case 26:
                return this.mLocalFlag;
            case 27:
                return getAliasHidden();
            case 28:
                return this.mServerTag;
            case 29:
                return this.mServerId;
            case 30:
                return Long.valueOf(this.mDateModified);
            case 31:
                return Integer.valueOf(this.mAliasModifyDate);
            case 32:
                String aliasClearThumbnail = getAliasClearThumbnail();
                if (aliasClearThumbnail != null) {
                    return aliasClearThumbnail;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 33:
                return getAliasIsFavorite();
            case 34:
                String str11 = this.mAlbumServerId;
                if (str11 != null) {
                    return str11;
                }
                if (!z) {
                    return null;
                }
                return "";
            case 35:
                return this.mSpecialTypeFlags;
            case 36:
                return Long.valueOf(this.mAliasSortTime);
            case 37:
                return Integer.valueOf(this.mAliasSortDate);
            case 38:
                Integer num4 = this.mOrientation;
                if (num4 != null) {
                    return num4;
                }
                if (!z) {
                    return null;
                }
                return CacheItem.DEFAULT_INT;
            case 39:
                return Long.valueOf(this.mBurstGroupKey);
            case 40:
                return this.mServerStatus;
            case 41:
                return getAliasRubbish();
            case 42:
                return Long.valueOf(this.mIsTimeBurst);
            case 43:
                return Integer.valueOf(this.mBurstIndex);
            case 44:
                return null;
            case 45:
                return this.mAlbumAttributes;
            case 46:
                return this.mSourcePkg;
            default:
                throw new IllegalArgumentException(" not recognized column. ");
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public int getType(int i) {
        switch (i) {
            case 0:
                return 1;
            case 1:
                return this.mSha1 != null ? 3 : 0;
            case 2:
                return this.mAlbumId != null ? 1 : 0;
            case 3:
                return this.mMicroThumb != null ? 3 : 0;
            case 4:
                return this.mThumbnail != null ? 3 : 0;
            case 5:
                return this.mFilePath != null ? 3 : 0;
            case 6:
                return this.mType != null ? 1 : 0;
            case 7:
                return this.mTitle != null ? 3 : 0;
            case 8:
                return this.mDuration != null ? 1 : 0;
            case 9:
                return this.mDescription != null ? 3 : 0;
            case 10:
                return 1;
            case 11:
                return this.mMimeType != null ? 3 : 0;
            case 12:
                return this.mLocation != null ? 3 : 0;
            case 13:
                return this.mLatitude != null ? 3 : 0;
            case 14:
                return this.mLatitudeRef != null ? 3 : 0;
            case 15:
                return this.mLongitude != null ? 3 : 0;
            case 16:
                return this.mLongitudeRef != null ? 3 : 0;
            case 17:
                return getAliasMicroThumbnail() != null ? 3 : 0;
            case 18:
            case 19:
            case 20:
                return 1;
            case 21:
                return this.mSecretKey != null ? 4 : 0;
            case 22:
                return this.mWidth != null ? 1 : 0;
            case 23:
                return this.mHeight != null ? 1 : 0;
            case 24:
                return 1;
            case 25:
                return 3;
            case 26:
            case 27:
            case 28:
                return 1;
            case 29:
                return 3;
            case 30:
            case 31:
                return 1;
            case 32:
                return getAliasClearThumbnail() != null ? 3 : 0;
            case 33:
                return 1;
            case 34:
                return this.mAlbumServerId != null ? 3 : 0;
            case 35:
            case 36:
            case 37:
                return 1;
            case 38:
                return this.mOrientation != null ? 1 : 0;
            case 39:
                return 1;
            case 40:
                return this.mServerStatus != null ? 3 : 0;
            case 41:
            case 42:
            case 43:
                return 1;
            case 44:
                return 4;
            case 45:
                return 1;
            case 46:
                return TextUtils.isEmpty(this.mSourcePkg) ? 0 : 3;
            default:
                throw new IllegalArgumentException(" not recognized column. ");
        }
    }

    public void internalCopy(MediaCacheItem mediaCacheItem) {
        mediaCacheItem.mId = this.mId;
        mediaCacheItem.mSha1 = this.mSha1;
        mediaCacheItem.mAlbumId = this.mAlbumId;
        mediaCacheItem.mMicroThumb = this.mMicroThumb;
        mediaCacheItem.mThumbnail = this.mThumbnail;
        mediaCacheItem.mFilePath = this.mFilePath;
        mediaCacheItem.mType = this.mType;
        mediaCacheItem.mTitle = this.mTitle;
        mediaCacheItem.mDuration = this.mDuration;
        mediaCacheItem.mDescription = this.mDescription;
        mediaCacheItem.mLocation = this.mLocation;
        mediaCacheItem.mSize = this.mSize;
        mediaCacheItem.mMimeType = this.mMimeType;
        mediaCacheItem.mLatitude = this.mLatitude;
        mediaCacheItem.mLatitudeRef = this.mLatitudeRef;
        mediaCacheItem.mLongitude = this.mLongitude;
        mediaCacheItem.mLongitudeRef = this.mLongitudeRef;
        mediaCacheItem.mSecretKey = this.mSecretKey;
        mediaCacheItem.mMixedTime = this.mMixedTime;
        mediaCacheItem.mLocalFlag = this.mLocalFlag;
        mediaCacheItem.mIsSyncing = this.mIsSyncing;
        mediaCacheItem.mWidth = this.mWidth;
        mediaCacheItem.mHeight = this.mHeight;
        mediaCacheItem.mServerStatus = this.mServerStatus;
        mediaCacheItem.mAlbumServerId = this.mAlbumServerId;
        mediaCacheItem.mSpecialTypeFlags = this.mSpecialTypeFlags;
        mediaCacheItem.mOrientation = this.mOrientation;
        mediaCacheItem.mAliasCreateDate = this.mAliasCreateDate;
        mediaCacheItem.mCreatorId = this.mCreatorId;
        mediaCacheItem.mServerTag = this.mServerTag;
        mediaCacheItem.mServerId = this.mServerId;
        mediaCacheItem.mDateModified = this.mDateModified;
        mediaCacheItem.mAliasModifyDate = this.mAliasModifyDate;
        mediaCacheItem.mFavoritesDelegate = this.mFavoritesDelegate;
        mediaCacheItem.mIsFavorite = this.mIsFavorite;
        mediaCacheItem.mAliasSortTime = this.mAliasSortTime;
        mediaCacheItem.mAliasSortDate = this.mAliasSortDate;
        mediaCacheItem.mBurstGroupKey = this.mBurstGroupKey;
        mediaCacheItem.mIsTimeBurst = this.mIsTimeBurst;
        mediaCacheItem.mBurstIndex = this.mBurstIndex;
        mediaCacheItem.mAlbumAttributes = this.mAlbumAttributes;
        mediaCacheItem.mSourcePkg = this.mSourcePkg;
        mediaCacheItem.mAliasSyncState = this.mAliasSyncState;
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    /* renamed from: copy */
    public MediaCacheItem mo1224copy() {
        MediaCacheItem mediaCacheItem = new MediaCacheItem();
        internalCopy(mediaCacheItem);
        return mediaCacheItem;
    }

    public String getAliasClearThumbnail() {
        if (!TextUtils.isEmpty(this.mFilePath)) {
            return this.mFilePath;
        }
        if (!TextUtils.isEmpty(this.mThumbnail)) {
            return this.mThumbnail;
        }
        if (TextUtils.isEmpty(this.mMicroThumb)) {
            return null;
        }
        return this.mMicroThumb;
    }

    public String getAliasMicroThumbnail() {
        if (!TextUtils.isEmpty(this.mMicroThumb)) {
            return this.mMicroThumb;
        }
        if (!TextUtils.isEmpty(this.mThumbnail)) {
            return this.mThumbnail;
        }
        if (TextUtils.isEmpty(this.mFilePath)) {
            return null;
        }
        return this.mFilePath;
    }

    public Long getAliasHidden() {
        if (getAlbumId() == null) {
            return CacheItem.FALSE;
        }
        return AlbumCacheManager.getInstance().isHidden(getAlbumId().longValue()) ? CacheItem.TRUE : CacheItem.FALSE;
    }

    public Long getAliasRubbish() {
        if (getAlbumId() == null) {
            return CacheItem.FALSE;
        }
        return AlbumCacheManager.getInstance().isRubbish(getAlbumId().longValue()) ? CacheItem.TRUE : CacheItem.FALSE;
    }

    public Long getAliasAlbumAttributes() {
        if (getAlbumId() == null) {
            return CacheItem.FALSE;
        }
        return Long.valueOf(AlbumCacheManager.getInstance().getAttributes(getAlbumId().longValue()));
    }

    public Long getAliasIsFavorite() {
        return this.mFavoritesDelegate.isFavorite(Long.valueOf(this.mId)) ? CacheItem.TRUE : CacheItem.FALSE;
    }

    public boolean isVideoItem() {
        return BaseFileMimeUtil.isVideoFromMimeType(this.mMimeType);
    }

    public boolean isImageItem() {
        return BaseFileMimeUtil.isImageFromMimeType(this.mMimeType);
    }

    @Override // com.miui.gallery.provider.cache.IRecord
    public long getId() {
        return this.mId;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Long getAlbumId() {
        return this.mAlbumId;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSha1() {
        return this.mSha1;
    }

    public int getAliasSortDate() {
        return this.mAliasSortDate;
    }

    public long getAliasSortTime() {
        return this.mAliasSortTime;
    }

    public int getAliasModifyDate() {
        return this.mAliasModifyDate;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public long getDateModified() {
        return this.mDateModified;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getLocation() {
        return this.mLocation;
    }

    public Long getAliasSyncState() {
        return Long.valueOf(this.mAliasSyncState);
    }

    public long calAliasSyncState() {
        Long l = this.mLocalFlag;
        if (l == null) {
            DefaultLogger.e(".provider.cache.MediaItem", "localFlag is null!");
            return 2147483647L;
        } else if (l.longValue() == 0) {
            return 0L;
        } else {
            if (this.mLocalFlag.longValue() == 5 || this.mLocalFlag.longValue() == 6 || this.mLocalFlag.longValue() == 9) {
                return 1L;
            }
            if (getAlbumId() == null || !AlbumCacheManager.getInstance().isAutoUpload(getAlbumId())) {
                return 4L;
            }
            if ("temp".equals(this.mServerStatus) && TextUtils.isEmpty(this.mFilePath)) {
                return 4L;
            }
            if (this.mIsSyncing == null) {
                UploadStatusItem uploadStatus = SyncProxy.getInstance().getUploadStatusProxy().getUploadStatus(new UploadStatusItem(ItemType.OWNER, String.valueOf(this.mId)));
                this.mIsSyncing = Boolean.valueOf(uploadStatus != null && uploadStatus.mStatus == SyncStatus.STATUS_INIT);
            }
            return this.mIsSyncing.booleanValue() ? 2L : 3L;
        }
    }

    public boolean isValidThumbnailFile() {
        return !StringUtils.isEmpty(this.mThumbnail);
    }

    public boolean isValidOriginFile() {
        return !StringUtils.isEmpty(this.mFilePath);
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public boolean hasValidLocationInfo() {
        if (StringUtils.isEmpty(this.mLatitude) || StringUtils.isEmpty(this.mLongitude)) {
            return false;
        }
        return (LocationUtil.convertRationalLatLonToDouble(this.mLatitude, String.valueOf(this.mLatitudeRef)) == SearchStatUtils.POW && LocationUtil.convertRationalLatLonToDouble(this.mLongitude, String.valueOf(this.mLongitudeRef)) == SearchStatUtils.POW) ? false : true;
    }

    public void regenerateSortTimeAndDate() {
        if (getAlbumId() == null) {
            this.mAliasSortTime = this.mMixedTime;
            this.mAliasSortDate = this.mAliasCreateDate;
            return;
        }
        MediaSortDateHelper.SortDate sortDate = AlbumCacheManager.getInstance().getSortDate(getAlbumId().longValue());
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$provider$MediaSortDateHelper$SortDate[sortDate.ordinal()];
        if (i == 1) {
            this.mAliasSortTime = this.mMixedTime;
            this.mAliasSortDate = this.mAliasCreateDate;
        } else if (i == 2) {
            this.mAliasSortTime = this.mDateModified;
            this.mAliasSortDate = this.mAliasModifyDate;
        } else {
            throw new IllegalArgumentException("Unsupported sort date " + sortDate);
        }
    }

    /* renamed from: com.miui.gallery.provider.cache.MediaCacheItem$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$provider$MediaSortDateHelper$SortDate;

        static {
            int[] iArr = new int[MediaSortDateHelper.SortDate.values().length];
            $SwitchMap$com$miui$gallery$provider$MediaSortDateHelper$SortDate = iArr;
            try {
                iArr[MediaSortDateHelper.SortDate.CREATE_TIME.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$MediaSortDateHelper$SortDate[MediaSortDateHelper.SortDate.MODIFY_TIME.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getThumbnail() {
        return this.mThumbnail;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getFilePath() {
        return this.mFilePath;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getType() {
        return this.mType.intValue();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getTitle() {
        return this.mTitle;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getDuration() {
        Long l = this.mDuration;
        if (l != null) {
            return l.longValue();
        }
        return 0L;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSize() {
        Long l = this.mSize;
        if (l != null) {
            return l.longValue();
        }
        return 0L;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getMimeType() {
        return this.mMimeType;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getLatitude() {
        return this.mLatitude;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Character getLatitudeRef() {
        return this.mLatitudeRef;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getLongitude() {
        return this.mLongitude;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public Character getLongitudeRef() {
        return this.mLongitudeRef;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSmallSizeThumb() {
        return getAliasMicroThumbnail();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getCreateTime() {
        return this.mMixedTime;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getCreateDate() {
        return this.mAliasCreateDate;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getSyncState() {
        return getAliasSyncState().intValue();
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public byte[] getSecretKey() {
        return this.mSecretKey;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getWidth() {
        Integer num = this.mWidth;
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getHeight() {
        Integer num = this.mHeight;
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public String getCreatorId() {
        return this.mCreatorId;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public int getModifyDate() {
        return this.mAliasModifyDate;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getClearThumbnail() {
        return getAliasClearThumbnail();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isFavorite() {
        return this.mIsFavorite;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSpecialTypeFlags() {
        Long l = this.mSpecialTypeFlags;
        if (l != null) {
            return l.longValue();
        }
        return 0L;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSortTime() {
        return this.mAliasSortTime;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public int getSortDate() {
        return this.mAliasSortDate;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getBurstGroupKey() {
        return this.mBurstGroupKey;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isTimeBurst() {
        return this.mIsTimeBurst == CacheItem.TRUE.longValue();
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getBurstIndex() {
        return this.mBurstIndex;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSourcePkg() {
        return this.mSourcePkg;
    }

    @Override // com.miui.gallery.provider.cache.IMedia
    public long getOrderDate(int i) {
        return getLong(i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.mId == ((MediaCacheItem) obj).mId;
    }

    public int hashCode() {
        return Long.hashCode(this.mId);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaItem{mId=");
        sb.append(this.mId);
        sb.append(", mSha1='");
        sb.append(this.mSha1);
        sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
        sb.append(", mAlbumId=");
        Long l = this.mAlbumId;
        sb.append(l != null ? l.longValue() : -1L);
        sb.append(", mMicroThumb='");
        sb.append(this.mMicroThumb);
        sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
        sb.append(", mThumbnail='");
        sb.append(this.mThumbnail);
        sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
        sb.append(", mFilePath='");
        sb.append(this.mFilePath);
        sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
        sb.append(", mType=");
        sb.append(this.mType);
        sb.append(", mTitle='");
        sb.append(this.mTitle);
        sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
        sb.append(", mMimeType='");
        sb.append(this.mMimeType);
        sb.append(CoreConstants.SINGLE_QUOTE_CHAR);
        sb.append(", mLocalFlag=");
        sb.append(this.mLocalFlag);
        sb.append(", mIsSyncing=");
        sb.append(this.mIsSyncing);
        sb.append(", mSecretKey=");
        sb.append(Arrays.toString(this.mSecretKey));
        sb.append(", mMixedTime=");
        sb.append(this.mMixedTime);
        sb.append(", mAliasCreateDate=");
        sb.append(this.mAliasCreateDate);
        sb.append(", mDateModified=");
        sb.append(this.mDateModified);
        sb.append(", mAliasSortTime=");
        sb.append(this.mAliasSortTime);
        sb.append(", mAlbumServerId=");
        sb.append(this.mAlbumServerId);
        sb.append(", mSpecialTypeFlags=");
        sb.append(this.mSpecialTypeFlags);
        sb.append('}');
        return sb.toString();
    }

    @Override // com.miui.gallery.provider.cache.CacheItem
    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaItem{id=");
        sb.append(this.mId);
        sb.append(", albumId=");
        Long l = this.mAlbumId;
        sb.append(l != null ? l.longValue() : -1L);
        sb.append(", type=");
        sb.append(this.mType);
        sb.append('}');
        return sb.toString();
    }

    /* loaded from: classes2.dex */
    public static class QueryFactory<T extends MediaCacheItem> implements CacheItem.QueryFactory<T> {
        public static final CacheItem.ColumnMapper COLUMN_MAPPER;
        public static final HashMap<String, Integer> PROJECTION;
        public static final CacheItem.Merger<MediaCacheItem> SHA1_MERGER;
        public final AlbumAttributesFilter mShowInHomePageFilter = new AlbumAttributesFilter(Filter.Comparator.EQUALS, String.valueOf(4L));
        public final Filter.CompareFilter<T> mNotSecretItemFilter = (Filter.CompareFilter<T>) new Filter.CompareFilter<T>(null, null) { // from class: com.miui.gallery.provider.cache.MediaCacheItem.QueryFactory.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((AnonymousClass1) ((MediaCacheItem) cacheItem));
            }

            public T filter(T t) {
                if (t.getAlbumId() == null || t.getAlbumId().longValue() == -1000) {
                    return null;
                }
                return t;
            }
        };
        public final FavoriteItemFilter mFavoriteItemFilter = new FavoriteItemFilter(null, null);

        static {
            HashMap<String, Integer> hashMap = new HashMap<>(66);
            PROJECTION = hashMap;
            hashMap.put(j.c, 0);
            hashMap.put("sha1", 1);
            hashMap.put("localGroupId", 2);
            hashMap.put("microthumbfile", 3);
            hashMap.put("thumbnailFile", 4);
            hashMap.put("localFile", 5);
            hashMap.put("serverType", 6);
            hashMap.put("title", 7);
            hashMap.put("duration", 8);
            hashMap.put("description", 9);
            hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, 10);
            hashMap.put("mimeType", 11);
            hashMap.put("location", 12);
            hashMap.put("exifGPSLatitude", 13);
            hashMap.put("exifGPSLatitudeRef", 14);
            hashMap.put("exifGPSLongitude", 15);
            hashMap.put("exifGPSLongitudeRef", 16);
            hashMap.put("alias_micro_thumbnail", 17);
            hashMap.put("alias_create_time", 18);
            hashMap.put("alias_create_date", 19);
            hashMap.put("alias_sync_state", 20);
            hashMap.put("secretKey", 21);
            hashMap.put("exifImageWidth", 22);
            hashMap.put("exifImageLength", 23);
            hashMap.put("alias_show_in_homepage", 24);
            hashMap.put("creatorId", 25);
            hashMap.put("localFlag", 26);
            hashMap.put("alias_hidden", 27);
            hashMap.put("serverTag", 28);
            hashMap.put("serverId", 29);
            hashMap.put("dateModified", 30);
            hashMap.put("alias_modify_date", 31);
            hashMap.put("alias_clear_thumbnail", 32);
            hashMap.put("alias_is_favorite", 33);
            hashMap.put("groupId", 34);
            hashMap.put("specialTypeFlags", 35);
            hashMap.put("alias_sort_time", 36);
            hashMap.put("alias_sort_date", 37);
            hashMap.put("exifOrientation", 38);
            hashMap.put("burst_group_id", 39);
            hashMap.put("serverStatus", 40);
            hashMap.put("alias_rubbish", 41);
            hashMap.put("is_time_burst", 42);
            hashMap.put("burst_index", 43);
            hashMap.put("thumbnail_blob", 44);
            hashMap.put("alias_album_attributes", 45);
            hashMap.put("sourcePackage", 46);
            hashMap.put("alias_fold_burst", -4);
            hashMap.put("alias_is_burst", -5);
            SHA1_MERGER = MediaCacheItem$QueryFactory$$ExternalSyntheticLambda0.INSTANCE;
            COLUMN_MAPPER = new CacheItem.ColumnMapper() { // from class: com.miui.gallery.provider.cache.MediaCacheItem.QueryFactory.2
                @Override // com.miui.gallery.provider.cache.CacheItem.ColumnMapper
                public int getIndex(String str) {
                    Integer num = QueryFactory.PROJECTION.get(str);
                    if (num == null) {
                        DefaultLogger.e(".provider.cache.MediaItem", "column '%s' not found", str);
                        return -1;
                    }
                    return num.intValue();
                }
            };
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public CacheItem.ColumnMapper getMapper() {
            return COLUMN_MAPPER;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public CacheItem.Merger<T> getMerger(int i) {
            if (i == 1) {
                return (CacheItem.Merger<T>) SHA1_MERGER;
            }
            return null;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory
        public Comparator<T> getComparator(int i, boolean z) {
            if (i == 18) {
                return new TimeComparator(z);
            }
            if (i == 7) {
                return new TitleComparator(z);
            }
            if (i == 10) {
                return new SizeComparator(z);
            }
            if (i == 30) {
                return new DateModifiedComparator(z);
            }
            if (i == 36) {
                return new SortTimeComparator(z);
            }
            if (i != 43) {
                return null;
            }
            return new BurstIndexComparator(z);
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.QueryFactory, com.miui.gallery.provider.cache.Filter.FilterFactory
        public Filter.CompareFilter<T> getFilter(int i, Filter.Comparator comparator, String str) {
            if (i == 24) {
                return this.mShowInHomePageFilter;
            }
            if (i == 27) {
                return new AlbumAttributesFilter(Long.valueOf(str).equals(CacheItem.FALSE) ? Filter.Comparator.NOT_EQUALS : Filter.Comparator.EQUALS, String.valueOf(16L));
            } else if (i == 33) {
                return this.mFavoriteItemFilter;
            } else {
                if (i == 41) {
                    return new AlbumAttributesFilter(Long.valueOf(str).equals(CacheItem.FALSE) ? Filter.Comparator.NOT_EQUALS : Filter.Comparator.EQUALS, String.valueOf(2048L));
                } else if (i == 0) {
                    return new IdFilter(comparator, str);
                } else {
                    if (i == 2) {
                        return new AlbumFilter(comparator, str);
                    }
                    if (i == 6) {
                        return new TypeFilter(comparator, str);
                    }
                    if (i == 1) {
                        return new Sha1Filter(comparator, str);
                    }
                    if (i == 20) {
                        return new SyncStateFilter(comparator, str);
                    }
                    if (i == 7) {
                        return new TitleFilter(comparator, str);
                    }
                    if (i == 5) {
                        return new LocalFileFilter(comparator, str);
                    }
                    if (i == 4) {
                        return new ThumbnailFilter(comparator, str);
                    }
                    if (i == 26) {
                        return new LocalFlagFilter(comparator, str);
                    }
                    if (i == 28) {
                        return new ServerTagFilter(comparator, str);
                    }
                    if (i == 29) {
                        return new ServerIdFilter(comparator, str);
                    }
                    if (i == 19) {
                        return new AliasCreateDateFilter(comparator, str);
                    }
                    if (i == 11) {
                        return new MimeTypeFilter(comparator, str);
                    }
                    if (i == 34) {
                        return new AlbumServerIdFilter(comparator, str);
                    }
                    if (i == 12) {
                        return new LocationFilter(comparator, str);
                    }
                    if (i == 10) {
                        return new SizeFilter(comparator, str);
                    }
                    if (i == 32) {
                        return new AliasClearThumbnailFilter(comparator, str);
                    }
                    if (i == 31) {
                        return new AliasModifyDateFilter(comparator, str);
                    }
                    if (i == 45) {
                        return new AlbumAttributesFilter(comparator, str);
                    }
                    if (i == -1) {
                        return new OriginFileCheckFilter(comparator, str);
                    }
                    if (i == -2) {
                        return new ValidCoverPathFilter(comparator, str);
                    }
                    if (i == 40) {
                        return new ServerStatusFilter(comparator, str);
                    }
                    if (i == -3) {
                        return this.mNotSecretItemFilter;
                    }
                    if (i == 15) {
                        return new LongitudeFilter(comparator, str);
                    }
                    if (i == 13) {
                        return new LatitudeFilter(comparator, str);
                    }
                    if (i == 16) {
                        return new LongitudeRefFilter(comparator, str);
                    }
                    if (i == 14) {
                        return new LatitudeRefFilter(comparator, str);
                    }
                    if (i == 35) {
                        return new SpecialTypeFlagsFilter(comparator, str);
                    }
                    if (i == -4) {
                        return new BurstFilter(comparator, str, new HashMap());
                    }
                    if (i == -5) {
                        return new BurstFilter(comparator, str);
                    }
                    return Filter.NOT_SUPPORTED_FILTER;
                }
            }
        }

        /* loaded from: classes2.dex */
        public static class SpecialTypeFlagsFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern PATTERN = Pattern.compile("(?<=\\s)(!=|<>|=)(?=\\s|$)");
            public long mValue;
            public Filter.Comparator mValueComparator;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((SpecialTypeFlagsFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public SpecialTypeFlagsFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mValue = 0L;
                if (this.mArgument.contains("!=") || this.mArgument.contains("<>") || this.mArgument.contains("=")) {
                    Matcher matcher = PATTERN.matcher(str);
                    if (matcher.find()) {
                        String group = matcher.group();
                        group.hashCode();
                        char c = 65535;
                        switch (group.hashCode()) {
                            case 61:
                                if (group.equals("=")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 1084:
                                if (group.equals("!=")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 1922:
                                if (group.equals("<>")) {
                                    c = 2;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                            case 1:
                            case 2:
                                this.mValueComparator = TextUtils.equals(group, "=") ? Filter.Comparator.EQUALS : Filter.Comparator.NOT_EQUALS;
                                this.mComparator = Filter.Comparator.BITWISE_AND;
                                this.mValue = Long.parseLong(this.mArgument.substring(matcher.end()).trim());
                                this.mArgument = this.mArgument.substring(0, matcher.start()).trim();
                                return;
                            default:
                                throw new IllegalArgumentException("not support argument:" + str);
                        }
                    }
                    try {
                        Long.parseLong(this.mArgument);
                    } catch (NumberFormatException unused) {
                        this.mArgument = "0";
                        DefaultLogger.e(".provider.cache.MediaItem", "SpecialTypeFlagsFilter IllegalArgumentException %s", str);
                    }
                }
            }

            public T filter(T t) {
                Filter.Comparator comparator = this.mComparator;
                Filter.Comparator comparator2 = Filter.Comparator.EQUALS;
                if (comparator == comparator2 && t.getSpecialTypeFlags() == Long.parseLong(this.mArgument)) {
                    return t;
                }
                Filter.Comparator comparator3 = this.mComparator;
                Filter.Comparator comparator4 = Filter.Comparator.NOT_EQUALS;
                if (comparator3 == comparator4 && t.getSpecialTypeFlags() != 0) {
                    return t;
                }
                if (this.mComparator != Filter.Comparator.BITWISE_AND) {
                    return null;
                }
                if (this.mValueComparator == comparator4 && (t.getSpecialTypeFlags() & Long.parseLong(this.mArgument)) != this.mValue) {
                    return t;
                }
                if (this.mValueComparator != comparator2 || (t.getSpecialTypeFlags() & Long.parseLong(this.mArgument)) != this.mValue) {
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class BurstFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final boolean mGetBurst;
            public final Map<Long, Void> mGroup;
            public final boolean mIsNeedFold;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((BurstFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public BurstFilter(Filter.Comparator comparator, String str) {
                this(comparator, str, null);
            }

            public BurstFilter(Filter.Comparator comparator, String str, Map<Long, Void> map) {
                super(comparator, str);
                boolean z = true;
                this.mGetBurst = TextUtils.equals(this.mArgument, String.valueOf(1));
                this.mGroup = map;
                this.mIsNeedFold = map == null ? false : z;
            }

            public T filter(T t) {
                if (this.mComparator == Filter.Comparator.EQUALS) {
                    long burstGroupKey = t.getBurstGroupKey();
                    if (!this.mGetBurst) {
                        if (burstGroupKey != 0) {
                            return null;
                        }
                        return t;
                    } else if (burstGroupKey == 0) {
                        return null;
                    } else {
                        if (this.mIsNeedFold) {
                            if (this.mGroup.containsKey(Long.valueOf(burstGroupKey))) {
                                return null;
                            }
                            this.mGroup.put(Long.valueOf(burstGroupKey), null);
                        }
                        return t;
                    }
                }
                return t;
            }
        }

        public static /* synthetic */ MediaCacheItem lambda$static$0(MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2, int i) {
            return mediaCacheItem.getAliasSyncState().longValue() < mediaCacheItem2.getAliasSyncState().longValue() ? mediaCacheItem : mediaCacheItem2;
        }

        /* loaded from: classes2.dex */
        public static class DateModifiedComparator extends TimeComparator {
            public DateModifiedComparator(boolean z) {
                super(z);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.provider.cache.MediaCacheItem.QueryFactory.TimeComparator, java.util.Comparator
            public int compare(MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
                int compare = Long.compare(mediaCacheItem.mDateModified, mediaCacheItem2.mDateModified);
                if (compare == 0) {
                    return super.compare(mediaCacheItem, mediaCacheItem2);
                }
                return this.mDescent ? -compare : compare;
            }
        }

        /* loaded from: classes2.dex */
        public static class SortTimeComparator extends TimeComparator {
            public SortTimeComparator(boolean z) {
                super(z);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.provider.cache.MediaCacheItem.QueryFactory.TimeComparator, java.util.Comparator
            public int compare(MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
                int compare = Long.compare(mediaCacheItem.mAliasSortTime, mediaCacheItem2.mAliasSortTime);
                if (compare == 0) {
                    return super.compare(mediaCacheItem, mediaCacheItem2);
                }
                return this.mDescent ? -compare : compare;
            }
        }

        /* loaded from: classes2.dex */
        public static class TimeComparator implements Comparator<MediaCacheItem> {
            public boolean mDescent;

            public TimeComparator(boolean z) {
                this.mDescent = z;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Removed duplicated region for block: B:11:0x0025  */
            /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
            @Override // java.util.Comparator
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public int compare(com.miui.gallery.provider.cache.MediaCacheItem r7, com.miui.gallery.provider.cache.MediaCacheItem r8) {
                /*
                    r6 = this;
                    long r0 = r7.mMixedTime
                    long r2 = r8.mMixedTime
                    int r0 = java.lang.Long.compare(r0, r2)
                    if (r0 != 0) goto L17
                    long r1 = r7.mDateModified
                    long r3 = r8.mDateModified
                    int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                    if (r5 == 0) goto L17
                    int r0 = java.lang.Long.compare(r1, r3)
                    goto L21
                L17:
                    if (r0 != 0) goto L21
                    long r0 = r7.mId
                    long r7 = r8.mId
                    int r0 = java.lang.Long.compare(r0, r7)
                L21:
                    boolean r7 = r6.mDescent
                    if (r7 == 0) goto L26
                    int r0 = -r0
                L26:
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cache.MediaCacheItem.QueryFactory.TimeComparator.compare(com.miui.gallery.provider.cache.MediaCacheItem, com.miui.gallery.provider.cache.MediaCacheItem):int");
            }
        }

        /* loaded from: classes2.dex */
        public static class TitleComparator implements Comparator<MediaCacheItem> {
            public final boolean mDescent;

            public TitleComparator(boolean z) {
                this.mDescent = z;
            }

            @Override // java.util.Comparator
            public int compare(MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
                int i;
                String str;
                String str2 = mediaCacheItem.mTitle;
                if (str2 != null && (str = mediaCacheItem2.mTitle) != null) {
                    i = str2.compareTo(str);
                } else if (str2 != null) {
                    i = 1;
                } else if (mediaCacheItem2.mTitle == null) {
                    return 0;
                } else {
                    i = -1;
                }
                return this.mDescent ? -i : i;
            }
        }

        /* loaded from: classes2.dex */
        public static class SizeComparator implements Comparator<MediaCacheItem> {
            public final boolean mDescent;

            public SizeComparator(boolean z) {
                this.mDescent = z;
            }

            @Override // java.util.Comparator
            public int compare(MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
                int compare = Long.compare(mediaCacheItem.getSize(), mediaCacheItem2.getSize());
                return this.mDescent ? -compare : compare;
            }
        }

        /* loaded from: classes2.dex */
        public static class AliasClearThumbnailFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern DELIMITER_PATTERN = Pattern.compile("'\\s*,\\s*'");
            public String mAliasClearThumbnail;
            public Set<String> mFileSet;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((AliasClearThumbnailFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public AliasClearThumbnailFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mAliasClearThumbnail = str;
                } else if (comparator != Filter.Comparator.IN && comparator != Filter.Comparator.NOT_IN) {
                } else {
                    StringBuilder sb = new StringBuilder(str);
                    int length = sb.length();
                    if (length > 0 && sb.charAt(0) == '(') {
                        sb.deleteCharAt(0);
                        while (true) {
                            length--;
                            if (length <= 0 || sb.charAt(0) != ' ') {
                                break;
                            }
                            sb.deleteCharAt(0);
                        }
                        if (length > 0 && sb.charAt(0) == '\'') {
                            sb.deleteCharAt(0);
                            length--;
                        }
                        if (length > 0) {
                            int i = length - 1;
                            if (sb.charAt(i) == ')') {
                                sb.deleteCharAt(i);
                                while (true) {
                                    length--;
                                    if (length <= 0) {
                                        break;
                                    }
                                    int i2 = length - 1;
                                    if (sb.charAt(i2) != ' ') {
                                        break;
                                    }
                                    sb.deleteCharAt(i2);
                                }
                                if (length > 0) {
                                    int i3 = length - 1;
                                    if (sb.charAt(i3) == '\'') {
                                        sb.deleteCharAt(i3);
                                        length--;
                                    }
                                }
                                HashSet hashSet = new HashSet();
                                this.mFileSet = hashSet;
                                if (length <= 0) {
                                    return;
                                }
                                Collections.addAll(hashSet, TextUtils.split(sb.toString(), DELIMITER_PATTERN));
                                return;
                            }
                        }
                        throw new IllegalArgumentException("argument must end with ')'");
                    }
                    throw new IllegalArgumentException("argument must start with '('");
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.getAliasClearThumbnail(), this.mAliasClearThumbnail)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && !TextUtils.isEmpty(t.getAliasClearThumbnail())) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !TextUtils.equals(t.getAliasClearThumbnail(), this.mAliasClearThumbnail)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.IN && this.mFileSet.contains(t.getAliasClearThumbnail())) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mFileSet.contains(t.getAliasClearThumbnail())) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class ValidCoverPathFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public String mCoverPath;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((ValidCoverPathFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public ValidCoverPathFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS) {
                    this.mCoverPath = str;
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.NOT_NULL || TextUtils.isEmpty(t.getAliasClearThumbnail())) {
                    if (this.mComparator == Filter.Comparator.IS_NULL && TextUtils.isEmpty(t.getAliasClearThumbnail())) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.EQUALS && Objects.equals(t.getAliasClearThumbnail(), this.mCoverPath)) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class OriginFileCheckFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((OriginFileCheckFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public OriginFileCheckFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
            }

            public T filter(T t) {
                boolean isValidOriginFile = t.isValidOriginFile();
                if (!t.isImageItem() || (!isValidOriginFile && !t.isValidThumbnailFile())) {
                    if (t.isVideoItem() && isValidOriginFile) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class BurstIndexComparator<T extends MediaCacheItem> implements Comparator<T> {
            public final boolean mDescent;

            public BurstIndexComparator(boolean z) {
                this.mDescent = z;
            }

            @Override // java.util.Comparator
            public int compare(T t, T t2) {
                int compare = Long.compare(t.mBurstIndex, t2.mBurstIndex);
                return this.mDescent ? -compare : compare;
            }
        }

        /* loaded from: classes2.dex */
        public static class FavoriteItemFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((FavoriteItemFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public FavoriteItemFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
            }

            public T filter(T t) {
                if (t.getAliasIsFavorite().equals(CacheItem.TRUE)) {
                    return t;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class AlbumAttributesFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final long mAlbumAttributes;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((AlbumAttributesFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public AlbumAttributesFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mAlbumAttributes = Long.parseLong(this.mArgument);
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || (t.getAliasAlbumAttributes().longValue() & this.mAlbumAttributes) == 0) {
                    if (this.mComparator != Filter.Comparator.NOT_EQUALS || (t.getAliasAlbumAttributes().longValue() & this.mAlbumAttributes) != 0) {
                        return null;
                    }
                    return t;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class TitleFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((TitleFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public TitleFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.LIKE || !containsIgnoreCase(t.mTitle, this.mArgument)) {
                    return null;
                }
                return t;
            }

            public static boolean containsIgnoreCase(String str, String str2) {
                return !TextUtils.isEmpty(str) && str.toLowerCase().contains(handleKeyword(str2));
            }

            public static String handleKeyword(String str) {
                String trim = str.trim();
                if (trim.startsWith("%") || trim.endsWith("%")) {
                    return trim.replace("%", "").toLowerCase();
                }
                return trim.toLowerCase();
            }
        }

        /* loaded from: classes2.dex */
        public static class IdFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern ID_PATTERN = Pattern.compile("\\d+");
            public long mId;
            public Set<Long> mIdSet;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((IdFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public IdFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS) {
                    this.mId = Long.parseLong(str);
                } else if (comparator != Filter.Comparator.IN) {
                } else {
                    this.mIdSet = new ArraySet();
                    Matcher matcher = ID_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mIdSet.add(Long.valueOf(Long.parseLong(matcher.group())));
                    }
                }
            }

            public T filter(T t) {
                Filter.Comparator comparator = this.mComparator;
                if (comparator == Filter.Comparator.EQUALS && t.mId == this.mId) {
                    return t;
                }
                if (comparator == Filter.Comparator.IN && this.mIdSet.contains(Long.valueOf(t.mId))) {
                    return t;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class AlbumFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern DELIMITER_PATTERN = Pattern.compile("\\s*,\\s*");
            public long mAlbumId;
            public Set<String> mAlbumIds;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((AlbumFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public AlbumFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mAlbumId = Long.parseLong(str);
                } else if (comparator != Filter.Comparator.IN && comparator != Filter.Comparator.NOT_IN) {
                } else {
                    StringBuilder sb = new StringBuilder(str);
                    int length = sb.length();
                    if (length > 0 && sb.charAt(0) == '(') {
                        sb.deleteCharAt(0);
                        while (true) {
                            length--;
                            if (length <= 0 || sb.charAt(0) != ' ') {
                                break;
                            }
                            sb.deleteCharAt(0);
                        }
                        if (length > 0 && sb.charAt(0) == '\'') {
                            sb.deleteCharAt(0);
                            length--;
                        }
                        if (length > 0) {
                            int i = length - 1;
                            if (sb.charAt(i) == ')') {
                                sb.deleteCharAt(i);
                                while (true) {
                                    length--;
                                    if (length <= 0) {
                                        break;
                                    }
                                    int i2 = length - 1;
                                    if (sb.charAt(i2) != ' ') {
                                        break;
                                    }
                                    sb.deleteCharAt(i2);
                                }
                                if (length > 0) {
                                    int i3 = length - 1;
                                    if (sb.charAt(i3) == '\'') {
                                        sb.deleteCharAt(i3);
                                        length--;
                                    }
                                }
                                HashSet hashSet = new HashSet();
                                this.mAlbumIds = hashSet;
                                if (length <= 0) {
                                    return;
                                }
                                Collections.addAll(hashSet, TextUtils.split(sb.toString(), DELIMITER_PATTERN));
                                return;
                            }
                        }
                        throw new IllegalArgumentException("argument must end with ')'");
                    }
                    throw new IllegalArgumentException("argument must start with '('");
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !Numbers.equals(t.getAlbumId(), this.mAlbumId)) {
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !Numbers.equals(t.getAlbumId(), this.mAlbumId)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.IN && t.getAlbumId() != null && this.mAlbumIds.contains(String.valueOf(t.mAlbumId))) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && t.getAlbumId() != null && !this.mAlbumIds.contains(String.valueOf(t.mAlbumId))) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class TypeFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public int mType;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((TypeFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public TypeFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mType = Integer.parseInt(str);
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !Numbers.equals(t.mType, this.mType)) {
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class LocalFlagFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern ID_PATTERN = Pattern.compile("\\d+");
            public Set<Long> mFlagSet;
            public int mLocalFlag;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((LocalFlagFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public LocalFlagFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.IN || comparator == Filter.Comparator.NOT_IN) {
                    this.mFlagSet = new ArraySet();
                    Matcher matcher = ID_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mFlagSet.add(Long.valueOf(Long.parseLong(matcher.group())));
                    }
                } else if (comparator == Filter.Comparator.IS_NULL) {
                } else {
                    this.mLocalFlag = Integer.parseInt(str);
                }
            }

            public T filter(T t) {
                Long l;
                Long l2;
                if (this.mComparator != Filter.Comparator.EQUALS || !Numbers.equals(t.mLocalFlag, this.mLocalFlag)) {
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !Numbers.equals(t.mLocalFlag, this.mLocalFlag)) {
                        return t;
                    }
                    Filter.Comparator comparator = this.mComparator;
                    if (comparator == Filter.Comparator.IS_NULL && t.mLocalFlag == null) {
                        return t;
                    }
                    if (comparator == Filter.Comparator.IN && (l2 = t.mLocalFlag) != null && this.mFlagSet.contains(l2)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && (l = t.mLocalFlag) != null && !this.mFlagSet.contains(l)) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class ThumbnailFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public String mThumbnail;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((ThumbnailFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public ThumbnailFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mThumbnail = str;
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mThumbnail, this.mThumbnail)) {
                    Filter.Comparator comparator = this.mComparator;
                    if (comparator == Filter.Comparator.NOT_NULL && t.mThumbnail != null) {
                        return t;
                    }
                    if (comparator == Filter.Comparator.NOT_EQUALS && !TextUtils.equals(t.mThumbnail, this.mThumbnail)) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class LocalFileFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern DELIMITER_PATTERN = Pattern.compile("'\\s*,\\s*'");
            public String mLocalFile;
            public Set<String> mLocalFileSet;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((LocalFileFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public LocalFileFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS || comparator == Filter.Comparator.NOT_EQUALS) {
                    this.mLocalFile = str;
                } else if (comparator != Filter.Comparator.IN && comparator != Filter.Comparator.NOT_IN) {
                } else {
                    StringBuilder sb = new StringBuilder(str);
                    int length = sb.length();
                    if (length > 0 && sb.charAt(0) == '(') {
                        sb.deleteCharAt(0);
                        while (true) {
                            length--;
                            if (length <= 0 || sb.charAt(0) != ' ') {
                                break;
                            }
                            sb.deleteCharAt(0);
                        }
                        if (length > 0 && sb.charAt(0) == '\'') {
                            sb.deleteCharAt(0);
                            length--;
                        }
                        if (length > 0) {
                            int i = length - 1;
                            if (sb.charAt(i) == ')') {
                                sb.deleteCharAt(i);
                                while (true) {
                                    length--;
                                    if (length <= 0) {
                                        break;
                                    }
                                    int i2 = length - 1;
                                    if (sb.charAt(i2) != ' ') {
                                        break;
                                    }
                                    sb.deleteCharAt(i2);
                                }
                                if (length > 0) {
                                    int i3 = length - 1;
                                    if (sb.charAt(i3) == '\'') {
                                        sb.deleteCharAt(i3);
                                        length--;
                                    }
                                }
                                HashSet hashSet = new HashSet();
                                this.mLocalFileSet = hashSet;
                                if (length <= 0) {
                                    return;
                                }
                                Collections.addAll(hashSet, TextUtils.split(sb.toString(), DELIMITER_PATTERN));
                                return;
                            }
                        }
                        throw new IllegalArgumentException("argument must end with ')'");
                    }
                    throw new IllegalArgumentException("argument must start with '('");
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mFilePath, this.mLocalFile)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && !TextUtils.isEmpty(t.mFilePath)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !TextUtils.equals(t.mFilePath, this.mLocalFile)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.IN && this.mLocalFileSet.contains(t.mFilePath)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mLocalFileSet.contains(t.mFilePath)) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class Sha1Filter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern SHA1_PATTERN = Pattern.compile("[0-9a-fA-F]+");
            public String mSha1;
            public Set<String> mSha1Set;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((Sha1Filter<T>) ((MediaCacheItem) cacheItem));
            }

            public Sha1Filter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS) {
                    this.mSha1 = str;
                } else if (comparator != Filter.Comparator.IN) {
                } else {
                    this.mSha1Set = new ArraySet();
                    Matcher matcher = SHA1_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mSha1Set.add(matcher.group());
                    }
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mSha1, this.mSha1)) {
                    if (this.mComparator == Filter.Comparator.IN && this.mSha1Set.contains(t.mSha1)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_NULL && t.mSha1 != null) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class SyncStateFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final int mSyncState;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((SyncStateFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public SyncStateFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mSyncState = Integer.parseInt(str);
            }

            public T filter(T t) {
                if (this.mComparator == Filter.Comparator.EQUALS && this.mSyncState == t.getAliasSyncState().longValue()) {
                    return t;
                }
                if (this.mComparator == Filter.Comparator.NOT_EQUALS && this.mSyncState != t.getAliasSyncState().longValue()) {
                    return t;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class ServerTagFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final long mServerTag;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((ServerTagFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public ServerTagFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mServerTag = Long.parseLong(str);
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.GREATER_OR_EQUAL || Numbers.compare(Long.valueOf(this.mServerTag), t.mServerTag) > 0) {
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class ServerStatusFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern ID_PATTERN = Pattern.compile("[0-9]+");
            public String mServerStatus;
            public Set<String> mStatusSet;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((ServerStatusFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public ServerStatusFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS) {
                    this.mServerStatus = str;
                } else if (comparator != Filter.Comparator.IN) {
                } else {
                    this.mStatusSet = new HashSet();
                    Matcher matcher = ID_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mStatusSet.add(matcher.group());
                    }
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mServerStatus, this.mServerStatus)) {
                    if (this.mComparator == Filter.Comparator.IN && this.mStatusSet.contains(t.mServerStatus)) {
                        return t;
                    }
                    Filter.Comparator comparator = this.mComparator;
                    if (comparator == Filter.Comparator.NOT_NULL && t.mServerStatus != null) {
                        return t;
                    }
                    if (comparator != Filter.Comparator.IS_NULL || t.mServerStatus != null) {
                        return null;
                    }
                    return t;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class ServerIdFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern ID_PATTERN = Pattern.compile("[0-9]+");
            public Set<String> mIdSet;
            public String mServerId;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((ServerIdFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public ServerIdFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.EQUALS) {
                    this.mServerId = str;
                } else if (comparator != Filter.Comparator.IN) {
                } else {
                    this.mIdSet = new HashSet();
                    Matcher matcher = ID_PATTERN.matcher(str);
                    while (matcher.find()) {
                        this.mIdSet.add(matcher.group());
                    }
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mServerId, this.mServerId)) {
                    if (this.mComparator == Filter.Comparator.IN && this.mIdSet.contains(t.mServerId)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_NULL && t.mServerId != null) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class AliasCreateDateFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern ID_PATTERN = Pattern.compile("\\d+");
            public int mAliasCreateDate;
            public Set<Integer> mCreateDateSet;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((AliasCreateDateFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public AliasCreateDateFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator != Filter.Comparator.IN) {
                    this.mAliasCreateDate = Integer.parseInt(str);
                    return;
                }
                this.mCreateDateSet = new HashSet();
                Matcher matcher = ID_PATTERN.matcher(str);
                while (matcher.find()) {
                    this.mCreateDateSet.add(Integer.valueOf(Integer.parseInt(matcher.group())));
                }
            }

            public T filter(T t) {
                Filter.Comparator comparator = this.mComparator;
                if (comparator == Filter.Comparator.EQUALS && t.mAliasCreateDate == this.mAliasCreateDate) {
                    return t;
                }
                if (comparator == Filter.Comparator.IN && this.mCreateDateSet.contains(Integer.valueOf(t.mAliasCreateDate))) {
                    return t;
                }
                Filter.Comparator comparator2 = this.mComparator;
                if (comparator2 == Filter.Comparator.GREATER && t.mAliasCreateDate > this.mAliasCreateDate) {
                    return t;
                }
                if (comparator2 == Filter.Comparator.GREATER_OR_EQUAL && t.mAliasCreateDate >= this.mAliasCreateDate) {
                    return t;
                }
                if (comparator2 == Filter.Comparator.LESS && t.mAliasCreateDate < this.mAliasCreateDate) {
                    return t;
                }
                if (comparator2 == Filter.Comparator.LESS_OR_EQUAL && t.mAliasCreateDate <= this.mAliasCreateDate) {
                    return t;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class MimeTypeFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public static final Pattern MIMETYPE_PATTERN = Pattern.compile("\\(|\\)|\\'");
            public Set<String> mMimeTypeSets;
            public String mMineType;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((MimeTypeFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public MimeTypeFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                if (comparator == Filter.Comparator.NOT_IN || comparator == Filter.Comparator.IN) {
                    this.mMimeTypeSets = new HashSet();
                    String[] split = MIMETYPE_PATTERN.matcher(str).replaceAll("").split(",");
                    for (int i = 0; split != null && i < split.length; i++) {
                        this.mMimeTypeSets.add(split[i]);
                    }
                } else if (comparator == Filter.Comparator.LIKE) {
                    this.mMineType = str.substring(0, str.indexOf(h.g));
                } else {
                    this.mMineType = str;
                }
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !StringUtils.equalsIgnoreCase(t.mMimeType, this.mMineType)) {
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !StringUtils.equalsIgnoreCase(t.mMimeType, this.mMineType)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.NOT_IN && !this.mMimeTypeSets.contains(t.mMimeType)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.IN && this.mMimeTypeSets.contains(t.mMimeType)) {
                        return t;
                    }
                    if (this.mComparator == Filter.Comparator.LIKE) {
                        String str = t.mMimeType;
                        if (str == null) {
                            DefaultLogger.e(".provider.cache.MediaItem", "item mimeType is null,why?,item is:[%s]", t.toString());
                            return null;
                        } else if (str.startsWith(this.mMineType)) {
                            return t;
                        }
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class AlbumServerIdFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final String mAlbumServerId;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((AlbumServerIdFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public AlbumServerIdFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mAlbumServerId = str;
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mAlbumServerId, this.mAlbumServerId)) {
                    if (this.mComparator == Filter.Comparator.NOT_EQUALS && !TextUtils.equals(t.mAlbumServerId, this.mAlbumServerId)) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class LocationFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final String mLocation;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((LocationFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public LocationFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mLocation = str;
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mLocation, this.mLocation)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && t.mLocation != null) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class LongitudeFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final String mLongitude;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((LongitudeFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public LongitudeFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mLongitude = str;
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mLongitude, this.mLongitude)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && t.mLongitude != null) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class LatitudeFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final String mLatitude;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((LatitudeFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public LatitudeFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mLatitude = str;
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mLatitude, this.mLatitude)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && t.mLatitude != null) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class LatitudeRefFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final String mLatitudeRef;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((LatitudeRefFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public LatitudeRefFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mLatitudeRef = str;
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mLatitudeRef.toString(), this.mLatitudeRef)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && t.mLatitudeRef != null) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class LongitudeRefFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final String mLongitudeRef;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((LongitudeRefFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public LongitudeRefFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mLongitudeRef = str;
            }

            public T filter(T t) {
                if (this.mComparator != Filter.Comparator.EQUALS || !TextUtils.equals(t.mLongitude.toString(), this.mLongitudeRef)) {
                    if (this.mComparator == Filter.Comparator.NOT_NULL && t.mLongitudeRef != null) {
                        return t;
                    }
                    return null;
                }
                return t;
            }
        }

        /* loaded from: classes2.dex */
        public static class SizeFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final long mSize;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((SizeFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public SizeFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mSize = Long.parseLong(str);
            }

            public T filter(T t) {
                if (this.mComparator == Filter.Comparator.EQUALS && t.getSize() == this.mSize) {
                    return t;
                }
                if (this.mComparator == Filter.Comparator.GREATER && t.getSize() > this.mSize) {
                    return t;
                }
                if (this.mComparator == Filter.Comparator.GREATER_OR_EQUAL && t.getSize() >= this.mSize) {
                    return t;
                }
                if (this.mComparator == Filter.Comparator.LESS && t.getSize() < this.mSize) {
                    return t;
                }
                if (this.mComparator == Filter.Comparator.LESS_OR_EQUAL && t.getSize() <= this.mSize) {
                    return t;
                }
                return null;
            }
        }

        /* loaded from: classes2.dex */
        public static class AliasModifyDateFilter<T extends MediaCacheItem> extends Filter.CompareFilter<T> {
            public final int mModifyDate;

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.miui.gallery.provider.cache.Filter
            public /* bridge */ /* synthetic */ CacheItem filter(CacheItem cacheItem) {
                return filter((AliasModifyDateFilter<T>) ((MediaCacheItem) cacheItem));
            }

            public AliasModifyDateFilter(Filter.Comparator comparator, String str) {
                super(comparator, str);
                this.mModifyDate = Integer.parseInt(str);
            }

            public T filter(T t) {
                Filter.Comparator comparator = this.mComparator;
                if (comparator == Filter.Comparator.EQUALS && t.mAliasModifyDate == this.mModifyDate) {
                    return t;
                }
                if (comparator == Filter.Comparator.GREATER && t.mAliasModifyDate > this.mModifyDate) {
                    return t;
                }
                if (comparator == Filter.Comparator.GREATER_OR_EQUAL && t.mAliasModifyDate >= this.mModifyDate) {
                    return t;
                }
                if (comparator == Filter.Comparator.LESS && t.mAliasModifyDate < this.mModifyDate) {
                    return t;
                }
                if (comparator == Filter.Comparator.LESS_OR_EQUAL && t.mAliasModifyDate <= this.mModifyDate) {
                    return t;
                }
                return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Generator<T extends MediaCacheItem> implements CacheItem.Generator<T> {
        public static final Map<String, Integer> COLUMN_MAP;
        public static final CacheItem.ColumnMapper COLUMN_MAPPER = new CacheItem.ColumnMapper() { // from class: com.miui.gallery.provider.cache.MediaCacheItem.Generator.1
            @Override // com.miui.gallery.provider.cache.CacheItem.ColumnMapper
            public int getIndex(String str) {
                Integer num = (Integer) Generator.COLUMN_MAP.get(str);
                if (num == null) {
                    DefaultLogger.w(".provider.cache.MediaItem", "column '%s' not found", str);
                    return -1;
                }
                return num.intValue();
            }
        };
        public static final String[] PROJECTION;
        public final FavoritesDelegate mFavoritesDelegate;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        public /* bridge */ /* synthetic */ void update(CacheItem cacheItem, ContentValues contentValues) {
            update((Generator<T>) ((MediaCacheItem) cacheItem), contentValues);
        }

        public Generator(FavoritesDelegate favoritesDelegate) {
            this.mFavoritesDelegate = favoritesDelegate;
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from  reason: collision with other method in class */
        public T mo1226from(Cursor cursor) {
            T genNewItem = genNewItem();
            initFromCursor(cursor, genNewItem);
            return genNewItem;
        }

        public T genNewItem() {
            return (T) new MediaCacheItem();
        }

        public static long truncateTimeMillis(long j) {
            return Math.min(j, 253402185599000L);
        }

        public void initFromCursor(Cursor cursor, T t) {
            t.mId = cursor.getLong(0);
            t.mSha1 = ParseUtils.getString(cursor, 1);
            t.mAlbumId = ParseUtils.getLong(cursor, 11);
            t.mMicroThumb = ParseUtils.getString(cursor, 2);
            t.mThumbnail = ParseUtils.getString(cursor, 3);
            t.mFilePath = ParseUtils.getString(cursor, 4);
            t.mType = ParseUtils.getInt(cursor, 5);
            t.mTitle = ParseUtils.getString(cursor, 6);
            t.mDuration = ParseUtils.getLong(cursor, 7);
            t.mDescription = ParseUtils.getString(cursor, 8);
            t.mLocation = ParseUtils.getString(cursor, 9);
            t.mSize = Long.valueOf(cursor.getLong(10));
            t.mMimeType = ParseUtils.getString(cursor, 12);
            t.mLatitude = ParseUtils.getString(cursor, 13);
            t.mLatitudeRef = ParseUtils.getChar(cursor, 14);
            t.mLongitude = ParseUtils.getString(cursor, 15);
            t.mLongitudeRef = ParseUtils.getChar(cursor, 16);
            t.mSecretKey = ParseUtils.getBlob(cursor, 17);
            t.mLocalFlag = ParseUtils.getLong(cursor, 18);
            t.mWidth = ParseUtils.getInt(cursor, 20);
            t.mHeight = ParseUtils.getInt(cursor, 21);
            t.mServerStatus = ParseUtils.getString(cursor, 22);
            t.mMixedTime = truncateTimeMillis(cursor.getLong(19));
            t.mDateModified = truncateTimeMillis(cursor.getLong(23));
            t.mAliasCreateDate = GalleryDateUtils.format(t.mMixedTime);
            t.mCreatorId = cursor.getString(24);
            t.mServerTag = Long.valueOf(cursor.getLong(25));
            t.mServerId = cursor.getString(26);
            t.mAliasModifyDate = GalleryDateUtils.format(t.mDateModified);
            FavoritesDelegate favoritesDelegate = this.mFavoritesDelegate;
            t.mFavoritesDelegate = favoritesDelegate;
            t.mIsFavorite = favoritesDelegate.isFavorite(Long.valueOf(t.mId));
            t.mAlbumServerId = ParseUtils.getString(cursor, 27);
            t.mSpecialTypeFlags = ParseUtils.getLong(cursor, 28);
            t.regenerateSortTimeAndDate();
            t.mOrientation = ParseUtils.getInt(cursor, 29);
            BurstInfo generateBurstInfo = BurstInfo.generateBurstInfo(t.mAlbumId, t.mTitle, t.mMimeType);
            if (generateBurstInfo == null) {
                Integer num = CacheItem.DEFAULT_INT;
                t.mBurstGroupKey = num.intValue();
                t.mIsTimeBurst = CacheItem.FALSE.longValue();
                t.mBurstIndex = num.intValue();
            } else {
                t.mBurstGroupKey = generateBurstInfo.mGroupKey;
                t.mIsTimeBurst = (generateBurstInfo.mIsTimeBurst ? CacheItem.TRUE : CacheItem.FALSE).longValue();
                t.mBurstIndex = generateBurstInfo.mBurstIndex;
            }
            if (t.mAlbumId != null) {
                t.mAlbumAttributes = Long.valueOf(AlbumCacheManager.getInstance().getAttributes(t.mAlbumId.longValue()));
            } else {
                DefaultLogger.w(".provider.cache.MediaItem", "localGroupId is null: %s", Long.valueOf(t.getId()));
                t.mAlbumAttributes = 0L;
            }
            if (t.mType == null) {
                DefaultLogger.w(".provider.cache.MediaItem", "initFromCursor - serverType shouldn't be null: %s", Long.valueOf(t.getId()));
                if (BaseFileMimeUtil.isVideoFromMimeType(t.mMimeType)) {
                    t.mType = 2;
                } else {
                    t.mType = 1;
                }
            }
            if (cursor.getColumnIndex("source_pkg") >= 0) {
                t.mSourcePkg = ParseUtils.getString(cursor, 30);
            }
            t.mAliasSyncState = t.calAliasSyncState();
        }

        public void initFromContentValues(long j, ContentValues contentValues, T t) {
            t.mId = j;
            String[] strArr = PROJECTION;
            t.mSha1 = contentValues.getAsString(strArr[1]);
            t.mAlbumId = contentValues.getAsLong(strArr[11]);
            t.mMicroThumb = contentValues.getAsString(strArr[2]);
            t.mThumbnail = contentValues.getAsString(strArr[3]);
            t.mFilePath = contentValues.getAsString(strArr[4]);
            t.mType = contentValues.getAsInteger(strArr[5]);
            t.mTitle = contentValues.getAsString(strArr[6]);
            t.mDuration = contentValues.getAsLong(strArr[7]);
            t.mDescription = contentValues.getAsString(strArr[8]);
            t.mLocation = contentValues.getAsString(strArr[9]);
            Long asLong = contentValues.getAsLong(strArr[10]);
            t.mSize = Long.valueOf(asLong == null ? 0L : asLong.longValue());
            t.mMimeType = contentValues.getAsString(strArr[12]);
            t.mLatitude = contentValues.getAsString(strArr[13]);
            String asString = contentValues.getAsString(strArr[14]);
            Character ch2 = null;
            t.mLatitudeRef = TextUtils.isEmpty(asString) ? null : Character.valueOf(asString.charAt(0));
            t.mLongitude = contentValues.getAsString(strArr[15]);
            String asString2 = contentValues.getAsString(strArr[16]);
            if (!TextUtils.isEmpty(asString2)) {
                ch2 = Character.valueOf(asString2.charAt(0));
            }
            t.mLongitudeRef = ch2;
            t.mSecretKey = contentValues.getAsByteArray(strArr[17]);
            t.mLocalFlag = contentValues.getAsLong(strArr[18]);
            t.mWidth = contentValues.getAsInteger(strArr[20]);
            t.mHeight = contentValues.getAsInteger(strArr[21]);
            t.mServerStatus = contentValues.getAsString(strArr[22]);
            Long asLong2 = contentValues.getAsLong(strArr[19]);
            t.mMixedTime = asLong2 == null ? 0L : truncateTimeMillis(asLong2.longValue());
            Long asLong3 = contentValues.getAsLong(strArr[23]);
            t.mDateModified = asLong3 == null ? 0L : truncateTimeMillis(asLong3.longValue());
            t.mAliasCreateDate = GalleryDateUtils.format(t.mMixedTime);
            t.mCreatorId = contentValues.getAsString(strArr[24]);
            t.mServerTag = contentValues.getAsLong(strArr[25]);
            t.mServerId = contentValues.getAsString(strArr[26]);
            t.mAliasModifyDate = GalleryDateUtils.format(t.mDateModified);
            FavoritesDelegate favoritesDelegate = this.mFavoritesDelegate;
            t.mFavoritesDelegate = favoritesDelegate;
            t.mIsFavorite = favoritesDelegate.isFavorite(Long.valueOf(t.mId));
            t.mAlbumServerId = contentValues.getAsString(strArr[27]);
            t.mSpecialTypeFlags = contentValues.getAsLong(strArr[28]);
            t.regenerateSortTimeAndDate();
            t.mOrientation = contentValues.getAsInteger(strArr[29]);
            t.mSourcePkg = contentValues.getAsString(strArr[30]);
            BurstInfo generateBurstInfo = BurstInfo.generateBurstInfo(t.getAlbumId(), t.mTitle, t.mMimeType);
            if (generateBurstInfo == null) {
                Integer num = CacheItem.DEFAULT_INT;
                t.mBurstGroupKey = num.intValue();
                t.mIsTimeBurst = CacheItem.FALSE.longValue();
                t.mBurstIndex = num.intValue();
            } else {
                t.mBurstGroupKey = generateBurstInfo.mGroupKey;
                t.mIsTimeBurst = (generateBurstInfo.mIsTimeBurst ? CacheItem.TRUE : CacheItem.FALSE).longValue();
                t.mBurstIndex = generateBurstInfo.mBurstIndex;
            }
            if (t.getAlbumId() != null) {
                t.mAlbumAttributes = Long.valueOf(AlbumCacheManager.getInstance().getAttributes(t.getAlbumId().longValue()));
            } else {
                DefaultLogger.e(".provider.cache.MediaItem", "mediaCacheItem localGroupId is null,item info:%s", t);
                t.mAlbumAttributes = 0L;
            }
            if (t.mType == null) {
                DefaultLogger.e(".provider.cache.MediaItem", "initFromContentValues - serverType shouldn't be null: %s", t);
                if (BaseFileMimeUtil.isVideoFromMimeType(t.mMimeType)) {
                    t.mType = 2;
                } else {
                    t.mType = 1;
                }
            }
            t.mAliasSyncState = t.calAliasSyncState();
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        /* renamed from: from  reason: collision with other method in class */
        public T mo1225from(long j, ContentValues contentValues) {
            T genNewItem = genNewItem();
            initFromContentValues(j, contentValues, genNewItem);
            return genNewItem;
        }

        /* JADX WARN: Removed duplicated region for block: B:101:0x024e  */
        /* JADX WARN: Removed duplicated region for block: B:104:0x0260  */
        /* JADX WARN: Removed duplicated region for block: B:110:0x027f  */
        /* JADX WARN: Removed duplicated region for block: B:113:0x0293  */
        /* JADX WARN: Removed duplicated region for block: B:116:0x02a7  */
        /* JADX WARN: Removed duplicated region for block: B:119:0x02bb  */
        /* JADX WARN: Removed duplicated region for block: B:124:0x02d3  */
        /* JADX WARN: Removed duplicated region for block: B:127:0x02de  */
        /* JADX WARN: Removed duplicated region for block: B:130:0x02f2  */
        /* JADX WARN: Removed duplicated region for block: B:133:0x0306  */
        /* JADX WARN: Removed duplicated region for block: B:136:0x031a  */
        /* JADX WARN: Removed duplicated region for block: B:139:0x032e  */
        /* JADX WARN: Removed duplicated region for block: B:141:0x033a  */
        /* JADX WARN: Removed duplicated region for block: B:144:0x0347  */
        /* JADX WARN: Removed duplicated region for block: B:147:0x0359  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x006f  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0080  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0091  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x00a2  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00bb  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0109  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x011b  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x016a  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x017c  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x019a  */
        /* JADX WARN: Removed duplicated region for block: B:66:0x01ac  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x01bf  */
        /* JADX WARN: Removed duplicated region for block: B:79:0x01e4  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x01f6  */
        /* JADX WARN: Removed duplicated region for block: B:91:0x0219  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x023a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void update(T r11, android.content.ContentValues r12) {
            /*
                Method dump skipped, instructions count: 878
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cache.MediaCacheItem.Generator.update(com.miui.gallery.provider.cache.MediaCacheItem, android.content.ContentValues):void");
        }

        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        public CacheItem.ColumnMapper getMapper() {
            return COLUMN_MAPPER;
        }

        @Override // com.miui.gallery.provider.cache.Filter.FilterFactory
        public Filter.CompareFilter<T> getFilter(int i, Filter.Comparator comparator, String str) {
            if (i != 0) {
                if (i == 1) {
                    return new QueryFactory.Sha1Filter(comparator, str);
                }
                if (i == 11) {
                    return new QueryFactory.AlbumFilter(comparator, str);
                }
                return Filter.NOT_SUPPORTED_FILTER;
            }
            return new QueryFactory.IdFilter(comparator, str);
        }

        @Override // com.miui.gallery.provider.cache.CacheItem.Generator
        public String[] getProjection() {
            return PROJECTION;
        }

        static {
            String[] strArr = {j.c, "sha1", "microthumbfile", "thumbnailFile", "localFile", "serverType", "title", "duration", "description", "location", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "localGroupId", "mimeType", "exifGPSLatitude", "exifGPSLatitudeRef", "exifGPSLongitude", "exifGPSLongitudeRef", "secretKey", "localFlag", "mixedDateTime", "exifImageWidth", "exifImageLength", "serverStatus", "dateModified", "creatorId", "serverTag", "serverId", "groupId", "specialTypeFlags", "exifOrientation", "source_pkg"};
            PROJECTION = strArr;
            ArrayMap arrayMap = new ArrayMap(strArr.length);
            COLUMN_MAP = arrayMap;
            arrayMap.put(j.c, 0);
            arrayMap.put("sha1", 1);
            arrayMap.put("microthumbfile", 2);
            arrayMap.put("thumbnailFile", 3);
            arrayMap.put("localFile", 4);
            arrayMap.put("serverType", 5);
            arrayMap.put("title", 6);
            arrayMap.put("duration", 7);
            arrayMap.put("description", 8);
            arrayMap.put("location", 9);
            arrayMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, 10);
            arrayMap.put("localGroupId", 11);
            arrayMap.put("mimeType", 12);
            arrayMap.put("exifGPSLatitude", 13);
            arrayMap.put("exifGPSLatitudeRef", 14);
            arrayMap.put("exifGPSLongitude", 15);
            arrayMap.put("exifGPSLongitudeRef", 16);
            arrayMap.put("secretKey", 17);
            arrayMap.put("localFlag", 18);
            arrayMap.put("mixedDateTime", 19);
            arrayMap.put("exifImageWidth", 20);
            arrayMap.put("exifImageLength", 21);
            arrayMap.put("serverStatus", 22);
            arrayMap.put("dateModified", 23);
            arrayMap.put("creatorId", 24);
            arrayMap.put("serverTag", 25);
            arrayMap.put("serverId", 26);
            arrayMap.put("groupId", 27);
            arrayMap.put("specialTypeFlags", 28);
            arrayMap.put("exifOrientation", 29);
            arrayMap.put("source_pkg", 30);
        }
    }
}
