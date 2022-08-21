package com.milink.sdk.photo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPhotoCastCallback extends IInterface {
    void onDisplayNum(int i) throws RemoteException;

    void onEnd() throws RemoteException;

    void onError(int i) throws RemoteException;

    void onInit() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPhotoCastCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.milink.sdk.photo.IPhotoCastCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.milink.sdk.photo.IPhotoCastCallback");
                onInit();
                parcel2.writeNoException();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.milink.sdk.photo.IPhotoCastCallback");
                onEnd();
                parcel2.writeNoException();
                return true;
            } else if (i == 3) {
                parcel.enforceInterface("com.milink.sdk.photo.IPhotoCastCallback");
                onError(parcel.readInt());
                parcel2.writeNoException();
                return true;
            } else if (i != 4) {
                if (i == 1598968902) {
                    parcel2.writeString("com.milink.sdk.photo.IPhotoCastCallback");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.milink.sdk.photo.IPhotoCastCallback");
                onDisplayNum(parcel.readInt());
                parcel2.writeNoException();
                return true;
            }
        }
    }
}
