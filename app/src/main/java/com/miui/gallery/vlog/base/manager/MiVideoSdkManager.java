package com.miui.gallery.vlog.base.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.interfaces.OnDurationChangeListener;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.base.widget.DisplayView;
import com.miui.gallery.vlog.base.widget.VlogPlayView;
import com.miui.gallery.vlog.caption.widget.VideoClipInfo;
import com.miui.gallery.vlog.clip.ClipMenuPresenter;
import com.miui.gallery.vlog.clip.ClipReverseHelper;
import com.miui.gallery.vlog.clip.MiTransResManager;
import com.miui.gallery.vlog.entity.VideoClip;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.home.VlogStorage;
import com.miui.gallery.vlog.ratio.MiVideoRatioManager;
import com.miui.gallery.vlog.sdk.callbacks.CompileCallback;
import com.miui.gallery.vlog.sdk.callbacks.ExportCallback2;
import com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback;
import com.miui.gallery.vlog.sdk.callbacks.ResizeCallback;
import com.miui.gallery.vlog.sdk.callbacks.SeekCallback;
import com.miui.gallery.vlog.sdk.callbacks.TimelineReleaseCallback;
import com.miui.gallery.vlog.sdk.callbacks.TimelineStartedCallback;
import com.miui.gallery.vlog.sdk.interfaces.BaseVlogManager;
import com.miui.gallery.vlog.sdk.interfaces.IAudioManager;
import com.miui.gallery.vlog.sdk.interfaces.ICaptionManager;
import com.miui.gallery.vlog.sdk.interfaces.IClipAudioManager;
import com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager;
import com.miui.gallery.vlog.sdk.interfaces.IRatioManager;
import com.miui.gallery.vlog.sdk.interfaces.ITransManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import com.miui.gallery.vlog.sdk.manager.MiVideoAudioManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoCaptionManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoClipAudioManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoClipManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoFilterManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoFrameLoader;
import com.miui.gallery.vlog.sdk.manager.MiVideoHeaderTailManager;
import com.miui.gallery.vlog.sdk.manager.MiVideoStateController;
import com.miui.gallery.vlog.sdk.models.BaseVideoClip;
import com.miui.gallery.vlog.sdk.models.MiVlogVideoClip;
import com.miui.gallery.vlog.template.MiVideoTemplateFilesManager;
import com.miui.gallery.vlog.template.TemplateFilesManager;
import com.miui.gallery.vlog.template.bean.FilterBeanProcessed;
import com.miui.gallery.vlog.tools.VlogTransFrameUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.nexstreaming.nexeditorsdk.nexChecker;
import com.xiaomi.milab.videosdk.OnReleaseListener;
import com.xiaomi.milab.videosdk.XmsAudioTrack;
import com.xiaomi.milab.videosdk.XmsContext;
import com.xiaomi.milab.videosdk.XmsTextureView;
import com.xiaomi.milab.videosdk.XmsTimeline;
import com.xiaomi.milab.videosdk.XmsVideoClip;
import com.xiaomi.milab.videosdk.XmsVideoFilter;
import com.xiaomi.milab.videosdk.XmsVideoTrack;
import com.xiaomi.milab.videosdk.interfaces.TimelineCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class MiVideoSdkManager {
    public static boolean HAS_MIVIDEOSDK_LOADED = false;
    public static int INIT_COUNT;
    public BaseVlogManager mAudioManager;
    public ICaptionManager mCaptionManger;
    public IClipAudioManager mClipAudioManager;
    public BaseVlogManager mClipManager;
    public ClipReverseHelper mClipReverseHelper;
    public Context mContext;
    public List<OnDurationChangeListener> mDurationChangeListeners;
    public ExportCallback2 mExportCallback2;
    public BaseVlogManager mFilterManager;
    public IHeaderTailManager mHeaderTailManager;
    public boolean mIsTimelineFirstStartCompleted;
    public XmsTextureView mLiveWindow;
    public OnLiveWindowLayoutUpdatedListener mLiveWindowLayoutUpdatedListener;
    public ViewGroup mLiveWindowValidLayout;
    public int[] mLiveWindowValidSize;
    public MiVideoTimelineCallback mMiVideoTimelineCallback;
    public int mOriginalHeight;
    public int mOriginalWidth;
    public IRatioManager mRatioManager;
    public MiVideoStateController mStateController;
    public TemplateFilesManager mTemplateManager;
    public TimelineReleaseCallback mTimelineReleaseCallback;
    public TimelineStartedCallback mTimelineStartedCallback;
    public ITransManager mTransManager;
    public VideoClipsManager mVideoClipsManager;
    public MiVideoFrameLoader mVideoFrameLoader;
    public XmsContext mXmsContext;
    public XmsTimeline mXmsTimeline;
    public final int MIN_EXPORT_SIZE = 120;
    public List<IVideoClip> mVideoClips = new ArrayList();
    public List<String> mSubTrackVideoPathList = new ArrayList();
    public Map<Object, IVideoClip> mVideoClipMap = new HashMap();
    public int mCurrentRatioType = 1;
    public int mAspectRatio = -1;
    public boolean mIgnoreDisconnectAndReconnect = false;
    public boolean mIsReleased = false;
    public ViewTreeObserver.OnGlobalLayoutListener mLiveWindowLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.base.manager.MiVideoSdkManager.1
        {
            MiVideoSdkManager.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            MiVideoSdkManager.this.mLiveWindow.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            if (MiVideoSdkManager.this.mLiveWindowLayoutUpdatedListener != null) {
                MiVideoSdkManager.this.mLiveWindowLayoutUpdatedListener.onLiveWindowLayoutUpdated();
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface IDoOperationCombined {
        void combined();
    }

    /* loaded from: classes2.dex */
    public interface IVideoClipReverseCallback {
        void onFail();

        void onSuccess(IVideoClip iVideoClip);
    }

    /* loaded from: classes2.dex */
    public interface OnLiveWindowLayoutUpdatedListener {
        void onLiveWindowLayoutUpdated();
    }

    public static /* synthetic */ void $r8$lambda$i3ij78jeI4kMwZQKK20fXbF5oIM(MiVideoSdkManager miVideoSdkManager, int i, int i2, int i3, int i4) {
        miVideoSdkManager.lambda$setLiveWindowRatio$0(i, i2, i3, i4);
    }

    public int appendClip(String str, long j, long j2, float f, float f2, String str2) {
        return 0;
    }

    public final boolean isWidthLimit(float f, float f2, float f3) {
        return f / f2 < f3;
    }

    public MiVideoSdkManager(Context context) {
        this.mContext = context;
        init();
    }

    public static void loadSo(Context context) {
        if (HAS_MIVIDEOSDK_LOADED) {
            return;
        }
        HAS_MIVIDEOSDK_LOADED = true;
        String absolutePath = context.getDir("libs", 0).getAbsolutePath();
        System.load(absolutePath + "/libmiffmpeg.so");
        System.load(absolutePath + "/libMiVideoSDK.so");
    }

    public static void initXmsContext(Context context) {
        XmsContext.getInstance().setContext(context.getApplicationContext());
        XmsContext.getInstance().initLister();
    }

    public final void init() {
        loadSo(this.mContext);
        int i = INIT_COUNT + 1;
        INIT_COUNT = i;
        DefaultLogger.d("MiVideoSdkManager_", "init XmsContext %d", Integer.valueOf(i));
        if (INIT_COUNT > 1) {
            DefaultLogger.d("MiVideoSdkManager_", "need release first");
            this.mXmsContext = XmsContext.getInstance();
            releaseTimeline();
        }
        this.mXmsContext = XmsContext.getInstance();
        initXmsContext(this.mContext);
        this.mXmsContext.setTimelineCallback(getMiVideoTimeLineCallback());
        XmsTimeline createTimeline = this.mXmsContext.createTimeline();
        this.mXmsTimeline = createTimeline;
        createTimeline.setReleaseListener(new TimelineReleaseListener(this));
        this.mVideoClipsManager = new VideoClipsManager();
        MiVideoStateController miVideoStateController = new MiVideoStateController();
        this.mStateController = miVideoStateController;
        this.mXmsContext.setPlayCallback(miVideoStateController);
        this.mXmsContext.setExportCallback(this.mStateController);
        this.mIsReleased = false;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setTimelineStartedCallback(TimelineStartedCallback timelineStartedCallback) {
        this.mTimelineStartedCallback = timelineStartedCallback;
    }

    public void setTimelineReleaseCallback(TimelineReleaseCallback timelineReleaseCallback) {
        this.mTimelineReleaseCallback = timelineReleaseCallback;
    }

    public final TimelineCallback getMiVideoTimeLineCallback() {
        if (this.mMiVideoTimelineCallback == null) {
            this.mMiVideoTimelineCallback = new MiVideoTimelineCallback(this);
        }
        return this.mMiVideoTimelineCallback;
    }

    /* loaded from: classes2.dex */
    public static class TimelineReleaseListener implements OnReleaseListener {
        public WeakReference<MiVideoSdkManager> ref;

        public TimelineReleaseListener(MiVideoSdkManager miVideoSdkManager) {
            this.ref = new WeakReference<>(miVideoSdkManager);
        }

        @Override // com.xiaomi.milab.videosdk.OnReleaseListener
        public void onRelease() {
            MiVideoSdkManager miVideoSdkManager = this.ref.get();
            if (miVideoSdkManager == null || miVideoSdkManager.mTimelineReleaseCallback == null) {
                return;
            }
            miVideoSdkManager.mTimelineReleaseCallback.onTimelineReleased();
        }
    }

    /* loaded from: classes2.dex */
    public static class MiVideoTimelineCallback implements TimelineCallback {
        public WeakReference<MiVideoSdkManager> mMiVideoSdkManager;

        public MiVideoTimelineCallback(MiVideoSdkManager miVideoSdkManager) {
            this.mMiVideoSdkManager = new WeakReference<>(miVideoSdkManager);
        }

        @Override // com.xiaomi.milab.videosdk.interfaces.TimelineCallback
        public void onTimelineStarted() {
            WeakReference<MiVideoSdkManager> weakReference = this.mMiVideoSdkManager;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.mMiVideoSdkManager.get().mIsTimelineFirstStartCompleted = true;
            if (this.mMiVideoSdkManager.get().mTimelineStartedCallback == null) {
                return;
            }
            this.mMiVideoSdkManager.get().mTimelineStartedCallback.onTimelineStarted();
        }

        public void release() {
            WeakReference<MiVideoSdkManager> weakReference = this.mMiVideoSdkManager;
            if (weakReference != null) {
                weakReference.clear();
                this.mMiVideoSdkManager = null;
            }
        }
    }

    public boolean isTimelineFirstStartCompleted() {
        return this.mIsTimelineFirstStartCompleted;
    }

    public void setDisplayView(View view) {
        this.mLiveWindow = (XmsTextureView) view;
    }

    public void initializeSurface(DisplayView displayView) {
        this.mLiveWindow = displayView;
        this.mXmsContext.attachTexture(this.mXmsTimeline, displayView, VlogTransFrameUtils.isNeedLowEndFrame() ? 20 : 30);
        XmsVideoTrack appendVideoTrack = appendVideoTrack();
        this.mXmsTimeline.mixAudioTrack(appendVideoTrack.getTrackIndex(), appendAudioTrack().getTrackIndex());
        this.mVideoClipsManager.setVideoTrack(appendVideoTrack);
    }

    public void initOriginalSize(int i, int i2) {
        int i3;
        int i4;
        int i5;
        this.mOriginalWidth = i;
        this.mOriginalHeight = i2;
        int[] liveWindowValidSize = getLiveWindowValidSize();
        int i6 = liveWindowValidSize[0];
        float f = i;
        float f2 = i2;
        if (i6 / liveWindowValidSize[1] < f / f2) {
            this.mOriginalWidth = (i6 / 4) * 4;
            this.mOriginalHeight = (int) ((i5 * i2) / f);
        } else {
            this.mOriginalWidth = (((int) ((i3 * i) / f2)) / 4) * 4;
            this.mOriginalHeight = (int) ((i4 * i2) / f);
        }
        this.mOriginalWidth = (this.mOriginalWidth / 4) * 4;
        this.mOriginalHeight = (this.mOriginalHeight / 2) * 2;
        int i7 = this.mAspectRatio;
        if (i7 != -1) {
            setLiveWindowRatio(i7);
        } else if (isSingleVideoEdit()) {
            setLiveWindowRatio(5);
        } else {
            setLiveWindowRatio(0);
        }
    }

    public void setLiveWindowValidLayout(ViewGroup viewGroup) {
        this.mLiveWindowValidLayout = viewGroup;
    }

    public int getOriginalWidth() {
        return this.mOriginalWidth;
    }

    public int getOriginalHeight() {
        return this.mOriginalHeight;
    }

    public int[] getLiveWindowValidSize() {
        int[] iArr = new int[2];
        getLiveWindowMaxSize(iArr, this.mLiveWindow);
        int i = iArr[0];
        int i2 = iArr[1];
        if (i == 0 || i2 == 0) {
            int[] iArr2 = this.mLiveWindowValidSize;
            return iArr2 == null ? new int[2] : iArr2;
        }
        int[] iArr3 = new int[4];
        getVlogPlayViewPadding(iArr3, this.mLiveWindow);
        int i3 = iArr3[0];
        int i4 = iArr3[1];
        int i5 = iArr3[2];
        int i6 = iArr3[3];
        this.mLiveWindowValidSize = new int[2];
        if (((View) this.mLiveWindow.getParent()) != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mLiveWindow.getLayoutParams();
            int i7 = (((i2 - i4) - i6) - marginLayoutParams.topMargin) - marginLayoutParams.bottomMargin;
            int[] iArr4 = this.mLiveWindowValidSize;
            iArr4[0] = (((i - i3) - i5) - marginLayoutParams.leftMargin) - marginLayoutParams.rightMargin;
            iArr4[1] = i7;
        }
        return this.mLiveWindowValidSize;
    }

    public final void getLiveWindowMaxSize(int[] iArr, View view) {
        if (view != null) {
            if (view == this.mLiveWindowValidLayout) {
                iArr[0] = view.getWidth();
                iArr[1] = view.getHeight();
            } else if (view.getParent() == null || !(view.getParent() instanceof View)) {
            } else {
                getLiveWindowMaxSize(iArr, view.getParent() == null ? null : (View) view.getParent());
            }
        }
    }

    public final void getVlogPlayViewPadding(int[] iArr, View view) {
        if (view != null) {
            if (view instanceof VlogPlayView) {
                iArr[0] = view.getPaddingLeft();
                iArr[1] = view.getPaddingTop();
                iArr[2] = view.getPaddingRight();
                iArr[3] = view.getPaddingBottom();
                return;
            }
            getVlogPlayViewPadding(iArr, view.getParent() == null ? null : (View) view.getParent());
        }
    }

    public int appendClips(List<String> list) {
        XmsVideoClip appendVideoClip;
        if (list == null) {
            return -1;
        }
        int size = list.size();
        XmsVideoTrack videoTrack = getVideoTrack(0);
        if (videoTrack == null) {
            return size;
        }
        for (String str : list) {
            DefaultLogger.d("MiVideoSdkManager_", "adding clip %s", str);
            if (!TextUtils.isEmpty(str) && (appendVideoClip = videoTrack.appendVideoClip(str)) != null) {
                frameAdjust(appendVideoClip, this.mCurrentRatioType);
                MiVlogVideoClip miVlogVideoClip = new MiVlogVideoClip(videoTrack, appendVideoClip);
                miVlogVideoClip.setOriginDuration(miVlogVideoClip.getTrimDuration());
                this.mVideoClips.add(miVlogVideoClip);
                this.mVideoClipMap.put(miVlogVideoClip.getTag(), miVlogVideoClip);
                size--;
            }
        }
        if (size > 0) {
            Context context = this.mContext;
            ToastUtils.makeText(context, String.format(context.getResources().getString(R$string.vlog_filter_no_support_nums), Integer.valueOf(size)));
        }
        return size;
    }

    public int appendClipParcels(List<VideoClip> list) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        int size = list.size();
        XmsVideoTrack videoTrack = getVideoTrack(0);
        if (videoTrack == null) {
            return size;
        }
        for (VideoClip videoClip : list) {
            XmsVideoClip appendVideoClip = videoTrack.appendVideoClip(videoClip.getFilePath());
            if (appendVideoClip != null) {
                frameAdjust(appendVideoClip, this.mCurrentRatioType);
                MiVlogVideoClip miVlogVideoClip = new MiVlogVideoClip(videoTrack, appendVideoClip);
                miVlogVideoClip.setOriginDuration(miVlogVideoClip.getTrimDuration());
                miVlogVideoClip.changeTrimInPoint(videoClip.getTrimIn(), true);
                miVlogVideoClip.changeTrimOutPoint(videoClip.getTrimOut(), true);
                miVlogVideoClip.setWidth(videoClip.getWidth());
                miVlogVideoClip.setHeight(videoClip.getHeight());
                miVlogVideoClip.setVolumeGain(100.0f, 100.0f);
                this.mVideoClips.add(miVlogVideoClip);
                this.mVideoClipMap.put(miVlogVideoClip.getTag(), miVlogVideoClip);
                size--;
            }
        }
        if (size > 0) {
            Context context = this.mContext;
            ToastUtils.makeText(context, String.format(context.getResources().getString(R$string.vlog_filter_no_support_nums), Integer.valueOf(size)));
        }
        return size;
    }

    public int appendClip(String str, long j, long j2, float f, float f2, List<FilterBeanProcessed> list) {
        XmsVideoTrack videoTrack;
        XmsVideoClip appendVideoClip;
        if (TextUtils.isEmpty(str) || (videoTrack = getVideoTrack(0)) == null || (appendVideoClip = videoTrack.appendVideoClip(str)) == null) {
            return -1;
        }
        MiVlogVideoClip miVlogVideoClip = new MiVlogVideoClip(videoTrack, appendVideoClip);
        frameAdjust(appendVideoClip, this.mCurrentRatioType);
        appendClipEffect(appendVideoClip, list);
        miVlogVideoClip.setOriginDuration(miVlogVideoClip.getTrimDuration());
        miVlogVideoClip.changeTrimInPoint(j, true);
        miVlogVideoClip.changeTrimOutPoint(j2, true);
        miVlogVideoClip.changeVariableSpeed(f, f2, true);
        miVlogVideoClip.setVolumeGain(0.0f, 0.0f);
        this.mVideoClips.add(miVlogVideoClip);
        this.mVideoClipMap.put(miVlogVideoClip.getTag(), miVlogVideoClip);
        return 0;
    }

    public int appendClip(String str) {
        XmsVideoTrack videoTrack;
        XmsVideoClip appendVideoClip;
        if (TextUtils.isEmpty(str) || (videoTrack = getVideoTrack(0)) == null || (appendVideoClip = videoTrack.appendVideoClip(str)) == null) {
            return 1;
        }
        frameAdjust(appendVideoClip, this.mCurrentRatioType);
        MiVlogVideoClip miVlogVideoClip = new MiVlogVideoClip(videoTrack, appendVideoClip);
        miVlogVideoClip.setOriginDuration(miVlogVideoClip.getTrimDuration());
        this.mVideoClips.add(miVlogVideoClip);
        this.mVideoClipMap.put(miVlogVideoClip.getTag(), miVlogVideoClip);
        return 0;
    }

    public void removeSubTrack() {
        this.mSubTrackVideoPathList.clear();
    }

    public int addSubTrackClip(String str, long j, long j2, long j3, float f, float f2, String str2) {
        if (TextUtils.isEmpty(str)) {
            return 1;
        }
        this.mSubTrackVideoPathList.add(str);
        return 0;
    }

    public int appendClips(int i, List<String> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return -1;
        }
        int size = list.size();
        XmsVideoTrack videoTrack = getVideoTrack(0);
        if (videoTrack == null) {
            return size;
        }
        for (String str : list) {
            if (!TextUtils.isEmpty(str)) {
                DefaultLogger.d("MiVideoSdkManager_", "appendClips %s", str);
                XmsVideoClip insertClip = videoTrack.insertClip(i, str);
                if (insertClip != null) {
                    frameAdjust(insertClip, this.mCurrentRatioType);
                    MiVlogVideoClip miVlogVideoClip = new MiVlogVideoClip(videoTrack, insertClip);
                    miVlogVideoClip.setOriginDuration(miVlogVideoClip.getTrimDuration());
                    if (i == getClipCount() - 1) {
                        this.mVideoClips.add(miVlogVideoClip);
                    } else {
                        this.mVideoClips.add(i, miVlogVideoClip);
                    }
                    this.mVideoClipMap.put(miVlogVideoClip.getTag(), miVlogVideoClip);
                    size--;
                    i++;
                }
            }
        }
        if (size > 0) {
            Context context = this.mContext;
            ToastUtils.makeText(context, String.format(context.getResources().getString(R$string.vlog_filter_no_support_nums), Integer.valueOf(size)));
        }
        return size;
    }

    public void onTimeLineDurationChanged() {
        IAudioManager iAudioManager = (IAudioManager) getManagerService(2);
        if (iAudioManager != null) {
            iAudioManager.onTimelineDurationChanged();
        }
        List<OnDurationChangeListener> list = this.mDurationChangeListeners;
        if (list != null) {
            for (OnDurationChangeListener onDurationChangeListener : list) {
                onDurationChangeListener.onDurationChanged();
            }
        }
    }

    public final boolean removeClip(int i, boolean z) {
        XmsVideoTrack videoTrack = getVideoTrack(0);
        videoTrack.removeClip(videoTrack.getVideoClip(i));
        return true;
    }

    public IVideoClip getClipByTimelinePosition(long j) {
        XmsVideoClip currentClip = this.mXmsTimeline.getVideoTrack(0).getCurrentClip(j / 1000);
        if (currentClip.getIndex() < 0 || currentClip.getIndex() >= this.mVideoClips.size()) {
            return null;
        }
        return this.mVideoClips.get(currentClip.getIndex());
    }

    public void removeAllClips() {
        XmsVideoTrack videoTrack;
        this.mSubTrackVideoPathList.clear();
        this.mVideoClips.clear();
        this.mVideoClipMap.clear();
        for (int i = 0; i < 3 && (videoTrack = getVideoTrack(i)) != null; i++) {
            videoTrack.removeAllClips();
        }
    }

    public final XmsVideoTrack appendVideoTrack() {
        XmsTimeline xmsTimeline = this.mXmsTimeline;
        if (xmsTimeline == null) {
            return null;
        }
        return xmsTimeline.appendVideoTrack();
    }

    public int getClipCount() {
        return getVideoTrack(0).getCount();
    }

    public final XmsAudioTrack appendAudioTrack() {
        XmsTimeline xmsTimeline = this.mXmsTimeline;
        if (xmsTimeline == null) {
            return null;
        }
        return xmsTimeline.appendAudioTrack();
    }

    public final XmsVideoTrack getVideoTrack(int i) {
        return this.mXmsTimeline.getVideoTrack(i);
    }

    public void pause() {
        if (this.mXmsTimeline.getStatus() != 2) {
            DefaultLogger.d("MiVideoSdkManager_", "pause timeline");
            this.mXmsContext.pause(this.mXmsTimeline);
        }
    }

    public void prepare(int i) {
        this.mXmsContext.prepareTimeline(this.mXmsTimeline, i);
    }

    public void seek(long j) {
        DefaultLogger.d("MiVideoSdkManager_", "seek timeline %d", Long.valueOf(j));
        pause();
        this.mXmsContext.seekTimeline(this.mXmsTimeline, (int) (j / 1000), 0);
    }

    public void flushTimeline() {
        DefaultLogger.d("MiVideoSdkManager_", "flush timeline");
        this.mXmsContext.flushTimeline(this.mXmsTimeline);
    }

    public void play() {
        DefaultLogger.d("MiVideoSdkManager_", "play timeline");
        if (this.mXmsTimeline.getStatus() == 0) {
            playbackTimeline(0L);
        } else {
            resume();
        }
    }

    public void disconnect() {
        if (!isIgnoreDisconnectAndReconnect()) {
            DefaultLogger.w("MiVideoSdkManager_", "disconnect timeline");
            this.mXmsTimeline.stop();
        }
    }

    public void reconnect() {
        if (!isIgnoreDisconnectAndReconnect()) {
            DefaultLogger.w("MiVideoSdkManager_", "reconnect timeline");
            this.mXmsTimeline.reconnect();
        }
    }

    public boolean isPlayEnd() {
        return getDuration() - getCurrentTimeMicro() <= ((long) (1000000 / (VlogTransFrameUtils.isNeedLowEndFrame() ? 20 : 30)));
    }

    public void resume() {
        DefaultLogger.d("MiVideoSdkManager_", "resume timeline %d", Long.valueOf(this.mXmsContext.getTimelineCurrentPosition(this.mXmsTimeline)));
        if (this.mXmsTimeline.getStatus() == 0) {
            playbackTimeline(this.mXmsContext.getTimelineCurrentPosition(this.mXmsTimeline));
        } else if (isPlayEnd()) {
            seek(0L);
            this.mXmsContext.resume(this.mXmsTimeline);
        } else {
            this.mXmsContext.resume(this.mXmsTimeline);
        }
    }

    public boolean isSingleVideoEdit() {
        return ((VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class)).isSingleVideoEdit();
    }

    public void updateLiveWindowLayoutAfterScreenSizeChanged() {
        XmsTextureView xmsTextureView;
        if (this.mIsReleased || (xmsTextureView = this.mLiveWindow) == null || xmsTextureView.getParent() == null) {
            return;
        }
        int[] liveWindowValidSize = getLiveWindowValidSize();
        int[] ratioSize = getRatioSize(liveWindowValidSize[0], liveWindowValidSize[1], this.mAspectRatio);
        ViewGroup.LayoutParams layoutParams = this.mLiveWindow.getLayoutParams();
        layoutParams.width = ratioSize[0];
        layoutParams.height = ratioSize[1];
        this.mLiveWindow.setLayoutParams(layoutParams);
        this.mLiveWindow.getViewTreeObserver().addOnGlobalLayoutListener(this.mLiveWindowLayoutListener);
    }

    public void setOnLiveWindowLayoutUpdatedListener(OnLiveWindowLayoutUpdatedListener onLiveWindowLayoutUpdatedListener) {
        this.mLiveWindowLayoutUpdatedListener = onLiveWindowLayoutUpdatedListener;
    }

    public int[] getRatioSize(int i, int i2, int i3) {
        float f;
        switch (i3) {
            case 0:
                f = 1.7777778f;
                break;
            case 1:
                f = 2.39f;
                break;
            case 2:
                f = 1.0f;
                break;
            case 3:
                f = 2.55f;
                break;
            case 4:
                f = 0.5625f;
                break;
            case 5:
                f = this.mOriginalWidth / this.mOriginalHeight;
                break;
            case 6:
                f = 0.75f;
                break;
            case 7:
                f = 1.3333334f;
                break;
            default:
                f = 0.0f;
                break;
        }
        float f2 = i;
        float f3 = i2;
        if (isWidthLimit(f2, f3, f)) {
            i2 = (int) (f2 / f);
        } else {
            i = (int) (f3 * f);
        }
        return new int[]{(i / 4) * 4, (i2 / 2) * 2};
    }

    public void setLiveWindowRatio(final int i) {
        if (((View) this.mLiveWindow.getParent()) == null) {
            return;
        }
        int[] liveWindowValidSize = getLiveWindowValidSize();
        int[] ratioSize = getRatioSize(liveWindowValidSize[0], liveWindowValidSize[1], i);
        final int i2 = ratioSize[0];
        final int i3 = ratioSize[1];
        XmsTextureView xmsTextureView = this.mLiveWindow;
        if (xmsTextureView != null && xmsTextureView.isCreated()) {
            this.mStateController.setResizeCallback(new ResizeCallback() { // from class: com.miui.gallery.vlog.base.manager.MiVideoSdkManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.vlog.sdk.callbacks.ResizeCallback
                public final void resize(int i4) {
                    MiVideoSdkManager.$r8$lambda$i3ij78jeI4kMwZQKK20fXbF5oIM(MiVideoSdkManager.this, i, i2, i3, i4);
                }
            });
            this.mXmsTimeline.resizeRenderBuffer(i2, i3);
            return;
        }
        updateLiveWindowLayout(i, i2, i3);
    }

    public /* synthetic */ void lambda$setLiveWindowRatio$0(int i, int i2, int i3, int i4) {
        this.mStateController.setResizeCallback(null);
        updateLiveWindowLayout(i, i2, i3);
    }

    public final void updateLiveWindowLayout(int i, int i2, int i3) {
        XmsTextureView xmsTextureView;
        if (!this.mIsReleased && (xmsTextureView = this.mLiveWindow) != null) {
            this.mAspectRatio = i;
            ViewGroup.LayoutParams layoutParams = xmsTextureView.getLayoutParams();
            layoutParams.width = i2;
            layoutParams.height = i3;
            this.mLiveWindow.setLayoutParams(layoutParams);
        }
    }

    public int getCurrentAspectRatio() {
        return this.mAspectRatio;
    }

    public List<IVideoClip> getVideoClips() {
        return this.mVideoClips;
    }

    public List<String> getVideoPathList() {
        ArrayList arrayList = new ArrayList();
        XmsVideoTrack videoTrack = getVideoTrack(0);
        if (videoTrack == null) {
            return arrayList;
        }
        for (XmsVideoClip firstVideoClip = videoTrack.getFirstVideoClip(); firstVideoClip != null; firstVideoClip = videoTrack.getNextVideoClip(firstVideoClip.getIndex())) {
            arrayList.add(firstVideoClip.getSourcePath());
        }
        List<String> list = this.mSubTrackVideoPathList;
        if (list != null && list.size() > 0) {
            arrayList.addAll(this.mSubTrackVideoPathList);
        }
        return arrayList;
    }

    public void addPlayCallback(PlaybackCallback playbackCallback) {
        MiVideoStateController miVideoStateController = this.mStateController;
        if (miVideoStateController == null) {
            return;
        }
        miVideoStateController.addPlaybackCallback(playbackCallback);
    }

    public void updateBaseSpeed() {
        for (IVideoClip iVideoClip : this.mVideoClips) {
            if (iVideoClip != null) {
                iVideoClip.updateBaseSpeed();
            }
        }
    }

    public void removePlayCallback(PlaybackCallback playbackCallback) {
        MiVideoStateController miVideoStateController = this.mStateController;
        if (miVideoStateController == null) {
            return;
        }
        miVideoStateController.removePlaybackCallback(playbackCallback);
    }

    public void setSeekCallback(SeekCallback seekCallback) {
        MiVideoStateController miVideoStateController = this.mStateController;
        if (miVideoStateController == null) {
            return;
        }
        miVideoStateController.setSeekCallback(seekCallback);
    }

    public void setExportCallback2(ExportCallback2 exportCallback2) {
        this.mExportCallback2 = exportCallback2;
    }

    public void removeExportCallback2() {
        this.mExportCallback2 = null;
    }

    public synchronized <T> T getManagerService(int i) {
        switch (i) {
            case 0:
                if (this.mFilterManager == null) {
                    this.mFilterManager = new MiVideoFilterManager(this, this.mXmsTimeline);
                }
                return (T) this.mFilterManager;
            case 1:
                if (this.mCaptionManger == null) {
                    this.mCaptionManger = new MiVideoCaptionManager(this, this.mXmsTimeline);
                }
                return (T) this.mCaptionManger;
            case 2:
                if (this.mAudioManager == null) {
                    this.mAudioManager = new MiVideoAudioManager(this, this.mXmsTimeline);
                }
                return (T) this.mAudioManager;
            case 3:
                if (this.mClipManager == null) {
                    this.mClipManager = new MiVideoClipManager(this, this.mXmsTimeline);
                }
                return (T) this.mClipManager;
            case 4:
                if (this.mTemplateManager == null) {
                    this.mTemplateManager = new MiVideoTemplateFilesManager(this.mContext);
                }
                return (T) this.mTemplateManager;
            case 5:
            case 6:
            default:
                return null;
            case 7:
                if (this.mClipAudioManager == null) {
                    this.mClipAudioManager = new MiVideoClipAudioManager(this, this.mXmsTimeline);
                }
                return (T) this.mClipAudioManager;
            case 8:
                if (this.mHeaderTailManager == null) {
                    this.mHeaderTailManager = new MiVideoHeaderTailManager(this, this.mXmsTimeline);
                }
                return (T) this.mHeaderTailManager;
            case 9:
                if (this.mTransManager == null) {
                    this.mTransManager = new MiTransResManager(this, this.mXmsTimeline);
                }
                return (T) this.mTransManager;
            case 10:
                if (this.mRatioManager == null) {
                    this.mRatioManager = new MiVideoRatioManager(this, null);
                }
                return (T) this.mRatioManager;
        }
    }

    public void export(String str, CompileCallback compileCallback) {
        MiVideoStateController miVideoStateController = this.mStateController;
        if (miVideoStateController != null) {
            miVideoStateController.setCompileCallback(compileCallback);
        }
        ExportCallback2 exportCallback2 = this.mExportCallback2;
        if (exportCallback2 != null) {
            exportCallback2.onExportStart();
        }
        compileFile(0L, getDuration(), str);
    }

    public void cancelExport() {
        XmsContext xmsContext = this.mXmsContext;
        if (xmsContext != null) {
            xmsContext.cancelExport(this.mXmsTimeline);
        }
    }

    public void removeCompileCallback() {
        MiVideoStateController miVideoStateController = this.mStateController;
        if (miVideoStateController != null) {
            miVideoStateController.setCompileCallback(null);
        }
    }

    public final void compileFile(long j, long j2, String str) {
        int i;
        int i2;
        int i3 = 0;
        XmsVideoTrack videoTrack = getVideoTrack(0);
        int count = videoTrack.getCount();
        if (count == 0) {
            DefaultLogger.w("MiVideoSdkManager_", "export empty timeline.");
            return;
        }
        int i4 = Integer.MAX_VALUE;
        VideoSizeInfo videoSizeInfo = new VideoSizeInfo(Integer.MAX_VALUE, Integer.MAX_VALUE);
        double ratio = getRatio(videoTrack.getVideoClip(0));
        if (this.mCurrentRatioType == 1) {
            videoTrack.getVideoClip(0);
            while (i3 < count) {
                XmsVideoClip videoClip = videoTrack.getVideoClip(i3);
                int height = videoClip.getHeight();
                int width = videoClip.getWidth();
                videoSizeInfo = minVideoSize(new VideoSizeInfo((int) ((height / ratio) + 0.5d), height), minVideoSize(new VideoSizeInfo(width, (int) ((width * ratio) + 0.5d)), videoSizeInfo));
                i3++;
            }
        } else {
            videoTrack.getVideoClip(0);
            while (i3 < count) {
                XmsVideoClip videoClip2 = videoTrack.getVideoClip(i3);
                i4 = Math.min(Math.min(i4, videoClip2.getHeight()), videoClip2.getWidth());
                i3++;
            }
            int i5 = i4 <= 720 ? 720 : (i4 > 1080 && i4 <= 2160) ? nexChecker.UHD_HEIGHT : 1080;
            if (ratio >= 1.0d) {
                i = (i5 * 16) / 9;
            } else {
                i = i5;
                i5 = (i5 * 16) / 9;
            }
            videoSizeInfo = minVideoSize(new VideoSizeInfo((int) ((i / ratio) + 0.5d), i), minVideoSize(new VideoSizeInfo(i5, (int) ((i5 * ratio) + 0.5d)), videoSizeInfo));
        }
        int min = Math.min(videoSizeInfo.mWidth, videoSizeInfo.mHeight);
        if (min <= 480) {
            i2 = 6000000;
        } else if (min <= 720) {
            i2 = 12000000;
        } else if (min <= 1080) {
            i2 = 17000000;
        } else if (min <= 2160) {
            i2 = 50000000;
        } else {
            DefaultLogger.w("MiVideoSdkManager_", "huge video %d X %d", Integer.valueOf(videoSizeInfo.mWidth), Integer.valueOf(videoSizeInfo.mHeight));
            i2 = 25000000;
        }
        this.mXmsContext.exportTimeline(this.mXmsTimeline, str, videoSizeInfo.mWidth, videoSizeInfo.mHeight, 30, i2, 1, 44100, 2, 96000);
    }

    public final VideoSizeInfo minVideoSize(VideoSizeInfo videoSizeInfo, VideoSizeInfo videoSizeInfo2) {
        if (videoSizeInfo.mHeight >= videoSizeInfo2.mHeight) {
            videoSizeInfo = videoSizeInfo2;
        }
        int i = videoSizeInfo.mWidth;
        int i2 = videoSizeInfo.mHeight;
        if (i < i2 && i < 120) {
            videoSizeInfo.mWidth = 120;
            videoSizeInfo.mHeight = (((int) (120.0f / (i / i2))) / 4) * 4;
        } else if (i2 < i && i2 < 120) {
            videoSizeInfo.mHeight = 120;
            videoSizeInfo.mWidth = (((int) (120 * (i / i2))) / 4) * 4;
        }
        return videoSizeInfo;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final double getRatio(XmsVideoClip xmsVideoClip) {
        int i = this.mAspectRatio;
        switch (i) {
            case 0:
                break;
            case 1:
                return 0.41841004184100417d;
            case 2:
                return 1.0d;
            case 3:
                return 0.3921568627450981d;
            case 4:
                return 1.7777777777777777d;
            case 5:
                return (xmsVideoClip.getHeight() * 1.0d) / xmsVideoClip.getWidth();
            case 6:
                return 1.3333333333333333d;
            case 7:
                return 0.75d;
            default:
                DefaultLogger.w("MiVideoSdkManager_", "unknown ratio. default to 16/9. %d", Integer.valueOf(i));
                break;
        }
        return 0.5625d;
    }

    public final void releaseTimeline() {
        XmsTimeline xmsTimeline = this.mXmsContext.getXmsTimeline();
        if (xmsTimeline != null) {
            this.mXmsContext.removeTimeline(xmsTimeline);
        }
    }

    public final void releaseXmsContext() {
        this.mXmsContext.unRegisterMessageHandler();
        this.mXmsContext.release();
    }

    public void onDestroy() {
        DefaultLogger.d("MiVideoSdkManager_", "onDestroy %d", Integer.valueOf(INIT_COUNT));
        this.mLiveWindowValidSize = null;
        this.mVideoClips.clear();
        this.mVideoClipMap.clear();
        ClipReverseHelper clipReverseHelper = this.mClipReverseHelper;
        if (clipReverseHelper != null) {
            clipReverseHelper.release();
        }
        MiVideoFrameLoader miVideoFrameLoader = this.mVideoFrameLoader;
        if (miVideoFrameLoader != null) {
            miVideoFrameLoader.release();
        }
        List<OnDurationChangeListener> list = this.mDurationChangeListeners;
        if (list != null) {
            list.clear();
            this.mDurationChangeListeners = null;
        }
        this.mVideoFrameLoader = null;
        this.mClipReverseHelper = null;
        VideoClipsManager videoClipsManager = this.mVideoClipsManager;
        if (videoClipsManager != null) {
            videoClipsManager.release();
            this.mVideoClipsManager = null;
        }
        ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).release();
        MiVideoTimelineCallback miVideoTimelineCallback = this.mMiVideoTimelineCallback;
        if (miVideoTimelineCallback != null) {
            miVideoTimelineCallback.release();
        }
        int i = INIT_COUNT - 1;
        INIT_COUNT = i;
        if (i == 0) {
            DefaultLogger.d("MiVideoSdkManager_", "release timeline");
            releaseTimeline();
            releaseXmsContext();
        }
        this.mIsReleased = true;
    }

    public boolean isPlay() {
        return this.mXmsTimeline.getStatus() == 1;
    }

    public long getCurrentTimeMicro() {
        return this.mXmsContext.getTimelineCurrentPosition(this.mXmsTimeline) * 1000;
    }

    public int getCurrentIndex() {
        IVideoClip clipByTimelinePosition = getClipByTimelinePosition(getCurrentTimeMicro());
        if (clipByTimelinePosition == null) {
            return 0;
        }
        return clipByTimelinePosition.getIndex();
    }

    public ArrayList<VideoClipInfo> getThumbnailImages() {
        XmsVideoTrack videoTrack;
        if (this.mXmsTimeline == null || (videoTrack = getVideoTrack(0)) == null) {
            return null;
        }
        ArrayList<VideoClipInfo> arrayList = new ArrayList<>();
        for (XmsVideoClip firstVideoClip = videoTrack.getFirstVideoClip(); firstVideoClip != null; firstVideoClip = videoTrack.getNextVideoClip(firstVideoClip.getIndex())) {
            int index = firstVideoClip.getIndex();
            VideoClipInfo videoClipInfo = new VideoClipInfo();
            videoClipInfo.mediaFilePath = firstVideoClip.getSourcePath();
            videoClipInfo.trimIn = firstVideoClip.getIn() * 1000;
            videoClipInfo.trimOut = firstVideoClip.getOut() * 1000;
            videoClipInfo.inPoint = videoTrack.getClipStartPos(index) * 1000;
            videoClipInfo.outPoint = (videoTrack.getClipStartPos(index) + firstVideoClip.getDuration()) * 1000;
            arrayList.add(videoClipInfo);
        }
        return arrayList;
    }

    public long getDuration() {
        XmsVideoTrack videoTrack = this.mXmsTimeline.getVideoTrack(0);
        if (videoTrack == null) {
            return 0L;
        }
        return videoTrack.getDuration() * 1000;
    }

    public void setTimelineInout(long j, long j2, boolean z) {
        this.mXmsTimeline.setInAndOut(j, j2, z);
        this.mXmsContext.seekTimeline(this.mXmsTimeline, 0L, 0);
    }

    public void resetTimelineInout() {
        this.mXmsTimeline.resetInAndOut();
    }

    public boolean playbackTimeline() {
        this.mXmsContext.resume(this.mXmsTimeline);
        return true;
    }

    public boolean playAndStopTimeline(long j) {
        this.mXmsContext.prepareTimeline(this.mXmsTimeline, (int) j);
        return true;
    }

    public boolean playbackTimeline(long j) {
        this.mXmsContext.playTimeline(this.mXmsTimeline, (int) j);
        return true;
    }

    public void buildTransitions(int i, String str, String str2) {
        XmsVideoTrack videoTrack;
        if (i >= 0 && (videoTrack = getVideoTrack(0)) != null) {
            if (i < this.mVideoClips.size()) {
                this.mVideoClips.get(i).setTrans(str, str2);
            }
            videoTrack.setVideoTransition(i, 900L, str, str2);
        }
    }

    public boolean removeVideo(int i) {
        return this.mVideoClipsManager.removeVideo(i);
    }

    public IVideoClip getVideoClip(int i) {
        return this.mVideoClipsManager.getVideoClip(i);
    }

    public boolean splitClip(int i, long j) {
        return this.mVideoClipsManager.splitClip(i, j);
    }

    public void reverseClip(int i, ClipMenuPresenter.CancelReverseCallback cancelReverseCallback) {
        this.mVideoClipsManager.reverseClip(i, cancelReverseCallback);
    }

    public VideoFrameLoader getVideoFrameLoader() {
        if (this.mVideoFrameLoader == null) {
            this.mVideoFrameLoader = new MiVideoFrameLoader();
        }
        return this.mVideoFrameLoader;
    }

    public boolean removeAudio(int i) {
        XmsAudioTrack audioTrack = this.mXmsTimeline.getAudioTrack(i);
        if (audioTrack == null) {
            return false;
        }
        audioTrack.removeAllClips();
        return true;
    }

    public void addDurationChangeListener(OnDurationChangeListener onDurationChangeListener) {
        if (this.mDurationChangeListeners == null) {
            this.mDurationChangeListeners = new ArrayList();
        }
        this.mDurationChangeListeners.add(onDurationChangeListener);
    }

    public void setIVideoClipReverse(IVideoClipReverseCallback iVideoClipReverseCallback) {
        this.mVideoClipsManager.setIVideoClipReverse(iVideoClipReverseCallback);
    }

    /* loaded from: classes2.dex */
    public class VideoClipsManager {
        public IVideoClipReverseCallback mIVideoClipReverseCallback;
        public Disposable mReverseDisposable;
        public XmsVideoTrack mVideoTrack;

        public static /* synthetic */ void $r8$lambda$7L7G3HaqUq339eNvb7Y9UUVrj9c(VideoClipsManager videoClipsManager, int i, IVideoClip iVideoClip, long j, long j2, ObservableEmitter observableEmitter) {
            videoClipsManager.lambda$reverseClip$1(i, iVideoClip, j, j2, observableEmitter);
        }

        /* renamed from: $r8$lambda$DwwoXhZvHocYld7xIlC-ZCJhHfU */
        public static /* synthetic */ void m1774$r8$lambda$DwwoXhZvHocYld7xIlCZCJhHfU(VideoClipsManager videoClipsManager, String str, ObservableEmitter observableEmitter) {
            videoClipsManager.lambda$removeVideo$0(str, observableEmitter);
        }

        public static /* synthetic */ void $r8$lambda$wlWK_HJdEyKoVJuH2YcFrL4H_yo(VideoClipsManager videoClipsManager, ObservableEmitter observableEmitter) {
            videoClipsManager.lambda$deleteTempReverseVideoClips$2(observableEmitter);
        }

        public void setIVideoClipReverse(IVideoClipReverseCallback iVideoClipReverseCallback) {
            this.mIVideoClipReverseCallback = iVideoClipReverseCallback;
        }

        public VideoClipsManager() {
            MiVideoSdkManager.this = r1;
        }

        public void setVideoTrack(XmsVideoTrack xmsVideoTrack) {
            this.mVideoTrack = xmsVideoTrack;
        }

        public IVideoClip getVideoClip(int i) {
            if (!BaseMiscUtil.isValid(MiVideoSdkManager.this.mVideoClips) || i < 0 || i >= MiVideoSdkManager.this.mVideoClips.size()) {
                return null;
            }
            return (IVideoClip) MiVideoSdkManager.this.mVideoClips.get(i);
        }

        public void updateClipsAfterSplit(int i, MiVlogVideoClip miVlogVideoClip) {
            if (miVlogVideoClip == null) {
                return;
            }
            BaseVideoClip.BaseInfo baseInfo = miVlogVideoClip.getBaseInfo();
            int i2 = i + 1;
            MiVlogVideoClip miVlogVideoClip2 = new MiVlogVideoClip(this.mVideoTrack, this.mVideoTrack.getVideoClip(i));
            if (baseInfo != null) {
                miVlogVideoClip2.setOriginDuration(baseInfo.getOriginDuration());
                miVlogVideoClip2.updateBaseInfo(baseInfo);
            }
            miVlogVideoClip2.setIsCuted(true);
            MiVideoSdkManager.this.mVideoClips.set(i, miVlogVideoClip2);
            MiVideoSdkManager.this.mVideoClipMap.put(miVlogVideoClip2.getTag(), miVlogVideoClip2);
            MiVlogVideoClip miVlogVideoClip3 = new MiVlogVideoClip(this.mVideoTrack, this.mVideoTrack.getVideoClip(i2));
            if (baseInfo != null) {
                miVlogVideoClip3.setOriginDuration(baseInfo.getOriginDuration());
                miVlogVideoClip3.updateBaseInfo(baseInfo);
            }
            miVlogVideoClip3.setIsCuted(true);
            miVlogVideoClip3.setPlayInReverse(miVlogVideoClip2.isInReverse());
            miVlogVideoClip3.setTrans(miVlogVideoClip.getTransName(), miVlogVideoClip.getTransParam());
            MiVideoSdkManager.this.mVideoClips.add(i2, miVlogVideoClip3);
            MiVideoSdkManager.this.mVideoClipMap.put(miVlogVideoClip3.getTag(), miVlogVideoClip3);
        }

        public boolean removeVideo(int i) {
            boolean removeClipFromTrack = removeClipFromTrack(i, (IVideoClip) MiVideoSdkManager.this.mVideoClips.get(i), false);
            final String str = "";
            if (removeClipFromTrack) {
                IVideoClip videoClip = getVideoClip(i);
                if (videoClip.getReverseTag() != null) {
                    IVideoClip iVideoClip = (IVideoClip) MiVideoSdkManager.this.mVideoClipMap.get(videoClip.getReverseTag());
                    if (iVideoClip != null) {
                        str = iVideoClip.getFilePath();
                        MiVideoSdkManager.this.mVideoClipMap.remove(iVideoClip.getTag());
                    }
                    if (((IVideoClip) MiVideoSdkManager.this.mVideoClipMap.get(videoClip.getTag())) != null) {
                        MiVideoSdkManager.this.mVideoClipMap.remove(videoClip.getTag());
                    }
                }
                if (videoClip.isInReverse() && videoClip.getOriginTag() != null) {
                    if (((IVideoClip) MiVideoSdkManager.this.mVideoClipMap.get(videoClip.getOriginTag())) != null) {
                        MiVideoSdkManager.this.mVideoClipMap.remove(videoClip.getOriginTag());
                    }
                    IVideoClip iVideoClip2 = (IVideoClip) MiVideoSdkManager.this.mVideoClipMap.get(videoClip.getTag());
                    if (iVideoClip2 != null) {
                        str = iVideoClip2.getFilePath();
                        MiVideoSdkManager.this.mVideoClipMap.remove(videoClip.getTag());
                    }
                }
                MiVideoSdkManager.this.mVideoClips.remove(i);
            }
            if (!TextUtils.isEmpty(str)) {
                Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.base.manager.MiVideoSdkManager$VideoClipsManager$$ExternalSyntheticLambda2
                    @Override // io.reactivex.ObservableOnSubscribe
                    public final void subscribe(ObservableEmitter observableEmitter) {
                        MiVideoSdkManager.VideoClipsManager.m1774$r8$lambda$DwwoXhZvHocYld7xIlCZCJhHfU(MiVideoSdkManager.VideoClipsManager.this, str, observableEmitter);
                    }
                }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe();
            }
            return removeClipFromTrack;
        }

        public /* synthetic */ void lambda$removeVideo$0(String str, ObservableEmitter observableEmitter) throws Exception {
            observableEmitter.onNext(Boolean.valueOf(deleteReverseFile(str)));
        }

        public final boolean deleteReverseFile(String str) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("MiVideoSdkManager_", "deleteReverseFile"));
            return documentFile != null && documentFile.delete();
        }

        public boolean splitClip(int i, long j) {
            IVideoClip videoClip = getVideoClip(i);
            if (videoClip == null) {
                return false;
            }
            videoClip.printTimeInfo("splitClip-->" + j);
            this.mVideoTrack.splitClip(i, ((long) (((((double) ((j / 1000) - this.mVideoTrack.getClipStartPos(i))) * 1.0d) / ((double) videoClip.getTimelineDuration())) * ((double) videoClip.getTrimDuration()))) / 1000);
            updateClipsAfterSplit(i, (MiVlogVideoClip) videoClip);
            return true;
        }

        public final boolean removeClipFromTrack(int i, IVideoClip iVideoClip, boolean z) {
            boolean removeClip = MiVideoSdkManager.this.removeClip(i, z);
            iVideoClip.setDeleted(removeClip);
            return removeClip;
        }

        public void reverseClip(final int i, final ClipMenuPresenter.CancelReverseCallback cancelReverseCallback) {
            DefaultLogger.d("MiVideoSdkManager_", "reverseClip index: %d", Integer.valueOf(i));
            final IVideoClip videoClip = getVideoClip(i);
            if (!videoClip.isInReverse() && videoClip.getReverseTag() != null) {
                double speed = videoClip.getSpeed();
                IVideoClip iVideoClip = (IVideoClip) MiVideoSdkManager.this.mVideoClipMap.get(videoClip.getReverseTag());
                if (Math.abs(videoClip.getTrimDuration() - videoClip.getTag().getTrimDuration()) == 0) {
                    DefaultLogger.d("MiVideoSdkManager_", "the reverse clip is existed.");
                    removeClipFromTrack(i, videoClip, false);
                    XmsVideoClip insertClip = this.mVideoTrack.insertClip(i, iVideoClip.getFilePath());
                    MiVideoSdkManager miVideoSdkManager = MiVideoSdkManager.this;
                    miVideoSdkManager.frameAdjust(insertClip, miVideoSdkManager.mCurrentRatioType);
                    iVideoClip.rebuild(insertClip);
                    if (speed != iVideoClip.getSpeed()) {
                        iVideoClip.changeSpeed(videoClip.getTag().mSpeed);
                    }
                    iVideoClip.setVolumeGain(0.0f, 0.0f);
                    iVideoClip.changeTrimInPoint(iVideoClip.getTag().mTrimIn, true);
                    iVideoClip.changeTrimOutPoint(iVideoClip.getTag().mTrimOut, true);
                    iVideoClip.setDeleted(false);
                    MiVideoSdkManager.this.mVideoClips.set(i, iVideoClip);
                    MiVideoSdkManager.this.mVideoClipMap.put(iVideoClip.getTag(), iVideoClip);
                    IVideoClipReverseCallback iVideoClipReverseCallback = this.mIVideoClipReverseCallback;
                    if (iVideoClipReverseCallback == null) {
                        return;
                    }
                    iVideoClipReverseCallback.onSuccess(iVideoClip);
                    return;
                }
            }
            if (videoClip.isInReverse() && videoClip.getOriginTag() != null) {
                double speed2 = videoClip.getSpeed();
                removeClipFromTrack(i, videoClip, false);
                IVideoClip iVideoClip2 = (IVideoClip) MiVideoSdkManager.this.mVideoClipMap.get(videoClip.getOriginTag());
                XmsVideoClip insertClip2 = this.mVideoTrack.insertClip(i, iVideoClip2.getFilePath());
                MiVideoSdkManager miVideoSdkManager2 = MiVideoSdkManager.this;
                miVideoSdkManager2.frameAdjust(insertClip2, miVideoSdkManager2.mCurrentRatioType);
                iVideoClip2.rebuild(insertClip2);
                iVideoClip2.setVolumeGain(100.0f, 100.0f);
                if (speed2 != iVideoClip2.getSpeed()) {
                    iVideoClip2.changeSpeed(videoClip.getTag().mSpeed);
                    if (iVideoClip2.isChangeSpeed()) {
                        iVideoClip2.setVolumeGain(0.0f, 0.0f);
                    }
                }
                iVideoClip2.changeTrimInPoint(iVideoClip2.getTag().mTrimIn, true);
                iVideoClip2.changeTrimOutPoint(iVideoClip2.getTag().mTrimOut, true);
                iVideoClip2.setDeleted(false);
                MiVideoSdkManager.this.mVideoClips.set(i, iVideoClip2);
                MiVideoSdkManager.this.mVideoClipMap.put(iVideoClip2.getTag(), iVideoClip2);
                IVideoClipReverseCallback iVideoClipReverseCallback2 = this.mIVideoClipReverseCallback;
                if (iVideoClipReverseCallback2 == null) {
                    return;
                }
                iVideoClipReverseCallback2.onSuccess(iVideoClip2);
                return;
            }
            Disposable disposable = this.mReverseDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                this.mReverseDisposable.dispose();
            }
            MiVideoSdkManager miVideoSdkManager3 = MiVideoSdkManager.this;
            miVideoSdkManager3.mClipReverseHelper = new ClipReverseHelper(miVideoSdkManager3.mContext);
            MiVideoSdkManager.this.mClipReverseHelper.setCallback(new ClipReverseHelper.Callback() { // from class: com.miui.gallery.vlog.base.manager.MiVideoSdkManager.VideoClipsManager.1
                {
                    VideoClipsManager.this = this;
                }

                @Override // com.miui.gallery.vlog.clip.ClipReverseHelper.Callback
                public void onSuccess(String str, String str2, int i2) {
                    DefaultLogger.d("MiVideoSdkManager_", "errorCode is %s , srcFile is %s , dstFile is %s", Integer.valueOf(i2), str, str2);
                    int index = MiVideoSdkManager.this.mClipReverseHelper.getIndex();
                    IVideoClip iVideoClip3 = (IVideoClip) MiVideoSdkManager.this.mVideoClips.get(index);
                    iVideoClip3.updateTagInfo();
                    double speed3 = iVideoClip3.getSpeed();
                    double baseInfoSpeed = iVideoClip3.getBaseInfoSpeed();
                    VideoClipsManager.this.removeClipFromTrack(index, iVideoClip3, false);
                    XmsVideoClip insertClip3 = VideoClipsManager.this.mVideoTrack.insertClip(index, str2);
                    MiVideoSdkManager miVideoSdkManager4 = MiVideoSdkManager.this;
                    miVideoSdkManager4.frameAdjust(insertClip3, miVideoSdkManager4.mCurrentRatioType);
                    MiVlogVideoClip miVlogVideoClip = new MiVlogVideoClip(VideoClipsManager.this.mVideoTrack, insertClip3);
                    miVlogVideoClip.setTrans(iVideoClip3.getTransName(), iVideoClip3.getTransParam());
                    miVlogVideoClip.setBaseInfoSpeed(baseInfoSpeed);
                    if (speed3 != miVlogVideoClip.getSpeed()) {
                        miVlogVideoClip.changeSpeed(iVideoClip3.getTag().mSpeed);
                    }
                    if (iVideoClip3.isCuted()) {
                        miVlogVideoClip.setIsCuted(true);
                    }
                    miVlogVideoClip.setPlayInReverse(!iVideoClip3.getTag().mIsReverse);
                    miVlogVideoClip.setOriginDuration(miVlogVideoClip.getTrimDuration());
                    miVlogVideoClip.setVolumeGain(0.0f, 0.0f);
                    miVlogVideoClip.updateTagInfo();
                    miVlogVideoClip.getTag().setReverseTagTrimDuration(iVideoClip3.getTag().getTrimDuration());
                    iVideoClip3.getTag().setReverseTagTrimDuration(miVlogVideoClip.getTag().getTrimDuration());
                    if (miVlogVideoClip.isInReverse()) {
                        miVlogVideoClip.setOriginTag(iVideoClip3.getTag());
                    } else {
                        miVlogVideoClip.setReverseTag(iVideoClip3.getTag());
                    }
                    if (iVideoClip3.getTag().mIsReverse) {
                        iVideoClip3.setOriginTag(miVlogVideoClip.getTag());
                    } else {
                        iVideoClip3.setReverseTag(miVlogVideoClip.getTag());
                    }
                    MiVideoSdkManager.this.mVideoClips.set(index, miVlogVideoClip);
                    MiVideoSdkManager.this.mVideoClipMap.put(miVlogVideoClip.getTag(), miVlogVideoClip);
                    if (VideoClipsManager.this.mIVideoClipReverseCallback != null) {
                        VideoClipsManager.this.mIVideoClipReverseCallback.onSuccess(miVlogVideoClip);
                    }
                    VideoClipsManager.this.releaseClipReverser();
                }

                @Override // com.miui.gallery.vlog.clip.ClipReverseHelper.Callback
                public void onFail(String str, String str2, int i2) {
                    if (VideoClipsManager.this.mIVideoClipReverseCallback != null) {
                        VideoClipsManager.this.mIVideoClipReverseCallback.onFail();
                    }
                    VideoClipsManager.this.releaseClipReverser();
                }

                @Override // com.miui.gallery.vlog.clip.ClipReverseHelper.Callback
                public void onCancel() {
                    ClipMenuPresenter.CancelReverseCallback cancelReverseCallback2 = cancelReverseCallback;
                    if (cancelReverseCallback2 != null) {
                        cancelReverseCallback2.onCancel();
                    }
                }
            });
            final long trimInWithTrans = videoClip.getTrimInWithTrans();
            final long trimOutWithTrans = videoClip.getTrimOutWithTrans();
            this.mReverseDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.base.manager.MiVideoSdkManager$VideoClipsManager$$ExternalSyntheticLambda1
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    MiVideoSdkManager.VideoClipsManager.$r8$lambda$7L7G3HaqUq339eNvb7Y9UUVrj9c(MiVideoSdkManager.VideoClipsManager.this, i, videoClip, trimInWithTrans, trimOutWithTrans, observableEmitter);
                }
            }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe();
        }

        public /* synthetic */ void lambda$reverseClip$1(int i, IVideoClip iVideoClip, long j, long j2, ObservableEmitter observableEmitter) throws Exception {
            observableEmitter.onNext(Boolean.valueOf(MiVideoSdkManager.this.mClipReverseHelper.reverseFile(i, iVideoClip.getFilePath(), iVideoClip.hashCode(), j, j2)));
        }

        public final void deleteTempReverseVideoClips() {
            DefaultLogger.d("MiVideoSdkManager_", "delete temp reverse dir files : %s", VlogStorage.getTempReverseFilePath());
            Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.base.manager.MiVideoSdkManager$VideoClipsManager$$ExternalSyntheticLambda0
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    MiVideoSdkManager.VideoClipsManager.$r8$lambda$wlWK_HJdEyKoVJuH2YcFrL4H_yo(MiVideoSdkManager.VideoClipsManager.this, observableEmitter);
                }
            }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe();
        }

        public /* synthetic */ void lambda$deleteTempReverseVideoClips$2(ObservableEmitter observableEmitter) throws Exception {
            observableEmitter.onNext(Boolean.valueOf(deleteTempReverseFolder(VlogStorage.getTempReverseFilePath())));
        }

        public final boolean deleteTempReverseFolder(String str) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("MiVideoSdkManager_", "deleteTempReverseFolder"));
            return documentFile != null && documentFile.delete();
        }

        public void release() {
            deleteTempReverseVideoClips();
            releaseClipReverser();
            this.mIVideoClipReverseCallback = null;
        }

        public void releaseClipReverser() {
            if (MiVideoSdkManager.this.mClipReverseHelper != null) {
                MiVideoSdkManager.this.mClipReverseHelper.setCallback(null);
                MiVideoSdkManager.this.mClipReverseHelper.release();
                MiVideoSdkManager.this.mClipReverseHelper = null;
            }
        }
    }

    public void setRatioType(int i) {
        this.mCurrentRatioType = i;
        XmsVideoTrack videoTrack = getVideoTrack(0);
        if (videoTrack == null) {
            return;
        }
        disconnect();
        for (XmsVideoClip firstVideoClip = videoTrack.getFirstVideoClip(); firstVideoClip != null; firstVideoClip = videoTrack.getNextVideoClip(firstVideoClip.getIndex())) {
            frameAdjust(firstVideoClip, this.mCurrentRatioType);
        }
        reconnect();
    }

    public void frameAdjust(XmsVideoClip xmsVideoClip, int i) {
        XmsVideoFilter effectByName = xmsVideoClip.getEffectByName("movit.filter.frame_adjust");
        if (effectByName == null) {
            effectByName = xmsVideoClip.appendVideoEffect("movit.filter.frame_adjust", "");
        }
        XmsVideoFilter xmsVideoFilter = effectByName;
        if (i == 1) {
            xmsVideoFilter.setDoubleParam("filter.fit.mode", 1.0d);
            return;
        }
        xmsVideoFilter.setDoubleParam("filter.fit.mode", -1.0d);
        xmsVideoFilter.setColorParam("filter.background.color", 0, 0, 0, 255);
    }

    public void appendClipEffect(XmsVideoClip xmsVideoClip, List<FilterBeanProcessed> list) {
        if (xmsVideoClip == null || list == null) {
            return;
        }
        for (FilterBeanProcessed filterBeanProcessed : list) {
            if (!filterBeanProcessed.paramIsFile || new File(filterBeanProcessed.filterParam).exists()) {
                xmsVideoClip.appendVideoEffect(filterBeanProcessed.filterName, filterBeanProcessed.filterParam);
            }
        }
    }

    public int getRatioType() {
        return this.mCurrentRatioType;
    }

    public void doOperationCombined(IDoOperationCombined iDoOperationCombined) {
        if (iDoOperationCombined == null) {
            return;
        }
        disconnect();
        setIgnoreDisconnectAndReconnect(true);
        iDoOperationCombined.combined();
        setIgnoreDisconnectAndReconnect(false);
        reconnect();
    }

    public boolean isIgnoreDisconnectAndReconnect() {
        return this.mIgnoreDisconnectAndReconnect;
    }

    public void setIgnoreDisconnectAndReconnect(boolean z) {
        this.mIgnoreDisconnectAndReconnect = z;
    }

    public XmsTimeline getmXmsTimeline() {
        return this.mXmsTimeline;
    }

    public boolean isReleased() {
        return this.mIsReleased;
    }

    public int getTimelineStatus() {
        return this.mXmsTimeline.getStatus();
    }

    /* loaded from: classes2.dex */
    public class VideoSizeInfo {
        public int mHeight;
        public int mWidth;

        public VideoSizeInfo(int i, int i2) {
            MiVideoSdkManager.this = r1;
            this.mWidth = (i / 4) * 4;
            this.mHeight = (i2 / 4) * 4;
        }
    }
}
