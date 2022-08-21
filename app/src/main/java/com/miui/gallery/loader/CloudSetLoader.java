package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import ch.qos.logback.core.FileAppender;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.BaseCloudDataSet;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.util.MediaCursorHelper;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CloudSetLoader extends CursorSetLoader {
    public long mAlbumId;
    public String mAlbumName;
    public int mInitPos;
    public String mOrderBy;
    public String mSelection;
    public String[] mSelectionArgs;
    public boolean mUnfoldBurst;
    public Uri mUri;

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getTAG() {
        return "CloudSetLoader";
    }

    public CloudSetLoader(Context context, Uri uri, Bundle bundle) {
        super(context);
        this.mAlbumId = -1L;
        this.mUri = uri;
        if (bundle != null) {
            this.mInitPos = bundle.getInt("photo_init_position", 0);
            this.mSelection = bundle.getString("photo_selection", null);
            this.mSelectionArgs = bundle.getStringArray("photo_selection_args");
            this.mOrderBy = bundle.getString("photo_order_by", null);
            this.mAlbumName = bundle.getString("album_name", null);
            this.mAlbumId = bundle.getLong("album_id", -1L);
            this.mUnfoldBurst = bundle.getBoolean("unford_burst", false);
        }
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String[] getProjection() {
        return MediaCursorHelper.PROJECTION;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public Uri getUri() {
        Uri uri = this.mUri;
        return uri == null ? GalleryContract.Media.URI : uri;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getSelection() {
        return this.mSelection;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String[] getSelectionArgs() {
        return this.mSelectionArgs;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getOrder() {
        return TextUtils.isEmpty(this.mOrderBy) ? "alias_create_time DESC " : this.mOrderBy;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public CursorDataSet wrapDataSet(Cursor cursor) {
        AlbumManager.queryScreenshotsAlbumId(GalleryApp.sGetAndroidContext());
        if (this.mUnfoldBurst) {
            return new UnfoldBurstCloudDataSet(cursor, this.mInitPos, this.mAlbumId, this.mAlbumName);
        }
        return new CloudDataSet(cursor, this.mInitPos, this.mAlbumId, this.mAlbumName);
    }

    /* loaded from: classes2.dex */
    public static class CloudDataSet extends BaseCloudDataSet {
        @Override // com.miui.gallery.model.CursorDataSet
        public int burstKeyIndex() {
            return 23;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean foldBurst() {
            return true;
        }

        public CloudDataSet(Cursor cursor, int i, long j, String str) {
            super(cursor, i, j, str);
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public void bindItem(BaseDataItem baseDataItem, int i) {
            if (!moveToPosition(i)) {
                return;
            }
            wrapItemByCursor(baseDataItem, this.mCursor);
            if (foldBurst()) {
                BurstFilterCursor burstFilterCursor = (BurstFilterCursor) this.mCursor;
                if (burstFilterCursor.isBurstPosition(i)) {
                    ArrayList arrayList = new ArrayList();
                    for (Integer num : burstFilterCursor.getBurstGroup(i)) {
                        Cursor contentCursorAtPosition = burstFilterCursor.getContentCursorAtPosition(num.intValue());
                        CloudItem cloudItem = new CloudItem();
                        wrapItemByCursor(cloudItem, contentCursorAtPosition);
                        arrayList.add(cloudItem);
                    }
                    baseDataItem.setBurstItem(true);
                    baseDataItem.setBurstGroup(arrayList);
                    if (burstFilterCursor.isTimeBurstPosition(i)) {
                        baseDataItem.setTimeBurstItem(true);
                        baseDataItem.setSpecialTypeFlags(8388608L);
                    } else {
                        baseDataItem.setSpecialTypeFlags(64L);
                    }
                }
            }
            baseDataItem.queryIsScreenshot(GalleryApp.sGetAndroidContext());
        }

        public final void wrapItemByCursor(BaseDataItem baseDataItem, Cursor cursor) {
            long mediaId = MediaCursorHelper.getMediaId(cursor);
            baseDataItem.setKey(mediaId).setMicroPath(MediaCursorHelper.getMicroThumbnailPath(cursor)).setThumbPath(MediaCursorHelper.getThumbnailPath(cursor)).setFilePath(MediaCursorHelper.getFilePath(cursor)).setMimeType(MediaCursorHelper.getMimeType(cursor)).setCreateTime(MediaCursorHelper.getCreateTime(cursor)).setLocation(MediaCursorHelper.getLocation(cursor)).setSize(MediaCursorHelper.getSize(cursor)).setWidth(MediaCursorHelper.getWidth(cursor)).setHeight(MediaCursorHelper.getHeight(cursor)).setDuration(MediaCursorHelper.getDuration(cursor)).setSecretKey(MediaCursorHelper.getSecretKey(cursor)).setLatitude(MediaCursorHelper.getLatitude(cursor)).setLongitude(MediaCursorHelper.getLongitude(cursor)).mo1096setOrientation(MediaCursorHelper.getOrientation(cursor)).setTitle(MediaCursorHelper.getTitle(cursor));
            CloudItem cloudItem = (CloudItem) baseDataItem;
            cloudItem.setId(mediaId).setSynced(MediaCursorHelper.isSynced(cursor)).setSha1(MediaCursorHelper.getSha1(cursor)).setShare(ShareMediaManager.isOtherShareMediaId(mediaId)).setCreatorId(MediaCursorHelper.getCreator(cursor)).setIsFavorite(MediaCursorHelper.isFavorite(cursor)).setServerId(MediaCursorHelper.getServerId(cursor)).setLocalGroupId(MediaCursorHelper.getAlbumId(cursor));
            if (BaseFileMimeUtil.isRawFromMimeType(baseDataItem.getMimeType())) {
                cloudItem.setSpecialTypeFlags(FileAppender.DEFAULT_BUFFER_SIZE);
            }
            if ((MediaCursorHelper.getSpecialTypeFlags(cursor) & Long.MIN_VALUE) != 0) {
                cloudItem.setWatermarked();
            }
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public long getItemKey(int i) {
            if (moveToPosition(i)) {
                return MediaCursorHelper.getMediaId(this.mCursor);
            }
            return -1L;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public String getItemPath(int i) {
            if (moveToPosition(i)) {
                String filePath = MediaCursorHelper.getFilePath(this.mCursor);
                if (!TextUtils.isEmpty(filePath)) {
                    return filePath;
                }
                String thumbnailPath = MediaCursorHelper.getThumbnailPath(this.mCursor);
                if (!TextUtils.isEmpty(thumbnailPath)) {
                    return thumbnailPath;
                }
                String microThumbnailPath = MediaCursorHelper.getMicroThumbnailPath(this.mCursor);
                if (TextUtils.isEmpty(microThumbnailPath)) {
                    return null;
                }
                return microThumbnailPath;
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class UnfoldBurstCloudDataSet extends CloudDataSet {
        @Override // com.miui.gallery.loader.CloudSetLoader.CloudDataSet, com.miui.gallery.model.BaseDataSet
        public boolean foldBurst() {
            return false;
        }

        public UnfoldBurstCloudDataSet(Cursor cursor, int i, long j, String str) {
            super(cursor, i, j, str);
        }
    }
}
