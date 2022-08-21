package com.baidu.mapsdkplatform.comapi.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class g {
    private static volatile g a;
    private boolean b = false;
    private boolean c = true;
    private final List<f> d = new ArrayList();
    private f e = null;

    private g() {
    }

    public static g a() {
        if (a == null) {
            synchronized (g.class) {
                if (a == null) {
                    a = new g();
                }
            }
        }
        return a;
    }

    private boolean a(String str) {
        boolean z = false;
        try {
            File file = new File(str + "/test.0");
            if (file.exists()) {
                file.delete();
            }
            z = file.createNewFile();
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x008a A[Catch: Exception -> 0x0095, TRY_LEAVE, TryCatch #2 {Exception -> 0x0095, blocks: (B:10:0x0023, B:12:0x002b, B:13:0x0033, B:15:0x0039, B:17:0x004e, B:19:0x0054, B:21:0x005c, B:22:0x0062, B:24:0x0068, B:26:0x0074, B:32:0x0086, B:34:0x008a, B:28:0x0079, B:31:0x0080), top: B:59:0x0023 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(android.content.Context r9) {
        /*
            Method dump skipped, instructions count: 251
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapsdkplatform.comapi.util.g.a(android.content.Context):void");
    }

    public boolean a(Context context, f fVar) {
        String a2 = fVar.a();
        if (!a(a2)) {
            return false;
        }
        SharedPreferences.Editor edit = context.getSharedPreferences("map_pref", 0).edit();
        edit.putString("PREFFERED_SD_CARD", a2);
        return edit.commit();
    }

    public f b() {
        return this.e;
    }

    public f b(Context context) {
        String string = context.getSharedPreferences("map_pref", 0).getString("PREFFERED_SD_CARD", "");
        if (string == null || string.length() <= 0) {
            return null;
        }
        for (f fVar : this.d) {
            if (fVar.a().equals(string)) {
                return fVar;
            }
        }
        return null;
    }
}
