package com.miui.gallery.projection;

import com.miui.gallery.model.BaseDataItem;

/* loaded from: classes2.dex */
public interface IConnectController {

    /* loaded from: classes2.dex */
    public interface DataSet {
        int getCount();

        int getIndexOfItem(String str, int i);

        BaseDataItem getItem(BaseDataItem baseDataItem, int i);
    }

    /* loaded from: classes2.dex */
    public interface OnMediaPlayListener {
        void onLoading();

        void onPaused();

        void onPlaying();

        void onStopped();
    }

    /* loaded from: classes2.dex */
    public interface OnStatusListener {
        void onConnectStatusChanged(int i);

        void onOpenStatusChanged(boolean z);
    }

    void closeService();

    int getConnectStatus();

    String getConnectedDevice();

    String getConnectingDevice();

    int getCurrentPosition();

    int getDuration();

    boolean isConnected();

    boolean isPlaying();

    default void onMirrorResume(String str) {
    }

    void pause();

    void playVideo(String str, String str2);

    void project();

    void registerMediaPlayListener(OnMediaPlayListener onMediaPlayListener);

    void registerStatusListener(OnStatusListener onStatusListener);

    default void release() {
    }

    void resume();

    void rotate(float f);

    void seekTo(int i);

    default void setVideoIsPlaying(boolean z) {
    }

    boolean showSlide(int i);

    void stop();

    void stopSlide();

    void syncRemoteView(float f, float f2, float f3, float f4, float f5, float f6, float f7);

    void unregisterMediaPlayListener(OnMediaPlayListener onMediaPlayListener);

    void unregisterStatusListener(OnStatusListener onStatusListener);

    void updateCurrentFolder(DataSet dataSet);

    void updateCurrentPhoto(String str, int i);
}
