package com.miui.gallery.model.dto;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.share.ShareAlbumCacheManager;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.ParcelableUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class Album extends Entity implements Parcelable {
    public long mAlbumId;
    public long mAlbumSize;
    public AlbumType mAlbumType;
    public long mAttributes;
    public CoverBean mCover;
    public long mDateModified;
    public long mDateTaken;
    public String mDirectoryPath;
    public String mDisplayedName;
    public ExtraInfo mExtraInfo;
    public String mName;
    public int mPhotoCount;
    public String mServerId;
    public long mSortBy;
    public String mSortInfo;
    public static final Pattern sMiuiVisiblePattern = Pattern.compile("(?:alarms|dcim|dcim/camera|dcim/screenshots|download|movies|music|pictures|podcasts|ringtones|notifications|" + GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH + ")$", 2);
    public static final Pattern sNamePattern = Pattern.compile("(.*)([_][0-9]*$)");
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() { // from class: com.miui.gallery.model.dto.Album.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Album mo1131createFromParcel(Parcel parcel) {
            return new Album(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Album[] mo1132newArray(int i) {
            return new Album[i];
        }
    };

    /* loaded from: classes2.dex */
    public enum AlbumType {
        PINNED,
        SYSTEM,
        BABY,
        CREATIVE,
        USER_CREATE,
        NORMAL,
        OTHERS_SHARE,
        OTHER_ALBUMS
    }

    public static boolean isAIAlbums(long j) {
        return j == 2147483639;
    }

    public static boolean isAllPhotosAlbum(long j) {
        return j == 2147483644 || j == -2147483644;
    }

    public static boolean isFavoritesAlbum(long j) {
        return j == 2147483642 || j == -2147483642;
    }

    public static boolean isHiddenAlbum(long j) {
        return (j & 16) != 0;
    }

    public static boolean isManualRubbishAlbum(long j) {
        return ((2048 & j) == 0 || (j & 4096) == 0) ? false : true;
    }

    public static boolean isOtherAlbum(long j) {
        return (j & 64) != 0;
    }

    public static boolean isOtherAlbums(long j) {
        return j == 2147483641;
    }

    public static boolean isRubbishAlbum(long j) {
        return (j & 2048) != 0;
    }

    public static boolean isScreenshotsRecorders(long j) {
        return j == 2147483645 || j == -2147483645;
    }

    public static boolean isShowedPhotosTabAlbum(long j) {
        return (j & 4) != 0;
    }

    public static boolean isTrashAlbums(long j) {
        return j == 2147483638;
    }

    public static boolean isUserManualMoveToAlbumHome(long j) {
        return (64 & j) == 0 && (j & 128) != 0;
    }

    public static boolean isVideoAlbum(long j) {
        return j == 2147483647L || j == -2147483647L;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Album() {
        this.mSortInfo = "0";
    }

    public Album(long j) {
        this.mSortInfo = "0";
        this.mAlbumId = j;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "album_id", "INTEGER");
        Entity.addColumn(arrayList, "name", "TEXT");
        Entity.addColumn(arrayList, "coverId", "INTEGER");
        Entity.addColumn(arrayList, "coverPath", "TEXT");
        Entity.addColumn(arrayList, "coverSha1", "TEXT");
        Entity.addColumn(arrayList, "coverSyncState", "INTEGER");
        Entity.addColumn(arrayList, "coverSize", "INTEGER");
        Entity.addColumn(arrayList, "serverId", "TEXT");
        Entity.addColumn(arrayList, "attributes", "INTEGER");
        Entity.addColumn(arrayList, "dateTaken", "INTEGER");
        Entity.addColumn(arrayList, "sortBy", "INTEGER");
        Entity.addColumn(arrayList, "localPath", "TEXT");
        Entity.addColumn(arrayList, "sortInfo", "TEXT");
        Entity.addColumn(arrayList, "dateModified", "INTEGER");
        Entity.addColumn(arrayList, "photoCount", "INTEGER");
        Entity.addColumn(arrayList, CallMethod.ARG_EXTRA_STRING, "TEXT");
        Entity.addColumn(arrayList, "is_manual_set_cover", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mAlbumId = Entity.getLong(cursor, "album_id");
        this.mName = Entity.getString(cursor, "name");
        long j = Entity.getLong(cursor, "coverId");
        String string = Entity.getString(cursor, "coverPath");
        String string2 = Entity.getString(cursor, "coverSha1");
        int i = Entity.getInt(cursor, "coverSyncState");
        long j2 = Entity.getLong(cursor, "coverSize");
        boolean z = true;
        if (cursor.getInt(cursor.getColumnIndex("is_manual_set_cover")) != 1) {
            z = false;
        }
        this.mCover = new CoverBean(j, string, string2, i, j2, z);
        this.mPhotoCount = Entity.getInt(cursor, "photoCount");
        this.mExtraInfo = AlbumDataHelper.parseExtraInfo(Entity.getString(cursor, CallMethod.ARG_EXTRA_STRING));
        this.mServerId = Entity.getString(cursor, "serverId");
        this.mAttributes = Entity.getLong(cursor, "attributes");
        this.mDateTaken = Entity.getLong(cursor, "dateTaken");
        this.mSortBy = Entity.getLong(cursor, "sortBy");
        this.mDirectoryPath = Entity.getString(cursor, "localPath");
        this.mSortInfo = Entity.getString(cursor, "sortInfo");
        this.mAlbumType = parseAlbumType(this);
        this.mDateModified = Entity.getLong(cursor, "dateModified");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("album_id", Long.valueOf(this.mAlbumId));
        contentValues.put("name", this.mName);
        contentValues.put("serverId", this.mServerId);
        contentValues.put("attributes", Long.valueOf(this.mAttributes));
        contentValues.put("dateTaken", Long.valueOf(this.mDateTaken));
        contentValues.put("dateModified", Long.valueOf(this.mDateModified));
        contentValues.put("sortBy", Long.valueOf(this.mSortBy));
        contentValues.put("localPath", this.mDirectoryPath);
        contentValues.put("sortInfo", this.mSortInfo);
        contentValues.put("photoCount", Integer.valueOf(this.mPhotoCount));
        CoverBean coverBean = this.mCover;
        if (coverBean != null) {
            contentValues.put("coverId", Long.valueOf(coverBean.id));
            contentValues.put("coverPath", this.mCover.mCoverPath);
            contentValues.put("coverSha1", this.mCover.mCoverSha1);
            contentValues.put("coverSyncState", Integer.valueOf(this.mCover.mCoverSyncState));
            contentValues.put("coverSize", Long.valueOf(this.mCover.mCoverSize));
            contentValues.put("is_manual_set_cover", Integer.valueOf(this.mCover.isManualSetCover ? 1 : 0));
        }
        ExtraInfo extraInfo = this.mExtraInfo;
        if (extraInfo != null) {
            contentValues.put(CallMethod.ARG_EXTRA_STRING, GsonUtils.toJson(extraInfo));
        }
    }

    public static Album fromContentValues(ContentValues contentValues) {
        Long asLong = contentValues.getAsLong(j.c);
        if (asLong == null) {
            return null;
        }
        Long asLong2 = contentValues.getAsLong("attributes");
        Album album = new Album(asLong.longValue());
        if (asLong2 != null) {
            album.setAttributes(asLong2.longValue());
        }
        Long asLong3 = contentValues.getAsLong("dateModified");
        if (asLong3 != null) {
            album.mDateModified = asLong3.longValue();
        }
        Long asLong4 = contentValues.getAsLong(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        if (asLong4 != null) {
            album.mAlbumSize = asLong4.longValue();
        }
        Long asLong5 = contentValues.getAsLong("coverId");
        String asString = contentValues.getAsString("coverPath");
        Boolean asBoolean = contentValues.getAsBoolean("is_manual_set_cover");
        String asString2 = contentValues.getAsString("coverSha1");
        Integer asInteger = contentValues.getAsInteger("coverSyncState");
        Long asLong6 = contentValues.getAsLong("coverSize");
        if (asLong5 != null || asString != null || asBoolean != null || asString2 != null || asInteger != null || asLong6 != null) {
            if (album.mCover == null) {
                album.mCover = new CoverBean();
            }
            album.mCover.id = asLong5.longValue();
            album.mCover.mCoverPath = asString;
            album.mCover.mCoverSyncState = asInteger.intValue();
            album.mCover.mCoverSha1 = asString2;
            album.mCover.mCoverSize = asLong6.longValue();
            album.mCover.isManualSetCover = asBoolean.booleanValue();
        }
        Long asLong7 = contentValues.getAsLong("dateTaken");
        if (asLong7 != null) {
            album.mDateTaken = asLong7.longValue();
        }
        String asString3 = contentValues.getAsString("name");
        if (asString3 != null) {
            album.mName = asString3;
        }
        String asString4 = contentValues.getAsString("serverId");
        if (asString4 != null) {
            album.mServerId = asString4;
        }
        String asString5 = contentValues.getAsString("localPath");
        if (asString5 != null) {
            album.mDirectoryPath = asString5;
        }
        String asString6 = contentValues.getAsString("sortInfo");
        if (asString6 != null && !asString6.isEmpty()) {
            album.mSortInfo = asString6;
        }
        Integer asInteger2 = contentValues.getAsInteger("sortBy");
        if (asInteger2 != null) {
            album.mSortBy = asInteger2.intValue();
        }
        String asString7 = contentValues.getAsString(CallMethod.ARG_EXTRA_STRING);
        if (asString7 != null) {
            album.mExtraInfo = ExtraInfo.newInstance(asString7);
        }
        return album;
    }

    public static Album fromCursor(Cursor cursor) {
        Album album = null;
        if (cursor != null && !cursor.isClosed()) {
            int columnIndex = cursor.getColumnIndex(j.c);
            if (-1 == columnIndex) {
                return null;
            }
            album = new Album(cursor.getLong(columnIndex));
            int columnIndex2 = cursor.getColumnIndex("name");
            if (!cursor.isNull(columnIndex2)) {
                album.mName = cursor.getString(columnIndex2);
            }
            int columnIndex3 = cursor.getColumnIndex("coverId");
            if (!cursor.isNull(columnIndex3)) {
                if (album.mCover == null) {
                    album.mCover = new CoverBean();
                }
                album.mCover.id = cursor.getLong(columnIndex3);
            }
            int columnIndex4 = cursor.getColumnIndex("coverPath");
            if (!cursor.isNull(columnIndex4)) {
                if (album.mCover == null) {
                    album.mCover = new CoverBean();
                }
                album.mCover.mCoverPath = cursor.getString(columnIndex4);
            }
            int columnIndex5 = cursor.getColumnIndex("coverSha1");
            if (!cursor.isNull(columnIndex5)) {
                if (album.mCover == null) {
                    album.mCover = new CoverBean();
                }
                album.mCover.mCoverSha1 = cursor.getString(columnIndex5);
            }
            int columnIndex6 = cursor.getColumnIndex("coverSyncState");
            if (!cursor.isNull(columnIndex6)) {
                if (album.mCover == null) {
                    album.mCover = new CoverBean();
                }
                album.mCover.mCoverSyncState = cursor.getInt(columnIndex6);
            }
            int columnIndex7 = cursor.getColumnIndex("coverSize");
            if (!cursor.isNull(columnIndex7)) {
                if (album.mCover == null) {
                    album.mCover = new CoverBean();
                }
                album.mCover.mCoverSize = cursor.getLong(columnIndex7);
            }
            int columnIndex8 = cursor.getColumnIndex("is_manual_set_cover");
            if (!cursor.isNull(columnIndex8)) {
                if (album.mCover == null) {
                    album.mCover = new CoverBean();
                }
                CoverBean coverBean = album.mCover;
                boolean z = true;
                if (cursor.getInt(columnIndex8) != 1) {
                    z = false;
                }
                coverBean.isManualSetCover = z;
            }
            int columnIndex9 = cursor.getColumnIndex("photoCount");
            if (!cursor.isNull(columnIndex9)) {
                album.mPhotoCount = cursor.getInt(columnIndex9);
            }
            int columnIndex10 = cursor.getColumnIndex(CallMethod.ARG_EXTRA_STRING);
            if (!cursor.isNull(columnIndex10)) {
                album.mExtraInfo = ExtraInfo.newInstance(cursor.getString(columnIndex10));
            }
            int columnIndex11 = cursor.getColumnIndex("serverId");
            if (!cursor.isNull(columnIndex11)) {
                album.mServerId = cursor.getString(columnIndex11);
            }
            int columnIndex12 = cursor.getColumnIndex("attributes");
            if (!cursor.isNull(columnIndex12)) {
                album.mAttributes = cursor.getLong(columnIndex12);
            }
            int columnIndex13 = cursor.getColumnIndex("dateTaken");
            if (!cursor.isNull(columnIndex13)) {
                album.mDateTaken = cursor.getLong(columnIndex13);
            }
            int columnIndex14 = cursor.getColumnIndex("sortBy");
            if (!cursor.isNull(columnIndex14)) {
                album.mSortBy = cursor.getLong(columnIndex14);
            }
            int columnIndex15 = cursor.getColumnIndex("localPath");
            if (!cursor.isNull(columnIndex15)) {
                album.mDirectoryPath = cursor.getString(columnIndex15);
            }
            int columnIndex16 = cursor.getColumnIndex("sortInfo");
            if (!cursor.isNull(columnIndex16)) {
                album.mSortInfo = cursor.getString(columnIndex16);
            }
            int columnIndex17 = cursor.getColumnIndex("dateModified");
            if (!cursor.isNull(columnIndex17)) {
                album.mDateModified = cursor.getLong(columnIndex17);
            }
            int columnIndex18 = cursor.getColumnIndex(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
            if (!cursor.isNull(columnIndex18)) {
                album.mAlbumSize = cursor.getLong(columnIndex18);
            }
            album.mAlbumType = parseAlbumType(album);
            if (IncompatibleMediaType.isUnsupportedMediaType(BaseFileMimeUtil.getMimeType(album.getCoverPath())) && album.getCoverUri() != null) {
                String microPath = CloudUtils.getMicroPath(album.getCoverSha1());
                if (!TextUtils.isEmpty(microPath)) {
                    album.setCoverPath(microPath);
                }
            }
        }
        return album;
    }

    public String getDisplayedAlbumName() {
        if (this.mDisplayedName == null) {
            this.mDisplayedName = getDisplayedAlbumName(this.mServerId, this.mAlbumId, this.mName, this.mDirectoryPath);
        }
        return this.mDisplayedName;
    }

    public static String getDisplayedAlbumName(String str, long j, String str2, String str3) {
        String localizedAlbumNameIfExists = AlbumDataHelper.getLocalizedAlbumNameIfExists(str, j, str2);
        if (!localizedAlbumNameIfExists.equals(str2)) {
            return localizedAlbumNameIfExists;
        }
        if (isUserCreateAlbum(str3)) {
            Matcher matcher = sNamePattern.matcher(str2);
            return matcher.find() ? matcher.group(1) : str2;
        }
        String str4 = null;
        AlbumsStrategy.Album albumByPath = CloudControlStrategyHelper.getAlbumByPath(str3);
        if (albumByPath != null && !TextUtils.isEmpty(albumByPath.getPackageName())) {
            str4 = PackageUtils.getAppNameByPackage(albumByPath.getPackageName());
            if (!TextUtils.isEmpty(str4)) {
                return str4;
            }
        }
        if (albumByPath != null && albumByPath.getBestName() != null) {
            str4 = albumByPath.getBestName();
        }
        if (str4 == null) {
            return str2;
        }
        boolean isManualRenameRestricted = isManualRenameRestricted(str3);
        if (!str2.contains("_") && !isManualRenameRestricted) {
            return str2;
        }
        Matcher matcher2 = sNamePattern.matcher(str2);
        return ((!matcher2.find() || !matcher2.group(1).equals(str4)) && !isManualRenameRestricted) ? str2 : str4;
    }

    public static AlbumType parseAlbumType(Album album) {
        if (GalleryPreferences.Album.isForceTopAlbumByTopTime(album.getSortBy())) {
            return AlbumType.PINNED;
        }
        if (isSystemAlbum(album.getServerId())) {
            return AlbumType.SYSTEM;
        }
        if (!TextUtils.isEmpty(album.getBabyInfo())) {
            return AlbumType.BABY;
        }
        if (ShareAlbumHelper.isOtherShareAlbumId(album.getAlbumId())) {
            return AlbumType.OTHERS_SHARE;
        }
        if (isUserCreative(album.getLocalPath())) {
            return AlbumType.CREATIVE;
        }
        if (isUserCreateAlbum(album.getLocalPath())) {
            return AlbumType.USER_CREATE;
        }
        return AlbumType.NORMAL;
    }

    public long getAlbumSize() {
        return this.mAlbumSize;
    }

    public long getAlbumId() {
        return this.mAlbumId;
    }

    public String getAlbumName() {
        return this.mName;
    }

    public void setAlbumName(String str) {
        this.mName = str;
    }

    public void setAlbumType(AlbumType albumType) {
        this.mAlbumType = albumType;
    }

    public void setAlbumSize(long j) {
        this.mAlbumSize = j;
    }

    public void setPhotoCount(int i) {
        this.mPhotoCount = i;
    }

    public void setAlbumId(long j) {
        this.mAlbumId = j;
    }

    public long getCoverId() {
        CoverBean coverBean = this.mCover;
        if (coverBean == null) {
            return 0L;
        }
        return coverBean.getId();
    }

    public void setCoverId(long j) {
        if (this.mCover == null) {
            this.mCover = new CoverBean();
        }
        this.mCover.id = j;
    }

    public void setCover(CoverBean coverBean) {
        this.mCover = coverBean;
    }

    public String getCoverPath() {
        CoverBean coverBean = this.mCover;
        if (coverBean == null) {
            return null;
        }
        return coverBean.getCoverPath();
    }

    public void setCoverPath(String str) {
        if (this.mCover == null) {
            this.mCover = new CoverBean();
        }
        this.mCover.mCoverPath = str;
    }

    public boolean isManualSetCover() {
        CoverBean coverBean = this.mCover;
        return coverBean != null && coverBean.isManualSetCover;
    }

    public void setManualSetCover(boolean z) {
        if (this.mCover == null) {
            this.mCover = new CoverBean();
        }
        this.mCover.isManualSetCover = z;
    }

    public void setCoverSyncState(int i) {
        if (this.mCover == null) {
            this.mCover = new CoverBean();
        }
        this.mCover.mCoverSyncState = i;
    }

    public String getCoverSha1() {
        CoverBean coverBean = this.mCover;
        if (coverBean == null) {
            return null;
        }
        return coverBean.getCoverSha1();
    }

    public int getCoverSyncState() {
        CoverBean coverBean = this.mCover;
        if (coverBean == null) {
            return 0;
        }
        return coverBean.getCoverSyncState();
    }

    public long getCoverSize() {
        CoverBean coverBean = this.mCover;
        if (coverBean == null) {
            return 0L;
        }
        return coverBean.getCoverSize();
    }

    public int getPhotoCount() {
        return this.mPhotoCount;
    }

    public String getPeopleId() {
        ExtraInfo extraInfo = this.mExtraInfo;
        if (extraInfo == null) {
            return null;
        }
        return extraInfo.getPeopleId();
    }

    public String getBabyInfo() {
        ExtraInfo extraInfo = this.mExtraInfo;
        if (extraInfo == null) {
            return null;
        }
        return extraInfo.getBabyInfo();
    }

    public String getShareInfo() {
        ExtraInfo extraInfo = this.mExtraInfo;
        if (extraInfo == null) {
            return null;
        }
        return extraInfo.getShareInfo();
    }

    public String getThumbnailInfoOfBaby() {
        ExtraInfo extraInfo = this.mExtraInfo;
        if (extraInfo == null) {
            return null;
        }
        return extraInfo.getThumbnailInfo();
    }

    public String getServerId() {
        return this.mServerId;
    }

    public void setServerId(String str) {
        this.mServerId = str;
    }

    public void setSortBy(long j) {
        this.mSortBy = j;
    }

    public void setExtraInfo(ExtraInfo extraInfo) {
        this.mExtraInfo = extraInfo;
    }

    public long getAttributes() {
        return this.mAttributes;
    }

    public void setAttributes(long j) {
        this.mAttributes = j;
    }

    public void setDateModified(long j) {
        this.mDateModified = j;
    }

    public void setDateTaken(long j) {
        this.mDateTaken = j;
    }

    public long getDateTaken() {
        return this.mDateTaken;
    }

    public long getSortBy() {
        return this.mSortBy;
    }

    public String getLocalPath() {
        return this.mDirectoryPath;
    }

    public void setLocalPath(String str) {
        this.mDirectoryPath = str;
    }

    public AlbumType getAlbumType() {
        return this.mAlbumType;
    }

    public String getAlbumSortInfo() {
        return this.mSortInfo;
    }

    public double getAlbumSortPosition() {
        return AlbumSplitGroupHelper.getSortPosition(this.mSortInfo);
    }

    public long getDateModified() {
        return this.mDateModified;
    }

    public boolean isImmutable() {
        String str = this.mDirectoryPath;
        return str != null && str.startsWith(h.g);
    }

    public boolean isUserManualMoveToAlbumHome() {
        return isUserManualMoveToAlbumHome(getAttributes());
    }

    public void setSortInfo(String str) {
        this.mSortInfo = str;
    }

    public boolean isManualMovePosition() {
        return this.mSortInfo.contains("|");
    }

    /* loaded from: classes2.dex */
    public static class ExtraInfo implements Parcelable {
        public static final Parcelable.Creator<ExtraInfo> CREATOR = new Parcelable.Creator<ExtraInfo>() { // from class: com.miui.gallery.model.dto.Album.ExtraInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public ExtraInfo mo1135createFromParcel(Parcel parcel) {
                return new ExtraInfo(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public ExtraInfo[] mo1136newArray(int i) {
                return new ExtraInfo[i];
            }
        };
        @SerializedName("isPublic")
        private Boolean isPublic;
        @SerializedName("appKey")
        public String mAppKey;
        @SerializedName("babyInfoJson")
        private String mBabyInfo;
        public DescriptionBean mDescriptionBean;
        @SerializedName("description")
        private String mDescriptionStr;
        @SerializedName("peopleId")
        private String mPeopleId;
        @SerializedName("publicUrl")
        private String mPublicUrl;
        @SerializedName("sharerInfo")
        private String mShareInfo;
        @SerializedName("thumbnailInfo")
        private String mThumbnailInfo;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public ExtraInfo() {
            this.mBabyInfo = "";
            this.mPeopleId = "";
            this.mShareInfo = "";
            this.mThumbnailInfo = "";
            this.mAppKey = "";
        }

        public static ExtraInfo newInstance(String str) {
            try {
                return (ExtraInfo) GsonUtils.fromJson(str, (Class<Object>) ExtraInfo.class);
            } catch (Exception unused) {
                DefaultLogger.e("Album", "ExtraInfo parse error: %s", str);
                return null;
            }
        }

        public String getAppKey() {
            return this.mAppKey;
        }

        public DescriptionBean getDescriptionBean() {
            String str = this.mDescriptionStr;
            if (str == null) {
                return null;
            }
            if (this.mDescriptionBean == null) {
                DescriptionBean descriptionBean = (DescriptionBean) GsonUtils.fromJson(str, (Class<Object>) DescriptionBean.class);
                this.mDescriptionBean = descriptionBean;
                if (descriptionBean != null) {
                    descriptionBean.descriptionString = this.mDescriptionStr;
                }
            }
            return this.mDescriptionBean;
        }

        public String getThumbnailInfo() {
            return this.mThumbnailInfo;
        }

        public String getPeopleId() {
            return this.mPeopleId;
        }

        public String getBabyInfo() {
            return this.mBabyInfo;
        }

        public String getShareInfo() {
            return this.mShareInfo;
        }

        public boolean isPublic() {
            Boolean bool = this.isPublic;
            return bool != null && bool.booleanValue();
        }

        public String getPublicUrl() {
            return this.mPublicUrl;
        }

        public void setBabyInfo(String str) {
            this.mBabyInfo = str;
        }

        public ExtraInfo(Parcel parcel) {
            this.mBabyInfo = "";
            this.mPeopleId = "";
            this.mShareInfo = "";
            this.mThumbnailInfo = "";
            this.mAppKey = "";
            this.mBabyInfo = parcel.readString();
            this.mPeopleId = parcel.readString();
            this.mShareInfo = parcel.readString();
            this.mThumbnailInfo = parcel.readString();
            this.mDescriptionBean = (DescriptionBean) parcel.readParcelable(Parcelable.class.getClassLoader());
            this.mAppKey = parcel.readString();
            this.isPublic = ParcelableUtil.readBool(parcel);
            this.mPublicUrl = parcel.readString();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mBabyInfo);
            parcel.writeString(this.mPeopleId);
            parcel.writeString(this.mShareInfo);
            parcel.writeString(this.mThumbnailInfo);
            parcel.writeParcelable(this.mDescriptionBean, i);
            parcel.writeString(this.mAppKey);
            ParcelableUtil.writeBool(parcel, this.isPublic);
            parcel.writeString(this.mPublicUrl);
        }

        public void update(String str, Object obj) {
            try {
                if (str.equals("appKey")) {
                    this.mAppKey = obj.toString();
                }
                if (str.equals("babyInfoJson")) {
                    this.mBabyInfo = obj.toString();
                }
                if (str.equals("peopleId")) {
                    this.mPeopleId = obj.toString();
                }
                if (str.equals("publicUrl")) {
                    this.mPublicUrl = obj.toString();
                }
                if (str.equals("sharerInfo")) {
                    this.mShareInfo = obj.toString();
                }
                if (str.equals("thumbnailInfo")) {
                    this.mThumbnailInfo = obj.toString();
                }
                if (str.equals("isPublic")) {
                    this.isPublic = Boolean.valueOf(Boolean.parseBoolean(obj.toString()));
                }
                if (!str.equals("description")) {
                    return;
                }
                String obj2 = obj.toString();
                this.mDescriptionStr = obj2;
                this.mDescriptionBean = DescriptionBean.newInstance(obj2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* loaded from: classes2.dex */
        public static class DescriptionBean implements Parcelable {
            public static final Parcelable.Creator<DescriptionBean> CREATOR = new Parcelable.Creator<DescriptionBean>() { // from class: com.miui.gallery.model.dto.Album.ExtraInfo.DescriptionBean.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.Creator
                /* renamed from: createFromParcel */
                public DescriptionBean mo1137createFromParcel(Parcel parcel) {
                    return new DescriptionBean(parcel);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.Creator
                /* renamed from: newArray */
                public DescriptionBean[] mo1138newArray(int i) {
                    return new DescriptionBean[i];
                }
            };
            public Boolean autoUpload;
            public String descriptionString;
            public Boolean hidden;
            public Boolean isFavorite;
            public String localFile;
            public Boolean manualHidden;
            public Boolean manualRubbish;
            public Boolean manualSetUpload;
            public Boolean manualShowInOtherAlbums;
            public Boolean manualShowInPhotosTab;
            public Boolean rubbish;
            public Boolean showInOtherAlbums;
            public Boolean showInPhotosTab;

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            public DescriptionBean() {
            }

            public static DescriptionBean newInstance(String str) {
                try {
                    if (TextUtils.isEmpty(str)) {
                        return null;
                    }
                    DescriptionBean descriptionBean = (DescriptionBean) GsonUtils.fromJson(str, (Class<Object>) DescriptionBean.class);
                    descriptionBean.descriptionString = str;
                    return descriptionBean;
                } catch (Exception e) {
                    try {
                        DefaultLogger.e("AlbumBean", e);
                        if (TextUtils.isEmpty(str)) {
                            return null;
                        }
                        JSONObject jSONObject = new JSONObject(str);
                        DescriptionBean descriptionBean2 = new DescriptionBean();
                        descriptionBean2.descriptionString = str;
                        descriptionBean2.rubbish = getRubbishAttributeFromDescription(jSONObject);
                        descriptionBean2.manualRubbish = getRubbishManualAttributeFromDescription(jSONObject);
                        descriptionBean2.autoUpload = getAutoUploadAttributeFromDescription(jSONObject);
                        descriptionBean2.manualSetUpload = getManualSetUploadFromDescription(jSONObject);
                        descriptionBean2.hidden = getHiddenAttributeFromDescription(jSONObject);
                        descriptionBean2.manualHidden = getHiddenManualAttributeFromDescription(jSONObject);
                        descriptionBean2.localFile = getLocalFileFromDescription(jSONObject);
                        descriptionBean2.showInOtherAlbums = getShowInOtherAlbumsAttributeFromDescription(jSONObject);
                        descriptionBean2.manualShowInOtherAlbums = getShowInOtherAlbumsManualAttributeFromDescription(jSONObject);
                        descriptionBean2.showInPhotosTab = getShowInPhotoTabAttributeFromDescription(jSONObject);
                        descriptionBean2.manualShowInPhotosTab = getShowInPhotoTabManualAttributeFromDescription(jSONObject);
                        descriptionBean2.isFavorite = getIsFavoriteFromDescription(jSONObject);
                        return descriptionBean2;
                    } catch (Exception e2) {
                        DefaultLogger.e("AlbumBean", e2);
                        return null;
                    }
                }
            }

            public String getDescriptionString() {
                return this.descriptionString;
            }

            public Boolean isHidden() {
                return this.hidden;
            }

            public Boolean isManualHidden() {
                return this.manualHidden;
            }

            public Boolean isAutoUpload() {
                return this.autoUpload;
            }

            public Boolean isManualSetUpload() {
                return this.manualSetUpload;
            }

            public Boolean isShowInOtherAlbums() {
                return this.showInOtherAlbums;
            }

            public Boolean isManualShowInOtherAlbums() {
                return this.manualShowInOtherAlbums;
            }

            public Boolean isShowInPhotosTab() {
                return this.showInPhotosTab;
            }

            public Boolean isManualShowInPhotosTab() {
                return this.manualShowInPhotosTab;
            }

            public Boolean isRubbish() {
                return this.rubbish;
            }

            public Boolean isFavorite() {
                return this.isFavorite;
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(this.descriptionString);
                parcel.writeString(this.localFile);
                ParcelableUtil.writeBool(parcel, this.hidden, this.manualHidden, this.autoUpload, this.manualSetUpload, this.showInOtherAlbums, this.manualShowInOtherAlbums, this.showInPhotosTab, this.manualShowInPhotosTab, this.rubbish, this.manualRubbish, this.isFavorite);
            }

            public DescriptionBean(Parcel parcel) {
                this.descriptionString = parcel.readString();
                this.localFile = parcel.readString();
                this.hidden = ParcelableUtil.readBool(parcel);
                this.manualHidden = ParcelableUtil.readBool(parcel);
                this.autoUpload = ParcelableUtil.readBool(parcel);
                this.manualSetUpload = ParcelableUtil.readBool(parcel);
                this.showInOtherAlbums = ParcelableUtil.readBool(parcel);
                this.manualShowInOtherAlbums = ParcelableUtil.readBool(parcel);
                this.showInPhotosTab = ParcelableUtil.readBool(parcel);
                this.manualShowInPhotosTab = ParcelableUtil.readBool(parcel);
                this.rubbish = ParcelableUtil.readBool(parcel);
                this.manualRubbish = ParcelableUtil.readBool(parcel);
                this.isFavorite = ParcelableUtil.readBool(parcel);
            }

            public String toString() {
                return "DescriptionBean{description='" + this.descriptionString + "'localFile='" + this.localFile + CoreConstants.SINGLE_QUOTE_CHAR + ", hidden=" + this.hidden + ", manualHidden=" + this.manualHidden + ", autoUpload=" + this.autoUpload + ", manualSetUpload=" + this.manualSetUpload + ", showInOtherAlbums=" + this.showInOtherAlbums + ", manualShowInOtherAlbums=" + this.manualShowInOtherAlbums + ", showInPhotosTab=" + this.showInPhotosTab + ", manualShowInPhotosTab=" + this.manualShowInPhotosTab + ", rubbish=" + this.rubbish + ", manualRubbish=" + this.manualRubbish + ", isFavorite=" + this.isFavorite + '}';
            }

            public static String getLocalFileFromDescription(JSONObject jSONObject) {
                if (jSONObject != null) {
                    try {
                        return jSONObject.optString("localFile");
                    } catch (Exception e) {
                        DefaultLogger.w("AlbumBean", e);
                        return null;
                    }
                }
                return null;
            }

            public static Boolean getAutoUploadAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("autoUpload", jSONObject);
            }

            public static Boolean getManualSetUploadFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("manualSetUpload", jSONObject);
            }

            public static Boolean getShowInOtherAlbumsAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("showInOtherAlbums", jSONObject);
            }

            public static Boolean getShowInOtherAlbumsManualAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("manualShowInOtherAlbums", jSONObject);
            }

            public static Boolean getShowInPhotoTabAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("showInPhotosTab", jSONObject);
            }

            public static Boolean getShowInPhotoTabManualAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("manualShowInPhotosTab", jSONObject);
            }

            public static Boolean getAttributeFromDescription(String str, JSONObject jSONObject) {
                if (jSONObject != null) {
                    try {
                        if (!jSONObject.has(str)) {
                            return null;
                        }
                        return Boolean.valueOf(jSONObject.getBoolean(str));
                    } catch (Exception e) {
                        DefaultLogger.w("AlbumBean", e);
                    }
                }
                return null;
            }

            public static Boolean getHiddenAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("hidden", jSONObject);
            }

            public static Boolean getHiddenManualAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("manualHidden", jSONObject);
            }

            public static Boolean getIsFavoriteFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("isFavorite", jSONObject);
            }

            public static Boolean getRubbishAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("rubbish", jSONObject);
            }

            public static Boolean getRubbishManualAttributeFromDescription(JSONObject jSONObject) {
                return getAttributeFromDescription("manualRubbish", jSONObject);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class CoverBean implements Parcelable {
        public static final Parcelable.Creator<CoverBean> CREATOR = new Parcelable.Creator<CoverBean>() { // from class: com.miui.gallery.model.dto.Album.CoverBean.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public CoverBean mo1133createFromParcel(Parcel parcel) {
                return new CoverBean(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public CoverBean[] mo1134newArray(int i) {
                return new CoverBean[i];
            }
        };
        public long id;
        public boolean isManualSetCover;
        public String mCoverPath;
        public String mCoverSha1;
        public long mCoverSize;
        public int mCoverSyncState;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public CoverBean() {
            this.id = -1L;
        }

        public CoverBean(long j, String str, String str2, int i, long j2, boolean z) {
            this.id = -1L;
            this.id = j;
            this.mCoverPath = str;
            this.mCoverSha1 = str2;
            this.mCoverSyncState = i;
            this.mCoverSize = j2;
            this.isManualSetCover = z;
        }

        public long getId() {
            return this.id;
        }

        public String getCoverPath() {
            return this.mCoverPath;
        }

        public String getCoverSha1() {
            return this.mCoverSha1;
        }

        public int getCoverSyncState() {
            return this.mCoverSyncState;
        }

        public long getCoverSize() {
            return this.mCoverSize;
        }

        public CoverBean(Parcel parcel) {
            this.id = -1L;
            this.id = parcel.readLong();
            this.mCoverPath = parcel.readString();
            this.mCoverSha1 = parcel.readString();
            this.mCoverSyncState = parcel.readInt();
            this.mCoverSize = parcel.readLong();
            Boolean readBool = ParcelableUtil.readBool(parcel);
            this.isManualSetCover = readBool == null ? false : readBool.booleanValue();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeLong(this.id);
            parcel.writeString(this.mCoverPath);
            parcel.writeString(this.mCoverSha1);
            parcel.writeInt(this.mCoverSyncState);
            parcel.writeLong(this.mCoverSize);
            ParcelableUtil.writeBool(parcel, Boolean.valueOf(this.isManualSetCover));
        }
    }

    public boolean isCameraAlbum() {
        return isCameraAlbum(this.mServerId);
    }

    public static boolean isCameraAlbum(String str) {
        return String.valueOf(1L).equals(str);
    }

    public Uri getCoverUri() {
        CoverBean coverBean = this.mCover;
        if (coverBean == null) {
            return null;
        }
        return getCoverUri(coverBean.getCoverSyncState(), this.mCover.getId());
    }

    public static Uri getCoverUri(int i, long j) {
        if (i == 0) {
            return getCoverUri(j);
        }
        return null;
    }

    public static Uri getCoverUri(long j) {
        return CloudUriAdapter.getDownloadUri(j);
    }

    public boolean isHiddenAlbum() {
        return isHiddenAlbum(getAttributes());
    }

    public boolean isAutoUploadedAlbum() {
        return isAutoUploadedAlbum(this);
    }

    public static boolean isAutoUploadedAlbum(Album album) {
        if (album == null) {
            return false;
        }
        return (album.getAttributes() & 1) != 0 || (isSystemAlbum(album.getServerId()) && !isScreenshotsAlbum(album.getServerId()) && !isScreenshotsRecorders(album.getAlbumId())) || isOtherShareAlbum(album.getAlbumId());
    }

    public static boolean isScreenshotsAlbum(String str) {
        return Objects.equals(String.valueOf(2L), str);
    }

    public boolean isScreenshotsAlbum() {
        return isScreenshotsAlbum(getServerId());
    }

    public static boolean isOtherShareAlbum(long j) {
        return ShareAlbumHelper.isOtherShareAlbumId(j);
    }

    public boolean isOtherShareAlbum() {
        return isOtherShareAlbum(getAlbumId());
    }

    public boolean isBabyAlbum() {
        ExtraInfo extraInfo = this.mExtraInfo;
        return extraInfo != null && !TextUtils.isEmpty(extraInfo.mBabyInfo);
    }

    public boolean isSystemAlbum() {
        return isSystemAlbum(getServerId());
    }

    public static boolean isSystemAlbum(String str) {
        return str != null && AlbumDataHelper.isSystemAlbum(str);
    }

    public boolean isShareToDevice() {
        return (this.mAttributes & 1280) != 0;
    }

    public boolean isShareAlbum() {
        return isShareAlbum(getAlbumId());
    }

    public static boolean isShareAlbum(long j) {
        return isOtherShareAlbum(j) || isOwnerShareAlbum(j);
    }

    public boolean isOwnerShareAlbum() {
        return isOwnerShareAlbum(getAlbumId());
    }

    public static boolean isOwnerShareAlbum(long j) {
        return j < 2147383647 && ShareAlbumCacheManager.getInstance().containsKey(j);
    }

    public boolean isOtherAlbum() {
        return isOtherAlbum(getAttributes());
    }

    public boolean isScreenshotsRecorders() {
        return isScreenshotsRecorders(getAlbumId());
    }

    public boolean isAllPhotosAlbum() {
        return isAllPhotosAlbum(getAlbumId());
    }

    public boolean isRubbishAlbum() {
        return isRubbishAlbum(getAttributes());
    }

    public boolean isManualRubbishAlbum() {
        return isManualRubbishAlbum(getAttributes());
    }

    public boolean isShowedPhotosTabAlbum() {
        return isShowedPhotosTabAlbum(this);
    }

    public boolean isShowedPhotosTabAlbum(Album album) {
        return isShowedPhotosTabAlbum(album.getAttributes());
    }

    public boolean albumUnwriteable() {
        return isImmutable();
    }

    public boolean isManualRenameRestricted() {
        return isManualRenameRestricted(getLocalPath());
    }

    public static boolean isManualRenameRestricted(String str) {
        AlbumsStrategy.Album albumByPath;
        return !TextUtils.isEmpty(str) && (albumByPath = CloudControlStrategyHelper.getAlbumByPath(StorageUtils.ensureCommonRelativePath(str))) != null && albumByPath.getAttributes() != null && albumByPath.getAttributes().isManualRenameRestricted();
    }

    public boolean isUserCreateAlbum() {
        return isUserCreateAlbum(getLocalPath());
    }

    public static boolean isUserCreateAlbum(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().contains(StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM.toLowerCase());
    }

    public boolean isVideoAlbum() {
        return isVideoAlbum(getAlbumId());
    }

    public boolean isFavoritesAlbum() {
        return isFavoritesAlbum(getAlbumId());
    }

    public boolean isForceTypeTime() {
        return isForceTypeTime(getSortBy());
    }

    public static boolean isForceTypeTime(long j) {
        return GalleryPreferences.Album.isForceTopAlbumByTopTime(j);
    }

    public boolean isUserCreative() {
        return isUserCreative(getLocalPath());
    }

    public static boolean isUserCreative(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String str2 = StorageConstants.RELATIVE_DIRECTORY_CREATIVE;
        return str.contains(str2) || str.toLowerCase().contains(str2.toLowerCase());
    }

    public boolean isScreenRecorderAlbum() {
        return isScreenRecorderAlbum(getLocalPath());
    }

    public static boolean isScreenRecorderAlbum(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.toLowerCase().contains(MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH.toLowerCase());
    }

    public boolean isThirdAlbum() {
        return isThirdAlbum(getServerId(), getLocalPath());
    }

    public static boolean isThirdAlbum(String str, String str2) {
        return !isUserCreateAlbum(str2) && !isSystemAlbum(str);
    }

    public boolean isCacheable() {
        long albumId = getAlbumId();
        return !isOtherAlbum() && !isAIAlbums(albumId) && !isOtherAlbums(albumId) && !isTrashAlbums(albumId);
    }

    public static boolean isRawAlbum(String str) {
        return MIUIStorageConstants.DIRECTORY_CAMERA_RAW_PATH.equalsIgnoreCase(str);
    }

    public boolean isRawAlbum() {
        return isRawAlbum(getLocalPath());
    }

    public boolean isVirtualAlbum() {
        return isVirtualAlbum(this.mAlbumId);
    }

    public static boolean isVirtualAlbum(long j) {
        Integer[] numArr;
        int length = GalleryContract.Album.ALL_VIRTUAL_ALBUM_IDS.length;
        for (int i = 0; i < length; i++) {
            if (numArr[i].intValue() == j) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVirtualAlbumByServerId(long j) {
        for (Long l : GalleryContract.Album.ALL_VIRTUAL_ALBUM_SERVER_IDS) {
            if (l.longValue() == j) {
                return true;
            }
        }
        return false;
    }

    public boolean isMustVisibleAlbum() {
        return isMustVisibleAlbum(this.mDirectoryPath);
    }

    public static boolean isMustVisibleAlbum(String str) {
        return str == null || sMiuiVisiblePattern.matcher(str).find();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mId);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mPhotoCount);
        parcel.writeString(this.mServerId);
        parcel.writeLong(this.mAttributes);
        parcel.writeLong(this.mDateTaken);
        parcel.writeLong(this.mSortBy);
        parcel.writeString(this.mDirectoryPath);
        parcel.writeInt(this.mAlbumType.ordinal());
        parcel.writeString(this.mSortInfo);
        parcel.writeLong(this.mDateModified);
        parcel.writeParcelable(this.mExtraInfo, i);
        parcel.writeParcelable(this.mCover, i);
    }

    public Album(Parcel parcel) {
        this.mSortInfo = "0";
        this.mId = parcel.readLong();
        this.mName = parcel.readString();
        this.mPhotoCount = parcel.readInt();
        this.mServerId = parcel.readString();
        this.mAttributes = parcel.readLong();
        this.mDateTaken = parcel.readLong();
        this.mSortBy = parcel.readLong();
        this.mDirectoryPath = parcel.readString();
        this.mAlbumType = AlbumType.values()[parcel.readInt()];
        this.mSortInfo = parcel.readString();
        this.mDateModified = parcel.readLong();
        this.mExtraInfo = (ExtraInfo) parcel.readParcelable(getClass().getClassLoader());
        this.mCover = (CoverBean) parcel.readParcelable(getClass().getClassLoader());
    }
}
