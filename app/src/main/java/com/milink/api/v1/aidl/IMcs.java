package com.milink.api.v1.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IMcs extends IInterface {
    int connect(String str, int i) throws RemoteException;

    int disconnect() throws RemoteException;

    int getPlaybackDuration() throws RemoteException;

    int getPlaybackProgress() throws RemoteException;

    int getPlaybackRate() throws RemoteException;

    void setDataSource(IMcsDataSource iMcsDataSource) throws RemoteException;

    void setDelegate(IMcsDelegate iMcsDelegate) throws RemoteException;

    void setDeviceListener(IMcsDeviceListener iMcsDeviceListener) throws RemoteException;

    void setDeviceName(String str) throws RemoteException;

    int setPlaybackProgress(int i) throws RemoteException;

    int setPlaybackRate(int i) throws RemoteException;

    int show(String str) throws RemoteException;

    void showScanList(IMcsScanListCallback iMcsScanListCallback, int i) throws RemoteException;

    int startPlayAudio(String str, String str2, int i, double d) throws RemoteException;

    int startPlayAudioEx(String str, String str2, String str3, int i, double d) throws RemoteException;

    int startPlayVideo(String str, String str2, int i, double d) throws RemoteException;

    int startPlayVideoEx(String str, String str2, String str3, int i, double d) throws RemoteException;

    int startShow() throws RemoteException;

    int startSlideshow(int i, boolean z) throws RemoteException;

    int stopPlay() throws RemoteException;

    int stopShow() throws RemoteException;

    void unsetDataSource(IMcsDataSource iMcsDataSource) throws RemoteException;

    void unsetDelegate(IMcsDelegate iMcsDelegate) throws RemoteException;

    void unsetDeviceListener(IMcsDeviceListener iMcsDeviceListener) throws RemoteException;

    int zoomPhoto(String str, int i, int i2, int i3, int i4, int i5, int i6, float f) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMcs {
        public static IMcs asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.milink.api.v1.aidl.IMcs");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IMcs)) {
                return (IMcs) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes.dex */
        public static class Proxy implements IMcs {
            public static IMcs sDefaultImpl;
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void setDeviceListener(IMcsDeviceListener iMcsDeviceListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeStrongBinder(iMcsDeviceListener != null ? iMcsDeviceListener.asBinder() : null);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceListener(iMcsDeviceListener);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void unsetDeviceListener(IMcsDeviceListener iMcsDeviceListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeStrongBinder(iMcsDeviceListener != null ? iMcsDeviceListener.asBinder() : null);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unsetDeviceListener(iMcsDeviceListener);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void setDataSource(IMcsDataSource iMcsDataSource) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeStrongBinder(iMcsDataSource != null ? iMcsDataSource.asBinder() : null);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDataSource(iMcsDataSource);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void unsetDataSource(IMcsDataSource iMcsDataSource) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeStrongBinder(iMcsDataSource != null ? iMcsDataSource.asBinder() : null);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unsetDataSource(iMcsDataSource);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void setDelegate(IMcsDelegate iMcsDelegate) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeStrongBinder(iMcsDelegate != null ? iMcsDelegate.asBinder() : null);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDelegate(iMcsDelegate);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void unsetDelegate(IMcsDelegate iMcsDelegate) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeStrongBinder(iMcsDelegate != null ? iMcsDelegate.asBinder() : null);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unsetDelegate(iMcsDelegate);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void setDeviceName(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceName(str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public void showScanList(IMcsScanListCallback iMcsScanListCallback, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeStrongBinder(iMcsScanListCallback != null ? iMcsScanListCallback.asBinder() : null);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showScanList(iMcsScanListCallback, i);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int connect(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().connect(str, i);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int disconnect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    if (!this.mRemote.transact(17, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disconnect();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int startShow() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    if (!this.mRemote.transact(18, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startShow();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int show(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(19, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().show(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int zoomPhoto(String str, int i, int i2, int i3, int i4, int i5, int i6, float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeInt(i5);
                    obtain.writeInt(i6);
                    obtain.writeFloat(f);
                    try {
                        if (!this.mRemote.transact(20, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                            int zoomPhoto = Stub.getDefaultImpl().zoomPhoto(str, i, i2, i3, i4, i5, i6, f);
                            obtain2.recycle();
                            obtain.recycle();
                            return zoomPhoto;
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

            @Override // com.milink.api.v1.aidl.IMcs
            public int stopShow() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    if (!this.mRemote.transact(21, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopShow();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int startSlideshow(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(22, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startSlideshow(i, z);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int startPlayVideo(String str, String str2, int i, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeDouble(d);
                    if (!this.mRemote.transact(24, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startPlayVideo(str, str2, i, d);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int startPlayAudio(String str, String str2, int i, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeDouble(d);
                    if (!this.mRemote.transact(25, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startPlayAudio(str, str2, i, d);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int stopPlay() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    if (!this.mRemote.transact(26, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopPlay();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int setPlaybackRate(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(27, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPlaybackRate(i);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int getPlaybackRate() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    if (!this.mRemote.transact(28, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPlaybackRate();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int setPlaybackProgress(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(29, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPlaybackProgress(i);
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int getPlaybackProgress() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    if (!this.mRemote.transact(30, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPlaybackProgress();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int getPlaybackDuration() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    if (!this.mRemote.transact(31, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPlaybackDuration();
                    }
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.milink.api.v1.aidl.IMcs
            public int startPlayVideoEx(String str, String str2, String str3, int i, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeInt(i);
                    obtain.writeDouble(d);
                    try {
                        if (!this.mRemote.transact(35, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                            int startPlayVideoEx = Stub.getDefaultImpl().startPlayVideoEx(str, str2, str3, i, d);
                            obtain2.recycle();
                            obtain.recycle();
                            return startPlayVideoEx;
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

            @Override // com.milink.api.v1.aidl.IMcs
            public int startPlayAudioEx(String str, String str2, String str3, int i, double d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.milink.api.v1.aidl.IMcs");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeInt(i);
                    obtain.writeDouble(d);
                    try {
                        if (!this.mRemote.transact(36, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                            int startPlayAudioEx = Stub.getDefaultImpl().startPlayAudioEx(str, str2, str3, i, d);
                            obtain2.recycle();
                            obtain.recycle();
                            return startPlayAudioEx;
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
        }

        public static IMcs getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
