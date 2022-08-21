package com.miui.gallery.magic;

import android.graphics.Path;

/* loaded from: classes2.dex */
public class PathResult {
    public Path path;
    public float scale;
    public float x;
    public float y;

    public void setPath(Path path) {
        this.path = path;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float f) {
        this.scale = f;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float f) {
        this.x = f;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float f) {
        this.y = f;
    }

    public float getNX(float f) {
        return (f * this.scale) + this.x;
    }

    public float getNY(float f) {
        return (f * this.scale) + this.y;
    }
}
