package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import ch.qos.logback.core.FileAppender;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.model.BaseCloudDataSet;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.util.BaseFileMimeUtil;

/* loaded from: classes2.dex */
public class FaceCloudSetLoader extends CloudSetLoader {
    public static final String[] PROJECTION = {"photo_id", "microthumbfile", "thumbnailFile", "localFile", "mimeType", " CASE WHEN mixedDateTime NOT NULL THEN mixedDateTime ELSE dateTaken END ", "location", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "exifImageWidth", "exifImageLength", "duration", "exifGPSLatitude", "exifGPSLatitudeRef", "exifGPSLongitude", "exifGPSLongitudeRef", "localGroupId", "secretKey", "sha1", "photo_server_id", "exifOrientation", "isFavorite", "title"};

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String getTAG() {
        return "FaceCloudSetLoader";
    }

    public FaceCloudSetLoader(Context context, Uri uri, Bundle bundle) {
        super(context, uri, bundle);
        this.mUnfoldBurst = true;
    }

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public String[] getProjection() {
        return PROJECTION;
    }

    @Override // com.miui.gallery.loader.CloudSetLoader, com.miui.gallery.loader.CursorSetLoader
    public CursorDataSet wrapDataSet(Cursor cursor) {
        return new CloudDataSet(cursor, this.mInitPos, this.mAlbumId, this.mAlbumName);
    }

    /* loaded from: classes2.dex */
    public static class CloudDataSet extends BaseCloudDataSet {
        @Override // com.miui.gallery.model.BaseDataSet
        public boolean foldBurst() {
            return false;
        }

        public CloudDataSet(Cursor cursor, int i, long j, String str) {
            super(cursor, i, j, str);
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public void bindItem(BaseDataItem baseDataItem, int i) {
            if (!moveToPosition(i)) {
                return;
            }
            baseDataItem.setKey(this.mCursor.getLong(0)).setMicroPath(this.mCursor.getString(1)).setThumbPath(this.mCursor.getString(2)).setFilePath(this.mCursor.getString(3)).setMimeType(this.mCursor.getString(4)).setCreateTime(this.mCursor.getLong(5)).setLocation(this.mCursor.getString(6)).setSize(this.mCursor.getLong(7)).setWidth(this.mCursor.getInt(8)).setHeight(this.mCursor.getInt(9)).setDuration(this.mCursor.getInt(10)).setSecretKey(this.mCursor.getBlob(16)).setTitle(this.mCursor.getString(21));
            String string = this.mCursor.getString(11);
            if (!TextUtils.isEmpty(string)) {
                String string2 = this.mCursor.getString(12);
                String string3 = this.mCursor.getString(13);
                String string4 = this.mCursor.getString(14);
                baseDataItem.setLatitude(LocationUtil.convertRationalLatLonToDouble(string, string2));
                baseDataItem.setLongitude(LocationUtil.convertRationalLatLonToDouble(string3, string4));
            }
            CloudItem cloudItem = (CloudItem) baseDataItem;
            cloudItem.setId(this.mCursor.getLong(0)).setSynced(true).setSha1(this.mCursor.getString(17)).setLocalGroupId(this.mCursor.getLong(15));
            String string5 = this.mCursor.getString(18);
            cloudItem.setServerId(string5);
            cloudItem.setHasFace(!TextUtils.isEmpty(string5));
            cloudItem.mo1096setOrientation(this.mCursor.getInt(19));
            cloudItem.setIsFavorite(this.mCursor.getInt(20));
            if (!BaseFileMimeUtil.isRawFromMimeType(baseDataItem.getMimeType())) {
                return;
            }
            cloudItem.setSpecialTypeFlags(FileAppender.DEFAULT_BUFFER_SIZE);
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public long getItemKey(int i) {
            if (isValidate(i)) {
                this.mCursor.moveToPosition(i);
                return this.mCursor.getLong(0);
            }
            return -1L;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public String getItemPath(int i) {
            if (isValidate(i)) {
                this.mCursor.moveToPosition(i);
                String string = this.mCursor.getString(3);
                if (!TextUtils.isEmpty(string)) {
                    return string;
                }
                String string2 = this.mCursor.getString(2);
                if (!TextUtils.isEmpty(string2)) {
                    return string2;
                }
                String string3 = this.mCursor.getString(1);
                if (TextUtils.isEmpty(string3)) {
                    return null;
                }
                return string3;
            }
            return null;
        }
    }
}
