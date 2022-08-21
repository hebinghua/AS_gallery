package miuix.appcompat.app.floatingactivity.multiapp;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify;

/* loaded from: classes3.dex */
public interface IFloatingService extends IInterface {
    Bundle callServiceMethod(int i, Bundle bundle) throws RemoteException;

    int registerServiceNotify(IServiceNotify iServiceNotify, String str) throws RemoteException;

    void unregisterServiceNotify(IServiceNotify iServiceNotify, String str) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IFloatingService {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "miuix.appcompat.app.floatingactivity.multiapp.IFloatingService");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (parcel2 == null) {
                return super.onTransact(i, parcel, null, i2);
            }
            if (i == 2) {
                parcel.enforceInterface("miuix.appcompat.app.floatingactivity.multiapp.IFloatingService");
                parcel2.writeBundle(callServiceMethod(parcel.readInt(), parcel.readBundle(getClass().getClassLoader())));
                parcel2.writeNoException();
                return true;
            } else if (i == 3) {
                parcel.enforceInterface("miuix.appcompat.app.floatingactivity.multiapp.IFloatingService");
                parcel2.writeInt(registerServiceNotify(IServiceNotify.Stub.asInterface(parcel.readStrongBinder()), parcel.readString()));
                parcel2.writeNoException();
                return true;
            } else if (i != 4) {
                if (i == 1598968902) {
                    parcel2.writeString("miuix.appcompat.app.floatingactivity.multiapp.IFloatingService");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("miuix.appcompat.app.floatingactivity.multiapp.IFloatingService");
                unregisterServiceNotify(IServiceNotify.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                parcel2.writeNoException();
                return true;
            }
        }
    }
}
