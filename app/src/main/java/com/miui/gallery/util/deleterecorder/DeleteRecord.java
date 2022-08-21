package com.miui.gallery.util.deleterecorder;

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
public class DeleteRecord extends RecordEntity {
    public String mFilePath;
    public String mTag;

    public DeleteRecord() {
    }

    public DeleteRecord(int i, String str, String str2) {
        this(System.currentTimeMillis(), i, str, str2);
    }

    public DeleteRecord(long j, int i, String str, String str2) {
        this.mTimestamp = j;
        this.mReason = i;
        this.mFilePath = str;
        this.mTag = str2;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    @Override // com.miui.gallery.util.recorder.RecordEntity
    public void addColumns(ArrayList<TableColumn> arrayList) {
        Entity.addColumn(arrayList, "reason", "INTEGER");
        Entity.addColumn(arrayList, "filePath", "TEXT");
        Entity.addColumn(arrayList, nexExportFormat.TAG_FORMAT_TAG, "TEXT");
    }

    @Override // com.miui.gallery.util.recorder.RecordEntity
    public void onInitParamsFromCursor(Cursor cursor) {
        this.mReason = Entity.getInt(cursor, "reason");
        this.mFilePath = Entity.getString(cursor, "filePath");
        this.mTag = Entity.getString(cursor, nexExportFormat.TAG_FORMAT_TAG);
    }

    @Override // com.miui.gallery.util.recorder.RecordEntity
    public void onConvertToContentParams(ContentValues contentValues) {
        contentValues.put("reason", Integer.valueOf(this.mReason));
        contentValues.put("filePath", this.mFilePath);
        contentValues.put(nexExportFormat.TAG_FORMAT_TAG, this.mTag);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DeleteRecord)) {
            return false;
        }
        DeleteRecord deleteRecord = (DeleteRecord) obj;
        if (this.mTimestamp != deleteRecord.mTimestamp || this.mReason != deleteRecord.mReason || !TextUtils.equals(this.mFilePath, deleteRecord.mFilePath)) {
            return false;
        }
        return TextUtils.equals(this.mTag, deleteRecord.mTag);
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mTimestamp), Integer.valueOf(this.mReason), this.mFilePath, this.mTag);
    }

    public String toString() {
        return String.format(Locale.US, "DeleteRecord [timestamp=%d, reason=%d, filePath=%s, tag=%s]", Long.valueOf(this.mTimestamp), Integer.valueOf(this.mReason), this.mFilePath, this.mTag);
    }
}
