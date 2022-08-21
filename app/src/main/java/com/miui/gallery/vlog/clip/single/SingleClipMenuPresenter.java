package com.miui.gallery.vlog.clip.single;

import android.content.Context;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.clip.single.SingleClipMenuFragment;
import com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;

/* loaded from: classes2.dex */
public class SingleClipMenuPresenter extends BasePresenter {
    public long mCurrentPreviewTimeMicros;
    public boolean mIsSeekWhenPlaying;
    public long mLastSeekTimeMicros;
    public long mLockedAreaEndTimeMicros;
    public long mLockedAreaStartTimeMicros;
    public boolean mPostponeResume;
    public SingleClipMenuContract$ISingleClipMenuModel mSingleClipMenuModel;
    public SingleClipMenuContract$ISingleClipMenuView mSingleClipMenuView;

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
    }

    public SingleClipMenuPresenter(Context context, SingleClipMenuContract$ISingleClipMenuView singleClipMenuContract$ISingleClipMenuView) {
        super(context);
        this.mSingleClipMenuView = singleClipMenuContract$ISingleClipMenuView;
        this.mSingleClipMenuModel = new SingleClipMenuModel(context);
    }

    public void resume() {
        this.mSingleClipMenuModel.resume();
        this.mSingleClipMenuView.showPlayProgress();
        this.mSingleClipMenuView.showPause();
    }

    public void handlePlayClick() {
        DefaultLogger.d("SingleClipMenuPresenter", "handlePlayClick isWaitingForLastSeekDone=%b", Boolean.valueOf(this.mSingleClipMenuModel.isWaitingForLastSeekDone()));
        if (this.mSingleClipMenuModel.isWaitingForLastSeekDone()) {
            this.mPostponeResume = true;
        } else if (this.mSingleClipMenuModel.isPlaying()) {
        } else {
            resume();
        }
    }

    public void handlePauseClick() {
        if (this.mSingleClipMenuModel.isPlaying()) {
            pause();
        }
    }

    public void handleLockedAreaStartTimeChange(long j) {
        DefaultLogger.d("SingleClipMenuPresenter", "handleLockedAreaStartTimeChange=%d", Long.valueOf(j));
        this.mLockedAreaStartTimeMicros = j;
        seek(j);
        this.mCurrentPreviewTimeMicros = 0L;
        this.mSingleClipMenuView.updateCurrentTime(0L);
        this.mSingleClipMenuView.updateTotalTime(this.mLockedAreaEndTimeMicros - this.mLockedAreaStartTimeMicros);
    }

    public void handleLockedAreaEndTimeChange(long j) {
        DefaultLogger.d("SingleClipMenuPresenter", "handleLockedAreaEndTimeChange=%d", Long.valueOf(j));
        this.mLockedAreaEndTimeMicros = j;
        seek(j);
        long j2 = this.mLockedAreaEndTimeMicros - this.mLockedAreaStartTimeMicros;
        this.mCurrentPreviewTimeMicros = j2;
        this.mSingleClipMenuView.updateCurrentTime(j2);
        this.mSingleClipMenuView.updateTotalTime(j2);
    }

    public void handleProgressTimeChange(long j) {
        DefaultLogger.d("SingleClipMenuPresenter", "handleProgressTimeChange=%d", Long.valueOf(j));
        pauseIfNeed();
        long actualTrimInMicros = j - this.mSingleClipMenuModel.getActualTrimInMicros();
        seek(actualTrimInMicros);
        this.mCurrentPreviewTimeMicros = j;
        this.mSingleClipMenuView.updateCurrentTime(actualTrimInMicros);
    }

    public final void pauseIfNeed() {
        if (!this.mSingleClipMenuModel.isPlaying() || this.mIsSeekWhenPlaying) {
            return;
        }
        pause();
        this.mIsSeekWhenPlaying = true;
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void seek(long j) {
        DefaultLogger.d("SingleClipMenuPresenter", "single seek to:%d", Long.valueOf(j));
        this.mSingleClipMenuModel.seek(j);
        this.mSingleClipMenuView.setPlayProgress(j);
        this.mLastSeekTimeMicros = j;
        this.mSingleClipMenuModel.setWaitingForLastSeekDone(true);
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void pause() {
        this.mSingleClipMenuModel.pause();
        this.mSingleClipMenuView.showPlay();
    }

    public void handleSeekDone(long j) {
        DefaultLogger.d("SingleClipMenuPresenter", "handleSeekDone: timeMillis=%d, mLastSeekTimeMicros=%d", Long.valueOf(j), Long.valueOf(this.mLastSeekTimeMicros));
        if (this.mLastSeekTimeMicros / 1000 == j) {
            this.mSingleClipMenuModel.setWaitingForLastSeekDone(false);
            if (this.mPostponeResume) {
                this.mPostponeResume = false;
                if (this.mSingleClipMenuView.getUIState() != SingleClipMenuFragment.UIState.PLAYING) {
                    return;
                }
                resume();
            } else if (!this.mIsSeekWhenPlaying || this.mSingleClipMenuView.isTouchingClipBar()) {
            } else {
                resume();
                this.mIsSeekWhenPlaying = false;
            }
        }
    }

    public void handleVlogPlayViewSeek(long j) {
        DefaultLogger.d("SingleClipMenuPresenter", "handleVlogPlayViewSeek=%d", Long.valueOf(j));
        pauseIfNeed();
        seek(j);
        this.mSingleClipMenuView.updateCurrentTime(j);
    }

    public void changeSpeed(double d) {
        pauseIfNeed();
        this.mSingleClipMenuModel.changeSpeed(d);
        long currentTimeWithSpeed = this.mSingleClipMenuModel.getCurrentTimeWithSpeed();
        seek(currentTimeWithSpeed);
        this.mSingleClipMenuView.updateTotalTime(this.mSingleClipMenuModel.getClipTotalTimeMicros());
        this.mSingleClipMenuView.updateCurrentTime(currentTimeWithSpeed);
        this.mSingleClipMenuView.updateSeekBar(this.mSingleClipMenuModel.getActualTotalTimeMicros(), this.mSingleClipMenuModel.getActualTrimInMicros(), this.mSingleClipMenuModel.getActualTrimOutMicros(), this.mSingleClipMenuView.calcClipProgressTimeMicros(currentTimeWithSpeed), d);
    }

    public void handleVideoClipReady() {
        this.mSingleClipMenuView.updateCurrentTime(0L);
        this.mSingleClipMenuView.updateTotalTime(this.mSingleClipMenuModel.getActualTotalTimeMicros());
        this.mSingleClipMenuView.initSeekBar(this.mSingleClipMenuModel.getActualTotalTimeMicros(), this.mSingleClipMenuModel.getCurrentDisplayRatio());
        this.mLockedAreaEndTimeMicros = this.mSingleClipMenuModel.getVideoClip().getOriginDuration();
    }

    public long getActualTrimInMicros() {
        return this.mSingleClipMenuModel.getActualTrimInMicros();
    }

    public long getCurrentTimeMicros() {
        return this.mSingleClipMenuModel.getCurrentTimeMicros();
    }

    public void handleSpeedClick() {
        long clipTotalTimeMicros = this.mSingleClipMenuModel.getClipTotalTimeMicros();
        double speed = this.mSingleClipMenuModel.getSpeed();
        boolean[] zArr = {true, true, true, true, true};
        double[] supportSpeeds = this.mSingleClipMenuView.getSupportSpeeds();
        for (int i = 0; i < 5; i++) {
            if (((long) ((clipTotalTimeMicros * speed) / supportSpeeds[i])) < 1000000) {
                zArr[i] = false;
            }
        }
        this.mSingleClipMenuView.updateSpeedPickerEnables(zArr);
    }

    public VideoFrameLoader getVideoFrameLoader() {
        return this.mSingleClipMenuModel.getVideoFrameLoader();
    }

    public String getVideoPath() {
        return this.mSingleClipMenuModel.getVideoPath();
    }

    public void handleTouchSeekBar(boolean z, SingleVideoClipBar.TouchArea touchArea) {
        if (touchArea == SingleVideoClipBar.TouchArea.LEFT_AXIS || touchArea == SingleVideoClipBar.TouchArea.RIGHT_AXIS) {
            if (z) {
                pauseIfNeed();
                DefaultLogger.d("SingleClipMenuPresenter", "setTrimIn=0,setTrimOut=" + this.mSingleClipMenuModel.getVideoClip().getOriginDuration());
                this.mSingleClipMenuModel.changeTrimIn(0L);
                SingleClipMenuContract$ISingleClipMenuModel singleClipMenuContract$ISingleClipMenuModel = this.mSingleClipMenuModel;
                singleClipMenuContract$ISingleClipMenuModel.changeTrimOut(singleClipMenuContract$ISingleClipMenuModel.getVideoClip().getOriginDuration());
            } else {
                DefaultLogger.d("SingleClipMenuPresenter", "handleTouchSeekBar setTrimIn=" + this.mSingleClipMenuModel.calcSdkSupportTrimIn(this.mLockedAreaStartTimeMicros) + ",setTrimOut=" + this.mSingleClipMenuModel.calcSdkSupportTrimOut(this.mLockedAreaEndTimeMicros));
                SingleClipMenuContract$ISingleClipMenuModel singleClipMenuContract$ISingleClipMenuModel2 = this.mSingleClipMenuModel;
                singleClipMenuContract$ISingleClipMenuModel2.changeTrimIn(singleClipMenuContract$ISingleClipMenuModel2.calcSdkSupportTrimIn(this.mLockedAreaStartTimeMicros));
                SingleClipMenuContract$ISingleClipMenuModel singleClipMenuContract$ISingleClipMenuModel3 = this.mSingleClipMenuModel;
                singleClipMenuContract$ISingleClipMenuModel3.changeTrimOut(singleClipMenuContract$ISingleClipMenuModel3.calcSdkSupportTrimOut(this.mLockedAreaEndTimeMicros));
                seek(this.mCurrentPreviewTimeMicros);
            }
        }
        if (!z && this.mIsSeekWhenPlaying && !this.mSingleClipMenuModel.isPlaying() && !this.mSingleClipMenuModel.isWaitingForLastSeekDone()) {
            resume();
            this.mIsSeekWhenPlaying = false;
        }
        if (!z) {
            this.mSingleClipMenuModel.onTimelineDurationChanged();
        }
    }
}
