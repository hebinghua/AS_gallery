package com.miui.gallery.projection;

import android.content.Context;
import android.provider.Settings;
import com.miui.gallery.projection.RemoteControlReceiver;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ReceiverUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class RemoteController {
    public static DisableRemoteControlRunnable sDisableRemoteControlRunnable = new DisableRemoteControlRunnable();
    public final RemoteControlReceiver.RemoteControlListener mListener;
    public RemoteControlReceiver mRemoteControlReceiver = new RemoteControlReceiver();
    public AtomicBoolean mRemoteControlRegisterStatus = new AtomicBoolean(false);

    /* loaded from: classes2.dex */
    public static class DisableRemoteControlRunnable implements Runnable {
        public WeakReference<Context> mContextRef;
        public WeakReference<RemoteController> mParentRef;

        public DisableRemoteControlRunnable() {
        }

        public void setParent(Context context, RemoteController remoteController) {
            this.mParentRef = new WeakReference<>(remoteController);
            this.mContextRef = new WeakReference<>(context);
        }

        @Override // java.lang.Runnable
        public void run() {
            WeakReference<RemoteController> weakReference = this.mParentRef;
            if (weakReference != null) {
                RemoteController remoteController = weakReference.get();
                Context context = this.mContextRef.get();
                if (remoteController != null && context != null) {
                    remoteController.disableRemoteControlWhenOnPause(context);
                } else {
                    DefaultLogger.e("RemoteController", "parent is null");
                }
            }
        }
    }

    public RemoteController(RemoteControlReceiver.RemoteControlListener remoteControlListener) {
        this.mListener = remoteControlListener;
    }

    public void enable(Context context) {
        ThreadManager.getMainHandler().removeCallbacks(sDisableRemoteControlRunnable);
        enableRemoteControl(context);
    }

    public void disableDelay(Context context) {
        sDisableRemoteControlRunnable.setParent(context, this);
        ThreadManager.getMainHandler().postDelayed(sDisableRemoteControlRunnable, 2000L);
    }

    public void disable(Context context) {
        ThreadManager.getMainHandler().removeCallbacks(sDisableRemoteControlRunnable);
        disableRemoteControl(context);
    }

    public final void enableRemoteControl(Context context) {
        if (this.mRemoteControlRegisterStatus.compareAndSet(false, true)) {
            ReceiverUtils.registerReceiver(context, this.mRemoteControlReceiver, "miui.intent.action.REMOTE_CONTROL");
            Settings.System.putString(context.getContentResolver(), "remote_control_pkg_name", context.getPackageName());
            Settings.System.putString(context.getContentResolver(), "remote_control_proc_name", context.getPackageName());
            this.mRemoteControlReceiver.setListener(this.mListener);
        }
    }

    public final void disableRemoteControl(Context context) {
        if (this.mRemoteControlRegisterStatus.compareAndSet(true, false)) {
            this.mRemoteControlReceiver.setListener(null);
            ReceiverUtils.safeUnregisterReceiver(context, this.mRemoteControlReceiver);
            Settings.System.putString(context.getContentResolver(), "remote_control_pkg_name", null);
            Settings.System.putString(context.getContentResolver(), "remote_control_proc_name", null);
        }
    }

    public final void disableRemoteControlWhenOnPause(Context context) {
        if (!MiscUtil.isKeyGuardLocked(context)) {
            disableRemoteControl(context);
        }
    }
}
