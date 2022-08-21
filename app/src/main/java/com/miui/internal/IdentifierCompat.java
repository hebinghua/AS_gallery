package com.miui.internal;

import android.content.Context;
import com.android.id.IdentifierManager;

/* loaded from: classes3.dex */
public class IdentifierCompat {
    public static String getOAID(Context context) {
        try {
            return IdentifierManager.isSupported() ? IdentifierManager.getOAID(context) : "";
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }
}
