package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import com.xiaomi.onetrack.util.p;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class m {
    public String a = "com.mdid.msa";
    public final LinkedBlockingQueue<IBinder> b = new LinkedBlockingQueue<>(1);
    public ServiceConnection c = new ServiceConnection() { // from class: com.xiaomi.onetrack.util.oaid.helpers.ZTEDeviceIDHelper$1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                m.this.b.offer(iBinder, 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                p.a("ZTEDeviceIDHelper", e.getMessage());
            }
        }
    };

    public final void a(String str, Context context) {
        Intent intent = new Intent();
        intent.setClassName(this.a, "com.mdid.msa.service.MsaKlService");
        intent.setAction("com.bun.msa.action.start.service");
        intent.putExtra("com.bun.msa.param.pkgname", str);
        try {
            intent.putExtra("com.bun.msa.param.runinset", true);
            context.startService(intent);
        } catch (Exception e) {
            p.a("ZTEDeviceIDHelper", e.getMessage());
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x006a -> B:33:0x0091). Please submit an issue!!! */
    public String a(Context context) {
        IBinder poll;
        try {
            context.getPackageManager().getPackageInfo(this.a, 0);
        } catch (Exception e) {
            p.a("ZTEDeviceIDHelper", e.getMessage());
        }
        String packageName = context.getPackageName();
        a(packageName, context);
        Intent intent = new Intent();
        intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaIdService");
        intent.setAction("com.bun.msa.action.bindto.service");
        intent.putExtra("com.bun.msa.param.pkgname", packageName);
        String str = "";
        try {
            try {
            } catch (Throwable th) {
                try {
                    context.unbindService(this.c);
                } catch (Exception e2) {
                    p.a("ZTEDeviceIDHelper", e2.getMessage());
                }
                throw th;
            }
        } catch (Exception e3) {
            String message = e3.getMessage();
            p.a("ZTEDeviceIDHelper", message);
            context = message;
        }
        if (context.bindService(intent, this.c, 1)) {
            try {
                poll = this.b.poll(1L, TimeUnit.SECONDS);
            } catch (Exception e4) {
                p.a("ZTEDeviceIDHelper", e4.getMessage());
                context.unbindService(this.c);
                context = context;
            }
            if (poll != null) {
                str = new com.xiaomi.onetrack.util.oaid.a.g(poll) { // from class: com.xiaomi.onetrack.util.oaid.a.g$a$a
                    public IBinder a;

                    {
                        this.a = poll;
                    }

                    @Override // android.os.IInterface
                    public IBinder asBinder() {
                        return this.a;
                    }

                    @Override // com.xiaomi.onetrack.util.oaid.a.g
                    public String b() {
                        String str2;
                        Parcel obtain = Parcel.obtain();
                        Parcel obtain2 = Parcel.obtain();
                        try {
                            obtain.writeInterfaceToken("com.bun.lib.MsaIdInterface");
                            this.a.transact(3, obtain, obtain2, 0);
                            obtain2.readException();
                            str2 = obtain2.readString();
                        } catch (Throwable unused) {
                            obtain2.recycle();
                            obtain.recycle();
                            str2 = null;
                        }
                        obtain2.recycle();
                        obtain.recycle();
                        return str2;
                    }
                }.b();
                context.unbindService(this.c);
                context = context;
            } else {
                try {
                    context.unbindService(this.c);
                } catch (Exception e5) {
                    p.a("ZTEDeviceIDHelper", e5.getMessage());
                }
                return str;
            }
        }
        return str;
    }
}
