package com.miui.gallery.magic.special.effects.video.effects.preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;

/* loaded from: classes2.dex */
public interface IPreview$V {
    void cutVideoSlice(Intent intent);

    void dismissDialog();

    int getCurrentType();

    long getTotalTime();

    String getVideoPath();

    void initVideoData(Intent intent);

    void onBack();

    void onComposeMP4();

    void onMute(boolean z);

    void onPause();

    void onPlayVideo();

    void onReceiverFinish();

    void playButtonStatus(boolean z);

    void resetPlayStopIcon(boolean z);

    void seekToEnd(long j);

    void seekToVideo(long j);

    void selectAudio(ListItem listItem);

    void selectAudioFile(String str);

    void setCoverBitmap(Bitmap bitmap);

    void setStartSegEffectId(int i, boolean z);

    void setSurfaceHolderPlayer(SurfaceHolder surfaceHolder, SurfaceView surfaceView);

    void setThumbnailVisible(boolean z);

    void setVideoTime(String str, String str2);

    void setVolume(float f);

    void showDialogProgress(Object obj, int i);

    void stopVideo();

    void stopVideoPreview();

    void surfaceCreated();

    void switchToAudioTrack();

    void switchToVideoEffect();
}
