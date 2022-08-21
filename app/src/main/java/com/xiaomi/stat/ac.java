package com.xiaomi.stat;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import com.xiaomi.stat.ab;
import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ac implements Callable<Cursor> {
    public final /* synthetic */ ab a;

    public ac(ab abVar) {
        this.a = abVar;
    }

    @Override // java.util.concurrent.Callable
    /* renamed from: a */
    public Cursor call() throws Exception {
        SQLiteOpenHelper sQLiteOpenHelper;
        try {
            sQLiteOpenHelper = this.a.g;
            return sQLiteOpenHelper.getWritableDatabase().query(ab.a.b, null, null, null, null, null, null);
        } catch (Exception unused) {
            return null;
        }
    }
}
