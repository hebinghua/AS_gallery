package com.miui.gallery.editor.photo.screen.shell;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class ShellResThread extends HandlerThread implements Handler.Callback {
    public Handler mHandler;
    public Handler mMainHandler;
    public ShellResListener mShellResListener;

    /* loaded from: classes2.dex */
    public static class ShellResData {
        public Matrix bitmapMatrix;
        public Bitmap current;
        public ScreenShellEntry shellEntry;
    }

    /* loaded from: classes2.dex */
    public interface ShellResListener {
        void onLoadEnd(boolean z);
    }

    public static /* synthetic */ void $r8$lambda$d90Cg6xGbSBOZx1gJGdMjGihIFU(ShellResThread shellResThread) {
        shellResThread.lambda$handleMessage$0();
    }

    public ShellResThread() {
        super("shell_res_thread");
        this.mMainHandler = ThreadManager.getMainHandler();
        start();
        this.mHandler = new Handler(getLooper(), this);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            ShellResData shellResData = (ShellResData) message.obj;
            if (shellResData == null || shellResData.current == null) {
                return false;
            }
            shellResData.shellEntry.generateBitmap();
            if (this.mShellResListener == null) {
                return true;
            }
            this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.editor.photo.screen.shell.ShellResThread$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ShellResThread.$r8$lambda$d90Cg6xGbSBOZx1gJGdMjGihIFU(ShellResThread.this);
                }
            });
            return true;
        }
        return true;
    }

    public /* synthetic */ void lambda$handleMessage$0() {
        ShellResListener shellResListener = this.mShellResListener;
        if (shellResListener != null) {
            shellResListener.onLoadEnd(true);
        }
    }

    public void sendGenerateShellMsg(ScreenShellEntry screenShellEntry, Bitmap bitmap, Matrix matrix) {
        this.mHandler.removeMessages(0);
        ShellResData shellResData = new ShellResData();
        shellResData.shellEntry = screenShellEntry;
        shellResData.current = bitmap;
        shellResData.bitmapMatrix = matrix;
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = shellResData;
        this.mHandler.sendMessage(obtain);
    }

    public void setShellResListener(ShellResListener shellResListener) {
        this.mShellResListener = shellResListener;
    }
}
