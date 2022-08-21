package com.miui.gallery.vlog.sdk.interfaces;

import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.xiaomi.milab.videosdk.XmsTimeline;

/* loaded from: classes2.dex */
public abstract class BaseVlogManager {
    public MiVideoSdkManager mSdkManager;
    public XmsTimeline mXmsTimeline;

    public BaseVlogManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        this.mSdkManager = miVideoSdkManager;
        this.mXmsTimeline = xmsTimeline;
    }

    public void disconnect() {
        this.mSdkManager.disconnect();
    }

    public void reconnect() {
        this.mSdkManager.reconnect();
    }
}
