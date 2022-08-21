package com.miui.gallery.trash;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TrashBinItem extends Entity implements Comparable<TrashBinItem> {
    public static String SERVER_TAG = "serverTag";
    public long mAlbumAttributes;
    public long mAlbumLocalId;
    public String mAlbumName;
    public String mAlbumPath;
    public String mAlbumServerId;
    public long mCloudId;
    public String mCloudServerId;
    public String mCreatorId;
    public long mDeleteTime;
    public long mDuration;
    public String mFileName;
    public int mImageHeight;
    public int mImageWidth;
    public String mInvokerTag;
    public int mIsOrigin;
    public String mMicroPath;
    public String mMimeType;
    public long mMixedDateTime;
    public int mOrientation;
    public byte[] mSecretKey;
    public long mServerTag;
    public String mSha1;
    public long mSize;
    public int mStatus;
    public String mTrashFilePath;

    public TrashBinItem() {
    }

    public TrashBinItem(String str, long j, String str2, String str3, long j2, String str4, String str5, String str6, long j3, long j4) {
        this.mFileName = str;
        this.mCloudId = j;
        this.mCloudServerId = str2;
        this.mSha1 = str3;
        this.mAlbumLocalId = j2;
        this.mAlbumName = str4;
        this.mAlbumServerId = str5;
        this.mAlbumPath = str6;
        this.mAlbumAttributes = j3;
        this.mSize = j4;
    }

    public TrashBinItem(Cursor cursor) {
        initFromCursor(cursor);
    }

    public void setCreatorId(String str) {
        this.mCreatorId = str;
    }

    public long getMixedDateTime() {
        return this.mMixedDateTime;
    }

    public void setMixedDateTime(long j) {
        this.mMixedDateTime = j;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public long getCloudId() {
        return this.mCloudId;
    }

    public void setCloudServerId(String str) {
        this.mCloudServerId = str;
    }

    public String getCloudServerId() {
        return this.mCloudServerId;
    }

    public void setDeleteTime(long j) {
        this.mDeleteTime = j;
    }

    public void setMicroPath(String str) {
        this.mMicroPath = str;
    }

    public String getTrashFilePath() {
        return this.mTrashFilePath;
    }

    public void setTrashFilePath(String str) {
        this.mTrashFilePath = str;
    }

    public int getIsOrigin() {
        return this.mIsOrigin;
    }

    public void setIsOrigin(int i) {
        this.mIsOrigin = i;
    }

    public void setSize(long j) {
        this.mSize = j;
    }

    public long getAlbumLocalId() {
        return this.mAlbumLocalId;
    }

    public String getAlbumPath() {
        return this.mAlbumPath;
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public void setMimeType(String str) {
        this.mMimeType = str;
    }

    public void setImageHeight(int i) {
        this.mImageHeight = i;
    }

    public void setImageWidth(int i) {
        this.mImageWidth = i;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    public long getServerTag() {
        return this.mServerTag;
    }

    public void setServerTag(long j) {
        this.mServerTag = j;
    }

    public void setSecretKey(byte[] bArr) {
        this.mSecretKey = bArr;
    }

    public long getAlbumAttributes() {
        return this.mAlbumAttributes;
    }

    public void setInvokerTag(String str) {
        this.mInvokerTag = str;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "cloudId", "INTEGER");
        Entity.addColumn(arrayList, "cloudServerId", "TEXT");
        Entity.addColumn(arrayList, "fileName", "TEXT");
        Entity.addColumn(arrayList, "deleteTime", "INTEGER");
        Entity.addColumn(arrayList, "microFilePath", "TEXT");
        Entity.addColumn(arrayList, "trashFilePath", "TEXT");
        Entity.addColumn(arrayList, "isOrigin", "INTEGER");
        Entity.addColumn(arrayList, "albumLocalId", "INTEGER");
        Entity.addColumn(arrayList, "albumServerId", "TEXT");
        Entity.addColumn(arrayList, "albumName", "TEXT");
        Entity.addColumn(arrayList, "albumPath", "TEXT");
        Entity.addColumn(arrayList, "sha1", "TEXT");
        Entity.addColumn(arrayList, "mimeType", "TEXT");
        Entity.addColumn(arrayList, "imageHeight", "INTEGER");
        Entity.addColumn(arrayList, "imageWidth", "INTEGER");
        Entity.addColumn(arrayList, "orientation", "INTEGER");
        Entity.addColumn(arrayList, "duration", "INTEGER");
        Entity.addColumn(arrayList, "mixedDateTime", "INTEGER");
        Entity.addColumn(arrayList, SERVER_TAG, "INTEGER");
        Entity.addColumn(arrayList, "secretKey", "BLOB");
        Entity.addColumn(arrayList, "creatorId", "TEXT");
        Entity.addColumn(arrayList, "imageSize", "INTEGER");
        Entity.addColumn(arrayList, "status", "INTEGER");
        Entity.addColumn(arrayList, nexExportFormat.TAG_FORMAT_TAG, "TEXT");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mCloudId = Entity.getLong(cursor, "cloudId");
        this.mCloudServerId = Entity.getString(cursor, "cloudServerId");
        this.mFileName = Entity.getString(cursor, "fileName");
        this.mDeleteTime = Entity.getLong(cursor, "deleteTime");
        this.mMicroPath = Entity.getString(cursor, "microFilePath");
        this.mTrashFilePath = Entity.getString(cursor, "trashFilePath");
        this.mIsOrigin = Entity.getInt(cursor, "isOrigin");
        this.mAlbumLocalId = Entity.getLong(cursor, "albumLocalId");
        this.mAlbumServerId = Entity.getString(cursor, "albumServerId");
        this.mAlbumName = Entity.getString(cursor, "albumName");
        this.mAlbumPath = Entity.getString(cursor, "albumPath");
        this.mSha1 = Entity.getString(cursor, "sha1");
        this.mMimeType = Entity.getString(cursor, "mimeType");
        this.mImageHeight = Entity.getInt(cursor, "imageHeight");
        this.mImageWidth = Entity.getInt(cursor, "imageWidth");
        this.mOrientation = Entity.getInt(cursor, "orientation");
        this.mDuration = Entity.getLong(cursor, "duration");
        this.mMixedDateTime = Entity.getLong(cursor, "mixedDateTime");
        this.mServerTag = Entity.getLong(cursor, SERVER_TAG);
        this.mSecretKey = Entity.getBlob(cursor, "secretKey");
        this.mCreatorId = Entity.getString(cursor, "creatorId");
        this.mSize = Entity.getInt(cursor, "imageSize");
        this.mStatus = Entity.getInt(cursor, "status");
        this.mInvokerTag = Entity.getString(cursor, nexExportFormat.TAG_FORMAT_TAG);
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("cloudId", Long.valueOf(this.mCloudId));
        contentValues.put("cloudServerId", this.mCloudServerId);
        contentValues.put("fileName", this.mFileName);
        contentValues.put("deleteTime", Long.valueOf(this.mDeleteTime));
        contentValues.put("microFilePath", this.mMicroPath);
        contentValues.put("trashFilePath", this.mTrashFilePath);
        contentValues.put("isOrigin", Integer.valueOf(this.mIsOrigin));
        contentValues.put("albumLocalId", Long.valueOf(this.mAlbumLocalId));
        contentValues.put("albumServerId", this.mAlbumServerId);
        contentValues.put("albumName", this.mAlbumName);
        contentValues.put("albumPath", this.mAlbumPath);
        contentValues.put("sha1", this.mSha1);
        contentValues.put("mimeType", this.mMimeType);
        contentValues.put("imageHeight", Integer.valueOf(this.mImageHeight));
        contentValues.put("imageWidth", Integer.valueOf(this.mImageWidth));
        contentValues.put("orientation", Integer.valueOf(this.mOrientation));
        contentValues.put("duration", Long.valueOf(this.mDuration));
        contentValues.put("mixedDateTime", Long.valueOf(this.mMixedDateTime));
        contentValues.put(SERVER_TAG, Long.valueOf(this.mServerTag));
        contentValues.put("secretKey", this.mSecretKey);
        contentValues.put("creatorId", this.mCreatorId);
        contentValues.put("imageSize", Long.valueOf(this.mSize));
        contentValues.put("status", Integer.valueOf(this.mStatus));
        contentValues.put(nexExportFormat.TAG_FORMAT_TAG, this.mInvokerTag);
    }

    @Override // java.lang.Comparable
    public int compareTo(TrashBinItem trashBinItem) {
        return Long.compare(this.mDeleteTime, trashBinItem.mDeleteTime);
    }
}
