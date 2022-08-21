package com.miui.video.localvideoplayer;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;
import com.miui.video.gallery.galleryvideo.gallery.GalleryVideoInfo;

/* loaded from: classes3.dex */
public interface VideoFrameInterface extends IInterface {
    FrameParams getFrameList(String str, int i, int i2) throws RemoteException;

    GalleryVideoInfo getVideoInfo(String str) throws RemoteException;

    void prepare(String str) throws RemoteException;

    void release(String str) throws RemoteException;

    void setSurface(Surface surface, int i, String str) throws RemoteException;

    void showPreviewFrameAtTime(Surface surface, int i, String str, long j) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements VideoFrameInterface {
        public static VideoFrameInterface asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.miui.video.localvideoplayer.VideoFrameInterface");
            if (queryLocalInterface != null && (queryLocalInterface instanceof VideoFrameInterface)) {
                return (VideoFrameInterface) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes3.dex */
        public static class Proxy implements VideoFrameInterface {
            public static VideoFrameInterface sDefaultImpl;
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.miui.video.localvideoplayer.VideoFrameInterface
            public void showPreviewFrameAtTime(Surface surface, int i, String str, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.video.localvideoplayer.VideoFrameInterface");
                    if (surface != null) {
                        obtain.writeInt(1);
                        surface.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().showPreviewFrameAtTime(surface, i, str, j);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.miui.video.localvideoplayer.VideoFrameInterface
            public FrameParams getFrameList(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.video.localvideoplayer.VideoFrameInterface");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFrameList(str, i, i2);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? FrameParams.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.miui.video.localvideoplayer.VideoFrameInterface
            public void prepare(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.video.localvideoplayer.VideoFrameInterface");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().prepare(str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.miui.video.localvideoplayer.VideoFrameInterface
            public void setSurface(Surface surface, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.video.localvideoplayer.VideoFrameInterface");
                    if (surface != null) {
                        obtain.writeInt(1);
                        surface.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSurface(surface, i, str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.miui.video.localvideoplayer.VideoFrameInterface
            public void release(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.video.localvideoplayer.VideoFrameInterface");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().release(str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.miui.video.localvideoplayer.VideoFrameInterface
            public GalleryVideoInfo getVideoInfo(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.video.localvideoplayer.VideoFrameInterface");
                    obtain.writeString(str);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVideoInfo(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? GalleryVideoInfo.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static VideoFrameInterface getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
