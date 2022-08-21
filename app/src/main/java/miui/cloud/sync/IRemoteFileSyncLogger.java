package miui.cloud.sync;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;

/* loaded from: classes3.dex */
public interface IRemoteFileSyncLogger extends IInterface {
    void release(ResultReceiver resultReceiver) throws RemoteException;

    void syncLog(String str, String str2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IRemoteFileSyncLogger {
        public static IRemoteFileSyncLogger asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("miui.cloud.sync.IRemoteFileSyncLogger");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IRemoteFileSyncLogger)) {
                return (IRemoteFileSyncLogger) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes3.dex */
        public static class Proxy implements IRemoteFileSyncLogger {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // miui.cloud.sync.IRemoteFileSyncLogger
            public void syncLog(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("miui.cloud.sync.IRemoteFileSyncLogger");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // miui.cloud.sync.IRemoteFileSyncLogger
            public void release(ResultReceiver resultReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("miui.cloud.sync.IRemoteFileSyncLogger");
                    if (resultReceiver != null) {
                        obtain.writeInt(1);
                        resultReceiver.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
