package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.mipush.sdk.b;
import com.xiaomi.push.ey;
import com.xiaomi.push.ik;
import com.xiaomi.push.iq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class MiPushClient4Hybrid {
    public static Map<String, b.a> dataMap = new HashMap();
    public static Map<String, Long> sRegisterTimeMap = new HashMap();

    public static void onReceiveRegisterResult(Context context, ik ikVar) {
        b.a aVar;
        String c = ikVar.c();
        if (ikVar.a() == 0 && (aVar = dataMap.get(c)) != null) {
            aVar.a(ikVar.f684e, ikVar.f685f);
            b.m1906a(context).a(c, aVar);
        }
        ArrayList arrayList = null;
        if (!TextUtils.isEmpty(ikVar.f684e)) {
            arrayList = new ArrayList();
            arrayList.add(ikVar.f684e);
        }
        PushMessageHelper.generateCommandMessage(ey.COMMAND_REGISTER.f318a, arrayList, ikVar.f672a, ikVar.f683d, null, null);
    }

    public static void onReceiveUnregisterResult(Context context, iq iqVar) {
        PushMessageHelper.generateCommandMessage(ey.COMMAND_UNREGISTER.f318a, null, iqVar.f750a, iqVar.f758d, null, null);
        iqVar.a();
    }
}
