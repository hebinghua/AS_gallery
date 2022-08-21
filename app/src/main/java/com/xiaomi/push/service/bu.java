package com.xiaomi.push.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes3.dex */
public class bu implements ServiceConnection {
    public final /* synthetic */ ServiceClient a;

    public bu(ServiceClient serviceClient) {
        this.a = serviceClient;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        List<Message> list;
        List list2;
        Messenger messenger;
        synchronized (this.a) {
            this.a.f817b = new Messenger(iBinder);
            this.a.f818b = false;
            list = this.a.f815a;
            for (Message message : list) {
                try {
                    messenger = this.a.f817b;
                    messenger.send(message);
                } catch (RemoteException e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                }
            }
            list2 = this.a.f815a;
            list2.clear();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.a.f817b = null;
        this.a.f818b = false;
    }
}
