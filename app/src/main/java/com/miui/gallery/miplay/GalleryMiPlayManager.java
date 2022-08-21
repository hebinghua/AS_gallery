package com.miui.gallery.miplay;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;
import java.io.File;

/* loaded from: classes2.dex */
public class GalleryMiPlayManager implements DefaultLifecycleObserver {
    public static final Object mLock = new Object();
    public static volatile GalleryMiPlayManager mMiPlayManager;
    public MiplayCirculateStatusListener mCirculateStatusListener;
    public GalleryMiplayCallback mGalleryMiplayCallback;
    public Handler mHandler;
    public volatile boolean mHasStopped;
    public MiplayStatusListener mPlayStatusListener;
    public RemoteMiplayCallback mRemoteMiplayCallback;
    public final int MSG_MIPLAY_TIMEOUT = 10;
    public final long TIMEOUT_WAIT_TIME = 5000;
    public volatile int mInitStatus = -1;
    public GalleryMiPlayClient mMiPlayClient = GalleryMiPlayClient.getInstance();

    public static GalleryMiPlayManager getInstance() {
        if (mMiPlayManager == null) {
            synchronized (mLock) {
                if (mMiPlayManager == null) {
                    mMiPlayManager = new GalleryMiPlayManager();
                }
            }
        }
        return mMiPlayManager;
    }

    private GalleryMiPlayManager() {
        if (this.mRemoteMiplayCallback == null) {
            this.mRemoteMiplayCallback = new RemoteMiplayCallback();
        }
        if (this.mGalleryMiplayCallback == null) {
            this.mGalleryMiplayCallback = new GalleryMiplayCallback() { // from class: com.miui.gallery.miplay.GalleryMiPlayManager.1
                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onInitSuccess() {
                    GalleryMiPlayManager.this.mInitStatus = 0;
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onInitError() {
                    GalleryMiPlayManager.this.mInitStatus = -1;
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onCirculatePreparing(MediaMetaData mediaMetaData) {
                    if (GalleryMiPlayManager.this.mCirculateStatusListener != null) {
                        GalleryMiPlayManager.this.mCirculateStatusListener.onCirculatePreparing(mediaMetaData);
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onPlayed() {
                    if (GalleryMiPlayManager.this.mHandler != null) {
                        GalleryMiPlayManager.this.mHandler.removeMessages(10);
                    }
                    if (GalleryMiPlayManager.this.mCirculateStatusListener != null) {
                        GalleryMiPlayManager.this.mCirculateStatusListener.onCirculateStart();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onCirculateFail(boolean z) {
                    DefaultLogger.d("GalleryMiPlayManager", "onCirculateFail: timeout->%s", Boolean.valueOf(z));
                    if (GalleryMiPlayManager.this.mHandler != null) {
                        GalleryMiPlayManager.this.mHandler.removeMessages(10);
                    }
                    if (GalleryMiPlayManager.this.mCirculateStatusListener != null) {
                        GalleryMiPlayManager.this.mCirculateStatusListener.onCirculateFailed();
                        GalleryMiPlayManager.this.mCirculateStatusListener = null;
                    }
                    if (z) {
                        GalleryMiPlayManager.this.cancelCirculate();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onResumeMirrorSuccess() {
                    DefaultLogger.d("GalleryMiPlayManager", "onResumeMirrorSuccess");
                    if (GalleryMiPlayManager.this.mCirculateStatusListener != null) {
                        GalleryMiPlayManager.this.mCirculateStatusListener.onMirrorResumed();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onPositionChanged(long j) {
                    if (GalleryMiPlayManager.this.mPlayStatusListener != null) {
                        GalleryMiPlayManager.this.mPlayStatusListener.notifyPositionChange(j);
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onStopPlay() {
                    GalleryMiPlayManager.this.mHasStopped = true;
                    if (GalleryMiPlayManager.this.mPlayStatusListener != null) {
                        GalleryMiPlayManager.this.mPlayStatusListener.onStopPlay();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onVideoEnd() {
                    GalleryMiPlayManager.this.mHasStopped = true;
                    if (GalleryMiPlayManager.this.mPlayStatusListener != null) {
                        GalleryMiPlayManager.this.mPlayStatusListener.onVideoEnd();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onPause() {
                    if (GalleryMiPlayManager.this.mPlayStatusListener != null) {
                        GalleryMiPlayManager.this.mPlayStatusListener.onPause();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onResumed() {
                    if (GalleryMiPlayManager.this.mPlayStatusListener != null) {
                        GalleryMiPlayManager.this.mPlayStatusListener.onResume();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onSeekDoneNotify() {
                    if (GalleryMiPlayManager.this.mPlayStatusListener != null) {
                        GalleryMiPlayManager.this.mPlayStatusListener.notifySeekDone();
                    }
                }

                @Override // com.miui.gallery.miplay.GalleryMiplayCallback
                public void onVolumeChange(double d) {
                    if (GalleryMiPlayManager.this.mPlayStatusListener != null) {
                        GalleryMiPlayManager.this.mPlayStatusListener.onVolumeChange((long) d);
                    }
                }
            };
        }
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.miui.gallery.miplay.GalleryMiPlayManager.2
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    if (message.what == 10 && GalleryMiPlayManager.this.mGalleryMiplayCallback != null) {
                        GalleryMiPlayManager.this.mGalleryMiplayCallback.onCirculateFail(true);
                    }
                    super.handleMessage(message);
                }
            };
        }
        this.mRemoteMiplayCallback.setRemoteMiplayCallback(this.mGalleryMiplayCallback);
    }

    public void init() {
        if (this.mInitStatus == -2 || this.mMiPlayClient == null || this.mRemoteMiplayCallback == null) {
            DefaultLogger.d("GalleryMiPlayManager", "miplay is not support: status->%s", Integer.valueOf(this.mInitStatus));
        } else if (this.mInitStatus == 0) {
            DefaultLogger.d("GalleryMiPlayManager", "miplay has been init, it is unnecessary to init again");
        } else {
            boolean initAsync = this.mMiPlayClient.initAsync(this.mRemoteMiplayCallback);
            DefaultLogger.d("GalleryMiPlayManager", "start init:%s", Boolean.valueOf(initAsync));
            if (initAsync) {
                return;
            }
            this.mInitStatus = -2;
        }
    }

    public void play(BaseDataItem baseDataItem, long j, long j2) {
        int i;
        MediaMetaData mediaMetaData = new MediaMetaData();
        if (this.mMiPlayClient == null || baseDataItem == null) {
            i = -1;
        } else {
            String originalPath = baseDataItem.getOriginalPath();
            mediaMetaData.setTitle(TextUtils.isEmpty(baseDataItem.getTitle()) ? BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(originalPath)) : baseDataItem.getTitle());
            mediaMetaData.setDuration(Math.max(baseDataItem.getDuration() * 1000, j));
            mediaMetaData.setPosition(j2 >= j ? 0L : j2);
            mediaMetaData.setUrl(Uri.fromFile(new File(originalPath)).toString());
            i = this.mMiPlayClient.play(mediaMetaData);
            DefaultLogger.d("GalleryMiPlayManager", "start play: result->%s, totalDuration->%s, curPosition->%s", Integer.valueOf(i), Long.valueOf(j), Long.valueOf(j2));
            this.mHasStopped = false;
        }
        if (i == -1) {
            GalleryMiplayCallback galleryMiplayCallback = this.mGalleryMiplayCallback;
            if (galleryMiplayCallback == null) {
                return;
            }
            galleryMiplayCallback.onCirculateFail(false);
        } else if (i != 0) {
        } else {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendEmptyMessageDelayed(10, 5000L);
            }
            GalleryMiplayCallback galleryMiplayCallback2 = this.mGalleryMiplayCallback;
            if (galleryMiplayCallback2 == null) {
                return;
            }
            galleryMiplayCallback2.onCirculatePreparing(mediaMetaData);
        }
    }

    public void setVolume(int i) {
        GalleryMiPlayClient galleryMiPlayClient = this.mMiPlayClient;
        if (galleryMiPlayClient != null) {
            galleryMiPlayClient.setVolume(i);
        }
    }

    public int getVolume() {
        GalleryMiPlayClient galleryMiPlayClient = this.mMiPlayClient;
        if (galleryMiPlayClient != null) {
            return galleryMiPlayClient.getVolume();
        }
        return 0;
    }

    public boolean hasInit() {
        DefaultLogger.d("GalleryMiPlayManager", "hasInit: mMiPlayCallback->%s, status->%s", Boolean.valueOf(this.mRemoteMiplayCallback == null), Integer.valueOf(this.mInitStatus));
        return this.mRemoteMiplayCallback != null && this.mInitStatus == 0;
    }

    public void stop() {
        if (this.mMiPlayClient == null || this.mHasStopped) {
            return;
        }
        DefaultLogger.d("GalleryMiPlayManager", "notify sdk stop");
        this.mHasStopped = true;
        this.mMiPlayClient.stop();
    }

    public final void quit() {
        if (this.mMiPlayClient != null && this.mInitStatus == 0) {
            this.mMiPlayClient.unInit();
        }
        this.mInitStatus = -1;
        this.mHandler = null;
        this.mRemoteMiplayCallback = null;
        this.mGalleryMiplayCallback = null;
        this.mMiPlayClient = null;
    }

    public void seek(long j) {
        GalleryMiPlayClient galleryMiPlayClient = this.mMiPlayClient;
        if (galleryMiPlayClient != null) {
            galleryMiPlayClient.seek(j);
        }
    }

    public void changePlayStatus(int i) {
        GalleryMiPlayClient galleryMiPlayClient = this.mMiPlayClient;
        if (galleryMiPlayClient == null) {
            return;
        }
        if (i == 1) {
            galleryMiPlayClient.pause();
        } else if (i != 0) {
        } else {
            galleryMiPlayClient.resume();
        }
    }

    public void registerMiplayStatusListener(MiplayStatusListener miplayStatusListener) {
        this.mPlayStatusListener = miplayStatusListener;
    }

    public void registerMiplayCirculateStatusListener(MiplayCirculateStatusListener miplayCirculateStatusListener) {
        this.mCirculateStatusListener = miplayCirculateStatusListener;
    }

    public final void cancelCirculate() {
        GalleryMiPlayClient galleryMiPlayClient = this.mMiPlayClient;
        if (galleryMiPlayClient != null) {
            DefaultLogger.d("GalleryMiPlayManager", "notify sdk to cancel: result->%s", Integer.valueOf(galleryMiPlayClient.cancelCirculate()));
        }
    }

    public boolean checkMiplayCondition() {
        if (this.mMiPlayClient == null || this.mRemoteMiplayCallback == null || this.mInitStatus != 0) {
            return false;
        }
        int circulateMode = this.mMiPlayClient.getCirculateMode();
        DefaultLogger.d("GalleryMiPlayManager", "getCirculateMode->%s", Integer.valueOf(circulateMode));
        return circulateMode == 0 || circulateMode == 1;
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
        DefaultLogger.d("GalleryMiPlayManager", "onAppStop");
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        DefaultLogger.d("GalleryMiPlayManager", "onAppDestroy");
        quit();
    }
}
