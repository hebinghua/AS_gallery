package com.xiaomi.miplay.phoneclientsdk.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClient;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes3.dex */
public class MiplayClientControl {
    public static final String TAG = "MiplayClientControl";
    public MiplayClientCallback mCallback;
    public IMiPlayExternalClient mClient;
    public Context mContext;
    public String mID;
    public SmHandler mSmHandler;
    public HandlerThread mSmThread;
    public MiplayClientCallback mInnerCallback = new MiplayClientCallback() { // from class: com.xiaomi.miplay.phoneclientsdk.external.MiplayClientControl.1
        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onInitSuccess() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(0).sendToTarget();
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onInitError() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(1).sendToTarget();
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onPositionChanged(long j) throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(2, Long.valueOf(j)).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onPlayed() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(3).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onStopped(int i) throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(4, Integer.valueOf(i)).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onNotifyPropertiesInfo(String str) throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(5, str).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onNext() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(6).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onPrev() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(7).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onCirculateStart() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(8).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onCirculateEnd() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(9).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onPaused() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(10).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onResumed() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(11).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onSeekedTo(long j) throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(12, Long.valueOf(j)).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onCirculateFail(String str) throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(13, str).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public int onSeekDoneNotify() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(14).sendToTarget();
            return 0;
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onVolumeChange(double d) throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(15, Double.valueOf(d)).sendToTarget();
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onCirculateModeChange(int i) throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(16, Integer.valueOf(i)).sendToTarget();
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onConnectMirrorSuccess() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(17).sendToTarget();
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onBuffering() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(18).sendToTarget();
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onResumeMirrorSuccess() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(19).sendToTarget();
        }

        @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
        public void onResumeMirrorFail() throws RemoteException {
            MiplayClientControl.this.mSmHandler.obtainMessage(20).sendToTarget();
        }
    };
    public ServiceConnection mConn = new ServiceConnection() { // from class: com.xiaomi.miplay.phoneclientsdk.external.MiplayClientControl.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MiplayClientControl.this.mClient = IMiPlayExternalClient.Stub.asInterface(iBinder);
            Log.i(MiplayClientControl.TAG, "onServiceConnected: ");
            if (MiplayClientControl.this.mClient != null) {
                try {
                    MiplayClientControl.this.mClient.initAsync(MiplayClientControl.this.mID, MiplayClientControl.this.mInnerCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(MiplayClientControl.TAG, "onServiceDisconnected: ");
        }
    };

    public MiplayClientControl(Context context) {
        this.mContext = context;
        this.mID = context.getPackageName();
        HandlerThread handlerThread = new HandlerThread(getClass().getName());
        this.mSmThread = handlerThread;
        handlerThread.start();
        this.mSmHandler = new SmHandler(this.mSmThread.getLooper());
    }

    /* loaded from: classes3.dex */
    public class SmHandler extends Handler {
        public SmHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onInitSuccess();
                        return;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return;
                    }
                case 1:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onInitError();
                        return;
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                        return;
                    }
                case 2:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onPositionChanged(((Long) message.obj).longValue());
                        return;
                    } catch (RemoteException e3) {
                        e3.printStackTrace();
                        return;
                    }
                case 3:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onPlayed();
                        return;
                    } catch (RemoteException e4) {
                        e4.printStackTrace();
                        return;
                    }
                case 4:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onStopped(((Integer) message.obj).intValue());
                        return;
                    } catch (RemoteException e5) {
                        e5.printStackTrace();
                        return;
                    }
                case 5:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onNotifyPropertiesInfo((String) message.obj);
                        return;
                    } catch (RemoteException e6) {
                        e6.printStackTrace();
                        return;
                    }
                case 6:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onNext();
                        return;
                    } catch (RemoteException e7) {
                        e7.printStackTrace();
                        return;
                    }
                case 7:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onPrev();
                        return;
                    } catch (RemoteException e8) {
                        e8.printStackTrace();
                        return;
                    }
                case 8:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onCirculateStart();
                        return;
                    } catch (RemoteException e9) {
                        e9.printStackTrace();
                        return;
                    }
                case 9:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onCirculateEnd();
                        return;
                    } catch (RemoteException e10) {
                        e10.printStackTrace();
                        return;
                    }
                case 10:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onPaused();
                        return;
                    } catch (RemoteException e11) {
                        e11.printStackTrace();
                        return;
                    }
                case 11:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onResumed();
                        return;
                    } catch (RemoteException e12) {
                        e12.printStackTrace();
                        return;
                    }
                case 12:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onSeekedTo(((Long) message.obj).longValue());
                        return;
                    } catch (RemoteException e13) {
                        e13.printStackTrace();
                        return;
                    }
                case 13:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onCirculateFail((String) message.obj);
                        return;
                    } catch (RemoteException e14) {
                        e14.printStackTrace();
                        return;
                    }
                case 14:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onSeekDoneNotify();
                        return;
                    } catch (RemoteException e15) {
                        e15.printStackTrace();
                        return;
                    }
                case 15:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onVolumeChange(((Double) message.obj).doubleValue());
                        return;
                    } catch (RemoteException e16) {
                        e16.printStackTrace();
                        return;
                    }
                case 16:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onCirculateModeChange(((Integer) message.obj).intValue());
                        return;
                    } catch (RemoteException e17) {
                        e17.printStackTrace();
                        return;
                    }
                case 17:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onConnectMirrorSuccess();
                        return;
                    } catch (RemoteException e18) {
                        e18.printStackTrace();
                        return;
                    }
                case 18:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onBuffering();
                        return;
                    } catch (RemoteException e19) {
                        e19.printStackTrace();
                        return;
                    }
                case 19:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onResumeMirrorSuccess();
                        return;
                    } catch (RemoteException e20) {
                        e20.printStackTrace();
                        return;
                    }
                case 20:
                    if (MiplayClientControl.this.mCallback == null) {
                        return;
                    }
                    try {
                        MiplayClientControl.this.mCallback.onResumeMirrorFail();
                        return;
                    } catch (RemoteException e21) {
                        e21.printStackTrace();
                        return;
                    }
                default:
                    Log.i(MiplayClientControl.TAG, "unknown msg!");
                    return;
            }
        }
    }

    public boolean initAsync(MiplayClientCallback miplayClientCallback) {
        this.mCallback = miplayClientCallback;
        Log.i(TAG, "initAsync: ");
        Intent intent = new Intent();
        intent.setPackage("com.milink.service");
        intent.setAction("COM.XIAOMI.MIPLAY.ACTION.EXTERNAL_CIRCULATION_CLIENT_SERVICE");
        return this.mContext.bindService(intent, this.mConn, Build.VERSION.SDK_INT >= 29 ? 4097 : 1);
    }

    public void unInit() {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                iMiPlayExternalClient.unInit(this.mID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            this.mContext.unbindService(this.mConn);
        }
    }

    public int play(String str, MediaMetaData mediaMetaData) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.play(str, mediaMetaData);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int stop(String str) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.stop(str);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int pause(String str) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.pause(str);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int resume(String str) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.resume(str);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int seek(String str, long j) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.seek(str, j);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int setVolume(String str, double d) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.setVolume(str, d);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int getVolume(String str) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.getVolume(str);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int getCirculateMode() {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.getCirculateMode();
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public int cancelCirculate(String str, int i) {
        IMiPlayExternalClient iMiPlayExternalClient = this.mClient;
        if (iMiPlayExternalClient != null) {
            try {
                return iMiPlayExternalClient.cancelCirculate(str, i);
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }
}
