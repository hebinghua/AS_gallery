package com.miui.gallery.editor.photo.app;

/* loaded from: classes2.dex */
public class WaterMaskWrapper {
    public boolean closeMaskFrame;
    public boolean isShowMask = true;
    public WaterMaskData mDeviceMask;
    public WaterMaskData mTimeMask;
    public int originHeight;
    public int originWidth;

    public WaterMaskData getDeviceMask() {
        return this.mDeviceMask;
    }

    public void setDeviceMask(WaterMaskData waterMaskData) {
        this.mDeviceMask = waterMaskData;
    }

    public WaterMaskData getTimeMask() {
        return this.mTimeMask;
    }

    public void setTimeMask(WaterMaskData waterMaskData) {
        this.mTimeMask = waterMaskData;
    }
}
