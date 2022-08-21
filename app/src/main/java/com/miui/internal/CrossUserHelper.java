package com.miui.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import miui.securityspace.CrossUserUtils;

/* loaded from: classes3.dex */
public class CrossUserHelper {
    public static boolean checkUidPermission(Context context, String str) {
        return CrossUserUtils.checkUidPermission(context, str);
    }

    public static ContentResolver getContentResolver(Context context, int i) {
        if (support()) {
            return context.getContentResolverForUser(new UserHandle(i));
        }
        throw new UnsupportedOperationException("not support multi users");
    }

    public static boolean support() {
        return Build.VERSION.SDK_INT > 19;
    }
}
