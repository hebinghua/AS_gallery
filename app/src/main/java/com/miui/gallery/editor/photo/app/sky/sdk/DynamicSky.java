package com.miui.gallery.editor.photo.app.sky.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.magicsky.SystemUtil;
import com.xiaomi.skyprocess.EffectNotifier;
import com.xiaomi.skyprocess.MagicSky;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class DynamicSky implements IVideoExporter {
    public static final DynamicSky INSTANCE = new DynamicSky();
    public FrameCallback mFrameCallback;
    public boolean mLoaded;
    public MagicSkyProxy mMagicSkyProxy;
    public PlayCallback mPlayCallback;
    public WorkerHandler sWorkerHandler;
    public HandlerThread sWorkerThread;
    public final Object mLock = new Object();
    public int mInitCounter = 0;
    public Handler mCallbackHandler = new Handler(Looper.getMainLooper());

    /* loaded from: classes2.dex */
    public interface FrameCallback {
        void onFrameCached();

        void onReceiveFailed();
    }

    /* loaded from: classes2.dex */
    public interface PlayCallback {
        void onAudioOff();

        void onAudioOn();

        void onPause();

        void onPlay();

        void onResume();

        void onStop();
    }

    public DynamicSky() {
        HandlerThread handlerThread = new HandlerThread("dynamic-sky");
        this.sWorkerThread = handlerThread;
        handlerThread.start();
        this.sWorkerHandler = new WorkerHandler(this.sWorkerThread.getLooper());
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public boolean init() {
        boolean z;
        synchronized (this.mLock) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            if (!this.mLoaded) {
                String libraryDirPath = LibraryUtils.getLibraryDirPath(sGetAndroidContext);
                System.loadLibrary("c++_shared");
                System.load(libraryDirPath + "/libffmpeg.so");
                System.load(libraryDirPath + "/libsegment.so");
                System.load(libraryDirPath + "/libmagicsky.so");
                this.mLoaded = true;
                DefaultLogger.d("DynamicSky", "library load success");
            }
            if (this.mLoaded) {
                SystemUtil.Init(sGetAndroidContext, 50012);
            }
            z = this.mLoaded;
        }
        return z;
    }

    public void setup() {
        if (!init()) {
            return;
        }
        reset();
        MagicSkyProxy magicSkyProxy = new MagicSkyProxy(new MagicSkyCallback());
        this.mMagicSkyProxy = magicSkyProxy;
        magicSkyProxy.mMagicSky.setGLSurfaceAvalibale(true);
        int i = this.mInitCounter + 1;
        this.mInitCounter = i;
        DefaultLogger.d("DynamicSky", "setup counter %d", Integer.valueOf(i));
    }

    public final void reset() {
        this.sWorkerHandler.removeCallbacksAndMessages(null);
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.release();
        }
        this.mPlayCallback = null;
        this.mFrameCallback = null;
    }

    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public void release() {
        int i = this.mInitCounter - 1;
        this.mInitCounter = i;
        if (i == 0) {
            this.mMagicSkyProxy.mMagicSky.setGLSurfaceAvalibale(false);
            this.sWorkerHandler.sendMessage(baseMsg(110));
            this.mPlayCallback = null;
            this.mFrameCallback = null;
        }
        DefaultLogger.d("DynamicSky", "release counter %d", Integer.valueOf(this.mInitCounter));
    }

    public void setGLSurfaceView(GLSurfaceView gLSurfaceView) {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.setGLSurfaceView(gLSurfaceView);
        }
    }

    public void surfaceCreated() {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.surfaceCreated();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.setBitmap(bitmap);
        }
    }

    public void setSegmentResult(int i) {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.setSegmentResult(i);
        }
    }

    public void setPlayCallback(PlayCallback playCallback) {
        this.mPlayCallback = playCallback;
    }

    public void setFrameCallback(FrameCallback frameCallback) {
        this.mFrameCallback = frameCallback;
    }

    public void setSubTitleModel(int i, String str) {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.setSubTitleModel(i, str);
        }
    }

    public String getSubTitle() {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        return magicSkyProxy != null ? magicSkyProxy.getSubTitle() : "";
    }

    public void play(int i, String str, int i2) {
        this.sWorkerHandler.removeMessages(100);
        Message baseMsg = baseMsg(100);
        ActionInfo actionInfo = (ActionInfo) baseMsg.obj;
        actionInfo.materialId = i;
        actionInfo.materialPath = str;
        actionInfo.progress = i2;
        this.sWorkerHandler.sendMessage(baseMsg);
    }

    public void pause() {
        removePlayableMsg();
        this.sWorkerHandler.sendMessage(baseMsg(102));
    }

    public void resume() {
        removePlayableMsg();
        this.sWorkerHandler.sendMessage(baseMsg(101));
    }

    public void stop() {
        removePlayableMsg();
        this.sWorkerHandler.sendMessage(baseMsg(103));
    }

    public void audioOn() {
        removePlayableMsg();
        this.sWorkerHandler.sendMessage(baseMsg(104));
    }

    public void audioOff() {
        removePlayableMsg();
        this.sWorkerHandler.sendMessage(baseMsg(105));
    }

    public void toggleAudio() {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            if (magicSkyProxy.isAudioOn()) {
                audioOff();
            } else {
                audioOn();
            }
        }
    }

    public void applyThreshold(int i) {
        removePlayableMsg();
        Message baseMsg = baseMsg(106);
        ((ActionInfo) baseMsg.obj).progress = i;
        this.sWorkerHandler.sendMessage(baseMsg);
    }

    public final void removePlayableMsg() {
        this.sWorkerHandler.removeMessages(101);
        this.sWorkerHandler.removeMessages(102);
        this.sWorkerHandler.removeMessages(103);
        this.sWorkerHandler.removeMessages(104);
        this.sWorkerHandler.removeMessages(105);
        this.sWorkerHandler.removeMessages(106);
    }

    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public int export(String str) {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy == null) {
            return 2;
        }
        return magicSkyProxy.export(str);
    }

    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public void cancel() {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.cancel();
        }
    }

    @Override // com.miui.gallery.editor.photo.app.sky.sdk.IVideoExporter
    public void setCallback(IVideoExporter.Callback callback) {
        MagicSkyProxy magicSkyProxy = this.mMagicSkyProxy;
        if (magicSkyProxy != null) {
            magicSkyProxy.setExportCallback(callback);
        }
    }

    public final Message baseMsg(int i) {
        Message obtain = Message.obtain();
        obtain.what = i;
        obtain.obj = new ActionInfo(this.mMagicSkyProxy);
        return obtain;
    }

    public final void sendCallback(final int i) {
        this.mCallbackHandler.post(new Runnable() { // from class: com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.1
            @Override // java.lang.Runnable
            public void run() {
                if (DynamicSky.this.mPlayCallback != null) {
                    switch (i) {
                        case 100:
                            DynamicSky.this.mPlayCallback.onPlay();
                            return;
                        case 101:
                            DynamicSky.this.mPlayCallback.onResume();
                            return;
                        case 102:
                            DynamicSky.this.mPlayCallback.onPause();
                            return;
                        case 103:
                            DynamicSky.this.mPlayCallback.onStop();
                            return;
                        case 104:
                            DynamicSky.this.mPlayCallback.onAudioOn();
                            return;
                        case 105:
                            DynamicSky.this.mPlayCallback.onAudioOff();
                            return;
                        default:
                            return;
                    }
                }
                DefaultLogger.d("DynamicSky", "play callback is null");
            }
        });
    }

    /* loaded from: classes2.dex */
    public class MagicSkyCallback implements EffectNotifier {
        @Override // com.xiaomi.skyprocess.EffectNotifier
        public void OnReceiveFinish() {
        }

        @Override // com.xiaomi.skyprocess.EffectNotifier
        public void OnReceiveFrameInfo(int i, int i2) {
        }

        @Override // com.xiaomi.skyprocess.EffectNotifier
        public void onReceiveProgressPercent(int i) {
        }

        public MagicSkyCallback() {
        }

        @Override // com.xiaomi.skyprocess.EffectNotifier
        public void OnReceiveFailed() {
            DefaultLogger.d("DynamicSky", "OnReceiveFailed");
            if (DynamicSky.this.mFrameCallback != null) {
                DynamicSky.this.mFrameCallback.onReceiveFailed();
            }
        }

        @Override // com.xiaomi.skyprocess.EffectNotifier
        public void OnReceiveCacheFinished() {
            DefaultLogger.d("DynamicSky", "OnReceiveCacheFinished");
            if (DynamicSky.this.mFrameCallback != null) {
                DynamicSky.this.mFrameCallback.onFrameCached();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MagicSkyProxy magicSkyProxy;
            super.handleMessage(message);
            ActionInfo actionInfo = (ActionInfo) message.obj;
            if (actionInfo == null || (magicSkyProxy = actionInfo.magicSkyProxy) == null) {
                return;
            }
            int i = message.what;
            if (i != 110) {
                switch (i) {
                    case 100:
                        magicSkyProxy.play(actionInfo.materialId, actionInfo.materialPath, actionInfo.progress);
                        DynamicSky.this.sendCallback(100);
                        return;
                    case 101:
                        magicSkyProxy.resume();
                        DynamicSky.this.sendCallback(101);
                        return;
                    case 102:
                        magicSkyProxy.pause();
                        DynamicSky.this.sendCallback(102);
                        return;
                    case 103:
                        magicSkyProxy.stop();
                        DynamicSky.this.sendCallback(103);
                        return;
                    case 104:
                        magicSkyProxy.audioOn();
                        DynamicSky.this.sendCallback(104);
                        return;
                    case 105:
                        magicSkyProxy.audioOff();
                        DynamicSky.this.sendCallback(105);
                        return;
                    case 106:
                        magicSkyProxy.applyThreshold(actionInfo.progress);
                        return;
                    default:
                        return;
                }
            }
            magicSkyProxy.release();
        }
    }

    /* loaded from: classes2.dex */
    public static class ActionInfo {
        public MagicSkyProxy magicSkyProxy;
        public int materialId;
        public String materialPath;
        public int progress;

        public ActionInfo(MagicSkyProxy magicSkyProxy) {
            this.magicSkyProxy = magicSkyProxy;
        }
    }

    /* loaded from: classes2.dex */
    public static class MagicSkyProxy {
        public Bitmap mBitmap;
        public volatile boolean mComposing;
        public IVideoExporter.Callback mExportCallback;
        public MagicSky mMagicSky;
        public volatile int mStatus;
        public volatile boolean mAudioOn = true;
        public AtomicBoolean mCancel = new AtomicBoolean();
        public AtomicBoolean mExporting = new AtomicBoolean();
        public Handler mHandler = new Handler(Looper.getMainLooper());
        public CountDownLatch mSurfaceLatch = new CountDownLatch(1);
        public int mSegmentType = -1;
        public final Object mPlayLock = new Object();
        public String mSubtitle = "LOVE";
        public Runnable mPullProgress = new Runnable() { // from class: com.miui.gallery.editor.photo.app.sky.sdk.DynamicSky.MagicSkyProxy.1
            @Override // java.lang.Runnable
            public void run() {
                if (!MagicSkyProxy.this.mComposing) {
                    return;
                }
                if (MagicSkyProxy.this.mExportCallback != null && MagicSkyProxy.this.mMagicSky != null) {
                    MagicSkyProxy.this.mExportCallback.onProgress(MagicSkyProxy.this.mMagicSky.getCurrentComposePercent());
                }
                if (!MagicSkyProxy.this.mComposing) {
                    return;
                }
                MagicSkyProxy.this.mHandler.postDelayed(MagicSkyProxy.this.mPullProgress, 200L);
            }
        };

        public MagicSkyProxy(EffectNotifier effectNotifier) {
            this.mStatus = 0;
            MagicSky magicSky = new MagicSky(GalleryApp.sGetAndroidContext(), null);
            this.mMagicSky = magicSky;
            magicSky.setPriewLoop(true);
            this.mMagicSky.setCallbackNotify(effectNotifier);
            this.mStatus = 1;
            this.mMagicSky.setSubtitleModel(0, this.mSubtitle);
        }

        public final boolean init() {
            synchronized (this.mPlayLock) {
                DefaultLogger.d("DynamicSky", "start init");
                if (this.mStatus < 1) {
                    return false;
                }
                Bitmap bitmap = this.mBitmap;
                if (bitmap == null) {
                    DefaultLogger.d("DynamicSky", "init fail - bitmap is null");
                    return false;
                }
                this.mMagicSky.setOriginalImageBitmap(bitmap);
                this.mMagicSky.createSegmentObjForBitmap();
                int i = this.mSegmentType;
                if (i == -1) {
                    DefaultLogger.d("DynamicSky", "init fail - segment type is -1");
                    return false;
                }
                this.mMagicSky.setExchangeResult(i);
                try {
                    DefaultLogger.d("DynamicSky", "wait surface");
                    this.mSurfaceLatch.await(10L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.mStatus = 2;
                DefaultLogger.d("DynamicSky", "init success");
                return true;
            }
        }

        public void setBitmap(Bitmap bitmap) {
            this.mBitmap = bitmap;
        }

        public void setSegmentResult(int i) {
            this.mSegmentType = i;
        }

        public void setGLSurfaceView(GLSurfaceView gLSurfaceView) {
            this.mMagicSky.setGLSurfaceView(gLSurfaceView);
        }

        public void surfaceCreated() {
            DefaultLogger.d("DynamicSky", "surfaceCreated");
            this.mSurfaceLatch.countDown();
        }

        public String getSubTitle() {
            return this.mSubtitle;
        }

        public void setSubTitleModel(int i, String str) {
            this.mSubtitle = str;
            this.mMagicSky.setSubtitleModel(i, str);
        }

        public void play(int i, String str, int i2) {
            synchronized (this.mPlayLock) {
                if (this.mStatus < 2) {
                    init();
                }
                if (this.mStatus >= 2 && this.mStatus != 5) {
                    this.mStatus = 3;
                    long currentTimeMillis = System.currentTimeMillis();
                    if (i != -1 && str != null) {
                        this.mMagicSky.setSkyModel(i, str);
                    }
                    try {
                        this.mMagicSky.startMagicSkyPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                        DefaultLogger.d("DynamicSky", e.getMessage());
                    }
                    if (i2 >= 0) {
                        this.mMagicSky.setThreshold(threshold(i2));
                    }
                    this.mStatus = 4;
                    DefaultLogger.d("DynamicSky", "play consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    return;
                }
                DefaultLogger.d("DynamicSky", "play status is wrong %d", Integer.valueOf(this.mStatus));
            }
        }

        public void play() {
            play(-1, null, -1);
        }

        public void pause() {
            synchronized (this.mPlayLock) {
                if (!checkPlayableStatus("pause")) {
                    return;
                }
                this.mMagicSky.pauseMagicSkyPreview();
                DefaultLogger.d("DynamicSky", "pause");
            }
        }

        public void resume() {
            synchronized (this.mPlayLock) {
                if (!checkPlayableStatus("resume")) {
                    return;
                }
                this.mMagicSky.resumeMagicSkyPreview();
                DefaultLogger.d("DynamicSky", "resume");
            }
        }

        public void stop() {
            synchronized (this.mPlayLock) {
                if (!checkPlayableStatus("stop")) {
                    return;
                }
                this.mMagicSky.stopMagicSkyPreview();
                DefaultLogger.d("DynamicSky", "stop");
            }
        }

        public void audioOn() {
            synchronized (this.mPlayLock) {
                if (!checkPlayableStatus("audio_on")) {
                    return;
                }
                this.mMagicSky.unmute();
                this.mAudioOn = true;
                DefaultLogger.d("DynamicSky", "audio on");
            }
        }

        public void audioOff() {
            synchronized (this.mPlayLock) {
                if (!checkPlayableStatus("audio_off")) {
                    return;
                }
                this.mMagicSky.mute();
                this.mAudioOn = false;
                DefaultLogger.d("DynamicSky", "audio off");
            }
        }

        public void applyThreshold(int i) {
            synchronized (this.mPlayLock) {
                if (!checkPlayableStatus("threshold")) {
                    return;
                }
                float threshold = threshold(i);
                this.mMagicSky.setThreshold(threshold);
                DefaultLogger.d("DynamicSky", "threshold %f", Float.valueOf(threshold));
            }
        }

        public void release() {
            synchronized (this.mPlayLock) {
                if (this.mStatus == 5) {
                    return;
                }
                this.mStatus = 5;
                long currentTimeMillis = System.currentTimeMillis();
                MagicSky magicSky = this.mMagicSky;
                if (magicSky != null) {
                    magicSky.setCallbackNotify(null);
                    this.mMagicSky.setVideoSurface(null, 0, 0);
                    this.mMagicSky.releaseSource();
                    this.mMagicSky = null;
                }
                this.mExportCallback = null;
                DefaultLogger.d("DynamicSky", "release consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            }
        }

        public boolean isAudioOn() {
            return this.mAudioOn;
        }

        public int export(String str) {
            synchronized (this.mPlayLock) {
                if (!checkPlayableStatus("export")) {
                    return 2;
                }
                if (this.mExporting.getAndSet(true)) {
                    return 2;
                }
                Process.setThreadPriority(0);
                DefaultLogger.d("DynamicSky", "send export");
                this.mCancel.set(false);
                int exportInternal = exportInternal(str);
                if (exportInternal != 1) {
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("DynamicSky", "export"));
                    if (documentFile != null) {
                        documentFile.delete();
                    }
                }
                if (exportInternal == 3) {
                    play();
                }
                this.mExporting.set(false);
                return exportInternal;
            }
        }

        public void cancel() {
            cancelInternal();
        }

        public final int exportInternal(String str) {
            File createTmpOutFile = createTmpOutFile(str);
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("DynamicSky", "exportInternal");
            if (createTmpOutFile == null) {
                return 2;
            }
            this.mMagicSky.setCompseFileName(createTmpOutFile.getAbsolutePath());
            if (this.mCancel.getAndSet(false)) {
                return 3;
            }
            long currentTimeMillis = System.currentTimeMillis();
            this.mComposing = true;
            this.mHandler.postDelayed(this.mPullProgress, 200L);
            DefaultLogger.d("DynamicSky", "start compose");
            boolean startComposeMp4 = this.mMagicSky.startComposeMp4();
            this.mComposing = false;
            DefaultLogger.d("DynamicSky", "compose consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            this.mHandler.removeCallbacksAndMessages(null);
            if (startComposeMp4) {
                StorageSolutionProvider.get().moveFile(createTmpOutFile.getAbsolutePath(), str, appendInvokerTag);
            } else {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(createTmpOutFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                if (documentFile != null) {
                    documentFile.delete();
                }
            }
            if (this.mCancel.getAndSet(false)) {
                return 3;
            }
            return startComposeMp4 ? 1 : 2;
        }

        public void setExportCallback(IVideoExporter.Callback callback) {
            this.mExportCallback = callback;
        }

        public final boolean checkPlayableStatus(String str) {
            DefaultLogger.d("DynamicSky", "action %s,status %d", str, Integer.valueOf(this.mStatus));
            return this.mStatus == 4;
        }

        public final float threshold(int i) {
            return (Math.min(100, Math.max(0, i)) / 50.0f) - 1.0f;
        }

        public final void cancelInternal() {
            if (checkPlayableStatus("cancel") && this.mExporting.get()) {
                if (this.mComposing) {
                    this.mMagicSky.cancelCompose();
                }
                this.mCancel.set(true);
            }
        }

        public final File createTmpOutFile(String str) {
            File parentFile = new File(str).getParentFile();
            if (parentFile.exists() || parentFile.mkdirs()) {
                String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/dynamicSkyTemp");
                StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("DynamicSky", "createTmpOutFile"));
                File file = new File(pathInPrimaryStorage, ".DynamicSkyVideo");
                if (file.exists()) {
                    file.delete();
                }
                return file;
            }
            return null;
        }
    }
}
