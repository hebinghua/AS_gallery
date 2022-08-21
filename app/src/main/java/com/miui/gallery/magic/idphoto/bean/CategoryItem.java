package com.miui.gallery.magic.idphoto.bean;

/* loaded from: classes2.dex */
public class CategoryItem {
    public Boolean check;
    public int index;
    public String text;
    public String textmm;
    public String title;
    public String type;

    public CategoryItem(String str, String str2, Boolean bool, String str3, String str4, int i) {
        this.title = str;
        this.text = str2;
        this.check = bool;
        this.type = str3;
        this.textmm = str4;
        this.index = i;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getTextmm() {
        return this.textmm;
    }

    public int getIndex() {
        return this.index;
    }

    public String getTitle() {
        return this.title;
    }
}
