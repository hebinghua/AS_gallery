package com.miui.mishare;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IMiShareDiscoverCallback extends IInterface {
    void onDeviceLost(String str) throws RemoteException;

    void onDeviceUpdated(RemoteDevice remoteDevice) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMiShareDiscoverCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.miui.mishare.IMiShareDiscoverCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.miui.mishare.IMiShareDiscoverCallback");
                onDeviceUpdated(parcel.readInt() != 0 ? RemoteDevice.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.miui.mishare.IMiShareDiscoverCallback");
                onDeviceLost(parcel.readString());
                return true;
            } else if (i == 1598968902) {
                parcel2.writeString("com.miui.mishare.IMiShareDiscoverCallback");
                return true;
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
