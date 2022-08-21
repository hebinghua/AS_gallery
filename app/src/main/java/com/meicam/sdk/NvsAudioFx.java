package com.meicam.sdk;

/* loaded from: classes.dex */
public class NvsAudioFx extends NvsFx {
    private native String nativeGetBuiltinAudioFxName(long j);

    private native int nativeGetIndex(long j);

    private native boolean nativeIsCustomAudioFx(long j);

    public int getIndex() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetIndex(this.m_internalObject);
    }

    public String getBuiltinAudioFxName() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetBuiltinAudioFxName(this.m_internalObject);
    }

    public boolean isCustomAudioFx() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsCustomAudioFx(this.m_internalObject);
    }
}
