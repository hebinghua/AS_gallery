package com.baidu.location.b;

import android.os.Bundle;
import com.xiaomi.milab.videosdk.message.MsgType;

/* loaded from: classes.dex */
public class c {
    private static Object a = new Object();
    private static c b;
    private int c = -1;

    public static c a() {
        c cVar;
        synchronized (a) {
            if (b == null) {
                b = new c();
            }
            cVar = b;
        }
        return cVar;
    }

    public void a(int i, int i2, String str) {
        if (i2 != this.c) {
            this.c = i2;
            Bundle bundle = new Bundle();
            bundle.putInt("loctype", i);
            bundle.putInt("diagtype", i2);
            bundle.putByteArray("diagmessage", str.getBytes());
            b.a().a(bundle, MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_CANCEL);
        }
    }
}
