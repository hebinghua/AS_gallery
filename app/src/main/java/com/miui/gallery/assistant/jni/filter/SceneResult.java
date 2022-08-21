package com.miui.gallery.assistant.jni.filter;

/* loaded from: classes.dex */
public class SceneResult {
    public static final int TAG_BANKCARD = 22;
    public static final int TAG_DOCUMENT = 0;
    public static final int TAG_PPT = 3;
    private final float score;
    private final int tag;

    public SceneResult(int i, float f) {
        this.tag = i;
        this.score = f;
    }

    public int getTag() {
        return this.tag;
    }

    public float getScore() {
        return this.score;
    }
}
