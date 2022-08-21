package com.miui.gallery.projection;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.milink.sdk.client.PhotoCastClient;
import com.milink.sdk.photo.IPhotoCastCallback;
import com.milink.sdk.photo.IPhotoCastDataSource;
import com.milink.sdk.photo.PhotoCastCallback;
import com.milink.sdk.photo.PhotoCastDataSource;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.activity.BurstPhotoActivity;
import com.miui.gallery.activity.PhotoDetailActivity;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.projection.IConnectController;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.ProjectSlideFragment;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ThreadUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes2.dex */
public class ConnectControllerMiPlay implements IConnectController, DefaultLifecycleObserver {
    public boolean isNeedRestoreShow;
    public WeakReference<GalleryActivity> mActivity;
    public Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    public PhotoCastClient mClient;
    public IConnectController.DataSet mMediaSet;
    public volatile int mSlidePossiblePrePos;
    public String mCurrentPhoto = "";
    public int mCurrentIndex = 0;
    public volatile int mSlidePossibleNextPos = 0;
    public float mLastPhotoScale = 0.0f;
    public float mLastPhotoCx = 0.0f;
    public float mLastPhotoCy = 0.0f;
    public long mLastPhotoScaleTime = 0;
    public float mLastRotateDegree = 0.0f;
    public boolean mNeedStopPhotoCastOnStop = true;
    public boolean mSupport = false;
    public volatile boolean mIsPhotoCastOnShow = false;
    public boolean mIsActivityOnStart = false;
    public volatile boolean mIsShowSlide = false;
    public boolean mIsVideoPlaying = false;
    public IPhotoCastCallback mCallback = new PhotoCastCallback() { // from class: com.miui.gallery.projection.ConnectControllerMiPlay.1
        {
            ConnectControllerMiPlay.this = this;
        }

        @Override // com.milink.sdk.photo.IPhotoCastCallback
        public void onInit() throws RemoteException {
            DefaultLogger.i("project_ConnectControllerMiPlay", "onInit");
            ConnectControllerMiPlay.this.refreshSupport();
            ConnectControllerMiPlay.this.show();
        }

        @Override // com.milink.sdk.photo.IPhotoCastCallback
        public void onEnd() throws RemoteException {
            DefaultLogger.i("project_ConnectControllerMiPlay", "onEnd");
            ConnectControllerMiPlay.this.mSupport = false;
            ConnectControllerMiPlay.this.stopSlide();
            ConnectControllerMiPlay.this.stop();
        }

        @Override // com.milink.sdk.photo.IPhotoCastCallback
        public void onError(int i) throws RemoteException {
            DefaultLogger.i("project_ConnectControllerMiPlay", "onError");
            ConnectControllerMiPlay.this.mSupport = false;
            ConnectControllerMiPlay.this.release();
        }

        @Override // com.milink.sdk.photo.IPhotoCastCallback
        public void onDisplayNum(int i) throws RemoteException {
            DefaultLogger.i("project_ConnectControllerMiPlay", "onDisplayNum: " + i);
            if (i == 1) {
                ConnectControllerMiPlay.this.refreshSupport();
                ConnectControllerMiPlay.this.show();
                return;
            }
            ConnectControllerMiPlay.this.stop();
        }
    };
    public IPhotoCastDataSource mDataSource = new PhotoCastDataSource() { // from class: com.miui.gallery.projection.ConnectControllerMiPlay.2
        {
            ConnectControllerMiPlay.this = this;
        }

        @Override // com.milink.sdk.photo.IPhotoCastDataSource
        public String getPrevPhoto(String str, boolean z) throws RemoteException {
            DefaultLogger.i("project_ConnectControllerMiPlay", "getPrevPhoto: input=" + str + " isRecyle=" + z);
            String str2 = null;
            if (ConnectControllerMiPlay.this.mMediaSet != null && !TextUtils.isEmpty(str)) {
                int indexOfItem = ConnectControllerMiPlay.this.mMediaSet.getIndexOfItem(str, ConnectControllerMiPlay.this.mSlidePossiblePrePos) - 1;
                if (!z && indexOfItem < 0) {
                    ConnectControllerMiPlay.this.mSlidePossiblePrePos = indexOfItem + 1;
                } else {
                    ConnectControllerMiPlay connectControllerMiPlay = ConnectControllerMiPlay.this;
                    indexOfItem = (connectControllerMiPlay.mMediaSet.getCount() + indexOfItem) % ConnectControllerMiPlay.this.mMediaSet.getCount();
                    connectControllerMiPlay.mSlidePossiblePrePos = indexOfItem;
                    BaseDataItem item = ConnectControllerMiPlay.this.mMediaSet.getItem(null, indexOfItem);
                    if (item != null) {
                        str2 = item.getPathDisplayBetter();
                    }
                }
                DefaultLogger.i("project_ConnectControllerMiPlay", "getPrevPhoto: pre=" + str2 + " index=" + indexOfItem);
            }
            return str2;
        }

        @Override // com.milink.sdk.photo.IPhotoCastDataSource
        public String getNextPhoto(String str, boolean z) throws RemoteException {
            DefaultLogger.i("project_ConnectControllerMiPlay", "getNextPhoto: input=" + str + " isRecyle=" + z);
            String str2 = null;
            if (ConnectControllerMiPlay.this.mMediaSet != null && !TextUtils.isEmpty(str)) {
                int indexOfItem = ConnectControllerMiPlay.this.mMediaSet.getIndexOfItem(str, ConnectControllerMiPlay.this.mSlidePossibleNextPos) + 1;
                if (!z && indexOfItem >= ConnectControllerMiPlay.this.mMediaSet.getCount()) {
                    ConnectControllerMiPlay.this.mSlidePossibleNextPos = indexOfItem - 1;
                } else {
                    ConnectControllerMiPlay connectControllerMiPlay = ConnectControllerMiPlay.this;
                    indexOfItem %= connectControllerMiPlay.mMediaSet.getCount();
                    connectControllerMiPlay.mSlidePossibleNextPos = indexOfItem;
                    BaseDataItem item = ConnectControllerMiPlay.this.mMediaSet.getItem(null, indexOfItem);
                    if (item != null) {
                        str2 = item.getPathDisplayBetter();
                    }
                }
                DefaultLogger.i("project_ConnectControllerMiPlay", "getNextPhoto: next=" + str2 + " index=" + indexOfItem);
            }
            return str2;
        }
    };

    /* renamed from: $r8$lambda$1D453lJn0pzJGdJkIgo-OKCtqWc */
    public static /* synthetic */ Object m1184$r8$lambda$1D453lJn0pzJGdJkIgoOKCtqWc(ConnectControllerMiPlay connectControllerMiPlay) {
        return connectControllerMiPlay.lambda$refreshSupport$0();
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void closeService() {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public int getConnectStatus() {
        return 0;
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
    public void seekTo(int i) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void unregisterMediaPlayListener(IConnectController.OnMediaPlayListener onMediaPlayListener) {
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void unregisterStatusListener(IConnectController.OnStatusListener onStatusListener) {
    }

    public ConnectControllerMiPlay(GalleryActivity galleryActivity) {
        this.mActivity = new WeakReference<>(galleryActivity);
        init();
    }

    public void init() {
        PhotoCastClient photoCastClient = new PhotoCastClient(GalleryApp.sGetAndroidContext());
        this.mClient = photoCastClient;
        photoCastClient.setDataSource(this.mDataSource);
        this.mClient.init(this.mCallback);
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.projection.ConnectControllerMiPlay.3
            {
                ConnectControllerMiPlay.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ConnectControllerMiPlay.this.mActivity == null || ConnectControllerMiPlay.this.mActivity.get() == null) {
                    return;
                }
                ((GalleryActivity) ConnectControllerMiPlay.this.mActivity.get()).getLifecycle().addObserver(ConnectControllerMiPlay.this);
            }
        });
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStart(LifecycleOwner lifecycleOwner) {
        DefaultLogger.i("project_ConnectControllerMiPlay", "onStart: ");
        this.mIsActivityOnStart = true;
        refreshSupport();
        show();
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onResume(LifecycleOwner lifecycleOwner) {
        DefaultLogger.i("project_ConnectControllerMiPlay", "onResume: ");
        refreshSupport();
        show();
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onPause(LifecycleOwner lifecycleOwner) {
        DefaultLogger.i("project_ConnectControllerMiPlay", "onPause: ");
        if (this.mActivityLifecycleCallbacks == null) {
            this.mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() { // from class: com.miui.gallery.projection.ConnectControllerMiPlay.4
                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityCreated(Activity activity, Bundle bundle) {
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity) {
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStarted(Activity activity) {
                }

                {
                    ConnectControllerMiPlay.this = this;
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityResumed(Activity activity) {
                    if (ConnectControllerMiPlay.this.isNeedStopActivity(activity)) {
                        ConnectControllerMiPlay.this.mNeedStopPhotoCastOnStop = false;
                    }
                    if (!(activity instanceof PhotoDetailActivity) || !ConnectControllerMiPlay.this.isNeedRestoreShow || ConnectControllerMiPlay.this.mActivity == null || ConnectControllerMiPlay.this.mActivity.get() == null) {
                        return;
                    }
                    ConnectControllerMiPlay connectControllerMiPlay = ConnectControllerMiPlay.this;
                    connectControllerMiPlay.onResume((LifecycleOwner) connectControllerMiPlay.mActivity.get());
                    ConnectControllerMiPlay.this.isNeedRestoreShow = false;
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityPaused(Activity activity) {
                    if (ConnectControllerMiPlay.this.isNeedStopActivity(activity)) {
                        ConnectControllerMiPlay.this.mNeedStopPhotoCastOnStop = true;
                    }
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStopped(Activity activity) {
                    if (!(activity instanceof PhotoDetailActivity) || MiscUtil.isAppProcessInForeground() || ConnectControllerMiPlay.this.mActivity == null || ConnectControllerMiPlay.this.mActivity.get() == null) {
                        return;
                    }
                    ConnectControllerMiPlay.this.mNeedStopPhotoCastOnStop = true;
                    ConnectControllerMiPlay connectControllerMiPlay = ConnectControllerMiPlay.this;
                    connectControllerMiPlay.onStop((LifecycleOwner) connectControllerMiPlay.mActivity.get());
                    ConnectControllerMiPlay.this.isNeedRestoreShow = true;
                }
            };
            WeakReference<GalleryActivity> weakReference = this.mActivity;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.mActivity.get().getApplication().registerActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
        }
    }

    public final boolean isNeedStopActivity(Activity activity) {
        return (activity instanceof PhotoDetailActivity) || (activity instanceof BurstPhotoActivity);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
        if (this.mNeedStopPhotoCastOnStop) {
            DefaultLogger.i("project_ConnectControllerMiPlay", "onStop: ");
            this.mIsActivityOnStart = false;
            stop();
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        DefaultLogger.i("project_ConnectControllerMiPlay", "onDestroy: ");
        release();
        WeakReference<GalleryActivity> weakReference = this.mActivity;
        if (weakReference != null && weakReference.get() != null) {
            this.mActivity.get().getApplication().unregisterActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
            this.mActivity.get().getLifecycle().removeObserver(this);
            this.mActivity.clear();
            this.mActivity = null;
        }
        if (this.mMediaSet != null) {
            this.mMediaSet = null;
        }
    }

    public final void show() {
        if (!this.mSupport || !this.mIsActivityOnStart || TextUtils.isEmpty(this.mCurrentPhoto) || isPlaying()) {
            return;
        }
        DefaultLogger.i("project_ConnectControllerMiPlay", "show: " + this.mCurrentPhoto + " mCurrentIndex=" + this.mCurrentIndex);
        this.mClient.show(this.mCurrentPhoto);
        this.mIsPhotoCastOnShow = true;
        float f = this.mLastRotateDegree;
        if (f <= 0.0f) {
            return;
        }
        rotate(f);
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void stop() {
        WeakReference<GalleryActivity> weakReference;
        if (!this.mNeedStopPhotoCastOnStop || (weakReference = this.mActivity) == null || weakReference.get() == null) {
            return;
        }
        ProjectSlideFragment projectSlideFragment = (ProjectSlideFragment) this.mActivity.get().getSupportFragmentManager().findFragmentByTag("ProjectSlideFragment");
        if (projectSlideFragment != null && projectSlideFragment.isVisible()) {
            projectSlideFragment.dismiss();
        }
        this.mClient.stop();
        this.mIsPhotoCastOnShow = false;
        DefaultLogger.i("project_ConnectControllerMiPlay", "stop: ");
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean isPlaying() {
        return this.mIsVideoPlaying;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void setVideoIsPlaying(boolean z) {
        this.mIsVideoPlaying = z;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void stopSlide() {
        this.mClient.stopSlide();
        this.mIsShowSlide = false;
        show();
        float f = this.mLastRotateDegree;
        if (f > 0.0f) {
            rotate(f);
        }
        DefaultLogger.i("project_ConnectControllerMiPlay", "stopSlide: ");
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void release() {
        this.mClient.release();
        DefaultLogger.i("project_ConnectControllerMiPlay", "release: ");
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void updateCurrentPhoto(String str, int i) {
        if (!TextUtils.equals(this.mCurrentPhoto, str) || i != this.mCurrentIndex) {
            this.mCurrentPhoto = str;
            this.mSlidePossibleNextPos = i;
            this.mSlidePossiblePrePos = i;
            this.mCurrentIndex = i;
            this.mLastRotateDegree = 0.0f;
            show();
        }
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void updateCurrentFolder(IConnectController.DataSet dataSet) {
        this.mMediaSet = dataSet;
    }

    public final void refreshSupport() {
        try {
            this.mSupport = ((Boolean) ThreadUtils.postOnBackgroundThread(new Callable() { // from class: com.miui.gallery.projection.ConnectControllerMiPlay$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return ConnectControllerMiPlay.m1184$r8$lambda$1D453lJn0pzJGdJkIgoOKCtqWc(ConnectControllerMiPlay.this);
                }
            }).get(200L, TimeUnit.MILLISECONDS)).booleanValue();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            DefaultLogger.i("project_ConnectControllerMiPlay", "refreshSupport err: " + e.getMessage());
            this.mSupport = false;
        }
    }

    public /* synthetic */ Object lambda$refreshSupport$0() throws Exception {
        boolean z = true;
        if (this.mClient.checkAccess() != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean isConnected() {
        return this.mSupport;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public String getConnectedDevice() {
        if (this.mSupport) {
            String castDeviceName = this.mClient.getCastDeviceName();
            DefaultLogger.i("project_ConnectControllerMiPlay", "getCastDeviceName: " + castDeviceName);
            return castDeviceName;
        }
        return "";
    }

    @Override // com.miui.gallery.projection.IConnectController
    public boolean showSlide(int i) {
        if (this.mSupport && !this.mIsShowSlide) {
            this.mSlidePossibleNextPos = i;
            this.mSlidePossiblePrePos = i;
            this.mCurrentIndex = i;
            this.mIsShowSlide = this.mClient.startSlide() > -1;
            DefaultLogger.i("project_ConnectControllerMiPlay", "startSlide: " + this.mIsShowSlide + " mCurrentIndex=" + i);
        }
        return this.mIsShowSlide;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void onMirrorResume(String str) {
        DefaultLogger.i("project_ConnectControllerMiPlay", "onMirrorResume: curPhoto->%s", str);
        this.mCurrentPhoto = str;
        refreshSupport();
        show();
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void syncRemoteView(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (!this.mSupport || !this.mIsPhotoCastOnShow || TextUtils.isEmpty(this.mCurrentPhoto)) {
            return;
        }
        if (this.mLastPhotoScale <= SearchStatUtils.POW) {
            this.mLastPhotoScale = f7;
            this.mLastPhotoCx = f;
            this.mLastPhotoCy = f2;
        }
        float abs = Math.abs(this.mLastPhotoScale - f7) / this.mLastPhotoScale;
        float abs2 = Math.abs(this.mLastPhotoCx - f) / f3;
        float abs3 = Math.abs(this.mLastPhotoCy - f2) / f4;
        if (abs <= 0.01d && abs2 <= 0.01d && abs3 <= 0.01d) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mLastPhotoScaleTime <= 0) {
            this.mLastPhotoScaleTime = currentTimeMillis;
        }
        long j = currentTimeMillis - this.mLastPhotoScaleTime;
        if (j <= 50) {
            return;
        }
        DefaultLogger.i("project_ConnectControllerMiPlay", "zoom: " + this.mCurrentPhoto + " cx:" + f + " cy:" + f2 + " sw:" + f3 + " sh:" + f4 + " iw:" + f5 + " ih:" + f6 + " s:" + f7 + " deltaTime:" + j + " deltaScaleP:" + abs);
        this.mClient.zoom(this.mCurrentPhoto, (int) f, (int) f2, (int) f3, (int) f4, (int) f5, (int) f6, f7);
        this.mLastPhotoScaleTime = currentTimeMillis;
        this.mLastPhotoScale = f7;
        this.mLastPhotoCx = f;
        this.mLastPhotoCy = f2;
    }

    @Override // com.miui.gallery.projection.IConnectController
    public void rotate(float f) {
        this.mLastRotateDegree = f;
        if (!this.mSupport || !this.mIsPhotoCastOnShow || TextUtils.isEmpty(this.mCurrentPhoto)) {
            return;
        }
        this.mClient.rotate(this.mCurrentPhoto, f);
        DefaultLogger.i("project_ConnectControllerMiPlay", "rotate: " + this.mCurrentPhoto + " miPlayDegree:" + f);
    }
}
