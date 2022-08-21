package com.miui.mishare;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IMiShareTaskStateListener extends IInterface {
    void onTaskIdChanged(MiShareTask miShareTask) throws RemoteException;

    void onTaskStateChanged(String str, int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMiShareTaskStateListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.miui.mishare.IMiShareTaskStateListener");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.miui.mishare.IMiShareTaskStateListener");
                onTaskStateChanged(parcel.readString(), parcel.readInt());
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.miui.mishare.IMiShareTaskStateListener");
                onTaskIdChanged(parcel.readInt() != 0 ? MiShareTask.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i == 1598968902) {
                parcel2.writeString("com.miui.mishare.IMiShareTaskStateListener");
                return true;
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
