package com.xiaomi.push;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;

/* loaded from: classes3.dex */
public class bf implements au {
    public static String a = "content://com.vivo.vms.IdProvider/IdentifierId/";
    public static String b = a + "OAID";
    public static String c = a + "VAID_";
    public static String d = a + "AAID_";
    public static String e = a + "OAIDSTATUS";
    public static String f = "persist.sys.identifierid.supported";

    /* renamed from: a  reason: collision with other field name */
    public Context f128a;

    public bf(Context context) {
        this.f128a = context;
    }

    public static boolean a(Context context) {
        try {
            ProviderInfo resolveContentProvider = context.getPackageManager().resolveContentProvider(Uri.parse(a).getAuthority(), 128);
            if (resolveContentProvider != null) {
                if ((resolveContentProvider.applicationInfo.flags & 1) != 0) {
                    return true;
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public String mo1967a() {
        return a(b);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0028, code lost:
        if (r10 != null) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x002a, code lost:
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
        if (r10 == null) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003c, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.String a(java.lang.String r10) {
        /*
            r9 = this;
            r0 = 0
            android.content.Context r1 = r9.f128a     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L38
            android.content.ContentResolver r2 = r1.getContentResolver()     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L38
            android.net.Uri r3 = android.net.Uri.parse(r10)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L38
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r10 = r2.query(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L38
            if (r10 == 0) goto L28
            boolean r1 = r10.moveToNext()     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L39
            if (r1 == 0) goto L28
            java.lang.String r1 = "value"
            int r1 = r10.getColumnIndex(r1)     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L39
            java.lang.String r0 = r10.getString(r1)     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L39
            goto L28
        L26:
            r0 = move-exception
            goto L32
        L28:
            if (r10 == 0) goto L3c
        L2a:
            r10.close()
            goto L3c
        L2e:
            r10 = move-exception
            r8 = r0
            r0 = r10
            r10 = r8
        L32:
            if (r10 == 0) goto L37
            r10.close()
        L37:
            throw r0
        L38:
            r10 = r0
        L39:
            if (r10 == 0) goto L3c
            goto L2a
        L3c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.bf.a(java.lang.String):java.lang.String");
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo1967a() {
        return "1".equals(u.a(f, "0"));
    }
}
