package com.miui.gallery.dao.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.db.sqlite3.GallerySQLiteOpenHelperFactory;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class EntityManager {
    public final SupportSQLiteOpenHelper mDbHelper;
    public ArrayList<Class<? extends Entity>> mTables = null;

    public abstract int getTablesCount();

    public abstract void onDatabaseDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2);

    public abstract void onDatabaseUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2);

    public abstract void onInitTableList();

    public EntityManager(Context context, String str, int i) {
        this.mDbHelper = new GallerySQLiteOpenHelperFactory().create(SupportSQLiteOpenHelper.Configuration.builder(context).name(context.getDatabasePath(str).getPath()).callback(new SQLiteOpenHelperCallback(i)).build());
    }

    public EntityTransaction getTransaction() {
        return new EntityTransaction(this.mDbHelper);
    }

    public long insert(Entity entity) {
        long j;
        if (entity == null) {
            return -1L;
        }
        try {
            j = this.mDbHelper.getWritableDatabase().insert(entity.getTableName(), 0, entity.convertToContents());
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "insert error. %s", e);
            j = -1;
        }
        if (j != -1) {
            entity.setRowId(j);
        }
        return j;
    }

    public int insert(Collection<? extends Entity> collection) {
        if (!BaseMiscUtil.isValid(collection)) {
            return 0;
        }
        SupportSQLiteDatabase writableDatabase = this.mDbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            try {
                int i = 0;
                for (Entity entity : collection) {
                    long insert = writableDatabase.insert(entity.getTableName(), 0, entity.convertToContents());
                    if (insert != -1) {
                        entity.setRowId(insert);
                        i++;
                    }
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                return i;
            } catch (Exception e) {
                DefaultLogger.e("EntityManager", "insert error %s", e);
                writableDatabase.endTransaction();
                return 0;
            }
        } catch (Throwable th) {
            writableDatabase.endTransaction();
            throw th;
        }
    }

    public boolean insertWithOnConflict(Entity entity, int i) {
        long j;
        if (entity == null) {
            return false;
        }
        try {
            j = this.mDbHelper.getWritableDatabase().insert(entity.getTableName(), i, entity.convertToContents());
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "insert error %s", e);
            j = -1;
        }
        int i2 = (j > (-1L) ? 1 : (j == (-1L) ? 0 : -1));
        if (i2 != 0) {
            entity.setRowId(j);
        }
        return i2 != 0;
    }

    public int update(Entity entity) {
        if (entity == null || entity.getRowId() < 0) {
            return 0;
        }
        try {
            return this.mDbHelper.getWritableDatabase().update(entity.getTableName(), 0, entity.convertToContents(), "_id=?", new String[]{String.valueOf(entity.getRowId())});
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "update error.\n", e);
            return 0;
        }
    }

    public int update(Class<? extends Entity> cls, ContentValues contentValues, String str, String[] strArr) {
        String tableName = getTableName(cls);
        if (tableName == null) {
            return 0;
        }
        try {
            return this.mDbHelper.getWritableDatabase().update(tableName, 0, contentValues, str, strArr);
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "update error.\n", e);
            return 0;
        }
    }

    public boolean delete(Entity entity) {
        int i;
        if (entity == null || entity.getRowId() < 0) {
            return false;
        }
        try {
            i = this.mDbHelper.getWritableDatabase().delete(entity.getTableName(), "_id=?", new String[]{String.valueOf(entity.getRowId())});
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "delete error.\n", e);
            i = 0;
        }
        return i > 0;
    }

    public boolean delete(Class<? extends Entity> cls, String str, String[] strArr) {
        return deleteCount(cls, str, strArr) > 0;
    }

    public int deleteCount(Class<? extends Entity> cls, String str, String[] strArr) {
        String tableName = getTableName(cls);
        if (tableName == null) {
            return 0;
        }
        try {
            return this.mDbHelper.getWritableDatabase().delete(tableName, str, strArr);
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "delete error.\n", e);
            return 0;
        }
    }

    public boolean deleteAll(Class<? extends Entity> cls) {
        return delete(cls, null, null);
    }

    public <T extends Entity> T find(Class<T> cls, long j) {
        return (T) find(cls, "_id=?", new String[]{String.valueOf(j)});
    }

    public <T extends Entity> T find(Class<T> cls, String... strArr) {
        Entity entity = (Entity) newInstance(cls);
        if (entity == null) {
            return null;
        }
        String tableName = entity.getTableName();
        String[] uniqueConstraints = entity.getUniqueConstraints();
        String constraintsSelection = getConstraintsSelection(uniqueConstraints);
        if (TextUtils.isEmpty(constraintsSelection) || strArr.length != uniqueConstraints.length) {
            throw new RuntimeException(String.format("uniques should match %s.getUniqueConstraints()", tableName));
        }
        return (T) find(cls, constraintsSelection, strArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T extends com.miui.gallery.dao.base.Entity> T find(java.lang.Class<T> r4, java.lang.String r5, java.lang.String[] r6) {
        /*
            r3 = this;
            java.lang.Object r4 = newInstance(r4)
            com.miui.gallery.dao.base.Entity r4 = (com.miui.gallery.dao.base.Entity) r4
            r0 = 0
            if (r4 != 0) goto La
            return r0
        La:
            java.lang.String r1 = r4.getTableName()
            androidx.sqlite.db.SupportSQLiteOpenHelper r2 = r3.mDbHelper     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            androidx.sqlite.db.SupportSQLiteDatabase r2 = r2.getReadableDatabase()     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            androidx.sqlite.db.SupportSQLiteQueryBuilder r1 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r1)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            androidx.sqlite.db.SupportSQLiteQueryBuilder r5 = r1.selection(r5, r6)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            androidx.sqlite.db.SupportSQLiteQuery r5 = r5.create()     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            android.database.Cursor r5 = r2.query(r5)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            if (r5 == 0) goto L32
            boolean r6 = r5.moveToFirst()     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L4b
            if (r6 == 0) goto L32
            r4.initFromCursor(r5)     // Catch: java.lang.Exception -> L30 java.lang.Throwable -> L4b
            goto L33
        L30:
            r4 = move-exception
            goto L3e
        L32:
            r4 = r0
        L33:
            if (r5 == 0) goto L38
            r5.close()
        L38:
            r0 = r4
            goto L4a
        L3a:
            r4 = move-exception
            goto L4d
        L3c:
            r4 = move-exception
            r5 = r0
        L3e:
            java.lang.String r6 = "EntityManager"
            java.lang.String r1 = "find error.\n"
            com.miui.gallery.util.logger.DefaultLogger.e(r6, r1, r4)     // Catch: java.lang.Throwable -> L4b
            if (r5 == 0) goto L4a
            r5.close()
        L4a:
            return r0
        L4b:
            r4 = move-exception
            r0 = r5
        L4d:
            if (r0 == 0) goto L52
            r0.close()
        L52:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.dao.base.EntityManager.find(java.lang.Class, java.lang.String, java.lang.String[]):com.miui.gallery.dao.base.Entity");
    }

    public <T extends Entity> List<T> query(Class<T> cls, String str, String[] strArr) {
        return query(cls, null, str, strArr, null, null, null, null);
    }

    public <T extends Entity> List<T> query(Class<T> cls, String str, String[] strArr, String str2, String str3) {
        return query(cls, null, str, strArr, null, null, str2, str3);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0069, code lost:
        if (r1 == null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x006c, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T extends com.miui.gallery.dao.base.Entity> java.util.List<T> query(java.lang.Class<T> r5, java.lang.String[] r6, java.lang.String r7, java.lang.String[] r8, java.lang.String r9, java.lang.String r10, java.lang.String r11, java.lang.String r12) {
        /*
            r4 = this;
            java.lang.String r0 = getTableName(r5)
            r1 = 0
            if (r0 != 0) goto L8
            return r1
        L8:
            java.util.List r2 = java.util.Collections.EMPTY_LIST
            androidx.sqlite.db.SupportSQLiteOpenHelper r3 = r4.mDbHelper     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteDatabase r3 = r3.getReadableDatabase()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r0)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQueryBuilder r6 = r0.columns(r6)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQueryBuilder r6 = r6.selection(r7, r8)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQueryBuilder r6 = r6.groupBy(r9)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQueryBuilder r6 = r6.having(r10)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQueryBuilder r6 = r6.orderBy(r11)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQueryBuilder r6 = r6.limit(r12)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            androidx.sqlite.db.SupportSQLiteQuery r6 = r6.create()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            android.database.Cursor r1 = r3.query(r6)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            if (r1 == 0) goto L59
            boolean r6 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            if (r6 == 0) goto L59
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            int r7 = r1.getCount()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            r6.<init>(r7)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
        L45:
            com.miui.gallery.dao.base.Entity r7 = r4.cursorToEntity(r5, r1)     // Catch: java.lang.Exception -> L56 java.lang.Throwable -> L5f
            if (r7 == 0) goto L4e
            r6.add(r7)     // Catch: java.lang.Exception -> L56 java.lang.Throwable -> L5f
        L4e:
            boolean r7 = r1.moveToNext()     // Catch: java.lang.Exception -> L56 java.lang.Throwable -> L5f
            if (r7 != 0) goto L45
            r2 = r6
            goto L59
        L56:
            r5 = move-exception
            r2 = r6
            goto L62
        L59:
            if (r1 == 0) goto L6c
        L5b:
            r1.close()
            goto L6c
        L5f:
            r5 = move-exception
            goto L6d
        L61:
            r5 = move-exception
        L62:
            java.lang.String r6 = "EntityManager"
            java.lang.String r7 = "query error.\n"
            com.miui.gallery.util.logger.DefaultLogger.e(r6, r7, r5)     // Catch: java.lang.Throwable -> L5f
            if (r1 == 0) goto L6c
            goto L5b
        L6c:
            return r2
        L6d:
            if (r1 == 0) goto L72
            r1.close()
        L72:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.dao.base.EntityManager.query(java.lang.Class, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    public boolean execSQL(String str) {
        try {
            this.mDbHelper.getWritableDatabase().execSQL(str);
            return true;
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "execSQL error.\n", e);
            return false;
        }
    }

    public Cursor rawQuery(String str) {
        try {
            return this.mDbHelper.getReadableDatabase().query(str);
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "query error.\n", e);
            return null;
        }
    }

    public Cursor rawQuery(Class<? extends Entity> cls, String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, String str5) {
        String tableName = getTableName(cls);
        if (tableName == null) {
            return null;
        }
        try {
            return this.mDbHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder(tableName).columns(strArr).selection(str, strArr2).groupBy(str2).having(str3).orderBy(str4).limit(str5).create());
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "query error.\n", e);
            return null;
        }
    }

    public static String getTableName(Class<? extends Entity> cls) {
        Entity entity = (Entity) newInstance(cls);
        if (entity != null) {
            return entity.getTableName();
        }
        return null;
    }

    public final <T extends Entity> T cursorToEntity(Class<T> cls, Cursor cursor) {
        T t = (T) newInstance(cls);
        if (t != null) {
            t.initFromCursor(cursor);
        }
        return t;
    }

    public static String getConstraintsSelection(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            stringBuffer.append(strArr[i]);
            if (i == strArr.length - 1) {
                stringBuffer.append("=?");
            } else {
                stringBuffer.append("=? AND ");
            }
        }
        return stringBuffer.toString();
    }

    public static <T> T newInstance(Class<T> cls) {
        if (cls == null) {
            return null;
        }
        try {
            Constructor<T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance(new Object[0]);
        } catch (Exception e) {
            DefaultLogger.e("EntityManager", "newInstance error.\n", e);
            return null;
        }
    }

    public static void createTable(SupportSQLiteDatabase supportSQLiteDatabase, Class<? extends Entity> cls) {
        Entity entity = (Entity) newInstance(cls);
        if (entity == null) {
            return;
        }
        supportSQLiteDatabase.execSQL(getCreateTableSql(entity.getTableName(), entity.getTableColumns()));
    }

    public static String getCreateTableSql(String str, List<TableColumn> list) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(str);
        sb.append("(_id INTEGER PRIMARY KEY AUTOINCREMENT");
        int size = list.size();
        for (int i = 0; i < size; i++) {
            sb.append(", ");
            TableColumn tableColumn = list.get(i);
            sb.append(tableColumn.name);
            sb.append(" ");
            sb.append(tableColumn.type);
            if (tableColumn.isUnique) {
                sb.append(" UNIQUE");
            } else if (tableColumn.defaultValue != null) {
                sb.append(" DEFAULT '");
                sb.append(tableColumn.defaultValue);
                sb.append("'");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static void dropTable(SupportSQLiteDatabase supportSQLiteDatabase, Class<? extends Entity> cls) {
        supportSQLiteDatabase.execSQL(getDropTableSql(getTableName(cls)));
    }

    public static String getDropTableSql(String str) {
        return "DROP TABLE IF EXISTS " + str;
    }

    public static String getAddColumnSql(String str, TableColumn tableColumn) {
        StringBuilder sb = new StringBuilder("ALTER TABLE ");
        sb.append(str);
        sb.append(" ADD ");
        sb.append(tableColumn.name);
        sb.append(" ");
        sb.append(tableColumn.type);
        if (tableColumn.isUnique) {
            sb.append(" UNIQUE");
        } else if (tableColumn.defaultValue != null) {
            sb.append(" DEFAULT '");
            sb.append(tableColumn.defaultValue);
            sb.append("'");
        }
        return sb.toString();
    }

    public static void checkTableChange(SupportSQLiteDatabase supportSQLiteDatabase, List<Class<? extends Entity>> list) {
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Entity entity = (Entity) newInstance(list.get(i));
            String tableName = entity.getTableName();
            List<TableColumn> tableColumns = entity.getTableColumns();
            Cursor cursor = null;
            try {
                cursor = supportSQLiteDatabase.query("SELECT sql FROM sqlite_master WHERE type=? AND name=?", new String[]{"table", tableName});
                if (cursor != null && cursor.moveToFirst()) {
                    String string = cursor.getString(0);
                    for (String str : string.substring(string.indexOf("(") + 1).split(",")) {
                        String str2 = str.trim().split(" ", 2)[0];
                        Iterator<TableColumn> it = tableColumns.iterator();
                        while (it.hasNext()) {
                            if (str2.equalsIgnoreCase(it.next().name)) {
                                it.remove();
                            }
                        }
                    }
                    for (TableColumn tableColumn : tableColumns) {
                        arrayList.add(getAddColumnSql(tableName, tableColumn));
                    }
                } else {
                    arrayList.add(getCreateTableSql(tableName, tableColumns));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        supportSQLiteDatabase.beginTransaction();
        try {
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                supportSQLiteDatabase.execSQL((String) it2.next());
            }
            supportSQLiteDatabase.setTransactionSuccessful();
        } finally {
            supportSQLiteDatabase.endTransaction();
        }
    }

    /* loaded from: classes.dex */
    public class SQLiteOpenHelperCallback extends SupportSQLiteOpenHelper.Callback {
        public SQLiteOpenHelperCallback(int i) {
            super(i);
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
            EntityManager.this.initTableList();
            try {
                Iterator it = EntityManager.this.mTables.iterator();
                while (it.hasNext()) {
                    EntityManager.createTable(supportSQLiteDatabase, (Class) it.next());
                }
            } catch (Exception e) {
                DefaultLogger.e("EntityManager", "Db onCreate error.\n", e);
            }
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            EntityManager.this.initTableList();
            try {
                EntityManager.checkTableChange(supportSQLiteDatabase, EntityManager.this.mTables);
                EntityManager.this.onDatabaseUpgrade(supportSQLiteDatabase, i, i2);
            } catch (Exception e) {
                DefaultLogger.e("EntityManager", "Db onUpgrade error.\n", e);
            }
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            SamplingStatHelper.recordCountEvent("db_helper", "db_downgrade");
            EntityManager.this.onDatabaseDowngrade(supportSQLiteDatabase, i, i2);
        }
    }

    public void addTable(Class<? extends Entity> cls) {
        if (this.mTables == null) {
            this.mTables = new ArrayList<>(getTablesCount());
        }
        this.mTables.add(cls);
    }

    public final void initTableList() {
        if (this.mTables != null) {
            return;
        }
        onInitTableList();
    }
}
