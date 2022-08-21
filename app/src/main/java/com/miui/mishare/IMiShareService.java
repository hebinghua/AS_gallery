package com.miui.mishare;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IMiShareService extends IInterface {
    void discover(IMiShareDiscoverCallback iMiShareDiscoverCallback) throws RemoteException;

    void discoverWithIntent(IMiShareDiscoverCallback iMiShareDiscoverCallback, Intent intent) throws RemoteException;

    int getState() throws RemoteException;

    void registerStateListener(IMiShareStateListener iMiShareStateListener) throws RemoteException;

    void registerTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) throws RemoteException;

    void stopDiscover(IMiShareDiscoverCallback iMiShareDiscoverCallback) throws RemoteException;

    void unregisterStateListener(IMiShareStateListener iMiShareStateListener) throws RemoteException;

    void unregisterTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMiShareService {
        public static IMiShareService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.miui.mishare.IMiShareService");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IMiShareService)) {
                return (IMiShareService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes3.dex */
        public static class Proxy implements IMiShareService {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.miui.mishare.IMiShareService
            public int getState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.miui.mishare.IMiShareService
            public void registerStateListener(IMiShareStateListener iMiShareStateListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    obtain.writeStrongBinder(iMiShareStateListener != null ? iMiShareStateListener.asBinder() : null);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.miui.mishare.IMiShareService
            public void unregisterStateListener(IMiShareStateListener iMiShareStateListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    obtain.writeStrongBinder(iMiShareStateListener != null ? iMiShareStateListener.asBinder() : null);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.miui.mishare.IMiShareService
            public void discover(IMiShareDiscoverCallback iMiShareDiscoverCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    obtain.writeStrongBinder(iMiShareDiscoverCallback != null ? iMiShareDiscoverCallback.asBinder() : null);
                    this.mRemote.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.miui.mishare.IMiShareService
            public void discoverWithIntent(IMiShareDiscoverCallback iMiShareDiscoverCallback, Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    obtain.writeStrongBinder(iMiShareDiscoverCallback != null ? iMiShareDiscoverCallback.asBinder() : null);
                    if (intent != null) {
                        obtain.writeInt(1);
                        intent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.miui.mishare.IMiShareService
            public void stopDiscover(IMiShareDiscoverCallback iMiShareDiscoverCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    obtain.writeStrongBinder(iMiShareDiscoverCallback != null ? iMiShareDiscoverCallback.asBinder() : null);
                    this.mRemote.transact(8, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.miui.mishare.IMiShareService
            public void registerTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    obtain.writeStrongBinder(iMiShareTaskStateListener != null ? iMiShareTaskStateListener.asBinder() : null);
                    this.mRemote.transact(11, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.miui.mishare.IMiShareService
            public void unregisterTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.mishare.IMiShareService");
                    obtain.writeStrongBinder(iMiShareTaskStateListener != null ? iMiShareTaskStateListener.asBinder() : null);
                    this.mRemote.transact(12, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
