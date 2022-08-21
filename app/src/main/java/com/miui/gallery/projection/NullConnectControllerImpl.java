package com.miui.gallery.projection;

import com.miui.gallery.projection.IConnectController;

/* loaded from: classes2.dex */
public class NullConnectControllerImpl implements IConnectController {
    @Override // com.miui.gallery.projection.IConnectController
    public void closeService() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public int getConnectStatus() {
        return -1;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public String getConnectedDevice() {
        return null;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public String getConnectingDevice() {
        return null;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public int getCurrentPosition() {
        return 0;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public int getDuration() {
        return 0;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean isConnected() {
        return false;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean isPlaying() {
        return false;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void pause() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void playVideo(String str, String str2) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void project() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void registerMediaPlayListener(IConnectController.OnMediaPlayListener onMediaPlayListener) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void registerStatusListener(IConnectController.OnStatusListener onStatusListener) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void resume() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void rotate(float f) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void seekTo(int i) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean showSlide(int i) {
        return false;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void stop() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void stopSlide() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void syncRemoteView(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void unregisterMediaPlayListener(IConnectController.OnMediaPlayListener onMediaPlayListener) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void unregisterStatusListener(IConnectController.OnStatusListener onStatusListener) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void updateCurrentFolder(IConnectController.DataSet dataSet) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void updateCurrentPhoto(String str, int i) {
    }
}
