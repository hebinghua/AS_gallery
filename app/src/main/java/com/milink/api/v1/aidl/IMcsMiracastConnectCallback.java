package com.milink.api.v1.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IMcsMiracastConnectCallback extends IInterface {
    void onConnectFail(String str) throws RemoteException;

    void onConnectSuccess(String str) throws RemoteException;

    void onConnecting(String str) throws RemoteException;

    void onResult(int i, String str, String str2) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMcsMiracastConnectCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.milink.api.v1.aidl.IMcsMiracastConnectCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.milink.api.v1.aidl.IMcsMiracastConnectCallback");
                onConnectSuccess(parcel.readString());
                parcel2.writeNoException();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.milink.api.v1.aidl.IMcsMiracastConnectCallback");
                onConnectFail(parcel.readString());
                parcel2.writeNoException();
                return true;
            } else if (i == 3) {
                parcel.enforceInterface("com.milink.api.v1.aidl.IMcsMiracastConnectCallback");
                onConnecting(parcel.readString());
                parcel2.writeNoException();
                return true;
            } else if (i != 4) {
                if (i == 1598968902) {
                    parcel2.writeString("com.milink.api.v1.aidl.IMcsMiracastConnectCallback");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.milink.api.v1.aidl.IMcsMiracastConnectCallback");
                onResult(parcel.readInt(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                return true;
            }
        }
    }
}
