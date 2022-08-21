package com.baidu.platform.comjni;

/* loaded from: classes.dex */
public abstract class NativeComponent extends JNIBaseApi {
    public volatile long mNativePointer;

    public abstract long create();

    public abstract int dispose();

    public void finalize() throws Throwable {
        dispose();
        super.finalize();
    }
}
