package com.xiaomi.stat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.xiaomi.stat.ab;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ae implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ String b;
    public final /* synthetic */ ab c;

    public ae(ab abVar, String str, String str2) {
        this.c = abVar;
        this.a = str;
        this.b = str2;
    }

    @Override // java.lang.Runnable
    public void run() {
        SQLiteOpenHelper sQLiteOpenHelper;
        Cursor cursor = null;
        try {
            try {
                sQLiteOpenHelper = this.c.g;
                SQLiteDatabase writableDatabase = sQLiteOpenHelper.getWritableDatabase();
                if (TextUtils.isEmpty(this.a)) {
                    writableDatabase.delete(ab.a.b, "pref_key=?", new String[]{this.b});
                    return;
                }
                Cursor query = writableDatabase.query(ab.a.b, null, "pref_key=?", new String[]{this.b}, null, null, null);
                try {
                    boolean z = query.getCount() <= 0;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ab.a.c, this.b);
                    contentValues.put(ab.a.d, this.a);
                    if (z) {
                        writableDatabase.insert(ab.a.b, null, contentValues);
                    } else {
                        writableDatabase.update(ab.a.b, contentValues, "pref_key=?", new String[]{this.b});
                    }
                    query.close();
                } catch (Exception e) {
                    e = e;
                    cursor = query;
                    com.xiaomi.stat.d.k.c("MiStatPref", "update pref db failed with " + e);
                    if (cursor == null) {
                        return;
                    }
                    cursor.close();
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
