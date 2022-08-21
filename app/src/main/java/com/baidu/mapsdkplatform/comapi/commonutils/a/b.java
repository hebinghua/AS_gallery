package com.baidu.mapsdkplatform.comapi.commonutils.a;

import com.baidu.mapsdkplatform.comapi.commonutils.a.c;
import java.io.File;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/* loaded from: classes.dex */
public class b implements Runnable {
    private String a;
    private File b;
    private com.baidu.mapsdkplatform.comapi.commonutils.a.a c;
    private c.b d;
    private volatile boolean e = false;

    /* loaded from: classes.dex */
    public static class a implements HostnameVerifier {
        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(str, sSLSession);
        }
    }

    public b(String str, File file, com.baidu.mapsdkplatform.comapi.commonutils.a.a aVar, c.b bVar) {
        this.a = str;
        this.b = file;
        this.c = aVar;
        this.d = bVar;
    }

    public boolean a() {
        return this.e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00cb A[Catch: IOException -> 0x00c7, TRY_LEAVE, TryCatch #1 {IOException -> 0x00c7, blocks: (B:37:0x00c3, B:41:0x00cb), top: B:47:0x00c3 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00c3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.io.RandomAccessFile, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v5, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r6 = this;
            r0 = 0
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.lang.String r3 = r6.a     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            boolean r3 = com.baidu.mapapi.http.HttpClient.isHttpsEnable     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            if (r3 == 0) goto L1c
            java.net.URLConnection r2 = r2.openConnection()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            javax.net.ssl.HttpsURLConnection r2 = (javax.net.ssl.HttpsURLConnection) r2     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            com.baidu.mapsdkplatform.comapi.commonutils.a.b$a r3 = new com.baidu.mapsdkplatform.comapi.commonutils.a.b$a     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r3.<init>()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r2.setHostnameVerifier(r3)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            goto L22
        L1c:
            java.net.URLConnection r2 = r2.openConnection()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
        L22:
            java.lang.String r3 = "GET"
            r2.setRequestMethod(r3)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r3 = 10000(0x2710, float:1.4013E-41)
            r2.setConnectTimeout(r3)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r2.setReadTimeout(r3)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.lang.String r3 = "Range"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r4.<init>()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.lang.String r5 = "bytes="
            r4.append(r5)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            com.baidu.mapsdkplatform.comapi.commonutils.a.a r5 = r6.c     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            int r5 = r5.a()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r4.append(r5)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.lang.String r5 = "-"
            r4.append(r5)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            com.baidu.mapsdkplatform.comapi.commonutils.a.a r5 = r6.c     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            int r5 = r5.b()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r4.append(r5)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            r2.setRequestProperty(r3, r4)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.lang.String r3 = "Connection"
            java.lang.String r4 = "Keep-Alive"
            r2.setRequestProperty(r3, r4)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.io.RandomAccessFile r3 = new java.io.RandomAccessFile     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.io.File r4 = r6.b     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            java.lang.String r5 = "rw"
            r3.<init>(r4, r5)     // Catch: java.lang.Throwable -> L9e java.lang.Exception -> La1
            com.baidu.mapsdkplatform.comapi.commonutils.a.a r4 = r6.c     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            int r4 = r4.a()     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            long r4 = (long) r4     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            r3.seek(r4)     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            int r4 = r2.getResponseCode()     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            r5 = 200(0xc8, float:2.8E-43)
            if (r4 == r5) goto L7f
            r5 = 206(0xce, float:2.89E-43)
            if (r4 != r5) goto L92
        L7f:
            r4 = 1048576(0x100000, float:1.469368E-39)
            byte[] r4 = new byte[r4]     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            java.io.InputStream r1 = r2.getInputStream()     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
        L87:
            int r2 = r1.read(r4)     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            r5 = -1
            if (r2 == r5) goto L92
            r3.write(r4, r0, r2)     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            goto L87
        L92:
            r2 = 1
            r6.e = r2     // Catch: java.lang.Exception -> La2 java.lang.Throwable -> Lc0
            if (r1 == 0) goto L9a
            r1.close()     // Catch: java.io.IOException -> Laf
        L9a:
            r3.close()     // Catch: java.io.IOException -> Laf
            goto Lba
        L9e:
            r0 = move-exception
            r3 = r1
            goto Lc1
        La1:
            r3 = r1
        La2:
            r6.e = r0     // Catch: java.lang.Throwable -> Lc0
            com.baidu.mapsdkplatform.comapi.commonutils.a.c$b r0 = r6.d     // Catch: java.lang.Throwable -> Lc0
            r0.b(r6)     // Catch: java.lang.Throwable -> Lc0
            if (r1 == 0) goto Lb1
            r1.close()     // Catch: java.io.IOException -> Laf
            goto Lb1
        Laf:
            r0 = move-exception
            goto Lb7
        Lb1:
            if (r3 == 0) goto Lba
            r3.close()     // Catch: java.io.IOException -> Laf
            goto Lba
        Lb7:
            r0.printStackTrace()
        Lba:
            com.baidu.mapsdkplatform.comapi.commonutils.a.c$b r0 = r6.d
            r0.a(r6)
            return
        Lc0:
            r0 = move-exception
        Lc1:
            if (r1 == 0) goto Lc9
            r1.close()     // Catch: java.io.IOException -> Lc7
            goto Lc9
        Lc7:
            r1 = move-exception
            goto Lcf
        Lc9:
            if (r3 == 0) goto Ld2
            r3.close()     // Catch: java.io.IOException -> Lc7
            goto Ld2
        Lcf:
            r1.printStackTrace()
        Ld2:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapsdkplatform.comapi.commonutils.a.b.run():void");
    }
}
