package com.miui.gallery.projection;

import android.app.Activity;
import android.text.TextUtils;
import com.milink.api.v1.MilinkClientManager;
import com.milink.api.v1.MilinkClientManagerDataSource;
import com.milink.api.v1.MilinkClientManagerDelegate;
import com.milink.api.v1.type.DeviceType;
import com.milink.api.v1.type.ErrorCode;
import com.milink.api.v1.type.MediaType;
import com.milink.api.v1.type.ReturnCode;
import com.milink.api.v1.type.SlideMode;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.projection.IConnectController;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public abstract class ConnectController implements IConnectController {
    public Map.Entry<String, String> mConnectedDevice;
    public Map.Entry<String, String> mConnectingDevice;
    public IConnectController.OnMediaPlayListener mMediaPlayListener;
    public MilinkClientManager mPhotoManager;
    public IConnectController.OnStatusListener mStatusListener;
    public Map.Entry<String, String> mToConnectDevice;
    public SlidingWindow mSlidingWindow = new SlidingWindow();
    public MilinkClientManagerDataSource mDataSource = new MilinkClientManagerDataSource() { // from class: com.miui.gallery.projection.ConnectController.1
        @Override // com.milink.api.v1.MilinkClientManagerDataSource
        public String getPrevPhoto(String str, boolean z) {
            return ConnectController.this.mSlidingWindow.getPrevious(str, z);
        }

        @Override // com.milink.api.v1.MilinkClientManagerDataSource
        public String getNextPhoto(String str, boolean z) {
            return ConnectController.this.mSlidingWindow.getNext(str, z);
        }
    };
    public String mCurrentPhoto = "";
    public int mCurrentIndex = 0;
    public final Object mStatusLock = new Object();
    public boolean mIsOpen = false;
    public AtomicBoolean mConnectTillOpen = new AtomicBoolean(false);
    public MilinkClientManagerDelegate mDelegate = new MilinkClientManagerDelegate() { // from class: com.miui.gallery.projection.ConnectController.2
        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onVolume(int i) {
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onOpen() {
            DefaultLogger.v("project_IConnectController", "service openned");
            ConnectController.this.setIsOpen(true);
            if (ConnectController.this.mConnectTillOpen.get()) {
                ConnectController.this.mConnectTillOpen.set(false);
                ConnectController.this.connectRenderer();
            }
            ConnectController.this.doOnOpen();
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onClose() {
            DefaultLogger.v("project_IConnectController", "service closed");
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onDeviceFound(String str, String str2, DeviceType deviceType) {
            DefaultLogger.v("project_IConnectController", "service onDeviceFound");
            ConnectController.this.doOnDeviceFound(str, str2, deviceType);
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onDeviceLost(String str) {
            DefaultLogger.v("project_IConnectController", "service onDeviceLost");
            ConnectController.this.doOnDeviceLost(str);
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onConnected() {
            DefaultLogger.d("project_IConnectController", "service onConnected");
            ConnectController.this.setIsConnected(true);
            ConnectController.this.doOnConnected();
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onConnectedFailed(ErrorCode errorCode) {
            DefaultLogger.d("project_IConnectController", "service onConnectedFailed");
            ConnectController.this.setIsConnected(false);
            ConnectController.this.doOnConnectedFail();
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onDisconnected() {
            DefaultLogger.d("project_IConnectController", "service onDisconnected");
            ConnectController.this.setIsConnected(false);
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onLoading() {
            DefaultLogger.v("project_IConnectController", "loading...");
            if (ConnectController.this.mMediaPlayListener != null) {
                ConnectController.this.mMediaPlayListener.onLoading();
            }
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onPlaying() {
            DefaultLogger.v("project_IConnectController", "playing...");
            if (ConnectController.this.mMediaPlayListener != null) {
                ConnectController.this.mMediaPlayListener.onPlaying();
            }
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onStopped() {
            DefaultLogger.v("project_IConnectController", "stopped");
            if (ConnectController.this.mMediaPlayListener != null) {
                ConnectController.this.mMediaPlayListener.onStopped();
            }
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onPaused() {
            DefaultLogger.v("project_IConnectController", "paused");
            if (ConnectController.this.mMediaPlayListener != null) {
                ConnectController.this.mMediaPlayListener.onPaused();
            }
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onNextAudio(boolean z) {
            DefaultLogger.v("project_IConnectController", "onRequestNextItem: " + z);
        }

        @Override // com.milink.api.v1.MilinkClientManagerDelegate
        public void onPrevAudio(boolean z) {
            DefaultLogger.v("project_IConnectController", "onRequestPrevItem: " + z);
        }
    };

    public void attachActivity(Activity activity) {
    }

    public abstract boolean chooseDevice();

    public void detachActivity(Activity activity) {
    }

    public abstract void doOnDeviceFound(String str, String str2, DeviceType deviceType);

    public abstract void doOnDeviceLost(String str);

    public abstract void doOnOpen();

    @Override // com.miui.gallery.projection.IConnectController
    public void rotate(float f) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void stopSlide() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void project() {
        if (isConnected()) {
            disconnectRenderer();
            return;
        }
        if (!isOpen()) {
            open();
        }
        chooseDevice();
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void release() {
        SlidingWindow slidingWindow = this.mSlidingWindow;
        if (slidingWindow != null) {
            slidingWindow.releaseData();
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean isConnected() {
        boolean z;
        synchronized (this.mStatusLock) {
            z = this.mConnectedDevice != null;
        }
        return z;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public int getConnectStatus() {
        synchronized (this.mStatusLock) {
            if (this.mConnectedDevice != null) {
                return 1;
            }
            return this.mConnectingDevice != null ? 0 : -1;
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void closeService() {
        setIsConnected(false);
        setIsOpen(false);
        this.mMediaPlayListener = null;
        this.mStatusListener = null;
        disconnectRenderer();
        synchronized (this) {
            if (this.mPhotoManager != null) {
                DefaultLogger.v("project_IConnectController", "close milink service");
                this.mPhotoManager.setDelegate(null);
                this.mPhotoManager.close();
                this.mPhotoManager = null;
            }
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void updateCurrentPhoto(String str, int i) {
        this.mCurrentPhoto = str;
        this.mCurrentIndex = i;
        if (!TextUtils.isEmpty(getConnectedDevice())) {
            this.mSlidingWindow.onCurrentIndexChanged(this.mCurrentIndex);
            showPhoto(str, i);
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void updateCurrentFolder(IConnectController.DataSet dataSet) {
        this.mSlidingWindow.setMediaSet(dataSet);
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void registerStatusListener(IConnectController.OnStatusListener onStatusListener) {
        if (onStatusListener != null) {
            this.mStatusListener = onStatusListener;
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void unregisterStatusListener(IConnectController.OnStatusListener onStatusListener) {
        if (onStatusListener == this.mStatusListener) {
            this.mStatusListener = null;
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void registerMediaPlayListener(IConnectController.OnMediaPlayListener onMediaPlayListener) {
        if (onMediaPlayListener != null) {
            this.mMediaPlayListener = onMediaPlayListener;
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void unregisterMediaPlayListener(IConnectController.OnMediaPlayListener onMediaPlayListener) {
        if (onMediaPlayListener == this.mMediaPlayListener) {
            this.mMediaPlayListener = null;
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public String getConnectingDevice() {
        Map.Entry<String, String> entry = this.mConnectingDevice;
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    @Override // com.miui.gallery.projection.IConnectController
    public String getConnectedDevice() {
        Map.Entry<String, String> entry = this.mConnectedDevice;
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    public void showPhoto(String str, int i) {
        if (!isConnected()) {
            DefaultLogger.v("project_IConnectController", "no renderer connected");
            return;
        }
        DefaultLogger.v("project_IConnectController", "the photo is: %s %d ", str, Integer.valueOf(i));
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            if (this.mPhotoManager.show(str) == ReturnCode.OK) {
                return;
            }
            doOnShowError();
        } catch (IllegalArgumentException e) {
            DefaultLogger.v("project_IConnectController", "MilinkClientManager show exception", e);
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void syncRemoteView(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.mPhotoManager.zoomPhoto(this.mCurrentPhoto, (int) f, (int) f2, (int) f3, (int) f4, (int) f5, (int) f6, f7);
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean showSlide(int i) {
        this.mCurrentIndex = i;
        this.mSlidingWindow.onCurrentIndexChanged(i);
        if (this.mPhotoManager.startSlideshow(Math.max(3000, GalleryPreferences.SlideShow.getSlideShowInterval() * 1000), SlideMode.Recyle) != ReturnCode.OK) {
            doOnShowError();
            return false;
        }
        return true;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void playVideo(String str, String str2) {
        if (this.mPhotoManager != null && !TextUtils.isEmpty(str)) {
            try {
                this.mPhotoManager.startPlay(URLEncoder.encode(str, Keyczar.DEFAULT_ENCODING), str2, 0, SearchStatUtils.POW, MediaType.Video);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public int getCurrentPosition() {
        MilinkClientManager milinkClientManager = this.mPhotoManager;
        if (milinkClientManager != null) {
            return milinkClientManager.getPlaybackProgress();
        }
        return 0;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public int getDuration() {
        MilinkClientManager milinkClientManager = this.mPhotoManager;
        if (milinkClientManager != null) {
            return milinkClientManager.getPlaybackDuration();
        }
        return 0;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void seekTo(int i) {
        MilinkClientManager milinkClientManager = this.mPhotoManager;
        if (milinkClientManager != null) {
            milinkClientManager.setPlaybackProgress(i);
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void pause() {
        MilinkClientManager milinkClientManager = this.mPhotoManager;
        if (milinkClientManager != null) {
            milinkClientManager.setPlaybackRate(0);
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void resume() {
        MilinkClientManager milinkClientManager = this.mPhotoManager;
        if (milinkClientManager != null) {
            milinkClientManager.setPlaybackRate(1);
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void stop() {
        MilinkClientManager milinkClientManager = this.mPhotoManager;
        if (milinkClientManager != null) {
            milinkClientManager.stopPlay();
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean isPlaying() {
        MilinkClientManager milinkClientManager = this.mPhotoManager;
        return milinkClientManager != null && milinkClientManager.getPlaybackRate() == 1;
    }

    public void setToConnectDevice(String str, String str2) {
        synchronized (this.mStatusLock) {
            DefaultLogger.d("project_IConnectController", "set toconnectdevice");
            this.mToConnectDevice = new AbstractMap.SimpleEntry(str, str2);
            connectRenderer();
        }
    }

    public void open() {
        synchronized (this) {
            if (isOpen()) {
                return;
            }
            if (this.mPhotoManager == null) {
                if (this.mDelegate == null) {
                    return;
                }
                MilinkClientManager milinkClientManager = new MilinkClientManager(GalleryApp.sGetAndroidContext());
                this.mPhotoManager = milinkClientManager;
                milinkClientManager.setDeviceName(BuildUtil.getDeviceName(GalleryApp.sGetAndroidContext()));
                this.mPhotoManager.setDataSource(this.mDataSource);
                this.mPhotoManager.setDelegate(this.mDelegate);
            }
            this.mPhotoManager.open();
        }
    }

    public void doOnConnected() {
        DefaultLogger.d("project_IConnectController", "connect is responded ok");
        this.mPhotoManager.startShow();
        if (!TextUtils.isEmpty(this.mCurrentPhoto)) {
            DefaultLogger.v("project_IConnectController", "==the to show photo is: " + this.mCurrentPhoto);
            showPhoto(this.mCurrentPhoto, this.mCurrentIndex);
        }
        SamplingStatHelper.recordCountEvent("photo", "project_photo_success");
    }

    public void doOnConnectedFail() {
        DefaultLogger.d("project_IConnectController", "renderer is failed to connect");
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.projection_device_connection_failed);
        SamplingStatHelper.recordCountEvent("photo", "project_photo_fail");
    }

    public final void doOnShowError() {
        DefaultLogger.d("project_IConnectController", "connection is broken without informing gallery");
        closeService();
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.projection_device_connection_failed);
        SamplingStatHelper.recordCountEvent("photo", "project_photo_fail");
    }

    public final boolean connectRenderer() {
        DefaultLogger.d("project_IConnectController", "connectrender");
        if (!isOpen()) {
            DefaultLogger.e("project_IConnectController", "connect before open");
            this.mConnectTillOpen.set(true);
            return false;
        }
        String value = this.mToConnectDevice.getValue();
        if (value == null) {
            DefaultLogger.e("project_IConnectController", "device not in devices map");
        }
        if (isConnected()) {
            DefaultLogger.e("project_IConnectController", "connected to another device");
            if (!disconnectRenderer()) {
                DefaultLogger.e("project_IConnectController", "could not disconnect to previous device");
                return false;
            }
        }
        if (this.mPhotoManager.connect(value, 6000) != ReturnCode.OK) {
            DefaultLogger.e("project_IConnectController", "connect error ");
            return false;
        }
        setConnectingDevice();
        return true;
    }

    public final boolean disconnectRenderer() {
        if (this.mPhotoManager != null) {
            setIsConnected(false);
            ReturnCode stopShow = this.mPhotoManager.stopShow();
            ReturnCode returnCode = ReturnCode.OK;
            if (stopShow != returnCode) {
                DefaultLogger.w("project_IConnectController", "stop show failed");
                return false;
            } else if (this.mPhotoManager.disconnect() == returnCode) {
                return true;
            } else {
                DefaultLogger.w("project_IConnectController", "disconnect renderer failed");
                return false;
            }
        }
        return false;
    }

    public final void setConnectingDevice() {
        synchronized (this.mStatusLock) {
            Map.Entry<String, String> entry = this.mToConnectDevice;
            if (entry == null) {
                return;
            }
            this.mConnectingDevice = entry;
            this.mToConnectDevice = null;
            IConnectController.OnStatusListener onStatusListener = this.mStatusListener;
            if (onStatusListener == null) {
                return;
            }
            onStatusListener.onConnectStatusChanged(0);
        }
    }

    public final void setConnectedDevice(boolean z) {
        synchronized (this.mStatusLock) {
            if (z) {
                Map.Entry<String, String> entry = this.mConnectingDevice;
                if (entry == null) {
                    return;
                }
                this.mConnectedDevice = entry;
                this.mConnectingDevice = null;
            } else {
                this.mToConnectDevice = null;
                this.mConnectingDevice = null;
                this.mConnectedDevice = null;
            }
            IConnectController.OnStatusListener onStatusListener = this.mStatusListener;
            if (onStatusListener != null) {
                if (z) {
                    onStatusListener.onConnectStatusChanged(1);
                } else {
                    onStatusListener.onConnectStatusChanged(-1);
                }
            }
        }
    }

    public final void setIsConnected(boolean z) {
        DefaultLogger.d("project_IConnectController", "setisconnected %s", Boolean.valueOf(z));
        setConnectedDevice(z);
    }

    public final void setIsOpen(boolean z) {
        synchronized (this.mStatusLock) {
            this.mIsOpen = z;
        }
        IConnectController.OnStatusListener onStatusListener = this.mStatusListener;
        if (onStatusListener != null) {
            onStatusListener.onOpenStatusChanged(z);
        }
    }

    public final boolean isOpen() {
        boolean z;
        synchronized (this.mStatusLock) {
            z = this.mIsOpen;
        }
        return z;
    }
}
