package com.xiaomi.stat.c;

import android.os.IBinder;
import android.os.RemoteException;
import com.xiaomi.a.a.a.a;
import com.xiaomi.stat.d.k;

/* loaded from: classes3.dex */
class e implements Runnable {
    public final /* synthetic */ IBinder a;
    public final /* synthetic */ d b;

    public e(d dVar, IBinder iBinder) {
        this.b = dVar;
        this.a = iBinder;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.xiaomi.a.a.a.a a = a.AbstractBinderC0106a.a(this.a);
        try {
            if (com.xiaomi.stat.b.e()) {
                if (com.xiaomi.stat.b.x()) {
                    d dVar = this.b;
                    dVar.a[0] = a.b(dVar.b, dVar.c);
                } else {
                    this.b.a[0] = null;
                }
            } else {
                d dVar2 = this.b;
                dVar2.a[0] = a.a(dVar2.b, dVar2.c);
            }
            k.b("UploadMode", " connected, do remote http post " + this.b.a[0]);
            synchronized (i.class) {
                try {
                    i.class.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (RemoteException e2) {
            k.e("UploadMode", " error while uploading the data by IPC." + e2.toString());
            this.b.a[0] = null;
        }
    }
}
