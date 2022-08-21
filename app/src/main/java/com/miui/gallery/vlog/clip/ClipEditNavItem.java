package com.miui.gallery.vlog.clip;

/* loaded from: classes2.dex */
public class ClipEditNavItem {
    public boolean mEnable = true;
    public int mImgId;
    public int mTitleResId;
    public String mType;

    public ClipEditNavItem(int i, int i2, String str) {
        this.mTitleResId = i;
        this.mImgId = i2;
        this.mType = str;
    }

    public boolean isSpeedx() {
        return this.mType.equals("type_speed");
    }

    public int getTitleResId() {
        return this.mTitleResId;
    }

    public void setTitleResId(int i) {
        this.mTitleResId = i;
    }

    public int getImgId() {
        return this.mImgId;
    }

    public String getType() {
        return this.mType;
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public void setEnable(boolean z) {
        this.mEnable = z;
    }
}
