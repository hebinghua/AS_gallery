package com.miui.gallery.util.face;

import android.content.ContentValues;
import android.database.Cursor;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class PeopleItem extends Entity {
    public long mCloudId;
    public int mExifOrientation;
    public int mFaceCount;
    public float mFaceHScale;
    public float mFaceWScale;
    public float mFaceXScale;
    public float mFaceYScale;
    public String mLocalFile;
    public long mLocalId;
    public String mMicroThumbFile;
    public String mName;
    public String mRelationText;
    public int mRelationType;
    public String mServerId;
    public String mSha1;
    public long mSize;
    public String mThumbFile;
    public int mVisibilityType;
    public static final String[] Columns = {"localId", "peopleName", "serverId", "imageCloudId", "sha1", "microthumbfile", "thumbnailFile", "localFile", "exifOrientation", "faceXScale", "faceYScale", "faceWScale", "faceHScale", "relationType", "relationText", "visibilityType", "faceCount", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE};
    public static final String[] COMPAT_PROJECTION = {"localId as _id", "peopleName", "serverId", "imageCloudId as photo_id", "sha1", "microthumbfile", "thumbnailFile", "localFile", "exifOrientation", "faceXScale", "faceYScale", "faceWScale", "faceHScale", "relationType", "relationText", "visibilityType", "faceCount", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE};

    public long getLocalId() {
        return this.mLocalId;
    }

    public String getName() {
        return this.mName;
    }

    public String getServerId() {
        return this.mServerId;
    }

    public long getCloudId() {
        return this.mCloudId;
    }

    public String getMicroThumbFile() {
        return this.mMicroThumbFile;
    }

    public String getThumbFile() {
        return this.mThumbFile;
    }

    public String getLocalFile() {
        return this.mLocalFile;
    }

    public int getExifOrientation() {
        return this.mExifOrientation;
    }

    public float getFaceXScale() {
        return this.mFaceXScale;
    }

    public float getFaceYScale() {
        return this.mFaceYScale;
    }

    public float getFaceWScale() {
        return this.mFaceWScale;
    }

    public float getFaceHScale() {
        return this.mFaceHScale;
    }

    public int getRelationType() {
        return this.mRelationType;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        String[] strArr = Columns;
        Entity.addColumn(arrayList, strArr[0], "INTEGER");
        Entity.addColumn(arrayList, strArr[1], "TEXT");
        Entity.addColumn(arrayList, strArr[2], "TEXT");
        Entity.addColumn(arrayList, strArr[3], "INTEGER");
        Entity.addColumn(arrayList, strArr[4], "TEXT");
        Entity.addColumn(arrayList, strArr[5], "TEXT");
        Entity.addColumn(arrayList, strArr[6], "TEXT");
        Entity.addColumn(arrayList, strArr[7], "TEXT");
        Entity.addColumn(arrayList, strArr[8], "INTEGER");
        Entity.addColumn(arrayList, strArr[9], "REAL");
        Entity.addColumn(arrayList, strArr[10], "REAL");
        Entity.addColumn(arrayList, strArr[11], "REAL");
        Entity.addColumn(arrayList, strArr[12], "REAL");
        Entity.addColumn(arrayList, strArr[13], "INTEGER");
        Entity.addColumn(arrayList, strArr[14], "TEXT");
        Entity.addColumn(arrayList, strArr[15], "INTEGER");
        Entity.addColumn(arrayList, strArr[16], "INTEGER");
        Entity.addColumn(arrayList, strArr[17], "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        String[] strArr = Columns;
        this.mLocalId = Entity.getLong(cursor, strArr[0]);
        this.mName = Entity.getString(cursor, strArr[1]);
        this.mServerId = Entity.getString(cursor, strArr[2]);
        this.mCloudId = Entity.getLong(cursor, strArr[3]);
        this.mSha1 = Entity.getString(cursor, strArr[4]);
        this.mMicroThumbFile = Entity.getString(cursor, strArr[5]);
        this.mThumbFile = Entity.getString(cursor, strArr[6]);
        this.mLocalFile = Entity.getString(cursor, strArr[7]);
        this.mExifOrientation = Entity.getInt(cursor, strArr[8]);
        this.mFaceXScale = Entity.getFloat(cursor, strArr[9]);
        this.mFaceYScale = Entity.getFloat(cursor, strArr[10]);
        this.mFaceWScale = Entity.getFloat(cursor, strArr[11]);
        this.mFaceHScale = Entity.getFloat(cursor, strArr[12]);
        this.mRelationType = Entity.getInt(cursor, strArr[13]);
        this.mRelationText = Entity.getString(cursor, strArr[14]);
        this.mVisibilityType = Entity.getInt(cursor, strArr[15]);
        this.mFaceCount = Entity.getInt(cursor, strArr[16]);
        this.mSize = Entity.getLong(cursor, strArr[17]);
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        String[] strArr = Columns;
        contentValues.put(strArr[0], Long.valueOf(this.mLocalId));
        contentValues.put(strArr[1], this.mName);
        contentValues.put(strArr[2], this.mServerId);
        contentValues.put(strArr[3], Long.valueOf(this.mCloudId));
        contentValues.put(strArr[4], this.mSha1);
        contentValues.put(strArr[5], this.mMicroThumbFile);
        contentValues.put(strArr[6], this.mThumbFile);
        contentValues.put(strArr[7], this.mLocalFile);
        contentValues.put(strArr[8], Integer.valueOf(this.mExifOrientation));
        contentValues.put(strArr[9], Float.valueOf(this.mFaceXScale));
        contentValues.put(strArr[10], Float.valueOf(this.mFaceYScale));
        contentValues.put(strArr[11], Float.valueOf(this.mFaceWScale));
        contentValues.put(strArr[12], Float.valueOf(this.mFaceHScale));
        contentValues.put(strArr[13], Integer.valueOf(this.mRelationType));
        contentValues.put(strArr[14], this.mRelationText);
        contentValues.put(strArr[15], Integer.valueOf(this.mVisibilityType));
        contentValues.put(strArr[16], Integer.valueOf(this.mFaceCount));
        contentValues.put(strArr[17], Long.valueOf(this.mSize));
    }

    public static PeopleItem fromCursor(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            return null;
        }
        PeopleItem peopleItem = new PeopleItem();
        String[] strArr = PeopleCursorHelper.PROJECTION;
        peopleItem.mLocalId = Entity.getLong(cursor, strArr[0]);
        peopleItem.mName = Entity.getString(cursor, strArr[1]);
        peopleItem.mServerId = Entity.getString(cursor, strArr[2]);
        peopleItem.mCloudId = Entity.getLong(cursor, strArr[3]);
        peopleItem.mSha1 = Entity.getString(cursor, strArr[4]);
        peopleItem.mMicroThumbFile = Entity.getString(cursor, strArr[5]);
        peopleItem.mThumbFile = Entity.getString(cursor, strArr[6]);
        peopleItem.mLocalFile = Entity.getString(cursor, strArr[7]);
        peopleItem.mExifOrientation = Entity.getInt(cursor, strArr[8]);
        peopleItem.mFaceXScale = Entity.getFloat(cursor, strArr[9]);
        peopleItem.mFaceYScale = Entity.getFloat(cursor, strArr[10]);
        peopleItem.mFaceWScale = Entity.getFloat(cursor, strArr[11]);
        peopleItem.mFaceHScale = Entity.getFloat(cursor, strArr[12]);
        peopleItem.mRelationType = Entity.getInt(cursor, strArr[13]);
        peopleItem.mRelationText = Entity.getString(cursor, strArr[14]);
        peopleItem.mVisibilityType = Entity.getInt(cursor, strArr[15]);
        peopleItem.mFaceCount = Entity.getInt(cursor, strArr[16]);
        peopleItem.mSize = Entity.getLong(cursor, strArr[17]);
        return peopleItem;
    }
}
