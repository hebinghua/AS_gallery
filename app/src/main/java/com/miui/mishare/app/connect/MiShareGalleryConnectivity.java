package com.miui.mishare.app.connect;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.miui.mishare.IMiShareDiscoverCallback;
import com.miui.mishare.IMiShareService;
import com.miui.mishare.IMiShareStateListener;
import com.miui.mishare.IMiShareTaskStateListener;
import com.miui.mishare.MiShareTask;
import com.miui.mishare.app.model2.MiShareDevice;
import com.miui.mishare.app.view.MiShareGalleryTransferView;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes3.dex */
public class MiShareGalleryConnectivity {
    public ServiceBindCallBack mCallback;
    public final ServiceConnection mConnection = new ServiceConnection() { // from class: com.miui.mishare.app.connect.MiShareGalleryConnectivity.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MiShareGalleryConnectivity.this.mService = IMiShareService.Stub.asInterface(iBinder);
            if (MiShareGalleryConnectivity.this.mCallback != null) {
                MiShareGalleryConnectivity.this.mCallback.onServiceBound();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            MiShareGalleryConnectivity.this.mService = null;
        }
    };
    public Context mContext;
    public boolean mIsBound;
    public IMiShareService mService;

    /* loaded from: classes3.dex */
    public interface ServiceBindCallBack {
        void onServiceBound();
    }

    public static boolean isAvailable(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.miui.mishare.connectivity", "com.miui.mishare.connectivity.MiShareService");
        PackageManager packageManager = context.getPackageManager();
        return packageManager != null && packageManager.queryIntentServices(intent, 0).size() > 0;
    }

    public MiShareGalleryConnectivity(Context context) {
        this.mContext = context;
    }

    public boolean bind(ServiceBindCallBack serviceBindCallBack) {
        this.mCallback = serviceBindCallBack;
        Intent intent = new Intent();
        intent.setClassName("com.miui.mishare.connectivity", "com.miui.mishare.connectivity.MiShareService");
        boolean bindService = this.mContext.bindService(intent, this.mConnection, 1);
        this.mIsBound = bindService;
        return bindService;
    }

    public void unbind() {
        if (this.mIsBound) {
            this.mCallback = null;
            if (this.mService != null) {
                this.mContext.unbindService(this.mConnection);
            }
            this.mIsBound = false;
        }
    }

    public void registerStateListener(IMiShareStateListener iMiShareStateListener) {
        Objects.requireNonNull(iMiShareStateListener, "null listener");
        ensureServiceBound();
        try {
            this.mService.registerStateListener(iMiShareStateListener);
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "registerStateListener: ", e);
        }
    }

    public void unregisterStateListener(IMiShareStateListener iMiShareStateListener) {
        Objects.requireNonNull(iMiShareStateListener, "null listener");
        ensureServiceBound();
        try {
            this.mService.unregisterStateListener(iMiShareStateListener);
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "unregisterStateListener: ", e);
        }
    }

    public int getServiceState() {
        IMiShareService iMiShareService = this.mService;
        if (iMiShareService == null) {
            return 0;
        }
        try {
            return iMiShareService.getState();
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "getServiceState: ", e);
            return 0;
        }
    }

    public void startDiscover(IMiShareDiscoverCallback iMiShareDiscoverCallback) {
        ensureServiceBound();
        try {
            this.mService.discover(iMiShareDiscoverCallback);
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "startDiscover: ", e);
        }
    }

    public void startDiscoverWithIntent(IMiShareDiscoverCallback iMiShareDiscoverCallback, Intent intent) {
        ensureServiceBound();
        try {
            this.mService.discoverWithIntent(iMiShareDiscoverCallback, intent);
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "startDiscoverWithConfig: ", e);
        }
    }

    public void stopDiscover(IMiShareDiscoverCallback iMiShareDiscoverCallback) {
        ensureServiceBound();
        try {
            this.mService.stopDiscover(iMiShareDiscoverCallback);
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "startDiscover: ", e);
        }
    }

    public void registerTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) {
        ensureServiceBound();
        try {
            this.mService.registerTaskStateListener(iMiShareTaskStateListener);
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "registerTaskStateListener: ", e);
        }
    }

    public void unregisterTaskStateListener(IMiShareTaskStateListener iMiShareTaskStateListener) {
        ensureServiceBound();
        try {
            this.mService.unregisterTaskStateListener(iMiShareTaskStateListener);
        } catch (RemoteException e) {
            Log.e("MiShareConnectivity", "unregisterTaskStateListener: ", e);
        }
    }

    public void sendData(MiShareTask miShareTask) {
        Uri uri;
        Objects.requireNonNull(miShareTask, "null task");
        try {
            ensureServiceBound();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < miShareTask.clipData.getItemCount(); i++) {
                if (miShareTask.clipData.getItemAt(i) != null && (uri = miShareTask.clipData.getItemAt(i).getUri()) != null && uri.getScheme().startsWith(MiStat.Param.CONTENT)) {
                    arrayList.add(uri);
                }
            }
            Intent intent = new Intent("com.miui.mishare.action.SEND_TASK");
            intent.setClassName("com.miui.mishare.connectivity", "com.miui.mishare.connectivity.MiShareService");
            if (arrayList.size() > 0) {
                intent.setClipData(getClipData(arrayList));
                intent.addFlags(1);
            }
            intent.putExtra("task", miShareTask);
            this.mContext.startService(intent);
        } catch (Exception e) {
            Log.e("MiShareConnectivity", "sendData: ", e);
        }
    }

    public final ClipData getClipData(List<Uri> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        ClipData clipData = new ClipData(new ClipDescription("mishare data", new String[]{""}), new ClipData.Item(list.get(0)));
        int size = list.size();
        for (int i = 1; i < size; i++) {
            Uri uri = list.get(i);
            if (uri != null) {
                clipData.addItem(new ClipData.Item(uri));
            }
        }
        return clipData;
    }

    public void cancel(MiShareDevice miShareDevice) {
        MiShareTask miShareTask = new MiShareTask();
        miShareTask.device = miShareDevice.remoteDevice;
        miShareTask.taskId = miShareDevice.taskId;
        miShareTask.clipData = MiShareGalleryTransferView.getClipData(miShareDevice.files);
        Intent intent = new Intent("com.miui.mishare.action.NOTIFICATION_CANCEL_CLICK");
        Bundle bundle = new Bundle();
        bundle.putParcelable("task", miShareTask);
        bundle.putString("device_name", miShareDevice.deviceName);
        intent.putExtras(bundle);
        intent.setClassName("com.miui.mishare.connectivity", "com.miui.mishare.connectivity.MiShareService");
        this.mContext.startService(intent);
    }

    public boolean checkServiceBound() {
        return this.mService != null;
    }

    public final void ensureServiceBound() {
        Objects.requireNonNull(this.mService, "service not bound");
    }
}
