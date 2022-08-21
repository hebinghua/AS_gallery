package com.xiaomi.onetrack.a;

import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public class j implements Callable<k> {
    public final /* synthetic */ String a;
    public final /* synthetic */ g b;

    public j(g gVar, String str) {
        this.b = gVar;
        this.a = str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x008a, code lost:
        if (r3 == null) goto L20;
     */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0097 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.util.concurrent.Callable
    /* renamed from: a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.xiaomi.onetrack.a.k call() throws java.lang.Exception {
        /*
            r12 = this;
            java.lang.String r0 = "getConfig  cursor.close"
            java.lang.String r1 = "ConfigDbManager"
            r2 = 0
            java.lang.String r6 = "app_id=?"
            com.xiaomi.onetrack.a.g r3 = r12.b     // Catch: java.lang.Throwable -> L7c java.lang.Exception -> L81
            com.xiaomi.onetrack.a.f r3 = com.xiaomi.onetrack.a.g.a(r3)     // Catch: java.lang.Throwable -> L7c java.lang.Exception -> L81
            android.database.sqlite.SQLiteDatabase r3 = r3.getWritableDatabase()     // Catch: java.lang.Throwable -> L7c java.lang.Exception -> L81
            java.lang.String r4 = "events_cloud"
            r5 = 0
            r7 = 1
            java.lang.String[] r7 = new java.lang.String[r7]     // Catch: java.lang.Throwable -> L7c java.lang.Exception -> L81
            r8 = 0
            java.lang.String r9 = r12.a     // Catch: java.lang.Throwable -> L7c java.lang.Exception -> L81
            r7[r8] = r9     // Catch: java.lang.Throwable -> L7c java.lang.Exception -> L81
            r8 = 0
            r9 = 0
            r10 = 0
            android.database.Cursor r3 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L7c java.lang.Exception -> L81
            java.lang.String r4 = "app_id"
            int r4 = r3.getColumnIndex(r4)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            java.lang.String r5 = "cloud_data"
            int r5 = r3.getColumnIndex(r5)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            java.lang.String r6 = "data_hash"
            int r6 = r3.getColumnIndex(r6)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            java.lang.String r7 = "timestamp"
            int r7 = r3.getColumnIndex(r7)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            boolean r8 = r3.moveToNext()     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            if (r8 == 0) goto L8c
            com.xiaomi.onetrack.a.k r8 = new com.xiaomi.onetrack.a.k     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r8.<init>()     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            java.lang.String r4 = r3.getString(r4)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r8.a = r4     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            java.lang.String r4 = r3.getString(r5)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            if (r5 != 0) goto L5d
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r5.<init>(r4)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r8.e = r5     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
        L5d:
            org.json.JSONObject r4 = r8.e     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            int r4 = com.xiaomi.onetrack.a.g.a(r4)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            long r4 = (long) r4     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r8.b = r4     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            java.lang.String r4 = r3.getString(r6)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r8.d = r4     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            long r4 = r3.getLong(r7)     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r8.c = r4     // Catch: java.lang.Exception -> L7a java.lang.Throwable -> L94
            r3.close()     // Catch: java.lang.Exception -> L76
            goto L79
        L76:
            com.xiaomi.onetrack.util.p.a(r1, r0)
        L79:
            return r8
        L7a:
            r4 = move-exception
            goto L83
        L7c:
            r3 = move-exception
            r11 = r3
            r3 = r2
            r2 = r11
            goto L95
        L81:
            r4 = move-exception
            r3 = r2
        L83:
            java.lang.String r4 = r4.getMessage()     // Catch: java.lang.Throwable -> L94
            com.xiaomi.onetrack.util.p.a(r1, r4)     // Catch: java.lang.Throwable -> L94
            if (r3 == 0) goto L93
        L8c:
            r3.close()     // Catch: java.lang.Exception -> L90
            goto L93
        L90:
            com.xiaomi.onetrack.util.p.a(r1, r0)
        L93:
            return r2
        L94:
            r2 = move-exception
        L95:
            if (r3 == 0) goto L9e
            r3.close()     // Catch: java.lang.Exception -> L9b
            goto L9e
        L9b:
            com.xiaomi.onetrack.util.p.a(r1, r0)
        L9e:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.onetrack.a.j.call():com.xiaomi.onetrack.a.k");
    }
}
