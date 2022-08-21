package com.miui.gallery.editor.photo.origin;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface EditorOriginFunc extends IInterface {
    boolean handlerOrigin(OriginRenderData originRenderData) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements EditorOriginFunc {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.miui.gallery.editor.photo.origin.EditorOriginFunc");
        }

        public static EditorOriginFunc asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.miui.gallery.editor.photo.origin.EditorOriginFunc");
            if (queryLocalInterface != null && (queryLocalInterface instanceof EditorOriginFunc)) {
                return (EditorOriginFunc) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1) {
                if (i == 1598968902) {
                    parcel2.writeString("com.miui.gallery.editor.photo.origin.EditorOriginFunc");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel.enforceInterface("com.miui.gallery.editor.photo.origin.EditorOriginFunc");
            boolean handlerOrigin = handlerOrigin(parcel.readInt() != 0 ? OriginRenderData.CREATOR.createFromParcel(parcel) : null);
            parcel2.writeNoException();
            parcel2.writeInt(handlerOrigin ? 1 : 0);
            return true;
        }

        /* loaded from: classes2.dex */
        public static class Proxy implements EditorOriginFunc {
            public static EditorOriginFunc sDefaultImpl;
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.miui.gallery.editor.photo.origin.EditorOriginFunc
            public boolean handlerOrigin(OriginRenderData originRenderData) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.miui.gallery.editor.photo.origin.EditorOriginFunc");
                    boolean z = true;
                    if (originRenderData != null) {
                        obtain.writeInt(1);
                        originRenderData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().handlerOrigin(originRenderData);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z = false;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static EditorOriginFunc getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
