package com.miui.gallery.vlog.sdk.manager;

import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.clip.ClipMenuPresenter;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.IClipManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.XmsVideoTrack;
import java.util.List;

/* loaded from: classes2.dex */
public class MiVideoClipManager extends BaseVlogManager implements IClipManager {
    public XmsVideoTrack mVideoTrack;

    public MiVideoClipManager(MiVideoSdkManager miVideoSdkManager, XmsTimeline xmsTimeline) {
        super(miVideoSdkManager, xmsTimeline);
        this.mVideoTrack = this.mXmsTimeline.getVideoTrack(0);
    }

    public boolean removeVideo(int i) {
        return this.mSdkManager.removeVideo(i);
    }

    public void reverseClip(int i, ClipMenuPresenter.CancelReverseCallback cancelReverseCallback) {
        this.mSdkManager.reverseClip(i, cancelReverseCallback);
    }

    public void changeSpeed(int i, double d) {
        IVideoClip videoClip = this.mSdkManager.getVideoClip(i);
        if (videoClip != null) {
            videoClip.changeSpeed(d);
            if (videoClip.isChangeSpeed()) {
                this.mSdkManager.getVideoClip(i).setVolumeGain(0.0f, 0.0f);
            } else {
                this.mSdkManager.getVideoClip(i).setVolumeGain(100.0f, 100.0f);
            }
        }
    }

    public boolean splitClip(int i, long j) {
        return this.mSdkManager.splitClip(i, j);
    }

    public boolean duCut(IVideoClip iVideoClip, long j) {
        if (iVideoClip == null) {
            return false;
        }
        long inPoint = iVideoClip.getInPoint();
        long outPoint = iVideoClip.getOutPoint();
        if (j - inPoint > 500000 && outPoint - j > 500000) {
            return splitClip(iVideoClip.getIndex(), j);
        }
        return false;
    }

    public boolean sortVideoClip(int i, int i2) {
        this.mVideoTrack.moveClip(i, i2);
        return true;
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.IClipManager
    public long insertVideoClips(List<String> list) {
        int currentIndex = this.mSdkManager.getCurrentIndex();
        int i = currentIndex + 1;
        if (currentIndex == this.mSdkManager.getClipCount() - 1) {
            this.mSdkManager.appendClips(list);
        } else {
            this.mSdkManager.appendClips(i, list);
        }
        return this.mSdkManager.getVideoClip(i).getInPoint();
    }
}
