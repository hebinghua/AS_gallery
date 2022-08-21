package cn.wps.moffice.service.exposed.permission;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface RequestPermissionCallback extends IInterface {
    void callback(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements RequestPermissionCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "cn.wps.moffice.service.exposed.permission.RequestPermissionCallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1) {
                if (i == 1598968902) {
                    parcel2.writeString("cn.wps.moffice.service.exposed.permission.RequestPermissionCallback");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel.enforceInterface("cn.wps.moffice.service.exposed.permission.RequestPermissionCallback");
            callback(parcel.readInt() != 0);
            parcel2.writeNoException();
            return true;
        }
    }
}
