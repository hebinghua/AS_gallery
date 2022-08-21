package com.xiaomi.micloudsdk.request.utils;

import android.accounts.Account;
import android.content.Context;
import miui.accounts.ExtraAccountManager;

/* loaded from: classes3.dex */
public class CloudUtils {
    public static int isInternationalAccount(boolean z) {
        return CloudRelocationUtils.isInternationalAccount(z);
    }

    public static Account getXiaomiAccount() {
        return getXiaomiAccount(Request.getContext());
    }

    public static Account getXiaomiAccount(Context context) {
        return ExtraAccountManager.getXiaomiAccount(context);
    }
}
