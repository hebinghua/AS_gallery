package com.miui.gallery.assistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PeopleEvent extends Entity {
    public float mEndTime;
    public int mEventType;
    public long mMediaId;
    public int mMediaType;
    public int mPeopleCount;
    public String mPeopleList;
    public float mStartTime;
    public int mVersion = 0;

    public PeopleEvent() {
    }

    public PeopleEvent(long j, int i, int i2) {
        this.mMediaId = j;
        this.mMediaType = i;
        this.mEventType = i2;
    }

    public long getMediaId() {
        return this.mMediaId;
    }

    public String getPeopleList() {
        return this.mPeopleList;
    }

    public void setPeopleList(String str) {
        this.mPeopleList = str;
    }

    public void setStartTime(float f) {
        this.mStartTime = f;
    }

    public void setEndTime(float f) {
        this.mEndTime = f;
    }

    public void setPeopleCount(int i) {
        this.mPeopleCount = i;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "mediaId", "INTEGER");
        Entity.addColumn(arrayList, "mediaType", "INTEGER");
        Entity.addColumn(arrayList, "eventType", "INTEGER");
        Entity.addColumn(arrayList, "peopleList", "TEXT");
        Entity.addColumn(arrayList, "peopleCount", "INTEGER");
        Entity.addColumn(arrayList, "startTime", "REAL");
        Entity.addColumn(arrayList, "endTime", "REAL");
        Entity.addColumn(arrayList, "version", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mMediaId = Entity.getLong(cursor, "mediaId");
        this.mMediaType = Entity.getInt(cursor, "mediaType");
        this.mEventType = Entity.getIntDefault(cursor, "eventType", -1);
        this.mPeopleList = Entity.getStringDefault(cursor, "peopleList", "");
        this.mPeopleCount = Entity.getIntDefault(cursor, "peopleCount", 0);
        this.mStartTime = Entity.getFloatDefault(cursor, "startTime", -1.0f);
        this.mEndTime = Entity.getFloatDefault(cursor, "endTime", -1.0f);
        this.mVersion = Entity.getInt(cursor, "version");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("mediaId", Long.valueOf(this.mMediaId));
        contentValues.put("mediaType", Integer.valueOf(this.mMediaType));
        contentValues.put("eventType", Integer.valueOf(this.mEventType));
        contentValues.put("peopleList", this.mPeopleList);
        contentValues.put("peopleCount", Integer.valueOf(this.mPeopleCount));
        contentValues.put("startTime", Float.valueOf(this.mStartTime));
        contentValues.put("endTime", Float.valueOf(this.mEndTime));
        contentValues.put("version", Integer.valueOf(this.mVersion));
    }
}
