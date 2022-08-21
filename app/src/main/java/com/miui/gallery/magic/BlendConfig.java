package com.miui.gallery.magic;

import android.graphics.PointF;
import com.miui.gallery.magic.ContourHelper;

/* loaded from: classes2.dex */
public class BlendConfig {
    public PointF point = new PointF(0.0f, 0.0f);
    public float degrees = 0.0f;
    public float scale = 1.0f;
    public boolean isBlend = true;
    public ContourHelper.Configure contourConfigure = null;
    public boolean isMirrorImage = false;

    public ContourHelper.Configure getContourConfigure() {
        return this.contourConfigure;
    }

    public BlendConfig setContourConfigure(ContourHelper.Configure configure) {
        this.contourConfigure = configure;
        return this;
    }

    public BlendConfig setBlend(boolean z) {
        this.isBlend = z;
        return this;
    }

    public boolean isMirrorImage() {
        return this.isMirrorImage;
    }

    public BlendConfig setMirrorImage(boolean z) {
        this.isMirrorImage = z;
        return this;
    }

    public PointF getPoint() {
        return this.point;
    }

    public BlendConfig setPoint(float f, float f2) {
        this.point = new PointF(f, f2);
        return this;
    }

    public float getDegrees() {
        return this.degrees;
    }

    public BlendConfig setDegrees(float f) {
        this.degrees = f;
        return this;
    }

    public float getScale() {
        return this.scale;
    }

    public BlendConfig setScale(float f) {
        this.scale = f;
        return this;
    }
}
