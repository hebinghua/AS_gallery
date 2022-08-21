package com.xiaomi.push;

import android.text.TextUtils;

/* loaded from: classes3.dex */
public enum ey {
    COMMAND_REGISTER("register"),
    COMMAND_UNREGISTER("unregister"),
    COMMAND_SET_ALIAS("set-alias"),
    COMMAND_UNSET_ALIAS("unset-alias"),
    COMMAND_SET_ACCOUNT("set-account"),
    COMMAND_UNSET_ACCOUNT("unset-account"),
    COMMAND_SUBSCRIBE_TOPIC("subscribe-topic"),
    COMMAND_UNSUBSCRIBE_TOPIC("unsubscibe-topic"),
    COMMAND_SET_ACCEPT_TIME("accept-time"),
    COMMAND_CHK_VDEVID("check-vdeviceid");
    

    /* renamed from: a  reason: collision with other field name */
    public final String f318a;

    ey(String str) {
        this.f318a = str;
    }

    public static int a(String str) {
        ey[] values;
        int i = -1;
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        for (ey eyVar : values()) {
            if (eyVar.f318a.equals(str)) {
                i = en.a(eyVar);
            }
        }
        return i;
    }
}
