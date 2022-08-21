package com.meicam.effect.sdk;

import com.meicam.sdk.NvsARSceneManipulate;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsFxDescription;
import com.meicam.sdk.NvsPaintingEffectContext;
import com.meicam.sdk.NvsParticleSystemContext;
import com.meicam.sdk.NvsPosition2D;
import com.meicam.sdk.NvsPosition3D;
import com.meicam.sdk.NvsUtils;

/* loaded from: classes.dex */
public class NvsEffect {
    public long m_internalObject = 0;

    private native void nativeDestory(long j);

    private native NvsARSceneManipulate nativeGetARSceneManipulate(long j);

    private native boolean nativeGetBooleanVal(long j, String str);

    private native NvsColor nativeGetColorVal(long j, String str);

    private native NvsFxDescription nativeGetDescription(long j);

    private native float nativeGetFilterIntensity(long j);

    private native double nativeGetFloatVal(long j, String str);

    private native int nativeGetIntVal(long j, String str);

    private native String nativeGetMenuVal(long j, String str);

    private native NvsPaintingEffectContext nativeGetPaintingEffectContext(long j);

    private native NvsParticleSystemContext nativeGetParticleSystemContext(long j);

    private native NvsPosition2D nativeGetPosition2DVal(long j, String str);

    private native NvsPosition3D nativeGetPosition3DVal(long j, String str);

    private native boolean nativeGetRegional(long j);

    private native float nativeGetRegionalFeatherWidth(long j);

    private native String nativeGetStringVal(long j, String str);

    private native void nativeSetArbDataVal(long j, String str, NvsArbitraryData nvsArbitraryData);

    private native void nativeSetBooleanVal(long j, String str, boolean z);

    private native void nativeSetColorVal(long j, String str, NvsColor nvsColor);

    private native void nativeSetFilterIntensity(long j, float f);

    private native void nativeSetFloatVal(long j, String str, double d);

    private native void nativeSetIntVal(long j, String str, int i);

    private native void nativeSetMenuVal(long j, String str, String str2);

    private native void nativeSetPosition2DVal(long j, String str, NvsPosition2D nvsPosition2D);

    private native void nativeSetPosition3DVal(long j, String str, NvsPosition3D nvsPosition3D);

    private native void nativeSetRegion(long j, float[] fArr);

    private native void nativeSetRegional(long j, boolean z);

    private native void nativeSetRegionalFeatherWidth(long j, float f);

    private native void nativeSetStringVal(long j, String str, String str2);

    public NvsFxDescription getDescription() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetDescription(this.m_internalObject);
    }

    public void setIntVal(String str, int i) {
        nativeSetIntVal(getInternalObject(), str, i);
    }

    public int getIntVal(String str) {
        return nativeGetIntVal(getInternalObject(), str);
    }

    public void setFloatVal(String str, double d) {
        nativeSetFloatVal(getInternalObject(), str, d);
    }

    public double getFloatVal(String str) {
        return nativeGetFloatVal(getInternalObject(), str);
    }

    public void setBooleanVal(String str, boolean z) {
        nativeSetBooleanVal(getInternalObject(), str, z);
    }

    public boolean getBooleanVal(String str) {
        return nativeGetBooleanVal(getInternalObject(), str);
    }

    public void setStringVal(String str, String str2) {
        nativeSetStringVal(getInternalObject(), str, str2);
    }

    public String getStringVal(String str) {
        return nativeGetStringVal(getInternalObject(), str);
    }

    public void setColorVal(String str, NvsColor nvsColor) {
        nativeSetColorVal(getInternalObject(), str, nvsColor);
    }

    public NvsColor getColorVal(String str) {
        return nativeGetColorVal(getInternalObject(), str);
    }

    public void setPosition2DVal(String str, NvsPosition2D nvsPosition2D) {
        nativeSetPosition2DVal(getInternalObject(), str, nvsPosition2D);
    }

    public NvsPosition2D getPosition2DVal(String str) {
        return nativeGetPosition2DVal(getInternalObject(), str);
    }

    public void setPosition3DVal(String str, NvsPosition3D nvsPosition3D) {
        nativeSetPosition3DVal(getInternalObject(), str, nvsPosition3D);
    }

    public NvsPosition3D getPosition3DVal(String str) {
        return nativeGetPosition3DVal(getInternalObject(), str);
    }

    public void setMenuVal(String str, String str2) {
        nativeSetMenuVal(getInternalObject(), str, str2);
    }

    public String getMenuVal(String str) {
        return nativeGetMenuVal(getInternalObject(), str);
    }

    public void setArbDataVal(String str, NvsArbitraryData nvsArbitraryData) {
        nativeSetArbDataVal(getInternalObject(), str, nvsArbitraryData);
    }

    public void setFilterIntensity(float f) {
        nativeSetFilterIntensity(getInternalObject(), f);
    }

    public float getFilterIntensity() {
        return nativeGetFilterIntensity(getInternalObject());
    }

    public void setRegional(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegional(getInternalObject(), z);
    }

    public boolean getRegional() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetRegional(getInternalObject());
    }

    public void setRegion(float[] fArr) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegion(getInternalObject(), fArr);
    }

    public void setRegionalFeatherWidth(float f) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetRegionalFeatherWidth(getInternalObject(), f);
    }

    public float getRegionalFeatherWidth() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetRegionalFeatherWidth(getInternalObject());
    }

    public void release() {
        long j = this.m_internalObject;
        if (j != 0) {
            nativeDestory(j);
            this.m_internalObject = 0L;
        }
    }

    public NvsParticleSystemContext getParticleSystemContext() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetParticleSystemContext(this.m_internalObject);
    }

    public NvsPaintingEffectContext getPaintingEffectContext() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPaintingEffectContext(this.m_internalObject);
    }

    public NvsARSceneManipulate getARSceneManipulate() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetARSceneManipulate(this.m_internalObject);
    }

    public void setInternalObject(long j) {
        this.m_internalObject = j;
    }

    public long getInternalObject() {
        return this.m_internalObject;
    }

    public void finalize() throws Throwable {
        long j = this.m_internalObject;
        if (j != 0) {
            nativeDestory(j);
            this.m_internalObject = 0L;
        }
        super.finalize();
    }
}
