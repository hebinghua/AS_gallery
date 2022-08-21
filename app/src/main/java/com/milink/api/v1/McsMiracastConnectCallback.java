package com.milink.api.v1;

import android.os.RemoteException;
import com.milink.api.v1.aidl.IMcsMiracastConnectCallback;

/* loaded from: classes.dex */
public class McsMiracastConnectCallback extends IMcsMiracastConnectCallback.Stub {
    public MiLinkClientMiracastConnectCallback mCallback;

    @Override // com.milink.api.v1.aidl.IMcsMiracastConnectCallback
    public void onConnectSuccess(String str) throws RemoteException {
        MiLinkClientMiracastConnectCallback miLinkClientMiracastConnectCallback = this.mCallback;
        if (miLinkClientMiracastConnectCallback != null) {
            miLinkClientMiracastConnectCallback.onConnectSuccess(str);
        }
    }

    @Override // com.milink.api.v1.aidl.IMcsMiracastConnectCallback
    public void onConnectFail(String str) throws RemoteException {
        MiLinkClientMiracastConnectCallback miLinkClientMiracastConnectCallback = this.mCallback;
        if (miLinkClientMiracastConnectCallback != null) {
            miLinkClientMiracastConnectCallback.onConnectFail(str);
        }
    }

    @Override // com.milink.api.v1.aidl.IMcsMiracastConnectCallback
    public void onConnecting(String str) throws RemoteException {
        MiLinkClientMiracastConnectCallback miLinkClientMiracastConnectCallback = this.mCallback;
        if (miLinkClientMiracastConnectCallback != null) {
            miLinkClientMiracastConnectCallback.onConnecting(str);
        }
    }

    @Override // com.milink.api.v1.aidl.IMcsMiracastConnectCallback
    public void onResult(int i, String str, String str2) throws RemoteException {
        MiLinkClientMiracastConnectCallback miLinkClientMiracastConnectCallback = this.mCallback;
        if (miLinkClientMiracastConnectCallback != null) {
            miLinkClientMiracastConnectCallback.onResult(i, str, str2);
        }
    }
}
