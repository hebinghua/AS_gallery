package com.miui.gallery.editor.photo.app.crop;

/* loaded from: classes2.dex */
public class Bbox {
    public int h;
    public int w;
    public int x;
    public int y;

    public boolean canCrop() {
        return (this.w == 0 || this.h == 0) ? false : true;
    }

    public String toString() {
        return "Bbox{x=" + this.x + ", y=" + this.y + ", w=" + this.w + ", h=" + this.h + '}';
    }
}
