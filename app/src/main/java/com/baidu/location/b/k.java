package com.baidu.location.b;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.baidu.location.Jni;
import com.xiaomi.stat.MiStat;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class k {
    private static k d;
    private static Object c = new Object();
    private static final String e = com.baidu.location.e.j.h() + "/hst.db";
    private SQLiteDatabase f = null;
    private boolean g = false;
    public a a = null;
    public a b = null;
    private String h = null;
    private int i = -2;

    /* loaded from: classes.dex */
    public class a extends com.baidu.location.e.e {
        private String b = null;
        private String c = null;
        private boolean d = true;
        private boolean e = false;

        public a() {
            this.j = new HashMap();
        }

        @Override // com.baidu.location.e.e
        public void a() {
            this.h = 1;
            this.g = com.baidu.location.e.j.d();
            String encodeTp4 = Jni.encodeTp4(this.c);
            this.c = null;
            this.j.put("bloc", encodeTp4);
        }

        public void a(String str, String str2) {
            if (!k.this.g) {
                k.this.g = true;
                this.b = str;
                this.c = str2;
                ExecutorService c = u.a().c();
                if (c != null) {
                    a(c, com.baidu.location.e.j.f);
                } else {
                    b(com.baidu.location.e.j.f);
                }
            }
        }

        @Override // com.baidu.location.e.e
        public void a(boolean z) {
            String str;
            if (z && (str = this.i) != null) {
                try {
                    if (this.d) {
                        JSONObject jSONObject = new JSONObject(str);
                        JSONObject jSONObject2 = jSONObject.has(MiStat.Param.CONTENT) ? jSONObject.getJSONObject(MiStat.Param.CONTENT) : null;
                        if (jSONObject2 != null && jSONObject2.has("imo")) {
                            Long valueOf = Long.valueOf(jSONObject2.getJSONObject("imo").getString("mac"));
                            int i = jSONObject2.getJSONObject("imo").getInt("mv");
                            if (Jni.encode3(this.b).longValue() == valueOf.longValue()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("tt", Integer.valueOf((int) (System.currentTimeMillis() / 1000)));
                                contentValues.put("hst", Integer.valueOf(i));
                                try {
                                    SQLiteDatabase sQLiteDatabase = k.this.f;
                                    if (sQLiteDatabase.update("hstdata", contentValues, "id = \"" + valueOf + "\"", null) <= 0) {
                                        contentValues.put("id", valueOf);
                                        k.this.f.insert("hstdata", null, contentValues);
                                    }
                                } catch (Exception unused) {
                                }
                                Bundle bundle = new Bundle();
                                bundle.putByteArray("mac", this.b.getBytes());
                                bundle.putInt("hotspot", i);
                                k.this.a(bundle);
                            }
                        }
                    }
                } catch (Exception unused2) {
                }
            } else if (this.d) {
                k.this.f();
            }
            Map<String, Object> map = this.j;
            if (map != null) {
                map.clear();
            }
            k.this.g = false;
        }
    }

    public static k a() {
        k kVar;
        synchronized (c) {
            if (d == null) {
                d = new k();
            }
            kVar = d;
        }
        return kVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String a(boolean r5) {
        /*
            r4 = this;
            com.baidu.location.c.b r0 = com.baidu.location.c.b.a()
            com.baidu.location.c.a r0 = r0.f()
            com.baidu.location.c.i r1 = com.baidu.location.c.i.a()
            com.baidu.location.c.h r1 = r1.o()
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r3 = 1024(0x400, float:1.435E-42)
            r2.<init>(r3)
            if (r0 == 0) goto L26
            boolean r3 = r0.b()
            if (r3 == 0) goto L26
            java.lang.String r0 = r0.h()
            r2.append(r0)
        L26:
            if (r1 == 0) goto L36
            int r0 = r1.a()
            r3 = 1
            if (r0 <= r3) goto L36
            r0 = 15
            java.lang.String r0 = r1.a(r0)
            goto L48
        L36:
            com.baidu.location.c.i r0 = com.baidu.location.c.i.a()
            java.lang.String r0 = r0.l()
            if (r0 == 0) goto L4b
            com.baidu.location.c.i r0 = com.baidu.location.c.i.a()
            java.lang.String r0 = r0.l()
        L48:
            r2.append(r0)
        L4b:
            if (r5 == 0) goto L52
            java.lang.String r5 = "&imo=1"
            r2.append(r5)
        L52:
            com.baidu.location.c.f r5 = com.baidu.location.c.f.a()
            java.lang.String r5 = r5.m()
            r2.append(r5)
            com.baidu.location.e.b r5 = com.baidu.location.e.b.a()
            r0 = 0
            java.lang.String r5 = r5.a(r0)
            r2.append(r5)
            com.baidu.location.b.b r5 = com.baidu.location.b.b.a()
            java.lang.String r5 = r5.c()
            r2.append(r5)
            java.lang.String r5 = r2.toString()
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.k.a(boolean):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Bundle bundle) {
        b.a().a(bundle, 406);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        Bundle bundle = new Bundle();
        bundle.putInt("hotspot", -1);
        a(bundle);
    }

    public void a(String str) {
        if (this.g) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = jSONObject.has(MiStat.Param.CONTENT) ? jSONObject.getJSONObject(MiStat.Param.CONTENT) : null;
            if (jSONObject2 == null || !jSONObject2.has("imo")) {
                return;
            }
            Long valueOf = Long.valueOf(jSONObject2.getJSONObject("imo").getString("mac"));
            int i = jSONObject2.getJSONObject("imo").getInt("mv");
            ContentValues contentValues = new ContentValues();
            contentValues.put("tt", Integer.valueOf((int) (System.currentTimeMillis() / 1000)));
            contentValues.put("hst", Integer.valueOf(i));
            SQLiteDatabase sQLiteDatabase = this.f;
            if (sQLiteDatabase.update("hstdata", contentValues, "id = \"" + valueOf + "\"", null) > 0) {
                return;
            }
            contentValues.put("id", valueOf);
            this.f.insert("hstdata", null, contentValues);
        } catch (Exception unused) {
        }
    }

    public void b() {
        try {
            File file = new File(e);
            if (!file.exists()) {
                file.createNewFile();
            }
            if (!file.exists()) {
                return;
            }
            SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(file, (SQLiteDatabase.CursorFactory) null);
            this.f = openOrCreateDatabase;
            openOrCreateDatabase.execSQL("CREATE TABLE IF NOT EXISTS hstdata(id Long PRIMARY KEY,hst INT,tt INT);");
            this.f.setVersion(1);
        } catch (Exception unused) {
            this.f = null;
        }
    }

    public void c() {
        SQLiteDatabase sQLiteDatabase = this.f;
        if (sQLiteDatabase != null) {
            try {
                sQLiteDatabase.close();
            } catch (Exception unused) {
            } catch (Throwable th) {
                this.f = null;
                throw th;
            }
            this.f = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0077, code lost:
        if (r3 != null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0079, code lost:
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0084, code lost:
        if (r3 != null) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int d() {
        /*
            r8 = this;
            monitor-enter(r8)
            r0 = -3
            boolean r1 = r8.g     // Catch: java.lang.Throwable -> L8b
            if (r1 == 0) goto L8
            monitor-exit(r8)
            return r0
        L8:
            com.baidu.location.c.i r1 = com.baidu.location.c.i.a()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            boolean r1 = r1.i()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            if (r1 == 0) goto L87
            android.database.sqlite.SQLiteDatabase r1 = r8.f     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            if (r1 == 0) goto L87
            com.baidu.location.c.i r1 = com.baidu.location.c.i.a()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            android.net.wifi.WifiInfo r1 = r1.k()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            if (r1 == 0) goto L87
            java.lang.String r2 = r1.getBSSID()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            if (r2 == 0) goto L87
            java.lang.String r1 = r1.getBSSID()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            java.lang.String r2 = ":"
            java.lang.String r3 = ""
            java.lang.String r1 = r1.replace(r2, r3)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            java.lang.Long r2 = com.baidu.location.Jni.encode3(r1)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            java.lang.String r3 = r8.h     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            r4 = -2
            if (r3 == 0) goto L47
            boolean r3 = r1.equals(r3)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            if (r3 == 0) goto L47
            int r3 = r8.i     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            if (r3 <= r4) goto L47
            r0 = r3
            goto L87
        L47:
            r3 = 0
            android.database.sqlite.SQLiteDatabase r5 = r8.f     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            r6.<init>()     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            java.lang.String r7 = "select * from hstdata where id = \""
            r6.append(r7)     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            r6.append(r2)     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            java.lang.String r2 = "\";"
            r6.append(r2)     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            java.lang.String r2 = r6.toString()     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            android.database.Cursor r3 = r5.rawQuery(r2, r3)     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            if (r3 == 0) goto L76
            boolean r2 = r3.moveToFirst()     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            if (r2 == 0) goto L76
            r2 = 1
            int r0 = r3.getInt(r2)     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            r8.h = r1     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            r8.i = r0     // Catch: java.lang.Throwable -> L7d java.lang.Exception -> L84
            goto L77
        L76:
            r0 = r4
        L77:
            if (r3 == 0) goto L87
        L79:
            r3.close()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
            goto L87
        L7d:
            r1 = move-exception
            if (r3 == 0) goto L83
            r3.close()     // Catch: java.lang.Exception -> L83 java.lang.Throwable -> L8b
        L83:
            throw r1     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L8b
        L84:
            if (r3 == 0) goto L87
            goto L79
        L87:
            r8.i = r0     // Catch: java.lang.Throwable -> L8b
            monitor-exit(r8)
            return r0
        L8b:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.k.d():int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x008c, code lost:
        if (r3 == null) goto L26;
     */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009e A[Catch: Exception -> 0x00b8, TryCatch #4 {Exception -> 0x00b8, blocks: (B:5:0x0005, B:7:0x000f, B:9:0x0013, B:11:0x001d, B:13:0x0023, B:35:0x009e, B:37:0x00a2, B:38:0x00a9, B:40:0x00ad, B:31:0x0098, B:41:0x00b5, B:15:0x0036, B:17:0x0054, B:19:0x005a, B:22:0x0074), top: B:50:0x0005, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void e() {
        /*
            r10 = this;
            boolean r0 = r10.g
            if (r0 == 0) goto L5
            return
        L5:
            com.baidu.location.c.i r0 = com.baidu.location.c.i.a()     // Catch: java.lang.Exception -> Lb8
            boolean r0 = r0.i()     // Catch: java.lang.Exception -> Lb8
            if (r0 == 0) goto Lb5
            android.database.sqlite.SQLiteDatabase r0 = r10.f     // Catch: java.lang.Exception -> Lb8
            if (r0 == 0) goto Lb5
            com.baidu.location.c.i r0 = com.baidu.location.c.i.a()     // Catch: java.lang.Exception -> Lb8
            android.net.wifi.WifiInfo r0 = r0.k()     // Catch: java.lang.Exception -> Lb8
            if (r0 == 0) goto Lb5
            java.lang.String r1 = r0.getBSSID()     // Catch: java.lang.Exception -> Lb8
            if (r1 == 0) goto Lb5
            java.lang.String r0 = r0.getBSSID()     // Catch: java.lang.Exception -> Lb8
            java.lang.String r1 = ":"
            java.lang.String r2 = ""
            java.lang.String r0 = r0.replace(r1, r2)     // Catch: java.lang.Exception -> Lb8
            java.lang.Long r1 = com.baidu.location.Jni.encode3(r0)     // Catch: java.lang.Exception -> Lb8
            r2 = 0
            r3 = 0
            r4 = 1
            android.database.sqlite.SQLiteDatabase r5 = r10.f     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            r6.<init>()     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            java.lang.String r7 = "select * from hstdata where id = \""
            r6.append(r7)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            r6.append(r1)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            java.lang.String r1 = "\";"
            r6.append(r1)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            java.lang.String r1 = r6.toString()     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            android.database.Cursor r3 = r5.rawQuery(r1, r3)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            if (r3 == 0) goto L8b
            boolean r1 = r3.moveToFirst()     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            if (r1 == 0) goto L8b
            int r1 = r3.getInt(r4)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            r5 = 2
            int r5 = r3.getInt(r5)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            r8 = 1000(0x3e8, double:4.94E-321)
            long r6 = r6 / r8
            long r8 = (long) r5     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            long r6 = r6 - r8
            r8 = 259200(0x3f480, double:1.28062E-318)
            int r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r5 <= 0) goto L74
            goto L8b
        L74:
            android.os.Bundle r5 = new android.os.Bundle     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            r5.<init>()     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            java.lang.String r6 = "mac"
            byte[] r7 = r0.getBytes()     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            r5.putByteArray(r6, r7)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            java.lang.String r6 = "hotspot"
            r5.putInt(r6, r1)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            r10.a(r5)     // Catch: java.lang.Throwable -> L92 java.lang.Exception -> L99
            goto L8c
        L8b:
            r2 = r4
        L8c:
            if (r3 == 0) goto L9c
        L8e:
            r3.close()     // Catch: java.lang.Exception -> L9c
            goto L9c
        L92:
            r0 = move-exception
            if (r3 == 0) goto L98
            r3.close()     // Catch: java.lang.Exception -> L98
        L98:
            throw r0     // Catch: java.lang.Exception -> Lb8
        L99:
            if (r3 == 0) goto L9c
            goto L8e
        L9c:
            if (r2 == 0) goto Lb8
            com.baidu.location.b.k$a r1 = r10.a     // Catch: java.lang.Exception -> Lb8
            if (r1 != 0) goto La9
            com.baidu.location.b.k$a r1 = new com.baidu.location.b.k$a     // Catch: java.lang.Exception -> Lb8
            r1.<init>()     // Catch: java.lang.Exception -> Lb8
            r10.a = r1     // Catch: java.lang.Exception -> Lb8
        La9:
            com.baidu.location.b.k$a r1 = r10.a     // Catch: java.lang.Exception -> Lb8
            if (r1 == 0) goto Lb8
            java.lang.String r2 = r10.a(r4)     // Catch: java.lang.Exception -> Lb8
            r1.a(r0, r2)     // Catch: java.lang.Exception -> Lb8
            goto Lb8
        Lb5:
            r10.f()     // Catch: java.lang.Exception -> Lb8
        Lb8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.k.e():void");
    }
}
