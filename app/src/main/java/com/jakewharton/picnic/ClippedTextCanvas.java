package com.jakewharton.picnic;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: textSurface.kt */
/* loaded from: classes.dex */
public final class ClippedTextCanvas implements TextCanvas {
    public final TextCanvas canvas;
    public final int height;
    public final int left;
    public final int top;
    public final int width;

    public ClippedTextCanvas(TextCanvas canvas, int i, int i2, int i3, int i4) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        this.canvas = canvas;
        this.left = i;
        this.top = i3;
        this.width = i2 - i;
        this.height = i4 - i3;
    }

    @Override // com.jakewharton.picnic.TextCanvas
    public int getWidth() {
        return this.width;
    }

    @Override // com.jakewharton.picnic.TextCanvas
    public int getHeight() {
        return this.height;
    }

    @Override // com.jakewharton.picnic.TextCanvas
    public void write(int i, int i2, String string) {
        Intrinsics.checkNotNullParameter(string, "string");
        this.canvas.write(this.top + i, this.left + i2, string);
    }
}
