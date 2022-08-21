package com.miui.gallery.editor.photo.penengine.entity;

import com.miui.gallery.R;
import com.miui.gallery.editor.photo.penengine.entity.Tool;

/* loaded from: classes2.dex */
public class Pen extends CommonBrush {
    public Pen(int[] iArr, int[] iArr2) {
        super(Tool.ToolType.PEN, R.drawable.screen_ic_brush_pen_head, R.drawable.screen_ic_brush_pen_body, R.drawable.screen_ic_brush_pen_shadow_select, R.drawable.screen_ic_brush_pen_shadow_unselect, iArr, 1, iArr2, 0, 1.0f);
    }
}
