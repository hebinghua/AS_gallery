package com.xiaomi.onetrack.b;

/* loaded from: classes3.dex */
public class f implements Runnable {
    public final /* synthetic */ b a;

    public f(b bVar) {
        this.a = bVar;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x00c0, code lost:
        if (r1 == null) goto L13;
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r15 = this;
            com.xiaomi.onetrack.b.b r0 = r15.a
            com.xiaomi.onetrack.b.b$a r0 = com.xiaomi.onetrack.b.b.a(r0)
            monitor-enter(r0)
            r1 = 0
            com.xiaomi.onetrack.b.b r2 = r15.a     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            com.xiaomi.onetrack.b.b$a r2 = com.xiaomi.onetrack.b.b.a(r2)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            android.database.sqlite.SQLiteDatabase r2 = r2.getWritableDatabase()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.util.Calendar r3 = java.util.Calendar.getInstance()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r3.setTimeInMillis(r4)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r4 = 6
            int r5 = r3.get(r4)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            int r5 = r5 + (-7)
            r3.set(r4, r5)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r4 = 11
            r11 = 0
            r3.set(r4, r11)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r4 = 12
            r3.set(r4, r11)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r4 = 13
            r3.set(r4, r11)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            long r3 = r3.getTimeInMillis()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r12 = "timestamp < ? "
            r13 = 1
            java.lang.String[] r14 = new java.lang.String[r13]     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r3 = java.lang.Long.toString(r3)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r14[r11] = r3     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r4 = "events"
            java.lang.String r3 = "timestamp"
            java.lang.String[] r5 = new java.lang.String[]{r3}     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r8 = 0
            r9 = 0
            java.lang.String r10 = "timestamp ASC"
            r3 = r2
            r6 = r12
            r7 = r14
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            int r3 = r1.getCount()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            if (r3 == 0) goto L7b
            java.lang.String r3 = "events"
            int r2 = r2.delete(r3, r12, r14)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r3 = "EventManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r4.<init>()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r5 = "*** deleted obsolete item count="
            r4.append(r5)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r4.append(r2)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r2 = r4.toString()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            com.xiaomi.onetrack.util.p.a(r3, r2)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
        L7b:
            com.xiaomi.onetrack.b.b r2 = com.xiaomi.onetrack.b.b.a()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            long r2 = r2.c()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r4 = 0
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 != 0) goto L8a
            r11 = r13
        L8a:
            com.xiaomi.onetrack.a.m.a(r11)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r4 = "EventManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r5.<init>()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r6 = "after delete obsolete record remains="
            r5.append(r6)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            r5.append(r2)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            java.lang.String r2 = r5.toString()     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
            com.xiaomi.onetrack.util.p.a(r4, r2)     // Catch: java.lang.Throwable -> La7 java.lang.Exception -> La9
        La3:
            r1.close()     // Catch: java.lang.Throwable -> Lcb
            goto Lc3
        La7:
            r2 = move-exception
            goto Lc5
        La9:
            r2 = move-exception
            java.lang.String r3 = "EventManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La7
            r4.<init>()     // Catch: java.lang.Throwable -> La7
            java.lang.String r5 = "remove obsolete events failed with "
            r4.append(r5)     // Catch: java.lang.Throwable -> La7
            r4.append(r2)     // Catch: java.lang.Throwable -> La7
            java.lang.String r2 = r4.toString()     // Catch: java.lang.Throwable -> La7
            com.xiaomi.onetrack.util.p.d(r3, r2)     // Catch: java.lang.Throwable -> La7
            if (r1 == 0) goto Lc3
            goto La3
        Lc3:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lcb
            return
        Lc5:
            if (r1 == 0) goto Lca
            r1.close()     // Catch: java.lang.Throwable -> Lcb
        Lca:
            throw r2     // Catch: java.lang.Throwable -> Lcb
        Lcb:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lcb
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.onetrack.b.f.run():void");
    }
}
