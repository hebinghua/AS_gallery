package com.miui.gallery.assistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.util.GsonUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FaceInfo extends Entity {
    public int mAge;
    public float[] mAngle;
    public float mConfidence;
    public float mEndTime;
    public long mFaceId;
    public long mGroupId;
    public int mKeyFaceFrameIndex;
    public float[] mKeyPoint;
    public float mLeftTopX;
    public float mLeftTopY;
    public long mMediaId;
    public int mMediaType;
    public float mRightBottomX;
    public float mRightBottomY;
    public int mSmile;
    public float mStartTime;
    public int mVersion = 1;

    public FaceInfo() {
    }

    public FaceInfo(long j, long j2, int i) {
        this.mFaceId = j2;
        this.mMediaId = j;
        this.mMediaType = i;
    }

    public long getFaceId() {
        return this.mFaceId;
    }

    public void setStartTime(float f) {
        this.mStartTime = f;
    }

    public void setEndTime(float f) {
        this.mEndTime = f;
    }

    public void setKeyFaceFrameIndex(int i) {
        this.mKeyFaceFrameIndex = i;
    }

    public void setKeyPoint(float[] fArr) {
        this.mKeyPoint = fArr;
    }

    public void setAangle(float[] fArr) {
        this.mAngle = fArr;
    }

    public void setConfidence(float f) {
        this.mConfidence = f;
    }

    public void setAge(int i) {
        this.mAge = i;
    }

    public void setSmile(int i) {
        this.mSmile = i;
    }

    public void setFacePos(float f, float f2, float f3, float f4) {
        this.mLeftTopX = f;
        this.mLeftTopY = f2;
        this.mRightBottomX = f3;
        this.mRightBottomY = f4;
    }

    public float getLeftTopX() {
        return this.mLeftTopX;
    }

    public float getLeftTopY() {
        return this.mLeftTopY;
    }

    public float getRightBottomX() {
        return this.mRightBottomX;
    }

    public float getRightBottomY() {
        return this.mRightBottomY;
    }

    public void setGroupId(long j) {
        this.mGroupId = j;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "faceId", "INTEGER");
        Entity.addColumn(arrayList, "mediaId", "INTEGER");
        Entity.addColumn(arrayList, "mediaType", "INTEGER");
        Entity.addColumn(arrayList, "startTime", "REAL");
        Entity.addColumn(arrayList, "endTime", "REAL");
        Entity.addColumn(arrayList, "keyFaceFrame", "INTEGER");
        Entity.addColumn(arrayList, "keyPoint", "TEXT");
        Entity.addColumn(arrayList, "angle", "TEXT");
        Entity.addColumn(arrayList, "confidence", "REAL");
        Entity.addColumn(arrayList, "age", "INTEGER");
        Entity.addColumn(arrayList, "smile", "INTEGER");
        Entity.addColumn(arrayList, "leftTopX", "REAL");
        Entity.addColumn(arrayList, "leftTopY", "REAL");
        Entity.addColumn(arrayList, "rightBottomX", "REAL");
        Entity.addColumn(arrayList, "rightBottomY", "REAL");
        Entity.addColumn(arrayList, "groupId", "INTEGER");
        Entity.addColumn(arrayList, "version", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mFaceId = Entity.getLong(cursor, "faceId");
        this.mMediaId = Entity.getLong(cursor, "mediaId");
        this.mMediaType = Entity.getInt(cursor, "mediaType");
        this.mStartTime = Entity.getFloat(cursor, "startTime");
        this.mEndTime = Entity.getFloat(cursor, "endTime");
        this.mKeyFaceFrameIndex = Entity.getIntDefault(cursor, "keyFaceFrame", -1);
        this.mKeyPoint = (float[]) GsonUtils.fromJson(Entity.getStringDefault(cursor, "keyPoint", ""), (Class<Object>) float[].class);
        this.mAngle = (float[]) GsonUtils.fromJson(Entity.getStringDefault(cursor, "angle", ""), (Class<Object>) float[].class);
        this.mConfidence = Entity.getFloat(cursor, "confidence");
        this.mAge = Entity.getInt(cursor, "age");
        this.mSmile = Entity.getIntDefault(cursor, "smile", -1);
        this.mLeftTopX = Entity.getFloat(cursor, "leftTopX");
        this.mLeftTopY = Entity.getFloat(cursor, "leftTopY");
        this.mRightBottomX = Entity.getFloat(cursor, "rightBottomX");
        this.mRightBottomY = Entity.getFloat(cursor, "rightBottomY");
        this.mGroupId = Entity.getLong(cursor, "groupId");
        this.mVersion = Entity.getInt(cursor, "version");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("faceId", Long.valueOf(this.mFaceId));
        contentValues.put("mediaId", Long.valueOf(this.mMediaId));
        contentValues.put("mediaType", Integer.valueOf(this.mMediaType));
        contentValues.put("startTime", Float.valueOf(this.mStartTime));
        contentValues.put("endTime", Float.valueOf(this.mEndTime));
        contentValues.put("keyFaceFrame", Integer.valueOf(this.mKeyFaceFrameIndex));
        contentValues.put("keyPoint", GsonUtils.toString(this.mKeyPoint));
        contentValues.put("angle", GsonUtils.toString(this.mAngle));
        contentValues.put("confidence", Float.valueOf(this.mConfidence));
        contentValues.put("age", Integer.valueOf(this.mAge));
        contentValues.put("smile", Integer.valueOf(this.mSmile));
        contentValues.put("leftTopX", Float.valueOf(this.mLeftTopX));
        contentValues.put("leftTopY", Float.valueOf(this.mLeftTopY));
        contentValues.put("rightBottomX", Float.valueOf(this.mRightBottomX));
        contentValues.put("rightBottomY", Float.valueOf(this.mRightBottomY));
        contentValues.put("groupId", Long.valueOf(this.mGroupId));
        contentValues.put("version", Integer.valueOf(this.mVersion));
    }
}
