package com.xiaomi.stat.c;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.xiaomi.stat.d.k;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class d implements ServiceConnection {
    public final /* synthetic */ String[] a;
    public final /* synthetic */ String b;
    public final /* synthetic */ Map c;

    public d(String[] strArr, String str, Map map) {
        this.a = strArr;
        this.b = str;
        this.c = map;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        com.xiaomi.stat.b.e.a().execute(new e(this, iBinder));
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        k.b("UploadMode", "onServiceDisconnected " + componentName);
        synchronized (i.class) {
            try {
                i.class.notify();
            } catch (Exception unused) {
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onBindingDied(ComponentName componentName) {
        synchronized (i.class) {
            try {
                i.class.notify();
            } catch (Exception unused) {
            }
        }
    }
}
