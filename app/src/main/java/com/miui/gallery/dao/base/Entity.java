package com.miui.gallery.dao.base;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.TableColumn;
import com.xiaomi.stat.a.j;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Entity {
    public long mId = -1;

    public abstract List<TableColumn> getTableColumns();

    public abstract void onConvertToContents(ContentValues contentValues);

    public abstract void onInitFromCursor(Cursor cursor);

    public final long getRowId() {
        return this.mId;
    }

    public final void setRowId(long j) {
        this.mId = j;
    }

    public String getTableName() {
        return getClass().getSimpleName();
    }

    public String[] getUniqueConstraints() {
        return new String[]{j.c};
    }

    public void initFromCursor(Cursor cursor) {
        this.mId = cursor.getLong(cursor.getColumnIndex(j.c));
        onInitFromCursor(cursor);
    }

    public ContentValues convertToContents() {
        ContentValues contentValues = new ContentValues();
        onConvertToContents(contentValues);
        return contentValues;
    }

    public static long getLong(Cursor cursor, String str) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(str));
    }

    public static long getLongDefault(Cursor cursor, String str, long j) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex != -1 ? cursor.getLong(columnIndex) : j;
    }

    public static String getString(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndexOrThrow(str));
    }

    public static String getStringDefault(Cursor cursor, String str, String str2) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex != -1 ? cursor.getString(columnIndex) : str2;
    }

    public static int getInt(Cursor cursor, String str) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(str));
    }

    public static int getIntDefault(Cursor cursor, String str, int i) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex != -1 ? cursor.getInt(columnIndex) : i;
    }

    public static byte[] getBlob(Cursor cursor, String str) {
        return cursor.getBlob(cursor.getColumnIndexOrThrow(str));
    }

    public static float getFloat(Cursor cursor, String str) {
        return cursor.getFloat(cursor.getColumnIndexOrThrow(str));
    }

    public static float getFloatDefault(Cursor cursor, String str, float f) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex != -1 ? cursor.getFloat(columnIndex) : f;
    }

    public static double getDouble(Cursor cursor, String str) {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(str));
    }

    public static double getDoubleDefault(Cursor cursor, String str, double d) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex != -1 ? cursor.getDouble(columnIndex) : d;
    }

    public static void addColumn(List<TableColumn> list, String str, String str2) {
        list.add(new TableColumn.Builder().setName(str).setType(str2).build());
    }

    public static void addColumn(List<TableColumn> list, String str, String str2, String str3) {
        list.add(new TableColumn.Builder().setName(str).setType(str2).setDefaultValue(str3).build());
    }

    public static void addColumn(List<TableColumn> list, String str, String str2, boolean z) {
        list.add(new TableColumn.Builder().setName(str).setType(str2).setUnique(z).build());
    }
}
