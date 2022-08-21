package com.miui.gallery.magic.matting.adapter;

/* loaded from: classes2.dex */
public class IconItem {
    public boolean check;
    public String icon;

    public IconItem(String str, boolean z) {
        this.icon = str;
        this.check = z;
    }

    public IconItem() {
    }

    public String getIcon() {
        return this.icon;
    }

    public boolean isCheck() {
        return this.check;
    }

    public void setCheck(boolean z) {
        this.check = z;
    }
}
