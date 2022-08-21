package com.xiaomi.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;

/* loaded from: classes3.dex */
public class as implements au {
    public static boolean a;

    /* renamed from: a  reason: collision with other field name */
    public Context f99a;

    /* renamed from: a  reason: collision with other field name */
    public ServiceConnection f100a;

    /* renamed from: a  reason: collision with other field name */
    public volatile int f98a = 0;

    /* renamed from: a  reason: collision with other field name */
    public volatile String f102a = null;

    /* renamed from: b  reason: collision with other field name */
    public volatile boolean f103b = false;
    public volatile String b = null;

    /* renamed from: a  reason: collision with other field name */
    public final Object f101a = new Object();

    /* loaded from: classes3.dex */
    public class a implements ServiceConnection {
        public a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                as.this.f102a = b.a(iBinder);
                as.this.f103b = b.m1942a(iBinder);
                as.this.b();
                as.this.f98a = 2;
                synchronized (as.this.f101a) {
                    try {
                        as.this.f101a.notifyAll();
                    } catch (Exception unused) {
                    }
                }
            } catch (Exception unused2) {
                as.this.b();
                as.this.f98a = 2;
                synchronized (as.this.f101a) {
                    try {
                        as.this.f101a.notifyAll();
                    } catch (Exception unused3) {
                    }
                }
            } catch (Throwable th) {
                as.this.b();
                as.this.f98a = 2;
                synchronized (as.this.f101a) {
                    try {
                        as.this.f101a.notifyAll();
                    } catch (Exception unused4) {
                    }
                    throw th;
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    /* loaded from: classes3.dex */
    public static class b {
        public static String a(IBinder iBinder) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.uodis.opendevice.aidl.OpenDeviceIdentifierService");
                iBinder.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readString();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public static boolean m1942a(IBinder iBinder) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.uodis.opendevice.aidl.OpenDeviceIdentifierService");
                boolean z = false;
                iBinder.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                return z;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }

    public as(Context context) {
        this.f99a = context;
        mo1967a();
    }

    public static boolean a(Context context) {
        boolean z;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.huawei.hwid", 128);
            z = (packageInfo.applicationInfo.flags & 1) != 0;
            a = packageInfo.versionCode >= 20602000;
        } catch (Exception unused) {
        }
        return z;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public String mo1967a() {
        a("getOAID");
        return this.f102a;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a  reason: collision with other method in class */
    public final void mo1967a() {
        boolean z;
        this.f100a = new a();
        Intent intent = new Intent("com.uodis.opendevice.OPENIDS_SERVICE");
        intent.setPackage("com.huawei.hwid");
        int i = 1;
        try {
            z = this.f99a.bindService(intent, this.f100a, 1);
        } catch (Exception unused) {
            z = false;
        }
        if (!z) {
            i = 2;
        }
        this.f98a = i;
    }

    public final void a(String str) {
        if (this.f98a != 1 || Looper.myLooper() == Looper.getMainLooper()) {
            return;
        }
        synchronized (this.f101a) {
            try {
                com.xiaomi.channel.commonutils.logger.b.m1859a("huawei's " + str + " wait...");
                this.f101a.wait(3000L);
            } catch (Exception unused) {
            }
        }
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public boolean mo1967a() {
        return a;
    }

    public final void b() {
        ServiceConnection serviceConnection = this.f100a;
        if (serviceConnection != null) {
            try {
                this.f99a.unbindService(serviceConnection);
            } catch (Exception unused) {
            }
        }
    }
}
