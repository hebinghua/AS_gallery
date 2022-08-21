package com.miui.gallery.editor.photo.penengine.entity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import com.miui.gallery.editor.photo.penengine.entity.Tool;

/* loaded from: classes2.dex */
public abstract class ActivableTool extends Tool implements IActivable {
    public int bgSelectedId;
    public int bgUnSelectedId;
    public int fgBodyResId;

    public ActivableTool(Tool.ToolType toolType, int i, int i2, int i3) {
        super(toolType);
        this.fgBodyResId = i;
        this.bgSelectedId = i2;
        this.bgUnSelectedId = i3;
    }

    public int getFgBodyResId() {
        return this.fgBodyResId;
    }

    public int getBgSelectedId() {
        return this.bgSelectedId;
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IActivable
    public Drawable createBg(Context context, boolean z) {
        return ContextCompat.getDrawable(context, getBgSelectedId());
    }

    @Override // com.miui.gallery.editor.photo.penengine.entity.IActivable
    public Drawable createFg(Context context) {
        return ContextCompat.getDrawable(context, getFgBodyResId());
    }
}
