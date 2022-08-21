package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class BuildingInfo implements Parcelable {
    public static final Parcelable.Creator<BuildingInfo> CREATOR = new a();
    private float a;
    private int b;
    private String c;
    private String d;

    public BuildingInfo() {
    }

    public BuildingInfo(Parcel parcel) {
        this.a = parcel.readFloat();
        this.b = parcel.readInt();
        this.c = parcel.readString();
        this.d = parcel.readString();
    }

    public float a() {
        return this.a;
    }

    public String b() {
        return this.c;
    }

    public String c() {
        return this.d;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("BuidingInfo: \n");
        stringBuffer.append("; height = ");
        stringBuffer.append(this.a);
        stringBuffer.append("; accuracy = ");
        stringBuffer.append(this.b);
        stringBuffer.append("; geom = ");
        stringBuffer.append(this.c);
        stringBuffer.append("; center = ");
        stringBuffer.append(this.d);
        return stringBuffer.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.a);
        parcel.writeInt(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
    }
}
