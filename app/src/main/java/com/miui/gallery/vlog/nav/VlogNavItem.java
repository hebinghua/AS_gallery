package com.miui.gallery.vlog.nav;

/* loaded from: classes2.dex */
public class VlogNavItem {
    public String mClassName;
    public int mItemNameId;
    public int mTagId;

    public VlogNavItem(int i, int i2, String str) {
        this.mTagId = i;
        this.mItemNameId = i2;
        this.mClassName = str;
    }

    public int getTagId() {
        return this.mTagId;
    }

    public int getItemNameId() {
        return this.mItemNameId;
    }

    public String getClassName() {
        return this.mClassName;
    }
}
