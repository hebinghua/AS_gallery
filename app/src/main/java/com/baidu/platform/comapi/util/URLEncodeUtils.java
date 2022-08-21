package com.baidu.platform.comapi.util;

import com.baidu.platform.comjni.JNIBaseApi;

/* loaded from: classes.dex */
public final class URLEncodeUtils extends JNIBaseApi {
    private static native String nativeMD5Sign(String str);

    private static native String nativeOperSign(String str);

    private static native String nativeUrlEncode(String str);

    private static native String nativeWebSign(String str);
}
