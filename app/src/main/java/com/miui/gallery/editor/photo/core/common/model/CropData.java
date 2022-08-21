package com.miui.gallery.editor.photo.core.common.model;

import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public abstract class CropData extends Metadata {
    public final int icon;
    public boolean isSelected;
    public final int talkbackName;

    public CropData(short s, String str, int i, int i2) {
        super(s, str);
        this.icon = i;
        this.talkbackName = i2;
    }

    /* loaded from: classes2.dex */
    public static class AspectRatio extends CropData {
        public final int height;
        public final int width;

        public AspectRatio(short s, String str, int i, int i2, int i3, int i4) {
            super(s, str, i, i2);
            this.width = i3;
            this.height = i4;
        }
    }
}
