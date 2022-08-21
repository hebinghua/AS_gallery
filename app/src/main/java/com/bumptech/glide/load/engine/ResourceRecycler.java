package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class ResourceRecycler {
    public final Handler handler = new Handler(Looper.getMainLooper(), new ResourceRecyclerCallback());
    public boolean isRecycling;

    public synchronized void recycle(Resource<?> resource, boolean z) {
        if (!this.isRecycling && !z) {
            this.isRecycling = true;
            resource.recycle();
            this.isRecycling = false;
        }
        this.handler.obtainMessage(1, resource).sendToTarget();
    }

    /* loaded from: classes.dex */
    public static final class ResourceRecyclerCallback implements Handler.Callback {
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                ((Resource) message.obj).recycle();
                return true;
            }
            return false;
        }
    }
}
