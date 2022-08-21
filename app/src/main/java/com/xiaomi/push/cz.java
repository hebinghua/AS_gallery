package com.xiaomi.push;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public abstract class cz {

    /* loaded from: classes3.dex */
    public static class a extends cy {
        public a() {
            super(1);
        }

        @Override // com.xiaomi.push.cy
        public String a(Context context, String str, List<bi> list) {
            URL url;
            if (list == null) {
                url = new URL(str);
            } else {
                Uri.Builder buildUpon = Uri.parse(str).buildUpon();
                for (bi biVar : list) {
                    buildUpon.appendQueryParameter(biVar.a(), biVar.b());
                }
                url = new URL(buildUpon.toString());
            }
            return bj.a(context, url);
        }
    }

    public static int a(int i, int i2) {
        return (((i2 + 243) / 1448) * BaiduSceneResult.VARIOUS_TICKETS) + 1080 + i + i2;
    }

    public static int a(int i, int i2, int i3) {
        return (((i2 + 200) / 1448) * BaiduSceneResult.VARIOUS_TICKETS) + com.xiaomi.stat.c.b.k + i2 + i + i3;
    }

    public static int a(cy cyVar, String str, List<bi> list, String str2) {
        if (cyVar.a() == 1) {
            return a(str.length(), a(str2));
        }
        if (cyVar.a() != 2) {
            return -1;
        }
        return a(str.length(), a(list), a(str2));
    }

    public static int a(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return str.getBytes(Keyczar.DEFAULT_ENCODING).length;
        } catch (UnsupportedEncodingException unused) {
            return 0;
        }
    }

    public static int a(List<bi> list) {
        int i = 0;
        for (bi biVar : list) {
            if (!TextUtils.isEmpty(biVar.a())) {
                i += biVar.a().length();
            }
            if (!TextUtils.isEmpty(biVar.b())) {
                i += biVar.b().length();
            }
        }
        return i * 2;
    }

    public static String a(Context context, String str, List<bi> list) {
        return a(context, str, list, new a(), true);
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00aa A[Catch: MalformedURLException -> 0x00c3, TRY_ENTER, TryCatch #4 {MalformedURLException -> 0x00c3, blocks: (B:4:0x000f, B:6:0x0016, B:8:0x0020, B:11:0x0027, B:13:0x002d, B:14:0x0030, B:15:0x0035, B:17:0x003b, B:19:0x0044, B:21:0x004c, B:49:0x00aa, B:50:0x00bc), top: B:64:0x000f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(android.content.Context r20, java.lang.String r21, java.util.List<com.xiaomi.push.bi> r22, com.xiaomi.push.cy r23, boolean r24) {
        /*
            r1 = r20
            r0 = r21
            r2 = r22
            r3 = r23
            boolean r4 = com.xiaomi.push.bj.b(r20)
            r5 = 0
            if (r4 == 0) goto Lc7
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch: java.net.MalformedURLException -> Lc3
            r4.<init>()     // Catch: java.net.MalformedURLException -> Lc3
            if (r24 == 0) goto L26
            com.xiaomi.push.cv r6 = com.xiaomi.push.cv.a()     // Catch: java.net.MalformedURLException -> Lc3
            com.xiaomi.push.cr r6 = r6.m2026a(r0)     // Catch: java.net.MalformedURLException -> Lc3
            if (r6 == 0) goto L24
            java.util.ArrayList r4 = r6.a(r0)     // Catch: java.net.MalformedURLException -> Lc3
        L24:
            r13 = r6
            goto L27
        L26:
            r13 = r5
        L27:
            boolean r6 = r4.contains(r0)     // Catch: java.net.MalformedURLException -> Lc3
            if (r6 != 0) goto L30
            r4.add(r0)     // Catch: java.net.MalformedURLException -> Lc3
        L30:
            java.util.Iterator r4 = r4.iterator()     // Catch: java.net.MalformedURLException -> Lc3
            r6 = r5
        L35:
            boolean r0 = r4.hasNext()     // Catch: java.net.MalformedURLException -> Lc3
            if (r0 == 0) goto Lc2
            java.lang.Object r0 = r4.next()     // Catch: java.net.MalformedURLException -> Lc3
            r14 = r0
            java.lang.String r14 = (java.lang.String) r14     // Catch: java.net.MalformedURLException -> Lc3
            if (r2 == 0) goto L4b
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.net.MalformedURLException -> Lc3
            r0.<init>(r2)     // Catch: java.net.MalformedURLException -> Lc3
            r15 = r0
            goto L4c
        L4b:
            r15 = r5
        L4c:
            long r16 = java.lang.System.currentTimeMillis()     // Catch: java.net.MalformedURLException -> Lc3
            boolean r0 = r3.m2035a(r1, r14, r15)     // Catch: java.io.IOException -> La4
            if (r0 != 0) goto L58
            goto Lc2
        L58:
            java.lang.String r12 = r3.a(r1, r14, r15)     // Catch: java.io.IOException -> La4
            boolean r0 = android.text.TextUtils.isEmpty(r12)     // Catch: java.io.IOException -> L9a
            if (r0 != 0) goto L7c
            if (r13 == 0) goto L7a
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.io.IOException -> L75
            long r8 = r6 - r16
            int r0 = a(r3, r14, r15, r12)     // Catch: java.io.IOException -> L75
            long r10 = (long) r0     // Catch: java.io.IOException -> L75
            r6 = r13
            r7 = r14
            r6.a(r7, r8, r10)     // Catch: java.io.IOException -> L75
            goto L7a
        L75:
            r0 = move-exception
            r18 = r0
            r0 = r12
            goto La8
        L7a:
            r6 = r12
            goto Lc2
        L7c:
            if (r13 == 0) goto L95
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.io.IOException -> L9a
            long r8 = r6 - r16
            int r0 = a(r3, r14, r15, r12)     // Catch: java.io.IOException -> L9a
            long r10 = (long) r0
            r0 = 0
            r6 = r13
            r7 = r14
            r18 = r12
            r12 = r0
            r6.a(r7, r8, r10, r12)     // Catch: java.io.IOException -> L93
            goto L97
        L93:
            r0 = move-exception
            goto L9d
        L95:
            r18 = r12
        L97:
            r6 = r18
            goto L35
        L9a:
            r0 = move-exception
            r18 = r12
        L9d:
            r19 = r18
            r18 = r0
            r0 = r19
            goto La8
        La4:
            r0 = move-exception
            r18 = r0
            r0 = r6
        La8:
            if (r13 == 0) goto Lbc
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.net.MalformedURLException -> Lc3
            long r8 = r6 - r16
            int r6 = a(r3, r14, r15, r0)     // Catch: java.net.MalformedURLException -> Lc3
            long r10 = (long) r6     // Catch: java.net.MalformedURLException -> Lc3
            r6 = r13
            r7 = r14
            r12 = r18
            r6.a(r7, r8, r10, r12)     // Catch: java.net.MalformedURLException -> Lc3
        Lbc:
            r18.printStackTrace()     // Catch: java.net.MalformedURLException -> Lc3
            r6 = r0
            goto L35
        Lc2:
            return r6
        Lc3:
            r0 = move-exception
            r0.printStackTrace()
        Lc7:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.cz.a(android.content.Context, java.lang.String, java.util.List, com.xiaomi.push.cy, boolean):java.lang.String");
    }
}
