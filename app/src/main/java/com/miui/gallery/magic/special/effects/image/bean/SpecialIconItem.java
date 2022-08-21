package com.miui.gallery.magic.special.effects.image.bean;

/* loaded from: classes2.dex */
public class SpecialIconItem {
    public static boolean download = true;
    public static boolean downloading;
    public String icon;
    public String[] masks;
    public boolean selected;
    public String title;

    public SpecialIconItem(String str, boolean z, String[] strArr, boolean z2, String str2) {
        this.icon = str;
        setDownload(z);
        this.masks = strArr;
        this.selected = z2;
        this.title = str2;
    }

    public SpecialIconItem(String str, String[] strArr, boolean z, String str2) {
        this.icon = str;
        this.masks = strArr;
        this.selected = z;
        this.title = str2;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean z) {
        this.selected = z;
    }

    public String[] getMasks() {
        return this.masks;
    }

    public static boolean getDownLoad() {
        return download;
    }

    public static void setDownload(boolean z) {
        download = z;
    }

    public String getIcon() {
        return this.icon;
    }

    public static boolean isDownloading() {
        return downloading;
    }

    public static void setDownloading(boolean z) {
        downloading = z;
    }
}
