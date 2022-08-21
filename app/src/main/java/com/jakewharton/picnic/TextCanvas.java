package com.jakewharton.picnic;

/* compiled from: textSurface.kt */
/* loaded from: classes.dex */
public interface TextCanvas {
    int getHeight();

    int getWidth();

    void write(int i, int i2, String str);

    default TextCanvas clip(int i, int i2, int i3, int i4) {
        return new ClippedTextCanvas(this, i, i2, i3, i4);
    }
}
