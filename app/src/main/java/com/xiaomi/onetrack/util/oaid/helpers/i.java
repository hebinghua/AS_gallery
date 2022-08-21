package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.Signature;
import android.os.IBinder;
import com.xiaomi.onetrack.util.oaid.a.d;
import com.xiaomi.onetrack.util.p;
import java.security.MessageDigest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class i {
    public com.xiaomi.onetrack.util.oaid.a.d a;
    public final LinkedBlockingQueue<IBinder> b = new LinkedBlockingQueue<>(1);
    public ServiceConnection c = new ServiceConnection() { // from class: com.xiaomi.onetrack.util.oaid.helpers.OnePlusDeviceIDHelper$1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                i.this.b.offer(iBinder, 1L, TimeUnit.SECONDS);
            } catch (Exception e) {
                p.a("OnePlusDeviceIDHelper", e.getMessage());
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            i.this.a = null;
        }
    };
    public String e;

    public String a(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.heytap.openid", "com.heytap.openid.IdentifyService"));
        intent.setAction("action.com.heytap.openid.OPEN_ID_SERVICE");
        String str = "";
        if (context.bindService(intent, this.c, 1)) {
            try {
                try {
                    try {
                        IBinder poll = this.b.poll(1L, TimeUnit.SECONDS);
                        if (poll != null) {
                            com.xiaomi.onetrack.util.oaid.a.d a = d.a.a(poll);
                            this.a = a;
                            if (a != null) {
                                str = a("OUID", context);
                            }
                            context.unbindService(this.c);
                        } else {
                            try {
                                context.unbindService(this.c);
                            } catch (Exception e) {
                                p.a("OnePlusDeviceIDHelper", e.getMessage());
                            }
                            return str;
                        }
                    } catch (Exception e2) {
                        p.a("OnePlusDeviceIDHelper", e2.getMessage());
                        context.unbindService(this.c);
                    }
                } catch (Exception e3) {
                    p.a("OnePlusDeviceIDHelper", e3.getMessage());
                }
            } catch (Throwable th) {
                try {
                    context.unbindService(this.c);
                } catch (Exception e4) {
                    p.a("OnePlusDeviceIDHelper", e4.getMessage());
                }
                throw th;
            }
        }
        return str;
    }

    public final String a(String str, Context context) {
        Signature[] signatureArr;
        String packageName = context.getPackageName();
        if (this.e == null) {
            String str2 = null;
            try {
                signatureArr = context.getPackageManager().getPackageInfo(packageName, 64).signatures;
            } catch (Exception e) {
                p.a("OnePlusDeviceIDHelper", e.getMessage());
                signatureArr = null;
            }
            if (signatureArr != null && signatureArr.length > 0) {
                byte[] byteArray = signatureArr[0].toByteArray();
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                    if (messageDigest != null) {
                        byte[] digest = messageDigest.digest(byteArray);
                        StringBuilder sb = new StringBuilder();
                        for (byte b : digest) {
                            sb.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
                        }
                        str2 = sb.toString();
                    }
                } catch (Exception e2) {
                    p.a("OnePlusDeviceIDHelper", e2.getMessage());
                }
            }
            this.e = str2;
        }
        return ((d.a.C0110a) this.a).a(packageName, this.e, str);
    }
}
