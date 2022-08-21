package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.xiaomi.onetrack.util.p;

/* loaded from: classes3.dex */
public class l {
    public String a(Context context) {
        String str = "";
        try {
            Cursor query = context.getContentResolver().query(Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID"), null, null, null, null);
            if (query != null) {
                if (query.moveToNext()) {
                    str = query.getString(query.getColumnIndex("value"));
                }
                query.close();
            }
        } catch (Exception e) {
            p.a("VivoDeviceIDHelper", e.getMessage());
        }
        return str;
    }
}
