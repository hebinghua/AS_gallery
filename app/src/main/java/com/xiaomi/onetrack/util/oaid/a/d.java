package com.xiaomi.onetrack.util.oaid.a;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes3.dex */
public interface d extends IInterface {

    /* loaded from: classes3.dex */
    public static abstract class a extends Binder implements d {

        /* renamed from: com.xiaomi.onetrack.util.oaid.a.d$a$a  reason: collision with other inner class name */
        /* loaded from: classes3.dex */
        public static class C0110a implements d {
            public IBinder a;

            public C0110a(IBinder iBinder) {
                this.a = iBinder;
            }

            public String a(String str, String str2, String str3) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    try {
                        obtain.writeInterfaceToken("com.heytap.openid.IOpenID");
                        obtain.writeString(str);
                        obtain.writeString(str2);
                        obtain.writeString(str3);
                        this.a.transact(1, obtain, obtain2, 0);
                        obtain2.readException();
                        return obtain2.readString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        obtain.recycle();
                        obtain2.recycle();
                        return null;
                    }
                } finally {
                    obtain.recycle();
                    obtain2.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.a;
            }
        }

        public static d a(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.heytap.openid.IOpenID");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof d)) {
                return new C0110a(iBinder);
            }
            return (d) queryLocalInterface;
        }
    }
}
