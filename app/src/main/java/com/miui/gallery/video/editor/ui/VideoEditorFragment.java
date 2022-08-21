package com.miui.gallery.video.editor.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import ch.qos.logback.core.joran.action.Action;
import com.android.internal.WindowCompat;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.EditorThreadPoolUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.NexVideoEditor;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.activity.VideoEditorActivity;
import com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback;
import com.miui.gallery.video.editor.model.MenuFragmentData;
import com.miui.gallery.video.editor.ui.AutoTrimProgressDialog;
import com.miui.gallery.video.editor.ui.VideoEditorFragment;
import com.miui.gallery.video.editor.util.TempFileCollector;
import com.miui.gallery.video.editor.util.ToolsUtil;
import com.miui.gallery.video.editor.util.VideoEditorHelper;
import com.miui.gallery.video.editor.widget.DisplayWrapper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoEditorFragment extends AndroidFragment implements VideoEditorActivity.OnBackPressedListener {
    public Activity mActivity;
    public Uri mData;
    public DisplayWrapper mDisplayWrapperView;
    public Disposable mHiddenNotchDisposable;
    public boolean mIsLoadSuccess;
    public View mMenuView;
    public MyStateChangeListener mMyStateChangeListener;
    public ProgressBar mProgressingView;
    public Guideline mTopLine;
    public VideoEditor mVideoEditor;
    public VideoEditorHelper mVideoEditorHelper;
    public int mPlayProgress = 0;
    public int mCurrentTime = 0;
    public ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new AnonymousClass1();
    public IVideoEditorListener$IVideoEditorFragmentCallback mMenuFragmentCallBack = new IVideoEditorListener$IVideoEditorFragmentCallback() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment.5
        {
            VideoEditorFragment.this = this;
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public VideoEditor getVideoEditor() {
            return VideoEditorFragment.this.mVideoEditor;
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public void changeEditMenu(MenuFragmentData menuFragmentData) {
            VideoEditorFragment.this.mVideoEditorHelper.changeEditMenu(menuFragmentData);
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public void showNavEditMenu() {
            VideoEditorFragment.this.mVideoEditorHelper.showNavEditMenu();
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public void onCancel() {
            VideoEditorFragment.this.mVideoEditorHelper.onCancel();
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public void onSave() {
            VideoEditorFragment.this.mVideoEditorHelper.onSave();
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public List<MenuFragmentData> getNavigatorData(int i) {
            return VideoEditorFragment.this.mVideoEditorHelper.getNavigatorData(i);
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public void updatePlayBtnView() {
            if (VideoEditorFragment.this.mVideoEditor.isTouchSeekBar()) {
                VideoEditorFragment.this.mDisplayWrapperView.showPlayBtn(false);
            } else {
                VideoEditorFragment.this.mVideoEditorHelper.updatePlayView();
            }
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public void updateAutoTrimView() {
            VideoEditorFragment.this.mVideoEditorHelper.updateAutoTrimView();
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public boolean isLoadSuccess() {
            return VideoEditorFragment.this.mIsLoadSuccess;
        }

        @Override // com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IVideoEditorFragmentCallback
        public void updateAudioVoiceView(boolean z) {
            VideoEditorFragment.this.mVideoEditorHelper.updateAudioVoiceView(z);
        }
    };

    /* renamed from: $r8$lambda$QMH2-oV2fvDp8b1oRtVQgVYsHwc */
    public static /* synthetic */ void m1762$r8$lambda$QMH2oV2fvDp8b1oRtVQgVYsHwc(VideoEditorFragment videoEditorFragment, View view) {
        videoEditorFragment.lambda$initListener$1(view);
    }

    /* renamed from: $r8$lambda$V1vmibhA_5HqEqFgyFwYYuFc-iQ */
    public static /* synthetic */ void m1763$r8$lambda$V1vmibhA_5HqEqFgyFwYYuFciQ(VideoEditorFragment videoEditorFragment, View view) {
        videoEditorFragment.lambda$initListener$2(view);
    }

    /* renamed from: $r8$lambda$nNI8SSHMZ-cnKYhCH9wlheDWtFk */
    public static /* synthetic */ void m1764$r8$lambda$nNI8SSHMZcnKYhCH9wlheDWtFk(VideoEditorFragment videoEditorFragment, View view) {
        videoEditorFragment.lambda$initListener$0(view);
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mVideoEditor = VideoEditor.create(getActivity().getApplicationContext(), "nex");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_editor, (ViewGroup) null);
        this.mDisplayWrapperView = (DisplayWrapper) inflate.findViewById(R.id.display_wrapper);
        this.mMenuView = inflate.findViewById(R.id.menu_panel);
        this.mTopLine = (Guideline) inflate.findViewById(R.id.display_top_line);
        this.mProgressingView = (ProgressBar) inflate.findViewById(R.id.progressing);
        this.mMyStateChangeListener = new MyStateChangeListener(this, null);
        this.mVideoEditor.setDisplayWrapper(this.mDisplayWrapperView);
        loadData();
        VideoEditorHelper videoEditorHelper = new VideoEditorHelper(viewGroup.getContext(), this);
        this.mVideoEditorHelper = videoEditorHelper;
        videoEditorHelper.showNavEditMenu();
        this.mMenuView.getViewTreeObserver().addOnGlobalLayoutListener(this.mGlobalLayoutListener);
        initListener();
        return inflate;
    }

    /* renamed from: com.miui.gallery.video.editor.ui.VideoEditorFragment$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ViewTreeObserver.OnGlobalLayoutListener {
        /* renamed from: $r8$lambda$d7KkVBpfuvJvZSMTWRLti-wGzmU */
        public static /* synthetic */ void m1765$r8$lambda$d7KkVBpfuvJvZSMTWRLtiwGzmU(AnonymousClass1 anonymousClass1, Boolean bool) {
            anonymousClass1.lambda$onGlobalLayout$1(bool);
        }

        public AnonymousClass1() {
            VideoEditorFragment.this = r1;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            VideoEditorFragment.this.mMenuView.getViewTreeObserver().removeOnGlobalLayoutListener(VideoEditorFragment.this.mGlobalLayoutListener);
            if (!WindowCompat.isNotch(VideoEditorFragment.this.getActivity())) {
                VideoEditorFragment.this.destroyHiddenNotchDisposable();
                Observable observeOn = Observable.create(VideoEditorFragment$1$$ExternalSyntheticLambda0.INSTANCE).subscribeOn(Schedulers.from(EditorThreadPoolUtils.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread());
                VideoEditorFragment.this.mHiddenNotchDisposable = observeOn.subscribe(new Consumer() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment$1$$ExternalSyntheticLambda1
                    @Override // io.reactivex.functions.Consumer
                    public final void accept(Object obj) {
                        VideoEditorFragment.AnonymousClass1.m1765$r8$lambda$d7KkVBpfuvJvZSMTWRLtiwGzmU(VideoEditorFragment.AnonymousClass1.this, (Boolean) obj);
                    }
                });
                return;
            }
            VideoEditorFragment.this.mTopLine.setGuidelineBegin(WindowCompat.getTopNotchHeight(VideoEditorFragment.this.getActivity()) + GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.display_top_line));
        }

        public static /* synthetic */ void lambda$onGlobalLayout$0(ObservableEmitter observableEmitter) throws Exception {
            observableEmitter.onNext(Boolean.valueOf(WindowCompat.isHiddenNotch(GalleryApp.sGetAndroidContext())));
        }

        public /* synthetic */ void lambda$onGlobalLayout$1(Boolean bool) throws Exception {
            if (bool.booleanValue()) {
                VideoEditorFragment.this.mTopLine.setGuidelineBegin(GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.display_top_line_hide_notch));
            }
        }
    }

    public final void initListener() {
        this.mVideoEditor.addStateChangeListener(this.mMyStateChangeListener);
        this.mDisplayWrapperView.setIvPlayListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoEditorFragment.m1764$r8$lambda$nNI8SSHMZcnKYhCH9wlheDWtFk(VideoEditorFragment.this, view);
            }
        });
        this.mDisplayWrapperView.setAutoTrimListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoEditorFragment.m1762$r8$lambda$QMH2oV2fvDp8b1oRtVQgVYsHwc(VideoEditorFragment.this, view);
            }
        });
        this.mVideoEditor.addStateChangeListener(new VideoEditor.StateChangeListener() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment.2
            @Override // com.miui.gallery.video.editor.VideoEditor.StateChangeListener
            public void onStateChanged(int i) {
            }

            {
                VideoEditorFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.StateChangeListener
            public void onTimeChanged(int i) {
                VideoEditorFragment.this.mCurrentTime = i;
                if (VideoEditorFragment.this.mVideoEditor != null) {
                    float projectTotalTime = i / VideoEditorFragment.this.mVideoEditor.getProjectTotalTime();
                    VideoEditorFragment videoEditorFragment = VideoEditorFragment.this;
                    videoEditorFragment.mPlayProgress = (int) (projectTotalTime * videoEditorFragment.mDisplayWrapperView.getViewWidth());
                    VideoEditorFragment.this.mDisplayWrapperView.updatePlayProgress(0, 0, VideoEditorFragment.this.mPlayProgress, 0);
                }
            }
        });
        this.mDisplayWrapperView.setClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VideoEditorFragment.m1763$r8$lambda$V1vmibhA_5HqEqFgyFwYYuFciQ(VideoEditorFragment.this, view);
            }
        });
        this.mDisplayWrapperView.setIProgress(new DisplayWrapper.IProgress() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment.3
            public int tempCurrentTime;

            {
                VideoEditorFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.widget.DisplayWrapper.IProgress
            public void onVideoProgressChanging(int i, float f) {
                this.tempCurrentTime = VideoEditorFragment.this.mCurrentTime;
                if (VideoEditorFragment.this.mVideoEditor == null || f <= 0.0f) {
                    return;
                }
                int projectTotalTime = (int) (VideoEditorFragment.this.mVideoEditor.getProjectTotalTime() * f);
                boolean z = false;
                if (i == DisplayWrapper.LEFT) {
                    if (VideoEditorFragment.this.mCurrentTime <= 0) {
                        return;
                    }
                    int i2 = this.tempCurrentTime;
                    this.tempCurrentTime = i2 > projectTotalTime ? i2 - projectTotalTime : 0;
                } else if (VideoEditorFragment.this.mCurrentTime >= VideoEditorFragment.this.mVideoEditor.getProjectTotalTime()) {
                    return;
                } else {
                    int i3 = this.tempCurrentTime + projectTotalTime;
                    this.tempCurrentTime = i3;
                    if (i3 > VideoEditorFragment.this.mVideoEditor.getProjectTotalTime()) {
                        this.tempCurrentTime = VideoEditorFragment.this.mVideoEditor.getProjectTotalTime();
                    }
                }
                VideoEditorFragment.this.mVideoEditor.seek(this.tempCurrentTime, null);
                float projectTotalTime2 = this.tempCurrentTime / VideoEditorFragment.this.mVideoEditor.getProjectTotalTime();
                VideoEditorFragment videoEditorFragment = VideoEditorFragment.this;
                videoEditorFragment.mPlayProgress = (int) (projectTotalTime2 * videoEditorFragment.mDisplayWrapperView.getViewWidth());
                VideoEditorFragment.this.mDisplayWrapperView.updatePlayProgress(0, 0, VideoEditorFragment.this.mPlayProgress, 0);
                DisplayWrapper displayWrapper = VideoEditorFragment.this.mDisplayWrapperView;
                if (this.tempCurrentTime > 0) {
                    z = true;
                }
                displayWrapper.showPlayProgress(z);
            }

            @Override // com.miui.gallery.video.editor.widget.DisplayWrapper.IProgress
            public void onVideoProgressChanged() {
                VideoEditorFragment.this.mCurrentTime = this.tempCurrentTime;
            }
        });
    }

    public /* synthetic */ void lambda$initListener$0(View view) {
        playVideoAction();
    }

    public /* synthetic */ void lambda$initListener$2(View view) {
        if (this.mVideoEditorHelper.isWaterMarkEditMenu()) {
            if (this.mVideoEditor.getState() == 1) {
                this.mVideoEditor.pause();
            } else if (this.mVideoEditor.getState() != 2 && this.mVideoEditor.getState() != 0) {
            } else {
                this.mVideoEditor.resume();
            }
        } else if (this.mVideoEditor.getState() != 1) {
        } else {
            this.mVideoEditor.pause();
        }
    }

    public final void playVideoAction() {
        if (!this.mIsLoadSuccess) {
            DefaultLogger.e("VideoEditorFragment", "the video has not loaded success.");
            return;
        }
        this.mVideoEditorHelper.onPlayButtonClicked();
        int state = this.mVideoEditor.getState();
        if (state == 0) {
            this.mVideoEditor.play();
        } else if (state != 2) {
        } else {
            this.mVideoEditor.resume();
        }
    }

    /* renamed from: AutoTrimAction */
    public final void lambda$initListener$1(final View view) {
        if (!this.mIsLoadSuccess) {
            DefaultLogger.e("VideoEditorFragment", "the video has not load success.");
            return;
        }
        this.mVideoEditor.saveEditState();
        this.mVideoEditor.resetProject(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment.4
            {
                VideoEditorFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                VideoEditorFragment.this.mDisplayWrapperView.setAutoTrimEnable(false);
                VideoEditorFragment.this.mVideoEditorHelper.setVideoSaving(true);
                VideoEditorFragment.this.mIsLoadSuccess = false;
                AutoTrimProgressDialog.startAutoTrim(VideoEditorFragment.this.mVideoEditor, new AutoTrimProgressDialog.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment.4.1
                    {
                        AnonymousClass4.this = this;
                    }

                    @Override // com.miui.gallery.video.editor.ui.AutoTrimProgressDialog.OnCompletedListener
                    public void onCompleted(boolean z, String str, int i, String str2) {
                        if (VideoEditorFragment.this.mVideoEditor != null) {
                            VideoEditorFragment.this.mDisplayWrapperView.setAutoTrimEnable(true);
                            VideoEditorFragment.this.mVideoEditorHelper.setVideoSaving(false);
                            if (z) {
                                DefaultLogger.d("VideoEditorFragment", "AutoTrimAction is success.");
                                SamplingStatHelper.recordCountEvent("video_editor", "video_editor_autotrim_success");
                                TempFileCollector.add(str);
                                VideoEditorFragment.this.mVideoEditor.load(str, new MyVideoLoadCompletedListener(VideoEditorFragment.this, null));
                                return;
                            }
                            HashMap hashMap = new HashMap();
                            hashMap.put("error", String.valueOf(str2));
                            SamplingStatHelper.recordCountEvent("video_editor", "video_editor_autotrim_failed", hashMap);
                            ToastUtils.makeText(view.getContext(), view.getResources().getString(R.string.video_editor_encode_video_error));
                            DefaultLogger.e("VideoEditorFragment", "AutoTrimAction is fail, and video encode error  msg :" + str2);
                            VideoEditorFragment.this.mVideoEditor.load(VideoEditorFragment.this.mData.getPath(), new MyVideoLoadCompletedListener(VideoEditorFragment.this, null));
                            return;
                        }
                        DefaultLogger.d("VideoEditorFragment", "the video editor is null with auto trim. ");
                    }
                }, VideoEditorFragment.this.getFragmentManager());
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null) {
            videoEditor.startPreview();
        }
        this.mVideoEditorHelper.updatePlayView();
        SamplingStatHelper.recordPageStart(this.mActivity, "video_editor");
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null) {
            videoEditor.pause();
        }
        SamplingStatHelper.recordPageEnd(getActivity(), "video_editor");
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null) {
            videoEditor.startPreview();
        }
    }

    public final void loadData() {
        Uri data = this.mActivity.getIntent().getData();
        this.mData = data;
        if (data != null && data.getPath() != null) {
            this.mVideoEditor.load(this.mData.getPath(), new InitializeListener(this, null));
        } else {
            handleNotSupportVideoFile();
        }
    }

    public Uri getData() {
        return this.mData;
    }

    /* loaded from: classes2.dex */
    public class MyVideoLoadCompletedListener implements VideoEditor.OnCompletedListener {
        public MyVideoLoadCompletedListener() {
            VideoEditorFragment.this = r1;
        }

        public /* synthetic */ MyVideoLoadCompletedListener(VideoEditorFragment videoEditorFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
        public void onCompleted() {
            if (VideoEditorFragment.this.mVideoEditor != null) {
                if (VideoEditorFragment.this.mVideoEditor.haveSavedEditState()) {
                    VideoEditorFragment.this.mVideoEditor.restoreEditState();
                    VideoEditorFragment.this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.ui.VideoEditorFragment.MyVideoLoadCompletedListener.1
                        @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                        public void onCompleted() {
                        }

                        {
                            MyVideoLoadCompletedListener.this = this;
                        }
                    });
                }
                VideoEditorFragment.this.mVideoEditorHelper.onVideoLoadCompleted();
                VideoEditorFragment.this.mIsLoadSuccess = true;
            }
        }
    }

    /* loaded from: classes2.dex */
    public class InitializeListener extends MyVideoLoadCompletedListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InitializeListener() {
            super(r2, null);
            VideoEditorFragment.this = r2;
        }

        public /* synthetic */ InitializeListener(VideoEditorFragment videoEditorFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.video.editor.ui.VideoEditorFragment.MyVideoLoadCompletedListener, com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
        public void onCompleted() {
            if (VideoEditorFragment.this.mVideoEditor instanceof NexVideoEditor) {
                if (!((NexVideoEditor) VideoEditorFragment.this.mVideoEditor).checkIDRSupport()) {
                    SamplingStatHelper.recordCountEvent("video_editor", "idr_warning");
                }
                VideoEditorFragment.this.mVideoEditor.seek(VideoEditorFragment.this.mVideoEditor.getVideoStartTime(), null);
            }
            super.onCompleted();
        }
    }

    public void exit() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null && videoEditor.isInit()) {
            this.mVideoEditor.release();
        }
        TempFileCollector.deleteAllTempFile(GalleryApp.sGetAndroidContext());
        this.mActivity.finish();
    }

    public void onAttachMenuFragment(Fragment fragment) {
        if (fragment instanceof MenuFragment) {
            ((MenuFragment) fragment).setCallBack(this.mMenuFragmentCallBack);
        }
    }

    public final void handleNotSupportVideoFile() {
        ToastUtils.makeText(this.mActivity, (int) R.string.video_editor_not_support_tips);
        if (this.mData != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(Action.FILE_ATTRIBUTE, this.mData.toString());
            SamplingStatHelper.recordCountEvent("video_editor", "video_editor_not_support", hashMap);
        }
        exit();
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
        if (activity instanceof VideoEditorActivity) {
            ((VideoEditorActivity) activity).setOnBackPressedListener(this);
        }
    }

    public VideoEditor getVideoEditor() {
        return this.mVideoEditor;
    }

    public int getEffectMenuContainerId() {
        View view = this.mMenuView;
        if (view != null) {
            return view.getId();
        }
        return 0;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        Activity activity = this.mActivity;
        if (activity instanceof VideoEditorActivity) {
            ((VideoEditorActivity) activity).setOnBackPressedListener(null);
        }
    }

    @Override // com.miui.gallery.video.editor.activity.VideoEditorActivity.OnBackPressedListener
    public boolean onBackPressed() {
        return this.mVideoEditorHelper.onBackPressed();
    }

    public final void destroyHiddenNotchDisposable() {
        Disposable disposable = this.mHiddenNotchDisposable;
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        this.mHiddenNotchDisposable.dispose();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor != null) {
            videoEditor.removeStateChangeListener(this.mMyStateChangeListener);
            this.mVideoEditor = null;
        }
        VideoEditorHelper videoEditorHelper = this.mVideoEditorHelper;
        if (videoEditorHelper != null) {
            videoEditorHelper.onDestroy();
        }
        destroyHiddenNotchDisposable();
        TempFileCollector.deleteAllTempFile(GalleryApp.sGetAndroidContext());
    }

    /* loaded from: classes2.dex */
    public class MyStateChangeListener implements VideoEditor.StateChangeListener {
        public MyStateChangeListener() {
            VideoEditorFragment.this = r1;
        }

        public /* synthetic */ MyStateChangeListener(VideoEditorFragment videoEditorFragment, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.miui.gallery.video.editor.VideoEditor.StateChangeListener
        public void onStateChanged(int i) {
            if (i == -500) {
                ToastUtils.makeText(VideoEditorFragment.this.mActivity, (int) R.string.video_editor_fatal_error);
                VideoEditorFragment.this.exit();
            } else if (i == 200) {
                ToolsUtil.hideView(VideoEditorFragment.this.mProgressingView);
            } else if (i != 500) {
                if (i != 0) {
                    if (i == 1) {
                        ToolsUtil.hideView(VideoEditorFragment.this.mProgressingView);
                    } else if (i != 2) {
                        if (i == 3) {
                            ToolsUtil.hideView(VideoEditorFragment.this.mProgressingView);
                        }
                    }
                }
                ToolsUtil.hideView(VideoEditorFragment.this.mProgressingView);
            } else {
                VideoEditorFragment.this.handleNotSupportVideoFile();
            }
            if (VideoEditorFragment.this.mVideoEditor.isTouchSeekBar()) {
                VideoEditorFragment.this.mDisplayWrapperView.showPlayBtn(false);
            } else {
                VideoEditorFragment.this.mVideoEditorHelper.updatePlayView();
            }
        }

        @Override // com.miui.gallery.video.editor.VideoEditor.StateChangeListener
        public void onTimeChanged(int i) {
            VideoEditorFragment.this.mDisplayWrapperView.showPlayProgress(true);
        }
    }
}
