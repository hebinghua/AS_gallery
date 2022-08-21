package com.android.systemui.screenshot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.systemui.screenshot.IScreenShotCallback;

/* loaded from: classes.dex */
public interface IBitmapService extends IInterface {
    void registerCallback(IScreenShotCallback iScreenShotCallback) throws RemoteException;

    void unregisterCallback(IScreenShotCallback iScreenShotCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBitmapService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.android.systemui.screenshot.IBitmapService");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.android.systemui.screenshot.IBitmapService");
                registerCallback(IScreenShotCallback.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                return true;
            } else if (i != 2) {
                if (i == 1598968902) {
                    parcel2.writeString("com.android.systemui.screenshot.IBitmapService");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.android.systemui.screenshot.IBitmapService");
                unregisterCallback(IScreenShotCallback.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                return true;
            }
        }
    }
}
