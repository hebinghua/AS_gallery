package com.miui.gallery.editor.photo.app.crop;

/* loaded from: classes2.dex */
public class AutoCropData {
    public int rotationResult;
    public Bbox mBox = new Bbox();
    public float[] angles = new float[3];

    public boolean canAutoRotation() {
        return this.rotationResult == 0 && Math.abs(getAngle()) > 0.1f && Math.abs(getAngle()) <= 10.0f;
    }

    public boolean canAutoCrop() {
        return this.mBox.canCrop();
    }

    public boolean canAutoCropOrRotation() {
        return canAutoRotation() || canAutoCrop();
    }

    public float getAngle() {
        float[] fArr = this.angles;
        return fArr[fArr.length - 1];
    }

    public float getDegree() {
        return -getAngle();
    }
}
