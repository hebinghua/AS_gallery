package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.xiaomi.onetrack.util.p;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class a {
    public final LinkedBlockingQueue<IBinder> a = new LinkedBlockingQueue<>(1);
    public ServiceConnection b = new ServiceConnection() { // from class: com.xiaomi.onetrack.util.oaid.helpers.ASUSDeviceIDHelper$1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                a.this.a.offer(iBinder, 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                p.a("ASUSDeviceIDHelper", e.getMessage());
            }
        }
    };

    /* JADX WARN: Type inference failed for: r3v3, types: [com.xiaomi.onetrack.util.oaid.a.a$a] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x0050 -> B:28:0x0077). Please submit an issue!!! */
    public String a(Context context) {
        IBinder poll;
        Intent intent = new Intent();
        intent.setAction("com.asus.msa.action.ACCESS_DID");
        intent.setComponent(new ComponentName("com.asus.msa.SupplementaryDID", "com.asus.msa.SupplementaryDID.SupplementaryDIDService"));
        String str = "";
        try {
        } catch (Exception e) {
            p.a("ASUSDeviceIDHelper", e.getMessage());
        }
        if (context.bindService(intent, this.b, 1)) {
            try {
                try {
                    poll = this.a.poll(1L, TimeUnit.SECONDS);
                } catch (Exception e2) {
                    p.a("ASUSDeviceIDHelper", e2.getMessage());
                    context.unbindService(this.b);
                }
                if (poll != null) {
                    str = new IInterface(poll) { // from class: com.xiaomi.onetrack.util.oaid.a.a$a
                        public IBinder a;

                        {
                            this.a = poll;
                        }

                        @Override // android.os.IInterface
                        public IBinder asBinder() {
                            return this.a;
                        }

                        public String a() {
                            String str2;
                            Parcel obtain = Parcel.obtain();
                            Parcel obtain2 = Parcel.obtain();
                            try {
                                obtain.writeInterfaceToken("com.asus.msa.SupplementaryDID.IDidAidlInterface");
                                this.a.transact(3, obtain, obtain2, 0);
                                obtain2.readException();
                                str2 = obtain2.readString();
                            } catch (Throwable th) {
                                obtain.recycle();
                                obtain2.recycle();
                                th.printStackTrace();
                                str2 = null;
                            }
                            obtain.recycle();
                            obtain2.recycle();
                            return str2;
                        }
                    }.a();
                    context.unbindService(this.b);
                } else {
                    try {
                        context.unbindService(this.b);
                    } catch (Exception e3) {
                        p.a("ASUSDeviceIDHelper", e3.getMessage());
                    }
                    return str;
                }
            } catch (Throwable th) {
                try {
                    context.unbindService(this.b);
                } catch (Exception e4) {
                    p.a("ASUSDeviceIDHelper", e4.getMessage());
                }
                throw th;
            }
        }
        return str;
    }
}
