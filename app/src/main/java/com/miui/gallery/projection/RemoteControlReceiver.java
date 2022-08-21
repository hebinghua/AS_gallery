package com.miui.gallery.projection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class RemoteControlReceiver extends BroadcastReceiver {
    public RemoteControlListener mListener;

    /* loaded from: classes2.dex */
    public interface RemoteControlListener {
        void onRemoteControl(boolean z);
    }

    public void setListener(RemoteControlListener remoteControlListener) {
        this.mListener = remoteControlListener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        KeyEvent keyEvent;
        RemoteControlListener remoteControlListener;
        DefaultLogger.d("RemoteControlReceiver", "onReceive broadcast");
        if (!"miui.intent.action.REMOTE_CONTROL".equals(intent.getAction()) || (keyEvent = (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT")) == null) {
            return;
        }
        if (25 == keyEvent.getKeyCode()) {
            RemoteControlListener remoteControlListener2 = this.mListener;
            if (remoteControlListener2 == null) {
                return;
            }
            remoteControlListener2.onRemoteControl(false);
        } else if (24 != keyEvent.getKeyCode() || (remoteControlListener = this.mListener) == null) {
        } else {
            remoteControlListener.onRemoteControl(true);
        }
    }
}
