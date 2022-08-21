package com.miui.gallery.gallerywidget.ui.editor;

import android.graphics.Matrix;

/* loaded from: classes2.dex */
public class ImageEntity {
    public float[] cropInfo;
    public Matrix cropMatrix;
    public String id;
    public String picPath;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getPicPath() {
        return this.picPath;
    }

    public void setPicPath(String str) {
        this.picPath = str;
    }

    public Matrix getCropMatrix() {
        return this.cropMatrix;
    }

    public void setCropMatrix(Matrix matrix) {
        this.cropMatrix = matrix;
    }

    public float[] getCropInfo() {
        return this.cropInfo;
    }

    public void setCropInfo(float[] fArr) {
        this.cropInfo = fArr;
    }
}
