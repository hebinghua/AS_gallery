package com.miui.gallery.miplay;

import android.os.RemoteException;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.miplay.phoneclientsdk.external.MiplayClientCallback;

/* loaded from: classes2.dex */
public class RemoteMiplayCallback extends MiplayClientCallback {
    public GalleryMiplayCallback mGalleryMiplayCallback;

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onBuffering() {
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onNext() {
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onNotifyPropertiesInfo(String str) {
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onPrev() {
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    @Deprecated
    public int onSeekedTo(long j) {
        return 0;
    }

    public void setRemoteMiplayCallback(GalleryMiplayCallback galleryMiplayCallback) {
        this.mGalleryMiplayCallback = galleryMiplayCallback;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onInitSuccess() {
        DefaultLogger.d("MiPlayCallback", "init success");
        this.mGalleryMiplayCallback.onInitSuccess();
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onInitError() {
        DefaultLogger.d("MiPlayCallback", "init fail");
        this.mGalleryMiplayCallback.onInitError();
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onPositionChanged(long j) {
        DefaultLogger.d("MiPlayCallback", "onPositionChanged: pos->%s", Long.valueOf(j));
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onPositionChanged(j);
            return 0;
        }
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onPlayed() {
        DefaultLogger.d("MiPlayCallback", "onPlayed callback");
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onPlayed();
            return 0;
        }
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onStopped(int i) {
        GalleryMiplayCallback galleryMiplayCallback;
        DefaultLogger.d("MiPlayCallback", "onStopped: status->%s", Integer.valueOf(i));
        if (i != 0) {
            if (i != 1 || (galleryMiplayCallback = this.mGalleryMiplayCallback) == null) {
                return 0;
            }
            galleryMiplayCallback.onVideoEnd();
            return 0;
        }
        GalleryMiplayCallback galleryMiplayCallback2 = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback2 == null) {
            return 0;
        }
        galleryMiplayCallback2.onStopPlay();
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onCirculateFail(String str) {
        DefaultLogger.d("MiPlayCallback", "onCirculateFail callback");
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onCirculateFail(false);
        }
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onPaused() {
        DefaultLogger.d("MiPlayCallback", "onPause");
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onPause();
            return 0;
        }
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onResumed() throws RemoteException {
        DefaultLogger.d("MiPlayCallback", "onResumed");
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onResumed();
            return 0;
        }
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onSeekDoneNotify() {
        DefaultLogger.d("MiPlayCallback", "onSeekDoneNotify");
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onSeekDoneNotify();
            return 0;
        }
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onVolumeChange(double d) {
        DefaultLogger.d("MiPlayCallback", "onVolumeChange->%s", Double.valueOf(d));
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onVolumeChange(d);
        }
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onCirculateModeChange(int i) {
        DefaultLogger.d("MiPlayCallback", "onCirculateModeChange: mode->%s", Integer.valueOf(i));
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onResumeMirrorSuccess() {
        DefaultLogger.d("MiPlayCallback", "onResumeMirrorSuccess");
        GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
        if (galleryMiplayCallback != null) {
            galleryMiplayCallback.onResumeMirrorSuccess();
        }
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onResumeMirrorFail() {
        DefaultLogger.d("MiPlayCallback", "onResumeMirrorFail");
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public void onConnectMirrorSuccess() {
        DefaultLogger.d("MiPlayCallback", "onConnectMirrorSuccess");
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onCirculateStart() {
        DefaultLogger.d("MiPlayCallback", "onCirculateStart callback");
        return 0;
    }

    @Override // com.xiaomi.miplay.phoneclientsdk.external.IMiPlayExternalClientCallback
    public int onCirculateEnd() {
        DefaultLogger.d("MiPlayCallback", "onCirculateEnd callback");
        return 0;
    }
}
