package com.miui.gallery.picker.helper;

import android.text.TextUtils;
import com.miui.gallery.provider.InternalContract$ShareImageMedia;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class PickerSqlHelper {
    public static String buildPickerResultQuery(String[] strArr, String str, String[] strArr2, String str2, String str3) {
        if (strArr == null) {
            throw new IllegalArgumentException("projection can not be null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM(");
        sb.append(buildQueryInTable(getQueryShareMediaTable(), strArr, str, strArr2));
        sb.append(!TextUtils.isEmpty(str2) ? " UNION ALL " : " UNION ");
        sb.append(buildQueryInTable("cloud", strArr, str, strArr2));
        sb.append(")");
        if (!TextUtils.isEmpty(str2)) {
            sb.append(" GROUP BY ");
            sb.append(str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            sb.append(" ORDER BY ");
            sb.append(str3);
        }
        return sb.toString();
    }

    public static String getQueryShareMediaTable() {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            acquire.append("(");
            acquire.append(InternalContract$ShareImageMedia.SQL_QUERY_SHARE_IMAGE);
            acquire.append(")");
            return acquire.toString();
        } finally {
            Pools.getStringBuilderPool().release(acquire);
        }
    }

    public static String buildQueryInTable(String str, String[] strArr, String str2, String[] strArr2) {
        StringBuilder sb = new StringBuilder();
        String join = TextUtils.join(",", strArr);
        sb.append("SELECT ");
        sb.append(join);
        sb.append(" FROM ");
        sb.append(str);
        if (!TextUtils.isEmpty(str2)) {
            if (strArr2 != null && strArr2.length > 0) {
                str2 = String.format(str2, strArr2);
            }
            sb.append(" WHERE ");
            sb.append(str2);
        }
        return sb.toString();
    }
}
