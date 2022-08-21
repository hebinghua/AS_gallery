package com.xiaomi.magicskyplayer;

import android.util.Log;
import android.view.Surface;
import com.xiaomi.magicskyplayer.callback.PlayerCallback;
import com.xiaomi.magicskyplayer.datastruct.VideoSize;
import com.xiaomi.magicskyplayer.enums.AVErrorState;
import com.xiaomi.magicskyplayer.enums.PlayerCacheType;
import com.xiaomi.magicskyplayer.enums.PlayerPlaybackState;
import com.xiaomi.magicskyplayer.enums.PlayerSeekingMode;
import com.xiaomi.magicskyplayer.enums.PlayerWorkingMode;
import com.xiaomi.stat.d;

/* loaded from: classes3.dex */
public class Player {
    private static String TAG = "Player";
    private long playerInst = 0;
    private String url = "";
    private PlayerCallback pc = null;

    private native long GetCurrentStreamPositionJni();

    private native void addFlashFilterJni(int i);

    private native void addGrayscaleFilterJni();

    private native void addMotionFlowFilterJni();

    private native void addPngMixFilterJni(String str, float f, float f2, float f3, float f4);

    private native void addRecordingSessionJni(long j);

    private native void addShakeFilterJni();

    private native void addSlowDownFilterJni();

    private native void addSobelEdgeDetectionFilterJni();

    private native void addSoulFilterJni();

    private native void addVideoMapFilterJni(String str, String str2, String str3);

    private native long bufferTimeMaxJni();

    private static native int cancelCompressingMP4FileJni(String str);

    private static native int compressMP4FileJni(String str, String str2, long j);

    private native long constructPlayerJni(String str, int i, long j);

    private native long currentPlaybackTimeJni();

    private native String debugReportJni();

    private native void destructPlayerJni();

    private native long durationJni();

    private native void enableVideoFilterJni(boolean z);

    private native long getAudioTransferJni();

    private native long getCurrentAudioTimestampJni();

    private native long getCurrentCachePositionJni();

    private native long getStreamIdJni();

    private native long getTimestampOfCurrentVideoFrameJni();

    private native boolean isPausedJni();

    private native void muteAudioJni();

    private native void pauseCacheThreadOtherJni(String str);

    private native void pauseCacheThreadUriAllJni();

    private native void pauseCacheThreadUriJni(String str);

    private native void pauseJni();

    private native int playbackStateJni();

    private native void redrawJni();

    private native boolean reloadJni(String str, boolean z);

    private native void removeRecordingSessionJni(long j);

    private native boolean resumeJni();

    private native boolean seekToJni(long j, int i);

    private native void setAllFileSizeJni(long j);

    private native void setBufferTimeMaxJni(long j);

    private native void setCachePathJni(String str);

    private native void setCacheSizeJni(long j);

    private native void setCacheSpeedJni(long j);

    private native void setEnableCacheTypeJni(int i);

    private native void setFrameLoopJni(boolean z);

    private native void setGravityJni(int i, int i2, int i3);

    private native void setIpListJni(String[] strArr);

    private native void setLocalCacheJni(String str, long j);

    private native void setResolveDnsJni(boolean z);

    private native void setSpeakerJni(boolean z);

    private native void setSpeakerVolumeJni(float f);

    private native boolean setSpeedRatioJni(double d);

    private native void setUserIdandClienIpJni(String str, String str2);

    private native void setVideoFilterIntensityJni(float f);

    private native void setVideoFilterJni(String str);

    private native void setVideoSurfaceJni(Surface surface);

    private native void setWifiStatusJni(boolean z);

    private native void shiftUpJni(float f, float f2, float f3, float f4, float f5);

    private native void startCacheUriAllJni();

    private native void startCacheUriJni(String str);

    private native void startCacheUriOtherJni(String str);

    private native boolean startJni(String str, String str2, boolean z);

    private native void startRecordingJni();

    private native boolean startWithTimeJni(String str, String str2, boolean z, long j);

    private native void stopJni();

    private native void stopRecordingJni();

    private native void unMuteAudioJni();

    private native void updateCacheUriJni(String[] strArr);

    private static native String versionJni();

    private native VideoSize videoSizeJni();

    public native boolean editorPlayerSetVolumeExternalMp3Jni(float f);

    public native boolean editorPlayerSetVolumeInnerJni(float f);

    public native boolean editorPlayerStartJni(String str, String str2, long j, long j2);

    public native void enableEqWithModeJni(boolean z, int i);

    public native void enableExtremeLargeVolumeJni(boolean z);

    public native void enableFixedVolumeJni(boolean z);

    public native void setMaxDownloadBufferTimeJni(long j);

    public native void setSpeedUpThresholdJni(long j);

    public void constructPlayer(String str, PlayerCallback playerCallback, PlayerWorkingMode playerWorkingMode, long j) {
        Log.d(TAG, "constructPlayer");
        this.pc = playerCallback;
        this.playerInst = constructPlayerJni(str, playerWorkingMode.ordinal(), j);
    }

    public void destructPlayer() {
        Log.d(TAG, "destructPlayer");
        setVideoSurface(null);
        destructPlayerJni();
        this.pc = null;
        this.playerInst = 0L;
    }

    public void setVideoSurface(Surface surface) {
        setVideoSurfaceJni(surface);
    }

    public boolean start(String str, String str2, boolean z) {
        this.url = str;
        return startJni(str, str2, z);
    }

    public boolean start(String str, String str2, boolean z, long j) {
        this.url = str;
        return startWithTimeJni(str, str2, z, j);
    }

    public boolean reload(String str, boolean z) {
        this.url = str;
        return reloadJni(str, z);
    }

    public void stop() {
        stopJni();
    }

    public void pause() {
        pauseJni();
    }

    public void redraw() {
        redrawJni();
    }

    public boolean resume() {
        return resumeJni();
    }

    public boolean isPaused() {
        return isPausedJni();
    }

    public void setSpeaker(boolean z) {
        setSpeakerJni(z);
    }

    public void setSpeakerVolume(float f) {
        setSpeakerVolumeJni(f);
    }

    public boolean setSpeedRatio(double d) {
        return setSpeedRatioJni(d);
    }

    public void setResolveDns(boolean z) {
        setResolveDnsJni(z);
    }

    public void setFrameLoop(boolean z) {
        setFrameLoopJni(z);
    }

    public void enableVideoFilter(boolean z) {
        enableVideoFilterJni(z);
    }

    public void setEnableCacheType(PlayerCacheType playerCacheType) {
        setEnableCacheTypeJni(playerCacheType.ordinal());
    }

    public void addVideoMapFilter(String str, String str2, String str3) {
        addVideoMapFilterJni(str, str2, str3);
    }

    public void addMotionFlowFilter() {
        addMotionFlowFilterJni();
    }

    public void addShakeFilter() {
        addShakeFilterJni();
    }

    public void addSobelEdgeDetectionFilter() {
        addSobelEdgeDetectionFilterJni();
    }

    public void addSoulFilter() {
        addSoulFilterJni();
    }

    public void addGrayscaleFilter() {
        addGrayscaleFilterJni();
    }

    public void addSlowDownFilter() {
        addSlowDownFilterJni();
    }

    public void addPngMixFilter(String str, float f, float f2, float f3, float f4) {
        addPngMixFilterJni(str, f, f2, f3, f4);
    }

    public void addFlashFilter(int i) {
        addFlashFilterJni(i);
    }

    public void updateCacheUri(String[] strArr) {
        updateCacheUriJni(strArr);
    }

    public void setVideoFilter(String str) {
        setVideoFilterJni(str);
    }

    public void setVideoFilterIntensity(float f) {
        setVideoFilterIntensityJni(f);
    }

    public boolean seekTo(long j, PlayerSeekingMode playerSeekingMode) {
        return seekToJni(j, playerSeekingMode.ordinal());
    }

    public PlayerPlaybackState playbackState() {
        return PlayerPlaybackState.int2enum(playbackStateJni());
    }

    public long bufferTimeMax() {
        return bufferTimeMaxJni();
    }

    public void setBufferTimeMax(long j) {
        if (j < 120) {
            return;
        }
        setBufferTimeMaxJni(j);
    }

    public long duration() {
        return durationJni();
    }

    public long GetCurrentStreamPosition() {
        return GetCurrentStreamPositionJni();
    }

    public long currentPlaybackTime() {
        return currentPlaybackTimeJni();
    }

    public long getCurrentCachePosition() {
        return getCurrentCachePositionJni();
    }

    public long getCurrentAudioTimestamp() {
        return getCurrentAudioTimestampJni();
    }

    public void setSpeedUpThreshold(long j) {
        setSpeedUpThresholdJni(j);
    }

    public void setMaxDownloadBufferTime(long j) {
        setMaxDownloadBufferTimeJni(j);
    }

    public void muteAudio() {
        muteAudioJni();
    }

    public void unMuteAudio() {
        unMuteAudioJni();
    }

    public void startCacheUri(String str) {
        startCacheUriJni(str);
    }

    public void startCacheUriAll() {
        startCacheUriAllJni();
    }

    public void startCacheUriOther(String str) {
        startCacheUriOtherJni(str);
    }

    public void pauseCacheThreadUri(String str) {
        pauseCacheThreadUriJni(str);
    }

    public void pauseCacheThreadUriAll() {
        pauseCacheThreadUriAllJni();
    }

    public void pauseCacheThreadOther(String str) {
        pauseCacheThreadOtherJni(str);
    }

    public void setCacheSize(long j) {
        setCacheSizeJni(j);
    }

    public void setAllFileSize(long j) {
        setAllFileSizeJni(j);
    }

    public void setCacheSpeed(long j) {
        setCacheSpeedJni(j);
    }

    public void setCachePath(String str) {
        setCachePathJni(str);
    }

    public void setWifiStatus(boolean z) {
        setWifiStatusJni(z);
    }

    public VideoSize videoSize() {
        return videoSizeJni();
    }

    public static String version() {
        return versionJni();
    }

    public String debugReport() {
        return debugReportJni();
    }

    public static int compressMP4File(String str, String str2, long j) {
        Log.d(TAG, "compress MP4 files");
        return compressMP4FileJni(str, str2, j);
    }

    public static int cancelCompressingMP4File(String str) {
        Log.d(TAG, "cancel compressing MP4 files");
        return cancelCompressingMP4FileJni(str);
    }

    public long getAudioTransfer() {
        Log.d(TAG, "get audio transfer");
        return getAudioTransferJni();
    }

    public long getStreamId() {
        Log.d(TAG, "get stream ID");
        return getStreamIdJni();
    }

    /* loaded from: classes3.dex */
    public enum SurfaceGravity {
        SurfaceGravityResizeAspect(0),
        SurfaceGravityResizeAspectFit(1),
        SurfaceGravityResizeAspectFill(2);
        
        private int code;

        SurfaceGravity(int i) {
            this.code = i;
        }

        public int toInt() {
            return this.code;
        }
    }

    public void setGravity(SurfaceGravity surfaceGravity, int i, int i2) {
        setGravityJni(surfaceGravity.toInt(), i, i2);
    }

    public void shiftUp(float f, float f2, float f3, float f4, float f5) {
        shiftUpJni(f, f2, f3, f4, f5);
    }

    public void setIpList(String[] strArr, String[] strArr2) {
        setIpListJni(strArr);
    }

    public void addRecordingSession(long j) {
        addRecordingSessionJni(j);
    }

    public void removeRecordingSession(long j) {
        removeRecordingSessionJni(j);
    }

    public long getTimestampOfCurrentVideoFrame() {
        return getTimestampOfCurrentVideoFrameJni();
    }

    public void setUserIdandClienIp(String str, String str2) {
        setUserIdandClienIpJni(str, str2);
    }

    public void enableFixedVolume(boolean z) {
        enableFixedVolumeJni(z);
    }

    public void enableExtremeLargeVolume(boolean z) {
        enableExtremeLargeVolumeJni(z);
    }

    public void enableEqWithMode(boolean z, int i) {
        enableEqWithModeJni(z, i);
    }

    public void onAudioRenderingStart() {
        Log.d(TAG, "callback:onAudioRenderingStart");
        this.pc.onAudioRenderingStart();
    }

    public void onVideoRenderingStart() {
        Log.d(TAG, "callback:onVideoRenderingStart");
        this.pc.onVideoRenderingStart();
    }

    public void onVideoSizeChanged(int i, int i2) {
        Log.d(TAG, "callback:onVideoSizeChanged");
        VideoSize videoSize = new VideoSize(0.0f, 0.0f);
        videoSize.video_width = i;
        videoSize.video_height = i2;
        this.pc.onVideoSizeChanged(videoSize);
    }

    public void onStartBuffering() {
        Log.d(TAG, "callback:onStartBuffering");
        this.pc.onStartBuffering();
    }

    public void onStartPlaying() {
        Log.d(TAG, "callback:onStartPlaying");
        this.pc.onStartPlaying();
    }

    public void onPlayerStarted() {
        Log.d(TAG, "callback:onPlayerStarted");
        this.pc.onPlayerStarted();
    }

    public void onPlayerStoped() {
        Log.d(TAG, "callback:onPlayerStoped");
        this.pc.onPlayerStoped();
    }

    public void onPlayerPaused() {
        Log.d(TAG, "callback:onPlayerPaused");
        this.pc.onPlayerPaused();
    }

    public void onPlayerResumed() {
        Log.d(TAG, "callback:onPlayerResumed");
        this.pc.onPlayerResumed();
    }

    public void onSeekCompleted() {
        Log.d(TAG, "callback:onSeekCompleted");
        this.pc.onSeekCompleted();
    }

    public void onStreamEOF() {
        Log.d(TAG, "callback:onStreamEOF");
        this.pc.onStreamEOF();
    }

    public void onFirstPacketRecved() {
        Log.d(TAG, "callback:onFirstPacketRecved");
        this.pc.onFirstPacketRecved();
    }

    public void onOpenStreamFailed(int i) {
        String str = TAG;
        Log.d(str, "callback:onOpenStreamFailed" + i);
        this.pc.onOpenStreamFailed(AVErrorState.int2enum(i));
    }

    public void onStartWithTimeInvalid(long j) {
        String str = TAG;
        Log.d(str, "debug::onStartWithTimeInvalid, the file duration is " + j + d.H);
        this.pc.onStartWithTimeInvalid(j);
    }

    public boolean editorPlayerStart(String str, String str2, long j, long j2) {
        this.url = str;
        return editorPlayerStartJni(str, str2, j, j2);
    }

    public boolean editorPlayerSetInnerVolume(float f) {
        return editorPlayerSetVolumeInnerJni(f);
    }

    public boolean editorPlayerSetMP3Volume(float f) {
        return editorPlayerSetVolumeExternalMp3Jni(f);
    }
}
