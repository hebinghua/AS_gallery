package com.miui.gallery.assistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.card.scenario.SceneTagQuery;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MediaScene extends Entity implements Parcelable {
    public static final Parcelable.Creator<MediaScene> CREATOR = new Parcelable.Creator<MediaScene>() { // from class: com.miui.gallery.assistant.model.MediaScene.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public MediaScene mo565createFromParcel(Parcel parcel) {
            return new MediaScene(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public MediaScene[] mo566newArray(int i) {
            return new MediaScene[i];
        }
    };
    public float mConfidence;
    public double mEndTime;
    public long mFileSize;
    public boolean mIsQuickResult;
    public float mLeftTopX;
    public float mLeftTopY;
    public long mMediaId;
    public int mMediaType;
    public String mPath;
    public String mPointPosition;
    public float mRightBottomX;
    public float mRightBottomY;
    public int mSceneTag;
    public double mStartTime;
    public int mSummarizedTag;
    public int mVersion;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MediaScene() {
        this.mVersion = 1;
    }

    public MediaScene(long j, int i, String str, long j2) {
        this.mMediaId = j;
        this.mMediaType = i;
        this.mPath = str;
        this.mFileSize = j2;
        this.mVersion = 1;
    }

    public String getPath() {
        return this.mPath;
    }

    public long getFileSize() {
        return this.mFileSize;
    }

    public long getMediaId() {
        return this.mMediaId;
    }

    public double getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(double d) {
        this.mStartTime = d;
    }

    public double getEndTime() {
        return this.mEndTime;
    }

    public void setEndTime(double d) {
        this.mEndTime = d;
    }

    public float getConfidence() {
        return this.mConfidence;
    }

    public void setConfidence(float f) {
        this.mConfidence = f;
    }

    public int getSceneTag() {
        return this.mSceneTag;
    }

    public void setSceneTag(int i) {
        this.mSceneTag = i;
    }

    public boolean isDocument() {
        List<Integer> certificateTags = SceneTagQuery.getInstance().getCertificateTags();
        if (BaseMiscUtil.isValid(certificateTags)) {
            return certificateTags.contains(Integer.valueOf(this.mSceneTag));
        }
        return false;
    }

    public void setIsQuickResult(boolean z) {
        this.mIsQuickResult = z;
    }

    public void setHeatMapPos(float f, float f2, float f3, float f4) {
        this.mLeftTopX = f;
        this.mLeftTopY = f2;
        this.mRightBottomX = f3;
        this.mRightBottomY = f4;
    }

    public static JSONObject getPointPositionJSONObj(float f, float f2, float f3, float f4) {
        try {
            return GsonUtils.toObject(new PointPosition(f, f2, f3, f4));
        } catch (JSONException e) {
            DefaultLogger.e("MediaScene", "error generate point pos", e);
            return null;
        }
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

    public String getPointPosition() {
        return this.mPointPosition;
    }

    public void setPointPosition(String str) {
        this.mPointPosition = str;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "mediaId", "INTEGER");
        Entity.addColumn(arrayList, "mediaType", "INTEGER");
        Entity.addColumn(arrayList, "sceneTag", "INTEGER");
        Entity.addColumn(arrayList, "startTime", "REAL");
        Entity.addColumn(arrayList, "endTime", "REAL");
        Entity.addColumn(arrayList, "confidence", "REAL");
        Entity.addColumn(arrayList, "fileSize", "INTEGER");
        Entity.addColumn(arrayList, "mediaPath", "TEXT");
        Entity.addColumn(arrayList, "version", "INTEGER");
        Entity.addColumn(arrayList, "isQuickResult", "INTEGER");
        Entity.addColumn(arrayList, "leftTopX", "REAL");
        Entity.addColumn(arrayList, "leftTopY", "REAL");
        Entity.addColumn(arrayList, "rightBottomX", "REAL");
        Entity.addColumn(arrayList, "rightBottomY", "REAL");
        Entity.addColumn(arrayList, "point_position", "TEXT");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mMediaId = Entity.getLong(cursor, "mediaId");
        this.mMediaType = Entity.getInt(cursor, "mediaType");
        this.mSceneTag = Entity.getIntDefault(cursor, "sceneTag", -1);
        this.mStartTime = Entity.getFloat(cursor, "startTime");
        this.mEndTime = Entity.getFloat(cursor, "endTime");
        this.mConfidence = Entity.getFloat(cursor, "confidence");
        this.mPath = Entity.getString(cursor, "mediaPath");
        this.mFileSize = Entity.getLong(cursor, "fileSize");
        this.mVersion = Entity.getInt(cursor, "version");
        boolean z = true;
        if (Entity.getInt(cursor, "isQuickResult") != 1) {
            z = false;
        }
        this.mIsQuickResult = z;
        this.mLeftTopX = Entity.getFloat(cursor, "leftTopX");
        this.mLeftTopY = Entity.getFloat(cursor, "leftTopY");
        this.mRightBottomX = Entity.getFloat(cursor, "rightBottomX");
        this.mRightBottomY = Entity.getFloat(cursor, "rightBottomY");
        this.mPointPosition = Entity.getString(cursor, "point_position");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("mediaId", Long.valueOf(this.mMediaId));
        contentValues.put("mediaType", Integer.valueOf(this.mMediaType));
        contentValues.put("sceneTag", Integer.valueOf(this.mSceneTag));
        contentValues.put("startTime", Double.valueOf(this.mStartTime));
        contentValues.put("endTime", Double.valueOf(this.mEndTime));
        contentValues.put("confidence", Float.valueOf(this.mConfidence));
        contentValues.put("mediaPath", this.mPath);
        contentValues.put("fileSize", Long.valueOf(this.mFileSize));
        contentValues.put("version", Integer.valueOf(this.mVersion));
        contentValues.put("isQuickResult", Integer.valueOf(this.mIsQuickResult ? 1 : 0));
        contentValues.put("leftTopX", Float.valueOf(this.mLeftTopX));
        contentValues.put("leftTopY", Float.valueOf(this.mLeftTopY));
        contentValues.put("rightBottomX", Float.valueOf(this.mRightBottomX));
        contentValues.put("rightBottomY", Float.valueOf(this.mRightBottomY));
        contentValues.put("point_position", this.mPointPosition);
    }

    /* loaded from: classes.dex */
    public static class PointPosition {
        public float mPointLeftTopX;
        public float mPointLeftTopY;
        public float mPointRightBottomX;
        public float mPointRightBottomY;

        public PointPosition(float f, float f2, float f3, float f4) {
            this.mPointLeftTopX = f;
            this.mPointLeftTopY = f2;
            this.mPointRightBottomX = f3;
            this.mPointRightBottomY = f4;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mMediaId);
        parcel.writeInt(this.mMediaType);
        parcel.writeInt(this.mSceneTag);
        parcel.writeFloat(this.mConfidence);
        parcel.writeDouble(this.mStartTime);
        parcel.writeDouble(this.mEndTime);
        parcel.writeInt(this.mVersion);
        parcel.writeInt(this.mSummarizedTag);
        parcel.writeString(this.mPath);
        parcel.writeLong(this.mFileSize);
        parcel.writeByte(this.mIsQuickResult ? (byte) 1 : (byte) 0);
    }

    public MediaScene(Parcel parcel) {
        this.mMediaId = parcel.readLong();
        this.mMediaType = parcel.readInt();
        this.mSceneTag = parcel.readInt();
        this.mConfidence = parcel.readFloat();
        this.mStartTime = parcel.readDouble();
        this.mEndTime = parcel.readDouble();
        this.mVersion = parcel.readInt();
        this.mSummarizedTag = parcel.readInt();
        this.mPath = parcel.readString();
        this.mFileSize = parcel.readLong();
        this.mIsQuickResult = parcel.readByte() != 0;
    }
}
