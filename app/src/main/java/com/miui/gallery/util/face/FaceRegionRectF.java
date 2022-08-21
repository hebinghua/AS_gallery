package com.miui.gallery.util.face;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Objects;

/* loaded from: classes2.dex */
public class FaceRegionRectF extends RectF {
    public static final Parcelable.Creator<FaceRegionRectF> CREATOR = new Parcelable.Creator<FaceRegionRectF>() { // from class: com.miui.gallery.util.face.FaceRegionRectF.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public FaceRegionRectF mo1730createFromParcel(Parcel parcel) {
            FaceRegionRectF faceRegionRectF = new FaceRegionRectF();
            faceRegionRectF.readFromParcel(parcel);
            return faceRegionRectF;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public FaceRegionRectF[] mo1731newArray(int i) {
            return new FaceRegionRectF[i];
        }
    };
    public int orientation;

    public FaceRegionRectF() {
    }

    public FaceRegionRectF(float f, float f2, float f3, float f4, int i) {
        super(reducePrecision(f), reducePrecision(f2), reducePrecision(f3), reducePrecision(f4));
        this.orientation = i;
    }

    public static FaceRegionRectF resolveFrom(String str) {
        try {
            String[] split = str.split(" ");
            float parseFloat = Float.parseFloat(split[0]);
            float parseFloat2 = Float.parseFloat(split[1]);
            float parseFloat3 = Float.parseFloat(split[2]);
            return new FaceRegionRectF(parseFloat, parseFloat2, parseFloat + parseFloat3, parseFloat2 + Float.parseFloat(split[3]), Integer.parseInt(split[4]));
        } catch (Exception unused) {
            DefaultLogger.e("FaceRegionRectF", "wrong face rect format: %s", str);
            return null;
        }
    }

    public static float reducePrecision(float f) {
        return (float) (Math.ceil(f * 100000.0f) / 100000.0d);
    }

    @Override // android.graphics.RectF
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof FaceRegionRectF) && super.equals(obj) && this.orientation == ((FaceRegionRectF) obj).orientation;
    }

    @Override // android.graphics.RectF
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.orientation));
    }

    @Override // android.graphics.RectF, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(((RectF) this).left);
        parcel.writeFloat(((RectF) this).top);
        parcel.writeFloat(((RectF) this).right);
        parcel.writeFloat(((RectF) this).bottom);
        parcel.writeInt(this.orientation);
    }

    @Override // android.graphics.RectF
    public void readFromParcel(Parcel parcel) {
        ((RectF) this).left = parcel.readFloat();
        ((RectF) this).top = parcel.readFloat();
        ((RectF) this).right = parcel.readFloat();
        ((RectF) this).bottom = parcel.readFloat();
        this.orientation = parcel.readInt();
    }

    @Override // android.graphics.RectF
    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append('[');
        sb.append(((RectF) this).left);
        sb.append(CoreConstants.COMMA_CHAR);
        sb.append(((RectF) this).top);
        sb.append("][");
        sb.append(((RectF) this).right);
        sb.append(CoreConstants.COMMA_CHAR);
        sb.append(((RectF) this).bottom);
        sb.append("][");
        sb.append(this.orientation);
        sb.append(']');
        return sb.toString();
    }
}
