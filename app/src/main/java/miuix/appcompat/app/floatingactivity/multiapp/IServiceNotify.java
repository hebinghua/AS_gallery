package miuix.appcompat.app.floatingactivity.multiapp;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IServiceNotify extends IInterface {
    Bundle notifyFromService(int i, Bundle bundle) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IServiceNotify {
        public static IServiceNotify asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IServiceNotify)) {
                return (IServiceNotify) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes3.dex */
        public static class Proxy implements IServiceNotify {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify
            public Bundle notifyFromService(int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify");
                    obtain.writeInt(i);
                    obtain.writeBundle(bundle);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    Bundle readBundle = obtain2.readBundle();
                    obtain2.readException();
                    return readBundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
