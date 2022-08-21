package com.miui.gallery.editor.photo.core.imports.text.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;

/* loaded from: classes2.dex */
public class TextStatusData implements Parcelable {
    public static final Parcelable.Creator<TextStatusData> CREATOR = new Parcelable.Creator<TextStatusData>() { // from class: com.miui.gallery.editor.photo.core.imports.text.model.TextStatusData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public TextStatusData mo895createFromParcel(Parcel parcel) {
            return new TextStatusData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public TextStatusData[] mo896newArray(int i) {
            return new TextStatusData[i];
        }
    };
    public int color;
    public int gradientsColor;
    public boolean isStroke;
    public boolean isSubstrate;
    public String text;
    public AutoLineLayout.TextAlignment textAlignment;
    public boolean textBold;
    public boolean textShadow;
    public TextStyle textStyle;
    public float transparentProgress;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.color);
        parcel.writeFloat(this.transparentProgress);
        parcel.writeByte(this.textBold ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.textShadow ? (byte) 1 : (byte) 0);
        AutoLineLayout.TextAlignment textAlignment = this.textAlignment;
        parcel.writeInt(textAlignment == null ? -1 : textAlignment.ordinal());
        parcel.writeString(this.text);
        parcel.writeByte(this.isStroke ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isSubstrate ? (byte) 1 : (byte) 0);
    }

    public TextStatusData() {
    }

    public TextStatusData(Parcel parcel) {
        this.color = parcel.readInt();
        this.transparentProgress = parcel.readFloat();
        boolean z = true;
        this.textBold = parcel.readByte() != 0;
        this.textShadow = parcel.readByte() != 0;
        this.isStroke = parcel.readByte() != 0;
        this.isSubstrate = parcel.readByte() == 0 ? false : z;
        int readInt = parcel.readInt();
        this.textAlignment = readInt == -1 ? null : AutoLineLayout.TextAlignment.values()[readInt];
        this.text = parcel.readString();
    }
}
