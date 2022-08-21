package com.milink.api.v1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.milink.api.v1.aidl.IMcs;
import com.milink.api.v1.type.MediaType;
import com.milink.api.v1.type.ReturnCode;
import com.milink.api.v1.type.SlideMode;

/* loaded from: classes.dex */
public class MilinkClientManager {
    public static final String TAG = "MilinkClientManager";
    public Context mContext;
    public McsDataSource mMcsDataSource;
    public McsDelegate mMcsDelegate;
    public McsDeviceListener mMcsDeviceListener;
    public MilinkClientManagerDelegate mDelegate = null;
    public IMcs mService = null;
    public boolean mIsbound = false;
    public String mDeviceName = null;
    public ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.milink.api.v1.MilinkClientManager.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(MilinkClientManager.TAG, "onServiceConnected");
            MilinkClientManager.this.mService = IMcs.Stub.asInterface(iBinder);
            try {
                MilinkClientManager.this.mService.setDeviceName(MilinkClientManager.this.mDeviceName);
                MilinkClientManager.this.mService.setDelegate(MilinkClientManager.this.mMcsDelegate);
                MilinkClientManager.this.mService.setDataSource(MilinkClientManager.this.mMcsDataSource);
                MilinkClientManager.this.mService.setDeviceListener(MilinkClientManager.this.mMcsDeviceListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            new Handler().post(new Runnable() { // from class: com.milink.api.v1.MilinkClientManager.1.1
                @Override // java.lang.Runnable
                public void run() {
                    if (MilinkClientManager.this.mDelegate != null) {
                        MilinkClientManager.this.mDelegate.onOpen();
                    }
                }
            });
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(MilinkClientManager.TAG, "onServiceDisconnected");
            new Handler().post(new Runnable() { // from class: com.milink.api.v1.MilinkClientManager.1.2
                @Override // java.lang.Runnable
                public void run() {
                    if (MilinkClientManager.this.mDelegate != null) {
                        MilinkClientManager.this.mDelegate.onClose();
                    }
                }
            });
            try {
                MilinkClientManager.this.mService.unsetDeviceListener(MilinkClientManager.this.mMcsDeviceListener);
                MilinkClientManager.this.mService.unsetDataSource(MilinkClientManager.this.mMcsDataSource);
                MilinkClientManager.this.mService.unsetDelegate(MilinkClientManager.this.mMcsDelegate);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            MilinkClientManager.this.mService = null;
        }
    };
    public McsOpenMiracastListener mMcsOpenMiracastListener = new McsOpenMiracastListener();
    public McsMiracastConnectCallback mMcsMiracastConnectCallback = new McsMiracastConnectCallback();
    public McsScanListCallback mMcsScanListCallback = new McsScanListCallback();

    public MilinkClientManager(Context context) {
        this.mContext = null;
        this.mMcsDataSource = null;
        this.mMcsDelegate = null;
        this.mMcsDeviceListener = null;
        this.mContext = context;
        this.mMcsDelegate = new McsDelegate();
        this.mMcsDataSource = new McsDataSource();
        this.mMcsDeviceListener = new McsDeviceListener();
    }

    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public void setDeviceName(String str) {
        this.mDeviceName = str;
    }

    public void setDataSource(MilinkClientManagerDataSource milinkClientManagerDataSource) {
        this.mMcsDataSource.setDataSource(milinkClientManagerDataSource);
    }

    public void setDelegate(MilinkClientManagerDelegate milinkClientManagerDelegate) {
        Log.d(TAG, "setDelegate");
        this.mDelegate = milinkClientManagerDelegate;
        this.mMcsDelegate.setDelegate(milinkClientManagerDelegate);
        this.mMcsDeviceListener.setDelegate(milinkClientManagerDelegate);
    }

    public void open() {
        Log.d(TAG, "open");
        bindMilinkClientService();
    }

    public void close() {
        unbindMilinkClientService();
    }

    public ReturnCode showScanList(MiLinkClientScanListCallback miLinkClientScanListCallback, int i) {
        if (this.mService == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            this.mMcsScanListCallback.setCallback(miLinkClientScanListCallback);
            this.mService.showScanList(this.mMcsScanListCallback, i);
            return returnCode;
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public final void bindMilinkClientService() {
        Log.d(TAG, "bindMilinkClientService");
        if (!this.mIsbound) {
            Intent intent = new Intent(IMcs.class.getName());
            intent.setPackage("com.milink.service");
            this.mIsbound = this.mContext.bindService(intent, this.mServiceConnection, 1);
        }
    }

    public final void unbindMilinkClientService() {
        if (this.mIsbound) {
            this.mContext.unbindService(this.mServiceConnection);
            this.mIsbound = false;
        }
    }

    public ReturnCode connect(String str, int i) {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.connect(str, i));
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public ReturnCode disconnect() {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.disconnect());
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public ReturnCode startShow() {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.startShow());
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public ReturnCode show(String str) {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.show(str));
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return ReturnCode.InvalidParams;
        }
    }

    public ReturnCode zoomPhoto(String str, int i, int i2, int i3, int i4, int i5, int i6, float f) {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.zoomPhoto(str, i, i2, i3, i4, i5, i6, f));
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public ReturnCode stopShow() {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.stopShow());
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public ReturnCode startSlideshow(int i, SlideMode slideMode) {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.startSlideshow(i, slideMode == SlideMode.Recyle));
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public ReturnCode startPlay(String str, String str2, int i, double d, MediaType mediaType) {
        return startPlay(str, str2, null, i, d, mediaType);
    }

    public ReturnCode startPlay(String str, String str2, String str3, int i, double d, MediaType mediaType) {
        if (this.mService == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            int i2 = AnonymousClass2.$SwitchMap$com$milink$api$v1$type$MediaType[mediaType.ordinal()];
            if (i2 == 1) {
                if (str3 == null) {
                    return getReturnCode(this.mService.startPlayAudio(str, str2, i, d));
                }
                return getReturnCode(this.mService.startPlayAudioEx(str, str2, str3, i, d));
            } else if (i2 != 2) {
                if (i2 == 3) {
                    ReturnCode returnCode2 = ReturnCode.InvalidParams;
                }
                return ReturnCode.InvalidParams;
            } else if (str3 == null) {
                return getReturnCode(this.mService.startPlayVideo(str, str2, i, d));
            } else {
                return getReturnCode(this.mService.startPlayVideoEx(str, str2, str3, i, d));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    /* renamed from: com.milink.api.v1.MilinkClientManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$milink$api$v1$type$MediaType;

        static {
            int[] iArr = new int[MediaType.values().length];
            $SwitchMap$com$milink$api$v1$type$MediaType = iArr;
            try {
                iArr[MediaType.Audio.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$milink$api$v1$type$MediaType[MediaType.Video.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$milink$api$v1$type$MediaType[MediaType.Photo.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public ReturnCode stopPlay() {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.stopPlay());
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public ReturnCode setPlaybackRate(int i) {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.setPlaybackRate(i));
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public int getPlaybackRate() {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return 0;
        }
        try {
            return iMcs.getPlaybackRate();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public ReturnCode setPlaybackProgress(int i) {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return ReturnCode.NotConnected;
        }
        ReturnCode returnCode = ReturnCode.OK;
        try {
            return getReturnCode(iMcs.setPlaybackProgress(i));
        } catch (RemoteException e) {
            e.printStackTrace();
            return ReturnCode.ServiceException;
        }
    }

    public int getPlaybackProgress() {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return 0;
        }
        try {
            return iMcs.getPlaybackProgress();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getPlaybackDuration() {
        IMcs iMcs = this.mService;
        if (iMcs == null) {
            return 0;
        }
        try {
            return iMcs.getPlaybackDuration();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public final ReturnCode getReturnCode(int i) {
        ReturnCode returnCode = ReturnCode.OK;
        if (i != -5) {
            if (i == -4) {
                return ReturnCode.NotConnected;
            }
            if (i == -3) {
                return ReturnCode.InvalidUrl;
            }
            if (i == -2) {
                return ReturnCode.InvalidParams;
            }
            if (i == -1) {
                return ReturnCode.Error;
            }
            return i != 0 ? ReturnCode.Error : returnCode;
        }
        return ReturnCode.NotSupport;
    }
}
