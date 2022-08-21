package com.cdv.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/* loaded from: classes.dex */
public class NvAndroidHandler implements Handler.Callback {
    private Handler m_handler;
    private int m_id;

    private static native void notifyHandlerMessage(int i, int i2, int i3, int i4);

    private NvAndroidHandler(int i, Looper looper) {
        this.m_handler = null;
        this.m_id = 0;
        try {
            if (looper == null) {
                this.m_handler = new Handler(this);
            } else {
                this.m_handler = new Handler(looper, this);
            }
        } catch (Exception e) {
            Log.e("NvAndroidHandler", "" + e.getMessage());
        }
        this.m_id = i;
    }

    public boolean sendMessage(int i, int i2, int i3) {
        Message obtain;
        if (this.m_handler == null || (obtain = Message.obtain()) == null) {
            return false;
        }
        obtain.what = i;
        obtain.arg1 = i2;
        obtain.arg2 = i3;
        return this.m_handler.sendMessage(obtain);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        notifyHandlerMessage(this.m_id, message.what, message.arg1, message.arg2);
        return true;
    }
}
