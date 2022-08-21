package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.model.TrashDataItem;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.xiaomi.stat.a.j;

/* loaded from: classes2.dex */
public class TrashPhotoSetLoader extends CursorSetLoader {
    public static final String[] PROJECTION = {j.c, "cloudId", "cloudServerId", "fileName", "deleteTime", "microFilePath", "trashFilePath", "isOrigin", "albumLocalId", "albumServerId", "albumName", "albumPath", "sha1", "mimeType", "duration", "imageHeight", "imageWidth", "orientation", "mixedDateTime", "secretKey", GalleryContract.TrashBin.SERVER_TAG};
    public long mAlbumId;
    public String mAlbumName;
    public int mInitPos;
    public String mOrderBy;
    public String mSelection;
    public String[] mSelectionArgs;
    public boolean mUnfoldBurst;

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getTAG() {
        return "TrashPhotoSetLoader";
    }

    public TrashPhotoSetLoader(Context context, Uri uri, Bundle bundle) {
        super(context);
        this.mAlbumId = -1L;
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
        return PROJECTION;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public Uri getUri() {
        return GalleryContract.TrashBin.TRASH_BIN_URI;
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
        return this.mOrderBy;
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public CursorDataSet wrapDataSet(Cursor cursor) {
        return new TrashDataSet(cursor, this.mInitPos);
    }

    /* loaded from: classes2.dex */
    public static class TrashDataSet extends CursorDataSet {
        @Override // com.miui.gallery.model.BaseDataSet
        public boolean addToAlbum(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
            return false;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public int doDelete(int i, BaseDataItem baseDataItem, boolean z) {
            return 0;
        }

        public TrashDataSet(Cursor cursor, int i) {
            super(cursor, i);
        }

        @Override // com.miui.gallery.model.CursorDataSet, com.miui.gallery.model.BaseDataSet
        public BaseDataItem createItem(int i) {
            TrashDataItem trashDataItem = new TrashDataItem();
            bindItem(trashDataItem, i);
            return trashDataItem;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public void bindItem(BaseDataItem baseDataItem, int i) {
            if (!moveToPosition(i)) {
                return;
            }
            wrapItemByCursor(baseDataItem, this.mCursor);
        }

        public final void wrapItemByCursor(BaseDataItem baseDataItem, Cursor cursor) {
            ((TrashDataItem) baseDataItem).setFileName(cursor.getString(3)).setIsOrigin(cursor.getInt(7)).setSha1(cursor.getString(12)).setCloudId(cursor.getLong(1)).setServerTag(cursor.getLong(20)).setCloudServerId(cursor.getString(2)).setDeleteTime(cursor.getInt(4)).setAlbumServerId(cursor.getString(9)).setAlbumName(cursor.getString(10)).setAlbumPath(cursor.getString(11)).setKey(cursor.getLong(0)).setFilePath(cursor.getString(6)).setMicroPath(cursor.getString(5)).setTitle(BaseFileUtils.getFileTitle(cursor.getString(3))).setMimeType(cursor.getString(13)).setCreateTime(cursor.getLong(18)).setDuration(cursor.getInt(14)).setWidth(cursor.getInt(15)).setHeight(cursor.getInt(16)).setLocalGroupId(cursor.getLong(8)).mo1096setOrientation(cursor.getInt(17)).setSecretKey(cursor.getBlob(19));
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
                String string = this.mCursor.getString(6);
                if (!TextUtils.isEmpty(string)) {
                    return string;
                }
                String string2 = this.mCursor.getString(5);
                if (TextUtils.isEmpty(string2)) {
                    return null;
                }
                return string2;
            }
            return null;
        }
    }
}
