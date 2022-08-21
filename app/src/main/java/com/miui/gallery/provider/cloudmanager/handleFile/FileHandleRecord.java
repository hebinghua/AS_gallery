package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.util.recorder.RecordEntity;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/* loaded from: classes2.dex */
public class FileHandleRecord extends RecordEntity {
    public String mFilePath;
    public String mFromPath;
    public int mHandleType;
    public String mTag;

    public void setFromPath(String str) {
        this.mFromPath = str;
    }

    public void setHandleType(int i) {
        this.mHandleType = i;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public void setTag(String str) {
        this.mTag = str;
    }

    public FileHandleRecord() {
        this.mTimestamp = System.currentTimeMillis();
    }

    @Override // com.miui.gallery.util.recorder.RecordEntity
    public void addColumns(ArrayList<TableColumn> arrayList) {
        Entity.addColumn(arrayList, "filePath", "TEXT");
        Entity.addColumn(arrayList, "fromPath", "TEXT");
        Entity.addColumn(arrayList, "handleType", "INTEGER");
        Entity.addColumn(arrayList, nexExportFormat.TAG_FORMAT_TAG, "TEXT");
    }

    @Override // com.miui.gallery.util.recorder.RecordEntity
    public void onInitParamsFromCursor(Cursor cursor) {
        this.mFilePath = Entity.getString(cursor, "filePath");
        this.mFromPath = Entity.getString(cursor, "fromPath");
        this.mHandleType = Entity.getInt(cursor, "handleType");
        this.mTag = Entity.getString(cursor, nexExportFormat.TAG_FORMAT_TAG);
    }

    @Override // com.miui.gallery.util.recorder.RecordEntity
    public void onConvertToContentParams(ContentValues contentValues) {
        contentValues.put("filePath", this.mFilePath);
        contentValues.put("fromPath", this.mFromPath);
        contentValues.put("handleType", Integer.valueOf(this.mHandleType));
        contentValues.put(nexExportFormat.TAG_FORMAT_TAG, this.mTag);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof FileHandleRecord)) {
            return false;
        }
        FileHandleRecord fileHandleRecord = (FileHandleRecord) obj;
        return this.mFromPath == fileHandleRecord.mFromPath && this.mHandleType == fileHandleRecord.mHandleType && TextUtils.equals(this.mFilePath, fileHandleRecord.mFilePath) && TextUtils.equals(this.mTag, fileHandleRecord.mTag);
    }

    public int hashCode() {
        return Objects.hash(this.mFromPath, this.mFilePath, Integer.valueOf(this.mHandleType), this.mTag);
    }

    public String toString() {
        return String.format(Locale.US, "FileHandleRecord [fromPath=%s, filePath=%s, tag=%s, handleType=%s]", this.mFromPath, this.mFilePath, this.mTag, Integer.valueOf(this.mHandleType));
    }
}
