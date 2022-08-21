package com.miui.gallery.movie.xmsdk;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.AudioResource;
import com.miui.gallery.movie.entity.ImageEntity;
import com.miui.gallery.movie.entity.MovieAspectRatio;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.entity.TemplateResource;
import com.miui.gallery.movie.ui.factory.TemplateFactory;
import com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.xmstreaming.XmsContext;
import com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView;
import com.miui.gallery.xmstreaming.XmsThemeHelper;
import com.miui.gallery.xmstreaming.XmsTimeline;
import com.miui.settings.Settings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import miuix.core.util.FileUtils;

/* loaded from: classes2.dex */
public class XmMovieManager extends MovieManager<ArrayList<XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc>> implements XmsContext.PlaybackCallback, XmsContext.StreamingEngineCallback {
    public MovieManager.EncodeStateInterface mExportListener;
    public boolean mIsPaused;
    public boolean mIsTimelineApply;
    public boolean mIsTimelineConnected;
    public MovieInfo mMovieInfo;
    public int mMovieState;
    public CopyOnWriteArrayList<MovieManager.StateChangeListener> mStateChangeListeners;
    public XmsContext mStreamingContext;
    public XmsTimeline mTimeline;
    public XmsThemeHelper mThemeHelper = new XmsThemeHelper();
    public Object mLock = new Object();
    public Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.miui.gallery.movie.xmsdk.XmMovieManager.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (!XmMovieManager.this.isReadyForSwitch()) {
                XmMovieManager.this.mHandler.sendEmptyMessageDelayed(0, 10L);
            } else {
                XmMovieManager.this.resetImage();
            }
        }
    };

    public final int getStateFromXmEngine(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 3;
                if (i != 3) {
                    return -500;
                }
            }
        }
        return i2;
    }

    @Override // com.miui.gallery.xmstreaming.XmsContext.StreamingEngineCallback
    public void onFirstVideoFramePresented() {
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void init(Context context) {
        XmsContext xmsContext = XmsContext.getInstance();
        this.mStreamingContext = xmsContext;
        xmsContext.initPlayer(true);
        TextureView textureView = new TextureView(context);
        this.mDisplayView = textureView;
        initTimeLine(context);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() { // from class: com.miui.gallery.movie.xmsdk.XmMovieManager.1
            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                XmMovieManager.this.mStreamingContext.connectTimelineWithSurface(XmMovieManager.this.mTimeline, new Surface(surfaceTexture));
                XmMovieManager.this.mIsTimelineConnected = true;
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
                DefaultLogger.d("XmMovieManager", "onSurfaceTextureSizeChanged %d:%d", Integer.valueOf(i), Integer.valueOf(i2));
                XmMovieManager.this.mStreamingContext.nativeSurfaceChanged(new Surface(surfaceTexture), i, i2);
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                XmMovieManager.this.mIsTimelineConnected = false;
                return false;
            }
        });
        this.mIsPaused = false;
    }

    public final void initTimeLine(Context context) {
        int physicalScreenSmallWidth;
        int physicalScreenLargeWidth;
        int i;
        int ratio;
        if (this.mStreamingContext == null) {
            DefaultLogger.e("XmMovieManager", "streamingContext is null!");
            return;
        }
        if (BaseBuildUtil.isFoldableDevice()) {
            physicalScreenSmallWidth = 1860;
            physicalScreenLargeWidth = 2480;
        } else {
            physicalScreenSmallWidth = ScreenUtils.getPhysicalScreenSmallWidth(context);
            physicalScreenLargeWidth = ScreenUtils.getPhysicalScreenLargeWidth(context);
        }
        float f = (physicalScreenLargeWidth * 1.0f) / physicalScreenSmallWidth;
        MovieConfig.setHeightToWidth(f);
        MovieAspectRatio fitRatio = MovieAspectRatio.getFitRatio(f);
        if (!MovieConfig.isUseLowResolution()) {
            i = (physicalScreenSmallWidth / 4) * 4;
            ratio = (((int) (i * fitRatio.getRatio())) / 4) * 4;
        } else {
            i = 720;
            ratio = (int) (((720 * fitRatio.getRatio()) / 4.0f) * 4.0f);
        }
        DefaultLogger.d("XmMovieManager", "initTimeLine in size: %d:%d", Integer.valueOf(i), Integer.valueOf(ratio));
        XmsTimeline createTimeline = this.mStreamingContext.createTimeline(physicalScreenSmallWidth, physicalScreenLargeWidth);
        this.mTimeline = createTimeline;
        createTimeline.setPreferWidth(i);
        this.mTimeline.setPreferHeight(ratio);
        this.mStreamingContext.setPlaybackCallback(this);
        this.mStreamingContext.setStreamingEngineCallback(this);
        int i2 = 0;
        if (BaseBuildUtil.isInternational()) {
            i2 = Settings.getRegion().endsWith("IN") ? 2 : 1;
        }
        int i3 = MovieConfig.sTestLocationType;
        if (i3 >= 0) {
            i2 = i3;
        }
        this.mThemeHelper.initHelper(context.getApplicationContext(), this.mTimeline, i2);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setInfo(MovieInfo movieInfo) {
        DefaultLogger.d("XmMovieManager", "setInfo");
        this.mMovieInfo = movieInfo;
        this.mThemeHelper.setShortVideo(movieInfo.isShortVideo);
        for (ImageEntity imageEntity : this.mMovieInfo.imageList) {
            this.mThemeHelper.appendClip(imageEntity.image);
        }
        String str = "assets:/xms/movieTemplateNormal";
        if (TextUtils.isEmpty(movieInfo.template) || TextUtils.equals(movieInfo.template, "movieAssetsNormal")) {
            movieInfo.template = "movieAssetsNormal";
        } else {
            String templatePath = TemplateFactory.getTemplatePath(movieInfo.template);
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(templatePath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("XmMovieManager", "setInfo"));
            if (documentFile == null || !documentFile.exists()) {
                movieInfo.template = "movieAssetsNormal";
                DefaultLogger.w("XmMovieManager", "default template:%s is not download", str);
            } else {
                str = templatePath;
            }
        }
        if (TextUtils.equals(movieInfo.template, "movieAssetsNormal")) {
            XmsTimeline xmsTimeline = this.mTimeline;
            MovieInfo movieInfo2 = this.mMovieInfo;
            xmsTimeline.setTitle(movieInfo2.title, movieInfo2.subTitle);
        }
        this.mThemeHelper.setTemplate(str);
        applyTimeline();
        DefaultLogger.d("XmMovieManager", "setInfo end");
    }

    public final void applyTimeline() {
        if (this.mIsTimelineConnected) {
            this.mStreamingContext.applyTimeline(this.mTimeline);
            this.mIsTimelineApply = true;
        }
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void onPause() {
        super.onPause();
        DefaultLogger.d("XmMovieManager", "onPause");
        synchronized (this.mLock) {
            pause();
            this.mStreamingContext.pauseToBackground();
            this.mHandler.removeMessages(0);
            this.mIsPaused = true;
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void pause() {
        DefaultLogger.d("XmMovieManager", "pause");
        if (!this.mIsTimelineApply) {
            return;
        }
        this.mStreamingContext.pause();
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void resume() {
        DefaultLogger.d("XmMovieManager", "resume");
        this.mStreamingContext.resume();
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void onResume() {
        DefaultLogger.d("XmMovieManager", "onResume");
        super.onResume();
        resumeToForeground();
    }

    public final void resumeToForeground() {
        synchronized (this.mLock) {
            if (this.mIsPaused && this.mIsTimelineConnected) {
                int resumeToForeground = this.mStreamingContext.resumeToForeground(this.mTimeline.getInternalObject());
                this.mIsPaused = false;
                seek(resumeToForeground * 40);
            }
        }
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void destroy() {
        super.destroy();
        DefaultLogger.d("XmMovieManager", "destroy");
        if (this.mIsPaused) {
            this.mStreamingContext.setPlaybackCallback(null);
            this.mStreamingContext.setStreamingEngineCallback(null);
            this.mStreamingContext.release();
            this.mIsTimelineConnected = false;
            this.mIsTimelineApply = false;
            this.mExportListener = null;
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void pauseOrResume() {
        DefaultLogger.d("XmMovieManager", "pauseOrResume:%d", Integer.valueOf(this.mMovieState));
        if (this.mMovieState == 1) {
            pause();
        } else {
            resume();
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void seek(int i) {
        this.mStreamingContext.seekToPos(i);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public int getTotalTime() {
        return this.mThemeHelper.getDuration();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void removeImageAtIndex(int i) {
        resetImage();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void addImage(List<ImageEntity> list) {
        resetImage();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void moveImage(int i, int i2) {
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessageDelayed(0, 250L);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void resetImage(List<ImageEntity> list, boolean z) {
        synchronized (this.mLock) {
            this.mThemeHelper.changeTimelineDuration(z);
            resetImage();
        }
    }

    public final void resetImage() {
        synchronized (this.mLock) {
            this.mThemeHelper.clearClip();
            for (ImageEntity imageEntity : this.mMovieInfo.imageList) {
                this.mThemeHelper.appendClip(imageEntity.image);
            }
            this.mThemeHelper.reBuildTimeLine();
            resumeToForeground();
            applyTimeline();
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setIsShortVideo(boolean z) {
        synchronized (this.mLock) {
            this.mThemeHelper.changeTimelineDuration(z);
            applyTimeline();
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setAudio(AudioResource audioResource) {
        DefaultLogger.d("XmMovieManager", "setAudio" + audioResource + audioResource.getResType() + audioResource.getDownloadSrcPath());
        synchronized (this.mLock) {
            resumeToForeground();
            if (audioResource.getResType() == 1) {
                this.mThemeHelper.changeMusic(null, 3);
            } else if (audioResource.getResType() == 2) {
                this.mThemeHelper.changeMusic(audioResource.getDownloadSrcPath(), 1);
            } else {
                this.mThemeHelper.changeMusic(audioResource.getDownloadSrcPath(), 2);
            }
            applyTimeline();
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public int seekToIndex(int i) {
        int geClipMiddleDuration;
        synchronized (this.mLock) {
            geClipMiddleDuration = this.mThemeHelper.geClipMiddleDuration(i);
            this.mStreamingContext.seekToPos(geClipMiddleDuration);
        }
        return geClipMiddleDuration;
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public boolean isSupportVideo(String str) {
        String lowerCase = FileUtils.getExtension(str).toLowerCase();
        DefaultLogger.d("XmMovieManager", "isSupportVideo extension=" + lowerCase);
        return "mp3".equals(lowerCase) || "aac".equals(lowerCase);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setTemplate(TemplateResource templateResource) {
        synchronized (this.mLock) {
            if (templateResource.isPackageAssets) {
                XmsTimeline xmsTimeline = this.mTimeline;
                MovieInfo movieInfo = this.mMovieInfo;
                xmsTimeline.setTitle(movieInfo.title, movieInfo.subTitle);
                this.mThemeHelper.setTemplate("assets:/xms/movieTemplateNormal");
            } else {
                this.mTimeline.setTitle(null, null);
                this.mThemeHelper.setTemplate(templateResource.getDownloadSrcPath());
            }
            applyTimeline();
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    /* renamed from: getThumbnailImages  reason: collision with other method in class */
    public ArrayList<XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc> mo1161getThumbnailImages() {
        ArrayList<XmsMultiThumbnailSequenceView.ThumbnailSequenceDesc> thumbnailImages;
        synchronized (this.mLock) {
            thumbnailImages = this.mThemeHelper.getThumbnailImages();
        }
        return thumbnailImages;
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public IMultiThumbnailSequenceView createThumbnailSequenceView(Context context) {
        return new XmsMultiThumbnailSequenceViewWrapper(context);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void export(String str, MovieManager.EncodeStateInterface encodeStateInterface) {
        synchronized (this.mLock) {
            this.mExportListener = encodeStateInterface;
            encodeStateInterface.onEncodeStart();
            this.mStreamingContext.compileTimeline(this.mTimeline, str, new XmsContext.CompileCallback() { // from class: com.miui.gallery.movie.xmsdk.XmMovieManager.3
                @Override // com.miui.gallery.xmstreaming.XmsContext.CompileCallback
                public void onCompileProgress(int i) {
                    if (XmMovieManager.this.mExportListener != null) {
                        XmMovieManager.this.mExportListener.onEncodeProgress(i);
                    }
                }

                @Override // com.miui.gallery.xmstreaming.XmsContext.CompileCallback
                public void onCompileFinished() {
                    if (XmMovieManager.this.mExportListener != null) {
                        XmMovieManager.this.mExportListener.onEncodeEnd(true, false, 0);
                    }
                }

                @Override // com.miui.gallery.xmstreaming.XmsContext.CompileCallback
                public void onCompileFailed(boolean z) {
                    if (XmMovieManager.this.mExportListener != null) {
                        XmMovieManager.this.mExportListener.onEncodeEnd(false, z, 0);
                    }
                }
            });
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void cancelExport() {
        synchronized (this.mLock) {
            this.mStreamingContext.cancelCompile(this.mTimeline.getInternalObject());
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void replay() {
        seek(0);
        resume();
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void removeStateChangeListener(MovieManager.StateChangeListener stateChangeListener) {
        this.mStateChangeListeners = null;
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void addStateChangeListener(MovieManager.StateChangeListener stateChangeListener) {
        if (stateChangeListener != null) {
            if (this.mStateChangeListeners == null) {
                this.mStateChangeListeners = new CopyOnWriteArrayList<>();
            }
            if (this.mStateChangeListeners.contains(stateChangeListener)) {
                return;
            }
            this.mStateChangeListeners.add(stateChangeListener);
        }
    }

    @Override // com.miui.gallery.xmstreaming.XmsContext.PlaybackCallback
    public void onPlaybackPreloadingCompletion() {
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onPlaybackPreloadingCompletion();
        }
        DefaultLogger.d("XmMovieManager", "onPlaybackPreloadingCompletion");
    }

    @Override // com.miui.gallery.xmstreaming.XmsContext.PlaybackCallback
    public void onPlaybackEOF() {
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onPlaybackEOF();
        }
        this.mStreamingContext.skipPause();
    }

    @Override // com.miui.gallery.xmstreaming.XmsContext.PlaybackCallback
    public void onPlaybackTimelinePosition(int i) {
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onPlaybackTimeChanged(i);
        }
    }

    @Override // com.miui.gallery.xmstreaming.XmsContext.StreamingEngineCallback
    public void onStreamingEngineStateChanged(int i) {
        DefaultLogger.d("XmMovieManager", "state: %d", Integer.valueOf(i));
        this.mMovieState = getStateFromXmEngine(i);
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onStateChanged(this.mMovieState);
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public boolean isReadyForSwitch() {
        return this.mStreamingContext.isReadyForSwitch();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public boolean isReadyForExport() {
        return this.mStreamingContext.isReadyForExport();
    }
}
