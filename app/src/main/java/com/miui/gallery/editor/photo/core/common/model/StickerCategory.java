package com.miui.gallery.editor.photo.core.common.model;

import com.miui.gallery.editor.photo.core.Metadata;

/* loaded from: classes2.dex */
public abstract class StickerCategory extends Metadata {
    public final long id;
    public String talkbackName;

    public StickerCategory(long j, short s, String str, String str2) {
        super(s, str);
        this.id = j;
        this.talkbackName = str2;
    }
}
