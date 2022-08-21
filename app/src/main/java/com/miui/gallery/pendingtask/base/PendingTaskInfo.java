package com.miui.gallery.pendingtask.base;

import android.content.ContentValues;
import android.database.Cursor;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class PendingTaskInfo extends Entity {
    public long mCreateTime;
    public byte[] mData;
    public long mExpireTime;
    public long mMinLatency;
    public int mNetType = 0;
    public boolean mRequireCharging;
    public boolean mRequireDeviceIdle;
    public int mRetryTime;
    public String mTaskTag;
    public int mTaskType;

    public int getTaskType() {
        return this.mTaskType;
    }

    public void setTaskType(int i) {
        this.mTaskType = i;
    }

    public void setTaskTag(String str) {
        this.mTaskTag = str;
    }

    public int getNetType() {
        return this.mNetType;
    }

    public void setNetType(int i) {
        this.mNetType = i;
    }

    public void setRequireCharging(boolean z) {
        this.mRequireCharging = z;
    }

    public boolean isRequireCharging() {
        return this.mRequireCharging;
    }

    public boolean isRequireDeviceIdle() {
        return this.mRequireDeviceIdle;
    }

    public void setRequireDeviceIdle(boolean z) {
        this.mRequireDeviceIdle = z;
    }

    public byte[] getData() {
        return this.mData;
    }

    public void setData(byte[] bArr) {
        this.mData = bArr;
    }

    public long getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime(long j) {
        this.mCreateTime = j;
    }

    public long getMinLatencyMillis() {
        return this.mMinLatency;
    }

    public void setMinLatencyMillis(long j) {
        this.mMinLatency = j;
    }

    public long getExpireTime() {
        return this.mExpireTime;
    }

    public void setExpireTime(long j) {
        this.mExpireTime = j;
    }

    public int getRetryTime() {
        return this.mRetryTime;
    }

    public void increaseRetryTime() {
        this.mRetryTime++;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public final List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "taskType", "INTEGER");
        Entity.addColumn(arrayList, "taskTag", "TEXT");
        Entity.addColumn(arrayList, "netType", "INTEGER");
        Entity.addColumn(arrayList, "charging", "INTEGER");
        Entity.addColumn(arrayList, "deviceIdle", "INTEGER");
        Entity.addColumn(arrayList, "data", "BLOB");
        Entity.addColumn(arrayList, "createTime", "INTEGER");
        Entity.addColumn(arrayList, "expireTime", "INTEGER", String.valueOf(0));
        Entity.addColumn(arrayList, "retryTime", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public final void onInitFromCursor(Cursor cursor) {
        this.mTaskType = Entity.getInt(cursor, "taskType");
        this.mTaskTag = Entity.getString(cursor, "taskTag");
        this.mNetType = Entity.getInt(cursor, "netType");
        boolean z = false;
        this.mRequireCharging = Entity.getInt(cursor, "charging") == 1;
        if (Entity.getInt(cursor, "deviceIdle") == 1) {
            z = true;
        }
        this.mRequireDeviceIdle = z;
        this.mData = Entity.getBlob(cursor, "data");
        this.mCreateTime = Entity.getLong(cursor, "createTime");
        this.mExpireTime = Entity.getLong(cursor, "expireTime");
        this.mRetryTime = Entity.getInt(cursor, "retryTime");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public final void onConvertToContents(ContentValues contentValues) {
        contentValues.put("taskType", Integer.valueOf(this.mTaskType));
        contentValues.put("taskTag", this.mTaskTag);
        contentValues.put("netType", Integer.valueOf(this.mNetType));
        contentValues.put("charging", Integer.valueOf(this.mRequireCharging ? 1 : 0));
        contentValues.put("deviceIdle", Integer.valueOf(this.mRequireDeviceIdle ? 1 : 0));
        contentValues.put("data", this.mData);
        contentValues.put("createTime", Long.valueOf(this.mCreateTime));
        contentValues.put("expireTime", Long.valueOf(this.mExpireTime));
        contentValues.put("retryTime", Integer.valueOf(this.mRetryTime));
    }

    public String toString() {
        return "PendingTaskInfo{mTaskType=" + this.mTaskType + ", mTaskTag='" + this.mTaskTag + CoreConstants.SINGLE_QUOTE_CHAR + ", mNetType=" + this.mNetType + ", mRequireCharging=" + this.mRequireCharging + ", mRequireDeviceIdle=" + this.mRequireDeviceIdle + ", mCreateTime=" + this.mCreateTime + ", mMinLatency=" + this.mMinLatency + ", mExpireTime=" + this.mExpireTime + ", mRetryTime=" + this.mRetryTime + '}';
    }
}
