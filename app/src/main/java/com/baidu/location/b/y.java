package com.baidu.location.b;

import android.location.GnssNavigationMessage;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class y extends Handler {
    public final /* synthetic */ x a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public y(x xVar, Looper looper) {
        super(looper);
        this.a = xVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        com.baidu.location.c.a c;
        com.baidu.location.c.h n;
        Location d;
        String a;
        Handler handler;
        Handler handler2;
        int i = message.what;
        if (i == 1) {
            Bundle data = message.getData();
            try {
                Location location = (Location) data.getParcelable("loc");
                data.getInt("satnum");
                if (location == null) {
                    return;
                }
                g.a().a(location);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (i == 2) {
            c = s.c();
            n = com.baidu.location.c.i.a().n();
            d = s.d();
            a = s.a();
        } else if (i != 3) {
            if (i == 4) {
                boolean i2 = com.baidu.location.c.i.a().i();
                if (com.baidu.location.e.j.b()) {
                    i2 = false;
                }
                if (i2) {
                    k.a().d();
                }
                try {
                    handler = this.a.d;
                    if (handler != null) {
                        handler2 = this.a.d;
                        handler2.sendEmptyMessageDelayed(4, com.baidu.location.e.j.R);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                v.a().b();
                return;
            } else if (i == 7) {
                w.a().c();
                return;
            } else if (i == 8 || i == 9) {
                message.getData();
                return;
            } else if (i != 11) {
                return;
            } else {
                Bundle data2 = message.getData();
                try {
                    long j = data2.getLong("gps_time");
                    v.a().a((GnssNavigationMessage) data2.getParcelable("gnss_navigation_message"), j);
                    return;
                } catch (Exception unused) {
                    return;
                }
            }
        } else {
            c = s.c();
            n = null;
            d = s.d();
            a = b.a().c();
        }
        w.a(c, n, d, a);
    }
}
