package com.milink.api.v1;

/* loaded from: classes.dex */
public interface MiLinkClientDeviceListener {
    void onDeviceFound(MiLinkClientDevice miLinkClientDevice);

    void onDeviceLost(MiLinkClientDevice miLinkClientDevice);
}
