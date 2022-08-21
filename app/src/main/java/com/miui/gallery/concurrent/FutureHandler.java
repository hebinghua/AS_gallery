package com.miui.gallery.concurrent;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public abstract class FutureHandler<T> implements FutureListener<T> {
    public Handler mHandler = new Handler(Looper.getMainLooper());

    public abstract void onPostExecute(Future<T> future);

    @Override // com.miui.gallery.concurrent.FutureListener
    public final void onFutureDone(final Future<T> future) {
        this.mHandler.post(new Runnable() { // from class: com.miui.gallery.concurrent.FutureHandler.1
            @Override // java.lang.Runnable
            public void run() {
                FutureHandler.this.onPostExecute(future);
            }
        });
    }
}
