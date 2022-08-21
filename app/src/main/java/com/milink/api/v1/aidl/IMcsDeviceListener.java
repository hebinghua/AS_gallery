package com.milink.api.v1.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IMcsDeviceListener extends IInterface {
    void onDeviceFound(String str, String str2, String str3) throws RemoteException;

    void onDeviceFound2(String str, String str2, String str3, String str4, String str5, String str6) throws RemoteException;

    void onDeviceLost(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMcsDeviceListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.milink.api.v1.aidl.IMcsDeviceListener");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDeviceListener");
                onDeviceFound(parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDeviceListener");
                onDeviceFound2(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                return true;
            } else if (i != 3) {
                if (i == 1598968902) {
                    parcel2.writeString("com.milink.api.v1.aidl.IMcsDeviceListener");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDeviceListener");
                onDeviceLost(parcel.readString());
                parcel2.writeNoException();
                return true;
            }
        }
    }
}
