package com.milink.api.v1;

import ch.qos.logback.core.CoreConstants;

/* loaded from: classes.dex */
public class MiLinkClientDevice {
    public String mDeviceId;
    public String mDeviceName;
    public String mDeviceType;
    public String mLastConnectTime;
    public String mP2pMac;
    public String mWifiMac;

    public void setDeviceId(String str) {
        this.mDeviceId = str;
    }

    public void setDeviceName(String str) {
        this.mDeviceName = str;
    }

    public void setDeviceType(String str) {
        this.mDeviceType = str;
    }

    public void setP2pMac(String str) {
        this.mP2pMac = str;
    }

    public void setWifiMac(String str) {
        this.mWifiMac = str;
    }

    public void setLastConnectTime(String str) {
        this.mLastConnectTime = str;
    }

    public String toString() {
        return "MiLinkClientDevice{mDeviceId='" + this.mDeviceId + CoreConstants.SINGLE_QUOTE_CHAR + ", mDeviceName='" + this.mDeviceName + CoreConstants.SINGLE_QUOTE_CHAR + ", mDeviceType='" + this.mDeviceType + CoreConstants.SINGLE_QUOTE_CHAR + ", mP2pMac='" + this.mP2pMac + CoreConstants.SINGLE_QUOTE_CHAR + ", mWifiMac='" + this.mWifiMac + CoreConstants.SINGLE_QUOTE_CHAR + ", mLastConnectTime='" + this.mLastConnectTime + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }
}
