package com.miui.gallery.editor.photo.screen.entity;

import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ScreenNavigatorData {
    public final int id;
    public final int name;

    public ScreenNavigatorData(int i) {
        this.id = i;
        if (i == 3) {
            this.name = R.string.screen_editor_text;
        } else {
            this.name = R.string.screen_editor_mosaic;
        }
    }
}
