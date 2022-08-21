package com.miui.gallery.editor.photo.core.common.model;

import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public class RemoverData extends Metadata {
    public int mIcon;
    public int mType;

    public RemoverData(short s, String str, int i, int i2) {
        super(s, str);
        this.mIcon = i;
        this.mType = i2;
    }
}
