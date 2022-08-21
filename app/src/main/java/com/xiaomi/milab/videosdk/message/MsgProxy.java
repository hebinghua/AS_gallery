package com.xiaomi.milab.videosdk.message;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class MsgProxy {
    private static final String TAG = "MsgProxy";
    private static SparseArray<List<Handler>> msgIdentiSpArray = new SparseArray<>();

    public static void registerMessageHandler(int i, Handler handler) {
        if (handler == null) {
            return;
        }
        synchronized (msgIdentiSpArray) {
            List<Handler> list = msgIdentiSpArray.get(i);
            if (list != null) {
                if (!list.contains(handler)) {
                    list.add(handler);
                }
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(handler);
                msgIdentiSpArray.put(i, arrayList);
            }
        }
    }

    public static void unRegisterMessageHandler(int i, Handler handler) {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            synchronized (msgIdentiSpArray) {
                List<Handler> list = msgIdentiSpArray.get(i);
                if (list != null) {
                    list.remove(handler);
                }
            }
        }
    }

    public static void dispatchMessage(int i, int i2, int i3, Object obj) {
        synchronized (msgIdentiSpArray) {
            List<Handler> list = msgIdentiSpArray.get(i);
            if (list != null && !list.isEmpty()) {
                for (Handler handler : list) {
                    Message.obtain(handler, i, i2, i3, obj).sendToTarget();
                }
            }
        }
    }

    public static void destory() {
        int size = msgIdentiSpArray.size();
        for (int i = 0; i < size; i++) {
            SparseArray<List<Handler>> sparseArray = msgIdentiSpArray;
            List<Handler> list = sparseArray.get(sparseArray.keyAt(i));
            if (list != null) {
                list.clear();
            }
        }
        msgIdentiSpArray.clear();
    }
}
