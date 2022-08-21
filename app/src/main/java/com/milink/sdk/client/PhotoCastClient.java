package com.milink.sdk.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.milink.sdk.photo.IPhotoCastCallback;
import com.milink.sdk.photo.IPhotoCastDataSource;
import com.milink.sdk.photo.IPhotoCastService;
import com.milink.sdk.util.ApiUtils;

/* loaded from: classes.dex */
public class PhotoCastClient {
    public IPhotoCastDataSource mCastDataSource;
    public IPhotoCastCallback mCastListener;
    public Context mContext;
    public IPhotoCastService mService;
    public boolean mBound = false;
    public ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.milink.sdk.client.PhotoCastClient.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("ML::PhotoCastClient", "onServiceConnected");
            PhotoCastClient.this.mService = IPhotoCastService.Stub.asInterface(iBinder);
            try {
                if (PhotoCastClient.this.mCastDataSource != null) {
                    PhotoCastClient.this.mService.setDataSource(PhotoCastClient.this.mCastDataSource);
                }
                if (PhotoCastClient.this.mCastListener == null) {
                    return;
                }
                PhotoCastClient.this.mService.init(PhotoCastClient.this.mCastListener);
                PhotoCastClient.this.mCastListener.onInit();
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "init error: ", e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("ML::PhotoCastClient", "onServiceDisconnected");
            PhotoCastClient.this.mService = null;
        }
    };

    public PhotoCastClient(Context context) {
        this.mContext = context;
    }

    public int init(IPhotoCastCallback iPhotoCastCallback) {
        if (ApiUtils.getVersionCode(this.mContext, "com.milink.service") < 12040700) {
            if (ApiUtils.getVersionCode(this.mContext, "com.milink.service") < 12020203) {
                Log.i("ML::PhotoCastClient", "MiLink not support photo cast");
                return -2;
            } else if (ApiUtils.getVersionCode(this.mContext, "com.xiaomi.miplay_client") < 101) {
                Log.i("ML::PhotoCastClient", "MiPlay not support photo cast");
                return -3;
            }
        }
        this.mCastListener = iPhotoCastCallback;
        Intent intent = new Intent(IPhotoCastService.class.getName());
        intent.setPackage("com.milink.service");
        this.mBound = this.mContext.bindService(intent, this.mServiceConnection, 1);
        return 1;
    }

    public void release() {
        if (this.mBound) {
            IPhotoCastService iPhotoCastService = this.mService;
            if (iPhotoCastService != null) {
                try {
                    iPhotoCastService.release(this.mCastListener);
                } catch (RemoteException e) {
                    Log.e("ML::PhotoCastClient", "release error: ", e);
                }
            }
            this.mContext.unbindService(this.mServiceConnection);
            this.mBound = false;
            this.mService = null;
            this.mCastListener = null;
        }
    }

    public int checkAccess() {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService == null) {
            return -1;
        }
        try {
            return iPhotoCastService.checkAccess();
        } catch (RemoteException e) {
            Log.e("ML::PhotoCastClient", "checkAccess error: ", e);
            return -1;
        }
    }

    public String getCastDeviceName() {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService == null) {
            return "";
        }
        try {
            return iPhotoCastService.getCastingDeviceName();
        } catch (RemoteException e) {
            Log.e("ML::PhotoCastClient", "getCastDeviceName error: ", e);
            return "";
        }
    }

    public void setDataSource(IPhotoCastDataSource iPhotoCastDataSource) {
        this.mCastDataSource = iPhotoCastDataSource;
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService != null) {
            try {
                iPhotoCastService.setDataSource(iPhotoCastDataSource);
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "setDataSource error: ", e);
            }
        }
    }

    public int show(String str) {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService != null) {
            try {
                return iPhotoCastService.show(str);
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "show error: ", e);
            }
        }
        return -1;
    }

    public int zoom(String str, int i, int i2, int i3, int i4, int i5, int i6, float f) {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService != null) {
            try {
                return iPhotoCastService.zoom(str, i, i2, i3, i4, i5, i6, f);
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "zoom error: ", e);
            }
        }
        return -1;
    }

    public int rotate(String str, float f) {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService != null) {
            try {
                return iPhotoCastService.rotate(str, f);
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "rotate error: ", e);
            }
        }
        return -1;
    }

    public int stop() {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService != null) {
            try {
                return iPhotoCastService.stop();
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "stop error: ", e);
            }
        }
        return -1;
    }

    public int startSlide() {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService != null) {
            try {
                return iPhotoCastService.startSlide();
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "startSlide error: ", e);
            }
        }
        return -1;
    }

    public int stopSlide() {
        IPhotoCastService iPhotoCastService = this.mService;
        if (iPhotoCastService != null) {
            try {
                return iPhotoCastService.stopSlide();
            } catch (RemoteException e) {
                Log.e("ML::PhotoCastClient", "stopSlide error: ", e);
            }
        }
        return -1;
    }
}
