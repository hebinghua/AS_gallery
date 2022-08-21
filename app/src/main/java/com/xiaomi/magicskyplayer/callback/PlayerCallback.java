package com.xiaomi.magicskyplayer.callback;

import com.xiaomi.magicskyplayer.datastruct.VideoSize;
import com.xiaomi.magicskyplayer.enums.AVErrorState;

/* loaded from: classes3.dex */
public interface PlayerCallback {
    void onAudioRenderingStart();

    void onFirstPacketRecved();

    void onOpenStreamFailed(AVErrorState aVErrorState);

    void onPlayerPaused();

    void onPlayerResumed();

    void onPlayerStarted();

    void onPlayerStoped();

    void onSeekCompleted();

    void onStartBuffering();

    void onStartPlaying();

    void onStartWithTimeInvalid(long j);

    void onStreamEOF();

    void onVideoRenderingStart();

    void onVideoSizeChanged(VideoSize videoSize);
}
