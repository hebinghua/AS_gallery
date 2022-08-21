package com.milink.api.v1;

import android.os.Handler;
import android.os.RemoteException;
import com.milink.api.v1.aidl.IMcsDeviceListener;
import com.milink.api.v1.type.DeviceType;

/* loaded from: classes.dex */
public class McsDeviceListener extends IMcsDeviceListener.Stub {
    public MiLinkClientDeviceListener mDeviceListener;
    public Handler mHandler = new Handler();
    public MilinkClientManagerDelegate mDelegate = null;

    public void setDelegate(MilinkClientManagerDelegate milinkClientManagerDelegate) {
        this.mDelegate = milinkClientManagerDelegate;
    }

    @Override // com.milink.api.v1.aidl.IMcsDeviceListener
    public void onDeviceFound(final String str, final String str2, final String str3) throws RemoteException {
        if (this.mDelegate == null) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.milink.api.v1.McsDeviceListener.1
            @Override // java.lang.Runnable
            public void run() {
                if (McsDeviceListener.this.mDelegate != null) {
                    if (!"airkan".equals(str3) && !"dlna.tv".equals(str3) && !"dlna.speaker".equals(str3)) {
                        return;
                    }
                    McsDeviceListener.this.mDelegate.onDeviceFound(str, str2, DeviceType.create(str3));
                }
            }
        });
    }

    @Override // com.milink.api.v1.aidl.IMcsDeviceListener
    public void onDeviceFound2(final String str, final String str2, final String str3, final String str4, final String str5, final String str6) throws RemoteException {
        if (this.mDelegate == null) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.milink.api.v1.McsDeviceListener.2
            @Override // java.lang.Runnable
            public void run() {
                if (McsDeviceListener.this.mDelegate != null && ("airkan".equals(str3) || "dlna.tv".equals(str3) || "dlna.speaker".equals(str3))) {
                    McsDeviceListener.this.mDelegate.onDeviceFound(str, str2, DeviceType.create(str3));
                }
                if (McsDeviceListener.this.mDeviceListener != null) {
                    if (str5 == null && str4 == null) {
                        return;
                    }
                    MiLinkClientDevice miLinkClientDevice = new MiLinkClientDevice();
                    miLinkClientDevice.setDeviceId(str);
                    miLinkClientDevice.setDeviceName(str2);
                    miLinkClientDevice.setDeviceType(str3);
                    miLinkClientDevice.setP2pMac(str4);
                    miLinkClientDevice.setWifiMac(str5);
                    miLinkClientDevice.setLastConnectTime(str6);
                    McsDeviceListener.this.mDeviceListener.onDeviceFound(miLinkClientDevice);
                }
            }
        });
    }

    @Override // com.milink.api.v1.aidl.IMcsDeviceListener
    public void onDeviceLost(final String str) throws RemoteException {
        if (this.mDelegate == null) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.milink.api.v1.McsDeviceListener.3
            @Override // java.lang.Runnable
            public void run() {
                if (McsDeviceListener.this.mDelegate != null) {
                    McsDeviceListener.this.mDelegate.onDeviceLost(str);
                }
                if (McsDeviceListener.this.mDeviceListener != null) {
                    MiLinkClientDevice miLinkClientDevice = new MiLinkClientDevice();
                    miLinkClientDevice.setDeviceId(str);
                    McsDeviceListener.this.mDeviceListener.onDeviceLost(miLinkClientDevice);
                }
            }
        });
    }
}
