package com.miui.mishare;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IMiShareStateListener extends IInterface {
    void onStateChanged(int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMiShareStateListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.miui.mishare.IMiShareStateListener");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.miui.mishare.IMiShareStateListener");
                onStateChanged(parcel.readInt());
                return true;
            } else if (i == 1598968902) {
                parcel2.writeString("com.miui.mishare.IMiShareStateListener");
                return true;
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
