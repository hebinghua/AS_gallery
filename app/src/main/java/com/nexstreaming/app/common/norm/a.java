package com.nexstreaming.app.common.norm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.google.gson_nex.Gson;
import com.nexstreaming.app.common.norm.NormColumnInfo;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* compiled from: NormDb.java */
/* loaded from: classes3.dex */
public abstract class a extends SQLiteOpenHelper {
    private static final String LOG_TAG = "NormDb";
    private final Gson gson;

    public abstract Class<? extends b>[] getTableClasses();

    public a(Context context, String str, int i) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, i);
        this.gson = new Gson();
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        String[] a;
        for (Class<? extends b> cls : getTableClasses()) {
            for (String str : c.a(cls).a()) {
                Log.d(LOG_TAG, "CreateTable:" + str);
                sQLiteDatabase.execSQL(str);
                Log.d(LOG_TAG, "Created");
            }
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        String[] a;
        String[] b;
        Log.d(LOG_TAG, "onUpgrade oldVersion=" + i + ", newVersion=" + i2);
        if (i < 11) {
            for (Class<? extends b> cls : getTableClasses()) {
                for (String str : c.a(cls).b()) {
                    Log.d(LOG_TAG, "DropTable:" + str);
                    sQLiteDatabase.execSQL(str);
                    Log.d(LOG_TAG, "Dropped");
                }
            }
            onCreate(sQLiteDatabase);
        } else if (i < i2) {
            for (Class<? extends b> cls2 : getTableClasses()) {
                for (String str2 : c.a(cls2).a(i, i2)) {
                    Log.d(LOG_TAG, "onUpgrade:" + str2);
                    sQLiteDatabase.execSQL(str2);
                    Log.d(LOG_TAG, "onUpgrade end");
                }
            }
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        String[] b;
        for (Class<? extends b> cls : getTableClasses()) {
            for (String str : c.a(cls).b()) {
                Log.d(LOG_TAG, "onDowngrade:" + str);
                sQLiteDatabase.execSQL(str);
                Log.d(LOG_TAG, "onDowngrade");
            }
        }
        onCreate(sQLiteDatabase);
    }

    public static String camelCaseToLCUnderscore(String str) {
        String lowerCase = str.replaceAll("(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])|(?<=[A-Za-z])(?=[^A-Za-z])", "_").toLowerCase(Locale.ENGLISH);
        if (lowerCase.length() < 1) {
            return "_";
        }
        char charAt = lowerCase.charAt(0);
        if (charAt == '_') {
            return lowerCase;
        }
        if (charAt >= 'a' && charAt <= 'z') {
            return lowerCase;
        }
        return "_" + lowerCase;
    }

    private <T extends b> ContentValues getContentValuesForRecord(T t) {
        NormColumnInfo[] e;
        ContentValues contentValues = new ContentValues();
        try {
            for (NormColumnInfo normColumnInfo : t.getTableInfo().e()) {
                if (normColumnInfo != null && !normColumnInfo.g) {
                    String str = null;
                    byte[] bArr = null;
                    byte[] bArr2 = null;
                    switch (AnonymousClass1.a[normColumnInfo.c.ordinal()]) {
                        case 1:
                            Object obj = normColumnInfo.b.get(t);
                            if (obj == null) {
                                contentValues.putNull(normColumnInfo.a);
                                break;
                            } else {
                                contentValues.put(normColumnInfo.a, String.valueOf(obj));
                                continue;
                            }
                        case 2:
                            contentValues.put(normColumnInfo.a, Integer.valueOf(normColumnInfo.b.getInt(t)));
                            continue;
                        case 3:
                            if (normColumnInfo.f) {
                                Object obj2 = normColumnInfo.b.get(t);
                                if (obj2 instanceof b) {
                                    contentValues.put(normColumnInfo.a, Long.valueOf(((b) obj2).getDbRowID()));
                                    break;
                                } else {
                                    continue;
                                }
                            } else {
                                contentValues.put(normColumnInfo.a, Long.valueOf(normColumnInfo.b.getLong(t)));
                                break;
                            }
                        case 4:
                            contentValues.put(normColumnInfo.a, Double.valueOf(normColumnInfo.b.getDouble(t)));
                            continue;
                        case 5:
                            contentValues.put(normColumnInfo.a, Float.valueOf(normColumnInfo.b.getFloat(t)));
                            continue;
                        case 6:
                            Enum r6 = (Enum) normColumnInfo.b.get(t);
                            String str2 = normColumnInfo.a;
                            if (r6 != null) {
                                str = r6.name();
                            }
                            contentValues.put(str2, str);
                            continue;
                        case 7:
                            contentValues.put(normColumnInfo.a, Integer.valueOf(normColumnInfo.b.getBoolean(t) ? 1 : 0));
                            continue;
                        case 8:
                            contentValues.put(normColumnInfo.a, (byte[]) normColumnInfo.b.get(t));
                            continue;
                        case 9:
                            Bitmap bitmap = (Bitmap) normColumnInfo.b.get(t);
                            if (bitmap != null) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                bArr2 = byteArrayOutputStream.toByteArray();
                            }
                            contentValues.put(normColumnInfo.a, bArr2);
                            continue;
                        case 10:
                            Bitmap bitmap2 = (Bitmap) normColumnInfo.b.get(t);
                            if (bitmap2 != null) {
                                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                                bitmap2.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream2);
                                bArr = byteArrayOutputStream2.toByteArray();
                            }
                            contentValues.put(normColumnInfo.a, bArr);
                            continue;
                        case 11:
                            contentValues.put(normColumnInfo.a, this.gson.toJson(normColumnInfo.b.get(t)));
                            continue;
                        default:
                            continue;
                    }
                }
            }
            return contentValues;
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException(e2);
        }
    }

    /* compiled from: NormDb.java */
    /* renamed from: com.nexstreaming.app.common.norm.a$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[NormColumnInfo.ColumnType.values().length];
            a = iArr;
            try {
                iArr[NormColumnInfo.ColumnType.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[NormColumnInfo.ColumnType.INT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[NormColumnInfo.ColumnType.LONG.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[NormColumnInfo.ColumnType.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[NormColumnInfo.ColumnType.FLOAT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[NormColumnInfo.ColumnType.ENUM.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[NormColumnInfo.ColumnType.BOOL.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[NormColumnInfo.ColumnType.BLOB.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[NormColumnInfo.ColumnType.PNG.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[NormColumnInfo.ColumnType.JPEG.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                a[NormColumnInfo.ColumnType.JSON.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    private <T extends b> void updateRecIndex(T t, long j) {
        try {
            t.getTableInfo().f().b.setLong(t, j);
            t.addedOrUpdatedToDb = true;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T extends b> void addOrUpdate(T t) {
        updateRecIndex(t, getWritableDatabase().insertWithOnConflict(t.getTableInfo().c(), null, getContentValuesForRecord(t), 5));
    }

    public <T extends b> void add(T t) {
        updateRecIndex(t, getWritableDatabase().insert(t.getTableInfo().c(), null, getContentValuesForRecord(t)));
    }

    public <T extends b> void update(T t) {
        try {
            getWritableDatabase().update(t.getTableInfo().c(), getContentValuesForRecord(t), "_id = ?", new String[]{String.valueOf(t.getTableInfo().f().b.getLong(t))});
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T extends b> T findById(Class<T> cls, long j) {
        List<T> query = query(cls, c.a(cls).f().a + " = ? LIMIT 1", Long.valueOf(j));
        if (query == null || query.isEmpty()) {
            return null;
        }
        return query.get(0);
    }

    public <T extends b> T findFirst(Class<T> cls, String str, Object... objArr) {
        List<T> query = query(cls, str + " LIMIT 1", objArr);
        if (query == null || query.isEmpty()) {
            return null;
        }
        return query.get(0);
    }

    public <T extends b> long findFirstRowId(Class<T> cls, String str, Object... objArr) {
        List<T> query = query(cls, str + " LIMIT 1", objArr);
        if (query == null || query.isEmpty()) {
            return 0L;
        }
        return query.get(0).getDbRowID();
    }

    public <T extends b> List<T> query(Class<T> cls) {
        return query_internal(cls, null, null, true);
    }

    public <T extends b> void delete(Class<T> cls, String str, Object... objArr) {
        String[] strArr = new String[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            strArr[i] = String.valueOf(objArr[i]);
        }
        getWritableDatabase().delete(c.a(cls).c(), str, strArr);
    }

    public <T extends b> void delete(T t) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        String c = t.getTableInfo().c();
        writableDatabase.delete(c, t.getTableInfo().f() + " = ?", new String[]{String.valueOf(t.getDbRowID())});
    }

    public <T extends b> List<T> query(Class<T> cls, String str, Object... objArr) {
        return query_internal(cls, str, objArr, true);
    }

    public <T extends b> int count(Class<T> cls) {
        return count_internal(cls, null, null, false);
    }

    public <T extends b> int count(Class<T> cls, String str, Object... objArr) {
        return count_internal(cls, str, objArr, false);
    }

    private <T extends b> int count_internal(Class<T> cls, String str, Object[] objArr, boolean z) {
        String[] strArr;
        if (objArr != null) {
            strArr = new String[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                strArr[i] = String.valueOf(objArr[i]);
            }
        } else {
            strArr = null;
        }
        c a = c.a(cls);
        Cursor query = getReadableDatabase().query(a.c(), new String[]{a.f().a}, str, strArr, null, null, null);
        int count = query.getCount();
        query.close();
        return count;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T extends b> List<T> query_internal(Class<T> cls, String str, Object[] objArr, boolean z) {
        String[] strArr;
        if (objArr != null) {
            String[] strArr2 = new String[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                strArr2[i] = String.valueOf(objArr[i]);
            }
            strArr = strArr2;
        } else {
            strArr = null;
        }
        c a = c.a(cls);
        NormColumnInfo[] e = a.e();
        Cursor query = getReadableDatabase().query(a.c(), a.d(), str, strArr, null, null, null);
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        try {
            try {
                query.moveToPosition(-1);
                while (query.moveToNext()) {
                    T newInstance = cls.newInstance();
                    for (int i2 = 0; i2 < e.length; i2++) {
                        switch (AnonymousClass1.a[e[i2].c.ordinal()]) {
                            case 1:
                                e[i2].b.set(newInstance, query.getString(i2));
                                break;
                            case 2:
                                e[i2].b.setInt(newInstance, query.getInt(i2));
                                break;
                            case 3:
                                if (e[i2].f) {
                                    if (!z) {
                                        break;
                                    } else {
                                        if (!hashMap.containsKey(e[i2].j)) {
                                            hashMap.put(e[i2].j, new HashMap());
                                        }
                                        long j = query.getLong(query.getColumnIndex(e[i2].a));
                                        Object obj = ((Map) hashMap.get(e[i2].j)).get(Long.valueOf(j));
                                        if (obj == null) {
                                            obj = findById(e[i2].j, j);
                                            ((Map) hashMap.get(e[i2].j)).put(Long.valueOf(j), obj);
                                        }
                                        e[i2].b.set(newInstance, obj);
                                        break;
                                    }
                                } else {
                                    e[i2].b.setLong(newInstance, query.getLong(i2));
                                    break;
                                }
                            case 4:
                                e[i2].b.setDouble(newInstance, query.getDouble(i2));
                                break;
                            case 5:
                                e[i2].b.setFloat(newInstance, query.getFloat(i2));
                                break;
                            case 6:
                                Class<?> type = e[i2].b.getType();
                                String string = query.getString(i2);
                                if (string != null) {
                                    try {
                                        e[i2].b.set(newInstance, Enum.valueOf(type, string));
                                        break;
                                    } catch (IllegalArgumentException unused) {
                                        e[i2].b.set(newInstance, null);
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            case 7:
                                e[i2].b.setBoolean(newInstance, query.getInt(i2) != 0);
                                break;
                            case 8:
                                e[i2].b.set(newInstance, query.getBlob(i2));
                                break;
                            case 9:
                            case 10:
                                byte[] blob = query.getBlob(i2);
                                e[i2].b.set(newInstance, BitmapFactory.decodeByteArray(blob, 0, blob.length));
                                break;
                            case 11:
                                e[i2].b.set(newInstance, this.gson.fromJson(query.getString(i2), e[i2].b.getGenericType()));
                                break;
                        }
                    }
                    arrayList.add(newInstance);
                }
                return arrayList;
            } finally {
                query.close();
            }
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException(e2);
        } catch (InstantiationException e3) {
            throw new IllegalStateException(e3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends b, RESULT_TYPE> List<RESULT_TYPE> queryIndividualField(Class<T> cls, String str, String str2, Object... objArr) {
        String[] strArr;
        Object string;
        if (objArr != null) {
            String[] strArr2 = new String[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                strArr2[i] = String.valueOf(objArr[i]);
            }
            strArr = strArr2;
        } else {
            strArr = null;
        }
        c a = c.a(cls);
        NormColumnInfo a2 = a.a(str);
        Cursor query = getReadableDatabase().query(a.c(), new String[]{str}, str2, strArr, null, null, null);
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        try {
            query.moveToPosition(-1);
            while (query.moveToNext()) {
                switch (AnonymousClass1.a[a2.c.ordinal()]) {
                    case 1:
                        string = query.getString(0);
                        continue;
                        arrayList.add(string);
                    case 2:
                        string = Integer.valueOf(query.getInt(0));
                        continue;
                        arrayList.add(string);
                    case 3:
                        if (a2.f) {
                            if (!hashMap.containsKey(a2.j)) {
                                hashMap.put(a2.j, new HashMap());
                            }
                            long j = query.getLong(0);
                            Object obj = ((Map) hashMap.get(a2.j)).get(Long.valueOf(j));
                            if (obj == null) {
                                obj = findById(a2.j, j);
                                ((Map) hashMap.get(a2.j)).put(Long.valueOf(j), obj);
                            }
                            string = obj;
                            continue;
                        } else {
                            string = Long.valueOf(query.getLong(0));
                        }
                        arrayList.add(string);
                    case 4:
                        string = Double.valueOf(query.getDouble(0));
                        continue;
                        arrayList.add(string);
                    case 5:
                        string = Float.valueOf(query.getFloat(0));
                        continue;
                        arrayList.add(string);
                    case 6:
                        Class<?> type = a2.b.getType();
                        String string2 = query.getString(0);
                        if (string2 != null) {
                            try {
                                string = Enum.valueOf(type, string2);
                                continue;
                            } catch (IllegalArgumentException unused) {
                                break;
                            }
                            arrayList.add(string);
                        }
                        break;
                    case 7:
                        string = Boolean.valueOf(query.getInt(0) != 0);
                        continue;
                        arrayList.add(string);
                    case 8:
                        string = query.getBlob(0);
                        continue;
                        arrayList.add(string);
                    case 9:
                    case 10:
                        byte[] blob = query.getBlob(0);
                        string = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                        continue;
                        arrayList.add(string);
                    case 11:
                        string = this.gson.fromJson(query.getString(0), a2.b.getGenericType());
                        continue;
                        arrayList.add(string);
                }
                string = null;
                arrayList.add(string);
            }
            return arrayList;
        } finally {
            query.close();
        }
    }

    public void beginTransaction() {
        getWritableDatabase().beginTransaction();
    }

    public void setTransactionSuccessful() {
        getWritableDatabase().setTransactionSuccessful();
    }

    public void endTransaction() throws SQLiteFullException {
        try {
            getWritableDatabase().endTransaction();
        } catch (SQLiteFullException unused) {
            throw new SQLiteFullException();
        }
    }
}
