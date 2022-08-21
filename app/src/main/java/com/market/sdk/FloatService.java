package com.market.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.market.ServiceProxy;
import com.market.sdk.compat.FutureTaskCompat;
import com.market.sdk.utils.Log;
import com.xiaomi.market.IAppDownloadManager;

/* loaded from: classes.dex */
public class FloatService extends ServiceProxy implements IAppDownloadManager {
    public IAppDownloadManager mAidl;

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return null;
    }

    @Override // com.market.ServiceProxy
    public void onDisconnected() {
    }

    public static IAppDownloadManager openService(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            str = MarketManager.MARKET_PACKAGE_NAME;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(str, "com.xiaomi.market.data.AppDownloadService"));
        return new FloatService(context, intent);
    }

    public FloatService(Context context, Intent intent) {
        super(context, intent);
    }

    @Override // com.market.ServiceProxy
    public void onConnected(IBinder iBinder) {
        this.mAidl = IAppDownloadManager.Stub.asInterface(iBinder);
    }

    @Override // com.xiaomi.market.IAppDownloadManager
    public boolean resume(final String str, final String str2) throws RemoteException {
        final FutureTaskCompat futureTaskCompat = new FutureTaskCompat();
        setTask(new ServiceProxy.ProxyTask() { // from class: com.market.sdk.FloatService.3
            @Override // com.market.ServiceProxy.ProxyTask
            public void run() throws RemoteException {
                if (FloatService.this.mAidl != null) {
                    futureTaskCompat.set(Boolean.valueOf(FloatService.this.mAidl.resume(str, str2)));
                } else {
                    Log.e("FloatService", "IAppDownloadManager is null");
                }
            }
        }, "resume");
        waitForCompletion();
        if (futureTaskCompat.isDone()) {
            return ((Boolean) futureTaskCompat.get()).booleanValue();
        }
        return false;
    }

    @Override // com.xiaomi.market.IAppDownloadManager
    public void downloadByUri(final Uri uri) throws RemoteException {
        setTask(new ServiceProxy.ProxyTask() { // from class: com.market.sdk.FloatService.4
            @Override // com.market.ServiceProxy.ProxyTask
            public void run() throws RemoteException {
                if (FloatService.this.mAidl != null) {
                    FloatService.this.mAidl.downloadByUri(uri);
                } else {
                    Log.e("FloatService", "IAppDownloadManager is null");
                }
            }
        }, "downloadByUri");
    }

    @Override // com.xiaomi.market.IAppDownloadManager
    public void resumeByUri(final Uri uri) throws RemoteException {
        setTask(new ServiceProxy.ProxyTask() { // from class: com.market.sdk.FloatService.6
            @Override // com.market.ServiceProxy.ProxyTask
            public void run() throws RemoteException {
                if (FloatService.this.mAidl != null) {
                    FloatService.this.mAidl.resumeByUri(uri);
                } else {
                    Log.e("FloatService", "IAppDownloadManager is null");
                }
            }
        }, "resumeByUri");
    }

    @Override // com.xiaomi.market.IAppDownloadManager
    public void lifecycleChanged(final String str, final int i) throws RemoteException {
        setTask(new ServiceProxy.ProxyTask() { // from class: com.market.sdk.FloatService.7
            @Override // com.market.ServiceProxy.ProxyTask
            public void run() throws RemoteException {
                if (FloatService.this.mAidl != null) {
                    FloatService.this.mAidl.lifecycleChanged(str, i);
                } else {
                    Log.e("FloatService", "IAppDownloadManager is null");
                }
            }
        }, "lifecycleChanged");
    }
}
