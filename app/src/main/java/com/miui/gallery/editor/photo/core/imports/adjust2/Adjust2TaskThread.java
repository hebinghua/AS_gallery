package com.miui.gallery.editor.photo.core.imports.adjust2;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/* loaded from: classes2.dex */
public class Adjust2TaskThread extends HandlerThread implements Handler.Callback {
    public AdjustTaskListener mAdjustTaskListener;
    public Handler mWorkHandler;

    /* loaded from: classes2.dex */
    public static class AdjustTaskData {
        public Bitmap currentBitmap;
        public Adjust2RenderData renderData;
    }

    /* loaded from: classes2.dex */
    public interface AdjustTaskListener {
        void handleMessage(Message message);
    }

    public Adjust2TaskThread() {
        super("adjust_task_thread");
        start();
        this.mWorkHandler = new Handler(getLooper(), this);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        AdjustTaskListener adjustTaskListener;
        if (message.what != 0 || (adjustTaskListener = this.mAdjustTaskListener) == null) {
            return true;
        }
        adjustTaskListener.handleMessage(message);
        return true;
    }

    public void sendFilterTaskMsg(Bitmap bitmap, Adjust2RenderData adjust2RenderData) {
        this.mWorkHandler.removeMessages(0);
        AdjustTaskData adjustTaskData = new AdjustTaskData();
        adjustTaskData.currentBitmap = bitmap;
        adjustTaskData.renderData = adjust2RenderData;
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = adjustTaskData;
        this.mWorkHandler.sendMessage(obtain);
    }

    public void setAdjustTaskListener(AdjustTaskListener adjustTaskListener) {
        this.mAdjustTaskListener = adjustTaskListener;
    }
}
