package com.miui.gallery.editor.photo.screen.shell;

/* loaded from: classes2.dex */
public class ShellInfoBean {
    public float bottomMargin;
    public float height;
    public float leftMargin;
    public float rightMargin;
    public float topMargin;
    public float with;

    public float getShellWidth() {
        return this.with + this.leftMargin + this.rightMargin;
    }

    public float getShellHeight() {
        return this.height + this.topMargin + this.bottomMargin;
    }
}
