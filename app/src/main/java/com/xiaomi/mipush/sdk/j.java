package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;

/* loaded from: classes3.dex */
public final class j implements Runnable {
    public final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ e f73a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f74a;

    public j(String str, Context context, e eVar) {
        this.f74a = str;
        this.a = context;
        this.f73a = eVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str;
        if (!TextUtils.isEmpty(this.f74a)) {
            String[] split = this.f74a.split("~");
            int length = split.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    str = "";
                    break;
                }
                String str2 = split[i];
                if (!TextUtils.isEmpty(str2) && str2.startsWith("token:")) {
                    str = str2.substring(str2.indexOf(":") + 1);
                    break;
                }
                i++;
            }
            if (TextUtils.isEmpty(str)) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : receive incorrect token");
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : receive correct token");
            i.d(this.a, this.f73a, str);
            i.a(this.a);
        }
    }
}
