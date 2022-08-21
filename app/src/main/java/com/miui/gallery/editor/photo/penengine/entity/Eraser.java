package com.miui.gallery.editor.photo.penengine.entity;

import com.miui.gallery.R;
import com.miui.gallery.editor.photo.penengine.entity.Tool;

/* loaded from: classes2.dex */
public class Eraser extends ActivableTool {
    public int size;

    public Eraser(int i) {
        super(Tool.ToolType.ERASER, R.drawable.screen_ic_tool_eraser_body, R.drawable.screen_ic_tool_eraser_shadow_select, R.drawable.screen_ic_tool_eraser_shadow_unselect);
        setSize(i);
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int i) {
        this.size = i;
    }
}
