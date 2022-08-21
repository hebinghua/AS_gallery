package com.miui.gallery.agreement.cn;

import android.content.Context;
import android.content.Intent;
import com.android.internal.SystemPropertiesCompat;
import com.miui.gallery.util.LazyValue;

/* loaded from: classes.dex */
public class SystemCTAAgreement {
    public static LazyValue<Context, Boolean> SUPPORT_SYSTEM_CTA = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.agreement.cn.SystemCTAAgreement.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            boolean z = false;
            if (SystemPropertiesCompat.getInt("ro.miui.ui.version.code", 0) < 9) {
                return Boolean.FALSE;
            }
            Intent intent = new Intent("miui.intent.action.SYSTEM_PERMISSION_DECLARE");
            intent.setPackage("com.miui.securitycenter");
            try {
                if (!context.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                    z = true;
                }
                return Boolean.valueOf(z);
            } catch (Exception e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    };
}
