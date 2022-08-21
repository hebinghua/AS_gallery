package com.miui.gallery.editor.photo.core.imports.doodle;

import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;

/* loaded from: classes2.dex */
public class DoodleConfig extends DoodleData {
    public DoodleItem mDoodleItem;

    public DoodleConfig(short s, String str, DoodleItem doodleItem) {
        super(s, str, doodleItem.normal, doodleItem.selected, doodleItem.talkback);
        this.mDoodleItem = doodleItem;
    }

    public DoodleItem getDoodleItem() {
        return this.mDoodleItem;
    }
}
