package cn.wps.moffice.service.exposed.permission;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface RequestPermissionService extends IInterface {
    void requestPermisson(RequestPermissionCallback requestPermissionCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements RequestPermissionService {
        public static RequestPermissionService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("cn.wps.moffice.service.exposed.permission.RequestPermissionService");
            if (queryLocalInterface != null && (queryLocalInterface instanceof RequestPermissionService)) {
                return (RequestPermissionService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes.dex */
        public static class Proxy implements RequestPermissionService {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // cn.wps.moffice.service.exposed.permission.RequestPermissionService
            public void requestPermisson(RequestPermissionCallback requestPermissionCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("cn.wps.moffice.service.exposed.permission.RequestPermissionService");
                    obtain.writeStrongBinder(requestPermissionCallback != null ? requestPermissionCallback.asBinder() : null);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
