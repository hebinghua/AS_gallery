package com.miui.gallery.magic.special.effects.video.effects.preview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MagicDependsModule;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.special.effects.video.VideoEffectsActivity;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.ui.ConfirmDialog;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicThreadHandler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.magic.widget.ExportCutApartFragment;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.kouren.SystemUtil;
import com.xiaomi.milab.videosdk.XmsContext;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class VideoPreviewFragment extends BaseFragment<VideoPreviewPresenter, IPreview$V> {
    public static long lastClickTime = 0;
    public static boolean mIsEdit = false;
    public static volatile boolean mIsLibLoaded = false;
    public boolean isShowBackBtn;
    public View mBanner;
    public ImageView mCover;
    public TextView mCurrentTime;
    public ExportCutApartFragment mExportCutApartDialog;
    public LottieAnimationView mFunctionButton;
    public RelativeLayout mMagicFunctionRl;
    public LottieAnimationView mPlayButton;
    public SurfaceView mPlayerView;
    public TextView mTotalTime;
    public ImageView mVideoBackButton;
    public View viewVideoSeize;
    public boolean mStop = true;
    public String mCurrMagicJson = "magic_stop.json";
    public boolean mMute = false;
    public boolean mAudioTrackMode = false;
    public boolean isVideoSetCompleted = false;

    public static /* synthetic */ void $r8$lambda$UlqGi2HaEuQZZGMJkdARdNCDHIs(VideoPreviewFragment videoPreviewFragment, Intent intent) {
        videoPreviewFragment.lambda$initData$0(intent);
    }

    public static /* synthetic */ void $r8$lambda$wKJg7dxDirXq430xCX48AiihpqM(VideoPreviewFragment videoPreviewFragment, Intent intent) {
        videoPreviewFragment.lambda$initData$1(intent);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IPreview$V mo1066initContract() {
        return new IPreview$V() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewFragment.1
            {
                VideoPreviewFragment.this = this;
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void onPlayVideo() {
                VideoPreviewFragment.this.mCover.setVisibility(8);
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().onPlayVideo();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void initVideoData(Intent intent) {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().initVideoData(intent, true);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void setCoverBitmap(Bitmap bitmap) {
                int measuredHeight = VideoPreviewFragment.this.mCover.getMeasuredHeight();
                int measuredWidth = VideoPreviewFragment.this.mCover.getMeasuredWidth();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) VideoPreviewFragment.this.mCover.getLayoutParams();
                float f = (width * 1.0f) / height;
                float f2 = measuredWidth;
                float f3 = measuredHeight;
                if (f > (1.0f * f2) / f3) {
                    layoutParams.width = measuredWidth;
                    int i = (int) (f2 / f);
                    layoutParams.height = i;
                    layoutParams.setMargins(0, (measuredHeight - i) / 2, 0, (measuredHeight - i) / 2);
                } else {
                    layoutParams.height = measuredHeight;
                    int i2 = (int) (f3 * f);
                    layoutParams.width = i2;
                    layoutParams.setMargins((measuredWidth - i2) / 2, 0, (measuredWidth - i2) / 2, 0);
                }
                VideoPreviewFragment.this.mCover.setLayoutParams(layoutParams);
                VideoPreviewFragment.this.mCover.setImageBitmap(bitmap);
                VideoPreviewFragment.this.mCover.setVisibility(0);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void setSurfaceHolderPlayer(SurfaceHolder surfaceHolder, SurfaceView surfaceView) {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().setSurfaceHolderPlayer(surfaceHolder, surfaceView);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void setStartSegEffectId(int i, boolean z) {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().setStartSegEffectId(i, z);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public String getVideoPath() {
                return ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().getVideoPath();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void setVideoTime(String str, String str2) {
                VideoPreviewFragment.this.mCurrentTime.setText(str);
                VideoPreviewFragment.this.mTotalTime.setText(str2);
                TextView textView = VideoPreviewFragment.this.mTotalTime;
                StringBuilder sb = new StringBuilder();
                sb.append(str2.substring(0, 2));
                Resources resources = VideoPreviewFragment.this.getContext().getResources();
                int i = R$string.acc_minutes;
                sb.append(resources.getString(i));
                sb.append(str2.substring(3));
                Resources resources2 = VideoPreviewFragment.this.getContext().getResources();
                int i2 = R$string.acc_seconds;
                sb.append(resources2.getString(i2));
                textView.setContentDescription(sb.toString());
                TextView textView2 = VideoPreviewFragment.this.mCurrentTime;
                textView2.setContentDescription(str.substring(0, 2) + VideoPreviewFragment.this.getContext().getResources().getString(i) + str.substring(3) + VideoPreviewFragment.this.getContext().getResources().getString(i2));
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public long getTotalTime() {
                return ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().getTotalTime();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void seekToVideo(long j) {
                if (VideoPreviewFragment.this.mCover.getVisibility() == 0) {
                    VideoPreviewFragment.this.mCover.setVisibility(8);
                }
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().progressToVideoSeek(j);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void seekToEnd(long j) {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().seekToEnd(j);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void onReceiverFinish() {
                VideoPreviewFragment.this.isVideoSetCompleted = true;
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void cutVideoSlice(Intent intent) {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().cutVideoSlice(intent);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void surfaceCreated() {
                if (VideoPreviewFragment.this.mCover != null) {
                    VideoPreviewFragment videoPreviewFragment = VideoPreviewFragment.this;
                    if (videoPreviewFragment.mPresenter == 0) {
                        return;
                    }
                    videoPreviewFragment.mCover.setVisibility(0);
                }
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void setThumbnailVisible(boolean z) {
                if (VideoPreviewFragment.this.mCover == null) {
                    return;
                }
                VideoPreviewFragment.this.mCover.setVisibility(z ? 0 : 8);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void stopVideoPreview() {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().stopVideoPreview();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void dismissDialog() {
                VideoPreviewFragment.this.dismissCutDialog();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void showDialogProgress(Object obj, int i) {
                MagicLog magicLog = MagicLog.INSTANCE;
                magicLog.showLog("MagicLogger VideoPreviewFragment", "showDialogProgress   " + obj + " type  " + i);
                if (i == 2002) {
                    VideoPreviewFragment.this.showCutDialogProgress((((Integer) obj).intValue() * 3) / 9);
                } else if (i != 2003) {
                } else {
                    VideoPreviewFragment.this.showCutDialogProgress((int) ((((Double) obj).doubleValue() * 66.0d) + 33.0d));
                }
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void onComposeMP4() {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().onComposeMP4();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void selectAudioFile(String str) {
                VideoPreviewFragment.setIsEdit(true);
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().selectAudioFile(str);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void selectAudio(ListItem listItem) {
                P p = VideoPreviewFragment.this.mPresenter;
                if (p == 0 || ((VideoPreviewPresenter) p).isFileCoposing) {
                    return;
                }
                VideoPreviewFragment.setIsEdit(true);
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().selectAudio(listItem);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void setVolume(float f) {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().setVolume(f);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void onBack() {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().onBack(VideoPreviewFragment.this.mVideoBackButton);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void onPause() {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().onPause();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void stopVideo() {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().stopVideo();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public int getCurrentType() {
                return ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).getContract().getCurrentType();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void playButtonStatus(boolean z) {
                VideoPreviewFragment.this.playButtonStop(z);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void resetPlayStopIcon(boolean z) {
                if (!VideoPreviewFragment.this.mStop) {
                    VideoPreviewFragment.this.playButtonStop(true);
                }
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void switchToVideoEffect() {
                VideoPreviewFragment.this.mAudioTrackMode = false;
                VideoPreviewFragment.this.mFunctionButton.setVisibility(8);
                if (VideoPreviewFragment.this.isShowBackBtn) {
                    VideoPreviewFragment.this.mVideoBackButton.setVisibility(0);
                } else {
                    VideoPreviewFragment.this.mMagicFunctionRl.setVisibility(8);
                }
                VideoPreviewFragment.this.viewVideoSeize.setVisibility(8);
                VideoPreviewFragment.this.mMagicFunctionRl.setContentDescription(VideoPreviewFragment.this.getResources().getString(R$string.acc_magic_huifu_description));
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void switchToAudioTrack() {
                VideoPreviewFragment videoPreviewFragment = VideoPreviewFragment.this;
                videoPreviewFragment.isShowBackBtn = videoPreviewFragment.mVideoBackButton.getVisibility() == 0;
                VideoPreviewFragment.this.mVideoBackButton.setVisibility(8);
                VideoPreviewFragment.this.mFunctionButton.setVisibility(0);
                VideoPreviewFragment.this.mAudioTrackMode = true;
                VideoPreviewFragment.this.mMagicFunctionRl.setVisibility(0);
                VideoPreviewFragment.this.viewVideoSeize.setVisibility(0);
                if (VideoPreviewFragment.this.mMute) {
                    VideoPreviewFragment.this.mMagicFunctionRl.setContentDescription(VideoPreviewFragment.this.getContext().getResources().getString(R$string.acc_open_function_button));
                } else {
                    VideoPreviewFragment.this.mMagicFunctionRl.setContentDescription(VideoPreviewFragment.this.getContext().getResources().getString(R$string.acc_clsoe_function_button));
                }
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.preview.IPreview$V
            public void onMute(boolean z) {
                ((VideoPreviewPresenter) VideoPreviewFragment.this.mPresenter).muteOriginAudioTrack(z);
                if (z) {
                    MagicToast.showToast(VideoPreviewFragment.this.getActivity(), VideoPreviewFragment.this.getString(R$string.magic_video_soundtrack_is_turned_off));
                } else {
                    MagicToast.showToast(VideoPreviewFragment.this.getActivity(), VideoPreviewFragment.this.getString(R$string.magic_video_soundtrack_is_turned_on));
                }
            }
        };
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R$id.magic_video_preview);
        this.mPlayerView = surfaceView;
        surfaceView.setZOrderOnTop(true);
        this.mCover = (ImageView) findViewById(R$id.magic_video_cover);
        this.viewVideoSeize = findViewById(R$id.view_video_seize);
        this.mTotalTime = (TextView) findViewById(R$id.magic_video_play_total_time);
        this.mCurrentTime = (TextView) findViewById(R$id.magic_video_play_current_time);
        this.mBanner = findViewById(R$id.banner);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R$id.magic_video_play_btn_lottie);
        this.mPlayButton = lottieAnimationView;
        lottieAnimationView.setContentDescription(getContext().getResources().getString(R$string.acc_video_play_btn));
        this.mFunctionButton = (LottieAnimationView) findViewById(R$id.magic_video_function_btn_lottie);
        this.mVideoBackButton = (ImageView) findViewById(R$id.magic_video_back_btn);
        int i = R$id.magic_function_btn;
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(i);
        this.mMagicFunctionRl = relativeLayout;
        relativeLayout.setContentDescription(getResources().getString(R$string.acc_magic_huifu_description));
        TextView textView = (TextView) findViewById(R$id.magic_a_bar_cancel);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.9f).build();
        FolmeUtil.setCustomTouchAnim(findViewById(R$id.magic_video_play_btn), build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(findViewById(i), build, null, null, null, true);
        AnimParams build2 = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(textView, build2, null, null, null, true);
        FolmeUtil.setCustomTouchAnim((TextView) findViewById(R$id.magic_a_bar_save), build2, null, null, null, true);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        final Intent intent = getActivity().getIntent();
        MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                VideoPreviewFragment.$r8$lambda$wKJg7dxDirXq430xCX48AiihpqM(VideoPreviewFragment.this, intent);
            }
        });
    }

    public /* synthetic */ void lambda$initData$1(final Intent intent) {
        initVideoLibs();
        getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VideoPreviewFragment.$r8$lambda$UlqGi2HaEuQZZGMJkdARdNCDHIs(VideoPreviewFragment.this, intent);
            }
        });
    }

    public /* synthetic */ void lambda$initData$0(Intent intent) {
        getContract().setSurfaceHolderPlayer(this.mPlayerView.getHolder(), this.mPlayerView);
        boolean booleanExtra = intent.getBooleanExtra("video_slice", false);
        showCutDialog();
        if (booleanExtra) {
            getContract().initVideoData(intent);
        } else {
            getContract().cutVideoSlice(intent);
        }
        this.mPlayerView.setZOrderOnTop(false);
        this.mPlayButton.setAnimation("magic_stop.json");
        this.mPlayButton.setContentDescription(getContext().getResources().getString(R$string.acc_video_play_btn));
        if (this.mMute) {
            this.mFunctionButton.setAnimation("magic_mute.json");
            this.mMagicFunctionRl.setContentDescription(getContext().getResources().getString(R$string.acc_open_function_button));
        } else {
            this.mFunctionButton.setAnimation("magic_unmute.json");
            this.mMagicFunctionRl.setContentDescription(getContext().getResources().getString(R$string.acc_clsoe_function_button));
        }
        this.mFunctionButton.playAnimation();
        this.mPlayButton.loop(false);
    }

    public final void showCutDialog() {
        FragmentActivity activity = getActivity();
        if (activity == null || !(activity instanceof BaseFragmentActivity)) {
            return;
        }
        if (this.mExportCutApartDialog == null) {
            this.mExportCutApartDialog = new ExportCutApartFragment();
        }
        this.mExportCutApartDialog.setCallbacks(new ExportCutApartFragment.Callbacks() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewFragment.2
            @Override // com.miui.gallery.magic.widget.ExportCutApartFragment.Callbacks
            public int doExport() {
                return 0;
            }

            @Override // com.miui.gallery.magic.widget.ExportCutApartFragment.Callbacks
            public void onCancelled() {
            }

            @Override // com.miui.gallery.magic.widget.ExportCutApartFragment.Callbacks
            public void onExported(boolean z) {
            }

            {
                VideoPreviewFragment.this = this;
            }

            @Override // com.miui.gallery.magic.widget.ExportCutApartFragment.Callbacks
            public void doCancel() {
                P p = VideoPreviewFragment.this.mPresenter;
                if (p != 0) {
                    ((VideoPreviewPresenter) p).onBackPress();
                }
            }
        });
        this.mExportCutApartDialog.show(activity.getSupportFragmentManager(), "showExportFragment");
    }

    public final void showCutDialogProgress(int i) {
        ExportCutApartFragment exportCutApartFragment = this.mExportCutApartDialog;
        if (exportCutApartFragment != null) {
            exportCutApartFragment.setProgress(i);
        }
    }

    public final void dismissCutDialog() {
        ExportCutApartFragment exportCutApartFragment = this.mExportCutApartDialog;
        if (exportCutApartFragment != null) {
            exportCutApartFragment.dismissAllowingStateLoss();
        }
    }

    public final void initVideoLibs() {
        loadLibrariesOnce();
        SystemUtil.Init(getBaseActivity().getApplicationContext(), 1001);
        XmsContext.getInstance().setContext(getContext());
        XmsContext.getInstance().createTimeline();
    }

    public final void loadLibrariesOnce() {
        if (!mIsLibLoaded) {
            synchronized (VideoEffectsActivity.class) {
                try {
                    if (!mIsLibLoaded) {
                        String libraryDirPath = ResourceUtil.getLibraryDirPath(MagicUtils.getGalleryApp());
                        if (((MagicDependsModule) ModuleRegistry.getModule(MagicDependsModule.class)).is8450()) {
                            System.load(libraryDirPath + "/libsnpe_dsp_domains_v2.so");
                            System.load(libraryDirPath + "/libsnpe_dsp_domains_v3.so");
                            System.load(libraryDirPath + "/libQnnHtpAltPrepV69Stub.so");
                            System.load(libraryDirPath + "/libSNPE.so");
                        } else {
                            System.load(libraryDirPath + "/libsnpe_dsp_domains_v2.so");
                            System.load(libraryDirPath + "/libsnpe_dsp_domains_v3.so");
                            System.load(libraryDirPath + "/libSNPE.so");
                        }
                        System.load(libraryDirPath + "/libffmpeg_effect.so");
                        System.load(libraryDirPath + "/libbuddy_rgmp_project.so");
                        System.load(libraryDirPath + "/libaction_recognition.so");
                        System.load(libraryDirPath + "/libplayer_effect.so");
                        mIsLibLoaded = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public VideoPreviewPresenter getPresenterInstance() {
        return new VideoPreviewPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_video_preview;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        P p = this.mPresenter;
        if (p != 0) {
            boolean z = ((VideoPreviewPresenter) p).isFileCoposing;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        getContract().onPause();
    }

    public final boolean checkOutFileCreatePermission() {
        return FilePermissionUtils.checkFileCreatePermission(getActivity(), ((VideoPreviewPresenter) this.mPresenter).getContract().getOutputFilePath());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.magic_video_play_btn) {
            playButtonStop(!this.mStop);
            getContract().onPlayVideo();
        } else if (id == R$id.magic_function_btn) {
            if (this.mAudioTrackMode) {
                clickMuteButton(this.mMute);
                getContract().onMute(this.mMute);
                return;
            }
            getContract().onBack();
        } else if (id == R$id.magic_a_bar_cancel) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime <= 500) {
                return;
            }
            lastClickTime = currentTimeMillis;
            MagicSampler.getInstance().recordCategory("video_post", "cancel");
            cancelEdit();
        } else {
            int i = R$id.magic_a_bar_save;
            if (id != i) {
                return;
            }
            MagicSampler.getInstance().recordCategory("video_post", "save");
            long currentTimeMillis2 = System.currentTimeMillis();
            if (!this.isVideoSetCompleted) {
                return;
            }
            if (!this.mStop) {
                playButtonStop(true);
            }
            getContract().stopVideoPreview();
            if (!checkOutFileCreatePermission()) {
                return;
            }
            findViewById(i).setEnabled(false);
            getContract().onComposeMP4();
            findViewById(i).setEnabled(true);
            long currentTimeMillis3 = System.currentTimeMillis() - currentTimeMillis2;
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "时间" + currentTimeMillis3);
            MagicSampler.getInstance().recordCategory("video_post", "save_time", hashMap);
        }
    }

    public final void clickMuteButton(boolean z) {
        if (z) {
            this.mFunctionButton.setAnimation("magic_unmute.json");
            this.mMagicFunctionRl.setContentDescription(getContext().getResources().getString(R$string.acc_clsoe_function_button));
        } else {
            this.mFunctionButton.setAnimation("magic_mute.json");
            this.mMagicFunctionRl.setContentDescription(getContext().getResources().getString(R$string.acc_open_function_button));
        }
        this.mMute = !z;
        this.mFunctionButton.playAnimation();
    }

    public final void playButtonStop(boolean z) {
        this.mCover.setVisibility(8);
        if (z && !this.mStop) {
            this.mPlayButton.setAnimation("magic_play.json");
            this.mPlayButton.playAnimation();
            this.mPlayButton.setContentDescription(getContext().getResources().getString(R$string.acc_video_play_btn));
            this.mStop = true;
        } else if (z || !this.mStop) {
        } else {
            this.mPlayButton.setAnimation("magic_stop.json");
            this.mPlayButton.playAnimation();
            this.mPlayButton.setContentDescription(getContext().getResources().getString(R$string.acc_video_pause_btn));
            this.mStop = false;
        }
    }

    @SuppressLint({"WrongConstant"})
    public void showVideoBackButton(boolean z) {
        if (z) {
            this.mVideoBackButton.setVisibility(0);
            this.mMagicFunctionRl.setVisibility(0);
            this.mVideoBackButton.setBackgroundResource(R$drawable.magic_video_back_btn);
            this.mVideoBackButton.clearColorFilter();
            return;
        }
        this.mVideoBackButton.setVisibility(8);
        if (this.mAudioTrackMode) {
            return;
        }
        this.mMagicFunctionRl.setVisibility(8);
    }

    public static boolean isIsEdit() {
        return mIsEdit;
    }

    public static void setIsEdit(boolean z) {
        mIsEdit = z;
    }

    public void cancelEdit() {
        if (!isIsEdit()) {
            getActivity().finish();
        } else {
            ConfirmDialog.showConfirmDialog(getActivity().getSupportFragmentManager(), getStringById(R$string.magic_edit_cancel), getStringById(R$string.magic_edit_dsc), getStringById(R$string.magic_cancel), getStringById(R$string.magic_ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewFragment.3
                @Override // com.miui.gallery.magic.ui.ConfirmDialog.ConfirmDialogInterface
                public void onCancel(DialogFragment dialogFragment) {
                }

                {
                    VideoPreviewFragment.this = this;
                }

                @Override // com.miui.gallery.magic.ui.ConfirmDialog.ConfirmDialogInterface
                public void onConfirm(DialogFragment dialogFragment) {
                    VideoPreviewFragment.this.getActivity().finish();
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        XmsContext.getInstance().setContext(null);
    }
}
