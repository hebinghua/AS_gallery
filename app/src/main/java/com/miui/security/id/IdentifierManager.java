package com.miui.security.id;

import android.content.Context;
import com.miui.core.SdkHelper;
import com.miui.internal.IdentifierCompat;

/* loaded from: classes3.dex */
public class IdentifierManager {
    public static String getOAID(Context context) {
        return SdkHelper.IS_MIUI ? IdentifierCompat.getOAID(context) : "";
    }
}
