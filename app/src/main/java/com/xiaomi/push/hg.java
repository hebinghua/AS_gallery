package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class hg {
    public static volatile hg a;

    /* renamed from: a  reason: collision with other field name */
    public final Context f449a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, hh> f450a = new HashMap();

    public hg(Context context) {
        this.f449a = context;
    }

    public static hg a(Context context) {
        if (context == null) {
            com.xiaomi.channel.commonutils.logger.b.d("[TinyDataManager]:mContext is null, TinyDataManager.getInstance(Context) failed.");
            return null;
        }
        if (a == null) {
            synchronized (hg.class) {
                if (a == null) {
                    a = new hg(context);
                }
            }
        }
        return a;
    }

    public hh a() {
        hh hhVar = this.f450a.get("UPLOADER_PUSH_CHANNEL");
        if (hhVar != null) {
            return hhVar;
        }
        hh hhVar2 = this.f450a.get("UPLOADER_HTTP");
        if (hhVar2 == null) {
            return null;
        }
        return hhVar2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, hh> m2223a() {
        return this.f450a;
    }

    public void a(hh hhVar, String str) {
        if (hhVar == null) {
            com.xiaomi.channel.commonutils.logger.b.d("[TinyDataManager]: please do not add null mUploader to TinyDataManager.");
        } else if (TextUtils.isEmpty(str)) {
            com.xiaomi.channel.commonutils.logger.b.d("[TinyDataManager]: can not add a provider from unkown resource.");
        } else {
            m2223a().put(str, hhVar);
        }
    }

    public boolean a(hn hnVar, String str) {
        if (TextUtils.isEmpty(str)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("pkgName is null or empty, upload ClientUploadDataItem failed.");
            return false;
        } else if (com.xiaomi.push.service.bz.a(hnVar, false)) {
            return false;
        } else {
            if (TextUtils.isEmpty(hnVar.d())) {
                hnVar.f(com.xiaomi.push.service.bz.a());
            }
            hnVar.g(str);
            com.xiaomi.push.service.ca.a(this.f449a, hnVar);
            return true;
        }
    }

    public boolean a(String str, String str2, long j, String str3) {
        return a(this.f449a.getPackageName(), this.f449a.getPackageName(), str, str2, j, str3);
    }

    public final boolean a(String str, String str2, String str3, String str4, long j, String str5) {
        hn hnVar = new hn();
        hnVar.d(str3);
        hnVar.c(str4);
        hnVar.a(j);
        hnVar.b(str5);
        hnVar.a(true);
        hnVar.a("push_sdk_channel");
        hnVar.e(str2);
        return a(hnVar, str);
    }
}
