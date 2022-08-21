package com.market;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.market.sdk.ThreadExecutors;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class ServiceProxy {
    public final Context mContext;
    public final Intent mIntent;
    public Executor mServiceExecutor;
    public long mStartTime;
    public ProxyTask mTask;
    public String mName = " unnamed";
    public final ServiceConnection mConnection = new ProxyConnection(this, null);
    public int mTimeout = 45;
    public boolean mTaskSet = false;
    public boolean mTaskCompleted = false;
    public final String mTag = getClass().getSimpleName();

    /* renamed from: com.market.ServiceProxy$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements ProxyTask {
    }

    /* loaded from: classes.dex */
    public interface ProxyTask {
        void run() throws RemoteException;
    }

    public abstract void onConnected(IBinder iBinder);

    public abstract void onDisconnected();

    public ServiceProxy(Context context, Intent intent) {
        this.mContext = context;
        this.mIntent = intent;
        if (Debug.isDebuggerConnected()) {
            this.mTimeout <<= 2;
        }
        if (this.mServiceExecutor == null) {
            this.mServiceExecutor = ThreadExecutors.newCachedThreadPool(5, 100, 5, "ServiceProxy");
        }
    }

    /* loaded from: classes.dex */
    public class ProxyConnection implements ServiceConnection {
        public ProxyConnection() {
        }

        public /* synthetic */ ProxyConnection(ServiceProxy serviceProxy, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ServiceProxy.this.onConnected(iBinder);
            new AsyncTask<Void, Void, Void>() { // from class: com.market.ServiceProxy.ProxyConnection.1
                @Override // android.os.AsyncTask
                public Void doInBackground(Void... voidArr) {
                    try {
                        ServiceProxy.this.mTask.run();
                    } catch (RemoteException unused) {
                    }
                    try {
                        ServiceProxy.this.mContext.unbindService(ServiceProxy.this.mConnection);
                    } catch (RuntimeException e) {
                        Log.e(ServiceProxy.this.mTag, "RuntimeException when trying to unbind from service", e);
                    }
                    ServiceProxy.this.mTaskCompleted = true;
                    synchronized (ServiceProxy.this.mConnection) {
                        ServiceProxy.this.mConnection.notify();
                    }
                    return null;
                }
            }.executeOnExecutor(ServiceProxy.this.mServiceExecutor, new Void[0]);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            ServiceProxy.this.onDisconnected();
        }
    }

    public boolean setTask(ProxyTask proxyTask, String str) throws IllegalStateException {
        if (this.mTaskSet) {
            throw new IllegalStateException("Cannot call setTask twice on the same ServiceProxy.");
        }
        this.mTaskSet = true;
        this.mName = str;
        this.mTask = proxyTask;
        this.mStartTime = System.currentTimeMillis();
        System.currentTimeMillis();
        return this.mContext.bindService(this.mIntent, this.mConnection, 1);
    }

    public void waitForCompletion() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("This cannot be called on the main thread.");
        }
        synchronized (this.mConnection) {
            System.currentTimeMillis();
            try {
                this.mConnection.wait(this.mTimeout * 1000);
            } catch (InterruptedException unused) {
            }
        }
    }
}
