package android.support.v4.media.session;

/* loaded from: classes.dex */
public final class MediaControllerCompat$PlaybackInfo {
    public final int mAudioStream;
    public final int mCurrentVolume;
    public final int mMaxVolume;
    public final int mPlaybackType;
    public final int mVolumeControl;

    public MediaControllerCompat$PlaybackInfo(int i, int i2, int i3, int i4, int i5) {
        this.mPlaybackType = i;
        this.mAudioStream = i2;
        this.mVolumeControl = i3;
        this.mMaxVolume = i4;
        this.mCurrentVolume = i5;
    }
}
