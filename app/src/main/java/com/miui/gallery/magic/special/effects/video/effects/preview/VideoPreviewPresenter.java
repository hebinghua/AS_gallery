package com.miui.gallery.magic.special.effects.video.effects.preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.fetch.VideoResourceFetcher;
import com.miui.gallery.magic.special.effects.video.VideoEffectsActivity;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;
import com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuPresenter;
import com.miui.gallery.magic.special.effects.video.effects.menu.VideoModel;
import com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter;
import com.miui.gallery.magic.special.effects.video.util.ClipReverseHelper;
import com.miui.gallery.magic.special.effects.video.util.SUndoHelper;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicThreadHandler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.magic.widget.ExportVideoFragment;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;
import com.qiniu.pili.droid.shortvideo.PLVideoFrame;
import com.xiaomi.kouren.SystemUtil;
import com.xiaomi.mediaprocess.EffectMediaPlayer;
import com.xiaomi.mediaprocess.EffectNotifier;
import com.xiaomi.mediaprocess.EffectType;
import com.xiaomi.mediaprocess.MediaComposeFile;
import com.xiaomi.mediaprocess.MediaEffect;
import com.xiaomi.mediaprocess.MediaEffectGraph;
import com.xiaomi.mediaprocess.PlayerStatus;
import com.xiaomi.mediaprocess.PreViewStatus;
import com.xiaomi.mediaprocess.SegmentEffectType;
import com.xiaomi.player.enums.PlayerSeekingMode;
import com.xiaomi.stat.b.h;
import com.xiaomi.stat.c.b;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class VideoPreviewPresenter extends BasePresenter<VideoPreviewFragment, VideoPreviewModel, IPreview$P> {
    public static int PREVIEW_SIZE_LEVEL_POS = 3;
    public static final String[] PREVIEW_SIZE_LEVEL_TIPS_ARRAY = {"0.3", "0.4", "0.45", "0.5", "0.55", "0.6", "0.7", "0.8"};
    public static int PREVIEW_SIZE_RATIO_POS;
    public boolean isFileCoposing;
    public VideoEffectsActivity mActivity;
    public ClipReverseHelper mClipReverseHelper;
    public boolean mFilter;
    public int mFixedSizeHeight;
    public int mFixedSizeWidth;
    public boolean mIsStop;
    public MediaEffectGraph mMediaEffectGraph;
    public SurfaceView mPlayerView;
    public SUndoHelper mSUndoHelper;
    public String mStrBackgroundVideo;
    public SurfaceHolder mSurfaceHolderPlayer;
    public int mVideoCutDuration;
    public int mVideoCutEndTime;
    public int mVideoCutStartTime;
    public int mVideoHeight;
    public int mVideoWidth;
    public float startTime;
    public boolean SAVING_BLACK_CLICK = false;
    public final int TARGET_FRAME_WIDTH = 256;
    public final int TARGET_FRAME_HEIGHT = 256;
    public String mStrForegroundVideo = "";
    public String mStrBackgroundAudio = "";
    public long mBgAudioTrackId = -1;
    public long mOriginAudioTrackId = -1;
    public float mBackgroundVideoDuration = 0.0f;
    public float mCurrentVideoDuration = 0.0f;
    public long mCurrentVideoPosition = 0;
    public long mForegroundSourceId = 0;
    public long mEffectId = 0;
    public EffectMediaPlayer mEffectPlayer = null;
    public MediaComposeFile mMediaComposFile = null;
    public long mSegEffectId = -1;
    public boolean mISFirst = true;
    public int lastProcess = 0;
    public long CURRENT_PLAYBACK_TIME = 0;
    public int mCurrentEffectType = -100;
    public String mEffectBasePath = null;

    public static /* synthetic */ void $r8$lambda$b6TRXuVrLlgukZfzoULpA9UXdmM(VideoPreviewPresenter videoPreviewPresenter, boolean z, boolean z2) {
        videoPreviewPresenter.lambda$setGravityPlayerView$0(z, z2);
    }

    public static /* synthetic */ void $r8$lambda$fhS_eq4NyGTScjLPlxFV3Y8jehs(VideoPreviewPresenter videoPreviewPresenter) {
        videoPreviewPresenter.lambda$playerOnlyPausePreView$3();
    }

    /* renamed from: $r8$lambda$juCR-Gt1cylHAGaiLKoDhJAGMwI */
    public static /* synthetic */ void m1068$r8$lambda$juCRGt1cylHAGaiLKoDhJAGMwI(VideoPreviewPresenter videoPreviewPresenter) {
        videoPreviewPresenter.lambda$playerPausePreView$2();
    }

    /* renamed from: $r8$lambda$yVQEC-UM2SX-WJGtp60b6lvdWoU */
    public static /* synthetic */ void m1069$r8$lambda$yVQECUM2SXWJGtp60b6lvdWoU(VideoPreviewPresenter videoPreviewPresenter) {
        videoPreviewPresenter.lambda$constructGraph$1();
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public VideoPreviewModel getModelInstance() {
        return new VideoPreviewModel(this);
    }

    /* renamed from: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IPreview$P {
        public static /* synthetic */ void $r8$lambda$IS5vDINtMP_KMA_ZfI_rDSRC1ew(AnonymousClass1 anonymousClass1) {
            anonymousClass1.lambda$seekToEnd$0();
        }

        public static /* synthetic */ void $r8$lambda$MC_UOEZBSFoECdhPDNH_YHg4RDE(AnonymousClass1 anonymousClass1, int i, int i2, float f) {
            anonymousClass1.lambda$firstFrameFinish$1(i, i2, f);
        }

        public AnonymousClass1() {
            VideoPreviewPresenter.this = r1;
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void onPlayVideo() {
            VideoPreviewPresenter.this.play(true);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void initVideoData(Intent intent, boolean z) {
            VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
            videoPreviewPresenter.mActivity = (VideoEffectsActivity) videoPreviewPresenter.getActivity();
            if (intent.getBooleanExtra("video_slice", false)) {
                VideoPreviewPresenter.this.mStrForegroundVideo = intent.getStringExtra("VideoFile");
            }
            if (TextUtils.isEmpty(VideoPreviewPresenter.this.mStrForegroundVideo)) {
                VideoPreviewPresenter videoPreviewPresenter2 = VideoPreviewPresenter.this;
                videoPreviewPresenter2.mStrForegroundVideo = ((VideoPreviewModel) videoPreviewPresenter2.mModel).getContract().decode(intent.getData());
            }
            VideoPreviewPresenter videoPreviewPresenter3 = VideoPreviewPresenter.this;
            Bitmap firstFrame = videoPreviewPresenter3.getFirstFrame(videoPreviewPresenter3.mStrForegroundVideo);
            if (firstFrame == null) {
                return;
            }
            VideoPreviewPresenter.this.mVideoWidth = firstFrame.getWidth();
            VideoPreviewPresenter.this.mVideoHeight = firstFrame.getHeight();
            if (z) {
                setCoverBitmap(firstFrame);
            } else {
                firstFrame.recycle();
            }
            VideoPreviewPresenter.this.getContract().setVideoTime(VideoPreviewPresenter.this.mCurrentVideoDuration, VideoPreviewPresenter.this.mBackgroundVideoDuration);
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter.1.1
                {
                    AnonymousClass1.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    VideoPreviewPresenter.this.constructGraph();
                }
            });
            new Handler().postDelayed(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter.1.2
                {
                    AnonymousClass1.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (VideoPreviewPresenter.this.getActivityWithSync() == null) {
                        return;
                    }
                    AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                    anonymousClass1.firstFrameFinish(VideoPreviewPresenter.this.mVideoWidth, VideoPreviewPresenter.this.mVideoHeight, VideoPreviewPresenter.this.mBackgroundVideoDuration);
                }
            }, 500L);
        }

        public void setCoverBitmap(Bitmap bitmap) {
            ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().setCoverBitmap(bitmap);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void setSurfaceHolderPlayer(SurfaceHolder surfaceHolder, SurfaceView surfaceView) {
            VideoPreviewPresenter.this.mSurfaceHolderPlayer = surfaceHolder;
            VideoPreviewPresenter.this.mPlayerView = surfaceView;
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public String getVideoPath() {
            return VideoPreviewPresenter.this.mStrForegroundVideo;
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void setVideoTime(float f, float f2) {
            VideoPreviewPresenter.this.mCurrentVideoDuration = f;
            ((VideoMenuPresenter) VideoPreviewPresenter.this.mActivity.mMenuFragment.mPresenter).setVideoTime(f2);
            ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().setVideoTime(VideoPreviewPresenter.this.getGapTime(f), VideoPreviewPresenter.this.getGapTime(f2));
            if (f >= 0.0f) {
                VideoPreviewPresenter.this.getActivity().event(1004, Float.valueOf(f));
            }
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void setThumbnailVisible(boolean z) {
            VideoPreviewFragment videoPreviewFragment;
            WeakReference<V> weakReference = VideoPreviewPresenter.this.mView;
            if (weakReference == 0 || (videoPreviewFragment = (VideoPreviewFragment) weakReference.get()) == null) {
                return;
            }
            videoPreviewFragment.getContract().setThumbnailVisible(z);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void stopVideoPreview() {
            if (VideoPreviewPresenter.this.mEffectPlayer != null) {
                VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
                videoPreviewPresenter.mCurrentVideoPosition = videoPreviewPresenter.mEffectPlayer.GetCurrentPlayingPosition();
                VideoPreviewPresenter.this.mEffectPlayer.StopPreView();
            }
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public String getOutputFilePath() {
            return VideoPreviewPresenter.this.getOutputMediaFilePath();
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public long getTotalTime() {
            return VideoPreviewPresenter.this.mBackgroundVideoDuration;
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void progressToVideoSeek(long j) {
            if (VideoPreviewPresenter.this.mEffectPlayer != null) {
                if (VideoPreviewPresenter.this.mEffectPlayer != null && VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewPlaying) {
                    VideoPreviewPresenter.this.mEffectPlayer.PausePreView();
                }
                VideoPreviewPresenter.this.mFilter = true;
                VideoPreviewPresenter.this.mCurrentVideoDuration = (float) j;
                VideoPreviewPresenter.this.seekTo(j, true);
            }
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void seekToEnd(long j) {
            String gapTime = VideoPreviewPresenter.this.getGapTime(j);
            VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
            ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().setVideoTime(gapTime, videoPreviewPresenter.getGapTime(videoPreviewPresenter.mBackgroundVideoDuration));
            if (VideoPreviewPresenter.this.mEffectPlayer == null || VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus() != PreViewStatus.PreViewPlaying) {
                if (VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewStopped) {
                    return;
                }
                if (VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewPaused) {
                    if (((float) j) >= VideoPreviewPresenter.this.mBackgroundVideoDuration) {
                        return;
                    }
                    ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().resetPlayStopIcon(false);
                    return;
                }
                VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus();
                PreViewStatus preViewStatus = PreViewStatus.PreViewEOF;
                return;
            }
            VideoPreviewPresenter.this.getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass1.$r8$lambda$IS5vDINtMP_KMA_ZfI_rDSRC1ew(VideoPreviewPresenter.AnonymousClass1.this);
                }
            });
        }

        public /* synthetic */ void lambda$seekToEnd$0() {
            VideoPreviewPresenter.this.mEffectPlayer.PausePreView();
            ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().playButtonStatus(true);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void onReceiverFinish() {
            VideoPreviewFragment videoPreviewFragment = (VideoPreviewFragment) VideoPreviewPresenter.this.mView.get();
            if (videoPreviewFragment != null) {
                videoPreviewFragment.getContract().onReceiverFinish();
            }
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void cutVideoSlice(Intent intent) {
            int[] intArrayExtra = intent.getIntArrayExtra("duration");
            String stringExtra = intent.getStringExtra(nexExportFormat.TAG_FORMAT_PATH);
            if (intArrayExtra == null || intArrayExtra.length != 2) {
                if (intArrayExtra != null) {
                    MagicLog magicLog = MagicLog.INSTANCE;
                    magicLog.showLog("MagicLogger VideoPreviewPresenter", "illegal param exception, duration: " + intArrayExtra.length + " file path: " + stringExtra);
                    return;
                }
                MagicLog magicLog2 = MagicLog.INSTANCE;
                magicLog2.showLog("MagicLogger VideoPreviewPresenter", "illegal param exception, file path: " + stringExtra);
                return;
            }
            Bitmap specialFrame = VideoPreviewPresenter.this.getSpecialFrame(stringExtra, intArrayExtra[0]);
            if (specialFrame != null) {
                VideoPreviewPresenter.this.mVideoWidth = specialFrame.getWidth();
                VideoPreviewPresenter.this.mVideoHeight = specialFrame.getHeight();
                setCoverBitmap(specialFrame);
            }
            VideoPreviewPresenter.this.mVideoCutStartTime = intArrayExtra[0];
            VideoPreviewPresenter.this.mVideoCutEndTime = intArrayExtra[1];
            VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
            videoPreviewPresenter.mVideoCutDuration = videoPreviewPresenter.mVideoCutEndTime - VideoPreviewPresenter.this.mVideoCutStartTime;
            VideoPreviewPresenter.this.mStrForegroundVideo = stringExtra;
            initVideoData(intent, false);
            MagicFileUtil.clearTempVideoFiles();
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void setStartSegEffectId(int i, boolean z) {
            if (i == 0) {
                i = -1;
            }
            VideoPreviewPresenter.this.addEffect(i, z);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void onComposeMP4() {
            VideoPreviewPresenter.this.saveComposeMP4();
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void selectAudioFile(String str) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Local music");
            MagicSampler.getInstance().recordCategory("video_post", "music", hashMap);
            VideoPreviewPresenter.this.changeBgAudio(str, false);
            ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().playButtonStatus(false);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void selectAudio(final ListItem listItem) {
            String str;
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "Music Library");
            MagicSampler.getInstance().recordCategory("video_post", "music", hashMap);
            hashMap.clear();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, listItem.getTitle());
            MagicSampler.getInstance().recordCategory("video_post", "music_library", hashMap);
            if (!TextUtils.isEmpty(listItem.getResName())) {
                str = listItem.getResPath() + listItem.getResName();
            } else {
                str = "";
            }
            if (!VideoPreviewPresenter.this.mISFirst) {
                VideoPreviewPresenter.this.changeBgAudio(str, false);
            } else {
                VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
                videoPreviewPresenter.play(videoPreviewPresenter.mISFirst);
                new Handler().postDelayed(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter.1.3
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (TextUtils.isEmpty(listItem.getResName())) {
                            VideoPreviewPresenter.this.changeBgAudio("", true);
                            return;
                        }
                        VideoPreviewPresenter videoPreviewPresenter2 = VideoPreviewPresenter.this;
                        videoPreviewPresenter2.changeBgAudio(listItem.getResPath() + listItem.getResName(), true);
                    }
                }, 200L);
            }
            ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().playButtonStatus(false);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void setVolume(float f) {
            VideoPreviewPresenter.this.mMediaEffectGraph.SetVolumeForAudioSource(VideoPreviewPresenter.this.mBgAudioTrackId, f);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void onBack(View view) {
            SUndoHelper.Entry undo = VideoPreviewPresenter.this.mSUndoHelper.undo();
            if (undo == null) {
                return;
            }
            if (VideoPreviewPresenter.this.mSUndoHelper.size() == 0) {
                view.setBackgroundResource(R$drawable.magic_video_back_btn_disable);
            }
            if (VideoPreviewPresenter.this.mEffectPlayer != null && VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewPlaying) {
                VideoPreviewPresenter.this.mEffectPlayer.PausePreView();
            }
            for (SUndoHelper.Entry entry : VideoPreviewPresenter.this.mSUndoHelper.getValueByLine(undo)) {
                VideoPreviewPresenter.this.addEffect(entry);
            }
            VideoPreviewPresenter.this.getActivity().event(b.j);
            VideoPreviewPresenter.this.mFilter = false;
            VideoPreviewPresenter.this.seekTo(undo.getStart(), true);
            VideoPreviewPresenter.this.getContract().setVideoTime(undo.getStart(), VideoPreviewPresenter.this.mBackgroundVideoDuration);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void onPause() {
            if (VideoPreviewPresenter.this.mEffectPlayer == null) {
                return;
            }
            VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
            videoPreviewPresenter.lastProcess = (int) videoPreviewPresenter.mEffectPlayer.GetCurrentPlayingPosition();
            if (Math.abs(VideoPreviewPresenter.this.lastProcess - VideoPreviewPresenter.this.mBackgroundVideoDuration) >= 100.0f) {
                return;
            }
            VideoPreviewPresenter.this.lastProcess = 0;
        }

        public void firstFrameFinish(final int i, final int i2, final float f) {
            VideoPreviewPresenter.this.getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass1.$r8$lambda$MC_UOEZBSFoECdhPDNH_YHg4RDE(VideoPreviewPresenter.AnonymousClass1.this, i, i2, f);
                }
            });
        }

        public /* synthetic */ void lambda$firstFrameFinish$1(int i, int i2, float f) {
            ((VideoMenuPresenter) VideoPreviewPresenter.this.mActivity.mMenuFragment.mPresenter).firstFrameFinish(i, i2, f, VideoPreviewPresenter.this.mStrForegroundVideo);
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public void stopVideo() {
            if (VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewStopped || VideoPreviewPresenter.this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewEOF) {
                VideoPreviewPresenter.this.setGravityPlayerView(false, false);
            }
        }

        @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$P
        public int getCurrentType() {
            return VideoPreviewPresenter.this.mCurrentEffectType;
        }
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IPreview$P mo1070initContract() {
        return new AnonymousClass1();
    }

    public final void setGravityPlayerView(final boolean z, final boolean z2) {
        if (this.mEffectPlayer != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.$r8$lambda$b6TRXuVrLlgukZfzoULpA9UXdmM(VideoPreviewPresenter.this, z2, z);
                }
            });
        }
    }

    public /* synthetic */ void lambda$setGravityPlayerView$0(boolean z, boolean z2) {
        PreViewStatus GetPreViewStatus;
        this.mISFirst = false;
        this.mPlayerView.setVisibility(0);
        SurfaceHolder holder = this.mPlayerView.getHolder();
        this.mSurfaceHolderPlayer = holder;
        this.mEffectPlayer.SetViewSurface(holder.getSurface());
        this.mEffectPlayer.setGravity(EffectMediaPlayer.SurfaceGravity.SurfaceGravityResizeAspectFit, this.mFixedSizeWidth, this.mFixedSizeHeight);
        if (z && ((GetPreViewStatus = this.mEffectPlayer.GetPreViewStatus()) == PreViewStatus.PreViewEOF || GetPreViewStatus == PreViewStatus.PreViewStopped)) {
            this.mEffectPlayer.StartPreView();
            ((VideoPreviewFragment) this.mView.get()).getContract().playButtonStatus(false);
        }
        this.mPlayerView.bringToFront();
        if (z2) {
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger VideoPreviewPresenter", "lastProcess: " + this.lastProcess);
            int i = this.lastProcess;
            if (i == 0 || Math.abs(this.mBackgroundVideoDuration - i) < 300.0f) {
                return;
            }
            this.mFilter = true;
            seekTo(this.lastProcess, false);
        }
    }

    public final void seekTo(long j, boolean z) {
        PreViewStatus GetPreViewStatus = this.mEffectPlayer.GetPreViewStatus();
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("MagicLogger VideoPreviewPresenter", "Video_seekTo: 调用算法SeekTo 到指定时间点：" + j + " link: " + z + " status:" + GetPreViewStatus);
        if (GetPreViewStatus == PreViewStatus.PreViewStopped || GetPreViewStatus == PreViewStatus.PreViewEOF) {
            this.mEffectPlayer.StartPreView();
        }
        this.mEffectPlayer.SeekTo(j, z, PlayerSeekingMode.PlayerSeekingPreciseMode);
    }

    public final void setGravityPlayerView() {
        setGravityPlayerView(false, true);
    }

    public final void addEffect(int i, boolean z) {
        if (this.mEffectPlayer == null) {
            return;
        }
        if (this.mSegEffectId == -1) {
            long CreateEffect = MediaEffect.CreateEffect(EffectType.kSegmentEffectFilter);
            this.mSegEffectId = CreateEffect;
            this.mMediaEffectGraph.AddEffect(CreateEffect, this.mForegroundSourceId);
        }
        this.mCurrentEffectType = i;
        Map<String, String> segmentEffectType = getSegmentEffectType(i);
        float f = 0.0f;
        float f2 = this.mCurrentVideoDuration;
        if (z && f2 <= 0.0f) {
            f2 = this.mBackgroundVideoDuration;
        }
        if (!z) {
            getActivity().event(1015, Integer.valueOf(this.mCurrentEffectType));
            if (this.mCurrentVideoDuration != 0.0f) {
                f = (float) this.mEffectPlayer.GetCurrentPlayingPosition();
            }
            this.startTime = f;
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("addEffect--添加特效", "start: " + this.startTime);
            ((VideoPreviewFragment) this.mView.get()).getContract().playButtonStatus(false);
        } else {
            segmentEffectType.put("start_time", this.startTime + "");
            segmentEffectType.put("end_time", f2 + "");
            MagicLog magicLog2 = MagicLog.INSTANCE;
            magicLog2.showLog("addEffect--添加特效", "start: " + this.startTime + ",  end:" + f2 + " ,EffectType: " + this.mCurrentEffectType);
            float f3 = this.startTime;
            if (f3 >= f2) {
                playerPausePreView();
                getActivity().event(b.j);
                return;
            }
            this.mSUndoHelper.addEntry(SUndoHelper.Entry.build((int) f3, (int) f2, (byte) this.mCurrentEffectType));
            this.mCurrentEffectType = -100;
            ((VideoPreviewFragment) this.mView.get()).getContract().playButtonStatus(true);
        }
        if (this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewPlaying) {
            playerOnlyPausePreView();
        }
        MediaEffect.SetParamsForEffect(EffectType.kSegmentEffectFilter, this.mSegEffectId, segmentEffectType);
        if (!z) {
            playerStartPreview(false);
        } else {
            playerPausePreView();
        }
    }

    public final void addEffect(SUndoHelper.Entry entry) {
        Map<String, String> segmentEffectType = getSegmentEffectType(entry.getType());
        segmentEffectType.put("start_time", entry.getStart() + "");
        segmentEffectType.put("end_time", entry.getEnd() + "");
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("Effect-- delete撤销的时间段和特效： " + entry.toString());
        if (this.mSegEffectId == -1) {
            long CreateEffect = MediaEffect.CreateEffect(EffectType.kSegmentEffectFilter);
            this.mSegEffectId = CreateEffect;
            this.mMediaEffectGraph.AddEffect(CreateEffect, this.mForegroundSourceId);
        }
        MediaEffect.SetParamsForEffect(EffectType.kSegmentEffectFilter, this.mSegEffectId, segmentEffectType);
    }

    public final Map<String, String> getSegmentEffectType(int i) {
        HashMap hashMap = new HashMap();
        SegmentEffectType segmentEffectType = SegmentEffectType.NoneType;
        if (this.mEffectBasePath == null) {
            this.mEffectBasePath = VideoResourceFetcher.INSTANCE.getResourceBasePath();
        }
        switch (i) {
            case -1:
                segmentEffectType = SegmentEffectType.WipeType;
                break;
            case 1:
                segmentEffectType = SegmentEffectType.colorEdgeType;
                break;
            case 2:
                segmentEffectType = SegmentEffectType.dotEdgeType;
                break;
            case 3:
                segmentEffectType = SegmentEffectType.blingEdgeType;
                break;
            case 4:
                segmentEffectType = SegmentEffectType.colorTailType;
                break;
            case 5:
                segmentEffectType = SegmentEffectType.waveSweptType;
                break;
            case 6:
                segmentEffectType = SegmentEffectType.doubleFlowType;
                break;
            case 7:
                segmentEffectType = SegmentEffectType.singleFlowType;
                break;
            case 8:
                segmentEffectType = SegmentEffectType.particleSurroundType;
                hashMap.put("mixertype", "1");
                hashMap.put("extern_video_source", VideoModel.getVideoList().get(8).getResPath() + "particle_surround.mp4");
                break;
            case 9:
                segmentEffectType = SegmentEffectType.textLayOutType;
                hashMap.put("mixertype", "0");
                hashMap.put("extern_video_source", VideoModel.getVideoList().get(9).getResPath() + "text_layout.mp4");
                break;
            case 10:
                segmentEffectType = SegmentEffectType.DevilWing;
                hashMap.put("mixertype", "2");
                hashMap.put("extern_video_source", VideoModel.getVideoList().get(10).getResPath() + "devil_wing.mp4");
                break;
            case 11:
                segmentEffectType = SegmentEffectType.AngelWing;
                hashMap.put("mixertype", "4");
                hashMap.put("extern_video_source", VideoModel.getVideoList().get(11).getResPath() + "angel_wing.mp4");
                break;
            case 13:
                segmentEffectType = SegmentEffectType.AngelWing;
                hashMap.put("mixertype", "2");
                hashMap.put("extern_video_source", this.mEffectBasePath + "angel_wing.mp4");
                break;
            case 14:
                segmentEffectType = SegmentEffectType.DevilWingFlow;
                hashMap.put("mixertype", "2");
                hashMap.put("extern_video_source", this.mEffectBasePath + "devil_wing.mp4");
                break;
        }
        hashMap.put("segment_effect_type", String.valueOf(segmentEffectType));
        BaseFragmentActivity activityWithSync = getActivityWithSync();
        if (activityWithSync != null) {
            hashMap.put("dark_colorlookup_path", activityWithSync.getFilesDir().getAbsolutePath() + File.separator + VideoEffectsActivity.VIDEO_EFFECTS_PNG);
        }
        return hashMap;
    }

    public final void playerStartPreview(boolean z) {
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer == null) {
            return;
        }
        if (z) {
            this.mFilter = true;
            effectMediaPlayer.StartPreView();
            setGravityPlayerView();
            return;
        }
        int i = AnonymousClass8.$SwitchMap$com$xiaomi$mediaprocess$PreViewStatus[effectMediaPlayer.GetPreViewStatus().ordinal()];
        if (i == 1) {
            this.mEffectPlayer.ResumePreView();
        } else if (i != 2) {
        } else {
            this.mFilter = true;
            this.mEffectPlayer.StartPreView();
            setGravityPlayerView();
        }
    }

    /* renamed from: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$8 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass8 {
        public static final /* synthetic */ int[] $SwitchMap$com$xiaomi$mediaprocess$PreViewStatus;

        static {
            int[] iArr = new int[PreViewStatus.values().length];
            $SwitchMap$com$xiaomi$mediaprocess$PreViewStatus = iArr;
            try {
                iArr[PreViewStatus.PreViewPaused.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$xiaomi$mediaprocess$PreViewStatus[PreViewStatus.PreViewStopped.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$xiaomi$mediaprocess$PreViewStatus[PreViewStatus.PreViewPlaying.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public final void constructGraph() {
        MagicLog.INSTANCE.startLog("ConstructGraph", "人脸识别检测分割");
        long currentTimeMillis = System.currentTimeMillis();
        String str = this.mStrForegroundVideo;
        if (str == null || str.isEmpty()) {
            Log.i("MagicLogger VideoPreviewPresenter", "on click button do not find foreground video");
            return;
        }
        String str2 = this.mStrBackgroundVideo;
        if (str2 == null || str2.isEmpty()) {
            Log.i("MagicLogger VideoPreviewPresenter", "on click button do not find background video");
            this.mStrBackgroundVideo = this.mStrForegroundVideo;
        }
        MediaEffectGraph mediaEffectGraph = new MediaEffectGraph();
        this.mMediaEffectGraph = mediaEffectGraph;
        mediaEffectGraph.ConstructMediaEffectGraph();
        this.mSUndoHelper = new SUndoHelper((int) this.mBackgroundVideoDuration);
        if (new File(this.mStrForegroundVideo).exists()) {
            this.mForegroundSourceId = this.mMediaEffectGraph.AddVideoSource(this.mStrForegroundVideo, this.mVideoCutStartTime, this.mVideoCutDuration);
            long j = 0;
            if (this.mEffectId == 0) {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoPreviewPresenter.m1069$r8$lambda$yVQECUM2SXWJGtp60b6lvdWoU(VideoPreviewPresenter.this);
                    }
                });
                long currentTimeMillis2 = System.currentTimeMillis();
                String libraryDirPath = ResourceUtil.getLibraryDirPath(MagicUtils.getGalleryApp());
                EffectType effectType = EffectType.kVideoSegmentFilter;
                long CreateEffect = MediaEffect.CreateEffect(effectType, libraryDirPath + h.g, 256, 256);
                this.mEffectId = CreateEffect;
                MediaEffect.SetFilterCallback(CreateEffect, new AnonymousClass2(currentTimeMillis2, currentTimeMillis));
            }
            HashMap hashMap = new HashMap();
            hashMap.put("model_type", String.valueOf(PREVIEW_SIZE_RATIO_POS));
            hashMap.put("model_thresh", PREVIEW_SIZE_LEVEL_TIPS_ARRAY[PREVIEW_SIZE_LEVEL_POS]);
            if (getActivityWithSync() == null) {
                return;
            }
            MediaEffect.SetParamsForEffect(EffectType.kVideoSegmentFilter, this.mEffectId, hashMap);
            this.mMediaEffectGraph.AddEffect(this.mEffectId, this.mForegroundSourceId);
            Log.d("MagicLogger VideoPreviewPresenter", "AddEffect, mEffectId = " + this.mEffectId + ", mForegroundSourceId = " + this.mForegroundSourceId);
            File file = new File(this.mStrForegroundVideo);
            StringBuilder sb = new StringBuilder();
            sb.append("AddVideoBackGround  mStrBackgroundVideo = ");
            sb.append(this.mStrBackgroundVideo);
            Log.d("MagicLogger VideoPreviewPresenter", sb.toString());
            this.mStrBackgroundAudio = this.mStrBackgroundVideo;
            if (file.exists()) {
                EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
                if (effectMediaPlayer != null) {
                    j = effectMediaPlayer.GetCurrentPlayingPosition();
                }
                this.mOriginAudioTrackId = this.mMediaEffectGraph.AddAudioTrack(this.mStrBackgroundAudio, this.mVideoCutStartTime, j, true);
                return;
            }
            Log.i("MagicLogger VideoPreviewPresenter", "Can not find background video file!");
            return;
        }
        Log.i("MagicLogger VideoPreviewPresenter", "Can not find foreground video file!");
    }

    public /* synthetic */ void lambda$constructGraph$1() {
        if (getRealV() == null) {
            return;
        }
        resetSurfaceViewSize(this.mPlayerView, this.mVideoWidth, this.mVideoHeight);
    }

    /* renamed from: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements EffectNotifier {
        public final /* synthetic */ long val$mStartSegmentTime;
        public final /* synthetic */ long val$timeStart;

        public static /* synthetic */ void $r8$lambda$JzmVkuUqnlSEnN4SiEA8zMWfZpI(AnonymousClass2 anonymousClass2, Bitmap bitmap, long j) {
            anonymousClass2.lambda$OnReceiveBitmap$1(bitmap, j);
        }

        public static /* synthetic */ void $r8$lambda$qZo_oaZPp0ZvlOSFmlz5YVumfHM(AnonymousClass2 anonymousClass2, long j) {
            anonymousClass2.lambda$OnReceiveFinish$0(j);
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveFailed(int i) {
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveStatus(int i) {
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveVideoFrame(byte[] bArr, int i, int i2, long j) {
        }

        public AnonymousClass2(long j, long j2) {
            VideoPreviewPresenter.this = r1;
            this.val$mStartSegmentTime = j;
            this.val$timeStart = j2;
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveFinish() {
            long currentTimeMillis = System.currentTimeMillis();
            Log.d("MagicLogger VideoPreviewPresenter", "Start CreateEffect kVideoSegmentFilter end, last time: " + (currentTimeMillis - this.val$mStartSegmentTime));
            VideoPreviewPresenter.this.lastProcess = 0;
            Handler handler = new Handler(Looper.getMainLooper());
            final long j = this.val$timeStart;
            handler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass2.$r8$lambda$qZo_oaZPp0ZvlOSFmlz5YVumfHM(VideoPreviewPresenter.AnonymousClass2.this, j);
                }
            });
        }

        public /* synthetic */ void lambda$OnReceiveFinish$0(long j) {
            MagicLog.INSTANCE.endLog("ConstructGraph", "人脸识别检测分割");
            if (VideoPreviewPresenter.this.getRealV() == null) {
                return;
            }
            VideoPreviewPresenter.this.initPlayVideo();
            VideoPreviewPresenter.this.getContract().onReceiverFinish();
            VideoPreviewPresenter.this.getActivity().event(2001);
            long currentTimeMillis = System.currentTimeMillis() - j;
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "时间" + currentTimeMillis);
            MagicSampler.getInstance().recordCategory("video_post", "check_time", hashMap);
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveBitmap(final Bitmap bitmap, final long j) {
            VideoPreviewPresenter.this.getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass2.$r8$lambda$JzmVkuUqnlSEnN4SiEA8zMWfZpI(VideoPreviewPresenter.AnonymousClass2.this, bitmap, j);
                }
            });
        }

        public /* synthetic */ void lambda$OnReceiveBitmap$1(Bitmap bitmap, long j) {
            ((VideoMenuPresenter) VideoPreviewPresenter.this.mActivity.mMenuFragment.mPresenter).setBodyImage(bitmap, (float) j);
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnProcessProgress(double d) {
            BaseFragmentActivity activityWithSync = VideoPreviewPresenter.this.getActivityWithSync();
            if (activityWithSync != null) {
                activityWithSync.event(2003, Double.valueOf(d));
            }
        }
    }

    public final void playerPausePreView() {
        getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                VideoPreviewPresenter.m1068$r8$lambda$juCRGt1cylHAGaiLKoDhJAGMwI(VideoPreviewPresenter.this);
            }
        });
    }

    public /* synthetic */ void lambda$playerPausePreView$2() {
        this.mEffectPlayer.PausePreView();
        ((VideoPreviewFragment) this.mView.get()).getContract().playButtonStatus(true);
    }

    public final void playerOnlyPausePreView() {
        getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VideoPreviewPresenter.$r8$lambda$fhS_eq4NyGTScjLPlxFV3Y8jehs(VideoPreviewPresenter.this);
            }
        });
    }

    public /* synthetic */ void lambda$playerOnlyPausePreView$3() {
        this.mEffectPlayer.PausePreView();
    }

    public final void releaseMediaEditor() {
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer != null) {
            effectMediaPlayer.StopPreView();
            this.mEffectPlayer.DestructMediaPlayer();
            this.mEffectPlayer = null;
        }
        long j = this.mSegEffectId;
        if (j > 0) {
            this.mMediaEffectGraph.RemoveEffect(j, this.mForegroundSourceId);
            this.mSegEffectId = -1L;
        }
        MediaComposeFile mediaComposeFile = this.mMediaComposFile;
        if (mediaComposeFile != null) {
            mediaComposeFile.DestructMediaComposeFile();
            this.mMediaComposFile = null;
            this.isFileCoposing = false;
        }
    }

    public final Bitmap getFirstFrame(String str) {
        PLMediaFile pLMediaFile = new PLMediaFile(str);
        this.mBackgroundVideoDuration = this.mVideoCutDuration;
        HashMap hashMap = new HashMap();
        hashMap.put("length", String.valueOf(this.mBackgroundVideoDuration));
        MagicSampler.getInstance().recordCategory("video_post", "video_length", hashMap);
        PLVideoFrame videoFrameByTime = pLMediaFile.getVideoFrameByTime(0L, true);
        pLMediaFile.release();
        if (videoFrameByTime != null) {
            return videoFrameByTime.toBitmap();
        }
        return null;
    }

    public final Bitmap getSpecialFrame(String str, long j) {
        PLMediaFile pLMediaFile = new PLMediaFile(str);
        PLVideoFrame videoFrameByTime = pLMediaFile.getVideoFrameByTime(j, true);
        pLMediaFile.release();
        if (videoFrameByTime != null) {
            return videoFrameByTime.toBitmap();
        }
        return null;
    }

    public void initPlayVideo() {
        if (this.isFileCoposing) {
            MagicToast.showToast(getActivity(), getActivity().getResources().getString(R$string.magic_compositing), 1);
        } else {
            this.mEffectPlayer = getEffectPlayer();
        }
    }

    public final EffectMediaPlayer getEffectPlayer() {
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer != null) {
            return effectMediaPlayer;
        }
        EffectMediaPlayer effectMediaPlayer2 = new EffectMediaPlayer(this.mMediaEffectGraph);
        this.mEffectPlayer = effectMediaPlayer2;
        effectMediaPlayer2.ConstructMediaPlayer(this.mFixedSizeWidth, this.mFixedSizeHeight);
        this.mEffectPlayer.SetViewSurface(this.mSurfaceHolderPlayer.getSurface());
        this.mEffectPlayer.setGravity(EffectMediaPlayer.SurfaceGravity.SurfaceGravityResizeAspectFit, this.mFixedSizeWidth, this.mFixedSizeHeight);
        this.mSurfaceHolderPlayer.addCallback(new SurfaceHolder.Callback() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter.3
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            }

            {
                VideoPreviewPresenter.this = this;
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                MagicLog.INSTANCE.showLog("---surfaceCreated---");
                WeakReference<V> weakReference = VideoPreviewPresenter.this.mView;
                if (weakReference == 0 || weakReference.get() == null) {
                    return;
                }
                VideoPreviewPresenter.this.mSurfaceHolderPlayer = surfaceHolder;
                if ((!VideoPreviewPresenter.this.mISFirst || VideoPreviewPresenter.this.mFixedSizeWidth != 0) && VideoPreviewPresenter.this.mFixedSizeHeight != 0) {
                    surfaceHolder.setFixedSize(VideoPreviewPresenter.this.mFixedSizeWidth, VideoPreviewPresenter.this.mFixedSizeHeight);
                } else {
                    VideoPreviewPresenter.this.mISFirst = false;
                    VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
                    videoPreviewPresenter.resetSurfaceViewSize(videoPreviewPresenter.mPlayerView, videoPreviewPresenter.mVideoWidth, VideoPreviewPresenter.this.mVideoHeight);
                }
                VideoPreviewPresenter.this.mEffectPlayer.SetViewSurface(surfaceHolder.getSurface());
                VideoPreviewPresenter.this.mEffectPlayer.setGravity(EffectMediaPlayer.SurfaceGravity.SurfaceGravityResizeAspectFit, VideoPreviewPresenter.this.mFixedSizeWidth, VideoPreviewPresenter.this.mFixedSizeHeight);
                ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().surfaceCreated();
                VideoPreviewPresenter videoPreviewPresenter2 = VideoPreviewPresenter.this;
                if (videoPreviewPresenter2.isFileCoposing || videoPreviewPresenter2.mEffectPlayer == null || VideoPreviewPresenter.this.mCurrentVideoDuration == 0.0f) {
                    return;
                }
                VideoPreviewPresenter.this.mFilter = false;
                VideoPreviewPresenter videoPreviewPresenter3 = VideoPreviewPresenter.this;
                videoPreviewPresenter3.seekTo(videoPreviewPresenter3.mCurrentVideoDuration, true);
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                VideoPreviewPresenter.this.mIsStop = true;
                VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
                if (!videoPreviewPresenter.isFileCoposing) {
                    videoPreviewPresenter.mEffectPlayer.StopPreView();
                }
                ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().playButtonStatus(true);
            }
        });
        this.mEffectPlayer.SetPlayerNotify(new AnonymousClass4());
        return this.mEffectPlayer;
    }

    /* renamed from: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements EffectNotifier {
        public static /* synthetic */ void $r8$lambda$CjsbG_HyymAFGmpvcuQGsikAM00(AnonymousClass4 anonymousClass4) {
            anonymousClass4.lambda$OnProcessProgress$1();
        }

        public static /* synthetic */ void $r8$lambda$h_RNfr6Y2Bu6VsHyBE4m0YLs3fQ(AnonymousClass4 anonymousClass4) {
            anonymousClass4.lambda$OnReceiveFinish$0();
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveBitmap(Bitmap bitmap, long j) {
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveFailed(int i) {
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveVideoFrame(byte[] bArr, int i, int i2, long j) {
        }

        public AnonymousClass4() {
            VideoPreviewPresenter.this = r1;
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveFinish() {
            Log.d("MagicLogger VideoPreviewPresenter", "mEffectPlayer OnReceiveFinish");
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass4.$r8$lambda$h_RNfr6Y2Bu6VsHyBE4m0YLs3fQ(VideoPreviewPresenter.AnonymousClass4.this);
                }
            });
        }

        public /* synthetic */ void lambda$OnReceiveFinish$0() {
            VideoPreviewPresenter.this.getContract().setVideoTime(VideoPreviewPresenter.this.mBackgroundVideoDuration, VideoPreviewPresenter.this.mBackgroundVideoDuration);
            ((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getContract().resetPlayStopIcon(false);
            VideoPreviewPresenter.this.mCurrentVideoDuration = 0.0f;
            VideoPreviewPresenter.this.getContract().setVideoTime(0.0f, VideoPreviewPresenter.this.mBackgroundVideoDuration);
            VideoPreviewPresenter.this.mEffectPlayer.StopPreView();
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnProcessProgress(double d) {
            Log.e("MagicLogger VideoPreviewPresenter", "OnProcessProgress--------" + d + "------");
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass4.$r8$lambda$CjsbG_HyymAFGmpvcuQGsikAM00(VideoPreviewPresenter.AnonymousClass4.this);
                }
            });
        }

        public /* synthetic */ void lambda$OnProcessProgress$1() {
            if (VideoPreviewPresenter.this.mEffectPlayer == null) {
                return;
            }
            long GetCurrentPlayingPosition = VideoPreviewPresenter.this.mEffectPlayer.GetCurrentPlayingPosition();
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("Video_seekTo: 算法 OnProcessProgress 调用mEffectPlayer.GetCurrentPlayingPosition返回 " + GetCurrentPlayingPosition);
            VideoPreviewPresenter.this.getContract().setThumbnailVisible(GetCurrentPlayingPosition <= 0);
            VideoPreviewPresenter.this.getContract().setVideoTime((float) GetCurrentPlayingPosition, VideoPreviewPresenter.this.mBackgroundVideoDuration);
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveStatus(int i) {
            Log.e("DBCXXX", i + "------");
            if (PlayerStatus.int2enum(i) != PlayerStatus.SEEK_COMPLETE || VideoPreviewPresenter.this.mFilter) {
                return;
            }
            Log.d("MagicLogger VideoPreviewPresenter", " SeekBar:seek complete, pause.");
            VideoPreviewPresenter.this.playerPausePreView();
        }
    }

    public final void play(boolean z) {
        int i = AnonymousClass8.$SwitchMap$com$xiaomi$mediaprocess$PreViewStatus[this.mEffectPlayer.GetPreViewStatus().ordinal()];
        if (i == 1) {
            this.mEffectPlayer.ResumePreView();
        } else if (i != 2) {
            if (i != 3) {
                return;
            }
            this.mEffectPlayer.PausePreView();
        } else {
            this.mFilter = true;
            if (z) {
                setGravityPlayerView();
            } else {
                this.mEffectPlayer.StartPreView();
            }
        }
    }

    public final void gonoPlay() {
        int i = AnonymousClass8.$SwitchMap$com$xiaomi$mediaprocess$PreViewStatus[this.mEffectPlayer.GetPreViewStatus().ordinal()];
        if (i == 1) {
            this.mEffectPlayer.ResumePreView();
        } else if (i != 2) {
        } else {
            this.mEffectPlayer.StartPreView();
        }
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        super.unBindView();
        MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter.5
            {
                VideoPreviewPresenter.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (VideoPreviewPresenter.this.mClipReverseHelper != null) {
                    VideoPreviewPresenter.this.mClipReverseHelper.release();
                }
                VideoPreviewPresenter.this.releaseMediaEditor();
                if (VideoPreviewPresenter.this.mEffectId != 0) {
                    MediaEffect.DestoryEffect(VideoPreviewPresenter.this.mEffectId);
                    VideoPreviewPresenter.this.mEffectId = 0L;
                }
                if (VideoPreviewPresenter.this.mMediaEffectGraph != null) {
                    VideoPreviewPresenter.this.mMediaEffectGraph.DestructMediaEffectGraph();
                    VideoPreviewPresenter.this.mMediaEffectGraph = null;
                }
                SystemUtil.UnInit();
            }
        });
    }

    public final String getGapTime(long j) {
        long j2 = j - ((j / 3600000) * 3600000);
        long j3 = j2 / 60000;
        long j4 = (j2 - (60000 * j3)) / 1000;
        String str = "" + j3;
        if (j4 < 10) {
            return str + ":0" + j4;
        }
        return str + ":" + j4;
    }

    public void muteOriginAudioTrack(boolean z) {
        EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
        if (effectMediaPlayer == null) {
            return;
        }
        if (z && this.mOriginAudioTrackId != -1) {
            boolean z2 = effectMediaPlayer.GetPreViewStatus() == PreViewStatus.PreViewPlaying;
            this.mEffectPlayer.PausePreView();
            this.mMediaEffectGraph.RemoveAudioTrack(this.mOriginAudioTrackId);
            if (z2) {
                this.mEffectPlayer.ResumePreView();
            }
            this.mOriginAudioTrackId = -1L;
            return;
        }
        String str = this.mStrBackgroundAudio;
        if (str == null || str.isEmpty()) {
            return;
        }
        long j = 0;
        EffectMediaPlayer effectMediaPlayer2 = this.mEffectPlayer;
        if (effectMediaPlayer2 != null) {
            j = effectMediaPlayer2.GetCurrentPlayingPosition();
        }
        long j2 = j;
        Log.d("MagicLogger VideoPreviewPresenter", "currentPlayingPosition " + j2);
        this.mOriginAudioTrackId = this.mMediaEffectGraph.AddAudioTrack(this.mStrBackgroundAudio, (long) this.mVideoCutStartTime, j2, true);
    }

    public final String getOutputMediaFilePath() {
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE);
        if (pathInPriorStorage == null) {
            return null;
        }
        if (StorageSolutionProvider.get().getDocumentFile(pathInPriorStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("MagicLogger VideoPreviewPresenter", "getOutputMediaFilePath")) == null) {
            DefaultLogger.e("MagicLogger VideoPreviewPresenter", "getOutputMediaFile failed");
            return null;
        }
        String format = String.format(Locale.US, "MP4_%s%s.mp4", DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis()), "VIDEOEFFECTS");
        return pathInPriorStorage + File.separator + format;
    }

    public final void saveComposeMP4() {
        Log.i("MagicLogger VideoPreviewPresenter", "on click button onComposeMP4");
        this.SAVING_BLACK_CLICK = false;
        if (this.isFileCoposing) {
            return;
        }
        String outputMediaFilePath = getOutputMediaFilePath();
        if (TextUtils.isEmpty(outputMediaFilePath)) {
            ToastUtils.makeText(((VideoPreviewFragment) this.mView.get()).getActivity(), R$string.magic_idp_dsc, 1);
            return;
        }
        File file = new File(outputMediaFilePath);
        MagicLog.INSTANCE.startLog("ConstructGraph_save", "视频扣人保存");
        getActivity().showExportVideoFragment(new ExportVideoFragment.Callbacks() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter.6
            @Override // com.miui.gallery.magic.widget.ExportVideoFragment.Callbacks
            public int doExport() {
                return 0;
            }

            @Override // com.miui.gallery.magic.widget.ExportVideoFragment.Callbacks
            public void onCancelled() {
            }

            @Override // com.miui.gallery.magic.widget.ExportVideoFragment.Callbacks
            public void onExported(boolean z) {
            }

            {
                VideoPreviewPresenter.this = this;
            }

            @Override // com.miui.gallery.magic.widget.ExportVideoFragment.Callbacks
            public void doCancel() {
                VideoPreviewPresenter.this.SAVING_BLACK_CLICK = true;
                VideoPreviewPresenter.this.mMediaComposFile.CancelComposeFile();
                VideoPreviewPresenter.this.mMediaComposFile.DestructMediaComposeFile();
                VideoPreviewPresenter.this.getActivity().removeExportFragment();
                if (VideoPreviewPresenter.this.mEffectPlayer != null) {
                    if (VideoPreviewPresenter.this.mISFirst) {
                        VideoPreviewPresenter.this.setGravityPlayerView(false, false);
                    }
                    VideoPreviewPresenter.this.mFilter = false;
                    VideoPreviewPresenter videoPreviewPresenter = VideoPreviewPresenter.this;
                    videoPreviewPresenter.seekTo(videoPreviewPresenter.mCurrentVideoPosition, true);
                }
            }
        });
        this.isFileCoposing = true;
        MediaComposeFile mediaComposeFile = new MediaComposeFile(this.mMediaEffectGraph);
        this.mMediaComposFile = mediaComposeFile;
        mediaComposeFile.ConstructMediaComposeFile(this.mVideoWidth, this.mVideoHeight, 20971520, 30);
        this.mMediaComposFile.SetComposeNotify(new AnonymousClass7(System.currentTimeMillis(), file));
        Log.d("MagicLogger VideoPreviewPresenter", "onComposeMP4 mCompseFileName: " + outputMediaFilePath);
        this.mMediaComposFile.SetComposeFileName(outputMediaFilePath);
        this.mMediaComposFile.BeginComposeFile();
    }

    /* renamed from: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements EffectNotifier {
        public final /* synthetic */ File val$composeFile;
        public final /* synthetic */ long val$mStartSegmentTime;

        public static /* synthetic */ void $r8$lambda$9aA4EMIHAPw3992ltgnvZVV3D6g(AnonymousClass7 anonymousClass7) {
            anonymousClass7.lambda$OnReceiveFailed$1();
        }

        /* renamed from: $r8$lambda$HoPOD2O8ibK-F-q-wJiO0_Udpko */
        public static /* synthetic */ void m1071$r8$lambda$HoPOD2O8ibKFqwJiO0_Udpko(AnonymousClass7 anonymousClass7, File file) {
            anonymousClass7.lambda$OnReceiveFinish$0(file);
        }

        public static /* synthetic */ void $r8$lambda$JpxBOxXHyCgjffSO9h8XiYWv3CU(AnonymousClass7 anonymousClass7, double d) {
            anonymousClass7.lambda$OnProcessProgress$2(d);
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveBitmap(Bitmap bitmap, long j) {
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveStatus(int i) {
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveVideoFrame(byte[] bArr, int i, int i2, long j) {
        }

        public AnonymousClass7(long j, File file) {
            VideoPreviewPresenter.this = r1;
            this.val$mStartSegmentTime = j;
            this.val$composeFile = file;
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveFinish() {
            long currentTimeMillis = System.currentTimeMillis();
            Log.d("MagicLogger VideoPreviewPresenter", "onComposeMP4 OnReceiveFinish, last time: " + (currentTimeMillis - this.val$mStartSegmentTime));
            Handler handler = new Handler(Looper.getMainLooper());
            final File file = this.val$composeFile;
            handler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$7$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass7.m1071$r8$lambda$HoPOD2O8ibKFqwJiO0_Udpko(VideoPreviewPresenter.AnonymousClass7.this, file);
                }
            });
        }

        public /* synthetic */ void lambda$OnReceiveFinish$0(File file) {
            Uri fromFile = Uri.fromFile(file);
            MagicFileUtil.openPreviewVideo(((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getActivity(), fromFile);
            VideoPreviewPresenter.this.isFileCoposing = false;
            Intent intent = new Intent();
            intent.setAction("receiver_action_save_finish");
            VideoPreviewPresenter.this.getActivity().sendBroadcast(intent);
            VideoPreviewPresenter.this.getActivity().finish();
            if (!VideoPreviewPresenter.this.SAVING_BLACK_CLICK) {
                ToastUtils.makeText(((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getActivity(), R$string.magic_save_ok, 1);
            }
            MagicUtils.scanSingleFile(((VideoPreviewFragment) VideoPreviewPresenter.this.mView.get()).getActivity(), fromFile.getPath());
            VideoPreviewPresenter.this.getActivity().removeExportFragment();
            MagicLog.INSTANCE.endLog("ConstructGraph_save", "视频扣人保存");
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnReceiveFailed(int i) {
            MagicLog.INSTANCE.endLog("ConstructGraph_save", "视频扣人保存");
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass7.$r8$lambda$9aA4EMIHAPw3992ltgnvZVV3D6g(VideoPreviewPresenter.AnonymousClass7.this);
                }
            });
        }

        public /* synthetic */ void lambda$OnReceiveFailed$1() {
            VideoPreviewPresenter.this.getActivity().removeLoadingDialog();
            VideoPreviewPresenter.this.isFileCoposing = false;
        }

        @Override // com.xiaomi.mediaprocess.EffectNotifier
        public void OnProcessProgress(final double d) {
            Log.d("MagicLogger VideoPreviewPresenter", "onComposeMP4 OnProcessProgress: " + d);
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewPresenter$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    VideoPreviewPresenter.AnonymousClass7.$r8$lambda$JpxBOxXHyCgjffSO9h8XiYWv3CU(VideoPreviewPresenter.AnonymousClass7.this, d);
                }
            });
        }

        public /* synthetic */ void lambda$OnProcessProgress$2(double d) {
            VideoPreviewPresenter.this.getActivity().setExportProgress((int) (d * 100.0d));
        }
    }

    public final void changeBgAudio(String str, boolean z) {
        Log.d("MagicLogger VideoPreviewPresenter", "Select background audio file: " + str + ", mAudioTrackId: " + this.mBgAudioTrackId);
        boolean z2 = false;
        if (TextUtils.isEmpty(str)) {
            if (this.mEffectPlayer.GetPreViewStatus() == PreViewStatus.PreViewPlaying) {
                z2 = true;
            }
            this.mEffectPlayer.PausePreView();
            this.mMediaEffectGraph.RemoveAudioTrack(this.mBgAudioTrackId);
            if (z2) {
                this.mEffectPlayer.ResumePreView();
            }
            this.mBgAudioTrackId = -1L;
            gonoPlay();
            this.mFilter = true;
            return;
        }
        this.mFilter = true;
        if (this.mBgAudioTrackId == -1) {
            Log.d("MagicLogger VideoPreviewPresenter", "changeBgAudio  AddAudioTrack");
            EffectMediaPlayer effectMediaPlayer = this.mEffectPlayer;
            this.mBgAudioTrackId = this.mMediaEffectGraph.AddAudioTrack(str, this.mVideoCutStartTime, effectMediaPlayer != null ? effectMediaPlayer.GetCurrentPlayingPosition() : 0L, true);
        } else {
            if (this.mEffectPlayer.GetPreViewStatus() != PreViewStatus.PreViewPaused) {
                this.mEffectPlayer.PausePreView();
            }
            Log.d("MagicLogger VideoPreviewPresenter", "changeBgAudio  setUrlForAudioSource");
            this.mMediaEffectGraph.setUrlForAudioSource(this.mBgAudioTrackId, this.mVideoCutStartTime, this.CURRENT_PLAYBACK_TIME, str);
            PreViewStatus GetPreViewStatus = this.mEffectPlayer.GetPreViewStatus();
            if (GetPreViewStatus == PreViewStatus.PreViewStopped || GetPreViewStatus == PreViewStatus.PreViewEOF) {
                this.mEffectPlayer.StartPreView();
            }
        }
        if (z) {
            return;
        }
        this.mCurrentVideoDuration = 0.0f;
        seekTo(0L, false);
        if (this.mEffectPlayer.GetPreViewStatus() != PreViewStatus.PreViewPaused) {
            return;
        }
        this.mEffectPlayer.ResumePreView();
    }

    public final void resetSurfaceViewSize(View view, int i, int i2) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int width = viewGroup.getWidth();
        int height = viewGroup.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        float f = (i * 1.0f) / i2;
        float f2 = width;
        float f3 = height;
        if (f > (1.0f * f2) / f3) {
            this.mFixedSizeWidth = width;
            int i3 = (int) (f2 / f);
            this.mFixedSizeHeight = i3;
            layoutParams.width = width;
            layoutParams.height = i3;
        } else {
            this.mFixedSizeHeight = height;
            int i4 = (int) (f3 * f);
            this.mFixedSizeWidth = i4;
            layoutParams.height = height;
            layoutParams.width = i4;
        }
        SurfaceHolder surfaceHolder = this.mSurfaceHolderPlayer;
        if (surfaceHolder != null) {
            surfaceHolder.setFixedSize(this.mFixedSizeWidth, this.mFixedSizeHeight);
        }
        view.setLayoutParams(layoutParams);
    }

    public boolean onBackPress() {
        BaseFragmentActivity activity = getActivity();
        if (activity == null) {
            return false;
        }
        activity.finish();
        return true;
    }
}
