package com.miui.gallery.movie.nvsdk;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsLiveWindowExt;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.themehelper.NvsImageFileDesc;
import com.meicam.themehelper.NvsThemeHelper;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MovieDependsModule;
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
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/* loaded from: classes2.dex */
public class NvMovieManager extends MovieManager<ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc>> {
    public static final String[] sCompileRateBlackList = {"grus", "pyxis", "davinci", "davinciin", "vela", "ginkgo", "willow", "tucana"};
    public static boolean sIsPaused;
    public Context mContext;
    public MovieManager.EncodeStateInterface mExportListener;
    public NvsLiveWindowExt mLiveWindow;
    public NvStateController mMovieController;
    public int mSeekIndex;
    public NvsStreamingContext mStreamingContext;
    public boolean mThemeEnable;
    public NvsThemeHelper mThemeHelper = new NvsThemeHelper();
    public NvsTimeline mTimeline;
    public int mVideoHeight;

    public static /* synthetic */ void $r8$lambda$mjBQqF_SzoguV58RUSNofRzype4(NvMovieManager nvMovieManager, NvsTimeline nvsTimeline, boolean z) {
        nvMovieManager.lambda$export$0(nvsTimeline, z);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public boolean isReadyForExport() {
        return true;
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public boolean isReadyForSwitch() {
        return true;
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        MovieDependsModule movieDependsModule = (MovieDependsModule) ModuleRegistry.getModule(MovieDependsModule.class);
        if (movieDependsModule != null) {
            NvsStreamingContext.setNativeLibraryDirPath(movieDependsModule.getMovieLibraryPath());
        }
        this.mStreamingContext = NvsStreamingContext.init((Activity) context, "assets:/nv/nvsdk.lic", 19);
        NvsLiveWindowExt nvsLiveWindowExt = new NvsLiveWindowExt(this.mContext);
        this.mLiveWindow = nvsLiveWindowExt;
        this.mDisplayView = nvsLiveWindowExt;
        NvsStreamingContext.setDebugLevel(Rom.IS_STABLE ? 1 : 3);
        initTimeLine(context);
    }

    public final void initTimeLine(Context context) {
        if (this.mStreamingContext == null) {
            DefaultLogger.e("NvMovieManager", "streamingContext is null!");
            return;
        }
        NvsVideoResolution nvsVideoResolution = new NvsVideoResolution();
        nvsVideoResolution.imageWidth = ScreenUtils.getScreenWidth();
        float fullScreenHeight = (ScreenUtils.getFullScreenHeight((Activity) context) * 1.0f) / nvsVideoResolution.imageWidth;
        MovieConfig.setHeightToWidth(fullScreenHeight);
        int ratio = (int) (nvsVideoResolution.imageWidth * MovieAspectRatio.getFitRatio(fullScreenHeight).getRatio());
        nvsVideoResolution.imageHeight = ratio;
        DefaultLogger.d("NvMovieManager", "initTimeLine in size: %d:%d", Integer.valueOf(ratio), Integer.valueOf(nvsVideoResolution.imageWidth));
        this.mVideoHeight = nvsVideoResolution.imageHeight;
        nvsVideoResolution.imagePAR = new NvsRational(1, 1);
        NvsRational nvsRational = new NvsRational(25, 1);
        NvsAudioResolution nvsAudioResolution = new NvsAudioResolution();
        nvsAudioResolution.sampleRate = 44100;
        nvsAudioResolution.channelCount = 2;
        NvsTimeline createTimeline = this.mStreamingContext.createTimeline(nvsVideoResolution, nvsRational, nvsAudioResolution);
        this.mTimeline = createTimeline;
        if (createTimeline == null) {
            DefaultLogger.e("NvMovieManager", "mTimeline is null!");
            return;
        }
        this.mMovieController = new NvStateController(this.mStreamingContext, createTimeline);
        this.mStreamingContext.connectTimelineWithLiveWindowExt(this.mTimeline, this.mLiveWindow);
        this.mStreamingContext.setStreamingEngineCallback(this.mMovieController);
        this.mStreamingContext.setPlaybackCallback(this.mMovieController);
        this.mStreamingContext.setPlaybackCallback2(this.mMovieController);
        sIsPaused = false;
    }

    public final ArrayList<NvsImageFileDesc> parseClipFromFile(List<ImageEntity> list) {
        ArrayList<NvsImageFileDesc> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            NvsImageFileDesc nvsImageFileDesc = new NvsImageFileDesc();
            nvsImageFileDesc.filePath = list.get(i).image;
            nvsImageFileDesc.hasFace = false;
            nvsImageFileDesc.show = true;
            arrayList.add(nvsImageFileDesc);
        }
        return arrayList;
    }

    public final void applyThemeAsset(String str) {
        this.mThemeHelper.applyTimelineTheme(str, false, TextUtils.equals("movieAssetsNormal", str));
    }

    public final boolean applyThemeFile(String str) {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("NvMovieManager", "applyThemeFile");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        if (documentFile != null && documentFile.exists()) {
            if (this.mThemeHelper.applyTimelineTheme(str, true, false)) {
                return true;
            }
            DefaultLogger.e("NvMovieManager", "failed to apply theme, delete file: %s,", str);
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile2 != null) {
                documentFile2.delete();
            }
            applyThemeAsset("movieAssetsNormal");
        }
        return false;
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void addStateChangeListener(MovieManager.StateChangeListener stateChangeListener) {
        this.mMovieController.addStateChangeListener(stateChangeListener);
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void removeStateChangeListener(MovieManager.StateChangeListener stateChangeListener) {
        this.mMovieController.removeStateChangeListener(stateChangeListener);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setInfo(MovieInfo movieInfo) {
        this.mThemeHelper.initHelper(this.mContext, this.mTimeline, parseClipFromFile(movieInfo.imageList));
        this.mThemeHelper.setDefaultRhythmPath("assets:/nv/defaultRhythm.json", "assets:/nv/defaultRhythm10s.json");
        this.mThemeHelper.changeCaptionText(movieInfo.title, 0);
        this.mThemeHelper.changeCaptionText(movieInfo.subTitle, 1);
        if (TextUtils.isEmpty(movieInfo.template)) {
            movieInfo.template = "movieAssetsNormal";
            applyThemeAsset("movieAssetsNormal");
        } else if (movieInfo.template.contains("Assets")) {
            applyThemeAsset(movieInfo.template);
        } else if (!applyThemeFile(TemplateFactory.getTemplatePath(movieInfo.template))) {
            movieInfo.template = "movieAssetsNormal";
        }
        if (!movieInfo.isShortVideo) {
            this.mThemeHelper.changeTimelineDuration(false);
        }
        replay();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void removeImageAtIndex(int i) {
        this.mThemeHelper.deleteClip(i);
        replay();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setIsShortVideo(boolean z) {
        this.mThemeHelper.changeTimelineDuration(z);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void addImage(List<ImageEntity> list) {
        this.mThemeHelper.insertClip(parseClipFromFile(list));
        replay();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void moveImage(int i, int i2) {
        this.mThemeHelper.moveClip(i, i2);
        replay();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void resetImage(List<ImageEntity> list, boolean z) {
        this.mThemeHelper.changeTimelineDuration(z);
        this.mThemeHelper.resetClip(parseClipFromFile(list));
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void onResume() {
        if (sIsPaused) {
            this.mLiveWindow.repaintVideoFrame();
        }
        this.mMovieController.onResume();
        super.onResume();
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void onPause() {
        this.mMovieController.onPause();
        sIsPaused = true;
        super.onPause();
    }

    @Override // com.miui.gallery.movie.core.MovieManager
    public void destroy() {
        if (sIsPaused) {
            this.mStreamingContext.removeTimeline(this.mTimeline);
            this.mStreamingContext.clearCachedResources(false);
            NvsStreamingContext.close();
        }
        this.mExportListener = null;
        this.mLiveWindow = null;
        super.destroy();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public int getTotalTime() {
        return (int) (this.mTimeline.getDuration() / 1000);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public boolean isSupportVideo(String str) {
        return this.mStreamingContext.getAVFileInfo(str) != null;
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setAudio(AudioResource audioResource) {
        if (audioResource == null || audioResource.getResType() == 1) {
            this.mThemeHelper.changeMusic(null, -1);
        } else if (audioResource.getResType() == 2) {
            this.mThemeHelper.changeMusic(audioResource.getDownloadSrcPath(), 1);
        } else {
            this.mThemeHelper.changeMusic(audioResource.getDownloadSrcPath(), 2);
        }
        replay();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void setTemplate(TemplateResource templateResource) {
        String srcPath = templateResource.getSrcPath();
        if (TextUtils.equals("movieAssetsNormal", srcPath)) {
            applyThemeAsset(srcPath);
        } else {
            applyThemeFile(srcPath);
        }
        replay();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void replay() {
        if (!this.mThemeEnable) {
            setThemeEnable(true);
        }
        this.mMovieController.replay();
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void seek(int i) {
        if (!this.mThemeEnable) {
            setThemeEnable(true);
        }
        this.mMovieController.seek(i);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public int seekToIndex(int i) {
        this.mSeekIndex = i;
        setThemeEnable(false);
        int timelinePosition = (int) (this.mThemeHelper.getTimelinePosition(i) / 1000);
        this.mMovieController.seek(timelinePosition);
        return timelinePosition;
    }

    public void setThemeEnable(boolean z) {
        this.mThemeEnable = z;
        this.mThemeHelper.setThemeEnabled(z);
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public void export(String str, MovieManager.EncodeStateInterface encodeStateInterface) {
        if (!this.mThemeEnable) {
            long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
            setThemeEnable(true);
            this.mMovieController.seek((int) (timelineCurrentPosition / 1000));
        }
        this.mExportListener = encodeStateInterface;
        encodeStateInterface.onEncodeStart();
        this.mStreamingContext.setCompileCallback2(new NvsStreamingContext.CompileCallback2() { // from class: com.miui.gallery.movie.nvsdk.NvMovieManager$$ExternalSyntheticLambda0
            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback2
            public final void onCompileCompleted(NvsTimeline nvsTimeline, boolean z) {
                NvMovieManager.$r8$lambda$mjBQqF_SzoguV58RUSNofRzype4(NvMovieManager.this, nvsTimeline, z);
            }
        });
        this.mStreamingContext.setCompileCallback(new NvsStreamingContext.CompileCallback() { // from class: com.miui.gallery.movie.nvsdk.NvMovieManager.1
            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileFinished(NvsTimeline nvsTimeline) {
            }

            {
                NvMovieManager.this = this;
            }

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileProgress(NvsTimeline nvsTimeline, int i) {
                if (NvMovieManager.this.mExportListener != null) {
                    NvMovieManager.this.mExportListener.onEncodeProgress(i);
                }
            }

            @Override // com.meicam.sdk.NvsStreamingContext.CompileCallback
            public void onCompileFailed(NvsTimeline nvsTimeline) {
                if (NvMovieManager.this.mExportListener != null) {
                    NvMovieManager.this.mExportListener.onEncodeEnd(false, false, 0);
                }
            }
        });
        this.mStreamingContext.setCustomCompileVideoHeight(this.mVideoHeight);
        Hashtable<String, Object> hashtable = new Hashtable<>();
        hashtable.put(NvsStreamingContext.COMPILE_USE_OPERATING_RATE, Boolean.valueOf(isDisableCompileRate()));
        this.mStreamingContext.setCompileConfigurations(hashtable);
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        NvsTimeline nvsTimeline = this.mTimeline;
        nvsStreamingContext.compileTimeline(nvsTimeline, 0L, nvsTimeline.getDuration(), str, 256, 2, 64);
    }

    public /* synthetic */ void lambda$export$0(NvsTimeline nvsTimeline, boolean z) {
        MovieManager.EncodeStateInterface encodeStateInterface = this.mExportListener;
        if (encodeStateInterface != null) {
            if (z) {
                encodeStateInterface.onEncodeEnd(false, true, 0);
            } else {
                encodeStateInterface.onEncodeEnd(true, false, 0);
            }
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void cancelExport() {
        this.mMovieController.cancelExport();
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void resume() {
        if (!this.mThemeEnable) {
            setThemeEnable(true);
            this.mMovieController.play(this.mThemeHelper.getTimelinePosition(this.mSeekIndex));
            return;
        }
        this.mMovieController.resume();
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void pause() {
        this.mMovieController.pause();
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void pauseOrResume() {
        if (this.mMovieController.isResume()) {
            if (!this.mThemeEnable) {
                setThemeEnable(true);
                this.mMovieController.play(this.mThemeHelper.getTimelinePosition(this.mSeekIndex));
                return;
            }
            this.mMovieController.resume();
            return;
        }
        this.mMovieController.pause();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    /* renamed from: getThumbnailImages */
    public ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> mo1161getThumbnailImages() {
        return this.mThemeHelper.getThumbnailImages();
    }

    @Override // com.miui.gallery.movie.core.IMovieManager
    public IMultiThumbnailSequenceView createThumbnailSequenceView(Context context) {
        return new NvsMultiThumbnailSequenceViewWrapper(context);
    }

    public final boolean isDisableCompileRate() {
        for (String str : sCompileRateBlackList) {
            if (str.equals(Build.DEVICE)) {
                return true;
            }
        }
        return false;
    }
}
