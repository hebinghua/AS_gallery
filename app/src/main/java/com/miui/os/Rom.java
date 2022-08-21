package com.miui.os;

import android.os.Build;
import android.text.TextUtils;
import com.android.internal.SystemPropertiesCompat;
import com.miui.core.SdkHelper;

/* loaded from: classes3.dex */
public interface Rom {
    public static final boolean IS_ALPHA;
    public static final boolean IS_DEBUGGABLE;
    public static final boolean IS_DEV;
    public static final boolean IS_INTERNATIONAL;
    public static final boolean IS_MIUI;
    public static final boolean IS_STABLE;

    static {
        boolean z = SdkHelper.IS_MIUI;
        IS_MIUI = z;
        IS_INTERNATIONAL = SystemPropertiesCompat.get("ro.product.mod_device", "").contains("_global");
        boolean z2 = true;
        IS_ALPHA = z && SystemPropertiesCompat.get("ro.product.mod_device", "").endsWith("_alpha");
        boolean z3 = z && !TextUtils.isEmpty(Build.VERSION.INCREMENTAL) && Build.VERSION.INCREMENTAL.matches("\\d+.\\d+.\\d+(-internal)?");
        IS_DEV = z3;
        IS_STABLE = !z || ("user".equals(Build.TYPE) && !z3);
        if (!z || SystemPropertiesCompat.getInt("ro.debuggable", 0) != 1) {
            z2 = false;
        }
        IS_DEBUGGABLE = z2;
    }
}
