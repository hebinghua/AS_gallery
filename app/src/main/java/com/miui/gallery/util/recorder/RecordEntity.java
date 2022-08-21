package com.miui.gallery.util.recorder;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class RecordEntity extends Entity {
    public int mReason;
    public long mTimestamp;

    public abstract void addColumns(ArrayList<TableColumn> arrayList);

    public abstract void onConvertToContentParams(ContentValues contentValues);

    public abstract void onInitParamsFromCursor(Cursor cursor);

    @Override // com.miui.gallery.dao.base.Entity
    public final List<TableColumn> getTableColumns() {
        ArrayList<TableColumn> arrayList = new ArrayList<>();
        Entity.addColumn(arrayList, "timestamp", "INTEGER");
        addColumns(arrayList);
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public final void onInitFromCursor(Cursor cursor) {
        this.mTimestamp = Entity.getLong(cursor, "timestamp");
        onInitParamsFromCursor(cursor);
    }

    @Override // com.miui.gallery.dao.base.Entity
    public final void onConvertToContents(ContentValues contentValues) {
        contentValues.put("timestamp", Long.valueOf(this.mTimestamp));
        onConvertToContentParams(contentValues);
    }
}
