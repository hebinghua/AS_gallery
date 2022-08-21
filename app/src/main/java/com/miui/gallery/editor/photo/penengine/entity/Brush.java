package com.miui.gallery.editor.photo.penengine.entity;

import com.miui.gallery.editor.photo.penengine.entity.Tool;

/* loaded from: classes2.dex */
public abstract class Brush extends ActivableTool implements IColorable {
    public float alpha;
    public int color;
    public int size;

    public Brush(Tool.ToolType toolType, int i, int i2, int i3) {
        super(toolType, i, i2, i3);
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IColorable
    public int getColor() {
        return this.color;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.Tool
    public String toString() {
        return "Brush{alpha=" + this.alpha + ", color=" + this.color + ", size=" + this.size + '}';
    }
}
