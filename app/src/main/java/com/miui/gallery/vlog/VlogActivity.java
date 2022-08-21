package com.miui.gallery.vlog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.airbnb.lottie.LottieAnimationView;
import com.android.internal.WindowCompat;
import com.miui.display.DisplayFeatureHelper;
import com.miui.gallery.editor.ui.view.EditorToast;
import com.miui.gallery.editor.utils.LayoutOrientationTracker;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.ProgressBarHandler;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.interfaces.IVlogActivity;
import com.miui.gallery.vlog.base.widget.VlogPlayView;
import com.miui.gallery.vlog.clip.widget.VlogTimeView;
import com.miui.gallery.vlog.home.IVlogViewDelegate;
import com.miui.gallery.vlog.home.VlogBackDialogFragment;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogContract$IVlogView;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.home.VlogPresenter;
import com.miui.gallery.vlog.home.VlogSavingFragment;
import com.miui.gallery.vlog.home.VlogViewLandscapeDelegate;
import com.miui.gallery.vlog.home.VlogViewPortraitDelegate;
import com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback;
import com.miui.gallery.vlog.sdk.callbacks.SeekCallback;
import com.miui.gallery.vlog.sdk.callbacks.TimelineReleaseCallback;
import com.miui.gallery.vlog.sdk.callbacks.TimelineStartedCallback;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.vlog.view.VlogAuxiliaryView;
import com.miui.gallery.vlog.view.VlogMenuTopView;
import com.miui.gallery.vlog.view.VlogMenuView;
import com.miui.gallery.vlog.view.VlogTitleView;
import java.util.List;

/* loaded from: classes2.dex */
public class VlogActivity extends FragmentActivity implements VlogContract$IVlogView, IVlogActivity {
    public VlogTitleView mApplyView;
    public Configuration mConfiguration;
    public String mCurrentTag;
    public View mCustomTitleView;
    public EditorToast mEditorToast;
    public FragmentManager mFragmentManager;
    public String mFutureTag;
    public Handler mHandler;
    public String mLastTag;
    public FrameLayout mMenuContainer;
    public View mMenuPanelLine;
    public LottieAnimationView mProgressBar;
    public ProgressBarHandler mProgressBarHandler;
    public FrameLayout mSeparateMenuContainer;
    public boolean mSuspendInputs;
    public FrameLayout mTitleContainer;
    public FrameLayout mVideoViewContainer;
    public VlogAuxiliaryView mVlogAuxiliaryView;
    public VlogBackDialogFragment mVlogBackDialogFragment;
    public VlogMenuView mVlogMenuView;
    public VlogPlayView mVlogPlayView;
    public VlogPresenter mVlogPresenter;
    public VlogTimeView mVlogTimeView;
    public IVlogViewDelegate mVlogViewDelegate;
    public boolean mIsEffectNavShow = true;
    public final LayoutOrientationTracker mLayoutOrientationTracker = new LayoutOrientationTracker(new LayoutOrientationTracker.OnLayoutOrientationChangeListener() { // from class: com.miui.gallery.vlog.VlogActivity$$ExternalSyntheticLambda0
        @Override // com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
        public final void onLayoutOrientationChange() {
            VlogActivity.m1772$r8$lambda$xgxdM1kqdnDAkxUbvzJj11Z57M(VlogActivity.this);
        }
    });
    public final ViewTreeObserver.OnGlobalLayoutListener mVideoViewContainerLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.VlogActivity.1
        {
            VlogActivity.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            VlogActivity.this.mVideoViewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            VlogActivity.this.addDisplayView();
            VlogActivity.this.mVlogPresenter.loadProject();
        }
    };
    public final ViewTreeObserver.OnGlobalLayoutListener mDisplayViewLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.VlogActivity.2
        {
            VlogActivity.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            VlogActivity.this.mVlogPlayView.getDisplayView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            VlogActivity.this.mVlogPresenter.updateLiveWindowLayout();
        }
    };
    public VlogPlayView.IProgress mIProgress = new VlogPlayView.IProgress() { // from class: com.miui.gallery.vlog.VlogActivity.3
        public long tempCurrentTime;

        {
            VlogActivity.this = this;
        }

        @Override // com.miui.gallery.vlog.base.widget.VlogPlayView.IProgress
        public void onPlayProgressChanging(int i, float f) {
            long j;
            long duration = VlogActivity.this.mVlogPresenter.getIVlogModel().getSdkManager().getDuration();
            if (f > 0.0f) {
                long j2 = ((float) duration) * f;
                if (i == VlogPlayView.LEFT) {
                    long j3 = this.tempCurrentTime;
                    j = 0;
                    if (j3 - j2 > 0) {
                        j = j3 - j2;
                    }
                } else {
                    long j4 = this.tempCurrentTime;
                    if (j4 + j2 < duration) {
                        duration = j4 + j2;
                    }
                    j = duration;
                }
                VlogActivity.this.onMenuFragmentSeekTime(j);
                DefaultLogger.d("VlogActivity_", "onPlayProgressChanging %f , %d , %d", Float.valueOf(f), Integer.valueOf(i), Long.valueOf(j));
            }
        }

        @Override // com.miui.gallery.vlog.base.widget.VlogPlayView.IProgress
        public void onPlayProgressStartChanged() {
            long currentTimeMicro = VlogActivity.this.mVlogPresenter.getIVlogModel().getSdkManager().getCurrentTimeMicro();
            this.tempCurrentTime = currentTimeMicro;
            DefaultLogger.d("VlogActivity_", "onPlayProgressStartChanged %d", Long.valueOf(currentTimeMicro));
        }

        @Override // com.miui.gallery.vlog.base.widget.VlogPlayView.IProgress
        public void onPlayProgressEndChanged() {
            this.tempCurrentTime = 0L;
        }
    };
    public VlogBackDialogFragment.DialogClickListener mBackDialogClickListener = new VlogBackDialogFragment.DialogClickListener() { // from class: com.miui.gallery.vlog.VlogActivity.4
        {
            VlogActivity.this = this;
        }

        @Override // com.miui.gallery.vlog.home.VlogBackDialogFragment.DialogClickListener
        public void exit() {
            VlogActivity.this.finish();
        }
    };
    public SeekCallback mSeekCallback = new SeekCallback() { // from class: com.miui.gallery.vlog.VlogActivity.5
        {
            VlogActivity.this = this;
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.SeekCallback
        public void seekTime(long j) {
            MenuFragment menuFragment = (MenuFragment) VlogActivity.this.mFragmentManager.findFragmentByTag(VlogActivity.this.mCurrentTag);
            if (menuFragment != null) {
                menuFragment.seekTime(j);
            }
        }
    };
    public TimelineStartedCallback mTimelineStartedCallback = new TimelineStartedCallback() { // from class: com.miui.gallery.vlog.VlogActivity.6
        {
            VlogActivity.this = this;
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.TimelineStartedCallback
        public void onTimelineStarted() {
            DefaultLogger.d("VlogActivity_", "onTimelineStarted");
            MenuFragment menuFragment = (MenuFragment) VlogActivity.this.mFragmentManager.findFragmentByTag(VlogActivity.this.mCurrentTag);
            if (menuFragment != null) {
                menuFragment.onTimelineStarted();
            }
        }
    };
    public TimelineReleaseCallback mTimelineReleaseCallback = new TimelineReleaseCallback() { // from class: com.miui.gallery.vlog.VlogActivity.7
        {
            VlogActivity.this = this;
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.TimelineReleaseCallback
        public void onTimelineReleased() {
            DefaultLogger.d("VlogActivity_", "onTimelineReleased");
            VlogActivity.this.finish();
        }
    };
    public PlaybackCallback mPlaybackCallback = new PlaybackCallback() { // from class: com.miui.gallery.vlog.VlogActivity.8
        @Override // com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackTimelinePositionMicro(long j) {
        }

        {
            VlogActivity.this = this;
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackStopped() {
            if (VlogActivity.this.getWindow() != null) {
                DefaultLogger.d("VlogActivity_", "video stop, clear flag_keep_screen_on.");
                VlogActivity.this.getWindow().clearFlags(128);
            }
            MenuFragment menuFragment = (MenuFragment) VlogActivity.this.mFragmentManager.findFragmentByTag(VlogActivity.this.mCurrentTag);
            if (menuFragment != null) {
                menuFragment.onPlaybackStopped();
            }
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackEOF() {
            MenuFragment menuFragment = (MenuFragment) VlogActivity.this.mFragmentManager.findFragmentByTag(VlogActivity.this.mCurrentTag);
            if (menuFragment != null) {
                menuFragment.onPlaybackEOF();
            }
            VlogActivity.this.mVlogPresenter.pause();
        }

        @Override // com.miui.gallery.vlog.sdk.callbacks.PlaybackCallback
        public void onPlaybackTimelinePosition(long j) {
            VlogActivity.this.mVlogPresenter.setPlayViewProgress(j);
            if (VlogActivity.this.getWindow() != null) {
                DefaultLogger.d("VlogActivity_", "video is playing, add flag_keep_screen_on.");
                VlogActivity.this.getWindow().addFlags(128);
            }
            MenuFragment menuFragment = (MenuFragment) VlogActivity.this.mFragmentManager.findFragmentByTag(VlogActivity.this.mCurrentTag);
            if (menuFragment != null) {
                menuFragment.onPlaybackTimelinePosition(j);
            }
        }
    };

    /* renamed from: $r8$lambda$m5xWGLvPkpaSh-r4ilyr2gXywZk */
    public static /* synthetic */ void m1771$r8$lambda$m5xWGLvPkpaShr4ilyr2gXywZk(VlogActivity vlogActivity, String str) {
        vlogActivity.lambda$showToast$1(str);
    }

    /* renamed from: $r8$lambda$xgxdM1kqdnDAkxUbvzJj11-Z57M */
    public static /* synthetic */ void m1772$r8$lambda$xgxdM1kqdnDAkxUbvzJj11Z57M(VlogActivity vlogActivity) {
        vlogActivity.lambda$new$0();
    }

    public boolean useDefaultScreenSceneMode() {
        return true;
    }

    public /* synthetic */ void lambda$new$0() {
        if (!this.mVlogPresenter.getIVlogModel().isSingleVideoEdit()) {
            return;
        }
        DefaultLogger.d("VlogActivity_", "layout orientation change");
        IVlogViewDelegate iVlogViewDelegate = this.mVlogViewDelegate;
        if (iVlogViewDelegate != null) {
            iVlogViewDelegate.release();
        }
        clearAllFragment();
        reloadView();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @SuppressLint({"SourceLockedOrientationActivity"})
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        this.mConfiguration = new Configuration(getResources().getConfiguration());
        DefaultLogger.d("VlogActivity_", "orientation=" + this.mConfiguration.orientation);
        this.mLayoutOrientationTracker.onConfigurationChange(this.mConfiguration);
        WindowCompat.setCutoutModeShortEdges(getWindow());
        getWindow().setFlags(1024, 1024);
        getWindow().setSoftInputMode(48);
        SystemUiUtil.setDrawSystemBarBackground(getWindow());
        VlogConfig.init();
        this.mHandler = new Handler();
        this.mFragmentManager = getSupportFragmentManager();
        VlogPresenter vlogPresenter = new VlogPresenter(this, this, this);
        this.mVlogPresenter = vlogPresenter;
        vlogPresenter.parseData(getIntent());
        if (!OrientationCheckHelper.isSupportOrientationChange()) {
            SystemUiUtil.setRequestedOrientation(1, this);
        }
        initView();
        this.mVlogPresenter.getIVlogModel().getSdkManager().setTimelineStartedCallback(this.mTimelineStartedCallback);
        this.mVlogPresenter.getIVlogModel().getSdkManager().setTimelineReleaseCallback(this.mTimelineReleaseCallback);
        this.mVlogPresenter.verifyData();
        this.mVlogPresenter.addPlayCallback(this.mPlaybackCallback);
        this.mVlogPresenter.setSeekCallback(this.mSeekCallback);
    }

    public final void reloadView() {
        this.mVlogPresenter.pause();
        VlogPlayView vlogPlayView = this.mVlogPlayView;
        if (vlogPlayView != null) {
            vlogPlayView.setIProgress(null);
        }
        VlogPlayView vlogPlayView2 = this.mVlogPlayView;
        if (vlogPlayView2 != null && vlogPlayView2.getParent() != null) {
            ((ViewGroup) this.mVlogPlayView.getParent()).removeView(this.mVlogPlayView);
        }
        IVlogViewDelegate iVlogViewDelegate = this.mVlogViewDelegate;
        if (iVlogViewDelegate != null) {
            iVlogViewDelegate.release();
        }
        this.mApplyView = null;
        ((VlogModel) VlogUtils.getViewModel(this, VlogModel.class)).reloadData(this);
        initView();
        VlogPresenter vlogPresenter = this.mVlogPresenter;
        vlogPresenter.seek(vlogPresenter.getIVlogModel().getSdkManager().getCurrentTimeMicro());
    }

    public final void initView() {
        if (VlogUtils.isLandscape(this)) {
            this.mVlogViewDelegate = new VlogViewLandscapeDelegate(this);
        } else {
            this.mVlogViewDelegate = new VlogViewPortraitDelegate(this);
        }
        this.mVlogViewDelegate.setContentView();
        this.mVlogMenuView = new VlogMenuView(this);
        if (this.mVlogPlayView == null) {
            this.mVlogPlayView = new VlogPlayView(this);
        }
        this.mVlogPlayView.setIProgress(this.mIProgress);
        this.mTitleContainer = (FrameLayout) findViewById(R$id.title_panel_container);
        this.mVlogAuxiliaryView = (VlogAuxiliaryView) findViewById(R$id.auxiliary_view);
        this.mMenuContainer = (FrameLayout) findViewById(R$id.bottomMenuContainer);
        this.mSeparateMenuContainer = (FrameLayout) findViewById(R$id.separateMenuContainer);
        FrameLayout frameLayout = (FrameLayout) findViewById(R$id.videoViewContainer);
        this.mVideoViewContainer = frameLayout;
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.mVideoViewContainerLayoutListener);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) this.mVideoViewContainer.findViewById(com.miui.gallery.editor.R$id.progress);
        this.mProgressBar = lottieAnimationView;
        lottieAnimationView.setZ(1.0f);
        this.mMenuContainer.addView(this.mVlogMenuView);
        this.mVlogMenuView.setDefaultContentView();
        this.mVlogTimeView = new VlogTimeView(this);
        showApplyView();
        this.mEditorToast = new EditorToast(this);
        this.mMenuPanelLine = findViewById(R$id.menu_panel_line);
        findViewById(R$id.panel_right_line);
        this.mVlogPresenter.getIVlogModel().getSdkManager().setLiveWindowValidLayout(this.mVideoViewContainer);
        this.mProgressBarHandler = new ProgressBarHandler(this.mProgressBar);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mLayoutOrientationTracker.onConfigurationChange(configuration);
        if ((this.mConfiguration.updateFrom(configuration) & 1024) != 0) {
            onScreenSizeChanged(configuration.screenLayout & 15);
        }
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z, configuration);
        if (BaseBuildUtil.isFoldableDevice() && z) {
            getWindow().clearFlags(1024);
        } else {
            getWindow().addFlags(1024);
        }
    }

    public final void onScreenSizeChanged(int i) {
        DefaultLogger.d("VlogActivity_", "onScreenSizeChanged: %d", Integer.valueOf(i));
        this.mVlogPresenter.updateScreenRelatedValues();
        this.mVlogPlayView.getDisplayView().getViewTreeObserver().addOnGlobalLayoutListener(this.mDisplayViewLayoutListener);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.mSuspendInputs || super.dispatchTouchEvent(motionEvent);
    }

    public final int getMenuContentViewId() {
        VlogMenuView vlogMenuView = this.mVlogMenuView;
        if (vlogMenuView != null) {
            return vlogMenuView.getContentContainerId();
        }
        return 0;
    }

    public final void clearAllFragment() {
        FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
        List<Fragment> fragments = this.mFragmentManager.getFragments();
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (!(fragment instanceof VlogSavingFragment)) {
                    beginTransaction.remove(fragment);
                }
            }
        }
        beginTransaction.commitNowAllowingStateLoss();
        this.mCurrentTag = null;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void showEffectMenuFragment(int i, String str, String str2) {
        int menuContentViewId = getMenuContentViewId();
        if (menuContentViewId == 0) {
            return;
        }
        this.mFutureTag = str2;
        FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
        if (!TextUtils.isEmpty(this.mCurrentTag)) {
            Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(this.mCurrentTag);
            if (findFragmentByTag != null) {
                beginTransaction.hide(findFragmentByTag);
            }
            this.mLastTag = this.mCurrentTag;
        }
        Fragment findFragmentByTag2 = this.mFragmentManager.findFragmentByTag(str2);
        if (findFragmentByTag2 == null) {
            findFragmentByTag2 = (Fragment) VlogUtils.newInstance(str);
            if (findFragmentByTag2 == null) {
                return;
            }
            beginTransaction.add(menuContentViewId, findFragmentByTag2, str2);
        }
        List<Fragment> fragments = this.mFragmentManager.getFragments();
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (!fragment.isHidden()) {
                    beginTransaction.hide(fragment);
                }
            }
        }
        beginTransaction.show(findFragmentByTag2).commitAllowingStateLoss();
        if (findFragmentByTag2 instanceof MenuFragment) {
            this.mVlogPresenter.getIVlogModel().setCurrentEffectMenuFragment(i, (MenuFragment) findFragmentByTag2);
        }
        this.mCurrentTag = str2;
        IVlogViewDelegate iVlogViewDelegate = this.mVlogViewDelegate;
        if (iVlogViewDelegate == null) {
            return;
        }
        iVlogViewDelegate.showEffectMenuAnimation(str2);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        if (WindowCompat.isNotch(this)) {
            SystemUiUtil.extendToStatusBar(getWindow().getDecorView());
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(this.mCurrentTag);
        if (findFragmentByTag != null) {
            findFragmentByTag.onActivityResult(i, i2, intent);
        }
        StorageSolutionProvider.get().onHandleRequestPermissionResult(this, i, i2, intent);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateClipList() {
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(this.mCurrentTag);
        if (findFragmentByTag instanceof MenuFragment) {
            ((MenuFragment) findFragmentByTag).updateClipList();
        }
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public VlogPlayView getVlogPlayView() {
        return this.mVlogPlayView;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void setPlayViewProgress(long j) {
        this.mVlogPresenter.setPlayViewProgress(j);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void setPlayProgressEnable(boolean z) {
        this.mVlogPlayView.setPlayProgressEnable(z);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void showProgressView() {
        this.mVlogPlayView.showProgressView();
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void hideProgressView() {
        this.mVlogPlayView.hideProgressView();
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void showProgressBar() {
        this.mSuspendInputs = true;
        ProgressBarHandler progressBarHandler = this.mProgressBarHandler;
        if (progressBarHandler != null) {
            progressBarHandler.showDelay(1000);
            DefaultLogger.d("VlogActivity_", "show progress bar");
        }
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void dismissProgressBar() {
        this.mSuspendInputs = false;
        ProgressBarHandler progressBarHandler = this.mProgressBarHandler;
        if (progressBarHandler != null) {
            progressBarHandler.hide();
            DefaultLogger.d("VlogActivity_", "show progress bar");
        }
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public View getLiveWindow() {
        VlogPlayView vlogPlayView = this.mVlogPlayView;
        if (vlogPlayView != null) {
            return vlogPlayView.getDisplayView();
        }
        return null;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void changeVoiceState(boolean z) {
        this.mVlogPresenter.doVideoVoiceBtnClickEvent(z);
    }

    public final void onMenuFragmentSeekTime(long j) {
        MenuFragment menuFragment = (MenuFragment) this.mFragmentManager.findFragmentByTag(this.mCurrentTag);
        if (menuFragment != null) {
            menuFragment.onSeek(j);
        }
    }

    public void addDisplayView() {
        if (this.mVlogPlayView.getParent() != null) {
            ((ViewGroup) this.mVlogPlayView.getParent()).removeView(this.mVlogPlayView);
        }
        this.mVideoViewContainer.addView(this.mVlogPlayView, new FrameLayout.LayoutParams(-1, -1));
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateTextEditorView(View view, boolean z) {
        this.mVlogAuxiliaryView.updateTextEditorView(view, z);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateCaptionClearView(View view, boolean z) {
        this.mVlogAuxiliaryView.updateCaptionClearView(view, z);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateTimeView(boolean z) {
        this.mVlogAuxiliaryView.updateTimeView(this.mVlogTimeView, z);
    }

    public final int getMenuNavViewId() {
        VlogMenuView vlogMenuView = this.mVlogMenuView;
        if (vlogMenuView != null) {
            return vlogMenuView.getNavContainerId();
        }
        return 0;
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IVlogActivity
    public VlogPresenter getVlogPresenter() {
        return this.mVlogPresenter;
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IVlogActivity
    public void onBack() {
        if (this.mVlogBackDialogFragment == null) {
            this.mVlogBackDialogFragment = new VlogBackDialogFragment();
        }
        this.mVlogPresenter.doExit();
        this.mVlogBackDialogFragment.setDialogClickListener(this.mBackDialogClickListener);
        this.mVlogBackDialogFragment.showAllowingStateLoss(this.mFragmentManager, "VlogBackDialogFragment");
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IVlogActivity
    public void onInitFailed() {
        finish();
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IVlogActivity
    public void onSave(boolean z, String str) {
        if (z) {
            this.mVlogPresenter.doSave(str);
        }
    }

    @Override // com.miui.gallery.vlog.base.interfaces.IVlogActivity
    public void onSaved(boolean z, Intent intent) {
        if (!z) {
            setResult(-1);
            startActivity(intent);
        } else {
            setResult(-1, intent);
        }
        super.finish();
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateEffectMenuView(boolean z) {
        int menuNavViewId = getMenuNavViewId();
        if (menuNavViewId == 0) {
            return;
        }
        View findViewById = findViewById(menuNavViewId);
        if (z) {
            VlogUtils.showViews(findViewById);
            this.mVlogMenuView.setTopGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuTopAndContentDimenId(this)));
            this.mVlogMenuView.setContentGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuContentAndBottomDimenId(this)));
        } else {
            VlogUtils.hideViews(findViewById);
            this.mVlogMenuView.setTopGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.vlog_main_menu_top_line));
            this.mVlogMenuView.setContentGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuBottomLineDimenId(this)));
        }
        this.mIsEffectNavShow = z;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateVlogBottomNaviView(boolean z) {
        int menuNavViewId = getMenuNavViewId();
        if (menuNavViewId == 0) {
            return;
        }
        View findViewById = findViewById(menuNavViewId);
        if (z) {
            VlogUtils.showViews(findViewById);
            this.mVlogMenuView.setContentGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuContentAndBottomDimenId(this)));
        } else {
            VlogUtils.hideViews(findViewById);
            this.mVlogMenuView.setContentGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuBottomLineDimenId(this)));
        }
        this.mIsEffectNavShow = z;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void showApplyView() {
        if (this.mApplyView == null) {
            VlogTitleView vlogTitleView = new VlogTitleView(this);
            this.mApplyView = vlogTitleView;
            this.mTitleContainer.addView(vlogTitleView, new FrameLayout.LayoutParams(-1, -1));
        }
        this.mApplyView.setVisibility(0);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void hideApplyView() {
        VlogTitleView vlogTitleView = this.mApplyView;
        if (vlogTitleView != null) {
            vlogTitleView.setVisibility(8);
        }
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void showCustomTitleView(View view) {
        if (view == null) {
            return;
        }
        this.mCustomTitleView = view;
        this.mTitleContainer.addView(view, new FrameLayout.LayoutParams(-1, -1));
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void hideCustomTitleView() {
        this.mTitleContainer.removeView(this.mCustomTitleView);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateDisplayOperationView(View view, boolean z) {
        this.mVlogAuxiliaryView.updateDisplayOperationView(view, z);
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void showToast(final String str) {
        if (VlogUtils.isLandscape(this)) {
            int[] iArr = new int[2];
            this.mVideoViewContainer.getLocationInWindow(iArr);
            this.mEditorToast.show(str, this.mMenuPanelLine, 48, (iArr[0] + (this.mVideoViewContainer.getWidth() / 2)) - (this.mEditorToast.getToastWidth(str) / 2), (-this.mEditorToast.getToastHeight()) - getResources().getDimensionPixelSize(com.miui.gallery.editor.R$dimen.editor_inner_toast_margin_bottom));
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.miui.gallery.vlog.VlogActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                VlogActivity.m1771$r8$lambda$m5xWGLvPkpaShr4ilyr2gXywZk(VlogActivity.this, str);
            }
        });
    }

    public /* synthetic */ void lambda$showToast$1(String str) {
        this.mEditorToast.show(str, this.mMenuPanelLine, 80, (ScreenUtils.getScreenHorizontal(this) - this.mEditorToast.getToastWidth(str)) / 2, (-this.mEditorToast.getToastHeight()) - getResources().getDimensionPixelSize(com.miui.gallery.editor.R$dimen.editor_inner_toast_margin_bottom));
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void setTopView(View view) {
        this.mVlogMenuView.mo1805getTopView().removeAllViews();
        if (view != null) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            this.mVlogMenuView.mo1805getTopView().addView(view);
        }
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public VlogMenuTopView getTopView() {
        return this.mVlogMenuView.mo1805getTopView();
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void setSeparatedView(View view) {
        if (getSeparatedView() == null) {
            return;
        }
        getSeparatedView().removeAllViews();
        if (view == null) {
            return;
        }
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        getSeparatedView().addView(view);
    }

    public ViewGroup getSeparatedView() {
        return this.mSeparateMenuContainer;
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public boolean isClickRightTab() {
        return this.mVlogMenuView.getNavView().isClickRightTab();
    }

    @Override // com.miui.gallery.vlog.home.VlogContract$IVlogView
    public void updateNavViewAfterExitClipSortView() {
        VlogUtils.hideViews(findViewById(getMenuNavViewId()));
        this.mVlogMenuView.setTopGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuTopAndContentDimenId(this)));
        this.mVlogMenuView.setContentGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuBottomLineDimenId(this)));
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        MenuFragment currentEffectMenuFragment = this.mVlogPresenter.getIVlogModel().getCurrentEffectMenuFragment();
        if (currentEffectMenuFragment != null && !currentEffectMenuFragment.onBackPressed()) {
            this.mVlogPresenter.doBackViewClickEvent();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (!useDefaultScreenSceneMode() || !ScreenUtils.isUseScreenSceneMode()) {
            return;
        }
        DisplayFeatureHelper.setScreenSceneClassification(0);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mVlogPresenter.pause();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        VlogPresenter vlogPresenter = this.mVlogPresenter;
        if (vlogPresenter != null) {
            if (vlogPresenter.isSaving()) {
                this.mVlogPresenter.cancelSaveEvent();
            }
            this.mVlogPresenter.removePlayCallback(this.mPlaybackCallback);
            this.mVlogPresenter.destroy();
        }
        VlogPlayView vlogPlayView = this.mVlogPlayView;
        if (vlogPlayView != null) {
            vlogPlayView.setIProgress(null);
        }
        IVlogViewDelegate iVlogViewDelegate = this.mVlogViewDelegate;
        if (iVlogViewDelegate != null) {
            iVlogViewDelegate.release();
        }
        super.onDestroy();
    }
}
