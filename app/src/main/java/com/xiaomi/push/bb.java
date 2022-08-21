package com.xiaomi.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import java.security.MessageDigest;

/* loaded from: classes3.dex */
public class bb implements au {
    public static boolean a;

    /* renamed from: a  reason: collision with other field name */
    public Context f122a;

    /* renamed from: a  reason: collision with other field name */
    public ServiceConnection f123a;

    /* renamed from: a  reason: collision with other field name */
    public volatile int f121a = 0;

    /* renamed from: a  reason: collision with other field name */
    public volatile a f124a = null;

    /* renamed from: a  reason: collision with other field name */
    public final Object f125a = new Object();

    /* loaded from: classes3.dex */
    public class a {

        /* renamed from: a  reason: collision with other field name */
        public String f126a;
        public String b;
        public String c;
        public String d;

        public a() {
            this.f126a = null;
            this.b = null;
            this.c = null;
            this.d = null;
        }
    }

    /* loaded from: classes3.dex */
    public class b implements ServiceConnection {
        public b() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (bb.this.f124a != null) {
                return;
            }
            new Thread(new bd(this, iBinder)).start();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    /* loaded from: classes3.dex */
    public static class c {
        public static String a(IBinder iBinder, String str, String str2, String str3) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.heytap.openid.IOpenID");
                obtain.writeString(str);
                obtain.writeString(str2);
                obtain.writeString(str3);
                iBinder.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readString();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }

    public bb(Context context) {
        this.f122a = context;
        mo1967a();
    }

    public static boolean a(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.heytap.openid", 128);
            if (packageInfo != null) {
                long longVersionCode = Build.VERSION.SDK_INT >= 28 ? packageInfo.getLongVersionCode() : packageInfo.versionCode;
                boolean z = (packageInfo.applicationInfo.flags & 1) != 0;
                a = longVersionCode >= 1;
                if (z) {
                    return true;
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public String mo1967a() {
        a("getOAID");
        if (this.f124a == null) {
            return null;
        }
        return this.f124a.b;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a  reason: collision with other method in class */
    public final void mo1967a() {
        boolean z;
        this.f123a = new b();
        Intent intent = new Intent();
        intent.setClassName("com.heytap.openid", "com.heytap.openid.IdentifyService");
        intent.setAction("action.com.heytap.openid.OPEN_ID_SERVICE");
        int i = 1;
        try {
            z = this.f122a.bindService(intent, this.f123a, 1);
        } catch (Exception unused) {
            z = false;
        }
        if (!z) {
            i = 2;
        }
        this.f121a = i;
    }

    public final void a(String str) {
        if (this.f121a != 1 || Looper.myLooper() == Looper.getMainLooper()) {
            return;
        }
        synchronized (this.f125a) {
            try {
                com.xiaomi.channel.commonutils.logger.b.m1859a("oppo's " + str + " wait...");
                this.f125a.wait(3000L);
            } catch (Exception unused) {
            }
        }
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public boolean mo1967a() {
        return a;
    }

    public final String b() {
        try {
            Signature[] signatureArr = this.f122a.getPackageManager().getPackageInfo(this.f122a.getPackageName(), 64).signatures;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            StringBuilder sb = new StringBuilder();
            for (byte b2 : messageDigest.digest(signatureArr[0].toByteArray())) {
                sb.append(Integer.toHexString((b2 & 255) | 256).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public final void m1965b() {
        ServiceConnection serviceConnection = this.f123a;
        if (serviceConnection != null) {
            try {
                this.f122a.unbindService(serviceConnection);
            } catch (Exception unused) {
            }
        }
    }
}
