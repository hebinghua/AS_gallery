package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class ap extends Handler {
    public final /* synthetic */ ao a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ap(ao aoVar, Looper looper) {
        super(looper);
        this.a = aoVar;
    }

    @Override // android.os.Handler
    public void dispatchMessage(Message message) {
        Context context;
        Context context2;
        Context context3;
        Context context4;
        ao aoVar;
        Context context5;
        HashMap<String, String> m1922a;
        Context context6;
        Context context7;
        Context context8;
        Context context9;
        Context context10;
        Context context11;
        Context context12;
        ao aoVar2;
        Context context13;
        Context context14;
        if (message.what != 19) {
            return;
        }
        String str = (String) message.obj;
        int i = message.arg1;
        synchronized (af.class) {
            context = this.a.f47a;
            if (af.a(context).m1887a(str)) {
                context2 = this.a.f47a;
                if (af.a(context2).a(str) < 10) {
                    au auVar = au.DISABLE_PUSH;
                    if (auVar.ordinal() == i) {
                        context14 = this.a.f47a;
                        if ("syncing".equals(af.a(context14).a(auVar))) {
                            aoVar2 = this.a;
                            aoVar2.a(str, auVar, true, (HashMap<String, String>) null);
                            context13 = this.a.f47a;
                            af.a(context13).b(str);
                        }
                    }
                    auVar = au.ENABLE_PUSH;
                    if (auVar.ordinal() == i) {
                        context12 = this.a.f47a;
                        if ("syncing".equals(af.a(context12).a(auVar))) {
                            aoVar2 = this.a;
                            aoVar2.a(str, auVar, true, (HashMap<String, String>) null);
                            context13 = this.a.f47a;
                            af.a(context13).b(str);
                        }
                    }
                    au auVar2 = au.UPLOAD_HUAWEI_TOKEN;
                    if (auVar2.ordinal() == i) {
                        context10 = this.a.f47a;
                        if ("syncing".equals(af.a(context10).a(auVar2))) {
                            aoVar = this.a;
                            context11 = aoVar.f47a;
                            m1922a = i.m1922a(context11, e.ASSEMBLE_PUSH_HUAWEI);
                            aoVar.a(str, auVar2, false, m1922a);
                            context13 = this.a.f47a;
                            af.a(context13).b(str);
                        }
                    }
                    auVar2 = au.UPLOAD_FCM_TOKEN;
                    if (auVar2.ordinal() == i) {
                        context8 = this.a.f47a;
                        if ("syncing".equals(af.a(context8).a(auVar2))) {
                            aoVar = this.a;
                            context9 = aoVar.f47a;
                            m1922a = i.m1922a(context9, e.ASSEMBLE_PUSH_FCM);
                            aoVar.a(str, auVar2, false, m1922a);
                            context13 = this.a.f47a;
                            af.a(context13).b(str);
                        }
                    }
                    auVar2 = au.UPLOAD_COS_TOKEN;
                    if (auVar2.ordinal() == i) {
                        context6 = this.a.f47a;
                        if ("syncing".equals(af.a(context6).a(auVar2))) {
                            aoVar = this.a;
                            context7 = aoVar.f47a;
                            m1922a = i.m1922a(context7, e.ASSEMBLE_PUSH_COS);
                            aoVar.a(str, auVar2, false, m1922a);
                            context13 = this.a.f47a;
                            af.a(context13).b(str);
                        }
                    }
                    auVar2 = au.UPLOAD_FTOS_TOKEN;
                    if (auVar2.ordinal() == i) {
                        context4 = this.a.f47a;
                        if ("syncing".equals(af.a(context4).a(auVar2))) {
                            aoVar = this.a;
                            context5 = aoVar.f47a;
                            m1922a = i.m1922a(context5, e.ASSEMBLE_PUSH_FTOS);
                            aoVar.a(str, auVar2, false, m1922a);
                        }
                    }
                    context13 = this.a.f47a;
                    af.a(context13).b(str);
                } else {
                    context3 = this.a.f47a;
                    af.a(context3).c(str);
                }
            }
        }
    }
}
