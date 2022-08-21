package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.SQLiteView;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.xiaomi.stat.a.j;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class GalleryDBUpdater96 extends GalleryDBUpdater {
    /* renamed from: $r8$lambda$SIdNVb_75Tnx8sR8e1Xb45F-HAM */
    public static /* synthetic */ List m1249$r8$lambda$SIdNVb_75Tnx8sR8e1Xb45FHAM(HashMap hashMap, Cursor cursor) {
        return lambda$doUpdate$1(hashMap, cursor);
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (updateResult.isRecreateTableFavorite()) {
            return UpdateResult.defaultResult();
        }
        GalleryDBHelper.addColumn(supportSQLiteDatabase, "favorites", new TableColumn.Builder().setName("cloud_id").setType("INTEGER").build());
        final HashMap hashMap = (HashMap) SafeDBUtil.safeQuery(supportSQLiteDatabase, "extended_cloud", new String[]{"sha1", j.c}, "isFavorite = 1", null, null, null, GalleryDBUpdater96$$ExternalSyntheticLambda1.INSTANCE);
        List list = (List) SafeDBUtil.safeQuery(supportSQLiteDatabase, "favorites", new String[]{"sha1", "source", "dateFavorite"}, "isFavorite = 1", (String[]) null, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater96$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public final Object mo1808handle(Cursor cursor) {
                return GalleryDBUpdater96.m1249$r8$lambda$SIdNVb_75Tnx8sR8e1Xb45FHAM(hashMap, cursor);
            }
        });
        supportSQLiteDatabase.execSQL("delete from favorites");
        SQLiteView.of("extended_cloud").createByVersion(supportSQLiteDatabase, 5);
        if (list == null || list.size() <= 0) {
            return UpdateResult.defaultResult();
        }
        supportSQLiteDatabase.beginTransaction();
        try {
            SupportSQLiteStatement compileStatement = supportSQLiteDatabase.compileStatement("insert into favorites (isFavorite, dateFavorite, source, cloud_id, sha1) values (?, ?, ?, ?, ?)");
            for (int i = 0; i < list.size(); i++) {
                compileStatement.bindString(1, ((ContentValues) list.get(i)).getAsString("isFavorite"));
                compileStatement.bindString(2, ((ContentValues) list.get(i)).getAsString("dateFavorite"));
                compileStatement.bindString(3, ((ContentValues) list.get(i)).getAsString("source"));
                compileStatement.bindString(4, ((ContentValues) list.get(i)).getAsString("cloud_id"));
                String asString = ((ContentValues) list.get(i)).getAsString("sha1");
                if (asString != null) {
                    compileStatement.bindString(5, asString);
                }
                compileStatement.execute();
                compileStatement.clearBindings();
            }
            supportSQLiteDatabase.setTransactionSuccessful();
            return UpdateResult.defaultResult();
        } finally {
            if (supportSQLiteDatabase.inTransaction()) {
                supportSQLiteDatabase.endTransaction();
            }
        }
    }

    public static /* synthetic */ HashMap lambda$doUpdate$0(Cursor cursor) {
        HashMap hashMap = new HashMap();
        while (cursor != null && cursor.moveToNext()) {
            String string = cursor.getString(0);
            Long valueOf = Long.valueOf(cursor.getLong(1));
            List list = (List) hashMap.get(string);
            if (list == null) {
                list = new LinkedList();
                hashMap.put(string, list);
            }
            list.add(valueOf);
        }
        return hashMap;
    }

    public static /* synthetic */ List lambda$doUpdate$1(HashMap hashMap, Cursor cursor) {
        LinkedList linkedList = new LinkedList();
        while (cursor != null && cursor.moveToNext()) {
            boolean z = false;
            String string = cursor.getString(0);
            Integer valueOf = Integer.valueOf(cursor.getInt(1));
            Long valueOf2 = Long.valueOf(cursor.getLong(2));
            List<Long> list = (List) hashMap.get(string);
            if (BaseMiscUtil.isValid(list)) {
                for (Long l : list) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isFavorite", (Integer) 1);
                    if (!z) {
                        contentValues.put("sha1", string);
                        z = true;
                    }
                    contentValues.put("dateFavorite", valueOf2);
                    contentValues.put("source", valueOf);
                    contentValues.put("cloud_id", l);
                    linkedList.add(contentValues);
                }
            }
        }
        return linkedList;
    }
}
