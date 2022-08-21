package com.xiaomi.micloudsdk.remote;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.xiaomi.micloudsdk.utils.ThreadUtil;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/* loaded from: classes3.dex */
public abstract class RemoteMethodInvoker<R> implements ServiceConnection {
    public final AsyncFuture<IBinder> future = new AsyncFuture<>();
    public final Context mContext;

    public abstract boolean bindService(Context context, ServiceConnection serviceConnection);

    public abstract R invokeRemoteMethod(IBinder iBinder) throws RemoteException;

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
    }

    public RemoteMethodInvoker(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        this.mContext = context.getApplicationContext();
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.i("RemoteMethodInvoker", "RemoteMethodInvoker connects remote service " + componentName.getShortClassName());
        this.future.setResult(iBinder);
    }

    public R invoke() {
        ThreadUtil.ensureWorkerThread();
        try {
            if (bindService(this.mContext, this)) {
                try {
                    try {
                        R invokeRemoteMethod = invokeRemoteMethod(this.future.get());
                        this.mContext.unbindService(this);
                        return invokeRemoteMethod;
                    } catch (RemoteException e) {
                        Log.e("RemoteMethodInvoker", "error while invoking service methods", e);
                        this.mContext.unbindService(this);
                        return null;
                    }
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                    this.mContext.unbindService(this);
                    return null;
                } catch (ExecutionException unused2) {
                    this.mContext.unbindService(this);
                    return null;
                }
            }
            Log.e("RemoteMethodInvoker", "Cannot bind remote service.");
            return null;
        } catch (Throwable th) {
            this.mContext.unbindService(this);
            throw th;
        }
    }

    /* loaded from: classes3.dex */
    public static class AsyncFuture<V> extends FutureTask<V> {
        public AsyncFuture() {
            super(new Callable<V>() { // from class: com.xiaomi.micloudsdk.remote.RemoteMethodInvoker.AsyncFuture.1
                @Override // java.util.concurrent.Callable
                public V call() throws Exception {
                    throw new IllegalStateException("this should never be called");
                }
            });
        }

        public void setResult(V v) {
            set(v);
        }
    }
}
