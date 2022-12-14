package com.baidu.platform.comjni.map.msgcenter;

import com.baidu.platform.comjni.JNIBaseApi;

/* loaded from: classes.dex */
public class NAMsgCenter extends JNIBaseApi {
    private long a = 0;

    private native boolean nativeCancelRequest(long j);

    private native long nativeCreate();

    private native boolean nativeFetchAccessToken(long j);

    private native String nativeGetCenterParam(long j, String str);

    private native boolean nativeMSGCStartup(long j);

    private native boolean nativeRegMsgCenter(long j, String str);

    private native int nativeRelease(long j);

    private native boolean nativeSetCenterParam(long j, String str);
}
