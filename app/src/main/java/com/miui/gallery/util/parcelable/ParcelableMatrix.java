package com.miui.gallery.util.parcelable;

import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class ParcelableMatrix extends Matrix implements Parcelable {
    public static final Parcelable.Creator<ParcelableMatrix> CREATOR = new Parcelable.Creator<ParcelableMatrix>() { // from class: com.miui.gallery.util.parcelable.ParcelableMatrix.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ParcelableMatrix mo1741createFromParcel(Parcel parcel) {
            return new ParcelableMatrix(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ParcelableMatrix[] mo1742newArray(int i) {
            return new ParcelableMatrix[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        float[] fArr = new float[9];
        getValues(fArr);
        parcel.writeFloatArray(fArr);
    }

    public ParcelableMatrix() {
    }

    public ParcelableMatrix(Parcel parcel) {
        float[] fArr = new float[9];
        parcel.readFloatArray(fArr);
        setValues(fArr);
    }
}
