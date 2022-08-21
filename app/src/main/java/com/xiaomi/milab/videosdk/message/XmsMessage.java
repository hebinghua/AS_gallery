package com.xiaomi.milab.videosdk.message;

/* loaded from: classes3.dex */
public class XmsMessage {
    private static final String TAG = "XmsMessage";

    public static native void OnUserCommand(int i, int i2, int i3, int i4);

    public static void destroy() {
    }

    private static void postMessage(int i, int i2, int i3, int i4) {
        MsgProxy.dispatchMessage(i, i2, i3, Integer.valueOf(i4));
    }

    private static void postMessage(int i, int i2, int i3, Object obj) {
        MsgProxy.dispatchMessage(i, i2, i3, obj);
    }
}
