package com.meicam.sdk;

import android.graphics.RectF;
import com.meicam.sdk.NvsCustomVideoFx;

/* loaded from: classes.dex */
public class NvsVideoClip extends NvsClip {
    public static final int CLIP_MOTIONMODE_LETTERBOX_ZOOMIN = 0;
    public static final int CLIP_WRAPMODE_REPEAT_LASTFRAME = 0;
    public static final int ClIP_BACKGROUNDMODE_BLUR = 1;
    public static final int ClIP_BACKGROUNDMODE_COLOR_SOLID = 0;
    public static final int ClIP_EXTRAVIDEOROTATION_0 = 0;
    public static final int ClIP_EXTRAVIDEOROTATION_180 = 2;
    public static final int ClIP_EXTRAVIDEOROTATION_270 = 3;
    public static final int ClIP_EXTRAVIDEOROTATION_90 = 1;
    public static final int ClIP_MOTIONMODE_LETTERBOX_ZOOMOUT = 1;
    public static final int ClIP_WRAPMODE_REPEAT = 2;
    public static final int ClIP_WRAPMODE_REPEAT_FIRSTFRAME = 1;
    public static final int IMAGE_CLIP_MOTIONMMODE_ROI = 2;
    public static final int ROLE_IN_THEME_GENERAL = 0;
    public static final int ROLE_IN_THEME_TITLE = 1;
    public static final int ROLE_IN_THEME_TRAILER = 2;
    public static final int VIDEO_CLIP_DECODE_TEMPORAL_LAYER_1 = 1;
    public static final int VIDEO_CLIP_DECODE_TEMPORAL_LAYER_2 = 2;
    public static final int VIDEO_CLIP_DECODE_TEMPORAL_LAYER_3 = 3;
    public static final int VIDEO_CLIP_DECODE_TEMPORAL_LAYER_BASE = 0;
    public static final int VIDEO_CLIP_DECODE_TEMPORAL_LAYER_NONE = -1;
    public static final int VIDEO_CLIP_TYPE_AV = 0;
    public static final int VIDEO_CLIP_TYPE_IMAGE = 1;

    private native NvsVideoFx nativeAppendBeautyFx(long j);

    private native NvsVideoFx nativeAppendBuiltinFx(long j, String str);

    private native NvsVideoFx nativeAppendPackagedFx(long j, String str);

    private native void nativeChangeVariableSpeed(long j, double d, double d2, boolean z);

    private native void nativeDisableAmbiguousCrop(long j, boolean z);

    private native void nativeEnableSlowMotionBlended(long j, boolean z);

    private native int nativeGetClipWrapMode(long j);

    private native RectF nativeGetEndROI(long j);

    private native double nativeGetEndSpeed(long j);

    private native int nativeGetExtraVideoRotation(long j);

    private native NvsVideoFx nativeGetFxByIndex(long j, int i);

    private native RectF nativeGetImageMaskROI(long j);

    private native boolean nativeGetImageMotionAnimationEnabled(long j);

    private native int nativeGetImageMotionMode(long j);

    private native NvsPanAndScan nativeGetPanAndScan(long j);

    private native boolean nativeGetPlayInReverse(long j);

    private native int nativeGetRoleInTheme(long j);

    private native NvsColor nativeGetSourceBackgroundColor(long j);

    private native int nativeGetSourceBackgroundMode(long j);

    private native RectF nativeGetStartROI(long j);

    private native double nativeGetStartSpeed(long j);

    private native int nativeGetVideoType(long j);

    private native NvsVideoFx nativeInsertBeautyFx(long j, int i);

    private native NvsVideoFx nativeInsertBuiltinFx(long j, String str, int i);

    private native NvsVideoFx nativeInsertCustomFx(long j, NvsCustomVideoFx.Renderer renderer, int i);

    private native NvsVideoFx nativeInsertPackagedFx(long j, String str, int i);

    private native boolean nativeIsAmbiguousCropDisabled(long j);

    private native boolean nativeIsSlowMotionBlended(long j);

    private native boolean nativeIsSoftWareDeocdingUsed(long j);

    private native boolean nativeRemoveAllFx(long j);

    private native boolean nativeRemoveFx(long j, int i);

    private native void nativeSetClipWrapMode(long j, int i);

    private native void nativeSetDecodeTemporalLayer(long j, int i);

    private native void nativeSetExtraVideoRotation(long j, int i);

    private native void nativeSetExtraVideoRotation2(long j, int i, boolean z);

    private native void nativeSetImageMaskROI(long j, RectF rectF);

    private native void nativeSetImageMotionAnimationEnabled(long j, boolean z);

    private native void nativeSetImageMotionMode(long j, int i);

    private native void nativeSetImageMotionROI(long j, RectF rectF, RectF rectF2);

    private native void nativeSetPanAndScan(long j, float f, float f2);

    private native void nativeSetPlayInReverse(long j, boolean z);

    private native void nativeSetSoftWareDecoding(long j, boolean z);

    private native void nativeSetSourceBackgroundColor(long j, NvsColor nvsColor);

    private native void nativeSetSourceBackgroundMode(long j, int i);

    public int getVideoType() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetVideoType(this.m_internalObject);
    }

    public int getRoleInTheme() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetRoleInTheme(this.m_internalObject);
    }

    public void setPlayInReverse(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetPlayInReverse(this.m_internalObject, z);
    }

    public boolean getPlayInReverse() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPlayInReverse(this.m_internalObject);
    }

    public void setExtraVideoRotation(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetExtraVideoRotation(this.m_internalObject, i);
    }

    public void setExtraVideoRotation(int i, boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetExtraVideoRotation2(this.m_internalObject, i, z);
    }

    public int getExtraVideoRotation() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetExtraVideoRotation(this.m_internalObject);
    }

    public void setSoftWareDecoding(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetSoftWareDecoding(this.m_internalObject, z);
    }

    public boolean isSoftWareDeocedUsed() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsSoftWareDeocdingUsed(this.m_internalObject);
    }

    public void disableAmbiguousCrop(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeDisableAmbiguousCrop(this.m_internalObject, z);
    }

    public boolean isAmbiguousCropDisabled() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsAmbiguousCropDisabled(this.m_internalObject);
    }

    public void setPanAndScan(float f, float f2) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetPanAndScan(this.m_internalObject, f, f2);
    }

    public NvsPanAndScan getPanAndScan() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetPanAndScan(this.m_internalObject);
    }

    public void enableSlowMotionBlended(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeEnableSlowMotionBlended(this.m_internalObject, z);
    }

    public boolean isSlowMotionBlended() {
        NvsUtils.checkFunctionInMainThread();
        return nativeIsSlowMotionBlended(this.m_internalObject);
    }

    public int getSourceBackgroundMode() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetSourceBackgroundMode(this.m_internalObject);
    }

    public void setSourceBackgroundMode(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetSourceBackgroundMode(this.m_internalObject, i);
    }

    public void setSourceBackgroundColor(NvsColor nvsColor) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetSourceBackgroundColor(this.m_internalObject, nvsColor);
    }

    public NvsColor getSourceBackgroundColor() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetSourceBackgroundColor(this.m_internalObject);
    }

    public int getImageMotionMode() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetImageMotionMode(this.m_internalObject);
    }

    public void setImageMotionMode(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetImageMotionMode(this.m_internalObject, i);
    }

    public boolean getImageMotionAnimationEnabled() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetImageMotionAnimationEnabled(this.m_internalObject);
    }

    public void setImageMotionAnimationEnabled(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetImageMotionAnimationEnabled(this.m_internalObject, z);
    }

    public RectF getStartROI() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetStartROI(this.m_internalObject);
    }

    public RectF getEndROI() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetEndROI(this.m_internalObject);
    }

    public void setImageMotionROI(RectF rectF, RectF rectF2) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetImageMotionROI(this.m_internalObject, rectF, rectF2);
    }

    public RectF getImageMaskROI() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetImageMaskROI(this.m_internalObject);
    }

    public void setImageMaskROI(RectF rectF) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetImageMaskROI(this.m_internalObject, rectF);
    }

    public void setClipWrapMode(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i < 0 || i > 2) {
            return;
        }
        nativeSetClipWrapMode(this.m_internalObject, i);
    }

    public int getClipWrapMode() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetClipWrapMode(this.m_internalObject);
    }

    public void changeVariableSpeed(double d, double d2, boolean z) {
        NvsUtils.checkFunctionInMainThread();
        nativeChangeVariableSpeed(this.m_internalObject, d, d2, z);
    }

    public double getStartSpeed() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetStartSpeed(this.m_internalObject);
    }

    public double getEndSpeed() {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetEndSpeed(this.m_internalObject);
    }

    public NvsVideoFx appendBuiltinFx(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeAppendBuiltinFx(this.m_internalObject, str);
    }

    public NvsVideoFx insertBuiltinFx(String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertBuiltinFx(this.m_internalObject, str, i);
    }

    public NvsVideoFx appendPackagedFx(String str) {
        NvsUtils.checkFunctionInMainThread();
        return nativeAppendPackagedFx(this.m_internalObject, str);
    }

    public NvsVideoFx insertPackagedFx(String str, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertPackagedFx(this.m_internalObject, str, i);
    }

    public NvsVideoFx appendCustomFx(NvsCustomVideoFx.Renderer renderer) {
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertCustomFx(this.m_internalObject, renderer, getFxCount());
    }

    public NvsVideoFx insertCustomFx(NvsCustomVideoFx.Renderer renderer, int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertCustomFx(this.m_internalObject, renderer, i);
    }

    public NvsVideoFx appendBeautyFx() {
        NvsUtils.checkFunctionInMainThread();
        return nativeAppendBeautyFx(this.m_internalObject);
    }

    public NvsVideoFx insertBeautyFx(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeInsertBeautyFx(this.m_internalObject, i);
    }

    public boolean removeFx(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeRemoveFx(this.m_internalObject, i);
    }

    public boolean removeAllFx() {
        NvsUtils.checkFunctionInMainThread();
        return nativeRemoveAllFx(this.m_internalObject);
    }

    public NvsVideoFx getFxByIndex(int i) {
        NvsUtils.checkFunctionInMainThread();
        return nativeGetFxByIndex(this.m_internalObject, i);
    }

    public void setDecodeTemporalLayer(int i) {
        NvsUtils.checkFunctionInMainThread();
        nativeSetDecodeTemporalLayer(this.m_internalObject, i);
    }
}
