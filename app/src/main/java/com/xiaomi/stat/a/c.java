package com.xiaomi.stat.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.stat.MiStatParams;
import com.xiaomi.stat.a.l;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.m;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/* loaded from: classes3.dex */
public class c {
    private static final String a = "EventManager";
    private static final int b = 10;
    private static final int c = 0;
    private static final int d = 300;
    private static final int e = 122880;
    private static final int f = 55;
    private static final int g = 2;
    private static final String h = "priority DESC, _id ASC";
    private static final int i = 7;
    private static final long j = 52428800;
    private static c k;
    private a l;
    private File m;

    public static c a() {
        if (k == null) {
            synchronized (c.class) {
                if (k == null) {
                    k = new c();
                }
            }
        }
        return k;
    }

    private c() {
        Context a2 = ak.a();
        this.l = new a(a2);
        this.m = a2.getDatabasePath(j.a);
    }

    public void a(l lVar) {
        com.xiaomi.stat.c.a(new d(this, lVar));
        com.xiaomi.stat.d.k.c(a, "add event: name=" + lVar.a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(l lVar) {
        d();
        SQLiteDatabase writableDatabase = this.l.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("e", lVar.a);
        contentValues.put("eg", lVar.b);
        contentValues.put("tp", lVar.c);
        contentValues.put("ts", Long.valueOf(lVar.e));
        if (c(lVar)) {
            a(lVar.d);
        }
        contentValues.put("ps", lVar.d.toJsonString());
        contentValues.put(j.i, lVar.f);
        contentValues.put(j.j, Integer.valueOf(lVar.g ? 1 : 0));
        contentValues.put(j.k, Integer.valueOf(TextUtils.equals(lVar.b, l.a.h) ? 10 : 0));
        writableDatabase.insert(j.b, null, contentValues);
    }

    private boolean c(l lVar) {
        return !lVar.c.startsWith(l.a.w);
    }

    private void a(MiStatParams miStatParams) {
        miStatParams.putString(l.a.n, com.xiaomi.stat.d.c.b());
        miStatParams.putString(l.a.o, com.xiaomi.stat.a.g);
        miStatParams.putString(l.a.p, m.c());
        miStatParams.putString(l.a.q, m.d());
        miStatParams.putString(l.a.r, com.xiaomi.stat.d.l.b(ak.a()));
        miStatParams.putString(l.a.s, m.a(ak.a()));
        miStatParams.putString(l.a.t, Build.MANUFACTURER);
        miStatParams.putString(l.a.u, Build.MODEL);
        miStatParams.putString(l.a.v, m.b());
    }

    public k a(b[] bVarArr) {
        FutureTask futureTask = new FutureTask(new e(this, bVarArr));
        com.xiaomi.stat.c.a(futureTask);
        try {
            return (k) futureTask.get();
        } catch (InterruptedException | ExecutionException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Not initialized variable reg: 11, insn: 0x0193: MOVE  (r7 I:??[OBJECT, ARRAY]) = (r11 I:??[OBJECT, ARRAY]), block:B:70:0x0193 */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0144 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.xiaomi.stat.a.k b(com.xiaomi.stat.a.b[] r35) {
        /*
            Method dump skipped, instructions count: 410
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.stat.a.c.b(com.xiaomi.stat.a.b[]):com.xiaomi.stat.a.k");
    }

    private boolean a(b[] bVarArr, String str, String str2, boolean z) {
        for (b bVar : bVarArr) {
            if (bVar.a(str, str2, z)) {
                return true;
            }
        }
        return false;
    }

    public void a(ArrayList<Long> arrayList) {
        FutureTask futureTask = new FutureTask(new f(this, arrayList), null);
        com.xiaomi.stat.c.a(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException | ExecutionException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(ArrayList<Long> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            try {
                SQLiteDatabase writableDatabase = this.l.getWritableDatabase();
                StringBuilder sb = new StringBuilder(((Long.toString(arrayList.get(0).longValue()).length() + 1) * arrayList.size()) + 16);
                sb.append(j.c);
                sb.append(" in (");
                sb.append(arrayList.get(0));
                int size = arrayList.size();
                for (int i2 = 1; i2 < size; i2++) {
                    sb.append(",");
                    sb.append(arrayList.get(i2));
                }
                sb.append(")");
                int delete = writableDatabase.delete(j.b, sb.toString(), null);
                com.xiaomi.stat.d.k.c(a, "deleted events number " + delete);
            } catch (Exception unused) {
            }
        }
    }

    public void b() {
        com.xiaomi.stat.c.a(new g(this));
    }

    private void d() {
        if (!this.m.exists() || this.m.length() < j) {
            return;
        }
        com.xiaomi.stat.d.k.e(a, "database too big: " + this.m.length());
        this.l.getWritableDatabase().delete(j.b, null, null);
    }

    public void a(String str) {
        com.xiaomi.stat.c.a(new h(this, str));
    }

    public long c() {
        FutureTask futureTask = new FutureTask(new i(this));
        com.xiaomi.stat.c.a(futureTask);
        try {
            return ((Long) futureTask.get()).longValue();
        } catch (InterruptedException | ExecutionException unused) {
            return -1L;
        }
    }
}
