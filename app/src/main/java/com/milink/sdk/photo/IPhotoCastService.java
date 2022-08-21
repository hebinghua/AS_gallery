package com.milink.sdk.photo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPhotoCastService extends IInterface {
    int checkAccess() throws RemoteException;

    String getCastingDeviceName() throws RemoteException;

    int init(IPhotoCastCallback iPhotoCastCallback) throws RemoteException;

    int release(IPhotoCastCallback iPhotoCastCallback) throws RemoteException;

    int rotate(String str, float f) throws RemoteException;

    int setDataSource(IPhotoCastDataSource iPhotoCastDataSource) throws RemoteException;

    int show(String str) throws RemoteException;

    int startSlide() throws RemoteException;

    int stop() throws RemoteException;

    int stopSlide() throws RemoteException;

    int zoom(String str, int i, int i2, int i3, int i4, int i5, int i6, float f) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPhotoCastService {
        public static IPhotoCastService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.milink.sdk.photo.IPhotoCastService");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IPhotoCastService)) {
                return (IPhotoCastService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes.dex */
        public static class Proxy implements IPhotoCastService {
            public static IPhotoCastService sDefaultImpl;
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int init(IPhotoCastCallback iPhotoCastCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    obtain.writeStrongBinder(iPhotoCastCallback != null ? iPhotoCastCallback.asBinder() : null);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().init(iPhotoCastCallback);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int release(IPhotoCastCallback iPhotoCastCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    obtain.writeStrongBinder(iPhotoCastCallback != null ? iPhotoCastCallback.asBinder() : null);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().release(iPhotoCastCallback);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int checkAccess() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkAccess();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int setDataSource(IPhotoCastDataSource iPhotoCastDataSource) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    obtain.writeStrongBinder(iPhotoCastDataSource != null ? iPhotoCastDataSource.asBinder() : null);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDataSource(iPhotoCastDataSource);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public String getCastingDeviceName() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCastingDeviceName();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int show(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().show(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int zoom(String str, int i, int i2, int i3, int i4, int i5, int i6, float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeInt(i5);
                    obtain.writeInt(i6);
                    obtain.writeFloat(f);
                    try {
                        if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                            int zoom = Stub.getDefaultImpl().zoom(str, i, i2, i3, i4, i5, i6, f);
                            obtain2.recycle();
                            obtain.recycle();
                            return zoom;
                        }
                        obtain2.readException();
                        int readInt = obtain2.readInt();
                        obtain2.recycle();
                        obtain.recycle();
                        return readInt;
                    } catch (Throwable th) {
                        th = th;
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int rotate(String str, float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    obtain.writeString(str);
                    obtain.writeFloat(f);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().rotate(str, f);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int stop() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    if (!this.mRemote.transact(10, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stop();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int startSlide() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    if (!this.mRemote.transact(11, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startSlide();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.sdk.photo.IPhotoCastService
            public int stopSlide() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.sdk.photo.IPhotoCastService");
                    if (!this.mRemote.transact(12, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopSlide();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IPhotoCastService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
