package com.miui.gallery.vlog.clip.single;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import com.miui.gallery.vlog.sdk.manager.MiVideoClipManager;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class SingleClipMenuModel implements SingleClipMenuContract$ISingleClipMenuModel {
    public MiVideoClipManager mClipManager;
    public boolean mIsWaitingForLastSeekDone;
    public double mMultiple = 1.0d;
    public VlogModel mVlogModel;

    public SingleClipMenuModel(Context context) {
        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) context, VlogModel.class);
        this.mVlogModel = vlogModel;
        this.mClipManager = (MiVideoClipManager) vlogModel.getSdkManager().getManagerService(3);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void changeSpeed(double d) {
        DefaultLogger.d("SingleClipMenuModel", "changeSpeed: speed=%f,current=%d", Double.valueOf(d), Long.valueOf(getCurrentTimeMicros()));
        this.mMultiple = d / getVideoClip().getSpeed();
        this.mVlogModel.getSdkManager().disconnect();
        this.mClipManager.changeSpeed(0, d);
        this.mVlogModel.getSdkManager().onTimeLineDurationChanged();
        this.mVlogModel.getSdkManager().reconnect();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long getCurrentTimeMicros() {
        return this.mVlogModel.getSdkManager().getCurrentTimeMicro();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public IVideoClip getVideoClip() {
        return this.mVlogModel.getIVideoClips().get(0);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void seek(long j) {
        this.mVlogModel.getSdkManager().seek(j);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long getClipTotalTimeMicros() {
        return getVideoClip().getTimelineDuration() * 1000;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long getActualTotalTimeMicros() {
        return (long) (getVideoClip().getOriginDuration() / getVideoClip().getSpeed());
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long getActualTrimInMicros() {
        IVideoClip videoClip = getVideoClip();
        long trimIn = videoClip.getTrimIn();
        return (long) (trimIn / videoClip.getSpeed());
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long getActualTrimOutMicros() {
        IVideoClip videoClip = getVideoClip();
        long trimOut = videoClip.getTrimOut();
        return (long) (trimOut / videoClip.getSpeed());
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public float getCurrentDisplayRatio() {
        return getVideoClip().getWidth() / getVideoClip().getHeight();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long calcSdkSupportTrimIn(long j) {
        return (long) (j * getVideoClip().getSpeed());
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void changeTrimIn(long j) {
        DefaultLogger.d("SingleClipMenuModel", "changeTrimIn:%d", Long.valueOf(j));
        getVideoClip().changeTrimInPoint(j, true);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long calcSdkSupportTrimOut(long j) {
        return (long) (j * getVideoClip().getSpeed());
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void changeTrimOut(long j) {
        DefaultLogger.d("SingleClipMenuModel", "changeTrimOut:%d", Long.valueOf(j));
        getVideoClip().changeTrimOutPoint(j, true);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public long getCurrentTimeWithSpeed() {
        return (long) (getCurrentTimeMicros() / this.mMultiple);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void resume() {
        this.mVlogModel.getSdkManager().resume();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void pause() {
        this.mVlogModel.getSdkManager().pause();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public VideoFrameLoader getVideoFrameLoader() {
        return this.mVlogModel.getSdkManager().getVideoFrameLoader();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public String getVideoPath() {
        return getVideoClip().getFilePath();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public boolean isPlaying() {
        return this.mVlogModel.getSdkManager().isPlay();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public double getSpeed() {
        return getVideoClip().getSpeed();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public boolean isWaitingForLastSeekDone() {
        return this.mIsWaitingForLastSeekDone;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void setWaitingForLastSeekDone(boolean z) {
        this.mIsWaitingForLastSeekDone = z;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuModel
    public void onTimelineDurationChanged() {
        this.mVlogModel.getSdkManager().onTimeLineDurationChanged();
    }
}
