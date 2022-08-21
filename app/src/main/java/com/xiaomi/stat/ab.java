package com.xiaomi.stat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.FileObserver;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes3.dex */
public class ab {
    private static final String a = "MiStatPref";
    private static final String b = "true";
    private static final String c = "false";
    private static ab e;
    private FileObserver d;
    private Map<String, String> f;
    private SQLiteOpenHelper g;

    public static ab a() {
        if (e == null) {
            synchronized (ab.class) {
                if (e == null) {
                    e = new ab();
                }
            }
        }
        return e;
    }

    private ab() {
        Context a2 = ak.a();
        this.f = new HashMap();
        this.g = new a(a2);
        b();
        c(a2.getDatabasePath(a.a).getAbsolutePath());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        FutureTask futureTask = new FutureTask(new ac(this));
        try {
            c.a(futureTask);
            Cursor cursor = null;
            try {
                cursor = (Cursor) futureTask.get(2L, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException unused) {
            }
            if (cursor == null) {
                return;
            }
            this.f.clear();
            try {
                com.xiaomi.stat.d.k.c(a, "load pref from db");
                int columnIndex = cursor.getColumnIndex(a.c);
                int columnIndex2 = cursor.getColumnIndex(a.d);
                while (cursor.moveToNext()) {
                    String string = cursor.getString(columnIndex);
                    String string2 = cursor.getString(columnIndex2);
                    this.f.put(string, string2);
                    com.xiaomi.stat.d.k.c(a, "key=" + string + " ,value=" + string2);
                }
            } catch (Exception unused2) {
            } catch (Throwable th) {
                cursor.close();
                throw th;
            }
            cursor.close();
        } catch (RejectedExecutionException e2) {
            com.xiaomi.stat.d.k.c(a, "load data execute failed with " + e2);
        }
    }

    private void c(String str) {
        ad adVar = new ad(this, str);
        this.d = adVar;
        adVar.startWatching();
    }

    public int a(String str, int i) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                try {
                    return Integer.valueOf(this.f.get(str)).intValue();
                } catch (NumberFormatException unused) {
                }
            }
            return i;
        }
    }

    public float a(String str, float f) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                try {
                    return Float.valueOf(this.f.get(str)).floatValue();
                } catch (NumberFormatException unused) {
                }
            }
            return f;
        }
    }

    public String a(String str, String str2) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                return this.f.get(str);
            }
            return str2;
        }
    }

    public long a(String str, long j) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                try {
                    return Long.valueOf(this.f.get(str)).longValue();
                } catch (NumberFormatException unused) {
                }
            }
            return j;
        }
    }

    public boolean a(String str, boolean z) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                String str2 = this.f.get(str);
                if (b.equalsIgnoreCase(str2)) {
                    return true;
                }
                if (c.equalsIgnoreCase(str2)) {
                    return false;
                }
            }
            return z;
        }
    }

    public void b(String str, int i) {
        c(str, Integer.toString(i));
    }

    public void b(String str, float f) {
        c(str, Float.toString(f));
    }

    public void b(String str, String str2) {
        c(str, str2);
    }

    public void b(String str, long j) {
        c(str, Long.toString(j));
    }

    public void b(String str, boolean z) {
        c(str, Boolean.toString(z));
    }

    private void c(String str, String str2) {
        synchronized (this) {
            boolean z = true;
            if (TextUtils.isEmpty(str2)) {
                if (this.f.containsKey(str)) {
                    this.f.remove(str);
                } else {
                    z = false;
                }
            } else {
                this.f.put(str, str2);
            }
            com.xiaomi.stat.d.k.c(a, "put value: key=" + str + " ,value=" + str2);
            if (!z) {
                return;
            }
            FutureTask futureTask = new FutureTask(new ae(this, str2, str), null);
            try {
                c.a(futureTask);
                try {
                    futureTask.get();
                } catch (InterruptedException | ExecutionException unused) {
                }
            } catch (RejectedExecutionException e2) {
                com.xiaomi.stat.d.k.c(a, "execute failed with " + e2);
            }
        }
    }

    public boolean a(String str) {
        boolean containsKey;
        synchronized (this) {
            containsKey = this.f.containsKey(str);
        }
        return containsKey;
    }

    public void b(String str) {
        b(str, (String) null);
    }

    /* loaded from: classes3.dex */
    public static class a extends SQLiteOpenHelper {
        public static final String a = "mistat_pf";
        public static final String b = "pref";
        public static final String c = "pref_key";
        public static final String d = "pref_value";
        private static final int e = 1;
        private static final String f = "_id";
        private static final String g = "CREATE TABLE pref (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,pref_key TEXT,pref_value TEXT)";

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public a(Context context) {
            super(context, a, (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(g);
        }
    }
}
