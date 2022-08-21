package com.miui.gallery.editor.photo.core.common.model;

import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public abstract class TextData extends Metadata {
    public int backgroundColor;
    public final String iconPath;
    public boolean mIsLast;
    public boolean showBackgroundColor;
    public String talkbackName;

    public boolean isLast() {
        return this.mIsLast;
    }

    public void setLast(boolean z) {
        this.mIsLast = z;
    }

    public TextData(short s, String str, String str2, String str3, int i) {
        super(s, str);
        this.iconPath = str3;
        this.backgroundColor = i;
        this.talkbackName = str2;
        this.showBackgroundColor = true;
    }

    public TextData(short s, String str, String str2, String str3) {
        super(s, str);
        this.iconPath = str3;
        this.talkbackName = str2;
    }
}
