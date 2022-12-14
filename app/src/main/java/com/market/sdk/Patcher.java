package com.market.sdk;

import com.market.sdk.utils.Log;

/* loaded from: classes.dex */
public class Patcher {
    public native int applyPatch(String str, String str2, String str3);

    public static boolean tryLoadLibrary() {
        try {
            System.loadLibrary("sdk_patcher_jni");
            return true;
        } catch (Throwable th) {
            Log.e("MarketPatcher", "load patcher library failed : " + th.toString());
            return false;
        }
    }

    public static int patch(String str, String str2, String str3) {
        return new Patcher().applyPatch(str, str2, str3);
    }
}
