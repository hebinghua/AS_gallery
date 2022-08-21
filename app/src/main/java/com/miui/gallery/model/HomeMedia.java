package com.miui.gallery.model;

import android.content.ContentValues;
import android.database.Cursor;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.cache.IMediaSnapshot;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class HomeMedia extends Entity implements IMediaSnapshot {
    public int isFavorite;
    public String mAliasClearPath;
    public long mAliasCreateDate;
    public long mAliasCreateTime;
    public String mAliasMicroPath;
    public long mAliasSortTime;
    public long mBurstGroup;
    public int mBurstIndex;
    public long mDuration;
    public int mImageHeight;
    public int mImageWidth;
    public long mIsTimeBurst;
    public String mLocation;
    public long mMediaId;
    public String mMimeType;
    public String mOriginPath;
    public String mSha1;
    public long mSize;
    public String mSourcePkg;
    public long mSpecialTypeFlags;
    public long mSyncState;
    public String mThumbPath;
    public byte[] mThumbnailBlob;
    public String mTitle;
    public int mType;

    /* loaded from: classes2.dex */
    public interface Constants {
        public static final String[] PROJECTION = {j.c, "alias_micro_thumbnail", "alias_create_date", "alias_create_time", "location", "sha1", "serverType", "duration", "mimeType", "alias_sync_state", "thumbnailFile", "localFile", "alias_clear_thumbnail", "alias_is_favorite", "specialTypeFlags", "alias_sort_time", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "title", "burst_group_id", "is_time_burst", "burst_index", "exifImageWidth", "exifImageLength", "thumbnail_blob", "sourcePackage"};
    }

    public long getMediaId() {
        return this.mMediaId;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSha1() {
        return this.mSha1;
    }

    public byte[] getThumbnailBlob() {
        return this.mThumbnailBlob;
    }

    public void setThumbnailBlob(byte[] bArr) {
        this.mThumbnailBlob = bArr;
    }

    @Override // com.miui.gallery.provider.cache.IRecord
    public long getId() {
        return this.mMediaId;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getThumbnail() {
        return this.mThumbPath;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getFilePath() {
        return this.mOriginPath;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getType() {
        return this.mType;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getTitle() {
        return this.mTitle;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getDuration() {
        return this.mDuration;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSize() {
        return this.mSize;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getMimeType() {
        return this.mMimeType;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getLocation() {
        return this.mLocation;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSmallSizeThumb() {
        return this.mAliasMicroPath;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getCreateTime() {
        return this.mAliasCreateTime;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getCreateDate() {
        return (int) this.mAliasCreateDate;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getSyncState() {
        return (int) this.mSyncState;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getWidth() {
        return this.mImageWidth;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getHeight() {
        return this.mImageHeight;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getClearThumbnail() {
        return this.mAliasClearPath;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isFavorite() {
        return this.isFavorite == 1;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSpecialTypeFlags() {
        return this.mSpecialTypeFlags;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getSortTime() {
        return this.mAliasSortTime;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public long getBurstGroupKey() {
        return this.mBurstGroup;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public boolean isTimeBurst() {
        return this.mIsTimeBurst == 1;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public int getBurstIndex() {
        return this.mBurstIndex;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public byte[] getThumbBlob() {
        return this.mThumbnailBlob;
    }

    @Override // com.miui.gallery.provider.cache.IMediaSnapshot
    public String getSourcePkg() {
        return this.mSourcePkg;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList(24);
        Entity.addColumn(arrayList, "media_id", "INTEGER");
        Entity.addColumn(arrayList, "alias_micro_thumbnail", "TEXT");
        Entity.addColumn(arrayList, "alias_create_date", "INTEGER");
        Entity.addColumn(arrayList, "alias_create_time", "INTEGER");
        Entity.addColumn(arrayList, "location", "TEXT");
        Entity.addColumn(arrayList, "sha1", "TEXT");
        Entity.addColumn(arrayList, "serverType", "INTEGER");
        Entity.addColumn(arrayList, "duration", "INTEGER");
        Entity.addColumn(arrayList, "mimeType", "TEXT");
        Entity.addColumn(arrayList, "alias_sync_state", "INTEGER");
        Entity.addColumn(arrayList, "thumbnailFile", "TEXT");
        Entity.addColumn(arrayList, "localFile", "TEXT");
        Entity.addColumn(arrayList, "alias_clear_thumbnail", "TEXT");
        Entity.addColumn(arrayList, "alias_is_favorite", "INTEGER");
        Entity.addColumn(arrayList, "specialTypeFlags", "INTEGER");
        Entity.addColumn(arrayList, "alias_sort_time", "INTEGER");
        Entity.addColumn(arrayList, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "INTEGER");
        Entity.addColumn(arrayList, "title", "TEXT");
        Entity.addColumn(arrayList, "burst_group_id", "INTEGER");
        Entity.addColumn(arrayList, "is_time_burst", "INTEGER");
        Entity.addColumn(arrayList, "burst_index", "INTEGER");
        Entity.addColumn(arrayList, "exifImageWidth", "INTEGER");
        Entity.addColumn(arrayList, "exifImageLength", "INTEGER");
        Entity.addColumn(arrayList, "thumbnail_blob", "BLOB");
        Entity.addColumn(arrayList, "sourcePackage", "TEXT");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mMediaId = Entity.getLong(cursor, "media_id");
        this.mAliasMicroPath = Entity.getString(cursor, "alias_micro_thumbnail");
        this.mAliasCreateDate = Entity.getLong(cursor, "alias_create_date");
        this.mAliasCreateTime = Entity.getLong(cursor, "alias_create_time");
        this.mLocation = Entity.getString(cursor, "location");
        this.mSha1 = Entity.getString(cursor, "sha1");
        this.mType = Entity.getInt(cursor, "serverType");
        this.mDuration = Entity.getLong(cursor, "duration");
        this.mMimeType = Entity.getString(cursor, "mimeType");
        this.mSyncState = Entity.getInt(cursor, "alias_sync_state");
        this.mThumbPath = Entity.getString(cursor, "thumbnailFile");
        this.mOriginPath = Entity.getString(cursor, "localFile");
        this.mAliasClearPath = Entity.getString(cursor, "alias_clear_thumbnail");
        this.isFavorite = Entity.getInt(cursor, "alias_is_favorite");
        this.mSpecialTypeFlags = Entity.getInt(cursor, "specialTypeFlags");
        this.mAliasSortTime = Entity.getLong(cursor, "alias_sort_time");
        this.mSize = Entity.getLong(cursor, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        this.mTitle = Entity.getString(cursor, "title");
        this.mBurstGroup = Entity.getLong(cursor, "burst_group_id");
        this.mIsTimeBurst = Entity.getLong(cursor, "is_time_burst");
        this.mBurstIndex = Entity.getInt(cursor, "burst_index");
        this.mImageWidth = Entity.getInt(cursor, "exifImageWidth");
        this.mImageHeight = Entity.getInt(cursor, "exifImageLength");
        this.mThumbnailBlob = Entity.getBlob(cursor, "thumbnail_blob");
        this.mSourcePkg = Entity.getString(cursor, "sourcePackage");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("media_id", Long.valueOf(this.mMediaId));
        contentValues.put("alias_micro_thumbnail", this.mAliasMicroPath);
        contentValues.put("alias_create_date", Long.valueOf(this.mAliasCreateDate));
        contentValues.put("alias_create_time", Long.valueOf(this.mAliasCreateTime));
        contentValues.put("location", this.mLocation);
        contentValues.put("sha1", this.mSha1);
        contentValues.put("serverType", Integer.valueOf(this.mType));
        contentValues.put("duration", Long.valueOf(this.mDuration));
        contentValues.put("mimeType", this.mMimeType);
        contentValues.put("alias_sync_state", Long.valueOf(this.mSyncState));
        contentValues.put("thumbnailFile", this.mThumbPath);
        contentValues.put("localFile", this.mOriginPath);
        contentValues.put("alias_clear_thumbnail", this.mAliasClearPath);
        contentValues.put("alias_is_favorite", Integer.valueOf(this.isFavorite));
        contentValues.put("specialTypeFlags", Long.valueOf(this.mSpecialTypeFlags));
        contentValues.put("alias_sort_time", Long.valueOf(this.mAliasSortTime));
        contentValues.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(this.mSize));
        contentValues.put("title", this.mTitle);
        contentValues.put("burst_group_id", Long.valueOf(this.mBurstGroup));
        contentValues.put("is_time_burst", Long.valueOf(this.mIsTimeBurst));
        contentValues.put("burst_index", Integer.valueOf(this.mBurstIndex));
        contentValues.put("exifImageWidth", Integer.valueOf(this.mImageWidth));
        contentValues.put("exifImageLength", Integer.valueOf(this.mImageHeight));
        contentValues.put("thumbnail_blob", this.mThumbnailBlob);
        contentValues.put("sourcePackage", this.mSourcePkg);
    }

    public Object[] values() {
        return new Object[]{Long.valueOf(this.mMediaId), this.mAliasMicroPath, Long.valueOf(this.mAliasCreateDate), Long.valueOf(this.mAliasCreateTime), this.mLocation, this.mSha1, Integer.valueOf(this.mType), Long.valueOf(this.mDuration), this.mMimeType, Long.valueOf(this.mSyncState), this.mThumbPath, this.mOriginPath, this.mAliasClearPath, Integer.valueOf(this.isFavorite), Long.valueOf(this.mSpecialTypeFlags), Long.valueOf(this.mAliasSortTime), Long.valueOf(this.mSize), this.mTitle, Long.valueOf(this.mBurstGroup), Long.valueOf(this.mIsTimeBurst), Integer.valueOf(this.mBurstIndex), Integer.valueOf(this.mImageWidth), Integer.valueOf(this.mImageHeight), this.mThumbnailBlob, this.mSourcePkg};
    }

    public static HomeMedia from(IMediaSnapshot iMediaSnapshot) {
        HomeMedia homeMedia = new HomeMedia();
        homeMedia.mMediaId = iMediaSnapshot.getId();
        homeMedia.mAliasMicroPath = iMediaSnapshot.getSmallSizeThumb();
        homeMedia.mAliasCreateDate = iMediaSnapshot.getCreateDate();
        homeMedia.mAliasCreateTime = iMediaSnapshot.getCreateTime();
        homeMedia.mLocation = iMediaSnapshot.getLocation();
        homeMedia.mSha1 = iMediaSnapshot.getSha1();
        homeMedia.mType = iMediaSnapshot.getType();
        homeMedia.mDuration = iMediaSnapshot.getDuration();
        homeMedia.mMimeType = iMediaSnapshot.getMimeType();
        homeMedia.mSyncState = iMediaSnapshot.getSyncState();
        homeMedia.mThumbPath = iMediaSnapshot.getThumbnail();
        homeMedia.mOriginPath = iMediaSnapshot.getFilePath();
        homeMedia.mAliasClearPath = iMediaSnapshot.getClearThumbnail();
        homeMedia.isFavorite = iMediaSnapshot.isFavorite() ? 1 : 0;
        homeMedia.mSpecialTypeFlags = iMediaSnapshot.getSpecialTypeFlags();
        homeMedia.mAliasSortTime = iMediaSnapshot.getSortTime();
        homeMedia.mSize = iMediaSnapshot.getSize();
        homeMedia.mTitle = iMediaSnapshot.getTitle();
        homeMedia.mBurstGroup = iMediaSnapshot.getBurstGroupKey();
        homeMedia.mIsTimeBurst = iMediaSnapshot.isTimeBurst() ? 1L : 0L;
        homeMedia.mBurstIndex = iMediaSnapshot.getBurstIndex();
        homeMedia.mImageWidth = iMediaSnapshot.getWidth();
        homeMedia.mImageHeight = iMediaSnapshot.getHeight();
        homeMedia.mThumbnailBlob = iMediaSnapshot.getThumbBlob();
        homeMedia.mSourcePkg = iMediaSnapshot.getSourcePkg();
        return homeMedia;
    }
}
