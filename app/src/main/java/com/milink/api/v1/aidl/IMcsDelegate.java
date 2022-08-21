package com.milink.api.v1.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IMcsDelegate extends IInterface {
    void onConnected() throws RemoteException;

    void onConnectedFailed() throws RemoteException;

    void onDisconnected() throws RemoteException;

    void onLoading() throws RemoteException;

    void onNextAudio(boolean z) throws RemoteException;

    void onPaused() throws RemoteException;

    void onPlaying() throws RemoteException;

    void onPrevAudio(boolean z) throws RemoteException;

    void onStopped() throws RemoteException;

    void onVolume(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMcsDelegate {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.milink.api.v1.aidl.IMcsDelegate");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString("com.milink.api.v1.aidl.IMcsDelegate");
                return true;
            }
            boolean z = false;
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onConnected();
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onConnectedFailed();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onDisconnected();
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onLoading();
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onPlaying();
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onStopped();
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onPaused();
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    onVolume(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    onNextAudio(z);
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface("com.milink.api.v1.aidl.IMcsDelegate");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    onPrevAudio(z);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }
}
