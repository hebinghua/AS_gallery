package com.xiaomi.onetrack.b;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.xiaomi.onetrack.util.aa;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class b {
    public static b h;
    public static BroadcastReceiver j = new c();
    public a i = new a(com.xiaomi.onetrack.e.a.a());

    public static b a() {
        if (h == null) {
            a(com.xiaomi.onetrack.e.a.b());
        }
        return h;
    }

    public static void a(Context context) {
        if (h == null) {
            synchronized (b.class) {
                if (h == null) {
                    h = new b();
                }
            }
        }
        b(context);
    }

    public b() {
        b();
    }

    public static void b(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(j, intentFilter);
    }

    public synchronized void a(com.xiaomi.onetrack.e.b bVar) {
        com.xiaomi.onetrack.b.a.a(new e(this, bVar));
    }

    public final void b(com.xiaomi.onetrack.e.b bVar) {
        synchronized (this.i) {
            if (!bVar.h()) {
                com.xiaomi.onetrack.util.p.c("EventManager", "addEventToDatabase event is inValid, event:" + bVar.d());
                return;
            }
            SQLiteDatabase writableDatabase = this.i.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("appid", bVar.b());
            contentValues.put("package", bVar.c());
            contentValues.put("event_name", bVar.d());
            contentValues.put(com.xiaomi.stat.a.j.k, Integer.valueOf(bVar.e()));
            contentValues.put("timestamp", Long.valueOf(System.currentTimeMillis()));
            byte[] a2 = a(bVar.f().toString());
            if (a2.length > 204800) {
                com.xiaomi.onetrack.util.p.b("EventManager", "Too large data, discard ***");
                return;
            }
            contentValues.put("data", a2);
            long insert = writableDatabase.insert(com.xiaomi.stat.a.j.b, null, contentValues);
            com.xiaomi.onetrack.util.p.a("EventManager", "DB-Thread: EventManager.addEventToDatabase , row=" + insert);
            if (insert != -1) {
                if (com.xiaomi.onetrack.util.p.a) {
                    com.xiaomi.onetrack.util.p.a("EventManager", "添加后，DB 中事件个数为 " + c());
                }
                long currentTimeMillis = System.currentTimeMillis();
                if ("onetrack_active".equals(bVar.d())) {
                    aa.a(currentTimeMillis);
                }
                com.xiaomi.onetrack.a.m.a(false);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x010a, code lost:
        if (r3 == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x011a, code lost:
        if (r3 == null) goto L39;
     */
    /* JADX WARN: Not initialized variable reg: 3, insn: 0x011f: MOVE  (r11 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:59:0x011f */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.xiaomi.onetrack.b.g a(int r22) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.onetrack.b.b.a(int):com.xiaomi.onetrack.b.g");
    }

    public int a(ArrayList<Long> arrayList) {
        synchronized (this.i) {
            if (arrayList != null) {
                if (arrayList.size() != 0) {
                    try {
                        SQLiteDatabase writableDatabase = this.i.getWritableDatabase();
                        boolean z = true;
                        StringBuilder sb = new StringBuilder(((Long.toString(arrayList.get(0).longValue()).length() + 1) * arrayList.size()) + 16);
                        sb.append(com.xiaomi.stat.a.j.c);
                        sb.append(" in (");
                        sb.append(arrayList.get(0));
                        int size = arrayList.size();
                        for (int i = 1; i < size; i++) {
                            sb.append(",");
                            sb.append(arrayList.get(i));
                        }
                        sb.append(")");
                        int delete = writableDatabase.delete(com.xiaomi.stat.a.j.b, sb.toString(), null);
                        com.xiaomi.onetrack.util.p.a("EventManager", "deleted events count " + delete);
                        long c = a().c();
                        if (c != 0) {
                            z = false;
                        }
                        com.xiaomi.onetrack.a.m.a(z);
                        com.xiaomi.onetrack.util.p.a("EventManager", "after delete DB record remains=" + c);
                        return delete;
                    } catch (Exception e) {
                        com.xiaomi.onetrack.util.p.b("EventManager", "e=" + e);
                        return 0;
                    }
                }
            }
            return 0;
        }
    }

    public void b() {
        com.xiaomi.onetrack.b.a.a(new f(this));
    }

    public long c() {
        return DatabaseUtils.queryNumEntries(this.i.getReadableDatabase(), com.xiaomi.stat.a.j.b);
    }

    public static byte[] a(String str) {
        return com.xiaomi.onetrack.c.a.a(str.getBytes(), com.xiaomi.onetrack.c.d.a(com.xiaomi.onetrack.c.c.a(), true).getBytes());
    }

    public static String a(byte[] bArr) {
        return new String(com.xiaomi.onetrack.c.a.b(bArr, com.xiaomi.onetrack.c.d.a(com.xiaomi.onetrack.c.c.a(), true).getBytes()));
    }

    public final void d() {
        try {
            this.i.getWritableDatabase().delete(com.xiaomi.stat.a.j.b, null, null);
            com.xiaomi.onetrack.util.p.a("EventManager", "delete table events");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes3.dex */
    public static class a extends SQLiteOpenHelper {
        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public a(Context context) {
            super(context, "onetrack", (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE events (_id INTEGER PRIMARY KEY AUTOINCREMENT,appid TEXT,package TEXT,event_name TEXT,priority INTEGER,data BLOB,timestamp INTEGER)");
        }
    }
}
