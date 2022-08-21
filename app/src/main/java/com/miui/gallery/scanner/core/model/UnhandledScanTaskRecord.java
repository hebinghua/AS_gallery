package com.miui.gallery.scanner.core.model;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class UnhandledScanTaskRecord extends Entity {
    public String mCallingPackageName;
    public long mCreateTime;
    public long mMediaStoreId;
    public String mOperatorPackageName;
    public int mParallelProcessState;
    public String mPath;
    public int mSceneCode;
    public boolean mUsingGaussian;

    public UnhandledScanTaskRecord() {
    }

    public UnhandledScanTaskRecord(String str, int i, long j, String str2, String str3, int i2, long j2, boolean z) {
        this.mPath = str;
        this.mSceneCode = i;
        this.mCreateTime = j;
        this.mCallingPackageName = str2;
        this.mOperatorPackageName = str3;
        this.mParallelProcessState = i2;
        this.mMediaStoreId = j2;
        this.mUsingGaussian = z;
    }

    public String getPath() {
        return this.mPath;
    }

    public int getSceneCode() {
        return this.mSceneCode;
    }

    public long getCreateTime() {
        return this.mCreateTime;
    }

    public String getCallingPackageName() {
        return this.mCallingPackageName;
    }

    public String getOperatorPackageName() {
        return this.mOperatorPackageName;
    }

    public int getParallelProcessState() {
        return this.mParallelProcessState;
    }

    public long getMediaStoreId() {
        return this.mMediaStoreId;
    }

    public boolean usingGaussian() {
        return this.mUsingGaussian;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "file_path", "TEXT");
        Entity.addColumn(arrayList, "scene_code", "INTEGER");
        Entity.addColumn(arrayList, "create_time", "INTEGER");
        Entity.addColumn(arrayList, "calling_package_name", "TEXT");
        Entity.addColumn(arrayList, "operator_package_name", "TEXT");
        Entity.addColumn(arrayList, "parallel_process_state", "INTEGER");
        Entity.addColumn(arrayList, "media_store_id", "INTEGER");
        Entity.addColumn(arrayList, "using_gaussian", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mPath = Entity.getString(cursor, "file_path");
        this.mSceneCode = Entity.getInt(cursor, "scene_code");
        this.mCreateTime = Entity.getLong(cursor, "create_time");
        this.mCallingPackageName = Entity.getString(cursor, "calling_package_name");
        this.mOperatorPackageName = Entity.getString(cursor, "operator_package_name");
        boolean z = false;
        this.mParallelProcessState = Entity.getIntDefault(cursor, "parallel_process_state", 0);
        this.mMediaStoreId = Entity.getLongDefault(cursor, "media_store_id", -1L);
        if (1 == Entity.getIntDefault(cursor, "using_gaussian", 0)) {
            z = true;
        }
        this.mUsingGaussian = z;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("file_path", this.mPath);
        contentValues.put("scene_code", Integer.valueOf(this.mSceneCode));
        contentValues.put("create_time", Long.valueOf(this.mCreateTime));
        contentValues.put("calling_package_name", this.mCallingPackageName);
        contentValues.put("operator_package_name", this.mOperatorPackageName);
        contentValues.put("parallel_process_state", Integer.valueOf(this.mParallelProcessState));
        contentValues.put("media_store_id", Long.valueOf(this.mMediaStoreId));
        contentValues.put("using_gaussian", Integer.valueOf(this.mUsingGaussian ? 1 : 0));
    }
}
