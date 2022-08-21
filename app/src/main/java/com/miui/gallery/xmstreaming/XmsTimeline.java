package com.miui.gallery.xmstreaming;

/* loaded from: classes3.dex */
public class XmsTimeline extends XmsObject {
    private static final String TAG = "XmsTimeline";
    private int mPreferHeight;
    private int mPreferWidth;

    private native XmsClip nativeAddClip(long j, String str);

    private native void nativeClearClip(long j);

    private native XmsClip nativeInsertClip(long j, String str, XmsClip xmsClip);

    private native void nativeSetFilter(long j, String str);

    private native void nativeSetMask(long j, String str);

    private native void nativeSetMusic(long j, String str);

    private native void nativeSetSticker(long j, String str, String str2);

    private native void nativeSetTitle(long j, String str, String str2);

    private native void nativeSetTransType(long j, int i);

    public XmsClip appendClip(String str) {
        return nativeAddClip(this.m_internalObject, str);
    }

    public void clearClip() {
        nativeClearClip(this.m_internalObject);
    }

    public XmsClip insertClip(String str) {
        return nativeInsertClip(this.m_internalObject, str, new XmsClip(str));
    }

    public void setTitle(String str, String str2) {
        nativeSetTitle(this.m_internalObject, str, str2);
    }

    public void setMusic(String str) {
        nativeSetMusic(this.m_internalObject, str);
    }

    public int getPreferWidth() {
        return this.mPreferWidth;
    }

    public void setPreferWidth(int i) {
        this.mPreferWidth = i;
    }

    public int getPreferHeight() {
        return this.mPreferHeight;
    }

    public void setPreferHeight(int i) {
        this.mPreferHeight = i;
    }

    public void setFilter(String str) {
        nativeSetFilter(this.m_internalObject, str);
    }

    public void setSticker(String str, String str2) {
        nativeSetSticker(this.m_internalObject, str, str2);
    }

    public void setMask(String str) {
        nativeSetMask(this.m_internalObject, str);
    }

    public void setTransType(int i) {
        nativeSetTransType(this.m_internalObject, i);
    }
}
