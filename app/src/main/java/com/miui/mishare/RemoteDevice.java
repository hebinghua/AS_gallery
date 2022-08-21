package com.miui.mishare;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes3.dex */
public class RemoteDevice implements Parcelable {
    public static final Parcelable.Creator<RemoteDevice> CREATOR = new Parcelable.Creator<RemoteDevice>() { // from class: com.miui.mishare.RemoteDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public RemoteDevice mo1841createFromParcel(Parcel parcel) {
            return new RemoteDevice(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public RemoteDevice[] mo1842newArray(int i) {
            return new RemoteDevice[i];
        }
    };
    public String mDeviceId;
    public Bundle mExtras;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public RemoteDevice(Parcel parcel) {
        this.mDeviceId = parcel.readString();
        this.mExtras = parcel.readBundle();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mDeviceId);
        parcel.writeBundle(this.mExtras);
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public boolean isPC() {
        Bundle bundle = this.mExtras;
        return bundle != null && bundle.getInt("sgnt") == 2;
    }

    public boolean equals(Object obj) {
        if (obj instanceof RemoteDevice) {
            return ((RemoteDevice) obj).mDeviceId.equals(this.mDeviceId);
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return Objects.hashCode(this.mDeviceId);
    }

    public String toString() {
        return "RemoteDevice: " + this.mDeviceId;
    }
}
