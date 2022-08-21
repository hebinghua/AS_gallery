package com.miui.gallery.magic.idphoto.bean;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import com.xiaomi.miai.api.StatusCode;

/* loaded from: classes2.dex */
public class PhotoStyle implements Parcelable {
    public int bgColor;
    public int hMM;
    public int height;
    public float q;
    public String sizeType;
    public boolean useDpi;
    public int wMM;
    public int width;
    public static String[] colors = {"#FFFFFF", "#0EABFC", "#49DAF3", "#D80F24", "#BDD672", "#F794B0", "#FFA176", "#7E7C8B", "#5892E2", "#DD3326"};
    public static final Parcelable.Creator<PhotoStyle> CREATOR = new Parcelable.Creator<PhotoStyle>() { // from class: com.miui.gallery.magic.idphoto.bean.PhotoStyle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public PhotoStyle mo1018createFromParcel(Parcel parcel) {
            return new PhotoStyle(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public PhotoStyle[] mo1019newArray(int i) {
            return new PhotoStyle[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PhotoStyle(Parcel parcel) {
        this.height = StatusCode.REQUEST_ENTITY_TOO_LARGE;
        this.width = 295;
        boolean z = false;
        this.bgColor = 0;
        this.hMM = 25;
        this.wMM = 35;
        this.q = 1.0f;
        this.useDpi = false;
        this.height = parcel.readInt();
        this.width = parcel.readInt();
        this.bgColor = parcel.readInt();
        this.hMM = parcel.readInt();
        this.wMM = parcel.readInt();
        this.q = parcel.readFloat();
        this.sizeType = parcel.readString();
        this.useDpi = parcel.readInt() != 0 ? true : z;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.height);
        parcel.writeInt(this.width);
        parcel.writeInt(this.bgColor);
        parcel.writeInt(this.hMM);
        parcel.writeInt(this.wMM);
        parcel.writeFloat(this.q);
        parcel.writeString(this.sizeType);
        parcel.writeInt(this.useDpi ? 1 : 0);
    }

    public PhotoStyle(int i, int i2) {
        this.height = StatusCode.REQUEST_ENTITY_TOO_LARGE;
        this.width = 295;
        this.bgColor = 0;
        this.hMM = 25;
        this.wMM = 35;
        this.q = 1.0f;
        this.useDpi = false;
        this.width = i;
        this.height = i2;
    }

    public PhotoStyle() {
        this.height = StatusCode.REQUEST_ENTITY_TOO_LARGE;
        this.width = 295;
        this.bgColor = 0;
        this.hMM = 25;
        this.wMM = 35;
        this.q = 1.0f;
        this.useDpi = false;
    }

    public PhotoStyle clonePhotoStyle() {
        PhotoStyle photoStyle = new PhotoStyle();
        photoStyle.q = this.q;
        photoStyle.bgColor = this.bgColor;
        return photoStyle;
    }

    public void sethMM(int i) {
        this.hMM = i;
    }

    public void setwMM(int i) {
        this.wMM = i;
    }

    public int gethMM() {
        return this.hMM;
    }

    public int getwMM() {
        return this.wMM;
    }

    public void setBgColor(int i) {
        this.bgColor = i;
    }

    public int getBgColor() {
        return this.bgColor;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public void setQ() {
        this.q = 1200.0f / this.width;
    }

    public int getColor() {
        return Color.parseColor(colors[this.bgColor]);
    }

    public int getBgWidth() {
        return (int) (this.width * this.q);
    }

    public int getBgHeight() {
        return (int) (this.height * this.q);
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return (int) (this.width * this.q);
    }

    public int getNormalWidth() {
        return this.width;
    }

    public int getNormalHeight() {
        return this.height;
    }

    public int getSaveWidth(int i) {
        int i2 = this.wMM;
        return i2 > 0 ? (int) ((i2 / 25.4d) * i) : this.width;
    }

    public int getSaveHeight(int i) {
        int i2 = this.hMM;
        return i2 > 0 ? (int) ((i2 / 25.4d) * i) : this.height;
    }

    public String toString() {
        return "PhotoStyle{height=" + this.height + ", width=" + this.width + ", bgColor=" + this.bgColor + '}';
    }

    public void setSizeType(String str) {
        this.sizeType = str;
    }

    public String getSizeType() {
        return this.sizeType;
    }

    public boolean isUseDpi() {
        return this.useDpi;
    }

    public void useDpi() {
        this.useDpi = true;
    }
}
