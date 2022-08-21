package com.miui.gallery.magic.special.effects.video.effects.preview;

import android.content.Intent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;

/* loaded from: classes2.dex */
public interface IPreview$P {
    void cutVideoSlice(Intent intent);

    int getCurrentType();

    String getOutputFilePath();

    long getTotalTime();

    String getVideoPath();

    void initVideoData(Intent intent, boolean z);

    void onBack(View view);

    void onComposeMP4();

    void onPause();

    void onPlayVideo();

    void onReceiverFinish();

    void progressToVideoSeek(long j);

    void seekToEnd(long j);

    void selectAudio(ListItem listItem);

    void selectAudioFile(String str);

    void setStartSegEffectId(int i, boolean z);

    void setSurfaceHolderPlayer(SurfaceHolder surfaceHolder, SurfaceView surfaceView);

    void setThumbnailVisible(boolean z);

    void setVideoTime(float f, float f2);

    void setVolume(float f);

    void stopVideo();

    void stopVideoPreview();
}
