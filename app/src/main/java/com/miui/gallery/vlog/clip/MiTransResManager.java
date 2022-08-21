package com.miui.gallery.vlog.clip;

import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.sdk.interfaces.ITransManager;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.XmsVideoTrack;

/* loaded from: classes2.dex */
public class MiTransResManager implements ITransManager {
    public MiVideoSdkManager mSdkManager;
    public XmsTimeline mTimeline;
    public XmsVideoTrack mVideoTrack;

    public MiTransResManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        this.mSdkManager = miVideoSdkManager;
        this.mTimeline = xmsTimeline;
        this.mVideoTrack = xmsTimeline.getVideoTrack(0);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.ITransManager
    public void buildTransitions(int i, String str) {
        buildTransitions(i, "movit.transition", str);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.ITransManager
    public void buildTransitions(int i, String str, String str2) {
        this.mSdkManager.buildTransitions(i, str, str2);
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.ITransManager
    public void setBuiltinTransition(int i, String str, String str2) {
        this.mSdkManager.buildTransitions(i, str, str2);
    }
}
