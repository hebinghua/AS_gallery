package com.android.systemui.screenshot;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IScreenShotCallback extends IInterface {
    void quitThumnail() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IScreenShotCallback {
        public static IScreenShotCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.systemui.screenshot.IScreenShotCallback");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IScreenShotCallback)) {
                return (IScreenShotCallback) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes.dex */
        public static class Proxy implements IScreenShotCallback {
            public static IScreenShotCallback sDefaultImpl;
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.systemui.screenshot.IScreenShotCallback
            public void quitThumnail() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.android.systemui.screenshot.IScreenShotCallback");
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().quitThumnail();
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IScreenShotCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
