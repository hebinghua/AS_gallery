package com.milink.sdk.photo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPhotoCastDataSource extends IInterface {
    String getNextPhoto(String str, boolean z) throws RemoteException;

    String getPrevPhoto(String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPhotoCastDataSource {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.milink.sdk.photo.IPhotoCastDataSource");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            boolean z = false;
            if (i == 1) {
                parcel.enforceInterface("com.milink.sdk.photo.IPhotoCastDataSource");
                String readString = parcel.readString();
                if (parcel.readInt() != 0) {
                    z = true;
                }
                String prevPhoto = getPrevPhoto(readString, z);
                parcel2.writeNoException();
                parcel2.writeString(prevPhoto);
                return true;
            } else if (i != 2) {
                if (i == 1598968902) {
                    parcel2.writeString("com.milink.sdk.photo.IPhotoCastDataSource");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.milink.sdk.photo.IPhotoCastDataSource");
                String readString2 = parcel.readString();
                if (parcel.readInt() != 0) {
                    z = true;
                }
                String nextPhoto = getNextPhoto(readString2, z);
                parcel2.writeNoException();
                parcel2.writeString(nextPhoto);
                return true;
            }
        }
    }
}
