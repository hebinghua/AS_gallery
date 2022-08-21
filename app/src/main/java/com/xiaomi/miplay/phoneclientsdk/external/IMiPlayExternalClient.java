package com.xiaomi.miplay.phoneclientsdk.external;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes3.dex */
public interface IMiPlayExternalClient extends IInterface {
    int cancelCirculate(String str, int i) throws RemoteException;

    int getCirculateMode() throws RemoteException;

    int getVolume(String str) throws RemoteException;

    int initAsync(String str, IMiPlayExternalClientCallback iMiPlayExternalClientCallback) throws RemoteException;

    int pause(String str) throws RemoteException;

    int play(String str, MediaMetaData mediaMetaData) throws RemoteException;

    int resume(String str) throws RemoteException;

    int seek(String str, long j) throws RemoteException;

    int setVolume(String str, double d) throws RemoteException;

    int stop(String str) throws RemoteException;

    int unInit(String str) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMiPlayExternalClient {
        public static IMiPlayExternalClient asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IMiPlayExternalClient)) {
                return (IMiPlayExternalClient) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes3.dex */
        public static class Proxy implements IMiPlayExternalClient {
            public static IMiPlayExternalClient sDefaultImpl;
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int initAsync(String str, IMiPlayExternalClientCallback iMiPlayExternalClientCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iMiPlayExternalClientCallback != null ? iMiPlayExternalClientCallback.asBinder() : null);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().initAsync(str, iMiPlayExternalClientCallback);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int play(String str, MediaMetaData mediaMetaData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    if (mediaMetaData != null) {
                        obtain.writeInt(1);
                        mediaMetaData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().play(str, mediaMetaData);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int unInit(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unInit(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int stop(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stop(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int pause(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().pause(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int resume(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resume(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int seek(String str, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(10, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().seek(str, j);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int setVolume(String str, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    obtain.writeDouble(d);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setVolume(str, d);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int getVolume(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVolume(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int getCirculateMode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    if (!this.mRemote.transact(15, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCirculateMode();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient
            public int cancelCirculate(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(16, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().cancelCirculate(str, i);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IMiPlayExternalClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
