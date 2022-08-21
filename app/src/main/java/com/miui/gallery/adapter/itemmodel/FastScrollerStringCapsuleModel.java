package com.miui.gallery.adapter.itemmodel;

import com.miui.gallery.widget.recyclerview.FastScrollerCapsuleContentProvider;

/* loaded from: classes.dex */
public class FastScrollerStringCapsuleModel implements FastScrollerCapsuleContentProvider<String> {
    public String mContent;

    public void setContent(String str) {
        this.mContent = str;
    }

    public String getContent() {
        return this.mContent;
    }
}
