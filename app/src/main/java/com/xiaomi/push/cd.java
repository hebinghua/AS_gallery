package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.cj;

/* loaded from: classes3.dex */
public class cd extends cf {
    public cd(String str, String str2, String[] strArr, String str3) {
        super(str, str2, strArr, str3);
    }

    public static cd a(Context context, String str, int i) {
        com.xiaomi.channel.commonutils.logger.b.b("delete  messages when db size is too bigger");
        String m2012a = cj.a(context).m2012a(str);
        if (TextUtils.isEmpty(m2012a)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("rowDataId in (select ");
        sb.append("rowDataId from " + m2012a);
        sb.append(" order by createTimeStamp asc");
        sb.append(" limit ?)");
        return new cd(str, sb.toString(), new String[]{String.valueOf(i)}, "a job build to delete history message");
    }

    public final void a(long j) {
        String[] strArr = ((cj.d) this).f163a;
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        strArr[0] = String.valueOf(j);
    }

    @Override // com.xiaomi.push.cj.a
    public void a(Context context, Object obj) {
        if (obj instanceof Long) {
            long longValue = ((Long) obj).longValue();
            long a = cp.a(m2014a());
            long j = cb.a;
            if (a <= j) {
                com.xiaomi.channel.commonutils.logger.b.b("db size is suitable");
                return;
            }
            long j2 = (long) ((((a - j) * 1.2d) / j) * longValue);
            a(j2);
            bx a2 = bx.a(context);
            a2.a("begin delete " + j2 + "noUpload messages , because db size is " + a + "B");
            super.a(context, obj);
        }
    }
}
