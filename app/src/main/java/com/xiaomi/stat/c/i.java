package com.xiaomi.stat.c;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.j;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.m;
import com.xiaomi.stat.d.r;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class i {
    private static final String a = "3.0";
    private static final String b = "UploaderEngine";
    private static final String c = "code";
    private static final String d = "UTF-8";
    private static final String e = "mistat";
    private static final String f = "uploader";
    private static final String g = "3.0.16";
    private static final String h = "Android";
    private static final int i = 200;
    private static final int j = 1;
    private static final int k = -1;
    private static final int l = 3;
    private static volatile i m;
    private final byte[] n = new byte[0];
    private FileLock o;
    private FileChannel p;
    private g q;
    private a r;

    private int a(int i2) {
        if (i2 == 1) {
            return -1;
        }
        return i2 == 3 ? 0 : 1;
    }

    public static i a() {
        if (m == null) {
            synchronized (i.class) {
                if (m == null) {
                    m = new i();
                }
            }
        }
        return m;
    }

    private i() {
        e();
    }

    private void e() {
        HandlerThread handlerThread = new HandlerThread("mi_analytics_uploader_worker");
        handlerThread.start();
        this.r = new a(handlerThread.getLooper());
        this.q = new g(handlerThread.getLooper());
    }

    public void b() {
        this.q.b();
        c();
    }

    public void c() {
        if (!l.a()) {
            f();
        } else if (!com.xiaomi.stat.b.a() || !com.xiaomi.stat.b.b()) {
            k.b(b, " postToServer statistic disable or network disable access! ");
        } else if (!com.xiaomi.stat.b.B()) {
            k.b(b, " postToServer can not upload data because of configuration!");
        } else {
            Message obtain = Message.obtain();
            obtain.what = 1;
            a(obtain);
        }
    }

    private void f() {
        g gVar = this.q;
        if (gVar != null) {
            gVar.c();
        }
    }

    private void a(Message message) {
        synchronized (this.n) {
            if (this.r == null || this.q == null) {
                e();
            }
            this.r.sendMessage(message);
        }
    }

    public static byte[] a(String str) {
        GZIPOutputStream gZIPOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        byte[] bArr = null;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream(str.getBytes("UTF-8").length);
            } catch (Throwable th) {
                th = th;
            }
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    gZIPOutputStream.write(str.getBytes("UTF-8"));
                    gZIPOutputStream.finish();
                    bArr = byteArrayOutputStream.toByteArray();
                } catch (Exception e2) {
                    e = e2;
                    k.e(b, " zipData failed! " + e.toString());
                    j.a((OutputStream) byteArrayOutputStream);
                    j.a((OutputStream) gZIPOutputStream);
                    return bArr;
                }
            } catch (Exception e3) {
                e = e3;
                gZIPOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                gZIPOutputStream = null;
                byteArrayOutputStream2 = byteArrayOutputStream;
                j.a((OutputStream) byteArrayOutputStream2);
                j.a((OutputStream) gZIPOutputStream);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            byteArrayOutputStream = null;
            gZIPOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            gZIPOutputStream = null;
            j.a((OutputStream) byteArrayOutputStream2);
            j.a((OutputStream) gZIPOutputStream);
            throw th;
        }
        j.a((OutputStream) byteArrayOutputStream);
        j.a((OutputStream) gZIPOutputStream);
        return bArr;
    }

    private byte[] a(byte[] bArr) {
        return com.xiaomi.stat.b.i.a().a(bArr);
    }

    private String b(byte[] bArr) {
        return com.xiaomi.stat.d.d.a(bArr);
    }

    /* loaded from: classes3.dex */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                i.this.g();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        if (!i()) {
            return;
        }
        if (com.xiaomi.stat.b.e()) {
            b(true);
            b(false);
        } else {
            a(c(false), com.xiaomi.stat.b.d.a().c());
        }
        j();
    }

    private void b(boolean z) {
        a(c(z), com.xiaomi.stat.b.d.a().a(z));
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0087  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(com.xiaomi.stat.a.b[] r11, java.lang.String r12) {
        /*
            r10 = this;
            int r0 = r11.length
            java.lang.String r1 = "UploaderEngine"
            if (r0 != 0) goto Lb
            java.lang.String r11 = "privacy policy or network state not matched"
            com.xiaomi.stat.d.k.e(r1, r11)
            return
        Lb:
            com.xiaomi.stat.a.c r0 = com.xiaomi.stat.a.c.a()
            com.xiaomi.stat.a.k r0 = r0.a(r11)
            java.util.concurrent.atomic.AtomicInteger r2 = new java.util.concurrent.atomic.AtomicInteger
            r2.<init>()
            r3 = 1
            if (r0 == 0) goto L1e
            boolean r4 = r0.c
            goto L1f
        L1e:
            r4 = r3
        L1f:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r1)
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            com.xiaomi.stat.d.k.b(r5)
            r5 = 0
            r6 = r5
        L33:
            if (r0 == 0) goto Lc8
            java.util.ArrayList<java.lang.Long> r6 = r0.b
            org.json.JSONArray r0 = r0.a
            java.lang.String r0 = r10.a(r0, r12)     // Catch: java.lang.Exception -> L7c
            java.lang.String r7 = " payload:"
            com.xiaomi.stat.d.k.a(r1, r7, r0)     // Catch: java.lang.Exception -> L7c
            byte[] r0 = a(r0)     // Catch: java.lang.Exception -> L7c
            byte[] r0 = r10.a(r0)     // Catch: java.lang.Exception -> L7c
            java.lang.String r0 = r10.b(r0)     // Catch: java.lang.Exception -> L7c
            java.lang.String r7 = " encodePayload "
            com.xiaomi.stat.d.k.a(r1, r7, r0)     // Catch: java.lang.Exception -> L7c
            com.xiaomi.stat.b.g r7 = com.xiaomi.stat.b.g.a()     // Catch: java.lang.Exception -> L7c
            java.lang.String r7 = r7.c()     // Catch: java.lang.Exception -> L7c
            boolean r8 = com.xiaomi.stat.d.k.b()     // Catch: java.lang.Exception -> L7c
            if (r8 == 0) goto L63
            java.lang.String r7 = "http://test.data.mistat.xiaomi.srv/mistats/v3"
        L63:
            java.util.HashMap r0 = r10.c(r0)     // Catch: java.lang.Exception -> L7c
            java.lang.String r0 = com.xiaomi.stat.c.c.a(r7, r0, r3)     // Catch: java.lang.Exception -> L7c
            java.lang.String r7 = " sendDataToServer response: "
            com.xiaomi.stat.d.k.a(r1, r7, r0)     // Catch: java.lang.Exception -> L7c
            boolean r7 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Exception -> L7c
            if (r7 == 0) goto L77
            goto L7c
        L77:
            boolean r0 = r10.b(r0)     // Catch: java.lang.Exception -> L7c
            goto L7d
        L7c:
            r0 = r5
        L7d:
            if (r0 == 0) goto L87
            com.xiaomi.stat.a.c r7 = com.xiaomi.stat.a.c.a()
            r7.a(r6)
            goto L8a
        L87:
            r2.addAndGet(r3)
        L8a:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " deleteData= "
            r6.append(r7)
            r6.append(r0)
            java.lang.String r7 = " retryCount.get()= "
            r6.append(r7)
            int r7 = r2.get()
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            com.xiaomi.stat.d.k.b(r1, r6)
            if (r4 != 0) goto Lc7
            if (r0 != 0) goto Lb6
            int r6 = r2.get()
            r7 = 3
            if (r6 <= r7) goto Lb6
            goto Lc7
        Lb6:
            com.xiaomi.stat.a.c r6 = com.xiaomi.stat.a.c.a()
            com.xiaomi.stat.a.k r6 = r6.a(r11)
            if (r6 == 0) goto Lc2
            boolean r4 = r6.c
        Lc2:
            r9 = r6
            r6 = r0
            r0 = r9
            goto L33
        Lc7:
            r6 = r0
        Lc8:
            com.xiaomi.stat.c.g r11 = r10.q
            if (r11 == 0) goto Lcf
            r11.b(r6)
        Lcf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.stat.c.i.a(com.xiaomi.stat.a.b[], java.lang.String):void");
    }

    private boolean b(String str) {
        try {
            int optInt = new JSONObject(str).optInt(c);
            if (optInt != 200) {
                if (optInt != 1002 && optInt != 1004 && optInt != 1005 && optInt != 1006 && optInt != 1007 && optInt != 1011) {
                    if (optInt == 2002 || optInt == 1012) {
                        com.xiaomi.stat.b.i.a().a(true);
                        com.xiaomi.stat.b.d.a().b();
                    }
                }
                com.xiaomi.stat.b.i.a().a(true);
                com.xiaomi.stat.b.d.a().b();
                return false;
            }
            return true;
        } catch (Exception e2) {
            k.d(b, "parseUploadingResult exception ", e2);
            return false;
        }
    }

    private com.xiaomi.stat.a.b[] c(boolean z) {
        ArrayList<String> h2 = h();
        int size = h2.size();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < size; i2++) {
            String str = h2.get(i2);
            int a2 = a(new f(str, z).a());
            if (a2 != -1) {
                arrayList.add(new com.xiaomi.stat.a.b(str, a2, z));
            }
        }
        com.xiaomi.stat.a.b d2 = d(z);
        if (d2 != null) {
            arrayList.add(d2);
        }
        return (com.xiaomi.stat.a.b[]) arrayList.toArray(new com.xiaomi.stat.a.b[arrayList.size()]);
    }

    private com.xiaomi.stat.a.b d(boolean z) {
        int a2 = new f(z).a();
        k.b(b, " createMainAppFilter: " + a2);
        int a3 = a(a2);
        if (a3 != -1) {
            return new com.xiaomi.stat.a.b(null, a3, z);
        }
        return null;
    }

    private ArrayList<String> h() {
        String[] o = com.xiaomi.stat.b.o();
        int length = o != null ? o.length : 0;
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i2 = 0; i2 < length; i2++) {
            if (!TextUtils.isEmpty(o[i2])) {
                arrayList.add(o[i2]);
            }
        }
        return arrayList;
    }

    private HashMap<String, String> c(String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ai", ak.b());
        hashMap.put(com.xiaomi.stat.d.b, "3.0.16");
        hashMap.put(com.xiaomi.stat.d.c, a);
        hashMap.put(com.xiaomi.stat.d.d, m.g());
        hashMap.put(com.xiaomi.stat.d.e, str);
        hashMap.put(com.xiaomi.stat.d.ak, com.xiaomi.stat.b.i.a().c());
        hashMap.put(com.xiaomi.stat.d.g, com.xiaomi.stat.b.i.a().b());
        return hashMap;
    }

    private String a(JSONArray jSONArray, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", str);
            a(str, jSONObject);
            jSONObject.put(com.xiaomi.stat.d.I, com.xiaomi.stat.d.e.d());
            jSONObject.put("rc", m.h());
            jSONObject.put(com.xiaomi.stat.d.j, com.xiaomi.stat.d.c.b());
            jSONObject.put(com.xiaomi.stat.d.k, com.xiaomi.stat.b.t());
            jSONObject.put(com.xiaomi.stat.d.l, h);
            jSONObject.put(com.xiaomi.stat.d.Z, m.a(ak.a()));
            g gVar = this.q;
            jSONObject.put(com.xiaomi.stat.d.m, gVar != null ? gVar.a() : 0L);
            jSONObject.put(com.xiaomi.stat.d.n, String.valueOf(r.b()));
            jSONObject.put(com.xiaomi.stat.d.o, m.e());
            jSONObject.put(com.xiaomi.stat.d.p, com.xiaomi.stat.c.a.a(ak.b()));
            String[] o = com.xiaomi.stat.b.o();
            if (o != null && o.length > 0) {
                jSONObject.put(com.xiaomi.stat.d.v, a(o));
            }
            jSONObject.put(com.xiaomi.stat.d.q, m.d());
            jSONObject.put("n", l.b(ak.a()));
            jSONObject.put("ud", com.xiaomi.stat.b.h());
            jSONObject.put(com.xiaomi.stat.d.u, jSONArray);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString();
    }

    private void a(String str, JSONObject jSONObject) {
        try {
            if (com.xiaomi.stat.b.e() || !TextUtils.isEmpty(str)) {
                return;
            }
            Context a2 = ak.a();
            jSONObject.put(com.xiaomi.stat.d.C, com.xiaomi.stat.d.e.b(a2));
            jSONObject.put(com.xiaomi.stat.d.J, com.xiaomi.stat.d.e.k(a2));
            jSONObject.put(com.xiaomi.stat.d.L, com.xiaomi.stat.d.e.n(a2));
            jSONObject.put(com.xiaomi.stat.d.O, com.xiaomi.stat.d.e.q(a2));
            jSONObject.put("ai", com.xiaomi.stat.d.e.p(a2));
        } catch (Exception unused) {
        }
    }

    private JSONArray a(String[] strArr) {
        JSONArray jSONArray = new JSONArray();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(strArr[i2], com.xiaomi.stat.c.a.a(strArr[i2]));
                jSONArray.put(jSONObject);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return jSONArray;
    }

    public synchronized void d() {
        g gVar = this.q;
        if (gVar != null) {
            gVar.d();
        }
    }

    public void a(boolean z) {
        g gVar = this.q;
        if (gVar != null) {
            gVar.a(z);
        }
    }

    private boolean i() {
        File file = new File(ak.a().getFilesDir(), e);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            FileChannel channel = new FileOutputStream(new File(file, f)).getChannel();
            this.p = channel;
            try {
                try {
                    FileLock tryLock = channel.tryLock();
                    this.o = tryLock;
                    if (tryLock != null) {
                        k.c(b, " acquire lock for uploader");
                        if (this.o == null) {
                            try {
                                this.p.close();
                                this.p = null;
                            } catch (Exception unused) {
                            }
                        }
                        return true;
                    }
                    k.c(b, " acquire lock for uploader failed");
                    if (this.o == null) {
                        try {
                            this.p.close();
                            this.p = null;
                        } catch (Exception unused2) {
                        }
                    }
                    return false;
                } catch (Exception e2) {
                    k.c(b, " acquire lock for uploader failed with " + e2);
                    if (this.o == null) {
                        try {
                            this.p.close();
                            this.p = null;
                        } catch (Exception unused3) {
                        }
                    }
                    return false;
                }
            } catch (Throwable th) {
                if (this.o == null) {
                    try {
                        this.p.close();
                        this.p = null;
                    } catch (Exception unused4) {
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e3) {
            k.c(b, " acquire lock for uploader failed with " + e3);
            return false;
        }
    }

    private void j() {
        try {
            FileLock fileLock = this.o;
            if (fileLock != null) {
                fileLock.release();
                this.o = null;
            }
            FileChannel fileChannel = this.p;
            if (fileChannel != null) {
                fileChannel.close();
                this.p = null;
            }
            k.c(b, " releaseLock lock for uploader");
        } catch (IOException e2) {
            k.c(b, " releaseLock lock for uploader failed with " + e2);
        }
    }
}
