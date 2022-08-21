package com.miui.gallery.assistant.jni.score;

/* loaded from: classes.dex */
public class SaliencyScore {
    private float complete;
    private float relativeBias;
    private float size;

    public SaliencyScore(float f, float f2, float f3) {
        this.relativeBias = f;
        this.size = f2;
        this.complete = f3;
    }

    public float getRelativeBias() {
        return this.relativeBias;
    }

    public float getSize() {
        return this.size;
    }

    public float getComplete() {
        return this.complete;
    }
}
