package com.miui.gallery.provider.cloudmanager.remark.info;

import android.content.ContentValues;
import android.database.Cursor;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MediaRemarkEntity extends Entity {
    public long mCloudId;
    public String mContentValuesJson;
    public int mOperationType = 0;
    public int mMethodType = 0;

    @Override // com.miui.gallery.dao.base.Entity
    public final String getTableName() {
        return "MediaRemarkInfo";
    }

    @Override // com.miui.gallery.dao.base.Entity
    public final List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "mediaId", "INTEGER");
        Entity.addColumn(arrayList, "operationType", "INTEGER");
        Entity.addColumn(arrayList, "methodType", "INTEGER");
        Entity.addColumn(arrayList, "contentValues", "TEXT");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mCloudId = Entity.getLong(cursor, "mediaId");
        this.mOperationType = Entity.getInt(cursor, "operationType");
        this.mMethodType = Entity.getInt(cursor, "methodType");
        this.mContentValuesJson = Entity.getString(cursor, "contentValues");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public final void onConvertToContents(ContentValues contentValues) {
        contentValues.put("mediaId", Long.valueOf(this.mCloudId));
        contentValues.put("operationType", Integer.valueOf(this.mOperationType));
        contentValues.put("methodType", Integer.valueOf(this.mMethodType));
        contentValues.put("contentValues", this.mContentValuesJson);
    }

    public void setCloudId(long j) {
        this.mCloudId = j;
    }

    public void setMethod(int i) {
        this.mMethodType = i;
    }

    public void setOperationType(int i) {
        this.mOperationType = i;
    }

    public void setContentValues(String str) {
        this.mContentValuesJson = str;
    }

    public String toString() {
        return "MediaRemarkEntity{mCloudId=" + this.mCloudId + ", mOperationType=" + this.mOperationType + ", mMethodType=" + this.mMethodType + ", mContentValuesJson='" + this.mContentValuesJson + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
