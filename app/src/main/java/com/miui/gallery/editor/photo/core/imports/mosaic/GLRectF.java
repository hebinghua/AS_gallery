package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class GLRectF implements Parcelable {
    public static final Parcelable.Creator<GLRectF> CREATOR = new Parcelable.Creator<GLRectF>() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.GLRectF.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public GLRectF mo820createFromParcel(Parcel parcel) {
            return new GLRectF(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public GLRectF[] mo821newArray(int i) {
            return new GLRectF[i];
        }
    };
    public float bottom;
    public float left;
    public float right;
    public float top;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public GLRectF() {
    }

    public GLRectF(GLRectF gLRectF) {
        set(gLRectF);
    }

    public GLRectF(float[] fArr) {
        setByVertex(fArr);
    }

    public void set(GLRectF gLRectF) {
        this.left = gLRectF.left;
        this.top = gLRectF.top;
        this.right = gLRectF.right;
        this.bottom = gLRectF.bottom;
    }

    public void setByVertex(float[] fArr) {
        this.left = fArr[0];
        this.top = fArr[5];
        this.right = fArr[2];
        this.bottom = fArr[1];
    }

    public void getVertex(float[] fArr) {
        float f = this.left;
        fArr[0] = f;
        float f2 = this.bottom;
        fArr[1] = f2;
        float f3 = this.right;
        fArr[2] = f3;
        fArr[3] = f2;
        fArr[4] = f;
        float f4 = this.top;
        fArr[5] = f4;
        fArr[6] = f3;
        fArr[7] = f4;
    }

    public float centerX() {
        return (this.left + this.right) * 0.5f;
    }

    public final float centerY() {
        return (this.top + this.bottom) * 0.5f;
    }

    public final float width() {
        return this.right - this.left;
    }

    public final float height() {
        return this.top - this.bottom;
    }

    public void offset(float f, float f2) {
        this.left += f;
        this.top += f2;
        this.right += f;
        this.bottom += f2;
    }

    public float getTop() {
        return this.top;
    }

    public void setTop(float f) {
        this.top = f;
    }

    public float getBottom() {
        return this.bottom;
    }

    public void setBottom(float f) {
        this.bottom = f;
    }

    /* loaded from: classes2.dex */
    public static class Iterator {
        public final float mBitmapHeight;
        public final float mBitmapWidth;
        public GLRectF mGLRectF = new GLRectF();
        public int mSize;
        public final float mXPixel;
        public float mXStep;
        public final float mYPixel;
        public float mYStep;

        public Iterator(float f, float f2) {
            this.mBitmapWidth = f;
            this.mBitmapHeight = f2;
            this.mXPixel = 1.0f / f;
            this.mYPixel = 1.0f / f2;
        }

        public void countMiddleRect(GLRectF gLRectF, GLRectF gLRectF2) {
            countMiddleRect(gLRectF, gLRectF2, Math.max(gLRectF.width() * this.mBitmapWidth, gLRectF.height() * this.mBitmapHeight) / 5.0f);
        }

        public void countMiddleRect(GLRectF gLRectF, GLRectF gLRectF2, float f) {
            float centerX = gLRectF2.centerX() - gLRectF.centerX();
            float centerY = gLRectF2.centerY() - gLRectF.centerY();
            int max = Math.max(Math.abs(Math.round((centerX / this.mXPixel) / f)), Math.abs(Math.round((centerY / this.mYPixel) / f)));
            this.mSize = max;
            this.mXStep = centerX / (max + 1);
            this.mYStep = centerY / (max + 1);
            this.mGLRectF.set(gLRectF);
        }

        public boolean hasNext() {
            return this.mSize >= 0;
        }

        public void next(float[] fArr) {
            if (this.mSize < 0) {
                throw new RuntimeException("iterator size error!!!");
            }
            this.mGLRectF.offset(this.mXStep, this.mYStep);
            this.mGLRectF.getVertex(fArr);
            this.mSize--;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.left);
        parcel.writeFloat(this.top);
        parcel.writeFloat(this.right);
        parcel.writeFloat(this.bottom);
    }

    public GLRectF(Parcel parcel) {
        this.left = parcel.readFloat();
        this.top = parcel.readFloat();
        this.right = parcel.readFloat();
        this.bottom = parcel.readFloat();
    }
}
