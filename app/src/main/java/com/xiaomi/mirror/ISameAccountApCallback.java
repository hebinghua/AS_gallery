package com.xiaomi.mirror;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes3.dex */
public interface ISameAccountApCallback extends IInterface {

    /* loaded from: classes3.dex */
    public static class Default implements ISameAccountApCallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.xiaomi.mirror.ISameAccountApCallback
        public void onApConnectedStatusUpdate(int i, Bundle bundle) {
        }

        @Override // com.xiaomi.mirror.ISameAccountApCallback
        public void onApInfoUpdate(Bundle bundle) {
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ISameAccountApCallback {
        private static final String DESCRIPTOR = "com.xiaomi.mirror.ISameAccountApCallback";
        public static final int TRANSACTION_onApConnectedStatusUpdate = 2;
        public static final int TRANSACTION_onApInfoUpdate = 1;

        /* loaded from: classes3.dex */
        public static class Proxy implements ISameAccountApCallback {
            public static ISameAccountApCallback sDefaultImpl;
            private IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.xiaomi.mirror.ISameAccountApCallback
            public void onApConnectedStatusUpdate(int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onApConnectedStatusUpdate(i, bundle);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.xiaomi.mirror.ISameAccountApCallback
            public void onApInfoUpdate(Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onApInfoUpdate(bundle);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISameAccountApCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISameAccountApCallback)) ? new Proxy(iBinder) : (ISameAccountApCallback) queryLocalInterface;
        }

        public static ISameAccountApCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ISameAccountApCallback iSameAccountApCallback) {
            if (Proxy.sDefaultImpl != null || iSameAccountApCallback == null) {
                return false;
            }
            Proxy.sDefaultImpl = iSameAccountApCallback;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            Bundle bundle = null;
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                if (parcel.readInt() != 0) {
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                onApInfoUpdate(bundle);
                return true;
            } else if (i != 2) {
                if (i != 1598968902) {
                    return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeString(DESCRIPTOR);
                return true;
            } else {
                parcel.enforceInterface(DESCRIPTOR);
                int readInt = parcel.readInt();
                if (parcel.readInt() != 0) {
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                onApConnectedStatusUpdate(readInt, bundle);
                return true;
            }
        }
    }

    void onApConnectedStatusUpdate(int i, Bundle bundle);

    void onApInfoUpdate(Bundle bundle);
}
